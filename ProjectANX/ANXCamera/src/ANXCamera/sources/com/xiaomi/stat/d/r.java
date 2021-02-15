package com.xiaomi.stat.d;

import android.content.Context;
import android.os.SystemClock;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.b.e;
import java.util.Calendar;

public class r {
    public static final long a = 86400000;
    private static final String b = "TimeUtil";
    private static final long c = 300000;
    private static long d;
    private static long e;
    private static long f;

    public static void a() {
        boolean z;
        Context a2 = ak.a();
        long f2 = p.f(a2);
        long g = p.g(a2);
        long h = p.h(a2);
        if (f2 == 0 || g == 0 || h == 0 || Math.abs((System.currentTimeMillis() - g) - (SystemClock.elapsedRealtime() - h)) > 300000) {
            z = true;
        } else {
            d = f2;
            f = g;
            e = h;
            z = false;
        }
        if (z) {
            e.a().execute(new s());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("syncTimeIfNeeded sync: ");
        sb.append(z);
        k.b(b, sb.toString());
    }

    public static void a(long j) {
        if (j > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("update server time:");
            sb.append(j);
            k.b("MI_STAT_TEST", sb.toString());
            d = j;
            e = SystemClock.elapsedRealtime();
            f = System.currentTimeMillis();
            Context a2 = ak.a();
            p.a(a2, d);
            p.b(a2, f);
            p.c(a2, e);
        }
    }

    public static boolean a(long j, long j2) {
        return Math.abs(b() - j) >= j2;
    }

    public static long b() {
        long j = d;
        return (j == 0 || e == 0) ? System.currentTimeMillis() : (j + SystemClock.elapsedRealtime()) - e;
    }

    public static boolean b(long j) {
        StringBuilder sb = new StringBuilder();
        sb.append("inToday,current ts :");
        sb.append(b());
        String str = "MI_STAT_TEST";
        k.b(str, sb.toString());
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(b());
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        long timeInMillis = instance.getTimeInMillis();
        long j2 = timeInMillis + 86400000;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[start]:");
        sb2.append(timeInMillis);
        sb2.append("\n[end]:");
        sb2.append(j2);
        sb2.append("duration");
        sb2.append((j2 - timeInMillis) - 86400000);
        k.b(str, sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("is in today:");
        int i = (timeInMillis > j ? 1 : (timeInMillis == j ? 0 : -1));
        boolean z = i <= 0 && j < j2;
        sb3.append(z);
        k.b(str, sb3.toString());
        return i <= 0 && j < j2;
    }
}
