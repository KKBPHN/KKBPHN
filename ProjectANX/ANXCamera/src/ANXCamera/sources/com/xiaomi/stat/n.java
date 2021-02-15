package com.xiaomi.stat;

import com.xiaomi.stat.a.l;

class n implements Runnable {
    final /* synthetic */ NetAvailableEvent a;
    final /* synthetic */ e b;

    n(e eVar, NetAvailableEvent netAvailableEvent) {
        this.b = eVar;
        this.a = netAvailableEvent;
    }

    public void run() {
        if (b.a() && this.b.g(false) && b.y()) {
            e eVar = this.b;
            eVar.a(l.a(this.a, eVar.b));
        }
    }
}
