package com.iqiyi.android.qigsaw.core.splitinstall;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.SplitAABInfoProvider;
import com.iqiyi.android.qigsaw.core.common.SplitBaseInfoProvider;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitdownload.DownloadRequest;
import com.iqiyi.android.qigsaw.core.splitdownload.Downloader;
import com.iqiyi.android.qigsaw.core.splitinstall.remote.SplitInstallSupervisor;
import com.iqiyi.android.qigsaw.core.splitinstall.remote.SplitInstallSupervisor.Callback;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManager;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfoManagerService;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jcodec.containers.mp4.boxes.Box;

final class SplitInstallSupervisorImpl extends SplitInstallSupervisor {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String TAG = "Split:SplitInstallSupervisorImpl";
    private final Context appContext;
    private final long downloadSizeThresholdValue;
    private final List dynamicFeatures;
    private final Set installedSplitForAAB;
    private final Class obtainUserConfirmationActivityClass;
    private final SplitInstallSessionManager sessionManager;
    private final SplitInstaller splitInstaller;
    private final Downloader userDownloader;
    private final boolean verifySignature;

    SplitInstallSupervisorImpl(Context context, SplitInstallSessionManager splitInstallSessionManager, Downloader downloader, Class cls, boolean z) {
        this.appContext = context;
        this.sessionManager = splitInstallSessionManager;
        this.userDownloader = downloader;
        long downloadSizeThresholdWhenUsingMobileData = downloader.getDownloadSizeThresholdWhenUsingMobileData();
        if (downloadSizeThresholdWhenUsingMobileData < 0) {
            downloadSizeThresholdWhenUsingMobileData = Long.MAX_VALUE;
        }
        this.downloadSizeThresholdValue = downloadSizeThresholdWhenUsingMobileData;
        this.installedSplitForAAB = new SplitAABInfoProvider(this.appContext).getInstalledSplitsForAAB();
        this.obtainUserConfirmationActivityClass = cls;
        this.splitInstaller = new SplitInstallerImpl(context, z);
        this.verifySignature = z;
        String[] dynamicFeatures2 = SplitBaseInfoProvider.getDynamicFeatures();
        this.dynamicFeatures = dynamicFeatures2 == null ? null : Arrays.asList(dynamicFeatures2);
        if (this.dynamicFeatures == null) {
            SplitLog.w(TAG, "Can't read dynamicFeatures from SplitBaseInfoProvider", new Object[0]);
        }
    }

    private int checkInternalErrorCode() {
        SplitInfoManager instance = SplitInfoManagerService.getInstance();
        String str = TAG;
        if (instance == null) {
            SplitLog.w(str, "Failed to fetch SplitInfoManager instance!", new Object[0]);
            return -100;
        }
        Collection allSplitInfo = instance.getAllSplitInfo(this.appContext);
        if (allSplitInfo == null || allSplitInfo.isEmpty()) {
            SplitLog.w(str, "Failed to parse json file of split info!", new Object[0]);
            return -100;
        }
        String qigsawId = instance.getQigsawId(this.appContext);
        String qigsawId2 = SplitBaseInfoProvider.getQigsawId();
        if (!TextUtils.isEmpty(qigsawId) && qigsawId.equals(qigsawId2)) {
            return 0;
        }
        SplitLog.w(str, "Failed to match base app qigsaw-version excepted %s but %s!", qigsawId2, qigsawId);
        return -100;
    }

    private int checkRequestErrorCode(List list) {
        if (isRequestInvalid(list)) {
            return -3;
        }
        return !isModuleAvailable(list) ? -2 : 0;
    }

