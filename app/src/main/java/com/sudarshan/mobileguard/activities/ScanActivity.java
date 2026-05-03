package com.sudarshan.mobileguard.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.sudarshan.mobileguard.R;
import com.sudarshan.mobileguard.database.AppDatabase;
import com.sudarshan.mobileguard.engine.ScanEngine;
import com.sudarshan.mobileguard.models.ScanResult;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ScanActivity extends AppCompatActivity {

    public static final String MODE_FULL_DEVICE = "full_device";
    public static final String MODE_SINGLE_PACKAGE = "single_package";
    public static final String MODE_APK_FILE = "apk_file";

    private LinearProgressIndicator progressBar;
    private TextView tvCurrentApp, tvPhase, tvProgress, tvSummary;
    private View btnViewResults;
    private AppDatabase db;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private int lastScanId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        db = AppDatabase.getInstance(this);
        initViews();

        String mode = getIntent().getStringExtra("scan_mode");
        if (mode == null) mode = MODE_SINGLE_PACKAGE;

        switch (mode) {
            case MODE_FULL_DEVICE:
                startFullDeviceScan();
                break;
            case MODE_APK_FILE:
                String apkPath = getIntent().getStringExtra("apk_path");
                startApkScan(apkPath);
                break;
            default:
                String pkgName = getIntent().getStringExtra("package_name");
                startPackageScan(pkgName);
                break;
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.scan_progress_bar);
        tvCurrentApp = findViewById(R.id.tv_current_app);
        tvPhase = findViewById(R.id.tv_phase);
        tvProgress = findViewById(R.id.tv_progress_percent);
        tvSummary = findViewById(R.id.tv_scan_summary);
        btnViewResults = findViewById(R.id.btn_view_results);
        btnViewResults.setVisibility(View.GONE);
    }

    // ─────────────────────────────────────────────────
    //  Full Device Scan
    // ─────────────────────────────────────────────────
    private void startFullDeviceScan() {
        tvCurrentApp.setText("Collecting installed apps...");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            PackageManager pm = getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(
                    PackageManager.GET_PERMISSIONS | PackageManager.GET_META_DATA);

            // Filter: only user-installed apps (skip system apps for speed)
            List<PackageInfo> userApps = new ArrayList<>();
            for (PackageInfo pkg : packages) {
                boolean isSystem = (pkg.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
                if (!isSystem || (pkg.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    if (!pkg.packageName.equals(getPackageName())) {
                        userApps.add(pkg);
                    }
                }
            }

            int total = userApps.size();
            AtomicInteger scanned = new AtomicInteger(0);
            AtomicInteger threats = new AtomicInteger(0);
            int lastScanIdRef[] = {-1};

            updateUI("Starting scan of " + total + " apps...", "", 0);

            for (PackageInfo pkg : userApps) {
                if (isFinishing()) break;

                String appName;
                try {
                    appName = pm.getApplicationLabel(pkg.applicationInfo).toString();
                } catch (Exception e) {
                    appName = pkg.packageName;
                }

                final String finalAppName = appName;
                final int current = scanned.incrementAndGet();
                int overallPercent = (current * 100) / total;

                updateUI("Scanning: " + finalAppName,
                         current + " of " + total + " apps",
                         overallPercent);

                // Scan this package synchronously on this background thread
                ScanEngine engine = new ScanEngine(this);
                final boolean[] done = {false};
                final Object lock = new Object();

                engine.setProgressListener(new ScanEngine.ScanProgressListener() {
                    @Override
                    public void onProgress(String phase, int percent) {}

                    @Override
                    public void onComplete(ScanResult result) {
                        new Thread(() -> {
                            long id = db.scanResultDao().insertScan(result);
                            result.id = (int) id;
                            lastScanIdRef[0] = result.id;
                            if (result.riskScore >= 51) threats.incrementAndGet();
                            engine.shutdown();
                            synchronized (lock) { done[0] = true; lock.notifyAll(); }
                        }).start();
                    }

                    @Override
                    public void onError(String message) {
                        engine.shutdown();
                        synchronized (lock) { done[0] = true; lock.notifyAll(); }
                    }
                });

                engine.scanPackage(pkg.packageName);

                // Wait for this scan to finish
                synchronized (lock) {
                    while (!done[0]) {
                        try { lock.wait(5000); } catch (InterruptedException e) { break; }
                    }
                }
            }

            // Done
            final int finalThreats = threats.get();
            final int finalTotal = total;
            lastScanId = lastScanIdRef[0];

            mainHandler.post(() -> {
                progressBar.setProgress(100);
                tvProgress.setText("100%");
                tvCurrentApp.setText("Scan Complete");

                if (finalThreats == 0) {
                    tvSummary.setText("✅ All " + finalTotal + " apps scanned. No threats found!");
                    tvSummary.setTextColor(0xFF00C853);
                } else {
                    tvSummary.setText("⚠️ " + finalThreats + " threat(s) found in " + finalTotal + " apps. Tap below to review.");
                    tvSummary.setTextColor(0xFFDD2C00);
                }
                tvSummary.setVisibility(View.VISIBLE);

                btnViewResults.setVisibility(View.VISIBLE);
                btnViewResults.setOnClickListener(v -> {
                    startActivity(new Intent(ScanActivity.this, HistoryActivity.class));
                    finish();
                });
            });
        });
    }

    // ─────────────────────────────────────────────────
    //  Single Package Scan
    // ─────────────────────────────────────────────────
    private void startPackageScan(String packageName) {
        ScanEngine engine = new ScanEngine(this);
        engine.setProgressListener(createSingleScanListener(engine));
        engine.scanPackage(packageName);
    }

    private void startApkScan(String apkPath) {
        ScanEngine engine = new ScanEngine(this);
        engine.setProgressListener(createSingleScanListener(engine));
        engine.scanApkFile(apkPath);
    }

    private ScanEngine.ScanProgressListener createSingleScanListener(ScanEngine engine) {
        return new ScanEngine.ScanProgressListener() {
            @Override
            public void onProgress(String phase, int percent) {
                updateUI("Analyzing...", phase, percent);
            }

            @Override
            public void onComplete(ScanResult result) {
                new Thread(() -> {
                    long id = db.scanResultDao().insertScan(result);
                    result.id = (int) id;
                    lastScanId = result.id;
                    engine.shutdown();

                    mainHandler.post(() -> {
                        progressBar.setProgress(100);
                        tvProgress.setText("100%");
                        tvCurrentApp.setText("Scan Complete: " + result.appName);
                        tvSummary.setText(result.verdict);
                        tvSummary.setTextColor(result.getRiskColor());
                        tvSummary.setVisibility(View.VISIBLE);

                        btnViewResults.setVisibility(View.VISIBLE);
                        btnViewResults.setOnClickListener(v -> {
                            Intent intent = new Intent(ScanActivity.this, ReportActivity.class);
                            intent.putExtra("scan_id", result.id);
                            startActivity(intent);
                            finish();
                        });
                    });
                }).start();
            }

            @Override
            public void onError(String message) {
                engine.shutdown();
                mainHandler.post(() -> {
                    tvCurrentApp.setText("Scan failed");
                    tvPhase.setText(message);
                });
            }
        };
    }

    private void updateUI(String appName, String phase, int percent) {
        mainHandler.post(() -> {
            tvCurrentApp.setText(appName);
            tvPhase.setText(phase);
            tvProgress.setText(percent + "%");
            progressBar.setProgress(percent);
        });
    }
}
