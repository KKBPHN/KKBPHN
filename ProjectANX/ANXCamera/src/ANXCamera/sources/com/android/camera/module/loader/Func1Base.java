package com.android.camera.module.loader;

import io.reactivex.functions.Function;

public abstract class Func1Base implements Function {
    protected int mTargetMode;

    public Func1Base(int i) {
        this.mTargetMode = i;
    }
}
