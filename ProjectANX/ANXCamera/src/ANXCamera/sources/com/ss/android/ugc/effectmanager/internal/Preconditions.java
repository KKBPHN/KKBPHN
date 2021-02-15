package com.ss.android.ugc.effectmanager.internal;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class Preconditions {
    @NonNull
    public static Object checkNotNull(Object obj) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException();
    }

    @NonNull
    public static Object checkNotNull(Object obj, Object obj2) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(String.valueOf(obj2));
    }
}
