package defpackage;

/* renamed from: dm reason: default package */
public enum dm {
    VOID(Void.class),
    INT(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    BOOLEAN(Boolean.class),
    STRING(String.class),
    BYTE_STRING(ck.class),
    ENUM(Integer.class),
    MESSAGE(Object.class);
    
    private final Class k;

    private dm(Class cls) {
        this.k = cls;
    }
}
