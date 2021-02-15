package com.xiaomi.stat;

import android.text.TextUtils;
import com.xiaomi.stat.b.g;
import com.xiaomi.stat.d.m;

class l implements Runnable {
    final /* synthetic */ boolean a;
    final /* synthetic */ String b;
    final /* synthetic */ e c;

    l(e eVar, boolean z, String str) {
        this.c = eVar;
        this.a = z;
        this.b = str;
    }

    public void run() {
        if (!m.a()) {
            b.c(this.a);
            g.a().a(this.a);
        }
        if (b.e() && !TextUtils.isEmpty(this.b)) {
            b.a(this.b);
            g.a().a(this.b);
        }
    }
}
