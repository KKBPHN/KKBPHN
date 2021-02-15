package com.google.android.play.core.listener;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.google.android.play.core.splitcompat.util.PlayCore;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class StateUpdateListenerRegister {
    private final Context context;
    private final IntentFilter intentFilter;
    private final Object mLock = new Object();
    private final Set mStateUpdatedListeners = Collections.newSetFromMap(new ConcurrentHashMap());
    protected final PlayCore playCore;
    private final StateUpdatedReceiver receiver = new StateUpdatedReceiver(this);

    protected StateUpdateListenerRegister(PlayCore playCore2, IntentFilter intentFilter2, Context context2) {
        this.playCore = playCore2;
        this.intentFilter = intentFilter2;
        this.context = context2;
    }

    public final void notifyListeners(Object obj) {
        for (StateUpdatedListener onStateUpdate : this.mStateUpdatedListeners) {
            onStateUpdate.onStateUpdate(obj);
        }
    }

    public abstract void onReceived(Intent intent);

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0038, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void registerListener(StateUpdatedListener stateUpdatedListener) {
        synchronized (this.mLock) {
            this.playCore.debug("registerListener", new Object[0]);
            if (this.mStateUpdatedListeners.contains(stateUpdatedListener)) {
                this.playCore.debug("listener has been registered!", new Object[0]);
                return;
            }
            this.mStateUpdatedListeners.add(stateUpdatedListener);
            if (this.mStateUpdatedListeners.size() == 1) {
                this.context.registerReceiver(this.receiver, this.intentFilter);
            }
        }
    }

    public final void unregisterListener(StateUpdatedListener stateUpdatedListener) {
        synchronized (this.mLock) {
            this.playCore.debug("unregisterListener", new Object[0]);
            boolean remove = this.mStateUpdatedListeners.remove(stateUpdatedListener);
            if (this.mStateUpdatedListeners.isEmpty() && remove) {
                try {
                    this.context.unregisterReceiver(this.receiver);
                } catch (IllegalArgumentException e) {
                    PlayCore playCore2 = this.playCore;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Receiver not registered: ");
                    sb.append(this.intentFilter.getAction(0));
                    playCore2.error(e, sb.toString(), new Object[0]);
                }
            }
        }
    }
}
