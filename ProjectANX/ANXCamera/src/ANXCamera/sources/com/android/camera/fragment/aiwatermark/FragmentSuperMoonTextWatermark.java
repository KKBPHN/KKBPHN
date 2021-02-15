package com.android.camera.fragment.aiwatermark;

import com.android.camera.aiwatermark.data.SuperMoonTextWatermark;
import java.util.List;

public class FragmentSuperMoonTextWatermark extends FragmentBaseWatermark {
    public static final int FRAGMENT_INFO = 1048570;
    private static final String TAG = "FragmentSuperMoonTextWatermark";

    public List getWatermarkList() {
        if (this.mData == null) {
            this.mData = new SuperMoonTextWatermark();
        }
        return this.mData.getForManual();
    }
}
