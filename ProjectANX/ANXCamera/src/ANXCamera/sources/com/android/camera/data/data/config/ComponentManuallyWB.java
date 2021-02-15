package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.text.TextUtils;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentManuallyWB extends ComponentData {
    public static final String MANUAL_WHITEBALANCE_VALUE = "pref_qc_manual_whitebalance_k_value_key";

    public ComponentManuallyWB(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    private void resetCustomWB(int i) {
        this.mParentDataItem.editor().remove(i == 169 ? CameraSettings.KEY_QC_FASTMOTION_PRO_MANUAL_WHITEBALANCE_VALUE : "pref_qc_manual_whitebalance_k_value_key").apply();
    }

    /* access modifiers changed from: protected */
    public boolean checkValueValid(int i, String str) {
        for (ComponentDataItem componentDataItem : getItems()) {
            if (TextUtils.equals(str, componentDataItem.mValue)) {
                return true;
            }
        }
        return false;
    }

    public int getContentDescriptionString() {
        return R.string.parameter_wb_title;
    }

    public int getCustomWB(int i) {
        return this.mParentDataItem.getInt(i == 169 ? CameraSettings.KEY_QC_FASTMOTION_PRO_MANUAL_WHITEBALANCE_VALUE : "pref_qc_manual_whitebalance_k_value_key", CameraAppImpl.getAndroidContext().getResources().getInteger(R.integer.default_manual_whitebalance_value));
    }

    public String getDefaultValue(int i) {
        return "1";
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_whitebalance_title_abbr;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        return i != 167 ? i != 169 ? CameraSettings.KEY_PRO_VIDEO_WHITE_BALANCE : CameraSettings.KEY_FASTMOTION_PRO_WHITE_BALANCE : CameraSettings.KEY_WHITE_BALANCE;
    }

    public int getValueDisplayString(int i) {
        if (getComponentValue(i).equals("1")) {
            return R.string.pref_camera_exposuretime_entry_auto_abbr;
        }
        return -1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x005e  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x007d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getValueSelectedDrawable(int i) {
        boolean z;
        String componentValue = getComponentValue(i);
        int hashCode = componentValue.hashCode();
        if (hashCode != -1081415738) {
            if (hashCode != 53) {
                if (hashCode != 54) {
                    switch (hashCode) {
                        case 49:
                            if (componentValue.equals("1")) {
                                z = false;
                                break;
                            }
                        case 50:
                            if (componentValue.equals("2")) {
                                z = true;
                                break;
                            }
                        case 51:
                            if (componentValue.equals("3")) {
                                z = true;
                                break;
                            }
                    }
                } else if (componentValue.equals("6")) {
                    z = true;
                    if (z) {
                        return R.drawable.ic_manua_auto;
                    }
                    if (z) {
                        return R.drawable.ic_white_balance_incandescent;
                    }
                    if (z) {
                        return R.drawable.ic_white_balance_sunlight;
                    }
                    if (z) {
                        return R.drawable.ic_white_balance_fluorescent;
                    }
                    if (z) {
                        return R.drawable.ic_white_balance_cloudy;
                    }
                    if (!z) {
                        return -1;
                    }
                    return R.drawable.ic_white_balance_manual;
                }
            } else if (componentValue.equals("5")) {
                z = true;
                if (z) {
                }
            }
        } else if (componentValue.equals("manual")) {
            z = true;
            if (z) {
            }
        }
        z = true;
        if (z) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x005e  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x007d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getValueSelectedShadowDrawable(int i) {
        boolean z;
        String componentValue = getComponentValue(i);
        int hashCode = componentValue.hashCode();
        if (hashCode != -1081415738) {
            if (hashCode != 53) {
                if (hashCode != 54) {
                    switch (hashCode) {
                        case 49:
                            if (componentValue.equals("1")) {
                                z = false;
                                break;
                            }
                        case 50:
                            if (componentValue.equals("2")) {
                                z = true;
                                break;
                            }
                        case 51:
                            if (componentValue.equals("3")) {
                                z = true;
                                break;
                            }
                    }
                } else if (componentValue.equals("6")) {
                    z = true;
                    if (z) {
                        return R.drawable.ic_manua_auto_shadow;
                    }
                    if (z) {
                        return R.drawable.ic_white_balance_incandescent_shadow;
                    }
                    if (z) {
                        return R.drawable.ic_white_balance_sunlight_shadow;
                    }
                    if (z) {
                        return R.drawable.ic_white_balance_fluorescent_shadow;
                    }
                    if (z) {
                        return R.drawable.ic_white_balance_cloudy_shadow;
                    }
                    if (!z) {
                        return -1;
                    }
                    return R.drawable.ic_white_balance_manual_shadow;
                }
            } else if (componentValue.equals("5")) {
                z = true;
                if (z) {
                }
            }
        } else if (componentValue.equals("manual")) {
            z = true;
            if (z) {
            }
        }
        z = true;
        if (z) {
        }
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities) {
        ArrayList arrayList = new ArrayList();
        ComponentDataItem componentDataItem = new ComponentDataItem(R.drawable.ic_manu_auto_normal, R.drawable.ic_manu_auto_normal_shadow, -1, -1, R.string.pref_video_focusmode_entryvalue_auto, "1");
        arrayList.add(componentDataItem);
        ComponentDataItem componentDataItem2 = new ComponentDataItem(R.drawable.ic_white_balance_incandescent_normal, R.drawable.ic_white_balance_incandescent_normal_shadow, -1, -1, R.string.pref_camera_whitebalance_entry_incandescent, "2");
        arrayList.add(componentDataItem2);
        ComponentDataItem componentDataItem3 = new ComponentDataItem(R.drawable.ic_white_balance_sunlight_normal, R.drawable.ic_white_balance_sunlight_normal_shadow, -1, -1, R.string.pref_camera_whitebalance_entry_daylight, "5");
        arrayList.add(componentDataItem3);
        ComponentDataItem componentDataItem4 = new ComponentDataItem(R.drawable.ic_white_balance_fluorescent_normal, R.drawable.ic_white_balance_fluorescent_normal_shadow, -1, -1, R.string.pref_camera_whitebalance_entry_fluorescent, "3");
        arrayList.add(componentDataItem4);
        ComponentDataItem componentDataItem5 = new ComponentDataItem(R.drawable.ic_white_balance_cloudy_normal, R.drawable.ic_white_balance_cloudy_normal_shadow, -1, -1, R.string.pref_camera_whitebalance_entry_cloudy, "6");
        arrayList.add(componentDataItem5);
        if (C0124O00000oO.Oo00o00() && C0122O00000o.instance().OOo00oO()) {
            ComponentDataItem componentDataItem6 = new ComponentDataItem(R.drawable.ic_white_balance_manual_normal, R.drawable.ic_white_balance_manual_normal_shadow, -1, -1, R.string.accessibility_whitebalance_custom_panel_on, "manual");
            arrayList.add(componentDataItem6);
        }
        this.mItems = Collections.unmodifiableList(arrayList);
    }

    public void reset(int i) {
        super.reset(i);
        resetCustomWB(i);
    }

    /* access modifiers changed from: protected */
    public void resetComponentValue(int i) {
        setComponentValue(i, getDefaultValue(i));
    }

    public void setCustomWB(int i, int i2) {
        this.mParentDataItem.editor().putInt(i == 169 ? CameraSettings.KEY_QC_FASTMOTION_PRO_MANUAL_WHITEBALANCE_VALUE : "pref_qc_manual_whitebalance_k_value_key", i2).apply();
    }
}
