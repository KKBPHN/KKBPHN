package defpackage;

import java.util.Iterator;

/* renamed from: fk reason: default package */
final class fk implements Iterator {
    final Iterator a = this.b.a.iterator();
    final /* synthetic */ fl b;

    public fk(fl flVar) {
        this.b = flVar;
    }

    public final boolean hasNext() {
        return this.a.hasNext();
    }

    public final /* bridge */ /* synthetic */ Object next() {
        return (String) this.a.next();
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
