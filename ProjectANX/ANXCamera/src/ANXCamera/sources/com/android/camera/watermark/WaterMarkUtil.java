package com.android.camera.watermark;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.res.Resources;
import android.os.Build;
import androidx.annotation.StringRes;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager;
import com.android.camera.Util;
import com.android.camera.data.data.runing.ComponentRunningEisPro;
import com.android.camera.log.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class WaterMarkUtil {
    public static final String DIR_WATER_MARK = "watermarks";
    private static final String FILE_NAME_PREFIX = "ic_water_mark_";
    private static final String GEN2_FILE_NAME_PREFIX = "gen2_";
    private static final int ICON_CC = 1;
    private static final int ICON_DUAL = 2;
    private static final int ICON_NONE = 0;
    private static final int ICON_PENTA = 5;
    private static final int ICON_QUAD = 4;
    private static final int ICON_TRIPLE = 3;
    private static final String TAG = "WaterMarkUtil";
    private static final String WATER_MARK_BACK_NAME_COMMON = "back_common";
    private static final String WATER_MARK_BACK_NAME_PREFIX = "back_";
    private static final String WATER_MARK_FRONT_NAME_COMMON = "front_common";
    private static final String WATER_MARK_FRONT_NAME_PREFIX = "front_";
    private static List sFrontWaterMarkList;
    private static List sWaterMarkList;

    @Retention(RetentionPolicy.SOURCE)
    @interface ICON_TYPE {
    }

    private WaterMarkUtil() {
    }

    public static String getBackIconName() {
        int backIconType = getBackIconType();
        String str = backIconType != 1 ? backIconType != 2 ? backIconType != 3 ? backIconType != 4 ? backIconType != 5 ? "" : "penta_back" : "quad" : "triple" : "dual" : "cc_back";
        StringBuilder sb = new StringBuilder();
        sb.append(FILE_NAME_PREFIX);
        sb.append(str);
        sb.append(".webp");
        return sb.toString();
    }

    public static String getBackIconNameGen2() {
        int backIconType = getBackIconType();
        String str = backIconType != 3 ? backIconType != 4 ? backIconType != 5 ? "dual" : "penta" : "quad" : "triple";
        StringBuilder sb = new StringBuilder();
        sb.append(GEN2_FILE_NAME_PREFIX);
        sb.append(str);
        sb.append(".webp");
        return sb.toString();
    }

    private static int getBackIconType() {
        return Integer.parseInt((String) getWaterCameraBack().get(0));
    }

    public static String getDescription() {
        String str = "";
        if (C0122O00000o.instance().OO0oo()) {
            return str;
        }
        if (getWaterCameraBack().size() > 2) {
            str = (String) getWaterCameraBack().get(2);
        }
        return str;
    }

    private static String getExtraSuffix() {
        if (C0124O00000oO.O0o0Ooo) {
            if (C0124O00000oO.OOooOOO()) {
                return "48m";
            }
            if (C0124O00000oO.OOooOOo()) {
                return "speed";
            }
        }
        return C0124O00000oO.OOooO00() ? "lite" : C0124O00000oO.OOooO0O() ? "s" : (!C0124O00000oO.OOooo0o() || !C0124O00000oO.Oo000O()) ? Build.MODEL.replace(" ", "_").toLowerCase(Locale.ENGLISH) : ComponentRunningEisPro.EIS_VALUE_PRO;
    }

    public static String getFrontIconName() {
        int frontIconType = getFrontIconType();
        String str = frontIconType != 1 ? frontIconType != 5 ? "" : "penta_front" : "cc_front";
        StringBuilder sb = new StringBuilder();
        sb.append(FILE_NAME_PREFIX);
        sb.append(str);
        sb.append(".webp");
        return sb.toString();
    }

    private static int getFrontIconType() {
        return Integer.parseInt((String) getWaterCameraFront().get(0));
    }

    public static String getFrontTitle() {
        return getWaterCameraFront().size() > 1 ? (String) getWaterCameraFront().get(1) : "";
    }

    private static int getStringId(Resources resources, String str) {
        return resources.getIdentifier(str, "string", CameraIntentManager.CALLER_MIUI_CAMERA);
    }

    public static String getTitle() {
        return getWaterCameraBack().size() > 1 ? (String) getWaterCameraBack().get(1) : "";
    }

    private static List getWaterCameraBack() {
        if (sWaterMarkList == null) {
            Resources resources = CameraAppImpl.getAndroidContext().getResources();
            sWaterMarkList = Arrays.asList(resources.getString(getWaterMarkId(resources, WATER_MARK_BACK_NAME_PREFIX, WATER_MARK_BACK_NAME_COMMON)).split(":"));
            if (!C0122O00000o.instance().OO0oo() && sWaterMarkList.size() < 3) {
                throw new InvalidParameterException("Back camera water mark need four parameters!");
            }
        }
        return sWaterMarkList;
    }

    private static List getWaterCameraFront() {
        if (sFrontWaterMarkList == null) {
            Resources resources = CameraAppImpl.getAndroidContext().getResources();
            sFrontWaterMarkList = Arrays.asList(resources.getString(getWaterMarkId(resources, WATER_MARK_FRONT_NAME_PREFIX, WATER_MARK_FRONT_NAME_COMMON)).split(":"));
            if (sFrontWaterMarkList.size() < 2) {
                throw new InvalidParameterException("Front camera water mark need two parameters!");
            }
        }
        return sFrontWaterMarkList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x008e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @StringRes
    private static int getWaterMarkId(Resources resources, String str, String str2) {
        String str3;
        int stringId;
        StringBuilder sb;
        String str4;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(C0122O00000o.instance().getClassName());
        String sb3 = sb2.toString();
        if (C0122O00000o.instance().OO0OOoo()) {
            sb = new StringBuilder();
            sb.append(sb3);
            str4 = "_india";
        } else if (Util.isGlobalVersion()) {
            sb = new StringBuilder();
            sb.append(sb3);
            str4 = "_global";
        } else {
            str3 = sb3;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str3);
            sb4.append("_");
            sb4.append(getExtraSuffix());
            String sb5 = sb4.toString();
            StringBuilder sb6 = new StringBuilder();
            sb6.append("getWaterMarkId: all = ");
            sb6.append(sb5);
            String sb7 = sb6.toString();
            String str5 = TAG;
            Log.d(str5, sb7);
            stringId = getStringId(resources, sb5);
            if (stringId <= 0) {
                StringBuilder sb8 = new StringBuilder();
                sb8.append("getWaterMarkId: find first = ");
                sb8.append(sb5);
                Log.d(str5, sb8.toString());
                return stringId;
            }
            int stringId2 = getStringId(resources, str3);
            if (stringId2 > 0) {
                StringBuilder sb9 = new StringBuilder();
                sb9.append("getWaterMarkId: find second = ");
                sb9.append(str3);
                Log.d(str5, sb9.toString());
                return stringId2;
            }
            int stringId3 = getStringId(resources, sb3);
            if (stringId3 <= 0) {
                return getStringId(resources, str2);
            }
            StringBuilder sb10 = new StringBuilder();
            sb10.append("getWaterMarkId: find third = ");
            sb10.append(sb3);
            Log.d(str5, sb10.toString());
            return stringId3;
        }
        sb.append(str4);
        str3 = sb.toString();
        StringBuilder sb42 = new StringBuilder();
        sb42.append(str3);
        sb42.append("_");
        sb42.append(getExtraSuffix());
        String sb52 = sb42.toString();
        StringBuilder sb62 = new StringBuilder();
        sb62.append("getWaterMarkId: all = ");
        sb62.append(sb52);
        String sb72 = sb62.toString();
        String str52 = TAG;
        Log.d(str52, sb72);
        stringId = getStringId(resources, sb52);
        if (stringId <= 0) {
        }
    }
}
