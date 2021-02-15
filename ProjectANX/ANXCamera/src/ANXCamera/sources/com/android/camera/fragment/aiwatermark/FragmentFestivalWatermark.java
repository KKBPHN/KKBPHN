package com.android.camera.fragment.aiwatermark;

import com.android.camera.aiwatermark.data.FestivalWatermark;
import java.util.List;

public class FragmentFestivalWatermark extends FragmentBaseWatermark {
    public static final int FRAGMENT_INFO = 1048571;

    public List getWatermarkList() {
        if (this.mData == null) {
            this.mData = new FestivalWatermark();
        }
        return this.mData.getForManual();
    }
}
