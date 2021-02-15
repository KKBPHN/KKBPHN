package com.miui.internal.log.appender;

import android.util.Log;
import com.miui.internal.log.Level;
import com.miui.internal.log.format.Formatter;
import com.miui.internal.log.message.Message;

public class FileAppender implements Appender {
    private static final String TAG = "FileAppender";
    private FileManager mFileManager;
    private Formatter mFormatter;

    private void doAppend(String str, String str2, long j, Level level, String str3, Throwable th, Message message) {
        String str4;
        Formatter formatter = this.mFormatter;
        String str5 = TAG;
        if (formatter == null) {
            str4 = "Fail to append log for formatter is null";
        } else {
            FileManager fileManager = this.mFileManager;
            if (fileManager == null) {
                str4 = "Fail to append log for FileManager is null";
            } else {
                Formatter formatter2 = formatter;
                String str6 = str;
                String str7 = str2;
                long j2 = j;
                Level level2 = level;
                fileManager.write(str3 == null ? formatter2.format(str6, str7, j2, level2, message) : formatter2.format(str6, str7, j2, level2, str3, th));
                return;
            }
        }
        Log.e(str5, str4);
    }

    public void append(String str, String str2, long j, Level level, Message message) {
        doAppend(str, str2, j, level, null, null, message);
    }

    public void append(String str, String str2, long j, Level level, String str3, Throwable th) {
        doAppend(str, str2, j, level, str3, th, null);
    }

    public void close() {
        FileManager fileManager = this.mFileManager;
        if (fileManager != null) {
            fileManager.close();
            this.mFileManager = null;
        }
    }

    public FileManager getFileManager() {
        return this.mFileManager;
    }

    public Formatter getFormatter() {
        return this.mFormatter;
    }

    public void setFileManager(FileManager fileManager) {
        if (this.mFileManager != fileManager) {
            close();
            this.mFileManager = fileManager;
        }
    }

    public void setFormatter(Formatter formatter) {
        this.mFormatter = formatter;
    }
}
