package defpackage;

import android.app.Activity;
import com.google.lens.sdk.LensApi;

/* renamed from: bk reason: default package */
public final /* synthetic */ class bk implements Runnable {
    private final LensApi a;
    private final Activity b;
    private final bs c;

    public bk(LensApi lensApi, Activity activity, bs bsVar) {
        this.a = lensApi;
        this.b = activity;
        this.c = bsVar;
    }

    public final void run() {
        this.a.a(this.b, this.c);
    }
}
