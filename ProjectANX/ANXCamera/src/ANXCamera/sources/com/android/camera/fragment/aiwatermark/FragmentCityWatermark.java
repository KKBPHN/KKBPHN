package com.android.camera.fragment.aiwatermark;

import com.android.camera.aiwatermark.data.CityWatermark;
import java.util.List;

public class FragmentCityWatermark extends FragmentBaseWatermark {
    public static final int FRAGMENT_INFO = 1048573;

    public List getWatermarkList() {
        if (this.mData == null) {
            this.mData = new CityWatermark();
        }
        return this.mData.getForManual();
    }
}
