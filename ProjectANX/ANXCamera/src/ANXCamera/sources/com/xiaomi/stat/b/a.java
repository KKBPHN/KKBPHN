package com.xiaomi.stat.b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import com.xiaomi.stat.ab;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.b;
import com.xiaomi.stat.d;
import com.xiaomi.stat.d.c;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.m;
import com.xiaomi.stat.d.r;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.json.JSONObject;

public class a {
    public static final int a = 1;
    public static final int b = 2;
    public static final int c = 4;
    private static final String d = "ConfigManager";
    private static final String e = "-";
    private static int f = 0;
    private static int g = 1;
    private static int h = 2;
    private static final String i = "config_request_count";
    private static final String j = "config_request_time";
    private static final String k = "config_success_requested";
    private static final int l = 1;
    private static final int m = 2;
    private static final int n = 0;
    private static final int o = -1;
    private static final int p = 0;
    private static final int q = 12;
    private static final String r = "t";
    private static final int s = 0;
    private static volatile a t;
    /* access modifiers changed from: private */
    public int u = 0;
    private Context v = ak.a();
    private String w;
    /* access modifiers changed from: private */
    public BroadcastReceiver x = new b(this);

    private a() {
    }

    public static a a() {
        if (t == null) {
            synchronized (a.class) {
                if (t == null) {
                    t = new a();
                }
            }
        }
        return t;
    }

    private void a(int i2) {
        if (i2 > 0) {
            try {
                Thread.sleep((long) (i2 * 1000));
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        b();
    }

    private void a(String str) {
        String str2 = d;
        try {
            k.b(str2, String.format("config result:%s", new Object[]{str}));
            d();
            if (!TextUtils.isEmpty(str)) {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.getInt("errorCode") == 0) {
                    long optLong = jSONObject.optLong("time", 0);
                    ab.a().b(k, optLong);
                    b.c(jSONObject.optString(b.i, b.n));
                    b.c(jSONObject.optInt(b.h, -1));
                    b.d(jSONObject.optInt(b.g, 0) / 1000);
                    b.h(jSONObject.optBoolean(b.k));
                    a(jSONObject);
                    r.a(optLong);
                    g.a().a(jSONObject);
                    if (this.u == 1) {
                        this.v.unregisterReceiver(this.x);
                    }
                    this.u = 2;
                }
            }
        } catch (Exception e2) {
            k.d(str2, "processResult exception", e2);
        }
    }

    private void a(JSONObject jSONObject) {
        try {
            int optInt = jSONObject.optInt(b.l);
            if (optInt > 0) {
                boolean z = false;
                b.k((optInt & 1) == 1);
                b.i((optInt & 2) == 2);
                if ((optInt & 4) == 4) {
                    z = true;
                }
                b.j(z);
            }
        } catch (Exception e2) {
            StringBuilder sb = new StringBuilder();
            sb.append("updateConfig: ");
            sb.append(e2);
            k.e(d, sb.toString());
        }
    }

    private String b() {
        String str = d;
        k.b(str, "requestConfigInner");
        this.w = g.a().b();
        if (k.b()) {
            this.w = k.a;
        }
        String str2 = null;
        try {
            TreeMap treeMap = new TreeMap();
            treeMap.put(r, String.valueOf(h));
            treeMap.put("ai", ak.b());
            treeMap.put("rc", m.h());
            treeMap.put(d.d, m.g());
            treeMap.put("m", Build.MODEL);
            treeMap.put(d.b, com.xiaomi.stat.a.g);
            treeMap.put(d.j, c.b());
            str2 = com.xiaomi.stat.c.c.a(this.w, (Map) treeMap, false);
            a(str2);
            return str2;
        } catch (Exception e2) {
            k.b(str, "requestConfigInner exception ", e2);
            return str2;
        }
    }

    private boolean c() {
        String str = j;
        long b2 = r.b();
        ab a2 = ab.a();
        try {
            boolean a3 = ab.a().a(str);
            String str2 = i;
            if (!a3) {
                a2.b(str, b2);
                a2.b(str2, 1);
                return false;
            } else if (r.b(a2.a(str, 0))) {
                return a2.a(str2, 0) >= 12;
            } else {
                a2.b(str, b2);
                a2.b(str2, 0);
                return false;
            }
        } catch (Exception e2) {
            k.d(d, "isRequestCountReachMax exception", e2);
            return false;
        }
    }

    private void d() {
        String str = i;
        try {
            ab a2 = ab.a();
            a2.b(str, a2.a(str, 0) + 1);
        } catch (Exception e2) {
            k.d(d, "addRequestCount exception", e2);
        }
    }

    public synchronized void a(boolean z, boolean z2) {
        if (b.a()) {
            if (b.b()) {
                if (!l.a()) {
                    k.b(d, "network is not connected!");
                    try {
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                        this.v.registerReceiver(this.x, intentFilter);
                        this.u = 1;
                    } catch (Exception e2) {
                        String str = d;
                        StringBuilder sb = new StringBuilder();
                        sb.append("updateConfig registerReceiver error:");
                        sb.append(e2);
                        k.e(str, sb.toString());
                    }
                } else {
                    k.b(d, "updateConfig");
                    if (!z2) {
                        k.b("MI_STAT_TEST", "updateConfig-InToday");
                        if (r.b(ab.a().a(k, 0))) {
                            k.b(d, "Today has successfully requested key.");
                            return;
                        } else if (c()) {
                            k.d(d, "config request to max count skip..");
                            return;
                        }
                    }
                    int i2 = 0;
                    if (z && !z2) {
                        String[] split = b.k().split(e);
                        if (split.length > 1) {
                            int parseInt = Integer.parseInt(split[0]);
                            int parseInt2 = Integer.parseInt(split[1]);
                            if (parseInt2 > parseInt) {
                                i2 = new Random().nextInt(parseInt2 - parseInt) + parseInt;
                            }
                        }
                    }
                    a(i2);
                    return;
                }
            }
        }
        k.c(d, "update abort: statistic or network is not enabled");
        return;
        return;
    }
}
