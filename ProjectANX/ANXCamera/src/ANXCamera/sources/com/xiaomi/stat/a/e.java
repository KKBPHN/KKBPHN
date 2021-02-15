package com.xiaomi.stat.a;

import java.util.concurrent.Callable;

class e implements Callable {
    final /* synthetic */ b[] a;
    final /* synthetic */ c b;

    e(c cVar, b[] bVarArr) {
        this.b = cVar;
        this.a = bVarArr;
    }

    /* renamed from: a */
    public k call() {
        return this.b.b(this.a);
    }
}
