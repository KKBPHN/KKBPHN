package com.xiaomi.stat.c;

import android.content.Context;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.b;
import com.xiaomi.stat.d.k;
import java.util.HashMap;
import java.util.Map.Entry;

public class a {
    private static final String a = "ClientConfigMoniter";
    private static final int b = 1;
    private static final int c = 2;
    private static final int d = 3;
    private static final int e = 4;
    private static final int f = 5;
    private static Context g = ak.a();
    private static HashMap h = new HashMap();

    static {
        HashMap hashMap = h;
        Integer valueOf = Integer.valueOf(1);
        hashMap.put(valueOf, valueOf);
        HashMap hashMap2 = h;
        Integer valueOf2 = Integer.valueOf(2);
        hashMap2.put(valueOf2, valueOf2);
        HashMap hashMap3 = h;
        Integer valueOf3 = Integer.valueOf(3);
        Integer valueOf4 = Integer.valueOf(4);
        hashMap3.put(valueOf3, valueOf4);
        h.put(valueOf4, Integer.valueOf(8));
        h.put(Integer.valueOf(5), Integer.valueOf(16));
    }

    public static int a(String str) {
        int i;
        int i2 = 0;
        try {
            for (Entry entry : h.entrySet()) {
                int intValue = ((Integer) entry.getKey()).intValue();
                int intValue2 = ((Integer) entry.getValue()).intValue();
                if (a(intValue, str)) {
                    i2 |= intValue2;
                }
            }
        } catch (Exception e2) {
            k.d(a, "getClientConfiguration exception", e2);
        }
        return i;
    }

    private static boolean a(int i, String str) {
        if (i == 1) {
            return b.u();
        }
        if (i == 2) {
            return b.d(str);
        }
        if (i == 3) {
            return k.a();
        }
        if (i == 4 || i != 5) {
            return false;
        }
        try {
            return b.g();
        } catch (Exception e2) {
            k.d(a, "checkSetting exception", e2);
            return false;
        }
    }
}
