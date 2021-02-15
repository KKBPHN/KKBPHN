package com.android.camera.data.data.config;

import java.util.List;

public class TopViewPositionArray {
    private static final int[][] VIEW_POSITION_ARRAY = {new int[]{10}, new int[]{0, 10}, new int[]{0, 5, 10}, new int[]{0, 3, 7, 10}, new int[]{0, 2, 5, 8, 10}, new int[]{0, 1, 4, 6, 9, 10}};

    public static SupportedConfigs fillNotUseViewPosition(List list) {
        if (list == null || list.isEmpty()) {
            return new SupportedConfigs();
        }
        SupportedConfigs supportedConfigs = new SupportedConfigs(11);
        TopConfigItem topConfigItem = new TopConfigItem(176);
        int i = 0;
        for (int i2 = 0; i2 < 11; i2++) {
            supportedConfigs.add(topConfigItem);
        }
        boolean z = list.size() <= 2;
        int size = list.size();
        int[] iArr = VIEW_POSITION_ARRAY[size - 1];
        while (i < size) {
            TopConfigItem topConfigItem2 = (TopConfigItem) list.get(i);
            topConfigItem2.index = i;
            int i3 = (!z || topConfigItem2.gravity != 17) ? iArr[i] : 5;
            topConfigItem2.bindViewPosition = i3;
            supportedConfigs.set(topConfigItem2.bindViewPosition, topConfigItem2);
            i++;
        }
        return supportedConfigs;
    }
}
