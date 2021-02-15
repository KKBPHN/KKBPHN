package com.iqiyi.android.qigsaw.core.splitrequest.splitinfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.splitreport.SplitUpdateReporter;
import java.util.concurrent.atomic.AtomicReference;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitUpdateReporterManager {
    private static final AtomicReference sUpdateReporterRef = new AtomicReference();

    @Nullable
    static SplitUpdateReporter getUpdateReporter() {
        return (SplitUpdateReporter) sUpdateReporterRef.get();
    }

    public static void install(@NonNull SplitUpdateReporter splitUpdateReporter) {
        sUpdateReporterRef.compareAndSet(null, splitUpdateReporter);
    }
}
