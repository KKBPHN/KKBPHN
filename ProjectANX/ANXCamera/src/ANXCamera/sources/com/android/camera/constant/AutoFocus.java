package com.android.camera.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class AutoFocus {
    public static final String LEGACY_AUTO = "auto";
    public static final String LEGACY_CONTINUOUS_PICTURE = "continuous-picture";
    public static final String LEGACY_CONTINUOUS_VIDEO = "continuous-video";
    public static final String LEGACY_EDOF = "edof";
    public static final String LEGACY_MACRO = "macro";
    public static final String LEGACY_MANUAL = "manual";

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusMode {
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int convertToFocusMode(String str) {
        char c;
        switch (str.hashCode()) {
            case -1081415738:
                if (str.equals("manual")) {
                    c = 5;
                    break;
                }
            case -194628547:
                if (str.equals(LEGACY_CONTINUOUS_VIDEO)) {
                    c = 2;
                    break;
                }
            case 3005871:
                if (str.equals("auto")) {
                    c = 0;
                    break;
                }
            case 3108534:
                if (str.equals(LEGACY_EDOF)) {
                    c = 4;
                    break;
                }
            case 103652300:
                if (str.equals("macro")) {
                    c = 1;
                    break;
                }
            case 910005312:
                if (str.equals(LEGACY_CONTINUOUS_PICTURE)) {
                    c = 3;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            return 1;
        }
        if (c == 1) {
            return 2;
        }
        if (c == 2) {
            return 3;
        }
        if (c != 3) {
            return c != 4 ? 0 : 5;
        }
        return 4;
    }

    public static List convertToLegacyFocusModes(int[] iArr) {
        String str;
        ArrayList arrayList = new ArrayList();
        for (int i : iArr) {
            if (i == 0) {
                str = "manual";
            } else if (i == 1) {
                str = "auto";
            } else if (i == 2) {
                str = "macro";
            } else if (i == 3) {
                str = LEGACY_CONTINUOUS_VIDEO;
            } else if (i == 4) {
                str = LEGACY_CONTINUOUS_PICTURE;
            } else if (i != 5) {
            } else {
                str = LEGACY_EDOF;
            }
            arrayList.add(str);
        }
        return arrayList;
    }
}
