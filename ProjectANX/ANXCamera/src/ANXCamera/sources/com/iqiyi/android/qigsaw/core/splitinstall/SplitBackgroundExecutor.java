package com.iqiyi.android.qigsaw.core.splitinstall;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

final class SplitBackgroundExecutor {
    private static final Executor sExecutor = Executors.newSingleThreadScheduledExecutor(new SplitBackgroundThread());

    SplitBackgroundExecutor() {
    }

    static Executor getExecutor() {
        return sExecutor;
    }
}
