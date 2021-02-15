package com.android.camera.fragment.aiwatermark;

import com.android.camera.aiwatermark.data.ASDWatermark;
import java.util.List;

public class FragmentASDWatermark extends FragmentBaseWatermark {
    public static final int FRAGMENT_INFO = 1048572;

    public List getWatermarkList() {
        if (this.mData == null) {
            this.mData = new ASDWatermark();
        }
        return this.mData.getForManual();
    }
}
