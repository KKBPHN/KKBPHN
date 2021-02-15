package com.android.camera.module.loader.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import com.android.camera.CameraSettings;
import com.android.camera.dualvideo.util.RenderSourceType;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera2.CameraCapabilities;
import com.xiaomi.camera.device.CameraHandlerThread.Cookie;
import com.xiaomi.camera.device.CameraHandlerThread.CookieStore;
import com.xiaomi.camera.device.CameraService;
import com.xiaomi.camera.device.callable.OpenCameraListener;
import com.xiaomi.camera.rx.CameraOpenObservable;
import com.xiaomi.camera.rx.CameraSchedulers;
import com.xiaomi.camera.util.Singleton;
import com.xiaomi.camera.util.SystemProperties;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import miui.text.ExtraTextUtils;

@SuppressLint({"MissingPermission"})
@TargetApi(21)
public class Camera2OpenManager implements ObservableOnSubscribe, OpenCameraListener {
    private static final long CAMERA_OPEN_OR_CLOSE_TIMEOUT = 5000;
    private static final long POP_CAMERA_DELAY_CREATE_SESSION = SystemProperties.getLong("delay_create_session", 450);
    private static final String TAG = "Camera2OpenManager";
    private static final Singleton sInstance = new Singleton() {
        /* access modifiers changed from: protected */
        public Camera2OpenManager create() {
            return new Camera2OpenManager();
        }
    };
    private ObservableEmitter mCameraResultEmitter;
    private ConnectableObservable mCameraResultObservable;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private AtomicInteger mCurrentModule = new AtomicInteger(160);
    private AtomicInteger mPendingCameraId = new AtomicInteger(-1);
    private AtomicLong mPopCameraTimestamp = new AtomicLong(-1);

    static /* synthetic */ SingleSource O000000o(ConcurrentHashMap concurrentHashMap, Camera2Result camera2Result) {
        return camera2Result.getResult() != 2 ? Single.just(camera2Result) : CameraOpenObservable.create(String.valueOf(concurrentHashMap.get(RenderSourceType.MAIN)), new String[0]);
    }

