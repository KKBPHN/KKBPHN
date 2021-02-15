package com.android.camera;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.android.camera.fragment.settings.VideoDenoiseFragment;

public class VideoDenoiseActivity extends BasePreferenceActivity {
    public static final String TAG = "VideoDenoiseFragment";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        String str = "VideoDenoiseFragment";
        if (fragmentManager.findFragmentByTag(str) == null) {
            VideoDenoiseFragment videoDenoiseFragment = new VideoDenoiseFragment();
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            beginTransaction.add(16908290, videoDenoiseFragment, str);
            beginTransaction.commit();
            this.mPreferenceFragment = videoDenoiseFragment;
        }
    }
}
