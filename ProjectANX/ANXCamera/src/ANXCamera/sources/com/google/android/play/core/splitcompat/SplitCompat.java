package com.google.android.play.core.splitcompat;

import android.content.Context;
import com.google.android.play.core.splitinstall.LoadedSplitFetcherSingleton;
import com.google.android.play.core.splitinstall.SplitSessionLoaderSingleton;
import com.google.android.play.core.tasks.TaskExecutors;
import com.iqiyi.android.qigsaw.core.splitload.SplitLoadManagerService;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class SplitCompat {
    private static final AtomicReference sSplitCompatReference = new AtomicReference(null);

    private SplitCompat() {
    }

    static boolean hasInstance() {
        return sSplitCompatReference.get() != null;
    }

    public static boolean install(Context context) {
        return installInternal(context);
    }

    private static boolean installInternal(Context context) {
        if (sSplitCompatReference.compareAndSet(null, new SplitCompat())) {
            SplitCompat splitCompat = (SplitCompat) sSplitCompatReference.get();
            SplitSessionLoaderSingleton.set(new SplitSessionLoaderImpl(TaskExecutors.MAIN_THREAD));
            LoadedSplitFetcherSingleton.set(new LoadedSplitFetcherImpl(splitCompat));
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public final Set getLoadedSplits() {
        return SplitLoadManagerService.getInstance().getLoadedSplitNames();
    }
}
