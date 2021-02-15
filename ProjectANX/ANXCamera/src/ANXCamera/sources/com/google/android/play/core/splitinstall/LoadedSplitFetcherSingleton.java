package com.google.android.play.core.splitinstall;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.concurrent.atomic.AtomicReference;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class LoadedSplitFetcherSingleton {
    private static final AtomicReference sInstalledSplitsFetcherRef = new AtomicReference(null);

    static LoadedSplitFetcher get() {
        return (LoadedSplitFetcher) sInstalledSplitsFetcherRef.get();
    }

    public static void set(LoadedSplitFetcher loadedSplitFetcher) {
        sInstalledSplitsFetcherRef.compareAndSet(null, loadedSplitFetcher);
    }
}
