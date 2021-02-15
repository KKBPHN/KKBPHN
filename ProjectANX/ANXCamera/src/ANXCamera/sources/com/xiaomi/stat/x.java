package com.xiaomi.stat;

import com.xiaomi.stat.a.l;

class x implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ long b;
    final /* synthetic */ long c;
    final /* synthetic */ MiStatParams d;
    final /* synthetic */ e e;

    x(e eVar, String str, long j, long j2, MiStatParams miStatParams) {
        this.e = eVar;
        this.a = str;
        this.b = j;
        this.c = j2;
        this.d = miStatParams;
    }

    public void run() {
        if (b.a() && this.e.g(false) && b.z()) {
            e eVar = this.e;
            String str = this.a;
            long j = this.b;
            eVar.a(l.a(str, j - this.c, j, this.d, eVar.b));
        }
    }
}
