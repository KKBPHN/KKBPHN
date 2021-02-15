package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.text.TextUtils;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentConfigHdr extends ComponentData {
    public static final int HDR_UI_STATUS_AUTO = 2;
    public static final int HDR_UI_STATUS_OFF = 0;
    public static final int HDR_UI_STATUS_ON = 1;
    public static final String HDR_VALUE_AUTO = "auto";
    public static final String HDR_VALUE_LIVE = "live";
    public static final String HDR_VALUE_NORMAL = "normal";
    public static final String HDR_VALUE_OFF = "off";
    public static final String HDR_VALUE_ON = "on";
    private boolean mAutoSupported;
    private boolean mIsClosed;
    private boolean mSupportHdrCheckerWhenOn;

    public ComponentConfigHdr(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
        this.mItems.add(new ComponentDataItem(getConfigHDROffRes()[0], getConfigHDROffRes()[1], (int) R.string.pref_camera_hdr_entry_off, "off"));
    }

    private int[] getConfigHDRAutoRes() {
        return new int[]{R.drawable.ic_new_config_hdr_auto, R.drawable.ic_new_config_hdr_auto, R.drawable.ic_new_config_hdr_auto_label};
    }

    private int[] getConfigHDRLiveRes() {
        return new int[]{R.drawable.ic_new_config_hdr_live, R.drawable.ic_new_config_hdr_live};
    }

    private int[] getConfigHDRNormalRes() {
        return new int[]{R.drawable.ic_new_config_hdr_normal, R.drawable.ic_new_config_hdr_normal};
    }

    private int[] getConfigHDROffRes() {
        return new int[]{R.drawable.ic_new_config_hdr_off, R.drawable.ic_new_config_hdr_off};
    }

    public static int getHdrUIStatus(String str) {
        if ("on".equals(str) || "normal".equals(str)) {
            return 1;
        }
        return "auto".equals(str) ? 2 : 0;
    }

    private void initForHDR(List list, CameraCapabilities cameraCapabilities) {
        ComponentDataItem componentDataItem;
        List list2 = list;
        ComponentDataItem componentDataItem2 = new ComponentDataItem(getConfigHDROffRes()[0], (int) R.drawable.ic_new_config_hdr_off_shadow, getConfigHDROffRes()[0], (int) R.string.pref_camera_hdr_entry_off, "off");
        list2.add(componentDataItem2);
        if (cameraCapabilities.isSupportAutoHdr()) {
            this.mAutoSupported = true;
            ComponentDataItem componentDataItem3 = new ComponentDataItem(getConfigHDRAutoRes()[0], R.drawable.ic_new_config_hdr_auto_shadow, getConfigHDRAutoRes()[0], getConfigHDRAutoRes()[0], getConfigHDRAutoRes()[2], R.drawable.ic_new_config_hdr_auto_label_shadow, R.string.pref_camera_hdr_entry_auto, "auto");
            list2.add(componentDataItem3);
        }
        if (!C0124O00000oO.Oo00OOO()) {
            componentDataItem = new ComponentDataItem(getConfigHDRNormalRes()[0], (int) R.drawable.ic_new_config_hdr_normal_shadow, getConfigHDRNormalRes()[0], (int) R.string.pref_simple_hdr_entry_on, "normal");
        } else {
            ComponentDataItem componentDataItem4 = new ComponentDataItem(getConfigHDRNormalRes()[0], (int) R.drawable.ic_new_config_hdr_normal_shadow, getConfigHDRNormalRes()[0], (int) R.string.pref_simple_hdr_entry_on, "normal");
            list2.add(componentDataItem4);
            componentDataItem = new ComponentDataItem(getConfigHDRLiveRes()[0], getConfigHDRLiveRes()[0], (int) R.string.pref_camera_hdr_entry_live, "live");
        }
        list2.add(componentDataItem);
        if (cameraCapabilities.isSupportHdrCheckerStatus()) {
            this.mSupportHdrCheckerWhenOn = true;
        }
    }

    public void clearClosed() {
        this.mIsClosed = false;
    }

    public String getComponentValue(int i) {
        String str = "off";
        return (!isClosed() && !isEmpty()) ? (171 != i || !C0122O00000o.instance().OOo0OO0() || !CameraSettings.isBackCamera()) ? super.getComponentValue(i) : "auto" : str;
    }

    public String getDefaultValue(int i) {
        String str = "off";
        if (isClosed() || isEmpty()) {
            return str;
        }
        if (CameraSettings.isFrontCamera()) {
            return (162 != i || !C0122O00000o.instance().o0ooo0OO().booleanValue()) ? str : "normal";
        }
        String str2 = "auto";
        if (171 == i && CameraSettings.isBackCamera() && C0122O00000o.instance().OOo0OO0()) {
            return str2;
        }
        String O0ooo0O = C0122O00000o.instance().O0ooo0O();
        if (!TextUtils.isEmpty(O0ooo0O)) {
            char c = 65535;
            int hashCode = O0ooo0O.hashCode();
            String str3 = "on";
            if (hashCode != 3551) {
                if (hashCode != 109935) {
                    if (hashCode == 3005871 && O0ooo0O.equals(str2)) {
                        c = 0;
                    }
                } else if (O0ooo0O.equals(str)) {
                    c = 2;
                }
            } else if (O0ooo0O.equals(str3)) {
                c = 1;
            }
            if (c == 0) {
                if (this.mAutoSupported) {
                    str = str2;
                }
                return str;
            } else if (c == 1) {
                return str3;
            } else {
                if (c == 2) {
                    return str;
                }
            }
        }
        return this.mAutoSupported ? str2 : str;
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_hdr_title;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        if (i != 160) {
            if (!(i == 162 || i == 169)) {
                if (i == 171) {
                    return CameraSettings.KEY_PORTRAIT_HDR;
                }
                if (i != 180) {
                    return CameraSettings.KEY_CAMERA_HDR;
                }
            }
            return CameraSettings.KEY_VIDEO_HDR;
        }
        throw new RuntimeException("unspecified hdr");
    }

    public String getPersistValue(int i) {
        return super.getComponentValue(i);
    }

    public int getValueSelectedDrawableIgnoreClose(int i) {
        String componentValue = getComponentValue(i);
        if ("off".equals(componentValue)) {
            return getConfigHDROffRes()[1];
        }
        if ("auto".equals(componentValue)) {
            return getConfigHDRAutoRes()[1];
        }
        if ("normal".equals(componentValue)) {
            return getConfigHDRNormalRes()[1];
        }
        if ("live".equals(componentValue)) {
            return getConfigHDRLiveRes()[1];
        }
        if ("on".equals(componentValue)) {
            return getConfigHDRNormalRes()[1];
        }
        return -1;
    }

    public int getValueSelectedShadowDrawable(int i) {
        String componentValue = getComponentValue(i);
        if ("off".equals(componentValue)) {
            return R.drawable.ic_new_config_hdr_off_shadow;
        }
        if ("auto".equals(componentValue)) {
            return R.drawable.ic_new_config_hdr_auto_shadow;
        }
        if ("normal".equals(componentValue)) {
            return R.drawable.ic_new_config_hdr_normal_shadow;
        }
        if ("live".equals(componentValue)) {
            return getConfigHDRLiveRes()[1];
        }
        if ("on".equals(componentValue)) {
            return R.drawable.ic_new_config_hdr_normal_shadow;
        }
        return -1;
    }

    public int getValueSelectedStringIdIgnoreClose(int i) {
        String componentValue = getComponentValue(i);
        if ("off".equals(componentValue)) {
            return R.string.accessibility_hdr_off;
        }
        if ("auto".equals(componentValue)) {
            return R.string.accessibility_hdr_auto;
        }
        if ("normal".equals(componentValue)) {
            return R.string.accessibility_hdr_on;
        }
        if ("live".equals(componentValue)) {
            return R.string.accessibility_hdr_live;
        }
        if ("on".equals(componentValue)) {
            return R.string.accessibility_hdr_on;
        }
        return -1;
    }

    public boolean isClosed() {
        return this.mIsClosed;
    }

    public boolean isHdrOnWithChecker(String str) {
        if ("on".equals(str) || "normal".equals(str)) {
            return this.mSupportHdrCheckerWhenOn;
        }
        return false;
    }

    public boolean isSupportAutoHdr() {
        return this.mAutoSupported;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00d0, code lost:
        if (r21 != 0) goto L_0x00d2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities, int i3) {
        ComponentDataItem componentDataItem;
        int i4 = i;
        CameraCapabilities cameraCapabilities2 = cameraCapabilities;
        ArrayList arrayList = new ArrayList();
        this.mAutoSupported = false;
        this.mSupportHdrCheckerWhenOn = false;
        if (cameraCapabilities.isSupportHdr()) {
            if (i4 != 165) {
                if (!(i4 == 167 || i4 == 169)) {
                    if (i4 != 171) {
                        if (i4 != 180) {
                            if (i4 != 205) {
                                switch (i4) {
                                    case 162:
                                        if (cameraCapabilities.isSupportVideoHdr()) {
                                            ComponentDataItem componentDataItem2 = new ComponentDataItem(getConfigHDROffRes()[0], (int) R.drawable.ic_new_config_hdr_off_shadow, getConfigHDROffRes()[0], (int) R.string.pref_camera_hdr_entry_off, "off");
                                            arrayList.add(componentDataItem2);
                                            componentDataItem = new ComponentDataItem(getConfigHDRNormalRes()[0], (int) R.drawable.ic_new_config_hdr_normal_shadow, getConfigHDRNormalRes()[0], (int) R.string.pref_simple_hdr_entry_on, "normal");
                                        }
                                        break;
                                    case 163:
                                        break;
                                }
                            }
                        }
                        initForHDR(arrayList, cameraCapabilities2);
                    } else if (C0122O00000o.instance().OOo0OO0() && CameraSettings.isBackCamera()) {
                        ComponentDataItem componentDataItem3 = new ComponentDataItem(getConfigHDROffRes()[0], (int) R.drawable.ic_new_config_hdr_off_shadow, getConfigHDROffRes()[0], (int) R.string.pref_camera_hdr_entry_off, "off");
                        arrayList.add(componentDataItem3);
                        if (cameraCapabilities.isSupportAutoHdr()) {
                            this.mAutoSupported = true;
                            componentDataItem = new ComponentDataItem(getConfigHDRAutoRes()[0], (int) R.drawable.ic_new_config_hdr_auto_shadow, getConfigHDRAutoRes()[0], (int) R.string.pref_camera_hdr_entry_auto, "auto");
                        }
                    }
                    arrayList.add(componentDataItem);
                }
                this.mItems = Collections.unmodifiableList(arrayList);
            }
            if (cameraCapabilities.isSupportLightTripartite()) {
            }
            initForHDR(arrayList, cameraCapabilities2);
            this.mItems = Collections.unmodifiableList(arrayList);
        }
    }

    public void resetIfNeed(String str, ProviderEditor providerEditor) {
        providerEditor.remove(str);
    }

    public void resetToDefault(ProviderEditor providerEditor) {
        setClosed(false);
        resetIfNeed(getKey(163), providerEditor);
        resetIfNeed(getKey(162), providerEditor);
        resetIfNeed(getKey(171), providerEditor);
    }

    public void setClosed(boolean z) {
        this.mIsClosed = z;
    }

    public void setComponentValue(int i, String str) {
        setClosed(false);
        super.setComponentValue(i, str);
    }
}
