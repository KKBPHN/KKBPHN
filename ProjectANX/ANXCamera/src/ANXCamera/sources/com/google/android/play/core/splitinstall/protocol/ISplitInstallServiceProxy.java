package com.google.android.play.core.splitinstall.protocol;

import android.os.Bundle;
import android.os.IInterface;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface ISplitInstallServiceProxy extends IInterface {
    void cancelInstall(String str, int i, Bundle bundle, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy);

    void deferredInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy);

    void deferredUninstall(String str, List list, Bundle bundle, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy);

    void getSessionState(String str, int i, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy);

    void getSessionStates(String str, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy);

    void startInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallbackProxy iSplitInstallServiceCallbackProxy);
}
