package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.annotation.TargetApi;
import androidx.annotation.NonNull;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TargetApi(21)
public class ComponentConfigUltraWide extends ComponentData {
    public static final String ULTRA_WIDE_VALUE_OFF = "OFF";
    public static final String ULTRA_WIDE_VALUE_ON = "ON";
    private int[] mUltraWideResource = getUltraWideResources();

    public ComponentConfigUltraWide(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    private int[] getUltraWideResources() {
        return new int[]{R.drawable.icon_config_ultra_wide_off, R.drawable.icon_config_ultra_wide_on};
    }

    @NonNull
    public String getDefaultValue(int i) {
        return "OFF";
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        if (i != 160) {
            if (i != 165) {
                if (i == 169 || i == 162) {
                    return "pref_ultra_wide_status162";
                }
                if (i != 163) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(CameraSettings.KEY_CAPTURE_ULTRA_WIDE_MODE);
                    sb.append(i);
                    return sb.toString();
                }
            }
            return "pref_ultra_wide_status163";
        }
        throw new RuntimeException("unspecified ultra wide");
    }

    public int getValueSelectedDrawableIgnoreClose(int i) {
        String componentValue = getComponentValue(i);
        if ("ON".equals(componentValue)) {
            return this.mUltraWideResource[1];
        }
        if ("OFF".equals(componentValue)) {
            return this.mUltraWideResource[0];
        }
        return -1;
    }

    public int getValueSelectedStringIdIgnoreClose(int i) {
        String componentValue = getComponentValue(i);
        if ("ON".equals(componentValue)) {
            return R.string.accessibility_ultra_wide_on;
        }
        if ("OFF".equals(componentValue)) {
            return R.string.accessibility_ultra_wide_off;
        }
        return -1;
    }

    public boolean isSupportUltraWide() {
        return !isEmpty();
    }

    public boolean isUltraWideOnInMode(int i) {
        return "ON".equals(getComponentValue(i));
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities) {
        ArrayList arrayList = new ArrayList();
        boolean isSupportUltraWide = C0122O00000o.instance().isSupportUltraWide();
        if (i2 == 0 && isSupportUltraWide && i != 166 && i != 171 && i != 172) {
            arrayList.add(new ComponentDataItem((int) R.drawable.icon_config_ultra_wide_off, (int) R.drawable.icon_config_ultra_wide_off, 0, "OFF"));
            arrayList.add(new ComponentDataItem((int) R.drawable.icon_config_ultra_wide_on, (int) R.drawable.icon_config_ultra_wide_on, 0, "ON"));
            this.mItems = Collections.unmodifiableList(arrayList);
        }
    }

    public void resetUltraWide(ProviderEditor providerEditor) {
        String str = "OFF";
        if (!str.equals(getComponentValue(163))) {
            providerEditor.putString(getKey(163), str);
        }
        if (!str.equals(getComponentValue(161))) {
            providerEditor.putString(getKey(161), str);
        }
        if (!str.equals(getComponentValue(172))) {
            providerEditor.putString(getKey(172), str);
        }
        if (!str.equals(getComponentValue(162))) {
            providerEditor.putString(getKey(162), str);
        }
        if (!C0122O00000o.instance().OOOo0oo() && !str.equals(getComponentValue(173))) {
            providerEditor.putString(getKey(173), str);
        }
        if (!str.equals(getComponentValue(165))) {
            providerEditor.putString(getKey(165), str);
        }
        if (!str.equals(getComponentValue(175))) {
            providerEditor.putString(getKey(175), str);
        }
    }

    public void setComponentValue(int i, String str) {
        super.setComponentValue(i, str);
    }
}
