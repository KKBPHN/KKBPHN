package com.android.camera.aiwatermark.data;

import java.util.ArrayList;

public class SuperMoonTextWatermark extends AbstractWatermarkData {
    public ArrayList getForAI() {
        return getWatermarkByType(12);
    }

    public ArrayList getForManual() {
        return getWatermarkByType(12);
    }
}
