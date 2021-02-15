package com.google.android.play.core.splitinstall.protocol;

import android.os.IBinder;
import android.os.IInterface;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.google.android.binder.BinderWrapper;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class ISplitInstallServiceHolder extends BinderWrapper implements ISplitInstallServiceProxy {
    protected ISplitInstallServiceHolder(String str) {
        super(str);
    }

    public static ISplitInstallServiceProxy queryLocalInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallService");
        return queryLocalInterface instanceof ISplitInstallServiceProxy ? (ISplitInstallServiceProxy) queryLocalInterface : new ISplitInstallServiceImpl(iBinder);
    }
}
