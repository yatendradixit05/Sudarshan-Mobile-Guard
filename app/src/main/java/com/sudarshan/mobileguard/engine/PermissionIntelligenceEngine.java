package com.sudarshan.mobileguard.engine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.sudarshan.mobileguard.models.PermissionProfile;
import java.util.*;

/**
 * PermissionIntelligenceEngine — Layer 2 of Sudarshan's detection system.
 *
 * Analyzes permissions using two techniques:
 *   1. Danger Score: Each dangerous permission has a base risk score.
 *   2. Context Mismatch Detection: Flags illogical permission combos
 *      (e.g., a calculator asking for GPS, a flashlight reading SMS).
 *
 * This is more powerful than simple blacklisting because it catches
 * novel malware that hasn't been seen before.
 */
public class PermissionIntelligenceEngine {

    private static final String TAG = "PermissionEngine";

    private final Map<String, PermissionProfile> profileMap;
    private final List<ContextMismatchRule> mismatchRules;

    public PermissionIntelligenceEngine() {
        profileMap = buildPermissionProfiles();
        mismatchRules = buildMismatchRules();
    }

    /**
     * Analyze an installed package's permissions.
     */
    public PermissionAnalysisResult analyzePackage(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(packageName,
                    PackageManager.GET_PERMISSIONS | PackageManager.GET_META_DATA);
            return analyze(info, pm);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Package not found: " + packageName);
            return PermissionAnalysisResult.empty();
        }
    }

    /**
     * Analyze from PackageInfo (works for both installed and APK file scan).
     */
    public PermissionAnalysisResult analyze(PackageInfo info, PackageManager pm) {
        PermissionAnalysisResult result = new PermissionAnalysisResult();

        String[] requestedPermissions = info.requestedPermissions;
        if (requestedPermissions == null || requestedPermissions.length == 0) {
            result.totalPermissions = 0;
            result.permissionRiskScore = 0;
            result.safeIndicators.add("App requests no permissions — minimal attack surface");
            return result;
        }

        result.totalPermissions = requestedPermissions.length;
        Set<PermissionProfile.Category> categoriesFound = new HashSet<>();
        int cumulativeRisk = 0;

        for (String perm : requestedPermissions) {
            PermissionProfile profile = profileMap.get(perm);

            if (profile != null) {
                result.profiledPermissions.add(profile);
                categoriesFound.add(profile.category);
                cumulativeRisk += profile.dangerScore;

                if (profile.dangerScore >= 7) {
                    result.highRiskPermissions.add(profile.humanReadable
                            + " [Risk: " + profile.dangerScore + "/10] — " + profile.abuseScenario);
                } else if (profile.dangerScore >= 4) {
                    result.mediumRiskPermissions.add(profile.humanReadable);
                }
            }
        }

        // Detect context mismatches
        String appLabel = "";
        try {
            appLabel = pm.getApplicationLabel(info.applicationInfo).toString().toLowerCase();
        } catch (Exception ignored) {}

        for (ContextMismatchRule rule : mismatchRules) {
            if (rule.matches(appLabel, categoriesFound, requestedPermissions)) {
                result.contextMismatches.add(rule.description);
                cumulativeRisk += rule.riskBonus;
            }
        }

        // Normalize score: 0-100
        // Max theoretical cumulative = sum of all danger scores = ~150
        result.permissionRiskScore = Math.min(100, (cumulativeRisk * 100) / 120);

        // Add safe indicators
        if (!categoriesFound.contains(PermissionProfile.Category.LOCATION)) {
            result.safeIndicators.add("No location access requested");
        }
        if (!categoriesFound.contains(PermissionProfile.Category.COMMUNICATION)) {
            result.safeIndicators.add("No access to calls or SMS");
        }
        if (!categoriesFound.contains(PermissionProfile.Category.SYSTEM_ADMIN)) {
            result.safeIndicators.add("No device administrator privileges");
        }

        return result;
    }

    // ─────────────────────────────────────────────────
    //  Permission Profiles Database
    // ─────────────────────────────────────────────────
    private Map<String, PermissionProfile> buildPermissionProfiles() {
        Map<String, PermissionProfile> map = new LinkedHashMap<>();

        // COMMUNICATION — High risk (used in stalkerware, RATs)
        add(map, "android.permission.READ_SMS", PermissionProfile.Category.COMMUNICATION, 9,
                "Read your SMS messages", "Banking OTPs and 2FA codes can be intercepted");
        add(map, "android.permission.SEND_SMS", PermissionProfile.Category.COMMUNICATION, 8,
                "Send SMS messages", "Premium-rate SMS fraud (Joker malware technique)");
        add(map, "android.permission.RECEIVE_SMS", PermissionProfile.Category.COMMUNICATION, 9,
                "Intercept incoming SMS", "OTP theft for account takeover");
        add(map, "android.permission.READ_CALL_LOG", PermissionProfile.Category.COMMUNICATION, 7,
                "Read call history", "Surveillance and social mapping");
        add(map, "android.permission.PROCESS_OUTGOING_CALLS", PermissionProfile.Category.COMMUNICATION, 8,
                "Intercept outgoing calls", "Call redirection to premium numbers");
        add(map, "android.permission.READ_CONTACTS", PermissionProfile.Category.COMMUNICATION, 6,
                "Read all contacts", "Spam propagation and social engineering");

        // LOCATION — Moderate to High
        add(map, "android.permission.ACCESS_FINE_LOCATION", PermissionProfile.Category.LOCATION, 7,
                "Precise GPS location", "Real-time tracking of user's movements");
        add(map, "android.permission.ACCESS_BACKGROUND_LOCATION", PermissionProfile.Category.LOCATION, 9,
                "Location access in background", "Continuous surveillance even when app is closed");
        add(map, "android.permission.ACCESS_COARSE_LOCATION", PermissionProfile.Category.LOCATION, 4,
                "Approximate location", "General area tracking");

        // CAMERA & MICROPHONE — Very High
        add(map, "android.permission.CAMERA", PermissionProfile.Category.CAMERA_MIC, 8,
                "Access camera", "Silent photo/video capture for surveillance");
        add(map, "android.permission.RECORD_AUDIO", PermissionProfile.Category.CAMERA_MIC, 9,
                "Record microphone audio", "Ambient room audio surveillance");
        add(map, "android.permission.CAPTURE_AUDIO_OUTPUT", PermissionProfile.Category.CAMERA_MIC, 10,
                "Capture all audio output", "Complete audio surveillance of device");

        // STORAGE
        add(map, "android.permission.READ_EXTERNAL_STORAGE", PermissionProfile.Category.STORAGE, 5,
                "Read files and media", "Exfiltration of personal photos and documents");
        add(map, "android.permission.WRITE_EXTERNAL_STORAGE", PermissionProfile.Category.STORAGE, 4,
                "Write/modify files", "Drop malware payloads to storage");
        add(map, "android.permission.MANAGE_EXTERNAL_STORAGE", PermissionProfile.Category.STORAGE, 8,
                "Full file system access", "Ransomware can encrypt all user files");

        // SYSTEM ADMIN — Critical
        add(map, "android.permission.BIND_DEVICE_ADMIN", PermissionProfile.Category.SYSTEM_ADMIN, 10,
                "Device administrator access", "Can wipe device, lock screen, prevent uninstall");
        add(map, "android.permission.BIND_ACCESSIBILITY_SERVICE", PermissionProfile.Category.SYSTEM_ADMIN, 10,
                "Accessibility service", "Can read all screen content, simulate taps (banking overlay attacks)");
        add(map, "android.permission.INSTALL_PACKAGES", PermissionProfile.Category.SYSTEM_ADMIN, 10,
                "Install other apps silently", "Dropper malware installs additional payloads");
        add(map, "android.permission.DELETE_PACKAGES", PermissionProfile.Category.SYSTEM_ADMIN, 8,
                "Uninstall other apps", "Can remove security/antivirus apps");
        add(map, "android.permission.CHANGE_COMPONENT_ENABLED_STATE", PermissionProfile.Category.SYSTEM_ADMIN, 9,
                "Enable/disable app components", "Used to hide app icon after install");
        add(map, "android.permission.REBOOT", PermissionProfile.Category.SYSTEM_ADMIN, 7,
                "Reboot device", "Persistence mechanism for malware");

        // OVERLAY — Critical for banking trojans
        add(map, "android.permission.SYSTEM_ALERT_WINDOW", PermissionProfile.Category.OVERLAY, 9,
                "Draw over other apps", "Classic banking overlay attack — fake login screens");

        // BACKGROUND
        add(map, "android.permission.RECEIVE_BOOT_COMPLETED", PermissionProfile.Category.BACKGROUND, 5,
                "Auto-start on boot", "Ensures malware persists across restarts");
        add(map, "android.permission.FOREGROUND_SERVICE", PermissionProfile.Category.BACKGROUND, 3,
                "Run in foreground", "Keeps service alive, used by legitimate apps too");
        add(map, "android.permission.WAKE_LOCK", PermissionProfile.Category.BACKGROUND, 3,
                "Prevent CPU sleep", "Can drain battery, keeps miners running");
        add(map, "android.permission.REQUEST_INSTALL_PACKAGES", PermissionProfile.Category.BACKGROUND, 7,
                "Request installing APKs", "Can prompt user to install additional malware");

        // NETWORK
        add(map, "android.permission.INTERNET", PermissionProfile.Category.NETWORK, 2,
                "Internet access", "Required by most apps, but enables data exfiltration");
        add(map, "android.permission.ACCESS_WIFI_STATE", PermissionProfile.Category.NETWORK, 2,
                "View WiFi connections", "Network fingerprinting");
        add(map, "android.permission.CHANGE_NETWORK_STATE", PermissionProfile.Category.NETWORK, 5,
                "Change network connectivity", "Force data connections for command & control");

        // IDENTITY
        add(map, "android.permission.READ_PHONE_STATE", PermissionProfile.Category.IDENTITY, 6,
                "Read device identity (IMEI)", "Device fingerprinting and tracking");
        add(map, "android.permission.GET_ACCOUNTS", PermissionProfile.Category.IDENTITY, 7,
                "Access all signed-in accounts", "Enumerate Gmail, banking app accounts");
        add(map, "android.permission.USE_CREDENTIALS", PermissionProfile.Category.IDENTITY, 8,
                "Use account credentials", "Can authenticate as you on other services");

        // FINANCIAL
        add(map, "android.permission.NFC", PermissionProfile.Category.FINANCIAL, 7,
                "NFC (contactless payment)", "Can relay NFC payment data");
        add(map, "android.permission.BILLING", PermissionProfile.Category.FINANCIAL, 6,
                "In-app billing", "Unauthorized purchases");

        return map;
    }

    private void add(Map<String, PermissionProfile> map, String perm,
                     PermissionProfile.Category cat, int score, String readable, String abuse) {
        map.put(perm, new PermissionProfile(perm, cat, score, readable, abuse));
    }

    // ─────────────────────────────────────────────────
    //  Context Mismatch Rules
    //  "A calculator should NOT need GPS"
    // ─────────────────────────────────────────────────
    private List<ContextMismatchRule> buildMismatchRules() {
        List<ContextMismatchRule> rules = new ArrayList<>();

        // Utility apps with surveillance perms
        rules.add(new ContextMismatchRule(
                "Utility/tool app requesting SMS access",
                Arrays.asList("calculator", "flashlight", "compass", "torch", "ruler",
                        "battery", "cleaner", "booster", "speed"),
                null,
                Arrays.asList("android.permission.READ_SMS", "android.permission.RECEIVE_SMS"),
                25,
                ContextMismatchRule.MatchType.APP_NAME_CONTAINS_ANY_AND_PERM
        ));

        rules.add(new ContextMismatchRule(
                "Flashlight/torch app requesting location",
                Arrays.asList("flashlight", "torch", "light"),
                null,
                Arrays.asList("android.permission.ACCESS_FINE_LOCATION",
                        "android.permission.ACCESS_BACKGROUND_LOCATION"),
                20,
                ContextMismatchRule.MatchType.APP_NAME_CONTAINS_ANY_AND_PERM
        ));

        // Game/entertainment with financial perms
        rules.add(new ContextMismatchRule(
                "Game app requesting SMS (premium fraud risk)",
                Arrays.asList("game", "puzzle", "quiz", "casual", "play", "fun"),
                null,
                Arrays.asList("android.permission.SEND_SMS", "android.permission.READ_SMS"),
                22,
                ContextMismatchRule.MatchType.APP_NAME_CONTAINS_ANY_AND_PERM
        ));

        // Any app with overlay + accessibility = banking trojan signature
        rules.add(new ContextMismatchRule(
                "CRITICAL: Overlay + Accessibility combo (banking trojan signature)",
                null,
                Arrays.asList(PermissionProfile.Category.OVERLAY, PermissionProfile.Category.SYSTEM_ADMIN),
                null,
                40,
                ContextMismatchRule.MatchType.HAS_ALL_CATEGORIES
        ));

        // Overlay + SMS combo
        rules.add(new ContextMismatchRule(
                "HIGH RISK: Overlay + SMS access (credential theft pattern)",
                null,
                null,
                Arrays.asList("android.permission.SYSTEM_ALERT_WINDOW",
                        "android.permission.READ_SMS"),
                30,
                ContextMismatchRule.MatchType.HAS_ALL_PERMS
        ));

        // Boot receiver + no obvious purpose
        rules.add(new ContextMismatchRule(
                "Suspicious auto-start with admin privileges",
                null,
                null,
                Arrays.asList("android.permission.RECEIVE_BOOT_COMPLETED",
                        "android.permission.BIND_DEVICE_ADMIN"),
                30,
                ContextMismatchRule.MatchType.HAS_ALL_PERMS
        ));

        return rules;
    }

    // ─────────────────────────────────────────────────
    //  Result Container
    // ─────────────────────────────────────────────────
    public static class PermissionAnalysisResult {
        public int totalPermissions = 0;
        public int permissionRiskScore = 0;  // 0-100
        public List<PermissionProfile> profiledPermissions = new ArrayList<>();
        public List<String> highRiskPermissions = new ArrayList<>();
        public List<String> mediumRiskPermissions = new ArrayList<>();
        public List<String> contextMismatches = new ArrayList<>();
        public List<String> safeIndicators = new ArrayList<>();

        public static PermissionAnalysisResult empty() {
            PermissionAnalysisResult r = new PermissionAnalysisResult();
            r.safeIndicators.add("No permission data available");
            return r;
        }
    }

    // ─────────────────────────────────────────────────
    //  Context Mismatch Rule
    // ─────────────────────────────────────────────────
    static class ContextMismatchRule {
        enum MatchType {
            APP_NAME_CONTAINS_ANY_AND_PERM,
            HAS_ALL_CATEGORIES,
            HAS_ALL_PERMS
        }

        final String description;
        final List<String> appNameKeywords;
        final List<PermissionProfile.Category> requiredCategories;
        final List<String> requiredPermissions;
        final int riskBonus;
        final MatchType matchType;

        ContextMismatchRule(String description, List<String> appNameKeywords,
                             List<PermissionProfile.Category> requiredCategories,
                             List<String> requiredPermissions, int riskBonus, MatchType matchType) {
            this.description = description;
            this.appNameKeywords = appNameKeywords;
            this.requiredCategories = requiredCategories;
            this.requiredPermissions = requiredPermissions;
            this.riskBonus = riskBonus;
            this.matchType = matchType;
        }

        boolean matches(String appName, Set<PermissionProfile.Category> categories,
                        String[] permList) {
            Set<String> permSet = new HashSet<>(Arrays.asList(permList));

            switch (matchType) {
                case APP_NAME_CONTAINS_ANY_AND_PERM:
                    boolean nameMatch = false;
                    if (appNameKeywords != null) {
                        for (String kw : appNameKeywords) {
                            if (appName.contains(kw)) { nameMatch = true; break; }
                        }
                    }
                    if (!nameMatch) return false;
                    if (requiredPermissions != null) {
                        for (String p : requiredPermissions) {
                            if (permSet.contains(p)) return true;
                        }
                    }
                    return false;

                case HAS_ALL_CATEGORIES:
                    if (requiredCategories == null) return false;
                    return categories.containsAll(requiredCategories);

                case HAS_ALL_PERMS:
                    if (requiredPermissions == null) return false;
                    return permSet.containsAll(requiredPermissions);
            }
            return false;
        }
    }
}
