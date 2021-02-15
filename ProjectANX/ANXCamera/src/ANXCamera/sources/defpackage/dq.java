package defpackage;

/* renamed from: dq reason: default package */
public class dq {
    protected volatile eh a;
    public volatile ck b;

    static {
        cs.a();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:6|7|8|9) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0010 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final eh O000000o(eh ehVar) {
        if (this.a == null) {
            synchronized (this) {
                if (this.a == null) {
                    this.a = ehVar;
                    this.b = ck.b;
                    this.a = ehVar;
                    this.b = ck.b;
                }
            }
        }
        return this.a;
    }

    public final ck a() {
        if (this.b != null) {
            return this.b;
        }
        synchronized (this) {
            if (this.b != null) {
                ck ckVar = this.b;
                return ckVar;
            }
            this.b = this.a != null ? this.a.a() : ck.b;
            ck ckVar2 = this.b;
            return ckVar2;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof dq)) {
            return false;
        }
        dq dqVar = (dq) obj;
        eh ehVar = this.a;
        eh ehVar2 = dqVar.a;
        return (ehVar == null && ehVar2 == null) ? a().equals(dqVar.a()) : (ehVar == null || ehVar2 == null) ? ehVar == null ? O000000o(ehVar2.i()).equals(ehVar2) : ehVar.equals(dqVar.O000000o(ehVar.i())) : ehVar.equals(ehVar2);
    }

    public int hashCode() {
        return 1;
    }
}
