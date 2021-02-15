package com.iqiyi.android.qigsaw.core;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public class DefaultObtainUserConfirmationDialog extends ObtainUserConfirmationDialog {
    private boolean fromUserClick;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (checkInternParametersIllegal()) {
            finish();
        }
    }
}
