package defpackage;

import java.util.Iterator;
import java.util.Map.Entry;

/* renamed from: fa reason: default package */
final class fa implements Iterator {
    final /* synthetic */ fc a;
    private int b = -1;
    private boolean c;
    private Iterator d;

    public fa(fc fcVar) {
        this.a = fcVar;
    }

    private final Iterator a() {
        if (this.d == null) {
            this.d = this.a.b.entrySet().iterator();
        }
        return this.d;
    }

    public final boolean hasNext() {
        boolean z = true;
        if (this.b + 1 >= this.a.a.size()) {
            if (this.a.b.isEmpty()) {
                z = false;
            } else if (a().hasNext()) {
                return z;
            } else {
                return false;
            }
        }
        return z;
    }

    public final /* bridge */ /* synthetic */ Object next() {
        this.c = true;
        int i = this.b + 1;
        this.b = i;
        return (Entry) (i >= this.a.a.size() ? a().next() : this.a.a.get(this.b));
    }

    public final void remove() {
        if (this.c) {
            this.c = false;
            this.a.c();
            if (this.b >= this.a.a.size()) {
                a().remove();
                return;
            }
            fc fcVar = this.a;
            int i = this.b;
            this.b = i - 1;
            fcVar.c(i);
            return;
        }
        throw new IllegalStateException("remove() was called before next()");
    }
}
