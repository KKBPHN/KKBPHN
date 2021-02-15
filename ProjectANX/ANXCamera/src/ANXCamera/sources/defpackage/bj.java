package defpackage;

import android.app.Activity;
import com.google.lens.sdk.LensApi;

/* renamed from: bj reason: default package */
public final /* synthetic */ class bj implements Runnable {
    private final LensApi a;
    private final Activity b;

    public bj(LensApi lensApi, Activity activity) {
        this.a = lensApi;
        this.b = activity;
    }

    public final void run() {
        this.a.a(this.b);
    }
}
