package com.sudarshan.mobileguard.models;

public class PermissionProfile {

    public enum Category {
        COMMUNICATION,   // SMS, Calls, Contacts
        LOCATION,        // GPS, Network location
        STORAGE,         // Files, Media
        CAMERA_MIC,      // Surveillance risk
        FINANCIAL,       // Billing, NFC payments
        SYSTEM_ADMIN,    // Device admin, accessibility
        OVERLAY,         // Draw over other apps
        BACKGROUND,      // Wake lock, boot receiver
        NETWORK,         // Internet, Wifi state
        IDENTITY         // Accounts, phone state
    }

    public final String permissionName;
    public final Category category;
    public final int dangerScore;        // 1–10: how suspicious if misused
    public final String humanReadable;
    public final String abuseScenario;   // Why this is dangerous

    public PermissionProfile(String permissionName, Category category,
                              int dangerScore, String humanReadable, String abuseScenario) {
        this.permissionName = permissionName;
        this.category = category;
        this.dangerScore = dangerScore;
        this.humanReadable = humanReadable;
        this.abuseScenario = abuseScenario;
    }
}
