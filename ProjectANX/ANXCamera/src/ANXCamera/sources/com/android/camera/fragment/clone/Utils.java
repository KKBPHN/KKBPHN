package com.android.camera.fragment.clone;

import java.util.Locale;

public class Utils {
    private Utils() {
    }

    public static boolean isChineseLanguage() {
        return "zh".equalsIgnoreCase(Locale.getDefault().getLanguage());
    }

    public static String mergeText(String... strArr) {
        String str;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strArr.length; i++) {
            if (i == 0) {
                str = strArr[i];
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("\n");
                sb2.append(strArr[i]);
                str = sb2.toString();
            }
            sb.append(str);
        }
        return sb.toString();
    }
}
