package com.android.camera;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import com.android.camera.fragment.settings.SoundSettingFragment;

public class SoundSettingActivity extends BasePreferenceActivity {
    public static final String TAG = "NoiseReductionActivity";

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        String str = SoundSettingFragment.TAG;
        if (fragmentManager.findFragmentByTag(str) == null) {
            SoundSettingFragment soundSettingFragment = new SoundSettingFragment();
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            beginTransaction.add(16908290, soundSettingFragment, str);
            beginTransaction.commit();
            this.mPreferenceFragment = soundSettingFragment;
        }
    }
}
