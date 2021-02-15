package com.iqiyi.android.qigsaw.core.splitload;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.extension.ContentProviderProxy;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class SplitContentProvider extends ContentProviderProxy {
    /* access modifiers changed from: protected */
    public boolean checkRealContentProviderInstallStatus(String str) {
        boolean z = true;
        if (getRealContentProvider() != null) {
            return true;
        }
        if (!SplitLoadManagerService.hasInstance()) {
            return false;
        }
        SplitLoadManagerService.getInstance().loadInstalledSplits();
        if (getRealContentProvider() == null) {
            z = false;
        }
        return z;
    }
}
