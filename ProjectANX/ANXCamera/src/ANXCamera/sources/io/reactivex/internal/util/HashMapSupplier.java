package io.reactivex.internal.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public enum HashMapSupplier implements Callable {
    INSTANCE;

    public static Callable asCallable() {
        return INSTANCE;
    }

    public Map call() {
        return new HashMap();
    }
}
