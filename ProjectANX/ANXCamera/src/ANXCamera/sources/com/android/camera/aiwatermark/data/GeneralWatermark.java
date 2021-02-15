package com.android.camera.aiwatermark.data;

import java.util.ArrayList;

public class GeneralWatermark extends AbstractWatermarkData {
    public ArrayList getForAI() {
        return getWatermarkByType(10);
    }

    public ArrayList getForManual() {
        return getWatermarkByType(0);
    }
}
