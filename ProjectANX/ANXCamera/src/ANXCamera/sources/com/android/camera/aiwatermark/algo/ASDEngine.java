package com.android.camera.aiwatermark.algo;

import com.android.camera.aiwatermark.util.WatermarkConstant;

public class ASDEngine {
    private ASDEngine() {
    }

    public static final String spots2ASDKey(int i) {
        String str = WatermarkConstant.ASD_NEGATIVE;
        if (i == 0) {
            return str;
        }
        if (i == 30) {
            return WatermarkConstant.MOTORCYCLE;
        }
        if (i == 33) {
            return WatermarkConstant.UNDERWATER;
        }
        if (i == 2) {
            return WatermarkConstant.FLOWER;
        }
        if (i == 3) {
            return WatermarkConstant.DELICACY;
        }
        if (i == 21) {
            return WatermarkConstant.CLOUDY;
        }
        if (i == 22) {
            return WatermarkConstant.OVERCAST;
        }
        if (i == 27) {
            return WatermarkConstant.BUDDHA;
        }
        if (i == 28) {
            return WatermarkConstant.COW;
        }
        switch (i) {
            case 5:
                return WatermarkConstant.BLUESKY;
            case 6:
                return WatermarkConstant.SUNRISE_SUNSET;
            case 7:
                return "cat";
            case 8:
                return WatermarkConstant.DOG;
            case 9:
                return WatermarkConstant.PLANTS;
            case 10:
                return WatermarkConstant.NIGHTSCAPE;
            default:
                switch (i) {
                    case 16:
                        return WatermarkConstant.GRASSPLOT;
                    case 17:
                        return WatermarkConstant.LEAFS;
                    case 18:
                        return WatermarkConstant.FLESHINESS;
                    default:
                        switch (i) {
                            case 44:
                                return WatermarkConstant.MONKEY;
                            case 45:
                                return WatermarkConstant.COCONUT_TREE;
                            case 46:
                                return WatermarkConstant.TUTUK;
                            default:
                                return str;
                        }
                }
        }
    }
}
