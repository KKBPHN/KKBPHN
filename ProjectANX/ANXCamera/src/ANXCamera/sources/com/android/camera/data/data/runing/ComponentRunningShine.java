package com.android.camera.data.data.runing;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.effect.FilterInfo;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.log.Log;
import com.android.camera2.CameraCapabilities;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ComponentRunningShine extends ComponentData {
    public static final int ENTRY_NONE = -1;
    public static final int ENTRY_POPUP_BEAUTY = 5;
    public static final int ENTRY_POPUP_SHINE = 4;
    public static final int ENTRY_TOP_BEAUTY = 2;
    public static final int ENTRY_TOP_FILTER = 3;
    public static final int ENTRY_TOP_SHINE = 1;
    public static final int FILTER_NATIVE_NONE_ID = 0;
    public static final String SHINE_BEAUTY_LEVEL_SMOOTH = "2";
    public static final String SHINE_BEAUTY_LEVEL_SWITCH = "1";
    public static final String SHINE_EYE_LIGHT = "9";
    public static final String SHINE_FIGURE = "6";
    public static final String SHINE_FILTER = "7";
    public static final String SHINE_FRONT_SUPER_NIGHT_BEAUTY = "17";
    public static final String SHINE_KALEIDOSCOPE = "16";
    public static final String SHINE_LIGHTING = "8";
    public static final String SHINE_LIVE_BEAUTY = "11";
    public static final String SHINE_LIVE_FILTER = "10";
    public static final String SHINE_LIVE_SPEED = "13";
    public static final String SHINE_LIVE_STICKER = "12";
    public static final String SHINE_MAKEUP = "5";
    public static final String SHINE_MI_LIVE_BEAUTY = "15";
    public static final String SHINE_MODEL_ADVANCE = "3";
    public static final String SHINE_MODEL_REMODELING = "4";
    public static final String SHINE_VIDEO_BOKEH_LEVEL = "14";
    private static final String TAG = "ComponentRunningShine";
    private boolean isFrontCamera;
    private BeautyValues mBeautyValues;
    private int mBeautyVersion;
    private boolean mCurrentStatus;
    private String mCurrentTipType;
    @ShineType
    private String mCurrentType;
    @ShineType
    private String mDefaultType;
    private boolean mIsClosed;
    private HashMap mIsVideoBeautySwitchedOnMap = new HashMap();
    @ShineEntry
    private int mShineEntry;
    private boolean mSupportBeautyBody;
    private boolean mSupportBeautyLevel;
    private boolean mSupportBeautyMakeUp;
    private boolean mSupportBeautyMiLive;
    private boolean mSupportBeautyModel;
    private boolean mSupportFrontNightBeauty;
    private boolean mSupportHalColorRententionBack;
    private boolean mSupportHalColorRententionFront;
    private boolean mSupportHalVideoBokehColorRetentionBack;
    private boolean mSupportHalVideoBokehColorRetentionFront;
    private boolean mSupportHalVideoBokehLevel;
    private boolean mSupportHalVideoFilter;
    private boolean mSupportSmoothLevel;
    private boolean mTargetShow;
    private TypeElementsBeauty mTypeElementsBeauty = new TypeElementsBeauty(this);

    public @interface ShineEntry {
    }

    public @interface ShineType {
    }

    public ComponentRunningShine(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    private ComponentDataItem generateBeautyLevelItem(boolean z) {
        String str = "1";
        return C0124O00000oO.Oo00O() ? new ComponentDataItem((int) R.drawable.ic_beauty_off, (int) R.drawable.ic_beauty_on, (int) R.string.beauty_fragment_tab_name_3d_beauty, str) : new ComponentDataItem((int) R.drawable.ic_beauty_off, (int) R.drawable.ic_beauty_on, (int) R.string.beauty_fragment_tab_name_beauty, str);
    }

    private ComponentDataItem generateFigureItem() {
        return new ComponentDataItem((int) R.drawable.ic_beauty_off, (int) R.drawable.ic_beauty_on, isSmoothDependBeautyVersion() ? R.string.beauty_fragment_tab_name_3d_beauty : R.string.beauty_body, "6");
    }

    private ComponentDataItem generateFilterItem() {
        return new ComponentDataItem((int) R.drawable.ic_new_effect_button_normal, (int) R.drawable.ic_new_effect_button_selected, (int) R.string.pref_camera_coloreffect_title, "7");
    }

    private ComponentDataItem generateFrontSuperNightBeauty() {
        return new ComponentDataItem((int) R.drawable.ic_beauty_off, (int) R.drawable.ic_beauty_on, (int) R.string.beauty_fragment_tab_name_3d_beauty, "17");
    }

    private ComponentDataItem generateKaleidoscopeItem() {
        return new ComponentDataItem((int) R.drawable.ic_vector_kaleidoscope, (int) R.drawable.ic_vector_kaleidoscope, (int) R.string.kaleidoscope_fragment_tab_name, "16");
    }

    private ComponentDataItem generateMakeupItem() {
        return new ComponentDataItem((int) R.drawable.ic_beauty_off, (int) R.drawable.ic_beauty_on, (int) R.string.beauty_fragment_tab_name_3d_makeup, "5");
    }

    private ComponentDataItem generateMiLiveItem() {
        return new ComponentDataItem((int) R.drawable.ic_beauty_off, (int) R.drawable.ic_beauty_on, (int) R.string.beauty_fragment_tab_name_3d_beauty, "15");
    }

    private ComponentDataItem generateModelItem() {
        if (!C0124O00000oO.Oo00O()) {
            return new ComponentDataItem((int) R.drawable.ic_beauty_off, (int) R.drawable.ic_beauty_on, (int) R.string.beauty_fragment_tab_name_makeup, "3");
        }
        return new ComponentDataItem((int) R.drawable.ic_beauty_off, (int) R.drawable.ic_beauty_on, isSmoothDependBeautyVersion() ? R.string.beauty_fragment_tab_name_3d_beauty : R.string.beauty_fragment_tab_name_3d_remodeling, "4");
    }

    private ComponentDataItem generateSmoothLevelItem(boolean z) {
        return new ComponentDataItem((int) R.drawable.ic_beauty_off, (int) R.drawable.ic_beauty_on, (int) R.string.beauty_fragment_tab_name_3d_beauty, "2");
    }

    private ComponentDataItem generateVideoBokeh() {
        return new ComponentDataItem((int) R.drawable.ic_beauty_off, (int) R.drawable.ic_beauty_on, (int) R.string.fragment_tab_name_bokeh, "14");
    }

    public void clearArrayMap() {
        this.mIsVideoBeautySwitchedOnMap.clear();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0113, code lost:
        r4 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0150, code lost:
        r3 = com.android.camera.CameraSettings.isFaceBeautyOn(r13, r12.mBeautyValues);
     */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e3  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f3  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0116  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x012e  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0136  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0139  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x014e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean determineStatus(int i) {
        char c;
        boolean z = false;
        if (this.mItems == null) {
            return false;
        }
        if (this.mBeautyValues == null) {
            this.mBeautyValues = new BeautyValues();
        }
        if (!isClosed() && this.mItems != null) {
            boolean isVideoShineForceOn = isVideoShineForceOn(i);
            Iterator it = this.mItems.iterator();
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = false;
            while (true) {
                boolean z6 = true;
                if (it.hasNext()) {
                    ComponentDataItem componentDataItem = (ComponentDataItem) it.next();
                    if (componentDataItem != null) {
                        String str = componentDataItem.mValue;
                        int hashCode = str.hashCode();
                        if (hashCode == 1567) {
                            if (str.equals("10")) {
                                c = 10;
                                switch (c) {
                                    case 0:
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        break;
                                    case 6:
                                        break;
                                    case 7:
                                        break;
                                    case 8:
                                        break;
                                    case 9:
                                        break;
                                    case 10:
                                        break;
                                    case 11:
                                        break;
                                    case 12:
                                        break;
                                }
                            }
                        } else if (hashCode == 1568) {
                            if (str.equals("11")) {
                                c = 8;
                                switch (c) {
                                    case 0:
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        if (z2) {
                                            break;
                                        }
                                    case 6:
                                        if (!z2) {
                                            if (!CameraSettings.isFaceBeautyOn(i, this.mBeautyValues) && !CameraSettings.isEyeLightOpen()) {
                                                z6 = false;
                                            }
                                            z2 = z6;
                                            break;
                                        } else {
                                            break;
                                        }
                                    case 7:
                                        if (z2) {
                                            break;
                                        }
                                    case 8:
                                        if (z2) {
                                            break;
                                        } else {
                                            z2 = CameraSettings.isLiveBeautyOpen();
                                            break;
                                        }
                                    case 9:
                                        if (z3) {
                                            break;
                                        } else {
                                            int shaderEffect = CameraSettings.getShaderEffect();
                                            if (!supportVideoFilter() ? shaderEffect == FilterInfo.FILTER_ID_NONE || shaderEffect <= 0 : shaderEffect == 0) {
                                                z6 = z3;
                                            }
                                        }
                                    case 10:
                                        if (!z3) {
                                            if (DataRepository.dataItemLive().getLiveFilter() == 0) {
                                                break;
                                            }
                                        } else {
                                            break;
                                        }
                                    case 11:
                                        if (z4) {
                                            break;
                                        } else {
                                            float videoBokehRatio = CameraSettings.getVideoBokehRatio();
                                            int videoBokehColorRetentionMode = CameraSettings.getVideoBokehColorRetentionMode();
                                            if (!(videoBokehRatio == 0.0f || videoBokehColorRetentionMode == 0)) {
                                                z4 = true;
                                                break;
                                            }
                                        }
                                    case 12:
                                        if (z5) {
                                            break;
                                        } else {
                                            z5 = DataRepository.dataItemRunning().getComponentRunningKaleidoscope().isSwitchOn();
                                            break;
                                        }
                                }
                            }
                        } else {
                            switch (hashCode) {
                                case 49:
                                    if (str.equals("1")) {
                                        c = 0;
                                        break;
                                    }
                                case 50:
                                    if (str.equals("2")) {
                                        c = 7;
                                        break;
                                    }
                                case 51:
                                    if (str.equals("3")) {
                                        c = 1;
                                        break;
                                    }
                                case 52:
                                    if (str.equals("4")) {
                                        c = 2;
                                        break;
                                    }
                                case 53:
                                    if (str.equals("5")) {
                                        c = 6;
                                        break;
                                    }
                                case 54:
                                    if (str.equals("6")) {
                                        c = 4;
                                        break;
                                    }
                                case 55:
                                    if (str.equals("7")) {
                                        c = 9;
                                        break;
                                    }
                                default:
                                    switch (hashCode) {
                                        case 1571:
                                            if (str.equals("14")) {
                                                c = 11;
                                                break;
                                            }
                                        case 1572:
                                            if (str.equals("15")) {
                                                c = 3;
                                                break;
                                            }
                                        case 1573:
                                            if (str.equals("16")) {
                                                c = 12;
                                                break;
                                            }
                                        case 1574:
                                            if (str.equals("17")) {
                                                c = 5;
                                                break;
                                            }
                                    }
                            }
                        }
                        c = 65535;
                        switch (c) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                break;
                            case 6:
                                break;
                            case 7:
                                break;
                            case 8:
                                break;
                            case 9:
                                break;
                            case 10:
                                break;
                            case 11:
                                break;
                            case 12:
                                break;
                        }
                    }
                } else if (isVideoShineForceOn || z2 || z3 || z4 || z5) {
                    z = true;
                }
            }
        }
        this.mCurrentStatus = z;
        return this.mCurrentStatus;
    }

    public int getBeautyVersion() {
        return this.mBeautyVersion;
    }

    @DrawableRes
    public int getBottomEntryRes(int i) {
        this.mCurrentStatus = determineStatus(i);
        int i2 = this.mShineEntry;
        if (i2 == 4) {
            boolean z = this.mCurrentStatus;
            return R.drawable.ic_vector_shine;
        } else if (i2 != 5) {
            return R.drawable.ic_shine_off;
        } else {
            return this.mCurrentStatus ? R.drawable.ic_beauty_on : R.drawable.ic_beauty_off;
        }
    }

    public boolean getCurrentStatus() {
        return this.mCurrentStatus;
    }

    public String getCurrentTipType() {
        return this.mCurrentTipType;
    }

    @ShineType
    public String getCurrentType() {
        return this.mCurrentType;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return this.mDefaultType;
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        return null;
    }

    @StringRes
    public int getTopConfigEntryDesRes() {
        int i = this.mShineEntry;
        return i != 2 ? i != 3 ? R.string.accessibility_beauty_function_panel_on : R.string.accessibility_filter_open_panel : R.string.accessibility_beauty_panel_open;
    }

    @DrawableRes
    public int getTopConfigEntryRes(int i) {
        this.mCurrentStatus = determineStatus(i);
        int i2 = this.mShineEntry;
        if (i2 == 1) {
            boolean z = this.mCurrentStatus;
            return R.drawable.ic_shine_off;
        } else if (i2 == 2) {
            boolean z2 = this.mCurrentStatus;
            return R.drawable.ic_vector_beauty_off;
        } else if (i2 != 3) {
            return R.drawable.ic_shine_off;
        } else {
            return this.mCurrentStatus ? R.drawable.ic_new_effect_button_selected : R.drawable.ic_new_effect_button_normal;
        }
    }

    @DrawableRes
    public int getTopConfigEntryShadowRes(int i) {
        switch (i) {
            case R.drawable.ic_new_effect_button_normal /*2131231480*/:
            case R.drawable.ic_new_effect_button_selected /*2131231482*/:
                return R.drawable.ic_new_effect_button_normal_shadow;
            case R.drawable.ic_shine_off /*2131231548*/:
                return R.drawable.ic_shine_off_shadow;
            case R.drawable.ic_vector_beauty_off /*2131231568*/:
                return R.drawable.ic_vector_beauty_off_shadow;
            default:
                return -1;
        }
    }

    public int getTopConfigItem() {
        int i = this.mShineEntry;
        if (i == 1 || i == 2 || i == 3) {
            return 212;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("unknown Shine");
        sb.append(this.mShineEntry);
        throw new RuntimeException(sb.toString());
    }

    public TypeElementsBeauty getTypeElementsBeauty() {
        return this.mTypeElementsBeauty;
    }

    public boolean isClosed() {
        return this.mIsClosed;
    }

    public boolean isLegacyBeautyVersion() {
        return this.mBeautyVersion == 1;
    }

    public boolean isSmoothDependBeautyVersion() {
        return this.mBeautyVersion == 3;
    }

    public boolean isTargetShow() {
        return this.mTargetShow;
    }

    public boolean isTopBeautyEntry() {
        return this.mShineEntry == 2;
    }

    public boolean isTopFilterEntry() {
        return this.mShineEntry == 3;
    }

    public boolean isTopShineEntry() {
        return this.mShineEntry == 1;
    }

    public boolean isVideoShineForceOn(int i) {
        if (i != 162 && i != 169 && i != 180) {
            return false;
        }
        String str = this.isFrontCamera ? "front" : "back";
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(str);
        return ((Boolean) this.mIsVideoBeautySwitchedOnMap.getOrDefault(sb.toString(), Boolean.FALSE)).booleanValue();
    }

    public void reInit() {
        if (this.mItems == null) {
            this.mItems = new CopyOnWriteArrayList();
        } else {
            this.mItems.clear();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:129:0x0291, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().isSupportBeautyBody() != false) goto L_0x0293;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:0x02ae, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOOoO() == false) goto L_0x0293;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x0310, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOOoOo0() != false) goto L_0x016d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0118, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().isSupportShortVideoBeautyBody() != false) goto L_0x0127;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0135, code lost:
        if (r2 == 1) goto L_0x0153;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x014f, code lost:
        if (r2 == 1) goto L_0x0153;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0152, code lost:
        r4 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0153, code lost:
        r2 = generateSmoothLevelItem(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x016b, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOOoOo0() != false) goto L_0x016d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities, boolean z) {
        ComponentDataItem componentDataItem;
        List list;
        ComponentDataItem componentDataItem2;
        List list2;
        StringBuilder sb;
        boolean z2;
        boolean z3;
        ComponentDataItem componentDataItem3;
        List list3;
        ComponentDataItem componentDataItem4;
        List list4;
        List list5;
        ComponentDataItem generateSmoothLevelItem;
        boolean z4;
        int i3 = i;
        int i4 = i2;
        boolean z5 = true;
        this.isFrontCamera = i4 == 1;
        reInit();
        this.mBeautyVersion = cameraCapabilities.getBeautyVersion();
        if (this.mBeautyVersion < 0) {
            if (C0124O00000oO.Oo00O()) {
                this.mBeautyVersion = 2;
            } else {
                this.mBeautyVersion = 1;
            }
        }
        this.mShineEntry = -1;
        this.mDefaultType = null;
        this.mSupportBeautyLevel = false;
        this.mSupportSmoothLevel = false;
        this.mSupportBeautyModel = false;
        this.mSupportBeautyMakeUp = false;
        this.mSupportBeautyBody = false;
        this.mSupportBeautyMiLive = false;
        this.mSupportFrontNightBeauty = false;
        this.mSupportHalVideoFilter = false;
        this.mSupportHalColorRententionFront = false;
        this.mSupportHalColorRententionBack = false;
        this.mSupportHalVideoBokehColorRetentionFront = false;
        this.mSupportHalVideoBokehColorRetentionBack = false;
        String str = "7";
        if (i3 != 165) {
            if (i3 != 167) {
                String str2 = "  mSupportColorRententionBack:";
                String str3 = "mSupportColorRententionFront:";
                String str4 = TAG;
                if (i3 == 169) {
                    CameraCapabilities cameraCapabilities2 = cameraCapabilities;
                    if (cameraCapabilities.isSupportVideoFilter() && ((C0122O00000o.instance().OOO00O0() || C0122O00000o.instance().OOO00Oo()) && !cameraCapabilities.isSupportVideoMasterFilter())) {
                        this.mShineEntry = 3;
                        this.mItems.add(generateFilterItem());
                        this.mSupportHalVideoFilter = true;
                        this.mSupportHalColorRententionBack = cameraCapabilities.isSupportVideoFilterColorRetentionBack();
                        sb = new StringBuilder();
                    }
                    this.mDefaultType = ((ComponentDataItem) this.mItems.get(0)).mValue;
                    this.mCurrentType = this.mDefaultType;
                } else if (i3 != 171) {
                    if (i3 != 180) {
                        if (i3 != 183) {
                            if (i3 != 184) {
                                if (i3 != 204) {
                                    if (i3 != 205) {
                                        switch (i3) {
                                            case 161:
                                                if (cameraCapabilities.isSupportVideoBeauty()) {
                                                    this.mShineEntry = 4;
                                                    if (i4 != 0) {
                                                        if (isSmoothDependBeautyVersion()) {
                                                            this.mSupportSmoothLevel = true;
                                                            list5 = this.mItems;
                                                            break;
                                                        } else {
                                                            this.mSupportBeautyLevel = true;
                                                            list5 = this.mItems;
                                                            if (i4 != 1) {
                                                                z4 = false;
                                                            }
                                                            generateSmoothLevelItem = generateBeautyLevelItem(z4);
                                                        }
                                                    } else {
                                                        this.mDefaultType = str;
                                                        if (isSmoothDependBeautyVersion()) {
                                                            this.mSupportSmoothLevel = true;
                                                            if (!C0122O00000o.instance().isSupportShortVideoBeautyBody()) {
                                                                list5 = this.mItems;
                                                                break;
                                                            }
                                                        } else {
                                                            this.mSupportBeautyLevel = true;
                                                            this.mItems.add(generateBeautyLevelItem(i4 == 1));
                                                            break;
                                                        }
                                                        this.mSupportBeautyBody = true;
                                                        list5 = this.mItems;
                                                        generateSmoothLevelItem = generateFigureItem();
                                                    }
                                                    list5.add(generateSmoothLevelItem);
                                                } else {
                                                    this.mShineEntry = 3;
                                                }
                                                this.mItems.add(generateFilterItem());
                                                break;
                                            case 162:
                                                break;
                                            case 163:
                                                break;
                                            default:
                                                switch (i3) {
                                                    case 173:
                                                        if (i4 == 1 && C0122O00000o.instance().OOO0OOO()) {
                                                            this.mShineEntry = 4;
                                                            this.mSupportSmoothLevel = true;
                                                            this.mSupportFrontNightBeauty = true;
                                                            list = this.mItems;
                                                            componentDataItem = generateFrontSuperNightBeauty();
                                                        }
                                                    case 174:
                                                        this.mShineEntry = 4;
                                                        String str5 = "10";
                                                        this.mDefaultType = str5;
                                                        this.mItems.add(new ComponentDataItem((int) R.drawable.ic_beauty_off, (int) R.drawable.ic_beauty_on, (int) R.string.beauty_fragment_tab_name_3d_beauty, "11"));
                                                        this.mItems.add(new ComponentDataItem((int) R.drawable.ic_new_effect_button_normal, (int) R.drawable.ic_new_effect_button_selected, (int) R.string.pref_camera_coloreffect_title, str5));
                                                        break;
                                                    case 175:
                                                        break;
                                                    case 176:
                                                        this.mShineEntry = 4;
                                                        if (!isSmoothDependBeautyVersion()) {
                                                            this.mSupportBeautyLevel = true;
                                                            list = this.mItems;
                                                            if (i4 != 1) {
                                                                z5 = false;
                                                            }
                                                            componentDataItem = generateBeautyLevelItem(z5);
                                                        } else {
                                                            this.mSupportSmoothLevel = true;
                                                            list = this.mItems;
                                                            if (i4 != 1) {
                                                                z5 = false;
                                                            }
                                                            componentDataItem = generateSmoothLevelItem(z5);
                                                        }
                                                        list.add(componentDataItem);
                                                        break;
                                                    case 177:
                                                        break;
                                                }
                                                break;
                                        }
                                    }
                                }
                                if (cameraCapabilities.isSupportVideoBeauty()) {
                                    this.mCurrentTipType = "2";
                                    if (!isSmoothDependBeautyVersion()) {
                                        this.mShineEntry = 2;
                                        this.mSupportBeautyLevel = true;
                                        list4 = this.mItems;
                                        componentDataItem4 = generateBeautyLevelItem(i4 == 1);
                                    } else {
                                        this.mShineEntry = 1;
                                        this.mSupportSmoothLevel = true;
                                        list4 = this.mItems;
                                        componentDataItem4 = generateSmoothLevelItem(i4 == 1);
                                    }
                                    list4.add(componentDataItem4);
                                }
                                boolean isSupportVideoFilter = cameraCapabilities.isSupportVideoFilter();
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("isSupportVideoFilter: ");
                                sb2.append(isSupportVideoFilter);
                                Log.i(str4, sb2.toString());
                                if (isSupportVideoFilter && !cameraCapabilities.isSupportVideoMasterFilter()) {
                                    this.mSupportHalVideoFilter = true;
                                    if (i4 == 0) {
                                        this.mDefaultType = str;
                                        this.mCurrentTipType = str;
                                    }
                                    this.mItems.add(generateFilterItem());
                                }
                                boolean isSupportVideoBokehAdjust = cameraCapabilities.isSupportVideoBokehAdjust();
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("isSupportVideoBokehLevel:");
                                sb3.append(isSupportVideoBokehAdjust);
                                Log.i(str4, sb3.toString());
                                if (isSupportVideoBokehAdjust && z) {
                                    this.mSupportHalVideoBokehLevel = true;
                                    this.mItems.add(generateVideoBokeh());
                                }
                                boolean isSupportVideoBokehColorRetention = cameraCapabilities.isSupportVideoBokehColorRetention(this.isFrontCamera);
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("isSupportVideoBokehColorRetention:");
                                sb4.append(isSupportVideoBokehColorRetention);
                                Log.i(str4, sb4.toString());
                                if (isSupportVideoBokehColorRetention) {
                                    if (this.isFrontCamera) {
                                        this.mSupportHalVideoBokehColorRetentionFront = true;
                                    } else {
                                        this.mSupportHalVideoBokehColorRetentionBack = true;
                                    }
                                }
                                this.mSupportHalColorRententionFront = cameraCapabilities.isSupportVideoFilterColorRetentionFront();
                                this.mSupportHalColorRententionBack = cameraCapabilities.isSupportVideoFilterColorRetentionBack();
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append(str3);
                                sb5.append(this.mSupportHalColorRententionFront);
                                sb5.append(str2);
                                sb5.append(this.mSupportHalColorRententionBack);
                                Log.i(str4, sb5.toString());
                                if (this.mItems.size() > 1) {
                                    this.mShineEntry = 1;
                                }
                            }
                            this.mSupportSmoothLevel = true;
                        } else {
                            CameraCapabilities cameraCapabilities3 = cameraCapabilities;
                            if (cameraCapabilities.isSupportBeauty()) {
                                this.mShineEntry = 4;
                                if (!isSmoothDependBeautyVersion()) {
                                    this.mSupportBeautyLevel = true;
                                    this.mItems.add(generateBeautyLevelItem(i4 == 1));
                                } else {
                                    this.mSupportSmoothLevel = true;
                                }
                                boolean isSmoothDependBeautyVersion = isSmoothDependBeautyVersion();
                                if (i4 == 0) {
                                    if (isSmoothDependBeautyVersion) {
                                        if (C0122O00000o.instance().isSupportBeautyBody()) {
                                        }
                                    }
                                    this.mSupportBeautyBody = true;
                                    list3 = this.mItems;
                                    componentDataItem3 = generateFigureItem();
                                    list3.add(componentDataItem3);
                                } else {
                                    if (!isSmoothDependBeautyVersion) {
                                        if (C0122O00000o.instance().OO0OOOO() || !C0122O00000o.instance().isSupportShortVideoBeautyBody()) {
                                            this.mSupportBeautyLevel = true;
                                            list3 = this.mItems;
                                            componentDataItem3 = generateBeautyLevelItem(false);
                                            list3.add(componentDataItem3);
                                        }
                                    } else if (C0122O00000o.instance().OO0OOOO() || !C0122O00000o.instance().isSupportShortVideoBeautyBody()) {
                                        this.mSupportSmoothLevel = true;
                                    }
                                    this.mSupportBeautyMiLive = true;
                                    list3 = this.mItems;
                                    componentDataItem3 = generateMiLiveItem();
                                    list3.add(componentDataItem3);
                                }
                                list3 = this.mItems;
                                componentDataItem3 = generateSmoothLevelItem(false);
                                list3.add(componentDataItem3);
                            } else {
                                this.mShineEntry = 3;
                            }
                            this.mDefaultType = str;
                            this.mItems.add(new ComponentDataItem((int) R.drawable.ic_new_effect_button_normal, (int) R.drawable.ic_new_effect_button_selected, (int) R.string.pref_camera_coloreffect_title, str));
                        }
                        list = this.mItems;
                        componentDataItem = generateKaleidoscopeItem();
                        list.add(componentDataItem);
                    } else {
                        CameraCapabilities cameraCapabilities4 = cameraCapabilities;
                        if (cameraCapabilities.isSupportVideoFilter() && !cameraCapabilities.isSupportVideoMasterFilter()) {
                            this.mShineEntry = 3;
                            this.mItems.add(generateFilterItem());
                            this.mSupportHalVideoFilter = true;
                            this.mSupportHalColorRententionBack = cameraCapabilities.isSupportVideoFilterColorRetentionBack();
                            sb = new StringBuilder();
                        }
                    }
                    if (this.mDefaultType == null && !this.mItems.isEmpty()) {
                        this.mDefaultType = ((ComponentDataItem) this.mItems.get(0)).mValue;
                    }
                    this.mCurrentType = this.mDefaultType;
                } else {
                    this.mShineEntry = 4;
                    if (C0122O00000o.instance().OO0oOo0()) {
                        if (!isSmoothDependBeautyVersion()) {
                            this.mSupportBeautyLevel = true;
                            list2 = this.mItems;
                            if (i4 != 1) {
                                z3 = false;
                            }
                            componentDataItem2 = generateBeautyLevelItem(z3);
                        } else {
                            this.mSupportSmoothLevel = true;
                            list2 = this.mItems;
                            if (i4 != 1) {
                                z2 = false;
                            }
                            componentDataItem2 = generateSmoothLevelItem(z2);
                        }
                        list2.add(componentDataItem2);
                    }
                    list = this.mItems;
                    componentDataItem = generateFilterItem();
                    list.add(componentDataItem);
                    this.mDefaultType = ((ComponentDataItem) this.mItems.get(0)).mValue;
                    this.mCurrentType = this.mDefaultType;
                }
                sb.append(str3);
                sb.append(this.mSupportHalColorRententionFront);
                sb.append(str2);
                sb.append(this.mSupportHalColorRententionBack);
                Log.i(str4, sb.toString());
                this.mDefaultType = ((ComponentDataItem) this.mItems.get(0)).mValue;
                this.mCurrentType = this.mDefaultType;
            }
            this.mShineEntry = 3;
            list = this.mItems;
            componentDataItem = generateFilterItem();
            list.add(componentDataItem);
            this.mDefaultType = ((ComponentDataItem) this.mItems.get(0)).mValue;
            this.mCurrentType = this.mDefaultType;
        }
        CameraCapabilities cameraCapabilities5 = cameraCapabilities;
        if (!CameraSettings.isUltraPixelRearOn()) {
            if (isSmoothDependBeautyVersion() || C0124O00000oO.O0o0oO) {
                this.mSupportSmoothLevel = true;
            } else {
                this.mSupportBeautyLevel = true;
                this.mItems.add(generateBeautyLevelItem(i4 == 1));
            }
            if (i4 == 0) {
                this.mShineEntry = 1;
                this.mDefaultType = str;
                if (C0122O00000o.instance().isSupportBeautyBody()) {
                    this.mSupportBeautyBody = true;
                    list2 = this.mItems;
                    componentDataItem2 = generateFigureItem();
                } else if (isSmoothDependBeautyVersion()) {
                    list2 = this.mItems;
                    componentDataItem2 = generateSmoothLevelItem(false);
                }
                list2.add(componentDataItem2);
            } else {
                this.mShineEntry = 4;
                if (!C0122O00000o.instance().OO0OOOO()) {
                    this.mSupportBeautyModel = true;
                    this.mItems.add(generateModelItem());
                    if (C0122O00000o.instance().OOo00o() && cameraCapabilities.isSupportBeautyMakeup()) {
                        this.mSupportBeautyMakeUp = true;
                    }
                } else if (isSmoothDependBeautyVersion()) {
                    list2 = this.mItems;
                    componentDataItem2 = generateSmoothLevelItem(true);
                    list2.add(componentDataItem2);
                }
            }
        } else {
            if (i4 != 0) {
                this.mShineEntry = 4;
            }
            this.mShineEntry = 3;
        }
        list = this.mItems;
        componentDataItem = generateFilterItem();
        list.add(componentDataItem);
        this.mDefaultType = ((ComponentDataItem) this.mItems.get(0)).mValue;
        this.mCurrentType = this.mDefaultType;
    }

    public void setClosed(boolean z) {
        this.mIsClosed = z;
    }

    public void setCurrentTipType(String str) {
        this.mCurrentTipType = str;
    }

    public void setCurrentType(@ShineType String str) {
        this.mCurrentType = str;
    }

    public void setTargetShow(boolean z) {
        this.mTargetShow = z;
    }

    public void setVideoShineForceOn(int i, boolean z) {
        String str = this.isFrontCamera ? "front" : "back";
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(str);
        this.mIsVideoBeautySwitchedOnMap.put(sb.toString(), Boolean.valueOf(z));
    }

    public boolean supportBeautyBody() {
        return this.mSupportBeautyBody;
    }

    public boolean supportBeautyLevel() {
        return this.mSupportBeautyLevel;
    }

    public boolean supportBeautyMakeUp() {
        return this.mSupportBeautyMakeUp;
    }

    public boolean supportBeautyMiLive() {
        return this.mSupportBeautyMiLive;
    }

    public boolean supportBeautyModel() {
        return this.mSupportBeautyModel;
    }

    public boolean supportColorRentention() {
        return this.isFrontCamera ? this.mSupportHalColorRententionFront : this.mSupportHalColorRententionBack;
    }

    public boolean supportFrontNightBeauty() {
        return this.mSupportFrontNightBeauty;
    }

    public boolean supportPopUpEntry() {
        int i = this.mShineEntry;
        return i == 4 || i == 5;
    }

    public boolean supportSmoothLevel() {
        return this.mSupportSmoothLevel;
    }

    public boolean supportTopConfigEntry() {
        int i = this.mShineEntry;
        return i == 1 || i == 2 || i == 3;
    }

    public boolean supportVideoBokehColorRetention() {
        return this.isFrontCamera ? this.mSupportHalVideoBokehColorRetentionFront : this.mSupportHalVideoBokehColorRetentionBack;
    }

    public boolean supportVideoFilter() {
        return this.mSupportHalVideoFilter;
    }
}
