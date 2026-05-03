package com.sudarshan.mobileguard.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sudarshan.mobileguard.models.ScanResult;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ScanResultDao_Impl implements ScanResultDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScanResult> __insertionAdapterOfScanResult;

  private final EntityDeletionOrUpdateAdapter<ScanResult> __updateAdapterOfScanResult;

  private final SharedSQLiteStatement __preparedStmtOfDeleteScan;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldScans;

  public ScanResultDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScanResult = new EntityInsertionAdapter<ScanResult>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `scan_results` (`id`,`packageName`,`appName`,`apkPath`,`sha256Hash`,`scanTimestamp`,`riskScore`,`riskLevel`,`hashMatchFound`,`suspiciousPermissions`,`behaviorFlagsFound`,`overlayAttackRisk`,`accessibilityAbuse`,`dangerousPermissions`,`suspiciousFindings`,`safeIndicators`,`apkSizeFormatted`,`installerSource`,`isSystemApp`,`verdict`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ScanResult entity) {
        statement.bindLong(1, entity.id);
        if (entity.packageName == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.packageName);
        }
        if (entity.appName == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.appName);
        }
        if (entity.apkPath == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.apkPath);
        }
        if (entity.sha256Hash == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.sha256Hash);
        }
        statement.bindLong(6, entity.scanTimestamp);
        statement.bindLong(7, entity.riskScore);
        final String _tmp = Converters.fromRiskLevel(entity.riskLevel);
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        final int _tmp_1 = entity.hashMatchFound ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        final int _tmp_2 = entity.suspiciousPermissions ? 1 : 0;
        statement.bindLong(10, _tmp_2);
        final int _tmp_3 = entity.behaviorFlagsFound ? 1 : 0;
        statement.bindLong(11, _tmp_3);
        final int _tmp_4 = entity.overlayAttackRisk ? 1 : 0;
        statement.bindLong(12, _tmp_4);
        final int _tmp_5 = entity.accessibilityAbuse ? 1 : 0;
        statement.bindLong(13, _tmp_5);
        final String _tmp_6 = Converters.fromStringList(entity.dangerousPermissions);
        if (_tmp_6 == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, _tmp_6);
        }
        final String _tmp_7 = Converters.fromStringList(entity.suspiciousFindings);
        if (_tmp_7 == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, _tmp_7);
        }
        final String _tmp_8 = Converters.fromStringList(entity.safeIndicators);
        if (_tmp_8 == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, _tmp_8);
        }
        if (entity.apkSizeFormatted == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.apkSizeFormatted);
        }
        if (entity.installerSource == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.installerSource);
        }
        final int _tmp_9 = entity.isSystemApp ? 1 : 0;
        statement.bindLong(19, _tmp_9);
        if (entity.verdict == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.verdict);
        }
      }
    };
    this.__updateAdapterOfScanResult = new EntityDeletionOrUpdateAdapter<ScanResult>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `scan_results` SET `id` = ?,`packageName` = ?,`appName` = ?,`apkPath` = ?,`sha256Hash` = ?,`scanTimestamp` = ?,`riskScore` = ?,`riskLevel` = ?,`hashMatchFound` = ?,`suspiciousPermissions` = ?,`behaviorFlagsFound` = ?,`overlayAttackRisk` = ?,`accessibilityAbuse` = ?,`dangerousPermissions` = ?,`suspiciousFindings` = ?,`safeIndicators` = ?,`apkSizeFormatted` = ?,`installerSource` = ?,`isSystemApp` = ?,`verdict` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ScanResult entity) {
        statement.bindLong(1, entity.id);
        if (entity.packageName == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.packageName);
        }
        if (entity.appName == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.appName);
        }
        if (entity.apkPath == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.apkPath);
        }
        if (entity.sha256Hash == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.sha256Hash);
        }
        statement.bindLong(6, entity.scanTimestamp);
        statement.bindLong(7, entity.riskScore);
        final String _tmp = Converters.fromRiskLevel(entity.riskLevel);
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        final int _tmp_1 = entity.hashMatchFound ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        final int _tmp_2 = entity.suspiciousPermissions ? 1 : 0;
        statement.bindLong(10, _tmp_2);
        final int _tmp_3 = entity.behaviorFlagsFound ? 1 : 0;
        statement.bindLong(11, _tmp_3);
        final int _tmp_4 = entity.overlayAttackRisk ? 1 : 0;
        statement.bindLong(12, _tmp_4);
        final int _tmp_5 = entity.accessibilityAbuse ? 1 : 0;
        statement.bindLong(13, _tmp_5);
        final String _tmp_6 = Converters.fromStringList(entity.dangerousPermissions);
        if (_tmp_6 == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, _tmp_6);
        }
        final String _tmp_7 = Converters.fromStringList(entity.suspiciousFindings);
        if (_tmp_7 == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, _tmp_7);
        }
        final String _tmp_8 = Converters.fromStringList(entity.safeIndicators);
        if (_tmp_8 == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, _tmp_8);
        }
        if (entity.apkSizeFormatted == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.apkSizeFormatted);
        }
        if (entity.installerSource == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.installerSource);
        }
        final int _tmp_9 = entity.isSystemApp ? 1 : 0;
        statement.bindLong(19, _tmp_9);
        if (entity.verdict == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.verdict);
        }
        statement.bindLong(21, entity.id);
      }
    };
    this.__preparedStmtOfDeleteScan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM scan_results WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldScans = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM scan_results WHERE scanTimestamp < ?";
        return _query;
      }
    };
  }

  @Override
  public long insertScan(final ScanResult scanResult) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfScanResult.insertAndReturnId(scanResult);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateScan(final ScanResult scanResult) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfScanResult.handle(scanResult);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteScan(final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteScan.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteScan.release(_stmt);
    }
  }

  @Override
  public void deleteOldScans(final long olderThan) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldScans.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, olderThan);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteOldScans.release(_stmt);
    }
  }

  @Override
  public LiveData<List<ScanResult>> getAllScans() {
    final String _sql = "SELECT * FROM scan_results ORDER BY scanTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"scan_results"}, false, new Callable<List<ScanResult>>() {
      @Override
      @Nullable
      public List<ScanResult> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfApkPath = CursorUtil.getColumnIndexOrThrow(_cursor, "apkPath");
          final int _cursorIndexOfSha256Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "sha256Hash");
          final int _cursorIndexOfScanTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "scanTimestamp");
          final int _cursorIndexOfRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "riskScore");
          final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
          final int _cursorIndexOfHashMatchFound = CursorUtil.getColumnIndexOrThrow(_cursor, "hashMatchFound");
          final int _cursorIndexOfSuspiciousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousPermissions");
          final int _cursorIndexOfBehaviorFlagsFound = CursorUtil.getColumnIndexOrThrow(_cursor, "behaviorFlagsFound");
          final int _cursorIndexOfOverlayAttackRisk = CursorUtil.getColumnIndexOrThrow(_cursor, "overlayAttackRisk");
          final int _cursorIndexOfAccessibilityAbuse = CursorUtil.getColumnIndexOrThrow(_cursor, "accessibilityAbuse");
          final int _cursorIndexOfDangerousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "dangerousPermissions");
          final int _cursorIndexOfSuspiciousFindings = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousFindings");
          final int _cursorIndexOfSafeIndicators = CursorUtil.getColumnIndexOrThrow(_cursor, "safeIndicators");
          final int _cursorIndexOfApkSizeFormatted = CursorUtil.getColumnIndexOrThrow(_cursor, "apkSizeFormatted");
          final int _cursorIndexOfInstallerSource = CursorUtil.getColumnIndexOrThrow(_cursor, "installerSource");
          final int _cursorIndexOfIsSystemApp = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemApp");
          final int _cursorIndexOfVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "verdict");
          final List<ScanResult> _result = new ArrayList<ScanResult>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScanResult _item;
            _item = new ScanResult();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _item.packageName = null;
            } else {
              _item.packageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            if (_cursor.isNull(_cursorIndexOfAppName)) {
              _item.appName = null;
            } else {
              _item.appName = _cursor.getString(_cursorIndexOfAppName);
            }
            if (_cursor.isNull(_cursorIndexOfApkPath)) {
              _item.apkPath = null;
            } else {
              _item.apkPath = _cursor.getString(_cursorIndexOfApkPath);
            }
            if (_cursor.isNull(_cursorIndexOfSha256Hash)) {
              _item.sha256Hash = null;
            } else {
              _item.sha256Hash = _cursor.getString(_cursorIndexOfSha256Hash);
            }
            _item.scanTimestamp = _cursor.getLong(_cursorIndexOfScanTimestamp);
            _item.riskScore = _cursor.getInt(_cursorIndexOfRiskScore);
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfRiskLevel)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfRiskLevel);
            }
            _item.riskLevel = Converters.toRiskLevel(_tmp);
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHashMatchFound);
            _item.hashMatchFound = _tmp_1 != 0;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSuspiciousPermissions);
            _item.suspiciousPermissions = _tmp_2 != 0;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfBehaviorFlagsFound);
            _item.behaviorFlagsFound = _tmp_3 != 0;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfOverlayAttackRisk);
            _item.overlayAttackRisk = _tmp_4 != 0;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfAccessibilityAbuse);
            _item.accessibilityAbuse = _tmp_5 != 0;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfDangerousPermissions)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfDangerousPermissions);
            }
            _item.dangerousPermissions = Converters.toStringList(_tmp_6);
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfSuspiciousFindings)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfSuspiciousFindings);
            }
            _item.suspiciousFindings = Converters.toStringList(_tmp_7);
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfSafeIndicators)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfSafeIndicators);
            }
            _item.safeIndicators = Converters.toStringList(_tmp_8);
            if (_cursor.isNull(_cursorIndexOfApkSizeFormatted)) {
              _item.apkSizeFormatted = null;
            } else {
              _item.apkSizeFormatted = _cursor.getString(_cursorIndexOfApkSizeFormatted);
            }
            if (_cursor.isNull(_cursorIndexOfInstallerSource)) {
              _item.installerSource = null;
            } else {
              _item.installerSource = _cursor.getString(_cursorIndexOfInstallerSource);
            }
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfIsSystemApp);
            _item.isSystemApp = _tmp_9 != 0;
            if (_cursor.isNull(_cursorIndexOfVerdict)) {
              _item.verdict = null;
            } else {
              _item.verdict = _cursor.getString(_cursorIndexOfVerdict);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<ScanResult>> getRecentScans(final int limit) {
    final String _sql = "SELECT * FROM scan_results ORDER BY scanTimestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return __db.getInvalidationTracker().createLiveData(new String[] {"scan_results"}, false, new Callable<List<ScanResult>>() {
      @Override
      @Nullable
      public List<ScanResult> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfApkPath = CursorUtil.getColumnIndexOrThrow(_cursor, "apkPath");
          final int _cursorIndexOfSha256Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "sha256Hash");
          final int _cursorIndexOfScanTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "scanTimestamp");
          final int _cursorIndexOfRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "riskScore");
          final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
          final int _cursorIndexOfHashMatchFound = CursorUtil.getColumnIndexOrThrow(_cursor, "hashMatchFound");
          final int _cursorIndexOfSuspiciousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousPermissions");
          final int _cursorIndexOfBehaviorFlagsFound = CursorUtil.getColumnIndexOrThrow(_cursor, "behaviorFlagsFound");
          final int _cursorIndexOfOverlayAttackRisk = CursorUtil.getColumnIndexOrThrow(_cursor, "overlayAttackRisk");
          final int _cursorIndexOfAccessibilityAbuse = CursorUtil.getColumnIndexOrThrow(_cursor, "accessibilityAbuse");
          final int _cursorIndexOfDangerousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "dangerousPermissions");
          final int _cursorIndexOfSuspiciousFindings = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousFindings");
          final int _cursorIndexOfSafeIndicators = CursorUtil.getColumnIndexOrThrow(_cursor, "safeIndicators");
          final int _cursorIndexOfApkSizeFormatted = CursorUtil.getColumnIndexOrThrow(_cursor, "apkSizeFormatted");
          final int _cursorIndexOfInstallerSource = CursorUtil.getColumnIndexOrThrow(_cursor, "installerSource");
          final int _cursorIndexOfIsSystemApp = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemApp");
          final int _cursorIndexOfVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "verdict");
          final List<ScanResult> _result = new ArrayList<ScanResult>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScanResult _item;
            _item = new ScanResult();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _item.packageName = null;
            } else {
              _item.packageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            if (_cursor.isNull(_cursorIndexOfAppName)) {
              _item.appName = null;
            } else {
              _item.appName = _cursor.getString(_cursorIndexOfAppName);
            }
            if (_cursor.isNull(_cursorIndexOfApkPath)) {
              _item.apkPath = null;
            } else {
              _item.apkPath = _cursor.getString(_cursorIndexOfApkPath);
            }
            if (_cursor.isNull(_cursorIndexOfSha256Hash)) {
              _item.sha256Hash = null;
            } else {
              _item.sha256Hash = _cursor.getString(_cursorIndexOfSha256Hash);
            }
            _item.scanTimestamp = _cursor.getLong(_cursorIndexOfScanTimestamp);
            _item.riskScore = _cursor.getInt(_cursorIndexOfRiskScore);
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfRiskLevel)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfRiskLevel);
            }
            _item.riskLevel = Converters.toRiskLevel(_tmp);
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHashMatchFound);
            _item.hashMatchFound = _tmp_1 != 0;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSuspiciousPermissions);
            _item.suspiciousPermissions = _tmp_2 != 0;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfBehaviorFlagsFound);
            _item.behaviorFlagsFound = _tmp_3 != 0;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfOverlayAttackRisk);
            _item.overlayAttackRisk = _tmp_4 != 0;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfAccessibilityAbuse);
            _item.accessibilityAbuse = _tmp_5 != 0;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfDangerousPermissions)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfDangerousPermissions);
            }
            _item.dangerousPermissions = Converters.toStringList(_tmp_6);
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfSuspiciousFindings)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfSuspiciousFindings);
            }
            _item.suspiciousFindings = Converters.toStringList(_tmp_7);
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfSafeIndicators)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfSafeIndicators);
            }
            _item.safeIndicators = Converters.toStringList(_tmp_8);
            if (_cursor.isNull(_cursorIndexOfApkSizeFormatted)) {
              _item.apkSizeFormatted = null;
            } else {
              _item.apkSizeFormatted = _cursor.getString(_cursorIndexOfApkSizeFormatted);
            }
            if (_cursor.isNull(_cursorIndexOfInstallerSource)) {
              _item.installerSource = null;
            } else {
              _item.installerSource = _cursor.getString(_cursorIndexOfInstallerSource);
            }
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfIsSystemApp);
            _item.isSystemApp = _tmp_9 != 0;
            if (_cursor.isNull(_cursorIndexOfVerdict)) {
              _item.verdict = null;
            } else {
              _item.verdict = _cursor.getString(_cursorIndexOfVerdict);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public ScanResult getLatestScanForPackage(final String packageName) {
    final String _sql = "SELECT * FROM scan_results WHERE packageName = ? ORDER BY scanTimestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (packageName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, packageName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
      final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
      final int _cursorIndexOfApkPath = CursorUtil.getColumnIndexOrThrow(_cursor, "apkPath");
      final int _cursorIndexOfSha256Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "sha256Hash");
      final int _cursorIndexOfScanTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "scanTimestamp");
      final int _cursorIndexOfRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "riskScore");
      final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
      final int _cursorIndexOfHashMatchFound = CursorUtil.getColumnIndexOrThrow(_cursor, "hashMatchFound");
      final int _cursorIndexOfSuspiciousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousPermissions");
      final int _cursorIndexOfBehaviorFlagsFound = CursorUtil.getColumnIndexOrThrow(_cursor, "behaviorFlagsFound");
      final int _cursorIndexOfOverlayAttackRisk = CursorUtil.getColumnIndexOrThrow(_cursor, "overlayAttackRisk");
      final int _cursorIndexOfAccessibilityAbuse = CursorUtil.getColumnIndexOrThrow(_cursor, "accessibilityAbuse");
      final int _cursorIndexOfDangerousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "dangerousPermissions");
      final int _cursorIndexOfSuspiciousFindings = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousFindings");
      final int _cursorIndexOfSafeIndicators = CursorUtil.getColumnIndexOrThrow(_cursor, "safeIndicators");
      final int _cursorIndexOfApkSizeFormatted = CursorUtil.getColumnIndexOrThrow(_cursor, "apkSizeFormatted");
      final int _cursorIndexOfInstallerSource = CursorUtil.getColumnIndexOrThrow(_cursor, "installerSource");
      final int _cursorIndexOfIsSystemApp = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemApp");
      final int _cursorIndexOfVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "verdict");
      final ScanResult _result;
      if (_cursor.moveToFirst()) {
        _result = new ScanResult();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfPackageName)) {
          _result.packageName = null;
        } else {
          _result.packageName = _cursor.getString(_cursorIndexOfPackageName);
        }
        if (_cursor.isNull(_cursorIndexOfAppName)) {
          _result.appName = null;
        } else {
          _result.appName = _cursor.getString(_cursorIndexOfAppName);
        }
        if (_cursor.isNull(_cursorIndexOfApkPath)) {
          _result.apkPath = null;
        } else {
          _result.apkPath = _cursor.getString(_cursorIndexOfApkPath);
        }
        if (_cursor.isNull(_cursorIndexOfSha256Hash)) {
          _result.sha256Hash = null;
        } else {
          _result.sha256Hash = _cursor.getString(_cursorIndexOfSha256Hash);
        }
        _result.scanTimestamp = _cursor.getLong(_cursorIndexOfScanTimestamp);
        _result.riskScore = _cursor.getInt(_cursorIndexOfRiskScore);
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfRiskLevel)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfRiskLevel);
        }
        _result.riskLevel = Converters.toRiskLevel(_tmp);
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfHashMatchFound);
        _result.hashMatchFound = _tmp_1 != 0;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfSuspiciousPermissions);
        _result.suspiciousPermissions = _tmp_2 != 0;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfBehaviorFlagsFound);
        _result.behaviorFlagsFound = _tmp_3 != 0;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfOverlayAttackRisk);
        _result.overlayAttackRisk = _tmp_4 != 0;
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfAccessibilityAbuse);
        _result.accessibilityAbuse = _tmp_5 != 0;
        final String _tmp_6;
        if (_cursor.isNull(_cursorIndexOfDangerousPermissions)) {
          _tmp_6 = null;
        } else {
          _tmp_6 = _cursor.getString(_cursorIndexOfDangerousPermissions);
        }
        _result.dangerousPermissions = Converters.toStringList(_tmp_6);
        final String _tmp_7;
        if (_cursor.isNull(_cursorIndexOfSuspiciousFindings)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getString(_cursorIndexOfSuspiciousFindings);
        }
        _result.suspiciousFindings = Converters.toStringList(_tmp_7);
        final String _tmp_8;
        if (_cursor.isNull(_cursorIndexOfSafeIndicators)) {
          _tmp_8 = null;
        } else {
          _tmp_8 = _cursor.getString(_cursorIndexOfSafeIndicators);
        }
        _result.safeIndicators = Converters.toStringList(_tmp_8);
        if (_cursor.isNull(_cursorIndexOfApkSizeFormatted)) {
          _result.apkSizeFormatted = null;
        } else {
          _result.apkSizeFormatted = _cursor.getString(_cursorIndexOfApkSizeFormatted);
        }
        if (_cursor.isNull(_cursorIndexOfInstallerSource)) {
          _result.installerSource = null;
        } else {
          _result.installerSource = _cursor.getString(_cursorIndexOfInstallerSource);
        }
        final int _tmp_9;
        _tmp_9 = _cursor.getInt(_cursorIndexOfIsSystemApp);
        _result.isSystemApp = _tmp_9 != 0;
        if (_cursor.isNull(_cursorIndexOfVerdict)) {
          _result.verdict = null;
        } else {
          _result.verdict = _cursor.getString(_cursorIndexOfVerdict);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public ScanResult getScanById(final int id) {
    final String _sql = "SELECT * FROM scan_results WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
      final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
      final int _cursorIndexOfApkPath = CursorUtil.getColumnIndexOrThrow(_cursor, "apkPath");
      final int _cursorIndexOfSha256Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "sha256Hash");
      final int _cursorIndexOfScanTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "scanTimestamp");
      final int _cursorIndexOfRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "riskScore");
      final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
      final int _cursorIndexOfHashMatchFound = CursorUtil.getColumnIndexOrThrow(_cursor, "hashMatchFound");
      final int _cursorIndexOfSuspiciousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousPermissions");
      final int _cursorIndexOfBehaviorFlagsFound = CursorUtil.getColumnIndexOrThrow(_cursor, "behaviorFlagsFound");
      final int _cursorIndexOfOverlayAttackRisk = CursorUtil.getColumnIndexOrThrow(_cursor, "overlayAttackRisk");
      final int _cursorIndexOfAccessibilityAbuse = CursorUtil.getColumnIndexOrThrow(_cursor, "accessibilityAbuse");
      final int _cursorIndexOfDangerousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "dangerousPermissions");
      final int _cursorIndexOfSuspiciousFindings = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousFindings");
      final int _cursorIndexOfSafeIndicators = CursorUtil.getColumnIndexOrThrow(_cursor, "safeIndicators");
      final int _cursorIndexOfApkSizeFormatted = CursorUtil.getColumnIndexOrThrow(_cursor, "apkSizeFormatted");
      final int _cursorIndexOfInstallerSource = CursorUtil.getColumnIndexOrThrow(_cursor, "installerSource");
      final int _cursorIndexOfIsSystemApp = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemApp");
      final int _cursorIndexOfVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "verdict");
      final ScanResult _result;
      if (_cursor.moveToFirst()) {
        _result = new ScanResult();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfPackageName)) {
          _result.packageName = null;
        } else {
          _result.packageName = _cursor.getString(_cursorIndexOfPackageName);
        }
        if (_cursor.isNull(_cursorIndexOfAppName)) {
          _result.appName = null;
        } else {
          _result.appName = _cursor.getString(_cursorIndexOfAppName);
        }
        if (_cursor.isNull(_cursorIndexOfApkPath)) {
          _result.apkPath = null;
        } else {
          _result.apkPath = _cursor.getString(_cursorIndexOfApkPath);
        }
        if (_cursor.isNull(_cursorIndexOfSha256Hash)) {
          _result.sha256Hash = null;
        } else {
          _result.sha256Hash = _cursor.getString(_cursorIndexOfSha256Hash);
        }
        _result.scanTimestamp = _cursor.getLong(_cursorIndexOfScanTimestamp);
        _result.riskScore = _cursor.getInt(_cursorIndexOfRiskScore);
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfRiskLevel)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfRiskLevel);
        }
        _result.riskLevel = Converters.toRiskLevel(_tmp);
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfHashMatchFound);
        _result.hashMatchFound = _tmp_1 != 0;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfSuspiciousPermissions);
        _result.suspiciousPermissions = _tmp_2 != 0;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfBehaviorFlagsFound);
        _result.behaviorFlagsFound = _tmp_3 != 0;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfOverlayAttackRisk);
        _result.overlayAttackRisk = _tmp_4 != 0;
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfAccessibilityAbuse);
        _result.accessibilityAbuse = _tmp_5 != 0;
        final String _tmp_6;
        if (_cursor.isNull(_cursorIndexOfDangerousPermissions)) {
          _tmp_6 = null;
        } else {
          _tmp_6 = _cursor.getString(_cursorIndexOfDangerousPermissions);
        }
        _result.dangerousPermissions = Converters.toStringList(_tmp_6);
        final String _tmp_7;
        if (_cursor.isNull(_cursorIndexOfSuspiciousFindings)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getString(_cursorIndexOfSuspiciousFindings);
        }
        _result.suspiciousFindings = Converters.toStringList(_tmp_7);
        final String _tmp_8;
        if (_cursor.isNull(_cursorIndexOfSafeIndicators)) {
          _tmp_8 = null;
        } else {
          _tmp_8 = _cursor.getString(_cursorIndexOfSafeIndicators);
        }
        _result.safeIndicators = Converters.toStringList(_tmp_8);
        if (_cursor.isNull(_cursorIndexOfApkSizeFormatted)) {
          _result.apkSizeFormatted = null;
        } else {
          _result.apkSizeFormatted = _cursor.getString(_cursorIndexOfApkSizeFormatted);
        }
        if (_cursor.isNull(_cursorIndexOfInstallerSource)) {
          _result.installerSource = null;
        } else {
          _result.installerSource = _cursor.getString(_cursorIndexOfInstallerSource);
        }
        final int _tmp_9;
        _tmp_9 = _cursor.getInt(_cursorIndexOfIsSystemApp);
        _result.isSystemApp = _tmp_9 != 0;
        if (_cursor.isNull(_cursorIndexOfVerdict)) {
          _result.verdict = null;
        } else {
          _result.verdict = _cursor.getString(_cursorIndexOfVerdict);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<ScanResult>> getHighRiskScans() {
    final String _sql = "SELECT * FROM scan_results WHERE riskScore >= 76 ORDER BY riskScore DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"scan_results"}, false, new Callable<List<ScanResult>>() {
      @Override
      @Nullable
      public List<ScanResult> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfApkPath = CursorUtil.getColumnIndexOrThrow(_cursor, "apkPath");
          final int _cursorIndexOfSha256Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "sha256Hash");
          final int _cursorIndexOfScanTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "scanTimestamp");
          final int _cursorIndexOfRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "riskScore");
          final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
          final int _cursorIndexOfHashMatchFound = CursorUtil.getColumnIndexOrThrow(_cursor, "hashMatchFound");
          final int _cursorIndexOfSuspiciousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousPermissions");
          final int _cursorIndexOfBehaviorFlagsFound = CursorUtil.getColumnIndexOrThrow(_cursor, "behaviorFlagsFound");
          final int _cursorIndexOfOverlayAttackRisk = CursorUtil.getColumnIndexOrThrow(_cursor, "overlayAttackRisk");
          final int _cursorIndexOfAccessibilityAbuse = CursorUtil.getColumnIndexOrThrow(_cursor, "accessibilityAbuse");
          final int _cursorIndexOfDangerousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "dangerousPermissions");
          final int _cursorIndexOfSuspiciousFindings = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousFindings");
          final int _cursorIndexOfSafeIndicators = CursorUtil.getColumnIndexOrThrow(_cursor, "safeIndicators");
          final int _cursorIndexOfApkSizeFormatted = CursorUtil.getColumnIndexOrThrow(_cursor, "apkSizeFormatted");
          final int _cursorIndexOfInstallerSource = CursorUtil.getColumnIndexOrThrow(_cursor, "installerSource");
          final int _cursorIndexOfIsSystemApp = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemApp");
          final int _cursorIndexOfVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "verdict");
          final List<ScanResult> _result = new ArrayList<ScanResult>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScanResult _item;
            _item = new ScanResult();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _item.packageName = null;
            } else {
              _item.packageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            if (_cursor.isNull(_cursorIndexOfAppName)) {
              _item.appName = null;
            } else {
              _item.appName = _cursor.getString(_cursorIndexOfAppName);
            }
            if (_cursor.isNull(_cursorIndexOfApkPath)) {
              _item.apkPath = null;
            } else {
              _item.apkPath = _cursor.getString(_cursorIndexOfApkPath);
            }
            if (_cursor.isNull(_cursorIndexOfSha256Hash)) {
              _item.sha256Hash = null;
            } else {
              _item.sha256Hash = _cursor.getString(_cursorIndexOfSha256Hash);
            }
            _item.scanTimestamp = _cursor.getLong(_cursorIndexOfScanTimestamp);
            _item.riskScore = _cursor.getInt(_cursorIndexOfRiskScore);
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfRiskLevel)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfRiskLevel);
            }
            _item.riskLevel = Converters.toRiskLevel(_tmp);
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHashMatchFound);
            _item.hashMatchFound = _tmp_1 != 0;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSuspiciousPermissions);
            _item.suspiciousPermissions = _tmp_2 != 0;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfBehaviorFlagsFound);
            _item.behaviorFlagsFound = _tmp_3 != 0;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfOverlayAttackRisk);
            _item.overlayAttackRisk = _tmp_4 != 0;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfAccessibilityAbuse);
            _item.accessibilityAbuse = _tmp_5 != 0;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfDangerousPermissions)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfDangerousPermissions);
            }
            _item.dangerousPermissions = Converters.toStringList(_tmp_6);
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfSuspiciousFindings)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfSuspiciousFindings);
            }
            _item.suspiciousFindings = Converters.toStringList(_tmp_7);
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfSafeIndicators)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfSafeIndicators);
            }
            _item.safeIndicators = Converters.toStringList(_tmp_8);
            if (_cursor.isNull(_cursorIndexOfApkSizeFormatted)) {
              _item.apkSizeFormatted = null;
            } else {
              _item.apkSizeFormatted = _cursor.getString(_cursorIndexOfApkSizeFormatted);
            }
            if (_cursor.isNull(_cursorIndexOfInstallerSource)) {
              _item.installerSource = null;
            } else {
              _item.installerSource = _cursor.getString(_cursorIndexOfInstallerSource);
            }
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfIsSystemApp);
            _item.isSystemApp = _tmp_9 != 0;
            if (_cursor.isNull(_cursorIndexOfVerdict)) {
              _item.verdict = null;
            } else {
              _item.verdict = _cursor.getString(_cursorIndexOfVerdict);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public int getTotalScanCount() {
    final String _sql = "SELECT COUNT(*) FROM scan_results";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getThreatCount() {
    final String _sql = "SELECT COUNT(*) FROM scan_results WHERE riskScore >= 51";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ScanResult> searchScans(final String query) {
    final String _sql = "SELECT * FROM scan_results WHERE appName LIKE '%' || ? || '%' OR packageName LIKE '%' || ? || '%' ORDER BY scanTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 2;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
      final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
      final int _cursorIndexOfApkPath = CursorUtil.getColumnIndexOrThrow(_cursor, "apkPath");
      final int _cursorIndexOfSha256Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "sha256Hash");
      final int _cursorIndexOfScanTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "scanTimestamp");
      final int _cursorIndexOfRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "riskScore");
      final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
      final int _cursorIndexOfHashMatchFound = CursorUtil.getColumnIndexOrThrow(_cursor, "hashMatchFound");
      final int _cursorIndexOfSuspiciousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousPermissions");
      final int _cursorIndexOfBehaviorFlagsFound = CursorUtil.getColumnIndexOrThrow(_cursor, "behaviorFlagsFound");
      final int _cursorIndexOfOverlayAttackRisk = CursorUtil.getColumnIndexOrThrow(_cursor, "overlayAttackRisk");
      final int _cursorIndexOfAccessibilityAbuse = CursorUtil.getColumnIndexOrThrow(_cursor, "accessibilityAbuse");
      final int _cursorIndexOfDangerousPermissions = CursorUtil.getColumnIndexOrThrow(_cursor, "dangerousPermissions");
      final int _cursorIndexOfSuspiciousFindings = CursorUtil.getColumnIndexOrThrow(_cursor, "suspiciousFindings");
      final int _cursorIndexOfSafeIndicators = CursorUtil.getColumnIndexOrThrow(_cursor, "safeIndicators");
      final int _cursorIndexOfApkSizeFormatted = CursorUtil.getColumnIndexOrThrow(_cursor, "apkSizeFormatted");
      final int _cursorIndexOfInstallerSource = CursorUtil.getColumnIndexOrThrow(_cursor, "installerSource");
      final int _cursorIndexOfIsSystemApp = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemApp");
      final int _cursorIndexOfVerdict = CursorUtil.getColumnIndexOrThrow(_cursor, "verdict");
      final List<ScanResult> _result = new ArrayList<ScanResult>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ScanResult _item;
        _item = new ScanResult();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfPackageName)) {
          _item.packageName = null;
        } else {
          _item.packageName = _cursor.getString(_cursorIndexOfPackageName);
        }
        if (_cursor.isNull(_cursorIndexOfAppName)) {
          _item.appName = null;
        } else {
          _item.appName = _cursor.getString(_cursorIndexOfAppName);
        }
        if (_cursor.isNull(_cursorIndexOfApkPath)) {
          _item.apkPath = null;
        } else {
          _item.apkPath = _cursor.getString(_cursorIndexOfApkPath);
        }
        if (_cursor.isNull(_cursorIndexOfSha256Hash)) {
          _item.sha256Hash = null;
        } else {
          _item.sha256Hash = _cursor.getString(_cursorIndexOfSha256Hash);
        }
        _item.scanTimestamp = _cursor.getLong(_cursorIndexOfScanTimestamp);
        _item.riskScore = _cursor.getInt(_cursorIndexOfRiskScore);
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfRiskLevel)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfRiskLevel);
        }
        _item.riskLevel = Converters.toRiskLevel(_tmp);
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfHashMatchFound);
        _item.hashMatchFound = _tmp_1 != 0;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfSuspiciousPermissions);
        _item.suspiciousPermissions = _tmp_2 != 0;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfBehaviorFlagsFound);
        _item.behaviorFlagsFound = _tmp_3 != 0;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfOverlayAttackRisk);
        _item.overlayAttackRisk = _tmp_4 != 0;
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfAccessibilityAbuse);
        _item.accessibilityAbuse = _tmp_5 != 0;
        final String _tmp_6;
        if (_cursor.isNull(_cursorIndexOfDangerousPermissions)) {
          _tmp_6 = null;
        } else {
          _tmp_6 = _cursor.getString(_cursorIndexOfDangerousPermissions);
        }
        _item.dangerousPermissions = Converters.toStringList(_tmp_6);
        final String _tmp_7;
        if (_cursor.isNull(_cursorIndexOfSuspiciousFindings)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getString(_cursorIndexOfSuspiciousFindings);
        }
        _item.suspiciousFindings = Converters.toStringList(_tmp_7);
        final String _tmp_8;
        if (_cursor.isNull(_cursorIndexOfSafeIndicators)) {
          _tmp_8 = null;
        } else {
          _tmp_8 = _cursor.getString(_cursorIndexOfSafeIndicators);
        }
        _item.safeIndicators = Converters.toStringList(_tmp_8);
        if (_cursor.isNull(_cursorIndexOfApkSizeFormatted)) {
          _item.apkSizeFormatted = null;
        } else {
          _item.apkSizeFormatted = _cursor.getString(_cursorIndexOfApkSizeFormatted);
        }
        if (_cursor.isNull(_cursorIndexOfInstallerSource)) {
          _item.installerSource = null;
        } else {
          _item.installerSource = _cursor.getString(_cursorIndexOfInstallerSource);
        }
        final int _tmp_9;
        _tmp_9 = _cursor.getInt(_cursorIndexOfIsSystemApp);
        _item.isSystemApp = _tmp_9 != 0;
        if (_cursor.isNull(_cursorIndexOfVerdict)) {
          _item.verdict = null;
        } else {
          _item.verdict = _cursor.getString(_cursorIndexOfVerdict);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
