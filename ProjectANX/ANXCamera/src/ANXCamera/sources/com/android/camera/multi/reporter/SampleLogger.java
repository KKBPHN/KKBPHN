package com.android.camera.multi.reporter;

import com.android.camera.log.Log;
import com.iqiyi.android.qigsaw.core.common.SplitLog.Logger;

public class SampleLogger implements Logger {
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
}
