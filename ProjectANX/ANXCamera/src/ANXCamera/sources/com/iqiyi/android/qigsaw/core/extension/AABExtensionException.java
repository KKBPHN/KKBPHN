package com.iqiyi.android.qigsaw.core.extension;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public class AABExtensionException extends Exception {
    AABExtensionException(Throwable th) {
        super(th);
    }
}
