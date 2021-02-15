package defpackage;

/* renamed from: cz reason: default package */
final class cz implements ef {
    public static final cz a = new cz();

    private cz() {
    }

    public final ee O000000o(Class cls) {
        if (!de.class.isAssignableFrom(cls)) {
            String valueOf = String.valueOf(cls.getName());
            String str = "Unsupported message type: ";
            throw new IllegalArgumentException(valueOf.length() == 0 ? new String(str) : str.concat(valueOf));
        }
        try {
            Class asSubclass = cls.asSubclass(de.class);
            de deVar = (de) de.j.get(asSubclass);
            if (deVar == null) {
                Class.forName(asSubclass.getName(), true, asSubclass.getClassLoader());
                deVar = (de) de.j.get(asSubclass);
            }
            if (deVar == null) {
                deVar = (de) ((de) fr.O00000Oo(asSubclass)).b(6);
                if (deVar != null) {
                    de.j.put(asSubclass, deVar);
                } else {
                    throw new IllegalStateException();
                }
            }
            return (ee) deVar.b(3);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class initialization cannot fail.", e);
        } catch (Exception e2) {
            String valueOf2 = String.valueOf(cls.getName());
            String str2 = "Unable to get message info for ";
            throw new RuntimeException(valueOf2.length() == 0 ? new String(str2) : str2.concat(valueOf2), e2);
        }
    }

    public final boolean O00000Oo(Class cls) {
        return de.class.isAssignableFrom(cls);
    }
}
