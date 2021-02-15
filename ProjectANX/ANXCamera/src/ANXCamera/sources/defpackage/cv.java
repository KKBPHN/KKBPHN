package defpackage;

/* renamed from: cv reason: default package */
enum cv {
    SCALAR(false),
    VECTOR(true),
    PACKED_VECTOR(true),
    MAP(false);
    
    public final boolean e;

    private cv(boolean z) {
        this.e = z;
    }
}
