package com.sudarshan.mobileguard.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.sudarshan.mobileguard.R;
import com.sudarshan.mobileguard.models.ScanResult;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScanResultAdapter extends ListAdapter<ScanResult, ScanResultAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ScanResult result);
    }

    private final OnItemClickListener listener;
    private static final SimpleDateFormat SDF =
            new SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault());

    public ScanResultAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scan_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScanResult result = getItem(position);
        holder.bind(result, listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAppName, tvPackage, tvRiskScore, tvRiskBadge, tvTime;

        ViewHolder(View itemView) {
            super(itemView);
            tvAppName   = itemView.findViewById(R.id.item_app_name);
            tvPackage   = itemView.findViewById(R.id.item_package);
            tvRiskScore = itemView.findViewById(R.id.item_risk_score);
            tvRiskBadge = itemView.findViewById(R.id.item_risk_badge);
            tvTime      = itemView.findViewById(R.id.item_scan_time);
        }

        void bind(ScanResult result, OnItemClickListener listener) {
            tvAppName.setText(result.appName != null ? result.appName : result.packageName);
            tvPackage.setText(result.packageName);
            tvRiskScore.setText(result.riskScore + "/100");
            tvTime.setText(SDF.format(new Date(result.scanTimestamp)));

            String badge = result.riskLevel != null ? result.riskLevel.name() : "UNKNOWN";
            tvRiskBadge.setText(badge);
            tvRiskBadge.setBackgroundColor(result.getRiskColor());

            itemView.setOnClickListener(v -> listener.onItemClick(result));
        }
    }

    private static final DiffUtil.ItemCallback<ScanResult> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ScanResult>() {
                @Override
                public boolean areItemsTheSame(@NonNull ScanResult a, @NonNull ScanResult b) {
                    return a.id == b.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull ScanResult a, @NonNull ScanResult b) {
                    return a.riskScore == b.riskScore && a.riskLevel == b.riskLevel;
                }
            };
}
