package com.sudarshan.mobileguard.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * BehaviorPatternAnalyzer — Layer 3 of Sudarshan's detection system.
 *
 * Analyzes structural and behavioral patterns in the APK metadata:
 *  - Installer origin verification
 *  - Debug flag detection (production malware is never debug-signed)
 *  - Component analysis (hidden activities, suspicious services)
 *  - Certificate anomalies
 *  - Package name spoofing (fake Google/Facebook apps)
 *  - Version code manipulation
 *
 * No network required. All analysis is done from PackageInfo metadata.
 */
public class BehaviorPatternAnalyzer {

    private static final String TAG = "BehaviorAnalyzer";

    // Known legitimate package prefixes for spoofing detection
    private static final String[] SPOOFED_BRANDS = {
            "google", "facebook", "whatsapp", "instagram", "amazon",
            "microsoft", "apple", "paypal", "netflix", "youtube",
            "twitter", "telegram", "signal", "sbi", "hdfc", "icici",
            "paytm", "phonepe", "gpay", "bhim", "upi"
    };

    private static final String[] LEGITIMATE_PREFIXES = {
            "com.google.", "com.facebook.", "com.whatsapp", "com.instagram",
            "com.amazon.", "com.microsoft.", "com.paypal.", "com.netflix.",
            "com.twitter.", "org.telegram.", "org.thoughtcrime.", "com.paytm",
            "com.phonepe.", "com.google.android.apps.nbu.paisa.user"
    };

    public BehaviorAnalysisResult analyze(Context context, PackageInfo info) {
        BehaviorAnalysisResult result = new BehaviorAnalysisResult();
        PackageManager pm = context.getPackageManager();

        checkInstallerOrigin(context, info.packageName, result);
        checkDebugFlag(info, result);
        checkPackageNameSpoofing(info, pm, result);
        checkSuspiciousComponents(info, result);
        checkVersionAnomaly(info, result);
        checkHiddenApp(info, pm, result);
        checkTestPackage(info, result);

        // Calculate score: sum of all behavior risk scores
        result.behaviorRiskScore = Math.min(100, result.behaviorRiskScore);
        return result;
    }

    // ─────────────────────────────────────────────────
    //  Check 1: Installer Origin
    //  Sideloaded APKs are much higher risk
    // ─────────────────────────────────────────────────
    private void checkInstallerOrigin(Context context, String packageName,
                                       BehaviorAnalysisResult result) {
        try {
            PackageManager pm = context.getPackageManager();
            String installer = pm.getInstallerPackageName(packageName);

            if (installer == null) {
                result.installerSource = "Unknown (Sideloaded / ADB)";
                result.isSideloaded = true;
                result.findings.add("SIDELOADED: App was not installed from any app store");
                result.behaviorRiskScore += 20;
            } else if (installer.equals("com.android.packageinstaller")
                    || installer.equals("com.google.android.packageinstaller")) {
                result.installerSource = "Manual APK install";
                result.isSideloaded = true;
                result.findings.add("Installed via manual APK file (not from Play Store)");
                result.behaviorRiskScore += 15;
            } else if (installer.contains("com.android.vending")) {
                result.installerSource = "Google Play Store";
                result.safeIndicators.add("Installed from Google Play Store");
            } else if (installer.contains("com.amazon.venezia")) {
                result.installerSource = "Amazon Appstore";
                result.safeIndicators.add("Installed from Amazon Appstore");
            } else {
                result.installerSource = installer;
                result.findings.add("Installed from unrecognized source: " + installer);
                result.behaviorRiskScore += 10;
            }
        } catch (Exception e) {
            Log.w(TAG, "Could not determine installer: " + e.getMessage());
            result.installerSource = "Unknown";
        }
    }

