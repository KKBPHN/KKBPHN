package defpackage;

/* renamed from: dc reason: default package */
public abstract class dc extends de implements ei {
    public cu d = cu.c;

    public final void O00000Oo(cq cqVar) {
        if (cqVar.a != ((de) b(6))) {
            throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
        }
    }

    /* access modifiers changed from: 0000 */
    public final cu d() {
        cu cuVar = this.d;
        if (cuVar.b) {
            this.d = cuVar.clone();
        }
        return this.d;
    }
}
