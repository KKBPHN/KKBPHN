package com.iqiyi.android.qigsaw.core.splitload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.splitreport.SplitLoadReporter;
import java.util.concurrent.atomic.AtomicReference;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitLoadReporterManager {
    private static final AtomicReference sLoadReporterRef = new AtomicReference();

    @Nullable
    static SplitLoadReporter getLoadReporter() {
        return (SplitLoadReporter) sLoadReporterRef.get();
    }

    public static void install(@NonNull SplitLoadReporter splitLoadReporter) {
        sLoadReporterRef.compareAndSet(null, splitLoadReporter);
    }
}