    private boolean checkSplitInfo(SplitInfo splitInfo) {
        return isCPUArchMatched(splitInfo) && isMinSdkVersionMatched(splitInfo);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo>, for r6v0, types: [java.util.Collection<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo>, java.util.Collection] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List createDownloadRequests(Collection<SplitInfo> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        for (SplitInfo splitInfo : collection) {
            File splitDir = SplitPathManager.require().getSplitDir(splitInfo);
            StringBuilder sb = new StringBuilder();
            sb.append(splitInfo.getSplitName());
            sb.append(".apk");
            arrayList.add(DownloadRequest.newBuilder().url(splitInfo.getUrl()).fileDir(splitDir.getAbsolutePath()).fileName(sb.toString()).fileMD5(splitInfo.getMd5()).moduleName(splitInfo.getSplitName()).build());
        }
        return arrayList;
    }

    private void deferredDownloadSplits(List list, Callback callback) {
        String str = TAG;
        try {
            long[] onPreDownloadSplits = onPreDownloadSplits(list);
            callback.onDeferredInstall(null);
            long j = onPreDownloadSplits[1];
            int createSessionId = SplitInstallSupervisor.createSessionId((Collection) list);
            StringBuilder sb = new StringBuilder();
            sb.append("DeferredInstall session id: ");
            sb.append(createSessionId);
            SplitLog.d(str, sb.toString(), new Object[0]);
            DeferredDownloadCallback deferredDownloadCallback = new DeferredDownloadCallback(this.splitInstaller, list);
            if (j == 0) {
                SplitLog.d(str, "Splits have been downloaded, install them directly!", new Object[0]);
                deferredDownloadCallback.onCompleted();
                return;
            }
            boolean z = j < this.downloadSizeThresholdValue && !this.userDownloader.isDeferredDownloadOnlyWhenUsingWifiData();
            this.userDownloader.deferredDownload(createSessionId, createDownloadRequests(list), deferredDownloadCallback, z);
        } catch (IOException e) {
            callback.onError(SplitInstallSupervisor.bundleErrorCode(-99));
            SplitLog.printErrStackTrace(str, e, "Failed to copy builtin split apks(%s)", "onDeferredInstall");
        }
    }

    private Set getInstalledSplitForAAB() {
        return this.installedSplitForAAB;
    }

    private List getNeed2BeInstalledSplits(List list) {
        SplitInfoManager instance = SplitInfoManagerService.getInstance();
        List<SplitInfo> splitInfos = instance.getSplitInfos(this.appContext, list);
        HashSet hashSet = new HashSet(0);
        for (SplitInfo splitInfo : splitInfos) {
            if (splitInfo.getDependencies() != null) {
                hashSet.addAll(splitInfo.getDependencies());
            }
        }
        if (hashSet.isEmpty()) {
            return splitInfos;
        }
        hashSet.removeAll(list);
        SplitLog.i(TAG, "Add dependencies %s automatically for install splits %s!", hashSet.toString(), list.toString());
        List splitInfos2 = instance.getSplitInfos(this.appContext, hashSet);
        splitInfos2.addAll(splitInfos);
        return splitInfos2;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo>, for r1v0, types: [java.util.List, java.util.List<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isAllSplitsBuiltIn(List<SplitInfo> list) {
        for (SplitInfo isBuiltIn : list) {
            if (!isBuiltIn.isBuiltIn()) {
                return false;
            }
        }
        return true;
    }

    private boolean isCPUArchMatched(SplitInfo splitInfo) {
        if (splitInfo.hasLibs()) {
            try {
                splitInfo.getLibInfo();
            } catch (Throwable unused) {
                return false;
            }
        }
        return true;
    }

    private boolean isMinSdkVersionMatched(SplitInfo splitInfo) {
        return splitInfo.getMinSdkVersion() <= VERSION.SDK_INT;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.String>, for r6v0, types: [java.util.List, java.util.List<java.lang.String>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isModuleAvailable(List<String> list) {
        Collection allSplitInfo = SplitInfoManagerService.getInstance().getAllSplitInfo(this.appContext);
        for (String str : list) {
            Iterator it = allSplitInfo.iterator();
            while (true) {
                if (it.hasNext()) {
                    SplitInfo splitInfo = (SplitInfo) it.next();
                    if (splitInfo.getSplitName().equals(str) && !checkSplitInfo(splitInfo)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isRequestInvalid(List list) {
        if (list != null && !list.isEmpty()) {
            List list2 = this.dynamicFeatures;
            if (list2 != null && list2.containsAll(list)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo>, for r11v0, types: [java.util.Collection<com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo>, java.util.Collection] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long[] onPreDownloadSplits(Collection<SplitInfo> collection) {
        File file;
        long j = 0;
        long j2 = 0;
        for (SplitInfo splitInfo : collection) {
            File splitDir = SplitPathManager.require().getSplitDir(splitInfo);
            StringBuilder sb = new StringBuilder();
            sb.append(splitInfo.getSplitName());
            sb.append(".apk");
            String sb2 = sb.toString();
            if (splitInfo.getUrl().startsWith(SplitConstants.URL_NATIVE)) {
                String str = this.appContext.getApplicationInfo().nativeLibraryDir;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(SplitConstants.SPLIT_PREFIX);
                sb3.append(splitInfo.getSplitName());
                file = new File(str, System.mapLibraryName(sb3.toString()));
            } else {
                file = new File(splitDir, sb2);
            }
            SplitDownloadPreprocessor splitDownloadPreprocessor = new SplitDownloadPreprocessor(splitDir, file);
            try {
                splitDownloadPreprocessor.load(this.appContext, splitInfo, this.verifySignature);
                FileUtil.closeQuietly(splitDownloadPreprocessor);
                j += splitInfo.getSize();
                if (!file.exists()) {
                    j2 += splitInfo.getSize();
                }
            } catch (Throwable th) {
                FileUtil.closeQuietly(splitDownloadPreprocessor);
                throw th;
            }
        }
        return new long[]{j, j2};
    }

    private int onPreInstallSplits(List list) {
        if (!getInstalledSplitForAAB().isEmpty()) {
            return !getInstalledSplitForAAB().containsAll(list) ? -3 : 0;
        }
        int checkInternalErrorCode = checkInternalErrorCode();
        if (checkInternalErrorCode == 0) {
            checkInternalErrorCode = checkRequestErrorCode(list);
        }
        return checkInternalErrorCode;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x009c A[Catch:{ IOException -> 0x00c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00a7 A[Catch:{ IOException -> 0x00c9 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void startDownloadSplits(List list, List list2, Callback callback) {
        boolean z;
        long j;
        int i;
        boolean isActiveSessionsLimitExceeded = this.sessionManager.isActiveSessionsLimitExceeded();
        String str = TAG;
        if (isActiveSessionsLimitExceeded) {
            SplitLog.w(str, "Start install request error code: ACTIVE_SESSIONS_LIMIT_EXCEEDED", new Object[0]);
            i = -1;
        } else {
            int createSessionId = SplitInstallSupervisor.createSessionId((Collection) list2);
            List createDownloadRequests = createDownloadRequests(list2);
            StringBuilder sb = new StringBuilder();
            sb.append("startInstall session id: ");
            sb.append(createSessionId);
            SplitLog.d(str, sb.toString(), new Object[0]);
            SplitInstallInternalSessionState sessionState = this.sessionManager.getSessionState(createSessionId);
            if (sessionState == null) {
                sessionState = new SplitInstallInternalSessionState(createSessionId, list, list2, createDownloadRequests);
            } else if (sessionState.status() == 8) {
                z = true;
                if (!z || !this.sessionManager.isIncompatibleWithExistingSession(list)) {
                    long[] onPreDownloadSplits = onPreDownloadSplits(list2);
                    callback.onStartInstall(createSessionId, null);
                    this.sessionManager.setSessionState(createSessionId, sessionState);
                    long j2 = onPreDownloadSplits[0];
                    j = onPreDownloadSplits[1];
                    SplitLog.d(str, "totalBytesToDownload: %d, realTotalBytesNeedToDownload: %d ", Long.valueOf(j2), Long.valueOf(j));
                    sessionState.setTotalBytesToDownload(j2);
                    StartDownloadCallback startDownloadCallback = new StartDownloadCallback(this.splitInstaller, createSessionId, this.sessionManager, list2);
                    if (j > 0) {
                        SplitLog.d(str, "Splits have been downloaded, install them directly!", new Object[0]);
                        startDownloadCallback.onCompleted();
                    } else if (!SplitInstallSupervisor.isMobileAvailable(this.appContext) || j <= this.downloadSizeThresholdValue) {
                        this.sessionManager.changeSessionState(createSessionId, 1);
                        this.sessionManager.emitSessionState(sessionState);
                        this.userDownloader.startDownload(createSessionId, createDownloadRequests, startDownloadCallback);
                    } else {
                        startUserConfirmationActivity(sessionState, j, createDownloadRequests);
                        return;
                    }
                    return;
                }
                SplitLog.w(str, "Start install request error code: INCOMPATIBLE_WITH_EXISTING_SESSION", new Object[0]);
                i = -8;
            }
            z = false;
            if (!z) {
            }
            try {
                long[] onPreDownloadSplits2 = onPreDownloadSplits(list2);
                callback.onStartInstall(createSessionId, null);
                this.sessionManager.setSessionState(createSessionId, sessionState);
                long j22 = onPreDownloadSplits2[0];
                j = onPreDownloadSplits2[1];
                SplitLog.d(str, "totalBytesToDownload: %d, realTotalBytesNeedToDownload: %d ", Long.valueOf(j22), Long.valueOf(j));
                sessionState.setTotalBytesToDownload(j22);
                StartDownloadCallback startDownloadCallback2 = new StartDownloadCallback(this.splitInstaller, createSessionId, this.sessionManager, list2);
                if (j > 0) {
                }
            } catch (IOException e) {
                SplitLog.w(str, "Failed to copy internal splits", (Throwable) e);
                callback.onError(SplitInstallSupervisor.bundleErrorCode(-99));
            }
            return;
        }
        callback.onError(SplitInstallSupervisor.bundleErrorCode(i));
    }

    private void startUserConfirmationActivity(SplitInstallInternalSessionState splitInstallInternalSessionState, long j, List list) {
        Intent intent = new Intent();
        intent.putExtra("sessionId", splitInstallInternalSessionState.sessionId());
        intent.putParcelableArrayListExtra("downloadRequests", (ArrayList) list);
        intent.putExtra("realTotalBytesNeedToDownload", j);
        intent.putStringArrayListExtra("moduleNames", (ArrayList) splitInstallInternalSessionState.moduleNames());
        intent.setClass(this.appContext, this.obtainUserConfirmationActivityClass);
        splitInstallInternalSessionState.setUserConfirmationIntent(PendingIntent.getActivity(this.appContext, 0, intent, Box.MAX_BOX_SIZE));
        this.sessionManager.changeSessionState(splitInstallInternalSessionState.sessionId(), 8);
        this.sessionManager.emitSessionState(splitInstallInternalSessionState);
    }

    public void cancelInstall(int i, Callback callback) {
        Object[] objArr = {Integer.valueOf(i)};
        String str = TAG;
        SplitLog.i(str, "start to cancel session id %d installation", objArr);
        SplitInstallInternalSessionState sessionState = this.sessionManager.getSessionState(i);
        if (sessionState == null) {
            SplitLog.i(str, "Session id is not found!", new Object[0]);
            callback.onError(SplitInstallSupervisor.bundleErrorCode(-4));
            return;
        }
        if (sessionState.status() == 1 || sessionState.status() == 2) {
            boolean cancelDownloadSync = this.userDownloader.cancelDownloadSync(i);
            StringBuilder sb = new StringBuilder();
            sb.append("result of cancel request : ");
            sb.append(cancelDownloadSync);
            SplitLog.d(str, sb.toString(), new Object[0]);
            if (cancelDownloadSync) {
                callback.onCancelInstall(i, null);
            }
        }
        callback.onError(SplitInstallSupervisor.bundleErrorCode(-3));
    }

    public boolean cancelInstallWithoutUserConfirmation(int i) {
        SplitInstallInternalSessionState sessionState = this.sessionManager.getSessionState(i);
        if (sessionState == null) {
            return false;
        }
        this.sessionManager.changeSessionState(sessionState.sessionId(), 7);
        this.sessionManager.emitSessionState(sessionState);
        return true;
    }

    public boolean continueInstallWithUserConfirmation(int i) {
        SplitInstallInternalSessionState sessionState = this.sessionManager.getSessionState(i);
        if (sessionState == null) {
            return false;
        }
        StartDownloadCallback startDownloadCallback = new StartDownloadCallback(this.splitInstaller, i, this.sessionManager, sessionState.needInstalledSplits);
        this.sessionManager.changeSessionState(i, 1);
        this.sessionManager.emitSessionState(sessionState);
        this.userDownloader.startDownload(sessionState.sessionId(), sessionState.downloadRequests, startDownloadCallback);
        return true;
    }

    public void deferredInstall(List list, Callback callback) {
        List unBundleModuleNames = SplitInstallSupervisor.unBundleModuleNames(list);
        int onPreInstallSplits = onPreInstallSplits(unBundleModuleNames);
        if (onPreInstallSplits != 0) {
            callback.onError(SplitInstallSupervisor.bundleErrorCode(onPreInstallSplits));
        } else if (getInstalledSplitForAAB().isEmpty()) {
            deferredDownloadSplits(getNeed2BeInstalledSplits(unBundleModuleNames), callback);
        } else if (getInstalledSplitForAAB().containsAll(unBundleModuleNames)) {
            callback.onDeferredInstall(null);
        }
    }

    public void deferredUninstall(List list, Callback callback) {
        int i;
        Bundle bundleErrorCode;
        if (!getInstalledSplitForAAB().isEmpty()) {
            i = -98;
        } else {
            List unBundleModuleNames = SplitInstallSupervisor.unBundleModuleNames(list);
            int checkInternalErrorCode = checkInternalErrorCode();
            if (checkInternalErrorCode != 0) {
                bundleErrorCode = SplitInstallSupervisor.bundleErrorCode(checkInternalErrorCode);
                callback.onError(bundleErrorCode);
            } else if (isRequestInvalid(unBundleModuleNames)) {
                i = -3;
            } else {
                boolean recordPendingUninstallSplits = new SplitPendingUninstallManager().recordPendingUninstallSplits(unBundleModuleNames);
                String str = TAG;
                if (recordPendingUninstallSplits) {
                    SplitLog.w(str, "Succeed to record pending uninstall splits %s!", unBundleModuleNames.toString());
                    callback.onDeferredUninstall(null);
                } else {
                    SplitLog.w(str, "Failed to record pending uninstall splits!", new Object[0]);
                    callback.onError(SplitInstallSupervisor.bundleErrorCode(-100));
                }
                return;
            }
        }
        bundleErrorCode = SplitInstallSupervisor.bundleErrorCode(i);
        callback.onError(bundleErrorCode);
    }

    public void getSessionState(int i, Callback callback) {
        SplitInstallInternalSessionState sessionState = this.sessionManager.getSessionState(i);
        if (sessionState == null) {
            callback.onError(SplitInstallSupervisor.bundleErrorCode(-4));
        } else {
            callback.onGetSession(i, SplitInstallInternalSessionState.transform2Bundle(sessionState));
        }
    }

    public void getSessionStates(Callback callback) {
        List<SplitInstallInternalSessionState> sessionStates = this.sessionManager.getSessionStates();
        if (sessionStates.isEmpty()) {
            callback.onGetSessionStates(Collections.emptyList());
            return;
        }
        ArrayList arrayList = new ArrayList(0);
        for (SplitInstallInternalSessionState transform2Bundle : sessionStates) {
            arrayList.add(SplitInstallInternalSessionState.transform2Bundle(transform2Bundle));
        }
        callback.onGetSessionStates(arrayList);
    }

    public void startInstall(List list, Callback callback) {
        List unBundleModuleNames = SplitInstallSupervisor.unBundleModuleNames(list);
        int onPreInstallSplits = onPreInstallSplits(unBundleModuleNames);
        if (onPreInstallSplits != 0) {
            callback.onError(SplitInstallSupervisor.bundleErrorCode(onPreInstallSplits));
        } else {
            List need2BeInstalledSplits = getNeed2BeInstalledSplits(unBundleModuleNames);
            if (isAllSplitsBuiltIn(need2BeInstalledSplits) || SplitInstallSupervisor.isNetworkAvailable(this.appContext)) {
                startDownloadSplits(unBundleModuleNames, need2BeInstalledSplits, callback);
            } else {
                callback.onError(SplitInstallSupervisor.bundleErrorCode(-6));
            }
        }
    }
}
