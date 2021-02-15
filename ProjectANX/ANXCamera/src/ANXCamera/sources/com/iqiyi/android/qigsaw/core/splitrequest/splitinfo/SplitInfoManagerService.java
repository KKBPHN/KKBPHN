package com.iqiyi.android.qigsaw.core.splitrequest.splitinfo;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.concurrent.atomic.AtomicReference;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitInfoManagerService {
    private static final AtomicReference sReference = new AtomicReference();

    private static SplitInfoManagerImpl createSplitInfoManager(Context context, boolean z) {
        SplitInfoVersionManager createSplitInfoVersionManager = SplitInfoVersionManagerImpl.createSplitInfoVersionManager(context, z);
        SplitInfoManagerImpl splitInfoManagerImpl = new SplitInfoManagerImpl();
        splitInfoManagerImpl.attach(createSplitInfoVersionManager);
        return splitInfoManagerImpl;
    }

    @Nullable
    public static SplitInfoManager getInstance() {
        return (SplitInfoManager) sReference.get();
    }

    public static void install(Context context, boolean z) {
        sReference.compareAndSet(null, createSplitInfoManager(context, z));
    }
}
