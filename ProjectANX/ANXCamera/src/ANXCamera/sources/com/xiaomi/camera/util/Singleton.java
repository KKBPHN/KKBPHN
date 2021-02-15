package com.xiaomi.camera.util;

public abstract class Singleton {
    private Object mInstance;

    public abstract Object create();

    public final Object get() {
        Object obj;
        synchronized (this) {
            if (this.mInstance == null) {
                this.mInstance = create();
            }
            obj = this.mInstance;
        }
        return obj;
    }
}
