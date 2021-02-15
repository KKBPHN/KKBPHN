package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.res.Resources;
import android.util.Size;
import android.util.SparseBooleanArray;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.log.Log;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentRunningUltraPixel extends ComponentData {
    private static final String TAG = "ComponentRunningUltraPixel";
    public static final String ULTRA_PIXEL_OFF = "OFF";
    public static final String ULTRA_PIXEL_ON_FRONT_32M = "FRONT_0x1";
    public static final String ULTRA_PIXEL_ON_REAR_108M = "REAR_0x3";
    public static final String ULTRA_PIXEL_ON_REAR_48M = "REAR_0x2";
    public static final String ULTRA_PIXEL_ON_REAR_64M = "REAR_0x1";
    public static final String ULTRA_PIXEL_ON_REAR_AI_108M = "REAR_0x4";
    private String mCloseTipString = null;
    private int mCurrentMode;
    private SparseBooleanArray mIsClosed;
    @DrawableRes
    private int mMenuDrawable = -1;
    private String mMenuString = null;
    private String mOpenTipString = null;

    public @interface UltraPixelSupport {
    }

    public ComponentRunningUltraPixel(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    private void add108M(List list) {
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        list.add(new ComponentDataItem((int) R.drawable.ic_ultra_pixel_photography_108mp, (int) R.drawable.ic_ultra_pixel_photography_108mp, resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_108mp)}), "OFF"));
        String string = resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_108mp)});
        String str = ULTRA_PIXEL_ON_REAR_108M;
        list.add(new ComponentDataItem((int) R.drawable.ic_ultra_pixel_photography_108mp, (int) R.drawable.ic_ultra_pixel_photography_108mp, string, str));
        initUltraPixelResource(str);
    }

    private void add48M(List list) {
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        list.add(new ComponentDataItem((int) R.drawable.ic_menu_ultra_pixel_photography_48mp, (int) R.drawable.ic_menu_ultra_pixel_photography_48mp, resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_48mp)}), "OFF"));
        String string = resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_48mp)});
        String str = ULTRA_PIXEL_ON_REAR_48M;
        list.add(new ComponentDataItem((int) R.drawable.ic_menu_ultra_pixel_photography_48mp, (int) R.drawable.ic_menu_ultra_pixel_photography_48mp, string, str));
        initUltraPixelResource(str);
    }

    private void add64M(List list) {
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        list.add(new ComponentDataItem((int) R.drawable.ic_menu_ultra_pixel_photography_64mp, (int) R.drawable.ic_menu_ultra_pixel_photography_64mp, resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_64mp)}), "OFF"));
        String string = resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_64mp)});
        String str = ULTRA_PIXEL_ON_REAR_64M;
        list.add(new ComponentDataItem((int) R.drawable.ic_menu_ultra_pixel_photography_64mp, (int) R.drawable.ic_menu_ultra_pixel_photography_64mp, string, str));
        initUltraPixelResource(str);
    }

    private void addAI108M(List list) {
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        list.add(new ComponentDataItem((int) R.drawable.ic_ultra_pixel_photography_108mp, (int) R.drawable.ic_ultra_pixel_photography_108mp, resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_108mp)}), "OFF"));
        list.add(new ComponentDataItem((int) R.drawable.ic_ultra_pixel_photography_108mp, (int) R.drawable.ic_ultra_pixel_photography_108mp, resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_108mp)}), ULTRA_PIXEL_ON_REAR_108M));
        initUltraPixelResource(ULTRA_PIXEL_ON_REAR_AI_108M);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0037, code lost:
        if (r8 != 3) goto L_0x00f2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List createItems(int i, int i2, CameraCapabilities cameraCapabilities) {
        String str;
        ComponentDataItem componentDataItem;
        this.mCurrentMode = i;
        ArrayList arrayList = new ArrayList();
        if (cameraCapabilities == null) {
            return arrayList;
        }
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        if (i == 163 || i == 165 || i == 167) {
            if (i2 == 0) {
                int OO000oO = C0122O00000o.instance().OO000oO();
                Size OO00OoO = C0122O00000o.instance().OO00OoO();
                if (OO000oO > -1 && cameraCapabilities.isUltraPixelPhotographySupported(OO00OoO)) {
                    String str2 = "OFF";
                    if (OO000oO == 1) {
                        arrayList.add(new ComponentDataItem((int) R.drawable.ic_menu_ultra_pixel_photography_48mp, (int) R.drawable.ic_menu_ultra_pixel_photography_48mp, resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_48mp)}), str2));
                        String string = resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_48mp)});
                        str = ULTRA_PIXEL_ON_REAR_48M;
                        componentDataItem = new ComponentDataItem((int) R.drawable.ic_menu_ultra_pixel_photography_48mp, (int) R.drawable.ic_menu_ultra_pixel_photography_48mp, string, str);
                    } else if (OO000oO == 2) {
                        arrayList.add(new ComponentDataItem((int) R.drawable.ic_menu_ultra_pixel_photography_64mp, (int) R.drawable.ic_menu_ultra_pixel_photography_64mp, resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_64mp)}), str2));
                        String string2 = resources.getString(R.string.pref_menu_ultra_pixel_photography, new Object[]{resources.getString(R.string.ultra_pixel_64mp)});
                        str = ULTRA_PIXEL_ON_REAR_64M;
                        componentDataItem = new ComponentDataItem((int) R.drawable.ic_menu_ultra_pixel_photography_64mp, (int) R.drawable.ic_menu_ultra_pixel_photography_64mp, string2, str);
                    } else if (OO000oO != 3) {
                        String str3 = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unknown rearPixel index: ");
                        sb.append(OO000oO);
                        Log.d(str3, sb.toString());
                    }
                    arrayList.add(componentDataItem);
                    initUltraPixelResource(str);
                }
            }
            return arrayList;
        }
        if (i == 175 && i2 == 0) {
            int OO000oO2 = C0122O00000o.instance().OO000oO();
            if (OO000oO2 > -1) {
                if (OO000oO2 != 1) {
                    if (OO000oO2 == 2) {
                        add64M(arrayList);
                    }
                } else if (CameraSettings.isSRTo108mModeOn()) {
                    addAI108M(arrayList);
                } else {
                    add48M(arrayList);
                }
            }
        }
        return arrayList;
        add108M(arrayList);
        return arrayList;
    }

    public static String getNoSupportZoomTip() {
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        int OO000oO = C0122O00000o.instance().OO000oO();
        if (DataRepository.dataItemRunning().getAi108Running()) {
            OO000oO = 3;
        }
        Object[] objArr = OO000oO != 1 ? OO000oO != 2 ? OO000oO != 3 ? new Object[]{resources.getString(R.string.ultra_pixel_48mp)} : new Object[]{resources.getString(R.string.ultra_pixel_108mp)} : new Object[]{resources.getString(R.string.ultra_pixel_64mp)} : new Object[]{resources.getString(R.string.ultra_pixel_48mp)};
        return resources.getString(R.string.ultra_pixel_zoom_no_support_tip, objArr);
    }

    public static int getUltraPixelIcon() {
        int OO000oO = C0122O00000o.instance().OO000oO();
        if (OO000oO == 0) {
            return R.drawable.ic_mode_32;
        }
        if (OO000oO == 1) {
            return R.drawable.ic_mode_48;
        }
        if (OO000oO == 2) {
            return R.drawable.ic_mode_64;
        }
        if (OO000oO != 3) {
            return 0;
        }
        return R.drawable.ic_mode_108;
    }

    public static String[] getUltraPixelSwitchTipsString() {
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        int OO000oO = C0122O00000o.instance().OO000oO();
        if (OO000oO == 1) {
            return new String[]{resources.getString(R.string.accessibility_ultra_pixel_photography_off, new Object[]{resources.getString(R.string.ultra_pixel_48mp)}), resources.getString(R.string.accessibility_ultra_pixel_photography_on, new Object[]{resources.getString(R.string.ultra_pixel_48mp)})};
        } else if (OO000oO == 2) {
            return new String[]{resources.getString(R.string.accessibility_ultra_pixel_photography_off, new Object[]{resources.getString(R.string.ultra_pixel_64mp)}), resources.getString(R.string.accessibility_ultra_pixel_photography_on, new Object[]{resources.getString(R.string.ultra_pixel_64mp)})};
        } else if (OO000oO != 3) {
            return new String[]{resources.getString(R.string.accessibility_ultra_pixel_photography_off, new Object[]{resources.getString(R.string.ultra_pixel_48mp)}), resources.getString(R.string.accessibility_ultra_pixel_photography_on, new Object[]{resources.getString(R.string.ultra_pixel_48mp)})};
        } else {
            return new String[]{resources.getString(R.string.accessibility_ultra_pixel_photography_off, new Object[]{resources.getString(R.string.ultra_pixel_108mp)}), resources.getString(R.string.accessibility_ultra_pixel_photography_on, new Object[]{resources.getString(R.string.ultra_pixel_108mp)})};
        }
    }

    public static int[] getUltraPixelTopMenuResources() {
        int OO000oO = C0122O00000o.instance().OO000oO();
        return OO000oO != 1 ? OO000oO != 2 ? OO000oO != 3 ? new int[]{R.drawable.ic_menu_ultra_pixel_photography_48mp, R.drawable.ic_menu_ultra_pixel_photography_48mp_shadow} : new int[]{R.drawable.ic_ultra_pixel_photography_108mp, R.drawable.ic_ultra_pixel_photography_108mp_shadow} : new int[]{R.drawable.ic_menu_ultra_pixel_photography_64mp, R.drawable.ic_menu_ultra_pixel_photography_64mp_shadow} : new int[]{R.drawable.ic_menu_ultra_pixel_photography_48mp, R.drawable.ic_menu_ultra_pixel_photography_48mp_shadow};
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0058  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0138  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initUltraPixelResource(@UltraPixelSupport String str) {
        char c;
        String str2;
        Object[] objArr;
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        int hashCode = str.hashCode();
        if (hashCode != -1379357773) {
            switch (hashCode) {
                case -70725170:
                    if (str.equals(ULTRA_PIXEL_ON_REAR_64M)) {
                        c = 2;
                        break;
                    }
                case -70725169:
                    if (str.equals(ULTRA_PIXEL_ON_REAR_48M)) {
                        c = 1;
                        break;
                    }
                case -70725168:
                    if (str.equals(ULTRA_PIXEL_ON_REAR_108M)) {
                        c = 3;
                        break;
                    }
                case -70725167:
                    if (str.equals(ULTRA_PIXEL_ON_REAR_AI_108M)) {
                        c = 4;
                        break;
                    }
            }
        } else if (str.equals(ULTRA_PIXEL_ON_FRONT_32M)) {
            c = 0;
            if (c != 0) {
                this.mMenuDrawable = R.drawable.ic_menu_ultra_pixel_photography_32mp;
                this.mOpenTipString = resources.getString(R.string.ultra_pixel_photography_open_tip, new Object[]{resources.getString(R.string.ultra_pixel_32mp)});
                this.mCloseTipString = resources.getString(R.string.ultra_pixel_photography_close_tip, new Object[]{resources.getString(R.string.ultra_pixel_32mp)});
                objArr = new Object[]{resources.getString(R.string.ultra_pixel_32mp)};
            } else if (c == 1) {
                this.mMenuDrawable = R.drawable.ic_menu_ultra_pixel_photography_48mp;
                this.mOpenTipString = resources.getString(R.string.ultra_pixel_photography_open_tip, new Object[]{resources.getString(R.string.ultra_pixel_48mp)});
                this.mCloseTipString = resources.getString(R.string.ultra_pixel_photography_close_tip, new Object[]{resources.getString(R.string.ultra_pixel_48mp)});
                objArr = new Object[]{resources.getString(R.string.ultra_pixel_48mp)};
            } else if (c == 2) {
                this.mMenuDrawable = R.drawable.ic_menu_ultra_pixel_photography_64mp;
                this.mOpenTipString = resources.getString(R.string.ultra_pixel_photography_open_tip, new Object[]{resources.getString(R.string.ultra_pixel_64mp)});
                this.mCloseTipString = resources.getString(R.string.ultra_pixel_photography_close_tip, new Object[]{resources.getString(R.string.ultra_pixel_64mp)});
                objArr = new Object[]{resources.getString(R.string.ultra_pixel_64mp)};
            } else if (c == 3) {
                this.mMenuDrawable = R.drawable.ic_ultra_pixel_photography_108mp;
                this.mOpenTipString = resources.getString(R.string.ultra_pixel_photography_open_tip, new Object[]{resources.getString(R.string.ultra_pixel_108mp)});
                this.mCloseTipString = resources.getString(R.string.ultra_pixel_photography_close_tip, new Object[]{resources.getString(R.string.ultra_pixel_108mp)});
                objArr = new Object[]{resources.getString(R.string.ultra_pixel_108mp)};
            } else if (c != 4) {
                String str3 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown ultra pixel size: ");
                sb.append(str);
                Log.d(str3, sb.toString());
                return;
            } else {
                this.mMenuDrawable = R.drawable.ic_ultra_pixel_photography_108mp;
                this.mOpenTipString = resources.getString(R.string.ultra_ai_pixel_photography_open_tip, new Object[]{resources.getString(R.string.ai_ultra_pixel_108mp)});
                this.mCloseTipString = resources.getString(R.string.ultra_ai_pixel_photography_close_tip, new Object[]{resources.getString(R.string.ai_ultra_pixel_108mp)});
                str2 = resources.getString(R.string.pref_menu_ai_ultra_pixel_photography, new Object[]{resources.getString(R.string.ai_ultra_pixel_108mp)});
                this.mMenuString = str2;
            }
            str2 = resources.getString(R.string.pref_menu_ultra_pixel_photography, objArr);
            this.mMenuString = str2;
        }
        c = 65535;
        if (c != 0) {
        }
        str2 = resources.getString(R.string.pref_menu_ultra_pixel_photography, objArr);
        this.mMenuString = str2;
    }

    @UltraPixelSupport
    public String getCurrentSupportUltraPixel() {
        return ((ComponentDataItem) this.mItems.get(1)).mValue;
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
        return "pref_ultra_pixel";
    }

    @DrawableRes
    public int getMenuDrawable() {
        return this.mMenuDrawable;
    }

    public String getMenuString() {
        return this.mMenuString;
    }

    public String getUltraPixelCloseTip() {
        return this.mCloseTipString;
    }

    public String getUltraPixelOpenTip() {
        return this.mOpenTipString;
    }

    public boolean isClosed() {
        if (this.mIsClosed == null) {
            return false;
        }
        if (this.mCurrentMode == 165) {
            this.mCurrentMode = 163;
        }
        return this.mIsClosed.get(this.mCurrentMode);
    }

    public boolean isFront32MPSwitchOn() {
        return ULTRA_PIXEL_ON_FRONT_32M.equals(getComponentValue(160));
    }

    public boolean isRear108MPSwitchOn() {
        if (isClosed()) {
            return false;
        }
        return ULTRA_PIXEL_ON_REAR_108M.equals(getComponentValue(160));
    }

    public boolean isRear48MPSwitchOn() {
        if (isClosed()) {
            return false;
        }
        return ULTRA_PIXEL_ON_REAR_48M.equals(getComponentValue(160));
    }

    public boolean isRear64MPSwitchOn() {
        if (isClosed()) {
            return false;
        }
        return ULTRA_PIXEL_ON_REAR_64M.equals(getComponentValue(160));
    }

    public boolean isRearSwitchOn() {
        return isRear48MPSwitchOn() || isRear64MPSwitchOn() || isRear108MPSwitchOn();
    }

    public boolean isSwitchOn() {
        if (isClosed()) {
            return false;
        }
        return !"OFF".equals(getComponentValue(160));
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities) {
        this.mItems = Collections.unmodifiableList(createItems(i, i2, cameraCapabilities));
    }

    public void setClosed(boolean z) {
        if (this.mIsClosed == null) {
            this.mIsClosed = new SparseBooleanArray();
        }
        if (this.mCurrentMode == 165) {
            this.mCurrentMode = 163;
        }
        this.mIsClosed.put(this.mCurrentMode, z);
    }

    public void switchOff() {
        setComponentValue(160, "OFF");
    }

    public void switchOn(@UltraPixelSupport String str) {
        setClosed(false);
        setComponentValue(160, str);
    }

    public void switchOnCurrentSupported(int i, int i2, CameraCapabilities cameraCapabilities) {
        if (isEmpty() || this.mItems.size() < 2) {
            reInit(i, i2, cameraCapabilities);
        }
        if (isEmpty()) {
            Log.e("UltraPixel:", "CameraCapabilities not supported");
            return;
        }
        setClosed(false);
        setComponentValue(160, getCurrentSupportUltraPixel());
    }
}
