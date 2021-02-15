package com.iqiyi.android.qigsaw.core.splitload.listener;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface OnSplitLoadListener {
    void onCompleted();

    void onFailed(int i);
}
