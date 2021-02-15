package miuix.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.RequiresPermission;
import miui.util.HapticFeedbackUtil;
import miuix.core.util.SystemProperties;
import miuix.view.HapticCompat;
import miuix.view.PlatformConstants;

public class HapticFeedbackCompat {
    private static final String PHYSICAL_EMULATION_REASON = "USAGE_PHYSICAL_EMULATION";
    private static final int RTP_MIN_VALUE = 0;
    private static final int RTP_V1_MAX_VALUE = 160;
    private static final String TAG = "HapticFeedbackCompat";
    private static boolean mAvailable;
    private static boolean mCanCheckExtHaptic;
    private static boolean mIsSupportHapticWithReason;
    private HapticFeedbackUtil hapticFeedbackUtil;

    public HapticFeedbackCompat(Context context) {
        this(context, false);
    }

    public HapticFeedbackCompat(Context context, boolean z) {
        String str;
        int i = PlatformConstants.VERSION;
        String str2 = TAG;
        if (i < 1) {
            str = "MiuiHapticFeedbackConstants not found or not compatible for LinearVibrator.";
        } else {
            try {
                mAvailable = HapticFeedbackUtil.isSupportLinearMotorVibrate();
            } catch (Throwable th) {
                Log.w(str2, "MIUI Haptic Implementation is not available", th);
                mAvailable = false;
            }
            if (!mAvailable) {
                str = "linear motor is not supported in this platform.";
            } else {
                this.hapticFeedbackUtil = new HapticFeedbackUtil(context, z);
                try {
                    HapticFeedbackUtil.class.getMethod("performHapticFeedback", new Class[]{Integer.TYPE, Double.TYPE, String.class});
                    mIsSupportHapticWithReason = true;
                } catch (Throwable th2) {
                    Log.w(str2, "Not support haptic with reason", th2);
                    mIsSupportHapticWithReason = false;
                }
                try {
                    HapticFeedbackUtil.class.getMethod("isSupportExtHapticFeedback", new Class[]{Integer.TYPE});
                    mCanCheckExtHaptic = true;
                } catch (Throwable unused) {
                    mCanCheckExtHaptic = false;
                }
                return;
            }
        }
        Log.w(str2, str);
    }

    public boolean isSupportExtHapticFeedback(int i) {
        HapticFeedbackUtil hapticFeedbackUtil2 = this.hapticFeedbackUtil;
        boolean z = false;
        if (hapticFeedbackUtil2 != null) {
            if (mCanCheckExtHaptic) {
                return hapticFeedbackUtil2.isSupportExtHapticFeedback(i);
            }
            if (i >= 0 && i <= 160) {
                z = true;
            }
        }
        return z;
    }

    @RequiresPermission("android.permission.VIBRATE")
    public boolean performEmulationHaptic(int i, double d) {
        return performHapticFeedback(i, d, PHYSICAL_EMULATION_REASON);
    }

    @RequiresPermission("android.permission.VIBRATE")
    public boolean performExtHapticFeedback(int i) {
        HapticFeedbackUtil hapticFeedbackUtil2 = this.hapticFeedbackUtil;
        if (hapticFeedbackUtil2 != null) {
            return hapticFeedbackUtil2.performExtHapticFeedback(i);
        }
        return false;
    }

    @RequiresPermission("android.permission.VIBRATE")
    public boolean performExtHapticFeedback(Uri uri) {
        HapticFeedbackUtil hapticFeedbackUtil2 = this.hapticFeedbackUtil;
        if (hapticFeedbackUtil2 != null) {
            return hapticFeedbackUtil2.performExtHapticFeedback(uri);
        }
        return false;
    }

    @RequiresPermission("android.permission.VIBRATE")
    public boolean performHapticFeedback(int i) {
        return performHapticFeedback(i, false);
    }

    @RequiresPermission("android.permission.VIBRATE")
    public boolean performHapticFeedback(int i, double d, String str) {
        if (this.hapticFeedbackUtil != null && mIsSupportHapticWithReason) {
            int obtainFeedBack = HapticCompat.obtainFeedBack(i);
            if (obtainFeedBack != -1) {
                return this.hapticFeedbackUtil.performHapticFeedback(obtainFeedBack, d, str);
            }
        }
        return false;
    }

    @RequiresPermission("android.permission.VIBRATE")
    public boolean performHapticFeedback(int i, int i2) {
        if (this.hapticFeedbackUtil != null) {
            int obtainFeedBack = HapticCompat.obtainFeedBack(i);
            if (obtainFeedBack != -1) {
                return this.hapticFeedbackUtil.performHapticFeedback(obtainFeedBack, false, i2);
            }
        }
        return false;
    }

    @RequiresPermission("android.permission.VIBRATE")
    public boolean performHapticFeedback(int i, int i2, boolean z) {
        if (this.hapticFeedbackUtil != null) {
            int obtainFeedBack = HapticCompat.obtainFeedBack(i);
            if (obtainFeedBack != -1) {
                return this.hapticFeedbackUtil.performHapticFeedback(obtainFeedBack, z, i2);
            }
        }
        return false;
    }

    @RequiresPermission("android.permission.VIBRATE")
    public boolean performHapticFeedback(int i, boolean z) {
        if (this.hapticFeedbackUtil != null) {
            int obtainFeedBack = HapticCompat.obtainFeedBack(i);
            if (obtainFeedBack != -1) {
                return this.hapticFeedbackUtil.performHapticFeedback(obtainFeedBack, z);
            }
        }
        return false;
    }

    public void release() {
        HapticFeedbackUtil hapticFeedbackUtil2 = this.hapticFeedbackUtil;
        if (hapticFeedbackUtil2 != null) {
            hapticFeedbackUtil2.release();
        }
    }

    public boolean supportKeyboardIntensity() {
        return SystemProperties.getBoolean("sys.haptic.intensityforkeyboard", false);
    }

    public boolean supportLinearMotor() {
        return mAvailable;
    }

    public boolean supportLinearMotorWithReason() {
        return mIsSupportHapticWithReason;
    }
}
