package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.text.TextUtils;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ComponentConfigVideoSubQuality extends ComponentData {
    private static List ALL_QUALITY_ITMES = new ArrayList();
    private static final List ALL_QUALITY_VALUE = new ArrayList();
    public static final String QUALITY_1080P = "6";
    public static final String QUALITY_4K = "8";
    public static final String QUALITY_720P = "5";
    public static final String QUALITY_8K = "3001";
    private static final String TAG = "ComponentConfigVideoSubQuality";
    private String[] mForceValue;

    static {
        ALL_QUALITY_VALUE.clear();
        ALL_QUALITY_VALUE.add("8");
        ALL_QUALITY_VALUE.add("6");
        ALL_QUALITY_VALUE.add("5");
        ALL_QUALITY_VALUE.add("3001");
    }

    public ComponentConfigVideoSubQuality(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    private String getSupportMaxValue(String str) {
        if (!(this.mItems == null || this.mItems.size() == 0)) {
            for (int size = this.mItems.size() - 1; size >= 0; size--) {
                if (!((ComponentDataItem) this.mItems.get(size)).mIsDisabled) {
                    return ((ComponentDataItem) this.mItems.get(size)).mValue;
                }
            }
        }
        return str;
    }

    private void initItem(List list, int i, CameraCapabilities cameraCapabilities, List list2) {
        list.clear();
        list2.clear();
        List supportedOutputSizeWithTargetMode = cameraCapabilities.getSupportedOutputSizeWithTargetMode(MediaRecorder.class, 32772);
        if (supportedOutputSizeWithTargetMode.contains(new CameraSize(1280, Util.LIMIT_SURFACE_WIDTH)) && CamcorderProfile.hasProfile(i, 5)) {
            String str = "5";
            list.add(new ComponentDataItem((int) R.drawable.ic_config_720p, (int) R.drawable.ic_config_720p, (int) R.string.pref_video_quality_sub_720p, str));
            list2.add(str);
        }
        if (supportedOutputSizeWithTargetMode.contains(new CameraSize(1920, 1080)) && CamcorderProfile.hasProfile(i, 6)) {
            String str2 = "6";
            list.add(new ComponentDataItem((int) R.drawable.ic_config_1080p, (int) R.drawable.ic_config_1080p, (int) R.string.pref_video_quality_sub_1080p, str2));
            list2.add(str2);
        }
        int i2 = CameraSettings.get4kProfile();
        if (C0124O00000oO.Oo0O00o() && supportedOutputSizeWithTargetMode.contains(new CameraSize(3840, 2160)) && CamcorderProfile.hasProfile(i, i2)) {
            String str3 = "8";
            list.add(new ComponentDataItem((int) R.drawable.ic_config_4k, (int) R.drawable.ic_config_4k, (int) R.string.pref_video_quality_sub_4k, str3));
            list2.add(str3);
        }
        int i3 = CameraSettings.get8kProfile();
        if (C0124O00000oO.Oo0O00o() && supportedOutputSizeWithTargetMode.contains(new CameraSize(7680, 4320)) && CamcorderProfile.hasProfile(i, i3)) {
            String str4 = "3001";
            list.add(new ComponentDataItem((int) R.drawable.ic_config_8k, (int) R.drawable.ic_config_8k, (int) R.string.pref_video_quality_sub_8k, str4));
            list2.add(str4);
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.data.data.ComponentDataItem>, for r4v0, types: [java.util.List, java.util.List<com.android.camera.data.data.ComponentDataItem>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isContain(String str, List<ComponentDataItem> list) {
        if (list == null || list.size() == 0) {
            return false;
        }
        for (ComponentDataItem componentDataItem : list) {
            if (TextUtils.equals(str, componentDataItem.mValue) && !componentDataItem.mIsDisabled) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean checkValueValid(int i, String str) {
        if (isContain(str, this.mItems)) {
            return true;
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("checkValueValid: invalid value: ");
        sb.append(str);
        Log.d(str2, sb.toString());
        return false;
    }

    public boolean disableUpdate() {
        return false;
    }

    public String getComponentValue(int i) {
        String defaultValue = getDefaultValue(i);
        String string = this.mParentDataItem.getString(getKey(i), defaultValue);
        String[] strArr = this.mForceValue;
        if (strArr != null) {
            if (!Arrays.asList(strArr).contains(string)) {
                String[] strArr2 = this.mForceValue;
                string = strArr2[strArr2.length - 1];
            }
            return string;
        }
        if (string != null && !checkValueValid(i, string)) {
            String simpleName = ComponentConfigVideoSubQuality.class.getSimpleName();
            StringBuilder sb = new StringBuilder();
            sb.append("reset invalid value ");
            sb.append(string);
            Log.e(simpleName, sb.toString());
            string = getSupportMaxValue(defaultValue);
        }
        return string;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return "6";
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        if (this.mItems == null) {
            Log.e(TAG, "List is empty!");
        }
        return this.mItems == null ? Collections.emptyList() : this.mItems;
    }

    public String getKey(int i) {
        return CameraSettings.KEY_VIDEO_SUB_QUALITY;
    }

    public boolean isForceQuality() {
        return this.mForceValue != null;
    }

    public boolean isSupport1080P() {
        for (int i = 0; i < ALL_QUALITY_ITMES.size(); i++) {
            if (((ComponentDataItem) ALL_QUALITY_ITMES.get(i)).mValue == "6") {
                return true;
            }
        }
        return false;
    }

    public boolean isSupport4K() {
        for (int i = 0; i < ALL_QUALITY_ITMES.size(); i++) {
            if (((ComponentDataItem) ALL_QUALITY_ITMES.get(i)).mValue == "8") {
                return true;
            }
        }
        return false;
    }

    public boolean isSupport8K() {
        for (int i = 0; i < ALL_QUALITY_ITMES.size(); i++) {
            if (((ComponentDataItem) ALL_QUALITY_ITMES.get(i)).mValue == "3001") {
                return true;
            }
        }
        return false;
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities, int i3) {
        String[] strArr;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (i == 162 || i == 180) {
            this.mForceValue = null;
            initItem(arrayList, i2, cameraCapabilities, arrayList2);
            if (arrayList2.size() > 0 && i2 == 0) {
                List list = ALL_QUALITY_ITMES;
                if (list == null || list.size() == 0) {
                    ArrayList arrayList3 = new ArrayList();
                    initItem(arrayList3, 0, Camera2DataContainer.getInstance().getCapabilities(0), new ArrayList());
                    copyList(arrayList3, ALL_QUALITY_ITMES);
                }
                filterSupprotedItems(ALL_QUALITY_ITMES, ALL_QUALITY_VALUE);
                copyList(ALL_QUALITY_ITMES, arrayList);
                if (!(i == 162 || cameraCapabilities.getCameraId() == Camera2DataContainer.getInstance().getMainBackCameraId() || cameraCapabilities.getCameraId() == Camera2DataContainer.getInstance().getVideoSATCameraId())) {
                    filterSupprotedItems((List) arrayList, (List) arrayList2);
                }
                if (i == 162 && (cameraCapabilities.getCameraId() == Camera2DataContainer.getInstance().getUltraWideCameraId() || cameraCapabilities.getCameraId() == Camera2DataContainer.getInstance().getStandaloneMacroCameraId() || cameraCapabilities.isTeleMacro(i))) {
                    filterSupprotedItems((List) arrayList, (List) arrayList2);
                }
            }
            String str = "6";
            if (CameraSettings.isAutoZoomEnabled(i)) {
                filterSupprotedItems((List) arrayList, str);
                this.mForceValue = new String[]{str};
            }
            String str2 = "5";
            if (CameraSettings.isAiEnhancedVideoEnabled(i)) {
                filterSupprotedItems((List) arrayList, str, str2);
                this.mForceValue = new String[]{str, str2};
            }
            boolean isVhdrOn = CameraSettings.isVhdrOn(cameraCapabilities, i);
            String str3 = "8";
            if (isVhdrOn) {
                filterSupprotedItems((List) arrayList, str2, str, str3);
                this.mForceValue = new String[]{str2, str, str3};
            }
            if (CameraSettings.isSuperEISEnabled(i)) {
                filterSupprotedItems((List) arrayList, str);
                this.mForceValue = new String[]{str};
            }
            if (CameraSettings.isMasterFilterOn(i)) {
                if (CameraSettings.getVideoMasterFilter() == 200) {
                    filterSupprotedItems((List) arrayList, str2);
                    strArr = new String[]{str2};
                } else {
                    filterSupprotedItems((List) arrayList, str, str2);
                    strArr = new String[]{str, str2};
                }
                this.mForceValue = strArr;
            }
            if (CameraSettings.isFaceBeautyOn(i, null)) {
                filterSupprotedItems((List) arrayList, str2);
                this.mForceValue = new String[]{str2};
            }
            if (CameraSettings.isVideoBokehOn()) {
                filterSupprotedItems((List) arrayList, str2);
                this.mForceValue = new String[]{str2};
            }
            if (CameraSettings.isProVideoLogOpen(i)) {
                filterSupprotedItems((List) arrayList, str3);
                this.mForceValue = new String[]{str3};
            }
        }
        this.mItems = Collections.unmodifiableList(arrayList);
    }

    public void reset() {
        List list = ALL_QUALITY_ITMES;
        if (list != null) {
            list.clear();
        }
        if (this.mParentDataItem.getString(getKey(160), "").equals("3001")) {
            this.mParentDataItem.remove(getKey(160));
        }
    }
}
