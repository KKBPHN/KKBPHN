package com.xiaomi.mi_connect_sdk.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.RemoteException;
import com.xiaomi.mi_connect_sdk.util.LogUtil;
import com.xiaomi.mi_connect_service.IIPCDataCallback;
import com.xiaomi.mi_connect_service.IMiConnect;
import com.xiaomi.mi_connect_service.IMiConnectCallback;
import com.xiaomi.mi_connect_service.IMiConnectCallback.Stub;
import java.util.ArrayList;
import java.util.List;

public class DefaultMiApp implements InnerMiApp {
    private static final String TAG = "DefaultMiApp";
    /* access modifiers changed from: private */
    public List commandList = new ArrayList();
    /* access modifiers changed from: private */
    public volatile boolean isAdvertising = false;
    /* access modifiers changed from: private */
    public volatile boolean isDestroying = false;
    /* access modifiers changed from: private */
    public volatile boolean isDiscovering = false;
    private int mAppId;
    /* access modifiers changed from: private */
    public IBinder mBinder = null;
    private IMiConnectCallback mCallback = new Stub() {
        public void onAdvertisingResult(final int i, final int i2) {
            if (i2 == ResultCode.START_ADVERTISING_SUCCESS.getCode()) {
                DefaultMiApp.this.isAdvertising = true;
            }
            if (i2 == ResultCode.STOP_ADVERTISING_SUCCESS.getCode()) {
                DefaultMiApp.this.isAdvertising = false;
            }
            DefaultMiApp.this.uiHandler.post(new Runnable() {
                public void run() {
                    DefaultMiApp.this.miAppCallback.onAdvertingResult(i, i2);
                }
            });
        }

        public void onConnectionInitiated(int i, int i2, String str, byte[] bArr, byte[] bArr2) {
            Handler access$100 = DefaultMiApp.this.uiHandler;
            final int i3 = i;
            final int i4 = i2;
            final String str2 = str;
            final byte[] bArr3 = bArr;
            final byte[] bArr4 = bArr2;
            AnonymousClass5 r1 = new Runnable() {
                public void run() {
                    MiAppCallback miAppCallback = DefaultMiApp.this.miAppCallback;
                    int i = i3;
                    int i2 = i4;
                    String str = str2;
                    if (str == null) {
                        str = "";
                    }
                    String str2 = str;
                    byte[] bArr = bArr3;
                    if (bArr == null) {
                        bArr = new byte[0];
                    }
                    byte[] bArr2 = bArr4;
                    if (bArr2 == null) {
                        bArr2 = new byte[0];
                    }
                    miAppCallback.onConnectionInitiated(i, i2, str2, bArr, bArr2);
                }
            };
            access$100.post(r1);
        }

        public void onConnectionResult(int i, int i2, String str, int i3) {
            Handler access$100 = DefaultMiApp.this.uiHandler;
            final int i4 = i;
            final int i5 = i2;
            final String str2 = str;
            final int i6 = i3;
            AnonymousClass6 r1 = new Runnable() {
                public void run() {
                    DefaultMiApp.this.miAppCallback.onConnectionResult(i4, i5, str2, i6);
                }
            };
            access$100.post(r1);
        }

        public void onDisconnection(final int i, final int i2) {
            DefaultMiApp.this.uiHandler.post(new Runnable() {
                public void run() {
                    DefaultMiApp.this.miAppCallback.onDisconnection(i, i2);
                }
            });
        }

        public void onDiscoveryResult(final int i, final int i2) {
            if (i2 == ResultCode.START_DISCOVERY_SUCCESS.getCode()) {
                DefaultMiApp.this.isDiscovering = true;
            }
            if (i2 == ResultCode.STOP_DISCOVERY_SUCCESS.getCode()) {
                DefaultMiApp.this.isDiscovering = false;
            }
            DefaultMiApp.this.uiHandler.post(new Runnable() {
                public void run() {
                    DefaultMiApp.this.miAppCallback.onDiscoveryResult(i, i2);
                }
            });
        }

        public void onEndpointFound(int i, int i2, String str, byte[] bArr) {
            LogUtil.d(DefaultMiApp.TAG, "onEndpointFound: manager", new Object[0]);
            Handler access$100 = DefaultMiApp.this.uiHandler;
            final int i3 = i;
            final int i4 = i2;
            final String str2 = str;
            final byte[] bArr2 = bArr;
            AnonymousClass3 r1 = new Runnable() {
                public void run() {
                    MiAppCallback miAppCallback = DefaultMiApp.this.miAppCallback;
                    int i = i3;
                    int i2 = i4;
                    String str = str2;
                    if (str == null) {
                        str = "";
                    }
                    byte[] bArr = bArr2;
                    if (bArr == null) {
                        bArr = new byte[0];
                    }
                    miAppCallback.onEndpointFound(i, i2, str, bArr);
                }
            };
            access$100.post(r1);
        }

        public void onEndpointLost(final int i, final int i2, final String str) {
            LogUtil.d(DefaultMiApp.TAG, "onEndpointLost: manager", new Object[0]);
            DefaultMiApp.this.uiHandler.post(new Runnable() {
                public void run() {
                    MiAppCallback miAppCallback = DefaultMiApp.this.miAppCallback;
                    int i = i;
                    int i2 = i2;
                    String str = str;
                    if (str == null) {
                        str = "";
                    }
                    miAppCallback.onEndpointLost(i, i2, str);
                }
            });
        }

        public void onPayloadReceived(final int i, final int i2, final byte[] bArr) {
            DefaultMiApp.this.uiHandler.post(new Runnable() {
                public void run() {
                    MiAppCallback miAppCallback = DefaultMiApp.this.miAppCallback;
                    int i = i;
                    int i2 = i2;
                    byte[] bArr = bArr;
                    if (bArr == null) {
                        bArr = new byte[0];
                    }
                    miAppCallback.onPayloadReceived(i, i2, bArr);
                }
            });
        }

        public void onPayloadSentResult(final int i, final int i2, final int i3) {
            DefaultMiApp.this.uiHandler.post(new Runnable() {
                public void run() {
                    DefaultMiApp.this.miAppCallback.onPayloadSentResult(i, i2, i3);
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public ServiceConnection mConnection = new ServiceConnection() {
        public void onBindingDied(ComponentName componentName) {
            DefaultMiApp.this.mService = null;
            LogUtil.d(DefaultMiApp.TAG, "onBindingDied", new Object[0]);
        }

        public void onNullBinding(ComponentName componentName) {
            DefaultMiApp.this.mService = null;
            LogUtil.d(DefaultMiApp.TAG, "onNullBinding", new Object[0]);
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (DefaultMiApp.this.isDestroying) {
                DefaultMiApp defaultMiApp = DefaultMiApp.this;
                defaultMiApp.mContext.unbindService(defaultMiApp.mConnection);
                return;
            }
            DefaultMiApp.this.mService = IMiConnect.Stub.asInterface(iBinder);
            DefaultMiApp.this.mBinder = iBinder;
            int i = 0;
            Object[] objArr = new Object[0];
            String str = DefaultMiApp.TAG;
            LogUtil.d(str, "onServiceConnected", objArr);
            try {
                iBinder.linkToDeath(DefaultMiApp.this.mDeathRecipient, 0);
                DefaultMiApp.this.serviceApiVersion = DefaultMiApp.this.mService.getServiceApiVersion();
                StringBuilder sb = new StringBuilder();
                sb.append("serviceApiVersion :");
                sb.append(DefaultMiApp.this.serviceApiVersion);
                LogUtil.i(str, sb.toString(), new Object[0]);
                if (DefaultMiApp.this.serviceApiVersion < 2) {
                    DefaultMiApp.this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
                    return;
                }
                DefaultMiApp.this.onServiceConnected();
                DefaultMiApp.this.miAppCallback.onServiceBind();
                List access$800 = DefaultMiApp.this.commandList;
                int size = access$800.size();
                while (size > 0 && i < size) {
                    ((Command) access$800.get(i)).execute();
                    i++;
                }
                access$800.clear();
            } catch (RemoteException e) {
                e.printStackTrace();
                DefaultMiApp.this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            LogUtil.d(DefaultMiApp.TAG, "onServiceDisconnected", new Object[0]);
            DefaultMiApp.this.unBindService();
        }
    };
    protected Context mContext;
    /* access modifiers changed from: private */
    public DeathRecipient mDeathRecipient = new DeathRecipient() {
        public void binderDied() {
            LogUtil.d(DefaultMiApp.TAG, "binderDied", new Object[0]);
            DefaultMiApp.this.uiHandler.post(new Runnable() {
                public void run() {
                    DefaultMiApp.this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
                }
            });
        }
    };
    /* access modifiers changed from: protected */
    public IMiConnect mService = null;
    /* access modifiers changed from: protected */
    public MiAppCallback miAppCallback;
    /* access modifiers changed from: private */
    public int serviceApiVersion;
    /* access modifiers changed from: private */
    public Handler uiHandler = new Handler(Looper.getMainLooper());

    class Command {
        private Runnable worker;

        Command(Runnable runnable) {
            this.worker = runnable;
        }

        /* access modifiers changed from: 0000 */
        public void execute() {
            this.worker.run();
        }
    }

    protected DefaultMiApp(Context context, MiAppCallback miAppCallback2, int i) {
        if (context == null || miAppCallback2 == null) {
            throw new IllegalArgumentException("context or callback can not be null");
        }
        this.mContext = context;
        this.miAppCallback = miAppCallback2;
        this.mAppId = i;
        startService();
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent();
        String str = "com.xiaomi.mi_connect_service.MiConnectService";
        intent.setAction(str);
        intent.setComponent(new ComponentName("com.xiaomi.mi_connect_service", str));
        if (!this.mContext.bindService(intent, this.mConnection, 1)) {
            LogUtil.e(TAG, "bindService failed", new Object[0]);
        }
    }

    private boolean requestServiceFromOtherApp(int i, AppConfig appConfig) {
        if (appConfig.getDiscAppIds().length < 1) {
            return false;
        }
        return (appConfig.getDiscAppIds().length == 1 && appConfig.getDiscAppIds()[0] == i) ? false : true;
    }

    private void startService() {
        LogUtil.i(TAG, "-startService-", new Object[0]);
        Intent intent = new Intent();
        String str = "com.xiaomi.mi_connect_service.MiConnectService";
        intent.setAction(str);
        intent.setComponent(new ComponentName("com.xiaomi.mi_connect_service", str));
        this.mContext.startService(intent);
    }

    /* access modifiers changed from: private */
    public void unBindService() {
        if (serviceAvailable()) {
            this.mBinder.unlinkToDeath(this.mDeathRecipient, 0);
            this.mContext.unbindService(this.mConnection);
            this.mService = null;
            MiAppCallback miAppCallback2 = this.miAppCallback;
            if (miAppCallback2 != null) {
                miAppCallback2.onServiceUnbind();
            }
        }
    }

    public void acceptConnection(ConnectionConfig connectionConfig) {
        if (serviceAvailable() && this.serviceApiVersion >= 2) {
            try {
                this.mService.acceptConnection(this.mAppId, connectionConfig.getRoleType(), connectionConfig.getEndPointId(), connectionConfig.isEndPointTrusted());
            } catch (RemoteException e) {
                e.printStackTrace();
                this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            }
            return;
        }
        this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
    }

    public void destroy(int i) {
        doDestroy();
        this.isDestroying = true;
        if (serviceAvailable()) {
            try {
                this.mService.destroy(this.mAppId, i);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unBindService();
        }
    }

    public byte[] deviceInfoIDM() {
        LogUtil.d(TAG, "deviceInfoIDM", new Object[0]);
        byte[] bArr = null;
        if (serviceAvailable() && this.serviceApiVersion >= 5) {
            try {
                bArr = this.mService.deviceInfoIDM();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return bArr;
        }
        this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
        return null;
    }

    public void disconnectFromEndPoint(ConnectionConfig connectionConfig) {
        if (serviceAvailable() && this.serviceApiVersion >= 2) {
            try {
                this.mService.disconnectFromEndPoint(this.mAppId, connectionConfig.getRoleType(), connectionConfig.getEndPointId());
            } catch (RemoteException e) {
                e.printStackTrace();
                this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            }
            return;
        }
        this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
    }

    /* access modifiers changed from: protected */
    public void doDestroy() {
    }

    public byte[] getIdHash() {
        LogUtil.d(TAG, "getIdHash", new Object[0]);
        byte[] bArr = null;
        if (serviceAvailable() && this.serviceApiVersion >= 2) {
            try {
                bArr = this.mService.getIdHash();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return bArr;
        }
        this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
        return null;
    }

    /* access modifiers changed from: protected */
    public void onServiceConnected() {
    }

    public int publish(String str, String str2, byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("publish resource: ");
        sb.append(str);
        sb.append(" did: ");
        sb.append(str2);
        String sb2 = sb.toString();
        Object[] objArr = new Object[0];
        String str3 = TAG;
        LogUtil.i(str3, sb2, objArr);
        if (!serviceAvailable()) {
            this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            return -1;
        }
        try {
            return this.mService.publish(this.mAppId, str, str2, bArr);
        } catch (RemoteException e) {
            LogUtil.e(str3, "", (Throwable) e);
            return 0;
        }
    }

    public void rejectConnection(ConnectionConfig connectionConfig) {
        if (serviceAvailable() && this.serviceApiVersion >= 2) {
            try {
                this.mService.rejectConnection(this.mAppId, connectionConfig.getRoleType(), connectionConfig.getEndPointId());
            } catch (RemoteException e) {
                e.printStackTrace();
                this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            }
            return;
        }
        this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
    }

    public void requestConnection(ConnectionConfig connectionConfig) {
        if (serviceAvailable() && this.serviceApiVersion >= 2) {
            try {
                this.mService.requestConnection(this.mAppId, connectionConfig.getEndPointId(), connectionConfig.getCommData());
            } catch (RemoteException e) {
                e.printStackTrace();
                this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            }
            return;
        }
        this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
    }

    public void sendPayload(PayloadConfig payloadConfig) {
        if (serviceAvailable() && this.serviceApiVersion >= 2) {
            try {
                this.mService.sendPayload(this.mAppId, payloadConfig.getRoleType(), payloadConfig.getEndPointId(), payloadConfig.getPayload());
            } catch (RemoteException e) {
                e.printStackTrace();
                this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            }
            return;
        }
        this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
    }

    /* access modifiers changed from: protected */
    public boolean serviceAvailable() {
        IMiConnect iMiConnect = this.mService;
        return iMiConnect != null && iMiConnect.asBinder().isBinderAlive();
    }

    public int setIPCDataCallback(String str, IIPCDataCallback iIPCDataCallback) {
        Object[] objArr = new Object[0];
        String str2 = TAG;
        LogUtil.i(str2, "setIPCDataCallback", objArr);
        if (!serviceAvailable()) {
            this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            return -1;
        }
        try {
            return this.mService.setIPCDataCallback(this.mAppId, str, iIPCDataCallback);
        } catch (RemoteException e) {
            LogUtil.e(str2, "", (Throwable) e);
            return -1;
        }
    }

    public void startAdvertising(final AppConfig appConfig) {
        if (!this.isDestroying) {
            if (!serviceAvailable()) {
                bindService();
                this.commandList.add(new Command(new Runnable() {
                    public void run() {
                        DefaultMiApp.this.startAdvertising(appConfig);
                    }
                }));
            } else if (this.serviceApiVersion < 2) {
                this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            } else {
                try {
                    this.mService.setCallback(this.mAppId, 1, this.mCallback);
                    this.mService.startAdvertising(this.mAppId, appConfig.getAdvData(), appConfig.getDiscType(), appConfig.getAppCommType(), appConfig.getCommData());
                } catch (RemoteException e) {
                    e.printStackTrace();
                    this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
                }
            }
        }
    }

    public void startDiscovery(AppConfig appConfig) {
        final AppConfig appConfig2 = appConfig;
        if (!this.isDestroying) {
            if (!serviceAvailable()) {
                bindService();
                this.commandList.add(new Command(new Runnable() {
                    public void run() {
                        DefaultMiApp.this.startDiscovery(appConfig2);
                    }
                }));
            } else if (this.serviceApiVersion < 2) {
                this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            } else {
                try {
                    this.mService.setCallback(this.mAppId, 2, this.mCallback);
                    if (!requestServiceFromOtherApp(this.mAppId, appConfig2) || this.serviceApiVersion <= 3) {
                        this.mService.startDiscovery(this.mAppId, appConfig.getCommData(), appConfig.getDiscType(), appConfig.getAppCommType(), appConfig.getAppCommDataType());
                    } else {
                        this.mService.startDiscoveryV2(this.mAppId, appConfig.getCommData(), appConfig.getDiscType(), appConfig.getAppCommType(), appConfig.getAppCommDataType(), appConfig.getDiscAppIds());
                    }
                } catch (RemoteException unused) {
                    this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
                }
            }
        }
    }

    public void stopAdvertising() {
        if (!serviceAvailable()) {
            if (this.isAdvertising) {
                LogUtil.e(TAG, "service unbind but advertising", new Object[0]);
                this.miAppCallback.onServiceError(ResultCode.STOP_ADVERTIDING_ERROR.getCode());
            } else {
                this.miAppCallback.onAdvertingResult(this.mAppId, ResultCode.STOP_ADVERTISING_SUCCESS.getCode());
            }
        } else if (this.serviceApiVersion < 2) {
            this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
        } else {
            try {
                this.mService.stopAdvertising(this.mAppId);
            } catch (RemoteException unused) {
                this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            }
        }
    }

    public void stopDiscovery() {
        if (!serviceAvailable()) {
            if (this.isDiscovering) {
                LogUtil.e(TAG, "service unbind but discovering", new Object[0]);
                this.miAppCallback.onServiceError(ResultCode.STOP_DISCOVERY_ERROR.getCode());
            } else {
                this.miAppCallback.onDiscoveryResult(this.mAppId, ResultCode.STOP_DISCOVERY_SUCCESS.getCode());
            }
        } else if (this.serviceApiVersion < 2) {
            this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
        } else {
            try {
                this.mService.stopDiscovery(this.mAppId);
            } catch (RemoteException e) {
                e.printStackTrace();
                this.miAppCallback.onServiceError(ResultCode.SERVICE_ERROR.getCode());
            }
        }
    }
}
