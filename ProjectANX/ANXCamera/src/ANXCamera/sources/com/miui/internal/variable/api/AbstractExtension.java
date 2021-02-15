package com.miui.internal.variable.api;

public abstract class AbstractExtension {
    private Overridable mExtension;
    private Object mOriginal;

    public void bindOriginal(Object obj) {
        this.mOriginal = obj;
        Overridable overridable = this.mExtension;
        if (overridable != null) {
            overridable.bind(obj);
        }
    }

    public Overridable getExtension() {
        return this.mExtension;
    }

    public void setExtension(Overridable overridable) {
        this.mExtension = overridable;
        Object obj = this.mOriginal;
        if (obj != null) {
            this.mExtension.bind(obj);
        }
    }
}
