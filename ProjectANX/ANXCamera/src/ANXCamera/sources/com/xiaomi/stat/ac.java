package com.xiaomi.stat;

import android.database.Cursor;
import java.util.concurrent.Callable;

class ac implements Callable {
    final /* synthetic */ ab a;

    ac(ab abVar) {
        this.a = abVar;
    }

    /* renamed from: a */
    public Cursor call() {
        try {
            return this.a.g.getWritableDatabase().query(a.b, null, null, null, null, null, null);
        } catch (Exception unused) {
            return null;
        }
    }
}
