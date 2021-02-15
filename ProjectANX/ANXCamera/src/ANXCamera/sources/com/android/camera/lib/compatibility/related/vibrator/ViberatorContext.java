package com.android.camera.lib.compatibility.related.vibrator;

import android.content.Context;
import android.util.Log;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;

public class ViberatorContext implements IViberatorStrategy {
    private static final String TAG = "ViberatorContext";
    private static volatile ViberatorContext mInstance;
    private boolean mIsSnapClickViberatorEnable = true;
    IViberatorStrategy mViberator;

    private ViberatorContext(Context context) {
        if (CompatibilityUtils.isSupportLinearMotorVibrate()) {
            this.mViberator = new LinearMotorStrategy(context.getApplicationContext());
            Log.d(TAG, "ViberatorContext: init LinearMotorStrategy");
        }
    }

    public static ViberatorContext getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ViberatorContext.class) {
                if (mInstance == null) {
                    mInstance = new ViberatorContext(context);
                }
            }
        }
        return mInstance;
    }

    private boolean isViberatorReady() {
        return this.mViberator != null;
    }

    public void performBokehAdjust() {
        if (isViberatorReady()) {
            this.mViberator.performBokehAdjust();
        }
    }

    public void performBurstCapture() {
        if (isViberatorReady()) {
            this.mViberator.performBurstCapture();
        }
    }

    public void performEVChange() {
        if (isViberatorReady()) {
            this.mViberator.performEVChange();
        }
    }

    public void performFocusValueLargeChangedInManual() {
        if (isViberatorReady()) {
            this.mViberator.performFocusValueLargeChangedInManual();
        }
    }

    public void performFocusValueLightChangedInManual() {
        if (isViberatorReady()) {
            this.mViberator.performFocusValueLightChangedInManual();
        }
    }

    public void performModeSwitch() {
        Log.d(TAG, "performModeSwitch: ");
        if (isViberatorReady()) {
            this.mViberator.performModeSwitch();
        }
    }

    public void performSelectZoomLight() {
        if (isViberatorReady()) {
            this.mViberator.performSelectZoomLight();
        }
    }

    public void performSelectZoomNormal() {
        if (isViberatorReady()) {
            this.mViberator.performSelectZoomNormal();
        }
    }

    public void performSlideScaleNormal() {
        if (isViberatorReady()) {
            this.mViberator.performSlideScaleNormal();
        }
    }

    public void performSnapClick() {
        if (this.mIsSnapClickViberatorEnable && isViberatorReady()) {
            this.mViberator.performSnapClick();
        }
    }

    public void performSwitchCamera() {
        if (isViberatorReady()) {
            this.mViberator.performSwitchCamera();
        }
    }

    public void setSnapClickVibratorEnable(boolean z) {
        this.mIsSnapClickViberatorEnable = z;
    }
}
