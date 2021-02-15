package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentConfigRatio extends ComponentData {
    public static final String RATIO_16X9 = "16x9";
    public static final String RATIO_1X1 = "1x1";
    public static final String RATIO_4X3 = "4x3";
    public static final String RATIO_FULL_18X9 = "18x9";
    public static final String RATIO_FULL_18_7_5X9 = "18.75x9";
    public static final String RATIO_FULL_195X9 = "19.5x9";
    public static final String RATIO_FULL_19X9 = "19x9";
    public static final String RATIO_FULL_20X9 = "20x9";
    public final float mCurrentScreenRatio = (((float) Display.getWindowHeight()) / ((float) Display.getWindowWidth()));
    private String mForceValue;
    @CameraRatio
    private Map mSupportDefaultValues = new HashMap();
    private ArrayList sEntryValues = new ArrayList();
    private boolean sSupport18_7_5x9;
    private boolean sSupport18x9;
    private boolean sSupport195x9;
    private boolean sSupport19x9;
    private boolean sSupport20x9;

    public @interface CameraRatio {
    }

    public ComponentConfigRatio(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    private boolean checkFullSize(@NonNull String str) {
        char c = (str.hashCode() == 1539455 && str.equals(RATIO_FULL_20X9)) ? (char) 0 : 65535;
        boolean z = true;
        if (c != 0) {
            return true;
        }
        if (((float) Display.getWindowHeight()) / ((float) Display.getWindowWidth()) < 2.2222223f) {
            z = false;
        }
        return z;
    }

    public void cleanDefaultValues() {
        Map map = this.mSupportDefaultValues;
        if (map != null) {
            map.clear();
        }
    }

    public String getComponentValue(int i) {
        String str = this.mForceValue;
        if (str == null || i == 165) {
            str = super.getComponentValue(i);
        }
        String str2 = RATIO_1X1;
        if (165 == i && str.equals(str2)) {
            return str2;
        }
        if (i == 184 && CameraSettings.isGifOn()) {
            return str2;
        }
        if (CameraSettings.isCinematicAspectRatioEnabled(i)) {
            return RATIO_16X9;
        }
        for (ComponentDataItem componentDataItem : getItems()) {
            if (componentDataItem != null && TextUtils.equals(str, componentDataItem.mValue)) {
                return str;
            }
        }
        return getDefaultValue(i);
    }

    @NonNull
    public String getDefaultValue(int i) {
        String O000O0o0 = C0122O00000o.instance().O000O0o0(CameraSettings.isFrontCamera());
        String str = (String) this.mSupportDefaultValues.get(Integer.valueOf(i));
        if (str == null) {
            str = O000O0o0;
        }
        return !checkFullSize(str) ? RATIO_4X3 : str;
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_picturesize_title_simple_mode;
    }

    public String[] getFullSupportRatioValues() {
        this.sEntryValues.clear();
        this.sEntryValues.add(RATIO_4X3);
        this.sEntryValues.add(RATIO_16X9);
        if (C0124O00000oO.OOoo0Oo()) {
            this.sEntryValues.add(RATIO_FULL_18X9);
        }
        if (C0122O00000o.instance().OOOooOo()) {
            this.sEntryValues.add(RATIO_FULL_19X9);
        }
        if (C0122O00000o.instance().OOOooOO()) {
            this.sEntryValues.add(RATIO_FULL_195X9);
        }
        if (C0122O00000o.instance().o0OOoOoo()) {
            this.sEntryValues.add(RATIO_FULL_18_7_5X9);
        }
        if (C0122O00000o.instance().OOOooo0()) {
            this.sEntryValues.add(RATIO_FULL_20X9);
        }
        String[] strArr = new String[this.sEntryValues.size()];
        this.sEntryValues.toArray(strArr);
        return strArr;
    }

    public List getItems() {
        if (this.mItems == null) {
            reInit(DataRepository.dataItemGlobal().getCurrentMode(), DataRepository.dataItemGlobal().getCurrentCameraId());
        }
        return this.mItems;
    }

    public String getKey(int i) {
        return i != 165 ? "pref_camera_picturesize_key" : "is_square";
    }

    public int getMappingModeByRatio(int i) {
        if (i != 163 && i != 165) {
            return i;
        }
        if (isSquareModule() && !CameraSettings.isCinematicAspectRatioEnabled(i)) {
            return 165;
        }
        String componentValue = getComponentValue(i);
        char c = 65535;
        if (componentValue.hashCode() == 50858 && componentValue.equals(RATIO_1X1)) {
            c = 0;
        }
        return c != 0 ? 163 : 165;
    }

    public String getNextValue(int i) {
        String persistValue = getPersistValue(i);
        if (this.mItems != null) {
            int size = this.mItems.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (TextUtils.equals(((ComponentDataItem) this.mItems.get(i2)).mValue, persistValue)) {
                    return ((ComponentDataItem) this.mItems.get((i2 + 1) % size)).mValue;
                }
            }
        }
        return getDefaultValue(i);
    }

    public String getPictureSizeRatioString(int i) {
        String str = this.mForceValue;
        return str != null ? str : getComponentValue(i);
    }

    public void initSensorRatio(Map map, int i, int i2) {
        C0122O00000o instance = C0122O00000o.instance();
        boolean z = true;
        if (1 != i2) {
            z = false;
        }
        String O000O0o0 = instance.O000O0o0(z);
        Map map2 = this.mSupportDefaultValues;
        Integer valueOf = Integer.valueOf(i);
        if (map.get(Float.valueOf(Util.getRatio(O000O0o0))) == null) {
            O000O0o0 = RATIO_16X9;
        }
        map2.put(valueOf, O000O0o0);
        reInit(i, i2);
    }

    public boolean isSquareModule() {
        String componentValue = getComponentValue(165);
        return componentValue != null && componentValue.equals(RATIO_1X1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x007f, code lost:
        if (com.android.camera.CameraSettings.isGifOn() == false) goto L_0x009a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0081, code lost:
        r3 = new com.android.camera.data.data.ComponentDataItem(com.android.camera.R.drawable.ic_config_1_1, com.android.camera.R.drawable.ic_config_1_1, com.android.camera.R.drawable.ic_config_1_1, com.android.camera.R.string.pref_camera_picturesize_entry_1_1, com.android.camera.R.string.accessibility_picturesize_1_1_button, RATIO_1X1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x009a, code lost:
        r10 = new com.android.camera.data.data.ComponentDataItem(com.android.camera.R.drawable.ic_config_4_3, com.android.camera.R.drawable.ic_config_4_3, com.android.camera.R.drawable.ic_config_4_3, com.android.camera.R.string.pref_camera_picturesize_entry_3_4, com.android.camera.R.string.accessibility_picturesize_3_4_button, RATIO_4X3);
        r2.add(r10);
        r3 = new com.android.camera.data.data.ComponentDataItem(com.android.camera.R.drawable.ic_config_16_9, com.android.camera.R.drawable.ic_config_16_9, com.android.camera.R.drawable.ic_config_16_9, com.android.camera.R.string.pref_camera_picturesize_entry_9_16, com.android.camera.R.string.accessibility_picturesize_9_16_button, RATIO_16X9);
        r2.add(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00d0, code lost:
        if (r0.sSupport18x9 == false) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00d2, code lost:
        r3 = new com.android.camera.data.data.ComponentDataItem(com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.string.pref_camera_picturesize_entry_fullscreen, com.android.camera.R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_18X9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00ed, code lost:
        if (r0.sSupport195x9 == false) goto L_0x0108;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ef, code lost:
        r3 = new com.android.camera.data.data.ComponentDataItem(com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.string.pref_camera_picturesize_entry_fullscreen, com.android.camera.R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_195X9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x010a, code lost:
        if (r0.sSupport19x9 == false) goto L_0x0125;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x010c, code lost:
        r3 = new com.android.camera.data.data.ComponentDataItem(com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.string.pref_camera_picturesize_entry_fullscreen, com.android.camera.R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_19X9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0127, code lost:
        if (r0.sSupport20x9 == false) goto L_0x048f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0129, code lost:
        r3 = new com.android.camera.data.data.ComponentDataItem(com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.drawable.ic_config_fullscreen, com.android.camera.R.string.pref_camera_picturesize_entry_fullscreen, com.android.camera.R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_20X9);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void reInit(int i, int i2) {
        ComponentDataItem componentDataItem;
        int i3 = i;
        if (C0124O00000oO.OOoo0Oo()) {
            this.sSupport18x9 = true;
        } else {
            this.sSupport18x9 = false;
        }
        if (C0122O00000o.instance().OOOooOO()) {
            this.sSupport195x9 = true;
        } else {
            this.sSupport195x9 = false;
        }
        if (C0122O00000o.instance().OOOooo0()) {
            this.sSupport20x9 = true;
        } else {
            this.sSupport20x9 = false;
        }
        if (C0122O00000o.instance().OOOooOo()) {
            this.sSupport19x9 = true;
        } else {
            this.sSupport19x9 = false;
        }
        if (C0122O00000o.instance().o0OOoOoo()) {
            this.sSupport18_7_5x9 = true;
        } else {
            this.sSupport18_7_5x9 = false;
        }
        ArrayList arrayList = new ArrayList();
        this.mForceValue = null;
        String str = RATIO_4X3;
        if (i3 != 163) {
            if (i3 != 171) {
                if (i3 != 173) {
                    if (!(i3 == 182 || i3 == 205)) {
                        if (i3 != 213) {
                            switch (i3) {
                                case 165:
                                    break;
                                case 166:
                                    this.mForceValue = RATIO_16X9;
                                    componentDataItem = new ComponentDataItem(R.drawable.ic_config_16_9, R.drawable.ic_config_16_9, R.drawable.ic_config_16_9, R.string.pref_camera_picturesize_entry_9_16, R.string.accessibility_picturesize_9_16_button, RATIO_16X9);
                                case 167:
                                    if (CameraSettings.isUltraPixelOn()) {
                                        this.mForceValue = str;
                                    }
                                    ComponentDataItem componentDataItem2 = new ComponentDataItem(R.drawable.ic_config_4_3, R.drawable.ic_config_4_3, R.drawable.ic_config_4_3, R.string.pref_camera_picturesize_entry_3_4, R.string.accessibility_picturesize_3_4_button, RATIO_4X3);
                                    arrayList.add(componentDataItem2);
                                    ComponentDataItem componentDataItem3 = new ComponentDataItem(R.drawable.ic_config_16_9, R.drawable.ic_config_16_9, R.drawable.ic_config_16_9, R.string.pref_camera_picturesize_entry_9_16, R.string.accessibility_picturesize_9_16_button, RATIO_16X9);
                                    arrayList.add(componentDataItem3);
                                    if (this.sSupport18_7_5x9) {
                                        componentDataItem = DataRepository.dataItemGlobal().getDisplayMode() == 2 ? new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_195X9) : new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_18_7_5X9);
                                    } else if (this.sSupport18x9) {
                                        componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_18X9);
                                    } else if (this.sSupport195x9) {
                                        componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_195X9);
                                    } else if (this.sSupport19x9) {
                                        componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_19X9);
                                    } else if (this.sSupport20x9) {
                                        componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_20X9);
                                    }
                                    break;
                                default:
                                    switch (i3) {
                                        case 175:
                                            break;
                                        case 176:
                                            break;
                                        case 177:
                                            break;
                                        default:
                                            switch (i3) {
                                                case 184:
                                                    break;
                                                case 185:
                                                case 187:
                                                    break;
                                                case 186:
                                                    break;
                                                case 188:
                                                    break;
                                            }
                                    }
                            }
                        }
                        this.mForceValue = str;
                    }
                    this.mForceValue = str;
                    componentDataItem = new ComponentDataItem(R.drawable.ic_config_4_3, R.drawable.ic_config_4_3, R.drawable.ic_config_4_3, R.string.pref_camera_picturesize_entry_3_4, R.string.accessibility_picturesize_3_4_button, RATIO_4X3);
                    arrayList.add(componentDataItem);
                    this.mItems = Collections.unmodifiableList(arrayList);
                }
            } else if (i2 != 0 || !C0122O00000o.instance().OO0Oo0()) {
                if (DataRepository.dataItemGlobal().getDisplayMode() == 1) {
                    ComponentDataItem componentDataItem4 = new ComponentDataItem(R.drawable.ic_config_4_3, R.drawable.ic_config_4_3, R.drawable.ic_config_4_3, R.string.pref_camera_picturesize_entry_3_4, R.string.accessibility_picturesize_3_4_button, RATIO_4X3);
                    arrayList.add(componentDataItem4);
                }
                ComponentDataItem componentDataItem5 = new ComponentDataItem(R.drawable.ic_config_16_9, R.drawable.ic_config_16_9, R.drawable.ic_config_16_9, R.string.pref_camera_picturesize_entry_9_16, R.string.accessibility_picturesize_9_16_button, RATIO_16X9);
                arrayList.add(componentDataItem5);
                if (this.sSupport18_7_5x9) {
                    componentDataItem = DataRepository.dataItemGlobal().getDisplayMode() == 2 ? new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_195X9) : new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_18_7_5X9);
                } else if (this.sSupport18x9) {
                    componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_18X9);
                } else if (this.sSupport195x9) {
                    componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_195X9);
                } else if (this.sSupport19x9) {
                    componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_19X9);
                } else {
                    if (this.sSupport20x9) {
                        componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_20X9);
                    }
                    this.mItems = Collections.unmodifiableList(arrayList);
                }
                arrayList.add(componentDataItem);
                this.mItems = Collections.unmodifiableList(arrayList);
            } else {
                this.mForceValue = str;
                componentDataItem = new ComponentDataItem(R.drawable.ic_config_4_3, R.drawable.ic_config_4_3, R.drawable.ic_config_4_3, R.string.pref_camera_picturesize_entry_3_4, R.string.accessibility_picturesize_3_4_button, RATIO_4X3);
                arrayList.add(componentDataItem);
                this.mItems = Collections.unmodifiableList(arrayList);
            }
        }
        boolean isUltraPixelOn = CameraSettings.isUltraPixelOn();
        if (i2 != 0 ? isUltraPixelOn || CameraSettings.isAIWatermarkOn() : isUltraPixelOn || CameraSettings.isAIWatermarkOn()) {
            this.mForceValue = str;
        }
        if (DataRepository.dataItemGlobal().isIntentIDPhoto()) {
            this.mForceValue = str;
        }
        if (i3 == 165 || i3 == 163) {
            ComponentDataItem componentDataItem6 = new ComponentDataItem(R.drawable.ic_config_1_1, R.drawable.ic_config_1_1, R.drawable.ic_config_1_1, R.string.pref_camera_picturesize_entry_1_1, R.string.accessibility_picturesize_1_1_button, RATIO_1X1);
            arrayList.add(componentDataItem6);
        }
        ComponentDataItem componentDataItem7 = new ComponentDataItem(R.drawable.ic_config_4_3, R.drawable.ic_config_4_3, R.drawable.ic_config_4_3, R.string.pref_camera_picturesize_entry_3_4, R.string.accessibility_picturesize_3_4_button, RATIO_4X3);
        arrayList.add(componentDataItem7);
        ComponentDataItem componentDataItem8 = new ComponentDataItem(R.drawable.ic_config_16_9, R.drawable.ic_config_16_9, R.drawable.ic_config_16_9, R.string.pref_camera_picturesize_entry_9_16, R.string.accessibility_picturesize_9_16_button, RATIO_16X9);
        arrayList.add(componentDataItem8);
        if (this.sSupport18_7_5x9) {
            componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_18_7_5X9);
        } else if (this.sSupport18x9) {
            componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_18X9);
        } else if (this.sSupport195x9) {
            componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_195X9);
        } else if (this.sSupport19x9) {
            componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_19X9);
        } else {
            if (this.sSupport20x9) {
                componentDataItem = new ComponentDataItem(R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.drawable.ic_config_fullscreen, R.string.pref_camera_picturesize_entry_fullscreen, R.string.accessibility_picturesize_fullscreen_button, RATIO_FULL_20X9);
            }
            this.mItems = Collections.unmodifiableList(arrayList);
        }
        arrayList.add(componentDataItem);
        this.mItems = Collections.unmodifiableList(arrayList);
    }

    public void setComponentValue(int i, String str) {
        String str2 = RATIO_1X1;
        if (i == 165 && !str.equals(str2)) {
            i = 163;
        }
        if (str.equals(str2)) {
            super.setComponentValue(165, str);
            return;
        }
        super.setComponentValue(165, null);
        super.setComponentValue(i, str);
    }

    public boolean supportRatioSwitch() {
        return this.mItems != null && this.mItems.size() > 1;
    }
}
