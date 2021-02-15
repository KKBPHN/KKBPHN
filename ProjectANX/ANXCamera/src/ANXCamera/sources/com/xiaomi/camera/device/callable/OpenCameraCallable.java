package com.xiaomi.camera.device.callable;

import android.app.admin.DevicePolicyManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.android.camera.CameraAppImpl;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.MiCamera2;
import com.xiaomi.camera.device.CameraHandlerThread.Cookie;
import com.xiaomi.camera.device.CameraService;
import com.xiaomi.camera.device.exception.CameraDisabledException;
import java.util.concurrent.atomic.AtomicInteger;

public class OpenCameraCallable extends CameraCallable {
    private static final int OPEN_RETRY_COUNT = 10;
    private static final int OPEN_RETRY_SLEEP = 200;
    /* access modifiers changed from: private */
    public static final String TAG = "OpenCameraCallable";
    private final String[] mExclusions;
    /* access modifiers changed from: private */
    public final OpenCameraListener mOpenCameraListener;
    /* access modifiers changed from: private */
    public AtomicInteger mRetryCount = new AtomicInteger(0);
    /* access modifiers changed from: private */
    public final Handler mServiceHandler;
    private final StateCallback mStateListener;

    public OpenCameraCallable(String str, CameraListener cameraListener, OpenCameraListener openCameraListener, Handler handler, Handler handler2, String... strArr) {
        super(str, cameraListener, handler);
        this.mExclusions = strArr;
        this.mOpenCameraListener = openCameraListener;
        this.mServiceHandler = handler2;
        this.mStateListener = new StateCallback() {
            public /* synthetic */ void O000000o(String str, int i) {
                OpenCameraCallable.this.mOpenCameraListener.onError(str, i);
            }

            public /* synthetic */ void O00000oo(String str) {
                OpenCameraCallable.this.mOpenCameraListener.onClosed(str);
            }

            public /* synthetic */ void O0000O0o(String str) {
                OpenCameraCallable.this.mOpenCameraListener.onDisconnected(str);
            }

            public /* synthetic */ void O0000OOo(String str) {
                OpenCameraCallable.this.mOpenCameraListener.onError(str, 231);
            }

            public /* synthetic */ void O0000Oo0(String str) {
                OpenCameraCallable.this.mOpenCameraListener.onOpened(str);
            }

            public void onClosed(@NonNull CameraDevice cameraDevice) {
                String id = cameraDevice.getId();
                String tag = OpenCameraCallable.this.getTag();
                StringBuilder sb = new StringBuilder();
                sb.append("onClosed: cid = ");
                sb.append(id);
                Log.k(3, tag, sb.toString());
                Cookie cookie = OpenCameraCallable.this.getCookieStore().getCookie(id);
                Camera2Proxy camera2Proxy = cookie.mCamera2Device;
                if (camera2Proxy != null && camera2Proxy.getCameraDevice() == cameraDevice) {
                    cookie.mCamera2Device = null;
                    String tag2 = OpenCameraCallable.this.getTag();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onClosed: cache removed: cid = ");
                    sb2.append(id);
                    Log.d(tag2, sb2.toString());
                }
                OpenCameraCallable.this.postCallback(new O00000o0(this, id));
            }

            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                String id = cameraDevice.getId();
                String tag = OpenCameraCallable.this.getTag();
                StringBuilder sb = new StringBuilder();
                sb.append("onDisconnected: cid = ");
                sb.append(id);
                Log.k(3, tag, sb.toString());
                Cookie cookie = OpenCameraCallable.this.getCookieStore().getCookie(id);
                Camera2Proxy camera2Proxy = cookie.mCamera2Device;
                if (camera2Proxy != null && camera2Proxy.getCameraDevice() == cameraDevice) {
                    cookie.mCamera2Device.onCameraDisconnected();
                    cookie.mCamera2Device.releasePreview(1);
                    cookie.mCamera2Device.resetConfigs();
                    cookie.mIsClosing = cookie.mCamera2Device.close();
                    cookie.mIsOpening = false;
                    cookie.mCamera2Device = null;
                    String tag2 = OpenCameraCallable.this.getTag();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onDisconnected: cache removed: cid = ");
                    sb2.append(id);
                    Log.d(tag2, sb2.toString());
                }
                OpenCameraCallable.this.postCallback(new C0707O00000oo(this, id));
            }

            public void onError(@NonNull CameraDevice cameraDevice, int i) {
                String id = cameraDevice.getId();
                String tag = OpenCameraCallable.this.getTag();
                StringBuilder sb = new StringBuilder();
                sb.append("onError: cid = ");
                sb.append(id);
                sb.append(", error = ");
                sb.append(i);
                Log.k(6, tag, sb.toString());
                Cookie cookie = OpenCameraCallable.this.getCookieStore().getCookie(id);
                Camera2Proxy camera2Proxy = cookie.mCamera2Device;
                if (camera2Proxy != null && camera2Proxy.getCameraDevice() == cameraDevice) {
                    cookie.mCamera2Device = null;
                    String tag2 = OpenCameraCallable.this.getTag();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onError: cache removed: cid = ");
                    sb2.append(id);
                    Log.e(tag2, sb2.toString());
                }
                if (!cookie.mIsOpening || OpenCameraCallable.this.mRetryCount.get() <= 10) {
                    Log.e(OpenCameraCallable.TAG, "onError: openCamera error, but we are retrying");
                    return;
                }
                Log.e(OpenCameraCallable.TAG, "onError: post onError to listener");
                OpenCameraCallable.this.postCallback(new O00000o(this, id, i));
                cookie.mIsOpening = false;
            }

            public void onOpened(@NonNull CameraDevice cameraDevice) {
                OpenCameraCallable openCameraCallable;
                Runnable o00000Oo;
                String id = cameraDevice.getId();
                String tag = OpenCameraCallable.this.getTag();
                StringBuilder sb = new StringBuilder();
                String str = "onOpened: cid = ";
                sb.append(str);
                sb.append(id);
                Log.k(3, tag, sb.toString());
                Cookie cookie = OpenCameraCallable.this.getCookieStore().getCookie(id);
                cookie.mIsOpening = false;
                OpenCameraCallable.this.mRetryCount.getAndSet(0);
                if (cookie.mCameraCapabilities == null) {
                    cookie.mCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Integer.parseInt(id));
                }
                if (cookie.mCameraCapabilities == null) {
                    String tag2 = OpenCameraCallable.this.getTag();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(id);
                    sb2.append(", but camera capabilities is null");
                    Log.d(tag2, sb2.toString());
                    openCameraCallable = OpenCameraCallable.this;
                    o00000Oo = new C0706O00000oO(this, id);
                } else {
                    Camera2Proxy camera2Proxy = cookie.mCamera2Device;
                    if (!(camera2Proxy == null || camera2Proxy.getCameraDevice() == null || cookie.mCamera2Device.getCameraDevice() == cameraDevice)) {
                        String tag3 = OpenCameraCallable.this.getTag();
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("onOpened: already cached: cid = ");
                        sb3.append(id);
                        Log.d(tag3, sb3.toString());
                    }
                    Camera2DataContainer.getInstance().setCurrentOpenedCameraId(Integer.parseInt(id));
                    MiCamera2 miCamera2 = new MiCamera2(Integer.parseInt(id), cameraDevice, cookie.mCameraCapabilities, OpenCameraCallable.this.mServiceHandler, cookie.mStreamingHandler);
                    cookie.mCamera2Device = miCamera2;
                    openCameraCallable = OpenCameraCallable.this;
                    o00000Oo = new O00000Oo(this, id);
                }
                openCameraCallable.postCallback(o00000Oo);
            }
        };
    }

    public /* synthetic */ void Oo0o0() {
        this.mOpenCameraListener.onOpened(this.mCameraId);
    }

    @NonNull
    public CallableReturn call() {
        Cookie cookie = getCookieStore().getCookie(this.mCameraId);
        if (cookie.mCamera2Device != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Camera is already opened: cid = ");
            sb.append(cookie.mCamera2Device.getId());
            Log.d(str, sb.toString());
            if (cookie.mCamera2Device.getCapabilities() == null) {
                return new CallableReturn((Exception) new IllegalStateException("Camera capabilities are null"));
            }
            cookie.mCamera2Device.setCaptureBusyCallback(null);
            cookie.mCamera2Device.releasePreview(0);
            cookie.mCamera2Device.resetConfigs();
            postCallback(new O0000O0o(this));
            return new CallableReturn((Exception) null);
        }
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) CameraAppImpl.getAndroidContext().getSystemService("device_policy");
        if (devicePolicyManager != null && devicePolicyManager.getCameraDisabled(null)) {
            return new CallableReturn((Exception) new CameraDisabledException());
        }
        for (Cookie cookie2 : getCookieStore().getCookies()) {
            if (cookie2.mIsOpening) {
                Log.k(3, TAG, String.format("CameraService is busy (cid = %s), postpone the open request (cid = %s)", new Object[]{cookie2.mCameraId, this.mCameraId}));
                CameraService.closeCamera(null, null, null, this.mExclusions);
                CameraService.addCameraCallable(this);
                return new CallableReturn((Exception) null);
            }
        }
        this.mRetryCount.getAndSet(0);
        while (true) {
            try {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("openCamera: E: cid = ");
                sb2.append(this.mCameraId);
                Log.k(3, str2, sb2.toString());
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("openCamera: retries = ");
                sb3.append(this.mRetryCount.get());
                Log.k(3, str3, sb3.toString());
                getCookieStore().mCameraManager.openCamera(this.mCameraId, this.mStateListener, this.mServiceHandler);
                getCookieStore().getCookie(this.mCameraId).mIsOpening = true;
                String str4 = TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("openCamera: X: cid = ");
                sb4.append(this.mCameraId);
                Log.k(3, str4, sb4.toString());
                return new CallableReturn((Exception) null);
            } catch (CameraAccessException e) {
                String str5 = TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("CameraAccessException: Can't open camera ");
                sb5.append(this.mCameraId);
                Log.d(str5, sb5.toString());
                return new CallableReturn((Exception) e);
            } catch (IllegalArgumentException | SecurityException e2) {
                String str6 = TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Can't open camera ");
                sb6.append(this.mCameraId);
                sb6.append(", ");
                sb6.append(e2);
                Log.d(str6, sb6.toString());
                if (this.mRetryCount.incrementAndGet() > 10) {
                    Log.d(TAG, "Retry exceed max limit, return exception");
                    return new CallableReturn((Exception) e2);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e3) {
                    String str7 = TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("InterruptedException: while opening camera ");
                    sb7.append(this.mCameraId);
                    Log.e(str7, sb7.toString());
                    return new CallableReturn((Exception) e3);
                }
            }
        }
    }

    public String getTag() {
        return TAG;
    }
}
