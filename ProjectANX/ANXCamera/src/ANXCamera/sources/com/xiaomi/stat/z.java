package com.xiaomi.stat;

import com.xiaomi.stat.a.l;

class z implements Runnable {
    final /* synthetic */ Throwable a;
    final /* synthetic */ String b;
    final /* synthetic */ boolean c;
    final /* synthetic */ e d;

    z(e eVar, Throwable th, String str, boolean z) {
        this.d = eVar;
        this.a = th;
        this.b = str;
        this.c = z;
    }

    public void run() {
        if (b.a() && this.d.g(false)) {
            e eVar = this.d;
            eVar.a(l.a(this.a, this.b, this.c, eVar.b));
        }
    }
}
