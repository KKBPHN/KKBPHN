package defpackage;

import android.app.Activity;
import com.google.lens.sdk.LensApi;

/* renamed from: bm reason: default package */
public final /* synthetic */ class bm implements ap {
    private final LensApi a;
    private final bs b;
    private final long c;
    private final Activity d;

    public bm(LensApi lensApi, bs bsVar, long j, Activity activity) {
        this.a = lensApi;
        this.b = bsVar;
        this.c = j;
        this.d = activity;
    }

    public final void a(int i) {
        LensApi lensApi = this.a;
        bs bsVar = this.b;
        long j = this.c;
        Activity activity = this.d;
        if (i == bh.b) {
            br b2 = bsVar.b();
            b2.a(Long.valueOf(j));
            lensApi.a(b2.a());
            return;
        }
        lensApi.a(activity);
    }
}
