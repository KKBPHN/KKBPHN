package defpackage;

import java.util.Map.Entry;

/* renamed from: dn reason: default package */
final class dn implements Entry {
    public Entry a;

    public dn(Entry entry) {
        this.a = entry;
    }

    public final Object getKey() {
        return this.a.getKey();
    }

    public final Object getValue() {
        if (((dp) this.a.getValue()) == null) {
            return null;
        }
        throw null;
    }

    public final Object setValue(Object obj) {
        if (obj instanceof eh) {
            dp dpVar = (dp) this.a.getValue();
            eh ehVar = (eh) obj;
            eh ehVar2 = dpVar.a;
            dpVar.b = null;
            dpVar.a = ehVar;
            return ehVar2;
        }
        throw new IllegalArgumentException("LazyField now only used for MessageSet, and the value of MessageSet must be an instance of MessageLite");
    }
}
