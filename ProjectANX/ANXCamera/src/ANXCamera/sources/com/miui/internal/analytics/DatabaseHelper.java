package com.miui.internal.analytics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import miui.util.AppConstants;
import miui.util.SoftReferenceSingleton;

class DatabaseHelper {
    private static final String CREATE_TABLE = "create table analytics (_id integer primary key autoincrement,_time datetime,_status integer,package text,type integer,event_id text,param text,time text,value text)";
    private static final String DATABASE_NAME = "analytics.db";
    private static final String DELETE_EXPIRED_ITEMS = "delete from analytics where _time < datetime('now', '-7 days')";
    private static final String DELETE_ITEMS = "delete from analytics where _status = 1";
    private static final String INSERT_ITEM = "insert into analytics values (null, datetime('now'), 0, ?, ?, ?, ?, ?, ?)";
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public DatabaseHelper createInstance() {
            return new DatabaseHelper();
        }
    };
    private static final String QUERY_ITEMS = "select * from analytics where _status = 1";
    private static final String TABLE_NAME = "analytics";
    private static final String TAG = "AnalyticsDatabaseHelper";
    private static final String UPDATE_ALL_ITEMS_SATUS = "update analytics set _status = 0";
    private static final String UPDATE_ITEMS_SATUS = "update analytics set _status = 1 where (_time < datetime(date(datetime('now'))))";
    private static final int VERSION = 2;
    private SQLiteOpenHelper mOpenHelper;

    private DatabaseHelper() {
        AnonymousClass1 r0 = new SQLiteOpenHelper(AppConstants.getCurrentApplication(), DATABASE_NAME, null, 2) {
            public void onCreate(SQLiteDatabase sQLiteDatabase) {
                sQLiteDatabase.execSQL(DatabaseHelper.CREATE_TABLE);
            }

            public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            }
        };
        this.mOpenHelper = r0;
    }

    public static DatabaseHelper getInstance() {
        return (DatabaseHelper) INSTANCE.get();
    }

    public void deleteLastDispatchedEvents() {
        try {
            SQLiteDatabase writableDatabase = this.mOpenHelper.getWritableDatabase();
            if (writableDatabase != null) {
                writableDatabase.execSQL(DELETE_ITEMS);
            }
        } catch (SQLiteException e) {
            Log.e(TAG, String.format("can't delete from database:%s", new Object[]{this.mOpenHelper.getDatabaseName()}), e);
        }
    }

    public void insert(String str, Integer num, String str2, String str3, String str4, String str5) {
        try {
            SQLiteDatabase writableDatabase = this.mOpenHelper.getWritableDatabase();
            if (writableDatabase != null) {
                writableDatabase.execSQL(INSERT_ITEM, new Object[]{str, num, str2, str3, str4, str5});
            }
        } catch (SQLiteException e) {
            Log.e(TAG, String.format("database:%s is not writable!", new Object[]{this.mOpenHelper.getDatabaseName()}), e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x006c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Cursor query(String str) {
        Cursor cursor;
        SQLiteDatabase sQLiteDatabase = null;
        try {
            SQLiteDatabase sQLiteDatabase2 = this.mOpenHelper.getWritableDatabase();
            try {
                sQLiteDatabase2.beginTransaction();
                sQLiteDatabase2.execSQL(DELETE_EXPIRED_ITEMS);
                sQLiteDatabase2.execSQL(UPDATE_ALL_ITEMS_SATUS);
                String str2 = UPDATE_ITEMS_SATUS;
                if (str != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("update analytics set _status = 1 where (_time < datetime(date(datetime('now')))) and (");
                    sb.append(str);
                    sb.append(")");
                    str2 = sb.toString();
                }
                sQLiteDatabase2.execSQL(str2);
                Cursor rawQuery = sQLiteDatabase2.rawQuery(QUERY_ITEMS, null);
                sQLiteDatabase2.setTransactionSuccessful();
                if (sQLiteDatabase2 == null) {
                    return rawQuery;
                }
                sQLiteDatabase2.endTransaction();
                return rawQuery;
            } catch (SQLiteException e) {
                e = e;
                SQLiteDatabase sQLiteDatabase3 = sQLiteDatabase2;
                cursor = null;
                sQLiteDatabase = sQLiteDatabase3;
                try {
                    Log.e(TAG, String.format("can't read database:%s", new Object[]{this.mOpenHelper.getDatabaseName()}), e);
                    if (sQLiteDatabase != null) {
                    }
                    return cursor;
                } catch (Throwable th) {
                    th = th;
                    sQLiteDatabase2 = sQLiteDatabase;
                    if (sQLiteDatabase2 != null) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                if (sQLiteDatabase2 != null) {
                    sQLiteDatabase2.endTransaction();
                }
                throw th;
            }
        } catch (SQLiteException e2) {
            e = e2;
            cursor = null;
            Log.e(TAG, String.format("can't read database:%s", new Object[]{this.mOpenHelper.getDatabaseName()}), e);
            if (sQLiteDatabase != null) {
                sQLiteDatabase.endTransaction();
            }
            return cursor;
        }
    }
}
