package com.android.camera.data.data.runing;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.text.TextUtils;
import androidx.collection.SimpleArrayMap;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentRunningLighting extends ComponentData {
    private CameraCapabilities mCapabilities;
    private SimpleArrayMap mTotalDataItems = new SimpleArrayMap();

    public ComponentRunningLighting(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
        String str = "1";
        this.mTotalDataItems.put(str, new ComponentDataItem((int) R.drawable.ic_lighting_nature, (int) R.drawable.ic_lighting_nature, (int) R.string.lighting_pattern_nature, str));
        String str2 = "2";
        this.mTotalDataItems.put(str2, new ComponentDataItem((int) R.drawable.ic_lighting_stage, (int) R.drawable.ic_lighting_stage, (int) R.string.lighting_pattern_stage, str2));
        String str3 = "3";
        this.mTotalDataItems.put(str3, new ComponentDataItem((int) R.drawable.ic_lighting_movie, (int) R.drawable.ic_lighting_movie, (int) R.string.lighting_pattern_movie, str3));
        String str4 = "4";
        this.mTotalDataItems.put(str4, new ComponentDataItem((int) R.drawable.ic_lighting_rainbow, (int) R.drawable.ic_lighting_rainbow, (int) R.string.lighting_pattern_rainbow, str4));
        String str5 = "5";
        this.mTotalDataItems.put(str5, new ComponentDataItem((int) R.drawable.ic_lighting_shutter, (int) R.drawable.ic_lighting_shutter, (int) R.string.lighting_pattern_shutter, str5));
        String str6 = "6";
        this.mTotalDataItems.put(str6, new ComponentDataItem((int) R.drawable.ic_lighting_dot, (int) R.drawable.ic_lighting_dot, (int) R.string.lighting_pattern_dot, str6));
        String str7 = "7";
        this.mTotalDataItems.put(str7, new ComponentDataItem((int) R.drawable.ic_lighting_leaf, (int) R.drawable.ic_lighting_leaf, (int) R.string.lighting_pattern_leaf, str7));
        String str8 = "8";
        this.mTotalDataItems.put(str8, new ComponentDataItem((int) R.drawable.ic_lighting_holi, (int) R.drawable.ic_lighting_holi, (int) R.string.lighting_pattern_holi, str8));
        this.mTotalDataItems.put("9", new ComponentDataItem((int) R.drawable.ic_2_lighting_neon, (int) R.drawable.ic_2_lighting_neon, (int) R.string.lighting_neon, "9"));
        this.mTotalDataItems.put("10", new ComponentDataItem((int) R.drawable.ic_2_lighting_phantom, (int) R.drawable.ic_2_lighting_phantom, (int) R.string.lighting_phantom, "10"));
        this.mTotalDataItems.put("11", new ComponentDataItem((int) R.drawable.ic_2_lighting_nostalgia, (int) R.drawable.ic_2_lighting_nostalgia, (int) R.string.lighting_nostalgia, "11"));
        this.mTotalDataItems.put("12", new ComponentDataItem((int) R.drawable.ic_2_lighting_rainbow, (int) R.drawable.ic_2_lighting_rainbow, (int) R.string.lighting_rainbow, "12"));
        this.mTotalDataItems.put("13", new ComponentDataItem((int) R.drawable.ic_2_lighting_lanshan, (int) R.drawable.ic_2_lighting_lanshan, (int) R.string.lighting_lanshan, "13"));
        this.mTotalDataItems.put("14", new ComponentDataItem((int) R.drawable.ic_2_lighting_dazzling, (int) R.drawable.ic_2_lighting_dazzling, (int) R.string.lighting_dazzling, "14"));
        this.mTotalDataItems.put("15", new ComponentDataItem((int) R.drawable.ic_2_lighting_gorgeous, (int) R.drawable.ic_2_lighting_gorgeous, (int) R.string.lighting_gorgeous, "15"));
        this.mTotalDataItems.put("16", new ComponentDataItem((int) R.drawable.ic_2_lighting_bright_red, (int) R.drawable.ic_2_lighting_bright_red, (int) R.string.lighting_bright_red, "16"));
        this.mTotalDataItems.put("17", new ComponentDataItem((int) R.drawable.ic_2_lighting_dreamland, (int) R.drawable.ic_2_lighting_dreamland, (int) R.string.lighting_dreamland, "17"));
    }

    public boolean checkValueValid(int i, String str) {
        List<ComponentDataItem> items = getItems();
        if (items != null) {
            for (ComponentDataItem componentDataItem : items) {
                if (TextUtils.equals(str, componentDataItem.mValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getDefaultValue(int i) {
        return "0";
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        if (this.mItems == null) {
            this.mItems = initItems();
        }
        return this.mItems;
    }

    public String getKey(int i) {
        return "pref_portrait_lighting";
    }

    public int getPortraitLightVersion() {
        CameraCapabilities cameraCapabilities = this.mCapabilities;
        if (cameraCapabilities != null) {
            return cameraCapabilities.getPortraitLightingVersion();
        }
        return 1;
    }

    public List initItems() {
        if (this.mCapabilities == null || this.mTotalDataItems == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        String str = "0";
        arrayList.add(this.mCapabilities.getPortraitLightingVersion() < 2 ? new ComponentDataItem((int) R.drawable.ic_lighting_none, (int) R.drawable.ic_lighting_none, (int) R.string.lighting_pattern_null, str) : new ComponentDataItem((int) R.drawable.ic_2_lighting_none, (int) R.drawable.ic_2_lighting_none, -1, str));
        int[] portraitLightingArray = this.mCapabilities.getPortraitLightingArray();
        if (portraitLightingArray != null) {
            for (int valueOf : portraitLightingArray) {
                Integer valueOf2 = Integer.valueOf(valueOf);
                if (valueOf2 != null) {
                    int intValue = valueOf2.intValue();
                    if (!"1".equals(String.valueOf(intValue)) || CameraSettings.isBackCamera() || Camera2DataContainer.getInstance().getBokehFrontCameraId() != -1) {
                        if (!"8".equals(String.valueOf(intValue)) || C0122O00000o.instance().OOoO00O()) {
                            arrayList.add((ComponentDataItem) this.mTotalDataItems.get(String.valueOf(intValue)));
                        }
                    }
                }
            }
        }
        this.mItems = Collections.unmodifiableList(arrayList);
        return this.mItems;
    }

    public boolean isSwitchOn(int i) {
        return !getComponentValue(i).equals("0");
    }

    public void reInit(CameraCapabilities cameraCapabilities) {
        this.mCapabilities = cameraCapabilities;
        this.mItems = initItems();
    }
}
