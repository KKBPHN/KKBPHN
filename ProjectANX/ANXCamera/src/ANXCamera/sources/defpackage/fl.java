package defpackage;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/* renamed from: fl reason: default package */
public final class fl extends AbstractList implements RandomAccess, ds {
    public final ds a;

    public fl(ds dsVar) {
        this.a = dsVar;
    }

    public final Object c(int i) {
        return this.a.c(i);
    }

    public final List d() {
        return this.a.d();
    }

    public final ds e() {
        return this;
    }

    public final /* bridge */ /* synthetic */ Object get(int i) {
        return ((dr) this.a).get(i);
    }

    public final Iterator iterator() {
        return new fk(this);
    }

    public final ListIterator listIterator(int i) {
        return new fj(this, i);
    }

    public final int size() {
        return this.a.size();
    }
}
