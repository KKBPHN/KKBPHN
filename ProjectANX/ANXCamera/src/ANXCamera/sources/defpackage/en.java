package defpackage;

/* renamed from: en reason: default package */
final class en {
    public static final fs a;
    public static final fs b = new fs();

    static {
        fs fsVar;
        try {
            fsVar = (fs) Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            fsVar = null;
        }
        a = fsVar;
    }
}
