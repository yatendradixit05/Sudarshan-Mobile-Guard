package com.sudarshan.mobileguard;

import android.app.Application;
import android.util.Log;
import com.sudarshan.mobileguard.engine.MalwareHashDatabase;

public class SudarshanApplication extends Application {

    private static final String TAG = "SudarshanApp";

    @Override
    public void onCreate() {
        super.onCreate();
        // Pre-warm the hash database on app start (runs once, cached as singleton)
        new Thread(() -> {
            MalwareHashDatabase db = MalwareHashDatabase.getInstance();
            Log.i(TAG, "Malware hash database loaded: " + db.getDatabaseSize() + " entries");
        }).start();
    }
}
