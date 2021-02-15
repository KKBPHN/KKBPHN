package com.google.android.play.core.splitcompat.util;

import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.Locale;

@RestrictTo({Scope.LIBRARY_GROUP})
public class PlayCore {
    private String mTag;

    public PlayCore(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("UID: [");
        sb.append(Process.myUid());
        sb.append("]  PID: [");
        sb.append(Process.myPid());
        sb.append("] ");
        String sb2 = sb.toString();
        String valueOf = String.valueOf(str);
        if (valueOf.length() != 0) {
            sb2 = sb2.concat(valueOf);
        }
        this.mTag = sb2;
    }

    private int log(int i, String str, Object[] objArr) {
        String str2 = "PlayCore";
        if (Log.isLoggable(str2, i)) {
            return Log.i(str2, logInternal(this.mTag, str, objArr));
        }
        return 0;
    }

    private static String logInternal(String str, String str2, Object... objArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" : ");
        sb.append(str2);
        String sb2 = sb.toString();
        if (objArr != null && objArr.length > 0) {
            try {
                sb2 = String.format(Locale.US, sb2, objArr);
                return sb2;
            } catch (Throwable th) {
                String str3 = "Unable to format ";
                if (sb2.length() != 0) {
                    str3 = str3.concat(sb2);
                }
                Log.e("PlayCore", str3, th);
                String join = TextUtils.join(", ", objArr);
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append(" [");
                sb3.append(join);
                sb3.append("]");
                sb2 = sb3.toString();
            }
        }
        return sb2;
    }

    public int debug(String str, Object... objArr) {
        return log(3, str, objArr);
    }

    public int error(String str, Object... objArr) {
        return log(6, str, objArr);
    }

    public int error(Throwable th, String str, Object... objArr) {
        String str2 = "PlayCore";
        if (Log.isLoggable(str2, 6)) {
            return Log.e(str2, logInternal(this.mTag, str, objArr), th);
        }
        return 0;
    }

    public int info(String str, Object... objArr) {
        return log(4, str, objArr);
    }

    public int warn(String str, Object... objArr) {
        return log(5, str, objArr);
    }
}
