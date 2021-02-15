package defpackage;

/* renamed from: ed reason: default package */
final class ed {
    public static final ff a;
    public static final ff b = new ff();

    static {
        ff ffVar;
        try {
            ffVar = (ff) Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            ffVar = null;
        }
        a = ffVar;
    }
}
