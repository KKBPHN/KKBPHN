package com.iqiyi.android.qigsaw.core.common;

import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

class FileLockHelper implements Closeable {
    private static final int LOCK_WAIT_EACH_TIME = 10;
    private static final String TAG = "Split.FileLockHelper";
    private final FileLock fileLock;
    private final FileOutputStream outputStream;

    private FileLockHelper(File file) {
        String str = TAG;
        this.outputStream = new FileOutputStream(file);
        FileLock fileLock2 = null;
        Throwable e = null;
        int i = 0;
        while (i < 3) {
            i++;
            try {
                fileLock2 = this.outputStream.getChannel().lock();
                if (fileLock2 != null) {
                    break;
                }
            } catch (Exception e2) {
                e = e2;
                Log.e(str, "getInfoLock Thread failed time:10");
            }
            try {
                Thread.sleep(10);
            } catch (Exception e3) {
                Log.e(str, "getInfoLock Thread sleep exception", e3);
            }
        }
        if (fileLock2 != null) {
            this.fileLock = fileLock2;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Tinker Exception:FileLockHelper lock file failed: ");
        sb.append(file.getAbsolutePath());
        throw new IOException(sb.toString(), e);
    }

    static FileLockHelper getFileLock(File file) {
        return new FileLockHelper(file);
    }

    public void close() {
        try {
            if (this.fileLock != null) {
                this.fileLock.release();
            }
        } finally {
            FileOutputStream fileOutputStream = this.outputStream;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }
}
