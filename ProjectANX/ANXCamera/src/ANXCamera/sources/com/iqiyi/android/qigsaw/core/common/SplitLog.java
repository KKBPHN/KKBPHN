package com.iqiyi.android.qigsaw.core.common;

import android.util.Log;

public class SplitLog {
    private static final String TAG = "Split.SplitLog";
    private static Logger defaultLogger = new Logger() {
        public void d(String str, String str2, Throwable th) {
            Log.d(str, str2, th);
        }

        public void d(String str, String str2, Object... objArr) {
            if (!(objArr == null || objArr.length == 0)) {
                str2 = String.format(str2, objArr);
            }
            Log.d(str, str2);
        }

        public void e(String str, String str2, Throwable th) {
            Log.e(str, str2, th);
        }

        public void e(String str, String str2, Object... objArr) {
            if (!(objArr == null || objArr.length == 0)) {
                str2 = String.format(str2, objArr);
            }
            Log.e(str, str2);
        }

        public void i(String str, String str2, Throwable th) {
            Log.i(str, str2, th);
        }

        public void i(String str, String str2, Object... objArr) {
            if (!(objArr == null || objArr.length == 0)) {
                str2 = String.format(str2, objArr);
            }
            Log.i(str, str2);
        }

        public void printErrStackTrace(String str, Throwable th, String str2, Object... objArr) {
            if (!(objArr == null || objArr.length == 0)) {
                str2 = String.format(str2, objArr);
            }
            if (str2 == null) {
                str2 = "";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append("  ");
            sb.append(Log.getStackTraceString(th));
            Log.e(str, sb.toString());
        }

        public void v(String str, String str2, Throwable th) {
            Log.v(str, str2, th);
        }

        public void v(String str, String str2, Object... objArr) {
            if (!(objArr == null || objArr.length == 0)) {
                str2 = String.format(str2, objArr);
            }
            Log.v(str, str2);
        }

        public void w(String str, String str2, Throwable th) {
            Log.w(str, str2, th);
        }

        public void w(String str, String str2, Object... objArr) {
            if (!(objArr == null || objArr.length == 0)) {
                str2 = String.format(str2, objArr);
            }
            Log.w(str, str2);
        }
    };
    private static Logger splitLogImp = defaultLogger;

    public interface Logger {
        void d(String str, String str2, Throwable th);

        void d(String str, String str2, Object... objArr);

        void e(String str, String str2, Throwable th);

        void e(String str, String str2, Object... objArr);

        void i(String str, String str2, Throwable th);

        void i(String str, String str2, Object... objArr);

        void printErrStackTrace(String str, Throwable th, String str2, Object... objArr);

        void v(String str, String str2, Throwable th);

        void v(String str, String str2, Object... objArr);

        void w(String str, String str2, Throwable th);

        void w(String str, String str2, Object... objArr);
    }

    private SplitLog() {
    }

    public static void d(String str, String str2, Throwable th) {
        Logger logger = splitLogImp;
        if (logger != null) {
            logger.d(str, str2, th);
        }
    }

    public static void d(String str, String str2, Object... objArr) {
        Logger logger = splitLogImp;
        if (logger != null) {
            logger.d(str, str2, objArr);
        }
    }

    public static void e(String str, String str2, Throwable th) {
        Logger logger = splitLogImp;
        if (logger != null) {
            logger.e(str, str2, th);
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        Logger logger = splitLogImp;
        if (logger != null) {
            logger.e(str, str2, objArr);
        }
    }

    public static Logger getImpl() {
        return splitLogImp;
    }

    public static void i(String str, String str2, Throwable th) {
        Logger logger = splitLogImp;
        if (logger != null) {
            logger.i(str, str2, th);
        }
    }

    public static void i(String str, String str2, Object... objArr) {
        Logger logger = splitLogImp;
        if (logger != null) {
            logger.i(str, str2, objArr);
        }
    }

    public static void printErrStackTrace(String str, Throwable th, String str2, Object... objArr) {
        Logger logger = splitLogImp;
        if (logger != null) {
            logger.printErrStackTrace(str, th, str2, objArr);
        }
    }

    public static void setSplitLogImp(Logger logger) {
        splitLogImp = logger;
    }

    public static void v(String str, String str2, Throwable th) {
        Logger logger = splitLogImp;
        if (logger != null) {
            logger.v(str, str2, th);
        }
    }

    public static void v(String str, String str2, Object... objArr) {
        Logger logger = splitLogImp;
        if (logger != null) {
            logger.v(str, str2, objArr);
        }
    }

    public static void w(String str, String str2, Throwable th) {
        Logger logger = splitLogImp;
        if (logger != null) {
            logger.w(str, str2, th);
        }
    }

    public static void w(String str, String str2, Object... objArr) {
        Logger logger = splitLogImp;
        if (logger != null) {
            logger.w(str, str2, objArr);
        }
    }
}
