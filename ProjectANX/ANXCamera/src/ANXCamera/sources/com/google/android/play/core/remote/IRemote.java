package com.google.android.play.core.remote;

import android.os.IBinder;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface IRemote {
    Object asInterface(IBinder iBinder);
}
