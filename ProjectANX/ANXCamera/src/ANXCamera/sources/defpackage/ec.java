package defpackage;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/* renamed from: ec reason: default package */
public final class ec extends LinkedHashMap {
    public static final ec b = new ec();
    public boolean a = true;

    static {
        b.b();
    }

    private ec() {
    }

    private ec(Map map) {
        super(map);
    }

    private static int O00000oO(Object obj) {
        if (obj instanceof byte[]) {
            return dj.c((byte[]) obj);
        }
        if (!(obj instanceof dg)) {
            return obj.hashCode();
        }
        throw new UnsupportedOperationException();
    }

    public final ec a() {
        return !isEmpty() ? new ec(this) : new ec();
    }

    public final void b() {
        this.a = false;
    }

    public final void c() {
        if (!this.a) {
            throw new UnsupportedOperationException();
        }
    }

    public final void clear() {
        c();
        super.clear();
    }

    public final Set entrySet() {
        return !isEmpty() ? super.entrySet() : Collections.emptySet();
    }

    public final boolean equals(Object obj) {
        boolean z;
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (this != map) {
                if (size() == map.size()) {
                    for (Entry entry : entrySet()) {
                        if (map.containsKey(entry.getKey())) {
                            Object value = entry.getValue();
                            Object obj2 = map.get(entry.getKey());
                            if (!(value instanceof byte[]) || !(obj2 instanceof byte[])) {
                                z = value.equals(obj2);
                                continue;
                            } else {
                                z = Arrays.equals((byte[]) value, (byte[]) obj2);
                                continue;
                            }
                            if (!z) {
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        for (Entry entry : entrySet()) {
            i += O00000oO(entry.getValue()) ^ O00000oO(entry.getKey());
        }
        return i;
    }

    public final Object put(Object obj, Object obj2) {
        c();
        dj.O00000oO(obj);
        dj.O00000oO(obj2);
        return super.put(obj, obj2);
    }

    public final void putAll(Map map) {
        c();
        for (Object next : map.keySet()) {
            dj.O00000oO(next);
            dj.O00000oO(map.get(next));
        }
        super.putAll(map);
    }

    public final Object remove(Object obj) {
        c();
        return super.remove(obj);
    }
}
