package com.android.camera.data.data.global;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Intent;
import androidx.core.util.Pair;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager;
import com.android.camera.CameraSettings;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.backup.DataBackUp;
import com.android.camera.data.data.DataItemBase;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import com.android.camera.log.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class DataItemGlobal extends DataItemBase {
    public static final int BACK_DISPLAY_MODE = 2;
    public static final String CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY = "can_connect_network";
    public static final String DATA_CLONE_MODEL_VERSION = "pref_clone_model_version";
    public static final String DATA_COMMON_AI_SCENE_HINT = "pref_camera_first_ai_scene_use_hint_shown_key";
    public static final String DATA_COMMON_CAMCORDER_TIP_8K_MAX_VIDEO_DURATION_SHOWN = "pref_camcorder_tip_8k_max_video_duration_shown";
    public static final String DATA_COMMON_CURRENT_CAMERA_ID = "pref_camera_id_key";
    public static final String DATA_COMMON_CURRENT_MODE = "pref_camera_mode_key_intent_";
    public static final String DATA_COMMON_CUSTOM_WATERMARK_VERSION = "pref_custom_watermark_version";
    public static final String DATA_COMMON_DEVICE_WATERMARK = "pref_dualcamera_watermark_key";
    public static final String DATA_COMMON_DOCUMENT_MODE_USE_HINT_SHOWN = "pref_document_use_hint_shown";
    public static final String DATA_COMMON_DUALCAMERA_USERDEFINE_WATERMARK = "user_define_watermark_key";
    public static final String DATA_COMMON_FIRST_USE_HINT = "pref_camera_first_use_hint_shown_key";
    public static final String DATA_COMMON_FOCUS_SHOOT = "pref_camera_focus_shoot_key";
    public static final String DATA_COMMON_FRONT_CAM_ROTATE_HINT = "pref_front_camera_first_use_hint_shown_key";
    public static final String DATA_COMMON_ID_CARD_MODE_HINT = "pref_camera_first_id_card_mode_use_hint_shown_key";
    public static final String DATA_COMMON_LPL_SELECTOR_USE_HINT_SHOWN = "pref_lpl_selector_use_hint_shown";
    public static final String DATA_COMMON_MACRO_MODE_HINT = "pref_camera_first_macro_mode_use_hint_shown_key";
    private static final String DATA_COMMON_OPEN_TIME = "pref_camera_open_time";
    public static final String DATA_COMMON_PORTRAIT_HINT = "pref_camera_first_portrait_use_hint_shown_key";
    public static final String DATA_COMMON_SORT_MODES = "pref_camera_sort_modes_key";
    public static final String DATA_COMMON_TIKTOK_MORE_BUTTON_SHOW_APP = "pref_camera_tiktok_more_show_app_key";
    public static final String DATA_COMMON_TIKTOK_MORE_BUTTON_SHOW_MARKET = "pref_camera_tiktok_more_show_market_key";
    public static final String DATA_COMMON_TIME_WATER_MARK = "pref_time_watermark_key";
    public static final String DATA_COMMON_ULTRA_TELE_HINT = "pref_camera_first_ultra_tele_use_hint_shown_key";
    public static final String DATA_COMMON_ULTRA_WIDE_HINT = "pref_camera_first_ultra_wide_use_hint_shown_key";
    public static final String DATA_COMMON_ULTRA_WIDE_SAT_HINT = "pref_camera_first_ultra_wide_sat_use_hint_shown_key";
    public static final String DATA_COMMON_USER_EDIT_MODES = "pref_user_edit_modes";
    public static final String DATA_COMMON_USER_MORE_MODE_TAB_STYLE = "pref_more_mode_tab_style";
    public static final String DATA_COMMON_VV_HINT = "pref_camera_first_vv_hint_shown_key";
    private static int[] DEFAULT_SORT_MODES = {167, 162, 163, 171, 254, 173, 175, 183, 174, 161, 166, 176, 186, 209, 172, 169, 204, 211, 205, 187, 188, 177, 184, 185};
    public static final int FRONT_DISPLAY_MODE = 1;
    public static final int INTENT_TYPE_IDPHOTO = 5;
    public static final int INTENT_TYPE_IMAGE = 1;
    public static final int INTENT_TYPE_NORMAL = 0;
    public static final int INTENT_TYPE_SCAN_QR = 3;
    public static final int INTENT_TYPE_UNSPECIFIED = -1;
    public static final int INTENT_TYPE_VIDEO = 2;
    public static final int INTENT_TYPE_VOICE_CONTROL = 4;
    public static final String IS_FIRST_SHOW_VIDEOTAG = "first_show_videotag";
    public static final String IS_FIRST_USE_CLONE_FREEZE_FRAME = "first_show_clone_freeze_frame";
    public static final String IS_FIRST_USE_CLONE_PHOTO = "first_show_clone_photo";
    public static final String IS_FIRST_USE_CLONE_VIDEO = "first_show_clone_video";
    public static final String KEY = "camera_settings_global";
    public static final int MORE_MODE_TAB_STYLE_DEFAULT = 0;
    public static final int MORE_MODE_TAB_STYLE_NEW = 1;
    public static final int MORE_MODE_TAB_STYLE_OLD = 0;
    public static final String SHORTCUT_VERSION = "shortcut_version";
    private static final String TAG = "DataItemGlobal";
    private static final int TIME_OUT = 30000;
    public static List sUseHints = new ArrayList();
    private boolean hasModesSortChanged = false;
    private int[] mAllSortModes;
    private DataBackUp mDataBackUp;
    private C0122O00000o mDataItemFeature;
    private DataItemRunning mDataItemRunning;
    private int mIntentType = 0;
    private int mIntentVideoQuality = -1;
    private boolean mIsForceMainBackCamera;
    private Boolean mIsTimeOut;
    private int mLastCameraId;
    private boolean mMimojiStandAlone;
    private ComponentModuleList mModuleList;
    private boolean mRetriedIfCameraError;
    private boolean mStartFromKeyguard;

    @Retention(RetentionPolicy.SOURCE)
    public @interface IntentType {
    }

    public @interface MoreModeTabStyle {
    }

    static {
        sUseHints.add("pref_camera_first_use_hint_shown_key");
        sUseHints.add("pref_camera_first_ai_scene_use_hint_shown_key");
        sUseHints.add("pref_camera_first_ultra_wide_use_hint_shown_key");
        sUseHints.add("pref_camera_first_portrait_use_hint_shown_key");
        sUseHints.add("pref_front_camera_first_use_hint_shown_key");
        sUseHints.add(DATA_COMMON_DOCUMENT_MODE_USE_HINT_SHOWN);
        sUseHints.add(DATA_COMMON_LPL_SELECTOR_USE_HINT_SHOWN);
        sUseHints.add(CameraSettings.KEY_RECORD_LOCATION);
    }

    public DataItemGlobal(C0122O00000o o00000o, DataItemRunning dataItemRunning, DataBackUp dataBackUp) {
        this.mDataItemFeature = o00000o;
        this.mDataItemRunning = dataItemRunning;
        this.mDataBackUp = dataBackUp;
        this.mMimojiStandAlone = this.mDataItemFeature.OOOOoo();
        this.mLastCameraId = getCurrentCameraId();
        this.mModuleList = new ComponentModuleList(this);
        this.mAllSortModes = getSortModes();
        if (getInt(CameraSettings.KEY_CAMERA_MORE_MODE_STYLE, 0) != 0) {
            applyMoreModeStyle();
        }
    }

    private boolean determineTimeOut() {
        if (!CameraSettings.retainCameraMode() || !this.mModuleList.isCommonMode(getCurrentMode())) {
            return isActualTimeOut();
        }
        return getCurrentMode() == 254;
    }

    private int[] getConfigCommonModes() {
        int[] iArr;
        boolean z;
        if (this.mMimojiStandAlone) {
            return new int[]{getDefaultMode(this.mIntentType)};
        }
        int[] O0ooO = this.mDataItemFeature.O0ooO();
        if (O0ooO == null) {
            return DEFAULT_SORT_MODES;
        }
        ArrayList arrayList = new ArrayList();
        for (int valueOf : O0ooO) {
            arrayList.add(Integer.valueOf(valueOf));
        }
        arrayList.add(Integer.valueOf(254));
        for (int i : DEFAULT_SORT_MODES) {
            Iterator it = arrayList.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((Integer) it.next()).intValue() == ComponentModuleList.getTransferredMode(i)) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (!z) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        int[] iArr2 = new int[arrayList.size()];
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            iArr2[i2] = ((Integer) arrayList.get(i2)).intValue();
        }
        return iArr2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006b, code lost:
        if (r3.mDataItemFeature.OOO0OOo() != false) goto L_0x002c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getCurrentCameraId(int i) {
        if (!(i == 166 || i == 167)) {
            String str = "pref_camera_id_key";
            if (i != 169) {
                if (!(i == 180 || i == 204 || i == 214 || i == 175)) {
                    if (i != 176) {
                        switch (i) {
                            case 171:
                                if (!this.mDataItemFeature.OOOo0Oo()) {
                                    return 0;
                                }
                                break;
                            case 172:
                                if (!this.mDataItemFeature.OOOo0OO()) {
                                    return 0;
                                }
                                break;
                            case 173:
                                if (!this.mDataItemFeature.OOo0Oo() && !this.mDataItemFeature.OOoo00()) {
                                    return 0;
                                }
                            default:
                                switch (i) {
                                    case 186:
                                    case 187:
                                    case 188:
                                        break;
                                    default:
                                        switch (i) {
                                            case 209:
                                            case 210:
                                            case 211:
                                                break;
                                        }
                                }
                        }
                    } else {
                        return 1;
                    }
                }
            }
            return Integer.parseInt(getString(str, String.valueOf(getDefaultCameraId(i))));
        }
        return 0;
    }

    private int getCurrentMode(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(DATA_COMMON_CURRENT_MODE);
        sb.append(i);
        return getInt(sb.toString(), getDefaultMode(i));
    }

    private int getCurrentModeForFrontCamera(int i) {
        int currentMode = getCurrentMode(i);
        switch (currentMode) {
            case 166:
            case 167:
            case 173:
            case 175:
                break;
            case 169:
            case 172:
                return 162;
            case 171:
                if (this.mDataItemFeature.OOOo0Oo()) {
                    return currentMode;
                }
                break;
            default:
                return currentMode;
        }
        return 163;
    }

    private int getDefaultCameraId(int i) {
        return 0;
    }

    private boolean isActualTimeOut() {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis - getLong(DATA_COMMON_OPEN_TIME, currentTimeMillis) > 30000 || this.mIsTimeOut == null;
    }

    public void applyMoreModeStyle() {
        int[] sortModes = getSortModes();
        if (!getBoolean(DATA_COMMON_USER_EDIT_MODES, false)) {
            int i = getInt(CameraSettings.KEY_CAMERA_MORE_MODE_STYLE, 0);
            int i2 = -1;
            for (int i3 = 0; i3 < sortModes.length; i3++) {
                if (sortModes[i3] == 254) {
                    i2 = i3;
                }
            }
            String str = TAG;
            if (i2 < 2) {
                Log.w(str, "filterByStyle find more index fail.");
                return;
            }
            if (i == 1) {
                if (i2 < sortModes.length - 1) {
                    int i4 = i2 + 1;
                    sortModes[i2] = sortModes[i4];
                    sortModes[i4] = 254;
                } else {
                    return;
                }
            } else if (i == 0) {
                int i5 = i2 - 1;
                sortModes[i2] = sortModes[i5];
                sortModes[i5] = 254;
            }
            this.mAllSortModes = sortModes;
            StringBuilder sb = new StringBuilder();
            sb.append("filterByStyle ");
            sb.append(Arrays.toString(sortModes));
            Log.d(str, sb.toString());
        }
    }

    public boolean getCTACanCollect() {
        return getBoolean(CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY, false);
    }

    public ComponentModuleList getComponentModuleList() {
        return this.mModuleList;
    }

    public int getCurrentCameraId() {
        return getCurrentCameraId(getCurrentMode());
    }

    public int getCurrentMode() {
        return getCurrentMode(this.mIntentType);
    }

    public int getDataBackUpKey(int i, int i2) {
        if (i == 165 || i == 185) {
            i = ComponentModuleList.getTransferredMode(i);
        }
        int i3 = i | ((this.mIntentType + 2) << 8) | (i2 << 12);
        return this.mStartFromKeyguard ? i3 | 65536 : i3;
    }

    public int getDefaultMode(int i) {
        if (i != 1) {
            if (i != 2) {
                return (i == 3 || i == 5 || !this.mMimojiStandAlone) ? 163 : 177;
            }
            return 162;
        }
        return 163;
    }

    public int getDisplayMode() {
        return (!this.mDataItemFeature.OOOOoO() || DataRepository.dataItemGlobal().getCurrentCameraId() != 1) ? 1 : 2;
    }

    public int getFavoriteModeCount() {
        int[] sortModes = getSortModes();
        for (int i = 0; i < sortModes.length; i++) {
            if (sortModes[i] == 254) {
                return i;
            }
        }
        return -1;
    }

    public int getIntentType() {
        return this.mIntentType;
    }

    public int getIntentVideoQuality() {
        return this.mIntentVideoQuality;
    }

    public int getLastCameraId() {
        return this.mLastCameraId;
    }

    @MoreModeTabStyle
    public int getMoreModeTabStyle() {
        if (!C0122O00000o.instance().OOo0oO()) {
            return 0;
        }
        return getInt(DATA_COMMON_USER_MORE_MODE_TAB_STYLE, 0);
    }

    public int getRawSuperNightImpl() {
        if (getCurrentMode() == 173) {
            if ((DataRepository.dataItemGlobal().getCurrentCameraId() == 0) && this.mDataItemFeature.OOoO0OO()) {
                int rawSuperNightImpl = this.mDataItemFeature.getRawSuperNightImpl();
                boolean OOo0oOO = this.mDataItemFeature.OOo0oOO();
                if (rawSuperNightImpl == 0) {
                    return 2;
                }
                if (rawSuperNightImpl != 1) {
                    if (rawSuperNightImpl == 2 && OOo0oOO) {
                        return 8;
                    }
                } else if (OOo0oOO) {
                    return 4;
                }
            }
        }
        return 0;
    }

    public int getShortCutVersion() {
        return getInt(SHORTCUT_VERSION, 0);
    }

    public int[] getSortModes() {
        int[] iArr = this.mAllSortModes;
        if (iArr != null) {
            return iArr;
        }
        String string = getString(DATA_COMMON_SORT_MODES, Arrays.toString(getConfigCommonModes()));
        try {
            int[] array = Arrays.stream(string.substring(1, string.length() - 1).split(",")).mapToInt(O000000o.INSTANCE).toArray();
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            for (int valueOf : array) {
                linkedHashSet.add(Integer.valueOf(valueOf));
            }
            if (linkedHashSet.size() != array.length) {
                Log.e(TAG, "found duplicate mode.");
            }
            return linkedHashSet.stream().mapToInt(O00000Oo.INSTANCE).toArray();
        } catch (NumberFormatException | PatternSyntaxException unused) {
            return DEFAULT_SORT_MODES;
        }
    }

    public boolean getStartFromKeyguard() {
        return this.mStartFromKeyguard;
    }

    public boolean hasModesChanged() {
        if (this.mAllSortModes == null) {
            return true;
        }
        if (!getBoolean(DATA_COMMON_USER_EDIT_MODES, false)) {
            return false;
        }
        return this.hasModesSortChanged;
    }

    public boolean isFirstShowCTAConCollect() {
        return !contains(CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY);
    }

    public boolean isFirstUseCloneFreezeFrame() {
        return getBoolean(IS_FIRST_USE_CLONE_FREEZE_FRAME, true);
    }

    public boolean isFirstUseClonePhoto() {
        return getBoolean(IS_FIRST_USE_CLONE_PHOTO, true);
    }

    public boolean isFirstUseCloneVideo() {
        return getBoolean(IS_FIRST_USE_CLONE_VIDEO, true);
    }

    public boolean isForceMainBackCamera() {
        return this.mIsForceMainBackCamera;
    }

    public boolean isGlobalSwitchOn(String str) {
        return getBoolean(str, false);
    }

    public boolean isIntentAction() {
        return this.mIntentType != 0;
    }

    public boolean isIntentIDPhoto() {
        return this.mIntentType == 5;
    }

    public boolean isNormalIntent() {
        return this.mIntentType == 0;
    }

    public boolean isOnSuperNightAlgoUpAndQuickShot() {
        return isOnSuperNightAlgoUpMode() && this.mDataItemFeature.OOoOOO0();
    }

    public boolean isOnSuperNightAlgoUpMode() {
        if (getCurrentMode() != 173) {
            return false;
        }
        boolean z = DataRepository.dataItemGlobal().getCurrentCameraId() == 1 && this.mDataItemFeature.OOoo00();
        boolean z2 = DataRepository.dataItemGlobal().getCurrentCameraId() == 0 && this.mDataItemFeature.OOo00Oo();
        return z2 || z;
    }

    public boolean isOnSuperNightHalfAlgoUp() {
        return getCurrentMode() == 173 && DataRepository.dataItemGlobal().getCurrentCameraId() == 0 && this.mDataItemFeature.OOo00o0();
    }

    public boolean isRetriedIfCameraError() {
        return this.mRetriedIfCameraError;
    }

    public boolean isTiktokMoreButtonEnabled(boolean z) {
        return getBoolean(z ? DATA_COMMON_TIKTOK_MORE_BUTTON_SHOW_APP : DATA_COMMON_TIKTOK_MORE_BUTTON_SHOW_MARKET, C0124O00000oO.O0o000o ? true : z);
    }

    public boolean isTimeOut() {
        Boolean bool = this.mIsTimeOut;
        return bool == null || bool.booleanValue();
    }

    public boolean isTransient() {
        return false;
    }

    public boolean matchCloneModelVersion(String str) {
        String str2 = DATA_CLONE_MODEL_VERSION;
        if (!contains(str2)) {
            return false;
        }
        String string = getString(str2, "");
        if (string.equals(str)) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("matchCloneModelVersion, pref version ");
        sb.append(string);
        sb.append(", asset version = ");
        sb.append(str);
        Log.w(TAG, sb.toString());
        return false;
    }

    public boolean matchCustomWatermarkVersion() {
        String O0ooO0o = this.mDataItemFeature.O0ooO0o();
        String str = DATA_COMMON_CUSTOM_WATERMARK_VERSION;
        if (!contains(str)) {
            return false;
        }
        if (arrayMapContainsKey(str)) {
            arrayMapRemove(str);
        }
        String string = getString(str, "");
        int indexOf = string.indexOf(58);
        if (indexOf > 0) {
            String substring = string.substring(0, indexOf);
            String substring2 = string.substring(indexOf + 1);
            if (substring.equals(C0122O00000o.instance().getClassName()) && substring2.equals(O0ooO0o)) {
                return true;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("mismatch custom watermark version: ");
        sb.append(string);
        Log.w(TAG, sb.toString());
        return false;
    }

    /* JADX WARNING: type inference failed for: r3v7, types: [com.android.camera.CameraIntentManager] */
    /* JADX WARNING: type inference failed for: r3v9, types: [com.android.camera.CameraIntentManager] */
    /* JADX WARNING: type inference failed for: r3v19 */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x01b5, code lost:
        if (r11 == 1) goto L_0x01bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x01b7, code lost:
        r7 = getCurrentMode(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x01bc, code lost:
        r7 = getCurrentModeForFrontCamera(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x01c0, code lost:
        r8 = getCurrentCameraId(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x01c4, code lost:
        if (r7 != 163) goto L_0x01dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x01d8, code lost:
        if (((com.android.camera.data.data.config.DataItemConfig) com.android.camera.data.DataRepository.provider().dataConfig(r8, r4)).getComponentConfigRatio().isSquareModule() == false) goto L_0x01dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x01da, code lost:
        r7 = 165;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x01de, code lost:
        if (r7 == 168) goto L_0x0202;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x01e2, code lost:
        if (r7 != 170) goto L_0x01e5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x01e9, code lost:
        if (isActualTimeOut() != false) goto L_0x01ed;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x01eb, code lost:
        if (r5 == false) goto L_0x020d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:0x01ef, code lost:
        if (r7 != 179) goto L_0x01f4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x01f1, code lost:
        r7 = 209;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x01f6, code lost:
        if (r7 != 185) goto L_0x01fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x01f8, code lost:
        r7 = 210;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:122:0x01fd, code lost:
        if (r7 != 213) goto L_0x020d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x01ff, code lost:
        r7 = 211;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x0208, code lost:
        if (r0.mDataItemFeature.OOoOoo0() == false) goto L_0x020c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x020a, code lost:
        r9 = 172;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x020c, code lost:
        r7 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x020d, code lost:
        com.android.camera.log.Log.d(r14, java.lang.String.format("parseIntent timeOut = %s, intentChanged = %s, action = %s, pendingOpenId = %s, pendingOpenModule = %s, intentCameraId = %s", new java.lang.Object[]{java.lang.Boolean.valueOf(r12), java.lang.Boolean.valueOf(r5), r3, java.lang.Integer.valueOf(r8), java.lang.Integer.valueOf(r7), java.lang.Integer.valueOf(r11)}));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x023c, code lost:
        if (r22 != false) goto L_0x0287;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x023e, code lost:
        r0.mIsTimeOut = java.lang.Boolean.valueOf(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x0244, code lost:
        if (r5 == false) goto L_0x0255;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x0246, code lost:
        r0.mIntentType = r4;
        r0.mIntentVideoQuality = r16;
        r0.mStartFromKeyguard = r1;
        r0.mModuleList.setIntentType(r0.mIntentType);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:0x0259, code lost:
        if (r7 == getCurrentMode()) goto L_0x0261;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x025b, code lost:
        setCurrentMode(r7);
        com.android.camera.module.ModuleManager.setActiveModuleIndex(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x0265, code lost:
        if (r8 == getCurrentCameraId()) goto L_0x026a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x0267, code lost:
        setCameraId(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:140:0x0270, code lost:
        if (r0.mIsTimeOut.booleanValue() == false) goto L_0x027c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x0272, code lost:
        r0.mDataItemRunning.clearArrayMap();
        r0.mDataBackUp.clearBackUp();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x027c, code lost:
        r0.mDataBackUp.revertOrCreateRunning(r0.mDataItemRunning, getDataBackUpKey(r7, r8));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x0294, code lost:
        return new androidx.core.util.Pair(java.lang.Integer.valueOf(r8), java.lang.Integer.valueOf(r7));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0091, code lost:
        r15 = r19;
        r4 = 0;
        r16 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x009c, code lost:
        if (r20.booleanValue() == false) goto L_0x0091;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x009f, code lost:
        r3 = com.android.camera.CameraIntentManager.getInstance(r19);
        r4 = r3.getCameraModeId();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00b1, code lost:
        if (r3.getCaller().equals(com.android.camera.CameraIntentManager.CALLER_MIUI_CAMERA) == false) goto L_0x00b8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00b3, code lost:
        com.android.camera.statistic.CameraStatUtils.trackShortcutClick(r19, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00ba, code lost:
        if (r4 != 160) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00c0, code lost:
        if (determineTimeOut() == false) goto L_0x00c7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00c2, code lost:
        r4 = getDefaultMode(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00c7, code lost:
        r4 = getCurrentMode(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00cb, code lost:
        r3 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        r3 = r3.isUseFrontCamera();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00d4, code lost:
        if (r3.isOnlyForceOpenMainBackCamera() != false) goto L_0x00d6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00d6, code lost:
        setForceMainBackCamera(true);
        r3 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00df, code lost:
        if (determineTimeOut() != false) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00e1, code lost:
        r3 = getDefaultCameraId(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00e6, code lost:
        r3 = getCurrentCameraId(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00ea, code lost:
        r5 = new java.lang.StringBuilder();
        r5.append("intent from voice control assist : pendingOpenId = ");
        r5.append(r3);
        r5.append(";pendingOpenModule = ");
        r5.append(r4);
        r5.append(",newIntentType = ");
        r5.append(0);
        com.android.camera.log.Log.d(r14, r5.toString());
        r0.mIntentType = 0;
        r0.mStartFromKeyguard = r1;
        r0.mModuleList.setIntentType(r0.mIntentType);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x011d, code lost:
        if (r4 == getCurrentMode()) goto L_0x0125;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x011f, code lost:
        setCurrentMode(r4);
        com.android.camera.module.ModuleManager.setActiveModuleIndex(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0129, code lost:
        if (r3 == getCurrentCameraId()) goto L_0x012e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x012b, code lost:
        setCameraId(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x013b, code lost:
        return new androidx.core.util.Pair(java.lang.Integer.valueOf(r3), java.lang.Integer.valueOf(r4));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x015b, code lost:
        r11 = com.android.camera.CameraIntentManager.getInstance(r19).getCameraFacing();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0163, code lost:
        if (r11 == -1) goto L_0x0168;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0165, code lost:
        setCameraIdTransient(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0168, code lost:
        if (r23 == false) goto L_0x0172;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x016e, code lost:
        if (determineTimeOut() == false) goto L_0x0172;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0170, code lost:
        r12 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0172, code lost:
        r12 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0175, code lost:
        if (r0.mIntentType != r4) goto L_0x017e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0179, code lost:
        if (r0.mStartFromKeyguard == r1) goto L_0x017c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x017c, code lost:
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x017e, code lost:
        r5 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x017f, code lost:
        r9 = 162;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0187, code lost:
        if (r8.equals(r3) == false) goto L_0x0190;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0189, code lost:
        r8 = getCurrentCameraId(163);
        r7 = 163;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0194, code lost:
        if (r7.equals(r3) == false) goto L_0x019d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0196, code lost:
        r8 = getCurrentCameraId(162);
        r7 = 162;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x01a5, code lost:
        if (com.android.camera.CameraIntentManager.getInstance(r19).isQuickLaunch() == false) goto L_0x01a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x01a8, code lost:
        if (r12 == false) goto L_0x01b5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x01aa, code lost:
        r7 = getDefaultMode(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x01ae, code lost:
        if (r11 >= 0) goto L_0x01c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x01b0, code lost:
        r8 = getDefaultCameraId(r7);
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v19
  assigns: [boolean, int, ?[int, float, boolean, short, byte, char, OBJECT, ARRAY]]
  uses: [com.android.camera.CameraIntentManager, int, ?[int, boolean, OBJECT, ARRAY, byte, short, char]]
  mth insns count: 234
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Pair parseIntent(Intent intent, Boolean bool, boolean z, boolean z2, boolean z3) {
        char c;
        int i;
        int i2;
        boolean z4 = z;
        setForceMainBackCamera(false);
        if (this.mDataItemFeature.OO0o00o() && Util.isScreenSlideOff(CameraAppImpl.getAndroidContext())) {
            setCameraId(0);
        }
        String action = intent.getAction();
        if (action == null) {
            action = "<unknown>";
        }
        String str = "android.media.action.VIDEO_CAMERA";
        String str2 = "android.media.action.STILL_IMAGE_CAMERA";
        switch (action.hashCode()) {
            case -1960745709:
                if (action.equals("android.media.action.IMAGE_CAPTURE")) {
                    c = 1;
                    break;
                }
            case -1658348509:
                if (action.equals("android.media.action.IMAGE_CAPTURE_SECURE")) {
                    c = 2;
                    break;
                }
            case -1528697361:
                if (action.equals(CameraIntentManager.ACTION_VOICE_CONTROL)) {
                    c = 8;
                    break;
                }
            case -1449841107:
                if (action.equals(CameraIntentManager.ACTION_QR_CODE_ZXING)) {
                    c = 5;
                    break;
                }
            case 464109999:
                if (action.equals(str2)) {
                    c = 6;
                    break;
                }
            case 701083699:
                if (action.equals("android.media.action.VIDEO_CAPTURE")) {
                    c = 3;
                    break;
                }
            case 1130890360:
                if (action.equals(str)) {
                    c = 7;
                    break;
                }
            case 1280056183:
                if (action.equals(CameraIntentManager.ACTION_QR_CODE_CAPTURE)) {
                    c = 4;
                    break;
                }
            case 1876747804:
                if (action.equals(CameraIntentManager.ACTION_IDPHOTO_IMAGE)) {
                    c = 0;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        String str3 = TAG;
        switch (c) {
            case 0:
                Intent intent2 = intent;
                i = -1;
                i2 = 5;
                break;
            case 1:
            case 2:
                Intent intent3 = intent;
                i = -1;
                i2 = 1;
                break;
            case 3:
                Intent intent4 = intent;
                i = CameraIntentManager.getInstance(intent).getVideoQuality();
                i2 = 2;
                break;
            case 4:
            case 5:
                Intent intent5 = intent;
                i = -1;
                i2 = 3;
                break;
            case 6:
            case 7:
                break;
            case 8:
                break;
        }
    }

    public String provideKey() {
        return "camera_settings_global";
    }

    public void reInit() {
        this.mModuleList.reInit(false);
        ProviderEditor editor = editor();
        this.mIsTimeOut = Boolean.valueOf(false);
        editor.putLong(DATA_COMMON_OPEN_TIME, System.currentTimeMillis());
        editor.putLong(CameraSettings.KEY_OPEN_CAMERA_FAIL, 0);
        int currentCameraId = getCurrentCameraId(getCurrentMode());
        this.mLastCameraId = currentCameraId;
        editor.putString("pref_camera_id_key", String.valueOf(currentCameraId));
        StringBuilder sb = new StringBuilder();
        sb.append("reInit: mLastCameraId = ");
        sb.append(this.mLastCameraId);
        sb.append(", currentCameraId = ");
        sb.append(currentCameraId);
        Log.d(TAG, sb.toString());
        if (getBoolean(DATA_COMMON_USER_EDIT_MODES, false)) {
            editor().putString(DATA_COMMON_SORT_MODES, Arrays.toString(getSortModes())).apply();
            this.hasModesSortChanged = false;
        }
        editor.apply();
    }

    public void resetAll() {
        this.mIsTimeOut = null;
        this.mAllSortModes = null;
        this.hasModesSortChanged = false;
        this.mModuleList.clear();
        editor().clear().putInt(CameraSettings.KEY_VERSION, 4).apply();
    }

    public void resetTimeOut() {
        this.mIsTimeOut = Boolean.valueOf(false);
        editor().putLong(DATA_COMMON_OPEN_TIME, System.currentTimeMillis()).apply();
    }

    public void setCTACanCollect(boolean z) {
        editor().putBoolean(CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY, z).apply();
    }

    public void setCameraId(int i) {
        this.mLastCameraId = getCurrentCameraId(getCurrentMode());
        editor().putString("pref_camera_id_key", String.valueOf(i)).apply();
        StringBuilder sb = new StringBuilder();
        sb.append("setCameraId: mLastCameraId = ");
        sb.append(this.mLastCameraId);
        sb.append(", cameraId = ");
        sb.append(i);
        Log.d(TAG, sb.toString());
    }

    public void setCameraIdTransient(int i) {
        this.mLastCameraId = getCurrentCameraId(getCurrentMode());
        putString("pref_camera_id_key", String.valueOf(i));
        StringBuilder sb = new StringBuilder();
        sb.append("setCameraIdTransient: mLastCameraId = ");
        sb.append(this.mLastCameraId);
        sb.append(", cameraId = ");
        sb.append(i);
        Log.d(TAG, sb.toString());
    }

    public void setCurrentMode(int i) {
        ProviderEditor editor = editor();
        StringBuilder sb = new StringBuilder();
        sb.append(DATA_COMMON_CURRENT_MODE);
        sb.append(this.mIntentType);
        editor.putInt(sb.toString(), i).apply();
    }

    public void setFirstUseCloneFreezeFrame(boolean z) {
        editor().putBoolean(IS_FIRST_USE_CLONE_FREEZE_FRAME, z).apply();
    }

    public void setFirstUseClonePhoto(boolean z) {
        editor().putBoolean(IS_FIRST_USE_CLONE_PHOTO, z).apply();
    }

    public void setFirstUseCloneVideo(boolean z) {
        editor().putBoolean(IS_FIRST_USE_CLONE_VIDEO, z).apply();
    }

    public void setForceMainBackCamera(boolean z) {
        this.mIsForceMainBackCamera = z;
    }

    public void setMoreModeStyle(@MoreModeTabStyle int i) {
        editor().putInt(DATA_COMMON_USER_MORE_MODE_TAB_STYLE, i).apply();
    }

    public void setRetriedIfCameraError(boolean z) {
        this.mRetriedIfCameraError = z;
    }

    public void setShortCutVersion(int i) {
        editor().putInt(SHORTCUT_VERSION, i).apply();
    }

    public void setSortModes(int[] iArr) {
        if (!Arrays.equals(this.mAllSortModes, iArr)) {
            this.mAllSortModes = iArr;
            this.hasModesSortChanged = true;
            StringBuilder sb = new StringBuilder();
            sb.append("setSortModes ");
            sb.append(Arrays.toString(iArr));
            Log.d(TAG, sb.toString());
        }
    }

    public void setStartFromKeyguard(boolean z) {
        this.mStartFromKeyguard = z;
    }

    public void updateCloneModelVersion(String str) {
        editor().putString(DATA_CLONE_MODEL_VERSION, str).apply();
        StringBuilder sb = new StringBuilder();
        sb.append("updateCloneModelVersion ");
        sb.append(str);
        Log.d(TAG, sb.toString());
    }

    public void updateCustomWatermarkVersion() {
        String O0ooO0o = this.mDataItemFeature.O0ooO0o();
        StringBuilder sb = new StringBuilder();
        sb.append(C0122O00000o.instance().getClassName());
        sb.append(":");
        sb.append(O0ooO0o);
        String sb2 = sb.toString();
        editor().putString(DATA_COMMON_CUSTOM_WATERMARK_VERSION, sb2).apply();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("custom watermark version updated: ");
        sb3.append(sb2);
        Log.i(TAG, sb3.toString());
    }

    public boolean useNewMoreTabStyle() {
        return getMoreModeTabStyle() == 1;
    }
}
