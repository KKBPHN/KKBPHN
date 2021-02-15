package com.iqiyi.android.qigsaw.core.splitinstall.remote;

import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

final class SplitDeleteRedundantVersionTask implements Runnable {
    private static final int MAX_SPLIT_CACHE_SIZE = 1;
    private static final String TAG = "SplitDeleteRedundantVersionTask";
    private final Collection allSplits;

    SplitDeleteRedundantVersionTask(Collection collection) {
        this.allSplits = collection;
    }

    private void deleteRedundantSplitVersionDirs(final File file, File file2, final File file3) {
        final String name = file2.getName();
        File[] listFiles = file2.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (!file.isDirectory() || file.equals(file)) {
                    return false;
                }
                SplitLog.i(SplitDeleteRedundantVersionTask.TAG, "Split %s version %s has been installed!", name, file.getName());
                return file3.exists();
            }
        });
        if (listFiles != null && listFiles.length > 1) {
            Arrays.sort(listFiles, new Comparator() {
                public int compare(File file, File file2) {
                    if (file.lastModified() < file2.lastModified()) {
                        return 1;
                    }
                    return file.lastModified() == file2.lastModified() ? 0 : -1;
                }
            });
            for (int i = 1; i < listFiles.length; i++) {
                SplitLog.i(TAG, "Split %s version %s is redundant, so we try to delete it", name, listFiles[i].getName());
                FileUtil.deleteDir(listFiles[i]);
            }
        }
    }

    public void run() {
        Collection<SplitInfo> collection = this.allSplits;
        if (collection != null) {
            for (SplitInfo splitInfo : collection) {
                deleteRedundantSplitVersionDirs(SplitPathManager.require().getSplitDir(splitInfo), SplitPathManager.require().getSplitRootDir(splitInfo), SplitPathManager.require().getSplitMarkFile(splitInfo));
            }
        }
    }
}
