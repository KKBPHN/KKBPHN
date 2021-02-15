package com.xiaomi.stat;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.os.SystemClock;

class r implements ActivityLifecycleCallbacks {
    final /* synthetic */ e a;
    private int b;

    r(e eVar) {
        this.a = eVar;
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
        this.a.k = this.a.k + 1;
        if (this.b == System.identityHashCode(activity)) {
            long elapsedRealtime = SystemClock.elapsedRealtime() - this.a.f;
            long l = this.a.d();
            this.a.a(activity.getClass().getName(), l - elapsedRealtime, l);
            this.a.h();
        }
    }

    public void onActivityResumed(Activity activity) {
        this.a.j = this.a.j + 1;
        this.b = System.identityHashCode(activity);
        this.a.f = SystemClock.elapsedRealtime();
        this.a.h();
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
        if (this.a.i == 0) {
            this.a.l = SystemClock.elapsedRealtime();
            this.a.j = 0;
            this.a.k = 0;
            this.a.e.execute(new s(this));
        }
        this.a.i = this.a.i + 1;
    }

    public void onActivityStopped(Activity activity) {
        this.a.i = this.a.i - 1;
        if (this.a.i == 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime() - this.a.l;
            long b2 = com.xiaomi.stat.d.r.b();
            e eVar = this.a;
            eVar.a(eVar.j, this.a.k, b2 - elapsedRealtime, b2);
            this.a.e.execute(new t(this));
        }
    }
}
