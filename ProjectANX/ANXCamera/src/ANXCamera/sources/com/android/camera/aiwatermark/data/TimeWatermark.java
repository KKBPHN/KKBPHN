package com.android.camera.aiwatermark.data;

import java.util.ArrayList;

public class TimeWatermark extends AbstractWatermarkData {
    public ArrayList getForAI() {
        return getWatermarkByType(9);
    }

    public ArrayList getForManual() {
        return getWatermarkByType(9);
    }
}
