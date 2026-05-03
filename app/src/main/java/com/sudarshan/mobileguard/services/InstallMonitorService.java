package com.sudarshan.mobileguard.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.sudarshan.mobileguard.R;
import com.sudarshan.mobileguard.activities.ReportActivity;

/**
 * InstallMonitorService — Foreground service that keeps Sudarshan Guard alive.
 *
 * Acts as the "always-on" guardian. When PackageInstallReceiver fires,
 * it delegates the scan to ScanJobService to avoid ANR on the receiver.
 */
public class InstallMonitorService extends Service {

    private static final String TAG = "InstallMonitorService";
    public static final String CHANNEL_ID = "sudarshan_guard_channel";
    public static final String CHANNEL_THREAT_ID = "sudarshan_threat_channel";
    public static final int NOTIFICATION_ID = 1001;
    public static final int THREAT_NOTIFICATION_ID = 1002;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        startForeground(NOTIFICATION_ID, buildGuardNotification());
        Log.i(TAG, "Sudarshan Guard is active");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Restart if killed
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ─────────────────────────────────────────────────
    //  Threat Alert Notification
    //  Called from ScanJobService when threat found
    // ─────────────────────────────────────────────────
    public static void showThreatNotification(android.content.Context context,
                                               String appName, int riskScore, int scanId) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra("scan_id", scanId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, scanId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String title = riskScore >= 90 ? "⚠️ CRITICAL THREAT DETECTED" : "⚠️ Suspicious App Detected";
        String body = appName + " has a risk score of " + riskScore + "/100. Tap to view report.";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_THREAT_ID)
                .setSmallIcon(R.drawable.ic_shield_alert)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setColor(0xFFDD2C00)
                .setVibrate(new long[]{0, 500, 200, 500});

        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(THREAT_NOTIFICATION_ID, builder.build());
        }
    }

    // ─────────────────────────────────────────────────
    //  Private Helpers
    // ─────────────────────────────────────────────────
    private Notification buildGuardNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_shield_active)
                .setContentTitle("Sudarshan Guard Active")
                .setContentText("Monitoring for malicious apps...")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setColor(0xFF00C853)
                .build();
    }

    private void createNotificationChannels() {
        NotificationManager nm = getSystemService(NotificationManager.class);
        if (nm == null) return;

        // Guardian status channel (low priority, persistent)
        NotificationChannel guardChannel = new NotificationChannel(
                CHANNEL_ID,
                "Guard Status",
                NotificationManager.IMPORTANCE_LOW);
        guardChannel.setDescription("Shows when Sudarshan Guard is actively protecting your device");
        nm.createNotificationChannel(guardChannel);

        // Threat alert channel (high priority)
        NotificationChannel threatChannel = new NotificationChannel(
                CHANNEL_THREAT_ID,
                "Threat Alerts",
                NotificationManager.IMPORTANCE_HIGH);
        threatChannel.setDescription("Critical alerts when malicious apps are detected");
        threatChannel.enableVibration(true);
        nm.createNotificationChannel(threatChannel);
    }
}
