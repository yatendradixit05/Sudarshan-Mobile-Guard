package com.sudarshan.mobileguard.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.sudarshan.mobileguard.models.ScanResult;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ScanEngine — Master orchestrator for Sudarshan Mobile Guard.
 *
 * Coordinates all 3 detection layers:
 *   Layer 1: HashEngine       → SHA-256 malware database lookup
 *   Layer 2: PermissionEngine → Permission intelligence & context mismatch
 *   Layer 3: BehaviorAnalyzer → Structural & behavioral pattern analysis
 *
 * Final risk score = weighted combination of all 3 layers.
 * Weights: Hash 50% | Permission 30% | Behavior 20%
 */
public class ScanEngine {

    private static final String TAG = "ScanEngine";

    private static final float HASH_WEIGHT       = 0.40f;
    private static final float PERMISSION_WEIGHT = 0.40f;
    private static final float BEHAVIOR_WEIGHT   = 0.20f;

    private final Context context;
    private final HashEngine hashEngine;
    private final PermissionIntelligenceEngine permissionEngine;
    private final BehaviorPatternAnalyzer behaviorAnalyzer;
    private final ExecutorService executor;

    private ScanProgressListener progressListener;

    public interface ScanProgressListener {
        void onProgress(String phase, int percent);
        void onComplete(ScanResult result);
        void onError(String message);
    }

    public ScanEngine(Context context) {
        this.context = context.getApplicationContext();
        this.hashEngine = new HashEngine();
        this.permissionEngine = new PermissionIntelligenceEngine();
        this.behaviorAnalyzer = new BehaviorPatternAnalyzer();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void setProgressListener(ScanProgressListener listener) {
        this.progressListener = listener;
    }

    // ─────────────────────────────────────────────────
    //  Scan by Package Name (installed app)
    // ─────────────────────────────────────────────────
    public void scanPackage(String packageName) {
        executor.submit(() -> {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo info = pm.getPackageInfo(packageName,
                        PackageManager.GET_PERMISSIONS |
                        PackageManager.GET_SERVICES |
                        PackageManager.GET_ACTIVITIES |
                        PackageManager.GET_PROVIDERS |
                        PackageManager.GET_META_DATA);

                ScanResult result = performScan(info, pm);
                notifyComplete(result);

            } catch (PackageManager.NameNotFoundException e) {
                notifyError("Package not found: " + packageName);
            } catch (Exception e) {
                Log.e(TAG, "Scan failed for " + packageName, e);
                notifyError("Scan failed: " + e.getMessage());
            }
        });
    }

