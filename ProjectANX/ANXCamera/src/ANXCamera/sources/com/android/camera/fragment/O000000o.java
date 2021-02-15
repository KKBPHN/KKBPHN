package com.android.camera.fragment;

import com.android.camera.fragment.lifeCircle.BaseLifecycleListener;
import io.reactivex.functions.Action;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements Action {
    private final /* synthetic */ BaseLifecycleListener O0OOoO0;

    public /* synthetic */ O000000o(BaseLifecycleListener baseLifecycleListener) {
        this.O0OOoO0 = baseLifecycleListener;
    }

    public final void run() {
        this.O0OOoO0.onLifeAlive();
    }
}
