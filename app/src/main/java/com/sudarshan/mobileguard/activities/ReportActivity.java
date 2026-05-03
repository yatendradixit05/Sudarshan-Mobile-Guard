package com.sudarshan.mobileguard.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.sudarshan.mobileguard.R;
import com.sudarshan.mobileguard.database.AppDatabase;
import com.sudarshan.mobileguard.models.ScanResult;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class ReportActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        db = AppDatabase.getInstance(this);
        int scanId = getIntent().getIntExtra("scan_id", -1);

        if (scanId == -1) { finish(); return; }

        Executors.newSingleThreadExecutor().execute(() -> {
            ScanResult result = db.scanResultDao().getScanById(scanId);
            if (result != null) {
                runOnUiThread(() -> populateReport(result));
            } else {
                runOnUiThread(this::finish);
            }
        });
    }

    private void populateReport(ScanResult result) {
        // ── Header / Verdict Banner ──
        View verdictBanner = findViewById(R.id.verdict_banner);
        TextView tvVerdict = findViewById(R.id.tv_verdict);
        TextView tvRiskScore = findViewById(R.id.tv_risk_score_report);
        CircularProgressIndicator riskGauge = findViewById(R.id.report_risk_gauge);
        TextView tvAppName = findViewById(R.id.tv_app_name_report);
        TextView tvPackageName = findViewById(R.id.tv_package_name);
        TextView tvScanTime = findViewById(R.id.tv_scan_time);
        TextView tvHash = findViewById(R.id.tv_sha256);
        TextView tvInstaller = findViewById(R.id.tv_installer_source);
        TextView tvApkSize = findViewById(R.id.tv_apk_size);

        // ── Risk Display ──
        int color = result.getRiskColor();
        tvVerdict.setText(result.verdict);
        tvRiskScore.setText(result.riskScore + "/100");
        riskGauge.setProgress(result.riskScore);
        riskGauge.setIndicatorColor(color);
        tvVerdict.setTextColor(color);
        verdictBanner.setBackgroundColor(applyAlpha(color, 25));

        // ── App Info ──
        tvAppName.setText(result.appName);
        tvPackageName.setText(result.packageName);
        tvApkSize.setText(result.apkSizeFormatted != null ? result.apkSizeFormatted : "N/A");
        tvInstaller.setText(result.installerSource != null ? result.installerSource : "Unknown");

        // ── Timestamps ──
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault());
        tvScanTime.setText(sdf.format(new Date(result.scanTimestamp)));

        // ── Hash ──
        tvHash.setText(result.sha256Hash != null ? result.sha256Hash : "N/A");

        // ── Hash Match Warning ──
        View hashMatchCard = findViewById(R.id.card_hash_match);
        if (result.hashMatchFound) {
            hashMatchCard.setVisibility(View.VISIBLE);
            TextView tvHashThreat = findViewById(R.id.tv_hash_threat_name);
            // Threat name is in the verdict
            tvHashThreat.setText(result.verdict);
        } else {
            hashMatchCard.setVisibility(View.GONE);
        }

        // ── Dangerous Permissions ──
        ChipGroup cgDangerPerms = findViewById(R.id.chip_group_dangerous_perms);
        View cardDangerPerms = findViewById(R.id.card_dangerous_permissions);
        populateChips(cgDangerPerms, result.dangerousPermissions, 0xFFDD2C00);
        cardDangerPerms.setVisibility(
                result.dangerousPermissions != null && !result.dangerousPermissions.isEmpty()
                        ? View.VISIBLE : View.GONE);

        // ── Suspicious Findings ──
        LinearLayout llFindings = findViewById(R.id.ll_suspicious_findings);
        View cardFindings = findViewById(R.id.card_suspicious_findings);
        populateFindingRows(llFindings, result.suspiciousFindings, "⚠️", 0xFFFF6F00);
        cardFindings.setVisibility(
                result.suspiciousFindings != null && !result.suspiciousFindings.isEmpty()
                        ? View.VISIBLE : View.GONE);

        // ── Safe Indicators ──
        LinearLayout llSafe = findViewById(R.id.ll_safe_indicators);
        View cardSafe = findViewById(R.id.card_safe_indicators);
        populateFindingRows(llSafe, result.safeIndicators, "✅", 0xFF00C853);
        cardSafe.setVisibility(
                result.safeIndicators != null && !result.safeIndicators.isEmpty()
                        ? View.VISIBLE : View.GONE);

        // ── Risk Badge ──
        TextView tvRiskBadge = findViewById(R.id.tv_risk_badge);
        tvRiskBadge.setText(result.riskLevel.name().replace("_", " "));
        tvRiskBadge.setBackgroundColor(color);
        tvRiskBadge.setTextColor(Color.WHITE);

        // ── Back button ──
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void populateChips(ChipGroup group, List<String> items, int colorInt) {
        if (group == null || items == null) return;
        group.removeAllViews();
        for (String item : items) {
            Chip chip = new Chip(this);
            // Shorten permission text for display
            String text = item.length() > 60 ? item.substring(0, 57) + "..." : item;
            chip.setText(text);
            chip.setChipBackgroundColor(
                    android.content.res.ColorStateList.valueOf(applyAlpha(colorInt, 30)));
            chip.setTextColor(colorInt);
            chip.setClickable(false);
            group.addView(chip);
        }
    }

    private void populateFindingRows(LinearLayout container, List<String> items,
                                      String icon, int textColor) {
        if (container == null || items == null) return;
        container.removeAllViews();
        for (String item : items) {
            TextView tv = new TextView(this);
            tv.setText(icon + " " + item);
            tv.setTextColor(textColor);
            tv.setTextSize(13f);
            tv.setPadding(0, 8, 0, 8);
            container.addView(tv);
        }
    }

    private int applyAlpha(int color, int alphaPercent) {
        int alpha = (int) (alphaPercent * 2.55f);
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
}
