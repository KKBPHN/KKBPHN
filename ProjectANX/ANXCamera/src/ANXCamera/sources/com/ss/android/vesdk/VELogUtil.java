package com.ss.android.vesdk;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ss.android.ttve.nativePort.TELogcat;

public class VELogUtil {
    private static String APPNAME = "VESDK-";
    private static byte DEBUG_LEVEL = -1;
    private static final byte LOGD = 8;
    private static final byte LOGE = 1;
    private static final byte LOGI = 4;
    private static final byte LOGN = 0;
    private static final byte LOGV = 16;
    private static final byte LOGW = 2;
    public static final byte LOG_LEVEL_D = 15;
    public static final byte LOG_LEVEL_E = 1;
    public static final byte LOG_LEVEL_I = 7;
    public static final byte LOG_LEVEL_N = 0;
    public static final byte LOG_LEVEL_V = 31;
    public static final byte LOG_LEVEL_W = 3;

    public static String __FILE__() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return (stackTrace == null || stackTrace.length < 3) ? "unknown file" : Thread.currentThread().getStackTrace()[2].getFileName();
    }

    public static String __FUNCTION__() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return (stackTrace == null || stackTrace.length < 3) ? "unknown function" : Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public static int __LINE__() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null || stackTrace.length < 3) {
            return -1;
        }
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    public static void d(Class cls, String str) {
        if ((DEBUG_LEVEL & LOGD) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(cls.getSimpleName());
            TELogcat.Log(LOGD, sb.toString(), str);
        }
    }

    public static void d(@NonNull String str, @NonNull String str2) {
        if ((DEBUG_LEVEL & LOGD) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(str);
            TELogcat.Log(LOGD, sb.toString(), str2);
        }
    }

    public static void e(Class cls, String str) {
        if ((DEBUG_LEVEL & 1) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(cls.getSimpleName());
            TELogcat.Log(1, sb.toString(), str);
        }
    }

    public static void e(Class cls, String str, Throwable th) {
        if ((DEBUG_LEVEL & 1) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(cls.getSimpleName());
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(" Throwable msg is : ");
            sb3.append(th.getMessage());
            TELogcat.Log(1, sb2, sb3.toString());
        }
    }

    public static void e(String str, String str2) {
        if ((DEBUG_LEVEL & 1) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(str);
            TELogcat.Log(1, sb.toString(), str2);
        }
    }

    public static byte getLogLevel(byte b) {
        if (b == 1) {
            return 1;
        }
        if (b == 3) {
            return 2;
        }
        if (b == 7) {
            return LOGI;
        }
        if (b == 15) {
            return LOGD;
        }
        if (b != 31) {
            return 0;
        }
        return LOGV;
    }

    public static void i(Class cls, String str) {
        if ((DEBUG_LEVEL & LOGI) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(cls.getSimpleName());
            TELogcat.Log(LOGI, sb.toString(), str);
        }
    }

    public static void i(@NonNull String str, @NonNull String str2) {
        if ((DEBUG_LEVEL & LOGI) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(str);
            TELogcat.Log(LOGI, sb.toString(), str2);
        }
    }

    public static void setUp(@Nullable String str, byte b) {
        if (!TextUtils.isEmpty(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(str);
            sb.append("-");
            APPNAME = sb.toString();
        }
        DEBUG_LEVEL = b;
        TELogcat.setLogLevel(getLogLevel(b));
    }

    public static void v(Class cls, String str) {
        if ((DEBUG_LEVEL & LOGV) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(cls.getSimpleName());
            TELogcat.Log(LOGV, sb.toString(), str);
        }
    }

    public static void v(String str, String str2) {
        if ((DEBUG_LEVEL & LOGV) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(str);
            TELogcat.Log(LOGV, sb.toString(), str2);
        }
    }

    public static void w(Class cls, String str) {
        if ((DEBUG_LEVEL & 2) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(cls.getSimpleName());
            TELogcat.Log(2, sb.toString(), str);
        }
    }

    public static void w(Class cls, String str, Throwable th) {
        if ((DEBUG_LEVEL & 2) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(cls.getSimpleName());
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(" Throwable msg is : ");
            sb3.append(th.getMessage());
            TELogcat.Log(2, sb2, sb3.toString());
        }
    }

    public static void w(String str, String str2) {
        if ((DEBUG_LEVEL & 2) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(str);
            TELogcat.Log(2, sb.toString(), str2);
        }
    }

    public static void w(String str, String str2, Throwable th) {
        if ((DEBUG_LEVEL & 2) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(APPNAME);
            sb.append(str);
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(" Throwable msg is : ");
            sb3.append(th.getMessage());
            TELogcat.Log(2, sb2, sb3.toString());
        }
    }
}
