package com.xiaomi.stat;

import com.xiaomi.stat.a.l;

class y implements Runnable {
    final /* synthetic */ boolean a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ MiStatParams d;
    final /* synthetic */ e e;

    y(e eVar, boolean z, String str, String str2, MiStatParams miStatParams) {
        this.e = eVar;
        this.a = z;
        this.b = str;
        this.c = str2;
        this.d = miStatParams;
    }

    public void run() {
        if (b.a() && this.e.g(this.a) && b.A()) {
            e eVar = this.e;
            eVar.a(l.a(this.b, this.c, this.d, eVar.b, this.a));
        }
    }
}