    // ─────────────────────────────────────────────────
    //  Scan by APK file path (sideloaded file)
    // ─────────────────────────────────────────────────
    public void scanApkFile(String apkPath) {
        executor.submit(() -> {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                        PackageManager.GET_PERMISSIONS |
                        PackageManager.GET_SERVICES |
                        PackageManager.GET_ACTIVITIES |
                        PackageManager.GET_PROVIDERS);

                if (info == null) {
                    notifyError("Could not read APK file. File may be corrupted.");
                    return;
                }
                info.applicationInfo.sourceDir = apkPath;
                info.applicationInfo.publicSourceDir = apkPath;

                ScanResult result = performScan(info, pm);
                result.apkPath = apkPath;
                result.apkSizeFormatted = formatFileSize(new File(apkPath).length());
                notifyComplete(result);

            } catch (Exception e) {
                Log.e(TAG, "APK scan failed: " + apkPath, e);
                notifyError("Failed to scan APK: " + e.getMessage());
            }
        });
    }

    // ─────────────────────────────────────────────────
    //  Core Scan Logic
    // ─────────────────────────────────────────────────
    private ScanResult performScan(PackageInfo info, PackageManager pm) {
        ScanResult result = new ScanResult();
        result.packageName = info.packageName;

        try {
            result.appName = pm.getApplicationLabel(info.applicationInfo).toString();
        } catch (Exception e) {
            result.appName = info.packageName;
        }

        result.apkPath = info.applicationInfo.sourceDir;
        result.isSystemApp = (info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;

        // ── Phase 1: Hashing (0–30%) ──────────────────
        notifyProgress("Computing SHA-256 hash...", 5);
        String sha256 = hashEngine.computeSHA256(result.apkPath);
        result.sha256Hash = sha256 != null ? sha256 : "ERROR";
        notifyProgress("Checking malware database...", 20);

        HashEngine.HashLookupResult hashResult = hashEngine.lookupHash(sha256);
        result.hashMatchFound = hashResult.isMalicious;
        int hashScore = hashResult.isMalicious ? hashResult.baseSeverityScore : 0;
        notifyProgress("Hash analysis complete", 30);

        // ── Phase 2: Permission Analysis (30–65%) ──────
        notifyProgress("Analyzing permissions...", 35);
        PermissionIntelligenceEngine.PermissionAnalysisResult permResult =
                permissionEngine.analyze(info, pm);
        result.suspiciousPermissions = permResult.permissionRiskScore > 40;
        result.dangerousPermissions = permResult.highRiskPermissions;
        notifyProgress("Checking permission context...", 55);

        // Context mismatches go into suspicious findings
        result.suspiciousFindings = new ArrayList<>(permResult.contextMismatches);
        notifyProgress("Permission analysis complete", 65);

        // ── Phase 3: Behavior Analysis (65–90%) ────────
        notifyProgress("Analyzing app behavior patterns...", 70);
        BehaviorPatternAnalyzer.BehaviorAnalysisResult behaviorResult =
                behaviorAnalyzer.analyze(context, info);
        result.behaviorFlagsFound = behaviorResult.findings.size() > 0;
        result.installerSource = behaviorResult.installerSource;
        result.overlayAttackRisk = behaviorResult.isSpoofedBrand;
        result.suspiciousFindings.addAll(behaviorResult.findings);
        notifyProgress("Behavior analysis complete", 90);

        // ── Phase 4: Score Calculation (90–100%) ───────
        notifyProgress("Calculating final risk score...", 92);
        int finalScore = calculateFinalScore(hashScore,
                permResult.permissionRiskScore,
                behaviorResult.behaviorRiskScore,
                result);
        result.riskScore = finalScore;
        result.riskLevel = ScanResult.levelFromScore(finalScore);

        // Compile safe indicators
        result.safeIndicators = new ArrayList<>();
        result.safeIndicators.addAll(permResult.safeIndicators);
        result.safeIndicators.addAll(behaviorResult.safeIndicators);
        if (!result.hashMatchFound && sha256 != null) {
            result.safeIndicators.add("Not found in known malware hash database");
        }

        // Generate verdict
        result.verdict = generateVerdict(result, hashResult);
        notifyProgress("Scan complete", 100);

        return result;
    }

    // ─────────────────────────────────────────────────
    //  Weighted Score Calculation
    // ─────────────────────────────────────────────────
    private int calculateFinalScore(int hashScore, int permScore, int behaviorScore,
                                    ScanResult result) {
        if (result.hashMatchFound) {
            int base = (int) (hashScore * HASH_WEIGHT +
                    permScore * PERMISSION_WEIGHT +
                    behaviorScore * BEHAVIOR_WEIGHT);
            return Math.max(base, 80);
        }

        int score = (int) (hashScore * HASH_WEIGHT +
                permScore * PERMISSION_WEIGHT +
                behaviorScore * BEHAVIOR_WEIGHT);

        // Sideloaded APK = minimum 30 score (never fully "SAFE")
        if (result.installerSource != null &&
                (result.installerSource.contains("Sideload") ||
                        result.installerSource.contains("Unknown") ||
                        result.installerSource.contains("Manual"))) {
            score = Math.max(score, 30);
        }

        return Math.min(score, 100);
    }

    // ─────────────────────────────────────────────────
    //  Verdict Generation
    // ─────────────────────────────────────────────────
    private String generateVerdict(ScanResult result, HashEngine.HashLookupResult hashResult) {
        if (result.hashMatchFound) {
            return "MALWARE DETECTED: " + hashResult.threatName;
        }

        switch (result.riskLevel) {
            case SAFE:
                return "App appears clean. No threats detected.";
            case LOW:
                return "Low risk. Minor permission concerns.";
            case MEDIUM:
                return "Moderate risk. Review suspicious permissions.";
            case HIGH:
                return "High risk. Multiple threat indicators found.";
            case CRITICAL:
                return "CRITICAL THREAT. Do NOT use this app.";
            default:
                return "Analysis complete.";
        }
    }

    // ─────────────────────────────────────────────────
    //  Utility
    // ─────────────────────────────────────────────────
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        DecimalFormat df = new DecimalFormat("#.#");
        if (bytes < 1024 * 1024) return df.format(bytes / 1024.0) + " KB";
        if (bytes < 1024 * 1024 * 1024) return df.format(bytes / (1024.0 * 1024)) + " MB";
        return df.format(bytes / (1024.0 * 1024 * 1024)) + " GB";
    }

    private void notifyProgress(String phase, int percent) {
        if (progressListener != null) {
            progressListener.onProgress(phase, percent);
        }
        Log.d(TAG, "[" + percent + "%] " + phase);
    }

    private void notifyComplete(ScanResult result) {
        if (progressListener != null) {
            progressListener.onComplete(result);
        }
    }

    private void notifyError(String message) {
        if (progressListener != null) {
            progressListener.onError(message);
        }
        Log.e(TAG, "Scan error: " + message);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
