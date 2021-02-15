package com.android.camera.multi.downloader;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.android.camera.log.Log;
import java.io.File;
import java.io.FilenameFilter;

public class DeleteDownloadedFilesTask implements Runnable {
    private static final String TAG = "Split:DeleteFilesTask";
    private final String dirPath;
    private final boolean isPrefix;
    private final String regEx;

    class DeleteFileFilter implements FilenameFilter {
        private boolean isPrefix;
        private String regEx;

        DeleteFileFilter(boolean z, @NonNull String str) {
            this.isPrefix = z;
            this.regEx = str;
        }

        public boolean accept(File file, String str) {
            boolean z = this.isPrefix;
            String str2 = this.regEx;
            return z ? str.startsWith(str2) : str.endsWith(str2);
        }
    }

    public DeleteDownloadedFilesTask(String str, boolean z, String str2) {
        this.regEx = str2;
        this.dirPath = str;
        this.isPrefix = z;
    }

    private void enumAllFileList() {
        if (!TextUtils.isEmpty(this.dirPath)) {
            File file = new File(this.dirPath);
            if (file.exists() && file.isDirectory() && !TextUtils.isEmpty(this.regEx)) {
                File[] listFiles = file.listFiles(new DeleteFileFilter(this.isPrefix, this.regEx));
                if (listFiles != null && listFiles.length > 0) {
                    int length = listFiles.length;
                    for (int i = 0; i < length; i++) {
                        File file2 = listFiles[i];
                        if (file2.isFile() && file2.exists()) {
                            boolean delete = file2.delete();
                            StringBuilder sb = new StringBuilder();
                            sb.append("delete assigned group download file:");
                            sb.append(this.regEx);
                            sb.append(delete ? "true." : "false!");
                            Log.i(TAG, sb.toString());
                        }
                    }
                }
            }
        }
    }

    public void run() {
        enumAllFileList();
    }
}
