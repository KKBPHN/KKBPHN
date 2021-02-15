package com.iqiyi.android.qigsaw.core.splitload;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitload.listener.OnSplitLoadListener;
import com.iqiyi.android.qigsaw.core.splitreport.SplitBriefInfo;
import com.iqiyi.android.qigsaw.core.splitreport.SplitLoadError;
import com.iqiyi.android.qigsaw.core.splitreport.SplitLoadReporter;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManager;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManagerService;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

abstract class SplitLoadTask implements Runnable {
    private static final String TAG = "SplitLoadTask";
    private final SplitActivator activator;
    final Context appContext;
    private final SplitInfoManager infoManager;
    private final OnSplitLoadListener loadListener;
    private final SplitLoadManager loadManager;
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    private final Handler mainHandler;
    private final List splitFileIntents;

    SplitLoadTask(@NonNull SplitLoadManager splitLoadManager, @NonNull List list, @Nullable OnSplitLoadListener onSplitLoadListener) {
        this.loadManager = splitLoadManager;
        this.splitFileIntents = list;
        this.loadListener = onSplitLoadListener;
        this.appContext = splitLoadManager.getContext();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.infoManager = SplitInfoManagerService.getInstance();
        this.activator = new SplitActivator(splitLoadManager.getContext());
    }

