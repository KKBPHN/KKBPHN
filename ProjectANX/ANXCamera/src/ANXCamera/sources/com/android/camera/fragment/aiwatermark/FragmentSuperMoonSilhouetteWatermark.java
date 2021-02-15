package com.android.camera.fragment.aiwatermark;

import com.android.camera.aiwatermark.data.SuperMoonSilhouetteWatermark;
import java.util.List;

public class FragmentSuperMoonSilhouetteWatermark extends FragmentBaseWatermark {
    public static final int FRAGMENT_INFO = 1048569;
    private static final String TAG = "FragmentSuperMoonSilhouetteWatermark";

    public List getWatermarkList() {
        if (this.mData == null) {
            this.mData = new SuperMoonSilhouetteWatermark();
        }
        return this.mData.getForManual();
    }
}
