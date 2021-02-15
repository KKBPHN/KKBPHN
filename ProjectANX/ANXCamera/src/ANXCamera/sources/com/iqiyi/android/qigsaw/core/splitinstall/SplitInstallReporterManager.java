package com.iqiyi.android.qigsaw.core.splitinstall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.splitreport.SplitInstallReporter;
import java.util.concurrent.atomic.AtomicReference;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitInstallReporterManager {
    private static final AtomicReference sInstallReporterRef = new AtomicReference();

    @Nullable
    static SplitInstallReporter getInstallReporter() {
        return (SplitInstallReporter) sInstallReporterRef.get();
    }

    public static void install(@NonNull SplitInstallReporter splitInstallReporter) {
        sInstallReporterRef.compareAndSet(null, splitInstallReporter);
    }
}