    private boolean checkSplitLoaded(String str) {
        for (Split split : this.loadManager.getLoadedSplits()) {
            if (split.splitName.equals(str)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void loadSplitInternal() {
        long j;
        SplitLoader splitLoader;
        ArrayList arrayList;
        String str;
        String str2;
        SplitBriefInfo splitBriefInfo;
        int i;
        ClassLoader loadCode;
        long currentTimeMillis = System.currentTimeMillis();
        SplitLoader createSplitLoader = createSplitLoader();
        HashSet hashSet = new HashSet();
        char c = 0;
        ArrayList arrayList2 = new ArrayList(0);
        ArrayList arrayList3 = new ArrayList(this.splitFileIntents.size());
        for (Intent intent : this.splitFileIntents) {
            String stringExtra = intent.getStringExtra(SplitConstants.KET_NAME);
            SplitInfo splitInfo = this.infoManager.getSplitInfo(this.appContext, stringExtra);
            String str3 = TAG;
            if (splitInfo == null) {
                Object[] objArr = new Object[1];
                if (stringExtra == null) {
                    stringExtra = "null";
                }
                objArr[c] = stringExtra;
                SplitLog.w(str3, "Unable to get info of %s, just skip!", objArr);
            } else {
                SplitBriefInfo splitBriefInfo2 = new SplitBriefInfo(splitInfo.getSplitName(), splitInfo.getSplitVersion(), splitInfo.isBuiltIn());
                if (checkSplitLoaded(stringExtra)) {
                    SplitLog.i(str3, "Split %s has been loaded!", stringExtra);
                } else {
                    String stringExtra2 = intent.getStringExtra(SplitConstants.KEY_APK);
                    try {
                        createSplitLoader.loadResources(stringExtra2);
                        ArrayList stringArrayListExtra = intent.getStringArrayListExtra(SplitConstants.KEY_ADDED_DEX);
                        File splitOptDir = SplitPathManager.require().getSplitOptDir(splitInfo);
                        File file = null;
                        if (splitInfo.hasLibs()) {
                            file = SplitPathManager.require().getSplitLibDir(splitInfo);
                        }
                        File file2 = file;
                        File splitDir = SplitPathManager.require().getSplitDir(splitInfo);
                        try {
                            SplitLoader splitLoader2 = createSplitLoader;
                            splitLoader = createSplitLoader;
                            File file3 = splitDir;
                            j = currentTimeMillis;
                            splitBriefInfo = splitBriefInfo2;
                            str2 = str3;
                            File file4 = splitOptDir;
                            ArrayList arrayList4 = arrayList2;
                            str = stringExtra;
                            try {
                                loadCode = loadCode(splitLoader2, stringExtra, stringArrayListExtra, file4, file2, splitInfo.getDependencies());
                            } catch (SplitLoadException e) {
                                e = e;
                                arrayList = arrayList4;
                                i = 1;
                                Object[] objArr2 = new Object[i];
                                objArr2[0] = str;
                                SplitLog.printErrStackTrace(str2, e, "Failed to load split %s code!", objArr2);
                                arrayList.add(new SplitLoadError(splitBriefInfo, e.getErrorCode(), e.getCause()));
                                arrayList2 = arrayList;
                                createSplitLoader = splitLoader;
                                currentTimeMillis = j;
                                c = 0;
                            }
                            try {
                                this.activator.activate(loadCode, str);
                                if (!file3.setLastModified(System.currentTimeMillis())) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Failed to set last modified time for ");
                                    sb.append(str);
                                    SplitLog.w(str2, sb.toString(), new Object[0]);
                                }
                                arrayList3.add(splitBriefInfo);
                                hashSet.add(new Split(str, stringExtra2));
                                arrayList2 = arrayList4;
                            } catch (SplitLoadException e2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Failed to activate ");
                                sb2.append(str);
                                SplitLog.printErrStackTrace(str2, e2, sb2.toString(), new Object[0]);
                                arrayList = arrayList4;
                                arrayList.add(new SplitLoadError(splitBriefInfo, e2.getErrorCode(), e2.getCause()));
                                onSplitActivateFailed(loadCode);
                                arrayList2 = arrayList;
                                createSplitLoader = splitLoader;
                                currentTimeMillis = j;
                                c = 0;
                            }
                        } catch (SplitLoadException e3) {
                            e = e3;
                            j = currentTimeMillis;
                            splitLoader = createSplitLoader;
                            arrayList = arrayList2;
                            splitBriefInfo = splitBriefInfo2;
                            str2 = str3;
                            str = stringExtra;
                            i = 1;
                            Object[] objArr22 = new Object[i];
                            objArr22[0] = str;
                            SplitLog.printErrStackTrace(str2, e, "Failed to load split %s code!", objArr22);
                            arrayList.add(new SplitLoadError(splitBriefInfo, e.getErrorCode(), e.getCause()));
                            arrayList2 = arrayList;
                            createSplitLoader = splitLoader;
                            currentTimeMillis = j;
                            c = 0;
                        }
                        createSplitLoader = splitLoader;
                        currentTimeMillis = j;
                    } catch (SplitLoadException e4) {
                        long j2 = currentTimeMillis;
                        SplitLoader splitLoader3 = createSplitLoader;
                        ArrayList arrayList5 = arrayList2;
                        SplitBriefInfo splitBriefInfo3 = splitBriefInfo2;
                        SplitLoadException splitLoadException = e4;
                        SplitLog.printErrStackTrace(str3, splitLoadException, "Failed to load split %s resources!", stringExtra);
                        arrayList5.add(new SplitLoadError(splitBriefInfo3, splitLoadException.getErrorCode(), splitLoadException.getCause()));
                        c = 0;
                        arrayList2 = arrayList5;
                        createSplitLoader = splitLoader3;
                        currentTimeMillis = j2;
                    }
                }
                c = 0;
            }
        }
        long j3 = currentTimeMillis;
        ArrayList arrayList6 = arrayList2;
        this.loadManager.putSplits(hashSet);
        reportLoadResult(arrayList3, arrayList6, System.currentTimeMillis() - j3);
    }

    private void reportLoadResult(List list, List list2, long j) {
        SplitLoadReporter loadReporter = SplitLoadReporterManager.getLoadReporter();
        if (!list2.isEmpty()) {
            if (this.loadListener != null) {
                this.loadListener.onFailed(((SplitLoadError) list2.get(list2.size() - 1)).errorCode);
            }
            if (loadReporter != null) {
                loadReporter.onLoadFailed(this.loadManager.currentProcessName, list, list2, j);
                return;
            }
            return;
        }
        OnSplitLoadListener onSplitLoadListener = this.loadListener;
        if (onSplitLoadListener != null) {
            onSplitLoadListener.onCompleted();
        }
        if (loadReporter != null) {
            loadReporter.onLoadOK(this.loadManager.currentProcessName, list, j);
        }
    }

    public abstract SplitLoader createSplitLoader();

    public abstract ClassLoader loadCode(SplitLoader splitLoader, String str, List list, File file, File file2, List list2);

    public abstract void onSplitActivateFailed(ClassLoader classLoader);

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            loadSplitInternal();
            return;
        }
        synchronized (this.mLock) {
            this.mainHandler.post(new Runnable() {
                public void run() {
                    synchronized (SplitLoadTask.this.mLock) {
                        SplitLoadTask.this.loadSplitInternal();
                        SplitLoadTask.this.mLock.notifyAll();
                    }
                }
            });
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
                SplitInfo splitInfo = this.infoManager.getSplitInfo(this.appContext, ((Intent) this.splitFileIntents.get(0)).getStringExtra(SplitConstants.KET_NAME));
                if (splitInfo != null) {
                    reportLoadResult(Collections.emptyList(), Collections.singletonList(new SplitLoadError(new SplitBriefInfo(splitInfo.getSplitName(), splitInfo.getSplitVersion(), splitInfo.isBuiltIn()), -26, e)), 0);
                }
            }
        }
    }
}
