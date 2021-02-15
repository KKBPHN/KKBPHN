package com.android.camera.module.loader;

public final class NullHolder {
    private int mException;
    private final Object mValue;

    private NullHolder(Object obj, int i) {
        this.mValue = obj;
        this.mException = i;
    }

    private static NullHolder of(Object obj, int i) {
        return new NullHolder(obj, i);
    }

    public static final NullHolder ofNullable(Object obj) {
        return of(obj, 224);
    }

    public static final NullHolder ofNullable(Object obj, int i) {
        return of(obj, i);
    }

    public Object get() {
        return this.mValue;
    }

    public int getException() {
        return this.mException;
    }

    public boolean isPresent() {
        return this.mValue != null;
    }

    public void setException(int i) {
        this.mException = i;
    }
}
