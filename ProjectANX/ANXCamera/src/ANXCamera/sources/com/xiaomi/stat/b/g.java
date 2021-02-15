package com.xiaomi.stat.b;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Looper;
import android.text.TextUtils;
import com.xiaomi.stat.ab;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.b;
import com.xiaomi.stat.d.h;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.m;
import com.xiaomi.stat.d.r;
import java.util.HashMap;
import org.json.JSONObject;

public class g {
    private static final String a = "RDM";
    private static final Object b = new Object();
    private static final String c = "CN";
    private static final String d = "INTL";
    private static final String e = "IN";
    private static final String f = "data.mistat.xiaomi.com";
    private static final String g = "data.mistat.intl.xiaomi.com";
    private static final String h = "data.mistat.india.xiaomi.com";
    private static final String i = "region-url";
    private static final String j = "/map_domain";
    private static final String k = "region";
    private static HashMap l = new HashMap();
    private static g r;
    private String m = c;
    private String n = f;
    private String o = null;
    private String p;
    private h q;

    static {
        l.put(c, f);
        l.put(d, g);
        l.put(e, h);
    }

    private g() {
        a(ak.a());
    }

    public static g a() {
        if (r == null) {
            synchronized (b) {
                if (r == null) {
                    r = new g();
                }
            }
        }
        return r;
    }

    private String b(String str) {
        int i2 = ak.a().getApplicationInfo().targetSdkVersion;
        String str2 = "https://";
        if (!b.e() && (VERSION.SDK_INT < 28 || i2 < 28)) {
            str2 = "http://";
        }
        return str2.concat(this.n).concat("/").concat(str);
    }

    private boolean c(String str) {
        boolean z;
        if (l.keySet().contains(str)) {
            this.m = str;
            this.n = (String) l.get(this.m);
            z = true;
        } else {
            this.m = d;
            this.n = (String) l.get(this.m);
            k.d(a, "unknown region,set to unknown(singapore)'s domain");
            z = false;
        }
        ab.a().b("region", str);
        return z;
    }

    private void d(String str) {
        this.o = str;
    }

    private static void e() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            k.d(a, "can not init in main thread!", null);
        }
    }

    private void f() {
        l = this.q.a(l, (HashMap) h.a(this.p.concat(j)));
        String str = (String) l.get(this.m);
        if (TextUtils.isEmpty(str)) {
            if (b.e()) {
                this.m = d;
                str = (String) l.get(this.m);
            } else {
                return;
            }
        }
        this.n = str;
    }

    public void a(Context context) {
        e();
        this.q = new h();
        this.p = context.getFilesDir().getPath();
        boolean e2 = b.e();
        String str = a;
        if (!e2) {
            this.m = c;
            this.n = f;
        } else {
            String g2 = m.g();
            StringBuilder sb = new StringBuilder();
            sb.append("[SystemRegion]:");
            sb.append(g2);
            k.b(str, sb.toString());
            String a2 = ab.a().a("region", (String) null);
            if (!TextUtils.isEmpty(g2)) {
                this.m = g2;
            }
            if (!TextUtils.isEmpty(a2)) {
                this.m = a2;
            }
            f();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[file-dir]:");
        sb2.append(this.p);
        sb2.append("\n[CurrentRegion]:");
        sb2.append(this.m);
        sb2.append("\n[domain]:");
        sb2.append(this.n);
        k.b(str, sb2.toString());
    }

    public void a(String str) {
        d(str);
        c(str);
    }

    public void a(JSONObject jSONObject) {
        String str;
        HashMap a2 = this.q.a(i, jSONObject);
        String str2 = TextUtils.isEmpty(this.o) ? this.m : this.o;
        if (a2 != null) {
            l = this.q.a(l, a2);
            if (!TextUtils.isEmpty(str2)) {
                str = (String) l.get(str2);
                if (!TextUtils.isEmpty(str)) {
                    this.m = str2;
                }
                h.a(l, this.p.concat(j));
            }
            if (b.e()) {
                this.m = d;
                str = (String) l.get(this.m);
            }
            h.a(l, this.p.concat(j));
            this.n = str;
            h.a(l, this.p.concat(j));
        }
    }

    public void a(boolean z) {
        if (z) {
            this.m = d;
            this.n = g;
            String str = TextUtils.isEmpty(this.o) ? this.m : this.o;
            if (!TextUtils.isEmpty(str)) {
                String str2 = (String) l.get(str);
                if (!TextUtils.isEmpty(str2)) {
                    this.m = str;
                    this.n = str2;
                    return;
                }
                return;
            }
            return;
        }
        this.m = c;
        this.n = f;
    }

    /* access modifiers changed from: 0000 */
    public String b() {
        return b("get_all_config");
    }

    public boolean b(Context context) {
        return r.b(ab.a().a("key_update_time", 0)) || !b.e();
    }

    public String c() {
        return b("mistats/v3");
    }

    /* access modifiers changed from: protected */
    public Object clone() {
        throw new CloneNotSupportedException("Cannot clone instance of this class");
    }

    public String d() {
        return b("key_get");
    }
}
