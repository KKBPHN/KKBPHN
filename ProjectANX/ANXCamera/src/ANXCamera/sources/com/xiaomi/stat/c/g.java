package com.xiaomi.stat.c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.android.camera.snap.SnapTrigger;
import com.xiaomi.stat.a.c;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.b;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.p;
import com.xiaomi.stat.d.r;
import java.util.concurrent.atomic.AtomicBoolean;

public class g extends Handler {
    private static final String c = "UploadTimer";
    private static final int d = 15000;
    private static final int e = 5;
    private static final int f = 86400;
    private static final int g = 1;
    private static final int h = 2;
    private static final int i = 3;
    public AtomicBoolean a;
    BroadcastReceiver b;
    private int j;
    private int k;
    private long l;
    private boolean m;
    private int n;

    public g(Looper looper) {
        super(looper);
        this.j = SnapTrigger.MAX_VIDEO_DURATION;
        this.a = new AtomicBoolean(true);
        this.k = 15000;
        this.l = r.b();
        this.m = true;
        this.b = new h(this);
        this.k = 60000;
        sendEmptyMessageDelayed(1, (long) this.k);
        a(ak.a());
        StringBuilder sb = new StringBuilder();
        sb.append(" UploadTimer: ");
        sb.append(this.k);
        k.b(c, sb.toString());
    }

    private int a(int i2) {
        if (i2 < 0) {
            return 0;
        }
        if (i2 <= 0 || i2 >= 5) {
            return i2 > f ? f : i2;
        }
        return 5;
    }

    private void a(Context context) {
        if (context != null) {
            try {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                context.registerReceiver(this.b, intentFilter);
            } catch (Exception e2) {
                StringBuilder sb = new StringBuilder();
                sb.append("registerNetReceiver: ");
                sb.append(e2);
                k.b(c, sb.toString());
            }
        }
    }

    private int f() {
        int a2 = a(b.m());
        if (a2 > 0) {
            return a2 * 1000;
        }
        int a3 = a(b.j());
        if (a3 > 0) {
            return a3 * 1000;
        }
        return 15000;
    }

    private void g() {
        i.a().c();
        e();
    }

    private void h() {
        if (l.a()) {
            b();
        }
    }

    private void i() {
        boolean z;
        AtomicBoolean atomicBoolean;
        int i2 = (c.a().c() > 0 ? 1 : (c.a().c() == 0 ? 0 : -1));
        if (i2 >= 0) {
            if (i2 > 0) {
                b();
                atomicBoolean = this.a;
                z = false;
            } else {
                atomicBoolean = this.a;
                z = true;
            }
            atomicBoolean.set(z);
            StringBuilder sb = new StringBuilder();
            sb.append(" checkDatabase mIsDatabaseEmpty=");
            sb.append(this.a.get());
            k.b(c, sb.toString());
        }
    }

    public long a() {
        return (long) this.k;
    }

    public void a(boolean z) {
        if (!z && !this.m) {
            b();
        }
        this.m = false;
    }

    public void b() {
        if (this.k != this.n) {
            this.n = f();
            this.k = this.n;
            if (r.b() - this.l > ((long) this.k)) {
                removeMessages(1);
                sendEmptyMessageDelayed(1, (long) this.k);
                this.l = r.b();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(" resetBackgroundInterval: ");
            sb.append(this.k);
            k.b(c, sb.toString());
        }
    }

    public void b(boolean z) {
        if (z) {
            b();
        }
        long c2 = c.a().c();
        int i2 = (c2 > 0 ? 1 : (c2 == 0 ? 0 : -1));
        if (i2 == 0) {
            this.a.set(true);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" totalCount=");
        sb.append(c2);
        sb.append(" deleteData=");
        sb.append(z);
        k.b(c, sb.toString());
        if (this.k < this.j) {
            if (i2 == 0 || !z) {
                this.k += 15000;
            }
        }
    }

    public void c() {
        this.k = this.j;
    }

    public void d() {
        if (this.a.get()) {
            sendEmptyMessage(2);
        }
    }

    public void e() {
        try {
            Context a2 = ak.a();
            long n2 = p.n(a2);
            long m2 = p.m(a2);
            long totalRxBytes = TrafficStats.getTotalRxBytes() == -1 ? 0 : TrafficStats.getTotalRxBytes() / 1024;
            long b2 = r.b();
            float f2 = ((float) ((totalRxBytes - m2) * 1000)) / ((float) (b2 - n2));
            p.e(a2, b2);
            p.d(a2, totalRxBytes);
            p.a(a2, f2);
        } catch (Throwable unused) {
        }
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        int i2 = message.what;
        if (i2 == 1) {
            g();
            sendEmptyMessageDelayed(1, (long) this.k);
        } else if (i2 == 2) {
            i();
        } else if (i2 == 3) {
            h();
        }
    }
}
