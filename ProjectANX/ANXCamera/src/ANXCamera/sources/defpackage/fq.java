package defpackage;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/* renamed from: fq reason: default package */
abstract class fq {
    final Unsafe a;

    public fq(Unsafe unsafe) {
        this.a = unsafe;
    }

    public abstract byte O000000o(Object obj, long j);

    public final int O000000o(Class cls) {
        return this.a.arrayIndexScale(cls);
    }

    public abstract void O000000o(Object obj, long j, byte b);

    public abstract void O000000o(Object obj, long j, double d);

    public abstract void O000000o(Object obj, long j, float f);

    public final void O000000o(Object obj, long j, int i) {
        this.a.putInt(obj, j, i);
    }

    public final void O000000o(Object obj, long j, long j2) {
        this.a.putLong(obj, j, j2);
    }

    public final void O000000o(Object obj, long j, Object obj2) {
        this.a.putObject(obj, j, obj2);
    }

    public abstract void O000000o(Object obj, long j, boolean z);

    public final void O000000o(Field field) {
        this.a.objectFieldOffset(field);
    }

    public final int O00000Oo(Class cls) {
        return this.a.arrayBaseOffset(cls);
    }

    public abstract boolean O00000Oo(Object obj, long j);

    public abstract double O00000o(Object obj, long j);

    public abstract float O00000o0(Object obj, long j);

    public final int O00000oO(Object obj, long j) {
        return this.a.getInt(obj, j);
    }

    public final long O00000oo(Object obj, long j) {
        return this.a.getLong(obj, j);
    }

    public final Object O0000O0o(Object obj, long j) {
        return this.a.getObject(obj, j);
    }
}
