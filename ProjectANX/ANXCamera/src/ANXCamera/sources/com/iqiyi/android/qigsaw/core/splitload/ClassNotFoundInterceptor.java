package com.iqiyi.android.qigsaw.core.splitload;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface ClassNotFoundInterceptor {
    Class findClass(String str);
}
