package com.sudarshan.mobileguard.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.sudarshan.mobileguard.database.Converters;
import java.util.List;

@Entity(tableName = "scan_results")
@TypeConverters(Converters.class)
public class ScanResult {

    public enum RiskLevel {
        SAFE,       // 0–25
        LOW,        // 26–50
        MEDIUM,     // 51–75
        HIGH,       // 76–90
        CRITICAL    // 91–100
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String packageName;
    public String appName;
    public String apkPath;
    public String sha256Hash;
    public long scanTimestamp;
    public int riskScore;           // 0–100
    public RiskLevel riskLevel;

    // Detection flags
    public boolean hashMatchFound;       // Known malware hash
    public boolean suspiciousPermissions;
    public boolean behaviorFlagsFound;
    public boolean overlayAttackRisk;
    public boolean accessibilityAbuse;

    public List<String> dangerousPermissions;
    public List<String> suspiciousFindings;
    public List<String> safeIndicators;

    // Meta
    public String apkSizeFormatted;
    public String installerSource;   // Play Store, Unknown, ADB, etc.
    public boolean isSystemApp;
    public String verdict;           // Human-readable one-liner

    public ScanResult() {
        this.scanTimestamp = System.currentTimeMillis();
    }

    public static RiskLevel levelFromScore(int score) {
        if (score <= 15) return RiskLevel.SAFE;
        if (score <= 35) return RiskLevel.LOW;
        if (score <= 60) return RiskLevel.MEDIUM;
        if (score <= 80) return RiskLevel.HIGH;
        return RiskLevel.CRITICAL;
    }

    public int getRiskColor() {
        switch (riskLevel) {
            case SAFE:     return 0xFF00C853;  // Green
            case LOW:      return 0xFF64DD17;  // Light green
            case MEDIUM:   return 0xFFFF6F00;  // Amber
            case HIGH:     return 0xFFDD2C00;  // Deep orange
            case CRITICAL: return 0xFFB71C1C;  // Dark red
            default:       return 0xFF9E9E9E;
        }
    }
}
