package com.xiaomi.stat;

import android.os.Looper;
import android.text.TextUtils;
import com.xiaomi.stat.d.m;

public class b {
    private static final String A = "pref_main_app_channel";
    private static final String B = "pref_instance_id_last_use_time";
    private static final String C = "pref_system_upload_intl_enabled";
    private static final String D = "pref_upload_enabled";
    private static final String E = "pref_sample_enabled";
    private static final String F = "pref_basic_enabled";
    private static final String G = "pref_custom_enabled";
    private static final String H = "pref_crash_enabled";
    private static final String I = "pref_http_event_enabled";
    private static int J = 31;
    private static int K = 15;
    private static String[] L = null;
    private static final String M = ",";
    private static boolean N = false;
    private static boolean O = false;
    private static String P = null;
    private static boolean Q = false;
    private static Object R = new Object();
    private static boolean S = false;
    public static final int a = -1;
    public static final int b = -1;
    public static final int c = 0;
    public static final int d = 1;
    public static final int e = 2;
    public static final int f = -1;
    public static final String g = "uploadInterval";
    public static final String h = "configNetwork";
    public static final String i = "configDelay";
    public static final String j = "time";
    public static final String k = "enableSample";
    public static final String l = "uploadSwitch";
    public static final String m = "0.0";
    public static final String n = "0-0";
    public static final int o = 0;
    private static final String p = "pref_statistic_enabled";
    private static final String q = "pref_network_access_enabled";
    private static final String r = "pref_user_id";
    private static final String s = "pref_random_uuid";
    private static final String t = "pref_using_custom_policy_";
    private static final String u = "pref_custom_policy_state_";
    private static final String v = "pref_app_previous_version";
    private static final String w = "pref_is_first_usage";
    private static final String x = "pref_last_dau_event_time";
    private static final String y = "pref_all_sub_ids_data";
    private static final String z = "pref_instance_id";

    public static boolean A() {
        return ab.a().a(G, true);
    }

    public static boolean B() {
        return ab.a().a(D, true);
    }

    public static void a(int i2) {
        J = i2;
    }

    public static void a(long j2) {
        ab.a().b(x, j2);
    }

    public static void a(String str) {
        P = str;
    }

    public static void a(String str, int i2) {
        if (!TextUtils.isEmpty(str)) {
            ab a2 = ab.a();
            StringBuilder sb = new StringBuilder();
            sb.append(u);
            sb.append(str);
            a2.b(sb.toString(), i2);
        }
    }

    public static void a(String str, boolean z2) {
        if (!TextUtils.isEmpty(str)) {
            ab a2 = ab.a();
            StringBuilder sb = new StringBuilder();
            sb.append(t);
            sb.append(str);
            a2.b(sb.toString(), z2);
        }
    }

    public static void a(boolean z2) {
        ab.a().b(p, z2);
    }

    public static boolean a() {
        return ab.a().a(p, true);
    }

    public static void b(int i2) {
        if (i2 <= 5) {
            i2 = 15;
        } else if (i2 > 86400) {
            i2 = 86400;
        }
        K = i2;
    }

    public static void b(long j2) {
        ab.a().b(B, j2);
    }

    public static void b(String str) {
        ab.a().b(r, str);
    }

    public static void b(boolean z2) {
        ab.a().b(q, z2);
    }

    public static boolean b() {
        return ab.a().a(q, true);
    }

    public static void c() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            long j2 = 5000;
            if (ab.a().a(q)) {
                j2 = 1000;
            }
            try {
                Thread.sleep(j2);
            } catch (InterruptedException unused) {
            }
        } else {
            throw new IllegalStateException("don't call this on main thread");
        }
    }

    public static void c(int i2) {
        ab.a().b(h, i2);
    }

    public static void c(String str) {
        ab.a().b(i, str);
    }

    public static void c(boolean z2) {
        O = z2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void d() {
        synchronized (R) {
            if (!Q) {
                Q = true;
                O = m.i();
                P = m.g();
                if (!O && !TextUtils.equals(P, "CN")) {
                    O = true;
                }
            }
        }
    }

    public static void d(int i2) {
        ab.a().b(g, i2);
    }

    public static void d(boolean z2) {
        S = z2;
    }

    public static boolean d(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ab a2 = ab.a();
        StringBuilder sb = new StringBuilder();
        sb.append(t);
        sb.append(str);
        return a2.a(sb.toString(), false);
    }

    public static int e(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        ab a2 = ab.a();
        StringBuilder sb = new StringBuilder();
        sb.append(u);
        sb.append(str);
        return a2.a(sb.toString(), 0);
    }

    public static void e(int i2) {
        if (i2 > 0) {
            ab.a().b(v, i2);
        }
    }

    public static void e(boolean z2) {
        ab.a().b(w, z2);
    }

    public static boolean e() {
        if (!Q) {
            d();
        }
        return O;
    }

    public static String f() {
        return P;
    }

    public static void f(String str) {
        String[] strArr = L;
        if (strArr != null) {
            int length = strArr.length;
            int i2 = 0;
            while (i2 < length) {
                if (!TextUtils.equals(str, strArr[i2])) {
                    i2++;
                } else {
                    return;
                }
            }
        }
        String[] strArr2 = L;
        String str2 = y;
        if (strArr2 == null) {
            ab.a().b(str2, str);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(L[0]);
            int length2 = L.length;
            int i3 = 1;
            while (true) {
                sb.append(M);
                if (i3 >= length2) {
                    break;
                }
                sb.append(L[i3]);
                i3++;
            }
            sb.append(str);
            ab.a().b(str2, sb.toString());
        }
    }

    public static void f(boolean z2) {
        N = z2;
    }

    public static void g(String str) {
        ab.a().b(z, str);
    }

    public static void g(boolean z2) {
        ab.a().b(C, z2);
    }

    public static boolean g() {
        return S;
    }

    public static String h() {
        return ab.a().a(r, (String) null);
    }

    public static void h(String str) {
        ab.a().b(A, str);
    }

    public static void h(boolean z2) {
        ab.a().b(E, z2);
    }

    public static int i() {
        return J;
    }

    public static void i(String str) {
        ab.a().b(s, str);
    }

    public static void i(boolean z2) {
        ab.a().b(F, z2);
    }

    public static int j() {
        return K;
    }

    public static void j(boolean z2) {
        ab.a().b(G, z2);
    }

    public static String k() {
        return ab.a().a(i, n);
    }

    public static void k(boolean z2) {
        ab.a().b(D, z2);
    }

    public static int l() {
        return ab.a().a(h, -1);
    }

    public static int m() {
        return ab.a().a(g, -1);
    }

    public static void n() {
        String a2 = ab.a().a(y, (String) null);
        if (!TextUtils.isEmpty(a2)) {
            L = a2.split(M);
        }
    }

    public static String[] o() {
        return L;
    }

    public static int p() {
        return ab.a().a(v, -1);
    }

    public static boolean q() {
        return ab.a().a(w, true);
    }

    public static long r() {
        return ab.a().a(x, -1);
    }

    public static String s() {
        return ab.a().a(z, (String) null);
    }

    public static String t() {
        return ab.a().a(A, (String) null);
    }

    public static boolean u() {
        return N;
    }

    public static long v() {
        return ab.a().a(B, 0);
    }

    public static String w() {
        return ab.a().a(s, (String) null);
    }

    public static boolean x() {
        return ab.a().a(C, false);
    }

    public static boolean y() {
        return ab.a().a(E, false);
    }

    public static boolean z() {
        return ab.a().a(F, true);
    }
}
