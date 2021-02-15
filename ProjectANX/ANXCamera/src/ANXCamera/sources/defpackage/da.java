package defpackage;

/* renamed from: da reason: default package */
public class da extends bt {
    public final de a;
    public de b;
    public boolean c = false;

    protected da(de deVar) {
        this.a = deVar;
        this.b = (de) deVar.b(4);
    }

    private static final void O000000o(de deVar, de deVar2) {
        ep.a.O00000oO(deVar).O00000Oo(deVar, deVar2);
    }

    /* access modifiers changed from: protected */
    public final /* bridge */ /* synthetic */ bt O000000o(bu buVar) {
        O000000o((de) buVar);
        return this;
    }

    public final void O000000o(de deVar) {
        if (this.c) {
            b();
            this.c = false;
        }
        O000000o(this.b, deVar);
    }

    public void b() {
        de deVar = (de) this.b.b(4);
        O000000o(deVar, this.b);
        this.b = deVar;
    }

    public final boolean c() {
        throw null;
    }

    /* renamed from: d */
    public final da clone() {
        da daVar = (da) this.a.b(5);
        daVar.O000000o(g());
        return daVar;
    }

    /* renamed from: e */
    public de g() {
        if (this.c) {
            return this.b;
        }
        de deVar = this.b;
        ep.a.O00000oO(deVar).O00000o(deVar);
        this.c = true;
        return this.b;
    }

    /* renamed from: f */
    public final de h() {
        de e = g();
        if (e.c()) {
            return e;
        }
        throw new fg();
    }

    public final /* bridge */ /* synthetic */ eh i() {
        return this.a;
    }
}
