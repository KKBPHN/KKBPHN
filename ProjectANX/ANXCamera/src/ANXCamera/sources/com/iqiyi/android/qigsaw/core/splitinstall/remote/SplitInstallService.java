package com.iqiyi.android.qigsaw.core.splitinstall.remote;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallService.Stub;
import com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class SplitInstallService extends Service {
    private static final Map sHandlerMap = Collections.synchronizedMap(new HashMap());
    Stub mBinder = new Stub() {
        public void cancelInstall(String str, int i, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
            SplitInstallService.getHandler(str).post(new OnCancelInstallTask(iSplitInstallServiceCallback, i));
        }

        public void deferredInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
            SplitInstallService.getHandler(str).post(new OnDeferredInstallTask(iSplitInstallServiceCallback, list));
        }

        public void deferredUninstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
            SplitInstallService.getHandler(str).post(new OnDeferredUninstallTask(iSplitInstallServiceCallback, list));
        }

        public void getSessionState(String str, int i, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
            SplitInstallService.getHandler(str).post(new OnGetSessionStateTask(iSplitInstallServiceCallback, i));
        }

        public void getSessionStates(String str, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
            SplitInstallService.getHandler(str).post(new OnGetSessionStatesTask(iSplitInstallServiceCallback));
        }

        public void startInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
            SplitInstallService.getHandler(str).post(new OnStartInstallTask(iSplitInstallServiceCallback, list));
        }
    };

    static Handler getHandler(String str) {
        Handler handler;
        synchronized (sHandlerMap) {
            if (!sHandlerMap.containsKey(str)) {
                StringBuilder sb = new StringBuilder();
                sb.append("split_remote_");
                sb.append(str);
                HandlerThread handlerThread = new HandlerThread(sb.toString(), 10);
                handlerThread.start();
                sHandlerMap.put(str, new Handler(handlerThread.getLooper()));
            }
            handler = (Handler) sHandlerMap.get(str);
        }
        return handler;
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }
}
