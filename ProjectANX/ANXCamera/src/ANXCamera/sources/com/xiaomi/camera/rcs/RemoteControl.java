package com.xiaomi.camera.rcs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.xiaomi.camera.rcs.IRemoteControlClient.Stub;

public class RemoteControl {
    public static final String BIND_SERVICE_INTENT = "com.xiaomi.camera.REMOTE_CONTROL_SERVICE_BIND";
    private static final String LOG_TAG_PREFIX = "CAM_RCS_";
    public static final int STATUS_DISCONNECTED = 4;
    public static final int STATUS_PERMISSION_DENIED = 1;
    public static final int STATUS_SERVICE_UNAVAILABLE = 3;
    public static final int STATUS_STREAMING_SERVER_BIND_FAILED = 1;
    public static final int STATUS_STREAMING_SERVER_READY = 2;
    public static final int STATUS_STREAMING_SESSION_ERROR = 1;
    public static final int STATUS_STREAMING_SESSION_STARTED = 2;
    public static final int STATUS_STREAMING_SESSION_STOPPED = 3;
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_UNKNOWN_ERROR = 100;
    /* access modifiers changed from: private */
    public static final String TAG;
    /* access modifiers changed from: private */
    public CallbackHandler mCallbackHandler = new CallbackHandler();
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public RemoteControlServiceConnection mServiceConnection;
    /* access modifiers changed from: private */
    public IRemoteControl mServiceInterface;

    class CallbackHandler extends Stub {
        ICustomCallbacks mCustomListener;
        ICallbacks mListener;
        IStreamingCallbacks mStreamingListener;

        private CallbackHandler() {
        }

        public void connectionStatus(int i) {
            ICallbacks iCallbacks = this.mListener;
            if (iCallbacks != null) {
                iCallbacks.connectionStatus(i);
            }
        }

        public void customCallback(String str, Bundle bundle) {
            ICustomCallbacks iCustomCallbacks = this.mCustomListener;
            if (iCustomCallbacks != null) {
                iCustomCallbacks.customCallback(str, bundle);
            }
        }

        public void streamingServerStatus(int i, Bundle bundle) {
            IStreamingCallbacks iStreamingCallbacks = this.mStreamingListener;
            if (iStreamingCallbacks != null) {
                iStreamingCallbacks.streamingServerStatus(i, bundle);
            }
        }

        public void streamingSessionStatus(int i, Bundle bundle) {
            IStreamingCallbacks iStreamingCallbacks = this.mStreamingListener;
            if (iStreamingCallbacks != null) {
                iStreamingCallbacks.streamingSessionStatus(i, bundle);
            }
        }
    }

    public interface ICallbacks {
        void connectionStatus(int i);
    }

    public interface ICustomCallbacks {
        void customCallback(String str, Bundle bundle);
    }

    public interface IStreamingCallbacks {
        void streamingServerStatus(int i, Bundle bundle);

        void streamingSessionStatus(int i, Bundle bundle);
    }

    public class RemoteControlException extends Exception {
    }

    class RemoteControlServiceConnection implements ServiceConnection {
        private RemoteControlServiceConnection() {
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(7:2|3|(1:5)(5:6|7|8|(2:16|17)|18)|19|(2:23|24)|25|26) */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0078 */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x0099  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            int i;
            int i2;
            Log.d(RemoteControl.TAG, "onServiceConnected: E");
            synchronized (RemoteControl.this) {
                if (RemoteControl.this.mServiceConnection == null) {
                    i = 4;
                    RemoteControl.this.mServiceInterface = null;
                } else {
                    IRemoteControl asInterface = IRemoteControl.Stub.asInterface(iBinder);
                    try {
                        i2 = asInterface.registerRemoteController(RemoteControl.this.mCallbackHandler);
                    } catch (SecurityException unused) {
                        i2 = 1;
                    } catch (RemoteException unused2) {
                        i2 = 3;
                    } catch (Throwable unused3) {
                        i2 = 100;
                    }
                    if (i2 == 0) {
                        RemoteControl.this.mServiceInterface = asInterface;
                    }
                    i = i2;
                }
                String access$200 = RemoteControl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onServiceConnected: rv = ");
                sb.append(i);
                Log.d(access$200, sb.toString());
                if (!(i == 0 || RemoteControl.this.mServiceConnection == null)) {
                    Log.d(RemoteControl.TAG, "onServiceConnected: unbind");
                    RemoteControl.this.mContext.unbindService(RemoteControl.this.mServiceConnection);
                    RemoteControl.this.mServiceConnection = null;
                }
            }
            CallbackHandler access$500 = RemoteControl.this.mCallbackHandler;
            String access$2002 = RemoteControl.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onServiceConnected: cb = ");
            sb2.append(access$500);
            Log.d(access$2002, sb2.toString());
            if (access$500 != null) {
                access$500.connectionStatus(i);
            }
            Log.d(RemoteControl.TAG, "onServiceConnected: X");
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(RemoteControl.TAG, "onServiceDisconnected");
            synchronized (RemoteControl.this) {
                RemoteControl.this.mServiceInterface = null;
            }
        }
    }

    public class ServiceExitedException extends RemoteControlException {
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(LOG_TAG_PREFIX);
        sb.append(RemoteControl.class.getSimpleName());
        TAG = sb.toString();
    }

