package com.google.android.play.core.remote;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface OnBinderDiedListener {
    void onBinderDied();
}
