package defpackage;

/* renamed from: bx reason: default package */
final class bx {
    public static final Class a = a("libcore.io.Memory");
    private static final boolean b = (a("org.robolectric.Robolectric") != null);

    private static Class a(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    static boolean a() {
        return a != null && !b;
    }
}
