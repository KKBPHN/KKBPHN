package miuix.view;

import androidx.collection.SparseArrayCompat;
import miui.view.MiuiHapticFeedbackConstants;

public class HapticFeedbackConstants {
    private static final String ILLEGAL_FEEDBACK = "IllegalFeedback";
    public static final int MIUI_FLICK = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SWITCH;
    public static final int MIUI_FLICK_LIGHT = 268435470;
    static final int MIUI_HAPTIC_END = 268435472;
    static final int MIUI_HAPTIC_START = 268435456;
    public static final int MIUI_HOLD = 268435471;
    public static final int MIUI_LONG_PRESS = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_NORMAL;
    public static final int MIUI_MESH_HEAVY = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_NORMAL;
    public static final int MIUI_MESH_LIGHT = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_LONG_PRESS;
    public static final int MIUI_MESH_NORMAL = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_LIGHT;
    public static final int MIUI_PICK_UP = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SCROLL_EDGE;
    public static final int MIUI_POPUP_LIGHT = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_PICK_UP;
    public static final int MIUI_POPUP_NORMAL = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_LIGHT;
    public static final int MIUI_SCROLL_EDGE = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TRIGGER_DRAWER;
    public static final int MIUI_SWITCH = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_HEAVY;
    public static final int MIUI_TAP_LIGHT = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_FLICK;
    public static final int MIUI_TAP_NORMAL = MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TAP_LIGHT;
    public static final int MIUI_TRIGGER_DRAWER = 268435469;
    public static final int MIUI_VIRTUAL_RELEASE = 268435456;
    private static final SparseArrayCompat NAMES = new SparseArrayCompat();

    static {
        buildNames();
    }

    private static void buildNames() {
        NAMES.append(MIUI_VIRTUAL_RELEASE, "MIUI_VIRTUAL_RELEASE");
        NAMES.append(MIUI_TAP_NORMAL, "MIUI_TAP_NORMAL");
        NAMES.append(MIUI_TAP_LIGHT, "MIUI_TAP_LIGHT");
        NAMES.append(MIUI_FLICK, "MIUI_FLICK");
        NAMES.append(MIUI_SWITCH, "MIUI_SWITCH");
        NAMES.append(MIUI_MESH_HEAVY, "MIUI_MESH_HEAVY");
        NAMES.append(MIUI_MESH_NORMAL, "MIUI_MESH_NORMAL");
        NAMES.append(MIUI_MESH_LIGHT, "MIUI_MESH_LIGHT");
        NAMES.append(MIUI_LONG_PRESS, "MIUI_LONG_PRESS");
        NAMES.append(MIUI_POPUP_NORMAL, "MIUI_POPUP_NORMAL");
        NAMES.append(MIUI_POPUP_LIGHT, "MIUI_POPUP_LIGHT");
        NAMES.append(MIUI_PICK_UP, "MIUI_PICK_UP");
        NAMES.append(MIUI_SCROLL_EDGE, "MIUI_SCROLL_EDGE");
        NAMES.append(MIUI_TRIGGER_DRAWER, "MIUI_TRIGGER_DRAWER");
        NAMES.append(MIUI_FLICK_LIGHT, "MIUI_FLICK_LIGHT");
        NAMES.append(MIUI_HOLD, "MIUI_HOLD");
    }

    public static String nameOf(int i) {
        return (String) NAMES.get(i, ILLEGAL_FEEDBACK);
    }
}
