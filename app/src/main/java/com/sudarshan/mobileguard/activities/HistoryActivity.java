package com.sudarshan.mobileguard.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sudarshan.mobileguard.R;
import com.sudarshan.mobileguard.adapters.ScanResultAdapter;
import com.sudarshan.mobileguard.database.AppDatabase;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        AppDatabase db = AppDatabase.getInstance(this);
        RecyclerView rv = findViewById(R.id.rv_history);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ScanResultAdapter adapter = new ScanResultAdapter(result -> {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("scan_id", result.id);
            startActivity(intent);
        });
        rv.setAdapter(adapter);

        db.scanResultDao().getAllScans().observe(this, adapter::submitList);

        findViewById(R.id.btn_back_history).setOnClickListener(v -> finish());
    }
}
