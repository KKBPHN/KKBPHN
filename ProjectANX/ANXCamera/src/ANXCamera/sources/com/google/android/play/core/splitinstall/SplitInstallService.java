package com.google.android.play.core.splitinstall;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import com.google.android.play.core.remote.RemoteManager;
import com.google.android.play.core.splitcompat.util.PlayCore;
import com.google.android.play.core.tasks.Task;
import com.google.android.play.core.tasks.TaskWrapper;
import com.ss.android.ugc.effectmanager.common.ErrorConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class SplitInstallService {
    static final PlayCore playCore = new PlayCore(SplitInstallService.class.getSimpleName());
    private final Context mContext;
    final String mPackageName;
    final RemoteManager mSplitRemoteManager;

    SplitInstallService(Context context) {
        this(context, context.getPackageName());
    }

    private SplitInstallService(Context context, String str) {
        OnBinderDiedListenerImpl onBinderDiedListenerImpl = new OnBinderDiedListenerImpl(this);
        this.mContext = context;
        this.mPackageName = str;
        String str2 = "SplitInstallService";
        RemoteManager remoteManager = new RemoteManager(context.getApplicationContext(), playCore, str2, new Intent("com.iqiyi.android.play.core.splitinstall.BIND_SPLIT_INSTALL_SERVICE").setPackage(str), SplitRemoteImpl.sInstance, onBinderDiedListenerImpl);
        this.mSplitRemoteManager = remoteManager;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<java.lang.String>, for r4v0, types: [java.util.Collection, java.util.Collection<java.lang.String>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static List wrapModuleNames(Collection<String> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        for (String str : collection) {
            Bundle bundle = new Bundle();
            bundle.putString("module_name", str);
            arrayList.add(bundle);
        }
        return arrayList;
    }

    static Bundle wrapVersionCode() {
        Bundle bundle = new Bundle();
        bundle.putInt("playcore_version_code", ErrorConstants.CODE_MD5_ERROR);
        return bundle;
    }

    /* access modifiers changed from: 0000 */
    public Task cancelInstall(int i) {
        playCore.info("cancelInstall(%d)", Integer.valueOf(i));
        TaskWrapper taskWrapper = new TaskWrapper();
        this.mSplitRemoteManager.bindService(new CancelInstallTask(this, taskWrapper, i, taskWrapper));
        return taskWrapper.getTask();
    }

    /* access modifiers changed from: 0000 */
    public Task deferredInstall(List list) {
        playCore.info("deferredInstall(%s)", list);
        TaskWrapper taskWrapper = new TaskWrapper();
        this.mSplitRemoteManager.bindService(new DeferredInstallTask(this, taskWrapper, list, taskWrapper));
        return taskWrapper.getTask();
    }

    /* access modifiers changed from: 0000 */
    public Task deferredUninstall(List list) {
        playCore.info("deferredUninstall(%s)", list);
        TaskWrapper taskWrapper = new TaskWrapper();
        this.mSplitRemoteManager.bindService(new DeferredUninstallTask(this, taskWrapper, list, taskWrapper));
        return taskWrapper.getTask();
    }

    /* access modifiers changed from: 0000 */
    public Task getSessionState(int i) {
        playCore.info("getSessionState(%d)", Integer.valueOf(i));
        TaskWrapper taskWrapper = new TaskWrapper();
        this.mSplitRemoteManager.bindService(new GetSessionStateTask(this, taskWrapper, i, taskWrapper));
        return taskWrapper.getTask();
    }

    /* access modifiers changed from: 0000 */
    public Task getSessionStates() {
        playCore.info("getSessionStates", new Object[0]);
        TaskWrapper taskWrapper = new TaskWrapper();
        this.mSplitRemoteManager.bindService(new GetSessionStatesTask(this, taskWrapper, taskWrapper));
        return taskWrapper.getTask();
    }

    /* access modifiers changed from: 0000 */
    public void onBinderDied() {
        playCore.info("onBinderDied", new Object[0]);
        Bundle bundle = new Bundle();
        bundle.putInt("session_id", -1);
        bundle.putInt("status", 6);
        bundle.putInt("error_code", -9);
        Intent intent = new Intent();
        intent.setPackage(this.mPackageName);
        intent.setAction("com.google.android.play.core.splitinstall.receiver.SplitInstallUpdateIntentService");
        intent.putExtra("session_state", bundle);
        intent.addFlags(1073741824);
        if (VERSION.SDK_INT >= 26) {
            intent.addFlags(2097152);
        }
        this.mContext.sendBroadcast(intent);
    }

    /* access modifiers changed from: 0000 */
    public Task startInstall(List list) {
        playCore.info("startInstall(%s)", list);
        TaskWrapper taskWrapper = new TaskWrapper();
        this.mSplitRemoteManager.bindService(new StartInstallTask(this, taskWrapper, list, taskWrapper));
        return taskWrapper.getTask();
    }
}
