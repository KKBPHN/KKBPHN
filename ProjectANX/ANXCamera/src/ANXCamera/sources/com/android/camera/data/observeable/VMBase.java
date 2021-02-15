package com.android.camera.data.observeable;

public abstract class VMBase {
    public abstract boolean achieveEndOfCycle();

    /* access modifiers changed from: protected */
    public final void judge() {
        if (achieveEndOfCycle()) {
            rollbackData();
        }
    }

    /* access modifiers changed from: protected */
    public void onCleared() {
    }

    public abstract void rollbackData();
}
