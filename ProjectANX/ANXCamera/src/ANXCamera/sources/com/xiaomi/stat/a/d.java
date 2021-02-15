package com.xiaomi.stat.a;

import com.xiaomi.stat.d.k;

class d implements Runnable {
    final /* synthetic */ l a;
    final /* synthetic */ c b;

    d(c cVar, l lVar) {
        this.b = cVar;
        this.a = lVar;
    }

    public void run() {
        try {
            this.b.b(this.a);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("addEvent exception: ");
            sb.append(e.toString());
            k.e("EventManager", sb.toString());
        }
    }
}
