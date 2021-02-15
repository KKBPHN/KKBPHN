package com.xiaomi.stat.c;

import com.xiaomi.stat.ak;
import com.xiaomi.stat.b;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.m;

public class f {
    public static final int a = 1;
    public static final int b = 2;
    public static final int c = 3;
    private static final String e = "UploadPolicy";
    boolean d;
    private String f;

    public f(String str, boolean z) {
        this.d = z;
        this.f = str;
    }

    public f(boolean z) {
        this.d = z;
        this.f = ak.b();
    }

    private boolean a(int i) {
        return (i & -32) == 0;
    }

    private int b() {
        boolean b2 = m.b(ak.a());
        StringBuilder sb = new StringBuilder();
        sb.append(" getExperiencePlanPolicy: ");
        sb.append(b2);
        sb.append(" isInternationalVersion= ");
        sb.append(b.e());
        sb.append(" isAnonymous= ");
        sb.append(this.d);
        k.b(e, sb.toString());
        if (b2) {
            return 3;
        }
        return (!b.e() || !this.d) ? 2 : 3;
    }

    private int c() {
        int e2 = b.e(this.f);
        StringBuilder sb = new StringBuilder();
        sb.append(" getCustomPrivacyPolicy: state=");
        sb.append(e2);
        k.b(e, sb.toString());
        return e2 == 1 ? 3 : 1;
    }

    private int d() {
        return b.d(this.f) ? c() : b();
    }

    private int e() {
        int a2 = l.a(ak.a());
        int l = a(b.l()) ? b.l() : b.i();
        StringBuilder sb = new StringBuilder();
        sb.append(" getHttpServicePolicy: currentNet= ");
        sb.append(a2);
        sb.append(" Config.getServerNetworkType= ");
        sb.append(b.l());
        sb.append(" Config.getUserNetworkType()= ");
        sb.append(b.i());
        sb.append(" (configNet & currentNet) == currentNet ");
        int i = l & a2;
        sb.append(i == a2);
        k.b(e, sb.toString());
        return i == a2 ? 3 : 1;
    }

    public int a() {
        return Math.min(d(), e());
    }
}
