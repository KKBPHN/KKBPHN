package com.iqiyi.android.qigsaw.core.splitinstall;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import java.util.concurrent.ThreadFactory;

final class SplitBackgroundThread implements ThreadFactory {
    SplitBackgroundThread() {
    }

    @SuppressLint({"NewThreadDirectly"})
    public Thread newThread(@NonNull Runnable runnable) {
        return new Thread(runnable, "SplitBackgroundThread");
    }
}
