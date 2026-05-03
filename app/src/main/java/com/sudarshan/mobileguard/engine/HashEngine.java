package com.sudarshan.mobileguard.engine;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * HashEngine — Layer 1 of Sudarshan's 3-layer detection system.
 *
 * Computes SHA-256 of any APK file and matches it against the
 * built-in MalwareHashDatabase. Completely offline, no network needed.
 */
public class HashEngine {

    private static final String TAG = "HashEngine";
    private static final int BUFFER_SIZE = 8192;

    private final MalwareHashDatabase hashDatabase;

    public HashEngine() {
        this.hashDatabase = MalwareHashDatabase.getInstance();
    }

    /**
     * Computes the SHA-256 hash of a file.
     * @param filePath absolute path to APK
     * @return hex string of SHA-256, or null on failure
     */
    public String computeSHA256(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            Log.e(TAG, "File not accessible: " + filePath);
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            try (FileInputStream fis = new FileInputStream(file)) {
                while ((bytesRead = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }

            byte[] hashBytes = digest.digest();
            return bytesToHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "SHA-256 not available", e);
        } catch (IOException e) {
            Log.e(TAG, "IO error while hashing: " + filePath, e);
        }
        return null;
    }

    /**
     * Check if the given SHA-256 hash is in the known malware database.
     * @return HashLookupResult with match status and threat info
     */
    public HashLookupResult lookupHash(String sha256) {
        if (sha256 == null || sha256.isEmpty()) {
            return new HashLookupResult(false, null, 0);
        }
        return hashDatabase.lookup(sha256.toLowerCase());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // ─────────────────────────────────────────────────
    //  Result container
    // ─────────────────────────────────────────────────

    public static class HashLookupResult {
        public final boolean isMalicious;
        public final String threatName;     // e.g. "Trojan.BankBot.A"
        public final int baseSeverityScore; // 0–100

        public HashLookupResult(boolean isMalicious, String threatName, int baseSeverityScore) {
            this.isMalicious = isMalicious;
            this.threatName = threatName;
            this.baseSeverityScore = baseSeverityScore;
        }
    }
}
