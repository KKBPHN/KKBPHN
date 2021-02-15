package defpackage;

import java.util.Iterator;
import java.util.Map.Entry;

/* renamed from: do reason: invalid class name and default package */
final class Cdo implements Iterator {
    private final Iterator a;

    public Cdo(Iterator it) {
        this.a = it;
    }

    public final boolean hasNext() {
        return this.a.hasNext();
    }

    public final /* bridge */ /* synthetic */ Object next() {
        Entry entry = (Entry) this.a.next();
        return entry.getValue() instanceof dp ? new dn(entry) : entry;
    }

    public final void remove() {
        this.a.remove();
    }
}
