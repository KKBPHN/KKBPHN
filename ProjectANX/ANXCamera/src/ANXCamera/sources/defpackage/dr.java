package defpackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/* renamed from: dr reason: default package */
public final class dr extends bw implements RandomAccess, ds {
    private static final dr b = new dr(10);
    private final List c;

    static {
        b.b();
    }

    public dr() {
        this(10);
    }

    public dr(int i) {
        this(new ArrayList(i));
    }

    private dr(ArrayList arrayList) {
        this.c = arrayList;
    }

    private static String O00000oO(Object obj) {
        return obj instanceof String ? (String) obj : obj instanceof ck ? ((ck) obj).e() : dj.b((byte[]) obj);
    }

    public final /* bridge */ /* synthetic */ di a(int i) {
        if (i >= size()) {
            ArrayList arrayList = new ArrayList(i);
            arrayList.addAll(this.c);
            return new dr(arrayList);
        }
        throw new IllegalArgumentException();
    }

    public final /* bridge */ /* synthetic */ void add(int i, Object obj) {
        String str = (String) obj;
        c();
        this.c.add(i, str);
        this.modCount++;
    }

    public final boolean addAll(int i, Collection collection) {
        c();
        if (collection instanceof ds) {
            collection = ((ds) collection).d();
        }
        boolean addAll = this.c.addAll(i, collection);
        this.modCount++;
        return addAll;
    }

    public final boolean addAll(Collection collection) {
        return addAll(size(), collection);
    }

    /* renamed from: b */
    public final String get(int i) {
        Object obj = this.c.get(i);
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof ck) {
            ck ckVar = (ck) obj;
            String e = ckVar.e();
            if (ckVar.c()) {
                this.c.set(i, e);
            }
            return e;
        }
        byte[] bArr = (byte[]) obj;
        String b2 = dj.b(bArr);
        if (dj.a(bArr)) {
            this.c.set(i, b2);
        }
        return b2;
    }

    public final Object c(int i) {
        return this.c.get(i);
    }

    public final void clear() {
        c();
        this.c.clear();
        this.modCount++;
    }

    public final List d() {
        return Collections.unmodifiableList(this.c);
    }

    public final ds e() {
        return this.a ? new fl(this) : this;
    }

    public final /* bridge */ /* synthetic */ Object remove(int i) {
        c();
        Object remove = this.c.remove(i);
        this.modCount++;
        return O00000oO(remove);
    }

    public final /* bridge */ /* synthetic */ Object set(int i, Object obj) {
        String str = (String) obj;
        c();
        return O00000oO(this.c.set(i, str));
    }

    public final int size() {
        return this.c.size();
    }
}
