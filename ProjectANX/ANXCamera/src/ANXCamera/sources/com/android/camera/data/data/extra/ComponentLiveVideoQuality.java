package com.android.camera.data.data.extra;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.graphics.SurfaceTexture;
import androidx.annotation.NonNull;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentLiveVideoQuality extends ComponentData {
    public static final String QUALITY_1080P = "6";
    public static final String QUALITY_720P = "5";
    public static final int SUPPORT_1080P_ONLY = 2;
    public static final int SUPPORT_720P_AND_1080P = 0;
    public static final int SUPPORT_720P_ONLY = 1;
    private static final String TAG = "ComponentLiveVideoQuality";
    private String mDefaultValue = "5";

    public ComponentLiveVideoQuality(DataItemLive dataItemLive) {
        super(dataItemLive);
    }

    private void initVideoQuality(CameraCapabilities cameraCapabilities, List list) {
        List supportedOutputSizeWithAssignedMode = cameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class);
        int O0oooo0 = C0122O00000o.instance().O0oooo0();
        boolean z = false;
        boolean z2 = O0oooo0 != 2 && supportedOutputSizeWithAssignedMode.contains(new CameraSize(1280, Util.LIMIT_SURFACE_WIDTH));
        if (O0oooo0 != 1 && supportedOutputSizeWithAssignedMode.contains(new CameraSize(1920, 1080))) {
            z = true;
        }
        String str = "6";
        String str2 = "5";
        if (z2 && z) {
            list.add(new ComponentDataItem((int) R.drawable.ic_config_720p_30, (int) R.drawable.ic_config_720p_30, (int) R.string.pref_video_quality_entry_720p, str2));
            list.add(new ComponentDataItem((int) R.drawable.ic_config_1080p_30, (int) R.drawable.ic_config_1080p_30, (int) R.string.pref_video_quality_entry_1080p, str));
        } else if (z2) {
            list.add(new ComponentDataItem((int) R.drawable.ic_config_720p_30, (int) R.drawable.ic_config_720p_30, (int) R.string.pref_video_quality_entry_720p, str2));
        } else if (z) {
            list.add(new ComponentDataItem((int) R.drawable.ic_config_1080p_30, (int) R.drawable.ic_config_1080p_30, (int) R.string.pref_video_quality_entry_1080p, str));
            this.mDefaultValue = str;
            return;
        } else {
            return;
        }
        this.mDefaultValue = str2;
    }

    public boolean disableUpdate() {
        return this.mItems == null || this.mItems.size() == 1;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return this.mDefaultValue;
    }

    public int getDisplayTitleString() {
        return R.string.pref_video_quality_title;
    }

    public List getItems() {
        if (this.mItems == null) {
            Log.e(TAG, "List is empty!");
        }
        return this.mItems == null ? Collections.emptyList() : this.mItems;
    }

    public String getKey(int i) {
        return CameraSettings.KEY_MI_LIVE_QUALITY;
    }

    public boolean isSupportVideoQuality(int i, int i2) {
        CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(i2);
        if (capabilities == null) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        initVideoQuality(capabilities, arrayList);
        String componentValue = getComponentValue(i);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            if (((ComponentDataItem) arrayList.get(i3)).mValue.equals(componentValue)) {
                return true;
            }
        }
        return false;
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities) {
        ArrayList arrayList = new ArrayList();
        initVideoQuality(cameraCapabilities, arrayList);
        this.mItems = Collections.unmodifiableList(arrayList);
    }
}
