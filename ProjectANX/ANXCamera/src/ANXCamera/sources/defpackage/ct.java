package defpackage;

/* renamed from: ct reason: default package */
final class ct {
    public static final ej a = new ej(null);
    private static final ej b;

    static {
        ej ejVar;
        try {
            ejVar = (ej) Class.forName("com.google.protobuf.ExtensionSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            ejVar = null;
        }
        b = ejVar;
    }

    static ej a() {
        ej ejVar = b;
        if (ejVar != null) {
            return ejVar;
        }
        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }
}
