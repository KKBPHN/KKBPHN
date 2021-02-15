package com.android.camera.data.data.config;

import androidx.annotation.NonNull;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import java.util.List;

public class ComponentConfigGradienter extends ComponentData {
    private static final String SCOPE_NS_DUMMY_FILM = "film";
    private static final String SCOPE_NS_PHOTO = "photo";
    private static final String SCOPE_NS_PHOTO_PRO = "photo_pro";
    private static final String SCOPE_NS_UNSUPPORTED = "unsupported";
    private static final String SCOPE_NS_VIDEO = "video";
    private static final String SCOPE_NS_VIDEO_PRO = "video_pro";
    private static final String TAG = "CCGradienter";
    public static final String VALUE_GRADIENTER_OFF = "off";
    public static final String VALUE_GRADIENTER_ON = "on";
    private int mCameraId;
    private int mCapturingMode;

    public ComponentConfigGradienter(DataItemConfig dataItemConfig, int i) {
        super(dataItemConfig);
        this.mCameraId = i;
    }

    public String getComponentValue(int i) {
        String str = "off";
        return (this.mCameraId != 1 && !getKey(i).endsWith(SCOPE_NS_UNSUPPORTED)) ? super.getComponentValue(i) : str;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return "off";
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        throw new UnsupportedOperationException("CCGradienter#getItems() not supported");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0042, code lost:
        return "pref_camera_gradienter_key_photo_pro";
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String getKey(int i) {
        if (i != 162) {
            if (!(i == 163 || i == 165 || i == 175 || i == 177)) {
                if (i == 180) {
                    return "pref_camera_gradienter_key_video_pro";
                }
                if (!(i == 184 || i == 205 || i == 214)) {
                    if (i == 207 || i == 208 || i == 211 || i == 212) {
                        return "pref_camera_gradienter_key_film";
                    }
                    switch (i) {
                        case 167:
                            break;
                        case 168:
                        case 169:
                            break;
                        default:
                            switch (i) {
                                case 171:
                                case 173:
                                    break;
                                case 172:
                                    break;
                                default:
                                    switch (i) {
                                        case 186:
                                        case 188:
                                            break;
                                        case 187:
                                            break;
                                        default:
                                            return "pref_camera_gradienter_key_unsupported";
                                    }
                            }
                    }
                }
            }
            return "pref_camera_gradienter_key_photo";
        }
        return "pref_camera_gradienter_key_video";
    }

    public boolean isSwitchOn(int i) {
        return "on".equals(getComponentValue(i));
    }

    public void reInit(int i, int i2) {
        this.mCapturingMode = i;
        this.mCameraId = i2;
    }

    public void resetToDefault(ProviderEditor providerEditor) {
        providerEditor.remove(getKey(163));
        providerEditor.remove(getKey(167));
        providerEditor.remove(getKey(162));
        providerEditor.remove(getKey(180));
    }

    public void setComponentValue(int i, String str) {
        if (this.mCameraId != 1 && !getKey(i).endsWith(SCOPE_NS_UNSUPPORTED)) {
            super.setComponentValue(i, str);
        }
    }

    public void toSwitch(int i, boolean z) {
        setComponentValue(i, z ? "on" : "off");
    }
}
