package com.iqiyi.android.qigsaw.core.splitinstall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.splitreport.SplitUninstallReporter;
import java.util.concurrent.atomic.AtomicReference;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitUninstallReporterManager {
    private static final AtomicReference sUninstallReporterRef = new AtomicReference();

    @Nullable
    public static SplitUninstallReporter getUninstallReporter() {
        return (SplitUninstallReporter) sUninstallReporterRef.get();
    }

    public static void install(@NonNull SplitUninstallReporter splitUninstallReporter) {
        sUninstallReporterRef.compareAndSet(null, splitUninstallReporter);
    }
}
