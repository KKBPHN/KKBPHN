package com.android.camera;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.android.camera.fragment.settings.CameraPreferenceFragment;

public class CameraPreferenceActivity extends BasePreferenceActivity {
    public static final String TAG = "CameraPreferenceActivity";
    private int mFromWhere;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.findFragmentByTag(CameraPreferenceFragment.TAG) == null) {
            CameraPreferenceFragment cameraPreferenceFragment = new CameraPreferenceFragment();
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            beginTransaction.add(16908290, cameraPreferenceFragment, CameraPreferenceFragment.TAG);
            beginTransaction.commit();
            this.mPreferenceFragment = cameraPreferenceFragment;
        }
    }
}
