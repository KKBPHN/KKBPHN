package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.graphics.SurfaceTexture;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class ComponentConfigVideoQuality extends ComponentData {
    private static List ITEMS_FROM_WIDE_FAST_MOTION = new ArrayList();
    private static List ITEMS_FROM_WIDE_RECORD_VIDEO = new ArrayList();
    public static final String QUALITY_1080P = "6";
    public static final String QUALITY_1080P_60FPS = "6,60";
    public static final String QUALITY_4K = "8";
    public static final String QUALITY_4K_60FPS = "8,60";
    public static final String QUALITY_720P = "5";
    public static final String QUALITY_8K = "3001";
    public static final String QUALITY_8K_24FPS = "3001,24";
    private static final List QUALITY_ALL = new ArrayList();
    private static final String TAG = "ComponentConfigVideoQuality";
    private ComponentConfigVideoSubFPS mComponentConfigVideoSubFps;
    private ComponentConfigVideoSubQuality mComponentConfigVideoSubQuality;
    private int mConfigType = 208;
    private String mDefaultValue = "6";

    static {
        QUALITY_ALL.clear();
        QUALITY_ALL.add(QUALITY_4K_60FPS);
        QUALITY_ALL.add("8");
        QUALITY_ALL.add(QUALITY_1080P_60FPS);
        QUALITY_ALL.add("6");
        QUALITY_ALL.add("5");
    }

    public ComponentConfigVideoQuality(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
        this.mComponentConfigVideoSubQuality = new ComponentConfigVideoSubQuality(dataItemConfig);
        this.mComponentConfigVideoSubFps = new ComponentConfigVideoSubFPS(dataItemConfig);
    }

    private void filterAllQualities(List list, int i, CameraCapabilities cameraCapabilities, int i2, List list2, List list3) {
        boolean z = cameraCapabilities.getCameraId() == Camera2DataContainer.getInstance().getMainBackCameraId() || Camera2DataContainer.getInstance().getVideoSATCameraId() == cameraCapabilities.getCameraId();
        if (i2 == 0 && ((list3 == null || list3.size() == 0) && z)) {
            copyList(list, list3);
        }
        if (list2.size() > 0 && i == 0 && list3 != null && list3.size() > 0) {
            filterSupprotedItems(list3, QUALITY_ALL);
            copyList(list3, list);
            if (!z && cameraCapabilities.getCameraId() != Camera2DataContainer.getInstance().getAuxCameraId() && cameraCapabilities.getCameraId() != Camera2DataContainer.getInstance().getUltraTeleCameraId()) {
                filterSupprotedItems(list, list2);
            }
        }
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getQualityMappedValue(String str) {
        char c;
        switch (str.hashCode()) {
            case -561920624:
                if (str.equals(QUALITY_8K_24FPS)) {
                    c = 5;
                    break;
                }
            case 53:
                if (str.equals("5")) {
                    c = 0;
                    break;
                }
            case 54:
                if (str.equals("6")) {
                    c = 1;
                    break;
                }
            case 56:
                if (str.equals("8")) {
                    c = 3;
                    break;
                }
            case 1567006:
                if (str.equals("3001")) {
                    c = 6;
                    break;
                }
            case 1652720:
                if (str.equals(QUALITY_1080P_60FPS)) {
                    c = 2;
                    break;
                }
            case 1712302:
                if (str.equals(QUALITY_4K_60FPS)) {
                    c = 4;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 8;
            case 4:
                return 16;
            case 5:
                return 32;
            case 6:
                return 64;
            default:
                return 0;
        }
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

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a6  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List initItem(List list, int i, int i2, CameraCapabilities cameraCapabilities, int i3) {
        List list2;
        ComponentDataItem componentDataItem;
        List list3 = list;
        int i4 = i;
        ArrayList arrayList = new ArrayList();
        String str = "6";
        if (i4 != 161) {
            if (i4 == 162 || i4 == 169 || i4 == 180) {
                initVideoMode(list, i, i2, cameraCapabilities, i3, arrayList);
                if (i4 != 162) {
                    list2 = ITEMS_FROM_WIDE_RECORD_VIDEO;
                } else {
                    if (i4 == 169) {
                        list2 = ITEMS_FROM_WIDE_FAST_MOTION;
                    }
                    return arrayList;
                }
                filterAllQualities(list, i2, cameraCapabilities, i3, arrayList, list2);
                return arrayList;
            }
            if (i4 != 204) {
                if (i4 != 212) {
                    switch (i4) {
                        case 207:
                            break;
                        case 208:
                            break;
                        case 209:
                        case 210:
                            break;
                    }
                }
                str = "8";
                componentDataItem = new ComponentDataItem((int) R.drawable.ic_config_4k_30, (int) R.drawable.ic_config_4k_30, (int) R.string.pref_video_quality_entry_4kuhd, str);
                list.add(componentDataItem);
                arrayList.add(str);
                CameraCapabilities cameraCapabilities2 = cameraCapabilities;
                if (i4 != 162) {
                }
                filterAllQualities(list, i2, cameraCapabilities, i3, arrayList, list2);
                return arrayList;
            }
            componentDataItem = new ComponentDataItem((int) R.drawable.ic_config_1080p_30, (int) R.drawable.ic_config_1080p_30, (int) R.string.pref_video_quality_entry_1080p, str);
            list.add(componentDataItem);
            arrayList.add(str);
            CameraCapabilities cameraCapabilities22 = cameraCapabilities;
            if (i4 != 162) {
            }
            filterAllQualities(list, i2, cameraCapabilities, i3, arrayList, list2);
            return arrayList;
        }
        List supportedOutputSizeWithAssignedMode = cameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class);
        if (supportedOutputSizeWithAssignedMode.contains(new CameraSize(1280, Util.LIMIT_SURFACE_WIDTH))) {
            String str2 = "5";
            list.add(new ComponentDataItem((int) R.drawable.ic_config_720p_30, (int) R.drawable.ic_config_720p_30, (int) R.string.pref_video_quality_entry_720p, str2));
            arrayList.add(str2);
        }
        if (!C0124O00000oO.O0o0oO0 && !C0124O00000oO.O0o0oO && supportedOutputSizeWithAssignedMode.contains(new CameraSize(1920, 1080))) {
            list.add(new ComponentDataItem((int) R.drawable.ic_config_1080p_30, (int) R.drawable.ic_config_1080p_30, (int) R.string.pref_video_quality_entry_1080p, str));
            arrayList.add(str);
        }
        if (i4 != 162) {
        }
        filterAllQualities(list, i2, cameraCapabilities, i3, arrayList, list2);
        return arrayList;
    }

    private boolean initTwoComponent(int i, int i2, CameraCapabilities cameraCapabilities, int i3) {
        if ((i != 180 && i != 162) || i2 != 0 || i3 != 0 || !CameraSettings.is8KCamcorderSupported(0)) {
            return false;
        }
        this.mConfigType = 173;
        this.mComponentConfigVideoSubQuality.reInit(i, i2, cameraCapabilities, i3);
        this.mComponentConfigVideoSubFps.reInit(i, i2, cameraCapabilities, i3, this.mComponentConfigVideoSubQuality);
        return true;
    }

    private void initVideoMode(List list, int i, int i2, CameraCapabilities cameraCapabilities, int i3, List list2) {
        List supportedOutputSizeWithTargetMode = cameraCapabilities.getSupportedOutputSizeWithTargetMode(MediaRecorder.class, 32772);
        if (supportedOutputSizeWithTargetMode.contains(new CameraSize(1280, Util.LIMIT_SURFACE_WIDTH)) && CamcorderProfile.hasProfile(i2, 5)) {
            String str = "5";
            list.add(new ComponentDataItem((int) R.drawable.ic_config_720p_30, (int) R.drawable.ic_config_720p_30, (int) R.string.pref_video_quality_entry_720p, str));
            list2.add(str);
        }
        if (supportedOutputSizeWithTargetMode.contains(new CameraSize(1920, 1080)) && CamcorderProfile.hasProfile(i2, 6)) {
            String str2 = "6";
            list.add(new ComponentDataItem((int) R.drawable.ic_config_1080p_30, (int) R.drawable.ic_config_1080p_30, (int) R.string.pref_video_quality_entry_1080p, str2));
            list2.add(str2);
            if (i != 169 && CameraSettings.isSupportFpsRange(1920, 1080, i3, cameraCapabilities)) {
                String str3 = QUALITY_1080P_60FPS;
                list.add(new ComponentDataItem((int) R.drawable.ic_config_1080p_60, (int) R.drawable.ic_config_1080p_60, (int) R.string.pref_video_quality_entry_1080p_60fps, str3));
                list2.add(str3);
            }
        }
        int i4 = CameraSettings.get4kProfile();
        if (C0124O00000oO.Oo0O00o() && supportedOutputSizeWithTargetMode.contains(new CameraSize(3840, 2160)) && CamcorderProfile.hasProfile(i2, i4)) {
            String str4 = "8";
            list.add(new ComponentDataItem((int) R.drawable.ic_config_4k_30, (int) R.drawable.ic_config_4k_30, (int) R.string.pref_video_quality_entry_4kuhd, str4));
            list2.add(str4);
            if (i != 169 && CameraSettings.isSupportFpsRange(3840, 2160, i3, cameraCapabilities)) {
                String str5 = QUALITY_4K_60FPS;
                list.add(new ComponentDataItem((int) R.drawable.ic_config_4k_60, (int) R.drawable.ic_config_4k_60, (int) R.string.pref_video_quality_entry_4kuhd_60fps, str5));
                list2.add(str5);
            }
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
        return this.mItems == null || this.mItems.size() == 1 || supprotedItemsSize(this.mItems) <= 1;
    }

    public ComponentConfigVideoSubFPS getComponentConfigVideoSubFps() {
        return this.mComponentConfigVideoSubFps;
    }

    public ComponentConfigVideoSubQuality getComponentConfigVideoSubQuality() {
        return this.mComponentConfigVideoSubQuality;
    }

    public String getComponentValue(int i) {
        return getComponentValue(i, "");
    }

    public String getComponentValue(int i, String str) {
        int i2 = this.mConfigType;
        if (i2 == 173) {
            String componentValue = this.mComponentConfigVideoSubQuality.getComponentValue(i);
            String componentValue2 = this.mComponentConfigVideoSubFps.getComponentValue(i);
            if (!componentValue2.equals("30")) {
                StringBuilder sb = new StringBuilder();
                sb.append(componentValue);
                sb.append(",");
                sb.append(componentValue2);
                componentValue = sb.toString();
            }
            return componentValue;
        } else if (i2 != 208) {
            return str;
        } else {
            if (!TextUtils.isEmpty(str) && checkValueValid(i, str)) {
                return str;
            }
            String defaultValue = getDefaultValue(i);
            String string = this.mParentDataItem.getString(getKey(i), defaultValue);
            if (string != null && !string.equals(defaultValue) && !checkValueValid(i, string)) {
                String simpleName = ComponentConfigVideoQuality.class.getSimpleName();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("reset invalid value ");
                sb2.append(string);
                Log.e(simpleName, sb2.toString());
                string = getSupportMaxValue(defaultValue);
            }
            return string;
        }
    }

    @NonNull
    public String getDefaultValue(int i) {
        String str = this.mDefaultValue;
        return str == null ? "6" : str;
    }

    public int getDisplayTitleString() {
        return R.string.pref_video_quality_title;
    }

    @NonNull
    public List getItems() {
        if (this.mItems == null) {
            Log.e(TAG, "List is empty!");
        }
        return this.mItems == null ? Collections.emptyList() : this.mItems;
    }

    public String getKey(int i) {
        return i != 161 ? i != 169 ? i != 180 ? "pref_video_quality_key" : CameraSettings.KEY_CAMERA_PRO_VIDEO_QUALITY : CameraSettings.KEY_CAMERA_FASTMOTION_QUALITY : CameraSettings.KEY_CAMERA_FUN_VIDEO_QUALITY;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000a, code lost:
        if (r0 != 208) goto L_0x0045;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isSupportVideoQuality(int i, int i2) {
        int i3 = this.mConfigType;
        if (i3 == 173) {
            if (i != 162) {
                return true;
            }
        }
        CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(i2);
        if (capabilities == null) {
            return false;
        }
        if (CameraSettings.isVideoQuality8KOpen(i)) {
            return CameraSettings.is8KCamcorderSupported(i2);
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        initVideoMode(arrayList, i, 0, capabilities, 0, arrayList2);
        return arrayList2.contains(getComponentValue(i));
    }

    public boolean isTwoComponent() {
        return this.mConfigType == 173;
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities, int i3) {
        if (!initTwoComponent(i, i2, cameraCapabilities, i3)) {
            this.mConfigType = 208;
            ArrayList arrayList = new ArrayList();
            new ArrayList();
            List initItem = initItem(arrayList, i, i2, cameraCapabilities, i3);
            this.mDefaultValue = null;
            String str = "6";
            if (i != 161) {
                if (CameraSettings.isAutoZoomEnabled(i)) {
                    filterSupprotedItems((List) arrayList, str);
                    this.mDefaultValue = str;
                }
                String str2 = "5";
                if (CameraSettings.isAiEnhancedVideoEnabled(i)) {
                    filterSupprotedItems((List) arrayList, str, str2);
                    this.mDefaultValue = str;
                }
                String str3 = "8";
                if (CameraSettings.isVhdrOn(cameraCapabilities, i)) {
                    filterSupprotedItems((List) arrayList, str2, str, str3);
                    this.mDefaultValue = str;
                }
                if (CameraSettings.isSuperEISEnabled(i)) {
                    filterSupprotedItems((List) arrayList, str);
                    this.mDefaultValue = str;
                }
                if (CameraSettings.isMasterFilterOn(i)) {
                    if (CameraSettings.getVideoMasterFilter() == 200) {
                        filterSupprotedItems((List) arrayList, str2);
                        this.mDefaultValue = str2;
                    } else {
                        filterSupprotedItems((List) arrayList, str, str2);
                        this.mDefaultValue = str;
                    }
                }
                if (CameraSettings.isFaceBeautyOn(i, null)) {
                    filterSupprotedItems((List) arrayList, str2);
                    this.mDefaultValue = str2;
                }
                if (CameraSettings.isVideoBokehOn()) {
                    filterSupprotedItems((List) arrayList, str2);
                    this.mDefaultValue = str2;
                }
                if (CameraSettings.isProVideoLogOpen(i)) {
                    filterSupprotedItems((List) arrayList, str3);
                    this.mDefaultValue = str3;
                }
            }
            if (this.mDefaultValue == null) {
                if (161 == i || i2 == 1) {
                    this.mDefaultValue = str;
                } else if (i2 == 0) {
                    this.mDefaultValue = CameraSettings.getDefaultValueByKey("pref_video_quality_key");
                }
            }
            if (initItem != null && initItem.size() > 0) {
                String str4 = this.mDefaultValue;
                if (str4 != null && !initItem.contains(str4)) {
                    int size = initItem.size();
                    if (size > 0) {
                        this.mDefaultValue = (String) initItem.get(size - 1);
                    }
                }
            }
            this.mItems = Collections.unmodifiableList(arrayList);
        }
    }

    public void reset() {
        List list = ITEMS_FROM_WIDE_RECORD_VIDEO;
        if (list != null) {
            list.clear();
        }
        List list2 = ITEMS_FROM_WIDE_FAST_MOTION;
        if (list2 != null) {
            list2.clear();
        }
        setForceValueOverlay(null);
        this.mComponentConfigVideoSubFps.reset();
        this.mComponentConfigVideoSubQuality.reset();
    }

    public void setComponentValue(int i, String str) {
        int i2 = this.mConfigType;
        if (i2 == 173) {
            String[] split = str.split(",");
            this.mComponentConfigVideoSubQuality.setComponentValue(i, split[0]);
            this.mComponentConfigVideoSubFps.setComponentValue(i, split.length > 1 ? split[1] : "30");
        } else if (i2 == 208) {
            super.setComponentValue(i, str);
        }
    }

    public void setForceValueOverlay(@Nullable String str) {
        if (str == null) {
            filterSupprotedItems(this.mItems, QUALITY_ALL);
            return;
        }
        filterSupprotedItems(this.mItems, str);
    }
}
