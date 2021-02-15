package com.android.camera;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.android.camera.fragment.settings.ProfessionalDisplayFragment;

public class ProfessionalDisplayActivity extends BasePreferenceActivity {
    public static final String TAG = "ProfessionalDisplayActivity";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        String str = ProfessionalDisplayFragment.TAG;
        if (fragmentManager.findFragmentByTag(str) == null) {
            ProfessionalDisplayFragment professionalDisplayFragment = new ProfessionalDisplayFragment();
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            beginTransaction.add(16908290, professionalDisplayFragment, str);
            beginTransaction.commit();
            this.mPreferenceFragment = professionalDisplayFragment;
        }
    }
}
