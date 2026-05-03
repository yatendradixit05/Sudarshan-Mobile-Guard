package com.sudarshan.mobileguard.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.sudarshan.mobileguard.R;
import com.sudarshan.mobileguard.adapters.ScanResultAdapter;
import com.sudarshan.mobileguard.database.AppDatabase;
import com.sudarshan.mobileguard.models.ScanResult;
import com.sudarshan.mobileguard.services.InstallMonitorService;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CircularProgressIndicator riskGauge;
    private TextView tvRiskScore, tvRiskLabel, tvTotalScans, tvThreatsFound;
    private MaterialButton btnScanAll, btnScanApk, btnViewHistory;
    private RecyclerView rvRecentScans;
    private ScanResultAdapter adapter;
    private AppDatabase db;

    // File picker for APK selection
    private final ActivityResultLauncher<String> apkPicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    String path = getRealPathFromUri(uri);
                    if (path != null) {
                        startScanActivity(null, path);
                    } else {
                        Toast.makeText(this, "Could not access selected file", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        initViews();
        setupRecyclerView();
        observeData();
        startGuardService();
        requestPermissions();
    }

    private void initViews() {
        riskGauge = findViewById(R.id.risk_gauge);
        tvRiskScore = findViewById(R.id.tv_risk_score);
        tvRiskLabel = findViewById(R.id.tv_risk_label);
        tvTotalScans = findViewById(R.id.tv_total_scans);
        tvThreatsFound = findViewById(R.id.tv_threats_found);
        btnScanAll = findViewById(R.id.btn_scan_all);
        btnScanApk = findViewById(R.id.btn_scan_apk);
        btnViewHistory = findViewById(R.id.btn_view_history);
        rvRecentScans = findViewById(R.id.rv_recent_scans);

        btnScanAll.setOnClickListener(v -> startFullDeviceScan());
        btnScanApk.setOnClickListener(v -> apkPicker.launch("application/vnd.android.package-archive"));
        btnViewHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
    }

    private void setupRecyclerView() {
        adapter = new ScanResultAdapter(result -> {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("scan_id", result.id);
            startActivity(intent);
        });
        rvRecentScans.setLayoutManager(new LinearLayoutManager(this));
        rvRecentScans.setAdapter(adapter);
    }

    private void observeData() {
        // Observe recent scans (last 5)
        db.scanResultDao().getRecentScans(5).observe(this, scans -> {
            adapter.submitList(scans);
            updateRiskDashboard(scans);
        });
    }

    private void updateRiskDashboard(List<ScanResult> scans) {
        // Calculate device-wide risk score = max of recent scan scores
        int maxRisk = 0;
        int threatCount = 0;
        for (ScanResult scan : scans) {
            if (scan.riskScore > maxRisk) maxRisk = scan.riskScore;
            if (scan.riskScore >= 51) threatCount++;
        }

        // Animate gauge
        riskGauge.setProgress(maxRisk, true);
        tvRiskScore.setText(String.valueOf(maxRisk));
        tvThreatsFound.setText(String.valueOf(threatCount));
        tvTotalScans.setText(String.valueOf(scans.size()));

        // Update label and color
        ScanResult.RiskLevel level = ScanResult.levelFromScore(maxRisk);
        String label;
        int color;
        switch (level) {
            case SAFE:     label = "DEVICE SAFE";     color = 0xFF00C853; break;
            case LOW:      label = "LOW RISK";        color = 0xFF64DD17; break;
            case MEDIUM:   label = "MODERATE RISK";   color = 0xFFFF6F00; break;
            case HIGH:     label = "HIGH RISK";       color = 0xFFDD2C00; break;
            case CRITICAL: label = "CRITICAL THREAT"; color = 0xFFB71C1C; break;
            default:       label = "SCAN YOUR DEVICE"; color = 0xFF64B5F6;
        }
        tvRiskLabel.setText(label);
        tvRiskLabel.setTextColor(color);
        riskGauge.setIndicatorColor(color);
    }

    // ─────────────────────────────────────────────────
    //  Full Device Scan — scans all installed user apps
    // ─────────────────────────────────────────────────
    private void startFullDeviceScan() {
        new AlertDialog.Builder(this)
                .setTitle("Full Device Scan")
                .setMessage("This will scan all installed apps. It may take 1–3 minutes depending on the number of apps. Continue?")
                .setPositiveButton("Start Scan", (d, w) -> {
                    Intent intent = new Intent(this, ScanActivity.class);
                    intent.putExtra("scan_mode", ScanActivity.MODE_FULL_DEVICE);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void startScanActivity(String packageName, String apkPath) {
        Intent intent = new Intent(this, ScanActivity.class);
        if (packageName != null) {
            intent.putExtra("package_name", packageName);
            intent.putExtra("scan_mode", ScanActivity.MODE_SINGLE_PACKAGE);
        } else {
            intent.putExtra("apk_path", apkPath);
            intent.putExtra("scan_mode", ScanActivity.MODE_APK_FILE);
        }
        startActivity(intent);
    }

    private void startGuardService() {
        Intent serviceIntent = new Intent(this, InstallMonitorService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }
    }

    private String getRealPathFromUri(Uri uri) {
        try {
            // Use content resolver to get path
            String[] proj = {android.provider.MediaStore.Files.FileColumns.DATA};
            android.database.Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(proj[0]);
                String path = idx >= 0 ? cursor.getString(idx) : null;
                cursor.close();
                if (path != null) return path;
            }
            // Fallback: copy to cache
            return copyUriToCache(uri);
        } catch (Exception e) {
            return copyUriToCache(uri);
        }
    }

    private String copyUriToCache(Uri uri) {
        try {
            java.io.InputStream is = getContentResolver().openInputStream(uri);
            if (is == null) return null;
            java.io.File cacheFile = new java.io.File(getCacheDir(), "temp_scan.apk");
            java.io.FileOutputStream fos = new java.io.FileOutputStream(cacheFile);
            byte[] buf = new byte[8192];
            int len;
            while ((len = is.read(buf)) > 0) fos.write(buf, 0, len);
            fos.close();
            is.close();
            return cacheFile.getAbsolutePath();
        } catch (Exception e) {
            Log.e(TAG, "Failed to copy URI to cache", e);
            return null;
        }
    }
}
