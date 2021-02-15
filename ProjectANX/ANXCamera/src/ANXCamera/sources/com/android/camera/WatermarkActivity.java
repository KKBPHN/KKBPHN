package com.android.camera;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.android.camera.fragment.settings.WatermarkFragment;

public class WatermarkActivity extends BasePreferenceActivity {
    public static final String TAG = "WatermarkActivity";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        String str = WatermarkFragment.TAG;
        if (fragmentManager.findFragmentByTag(str) == null) {
            WatermarkFragment watermarkFragment = new WatermarkFragment();
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            beginTransaction.add(16908290, watermarkFragment, str);
            beginTransaction.commit();
            this.mPreferenceFragment = watermarkFragment;
        }
    }
}
