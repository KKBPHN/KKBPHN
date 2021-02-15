package com.google.android.play.core.splitcompat;

import com.google.android.play.core.splitinstall.SplitSessionLoader;
import com.google.android.play.core.splitinstall.SplitSessionStatusChanger;
import java.util.List;
import java.util.concurrent.Executor;

final class SplitSessionLoaderImpl implements SplitSessionLoader {
    private final Executor mExecutor;

    SplitSessionLoaderImpl(Executor executor) {
        this.mExecutor = executor;
    }

    public void load(List list, SplitSessionStatusChanger splitSessionStatusChanger) {
        if (SplitCompat.hasInstance()) {
            this.mExecutor.execute(new SplitLoadSessionTask(list, splitSessionStatusChanger));
            return;
        }
        throw new IllegalStateException("Ingestion should only be called in SplitCompat mode.");
    }
}
