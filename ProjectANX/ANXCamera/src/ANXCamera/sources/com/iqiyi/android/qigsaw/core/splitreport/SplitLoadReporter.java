package com.iqiyi.android.qigsaw.core.splitreport;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import java.util.List;

public interface SplitLoadReporter {
    @MainThread
    void onLoadFailed(String str, @NonNull List list, @NonNull List list2, long j);

    @MainThread
    void onLoadOK(String str, @NonNull List list, long j);
}
