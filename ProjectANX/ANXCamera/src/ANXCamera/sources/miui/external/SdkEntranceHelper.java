package miui.external;

import android.util.Log;

class SdkEntranceHelper implements SdkConstants {
    private static final String SDK_ENTRANCE_CLASS = "miui.core.SdkManager";
    private static final String SDK_ENTRANCE_FALLBACK_CLASS = "com.miui.internal.core.SdkManager";

    SdkEntranceHelper() {
    }

    public static Class getSdkEntrance() {
        String str;
        try {
            str = "miuisdk";
            Class cls = Class.forName(SDK_ENTRANCE_CLASS);
            str = cls;
            return cls;
        } catch (ClassNotFoundException unused) {
            try {
                Class cls2 = Class.forName(SDK_ENTRANCE_FALLBACK_CLASS);
                Log.w(str, "using legacy sdk");
                return cls2;
            } catch (ClassNotFoundException e) {
                Log.e(str, "no sdk found");
                throw e;
            }
        }
    }
}
