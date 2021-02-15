package defpackage;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/* renamed from: fc reason: default package */
class fc extends AbstractMap {
    public List a = Collections.emptyList();
    public Map b = Collections.emptyMap();
    public boolean c;
    public Map d = Collections.emptyMap();
    private final int e;
    private volatile fb f;

    public fc(int i) {
        this.e = i;
    }

    private final int O000000o(Comparable comparable) {
        int size = this.a.size() - 1;
        int i = 0;
        if (size >= 0) {
            int compareTo = comparable.compareTo(((ez) this.a.get(size)).a);
            if (compareTo > 0) {
                return -(size + 2);
            }
            if (compareTo == 0) {
                return size;
            }
        }
        while (i <= size) {
            int i2 = (i + size) / 2;
            int compareTo2 = comparable.compareTo(((ez) this.a.get(i2)).a);
            if (compareTo2 < 0) {
                size = i2 - 1;
            } else if (compareTo2 <= 0) {
                return i2;
            } else {
                i = i2 + 1;
            }
        }
        return -(i + 1);
    }

    static fc a(int i) {
        return new ev(i);
    }

    private final SortedMap d() {
        c();
        if (this.b.isEmpty() && !(this.b instanceof TreeMap)) {
            this.b = new TreeMap();
            this.d = ((TreeMap) this.b).descendingMap();
        }
        return (SortedMap) this.b;
    }

    /* renamed from: O000000o */
    public final Object put(Comparable comparable, Object obj) {
        c();
        int O000000o2 = O000000o(comparable);
        if (O000000o2 >= 0) {
            return ((ez) this.a.get(O000000o2)).setValue(obj);
        }
        c();
        if (this.a.isEmpty() && !(this.a instanceof ArrayList)) {
            this.a = new ArrayList(this.e);
        }
        int i = -(O000000o2 + 1);
        if (i >= this.e) {
            return d().put(comparable, obj);
        }
        int size = this.a.size();
        int i2 = this.e;
        if (size == i2) {
            ez ezVar = (ez) this.a.remove(i2 - 1);
            d().put(ezVar.a, ezVar.b);
        }
        this.a.add(i, new ez(this, comparable, obj));
        return null;
    }

    public final int a() {
        return this.a.size();
    }

    public final Iterable b() {
        return !this.b.isEmpty() ? this.b.entrySet() : ey.b;
    }

    public final Entry b(int i) {
        return (Entry) this.a.get(i);
    }

    public final Object c(int i) {
        c();
        Object obj = ((ez) this.a.remove(i)).b;
        if (!this.b.isEmpty()) {
            Iterator it = d().entrySet().iterator();
            Entry entry = (Entry) it.next();
            this.a.add(new ez(this, (Comparable) entry.getKey(), entry.getValue()));
            it.remove();
        }
        return obj;
    }

    public final void c() {
        if (this.c) {
            throw new UnsupportedOperationException();
        }
    }

    public final void clear() {
        c();
        if (!this.a.isEmpty()) {
            this.a.clear();
        }
        if (!this.b.isEmpty()) {
            this.b.clear();
        }
    }

    public final boolean containsKey(Object obj) {
        Comparable comparable = (Comparable) obj;
        return O000000o(comparable) >= 0 || this.b.containsKey(comparable);
    }

    public final Set entrySet() {
        if (this.f == null) {
            this.f = new fb(this);
        }
        return this.f;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof fc)) {
            return super.equals(obj);
        }
        fc fcVar = (fc) obj;
        int size = size();
        if (size != fcVar.size()) {
            return false;
        }
        int a2 = a();
        if (a2 != fcVar.a()) {
            return entrySet().equals(fcVar.entrySet());
        }
        for (int i = 0; i < a2; i++) {
            if (!b(i).equals(fcVar.b(i))) {
                return false;
            }
        }
        if (a2 != size) {
            return this.b.equals(fcVar.b);
        }
        return true;
    }

    public final Object get(Object obj) {
        Comparable comparable = (Comparable) obj;
        int O000000o2 = O000000o(comparable);
        return O000000o2 >= 0 ? ((ez) this.a.get(O000000o2)).b : this.b.get(comparable);
    }

    public final int hashCode() {
        int i = 0;
        for (int i2 = 0; i2 < a(); i2++) {
            i += ((ez) this.a.get(i2)).hashCode();
        }
        return this.b.size() > 0 ? i + this.b.hashCode() : i;
    }

    public final Object remove(Object obj) {
        c();
        Comparable comparable = (Comparable) obj;
        int O000000o2 = O000000o(comparable);
        if (O000000o2 >= 0) {
            return c(O000000o2);
        }
        if (!this.b.isEmpty()) {
            return this.b.remove(comparable);
        }
        return null;
    }

    public final int size() {
        return this.a.size() + this.b.size();
    }
}
