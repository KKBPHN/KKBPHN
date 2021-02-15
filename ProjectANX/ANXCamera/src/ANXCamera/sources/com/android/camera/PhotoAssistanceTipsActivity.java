package com.android.camera;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.android.camera.fragment.settings.PhotoAssistanceTipsFragment;

public class PhotoAssistanceTipsActivity extends BasePreferenceActivity {
    private static final String TAG = "PhotoAssistanceTipsActivity";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.findFragmentByTag(PhotoAssistanceTipsFragment.TAG) == null) {
            PhotoAssistanceTipsFragment photoAssistanceTipsFragment = new PhotoAssistanceTipsFragment();
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            beginTransaction.add(16908290, photoAssistanceTipsFragment, PhotoAssistanceTipsFragment.TAG);
            beginTransaction.commit();
            this.mPreferenceFragment = photoAssistanceTipsFragment;
        }
    }
}
