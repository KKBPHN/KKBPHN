package com.xiaomi.camera.device;

import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Message;
import com.android.camera.CameraAppImpl;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.xiaomi.camera.device.CameraHandlerThread.CookieStore;
import com.xiaomi.camera.device.callable.CallableReturn;
import com.xiaomi.camera.device.callable.CameraCallable;
import com.xiaomi.camera.device.callable.CameraListener;
import com.xiaomi.camera.device.callable.CloseCameraCallable;
import com.xiaomi.camera.device.callable.OpenCameraCallable;
import com.xiaomi.camera.device.callable.OpenCameraListener;
import com.xiaomi.camera.device.callable.ReleaseCameraCallable;
import com.xiaomi.camera.util.Singleton;
import com.xiaomi.camera.util.ThreadUtils;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;

public class CameraService {
    private static final int NORMAL_MSG_TYPE = 1;
    public static final int REASON_CLOSE = 0;
    public static final int REASON_DISCONNECTED = 1;
    public static final int SHOTBOOST_TYPE = 101;
    private static final int STICKY_MSG_TYPE = 10;
    private static final Singleton sInstance = new Singleton() {
        /* access modifiers changed from: protected */
        public CameraService create() {
            return new CameraService();
        }
    };
    private final Handler mCameraCallableHandler;
    private final CameraHandlerThread mCameraHandlerThread;

    private CameraService() {
        this.mCameraHandlerThread = new CameraHandlerThread();
        this.mCameraHandlerThread.start();
        CameraManager cameraManager = (CameraManager) CameraAppImpl.getAndroidContext().getSystemService("camera");
        this.mCameraHandlerThread.getCookieStore().mCameraManager = cameraManager;
        this.mCameraCallableHandler = new Handler(this.mCameraHandlerThread.getLooper(), O00000o0.INSTANCE);
        Camera2DataContainer.getInstance(cameraManager);
    }

    static /* synthetic */ boolean O000000o(Message message) {
        Object obj = message.obj;
        if (obj instanceof CameraCallable) {
            ((CameraCallable) obj).run();
        }
        return true;
    }

    private static void addCameraCallable(int i, CameraCallable cameraCallable) {
        Handler handler = getInstance().mCameraCallableHandler;
        handler.sendMessage(handler.obtainMessage(i, cameraCallable));
    }

    public static void addCameraCallable(CameraCallable cameraCallable) {
        addCameraCallable(1, cameraCallable);
    }

    private static void addCameraCallableDelayed(int i, CameraCallable cameraCallable, long j) {
        Handler handler = getInstance().mCameraCallableHandler;
        handler.sendMessageDelayed(handler.obtainMessage(i, cameraCallable), j);
    }

    public static void addShotBoostCallableDelayed(CameraCallable cameraCallable, long j) {
        addCameraCallableDelayed(101, cameraCallable, j);
    }

    public static void addStickyCameraCallable(CameraCallable cameraCallable) {
        addCameraCallable(10, cameraCallable);
    }

    public static void closeCamera(String str, CameraListener cameraListener, Handler handler, String... strArr) {
        addCameraCallable(new CloseCameraCallable(str, cameraListener, handler, strArr));
    }

    public static CallableReturn execute(CameraCallable cameraCallable) {
        Handler handler = getInstance().mCameraCallableHandler;
        Objects.requireNonNull(cameraCallable);
        return (CallableReturn) ThreadUtils.invokeAtFrontUninterruptibly(handler, (Callable) new O000000o(cameraCallable));
    }

    public static Object execute(Callable callable) {
        return ThreadUtils.invokeAtFrontUninterruptibly(getInstance().mCameraCallableHandler, callable);
    }

    public static void execute(Runnable runnable) {
        ThreadUtils.invokeAtFrontUninterruptibly(getInstance().mCameraCallableHandler, runnable);
    }

    public static Handler getCameraCallableHandler() {
        return getInstance().mCameraCallableHandler;
    }

    public static CookieStore getCookieStore() {
        return getInstance().mCameraHandlerThread.getCookieStore();
    }

    private static CameraService getInstance() {
        return (CameraService) sInstance.get();
    }

    public static boolean hasPendingCallable(int i) {
        return getInstance().mCameraCallableHandler.hasMessages(i);
    }

    public static void openCamera(String str, CameraListener cameraListener, OpenCameraListener openCameraListener, Handler handler, String... strArr) {
        OpenCameraCallable openCameraCallable = new OpenCameraCallable(str, cameraListener, openCameraListener, getCameraCallableHandler(), handler, strArr);
        addCameraCallable(openCameraCallable);
    }

    public static void preload() {
        getInstance();
    }

    public static void release(String str, boolean z, CameraListener cameraListener, Handler handler) {
        addCameraCallable(new ReleaseCameraCallable(str, z, cameraListener, handler));
    }

    public static void removeCameraCallables() {
        getInstance().mCameraCallableHandler.removeMessages(1);
    }

    public static void removeShotBoostCallable() {
        getInstance().mCameraCallableHandler.removeMessages(101);
    }

    public Set getConcurrentStreamingCameraIds() {
        return null;
    }
}
