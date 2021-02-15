package com.xiaomi.stat;

import com.xiaomi.stat.a.l;

class j implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ int b;
    final /* synthetic */ e c;

    j(e eVar, int i, int i2) {
        this.c = eVar;
        this.a = i;
        this.b = i2;
    }

    public void run() {
        if (b.a() && this.c.g()) {
            b.e(this.a);
            this.c.a(l.a(this.b));
        }
    }
}
