package com.google.android.play.core.splitinstall;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.concurrent.atomic.AtomicReference;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class SplitSessionLoaderSingleton {
    private static final AtomicReference sSplitLoaderHolder = new AtomicReference();

    static SplitSessionLoader get() {
        return (SplitSessionLoader) sSplitLoaderHolder.get();
    }

    public static void set(SplitSessionLoader splitSessionLoader) {
        sSplitLoaderHolder.compareAndSet(null, splitSessionLoader);
    }
}
