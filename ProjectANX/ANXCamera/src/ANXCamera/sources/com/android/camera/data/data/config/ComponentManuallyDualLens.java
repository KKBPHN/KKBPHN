package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.text.TextUtils;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import java.util.ArrayList;
import java.util.List;

public class ComponentManuallyDualLens extends ComponentData {
    public static final String LENS_MACRO = "macro";
    public static final String LENS_TELE = "tele";
    public static final String LENS_ULTRA = "ultra";
    public static final String LENS_ULTRA_TELE = "Standalone";
    public static final String LENS_WIDE = "wide";
    private static final String TAG = "DualLens";

    public ComponentManuallyDualLens(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    private int indexOf(String str) {
        List items = getItems();
        if (items != null && !TextUtils.isEmpty(str)) {
            for (int i = 0; i < items.size(); i++) {
                if (TextUtils.equals(((ComponentDataItem) items.get(i)).mValue, str)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private List initItems(int i) {
        ComponentDataItem componentDataItem;
        ComponentDataItem componentDataItem2;
        ArrayList arrayList = new ArrayList();
        if (i != 175) {
            if (C0122O00000o.instance().OOoOO0o() && C0122O00000o.instance().OOoOO()) {
                ComponentDataItem componentDataItem3 = new ComponentDataItem((int) R.drawable.ic_camera_zoom_mode_entry_macro, (int) R.drawable.ic_camera_zoom_mode_entry_macro_shadow, -1, (int) R.string.pref_camera_zoom_mode_entry_macro_abbr, "macro");
                arrayList.add(componentDataItem3);
            }
            if (C0122O00000o.instance().isSupportUltraWide()) {
                ComponentDataItem componentDataItem4 = new ComponentDataItem((int) R.drawable.ic_camera_zoom_mode_entry_ultra, (int) R.drawable.ic_camera_zoom_mode_entry_ultra_shadow, -1, (int) R.string.pref_camera_zoom_mode_entry_ultra_abbr, LENS_ULTRA);
                arrayList.add(componentDataItem4);
            }
            ComponentDataItem componentDataItem5 = new ComponentDataItem((int) R.drawable.ic_camera_zoom_mode_entry_wide, (int) R.drawable.ic_camera_zoom_mode_entry_wide_shadow, -1, (int) R.string.pref_camera_zoom_mode_entry_wide_abbr, LENS_WIDE);
            arrayList.add(componentDataItem5);
            if (CameraSettings.isSupportedOpticalZoom()) {
                if (C0122O00000o.instance().OOOOoOo()) {
                    if ((!C0122O00000o.instance().OOo000() || i != 180) && Camera2DataContainer.getInstance().getAuxCameraId() >= 0) {
                        componentDataItem2 = new ComponentDataItem((int) R.drawable.ic_camera_zoom_mode_entry_tele2x, (int) R.drawable.ic_camera_zoom_mode_entry_tele2x_shadow, -1, (int) R.string.pref_camera_zoom_mode_entry_tele_2X_abbr, LENS_TELE);
                    }
                } else if (Camera2DataContainer.getInstance().getAuxCameraId() >= 0) {
                    componentDataItem2 = new ComponentDataItem((int) R.drawable.ic_camera_zoom_mode_entry_tele2x, (int) R.drawable.ic_camera_zoom_mode_entry_tele2x_shadow, -1, (int) R.string.pref_camera_zoom_mode_entry_tele_abbr, LENS_TELE);
                }
                arrayList.add(componentDataItem2);
            }
            if (C0122O00000o.instance().OOOOoOo() && (i != 180 || C0122O00000o.instance().OOo000())) {
                componentDataItem = new ComponentDataItem((int) R.drawable.ic_camera_zoom_mode_entry_tele5x, (int) R.drawable.ic_camera_zoom_mode_entry_tele5x_shadow, -1, (int) R.string.pref_camera_zoom_mode_entry_tele_5X_abbr, LENS_ULTRA_TELE);
            }
            return arrayList;
        }
        ComponentDataItem componentDataItem6 = new ComponentDataItem((int) R.drawable.ic_camera_zoom_mode_entry_wide, (int) R.drawable.ic_camera_zoom_mode_entry_wide_shadow, -1, (int) R.string.pref_camera_zoom_mode_entry_wide_abbr, LENS_WIDE);
        arrayList.add(componentDataItem6);
        if (C0122O00000o.instance().OOOOoOo()) {
            componentDataItem = new ComponentDataItem((int) R.drawable.ic_camera_zoom_mode_entry_tele5x, (int) R.drawable.ic_camera_zoom_mode_entry_tele5x_shadow, -1, (int) R.string.pref_camera_zoom_mode_entry_tele_5X_abbr, LENS_ULTRA_TELE);
        }
        return arrayList;
        arrayList.add(componentDataItem);
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public boolean checkValueValid(int i, String str) {
        for (ComponentDataItem componentDataItem : getItems()) {
            if (TextUtils.equals(str, componentDataItem.mValue)) {
                return true;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("checkValueValid: invalid value: ");
        sb.append(str);
        Log.d(TAG, sb.toString());
        return false;
    }

    public int getContentDescriptionString() {
        return R.string.parameter_lens_title;
    }

    public String getDefaultValue(int i) {
        return LENS_WIDE;
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_zoom_mode_title_abbr;
    }

    public List getItems() {
        if (this.mItems == null) {
            this.mItems = initItems(DataRepository.dataItemGlobal().getCurrentMode());
        }
        return this.mItems;
    }

    public String getKey(int i) {
        return i != 167 ? i != 175 ? i != 180 ? CameraSettings.KEY_CAMERA_ZOOM_MODE : CameraSettings.KEY_CAMERA_PRO_VIDEO_LENS : CameraSettings.KEY_CAMERA_PIXEL_LENS : CameraSettings.KEY_CAMERA_MANUALLY_LENS;
    }

    public String next(String str, int i) {
        int indexOf = indexOf(str);
        List items = getItems();
        if (items == null) {
            return LENS_WIDE;
        }
        return ((ComponentDataItem) items.get(indexOf == items.size() + -1 ? 0 : indexOf + 1)).mValue;
    }

    public List reInit(int i) {
        this.mItems = initItems(i);
        return this.mItems;
    }

    /* access modifiers changed from: protected */
    public void resetComponentValue(int i) {
        this.mItems = initItems(i);
        setComponentValue(i, getDefaultValue(i));
    }

    public void resetLensType(ComponentConfigUltraWide componentConfigUltraWide, ProviderEditor providerEditor) {
        String componentValue = getComponentValue(167);
        String str = LENS_WIDE;
        if (!str.equals(componentValue)) {
            providerEditor.putString(getKey(167), str);
            if (LENS_ULTRA.equals(componentValue)) {
                providerEditor.putString(componentConfigUltraWide.getKey(167), "OFF");
            }
        }
    }
}
