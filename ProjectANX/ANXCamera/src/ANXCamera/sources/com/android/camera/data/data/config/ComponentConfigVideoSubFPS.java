package com.android.camera.data.data.config;

import android.text.TextUtils;
import android.util.Range;
import android.util.Size;
import androidx.annotation.NonNull;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ComponentConfigVideoSubFPS extends ComponentData {
    private static List ALL_FPS_ITEMS = new ArrayList();
    private static final List ALL_FPS_VALUE = new ArrayList();
    public static final String FPS_120 = "120";
    public static final String FPS_24 = "24";
    public static final String FPS_30 = "30";
    public static final String FPS_60 = "60";
    private static final String TAG = "ComponentConfigVideoSubFPS";
    private String mForceValue = null;

    static {
        ALL_FPS_VALUE.clear();
        ALL_FPS_VALUE.add(FPS_24);
        ALL_FPS_VALUE.add("30");
        ALL_FPS_VALUE.add("60");
        ALL_FPS_VALUE.add("120");
    }

    public ComponentConfigVideoSubFPS(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    private String getSupportMinValue(String str) {
        if (this.mItems == null || this.mItems.size() == 0 || isContain(str, this.mItems)) {
            return str;
        }
        for (int i = 0; i < this.mItems.size(); i++) {
            if (!((ComponentDataItem) this.mItems.get(i)).mIsDisabled) {
                return ((ComponentDataItem) this.mItems.get(i)).mValue;
            }
        }
        return str;
    }

    private void initAllItem(List list, CameraCapabilities cameraCapabilities, List list2, int i, ComponentConfigVideoSubQuality componentConfigVideoSubQuality) {
        if (componentConfigVideoSubQuality.isSupport8K()) {
            String str = FPS_24;
            list.add(new ComponentDataItem((int) R.drawable.ic_config_24fps, (int) R.drawable.ic_config_24fps, (int) R.string.pref_video_quality_sub_24fps, str));
            list2.add(str);
        }
        String str2 = "30";
        list.add(new ComponentDataItem((int) R.drawable.ic_config_30fps, (int) R.drawable.ic_config_30fps, (int) R.string.pref_video_quality_sub_30fps, str2));
        list2.add(str2);
        if ((componentConfigVideoSubQuality.isSupport1080P() && CameraSettings.isSupportFpsRange(1920, 1080, i, cameraCapabilities)) || (componentConfigVideoSubQuality.isSupport4K() && CameraSettings.isSupportFpsRange(3840, 2160, i, cameraCapabilities))) {
            String str3 = "60";
            list.add(new ComponentDataItem((int) R.drawable.ic_config_60fps, (int) R.drawable.ic_config_60fps, (int) R.string.pref_video_quality_sub_60fps, str3));
            list2.add(str3);
        }
        if (componentConfigVideoSubQuality.isSupport4K() && isSupported4K120Fps(cameraCapabilities)) {
            String str4 = "120";
            list.add(new ComponentDataItem((int) R.drawable.ic_config_120fps, (int) R.drawable.ic_config_120fps, (int) R.string.pref_video_quality_sub_120fps, str4));
            list2.add(str4);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00dc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initItem(int i, List list, CameraCapabilities cameraCapabilities, List list2, int i2, ComponentConfigVideoSubQuality componentConfigVideoSubQuality) {
        char c;
        ComponentDataItem componentDataItem;
        String componentValue = componentConfigVideoSubQuality.getComponentValue(i);
        int hashCode = componentValue.hashCode();
        if (hashCode != 53) {
            if (hashCode != 54) {
                if (hashCode != 56) {
                    if (hashCode == 1567006 && componentValue.equals("3001")) {
                        c = 3;
                        String str = "30";
                        if (c != 0) {
                            String str2 = "60";
                            if (c == 1) {
                                list.add(new ComponentDataItem((int) R.drawable.ic_config_30fps, (int) R.drawable.ic_config_30fps, (int) R.string.pref_video_quality_sub_30fps, str));
                                list2.add(str);
                                if (CameraSettings.isSupportFpsRange(1920, 1080, i2, cameraCapabilities)) {
                                    list.add(new ComponentDataItem((int) R.drawable.ic_config_60fps, (int) R.drawable.ic_config_60fps, (int) R.string.pref_video_quality_sub_60fps, str2));
                                    list2.add(str2);
                                    return;
                                }
                                return;
                            } else if (c == 2) {
                                list.add(new ComponentDataItem((int) R.drawable.ic_config_30fps, (int) R.drawable.ic_config_30fps, (int) R.string.pref_video_quality_sub_30fps, str));
                                list2.add(str);
                                if (CameraSettings.isSupportFpsRange(3840, 2160, i2, cameraCapabilities)) {
                                    list.add(new ComponentDataItem((int) R.drawable.ic_config_60fps, (int) R.drawable.ic_config_60fps, (int) R.string.pref_video_quality_sub_60fps, str2));
                                    list2.add(str2);
                                }
                                if (isSupported4K120Fps(cameraCapabilities)) {
                                    String str3 = "120";
                                    list.add(new ComponentDataItem((int) R.drawable.ic_config_120fps, (int) R.drawable.ic_config_120fps, (int) R.string.pref_video_quality_sub_120fps, str3));
                                    list2.add(str3);
                                    return;
                                }
                                return;
                            } else if (c == 3) {
                                String str4 = FPS_24;
                                list.add(new ComponentDataItem((int) R.drawable.ic_config_24fps, (int) R.drawable.ic_config_24fps, (int) R.string.pref_video_quality_sub_24fps, str4));
                                list2.add(str4);
                                if (cameraCapabilities.get8KMaxFpsSupported() > 24) {
                                    componentDataItem = new ComponentDataItem((int) R.drawable.ic_config_30fps, (int) R.drawable.ic_config_30fps, (int) R.string.pref_video_quality_sub_30fps, str);
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            componentDataItem = new ComponentDataItem((int) R.drawable.ic_config_30fps, (int) R.drawable.ic_config_30fps, (int) R.string.pref_video_quality_sub_30fps, str);
                        }
                        list.add(componentDataItem);
                        list2.add(str);
                    }
                } else if (componentValue.equals("8")) {
                    c = 2;
                    String str5 = "30";
                    if (c != 0) {
                    }
                    list.add(componentDataItem);
                    list2.add(str5);
                }
            } else if (componentValue.equals("6")) {
                c = 1;
                String str52 = "30";
                if (c != 0) {
                }
                list.add(componentDataItem);
                list2.add(str52);
            }
        } else if (componentValue.equals("5")) {
            c = 0;
            String str522 = "30";
            if (c != 0) {
            }
            list.add(componentDataItem);
            list2.add(str522);
        }
        c = 65535;
        String str5222 = "30";
        if (c != 0) {
        }
        list.add(componentDataItem);
        list2.add(str5222);
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

    public static boolean isSupported4K120Fps(CameraCapabilities cameraCapabilities) {
        Size[] supportedHighSpeedVideoSize;
        ArrayList arrayList = new ArrayList();
        if (cameraCapabilities == null) {
            Log.w(TAG, "getSupportedHfrSettings: CameraCapabilities is null!!!");
            return false;
        }
        for (Size size : cameraCapabilities.getSupportedHighSpeedVideoSize()) {
            if (size.getWidth() == 3840) {
                for (Range upper : cameraCapabilities.getSupportedHighSpeedVideoFPSRange(size)) {
                    String format = String.format(Locale.ENGLISH, "%dx%d:%d", new Object[]{Integer.valueOf(size.getWidth()), Integer.valueOf(size.getHeight()), upper.getUpper()});
                    if (!arrayList.contains(format)) {
                        arrayList.add(format);
                    }
                }
            }
        }
        return arrayList.contains("3840x2160:120");
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

    public String getComponentValue(int i) {
        String str = this.mForceValue;
        if (str != null) {
            return str;
        }
        String defaultValue = getDefaultValue(i);
        String string = this.mParentDataItem.getString(getKey(i), defaultValue);
        if (string != null && !checkValueValid(i, string)) {
            String simpleName = ComponentConfigVideoSubFPS.class.getSimpleName();
            StringBuilder sb = new StringBuilder();
            sb.append("reset invalid value ");
            sb.append(string);
            Log.e(simpleName, sb.toString());
            string = getSupportMinValue(defaultValue);
        }
        return string;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return "30";
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
        return CameraSettings.KEY_VIDEO_SUB_FPS;
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities, int i3, ComponentConfigVideoSubQuality componentConfigVideoSubQuality) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (i == 162 || i == 180) {
            initItem(i, arrayList, cameraCapabilities, arrayList2, i3, componentConfigVideoSubQuality);
            if (arrayList2.size() > 0 && i2 == 0) {
                List list = ALL_FPS_ITEMS;
                if (list == null || list.size() == 0) {
                    ArrayList arrayList3 = new ArrayList();
                    ArrayList arrayList4 = arrayList3;
                    initAllItem(arrayList4, Camera2DataContainer.getInstance().getCapabilities(0), new ArrayList(), i3, componentConfigVideoSubQuality);
                    copyList(arrayList3, ALL_FPS_ITEMS);
                }
                filterSupprotedItems(ALL_FPS_ITEMS, ALL_FPS_VALUE);
                copyList(ALL_FPS_ITEMS, arrayList);
                filterSupprotedItems((List) arrayList, (List) arrayList2);
            }
            if (componentConfigVideoSubQuality.isForceQuality()) {
                this.mForceValue = "30";
                filterSupprotedItems((List) arrayList, this.mForceValue);
            } else {
                this.mForceValue = null;
            }
        }
        this.mItems = Collections.unmodifiableList(arrayList);
    }

    public void reset() {
        List list = ALL_FPS_ITEMS;
        if (list != null) {
            list.clear();
        }
        if (this.mParentDataItem.getString(getKey(160), "").equals(FPS_24)) {
            this.mParentDataItem.remove(getKey(160));
        }
    }
}
