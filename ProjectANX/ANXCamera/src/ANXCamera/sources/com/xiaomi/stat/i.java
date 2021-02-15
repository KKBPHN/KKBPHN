package com.xiaomi.stat;

import com.xiaomi.stat.a.c;

class i implements Runnable {
    final /* synthetic */ boolean a;
    final /* synthetic */ e b;

    i(e eVar, boolean z) {
        this.b = eVar;
        this.a = z;
    }

    public void run() {
        if (b.d(this.b.c)) {
            int i = 2;
            if (!this.a && b.e(this.b.c) != 2) {
                c.a().a(this.b.c);
            }
            String b2 = this.b.c;
            if (this.a) {
                i = 1;
            }
            b.a(b2, i);
        }
    }
}
