package com.google.android.play.core.splitinstall.protocol;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.google.android.binder.IInterfaceProxy;
import com.google.android.binder.ParcelHelper;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public class ISplitInstallServiceImpl extends IInterfaceProxy implements ISplitInstallServiceProxy {
    ISplitInstallServiceImpl(IBinder iBinder) {
        super(iBinder, "com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallService");
    }

    public void cancelInstall(String str, int i, Bundle bundle, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy) {
        Parcel obtainData = obtainData();
        obtainData.writeString(str);
        obtainData.writeInt(i);
        ParcelHelper.writeToParcel(obtainData, bundle);
        ParcelHelper.writeStrongBinder(obtainData, iSplitInstallServiceCallbackProxy);
        transact(2, obtainData);
    }

    public void deferredInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy) {
        Parcel obtainData = obtainData();
        obtainData.writeString(str);
        obtainData.writeTypedList(list);
        ParcelHelper.writeToParcel(obtainData, bundle);
        ParcelHelper.writeStrongBinder(obtainData, iSplitInstallServiceCallbackProxy);
        transact(5, obtainData);
    }

    public void deferredUninstall(String str, List list, Bundle bundle, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy) {
        Parcel obtainData = obtainData();
        obtainData.writeString(str);
        obtainData.writeTypedList(list);
        ParcelHelper.writeToParcel(obtainData, bundle);
        ParcelHelper.writeStrongBinder(obtainData, iSplitInstallServiceCallbackProxy);
        transact(6, obtainData);
    }

    public void getSessionState(String str, int i, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy) {
        Parcel obtainData = obtainData();
        obtainData.writeString(str);
        obtainData.writeInt(i);
        ParcelHelper.writeStrongBinder(obtainData, iSplitInstallServiceCallbackProxy);
        transact(3, obtainData);
    }

    public void getSessionStates(String str, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy) {
        Parcel obtainData = obtainData();
        obtainData.writeString(str);
        ParcelHelper.writeStrongBinder(obtainData, iSplitInstallServiceCallbackProxy);
        transact(4, obtainData);
    }

    public final void startInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy) {
        Parcel obtainData = obtainData();
        obtainData.writeString(str);
        obtainData.writeTypedList(list);
        ParcelHelper.writeToParcel(obtainData, bundle);
        ParcelHelper.writeStrongBinder(obtainData, iSplitInstallServiceCallbackProxy);
        transact(1, obtainData);
    }
}
