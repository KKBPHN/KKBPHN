package defpackage;

/* renamed from: cw reason: default package */
public enum cw {
    DOUBLE(0, cv.SCALAR, dm.DOUBLE),
    FLOAT(1, cv.SCALAR, dm.FLOAT),
    INT64(2, cv.SCALAR, dm.LONG),
    UINT64(3, cv.SCALAR, dm.LONG),
    INT32(4, cv.SCALAR, dm.INT),
    FIXED64(5, cv.SCALAR, dm.LONG),
    FIXED32(6, cv.SCALAR, dm.INT),
    BOOL(7, cv.SCALAR, dm.BOOLEAN),
    STRING(8, cv.SCALAR, dm.STRING),
    MESSAGE(9, cv.SCALAR, dm.MESSAGE),
    BYTES(10, cv.SCALAR, dm.BYTE_STRING),
    UINT32(11, cv.SCALAR, dm.INT),
    ENUM(12, cv.SCALAR, dm.ENUM),
    SFIXED32(13, cv.SCALAR, dm.INT),
    SFIXED64(14, cv.SCALAR, dm.LONG),
    SINT32(15, cv.SCALAR, dm.INT),
    SINT64(16, cv.SCALAR, dm.LONG),
    GROUP(17, cv.SCALAR, dm.MESSAGE),
    DOUBLE_LIST(18, cv.VECTOR, dm.DOUBLE),
    FLOAT_LIST(19, cv.VECTOR, dm.FLOAT),
    INT64_LIST(20, cv.VECTOR, dm.LONG),
    UINT64_LIST(21, cv.VECTOR, dm.LONG),
    INT32_LIST(22, cv.VECTOR, dm.INT),
    FIXED64_LIST(23, cv.VECTOR, dm.LONG),
    FIXED32_LIST(24, cv.VECTOR, dm.INT),
    BOOL_LIST(25, cv.VECTOR, dm.BOOLEAN),
    STRING_LIST(26, cv.VECTOR, dm.STRING),
    MESSAGE_LIST(27, cv.VECTOR, dm.MESSAGE),
    BYTES_LIST(28, cv.VECTOR, dm.BYTE_STRING),
    UINT32_LIST(29, cv.VECTOR, dm.INT),
    ENUM_LIST(30, cv.VECTOR, dm.ENUM),
    SFIXED32_LIST(31, cv.VECTOR, dm.INT),
    SFIXED64_LIST(32, cv.VECTOR, dm.LONG),
    SINT32_LIST(33, cv.VECTOR, dm.INT),
    SINT64_LIST(34, cv.VECTOR, dm.LONG),
    DOUBLE_LIST_PACKED(35, cv.PACKED_VECTOR, dm.DOUBLE),
    FLOAT_LIST_PACKED(36, cv.PACKED_VECTOR, dm.FLOAT),
    INT64_LIST_PACKED(37, cv.PACKED_VECTOR, dm.LONG),
    UINT64_LIST_PACKED(38, cv.PACKED_VECTOR, dm.LONG),
    INT32_LIST_PACKED(39, cv.PACKED_VECTOR, dm.INT),
    FIXED64_LIST_PACKED(40, cv.PACKED_VECTOR, dm.LONG),
    FIXED32_LIST_PACKED(41, cv.PACKED_VECTOR, dm.INT),
    BOOL_LIST_PACKED(42, cv.PACKED_VECTOR, dm.BOOLEAN),
    UINT32_LIST_PACKED(43, cv.PACKED_VECTOR, dm.INT),
    ENUM_LIST_PACKED(44, cv.PACKED_VECTOR, dm.ENUM),
    SFIXED32_LIST_PACKED(45, cv.PACKED_VECTOR, dm.INT),
    SFIXED64_LIST_PACKED(46, cv.PACKED_VECTOR, dm.LONG),
    SINT32_LIST_PACKED(47, cv.PACKED_VECTOR, dm.INT),
    SINT64_LIST_PACKED(48, cv.PACKED_VECTOR, dm.LONG),
    GROUP_LIST(49, cv.VECTOR, dm.MESSAGE),
    MAP(50, cv.MAP, dm.VOID);
    
    private static final cw[] ab = null;
    public final int Z;
    private final cv aa;

    static {
        int i;
        cw[] values;
        ab = new cw[r1];
        for (cw cwVar : values()) {
            ab[cwVar.Z] = cwVar;
        }
    }

    private cw(int i, cv cvVar, dm dmVar) {
        this.Z = i;
        this.aa = cvVar;
        dm dmVar2 = dm.VOID;
        cv cvVar2 = cv.SCALAR;
        cvVar.ordinal();
        if (cvVar == cv.SCALAR) {
            dmVar.ordinal();
        }
    }
}
