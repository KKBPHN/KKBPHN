package defpackage;

import sun.misc.Unsafe;

/* renamed from: fo reason: default package */
final class fo extends fq {
    public fo(Unsafe unsafe) {
        super(unsafe);
    }

    public final byte O000000o(Object obj, long j) {
        return fr.d ? fr.O0000O0o(obj, j) : fr.O0000OOo(obj, j);
    }

    public final void O000000o(Object obj, long j, byte b) {
        if (fr.d) {
            fr.O000000o(obj, j, b);
        } else {
            fr.O00000Oo(obj, j, b);
        }
    }

    public final void O000000o(Object obj, long j, double d) {
        O000000o(obj, j, Double.doubleToLongBits(d));
    }

    public final void O000000o(Object obj, long j, float f) {
        O000000o(obj, j, Float.floatToIntBits(f));
    }

    public final void O000000o(Object obj, long j, boolean z) {
        if (fr.d) {
            fr.O00000Oo(obj, j, z);
        } else {
            fr.O00000o0(obj, j, z);
        }
    }

    public final boolean O00000Oo(Object obj, long j) {
        return fr.d ? fr.O0000Oo0(obj, j) : fr.O0000Oo(obj, j);
    }

    public final double O00000o(Object obj, long j) {
        return Double.longBitsToDouble(O00000oo(obj, j));
    }

    public final float O00000o0(Object obj, long j) {
        return Float.intBitsToFloat(O00000oO(obj, j));
    }
}
