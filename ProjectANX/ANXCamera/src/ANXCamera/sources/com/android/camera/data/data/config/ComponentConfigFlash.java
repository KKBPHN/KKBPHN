package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.annotation.TargetApi;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.ThermalDetector;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TargetApi(21)
public class ComponentConfigFlash extends ComponentData {
    public static final String FLASH_VALUE_AUTO = "3";
    public static final String FLASH_VALUE_BACK_SOFT_LIGHT = "5";
    public static final String FLASH_VALUE_MANUAL_OFF = "200";
    public static final String FLASH_VALUE_OFF = "0";
    public static final String FLASH_VALUE_ON = "1";
    public static final String FLASH_VALUE_SCREEN_LIGHT_AUTO = "103";
    public static final String FLASH_VALUE_SCREEN_LIGHT_ON = "101";
    public static final String FLASH_VALUE_TORCH = "2";
    private SparseArray mFlashValuesForSceneMode = new SparseArray();
    private boolean mIsBackSoftLightSupported;
    private boolean mIsClosed;
    private boolean mIsHardwareSupported;

    public ComponentConfigFlash(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
        this.mItems.add(new ComponentDataItem(getFlashOffRes()[0], getFlashOffRes()[1], (int) R.string.pref_camera_flashmode_entry_off, "0"));
    }

