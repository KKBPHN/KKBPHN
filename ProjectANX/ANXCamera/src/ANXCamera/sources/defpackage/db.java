package defpackage;

/* renamed from: db reason: default package */
public class db extends da implements ei {
    protected db(dc dcVar) {
        super(dcVar);
    }

    public final void O000000o(cq cqVar, Object obj) {
        de.O000000o(cqVar);
        if (cqVar.a == this.a) {
            if (this.c) {
                b();
                this.c = false;
            }
            cu cuVar = ((dc) this.b).d;
            if (cuVar.b) {
                cuVar = cuVar.clone();
                ((dc) this.b).d = cuVar;
            }
            cuVar.O000000o(cqVar.d, cqVar.O0000O0o(obj));
            return;
        }
        throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
    }

    public final void b() {
        super.b();
        dc dcVar = (dc) this.b;
        dcVar.d = dcVar.d.clone();
    }

    /* renamed from: j */
    public final dc g() {
        de deVar;
        if (!this.c) {
            ((dc) this.b).d.b();
            deVar = super.g();
        } else {
            deVar = this.b;
        }
        return (dc) deVar;
    }
}
