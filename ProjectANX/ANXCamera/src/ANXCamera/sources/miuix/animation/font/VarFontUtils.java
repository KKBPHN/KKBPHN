package miuix.animation.font;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.util.Log;
import android.widget.TextView;
import java.io.File;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.DeviceUtils;

@TargetApi(26)
public class VarFontUtils {
    private static final int DEFAULT_WGHT = 50;
    private static final String FORMAT_WGHT = "'wght' ";
    private static final boolean IS_USING_VAR_FONT;
    private static final String KEY_FONT_WEIGHT = "key_miui_font_weight_scale";
    public static final int MIN_WGHT;
    private static final int[] MITYPE_WGHT;
    private static final int[] MIUI_WGHT;
    private static final int[][][] RULES;
    private static final String THEME_FONT_DIR = "/data/system/theme/fonts/";

    static {
        boolean z = !isUsingThemeFont() && VERSION.SDK_INT >= 26 && DeviceUtils.getTotalRam() > 6 && !isFontAnimDisabled() && DeviceUtils.getDeviceLevel() > 0;
        IS_USING_VAR_FONT = z;
        if (IS_USING_VAR_FONT) {
            MIUI_WGHT = new int[]{150, 200, 250, af.ct, m.ah, 400, 480, 540, 630, 700};
            MITYPE_WGHT = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
            MIN_WGHT = 10;
            RULES = new int[3][][];
            int[][][] iArr = RULES;
            iArr[0] = new int[][]{new int[]{0, 5}, new int[]{0, 5}, new int[]{1, 6}, new int[]{2, 6}, new int[]{2, 7}, new int[]{3, 8}, new int[]{4, 8}, new int[]{5, 9}, new int[]{6, 9}, new int[]{7, 9}};
            iArr[1] = new int[][]{new int[]{0, 2}, new int[]{0, 3}, new int[]{1, 4}, new int[]{1, 5}, new int[]{2, 6}, new int[]{2, 7}, new int[]{3, 8}, new int[]{4, 9}, new int[]{5, 9}, new int[]{6, 9}};
            iArr[2] = new int[][]{new int[]{0, 5}, new int[]{1, 5}, new int[]{2, 5}, new int[]{3, 6}, new int[]{3, 6}, new int[]{4, 7}, new int[]{5, 8}, new int[]{6, 8}, new int[]{7, 8}, new int[]{8, 9}};
            return;
        }
        MIN_WGHT = 0;
        int[] iArr2 = new int[0];
        MITYPE_WGHT = iArr2;
        MIUI_WGHT = iArr2;
        RULES = new int[0][][];
    }

    private VarFontUtils() {
    }

    static int getScaleWght(int i, float f, int i2, int i3) {
        float f2;
        if (!IS_USING_VAR_FONT) {
            return i;
        }
        int[] wghtRange = getWghtRange(i, f);
        int wghtByType = getWghtByType(wghtRange[0], i2);
        int wghtByType2 = getWghtByType(i, i2);
        int wghtByType3 = getWghtByType(wghtRange[1], i2);
        if (i3 < 50) {
            float f3 = ((float) i3) / 50.0f;
            f2 = ((1.0f - f3) * ((float) wghtByType)) + (f3 * ((float) wghtByType2));
        } else {
            if (i3 > 50) {
                float f4 = ((float) (i3 - 50)) / 50.0f;
                f2 = ((1.0f - f4) * ((float) wghtByType2)) + (f4 * ((float) wghtByType3));
            }
            return wghtByType2;
        }
        wghtByType2 = (int) f2;
        return wghtByType2;
    }

    static int getSysFontScale(Context context) {
        return System.getInt(context.getContentResolver(), KEY_FONT_WEIGHT, 50);
    }

    private static int[] getWghtArray(int i) {
        return (i == 1 || i == 2) ? MITYPE_WGHT : MIUI_WGHT;
    }

    private static int getWghtByType(int i, int i2) {
        return getWghtArray(i2)[i];
    }

    private static int[] getWghtRange(int i, float f) {
        char c = f > 20.0f ? 1 : (f <= 0.0f || f >= 12.0f) ? (char) 0 : 2;
        return RULES[c][i];
    }

    private static boolean isFontAnimDisabled() {
        try {
            return CommonUtils.readProp("ro.miui.ui.font.animation").equals("disable");
        } catch (Exception e) {
            Log.w(CommonUtils.TAG, "isFontAnimationEnabled failed", e);
            return false;
        }
    }

    private static boolean isUsingThemeFont() {
        File file = new File(THEME_FONT_DIR);
        boolean z = false;
        try {
            if (file.exists()) {
                String[] list = file.list();
                if (list != null && list.length > 0) {
                    z = true;
                }
                return z;
            }
        } catch (Exception e) {
            Log.w(CommonUtils.TAG, "isUsingThemeFont, failed to check theme font directory", e);
        }
        return false;
    }

    static void setVariationFont(TextView textView, int i) {
        if (IS_USING_VAR_FONT) {
            StringBuilder sb = new StringBuilder();
            sb.append(FORMAT_WGHT);
            sb.append(i);
            textView.setFontVariationSettings(sb.toString());
        }
    }
}