    /* JADX WARNING: Removed duplicated region for block: B:104:0x02c2  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00cc A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01e1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List createItems(int i, int i2, CameraCapabilities cameraCapabilities, ComponentConfigUltraWide componentConfigUltraWide) {
        ComponentDataItem componentDataItem;
        ComponentDataItem componentDataItem2;
        ComponentDataItem componentDataItem3;
        int i3 = i;
        int i4 = i2;
        ArrayList arrayList = new ArrayList();
        boolean z = cameraCapabilities.isFlashSupported() && DataRepository.dataItemGlobal().getDisplayMode() == 1;
        this.mIsHardwareSupported = z;
        boolean z2 = C0122O00000o.instance().OOO00oo() && i4 == 0 && cameraCapabilities.isBackSoftLightSupported();
        this.mIsBackSoftLightSupported = z2;
        if (i3 != 166) {
            if (i3 != 171) {
                if (!(i3 == 173 || i3 == 187 || i3 == 189)) {
                    if (i3 != 204) {
                        if (i3 != 184) {
                            if (i3 != 185) {
                                switch (i3) {
                                    case 207:
                                    case 208:
                                    case 209:
                                    case 210:
                                    case 211:
                                    case 212:
                                    case 213:
                                    case 214:
                                        break;
                                }
                            }
                        } else if (DataRepository.dataItemLive().getMimojiStatusManager2().getMimojiRecordState() == 1 && CameraSettings.isFrontCamera()) {
                            return arrayList;
                        }
                    } else if (CameraSettings.getDualVideoConfig().ismDrawSelectWindow() && !CameraSettings.isDualVideoRecording()) {
                        return arrayList;
                    }
                    if (this.mIsHardwareSupported) {
                        if (i4 == 1 && C0124O00000oO.Oo00Oo()) {
                            boolean z3 = i3 == 173 && C0122O00000o.instance().OOoOOO();
                            if (i3 == 163 || i3 == 165 || i3 == 171 || i3 == 205 || z3) {
                                ComponentDataItem componentDataItem4 = new ComponentDataItem(getFlashOffRes()[0], (int) R.drawable.ic_new_config_flash_off_shadow, getFlashOffRes()[1], (int) R.string.pref_camera_flashmode_entry_off, "0");
                                arrayList.add(componentDataItem4);
                                ComponentDataItem componentDataItem5 = new ComponentDataItem(getFlashAutoRes()[0], R.drawable.ic_new_config_flash_auto_shadow, getFlashAutoRes()[1], getFlashAutoRes()[0], getFlashAutoRes()[2], R.drawable.ic_new_config_fash_auto_label_shadow, R.string.pref_camera_flashmode_entry_auto, FLASH_VALUE_SCREEN_LIGHT_AUTO);
                                arrayList.add(componentDataItem5);
                                ComponentDataItem componentDataItem6 = new ComponentDataItem(getFlashOnRes()[0], (int) R.drawable.ic_new_config_flash_on_shadow, getFlashOnRes()[1], (int) R.string.pref_camera_flashmode_entry_on, FLASH_VALUE_SCREEN_LIGHT_ON);
                                arrayList.add(componentDataItem6);
                            }
                            if (i3 == 177) {
                                ComponentDataItem componentDataItem7 = new ComponentDataItem(getFlashOffRes()[0], (int) R.drawable.ic_new_config_flash_off_shadow, getFlashOffRes()[1], (int) R.string.pref_camera_flashmode_entry_off, "0");
                                arrayList.add(componentDataItem7);
                                componentDataItem3 = new ComponentDataItem(getFlashOnRes()[0], (int) R.drawable.ic_new_config_flash_on_shadow, getFlashOnRes()[1], (int) R.string.pref_camera_flashmode_entry_on, FLASH_VALUE_SCREEN_LIGHT_ON);
                            } else if (i3 == 184 && DataRepository.dataItemLive().getMimojiStatusManager2().getMimojiRecordState() == 0) {
                                ComponentDataItem componentDataItem8 = new ComponentDataItem(getFlashOffRes()[0], (int) R.drawable.ic_new_config_flash_off_shadow, getFlashOffRes()[1], (int) R.string.pref_camera_flashmode_entry_off, "0");
                                arrayList.add(componentDataItem8);
                                componentDataItem3 = new ComponentDataItem(getFlashOnRes()[0], (int) R.drawable.ic_new_config_flash_on_shadow, getFlashOnRes()[1], (int) R.string.pref_camera_flashmode_entry_on, FLASH_VALUE_SCREEN_LIGHT_ON);
                            }
                            arrayList.add(componentDataItem3);
                        }
                        return arrayList;
                    }
                    ComponentDataItem componentDataItem9 = new ComponentDataItem(getFlashOffRes()[0], (int) R.drawable.ic_new_config_flash_off_shadow, getFlashOffRes()[1], (int) R.string.pref_camera_flashmode_entry_off, "0");
                    arrayList.add(componentDataItem9);
                    if (!(i3 == 161 || i3 == 162 || i3 == 169 || i3 == 172 || i3 == 174)) {
                        if (i3 == 177) {
                            if (CameraSettings.isBackCamera()) {
                                ComponentDataItem componentDataItem10 = new ComponentDataItem(getFlashOnRes()[0], (int) R.drawable.ic_new_config_flash_on_shadow, getFlashOnRes()[1], (int) R.string.pref_camera_flashmode_entry_on, "1");
                                arrayList.add(componentDataItem10);
                            }
                            if (!CameraSettings.isFrontCamera() || !C0124O00000oO.Oo0o0Oo()) {
                                if (C0124O00000oO.Oo0O00()) {
                                    componentDataItem = new ComponentDataItem(getFlashTorchRes()[0], (int) R.drawable.ic_new_config_flash_torch_shadow, getFlashTorchRes()[1], (int) R.string.pref_camera_flashmode_entry_torch, "2");
                                    arrayList.add(componentDataItem);
                                }
                                return arrayList;
                            }
                            componentDataItem = new ComponentDataItem(getFlashOnRes()[0], (int) R.drawable.ic_new_config_flash_on_shadow, getFlashOnRes()[1], (int) R.string.pref_camera_flashmode_entry_on, "2");
                            arrayList.add(componentDataItem);
                            return arrayList;
                        } else if (!(i3 == 204 || i3 == 179 || i3 == 180 || i3 == 183)) {
                            if (i3 != 184) {
                                ComponentDataItem componentDataItem11 = new ComponentDataItem(getFlashAutoRes()[0], R.drawable.ic_new_config_flash_auto_shadow, getFlashAutoRes()[1], getFlashAutoRes()[0], getFlashAutoRes()[2], R.drawable.ic_new_config_fash_auto_label_shadow, R.string.pref_camera_flashmode_entry_auto, "3");
                                arrayList.add(componentDataItem11);
                                if (CameraSettings.isBackCamera()) {
                                    ComponentDataItem componentDataItem12 = new ComponentDataItem(getFlashOnRes()[0], (int) R.drawable.ic_new_config_flash_on_shadow, getFlashOnRes()[1], (int) R.string.pref_camera_flashmode_entry_on, "1");
                                    arrayList.add(componentDataItem12);
                                }
                                if (!CameraSettings.isFrontCamera() || !C0124O00000oO.Oo0o0Oo()) {
                                    if (C0124O00000oO.Oo0O00()) {
                                        componentDataItem2 = new ComponentDataItem(getFlashTorchRes()[0], (int) R.drawable.ic_new_config_flash_torch_shadow, getFlashTorchRes()[1], (int) R.string.pref_camera_flashmode_entry_torch, "2");
                                    }
                                    if (this.mIsBackSoftLightSupported) {
                                        componentDataItem = new ComponentDataItem(getFlashBackSoftLightSelectedRes(), (int) R.drawable.ic_new_config_flash_back_soft_light_shadow, getFlashBackSoftLightSelectedRes(), (int) R.string.pref_camera_flashmode_entry_back_soft_light, "5");
                                        arrayList.add(componentDataItem);
                                    }
                                    return arrayList;
                                }
                                componentDataItem2 = new ComponentDataItem(getFlashOnRes()[0], (int) R.drawable.ic_new_config_flash_on_shadow, getFlashOnRes()[1], (int) R.string.pref_camera_flashmode_entry_on, "2");
                                arrayList.add(componentDataItem2);
                                if (this.mIsBackSoftLightSupported) {
                                }
                                return arrayList;
                            }
                            if (CameraSettings.isBackCamera() && DataRepository.dataItemLive().getMimojiStatusManager2().getMimojiRecordState() == 0) {
                                ComponentDataItem componentDataItem13 = new ComponentDataItem(getFlashOnRes()[0], (int) R.drawable.ic_new_config_flash_on_shadow, getFlashOnRes()[1], (int) R.string.pref_camera_flashmode_entry_on, "1");
                                arrayList.add(componentDataItem13);
                            }
                            if (!CameraSettings.isFrontCamera() || !C0124O00000oO.Oo0o0Oo()) {
                                if (C0124O00000oO.Oo0O00()) {
                                    componentDataItem = new ComponentDataItem(getFlashTorchRes()[0], (int) R.drawable.ic_new_config_flash_torch_shadow, getFlashTorchRes()[1], (int) R.string.pref_camera_flashmode_entry_torch, "2");
                                    arrayList.add(componentDataItem);
                                }
                                return arrayList;
                            }
                            componentDataItem = new ComponentDataItem(getFlashOnRes()[0], (int) R.drawable.ic_new_config_flash_on_shadow, getFlashOnRes()[1], (int) R.string.pref_camera_flashmode_entry_on, "2");
                            arrayList.add(componentDataItem);
                            return arrayList;
                        }
                    }
                    ComponentDataItem componentDataItem14 = new ComponentDataItem(getFlashTorchRes()[0], (int) R.drawable.ic_new_config_flash_torch_shadow, getFlashTorchRes()[1], (int) R.string.pref_camera_flashmode_entry_torch, "2");
                    arrayList.add(componentDataItem14);
                    if (this.mIsBackSoftLightSupported) {
                        componentDataItem = new ComponentDataItem(getFlashBackSoftLightSelectedRes(), (int) R.drawable.ic_new_config_flash_back_soft_light_shadow, getFlashBackSoftLightSelectedRes(), (int) R.string.pref_camera_flashmode_entry_back_soft_light, "5");
                        arrayList.add(componentDataItem);
                    }
                    return arrayList;
                }
            } else if (this.mIsHardwareSupported && this.mIsBackSoftLightSupported) {
                ComponentDataItem componentDataItem15 = new ComponentDataItem(getFlashBackSoftLightRes()[0], (int) R.drawable.ic_new_config_flash_back_soft_light_shadow, getFlashBackSoftLightRes()[1], (int) R.string.pref_camera_flashmode_entry_off, "0");
                arrayList.add(componentDataItem15);
                ComponentDataItem componentDataItem16 = new ComponentDataItem(getFlashBackSoftLightSelectedRes(), (int) R.drawable.ic_new_config_flash_back_soft_light_shadow, getFlashBackSoftLightSelectedRes(), (int) R.string.pref_camera_flashmode_entry_back_soft_light, "5");
                arrayList.add(componentDataItem16);
                return arrayList;
            }
        }
        if (i4 == 0) {
            return arrayList;
        }
        if (this.mIsHardwareSupported) {
        }
    }

    private String getComponentValueInternal(int i) {
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        if (dataItemRunning.isSwitchOn("pref_camera_scenemode_setting_key")) {
            String flashModeByScene = CameraSettings.getFlashModeByScene(dataItemRunning.getComponentRunningSceneValue().getComponentValue(i));
            if (!TextUtils.isEmpty(flashModeByScene)) {
                return flashModeByScene;
            }
        }
        return super.getComponentValue(i);
    }

    private int[] getFlashAutoRes() {
        return new int[]{R.drawable.ic_new_config_flash_auto, R.drawable.ic_new_config_flash_auto, R.drawable.ic_new_config_fash_auto_label};
    }

    private int[] getFlashBackSoftLightRes() {
        return new int[]{R.drawable.ic_new_config_flash_back_soft_light, R.drawable.ic_new_config_flash_back_soft_light};
    }

    private int getFlashBackSoftLightSelectedRes() {
        return R.drawable.ic_new_config_flash_back_soft_light;
    }

    private int[] getFlashOffRes() {
        return new int[]{R.drawable.ic_new_config_flash_off, R.drawable.ic_new_config_flash_off};
    }

    private int[] getFlashOnRes() {
        return new int[]{R.drawable.ic_new_config_flash_on, R.drawable.ic_new_config_flash_on};
    }

    private int[] getFlashTorchRes() {
        return new int[]{R.drawable.ic_new_config_flash_torch, R.drawable.ic_new_config_flash_torch};
    }

    public void clearClosed() {
        this.mIsClosed = false;
    }

    public boolean disableUpdate() {
        return ThermalDetector.getInstance().thermalCloseFlash() && isHardwareSupported();
    }

    public String getComponentValue(int i) {
        String str = "0";
        return (!isClosed() && !isDisabled(i)) ? (!isEmpty() || i == 204) ? getComponentValueInternal(i) : str : str;
    }

    public String getDefaultValue(int i) {
        return "0";
    }

    public int getDisableReasonString() {
        return CameraSettings.isFrontCamera() ? R.string.close_front_flash_toast : R.string.close_back_flash_toast;
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_flashmode_title;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        if (!(i == 169 || i == 174)) {
            if (i == 177) {
                return CameraSettings.KEY_FUN_AR_FLASH_MODE;
            }
            if (i != 204) {
                if (i == 171) {
                    return CameraSettings.KEY_PORTRAIT_FLASH_MODE;
                }
                if (!(i == 172 || i == 179 || i == 180 || i == 183)) {
                    if (i != 184) {
                        switch (i) {
                            case 160:
                                throw new RuntimeException("unspecified flash");
                            case 161:
                            case 162:
                                break;
                            default:
                                return CameraSettings.KEY_FLASH_MODE;
                        }
                    } else {
                        return DataRepository.dataItemLive().getMimojiStatusManager2().getMimojiRecordState() == 0 ? CameraSettings.KEY_FUN_AR2_PHOTO_FLASH_MODE : CameraSettings.KEY_FUN_AR2_VIDEO_FLASH_MODE;
                    }
                }
            }
        }
        return CameraSettings.KEY_VIDEOCAMERA_FLASH_MODE;
    }

    public int getValueSelectedDrawableIgnoreClose(int i) {
        String componentValue = getComponentValue(i);
        if ("1".equals(componentValue)) {
            return getFlashOnRes()[0];
        }
        if ("3".equals(componentValue)) {
            return getFlashAutoRes()[0];
        }
        if ("0".equals(componentValue)) {
            return (i != 171 || !this.mIsBackSoftLightSupported) ? getFlashOffRes()[0] : getFlashBackSoftLightRes()[0];
        }
        if ("2".equals(componentValue)) {
            return CameraSettings.isFrontCamera() ? getFlashOnRes()[0] : getFlashTorchRes()[0];
        } else if (FLASH_VALUE_SCREEN_LIGHT_AUTO.equals(componentValue)) {
            return getFlashAutoRes()[0];
        } else {
            if (FLASH_VALUE_SCREEN_LIGHT_ON.equals(componentValue)) {
                return getFlashOnRes()[0];
            }
            if ("5".equals(componentValue)) {
                return getFlashBackSoftLightSelectedRes();
            }
            return -1;
        }
    }

    public int getValueSelectedShadowDrawable(int i) {
        String componentValue = getComponentValue(i);
        boolean equals = "1".equals(componentValue);
        int i2 = R.drawable.ic_new_config_flash_on_shadow;
        if (equals) {
            return R.drawable.ic_new_config_flash_on_shadow;
        }
        if ("3".equals(componentValue)) {
            return R.drawable.ic_new_config_flash_auto_shadow;
        }
        if ("0".equals(componentValue)) {
            return (i != 171 || !this.mIsBackSoftLightSupported) ? R.drawable.ic_new_config_flash_off_shadow : R.drawable.ic_new_config_flash_back_soft_light_shadow;
        }
        if ("2".equals(componentValue)) {
            if (!CameraSettings.isFrontCamera()) {
                i2 = R.drawable.ic_new_config_flash_torch_shadow;
            }
            return i2;
        } else if (FLASH_VALUE_SCREEN_LIGHT_AUTO.equals(componentValue)) {
            return R.drawable.ic_new_config_flash_auto_shadow;
        } else {
            if (FLASH_VALUE_SCREEN_LIGHT_ON.equals(componentValue)) {
                return R.drawable.ic_new_config_flash_on_shadow;
            }
            if ("5".equals(componentValue)) {
                return R.drawable.ic_new_config_flash_back_soft_light_shadow;
            }
            return -1;
        }
    }

    public int getValueSelectedStringIdIgnoreClose(int i) {
        String componentValue = getComponentValue(i);
        boolean equals = "1".equals(componentValue);
        int i2 = R.string.accessibility_flash_on;
        if (equals) {
            return R.string.accessibility_flash_on;
        }
        if ("3".equals(componentValue)) {
            return R.string.accessibility_flash_auto;
        }
        if ("0".equals(componentValue)) {
            return R.string.accessibility_flash_off;
        }
        if ("2".equals(componentValue)) {
            if (!CameraSettings.isFrontCamera()) {
                i2 = R.string.accessibility_flash_torch;
            }
            return i2;
        } else if (FLASH_VALUE_SCREEN_LIGHT_AUTO.equals(componentValue)) {
            return R.string.accessibility_flash_auto;
        } else {
            if (FLASH_VALUE_SCREEN_LIGHT_ON.equals(componentValue)) {
                return R.string.accessibility_flash_on;
            }
            if ("5".equals(componentValue)) {
                return R.string.accessibility_flash_back_soft_light;
            }
            return -1;
        }
    }

    public boolean isClosed() {
        return this.mIsClosed;
    }

    public boolean isDisabled(int i) {
        boolean z = true;
        if (i == 167 && !CameraSettings.isFlashSupportedInManualMode()) {
            return true;
        }
        if (i == 184 && DataRepository.dataItemLive().getMimojiStatusManager2().getMode() == 4) {
            return true;
        }
        if (!(i == 188 || i == 189)) {
            z = false;
        }
        return z;
    }

    public boolean isHardwareSupported() {
        return this.mIsHardwareSupported;
    }

    public boolean isValidFlashValue(String str) {
        return str.matches("^[0-9]+$");
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities, ComponentConfigUltraWide componentConfigUltraWide) {
        this.mItems = Collections.unmodifiableList(createItems(i, i2, cameraCapabilities, componentConfigUltraWide));
    }

    public void resetIfNeed(String str, ProviderEditor providerEditor) {
        String str2 = "0";
        String string = this.mParentDataItem.getString(str, str2);
        if (!string.equals(str2) && !string.equals("3")) {
            providerEditor.remove(str);
        }
    }

    public void resetToDefault(ProviderEditor providerEditor) {
        setClosed(false);
        resetIfNeed(getKey(163), providerEditor);
        resetIfNeed(getKey(162), providerEditor);
        resetIfNeed(getKey(171), providerEditor);
        resetIfNeed(getKey(177), providerEditor);
        resetIfNeed(CameraSettings.KEY_FUN_AR2_PHOTO_FLASH_MODE, providerEditor);
        resetIfNeed(CameraSettings.KEY_FUN_AR2_VIDEO_FLASH_MODE, providerEditor);
    }

    public void setClosed(boolean z) {
        this.mIsClosed = z;
    }

    public void setComponentValue(int i, String str) {
        setClosed(false);
        super.setComponentValue(i, str);
    }

    public void setSceneModeFlashValue(int i, String str) {
        this.mFlashValuesForSceneMode.put(i, str);
    }
}
