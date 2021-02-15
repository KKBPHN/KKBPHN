package com.google.android.play.core.splitinstall.protocol;

import android.os.Bundle;
import android.os.IInterface;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface ISplitInstallServiceCallbackProxy extends IInterface {
    void onCancelInstall(int i, Bundle bundle);

    void onCompleteInstall(int i);

    void onDeferredInstall(Bundle bundle);

    void onDeferredUninstall(Bundle bundle);

    void onError(Bundle bundle);

    void onGetSession(int i, Bundle bundle);

    void onGetSessionStates(List list);

    void onStartInstall(int i, Bundle bundle);
}
