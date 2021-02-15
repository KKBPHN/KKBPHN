package com.android.camera.customization;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.android.camera.BasePreferenceActivity;

public class ActivityCustomSound extends BasePreferenceActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        String str = FragmentCustomSound.TAG;
        if (fragmentManager.findFragmentByTag(str) == null) {
            FragmentCustomSound fragmentCustomSound = new FragmentCustomSound();
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            beginTransaction.add(16908290, fragmentCustomSound, str);
            beginTransaction.commit();
            this.mPreferenceFragment = fragmentCustomSound;
        }
    }
}
