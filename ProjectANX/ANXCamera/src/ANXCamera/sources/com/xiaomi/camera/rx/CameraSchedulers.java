package com.xiaomi.camera.rx;

import android.os.Looper;
import com.xiaomi.camera.device.CameraService;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CameraSchedulers {
    public static final Scheduler sCameraCallableScheduler = AndroidSchedulers.from(CameraService.getCameraCallableHandler().getLooper());
    public static final Scheduler sCameraSetupScheduler;
    private static final ThreadLocal sIsCameraSetupThread = new ThreadLocal() {
        /* access modifiers changed from: protected */
        public Boolean initialValue() {
            return Boolean.FALSE;
        }
    };
    public static final Scheduler sMainThreadScheduler = AndroidSchedulers.mainThread();

    static {
        Scheduler single = Schedulers.single();
        single.scheduleDirect(O000000o.INSTANCE);
        sCameraSetupScheduler = single;
    }

    public static boolean isOnCameraHandlerThread() {
        return Looper.myLooper() == CameraService.getCameraCallableHandler().getLooper();
    }

    public static boolean isOnCameraSetupThread() {
        return ((Boolean) sIsCameraSetupThread.get()).booleanValue();
    }

    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
