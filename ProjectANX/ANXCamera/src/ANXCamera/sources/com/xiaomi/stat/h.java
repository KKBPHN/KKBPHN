package com.xiaomi.stat;

import android.text.TextUtils;
import com.xiaomi.stat.a.l;

class h implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ e b;

    h(e eVar, String str) {
        this.b = eVar;
        this.a = str;
    }

    public void run() {
        if (b.a() && !TextUtils.equals(b.h(), this.a)) {
            b.b(this.a);
            if (this.b.g()) {
                this.b.a(l.a(this.a));
            }
        }
    }
}
