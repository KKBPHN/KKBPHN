package com.xiaomi.stat;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.SystemClock;
import com.xiaomi.stat.a.c;
import com.xiaomi.stat.a.l;
import com.xiaomi.stat.c.i;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.m;
import com.xiaomi.stat.d.n;
import com.xiaomi.stat.d.r;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class e {
    /* access modifiers changed from: private */
    public boolean a;
    /* access modifiers changed from: private */
    public String b;
    /* access modifiers changed from: private */
    public String c;
    private Context d;
    /* access modifiers changed from: private */
    public Executor e;
    /* access modifiers changed from: private */
    public long f;
    private Map g;
    private al h;
    /* access modifiers changed from: private */
    public int i;
    /* access modifiers changed from: private */
    public int j;
    /* access modifiers changed from: private */
    public int k;
    /* access modifiers changed from: private */
    public long l;

    public e(Context context, String str, String str2, String str3, boolean z) {
        this.i = 0;
        this.j = 0;
        this.k = 0;
        this.a = false;
        this.b = str3;
        a(context, str, str2, z, (String) null);
    }

    public e(Context context, String str, String str2, boolean z) {
        this(context, str, str2, z, (String) null);
    }

    public e(Context context, String str, String str2, boolean z, String str3) {
        this.i = 0;
        this.j = 0;
        this.k = 0;
        this.a = true;
        a(context, str, str2, z, str3);
    }

    /* access modifiers changed from: private */
    public void a(int i2, int i3, long j2, long j3) {
        Executor executor = this.e;
        p pVar = new p(this, i2, i3, j2, j3);
        executor.execute(pVar);
    }

    private void a(Context context, String str, String str2, boolean z, String str3) {
        this.d = context.getApplicationContext();
        ak.a(context.getApplicationContext(), str, str2);
        if (!this.a) {
            str = this.b;
        }
        this.c = str;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
        this.e = threadPoolExecutor;
        if (this.a) {
            e();
        }
        r.a();
        this.e.execute(new f(this, str3, z));
    }

    private void a(MiStatParams miStatParams, boolean z) {
        if (miStatParams == null || miStatParams.isEmpty()) {
            k.e("set user profile failed: empty property !");
        } else if (c(miStatParams)) {
            this.e.execute(new aa(this, z, miStatParams));
        }
    }

    /* access modifiers changed from: private */
    public void a(l lVar) {
        c.a().a(lVar);
        i.a().d();
    }

    /* access modifiers changed from: private */
    public void a(String str, long j2, long j3) {
        Executor executor = this.e;
        u uVar = new u(this, str, j2, j3);
        executor.execute(uVar);
    }

    private void a(String str, String str2, MiStatParams miStatParams, boolean z) {
        if (!n.a(str)) {
            n.e(str);
        } else if (str2 != null && !n.a(str2)) {
            n.e(str2);
        } else if (miStatParams == null || c(miStatParams)) {
            Executor executor = this.e;
            y yVar = new y(this, z, str, str2, miStatParams);
            executor.execute(yVar);
        }
    }

    /* access modifiers changed from: private */
    public boolean a(long j2, long j3) {
        boolean z = true;
        if (j2 == -1) {
            return true;
        }
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j2);
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(j3);
        if (instance.get(1) == instance2.get(1) && instance.get(6) == instance2.get(6)) {
            z = false;
        }
        return z;
    }

    private boolean c(MiStatParams miStatParams) {
        return miStatParams.getClass().equals(MiStatParams.class) && miStatParams.getParamsNumber() <= 30;
    }

    /* access modifiers changed from: private */
    public long d() {
        return r.b();
    }

    private void e() {
        ((Application) this.d).registerActivityLifecycleCallbacks(new r(this));
    }

    /* access modifiers changed from: private */
    public void f() {
        if (this.a) {
            int p = b.p();
            int a2 = com.xiaomi.stat.d.c.a();
            if (p == -1) {
                b.e(a2);
                return;
            }
            if (p < a2) {
                this.e.execute(new j(this, a2, p));
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean g() {
        return !b.d(this.c) || b.e(this.c) != 2;
    }

    /* access modifiers changed from: private */
    public boolean g(boolean z) {
        boolean z2 = true;
        if (b.d(this.c)) {
            if (b.e(this.c) == 2) {
                z2 = false;
            }
            return z2;
        } else if (!b.e() || z) {
            return m.b(this.d);
        } else {
            return true;
        }
    }

    /* access modifiers changed from: private */
    public void h() {
        this.e.execute(new k(this));
    }

    private boolean i() {
        boolean z;
        boolean z2 = (this.d.getApplicationInfo().flags & 1) == 1;
        PackageManager packageManager = this.d.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(this.d.getPackageName(), 64);
            PackageInfo packageInfo2 = packageManager.getPackageInfo("android", 64);
            if (!(packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.length <= 0 || packageInfo2 == null || packageInfo2.signatures == null || packageInfo2.signatures.length <= 0)) {
                z = packageInfo2.signatures[0].equals(packageInfo.signatures[0]);
                return z2 || z;
            }
        } catch (NameNotFoundException unused) {
        }
        z = false;
        if (z2) {
            return true;
        }
    }

    public int a() {
        return b.i();
    }

    public void a(int i2) {
        if (this.a) {
            b.a(i2);
        }
    }

    public void a(HttpEvent httpEvent) {
        if (httpEvent != null) {
            this.e.execute(new m(this, httpEvent));
        }
    }

    public void a(MiStatParams miStatParams) {
        a(miStatParams, false);
    }

    public void a(NetAvailableEvent netAvailableEvent) {
        if (netAvailableEvent != null) {
            this.e.execute(new n(this, netAvailableEvent));
        }
    }

    public void a(String str) {
        if (this.g == null) {
            this.g = new HashMap();
        }
        this.g.put(str, Long.valueOf(SystemClock.elapsedRealtime()));
    }

    public void a(String str, MiStatParams miStatParams) {
        Map map = this.g;
        if (map != null) {
            Long l2 = (Long) map.get(str);
            if (l2 != null) {
                this.g.remove(str);
                if (n.a(str)) {
                    if (miStatParams == null || c(miStatParams)) {
                        long elapsedRealtime = SystemClock.elapsedRealtime() - l2.longValue();
                        long d2 = d();
                        Executor executor = this.e;
                        x xVar = new x(this, str, d2, elapsedRealtime, miStatParams);
                        executor.execute(xVar);
                    }
                }
            }
        }
    }

    public void a(String str, String str2) {
        a(str, str2, (MiStatParams) null);
    }

    public void a(String str, String str2, MiStatParams miStatParams) {
        a(str, str2, miStatParams, false);
    }

    public void a(Throwable th) {
        a(th, (String) null);
    }

    public void a(Throwable th, String str) {
        a(th, str, true);
    }

    /* access modifiers changed from: 0000 */
    public void a(Throwable th, String str, boolean z) {
        if (th != null) {
            this.e.execute(new z(this, th, str, z));
        }
    }

    public void a(boolean z) {
        if (this.a) {
            this.e.execute(new v(this, z));
        }
    }

    public void a(boolean z, String str) {
        if (this.a) {
            this.e.execute(new l(this, z, str));
        }
    }

    public boolean a(boolean z, boolean z2) {
        if (!i()) {
            return false;
        }
        b.f(z);
        b.g(z2);
        return true;
    }

    public int b() {
        return b.j();
    }

    public void b(int i2) {
        if (this.a) {
            b.b(i2);
        }
    }

    public void b(MiStatParams miStatParams) {
        a(miStatParams, true);
    }

    public void b(String str) {
        a(str, (MiStatParams) null);
    }

    public void b(String str, MiStatParams miStatParams) {
        a(str, (String) null, miStatParams);
    }

    public void b(String str, String str2) {
        b(str, str2, null);
    }

    public void b(String str, String str2, MiStatParams miStatParams) {
        a(str, str2, miStatParams, true);
    }

    public void b(boolean z) {
        if (this.a) {
            this.e.execute(new w(this, z));
        }
    }

    public String c() {
        FutureTask futureTask = new FutureTask(new o(this));
        com.xiaomi.stat.b.e.a().execute(futureTask);
        try {
            return (String) futureTask.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException unused) {
            return null;
        }
    }

    public void c(String str) {
        a(str, (String) null, (MiStatParams) null);
    }

    public void c(String str, MiStatParams miStatParams) {
        a(str, (String) null, miStatParams);
    }

    public void c(String str, String str2) {
        if (!n.a(str)) {
            n.e(str);
        } else if (!n.b(str2)) {
            n.f(str2);
        } else {
            MiStatParams miStatParams = new MiStatParams();
            miStatParams.putString(str, str2);
            a(miStatParams);
        }
    }

    public void c(boolean z) {
        if (this.a) {
            b.d(z);
            al alVar = this.h;
            if (alVar != null) {
                alVar.a(z);
            } else if (z) {
                this.h = new al(this);
                this.h.a();
            }
        }
    }

    public void d(String str) {
        b(str, null, null);
    }

    public void d(String str, String str2) {
        if (!n.a(str)) {
            n.e(str);
        } else if (!n.b(str2)) {
            n.f(str2);
        } else {
            MiStatParams miStatParams = new MiStatParams();
            miStatParams.putString(str, str2);
            b(miStatParams);
        }
    }

    public void d(boolean z) {
        this.e.execute(new i(this, z));
    }

    public void e(String str) {
        if (this.a) {
            this.e.execute(new h(this, str));
        }
    }

    public void e(String str, String str2) {
        if (!n.a(str)) {
            n.e(str);
        } else if (!n.d(str2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("invalid plain text value for event: ");
            sb.append(str);
            k.e(sb.toString());
        } else {
            this.e.execute(new q(this, str, str2));
        }
    }

    public boolean e(boolean z) {
        return a(z, false);
    }

    public void f(boolean z) {
        k.a(z);
    }
}
