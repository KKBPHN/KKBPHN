package com.xiaomi.stat;

import com.xiaomi.stat.a.c;
import com.xiaomi.stat.b.g;
import com.xiaomi.stat.d.e;

class f implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ boolean b;
    final /* synthetic */ e c;

    f(e eVar, String str, boolean z) {
        this.c = eVar;
        this.a = str;
        this.b = z;
    }

    public void run() {
        e.a();
        if (this.c.a) {
            b.h(this.a);
        }
        b.d();
        g.a().a(b.f());
        b.a(this.c.c, this.b);
        b.n();
        if (!this.c.a) {
            b.f(this.c.b);
        }
        this.c.f();
        c.a().b();
        com.xiaomi.stat.b.e.a().execute(new g(this));
    }
}
