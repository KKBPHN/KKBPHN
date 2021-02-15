package com.iqiyi.android.qigsaw.core.common;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public class ProcessUtil {
    private static final String TAG = "Split:ProcessUtil";

    public static String getProcessName(Context context) {
        String str;
        try {
            str = getProcessNameClassical(context);
        } catch (Exception unused) {
            str = null;
        }
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String processNameSecure = getProcessNameSecure();
        SplitLog.i(TAG, "Get process name: %s in secure mode.", processNameSecure);
        return processNameSecure;
    }

    private static String getProcessNameClassical(Context context) {
        int myPid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        String str = "";
        if (activityManager == null) {
            return str;
        }
        List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return str;
        }
        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.pid == myPid) {
                str = runningAppProcessInfo.processName;
            }
        }
        return str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0039, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0042, code lost:
        throw r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static String getProcessNameSecure() {
        String str = "";
        StringBuilder sb = new StringBuilder();
        sb.append("/proc/");
        sb.append(Process.myPid());
        sb.append("/cmdline");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(sb.toString())));
            str = bufferedReader.readLine().trim();
            bufferedReader.close();
        } catch (Exception unused) {
        } catch (Throwable th) {
            r1.addSuppressed(th);
        }
        return str;
    }

    public static void killAllOtherProcess(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager != null) {
            List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
            if (runningAppProcesses != null) {
                for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                    if (runningAppProcessInfo.uid == Process.myUid() && runningAppProcessInfo.pid != Process.myPid()) {
                        Process.killProcess(runningAppProcessInfo.pid);
                    }
                }
            }
        }
    }
}
