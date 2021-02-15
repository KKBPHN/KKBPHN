package com.xiaomi.stat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.xiaomi.stat.d.k;

class ae implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ ab c;

    ae(ab abVar, String str, String str2) {
        this.c = abVar;
        this.a = str;
        this.b = str2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x008e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        Cursor cursor = null;
        try {
            SQLiteDatabase writableDatabase = this.c.g.getWritableDatabase();
            boolean isEmpty = TextUtils.isEmpty(this.a);
            String str = "pref_key=?";
            String str2 = a.b;
            if (isEmpty) {
                writableDatabase.delete(str2, str, new String[]{this.b});
                return;
            }
            Cursor query = writableDatabase.query(a.b, null, "pref_key=?", new String[]{this.b}, null, null, null);
            try {
                boolean z = query.getCount() <= 0;
                ContentValues contentValues = new ContentValues();
                contentValues.put(a.c, this.b);
                contentValues.put(a.d, this.a);
                if (z) {
                    writableDatabase.insert(str2, null, contentValues);
                } else {
                    writableDatabase.update(str2, contentValues, str, new String[]{this.b});
                }
                if (query != null) {
                    query.close();
                }
            } catch (Exception e) {
                e = e;
                cursor = query;
                String str3 = "MiStatPref";
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("update pref db failed with ");
                    sb.append(e);
                    k.c(str3, sb.toString());
                    if (cursor != null) {
                    }
                } catch (Throwable th) {
                    th = th;
                    if (cursor != null) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                cursor = query;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            String str32 = "MiStatPref";
            StringBuilder sb2 = new StringBuilder();
            sb2.append("update pref db failed with ");
            sb2.append(e);
            k.c(str32, sb2.toString());
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
