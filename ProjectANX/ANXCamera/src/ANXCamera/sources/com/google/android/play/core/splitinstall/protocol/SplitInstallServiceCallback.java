package com.google.android.play.core.splitinstall.protocol;

import android.os.Bundle;
import android.os.Parcel;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.google.android.binder.BinderWrapper;
import com.google.android.binder.ParcelHelper;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class SplitInstallServiceCallback extends BinderWrapper implements ISplitInstallServiceCallbackProxy {
    protected SplitInstallServiceCallback() {
        super("com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback");
    }

    /* access modifiers changed from: protected */
    public final boolean dispatchTransact(int i, Parcel parcel) {
        switch (i) {
            case 1:
                onStartInstall(parcel.readInt(), (Bundle) ParcelHelper.createFromParcel(parcel, Bundle.CREATOR));
                break;
            case 2:
                int readInt = parcel.readInt();
                ParcelHelper.createFromParcel(parcel, Bundle.CREATOR);
                onCompleteInstall(readInt);
                break;
            case 3:
                onCancelInstall(parcel.readInt(), (Bundle) ParcelHelper.createFromParcel(parcel, Bundle.CREATOR));
                break;
            case 4:
                onGetSession(parcel.readInt(), (Bundle) ParcelHelper.createFromParcel(parcel, Bundle.CREATOR));
                break;
            case 5:
                onDeferredUninstall((Bundle) ParcelHelper.createFromParcel(parcel, Bundle.CREATOR));
                break;
            case 6:
                onDeferredInstall((Bundle) ParcelHelper.createFromParcel(parcel, Bundle.CREATOR));
                break;
            case 7:
                onGetSessionStates(parcel.createTypedArrayList(Bundle.CREATOR));
                break;
            case 8:
                onError((Bundle) ParcelHelper.createFromParcel(parcel, Bundle.CREATOR));
                break;
            default:
                return false;
        }
        return true;
    }
}
