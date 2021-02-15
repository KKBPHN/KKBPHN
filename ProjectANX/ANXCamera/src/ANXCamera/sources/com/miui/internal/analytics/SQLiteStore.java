package com.miui.internal.analytics;

import android.content.Context;
import android.database.Cursor;

class SQLiteStore implements Storable {
    public static final String TAG = "ANALYTICS.SQLITESTORE";
    private DatabaseHelper mDatabaseHelper;

    SQLiteStore() {
    }

    public void close() {
    }

    public void create(Context context) {
        this.mDatabaseHelper = DatabaseHelper.getInstance();
    }

    public Cursor readDataset(String str) {
        DatabaseHelper databaseHelper = this.mDatabaseHelper;
        if (databaseHelper != null) {
            return databaseHelper.query(str);
        }
        return null;
    }

    public void writeData(String str, Integer num, String str2, String str3, String str4, String str5) {
        DatabaseHelper databaseHelper = this.mDatabaseHelper;
        if (databaseHelper != null) {
            databaseHelper.insert(str, num, str2, str3, str4, str5);
        }
    }
}
