package com.airbnb.lottie.model;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.VisibleForTesting;
import androidx.collection.LruCache;
import com.airbnb.lottie.C0064O0000o0O;

@RestrictTo({Scope.LIBRARY})
public class O0000O0o {
    private static final O0000O0o INSTANCE = new O0000O0o();
    private final LruCache cache = new LruCache(20);

    @VisibleForTesting
    O0000O0o() {
    }

    public static O0000O0o getInstance() {
        return INSTANCE;
    }

    public void O000000o(@Nullable String str, C0064O0000o0O o0000o0O) {
        if (str != null) {
            this.cache.put(str, o0000o0O);
        }
    }

    public void clear() {
        this.cache.evictAll();
    }

    @Nullable
    public C0064O0000o0O get(@Nullable String str) {
        if (str == null) {
            return null;
        }
        return (C0064O0000o0O) this.cache.get(str);
    }

    public void resize(int i) {
        this.cache.resize(i);
    }
}
