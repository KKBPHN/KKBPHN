package com.miui.internal.log.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.miui.internal.log.util.Config;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import miui.os.FileUtils;

public class DumpReceiver extends BroadcastReceiver {
    public static final String INTENT_ACTION_DUMP_CACHED_LOG = "com.miui.core.intent.ACTION_DUMP_CACHED_LOG";
    private static final String TAG = "DumpReceiver";
    private final String mCacheLogDirPath;
    private final String mSdcardLogDirPath;

    public DumpReceiver() {
        this(Config.getDefaultCacheLogDir(), Config.getDefaultSdcardLogDir());
    }

    public DumpReceiver(String str, String str2) {
        this.mCacheLogDirPath = str;
        this.mSdcardLogDirPath = str2;
    }

    private static void dumpCachedLog(String str, String str2) {
        String str3;
        File file = new File(str);
        boolean exists = file.exists();
        String str4 = TAG;
        if (exists) {
            String[] list = file.list();
            if (list == null || list.length <= 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("Skip dumpCachedLog, logNames=");
                sb.append(Arrays.toString(list));
                str3 = sb.toString();
            } else {
                File file2 = new File(str2);
                if (file2.exists() || file2.mkdirs() || file2.exists()) {
                    file2.listFiles(new FileFilter() {
                        public boolean accept(File file) {
                            file.delete();
                            return false;
                        }
                    });
                    for (String str5 : list) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(File.separatorChar);
                        sb2.append(str5);
                        File file3 = new File(sb2.toString());
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str2);
                        sb3.append(File.separatorChar);
                        sb3.append(str5);
                        File file4 = new File(sb3.toString());
                        FileUtils.copyFile(file3, file4);
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Dump log from ");
                        sb4.append(file3.getPath());
                        sb4.append(" to ");
                        sb4.append(file4.getPath());
                        Log.d(str4, sb4.toString());
                    }
                }
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Fail to make dump dir: ");
                sb5.append(file2.getPath());
                Log.e(str4, sb5.toString());
                return;
            }
        } else {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Skip dumpCachedLog, cacheLogDir is not exist: ");
            sb6.append(str);
            str3 = sb6.toString();
        }
        Log.i(str4, str3);
    }

    public void onReceive(Context context, Intent intent) {
        String str = TAG;
        Log.d(str, "dumpCachedLog start");
        dumpCachedLog(this.mCacheLogDirPath, this.mSdcardLogDirPath);
        Log.d(str, "dumpCachedLog end");
    }
}
