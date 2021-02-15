package com.android.camera;

import com.android.camera.fragment.settings.BasePreferenceFragment;
import com.android.camera.log.Log;
import miui.app.Activity;

public abstract class BasePreferenceActivity extends Activity {
    private static final String TAG = "BasePreferenceActivity";
    protected BasePreferenceFragment mPreferenceFragment;

    public void onBackPressed() {
        super.onBackPressed();
        if (this.mPreferenceFragment != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onBackPressed: ");
            sb.append(this.mPreferenceFragment.getClass().getName());
            Log.u(str, sb.toString());
            this.mPreferenceFragment.onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        BasePreferenceFragment basePreferenceFragment = this.mPreferenceFragment;
        if (basePreferenceFragment != null) {
            basePreferenceFragment.onRestart();
        }
    }
}
