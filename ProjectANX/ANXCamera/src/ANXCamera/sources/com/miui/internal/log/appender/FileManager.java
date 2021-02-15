package com.miui.internal.log.appender;

import android.os.SystemClock;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import miui.util.IOUtils;

public class FileManager {
    private static final int FILE_CHECK_INTERVAL = 1000;
    private static final String LOG_EXTENSION = ".log";
    private static final int PREPARE_RETRY_INTERVAL = 30000;
    private static final int RETRY_LIMIT = 10;
    private static final String TAG = "FileManager";
    private long mFileCheckStamp;
    protected File mLogFile;
    private long mLogLength;
    protected String mLogName;
    protected String mLogRoot;
    private FileOutputStream mOutputStream;
    private long mPrepareRetryStamp;
    private int mRetryCount;
    private OutputStreamWriter mWriter;

    public FileManager(String str, String str2) {
        this.mLogRoot = str;
        this.mLogName = str2;
    }

    private void doWrite(String str) {
        this.mWriter.write(str);
        this.mWriter.flush();
        this.mLogLength += (long) str.length();
    }

    private void prepareWriter() {
        if (this.mRetryCount >= 10) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (elapsedRealtime - this.mPrepareRetryStamp > 30000) {
                this.mPrepareRetryStamp = elapsedRealtime;
            } else {
                return;
            }
        }
        this.mRetryCount++;
        this.mLogFile = onCreateLogFile();
        File file = this.mLogFile;
        if (file != null) {
            try {
                this.mOutputStream = new FileOutputStream(file, true);
                this.mWriter = new OutputStreamWriter(this.mOutputStream);
                this.mRetryCount = 0;
                this.mLogLength = this.mLogFile.length();
            } catch (FileNotFoundException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Fail to create writer: ");
                sb.append(this.mLogFile.getPath());
                Log.e(TAG, sb.toString(), e);
            }
        }
    }

    private void repairWriter() {
        close();
        prepareWriter();
    }

    public synchronized void close() {
        this.mLogFile = null;
        this.mRetryCount = 0;
        this.mOutputStream = null;
        IOUtils.closeQuietly((Writer) this.mWriter);
        this.mWriter = null;
        this.mLogLength = 0;
    }

    public String getLogExtension() {
        return LOG_EXTENSION;
    }

    public File getLogFile() {
        return this.mLogFile;
    }

    public String getLogName() {
        return this.mLogName;
    }

    public String getLogRoot() {
        return this.mLogRoot;
    }

    public long getLogSize() {
        return this.mLogLength;
    }

    /* access modifiers changed from: protected */
    public String onBuildLogPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mLogRoot);
        sb.append("/");
        sb.append(this.mLogName);
        sb.append(LOG_EXTENSION);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public File onCreateLogFile() {
        String str = "Fail to create LogFile: ";
        String onBuildLogPath = onBuildLogPath();
        String str2 = TAG;
        if (onBuildLogPath == null) {
            Log.e(str2, "Fail to build log path");
            return null;
        }
        File file = new File(onBuildLogPath);
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            if (!parentFile.isDirectory()) {
                StringBuilder sb = new StringBuilder();
                sb.append("LogDir is not a directory: ");
                sb.append(parentFile.getPath());
                Log.e(str2, sb.toString());
                return null;
            }
        } else if (!parentFile.mkdirs() && !parentFile.exists()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Fail to create directory: ");
            sb2.append(parentFile.getPath());
            Log.e(str2, sb2.toString());
            return null;
        }
        if (!file.exists()) {
            try {
                if (!file.createNewFile() && !file.exists()) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(onBuildLogPath);
                    Log.e(str2, sb3.toString());
                    return null;
                }
            } catch (IOException e) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str);
                sb4.append(onBuildLogPath);
                Log.e(str2, sb4.toString(), e);
                return null;
            }
        } else if (!file.isFile()) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("LogFile is not a file: ");
            sb5.append(onBuildLogPath);
            Log.e(str2, sb5.toString());
            return null;
        }
        return file;
    }

    public synchronized void write(String str) {
        String str2;
        String str3;
        if (this.mWriter == null) {
            prepareWriter();
        } else if (this.mWriter != null) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (elapsedRealtime - this.mFileCheckStamp > 1000) {
                this.mFileCheckStamp = elapsedRealtime;
                if (!this.mLogFile.exists()) {
                    Log.d(TAG, "Repair writer for log file is missing");
                    repairWriter();
                }
            }
        }
        if (this.mWriter == null) {
            str2 = TAG;
            str3 = "Fail to append log for writer is null";
        } else {
            try {
                doWrite(str);
            } catch (IOException e) {
                Log.w(TAG, "Retry to write log", e);
                repairWriter();
                if (this.mWriter == null) {
                    str2 = TAG;
                    str3 = "Fail to append log for writer is null when retry";
                } else {
                    try {
                        doWrite(str);
                    } catch (IOException e2) {
                        Log.e(TAG, "Fail to append log for writer is null when retry", e2);
                    }
                }
            }
        }
        Log.e(str2, str3);
        return;
    }
}
