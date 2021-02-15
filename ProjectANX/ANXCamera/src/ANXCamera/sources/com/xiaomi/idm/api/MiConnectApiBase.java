package com.xiaomi.idm.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.xiaomi.mi_connect_sdk.api.MiAppCallback;
import com.xiaomi.mi_connect_sdk.api.ResultCode;
import com.xiaomi.mi_connect_sdk.util.LogUtil;
import com.xiaomi.mi_connect_service.IMiConnect;
import com.xiaomi.mi_connect_service.IMiConnect.Stub;

public class MiConnectApiBase {
    private static final String TAG = "MiConnectApi";
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Object[] objArr = new Object[0];
            String str = MiConnectApiBase.TAG;
            LogUtil.d(str, "onServiceConnected", objArr);
            MiConnectApiBase.this.mService = Stub.asInterface(iBinder);
            if (MiConnectApiBase.this.mPendingUnbind) {
                MiConnectApiBase.this.unbindService();
                return;
            }
            MiConnectApiBase.this.mPendingUnbind = false;
            int i = -1;
            try {
                i = MiConnectApiBase.this.mService.getServiceApiVersion();
            } catch (RemoteException e) {
                LogUtil.e(str, e.getMessage(), (Throwable) e);
            }
            MiConnectApiBase miConnectApiBase = MiConnectApiBase.this;
            MiAppCallback miAppCallback = miConnectApiBase.mMiAppCallback;
            if (miAppCallback != null) {
                if (i < 2) {
                    miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
                } else {
                    miConnectApiBase.onServiceConnected();
                    MiConnectApiBase.this.mMiAppCallback.onServiceBind();
                }
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            LogUtil.d(MiConnectApiBase.TAG, "onServiceDisconnected", new Object[0]);
            MiConnectApiBase miConnectApiBase = MiConnectApiBase.this;
            miConnectApiBase.mService = null;
            MiAppCallback miAppCallback = miConnectApiBase.mMiAppCallback;
            if (miAppCallback != null) {
                miAppCallback.onServiceUnbind();
            }
        }
    };
    protected Context mContext;
    protected MiAppCallback mMiAppCallback;
    /* access modifiers changed from: private */
    public boolean mPendingUnbind;
    protected IMiConnect mService;

    protected MiConnectApiBase(Context context, MiAppCallback miAppCallback) {
        if (context != null) {
            this.mContext = context;
            this.mMiAppCallback = miAppCallback;
            doInit();
            bindService();
            return;
        }
        throw new IllegalArgumentException("context should not be null");
    }

    private void bindService() {
        Intent intent = new Intent();
        String str = "com.xiaomi.mi_connect_service.MiConnectService";
        intent.setAction(str);
        intent.setComponent(new ComponentName("com.xiaomi.mi_connect_service", str));
        this.mContext.startService(intent);
        if (!this.mContext.bindService(intent, this.mConnection, 1)) {
            LogUtil.e(TAG, "bindService failed", new Object[0]);
        }
    }

    /* access modifiers changed from: private */
    public void unbindService() {
        this.mPendingUnbind = true;
        if (serviceAvailable()) {
            this.mContext.unbindService(this.mConnection);
            this.mService = null;
            MiAppCallback miAppCallback = this.mMiAppCallback;
            if (miAppCallback != null) {
                miAppCallback.onServiceUnbind();
            }
        }
    }

    public void destroy() {
        doDestroy();
        unbindService();
    }

    /* access modifiers changed from: protected */
    public void doDestroy() {
    }

    /* access modifiers changed from: protected */
    public void doInit() {
    }

    /* access modifiers changed from: protected */
    public void onServiceConnected() {
    }

    /* access modifiers changed from: protected */
    public boolean serviceAvailable() {
        IMiConnect iMiConnect = this.mService;
        return iMiConnect != null && iMiConnect.asBinder().isBinderAlive();
    }
}
