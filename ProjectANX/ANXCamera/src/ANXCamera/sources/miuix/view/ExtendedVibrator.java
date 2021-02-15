package miuix.view;

import android.util.Log;
import android.view.View;
import androidx.annotation.Keep;

@Keep
class ExtendedVibrator implements HapticFeedbackProvider {
    private static final String TAG = "ExtendedVibrator";

    static {
        initialize();
    }

    private ExtendedVibrator() {
    }

    private static void initialize() {
        int i = PlatformConstants.VERSION;
        String str = TAG;
        if (i < 0) {
            Log.w(str, "MiuiHapticFeedbackConstants not found.");
            return;
        }
        HapticCompat.registerProvider(new ExtendedVibrator());
        Log.i(str, "setup ExtendedVibrator success.");
    }

    public boolean performHapticFeedback(View view, int i) {
        if (i == HapticFeedbackConstants.MIUI_VIRTUAL_RELEASE) {
            return view.performHapticFeedback(2);
        }
        return false;
    }
}