    // ─────────────────────────────────────────────────
    //  Check 2: Debug Flag
    //  Real malware is never debug-signed
    // ─────────────────────────────────────────────────
    private void checkDebugFlag(PackageInfo info, BehaviorAnalysisResult result) {
        if ((info.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            result.isDebuggable = true;
            // Debug apps are usually test builds — lower risk score slightly
            result.safeIndicators.add("App is a debug build (likely a developer test build)");
        } else {
            result.safeIndicators.add("Production-signed app (not a debug build)");
        }
    }

    // ─────────────────────────────────────────────────
    //  Check 3: Package Name Spoofing
    //  Fake apps mimicking Google, PayTM, SBI, etc.
    // ─────────────────────────────────────────────────
    private void checkPackageNameSpoofing(PackageInfo info, PackageManager pm,
                                           BehaviorAnalysisResult result) {
        String pkg = info.packageName.toLowerCase();
        String appLabel = "";
        try {
            appLabel = pm.getApplicationLabel(info.applicationInfo).toString().toLowerCase();
        } catch (Exception ignored) {}

        // Check if app name contains a brand name but package doesn't match known legit prefix
        for (String brand : SPOOFED_BRANDS) {
            boolean nameContainsBrand = appLabel.contains(brand) || pkg.contains(brand);
            if (!nameContainsBrand) continue;

            boolean isLegitimate = false;
            for (String legitPrefix : LEGITIMATE_PREFIXES) {
                if (pkg.startsWith(legitPrefix)) {
                    isLegitimate = true;
                    break;
                }
            }

            if (!isLegitimate) {
                result.findings.add("SPOOFING RISK: App name/package contains '" + brand
                        + "' but is NOT the official app");
                result.behaviorRiskScore += 30;
                result.isSpoofedBrand = true;
                break;
            }
        }

        // Check for typosquatting patterns
        if (pkg.contains("g00gle") || pkg.contains("faceb00k") || pkg.contains("whatsaap")
                || pkg.contains("googlee") || pkg.contains("facebok")) {
            result.findings.add("TYPOSQUATTING: Package name is a misspelling of a famous app");
            result.behaviorRiskScore += 35;
        }
    }

    // ─────────────────────────────────────────────────
    //  Check 4: Suspicious Components
    //  Hidden services, unusual number of activities, etc.
    // ─────────────────────────────────────────────────
    private void checkSuspiciousComponents(PackageInfo info, BehaviorAnalysisResult result) {
        // Check for suspicious service names
        if (info.services != null) {
            for (ServiceInfo service : info.services) {
                String name = service.name.toLowerCase();
                if (name.contains("keylog") || name.contains("spy") || name.contains("monitor")
                        || name.contains("stealer") || name.contains("hook") || name.contains("intercept")) {
                    result.findings.add("SUSPICIOUS SERVICE: Component name suggests surveillance: "
                            + service.name);
                    result.behaviorRiskScore += 25;
                }
                // Service exported without permission = attack surface
                if (service.exported && service.permission == null) {
                    result.findings.add("Exported service without permission guard: " + service.name);
                    result.behaviorRiskScore += 5;
                }
            }
        }

        // Check activities for suspicious names
        if (info.activities != null) {
            for (ActivityInfo activity : info.activities) {
                String name = activity.name.toLowerCase();
                if (name.contains("phish") || name.contains("inject") || name.contains("overlay")) {
                    result.findings.add("SUSPICIOUS ACTIVITY COMPONENT: " + activity.name);
                    result.behaviorRiskScore += 20;
                }
            }
        }

        // Check providers
        if (info.providers != null) {
            for (ProviderInfo provider : info.providers) {
                if (provider.exported && provider.readPermission == null
                        && provider.writePermission == null) {
                    result.findings.add("Exposed content provider (data leak risk): "
                            + provider.name);
                    result.behaviorRiskScore += 8;
                }
            }
        }
    }

    // ─────────────────────────────────────────────────
    //  Check 5: Version Anomaly
    // ─────────────────────────────────────────────────
    private void checkVersionAnomaly(PackageInfo info, BehaviorAnalysisResult result) {
        // versionCode 0 or extremely high = suspicious
        long versionCode = info.getLongVersionCode();
        if (versionCode == 0) {
            result.findings.add("Version code is 0 — possibly a test or fake build");
            result.behaviorRiskScore += 5;
        } else if (versionCode > 999_999_999L) {
            result.findings.add("Unusually high version code: " + versionCode);
            result.behaviorRiskScore += 5;
        }

        // Empty version name
        if (info.versionName == null || info.versionName.isEmpty()) {
            result.findings.add("App has no version name — unusual for legitimate apps");
            result.behaviorRiskScore += 8;
        }
    }

    // ─────────────────────────────────────────────────
    //  Check 6: Hidden App (no launcher icon)
    //  Used by malware to hide from user
    // ─────────────────────────────────────────────────
    private void checkHiddenApp(PackageInfo info, PackageManager pm,
                                  BehaviorAnalysisResult result) {
        boolean hasLauncherActivity = false;
        if (info.activities != null) {
            for (ActivityInfo activity : info.activities) {
                if (activity.exported) {
                    hasLauncherActivity = true;
                    break;
                }
            }
        }

        // System apps legitimately have no launcher — skip them
        boolean isSystem = (info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        if (!hasLauncherActivity && !isSystem && info.activities != null && info.activities.length > 0) {
            result.findings.add("App has no visible launcher icon — may be hiding from user");
            result.behaviorRiskScore += 20;
            result.isHiddenApp = true;
        }

        if (isSystem) {
            result.safeIndicators.add("System app — pre-installed by manufacturer");
            result.isSystemApp = true;
        }
    }

    // ─────────────────────────────────────────────────
    //  Check 7: Test Package Detection
    // ─────────────────────────────────────────────────
    private void checkTestPackage(PackageInfo info, BehaviorAnalysisResult result) {
        if ((info.applicationInfo.flags & ApplicationInfo.FLAG_TEST_ONLY) != 0) {
            result.safeIndicators.add("Test-only app (not a production release)");
        }
    }

    // ─────────────────────────────────────────────────
    //  Result Container
    // ─────────────────────────────────────────────────
    public static class BehaviorAnalysisResult {
        public int behaviorRiskScore = 0;
        public String installerSource = "Unknown";
        public boolean isSideloaded = false;
        public boolean isDebuggable = false;
        public boolean isSpoofedBrand = false;
        public boolean isHiddenApp = false;
        public boolean isSystemApp = false;
        public List<String> findings = new ArrayList<>();
        public List<String> safeIndicators = new ArrayList<>();
    }
}
