package miuix.animation.utils;

import android.util.Log;

public class LogUtils {
    private static final String COMMA = ", ";
    private static final boolean IS_LOG_ENABLED;

    static {
        String str = CommonUtils.TAG;
        String str2 = "";
        try {
            String readProp = CommonUtils.readProp("log.tag.folme.level");
            if (readProp != null) {
                str2 = readProp;
            }
        } catch (Exception e) {
            Log.i(str, "can not access property log.tag.folme.level, no log", e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("logLevel = ");
        sb.append(str2);
        Log.d(str, sb.toString());
        IS_LOG_ENABLED = str2.equals("D");
    }

    private LogUtils() {
    }

    public static void debug(String str, Object... objArr) {
        if (IS_LOG_ENABLED) {
            int length = objArr.length;
            String str2 = CommonUtils.TAG;
            if (length > 0) {
                String str3 = COMMA;
                StringBuilder sb = new StringBuilder(str3);
                int length2 = sb.length();
                for (Object obj : objArr) {
                    if (sb.length() > length2) {
                        sb.append(str3);
                    }
                    sb.append(obj);
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(sb.toString());
                str = sb2.toString();
            }
            Log.i(str2, str);
        }
    }

    public static boolean isLogEnabled() {
        return IS_LOG_ENABLED;
    }
}
