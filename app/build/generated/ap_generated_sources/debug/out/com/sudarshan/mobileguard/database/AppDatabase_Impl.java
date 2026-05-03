package com.sudarshan.mobileguard.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ScanResultDao _scanResultDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `scan_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT, `appName` TEXT, `apkPath` TEXT, `sha256Hash` TEXT, `scanTimestamp` INTEGER NOT NULL, `riskScore` INTEGER NOT NULL, `riskLevel` TEXT, `hashMatchFound` INTEGER NOT NULL, `suspiciousPermissions` INTEGER NOT NULL, `behaviorFlagsFound` INTEGER NOT NULL, `overlayAttackRisk` INTEGER NOT NULL, `accessibilityAbuse` INTEGER NOT NULL, `dangerousPermissions` TEXT, `suspiciousFindings` TEXT, `safeIndicators` TEXT, `apkSizeFormatted` TEXT, `installerSource` TEXT, `isSystemApp` INTEGER NOT NULL, `verdict` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4ede200c70d708a779593c6be060cfa2')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `scan_results`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsScanResults = new HashMap<String, TableInfo.Column>(20);
        _columnsScanResults.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("packageName", new TableInfo.Column("packageName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("appName", new TableInfo.Column("appName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("apkPath", new TableInfo.Column("apkPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("sha256Hash", new TableInfo.Column("sha256Hash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("scanTimestamp", new TableInfo.Column("scanTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("riskScore", new TableInfo.Column("riskScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("riskLevel", new TableInfo.Column("riskLevel", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("hashMatchFound", new TableInfo.Column("hashMatchFound", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("suspiciousPermissions", new TableInfo.Column("suspiciousPermissions", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("behaviorFlagsFound", new TableInfo.Column("behaviorFlagsFound", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("overlayAttackRisk", new TableInfo.Column("overlayAttackRisk", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("accessibilityAbuse", new TableInfo.Column("accessibilityAbuse", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("dangerousPermissions", new TableInfo.Column("dangerousPermissions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("suspiciousFindings", new TableInfo.Column("suspiciousFindings", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("safeIndicators", new TableInfo.Column("safeIndicators", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("apkSizeFormatted", new TableInfo.Column("apkSizeFormatted", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("installerSource", new TableInfo.Column("installerSource", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("isSystemApp", new TableInfo.Column("isSystemApp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanResults.put("verdict", new TableInfo.Column("verdict", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScanResults = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScanResults = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoScanResults = new TableInfo("scan_results", _columnsScanResults, _foreignKeysScanResults, _indicesScanResults);
        final TableInfo _existingScanResults = TableInfo.read(db, "scan_results");
        if (!_infoScanResults.equals(_existingScanResults)) {
          return new RoomOpenHelper.ValidationResult(false, "scan_results(com.sudarshan.mobileguard.models.ScanResult).\n"
                  + " Expected:\n" + _infoScanResults + "\n"
                  + " Found:\n" + _existingScanResults);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4ede200c70d708a779593c6be060cfa2", "4b13297330046fd8632b215d792f9e5b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "scan_results");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `scan_results`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ScanResultDao.class, ScanResultDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ScanResultDao scanResultDao() {
    if (_scanResultDao != null) {
      return _scanResultDao;
    } else {
      synchronized(this) {
        if(_scanResultDao == null) {
          _scanResultDao = new ScanResultDao_Impl(this);
        }
        return _scanResultDao;
      }
    }
  }
}
