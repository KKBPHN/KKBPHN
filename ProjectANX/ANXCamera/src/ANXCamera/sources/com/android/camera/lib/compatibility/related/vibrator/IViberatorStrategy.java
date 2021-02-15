package com.android.camera.lib.compatibility.related.vibrator;

public interface IViberatorStrategy {
    void performBokehAdjust();

    void performBurstCapture();

    void performEVChange();

    void performFocusValueLargeChangedInManual();

    void performFocusValueLightChangedInManual();

    void performModeSwitch();

    void performSelectZoomLight();

    void performSelectZoomNormal();

    void performSlideScaleNormal();

    void performSnapClick();

    void performSwitchCamera();
}
