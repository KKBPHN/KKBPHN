package com.iqiyi.android.qigsaw.core.splitinstall;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.splitdownload.Downloader;
import com.iqiyi.android.qigsaw.core.splitinstall.remote.SplitInstallSupervisor;
import java.util.concurrent.atomic.AtomicReference;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class SplitApkInstaller {
    private static final AtomicReference sSplitApkInstallerRef = new AtomicReference();

    private SplitApkInstaller() {
    }

    @Nullable
    public static SplitInstallSupervisor getSplitInstallSupervisor() {
        return (SplitInstallSupervisor) sSplitApkInstallerRef.get();
    }

    public static void install(Context context, Downloader downloader, Class cls, boolean z) {
        if (sSplitApkInstallerRef.get() == null) {
            AtomicReference atomicReference = sSplitApkInstallerRef;
            SplitInstallSupervisorImpl splitInstallSupervisorImpl = new SplitInstallSupervisorImpl(context, new SplitInstallSessionManagerImpl(context), downloader, cls, z);
            atomicReference.set(splitInstallSupervisorImpl);
        }
    }

    public static void startUninstallSplits(Context context) {
        if (sSplitApkInstallerRef.get() != null) {
            ((SplitInstallSupervisor) sSplitApkInstallerRef.get()).startUninstall(context);
            return;
        }
        throw new RuntimeException("Have you install SplitApkInstaller?");
    }
}
