package com.google.android.play.core.splitinstall;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import com.google.android.play.core.listener.StateUpdateListenerRegister;
import com.google.android.play.core.splitcompat.util.PlayCore;

final class SplitInstallListenerRegistry extends StateUpdateListenerRegister {
    private final SplitSessionLoader mLoader;
    final Handler mMainHandler;

    SplitInstallListenerRegistry(Context context) {
        this(context, SplitSessionLoaderSingleton.get());
    }

    private SplitInstallListenerRegistry(Context context, SplitSessionLoader splitSessionLoader) {
        super(new PlayCore("SplitInstallListenerRegistry"), new IntentFilter("com.iqiyi.android.play.core.splitinstall.receiver.SplitInstallUpdateIntentService"), context);
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mLoader = splitSessionLoader;
    }

    /* access modifiers changed from: protected */
    public void onReceived(Intent intent) {
        SplitInstallSessionState createFrom = SplitInstallSessionState.createFrom(intent.getBundleExtra("session_state"));
        this.playCore.info("ListenerRegistryBroadcastReceiver.onReceive: %s", createFrom);
        if (createFrom.status() == 10) {
            SplitSessionLoader splitSessionLoader = this.mLoader;
            if (splitSessionLoader != null) {
                splitSessionLoader.load(createFrom.splitFileIntents, new SplitSessionStatusChanger(this, createFrom));
                return;
            }
        }
        notifyListeners(createFrom);
    }
}