    static /* synthetic */ ObservableSource O0000Ooo(Throwable th) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Exception occurs in camera open or close: ");
        sb.append(th);
        Log.d(str, sb.toString());
        if (!CameraService.getCameraCallableHandler().getLooper().getQueue().isPolling()) {
            Log.d(TAG, "CameraHandlerThread is being stuck...");
        }
        for (Cookie cookie : CameraService.getCookieStore().getCookies()) {
            if (cookie != null) {
                Log.d(TAG, "open camera timeout cookie.mIsOpening false");
                cookie.mIsOpening = false;
            }
        }
        return Observable.just(Camera2Result.create(3).setCameraError(236));
    }

    static /* synthetic */ String[] O0000oOo(int i) {
        return new String[i];
    }

    private void abandonCameraResultObservable() {
        Log.d(TAG, "abandonCameraResultObservable: E");
        ObservableEmitter observableEmitter = this.mCameraResultEmitter;
        if (observableEmitter != null && !observableEmitter.isDisposed()) {
            Log.d(TAG, "abandonCameraResultObservable: fire");
            this.mCameraResultEmitter.onNext(Camera2Result.create(3).setCameraError(225));
            this.mCameraResultEmitter.onComplete();
            this.mCameraResultEmitter = null;
        }
        Log.d(TAG, "abandonCameraResultObservable: X");
    }

    private void delay() {
        if (C0122O00000o.instance().OO0OOOo()) {
            long currentTimeMillis = System.currentTimeMillis() - this.mPopCameraTimestamp.get();
            long j = POP_CAMERA_DELAY_CREATE_SESSION - currentTimeMillis;
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("delay: elapsed = ");
            sb.append(currentTimeMillis);
            sb.append(", remaining = ");
            sb.append(j);
            Log.d(str, sb.toString());
            if (j > 0) {
                Log.d(TAG, "delay: sleep: E");
                try {
                    TimeUnit.MILLISECONDS.sleep(j);
                } catch (InterruptedException unused) {
                }
                Log.d(TAG, "delay: sleep: X");
            }
            this.mPopCameraTimestamp.set(-1);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void fire(Camera2Result camera2Result) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("fire: result = ");
        sb.append(camera2Result);
        sb.append(", requested cid = ");
        sb.append(this.mPendingCameraId.get());
        Log.d(str, sb.toString());
        delay();
        if (this.mCameraResultEmitter == null || this.mCameraResultEmitter.isDisposed()) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("fire: skipped: ");
            sb2.append(this.mCameraResultEmitter);
            Log.d(str2, sb2.toString());
        } else {
            Log.d(TAG, "fire: emitted");
            this.mCameraResultEmitter.onNext(camera2Result);
            this.mCameraResultEmitter.onComplete();
        }
    }

    private static long getCameraOpTimeout() {
        return ModuleManager.isProModule() ? CAMERA_OPEN_OR_CLOSE_TIMEOUT + (CameraSettings.getExposureTime() / ExtraTextUtils.MB) : CAMERA_OPEN_OR_CLOSE_TIMEOUT;
    }

    public static Camera2OpenManager getInstance() {
        return (Camera2OpenManager) sInstance.get();
    }

    private void subscribeCameraResultObservable(Observer observer) {
        Log.d(TAG, "subscribeCameraResultObservable: E");
        ObservableEmitter observableEmitter = this.mCameraResultEmitter;
        if (observableEmitter == null || observableEmitter.isDisposed()) {
            this.mCameraResultObservable = Observable.create(this).timeout(getCameraOpTimeout(), TimeUnit.MILLISECONDS).onErrorResumeNext((Function) O000000o.INSTANCE).observeOn(CameraSchedulers.sCameraSetupScheduler).publish();
            this.mCameraResultObservable.subscribe(observer);
            this.mCameraResultObservable.connect();
        } else {
            this.mCameraResultObservable.subscribe(observer);
        }
        Log.d(TAG, "subscribeCameraResultObservable: X");
    }

    public synchronized CookieStore getCookieStore() {
        return CameraService.getCookieStore();
    }

    public synchronized void onClosed(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onClosed: cid = ");
        sb.append(str);
        Log.d(str2, sb.toString());
        if (C0122O00000o.instance().OO0OOOo() && this.mPendingCameraId.get() == -1 && (Integer.parseInt(str) == 1 || this.mPopCameraTimestamp.get() > 0)) {
            boolean takebackMotor = CompatibilityUtils.takebackMotor();
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onClosed: takebackMotor = ");
            sb2.append(takebackMotor);
            Log.d(str3, sb2.toString());
        }
    }

    public synchronized void onDisconnected(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onDisconnected: cid = ");
        sb.append(str);
        Log.d(str2, sb.toString());
    }

    public synchronized void onError(String str, int i) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onClosed: cid = ");
        sb.append(str);
        sb.append(", errno = ");
        sb.append(i);
        Log.d(str2, sb.toString());
    }

    public synchronized void onOpened(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onOpened: cid = ");
        sb.append(str);
        Log.d(str2, sb.toString());
    }

    @SuppressLint({"CheckResult"})
    public synchronized void openCamera(int i, int i2, Observer observer, boolean z) {
        CompositeDisposable compositeDisposable;
        Disposable subscribe;
        int actualOpenCameraId = Camera2DataContainer.getInstance().getActualOpenCameraId(i, i2);
        boolean z2 = false;
        if (C0122O00000o.instance().OO0OOOo()) {
            boolean z3 = actualOpenCameraId == 1;
            if (z3 && this.mPendingCameraId.get() != actualOpenCameraId) {
                boolean popupMotor = CompatibilityUtils.popupMotor();
                this.mPopCameraTimestamp.set(System.currentTimeMillis());
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("openCamera: popupMotor = ");
                sb.append(popupMotor);
                Log.d(str, sb.toString());
            } else if (!z3) {
                this.mPopCameraTimestamp.set(-1);
            }
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("cid: ");
        sb2.append(this.mPendingCameraId.get());
        sb2.append(" -> ");
        sb2.append(actualOpenCameraId);
        sb2.append(", mid: ");
        sb2.append(this.mCurrentModule.get());
        sb2.append(" -> ");
        sb2.append(i2);
        sb2.append(", fcc: ");
        sb2.append(z);
        Log.k(3, str2, sb2.toString());
        if (!(this.mPendingCameraId.get() == actualOpenCameraId && this.mCurrentModule.get() == i2)) {
            this.mPendingCameraId.set(actualOpenCameraId);
            this.mCurrentModule.set(i2);
            abandonCameraResultObservable();
        }
        this.mCompositeDisposable.clear();
        CameraService.removeCameraCallables();
        String[] strArr = z ? null : i2 == 204 ? (String[]) CameraSettings.getDualVideoConfig().getLocalCameraId().values().stream().map(O0000O0o.INSTANCE).toArray(O00000o.INSTANCE) : new String[]{String.valueOf(actualOpenCameraId)};
        CameraService.closeCamera(null, null, null, strArr);
        subscribeCameraResultObservable(observer);
        if (i2 == 204) {
            ConcurrentHashMap localCameraId = CameraSettings.getDualVideoConfig().getLocalCameraId();
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("dual video openCamera: ");
            sb3.append(localCameraId.toString());
            Log.d(str3, sb3.toString());
            if (localCameraId.size() == 2) {
                this.mCompositeDisposable.add(CameraOpenObservable.create(String.valueOf(localCameraId.get(RenderSourceType.SUB)), new String[0]).flatMap(new O00000Oo(localCameraId)).subscribe((Consumer) new O00000o0(this)));
            } else if (localCameraId.size() == 1) {
                compositeDisposable = this.mCompositeDisposable;
                subscribe = CameraOpenObservable.create(String.valueOf(actualOpenCameraId), strArr).subscribe((Consumer) new O00000o0(this));
            }
            CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(actualOpenCameraId);
            if (capabilities != null && capabilities.isSupportParallelCameraDevice()) {
                z2 = true;
            }
            ParallelSnapshotManager.isParallelTagOpen = z2;
            if (ParallelSnapshotManager.isParallelTagOpen && i2 == 163) {
                ParallelSnapshotManager.getInstance().openCamera();
            }
        } else {
            compositeDisposable = this.mCompositeDisposable;
            subscribe = CameraOpenObservable.create(String.valueOf(actualOpenCameraId), strArr).subscribe((Consumer) new O00000o0(this));
        }
        compositeDisposable.add(subscribe);
        CameraCapabilities capabilities2 = Camera2DataContainer.getInstance().getCapabilities(actualOpenCameraId);
        z2 = true;
        ParallelSnapshotManager.isParallelTagOpen = z2;
        ParallelSnapshotManager.getInstance().openCamera();
    }

    public synchronized void release(boolean z) {
        this.mPendingCameraId.set(-1);
        this.mCompositeDisposable.clear();
        CameraService.removeCameraCallables();
        abandonCameraResultObservable();
        CameraService.release(null, z, null, null);
    }

    public synchronized void subscribe(ObservableEmitter observableEmitter) {
        this.mCameraResultEmitter = observableEmitter;
    }
}
