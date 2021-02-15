package com.google.android.play.core.splitcompat;

import com.google.android.play.core.splitinstall.LoadedSplitFetcher;
import java.util.Set;

final class LoadedSplitFetcherImpl implements LoadedSplitFetcher {
    private final SplitCompat mSplitCompat;

    LoadedSplitFetcherImpl(SplitCompat splitCompat) {
        this.mSplitCompat = splitCompat;
    }

    public Set loadedSplits() {
        return this.mSplitCompat.getLoadedSplits();
    }
}
