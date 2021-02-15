package com.android.camera.aiwatermark.data;

import java.util.ArrayList;

public class ASDWatermark extends AbstractWatermarkData {
    public ArrayList getForAI() {
        return getWatermarkByType(7);
    }

    public ArrayList getForManual() {
        return getWatermarkByType(3);
    }
}
