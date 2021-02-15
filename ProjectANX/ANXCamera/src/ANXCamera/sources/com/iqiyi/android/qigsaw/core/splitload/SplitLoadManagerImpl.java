package com.iqiyi.android.qigsaw.core.splitload;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.OEMCompat;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitload.listener.OnSplitLoadListener;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManager;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManagerService;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class SplitLoadManagerImpl extends SplitLoadManager {
    private final String[] forbiddenWorkProcesses;
    private final boolean qigsawMode;
    private final String[] workProcesses;

    SplitLoadManagerImpl(Context context, int i, boolean z, boolean z2, String str, String[] strArr, String[] strArr2) {
        super(context, str, i);
        this.qigsawMode = z;
        this.workProcesses = strArr;
        this.forbiddenWorkProcesses = strArr2;
        SplitInfoManagerService.install(context, z2);
        SplitPathManager.install(context);
    }

    private boolean canBeWorkedInThisProcessForSplit(SplitInfo splitInfo) {
        List workProcesses2 = splitInfo.getWorkProcesses();
        if (workProcesses2 == null || workProcesses2.isEmpty()) {
            return true;
        }
        return workProcesses2.contains(this.currentProcessName.replace(getContext().getPackageName(), ""));
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo>, for r9v0, types: [java.util.Collection<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo>, java.util.Collection] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List createInstalledSplitFileIntents(@NonNull Collection<SplitInfo> collection) {
        Object[] objArr;
        String str;
        ArrayList arrayList = new ArrayList();
        for (SplitInfo splitInfo : collection) {
            String str2 = "SplitLoadManager";
            if (!canBeWorkedInThisProcessForSplit(splitInfo)) {
                objArr = new Object[]{splitInfo.getSplitName(), this.currentProcessName};
                str = "Split %s do not need work in process %s";
            } else if (getLoadedSplitNames().contains(splitInfo.getSplitName())) {
                objArr = new Object[]{splitInfo.getSplitName()};
                str = "Split %s has been loaded, ignore it!";
            } else {
                Intent createLastInstalledSplitFileIntent = createLastInstalledSplitFileIntent(splitInfo);
                if (createLastInstalledSplitFileIntent != null) {
                    arrayList.add(createLastInstalledSplitFileIntent);
                }
                Object[] objArr2 = new Object[4];
                objArr2[0] = splitInfo.getSplitName();
                objArr2[1] = this.currentProcessName;
                objArr2[2] = createLastInstalledSplitFileIntent == null ? "but" : "and";
                objArr2[3] = createLastInstalledSplitFileIntent == null ? "not installed" : "installed";
                SplitLog.i(str2, "Split %s will work in process %s, %s it is %s", objArr2);
            }
            SplitLog.i(str2, str, objArr);
        }
        return arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x010e  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x019a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private Intent createLastInstalledSplitFileIntent(SplitInfo splitInfo) {
        File file;
        List<String> dependencies;
        File oatFilePath;
        String str;
        StringBuilder sb;
        String splitName = splitInfo.getSplitName();
        File splitDir = SplitPathManager.require().getSplitDir(splitInfo);
        File splitMarkFile = SplitPathManager.require().getSplitMarkFile(splitInfo);
        File splitSpecialMarkFile = SplitPathManager.require().getSplitSpecialMarkFile(splitInfo);
        if (!splitInfo.isBuiltIn() || !splitInfo.getUrl().startsWith(SplitConstants.URL_NATIVE)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(splitName);
            sb2.append(".apk");
            file = new File(splitDir, sb2.toString());
        } else {
            String str2 = getContext().getApplicationInfo().nativeLibraryDir;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(SplitConstants.SPLIT_PREFIX);
            sb3.append(splitInfo.getSplitName());
            file = new File(str2, System.mapLibraryName(sb3.toString()));
        }
        String str3 = "SplitLoadManager";
        if (splitSpecialMarkFile.exists() && !splitMarkFile.exists()) {
            SplitLog.v(str3, "In vivo & oppo, we need to check oat file when split is going to be loaded.", new Object[0]);
            oatFilePath = OEMCompat.getOatFilePath(file, SplitPathManager.require().getSplitOptDir(splitInfo));
            if (FileUtil.isLegalFile(oatFilePath)) {
                boolean checkOatFile = OEMCompat.checkOatFile(oatFilePath);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Check result of oat file %s is ");
                sb4.append(checkOatFile);
                SplitLog.v(str3, sb4.toString(), oatFilePath.getAbsoluteFile());
                File splitSpecialLockFile = SplitPathManager.require().getSplitSpecialLockFile(splitInfo);
                if (checkOatFile) {
                    try {
                        FileUtil.createFileSafelyLock(splitMarkFile, splitSpecialLockFile);
                    } catch (IOException unused) {
                        sb = new StringBuilder();
                        str = "Failed to create installed mark file ";
                    }
                } else {
                    try {
                        FileUtil.deleteFileSafelyLock(oatFilePath, splitSpecialLockFile);
                    } catch (IOException unused2) {
                        sb = new StringBuilder();
                        str = "Failed to delete corrupted oat file ";
                    }
                }
            } else {
                SplitLog.v(str3, "Oat file %s is still not exist in vivo & oppo, system continue to use interpreter mode.", oatFilePath.getAbsoluteFile());
            }
        }
        ArrayList arrayList = null;
        if (splitMarkFile.exists() && !splitSpecialMarkFile.exists()) {
            return null;
        }
        dependencies = splitInfo.getDependencies();
        if (dependencies != null) {
            SplitLog.i(str3, "Split %s has dependencies %s !", splitName, dependencies);
            for (String str4 : dependencies) {
                if (!SplitPathManager.require().getSplitMarkFile(SplitInfoManagerService.getInstance().getSplitInfo(getContext(), str4)).exists()) {
                    SplitLog.i(str3, "Dependency %s mark file is not existed!", str4);
                    return null;
                }
            }
        }
        if (splitInfo.hasDex()) {
            arrayList = new ArrayList();
            arrayList.add(file.getAbsolutePath());
            File[] listFiles = SplitPathManager.require().getSplitCodeCacheDir(splitInfo).listFiles(new FilenameFilter() {
                public boolean accept(File file, String str) {
                    return str.endsWith(".zip");
                }
            });
            if (listFiles != null && listFiles.length > 0) {
                for (File absolutePath : listFiles) {
                    arrayList.add(absolutePath.getAbsolutePath());
                }
            }
        }
        Intent intent = new Intent();
        intent.putExtra(SplitConstants.KET_NAME, splitName);
        intent.putExtra(SplitConstants.KEY_APK, file.getAbsolutePath());
        if (arrayList != null) {
            intent.putStringArrayListExtra(SplitConstants.KEY_ADDED_DEX, arrayList);
        }
        return intent;
        sb.append(str);
        sb.append(oatFilePath.exists());
        SplitLog.w(str3, sb.toString(), new Object[0]);
        ArrayList arrayList2 = null;
        if (splitMarkFile.exists()) {
        }
        dependencies = splitInfo.getDependencies();
        if (dependencies != null) {
        }
        if (splitInfo.hasDex()) {
        }
        Intent intent2 = new Intent();
        intent2.putExtra(SplitConstants.KET_NAME, splitName);
        intent2.putExtra(SplitConstants.KEY_APK, file.getAbsolutePath());
        if (arrayList2 != null) {
        }
        return intent2;
    }

    private void deferredLoadInstalledSplitsIfNeed() {
        if (this.splitLoadMode == 1) {
            Looper.myQueue().addIdleHandler(new IdleHandler() {
                public boolean queueIdle() {
                    SplitLoadManagerImpl.this.loadInstalledSplits();
                    return false;
                }
            });
        } else {
            loadInstalledSplits();
        }
    }

    private Context getBaseContext() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return context;
    }

    private String getCompleteProcessName(@Nullable String str) {
        String packageName = getContext().getPackageName();
        if (TextUtils.isEmpty(str)) {
            return packageName;
        }
        if (str.startsWith(packageName)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(packageName);
        sb.append(str);
        return sb.toString();
    }

    private void injectClassLoader(ClassLoader classLoader) {
        try {
            SplitDelegateClassloader.inject(classLoader, getBaseContext());
        } catch (Exception e) {
            SplitLog.printErrStackTrace("SplitLoadManager", e, "Failed to hook PathClassloader", new Object[0]);
        }
    }

    private boolean isInjectPathClassloaderNeeded() {
        if (VERSION.SDK_INT < 29) {
            return this.qigsawMode;
        }
        boolean z = !(getContext().getClassLoader() instanceof SplitDelegateClassloader) && this.qigsawMode;
        return z;
    }

    private boolean isProcessAllowedToWork() {
        if ((this.workProcesses == null && this.forbiddenWorkProcesses == null) || getContext().getPackageName().equals(this.currentProcessName)) {
            return true;
        }
        String[] strArr = this.forbiddenWorkProcesses;
        int i = 0;
        if (strArr != null) {
            for (String completeProcessName : strArr) {
                if (getCompleteProcessName(completeProcessName).equals(this.currentProcessName)) {
                    return false;
                }
            }
        }
        String[] strArr2 = this.workProcesses;
        if (strArr2 != null) {
            int length = strArr2.length;
            while (i < length && !getCompleteProcessName(strArr2[i]).equals(this.currentProcessName)) {
                i++;
            }
            return true;
        }
        return true;
    }

    public Runnable createSplitLoadTask(List list, @Nullable OnSplitLoadListener onSplitLoadListener) {
        return this.splitLoadMode == 1 ? new SplitLoadTaskImpl(this, list, onSplitLoadListener) : new SplitLoadTaskImpl2(this, list, onSplitLoadListener);
    }

    public void getResources(Resources resources) {
        try {
            SplitCompatResourcesLoader.loadResources(getContext(), resources);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void injectPathClassloader() {
        if (isInjectPathClassloaderNeeded() && isProcessAllowedToWork()) {
            injectClassLoader(getContext().getClassLoader());
        }
        ClassLoader classLoader = getContext().getClassLoader();
        if (classLoader instanceof SplitDelegateClassloader) {
            ((SplitDelegateClassloader) classLoader).setClassNotFoundInterceptor(new DefaultClassNotFoundInterceptor(getContext(), SplitLoadManagerImpl.class.getClassLoader(), this.splitLoadMode));
        }
    }

    public void loadInstalledSplits() {
        SplitInfoManager instance = SplitInfoManagerService.getInstance();
        String str = "SplitLoadManager";
        if (instance == null) {
            SplitLog.w(str, "Failed to get SplitInfoManager instance, have you invoke Qigsaw#install(...) method?", new Object[0]);
            return;
        }
        Collection allSplitInfo = instance.getAllSplitInfo(getContext());
        if (allSplitInfo == null) {
            SplitLog.w(str, "Failed to get Split-Info list!", new Object[0]);
            return;
        }
        List createInstalledSplitFileIntents = createInstalledSplitFileIntents(allSplitInfo);
        if (createInstalledSplitFileIntents.isEmpty()) {
            SplitLog.w(str, "There are no installed splits!", new Object[0]);
            return;
        }
        createSplitLoadTask(createInstalledSplitFileIntents, null).run();
    }

    public void loadInstalledSplitsWhenAppLaunches() {
        if (this.qigsawMode && isProcessAllowedToWork()) {
            deferredLoadInstalledSplitsIfNeed();
        }
    }
}
