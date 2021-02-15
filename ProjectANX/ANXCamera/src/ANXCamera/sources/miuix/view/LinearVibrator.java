package miuix.view;

import android.util.Log;
import android.view.View;
import androidx.annotation.Keep;
import androidx.collection.SparseArrayCompat;
import miui.util.HapticFeedbackUtil;
import miui.view.MiuiHapticFeedbackConstants;

@Keep
class LinearVibrator implements HapticFeedbackProvider {
    private static final String TAG = "LinearVibrator";
    private final SparseArrayCompat mIds = new SparseArrayCompat();

    static {
        initialize();
    }

    private LinearVibrator() {
        buildIds();
    }

    private void buildIds() {
        this.mIds.append(HapticFeedbackConstants.MIUI_TAP_NORMAL, Integer.valueOf(268435456));
        this.mIds.append(HapticFeedbackConstants.MIUI_TAP_LIGHT, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TAP_LIGHT));
        this.mIds.append(HapticFeedbackConstants.MIUI_FLICK, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_FLICK));
        this.mIds.append(HapticFeedbackConstants.MIUI_SWITCH, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SWITCH));
        this.mIds.append(HapticFeedbackConstants.MIUI_MESH_HEAVY, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_HEAVY));
        this.mIds.append(HapticFeedbackConstants.MIUI_MESH_NORMAL, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_NORMAL));
        this.mIds.append(HapticFeedbackConstants.MIUI_MESH_LIGHT, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_LIGHT));
        this.mIds.append(HapticFeedbackConstants.MIUI_LONG_PRESS, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_LONG_PRESS));
        this.mIds.append(HapticFeedbackConstants.MIUI_POPUP_NORMAL, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_NORMAL));
        this.mIds.append(HapticFeedbackConstants.MIUI_POPUP_LIGHT, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_LIGHT));
        if (PlatformConstants.VERSION >= 2) {
            this.mIds.append(HapticFeedbackConstants.MIUI_PICK_UP, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_PICK_UP));
            this.mIds.append(HapticFeedbackConstants.MIUI_SCROLL_EDGE, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SCROLL_EDGE));
            this.mIds.append(HapticFeedbackConstants.MIUI_TRIGGER_DRAWER, Integer.valueOf(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TRIGGER_DRAWER));
            if (PlatformConstants.VERSION >= 3) {
                this.mIds.append(HapticFeedbackConstants.MIUI_FLICK_LIGHT, Integer.valueOf(268435469));
                if (PlatformConstants.VERSION >= 4) {
                    this.mIds.append(HapticFeedbackConstants.MIUI_HOLD, Integer.valueOf(268435470));
                }
            }
        }
    }

    private static void initialize() {
        boolean z;
        int i = PlatformConstants.VERSION;
        String str = TAG;
        if (i < 1) {
            Log.w(str, "MiuiHapticFeedbackConstants not found or not compatible for LinearVibrator.");
            return;
        }
        try {
            z = HapticFeedbackUtil.isSupportLinearMotorVibrate();
        } catch (Throwable th) {
            Log.w(str, "MIUI Haptic Implementation is not available", th);
            z = false;
        }
        if (!z) {
            Log.w(str, "linear motor is not supported in this platform.");
            return;
        }
        HapticCompat.registerProvider(new LinearVibrator());
        Log.i(str, "setup LinearVibrator success.");
    }

    /* access modifiers changed from: 0000 */
    public int obtainFeedBack(int i) {
        int indexOfKey = this.mIds.indexOfKey(i);
        if (indexOfKey >= 0) {
            return ((Integer) this.mIds.valueAt(indexOfKey)).intValue();
        }
        return -1;
    }

    public boolean performHapticFeedback(View view, int i) {
        String format;
        int indexOfKey = this.mIds.indexOfKey(i);
        String str = TAG;
        if (indexOfKey < 0) {
            format = String.format("feedback(0x%08x-%s) is not found in current platform(v%d)", new Object[]{Integer.valueOf(i), HapticFeedbackConstants.nameOf(i), Integer.valueOf(PlatformConstants.VERSION)});
        } else {
            int intValue = ((Integer) this.mIds.valueAt(indexOfKey)).intValue();
            if (HapticFeedbackUtil.isSupportLinearMotorVibrate(intValue)) {
                return view.performHapticFeedback(intValue);
            }
            format = String.format("unsupported feedback: 0x%08x. platform version: %d", new Object[]{Integer.valueOf(intValue), Integer.valueOf(PlatformConstants.VERSION)});
        }
        Log.w(str, format);
        return false;
    }

    public boolean supportLinearMotor(int i) {
        String format;
        int indexOfKey = this.mIds.indexOfKey(i);
        String str = TAG;
        if (indexOfKey < 0) {
            format = String.format("feedback(0x%08x-%s) is not found in current platform(v%d)", new Object[]{Integer.valueOf(i), HapticFeedbackConstants.nameOf(i), Integer.valueOf(PlatformConstants.VERSION)});
        } else {
            int intValue = ((Integer) this.mIds.valueAt(indexOfKey)).intValue();
            if (HapticFeedbackUtil.isSupportLinearMotorVibrate(intValue)) {
                return HapticFeedbackUtil.isSupportLinearMotorVibrate(intValue);
            }
            format = String.format("unsupported feedback: 0x%08x. platform version: %d", new Object[]{Integer.valueOf(intValue), Integer.valueOf(PlatformConstants.VERSION)});
        }
        Log.w(str, format);
        return false;
    }
}
