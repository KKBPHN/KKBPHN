package com.bumptech.glide.util;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Collection;

public final class Preconditions {
    private Preconditions() {
    }

    public static void checkArgument(boolean z, @NonNull String str) {
        if (!z) {
            throw new IllegalArgumentException(str);
        }
    }

    @NonNull
    public static String checkNotEmpty(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        throw new IllegalArgumentException("Must not be null or empty");
    }

    @NonNull
    public static Collection checkNotEmpty(@NonNull Collection collection) {
        if (!collection.isEmpty()) {
            return collection;
        }
        throw new IllegalArgumentException("Must not be empty.");
    }

    @NonNull
    public static Object checkNotNull(@Nullable Object obj) {
        checkNotNull(obj, "Argument must not be null");
        return obj;
    }

    @NonNull
    public static Object checkNotNull(@Nullable Object obj, @NonNull String str) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(str);
    }
}
