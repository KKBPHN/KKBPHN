package com.xiaomi.stat;

import com.xiaomi.stat.a.l;

class m implements Runnable {
    final /* synthetic */ HttpEvent a;
    final /* synthetic */ e b;

    m(e eVar, HttpEvent httpEvent) {
        this.b = eVar;
        this.a = httpEvent;
    }

    public void run() {
        if (b.a() && this.b.g(false)) {
            e eVar = this.b;
            eVar.a(l.a(this.a, eVar.b));
        }
    }
}
