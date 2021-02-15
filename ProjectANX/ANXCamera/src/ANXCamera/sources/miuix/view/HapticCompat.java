package miuix.view;

import android.util.Log;
import android.view.View;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class HapticCompat {
    static final String TAG = "HapticCompat";
    private static List sProviders = new ArrayList();

    static {
        loadProviders("miuix.view.LinearVibrator", "miuix.view.ExtendedVibrator");
    }

    private static void loadProviders(String... strArr) {
        for (String str : strArr) {
            StringBuilder sb = new StringBuilder();
            sb.append("loading provider: ");
            sb.append(str);
            String sb2 = sb.toString();
            String str2 = TAG;
            Log.i(str2, sb2);
            try {
                Class.forName(str, true, HapticCompat.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                Log.w(str2, String.format("load provider %s failed.", new Object[]{str}), e);
            }
        }
    }

    public static int obtainFeedBack(int i) {
        for (HapticFeedbackProvider hapticFeedbackProvider : sProviders) {
            if (hapticFeedbackProvider instanceof LinearVibrator) {
                return ((LinearVibrator) hapticFeedbackProvider).obtainFeedBack(i);
            }
        }
        return -1;
    }

    @Keep
    public static boolean performHapticFeedback(@NonNull View view, int i) {
        String str = TAG;
        if (i < 268435456) {
            Log.i(str, String.format("perform haptic: 0x%08x", new Object[]{Integer.valueOf(i)}));
            return view.performHapticFeedback(i);
        } else if (i > HapticFeedbackConstants.MIUI_HAPTIC_END) {
            Log.w(str, String.format("illegal feedback constant, should be in range [0x%08x..0x%08x]", new Object[]{Integer.valueOf(268435456), Integer.valueOf(HapticFeedbackConstants.MIUI_HAPTIC_END)}));
            return false;
        } else {
            for (HapticFeedbackProvider performHapticFeedback : sProviders) {
                if (performHapticFeedback.performHapticFeedback(view, i)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Keep
    static void registerProvider(HapticFeedbackProvider hapticFeedbackProvider) {
        sProviders.add(hapticFeedbackProvider);
    }

    public static boolean supportLinearMotor(int i) {
        String str = TAG;
        if (i < 268435456) {
            Log.i(str, String.format("perform haptic: 0x%08x", new Object[]{Integer.valueOf(i)}));
            return false;
        } else if (i > HapticFeedbackConstants.MIUI_HAPTIC_END) {
            Log.w(str, String.format("illegal feedback constant, should be in range [0x%08x..0x%08x]", new Object[]{Integer.valueOf(268435456), Integer.valueOf(HapticFeedbackConstants.MIUI_HAPTIC_END)}));
            return false;
        } else {
            for (HapticFeedbackProvider hapticFeedbackProvider : sProviders) {
                if ((hapticFeedbackProvider instanceof LinearVibrator) && ((LinearVibrator) hapticFeedbackProvider).supportLinearMotor(i)) {
                    return true;
                }
            }
            return false;
        }
    }
}