    private RemoteControl(Context context, ICallbacks iCallbacks, IStreamingCallbacks iStreamingCallbacks, ICustomCallbacks iCustomCallbacks) {
        CallbackHandler callbackHandler = this.mCallbackHandler;
        callbackHandler.mListener = iCallbacks;
        callbackHandler.mStreamingListener = iStreamingCallbacks;
        callbackHandler.mCustomListener = iCustomCallbacks;
        this.mContext = context;
        Intent intent = new Intent(BIND_SERVICE_INTENT);
        ResolveInfo resolveService = context.getPackageManager().resolveService(intent, 0);
        if (resolveService != null) {
            ServiceInfo serviceInfo = resolveService.serviceInfo;
            if (serviceInfo != null && serviceInfo.enabled && resolveService.serviceInfo.exported && resolveService.serviceInfo.applicationInfo != null) {
                this.mServiceConnection = new RemoteControlServiceConnection();
                String str = resolveService.serviceInfo.applicationInfo.packageName;
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Binding to service found in package: ");
                sb.append(str);
                Log.i(str2, sb.toString());
                intent.setPackage(str);
                if (!context.bindService(intent, this.mServiceConnection, 1)) {
                    Log.e(TAG, "bind service failed");
                    context.unbindService(this.mServiceConnection);
                    this.mServiceConnection = null;
                    this.mCallbackHandler.connectionStatus(3);
                }
                return;
            }
        }
        Log.e(TAG, "Failed to resolve service");
        this.mCallbackHandler.connectionStatus(3);
    }

    private CallbackHandler getCallbackHandler() {
        CallbackHandler callbackHandler = this.mCallbackHandler;
        if (callbackHandler != null) {
            return callbackHandler;
        }
        throw new ServiceExitedException();
    }

    public static RemoteControl getRemoteControl(Context context, ICallbacks iCallbacks) {
        return new RemoteControl(context, iCallbacks, null, null);
    }

    public static RemoteControl getRemoteControl(Context context, ICallbacks iCallbacks, IStreamingCallbacks iStreamingCallbacks) {
        return new RemoteControl(context, iCallbacks, iStreamingCallbacks, null);
    }

    public static RemoteControl getRemoteControl(Context context, ICallbacks iCallbacks, IStreamingCallbacks iStreamingCallbacks, ICustomCallbacks iCustomCallbacks) {
        return new RemoteControl(context, iCallbacks, iStreamingCallbacks, iCustomCallbacks);
    }

    private IRemoteControl getServiceInterface() {
        IRemoteControl iRemoteControl = this.mServiceInterface;
        if (iRemoteControl != null) {
            return iRemoteControl;
        }
        throw new ServiceExitedException();
    }

    public static boolean serviceAvailable(Context context) {
        return context.getPackageManager().resolveService(new Intent(BIND_SERVICE_INTENT), 0) != null;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:7|8|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001b, code lost:
        throw new com.xiaomi.camera.rcs.RemoteControl.ServiceExitedException();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0016 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized Bundle customClientRequest(String str, Bundle bundle) {
        Log.d(TAG, "customClientRequest");
        return getServiceInterface().customClientRequest(getCallbackHandler(), str, bundle);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:7|8|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
        throw new com.xiaomi.camera.rcs.RemoteControl.ServiceExitedException();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0012 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized Bundle customRequest(String str, Bundle bundle) {
        Log.d(TAG, "customRequest");
        return getServiceInterface().customRequest(str, bundle);
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize: E");
        try {
            release();
            super.finalize();
            Log.d(TAG, "finalize: X");
        } catch (Throwable th) {
            super.finalize();
            throw th;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:7|8|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001a, code lost:
        throw new com.xiaomi.camera.rcs.RemoteControl.ServiceExitedException();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0015 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void injectKeyEvent(KeyEvent keyEvent) {
        Log.d(TAG, "injectKeyEvent");
        getServiceInterface().injectKeyEvent(getCallbackHandler(), keyEvent);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:7|8|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001a, code lost:
        throw new com.xiaomi.camera.rcs.RemoteControl.ServiceExitedException();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0015 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void injectMotionEvent(MotionEvent motionEvent) {
        Log.d(TAG, "injectMotionEvent");
        getServiceInterface().injectMotionEvent(getCallbackHandler(), motionEvent);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:1|2|3|4|5|6|7|8) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0015 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean isStreaming() {
        boolean z;
        Log.d(TAG, "isStreaming");
        z = false;
        z = getServiceInterface().isStreaming(getCallbackHandler());
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isStreaming: -> ");
        sb.append(z);
        Log.d(str, sb.toString());
        return z;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x002d */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0019 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void release() {
        Log.d(TAG, "release: E");
        if (!(this.mServiceInterface == null || this.mCallbackHandler == null)) {
            this.mServiceInterface.unregisterRemoteController(this.mCallbackHandler);
            Log.w(TAG, "failed to unregister client");
            this.mServiceInterface = null;
        }
        if (this.mServiceConnection != null) {
            this.mContext.unbindService(this.mServiceConnection);
            this.mServiceConnection = null;
        }
        if (this.mCallbackHandler != null) {
            this.mCallbackHandler.mListener = null;
            this.mCallbackHandler.mStreamingListener = null;
            this.mCallbackHandler.mCustomListener = null;
            this.mCallbackHandler = null;
        }
        Log.d(TAG, "release: X");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:7|8|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001a, code lost:
        throw new com.xiaomi.camera.rcs.RemoteControl.ServiceExitedException();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0015 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void startStreaming(Bundle bundle) {
        Log.d(TAG, "startStreaming");
        getServiceInterface().startStreaming(getCallbackHandler(), bundle);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:7|8|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001a, code lost:
        throw new com.xiaomi.camera.rcs.RemoteControl.ServiceExitedException();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0015 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void stopStreaming(Bundle bundle) {
        Log.d(TAG, "stopStreaming");
        getServiceInterface().stopStreaming(getCallbackHandler(), bundle);
    }
}
