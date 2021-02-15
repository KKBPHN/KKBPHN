package com.airbnb.lottie;

import android.content.Context;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

/* renamed from: com.airbnb.lottie.O0000oOO reason: case insensitive filesystem */
class C0068O0000oOO implements Callable {
    final /* synthetic */ int O000oOo;
    final /* synthetic */ WeakReference O000oOo0;
    final /* synthetic */ Context val$appContext;

    C0068O0000oOO(WeakReference weakReference, Context context, int i) {
        this.O000oOo0 = weakReference;
        this.val$appContext = context;
        this.O000oOo = i;
    }

    public C0086O000Ooo call() {
        Context context = (Context) this.O000oOo0.get();
        if (context == null) {
            context = this.val$appContext;
        }
        return C0096O00oOooo.O00000Oo(context, this.O000oOo);
    }
}
