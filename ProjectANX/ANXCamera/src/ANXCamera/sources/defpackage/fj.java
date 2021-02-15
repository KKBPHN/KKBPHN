package defpackage;

import java.util.ListIterator;

/* renamed from: fj reason: default package */
final class fj implements ListIterator {
    final ListIterator a = this.c.a.listIterator(this.b);
    final /* synthetic */ int b;
    final /* synthetic */ fl c;

    public fj(fl flVar, int i) {
        this.c = flVar;
        this.b = i;
    }

    public final /* bridge */ /* synthetic */ void add(Object obj) {
        String str = (String) obj;
        throw new UnsupportedOperationException();
    }

    public final boolean hasNext() {
        return this.a.hasNext();
    }

    public final boolean hasPrevious() {
        return this.a.hasPrevious();
    }

    public final /* bridge */ /* synthetic */ Object next() {
        return (String) this.a.next();
    }

    public final int nextIndex() {
        return this.a.nextIndex();
    }

    public final /* bridge */ /* synthetic */ Object previous() {
        return (String) this.a.previous();
    }

    public final int previousIndex() {
        return this.a.previousIndex();
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }

    public final /* bridge */ /* synthetic */ void set(Object obj) {
        String str = (String) obj;
        throw new UnsupportedOperationException();
    }
}
