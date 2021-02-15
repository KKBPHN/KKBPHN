package com.ss.android.ugc.effectmanager.effect.bridge;

import com.ss.android.ugc.effectmanager.effect.sync.SyncTask;

public interface EffectFetcher {
    SyncTask fetchEffect(EffectFetcherArguments effectFetcherArguments);
}
