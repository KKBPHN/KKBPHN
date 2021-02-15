package miui.util;

import android.text.TextUtils;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miui.os.SystemProperties;
import miui.view.MiuiHapticFeedbackConstants;

public class HapticFeedbackUtil {
    public static final String EFFECT_KEY_FLICK = "flick";
    public static final String EFFECT_KEY_LONG_PRESS = "long_press";
    public static final String EFFECT_KEY_MESH_HEAVY = "mesh_heavy";
    public static final String EFFECT_KEY_MESH_LIGHT = "mesh_light";
    public static final String EFFECT_KEY_MESH_NORMAL = "mesh_normal";
    public static final String EFFECT_KEY_POPUP_LIGHT = "popup_light";
    public static final String EFFECT_KEY_POPUP_NORMAL = "popup_normal";
    public static final String EFFECT_KEY_SWITCH = "switch";
    public static final String EFFECT_KEY_TAP_LIGHT = "tap_light";
    public static final String EFFECT_KEY_TAP_NORMAL = "tap_normal";
    public static final String EFFECT_KEY_VIRTUAL_KEY_DOWN = "virtual_key_down";
    public static final String EFFECT_KEY_VIRTUAL_KEY_LONGPRESS = "virtual_key_longpress";
    public static final String EFFECT_KEY_VIRTUAL_KEY_TAP = "virtual_key_tap";
    public static final String EFFECT_KEY_VIRTUAL_KEY_UP = "virtual_key_up";
    private static final SparseArray ID_TO_KEY = new SparseArray();
    private static final HashMap PROPERTY_KEY = new HashMap();
    private static final List PROPERTY_MOTOR_KEY = new ArrayList();
    private static final int VIRTUAL_RELEASED = 2;

    static {
        SparseArray sparseArray = ID_TO_KEY;
        String str = EFFECT_KEY_VIRTUAL_KEY_DOWN;
        sparseArray.put(1, str);
        SparseArray sparseArray2 = ID_TO_KEY;
        String str2 = EFFECT_KEY_VIRTUAL_KEY_LONGPRESS;
        sparseArray2.put(0, str2);
        SparseArray sparseArray3 = ID_TO_KEY;
        String str3 = EFFECT_KEY_VIRTUAL_KEY_TAP;
        sparseArray3.put(3, str3);
        SparseArray sparseArray4 = ID_TO_KEY;
        String str4 = EFFECT_KEY_VIRTUAL_KEY_UP;
        sparseArray4.put(2, str4);
        SparseArray sparseArray5 = ID_TO_KEY;
        String str5 = EFFECT_KEY_TAP_NORMAL;
        sparseArray5.put(268435456, str5);
        SparseArray sparseArray6 = ID_TO_KEY;
        String str6 = EFFECT_KEY_TAP_LIGHT;
        sparseArray6.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TAP_LIGHT, str6);
        SparseArray sparseArray7 = ID_TO_KEY;
        String str7 = EFFECT_KEY_FLICK;
        sparseArray7.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_FLICK, str7);
        SparseArray sparseArray8 = ID_TO_KEY;
        String str8 = EFFECT_KEY_SWITCH;
        sparseArray8.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SWITCH, str8);
        SparseArray sparseArray9 = ID_TO_KEY;
        String str9 = EFFECT_KEY_MESH_HEAVY;
        sparseArray9.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_HEAVY, str9);
        SparseArray sparseArray10 = ID_TO_KEY;
        String str10 = EFFECT_KEY_MESH_NORMAL;
        sparseArray10.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_NORMAL, str10);
        SparseArray sparseArray11 = ID_TO_KEY;
        String str11 = EFFECT_KEY_MESH_LIGHT;
        sparseArray11.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_LIGHT, str11);
        SparseArray sparseArray12 = ID_TO_KEY;
        String str12 = EFFECT_KEY_LONG_PRESS;
        sparseArray12.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_LONG_PRESS, str12);
        SparseArray sparseArray13 = ID_TO_KEY;
        String str13 = EFFECT_KEY_POPUP_NORMAL;
        sparseArray13.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_NORMAL, str13);
        SparseArray sparseArray14 = ID_TO_KEY;
        String str14 = EFFECT_KEY_POPUP_LIGHT;
        sparseArray14.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_LIGHT, str14);
        PROPERTY_KEY.put(str, "sys.haptic.down");
        PROPERTY_KEY.put(str2, "sys.haptic.long");
        PROPERTY_KEY.put(str3, "sys.haptic.tap");
        PROPERTY_KEY.put(str4, "sys.haptic.up");
        PROPERTY_KEY.put(str5, "sys.haptic.tap.normal");
        PROPERTY_KEY.put(str6, "sys.haptic.tap.light");
        PROPERTY_KEY.put(str7, "sys.haptic.flick");
        PROPERTY_KEY.put(str8, "sys.haptic.switch");
        PROPERTY_KEY.put(str9, "sys.haptic.mesh.heavy");
        PROPERTY_KEY.put(str10, "sys.haptic.mesh.normal");
        PROPERTY_KEY.put(str11, "sys.haptic.mesh.light");
        PROPERTY_KEY.put(str12, "sys.haptic.long.press");
        PROPERTY_KEY.put(str13, "sys.haptic.popup.normal");
        PROPERTY_KEY.put(str14, "sys.haptic.popup.light");
        PROPERTY_MOTOR_KEY.add(str5);
        PROPERTY_MOTOR_KEY.add(str6);
        PROPERTY_MOTOR_KEY.add(str7);
        PROPERTY_MOTOR_KEY.add(str8);
        PROPERTY_MOTOR_KEY.add(str9);
        PROPERTY_MOTOR_KEY.add(str10);
        PROPERTY_MOTOR_KEY.add(str11);
        PROPERTY_MOTOR_KEY.add(str12);
        PROPERTY_MOTOR_KEY.add(str13);
        PROPERTY_MOTOR_KEY.add(str14);
    }

    public static boolean isSupportLinearMotorVibrate() {
        return "linear".equals(SystemProperties.get("sys.haptic.motor"));
    }

    public static boolean isSupportLinearMotorVibrate(int i) {
        if (isSupportLinearMotorVibrate()) {
            String str = (String) ID_TO_KEY.get(i);
            if (PROPERTY_MOTOR_KEY.contains(str) && !TextUtils.isEmpty(SystemProperties.get((String) PROPERTY_KEY.get(str)))) {
                return true;
            }
        }
        return false;
    }
}
