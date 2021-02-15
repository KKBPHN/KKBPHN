package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.res.Resources;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.log.Log;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentConfigSlowMotion extends ComponentData {
    public static final String DATA_CONFIG_NEW_SLOW_MOTION_120 = "slow_motion_120";
    public static final String DATA_CONFIG_NEW_SLOW_MOTION_1920 = "slow_motion_1920";
    public static final String DATA_CONFIG_NEW_SLOW_MOTION_240 = "slow_motion_240";
    public static final String DATA_CONFIG_NEW_SLOW_MOTION_480 = "slow_motion_480";
    public static final String DATA_CONFIG_NEW_SLOW_MOTION_480_DIRECT = "slow_motion_480_direct";
    public static final String DATA_CONFIG_NEW_SLOW_MOTION_960 = "slow_motion_960";
    private static final String TAG = "ComponentConfigSlowMotion";
    public String mCurrentQuality = "5";

    public ComponentConfigSlowMotion(DataItemConfig dataItemConfig, int i) {
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

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String getValueSelectedString(String str) {
        char c;
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        switch (str.hashCode()) {
            case -1299788783:
                if (str.equals(DATA_CONFIG_NEW_SLOW_MOTION_1920)) {
                    c = 5;
                    break;
                }
            case -1150307548:
                if (str.equals(DATA_CONFIG_NEW_SLOW_MOTION_120)) {
                    c = 0;
                    break;
                }
            case -1150306525:
                if (str.equals(DATA_CONFIG_NEW_SLOW_MOTION_240)) {
                    c = 1;
                    break;
                }
            case -1150304479:
                if (str.equals(DATA_CONFIG_NEW_SLOW_MOTION_480)) {
                    c = 2;
                    break;
                }
            case -1150299736:
                if (str.equals(DATA_CONFIG_NEW_SLOW_MOTION_960)) {
                    c = 4;
                    break;
                }
            case 272876231:
                if (str.equals(DATA_CONFIG_NEW_SLOW_MOTION_480_DIRECT)) {
                    c = 3;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            return resources.getString(R.string.accessibility_camera_video_960fps_120);
        }
        if (c == 1) {
            return resources.getString(R.string.accessibility_camera_video_960fps_240);
        }
        if (c == 2 || c == 3) {
            return resources.getString(R.string.accessibility_camera_video_slow_motion_fps, new Object[]{Integer.valueOf(480)});
        } else if (c == 4) {
            return resources.getString(R.string.accessibility_camera_video_960fps_960);
        } else {
            if (c != 5) {
                return null;
            }
            return resources.getString(R.string.accessibility_camera_video_slow_motion_fps, new Object[]{Integer.valueOf(1920)});
        }
    }

    private boolean isDefaultValue(int i) {
        return getComponentValue(i).equals(getDefaultValue(i));
    }

    /* access modifiers changed from: protected */
    public boolean checkValueValid(int i, String str) {
        for (ComponentDataItem componentDataItem : getItems()) {
            if (TextUtils.equals(str, componentDataItem.mValue) && !componentDataItem.mIsDisabled) {
                return true;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("checkValueValid: invalid value: ");
        sb.append(str);
        Log.d(TAG, sb.toString());
        return false;
    }

    public String getComponentValue(int i) {
        String defaultValue = getDefaultValue(i);
        String string = this.mParentDataItem.getString(getKey(i), defaultValue);
        if (string == null || checkValueValid(i, string)) {
            return string;
        }
        String simpleName = ComponentConfigSlowMotion.class.getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("reset invalid value ");
        sb.append(string);
        Log.e(simpleName, sb.toString());
        return getSupportMaxValue(defaultValue);
    }

    @NonNull
    public String getDefaultValue(int i) {
        return this.mItems.size() == 0 ? "" : DATA_CONFIG_NEW_SLOW_MOTION_120;
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        return DataItemConfig.DATA_CONFIG_NEW_SLOW_MOTION_KEY;
    }

    public String getValueSelectedStringIdIgnoreClose(int i) {
        if (isDefaultValue(i)) {
            return null;
        }
        return getValueSelectedString(getComponentValue(i));
    }

    public boolean isSlowMotionFps1920() {
        return DATA_CONFIG_NEW_SLOW_MOTION_1920.equals(getComponentValue(172));
    }

    public boolean isSlowMotionFps480() {
        return DATA_CONFIG_NEW_SLOW_MOTION_480.equals(getComponentValue(172));
    }

    public boolean isSlowMotionFps960() {
        return DATA_CONFIG_NEW_SLOW_MOTION_960.equals(getComponentValue(172));
    }

    public void reInit(int i, int i2, ComponentConfigSlowMotionQuality componentConfigSlowMotionQuality, CameraCapabilities cameraCapabilities) {
        ComponentDataItem componentDataItem;
        ComponentDataItem componentDataItem2;
        int i3 = i;
        ArrayList arrayList = new ArrayList();
        if (i3 == 172) {
            ArrayList supportedHfrSettings = CameraSettings.getSupportedHfrSettings(cameraCapabilities);
            this.mCurrentQuality = componentConfigSlowMotionQuality.getComponentValue(i3);
            StringBuffer stringBuffer = new StringBuffer();
            String str = "1280x720:";
            if (!this.mCurrentQuality.equals("5") && this.mCurrentQuality.equals("6")) {
                str = "1920x1080:";
            }
            String str2 = "120";
            String str3 = DATA_CONFIG_NEW_SLOW_MOTION_120;
            C0122O00000o instance = C0122O00000o.instance();
            if (i2 == 0) {
                boolean OOOo0 = instance.OOOo0();
                String str4 = DATA_CONFIG_NEW_SLOW_MOTION_960;
                String str5 = "240";
                String str6 = DATA_CONFIG_NEW_SLOW_MOTION_240;
                if (OOOo0) {
                    ComponentDataItem componentDataItem3 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_120, (int) R.drawable.ic_new_video_960fps_120, getValueSelectedString(str3), str3);
                    stringBuffer.setLength(0);
                    stringBuffer.append(str);
                    stringBuffer.append(str2);
                    componentDataItem3.mIsDisabled = !supportedHfrSettings.contains(stringBuffer.toString());
                    arrayList.add(componentDataItem3);
                    ComponentDataItem componentDataItem4 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_240, (int) R.drawable.ic_new_video_960fps_240, getValueSelectedString(str6), str6);
                    stringBuffer.setLength(0);
                    stringBuffer.append(str);
                    stringBuffer.append(str5);
                    componentDataItem4.mIsDisabled = !supportedHfrSettings.contains(stringBuffer.toString());
                    arrayList.add(componentDataItem4);
                    String str7 = DATA_CONFIG_NEW_SLOW_MOTION_480_DIRECT;
                    ComponentDataItem componentDataItem5 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_480, (int) R.drawable.ic_new_video_960fps_480, getValueSelectedString(str7), str7);
                    stringBuffer.setLength(0);
                    stringBuffer.append(str);
                    String str8 = "480";
                    stringBuffer.append(str8);
                    componentDataItem5.mIsDisabled = !supportedHfrSettings.contains(stringBuffer.toString());
                    arrayList.add(componentDataItem5);
                    ComponentDataItem componentDataItem6 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_960, (int) R.drawable.ic_new_video_960fps_960, getValueSelectedString(str4), str4);
                    stringBuffer.setLength(0);
                    stringBuffer.append(str);
                    stringBuffer.append(str5);
                    componentDataItem6.mIsDisabled = !supportedHfrSettings.contains(stringBuffer.toString());
                    arrayList.add(componentDataItem6);
                    componentDataItem2 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_1920, (int) R.drawable.ic_new_video_960fps_1920, getValueSelectedString(DATA_CONFIG_NEW_SLOW_MOTION_1920), DATA_CONFIG_NEW_SLOW_MOTION_1920);
                    stringBuffer.setLength(0);
                    stringBuffer.append(str);
                    stringBuffer.append(str8);
                } else {
                    if (C0122O00000o.instance().OOOo0O0()) {
                        ComponentDataItem componentDataItem7 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_120, (int) R.drawable.ic_new_video_960fps_120, getValueSelectedString(str3), str3);
                        stringBuffer.setLength(0);
                        stringBuffer.append(str);
                        stringBuffer.append(str2);
                        componentDataItem7.mIsDisabled = !supportedHfrSettings.contains(stringBuffer.toString());
                        arrayList.add(componentDataItem7);
                        ComponentDataItem componentDataItem8 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_240, (int) R.drawable.ic_new_video_960fps_240, getValueSelectedString(str6), str6);
                        stringBuffer.setLength(0);
                        stringBuffer.append(str);
                        stringBuffer.append(str5);
                        componentDataItem8.mIsDisabled = !supportedHfrSettings.contains(stringBuffer.toString());
                        arrayList.add(componentDataItem8);
                        componentDataItem2 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_960, (int) R.drawable.ic_new_video_960fps_960, getValueSelectedString(str4), str4);
                    } else if (C0122O00000o.instance().getConfig().Oo0OoO()) {
                        ComponentDataItem componentDataItem9 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_120, (int) R.drawable.ic_new_video_960fps_120, getValueSelectedString(str3), str3);
                        stringBuffer.setLength(0);
                        stringBuffer.append(str);
                        stringBuffer.append(str2);
                        componentDataItem9.mIsDisabled = !supportedHfrSettings.contains(stringBuffer.toString());
                        arrayList.add(componentDataItem9);
                        String str9 = DATA_CONFIG_NEW_SLOW_MOTION_480;
                        ComponentDataItem componentDataItem10 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_480, (int) R.drawable.ic_new_video_960fps_480, getValueSelectedString(str9), str9);
                        stringBuffer.setLength(0);
                        stringBuffer.append(str);
                        stringBuffer.append(str2);
                        componentDataItem10.mIsDisabled = !supportedHfrSettings.contains(stringBuffer.toString());
                        arrayList.add(componentDataItem10);
                    } else if (C0122O00000o.instance().ooOOo00()) {
                        ComponentDataItem componentDataItem11 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_120, (int) R.drawable.ic_new_video_960fps_120, getValueSelectedString(str3), str3);
                        stringBuffer.setLength(0);
                        stringBuffer.append(str);
                        stringBuffer.append(str2);
                        componentDataItem11.mIsDisabled = !supportedHfrSettings.contains(stringBuffer.toString());
                        arrayList.add(componentDataItem11);
                        componentDataItem2 = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_240, (int) R.drawable.ic_new_video_960fps_240, getValueSelectedString(str6), str6);
                    } else if (C0122O00000o.instance().OOOo0o0()) {
                        componentDataItem = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_120, (int) R.drawable.ic_new_video_960fps_120, getValueSelectedString(str3), str3);
                    }
                    stringBuffer.setLength(0);
                    stringBuffer.append(str);
                    stringBuffer.append(str5);
                }
                componentDataItem2.mIsDisabled = !supportedHfrSettings.contains(stringBuffer.toString());
                arrayList.add(componentDataItem2);
            } else if (instance.OOOo0OO()) {
                componentDataItem = new ComponentDataItem((int) R.drawable.ic_new_video_960fps_120, (int) R.drawable.ic_new_video_960fps_120, getValueSelectedString(str3), str3);
            }
            stringBuffer.setLength(0);
            stringBuffer.append(str);
            stringBuffer.append(str2);
            componentDataItem.mIsDisabled = !supportedHfrSettings.contains(stringBuffer.toString());
            arrayList.add(componentDataItem);
        }
        this.mItems = Collections.unmodifiableList(arrayList);
    }
}
