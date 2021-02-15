package com.iqiyi.android.qigsaw.core.splitload;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class SplitLoad {
    public static final int MULTIPLE_CLASSLOADER = 1;
    public static final int SINGLE_CLASSLOADER = 2;

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SplitLoadMode {
    }
}
