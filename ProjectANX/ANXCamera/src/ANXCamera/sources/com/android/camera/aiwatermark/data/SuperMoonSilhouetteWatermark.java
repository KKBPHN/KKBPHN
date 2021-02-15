package com.android.camera.aiwatermark.data;

import java.util.ArrayList;

public class SuperMoonSilhouetteWatermark extends AbstractWatermarkData {
    public ArrayList getForAI() {
        return getWatermarkByType(11);
    }

    public ArrayList getForManual() {
        return getWatermarkByType(11);
    }
}
