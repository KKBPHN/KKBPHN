package com.android.camera.fragment.aiwatermark;

import com.android.camera.aiwatermark.data.ScenicSpotsWatermark;
import java.util.List;

public class FragmentSpotsWatermark extends FragmentBaseWatermark {
    public static final int FRAGMENT_INFO = 1048568;

    public List getWatermarkList() {
        if (this.mData == null) {
            this.mData = new ScenicSpotsWatermark();
        }
        return this.mData.getForManual();
    }
}
