package com.android.camera.module;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.O000OoO reason: case insensitive filesystem */
public final /* synthetic */ class C0364O000OoO implements Runnable {
    private final /* synthetic */ TimeFreezeModule O0OOoO0;

    public /* synthetic */ C0364O000OoO(TimeFreezeModule timeFreezeModule) {
        this.O0OOoO0 = timeFreezeModule;
    }

    public final void run() {
        this.O0OOoO0.onHostStopAndNotifyActionStop();
    }
}
