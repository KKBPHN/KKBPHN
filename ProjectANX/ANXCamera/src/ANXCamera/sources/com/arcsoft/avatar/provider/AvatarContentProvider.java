package com.arcsoft.avatar.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.arcsoft.avatar.util.LOG;

public class AvatarContentProvider extends ContentProvider {
    private static final String a = "AvatarContentProvider";
    private DBHelper b;

    public class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "_arc_avatar.db", null, 2);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE avatar_db (_id integer primary key autoincrement,thumbnail BLOB,config_path TEXT);");
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS avatar_db");
            onCreate(sQLiteDatabase);
        }
    }

    /* access modifiers changed from: protected */
    public String a(Uri uri, String str, String str2) {
        StringBuilder sb = new StringBuilder();
        String str3 = "";
        sb.append(str3);
        sb.append(ContentUris.parseId(uri));
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(" = ");
        sb3.append(sb2);
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb4);
        if (!TextUtils.isEmpty(str2)) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("and ( ");
            sb6.append(str2);
            sb6.append(" )");
            str3 = sb6.toString();
        }
        sb5.append(str3);
        String sb7 = sb5.toString();
        StringBuilder sb8 = new StringBuilder();
        sb8.append("newSelection : ");
        sb8.append(sb7);
        LOG.d("DELETE", sb8.toString());
        return sb7;
    }

    public int delete(@NonNull Uri uri, @Nullable String str, @Nullable String[] strArr) {
        int i;
        synchronized (this.b) {
            SQLiteDatabase sQLiteDatabase = null;
            try {
                SQLiteDatabase writableDatabase = this.b.getWritableDatabase();
                writableDatabase.beginTransaction();
                if (AvatarProfile.sUriMatcher.match(uri) != 1) {
                    i = -1;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("_id = ");
                    sb.append(str);
                    i = writableDatabase.delete("avatar_db", sb.toString(), strArr);
                }
                writableDatabase.setTransactionSuccessful();
                if (writableDatabase.inTransaction()) {
                    writableDatabase.endTransaction();
                }
            } catch (Throwable th) {
                if (sQLiteDatabase.inTransaction()) {
                    sQLiteDatabase.endTransaction();
                }
                throw th;
            }
        }
        String str2 = a;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("DELETE count = ");
        sb2.append(i);
        LOG.d(str2, sb2.toString());
        return i;
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0070 A[Catch:{ all -> 0x0066 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri uri2;
        SQLiteDatabase sQLiteDatabase;
        synchronized (this.b) {
            uri2 = null;
            try {
                sQLiteDatabase = this.b.getWritableDatabase();
                try {
                    sQLiteDatabase.beginTransaction();
                    long j = -1;
                    if (AvatarProfile.sUriMatcher.match(uri) == 1) {
                        j = sQLiteDatabase.insert("avatar_db", null, contentValues);
                    }
                    sQLiteDatabase.setTransactionSuccessful();
                    if (j < 0) {
                        String str = a;
                        StringBuilder sb = new StringBuilder();
                        sb.append("insert err:rowId=");
                        sb.append(j);
                        Log.e(str, sb.toString());
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(uri);
                        sb2.append("/");
                        sb2.append(String.valueOf(j));
                        uri2 = Uri.parse(sb2.toString());
                    }
                    if (sQLiteDatabase.inTransaction()) {
                        sQLiteDatabase.endTransaction();
                    }
                } catch (Throwable th) {
                    th = th;
                    if (sQLiteDatabase.inTransaction()) {
                        sQLiteDatabase.endTransaction();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                sQLiteDatabase = null;
                if (sQLiteDatabase.inTransaction()) {
                }
                throw th;
            }
        }
        return uri2;
    }

    public boolean onCreate() {
        this.b = new DBHelper(getContext());
        return true;
    }

    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] strArr, @Nullable String str, @Nullable String[] strArr2, @Nullable String str2) {
        Cursor cursor;
        SQLiteDatabase sQLiteDatabase;
        Uri uri2 = uri;
        StringBuilder sb = new StringBuilder();
        sb.append("URI = ");
        sb.append(uri);
        LOG.d("DELETE", sb.toString());
        synchronized (this.b) {
            cursor = null;
            try {
                sQLiteDatabase = this.b.getReadableDatabase();
                try {
                    sQLiteDatabase.beginTransaction();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("URI = ");
                    sb2.append(uri);
                    LOG.d("DELETE", sb2.toString());
                    if (AvatarProfile.sUriMatcher.match(uri) == 1) {
                        cursor = sQLiteDatabase.query("avatar_db", strArr, str, strArr2, null, null, str2);
                    }
                    sQLiteDatabase.setTransactionSuccessful();
                    if (cursor == null) {
                        Log.e(a, "query err:retCursor==null");
                    } else {
                        cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    }
                    if (sQLiteDatabase != null) {
                        if (sQLiteDatabase.inTransaction()) {
                            sQLiteDatabase.endTransaction();
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    if (sQLiteDatabase != null && sQLiteDatabase.inTransaction()) {
                        sQLiteDatabase.endTransaction();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                sQLiteDatabase = null;
                sQLiteDatabase.endTransaction();
                throw th;
            }
        }
        return cursor;
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String str, @Nullable String[] strArr) {
        int i;
        synchronized (this.b) {
            SQLiteDatabase sQLiteDatabase = null;
            try {
                SQLiteDatabase writableDatabase = this.b.getWritableDatabase();
                writableDatabase.beginTransaction();
                if (AvatarProfile.sUriMatcher.match(uri) != 1) {
                    i = -1;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("_id = ");
                    sb.append(str);
                    i = writableDatabase.update("avatar_db", contentValues, sb.toString(), strArr);
                }
                writableDatabase.setTransactionSuccessful();
                if (writableDatabase.inTransaction()) {
                    writableDatabase.endTransaction();
                }
            } catch (Throwable th) {
                if (sQLiteDatabase.inTransaction()) {
                    sQLiteDatabase.endTransaction();
                }
                throw th;
            }
        }
        return i;
    }
}
