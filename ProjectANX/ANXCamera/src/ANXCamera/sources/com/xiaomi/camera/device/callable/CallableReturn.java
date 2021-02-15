package com.xiaomi.camera.device.callable;

public class CallableReturn {
    public final Exception exception;
    public final Object value;

    public CallableReturn(Exception exc) {
        this.exception = exc;
        this.value = null;
    }

    public CallableReturn(Object obj) {
        this.value = obj;
        this.exception = null;
    }

    public Exception getError() {
        return this.exception;
    }

    public Object getValue() {
        return this.value;
    }
}
