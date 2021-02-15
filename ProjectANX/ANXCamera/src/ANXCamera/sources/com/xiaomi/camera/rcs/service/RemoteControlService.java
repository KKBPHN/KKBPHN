package com.xiaomi.camera.rcs.service;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.hardware.input.InputManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.android.camera.R;
import com.xiaomi.camera.rcs.IRemoteControl.Stub;
import com.xiaomi.camera.rcs.IRemoteControlClient;
import com.xiaomi.camera.rcs.RemoteControlContract;
import com.xiaomi.camera.rcs.RemoteControlExtension;
import com.xiaomi.camera.rcs.input.EventInjector;
import com.xiaomi.camera.rcs.streaming.StreamingServer;
import com.xiaomi.camera.rcs.streaming.StreamingStateCallback;
import com.xiaomi.camera.rcs.util.RCSDebug;
import com.xiaomi.camera.util.SystemProperties;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class RemoteControlService extends Service implements DeathRecipient {
    private static final String CAMERA_APP_PACKAGE_NAME = "com.android.camera";
    private static final String SYSTEM_APP_PACKAGE_NAME = "android";
    /* access modifiers changed from: private */
    public static final String TAG = RCSDebug.createTag(RemoteControlService.class);
    /* access modifiers changed from: private */
    public static final Object mCondVar = new Object();
    /* access modifiers changed from: private */
    public final HashMap mClients = new HashMap();
    /* access modifiers changed from: private */
    public InputManager mInputManager;
    private BinderInterface mInterface = new BinderInterface();
    /* access modifiers changed from: private */
    public StreamingServer mStreamingServer;
    /* access modifiers changed from: private */
    public Set mSystemSigningKeys;
    private BroadcastReceiver mWifiStateReceiver;

    class BinderInterface extends Stub {
        private BinderInterface() {
        }

        public Bundle customClientRequest(IRemoteControlClient iRemoteControlClient, String str, Bundle bundle) {
            Bundle access$1700;
            synchronized (RemoteControlService.mCondVar) {
                access$1700 = RemoteControlService.this.checkClient(iRemoteControlClient).customClientRequest(str, bundle);
            }
            return access$1700;
        }

        public Bundle customRequest(String str, Bundle bundle) {
            String access$100 = RemoteControlService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("customRequest: ");
            sb.append(str);
            sb.append(" -> ");
            sb.append(RemoteControlContract.jsonify(bundle));
            RCSDebug.d(access$100, sb.toString());
            synchronized (RemoteControlService.mCondVar) {
                if (!str.equals(RemoteControlExtension.CUSTOM_REQUEST_SET_CAPTURING_MODE)) {
                    return null;
                }
                for (IBinder iBinder : RemoteControlService.this.mClients.keySet()) {
                    RemoteControlClient remoteControlClient = (RemoteControlClient) RemoteControlService.this.mClients.get(iBinder);
                    if (remoteControlClient != null && !remoteControlClient.mIsGroupOwner && iBinder.isBinderAlive()) {
                        remoteControlClient.customCallback(str, bundle);
                    }
                }
                Bundle bundle2 = new Bundle();
                RemoteControlContract.setErrorCode(bundle2, 0);
                return bundle2;
            }
        }

        public void injectKeyEvent(IRemoteControlClient iRemoteControlClient, KeyEvent keyEvent) {
            synchronized (RemoteControlService.mCondVar) {
                RemoteControlService.this.checkClient(iRemoteControlClient).injectKeyEvent(keyEvent);
            }
        }

        public void injectMotionEvent(IRemoteControlClient iRemoteControlClient, MotionEvent motionEvent) {
            synchronized (RemoteControlService.mCondVar) {
                RemoteControlService.this.checkClient(iRemoteControlClient).injectMotionEvent(motionEvent);
            }
        }

        public boolean isStreaming(IRemoteControlClient iRemoteControlClient) {
            boolean access$1500;
            synchronized (RemoteControlService.mCondVar) {
                access$1500 = RemoteControlService.this.checkClient(iRemoteControlClient).isStreaming();
            }
            return access$1500;
        }

        /* JADX WARNING: Removed duplicated region for block: B:24:0x009a A[Catch:{ NotFoundException -> 0x0161, RemoteException -> 0x0105 }] */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x00c3 A[Catch:{ NotFoundException -> 0x0161, RemoteException -> 0x0105 }] */
        /* JADX WARNING: Removed duplicated region for block: B:35:0x00c6 A[Catch:{ NotFoundException -> 0x0161, RemoteException -> 0x0105 }] */
        /* JADX WARNING: Removed duplicated region for block: B:47:0x014a A[Catch:{ NotFoundException -> 0x0161, RemoteException -> 0x0105 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int registerRemoteController(IRemoteControlClient iRemoteControlClient) {
            boolean z;
            String string;
            synchronized (RemoteControlService.mCondVar) {
                if (C0122O00000o.instance().OO0o0Oo()) {
                    String[] packagesForUid = RemoteControlService.this.getPackageManager().getPackagesForUid(Binder.getCallingUid());
                    if (packagesForUid == null || packagesForUid.length != 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Access denied: ");
                        sb.append(Arrays.toString(packagesForUid));
                        throw new SecurityException(sb.toString());
                    }
                    String str = packagesForUid[0];
                    HashSet<String> hashSet = new HashSet<>(RemoteControlService.this.mSystemSigningKeys);
                    try {
                        if (str.equalsIgnoreCase("com.android.camera")) {
                            if (SystemProperties.getBoolean("cam.rcs.debug.enabled", false)) {
                                string = RemoteControlService.this.getResources().getString(R.string.certificate_com_android_camera);
                            }
                            Set access$700 = RemoteControlService.getSigningKeysForPackage(RemoteControlService.this.getBaseContext(), str);
                            if (hashSet.size() > 0) {
                                z = false;
                                for (String str2 : hashSet) {
                                    Iterator it = access$700.iterator();
                                    while (true) {
                                        if (it.hasNext()) {
                                            if (TextUtils.equals(str2, (String) it.next())) {
                                                z = true;
                                                break;
                                            }
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            } else {
                                z = false;
                            }
                            if (z) {
                                IBinder asBinder = iRemoteControlClient.asBinder();
                                if (!RemoteControlService.this.mClients.containsKey(asBinder)) {
                                    RemoteControlClient remoteControlClient = new RemoteControlClient(iRemoteControlClient, str);
                                    RemoteControlService.this.mClients.put(asBinder, remoteControlClient);
                                    String access$100 = RemoteControlService.TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("register: ");
                                    sb2.append(remoteControlClient);
                                    RCSDebug.d(access$100, sb2.toString());
                                    asBinder.linkToDeath(RemoteControlService.this, 0);
                                } else {
                                    String access$1002 = RemoteControlService.TAG;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("register: already registered: ");
                                    sb3.append(RemoteControlService.this.mClients.get(asBinder));
                                    RCSDebug.e(access$1002, sb3.toString());
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Already registered: ");
                                    sb4.append(asBinder);
                                    throw new IllegalStateException(sb4.toString());
                                }
                            } else {
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("Access denied: ");
                                sb5.append(str);
                                throw new SecurityException(sb5.toString());
                            }
                        } else {
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("certificate_");
                            sb6.append(str.replace('.', '_'));
                            int identifier = RemoteControlService.this.getResources().getIdentifier(sb6.toString(), "string", RemoteControlService.this.getPackageName());
                            if (identifier != 0) {
                                string = RemoteControlService.this.getResources().getString(identifier);
                            }
                            Set access$7002 = RemoteControlService.getSigningKeysForPackage(RemoteControlService.this.getBaseContext(), str);
                            if (hashSet.size() > 0) {
                            }
                            if (z) {
                            }
                        }
                        hashSet.add(string);
                        Set access$70022 = RemoteControlService.getSigningKeysForPackage(RemoteControlService.this.getBaseContext(), str);
                        if (hashSet.size() > 0) {
                        }
                        if (z) {
                        }
                    } catch (NotFoundException unused) {
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("Access denied: ");
                        sb7.append(str);
                        throw new SecurityException(sb7.toString());
                    } catch (RemoteException e) {
                        RCSDebug.e(RemoteControlService.TAG, "register: exception", e);
                    }
                } else {
                    throw new SecurityException("Access denied: streaming not supported");
                }
            }
            return 0;
        }

        public void startStreaming(IRemoteControlClient iRemoteControlClient, Bundle bundle) {
            RCSDebug.d(RemoteControlService.TAG, "startStreaming: E");
            synchronized (RemoteControlService.mCondVar) {
                RemoteControlClient access$1000 = RemoteControlService.this.checkClient(iRemoteControlClient);
                access$1000.startStreaming(bundle);
                String access$100 = RemoteControlService.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("startStreaming: ");
                sb.append(access$1000);
                RCSDebug.d(access$100, sb.toString());
            }
            RCSDebug.d(RemoteControlService.TAG, "startStreaming: X");
        }

        public void stopStreaming(IRemoteControlClient iRemoteControlClient, Bundle bundle) {
            RCSDebug.d(RemoteControlService.TAG, "stopStreaming: E");
            synchronized (RemoteControlService.mCondVar) {
                RemoteControlService.this.checkClient(iRemoteControlClient).stopStreaming(bundle);
            }
            RCSDebug.d(RemoteControlService.TAG, "stopStreaming: X");
        }

        public void unregisterRemoteController(IRemoteControlClient iRemoteControlClient) {
            synchronized (RemoteControlService.mCondVar) {
                RemoteControlClient access$1000 = RemoteControlService.this.checkClient(iRemoteControlClient);
                String access$100 = RemoteControlService.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unregister: ");
                sb.append(access$1000);
                RCSDebug.i(access$100, sb.toString());
                RemoteControlService.this.cleanupClient(iRemoteControlClient.asBinder());
                String access$1002 = RemoteControlService.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("unregister: count = ");
                sb2.append(RemoteControlService.this.mClients.size());
                RCSDebug.i(access$1002, sb2.toString());
                boolean z = false;
                Iterator it = RemoteControlService.this.mClients.keySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    IBinder iBinder = (IBinder) it.next();
                    RemoteControlClient remoteControlClient = (RemoteControlClient) RemoteControlService.this.mClients.get(iBinder);
                    String access$1003 = RemoteControlService.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("unregister: enumerating ");
                    sb3.append(remoteControlClient);
                    RCSDebug.i(access$1003, sb3.toString());
                    if (remoteControlClient != null && remoteControlClient.mIsGroupOwner && iBinder.isBinderAlive()) {
                        z = true;
                        break;
                    }
                }
                String access$1004 = RemoteControlService.TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("unregister: is group owner alive = ");
                sb4.append(z);
                RCSDebug.i(access$1004, sb4.toString());
                if (!z) {
                    for (IBinder iBinder2 : RemoteControlService.this.mClients.keySet()) {
                        RemoteControlClient remoteControlClient2 = (RemoteControlClient) RemoteControlService.this.mClients.get(iBinder2);
                        String access$1005 = RemoteControlService.TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("unregister: broadcasting ");
                        sb5.append(remoteControlClient2);
                        RCSDebug.i(access$1005, sb5.toString());
                        if (remoteControlClient2 != null && !remoteControlClient2.mIsGroupOwner && iBinder2.isBinderAlive()) {
                            remoteControlClient2.customCallback(RemoteControlContract.CUSTOM_CALLBACK_GROUP_OWNER_DIED, null);
                        }
                    }
                }
            }
        }
    }

    class RemoteControlClient implements StreamingStateCallback {
        /* access modifiers changed from: private */
        public volatile boolean mIsGroupOwner;
        private IRemoteControlClient mListener;
        private String mPkgName;

        private RemoteControlClient(IRemoteControlClient iRemoteControlClient, String str) {
            this.mIsGroupOwner = false;
            RCSDebug.i(RemoteControlService.TAG, "RemoteControlClient: create: E");
            this.mListener = iRemoteControlClient;
            this.mPkgName = str;
            synchronized (RemoteControlService.this) {
                if (RemoteControlService.this.mStreamingServer != null) {
                    RemoteControlService.this.mStreamingServer.addCallbackListener(this);
                }
            }
            RCSDebug.i(RemoteControlService.TAG, "RemoteControlClient: create: X");
        }

        /* access modifiers changed from: private */
        public void customCallback(String str, Bundle bundle) {
            String access$100 = RemoteControlService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("RemoteControlClient: customCallback: ");
            sb.append(str);
            sb.append(" -> ");
            sb.append(this);
            RCSDebug.d(access$100, sb.toString());
            try {
                this.mListener.customCallback(str, bundle);
            } catch (RemoteException unused) {
            }
        }

        /* access modifiers changed from: private */
        public Bundle customClientRequest(String str, Bundle bundle) {
            String access$100 = RemoteControlService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("RemoteControlClient: customClientRequest: ");
            sb.append(str);
            sb.append(" -> ");
            sb.append(this);
            RCSDebug.i(access$100, sb.toString());
            return null;
        }

        /* access modifiers changed from: private */
        public boolean isStreaming() {
            boolean z;
            synchronized (RemoteControlService.this) {
                z = RemoteControlService.this.mStreamingServer != null && RemoteControlService.this.mStreamingServer.isStreaming();
            }
            return z;
        }

        /* access modifiers changed from: private */
        public void release() {
            String access$100 = RemoteControlService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("RemoteControlClient: release: E -> ");
            sb.append(this);
            RCSDebug.i(access$100, sb.toString());
            this.mListener = null;
            synchronized (RemoteControlService.this) {
                if (RemoteControlService.this.mStreamingServer != null) {
                    RemoteControlService.this.mStreamingServer.removeCallbackListener(this);
                }
            }
            RCSDebug.i(RemoteControlService.TAG, "RemoteControlClient: release: X");
        }

        /* access modifiers changed from: private */
        public void startStreaming(Bundle bundle) {
            RCSDebug.d(RemoteControlService.TAG, "RemoteControlClient: start: E");
            boolean z = this.mIsGroupOwner;
            boolean z2 = bundle != null && RemoteControlExtension.isGroupOwner(bundle);
            this.mIsGroupOwner = z2;
            String access$100 = RemoteControlService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("start: is client group owner = ");
            sb.append(this.mIsGroupOwner);
            RCSDebug.d(access$100, sb.toString());
            synchronized (RemoteControlService.this) {
                if (!z) {
                    if (this.mIsGroupOwner && RemoteControlService.this.mStreamingServer != null) {
                        RemoteControlService.this.mStreamingServer.start();
                        String access$1002 = RemoteControlService.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("RemoteControlClient: start: notify port = ");
                        sb2.append(RemoteControlService.this.mStreamingServer.getPort());
                        RCSDebug.d(access$1002, sb2.toString());
                        RemoteControlService.this.mStreamingServer.postStreamingServerState(2);
                    }
                }
            }
            RCSDebug.d(RemoteControlService.TAG, "RemoteControlClient: start: X");
        }

        /* access modifiers changed from: private */
        public void stopStreaming(Bundle bundle) {
            RCSDebug.d(RemoteControlService.TAG, "RemoteControlClient: stop: E");
            boolean isGroupOwner = RemoteControlExtension.isGroupOwner(bundle);
            String access$100 = RemoteControlService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("stop: is client group owner = ");
            sb.append(isGroupOwner);
            RCSDebug.d(access$100, sb.toString());
            synchronized (RemoteControlService.this) {
                if (isGroupOwner) {
                    if (RemoteControlService.this.mStreamingServer != null) {
                        RemoteControlService.this.mStreamingServer.stop();
                        RemoteControlService.this.mStreamingServer = null;
                    }
                }
            }
            RCSDebug.d(RemoteControlService.TAG, "RemoteControlClient: stop: X");
        }

        /* access modifiers changed from: 0000 */
        public void injectKeyEvent(KeyEvent keyEvent) {
            try {
                RemoteControlService.this.mInputManager.injectInputEvent(keyEvent, 0);
            } catch (Exception e) {
                RCSDebug.e(RemoteControlService.TAG, "RemoteControlClient: exception in injectKeyEvent", e);
            }
        }

        /* access modifiers changed from: 0000 */
        public void injectMotionEvent(MotionEvent motionEvent) {
            try {
                motionEvent.setSource(4098);
                RemoteControlService.this.mInputManager.injectInputEvent(motionEvent, 0);
            } catch (Exception e) {
                RCSDebug.e(RemoteControlService.TAG, "RemoteControlClient: exception in injectMotionEvent", e);
            }
        }

        public void onStreamingServerStateChanged(int i, Bundle bundle) {
            String access$100 = RemoteControlService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onStreamingServerStateChanged: ");
            sb.append(i);
            sb.append(" -> ");
            sb.append(this);
            RCSDebug.d(access$100, sb.toString());
            try {
                this.mListener.streamingServerStatus(i, bundle);
            } catch (RemoteException unused) {
            }
        }

        public void onStreamingSessionStateChanged(int i, Bundle bundle) {
            String access$100 = RemoteControlService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onStreamingSessionStateChanged: ");
            sb.append(i);
            sb.append(" -> ");
            sb.append(this);
            RCSDebug.d(access$100, sb.toString());
            try {
                this.mListener.streamingSessionStatus(i, bundle);
            } catch (RemoteException unused) {
            }
        }

        public String toString() {
            return String.format(Locale.ROOT, "RCC(ID=%d, CN=%s, GO=%s, BP=%s)", new Object[]{Integer.valueOf(hashCode()), this.mPkgName, Boolean.valueOf(this.mIsGroupOwner), Integer.valueOf(this.mListener.hashCode())});
        }
    }

    /* access modifiers changed from: private */
    public RemoteControlClient checkClient(IRemoteControlClient iRemoteControlClient) {
        IBinder asBinder = iRemoteControlClient.asBinder();
        RemoteControlClient remoteControlClient = (RemoteControlClient) this.mClients.get(asBinder);
        if (remoteControlClient != null) {
            return remoteControlClient;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        String str2 = "Client ";
        sb.append(str2);
        sb.append(asBinder);
        String str3 = " is not registered";
        sb.append(str3);
        RCSDebug.e(str, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        sb2.append(asBinder);
        sb2.append(str3);
        throw new IllegalStateException(sb2.toString());
    }

    /* access modifiers changed from: private */
    public void cleanupClient(IBinder iBinder) {
        iBinder.unlinkToDeath(this, 0);
        RemoteControlClient remoteControlClient = (RemoteControlClient) this.mClients.get(iBinder);
        if (remoteControlClient != null) {
            remoteControlClient.release();
        }
        this.mClients.remove(iBinder);
    }

    private static String getHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            sb.append(Integer.toString((b & -1) + 0, 16).substring(1));
        }
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public static Set getSigningKeysForPackage(Context context, String str) {
        String str2;
        String str3;
        PackageManager packageManager = context.getPackageManager();
        HashSet hashSet = new HashSet();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(str, 64);
            if (packageInfo != null) {
                String str4 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Found ");
                sb.append(packageInfo.signatures.length);
                sb.append(" signing keys for ");
                sb.append(str);
                RCSDebug.i(str4, sb.toString());
                for (int i = 0; i < packageInfo.signatures.length; i++) {
                    MessageDigest instance = MessageDigest.getInstance("SHA1");
                    instance.update(packageInfo.signatures[i].toByteArray());
                    String hexString = getHexString(instance.digest());
                    hashSet.add(hexString);
                    String str5 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("\t[");
                    sb2.append(i);
                    sb2.append("]: ");
                    sb2.append(hexString);
                    RCSDebug.i(str5, sb2.toString());
                }
            }
        } catch (NoSuchAlgorithmException unused) {
            str3 = TAG;
            str2 = "Failed to find algorithm for getting signing keys";
            RCSDebug.e(str3, str2);
            return hashSet;
        } catch (NameNotFoundException unused2) {
            str3 = TAG;
            str2 = "Failed to find package for getting signing keys";
            RCSDebug.e(str3, str2);
            return hashSet;
        }
        return hashSet;
    }

    public void binderDied() {
        synchronized (mCondVar) {
            for (IBinder iBinder : this.mClients.keySet()) {
                if (!iBinder.isBinderAlive()) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("binderDied: ");
                    sb.append(iBinder);
                    RCSDebug.i(str, sb.toString());
                    cleanupClient(iBinder);
                    return;
                }
            }
        }
    }

    public IBinder onBind(Intent intent) {
        RCSDebug.d(TAG, "onBind: E ");
        RCSDebug.d(TAG, "onBind: X ");
        return this.mInterface;
    }

    public void onCreate() {
        RCSDebug.d(TAG, "onCreate: E");
        this.mSystemSigningKeys = getSigningKeysForPackage(this, SYSTEM_APP_PACKAGE_NAME);
        this.mInputManager = (InputManager) getSystemService(EventInjector.INPUT_COMMAND_STRING);
        this.mStreamingServer = new StreamingServer(this);
        this.mWifiStateReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String str;
                String str2;
                String action = intent.getAction();
                if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                    int intExtra = intent.getIntExtra("wifi_state", 14);
                    if (intExtra == 14) {
                        str2 = RemoteControlService.TAG;
                        str = "SoftAP start failed";
                    } else if (intExtra == 11) {
                        str2 = RemoteControlService.TAG;
                        str = "SoftAP state disabled";
                    } else if (intExtra == 13) {
                        str2 = RemoteControlService.TAG;
                        str = "SoftAP state enabled";
                    } else {
                        return;
                    }
                } else if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        NetworkInfo networkInfo = (NetworkInfo) extras.getParcelable("networkInfo");
                        if (networkInfo == null) {
                            return;
                        }
                        if (networkInfo.isConnected()) {
                            str2 = RemoteControlService.TAG;
                            str = "Wifi state connected";
                        } else if (!networkInfo.isConnectedOrConnecting()) {
                            str2 = RemoteControlService.TAG;
                            str = "Wifi state not connected yet";
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                RCSDebug.e(str2, str);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        RCSDebug.d(TAG, "++ mWifiStateReceiver");
        registerReceiver(this.mWifiStateReceiver, new IntentFilter(intentFilter));
        RCSDebug.d(TAG, "onCreate: X");
    }

    public void onDestroy() {
        RCSDebug.d(TAG, "onDestroy: E");
        if (this.mWifiStateReceiver != null) {
            RCSDebug.d(TAG, "-- mWifiStateReceiver");
            unregisterReceiver(this.mWifiStateReceiver);
            this.mWifiStateReceiver = null;
        }
        synchronized (this) {
            if (this.mStreamingServer != null) {
                this.mStreamingServer.stop();
                this.mStreamingServer = null;
            }
        }
        RCSDebug.d(TAG, "onDestroy: X");
    }

    public void onRebind(Intent intent) {
        RCSDebug.d(TAG, "onRebind: E ");
        RCSDebug.d(TAG, "onRebind: X ");
    }

    public boolean onUnbind(Intent intent) {
        RCSDebug.d(TAG, "onUnbind: E ");
        RCSDebug.d(TAG, "onUnbind: X ");
        return false;
    }
}
