package defpackage;

/* renamed from: fh reason: default package */
final class fh {
    public fh() {
    }

    public fh(byte[] bArr) {
        this();
    }

    public static void O000000o(Object obj, fi fiVar) {
        ((de) obj).h = fiVar;
    }

    public static /* bridge */ /* synthetic */ Object O00000o0(Object obj, Object obj2) {
        fi fiVar = (fi) obj;
        fi fiVar2 = (fi) obj2;
        return !fiVar2.equals(fi.a) ? fi.O000000o(fiVar, fiVar2) : fiVar;
    }

    public static fi O0000O0o(Object obj) {
        return ((de) obj).h;
    }

    public /* bridge */ /* synthetic */ void O000000o(Object obj, Object obj2) {
        O000000o(obj, (fi) obj2);
    }

    public /* bridge */ /* synthetic */ Object O00000Oo(Object obj) {
        return O0000O0o(obj);
    }

    public /* bridge */ /* synthetic */ void O00000Oo(Object obj, Object obj2) {
        O000000o(obj, (fi) obj2);
    }

    public void O00000oO(Object obj) {
        O0000O0o(obj).e = false;
    }
}
