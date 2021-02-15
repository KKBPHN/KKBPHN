package com.google.android.play.core.splitinstall;

import android.os.IBinder;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.google.android.play.core.remote.IRemote;
import com.google.android.play.core.splitinstall.protocol.ISplitInstallServiceHolder;
import com.google.android.play.core.splitinstall.protocol.ISplitInstallServiceProxy;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitRemoteImpl implements IRemote {
    static final IRemote sInstance = new SplitRemoteImpl();

    public ISplitInstallServiceProxy asInterface(IBinder iBinder) {
        return ISplitInstallServiceHolder.queryLocalInterface(iBinder);
    }
}
