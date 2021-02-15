package com.android.camera;

import android.util.SparseIntArray;

public class MemoryHelper {
    private static SparseIntArray sCapturedNumberArray = new SparseIntArray(4);
    private static int sTrimLevel;

    public static void addCapturedNumber(int i, int i2) {
        sCapturedNumberArray.put(i, sCapturedNumberArray.get(i) + i2);
    }

    public static void clear() {
        sCapturedNumberArray.clear();
    }

    public static void resetCapturedNumber(int i) {
        sCapturedNumberArray.put(i, 0);
    }

    public static void setTrimLevel(int i) {
        sTrimLevel = i;
    }

    public static boolean shouldTrimMemory(int i) {
        boolean z;
        int i2 = sTrimLevel;
        boolean z2 = true;
        if (i2 == 5) {
            if (sCapturedNumberArray.get(i) < 30) {
                z = false;
            }
            return z;
        } else if (i2 == 10) {
            if (sCapturedNumberArray.get(i) < 20) {
                z2 = false;
            }
            return z2;
        } else if (i2 != 15) {
            return false;
        } else {
            if (sCapturedNumberArray.get(i) < 10) {
                z2 = false;
            }
            return z2;
        }
    }
}
