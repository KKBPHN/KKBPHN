package com.iqiyi.android.qigsaw.core;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import androidx.annotation.NonNull;
import com.google.android.play.core.splitcompat.SplitCompat;
import com.iqiyi.android.qigsaw.core.common.ProcessUtil;
import com.iqiyi.android.qigsaw.core.common.SplitBaseInfoProvider;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.extension.AABExtension;
import com.iqiyi.android.qigsaw.core.splitdownload.Downloader;
import com.iqiyi.android.qigsaw.core.splitinstall.SplitApkInstaller;
import com.iqiyi.android.qigsaw.core.splitinstall.SplitInstallReporterManager;
import com.iqiyi.android.qigsaw.core.splitinstall.SplitUninstallReporterManager;
import com.iqiyi.android.qigsaw.core.splitload.SplitLoadManagerService;
import com.iqiyi.android.qigsaw.core.splitload.SplitLoadReporterManager;
import com.iqiyi.android.qigsaw.core.splitreport.DefaultSplitInstallReporter;
import com.iqiyi.android.qigsaw.core.splitreport.DefaultSplitLoadReporter;
import com.iqiyi.android.qigsaw.core.splitreport.DefaultSplitUninstallReporter;
import com.iqiyi.android.qigsaw.core.splitreport.DefaultSplitUpdateReporter;
import com.iqiyi.android.qigsaw.core.splitreport.SplitInstallReporter;
import com.iqiyi.android.qigsaw.core.splitreport.SplitLoadReporter;
import com.iqiyi.android.qigsaw.core.splitreport.SplitUninstallReporter;
import com.iqiyi.android.qigsaw.core.splitreport.SplitUpdateReporter;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitUpdateReporterManager;
import java.util.concurrent.atomic.AtomicReference;

public class Qigsaw {
    private static final AtomicReference sReference = new AtomicReference();
    /* access modifiers changed from: private */
    public final Context context;
    private final String currentProcessName;
    private final Downloader downloader;
    private final boolean isMainProcess;
    private final SplitConfiguration splitConfiguration;

    private Qigsaw(Context context2, Downloader downloader2, @NonNull SplitConfiguration splitConfiguration2) {
        this.context = context2;
        this.downloader = downloader2;
        this.splitConfiguration = splitConfiguration2;
        this.currentProcessName = ProcessUtil.getProcessName(context2);
        this.isMainProcess = context2.getPackageName().equals(this.currentProcessName);
    }

    /* access modifiers changed from: private */
    public static void cleanStaleSplits(Context context2) {
        try {
            Intent intent = new Intent();
            intent.setClassName(context2, "com.iqiyi.android.qigsaw.core.splitinstall.SplitCleanService");
            context2.startService(intent);
        } catch (Exception unused) {
        }
    }

    public static void install(@NonNull Context context2, @NonNull Downloader downloader2) {
        install(context2, downloader2, SplitConfiguration.newBuilder().build());
    }

    public static void install(@NonNull Context context2, @NonNull Downloader downloader2, @NonNull SplitConfiguration splitConfiguration2) {
        if (sReference.compareAndSet(null, new Qigsaw(context2, downloader2, splitConfiguration2))) {
            instance().onBaseContextAttached();
        }
    }

    private static Qigsaw instance() {
        if (sReference.get() != null) {
            return (Qigsaw) sReference.get();
        }
        throw new RuntimeException("Have you invoke Qigsaw#install(...)?");
    }

    public static void onApplicationCreated() {
        instance().onCreated();
    }

    public static void onApplicationGetResources(Resources resources) {
        if (SplitLoadManagerService.hasInstance() && resources != null) {
            SplitLoadManagerService.getInstance().getResources(resources);
        }
    }

