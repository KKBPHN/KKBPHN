package io.reactivex.internal.util;

import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public enum ArrayListSupplier implements Callable, Function {
    INSTANCE;

    public static Callable asCallable() {
        return INSTANCE;
    }

    public static Function asFunction() {
        return INSTANCE;
    }

    public List apply(Object obj) {
        return new ArrayList();
    }

    public List call() {
        return new ArrayList();
    }
}
