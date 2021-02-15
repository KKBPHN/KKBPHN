package com.xiaomi.stat.a;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.k;

class h implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ c b;

    h(c cVar, String str) {
        this.b = cVar;
        this.a = str;
    }

    public void run() {
        String str;
        String[] strArr;
        try {
            SQLiteDatabase writableDatabase = this.b.l.getWritableDatabase();
            if (TextUtils.equals(this.a, ak.b())) {
                str = "sub is null";
                strArr = null;
            } else {
                String[] strArr2 = {this.a};
                str = "sub = ?";
                strArr = strArr2;
            }
            writableDatabase.delete(j.b, str, strArr);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("removeAllEventsForApp exception: ");
            sb.append(e.toString());
            k.b("EventManager", sb.toString());
        }
    }
}
