package defpackage;

import sun.misc.Unsafe;

/* renamed from: fp reason: default package */
final class fp extends fq {
    public fp(Unsafe unsafe) {
        super(unsafe);
    }

    public final byte O000000o(Object obj, long j) {
        return this.a.getByte(obj, j);
    }

    public final void O000000o(Object obj, long j, byte b) {
        this.a.putByte(obj, j, b);
    }

    public final void O000000o(Object obj, long j, double d) {
        this.a.putDouble(obj, j, d);
    }

    public final void O000000o(Object obj, long j, float f) {
        this.a.putFloat(obj, j, f);
    }

    public final void O000000o(Object obj, long j, boolean z) {
        this.a.putBoolean(obj, j, z);
    }

    public final boolean O00000Oo(Object obj, long j) {
        return this.a.getBoolean(obj, j);
    }

    public final double O00000o(Object obj, long j) {
        return this.a.getDouble(obj, j);
    }

    public final float O00000o0(Object obj, long j) {
        return this.a.getFloat(obj, j);
    }
}
