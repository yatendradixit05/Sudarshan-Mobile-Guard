package com.sudarshan.mobileguard.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.sudarshan.mobileguard.models.ScanResult;
import java.util.List;

@Dao
public interface ScanResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertScan(ScanResult scanResult);

    @Update
    void updateScan(ScanResult scanResult);

    @Query("SELECT * FROM scan_results ORDER BY scanTimestamp DESC")
    LiveData<List<ScanResult>> getAllScans();

    @Query("SELECT * FROM scan_results ORDER BY scanTimestamp DESC LIMIT :limit")
    LiveData<List<ScanResult>> getRecentScans(int limit);

    @Query("SELECT * FROM scan_results WHERE packageName = :packageName ORDER BY scanTimestamp DESC LIMIT 1")
    ScanResult getLatestScanForPackage(String packageName);

    @Query("SELECT * FROM scan_results WHERE id = :id")
    ScanResult getScanById(int id);

    @Query("SELECT * FROM scan_results WHERE riskScore >= 76 ORDER BY riskScore DESC")
    LiveData<List<ScanResult>> getHighRiskScans();

    @Query("SELECT COUNT(*) FROM scan_results")
    int getTotalScanCount();

    @Query("SELECT COUNT(*) FROM scan_results WHERE riskScore >= 51")
    int getThreatCount();

    @Query("DELETE FROM scan_results WHERE id = :id")
    void deleteScan(int id);

    @Query("DELETE FROM scan_results WHERE scanTimestamp < :olderThan")
    void deleteOldScans(long olderThan);

    @Query("SELECT * FROM scan_results WHERE appName LIKE '%' || :query || '%' OR packageName LIKE '%' || :query || '%' ORDER BY scanTimestamp DESC")
    List<ScanResult> searchScans(String query);
}
