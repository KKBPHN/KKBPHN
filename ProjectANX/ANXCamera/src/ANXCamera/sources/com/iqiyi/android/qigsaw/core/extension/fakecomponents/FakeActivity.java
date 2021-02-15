package com.iqiyi.android.qigsaw.core.extension.fakecomponents;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public class FakeActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        int releaseFixedOrientation = OrientationCompat.releaseFixedOrientation(this);
        super.onCreate(bundle);
        OrientationCompat.fixedOrientation(this, releaseFixedOrientation);
        if (getIntent() != null) {
            setIntent(null);
        }
        finish();
    }
}
