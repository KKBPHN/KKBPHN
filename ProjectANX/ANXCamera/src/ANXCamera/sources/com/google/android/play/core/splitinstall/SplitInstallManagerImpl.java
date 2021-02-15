package com.google.android.play.core.splitinstall;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.google.android.play.core.tasks.Task;
import com.google.android.play.core.tasks.Tasks;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class SplitInstallManagerImpl implements SplitInstallManager {
    private static final String TAG = "SplitInstallManagerImpl";
    private final Context context;
    private final SplitInstallService mInstallService;
    private final Handler mMainHandler;
    private SplitInstallListenerRegistry mRegistry;
    private final String packageName;

    SplitInstallManagerImpl(SplitInstallService splitInstallService, Context context2) {
        this(splitInstallService, context2, context2.getPackageName());
    }

    private SplitInstallManagerImpl(SplitInstallService splitInstallService, Context context2, String str) {
        this.context = context2;
        this.packageName = str;
        this.mInstallService = splitInstallService;
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mRegistry = new SplitInstallListenerRegistry(context2);
    }

    private String cutSplitName(String str) {
        return str.split("\\.config\\.")[0];
    }

    private Set getFusedModules() {
        String str = TAG;
        HashSet hashSet = new HashSet();
        try {
            ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(this.packageName, 128);
            if (applicationInfo.metaData != null) {
                String string = applicationInfo.metaData.getString("shadow.bundletool.com.android.dynamic.apk.fused.modules");
                if (string == null || string.isEmpty()) {
                    Log.d(str, "App has no fused modules.");
                    return hashSet;
                }
                Collections.addAll(hashSet, string.split(",", -1));
                hashSet.remove("");
                return hashSet;
            }
            Log.d(str, "App has no applicationInfo or metaData");
            return hashSet;
        } catch (Throwable unused) {
            Log.w(str, "App is not found in PackageManager");
            return hashSet;
        }
    }

    private Set getInstalledSplitInstallInfo() {
        Set fusedModules = getFusedModules();
        if (VERSION.SDK_INT < 21) {
            return fusedModules;
        }
        String[] splitInstallInfo = getSplitInstallInfo();
        String str = TAG;
        if (splitInstallInfo == null) {
            Log.d(str, "No splits are found or app cannot be found in package manager.");
            return fusedModules;
        }
        String str2 = "Split names are: ";
        String arrays = Arrays.toString(splitInstallInfo);
        if (arrays.length() != 0) {
            str2 = str2.concat(arrays);
        }
        Log.d(str, str2);
        for (String str3 : splitInstallInfo) {
            if (!str3.startsWith("config.")) {
                fusedModules.add(cutSplitName(str3));
            }
        }
        return fusedModules;
    }

    @RequiresApi(api = 21)
    private String[] getSplitInstallInfo() {
        String[] strArr = null;
        try {
            PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(this.packageName, 0);
            if (packageInfo != null) {
                strArr = packageInfo.splitNames;
            }
            return strArr;
        } catch (Throwable unused) {
            Log.d(TAG, "App is not found in PackageManager");
            return null;
        }
    }

    public Task cancelInstall(int i) {
        return this.mInstallService.cancelInstall(i);
    }

    public Task deferredInstall(List list) {
        return this.mInstallService.deferredInstall(list);
    }

    public Task deferredUninstall(List list) {
        return this.mInstallService.deferredUninstall(list);
    }

    public Set getInstalledModules() {
        Set installedSplitInstallInfo = getInstalledSplitInstallInfo();
        return (installedSplitInstallInfo == null || installedSplitInstallInfo.isEmpty()) ? LoadedSplitFetcherSingleton.get().loadedSplits() : installedSplitInstallInfo;
    }

    /* access modifiers changed from: 0000 */
    public SplitInstallListenerRegistry getRegistry() {
        return this.mRegistry;
    }

    public Task getSessionState(int i) {
        return this.mInstallService.getSessionState(i);
    }

    public Task getSessionStates() {
        return this.mInstallService.getSessionStates();
    }

    public void registerListener(SplitInstallStateUpdatedListener splitInstallStateUpdatedListener) {
        getRegistry().registerListener(splitInstallStateUpdatedListener);
    }

    public boolean startConfirmationDialogForResult(SplitInstallSessionState splitInstallSessionState, Activity activity, int i) {
        if (splitInstallSessionState.status() != 8 || splitInstallSessionState.resolutionIntent() == null) {
            return false;
        }
        activity.startIntentSenderForResult(splitInstallSessionState.resolutionIntent().getIntentSender(), i, null, 0, 0, 0);
        return true;
    }

    public Task startInstall(SplitInstallRequest splitInstallRequest) {
        if (!getInstalledModules().containsAll(splitInstallRequest.getModuleNames())) {
            return this.mInstallService.startInstall(splitInstallRequest.getModuleNames());
        }
        this.mMainHandler.post(new SplitInstalledDisposer(this, splitInstallRequest));
        return Tasks.createTaskAndSetResult(Integer.valueOf(0));
    }

    public void unregisterListener(SplitInstallStateUpdatedListener splitInstallStateUpdatedListener) {
        getRegistry().unregisterListener(splitInstallStateUpdatedListener);
    }
}