    private void onBaseContextAttached() {
        SplitBaseInfoProvider.setPackageName(this.context.getPackageName());
        boolean isQigsawMode = SplitBaseInfoProvider.isQigsawMode();
        if (this.isMainProcess) {
            SplitUpdateReporter splitUpdateReporter = this.splitConfiguration.updateReporter;
            if (splitUpdateReporter == null) {
                splitUpdateReporter = new DefaultSplitUpdateReporter(this.context);
            }
            SplitUpdateReporterManager.install(splitUpdateReporter);
        }
        Context context2 = this.context;
        SplitConfiguration splitConfiguration2 = this.splitConfiguration;
        SplitLoadManagerService.install(context2, splitConfiguration2.splitLoadMode, isQigsawMode, this.isMainProcess, this.currentProcessName, splitConfiguration2.workProcesses, splitConfiguration2.forbiddenWorkProcesses);
        SplitLoadManagerService.getInstance().injectPathClassloader();
        AABExtension.getInstance().createAndActiveSplitApplication(this.context, isQigsawMode);
        SplitCompat.install(this.context);
    }

    private void onCreated() {
        AABExtension.getInstance().onApplicationCreate();
        SplitLoadReporter splitLoadReporter = this.splitConfiguration.loadReporter;
        if (splitLoadReporter == null) {
            splitLoadReporter = new DefaultSplitLoadReporter(this.context);
        }
        SplitLoadReporterManager.install(splitLoadReporter);
        if (this.isMainProcess) {
            SplitInstallReporter splitInstallReporter = this.splitConfiguration.installReporter;
            if (splitInstallReporter == null) {
                splitInstallReporter = new DefaultSplitInstallReporter(this.context);
            }
            SplitInstallReporterManager.install(splitInstallReporter);
            SplitUninstallReporter splitUninstallReporter = this.splitConfiguration.uninstallReporter;
            if (splitUninstallReporter == null) {
                splitUninstallReporter = new DefaultSplitUninstallReporter(this.context);
            }
            SplitUninstallReporterManager.install(splitUninstallReporter);
            Context context2 = this.context;
            Downloader downloader2 = this.downloader;
            SplitConfiguration splitConfiguration2 = this.splitConfiguration;
            SplitApkInstaller.install(context2, downloader2, splitConfiguration2.obtainUserConfirmationDialogClass, splitConfiguration2.verifySignature);
            SplitApkInstaller.startUninstallSplits(this.context);
            if (Looper.myLooper() != null) {
                Looper.myQueue().addIdleHandler(new IdleHandler() {
                    public boolean queueIdle() {
                        Qigsaw.cleanStaleSplits(Qigsaw.this.context);
                        return false;
                    }
                });
            } else {
                cleanStaleSplits(this.context);
            }
        }
        SplitLoadManagerService.getInstance().loadInstalledSplitsWhenAppLaunches();
    }

    public static void registerSplitActivityLifecycleCallbacks(SplitActivityLifecycleCallbacks splitActivityLifecycleCallbacks) {
        Context context2 = instance().context;
        if (context2 instanceof Application) {
            ((Application) context2).registerActivityLifecycleCallbacks(splitActivityLifecycleCallbacks);
            return;
        }
        throw new RuntimeException("If you want to monitor lifecycle of split activity, Application context must be required for Qigsaw#install(...)!");
    }

    public static void unregisterSplitActivityLifecycleCallbacks(SplitActivityLifecycleCallbacks splitActivityLifecycleCallbacks) {
        Context context2 = instance().context;
        if (context2 instanceof Application) {
            ((Application) context2).unregisterActivityLifecycleCallbacks(splitActivityLifecycleCallbacks);
            return;
        }
        throw new RuntimeException("If you want to monitor lifecycle of split activity, Application context must be required for Qigsaw#install(...)!");
    }

    public static boolean updateSplits(Context context2, @NonNull String str, @NonNull String str2) {
        try {
            Intent intent = new Intent();
            intent.setClassName(context2, "com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitUpdateService");
            intent.putExtra(SplitConstants.NEW_SPLIT_INFO_VERSION, str);
            intent.putExtra(SplitConstants.NEW_SPLIT_INFO_PATH, str2);
            context2.startService(intent);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}
