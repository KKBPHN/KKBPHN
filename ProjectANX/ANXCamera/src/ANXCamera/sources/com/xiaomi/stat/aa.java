package com.xiaomi.stat;

import com.xiaomi.stat.a.l;

class aa implements Runnable {
    final /* synthetic */ boolean a;
    final /* synthetic */ MiStatParams b;
    final /* synthetic */ e c;

    aa(e eVar, boolean z, MiStatParams miStatParams) {
        this.c = eVar;
        this.a = z;
        this.b = miStatParams;
    }

    public void run() {
        if (b.a() && this.c.g(this.a)) {
            e eVar = this.c;
            eVar.a(l.a(this.b, this.a, eVar.b));
        }
    }
}
