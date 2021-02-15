package com.xiaomi.stat;

import android.os.FileObserver;

class ad extends FileObserver {
    final /* synthetic */ ab a;

    ad(ab abVar, String str) {
        this.a = abVar;
        super(str);
    }

    public void onEvent(int i, String str) {
        if (i == 2) {
            synchronized (this.a) {
                this.a.b();
            }
            b.n();
        }
    }
}
