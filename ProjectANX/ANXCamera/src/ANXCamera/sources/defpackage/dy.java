package defpackage;

/* renamed from: dy reason: default package */
final class dy implements ef {
    private final ef[] a;

    public dy(ef... efVarArr) {
        this.a = efVarArr;
    }

    public final ee O000000o(Class cls) {
        ef[] efVarArr = this.a;
        for (int i = 0; i < 2; i++) {
            ef efVar = efVarArr[i];
            if (efVar.O00000Oo(cls)) {
                return efVar.O000000o(cls);
            }
        }
        String valueOf = String.valueOf(cls.getName());
        String str = "No factory is available for message type: ";
        throw new UnsupportedOperationException(valueOf.length() == 0 ? new String(str) : str.concat(valueOf));
    }

    public final boolean O00000Oo(Class cls) {
        ef[] efVarArr = this.a;
        for (int i = 0; i < 2; i++) {
            if (efVarArr[i].O00000Oo(cls)) {
                return true;
            }
        }
        return false;
    }
}
