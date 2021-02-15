package com.android.camera;

public interface CameraBrightnessCallback {
    void adjustBrightnessInAutoMode(float f);

    int getPreviousBrightnessMode();

    void setBrightness(int i);

    void setPreviousBrightnessMode(int i);
}
