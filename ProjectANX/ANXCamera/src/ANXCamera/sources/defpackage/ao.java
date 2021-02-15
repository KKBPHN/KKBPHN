package defpackage;

/* renamed from: ao reason: default package */
public final /* synthetic */ class ao implements ap {
    private final aq a;
    private final ap b;

    public ao(aq aqVar, ap apVar) {
        this.a = aqVar;
        this.b = apVar;
    }

    public final void a(int i) {
        int i2;
        aq aqVar = this.a;
        ap apVar = this.b;
        eb.c();
        if (!aqVar.a.c()) {
            i2 = aqVar.a.e();
        } else {
            z c = aqVar.c();
            i2 = ((c.a & 1) == 0 || aqVar.a.b() < c.b) ? bh.l : bh.b;
        }
        apVar.a(i2);
    }
}
