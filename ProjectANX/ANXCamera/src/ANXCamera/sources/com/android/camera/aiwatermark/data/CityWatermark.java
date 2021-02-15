package com.android.camera.aiwatermark.data;

import java.util.ArrayList;

public class CityWatermark extends AbstractWatermarkData {
    public ArrayList getForAI() {
        return getWatermarkByType(8);
    }

    public ArrayList getForManual() {
        return getWatermarkByType(4);
    }
}
