package com.xiaomi.stat.b;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import com.xiaomi.stat.ab;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.c.c;
import com.xiaomi.stat.d.a;
import com.xiaomi.stat.d.b;
import com.xiaomi.stat.d.d;
import com.xiaomi.stat.d.g;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.o;
import com.xiaomi.stat.d.r;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class i {
    private static final String a = "SecretKeyManager";
    private static final String b = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCA1ynlvPE46RxIPx6qrb8f20DU\n\rkAJgwHtD3zCEkgOjcvFY2mLl0UGnK1F0Vsh4LvImSCa8o8qYYfBguROgIXRdJGZ+\n\rk9stSV7vWmcsxphMfHEE9R4q+QWqgPBSzwyWmwmAQ7PZmHifOrEYl9t/l0YtmjnW\n\r8Zs3aL7Ap9CGse2kWwIDAQAB\r";
    private static final String c = "sid";
    private static final String d = "sk";
    private static final String e = "rt";
    private static final String f = "rc";
    private static final String g = "request_history";
    private static final String h = "last_aes_content";
    private static final String i = "last_success_time";
    private static final String j = "4ef8b4ac42dbc3f95320b73ae0edbd43";
    private static final String k = "050f03d86eeafeb29cf38986462d957c";
    private static final int l = 1;
    private static final int m = 2;
    private static final String n = "1";
    private static final String o = "0";
    private static final int p = 7;
    private static final int q = 15;
    private static volatile i r;
    private Context s = ak.a();
    private byte[] t;
    private byte[] u;
    private String v;

    private i() {
        d();
    }

    public static i a() {
        if (r == null) {
            synchronized (i.class) {
                if (r == null) {
                    r = new i();
                }
            }
        }
        return r;
    }

    private boolean b(boolean z) {
        String str;
        int i2 = VERSION.SDK_INT;
        String str2 = a;
        if (i2 < 18) {
            str = "under 4.3,use randomly generated key";
        } else {
            if (j()) {
                k();
            }
            JSONObject g2 = g();
            if (g2 != null) {
                String optString = g2.optString("sid");
                if (!TextUtils.isEmpty(g2.optString("sk")) && !TextUtils.isEmpty(optString) && !z) {
                    str = "key and sid already requested successfully in recent 7 days!";
                }
            }
            JSONObject h2 = h();
            long optLong = h2.optLong(e);
            int optInt = h2.optInt("rc");
            if (!r.b(optLong) || optInt < 15 || z) {
                return f();
            }
            str = "request count > max count today, skip...";
        }
        k.b(str2, str);
        return false;
    }

    private void d() {
        this.u = a.a();
        byte[] bArr = this.u;
        if (bArr == null || bArr.length <= 0) {
            this.u = a.a(k);
        }
        String concat = g.a(this.u, true).concat("_").concat(String.valueOf(r.b()));
        try {
            concat = g.a(concat.getBytes("utf-8"), true);
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        this.t = a.a(a.a(concat), j);
    }

    private String e() {
        CharSequence charSequence;
        String str = null;
        if (VERSION.SDK_INT >= 18) {
            JSONObject g2 = g();
            if (g2 != null) {
                str = g2.optString("sk");
                charSequence = g2.optString("sid");
                return (!TextUtils.isEmpty(str) || TextUtils.isEmpty(charSequence)) ? g.a(this.u, true) : str;
            }
        }
        charSequence = null;
        if (!TextUtils.isEmpty(str)) {
        }
    }

    private boolean f() {
        String str = "sid";
        String str2 = a;
        boolean z = false;
        try {
            byte[] a2 = a.a();
            String a3 = d.a(o.a(d.a(b), a2));
            i();
            HashMap hashMap = new HashMap();
            hashMap.put("skey_rsa", a3);
            String a4 = c.a(g.a().d(), (Map) hashMap, false);
            if (!TextUtils.isEmpty(a4)) {
                StringBuilder sb = new StringBuilder();
                sb.append("result:");
                sb.append(a4);
                k.b(str2, sb.toString());
                JSONObject jSONObject = new JSONObject(a4);
                String optString = jSONObject.optString(NotificationCompat.CATEGORY_MESSAGE);
                int optInt = jSONObject.optInt("code");
                long optLong = jSONObject.optLong("curTime");
                JSONObject optJSONObject = jSONObject.optJSONObject("result");
                if (optInt == 1 && optJSONObject != null) {
                    try {
                        String optString2 = optJSONObject.optString(str);
                        String a5 = a.a(optJSONObject.optString(WatermarkConstant.ITEM_KEY), a2);
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put("sk", a5);
                        jSONObject2.put(str, optString2);
                        this.v = jSONObject2.toString();
                        ab.a().b("last_aes_content", b.a(this.s, jSONObject2.toString()));
                        ab.a().b("last_success_time", optLong);
                        r.a(optLong);
                        return false;
                    } catch (Exception e2) {
                        e = e2;
                    }
                } else if (optInt == 2) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("update secret-key failed: ");
                    sb2.append(optString);
                    k.b(str2, sb2.toString());
                }
            }
            return true;
        } catch (Exception e3) {
            e = e3;
            z = true;
            k.d(str2, "updateSecretKey e", e);
            return z;
        }
    }

    private JSONObject g() {
        String str;
        String a2 = ab.a().a("last_aes_content", "");
        try {
            if (!TextUtils.isEmpty(a2)) {
                if (!TextUtils.isEmpty(this.v)) {
                    str = this.v;
                } else {
                    String b2 = b.b(this.s, a2);
                    this.v = b2;
                    str = b2;
                }
                return new JSONObject(str);
            }
        } catch (Exception e2) {
            k.d(a, "decodeFromAndroidKeyStore e", e2);
        }
        return null;
    }

    private JSONObject h() {
        try {
            String a2 = ab.a().a("request_history", "");
            if (!TextUtils.isEmpty(a2)) {
                return new JSONObject(a2);
            }
        } catch (Exception e2) {
            k.d(a, "getRequestHistory e", e2);
        }
        return new JSONObject();
    }

    private void i() {
        String str = e;
        String str2 = "rc";
        try {
            JSONObject h2 = h();
            long optLong = h2.optLong(str);
            int optInt = h2.optInt(str2);
            if (r.b(optLong)) {
                h2.put(str2, optInt + 1);
            } else {
                h2.put(str2, 1);
            }
            h2.put(str, r.b());
            ab.a().b("request_history", h2.toString());
        } catch (JSONException e2) {
            k.b(a, "updateSecretKey e", e2);
        }
    }

    private boolean j() {
        long a2 = ab.a().a("last_success_time", 0);
        return a2 != 0 && r.a(a2, 604800000);
    }

    private void k() {
        ab a2 = ab.a();
        this.v = null;
        a2.b("last_aes_content");
        a2.b("last_success_time");
    }

    private synchronized boolean l() {
        boolean z;
        JSONObject g2 = g();
        z = true;
        if (g2 != null) {
            String optString = g2.optString("sk");
            String optString2 = g2.optString("sid");
            if (!TextUtils.isEmpty(optString) && !TextUtils.isEmpty(optString2)) {
                z = false;
            }
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0036, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void a(boolean z) {
        if (com.xiaomi.stat.b.a()) {
            if (com.xiaomi.stat.b.b()) {
                if (l.a()) {
                    int i2 = 1;
                    while (i2 <= 3 && b(z) && i2 != 3) {
                        try {
                            Thread.sleep(FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        i2++;
                    }
                } else {
                    k.b(a, "network not connected!");
                }
            }
        }
        k.c(a, "update abort: statistic or network is not enabled");
    }

    public synchronized byte[] a(byte[] bArr) {
        if (bArr == null) {
            k.b(a, "encrypt content is empty");
            return null;
        }
        return a.a(bArr, e());
    }

    public synchronized String b() {
        String str;
        CharSequence charSequence;
        str = null;
        if (VERSION.SDK_INT >= 18) {
            JSONObject g2 = g();
            if (g2 != null) {
                str = g2.optString("sid");
                charSequence = g2.optString("sk");
                if (TextUtils.isEmpty(str) || TextUtils.isEmpty(charSequence)) {
                    str = g.a(this.t, true);
                }
            }
        }
        charSequence = null;
        str = g.a(this.t, true);
        return str;
    }

    public String c() {
        return (VERSION.SDK_INT < 18 || l()) ? "1" : "0";
    }
}
