package com.google.android.play.core.remote;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder.DeathRecipient;
import android.os.IInterface;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.google.android.play.core.splitcompat.util.PlayCore;
import com.google.android.play.core.tasks.TaskWrapper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class RemoteManager {
    private static final Map sHandlerMap = Collections.synchronizedMap(new HashMap());
    boolean mBindingService;
    final Context mContext;
    private final DeathRecipient mDeathRecipient = new DeathRecipientImpl(this);
    IInterface mIInterface;
    private final String mKey;
    private final WeakReference mOnBinderDiedListenerWkRef;
    final List mPendingTasks = new ArrayList();
    final PlayCore mPlayCore;
    final IRemote mRemote;
    ServiceConnection mServiceConnection;
    private final Intent mSplitInstallServiceIntent;

    public RemoteManager(Context context, PlayCore playCore, String str, Intent intent, IRemote iRemote, OnBinderDiedListener onBinderDiedListener) {
        this.mContext = context;
        this.mPlayCore = playCore;
        this.mKey = str;
        this.mSplitInstallServiceIntent = intent;
        this.mRemote = iRemote;
        this.mOnBinderDiedListenerWkRef = new WeakReference(onBinderDiedListener);
    }

    private Handler getHandler() {
        Handler handler;
        synchronized (sHandlerMap) {
            if (!sHandlerMap.containsKey(this.mKey)) {
                HandlerThread handlerThread = new HandlerThread(this.mKey, 10);
                handlerThread.start();
                sHandlerMap.put(this.mKey, new Handler(handlerThread.getLooper()));
            }
            handler = (Handler) sHandlerMap.get(this.mKey);
        }
        return handler;
    }

    public void bindService(RemoteTask remoteTask) {
        post(new BindServiceTask(this, remoteTask));
    }

    /* access modifiers changed from: 0000 */
    public void bindServiceInternal(RemoteTask remoteTask) {
        if (this.mIInterface == null && !this.mBindingService) {
            this.mPlayCore.info("Initiate binding to the service.", new Object[0]);
            this.mPendingTasks.add(remoteTask);
            this.mServiceConnection = new ServiceConnectionImpl(this);
            this.mBindingService = true;
            if (!this.mContext.bindService(this.mSplitInstallServiceIntent, this.mServiceConnection, 1)) {
                this.mPlayCore.info("Failed to bind to the service.", new Object[0]);
                this.mBindingService = false;
                for (RemoteTask task : this.mPendingTasks) {
                    TaskWrapper task2 = task.getTask();
                    if (task2 != null) {
                        task2.setException(new RemoteServiceException());
                    }
                }
                this.mPendingTasks.clear();
            }
        } else if (this.mBindingService) {
            this.mPlayCore.info("Waiting to bind to the service.", new Object[0]);
            this.mPendingTasks.add(remoteTask);
        } else {
            remoteTask.run();
        }
    }

    public IInterface getIInterface() {
        return this.mIInterface;
    }

    /* access modifiers changed from: 0000 */
    public void linkToDeath() {
        this.mPlayCore.info("linkToDeath", new Object[0]);
        try {
            this.mIInterface.asBinder().linkToDeath(this.mDeathRecipient, 0);
        } catch (Throwable unused) {
            this.mPlayCore.info("linkToDeath failed", new Object[0]);
        }
    }

    /* access modifiers changed from: 0000 */
    public void post(RemoteTask remoteTask) {
        getHandler().post(remoteTask);
    }

    /* access modifiers changed from: 0000 */
    public void reportBinderDeath() {
        this.mPlayCore.info("reportBinderDeath", new Object[0]);
        OnBinderDiedListener onBinderDiedListener = (OnBinderDiedListener) this.mOnBinderDiedListenerWkRef.get();
        if (onBinderDiedListener != null) {
            this.mPlayCore.info("calling onBinderDied", new Object[0]);
            onBinderDiedListener.onBinderDied();
        }
    }

    public void unbindService() {
        post(new UnbindServiceTask(this));
    }

    /* access modifiers changed from: 0000 */
    public void unlinkToDeath() {
        this.mPlayCore.info("unlinkToDeath", new Object[0]);
        this.mIInterface.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
    }
}
