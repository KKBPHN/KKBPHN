package defpackage;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;

/* renamed from: fb reason: default package */
final class fb extends AbstractSet {
    final /* synthetic */ fc a;

    public fb(fc fcVar) {
        this.a = fcVar;
    }

    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        Entry entry = (Entry) obj;
        if (contains(entry)) {
            return false;
        }
        this.a.put((Comparable) entry.getKey(), entry.getValue());
        return true;
    }

    public final void clear() {
        this.a.clear();
    }

    public final boolean contains(Object obj) {
        Entry entry = (Entry) obj;
        Object obj2 = this.a.get(entry.getKey());
        Object value = entry.getValue();
        boolean z = true;
        if (obj2 != value) {
            if (obj2 == null) {
                z = false;
            } else if (obj2.equals(value)) {
                return z;
            } else {
                return false;
            }
        }
        return z;
    }

    public Iterator iterator() {
        return new fa(this.a);
    }

    public final boolean remove(Object obj) {
        Entry entry = (Entry) obj;
        if (!contains(entry)) {
            return false;
        }
        this.a.remove(entry.getKey());
        return true;
    }

    public final int size() {
        return this.a.size();
    }
}
