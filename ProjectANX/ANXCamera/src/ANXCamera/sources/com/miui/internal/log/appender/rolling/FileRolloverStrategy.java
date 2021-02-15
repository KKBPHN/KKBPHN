package com.miui.internal.log.appender.rolling;

import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import java.io.File;

public class FileRolloverStrategy implements RolloverStrategy {
    private static final String TAG = "FileRolloverStrategy";
    private int mMaxBackupIndex = 1;
    private long mMaxFileSize = PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;

    public int getMaxBackupIndex() {
        return this.mMaxBackupIndex;
    }

    public long getMaxFileSize() {
        return this.mMaxFileSize;
    }

    public String rollover(RollingFileManager rollingFileManager) {
        if (rollingFileManager.getLogSize() < this.mMaxFileSize) {
            return null;
        }
        File logFile = rollingFileManager.getLogFile();
        String str = TAG;
        Log.d(str, "Start to rollover");
        StringBuilder sb = new StringBuilder();
        sb.append(logFile.getPath());
        sb.append('.');
        String sb2 = sb.toString();
        for (int i = this.mMaxBackupIndex - 1; i > 0; i--) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append(i);
            File file = new File(sb3.toString());
            if (file.exists()) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(sb2);
                sb4.append(i + 1);
                file.renameTo(new File(sb4.toString()));
            }
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb2);
        sb5.append(1);
        logFile.renameTo(new File(sb5.toString()));
        Log.d(str, "Rollover done");
        return logFile.getPath();
    }

    public void setMaxBackupIndex(int i) {
        if (i >= 1) {
            this.mMaxBackupIndex = i;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index can't be less than 1: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    public void setMaxFileSize(int i) {
        if (i >= 1) {
            this.mMaxFileSize = (long) i;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("size can't be less than 1: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
}
