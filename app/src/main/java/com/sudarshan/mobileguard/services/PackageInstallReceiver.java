package com.sudarshan.mobileguard.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.sudarshan.mobileguard.database.AppDatabase;
import com.sudarshan.mobileguard.engine.ScanEngine;
import com.sudarshan.mobileguard.models.ScanResult;

/**
 * PackageInstallReceiver — Listens for new app installs.
 *
 * Triggers automatic background scan whenever a new APK is installed
 * or updated on the device. This is the "zero-click" protection feature.
 */
public class PackageInstallReceiver extends BroadcastReceiver {

    private static final String TAG = "PackageInstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) return;

        String action = intent.getAction();
        Uri data = intent.getData();
        if (data == null) return;

        String packageName = data.getSchemeSpecificPart();
        if (packageName == null || packageName.isEmpty()) return;

        // Don't scan ourselves
        if (packageName.equals(context.getPackageName())) return;

        Log.i(TAG, "New package event: " + action + " → " + packageName);

        boolean isReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
        String eventType = isReplacing ? "UPDATE" : "NEW_INSTALL";

        Log.i(TAG, "Triggering auto-scan for " + packageName + " [" + eventType + "]");

        // Run scan in background thread (BroadcastReceiver has 10s limit)
        Thread scanThread = new Thread(() -> {
            autoScanPackage(context, packageName);
        });
        scanThread.setDaemon(true);
        scanThread.start();
    }

    private void autoScanPackage(Context context, String packageName) {
        ScanEngine engine = new ScanEngine(context);
        AppDatabase db = AppDatabase.getInstance(context);

        engine.setProgressListener(new ScanEngine.ScanProgressListener() {
            @Override
            public void onProgress(String phase, int percent) {
                Log.d(TAG, "Auto-scan [" + packageName + "] " + percent + "% — " + phase);
            }

            @Override
            public void onComplete(ScanResult result) {
                // Save to database
                new Thread(() -> {
                    long id = db.scanResultDao().insertScan(result);
                    result.id = (int) id;

                    // Alert user if risk is MEDIUM or higher
                    if (result.riskScore >= 51) {
                        InstallMonitorService.showThreatNotification(
                                context, result.appName, result.riskScore, result.id);
                        Log.w(TAG, "THREAT FOUND in auto-scan: " + packageName
                                + " score=" + result.riskScore);
                    } else {
                        Log.i(TAG, "Auto-scan clean: " + packageName
                                + " score=" + result.riskScore);
                    }
                    engine.shutdown();
                }).start();
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "Auto-scan error for " + packageName + ": " + message);
                engine.shutdown();
            }
        });

        engine.scanPackage(packageName);
    }
}
