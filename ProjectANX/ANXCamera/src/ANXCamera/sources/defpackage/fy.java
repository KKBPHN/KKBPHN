package defpackage;

/* renamed from: fy reason: default package */
public enum fy {
    DOUBLE(fz.DOUBLE, 1),
    FLOAT(fz.FLOAT, 5),
    INT64(fz.LONG, 0),
    UINT64(fz.LONG, 0),
    INT32(fz.INT, 0),
    FIXED64(fz.LONG, 1),
    FIXED32(fz.INT, 5),
    BOOL(fz.BOOLEAN, 0),
    STRING(fz.STRING, 2),
    GROUP(fz.MESSAGE, 3),
    MESSAGE(fz.MESSAGE, 2),
    BYTES(fz.BYTE_STRING, 2),
    UINT32(fz.INT, 0),
    ENUM(fz.ENUM, 0),
    SFIXED32(fz.INT, 5),
    SFIXED64(fz.LONG, 1),
    SINT32(fz.INT, 0),
    SINT64(fz.LONG, 0);
    
    public final fz s;
    private final int t;

    private fy(fz fzVar, int i) {
        this.s = fzVar;
        this.t = i;
    }
}
