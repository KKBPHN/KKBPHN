package com.android.camera;

import O00000Oo.O00000Oo.O000000o.O00000o;
import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.hardware.camera2.CameraManager;
import android.opengl.EGLContext;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.LocaleList;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Size;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.MainThread;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.camera.CameraIntentManager.ControlActions;
import com.android.camera.LocalParallelService.LocalBinder;
import com.android.camera.Manifest.permission;
import com.android.camera.ThermalDetector.OnThermalNotificationListener;
import com.android.camera.aftersales.AftersalesManager;
import com.android.camera.animation.AnimationComposite;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.data.observeable.VMProcessing;
import com.android.camera.data.observeable.VMResource;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.external.NotifyExternalManager;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.dialog.AiSceneNewbieDialogFragment;
import com.android.camera.fragment.dialog.BaseDialogFragment;
import com.android.camera.fragment.dialog.FrontRotateNewbieDialogFragment;
import com.android.camera.fragment.dialog.HibernationFragment;
import com.android.camera.fragment.dialog.IDCardModeNewbieDialogFragment;
import com.android.camera.fragment.dialog.LensDirtyDetectDialogFragment;
import com.android.camera.fragment.dialog.LongPressLiveFragment;
import com.android.camera.fragment.dialog.MacroModeNewbieDialogFragment;
import com.android.camera.fragment.dialog.PortraitNewbieDialogFragment;
import com.android.camera.fragment.dialog.UltraTeleNewbieDialogFragment;
import com.android.camera.fragment.dialog.UltraWideNewbieDialogFragment;
import com.android.camera.fragment.dialog.VVNewbieDialogFragment;
import com.android.camera.fragment.lifeCircle.BaseLifecycleListener;
import com.android.camera.fragment.top.FragmentTopConfig;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.Camera2Module;
import com.android.camera.module.Module;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.VideoModule;
import com.android.camera.module.impl.ImplFactory;
import com.android.camera.module.loader.FunctionCameraPrepare;
import com.android.camera.module.loader.FunctionDataSetup;
import com.android.camera.module.loader.FunctionModuleSetup;
import com.android.camera.module.loader.FunctionResumeModule;
import com.android.camera.module.loader.FunctionUISetup;
import com.android.camera.module.loader.NullHolder;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.Camera2OpenManager;
import com.android.camera.module.loader.camera2.Camera2OpenOnSubscribe;
import com.android.camera.module.loader.camera2.Camera2Result;
import com.android.camera.module.loader.camera2.CompletablePreFixCamera2Setup;
import com.android.camera.module.loader.camera2.ParallelSnapshotManager;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera.permission.PermissionManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.AutoHibernation;
import com.android.camera.protocol.ModeProtocol.BaseProtocol;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.ModeChangeController;
import com.android.camera.protocol.ModeProtocol.MoreModePopupController;
import com.android.camera.protocol.ModeProtocol.MultiFeatureManager;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.scene.MiAlgoAsdSceneProfile;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.NonUI;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.statistic.ScenarioTrackUtil;
import com.android.camera.storage.ImageSaver;
import com.android.camera.storage.PriorityStorageBroadcastReceiver;
import com.android.camera.storage.Storage;
import com.android.camera.ui.CameraRootView;
import com.android.camera.ui.V6CameraGLSurfaceView;
import com.android.camera.ui.V6GestureRecognizer;
import com.android.camera.ui.V9EdgeShutterView;
import com.android.camera2.Camera2Proxy;
import com.android.gallery3d.ui.GLCanvas;
import com.google.lens.sdk.LensApi;
import com.google.lens.sdk.LensApi.LensLaunchStatusCallback;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import com.xiaomi.camera.device.CameraHandlerThread.Cookie;
import com.xiaomi.camera.device.CameraService;
import com.xiaomi.camera.device.callable.CallableReturn;
import com.xiaomi.camera.device.callable.CameraCallable;
import com.xiaomi.camera.imagecodec.ImagePool;
import com.xiaomi.camera.liveshot.gles.RenderThread;
import com.xiaomi.camera.rcs.RemoteControl;
import com.xiaomi.camera.rcs.RemoteControl.ICallbacks;
import com.xiaomi.camera.rcs.RemoteControl.ICustomCallbacks;
import com.xiaomi.camera.rcs.RemoteControl.IStreamingCallbacks;
import com.xiaomi.camera.rcs.RemoteControl.ServiceExitedException;
import com.xiaomi.camera.rcs.RemoteControlContract;
import com.xiaomi.camera.rcs.RemoteControlExtension;
import com.xiaomi.camera.rx.CameraSchedulers;
import com.xiaomi.camera.util.ThreadUtils;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import miui.hardware.display.DisplayFeatureManager;
import miui.os.Build;

public class Camera extends ActivityBase implements ICallbacks, IStreamingCallbacks, ICustomCallbacks, OnRequestPermissionsResultCallback, BaseLifecycleListener, BaseProtocol {
    private static final boolean LIVE_STREAMING_ALWAYS_ENABLING = false;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_RESULT_CODE = 2308;
    private static final int REPEAT_KEY_EVENT_GAP = 250;
    /* access modifiers changed from: private */
    public final String TAG;
    private DialogFragment lplDialog;
    /* access modifiers changed from: private */
    public BaseFragmentDelegate mBaseFragmentDelegate;
    private Camera2OpenOnSubscribe mCamera2OpenOnSubscribe;
    /* access modifiers changed from: private */
    public CommonRotationLayout mCameraLayoutRotation;
    private BiFunction mCameraOpenResult = new O0000OOo(this);
    private Disposable mCameraPendingSetupDisposable;
    private Consumer mCameraSetupConsumer = new Consumer() {
        public void accept(@NonNull NullHolder nullHolder) {
            if (!nullHolder.isPresent()) {
                Camera.this.showCameraError(nullHolder.getException());
                Camera.this.mCurrentModule = null;
            } else {
                V6GestureRecognizer.getInstance(Camera.this).setCurrentModule((Module) nullHolder.get());
            }
            Camera.this.setSwitchingModule(false);
            Camera.this.getCameraScreenNail().resetFrameAvailableFlag();
            ThreadUtils.assertMainThread();
            Camera.this.mCameraSetupDisposable = null;
            DataRepository.dataCloudMgr().fillCloudValues();
            AutoLockManager.getInstance(Camera.this).hibernateDelayed();
            if (CameraSettings.isCameraParallelProcessEnable()) {
                ImagePool.getInstance().trimPoolBuffer();
            }
            MultiFeatureManager multiFeatureManager = (MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929);
            if (multiFeatureManager != null) {
                multiFeatureManager.init();
            }
            Log.d(Camera.this.TAG, "CameraSetupConsumer#accept: switch module done");
        }
    };
    /* access modifiers changed from: private */
    public Disposable mCameraSetupDisposable;
    /* access modifiers changed from: private */
    public int mCurrentDisplayMode;
    private LogThread mDebugThread;
    private boolean mDelayReleaseCamera = true;
    private boolean mDidRegister;
    private DisplayFeatureManager mDisplayFeatureManager;
    /* access modifiers changed from: private */
    public boolean mFirstOrientationArrived;
    private boolean mHasBeenSetupOnFocusChanged = false;
    /* access modifiers changed from: private */
    public boolean mHasFocus;
    private ImageSaver mImageSaver;
    private ImplFactory mImplFactory;
    private volatile boolean mIntentPhotoDone;
    private boolean mIsGalleryServiceBound = false;
    private boolean mIsInRequestRuntimePermission = false;
    private boolean mIsLunchFromAutoTest = false;
    private boolean mIsModeSwitched;
    private boolean mIsScreenSlideOff;
    private int mLastIgnoreKey = -1;
    private long mLastKeyDownEventTime = 0;
    private long mLastKeyUpEventTime = 0;
    private LensApi mLensApi;
    private boolean mLockStreamOrientation = true;
    private MainContentProtocol mMainContentProtocolForDispatchTouch;
    private ModeChangeController mMoreChangeCtrl;
    private MoreModePopupController mMoreModePopupController;
    private MyOrientationEventListener mOrientationListener;
    private ProximitySensorLock mProximitySensorLock;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (CameraIntentManager.ACTION_VOICE_CONTROL.equals(intent.getAction()) && CameraIntentManager.getInstance(intent).getVoiceControlAction().equals(ControlActions.CONTROL_ACTION_QUERY_CAMERA_STATUS)) {
                int currentCameraId = DataRepository.dataItemGlobal().getCurrentCameraId();
                NotifyExternalManager.getInstance(Camera.this.getApplicationContext()).notifyModeAndFacing(DataRepository.dataItemGlobal().getCurrentMode(), currentCameraId);
            }
            Module module = Camera.this.mCurrentModule;
            if (module != null && !module.isDeparted()) {
                Camera.this.mCurrentModule.onBroadcastReceived(context, intent);
            }
        }
    };
    private RemoteControl mRemoteControl;
    private boolean mRemoteControlConnected = false;
    private BroadcastReceiver mSDReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!Camera.this.mCurrentModule.isDeparted()) {
                Camera.this.mCurrentModule.onBroadcastReceived(context, intent);
            }
        }
    };
    private ContentObserver mScreenSlideStatusObserver = new ContentObserver(this.mHandler) {
        public boolean deliverSelfNotifications() {
            return true;
        }

        public void onChange(boolean z) {
            super.onChange(z);
            if (!Camera.this.mHasFocus && !Camera.this.mActivityPaused) {
                int i = Util.isScreenSlideOff(Camera.this) ? 701 : 700;
                String access$300 = Camera.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("focus lost, try key code: ");
                sb.append(i);
                Log.d(access$300, sb.toString());
                Camera.this.onKeyDown(i, new KeyEvent(0, i));
            }
        }
    };
    private SensorStateManager mSensorStateManager;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
    private StartControl mStartControl;
    private RenderThread mStreamingSurfaceRenderThread = null;
    /* access modifiers changed from: private */
    public boolean mSupportOrientation;
    private OnThermalNotificationListener mThermalNotificationListener = new O000000o(this);
    /* access modifiers changed from: private */
    public volatile int mTick;
    private TopAlert mTopAlert;
    private Thread mWatchDog;
    private String newbieDialogFragmentTag = null;
    /* access modifiers changed from: private */
    public final Runnable tickerRunnable = new Runnable() {
        public void run() {
            Camera camera = Camera.this;
            camera.mTick = (camera.mTick + 1) % 10;
        }
    };

    class AdjBoostCallable extends CameraCallable {
        private static final String TAG = "AdjBoostCallable";

        private AdjBoostCallable() {
            super(null, null);
        }

        @androidx.annotation.NonNull
        public CallableReturn call() {
            Iterator it = getCookieStore().getCookies().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Camera2Proxy camera2Proxy = ((Cookie) it.next()).mCamera2Device;
                if (camera2Proxy != null && camera2Proxy.isCaptureBusy(true)) {
                    Util.adjBoost();
                    break;
                }
            }
            return new CallableReturn((Exception) null);
        }

        /* access modifiers changed from: protected */
        public String getTag() {
            return TAG;
        }
    }

    class CameraRunnable implements Runnable {
        private static final String TAG = "CameraRunnable";
        private WeakReference mCamera;
        private boolean mReleaseDevice;
        private boolean mReleaseImmediate;

        CameraRunnable(WeakReference weakReference, boolean z, boolean z2) {
            this.mCamera = weakReference;
            this.mReleaseDevice = z;
            this.mReleaseImmediate = z2;
        }

        public void run() {
            Camera camera = (Camera) this.mCamera.get();
            if (camera != null) {
                if (camera.isCurrentModuleAlive()) {
                    Module module = camera.mCurrentModule;
                    if (ModeCoordinatorImpl.isAlive(camera.hashCode())) {
                        module.unRegisterProtocol();
                        module.unRegisterModulePersistProtocol();
                    }
                    module.onPause();
                    module.onStop();
                    module.onDestroy();
                }
                DataRepository.dataItemGlobal().resetTimeOut();
                if (this.mReleaseDevice) {
                    boolean containsResumedCameraInStack = camera.mApplication.containsResumedCameraInStack();
                    StringBuilder sb = new StringBuilder();
                    sb.append("start releaseCameraDevice: ");
                    sb.append(!containsResumedCameraInStack);
                    String sb2 = sb.toString();
                    String str = TAG;
                    Log.d(str, sb2);
                    if (!containsResumedCameraInStack) {
                        Camera2OpenManager.getInstance().release(this.mReleaseImmediate);
                        boolean isActivityStopped = camera.isActivityStopped();
                        boolean z = C0122O00000o.instance().OO0OOOo() && camera.mApplication.containsResumedCameraInStack();
                        if (isActivityStopped && !z) {
                            camera.releaseCameraScreenNail();
                        }
                    } else {
                        Log.d(str, "Camera2OpenManager release ignored.");
                    }
                }
            }
        }
    }

    public class HibernateRunnable implements Runnable {
        WeakReference mModule;

        public HibernateRunnable(Module module) {
            this.mModule = new WeakReference(module);
        }

        public void run() {
            WeakReference weakReference = this.mModule;
            Module module = weakReference != null ? (Module) weakReference.get() : null;
            if (module != null && module.isCreated()) {
                module.setDeparted();
                module.canIgnoreFocusChanged();
            }
            Camera2OpenManager.getInstance().release(true);
        }
    }

    class LogThread extends Thread {
        private volatile boolean mRunFlag = true;

        LogThread() {
        }

        public void run() {
            while (this.mRunFlag) {
                try {
                    Thread.sleep(10);
                    if (!Camera.this.isActivityPaused()) {
                        Camera.this.mHandler.obtainMessage(0, Util.getDebugInfo()).sendToTarget();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        public void setRunFlag(boolean z) {
            this.mRunFlag = z;
        }
    }

    class MyOrientationEventListener extends OrientationEventListener {
        public MyOrientationEventListener(Context context) {
            super(context);
        }

        public void onOrientationChanged(int i) {
            if (C0124O00000oO.isMTKPlatform()) {
                StringBuilder sb = new StringBuilder();
                sb.append("onOrientationChanged: ");
                sb.append(i);
                Log.d("MTKCAMERAXM", sb.toString());
            }
            if (i != -1) {
                Camera camera = Camera.this;
                camera.mOrientation = Util.roundOrientation(i, camera.mOrientation);
                if (Camera.this.mCurrentDisplayMode == 2) {
                    Camera camera2 = Camera.this;
                    camera2.mOrientation = 360 - camera2.mOrientation;
                    i = 360 - i;
                }
                if (!Camera.this.mFirstOrientationArrived) {
                    Camera.this.mFirstOrientationArrived = true;
                    String access$300 = Camera.this.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onOrientationChanged: first orientation is arrived... , orientation = ");
                    sb2.append(i);
                    sb2.append(", mOrientation = ");
                    sb2.append(Camera.this.mOrientation);
                    Log.d(access$300, sb2.toString());
                }
                int displayRotation = Util.getDisplayRotation(Camera.this);
                Camera camera3 = Camera.this;
                if (displayRotation != camera3.mDisplayRotation) {
                    camera3.mDisplayRotation = displayRotation;
                    camera3.onDisplayRotationChanged();
                }
                Camera camera4 = Camera.this;
                int i2 = camera4.mOrientationCompensation;
                camera4.mOrientationCompensation = (camera4.mOrientation + camera4.mDisplayRotation) % m.cQ;
                Display.updateOrientation(camera4.mOrientationCompensation);
                Camera camera5 = Camera.this;
                Module module = camera5.mCurrentModule;
                if (module != null) {
                    module.onOrientationChanged(camera5.mOrientation, camera5.mOrientationCompensation, i);
                }
                if (Camera.this.mSupportOrientation && Camera.this.mCameraLayoutRotation != null) {
                    Camera.this.mCameraLayoutRotation.provideOrientationChanged(Camera.this.getCurrentModuleIndex(), Camera.this.mOrientationCompensation);
                }
                if (Camera.this.mBaseFragmentDelegate != null) {
                    AnimationComposite animationComposite = Camera.this.mBaseFragmentDelegate.getAnimationComposite();
                    Camera camera6 = Camera.this;
                    animationComposite.disposeRotation(camera6.mOrientationCompensation, camera6.mSupportOrientation);
                }
                AutoHibernation autoHibernation = (AutoHibernation) ModeCoordinatorImpl.getInstance().getAttachProtocol(936);
                if (autoHibernation != null) {
                    autoHibernation.setOrientation(Camera.this.mOrientation);
                }
            }
        }
    }

    class WatchDogThread extends Thread {
        private static final String TAG = "WatchDogThread";
        private static final int TIMEOUT_INTERVAL = 5000;

        private WatchDogThread() {
        }

        public void run() {
            setName("ANR-WatchDog");
            while (!isInterrupted()) {
                StringBuilder sb = new StringBuilder();
                sb.append("watch dog run ");
                sb.append(Thread.currentThread().getId());
                String sb2 = sb.toString();
                String str = TAG;
                Log.v(str, sb2);
                int access$000 = Camera.this.mTick;
                Camera camera = Camera.this;
                camera.mHandler.post(camera.tickerRunnable);
                try {
                    Thread.sleep(5000);
                    if (Camera.this.mTick == access$000) {
                        if (C0124O00000oO.Oo0o0O0()) {
                            CameraSettings.setEdgeMode(Camera.this, false);
                        }
                        Camera.this.setBrightnessRampRate(-1);
                        Camera.this.setScreenEffect(false);
                        AftersalesManager.getInstance().count(System.currentTimeMillis(), 2);
                        if (Util.sIsKillCameraService && C0122O00000o.instance().OO0ooOo() && SystemClock.elapsedRealtime() - CameraSettings.getBroadcastKillServiceTime() > 60000) {
                            Log.d(str, "ANR: broadcastKillService");
                            Util.broadcastKillService(Camera.this, false);
                        }
                        return;
                    }
                } catch (InterruptedException unused) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("watch dog InterruptedException ");
                    sb3.append(Thread.currentThread().getId());
                    Log.e(str, sb3.toString());
                }
            }
        }
    }

    public Camera() {
        StringBuilder sb = new StringBuilder();
        sb.append(Camera.class.getSimpleName());
        sb.append("@");
        sb.append(hashCode());
        this.TAG = sb.toString();
    }

    private void bindServices() {
        try {
            Intent intent = new Intent(Util.ACTION_BIND_GALLERY_SERVICE);
            intent.setPackage(Util.REVIEW_ACTIVITY_PACKAGE);
            bindService(intent, this.mServiceConnection, 5);
            this.mIsGalleryServiceBound = true;
        } catch (Exception e) {
            Log.w(this.TAG, "bindServices error.", e.getCause());
        }
    }

    private void boostParallelServiceAdj() {
        if (!this.mCameraIntentManager.isImageCaptureIntent()) {
            LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
            boolean z = true;
            boolean z2 = CameraSettings.isCameraParallelProcessEnable() && localBinder != null && !localBinder.isIdle();
            if (z2) {
                Util.adjBoost();
                return;
            }
            ImageSaver imageSaver = this.mImageSaver;
            if (imageSaver == null || !imageSaver.isPendingSave()) {
                z = false;
            }
            if (z) {
                Util.adjBoost();
                return;
            }
            Module module = this.mCurrentModule;
            if (!(module instanceof VideoModule) || (!((VideoModule) module).is4KCamcorder() && !((VideoModule) this.mCurrentModule).is8KCamcorder())) {
                CameraService.addStickyCameraCallable(new AdjBoostCallable());
            } else {
                Util.adjBoost();
            }
        }
    }

    private void closeCameraSetup() {
        ThreadUtils.assertMainThread();
        Log.d(this.TAG, "closeCameraSetup: CameraPendingSetupDisposable: X");
        Disposable disposable = this.mCameraPendingSetupDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mCameraPendingSetupDisposable.dispose();
            this.mCameraPendingSetupDisposable = null;
        }
        Log.d(this.TAG, "closeCameraSetup: CameraSetupDisposable: X");
        Disposable disposable2 = this.mCameraSetupDisposable;
        if (disposable2 != null && !disposable2.isDisposed()) {
            this.mCameraSetupDisposable.dispose();
            this.mCameraSetupDisposable = null;
        }
    }

    private Module createNewModule(StartControl startControl) {
        Module moduleByIndex = ModuleManager.getModuleByIndex(startControl.mTargetMode);
        if (moduleByIndex != null) {
            moduleByIndex.setActivity(this);
            moduleByIndex.fillFeatureControl(startControl);
            moduleByIndex.preTransferOrientation(this.mOrientation, this.mOrientationCompensation);
            return moduleByIndex;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("invalid module index ");
        sb.append(startControl.mTargetMode);
        throw new RuntimeException(sb.toString());
    }

    private void hideHibernationFragment() {
        Fragment findFragmentByTag = getSupportFragmentManager().findFragmentByTag(HibernationFragment.TAG);
        if (findFragmentByTag != null && (findFragmentByTag instanceof DialogFragment)) {
            ((DialogFragment) findFragmentByTag).dismissAllowingStateLoss();
        }
    }

    private boolean interceptTouchEvent(MotionEvent motionEvent) {
        if (this.mMoreChangeCtrl == null) {
            this.mMoreChangeCtrl = (ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179);
        }
        ModeChangeController modeChangeController = this.mMoreChangeCtrl;
        if (modeChangeController == null || !modeChangeController.modeChanging()) {
            if (CameraSettings.isPopupMoreStyle()) {
                if (this.mMoreModePopupController == null) {
                    this.mMoreModePopupController = (MoreModePopupController) ModeCoordinatorImpl.getInstance().getAttachProtocol(2561);
                }
                MoreModePopupController moreModePopupController = this.mMoreModePopupController;
                if (moreModePopupController != null && moreModePopupController.isExpanded()) {
                    return false;
                }
            }
            if (this.mTopAlert == null) {
                this.mTopAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            }
            TopAlert topAlert = this.mTopAlert;
            if (topAlert != null && topAlert.isExtraMenuShowing()) {
                return false;
            }
            if (this.mMainContentProtocolForDispatchTouch == null) {
                this.mMainContentProtocolForDispatchTouch = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            }
            MainContentProtocol mainContentProtocol = this.mMainContentProtocolForDispatchTouch;
            if (mainContentProtocol != null) {
                mainContentProtocol.checkTouchRegionContainSplitFocusExposure(motionEvent);
                if (this.mMainContentProtocolForDispatchTouch.isSplitFocusExposureDown()) {
                    V6GestureRecognizer.getInstance(this).onTouchEvent(motionEvent);
                    return true;
                }
            }
            Module module = this.mCurrentModule;
            if (module != null && (module instanceof Camera2Module)) {
                Camera2Module camera2Module = (Camera2Module) module;
                if (camera2Module.isLongPressedRecording() && motionEvent.getAction() == 262) {
                    camera2Module.onSingleTapUp((int) motionEvent.getX(1), (int) motionEvent.getY(1), false);
                }
            }
            return false;
        } else if (motionEvent.getActionMasked() != 5) {
            return false;
        } else {
            Log.d(this.TAG, "Touch event intercept caz mode change.");
            return true;
        }
    }

    private boolean isFromKeyguard() {
        Intent intent = getIntent();
        boolean z = false;
        if (intent == null) {
            return false;
        }
        String action = intent.getAction();
        if ((TextUtils.equals(action, "android.media.action.STILL_IMAGE_CAMERA") || TextUtils.equals(action, "android.media.action.STILL_IMAGE_CAMERA_SECURE")) && getKeyguardFlag()) {
            z = true;
        }
        return z;
    }

    private boolean isFromOneShotKeyPressed(KeyEvent keyEvent) {
        boolean isTimeout = Util.isTimeout(keyEvent.getEventTime(), this.mLastKeyUpEventTime, 250);
        boolean z = this.mLastKeyDownEventTime > this.mLastKeyUpEventTime;
        if (isTimeout && !z) {
            return false;
        }
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isFromOneShotKeyPressed: lastUpTIme ");
        sb.append(this.mLastKeyUpEventTime);
        sb.append(" | eventTime ");
        sb.append(keyEvent.getEventTime());
        sb.append(" isKeyEventOrderWrong: ");
        sb.append(z);
        Log.i(str, sb.toString());
        return true;
    }

    private boolean isStreamingRequested(Intent intent) {
        return C0122O00000o.instance().OO0o0Oo() && CameraIntentManager.isLaunchByMiWatch(intent);
    }

    /* access modifiers changed from: private */
    public void onDisplayRotationChanged() {
        if (C0124O00000oO.OOooO0o()) {
            FrontRotateNewbieDialogFragment frontRotateNewbieDialogFragment = (FrontRotateNewbieDialogFragment) getSupportFragmentManager().findFragmentByTag(FrontRotateNewbieDialogFragment.TAG);
            if (frontRotateNewbieDialogFragment != null) {
                frontRotateNewbieDialogFragment.animateOut(0);
            }
        }
    }

    private void parseLocationPermission(String[] strArr, int[] iArr) {
        if (PermissionManager.isContainLocationPermissions(strArr)) {
            boolean isCameraLocationPermissionsResultReady = PermissionManager.isCameraLocationPermissionsResultReady(strArr, iArr);
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onRequestPermissionsResult: is location granted = ");
            sb.append(isCameraLocationPermissionsResultReady);
            Log.u(str, sb.toString());
            CameraSettings.updateRecordLocationPreference(isCameraLocationPermissionsResultReady);
            if (!isActivityPaused()) {
                LocationManager.instance().recordLocation(CameraSettings.isRecordLocation());
            }
        }
    }

    private void prefixCamera2Setup() {
        CompletablePreFixCamera2Setup completablePreFixCamera2Setup = new CompletablePreFixCamera2Setup(null, null, null, getIntent(), hashCode(), startFromSecureKeyguard(), this.mCameraIntentManager.checkCallerLegality());
        Completable.create(completablePreFixCamera2Setup).subscribeOn(CameraSchedulers.sCameraSetupScheduler).subscribe();
    }

    private final void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.REBOOT");
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        intentFilter.addAction(CameraIntentManager.ACTION_SPEECH_SHUTTER);
        registerReceiver(this.mReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(CameraIntentManager.ACTION_VOICE_CONTROL);
        registerReceiver(this.mReceiver, intentFilter2, permission.AUX_CONTROL, null);
        this.mDidRegister = true;
    }

    private void registerSDReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MEDIA_EJECT");
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_SCANNER_STARTED");
        intentFilter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
        intentFilter.addDataScheme(ComposerHelper.COMPOSER_PATH);
        registerReceiver(this.mSDReceiver, intentFilter);
    }

    private void releaseAll(boolean z, int i, boolean z2, boolean z3) {
        boolean isFinishing = isFinishing();
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("releaseAll: releaseDevice = ");
        sb.append(z2);
        sb.append(", isCurrentModuleAlive = ");
        sb.append(isCurrentModuleAlive());
        sb.append(", releaseImmediate = ");
        sb.append(z3);
        sb.append(", isFinishing = ");
        sb.append(isFinishing);
        Log.d(str, sb.toString());
        this.mReleaseByModule = false;
        if (ParallelSnapshotManager.isParallelTagOpen) {
            ParallelSnapshotManager.getInstance().setCameraCloseState(true, hashCode());
        }
        Module module = this.mCurrentModule;
        if (module != null) {
            module.setDeparted();
        }
        CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new CameraRunnable(new WeakReference(this), z2, z3), (long) i, TimeUnit.MILLISECONDS);
        this.mImplFactory.detachModulePersistent();
    }

    private void resumeCamera() {
        boolean z;
        boolean z2;
        int i;
        Log.d(this.TAG, "resumeCamera: E");
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        int i2 = 2;
        if (dataItemGlobal.getIntentType() == 1 || dataItemGlobal.getIntentType() == 2) {
            CameraSettings.resetRetainZoom();
        }
        if (!isSwitchingModule()) {
            if (!ModeCoordinatorImpl.isAlive(hashCode())) {
                Log.d(this.TAG, "resumeCamera: module is obsolete");
                unRegisterProtocol();
                registerProtocol();
            } else {
                boolean checkCallerLegality = this.mCameraIntentManager.checkCallerLegality();
                boolean z3 = false;
                if (isJumpBack()) {
                    String str = this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("resumeCamera: from gallery, mReleaseByModule = ");
                    sb.append(this.mReleaseByModule);
                    Log.d(str, sb.toString());
                    this.mJumpedToGallery = false;
                    if (this.mReleaseByModule) {
                        Module module = this.mCurrentModule;
                        if (module != null && module.isShot2GalleryOrEnableParallel()) {
                            this.mCurrentModule.enableCameraControls(true);
                            this.mReleaseByModule = false;
                            return;
                        }
                    }
                    z2 = false;
                    z = false;
                } else {
                    int currentCameraId = dataItemGlobal.getCurrentCameraId();
                    int intentType = dataItemGlobal.getIntentType();
                    int currentMode = dataItemGlobal.getCurrentMode();
                    boolean isCameraAliveWhenResume = isCameraAliveWhenResume();
                    dataItemGlobal.parseIntent(getIntent(), Boolean.valueOf(checkCallerLegality), startFromSecureKeyguard(), false, !isCameraAliveWhenResume);
                    int intentType2 = dataItemGlobal.getIntentType();
                    int currentMode2 = dataItemGlobal.getCurrentMode();
                    int currentCameraId2 = dataItemGlobal.getCurrentCameraId();
                    z2 = currentMode != currentMode2;
                    z = intentType != intentType2;
                    String str2 = "resumeCamera: lastType=";
                    if (intentType != 0) {
                        Module module2 = this.mCurrentModule;
                        if (module2 != null && module2.isSelectingCapturedResult()) {
                            z3 = true;
                        }
                        String str3 = this.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str2);
                        sb2.append(intentType);
                        sb2.append(" curType=");
                        sb2.append(intentType2);
                        sb2.append(" captureFinish=");
                        sb2.append(z3);
                        Log.d(str3, sb2.toString());
                        if (intentType == intentType2 && z3) {
                            resumeCurrentMode(currentMode2);
                            return;
                        } else if (z3) {
                            this.mBaseFragmentDelegate.delegateEvent(6);
                        }
                    } else {
                        String str4 = this.TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str2);
                        sb3.append(intentType);
                        sb3.append(" | mReleaseByModule ");
                        sb3.append(this.mReleaseByModule);
                        Log.d(str4, sb3.toString());
                        if (isCameraAliveWhenResume && currentCameraId == currentCameraId2 && !z2 && !z) {
                            Module module3 = this.mCurrentModule;
                            if (module3 != null) {
                                module3.notifyAfterFirstFrameArrived();
                            }
                            this.mBaseFragmentDelegate.getAnimationComposite().notifyAfterFirstFrameArrived(4);
                            this.mReleaseByModule = false;
                            return;
                        }
                    }
                }
                if (dataItemGlobal.isTimeOut() || z2 || z) {
                    BaseFragmentDelegate baseFragmentDelegate = this.mBaseFragmentDelegate;
                    if (baseFragmentDelegate != null) {
                        if (baseFragmentDelegate.getActiveFragment(R.id.full_screen_feature) == 1048561) {
                            this.mBaseFragmentDelegate.delegateEvent(11);
                        }
                        this.mBaseFragmentDelegate.delegateEvent(7);
                    }
                    i = 3;
                } else {
                    i = 2;
                }
                if (i == 3 || !checkCallerLegality) {
                    if (i == 3 || dataItemGlobal.getCurrentMode() != 179) {
                        i2 = 1;
                    } else if (((VMProcessing) DataRepository.dataItemObservable().get(VMProcessing.class)).getCurrentState() == 7) {
                        Log.d(this.TAG, "resumeCamera: vv combine, return");
                        return;
                    } else {
                        i2 = -1;
                    }
                }
                onModeSelected(StartControl.create(dataItemGlobal.getCurrentMode()).setResetType(i).setViewConfigType(i2));
            }
            Log.d(this.TAG, "resumeCamera: X");
        }
    }

    private void resumeIfNotRecording() {
        if (!isRecording()) {
            if (this.mCameraSound == null) {
                this.mCameraSound = new MiuiCameraSound(this);
            }
            this.mLocationManager.recordLocation(CameraSettings.isRecordLocation());
            this.mCameraBrightness.onResume();
        }
    }

    /* access modifiers changed from: private */
    public void setBrightnessRampRate(int i) {
    }

    /* access modifiers changed from: private */
    public void setScreenEffect(boolean z) {
        DisplayFeatureManager displayFeatureManager = this.mDisplayFeatureManager;
        if (displayFeatureManager != null) {
            displayFeatureManager.setScreenEffect(14, z ? 1 : 0);
        }
    }

    private void setTranslucentNavigation(boolean z) {
        if (Display.checkDeviceHasNavigationBar(this)) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(768);
            window.addFlags(Integer.MIN_VALUE);
        }
    }

    private void setupCamera(StartControl startControl) {
        ThreadUtils.assertMainThread();
        if (this.mIsInRequestRuntimePermission) {
            Log.d(this.TAG, "setupCamera: skipped since we're requesting runtime permission.");
        } else if (isActivityPaused()) {
            Log.w(this.TAG, "setupCamera: skipped since activity is paused!");
            setSwitchingModule(false);
        } else if (!PermissionManager.checkCameraLaunchPermissions()) {
            Log.w(this.TAG, "setupCamera: skipped since some permissions need to be granted");
            setSwitchingModule(false);
        } else if (startControl.mResetType == 4 && startControl.mTargetMode == getCurrentModuleIndex() && this.mCurrentModule.isCreated()) {
            Log.w(this.TAG, "setupCamera: skipped since module has been created");
        } else {
            Log.d(this.TAG, "setupCamera: E");
            closeCameraSetup();
            FunctionCameraPrepare functionCameraPrepare = new FunctionCameraPrepare(startControl.mTargetMode, startControl.mLastMode, startControl.mResetType, startControl.mNeedReConfigureData, (BaseModule) this.mCurrentModule);
            FunctionModuleSetup functionModuleSetup = new FunctionModuleSetup(startControl.mTargetMode);
            FunctionDataSetup functionDataSetup = new FunctionDataSetup(startControl.mTargetMode);
            FunctionUISetup functionUISetup = new FunctionUISetup(startControl.mTargetMode, startControl.needNotifyUI());
            Single map = Single.just(NullHolder.ofNullable(this)).observeOn(CameraSchedulers.sCameraSetupScheduler).map(functionCameraPrepare);
            Single observeOn = Single.create(this.mCamera2OpenOnSubscribe).subscribeOn(CameraSchedulers.sCameraSetupScheduler).observeOn(CameraSchedulers.sCameraSetupScheduler);
            Log.d(this.TAG, "setupCamera: CameraSetupDisposable: E");
            this.mCameraSetupDisposable = map.zipWith(observeOn, this.mCameraOpenResult).map(functionModuleSetup).map(functionDataSetup).observeOn(AndroidSchedulers.mainThread()).map(functionUISetup).subscribe(this.mCameraSetupConsumer);
            Log.d(this.TAG, "setupCamera: X");
        }
    }

    private boolean shouldReleaseLater() {
        return isCurrentModuleAlive() && this.mCurrentModule.shouldReleaseLater();
    }

    /* access modifiers changed from: private */
    public void showCameraError(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(i);
        CameraStatUtils.trackCameraError(sb.toString());
        AftersalesManager.getInstance().count(System.currentTimeMillis(), 4, CameraSettings.getCameraId());
        Message obtain = Message.obtain();
        obtain.what = 10;
        obtain.arg1 = i;
        this.mHandler.sendMessage(obtain);
        if (C0122O00000o.instance().OO0OOOo()) {
            CompatibilityUtils.takebackMotor();
        }
    }

    private void showDebug() {
        if (Util.isShowDebugInfo()) {
            TextView textView = this.mDebugInfoView;
            if (textView != null) {
                textView.setVisibility(0);
            }
            this.mDebugThread = new LogThread();
            this.mDebugThread.start();
        }
        if (this.mDebugInfoView != null && Util.isShowPreviewDebugInfo()) {
            this.mDebugInfoView.setVisibility(0);
        }
    }

    private void showFirstUseHintIfNeeded() {
        if (!startFromSecureKeyguard()) {
            CameraRootView cameraRootView = this.mCameraRootView;
            if (cameraRootView != null) {
                cameraRootView.disableTouchEvent();
            }
            this.mHandler.postDelayed(new Runnable() {
                public /* synthetic */ void O00o0OoO() {
                    if (DataRepository.dataItemGlobal().getBoolean(DataItemGlobal.DATA_COMMON_LPL_SELECTOR_USE_HINT_SHOWN, true)) {
                        Camera.this.showLplSelectDialog();
                    }
                }

                public void run() {
                    if (!Camera.this.isActivityPaused()) {
                        Camera.this.getScreenHint().showFirstUseHint(new O00000Oo(this));
                        CameraRootView cameraRootView = Camera.this.mCameraRootView;
                        if (cameraRootView != null) {
                            cameraRootView.enableTouchEvent();
                        }
                    }
                }
            }, 1000);
        }
    }

    private void showFirstUsePermissionActivity() {
        if (DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_CAMERA_FIRST_USE_PERMISSION_SHOWN, true)) {
            boolean z = Build.IS_INTERNATIONAL_BUILD;
            String str = Util.sRegion;
            if (z && "KR".equals(str)) {
                Intent intent = new Intent("miui.intent.action.APP_PERMISSION_USE");
                ArrayList arrayList = new ArrayList(6);
                arrayList.add(getResources().getString(R.string.permission_contacts));
                arrayList.add(getResources().getString(R.string.permission_location));
                arrayList.add(getResources().getString(R.string.permission_camera));
                arrayList.add(getResources().getString(R.string.permission_phone_state));
                arrayList.add(getResources().getString(R.string.permission_storage));
                arrayList.add(getResources().getString(R.string.permission_microphone));
                intent.putStringArrayListExtra("extra_main_permission_groups", arrayList);
                intent.putExtra("extra_pkgname", CameraIntentManager.CALLER_MIUI_CAMERA);
                try {
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    String str2 = this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("KR Exception:");
                    sb.append(e);
                    Log.i(str2, sb.toString());
                }
            }
        }
    }

    private void showHibernationFragment() {
        HibernationFragment hibernationFragment = new HibernationFragment();
        hibernationFragment.setStyle(2, R.style.DialogFragmentFullScreen);
        getSupportFragmentManager().beginTransaction().add((Fragment) hibernationFragment, HibernationFragment.TAG).commitAllowingStateLoss();
    }

    private void switchEdgeFingerMode(boolean z) {
        if (C0124O00000oO.Oo0o0O0()) {
            CameraSettings.setEdgeMode(this, z);
        }
    }

    private void triggerWatchDog(boolean z) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("triggerWatchDog: ");
        sb.append(z);
        Log.d(str, sb.toString());
        if (C0124O00000oO.O0o0ooO && C0122O00000o.instance().OO0ooOo()) {
            if (z) {
                this.mWatchDog = new WatchDogThread();
                this.mWatchDog.start();
                return;
            }
            Thread thread = this.mWatchDog;
            if (thread != null) {
                thread.interrupt();
                this.mWatchDog = null;
            }
        }
    }

    private void unbindServices() {
        if (this.mIsGalleryServiceBound) {
            unbindService(this.mServiceConnection);
            this.mIsGalleryServiceBound = false;
        }
    }

    private void unregisterSDReceiver() {
        try {
            unregisterReceiver(this.mSDReceiver);
        } catch (Exception e) {
            Log.e(this.TAG, e.getMessage());
        }
    }

    public /* synthetic */ NullHolder O000000o(NullHolder nullHolder) {
        if (!nullHolder.isPresent()) {
            return nullHolder;
        }
        BaseModule baseModule = (BaseModule) nullHolder.get();
        if (!baseModule.isDeparted()) {
            return nullHolder;
        }
        Log.d(this.TAG, "cameraSingle: EXCEPTION_CAMERA_OPEN_CANCEL");
        return NullHolder.ofNullable(baseModule, 225);
    }

    public /* synthetic */ NullHolder O000000o(NullHolder nullHolder, Camera2Result camera2Result) {
        int result = camera2Result.getResult();
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mCameraOpenResult apply: result = ");
        sb.append(result);
        Log.d(str, sb.toString());
        if (result != 2 && result == 3) {
            if (nullHolder.isPresent()) {
                ((BaseModule) nullHolder.get()).setDeparted();
            }
            showCameraError(camera2Result.getCameraError());
        }
        return nullHolder;
    }

    public /* synthetic */ void O000000o(DataItemGlobal dataItemGlobal) {
        if (!CameraSettings.isShowFirstUseHint() && dataItemGlobal.getBoolean(DataItemGlobal.DATA_COMMON_LPL_SELECTOR_USE_HINT_SHOWN, true)) {
            showLplSelectDialog();
        }
    }

    public /* synthetic */ void O00000o(int i) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("checkLensAvailability callback: status = ");
        sb.append(i);
        Log.d(str, sb.toString());
        CameraSettings.setGoogleLensAvailability(i == 0);
    }

    public /* synthetic */ void O00000o0(int i) {
        if (!this.mIsLunchFromAutoTest) {
            if (ThermalDetector.getInstance().thermalConstrained()) {
                if (!isActivityPaused()) {
                    this.mHandler.sendEmptyMessage(3);
                } else {
                    return;
                }
            }
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChanges == null) {
                Log.w(this.TAG, "onThermalNotification config is null");
                return;
            }
            try {
                configChanges.onThermalNotification(i);
            } catch (Exception e) {
                Log.w(this.TAG, "onThermalNotification error", (Throwable) e);
            }
        }
    }

    public /* synthetic */ void O00000oO(int i) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startLensActivity: launch success ? ");
        sb.append(i == 0);
        Log.d(str, sb.toString());
    }

    public /* synthetic */ void O00oOooO() {
        if (!isFinishing()) {
            Fragment findFragmentByTag = getSupportFragmentManager().findFragmentByTag(LensDirtyDetectDialogFragment.TAG);
            if (findFragmentByTag != null && (findFragmentByTag instanceof DialogFragment)) {
                ((DialogFragment) findFragmentByTag).dismissAllowingStateLoss();
            }
        }
    }

    public void boostCameraByThreshold(long j) {
        if (j > 0) {
            try {
                String str = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("boostCameraByThreshold ");
                sb.append(j);
                Log.d(str, sb.toString());
                Class.forName("miui.process.ProcessManager").getDeclaredMethod("boostCameraByThreshold", new Class[]{Long.TYPE}).invoke(null, new Object[]{Long.valueOf(j)});
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                Log.e(this.TAG, e.getMessage(), (Throwable) e);
            }
        }
    }

    public void cancelPresentationDisplay() {
        Handler handler = this.mHandler;
        if (handler != null) {
            if (handler.hasMessages(5)) {
                this.mHandler.removeMessages(5);
            }
            this.mHandler.sendEmptyMessage(5);
        }
    }

    public void changeRequestOrientation() {
        if (C0124O00000oO.OOooO0o()) {
            setRequestedOrientation(CameraSettings.isFrontCamera() ? 7 : 1);
        }
    }

    public void connectionStatus(int i) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("connectionStatus: ");
        sb.append(i);
        Log.d(str, sb.toString());
        if (i == 0) {
            try {
                this.mRemoteControlConnected = true;
                Bundle bundle = new Bundle();
                RemoteControlExtension.setIsGroupOwner(bundle, true);
                this.mRemoteControl.startStreaming(bundle);
            } catch (ServiceExitedException e) {
                Log.d(this.TAG, "connectionStatus: startStreaming failed", (Throwable) e);
            }
        }
    }

    public void customCallback(String str, Bundle bundle) {
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("customCallback:\n\textensionName = ");
        sb.append(str);
        sb.append("\n\tPayload = ");
        sb.append(RemoteControlContract.jsonify(bundle));
        Log.d(str2, sb.toString());
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        ProximitySensorLock proximitySensorLock = this.mProximitySensorLock;
        if (proximitySensorLock != null && proximitySensorLock.intercept(keyEvent)) {
            return true;
        }
        if (this.mMoreChangeCtrl == null) {
            this.mMoreChangeCtrl = (ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179);
        }
        ModeChangeController modeChangeController = this.mMoreChangeCtrl;
        if (modeChangeController == null || !modeChangeController.modeChanging()) {
            return super.dispatchKeyEvent(keyEvent);
        }
        Log.d(this.TAG, "Key event intercept caz mode change.");
        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean z = true;
        if (this.mActivityPaused) {
            return true;
        }
        ProximitySensorLock proximitySensorLock = this.mProximitySensorLock;
        if (proximitySensorLock != null && proximitySensorLock.active()) {
            return true;
        }
        Module module = this.mCurrentModule;
        if (module == null || module.isIgnoreTouchEvent()) {
            return super.dispatchTouchEvent(motionEvent);
        }
        if (interceptTouchEvent(motionEvent)) {
            return true;
        }
        if (!super.dispatchTouchEvent(motionEvent) && !V6GestureRecognizer.getInstance(this).onTouchEvent(motionEvent)) {
            z = false;
        }
        return z;
    }

    public void exitAutoHibernationMode() {
        Log.d(this.TAG, "exitAutoHibernationMode");
        Module module = this.mCurrentModule;
        if (module != null) {
            module.exitAutoHibernation();
        }
    }

    public int getCapturePosture() {
        return this.mSensorStateManager.getCapturePosture();
    }

    public int getCurrentBrightness() {
        return this.mCameraBrightness.getCurrentBrightness();
    }

    public int getCurrentModuleIndex() {
        if (isCurrentModuleAlive()) {
            return this.mCurrentModule.getModuleIndex();
        }
        return 160;
    }

    public ImageSaver getImageSaver() {
        return this.mImageSaver;
    }

    public ImplFactory getImplFactory() {
        return this.mImplFactory;
    }

    public SensorStateManager getSensorStateManager() {
        return this.mSensorStateManager;
    }

    public boolean handleScreenSlideKeyEvent(int i, KeyEvent keyEvent) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("handleScreenSlideKeyEvent ");
        sb.append(i);
        Log.d(str, sb.toString());
        if (C0122O00000o.instance().OOOOoO()) {
            return true;
        }
        if (i == 701 && getCameraIntentManager().isFromScreenSlide().booleanValue() && !isModeSwitched()) {
            finish();
            overridePendingTransition(R.anim.anim_screen_slide_fade_in, R.anim.anim_screen_slide_fade_out);
            return true;
        } else if (isPostProcessing()) {
            return true;
        } else {
            DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
            int currentCameraId = dataItemGlobal.getCurrentCameraId();
            boolean z = false;
            int i2 = i == 700 ? 1 : 0;
            int currentMode = dataItemGlobal.getCurrentMode();
            if ((currentMode == 171 && !C0122O00000o.instance().OOOo0Oo()) || currentMode == 166 || currentMode == 167 || currentMode == 173 || currentMode == 175) {
                currentMode = 163;
            }
            if (currentMode == 163 || currentMode == 165) {
                currentMode = ((DataItemConfig) DataRepository.provider().dataConfig(i2)).getComponentConfigRatio().getMappingModeByRatio(163);
            }
            if (currentCameraId != i2) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
                if (topAlert != null) {
                    topAlert.removeExtraMenu(4);
                }
                if (actionProcessing != null) {
                    actionProcessing.hideExtra();
                }
                ModeChangeController modeChangeController = (ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179);
                if (modeChangeController != null) {
                    modeChangeController.resetModeSelectView(currentMode);
                }
                dataItemGlobal.setCurrentMode(currentMode);
                dataItemGlobal.setCameraId(i2);
                boolean z2 = currentCameraId == 1;
                if (i2 == 1) {
                    z = true;
                }
                ScenarioTrackUtil.trackSwitchCameraStart(z2, z, dataItemGlobal.getCurrentMode());
                onModeSelected(StartControl.create(currentMode).setFromScreenSlide(true).setNeedBlurAnimation(true).setViewConfigType(2));
            } else if (i == 700 && isCurrentModuleAlive()) {
                ((BaseModule) this.mCurrentModule).updateScreenSlide(true);
                MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
                if (Util.isAccessible() && mainContentProtocol != null) {
                    mainContentProtocol.updateContentDescription();
                }
            }
            return true;
        }
    }

    public void hideLensDirtyDetectedHint() {
        if (!C0122O00000o.instance().OOO0o0()) {
            this.mHandler.post(new O00000o0(this));
        }
    }

    public boolean isCurrentModuleAlive() {
        Module module = this.mCurrentModule;
        return module != null && module.isCreated();
    }

    public boolean isIntentPhotoDone() {
        return this.mIntentPhotoDone;
    }

    public boolean isModeSwitched() {
        return this.mIsModeSwitched;
    }

    public boolean isNewBieAlive(int i) {
        String str;
        switch (i) {
            case 1:
                str = PortraitNewbieDialogFragment.TAG;
                break;
            case 2:
                str = FrontRotateNewbieDialogFragment.TAG;
                break;
            case 3:
                str = AiSceneNewbieDialogFragment.TAG;
                break;
            case 4:
                str = UltraWideNewbieDialogFragment.TAG;
                break;
            case 5:
                str = MacroModeNewbieDialogFragment.TAG;
                break;
            case 6:
                str = VVNewbieDialogFragment.TAG;
                break;
            case 7:
                str = UltraTeleNewbieDialogFragment.TAG;
                break;
            default:
                str = null;
                break;
        }
        boolean z = false;
        if (str == null) {
            return false;
        }
        if (getSupportFragmentManager().findFragmentByTag(str) != null) {
            z = true;
        }
        return z;
    }

    public boolean isRecording() {
        return isCurrentModuleAlive() && this.mCurrentModule.isRecording();
    }

    public boolean isScreenSlideOff() {
        return this.mIsScreenSlideOff;
    }

    public boolean isSelectingCapturedResult() {
        return isCurrentModuleAlive() && this.mCurrentModule.isSelectingCapturedResult();
    }

    public boolean isStable() {
        return this.mSensorStateManager.isStable();
    }

    public boolean isStreaming() {
        boolean z = false;
        try {
            if (this.mRemoteControlConnected && this.mRemoteControl != null && this.mRemoteControl.isStreaming()) {
                z = true;
            }
        } catch (ServiceExitedException e) {
            e.printStackTrace();
        }
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isStreaming: ");
        sb.append(z);
        Log.d(str, sb.toString());
        return z;
    }

    public void notifyOnFirstFrameArrived(int i) {
        Handler handler = this.mHandler;
        if (handler != null) {
            if (handler.hasMessages(4)) {
                this.mHandler.removeMessages(4);
            }
            this.mHandler.sendEmptyMessage(4);
        }
        Module module = this.mCurrentModule;
        if (module != null && !module.isDeparted() && !isSwitchingModule()) {
            if (this.mCurrentModule.getModuleIndex() != 254) {
                this.mHandler.sendEmptyMessageDelayed(2, 2000);
            }
            getCameraScreenNail().clearAnimation();
            this.mBaseFragmentDelegate.getAnimationComposite().notifyAfterFirstFrameArrived(i);
            this.mCurrentModule.enableCameraControls(true);
            this.mCurrentModule.setFrameAvailable(true);
            if (this.mModeSelectGaussianTime > 0 && SystemClock.uptimeMillis() - this.mModeSelectGaussianTime > 3000) {
                AftersalesManager.getInstance().count(SystemClock.uptimeMillis(), 3);
                this.mModeSelectGaussianTime = -1;
            }
            if (getCurrentModuleIndex() == 254) {
                getCameraScreenNail().startRealtimeBlur();
            } else if ((getCurrentModuleIndex() == 165 || getCurrentModuleIndex() == 163) && C0124O00000oO.OOooO0o() && CameraSettings.isFrontCamera() && this.mDisplayRotation == 0) {
                String str = "pref_front_camera_first_use_hint_shown_key";
                if (DataRepository.dataItemGlobal().getBoolean(str, true)) {
                    DataRepository.dataItemGlobal().editor().putBoolean(str, false).apply();
                    showNewBie(2);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 1) {
            if (i == 161) {
                if (FileCompat.handleActivityResult(this, i, i2, intent)) {
                    DocumentFile fromTreeUri = DocumentFile.fromTreeUri(this, intent.getData());
                    String str = "Camera";
                    if (fromTreeUri.findFile(str) == null) {
                        fromTreeUri.createDirectory(str);
                        return;
                    }
                    return;
                }
                Log.w(this.TAG, "onActivityResult documents permission not granted");
                PriorityStorageBroadcastReceiver.setPriorityStorage(false);
            }
        } else if (i2 == PERMISSION_RESULT_CODE) {
            DataRepository.dataItemGlobal().putBoolean(CameraSettings.KEY_CAMERA_FIRST_USE_PERMISSION_SHOWN, false);
        }
    }

    public void onAwaken() {
        Log.d(this.TAG, "onAwaken");
        getCameraScreenNail().requestAwaken();
        onModeSelected(this.mStartControl);
    }

    public void onBackPressed() {
        Log.u(this.TAG, "onBackPressed");
        Module module = this.mCurrentModule;
        if (module == null || !module.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void onConfigurationChanged(@androidx.annotation.NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        DataRepository.dataItemConfig().getComponentConfigRatio().cleanDefaultValues();
    }

    public void onCreate(Bundle bundle) {
        Log.d(this.TAG, "onCreate start");
        this.mApplication = (CameraAppImpl) getApplication();
        this.mIsLunchFromAutoTest = SystemProperties.getBoolean("camera.test.auto", false);
        MistatsWrapper.initialize(CameraApplicationDelegate.getAndroidContext());
        trackAppLunchTimeStart(this.mApplication.isApplicationFirstLaunched());
        this.mAppStartTime = System.currentTimeMillis();
        this.mIntentPhotoDone = false;
        this.mCameraIntentManager = CameraIntentManager.getInstance(getIntent());
        this.mCameraIntentManager.setReferer(this);
        if (CompatibilityUtils.isInMultiWindowMode(this)) {
            super.onCreate(null);
            ToastUtils.showToast((Context) this, C0122O00000o.instance().OOOOoo() ? R.string.only_mimoji_multi_window_mode_not_supported : R.string.multi_window_mode_not_supported);
            Log.d(this.TAG, "isInMultiWindowMode call finish");
            finish();
            return;
        }
        if (!CameraIntentManager.ACTION_VOICE_CONTROL.equals(getIntent().getAction()) || this.mCameraIntentManager.checkCallerLegality()) {
            String caller = this.mCameraIntentManager.getCaller();
            if (this.mCameraIntentManager.checkCallerLegality()) {
                CameraStatUtils.trackCallerControl(getIntent(), caller);
            }
            if (C0122O00000o.instance().OOOOoO()) {
                Display.init(this.mApplication);
            }
            Display.initStatusBarHeight(getApplicationContext());
            super.onCreate(bundle);
            Util.updateDeviceConfig(this);
            showFirstUsePermissionActivity();
            if (!getKeyguardFlag()) {
                if (CameraSettings.isShowFirstUseHint()) {
                    showFirstUseHintIfNeeded();
                } else if (PermissionManager.checkCameraLaunchPermissions()) {
                    Log.d(this.TAG, "onCreate(): prefixCamera2Setup");
                    prefixCamera2Setup();
                } else {
                    this.mIsInRequestRuntimePermission = !PermissionManager.requestCameraPermissions(this, 100);
                }
            }
            if (ProximitySensorLock.enabled() && isFromKeyguard()) {
                if (!Util.isNonUIEnabled() || !this.mCameraIntentManager.isFromVolumeKey().booleanValue()) {
                    if (ProximitySensorLock.supported()) {
                        this.mProximitySensorLock = new ProximitySensorLock(this);
                        this.mProximitySensorLock.startWatching();
                    }
                } else if (Util.isNonUI()) {
                    CameraStatUtils.trackPocketModeEnter(NonUI.POCKET_MODE_NONUI_ENTER_VOLUME);
                    Log.d(this.TAG, "Finish from NonUI mode.");
                    finish();
                    return;
                }
            }
            EffectController.releaseInstance();
            setContentView(R.layout.v9_main);
            getWindow().setBackgroundDrawable(null);
            this.mGLView = (V6CameraGLSurfaceView) findViewById(R.id.v6_gl_surface_view);
            this.mGLCoverView = (ImageView) findViewById(R.id.gl_root_cover);
            this.mDebugInfoView = (TextView) findViewById(R.id.camera_debug_content);
            LayoutParams layoutParams = (LayoutParams) this.mDebugInfoView.getLayoutParams();
            layoutParams.topMargin = Util.getDisplayRect().top;
            this.mDebugInfoView.setLayoutParams(layoutParams);
            this.mEdgeShutterView = (V9EdgeShutterView) findViewById(R.id.v9_edge_shutter_view);
            this.mCameraRootView = (CameraRootView) findViewById(R.id.camera_app_root);
            if (this.mCameraLayoutRotation == null) {
                this.mCameraLayoutRotation = new CommonRotationLayout(getWindow());
            }
            this.mSensorStateManager = new SensorStateManager(this, getMainLooper());
            this.mOrientationListener = new MyOrientationEventListener(this);
            createCameraScreenNail(false, false);
            this.mCamera2OpenOnSubscribe = new Camera2OpenOnSubscribe(this);
            registerProtocol();
            if (C0124O00000oO.Oo00Oo()) {
                try {
                    this.mDisplayFeatureManager = DisplayFeatureManager.getInstance();
                } catch (Exception e) {
                    Log.w(this.TAG, "DisplayFeatureManager init failed", (Throwable) e);
                }
            }
            setTranslucentNavigation(true);
            EffectChangedListenerController.setHoldKey(hashCode());
            if (C0124O00000oO.OOo0oOo()) {
                FrameLayout frameLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.layout_google_lens, null);
                this.mCameraRootView.addView(frameLayout);
                O00000o.getInstance().O000000o((Activity) this, (View) this.mGLView, (ViewGroup) frameLayout);
            } else if (CameraSettings.supportGoogleLens()) {
                this.mLensApi = new LensApi(this);
                this.mLensApi.checkLensAvailability(new C0127O00000oo(this));
                Log.d(this.TAG, "Bind Lens service: E");
                this.mLensApi.onResume();
                Log.d(this.TAG, "Bind Lens service: X");
            }
            showDebug();
            this.mCurrentDisplayMode = DataRepository.dataItemGlobal().getDisplayMode();
            ThermalDetector.getInstance().onCreate(CameraAppImpl.getAndroidContext());
            ViberatorContext.getInstance(getBaseContext()).setSnapClickVibratorEnable(C0122O00000o.instance().OOoOO00());
            if (isStreamingRequested(getIntent()) && this.mRemoteControl == null) {
                Log.d(this.TAG, "onCreate: bind rcs");
                this.mRemoteControlConnected = false;
                this.mRemoteControl = RemoteControl.getRemoteControl(this, this, this, this);
            }
            this.mSupportOrientation = C0122O00000o.instance().O000OOoo(Display.getDisplayRatio());
            Coverage.initCoverageService(this);
            Log.d(this.TAG, "onCreate end");
            return;
        }
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("An illegal caller:");
        sb.append(this.mCameraIntentManager.getCaller());
        sb.append(" use VOICE_CONTROL_INTENT!");
        Log.e(str, sb.toString());
        super.onCreate(null);
        finish();
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0096  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00a1  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00b9  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00e2  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00f4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDestroy() {
        ImageSaver imageSaver;
        SensorStateManager sensorStateManager;
        LogThread logThread;
        ProximitySensorLock proximitySensorLock;
        CameraIntentManager cameraIntentManager;
        DataItemGlobal dataItemGlobal;
        Log.d(this.TAG, "onDestroy start");
        super.onDestroy();
        int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
        int i = 163;
        if (currentMode == 185) {
            dataItemGlobal = DataRepository.dataItemGlobal();
            if (C0122O00000o.instance().OO0oo00()) {
                i = 210;
            }
        } else if (currentMode == 179) {
            dataItemGlobal = DataRepository.dataItemGlobal();
            if (C0122O00000o.instance().OOO00o()) {
                i = 209;
            }
        } else {
            if (currentMode == 213 || currentMode == 207 || currentMode == 208 || currentMode == 212 || currentMode == 189) {
                dataItemGlobal = DataRepository.dataItemGlobal();
                i = 211;
            }
            AutoLockManager.removeInstance(this);
            unRegisterProtocol();
            ThermalDetector.getInstance().onDestroy();
            imageSaver = this.mImageSaver;
            if (imageSaver != null) {
                imageSaver.onHostDestroy();
            }
            sensorStateManager = this.mSensorStateManager;
            if (sensorStateManager != null) {
                sensorStateManager.onDestroy();
            }
            this.mDisplayFeatureManager = null;
            V6GestureRecognizer.onDestroy(this);
            EffectChangedListenerController.removeEffectChangedListenerMap(hashCode());
            logThread = this.mDebugThread;
            if (logThread != null) {
                logThread.setRunFlag(false);
            }
            proximitySensorLock = this.mProximitySensorLock;
            if (proximitySensorLock != null) {
                proximitySensorLock.destroy();
            }
            cameraIntentManager = this.mCameraIntentManager;
            if (cameraIntentManager != null) {
                cameraIntentManager.destroy();
                this.mCameraIntentManager = null;
            }
            if (getScreenHint() != null) {
                getScreenHint().dismissSystemChoiceDialog();
            }
            CameraIntentManager.removeAllInstance();
            if (!C0124O00000oO.OOo0oOo()) {
                O00000o.getInstance().release();
            } else if (CameraSettings.supportGoogleLens()) {
                if (this.mLensApi == null) {
                    Log.w(this.TAG, "onDestroy: mLensApi is null!");
                } else {
                    Log.d(this.TAG, "Unbind Lens service: E");
                    this.mLensApi.onPause();
                    Log.d(this.TAG, "Unbind Lens service: X");
                }
            }
            if (this.mStreamingSurfaceRenderThread != null) {
                Log.d(this.TAG, "onDestroy: release glrender thread");
                this.mStreamingSurfaceRenderThread.quit();
                this.mStreamingSurfaceRenderThread = null;
            }
            if (this.mRemoteControl != null) {
                Log.d(this.TAG, "onDestroy: release rcs");
                this.mRemoteControl.release();
                this.mRemoteControl = null;
                this.mRemoteControlConnected = false;
            }
            ((VMResource) DataRepository.dataItemObservable().get(VMResource.class)).onDestroy();
            Log.d(this.TAG, "onDestroy end");
        }
        dataItemGlobal.setCurrentMode(i);
        AutoLockManager.removeInstance(this);
        unRegisterProtocol();
        ThermalDetector.getInstance().onDestroy();
        imageSaver = this.mImageSaver;
        if (imageSaver != null) {
        }
        sensorStateManager = this.mSensorStateManager;
        if (sensorStateManager != null) {
        }
        this.mDisplayFeatureManager = null;
        V6GestureRecognizer.onDestroy(this);
        EffectChangedListenerController.removeEffectChangedListenerMap(hashCode());
        logThread = this.mDebugThread;
        if (logThread != null) {
        }
        proximitySensorLock = this.mProximitySensorLock;
        if (proximitySensorLock != null) {
        }
        cameraIntentManager = this.mCameraIntentManager;
        if (cameraIntentManager != null) {
        }
        if (getScreenHint() != null) {
        }
        CameraIntentManager.removeAllInstance();
        if (!C0124O00000oO.OOo0oOo()) {
        }
        if (this.mStreamingSurfaceRenderThread != null) {
        }
        if (this.mRemoteControl != null) {
        }
        ((VMResource) DataRepository.dataItemObservable().get(VMResource.class)).onDestroy();
        Log.d(this.TAG, "onDestroy end");
    }

    public void onHibernate() {
        Log.d(this.TAG, "onHibernate");
        if (isDestroyed()) {
            AutoLockManager.getInstance(this).removeMessage();
            return;
        }
        showHibernationFragment();
        getCameraScreenNail().requestHibernate();
        CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new HibernateRunnable(this.mCurrentModule));
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        int i2 = i;
        KeyEvent keyEvent2 = keyEvent;
        if (this.mActivityPaused) {
            return super.onKeyDown(i, keyEvent);
        }
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onKeyDown: keycode ");
        sb.append(i2);
        Log.u(str, sb.toString());
        if (keyEvent.getRepeatCount() == 0 && (i2 == 66 || i2 == 27 || i2 == 88 || i2 == 87 || i2 == 24 || i2 == 25)) {
            if (this.mLastKeyDownEventTime == 0 || keyEvent.getEventTime() >= this.mLastKeyDownEventTime) {
                String volumeCameraFunction = CameraSettings.getVolumeCameraFunction("");
                if (this.mLastKeyDownEventTime == 0 || !isFromOneShotKeyPressed(keyEvent2) || volumeCameraFunction.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_volume))) {
                    this.mLastIgnoreKey = -1;
                    this.mLastKeyDownEventTime = keyEvent.getEventTime();
                } else {
                    String str2 = this.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onKeyDown: isFromOneShotKeyPressed and return! keyCode is ");
                    sb2.append(i2);
                    Log.d(str2, sb2.toString());
                }
            }
            this.mLastIgnoreKey = i2;
            this.mLastKeyDownEventTime = 0;
            return true;
        } else if (keyEvent.getRepeatCount() > 0 && i2 == this.mLastIgnoreKey) {
            this.mLastIgnoreKey = -1;
        }
        boolean z = false;
        if (i2 == 700) {
            this.mIsScreenSlideOff = false;
        } else if (i2 == 701) {
            this.mIsScreenSlideOff = true;
        }
        if (!isCurrentModuleAlive() || !this.mCurrentModule.isFrameAvailable()) {
            if (i2 == 24 || i2 == 25 || i2 == 27 || i2 == 66 || i2 == 80 || i2 == 87 || i2 == 88) {
                return true;
            }
            return (i2 == 700 || i2 == 701) ? handleScreenSlideKeyEvent(i, keyEvent) : super.onKeyDown(i, keyEvent);
        } else if (C0124O00000oO.O0o0OOo && (i2 == 24 || i2 == 25 || i2 == 87 || i2 == 88)) {
            return super.onKeyDown(i, keyEvent);
        } else {
            if (this.mCurrentModule.onKeyDown(i2, keyEvent2) || super.onKeyDown(i, keyEvent)) {
                z = true;
            }
            return z;
        }
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (this.mActivityPaused) {
            return super.onKeyUp(i, keyEvent);
        }
        boolean z = false;
        if (i == 4 && (!keyEvent.isTracking() || keyEvent.isCanceled())) {
            Log.d(this.TAG, "onKeyUp: keyCode KeyEvent.KEYCODE_BACK is not isTracking or isCanceled");
            return false;
        } else if (i == this.mLastIgnoreKey) {
            this.mLastKeyUpEventTime = 0;
            this.mLastIgnoreKey = -1;
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onKeyUp: key is lastIgnore key   keyCode : ");
            sb.append(i);
            Log.d(str, sb.toString());
            return true;
        } else {
            this.mLastKeyUpEventTime = keyEvent.getEventTime();
            String str2 = this.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onKeyUp: mLastKeyUpEventTime ");
            sb2.append(this.mLastKeyUpEventTime);
            sb2.append(" keyCode : ");
            sb2.append(i);
            Log.u(str2, sb2.toString());
            if (this.mCurrentModule == null) {
                return super.onKeyUp(i, keyEvent);
            }
            if (C0124O00000oO.O0o0OOo && (i == 24 || i == 25 || i == 87 || i == 88)) {
                return super.onKeyUp(i, keyEvent);
            }
            if (this.mCurrentModule.onKeyUp(i, keyEvent) || super.onKeyUp(i, keyEvent)) {
                z = true;
            }
            return z;
        }
    }

    public void onLifeAlive() {
        String str;
        String str2;
        ThreadUtils.assertMainThread();
        Log.d(this.TAG, String.format(Locale.ENGLISH, "onLifeAlive module 0x%x, need anim %d, need blur %b need reconfig %b reset type %d", new Object[]{Integer.valueOf(this.mStartControl.mTargetMode), Integer.valueOf(this.mStartControl.mViewConfigType), Boolean.valueOf(this.mStartControl.mNeedBlurAnimation), Boolean.valueOf(this.mStartControl.mNeedReConfigureCamera), Integer.valueOf(this.mStartControl.mResetType)}));
        String str3 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onLifeAlive: isFromKeyguard: ");
        sb.append(isFromKeyguard());
        sb.append(", mHasBeenSetupOnFocusChanged: ");
        sb.append(this.mHasBeenSetupOnFocusChanged);
        sb.append(", mHasFocus: ");
        sb.append(this.mHasFocus);
        Log.d(str3, sb.toString());
        if (!isFromKeyguard()) {
            str = this.TAG;
            str2 = "onLifeAlive: setupCamera: !keyguard";
        } else if ((this.mHasBeenSetupOnFocusChanged || this.mHasFocus) && this.mCameraSetupDisposable == null) {
            str = this.TAG;
            str2 = "onLifeAlive: setupCamera: focused";
        } else {
            return;
        }
        Log.d(str, str2);
        setupCamera(this.mStartControl);
    }

    public void onLifeDestroy(String str) {
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onLifeDestroy ");
        sb.append(str);
        Log.d(str2, sb.toString());
    }

    public void onLifeStart(String str) {
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onLifeStart ");
        sb.append(str);
        Log.d(str2, sb.toString());
    }

    public void onLifeStop(String str) {
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onLifeStop ");
        sb.append(str);
        Log.d(str2, sb.toString());
    }

    @MainThread
    public void onModeSelected(StartControl startControl) {
        ThreadUtils.assertMainThread();
        int currentModuleIndex = getCurrentModuleIndex();
        this.mIsModeSwitched = this.mStartControl != null;
        this.mIsScreenSlideOff = Util.isScreenSlideOff(this);
        Log.u(this.TAG, String.format(Locale.ENGLISH, "onModeSelected from 0x%x to 0x%x, ScreenSlideOff = %b", new Object[]{Integer.valueOf(currentModuleIndex), Integer.valueOf(startControl.mTargetMode), Boolean.valueOf(this.mIsScreenSlideOff)}));
        if (currentModuleIndex != 160 && !CameraStatUtils.modeIdToName(currentModuleIndex).equals(CameraStatUtils.modeIdToName(startControl.mTargetMode))) {
            this.mHandler.removeMessages(2);
            ScenarioTrackUtil.trackSwitchModeStart(currentModuleIndex, startControl.mTargetMode, CameraSettings.isFrontCamera());
        }
        if (startControl.mTargetMode != 163) {
            if (ParallelSnapshotManager.isParallelTagOpen) {
                ParallelSnapshotManager.getInstance().setCameraCloseState(true, hashCode());
            }
        } else if (ParallelSnapshotManager.isParallelTagOpen) {
            ParallelSnapshotManager.getInstance().setCameraCloseState(false, hashCode());
        }
        if (this.mRemoteControl != null) {
            try {
                Bundle bundle = new Bundle();
                RemoteControlContract.setPreviousCapturingMode(bundle, currentModuleIndex);
                RemoteControlContract.setCurrentCapturingMode(bundle, startControl.mTargetMode);
                int errorCode = RemoteControlContract.getErrorCode(this.mRemoteControl.customRequest(RemoteControlExtension.CUSTOM_REQUEST_SET_CAPTURING_MODE, bundle));
                if (errorCode != 0) {
                    String str = this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("send payload failed with error code: ");
                    sb.append(errorCode);
                    Log.d(str, sb.toString());
                }
            } catch (ServiceExitedException e) {
                Log.d(this.TAG, "send payload failed", (Throwable) e);
            }
        }
        closeCameraSetup();
        this.mStartControl = startControl;
        ModuleManager.setActiveModuleIndex(startControl.mTargetMode);
        Completable completable = null;
        if (!startControl.mNeedReConfigureCamera) {
            this.mBaseFragmentDelegate.delegateMode(null, startControl, null);
            return;
        }
        BaseFragmentDelegate baseFragmentDelegate = this.mBaseFragmentDelegate;
        if (baseFragmentDelegate != null) {
            baseFragmentDelegate.getAnimationComposite().setClickEnable(false);
        }
        this.mModeSelectGaussianTime = -1;
        if (startControl.mNeedBlurAnimation) {
            this.mModeSelectGaussianTime = SystemClock.uptimeMillis();
        }
        setSwitchingModule(true);
        if (!isCurrentModuleAlive()) {
            startControl.mNeedBlurAnimation = false;
            getWindow().clearFlags(128);
        }
        V6GestureRecognizer.getInstance(this).setCurrentModule(null);
        BaseModule baseModule = (BaseModule) this.mCurrentModule;
        if (baseModule != null) {
            baseModule.setDeparted();
            startControl.setLastMode(baseModule.getModuleIndex());
        }
        this.mCurrentModule = createNewModule(startControl);
        String str2 = this.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onModeSelected: module instantiated: ");
        sb2.append(this.mCurrentModule);
        Log.d(str2, sb2.toString());
        if (this.mBaseFragmentDelegate == null) {
            Log.d(this.TAG, "onModeSelected: without prefix");
            this.mBaseFragmentDelegate = new BaseFragmentDelegate(this);
            this.mBaseFragmentDelegate.init(getSupportFragmentManager(), startControl.mTargetMode, this);
        } else {
            Log.d(this.TAG, "onModeSelected: with prefix");
            boolean checkCallerLegality = this.mCameraIntentManager.checkCallerLegality();
            if (PermissionManager.checkCameraLaunchPermissions()) {
                CompletablePreFixCamera2Setup completablePreFixCamera2Setup = new CompletablePreFixCamera2Setup(baseModule, startControl, getCameraScreenNail(), null, hashCode(), startFromSecureKeyguard(), checkCallerLegality);
                completable = Completable.create(completablePreFixCamera2Setup).subscribeOn(CameraSchedulers.sCameraSetupScheduler).observeOn(AndroidSchedulers.mainThread());
            }
            Log.d(this.TAG, "CameraPendingSetupDisposable: E");
            this.mCameraPendingSetupDisposable = this.mBaseFragmentDelegate.delegateMode(completable, startControl, this);
        }
        this.mBaseFragmentDelegate.lazyLoadFragment(R.id.full_screen, 4086);
        this.mBaseFragmentDelegate.batchFragmentRequest(startControl.getFeatureFragmentAlias());
        if (this.mSupportOrientation) {
            CommonRotationLayout commonRotationLayout = this.mCameraLayoutRotation;
            if (commonRotationLayout != null) {
                commonRotationLayout.provideOrientationChanged(startControl.mTargetMode, this.mOrientationCompensation);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        Log.d(this.TAG, "onNewIntent start");
        setIntent(intent);
        super.onNewIntent(intent);
        this.mCameraIntentManager.destroy();
        this.mIntentPhotoDone = false;
        this.mCameraIntentManager = CameraIntentManager.getInstance(intent);
        this.mCameraIntentManager.setReferer(this);
        this.mJumpedToGallery = false;
        Log.d(this.TAG, "onNewIntent end");
    }

    public void onPause() {
        pauseActivity(true);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        String str = "ignore this onRequestPermissionsResult callback";
        if (i == 100) {
            int i2 = 0;
            this.mIsInRequestRuntimePermission = false;
            if (!PermissionManager.isCameraLaunchPermissionsResultReady(strArr, iArr)) {
                String str2 = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onRequestPermissionsResult: no permission finish, ");
                sb.append(Arrays.toString(strArr));
                sb.append(Arrays.toString(iArr));
                Log.w(str2, sb.toString());
                finish();
                return;
            } else if (strArr.length == 0 && iArr.length == 0) {
                Log.w(this.TAG, str);
                return;
            } else {
                while (true) {
                    if (i2 >= strArr.length) {
                        break;
                    }
                    if ("android.permission.CAMERA".equals(strArr[i2])) {
                        Camera2DataContainer.getInstance().init((CameraManager) CameraAppImpl.getAndroidContext().getSystemService("camera"));
                        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
                        if (localBinder != null) {
                            localBinder.updateVirtualCameraIds();
                        }
                    } else {
                        i2++;
                    }
                }
                Log.d(this.TAG, "onRequestPermissionsResult: prefixCamera2Setup");
                prefixCamera2Setup();
                setupCamera(this.mStartControl);
            }
        } else {
            if (i == 101) {
                if (strArr.length == 0 && iArr.length == 0) {
                    Log.w(this.TAG, str);
                    return;
                }
            }
        }
        parseLocationPermission(strArr, iArr);
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        Log.d(this.TAG, "onRestart start");
        trackAppLunchTimeStart(false);
        this.mAppStartTime = System.currentTimeMillis();
        if (!getKeyguardFlag()) {
            if (CameraSettings.isShowFirstUseHint()) {
                showFirstUseHintIfNeeded();
            } else if (DataRepository.dataItemGlobal().getBoolean(DataItemGlobal.DATA_COMMON_LPL_SELECTOR_USE_HINT_SHOWN, true)) {
                showLplSelectDialog();
            }
        }
        Log.d(this.TAG, "onRestart end");
    }

    public void onResume() {
        resumeActivity(true);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        Log.d(this.TAG, "onSaveInstanceState");
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        Log.d(this.TAG, "onStart start");
        this.mActivityStarted = true;
        super.onStart();
        BaseFragmentDelegate baseFragmentDelegate = this.mBaseFragmentDelegate;
        if (baseFragmentDelegate != null) {
            baseFragmentDelegate.getAnimationComposite().onStart();
        }
        Log.d(this.TAG, "onStart end");
    }

    public void onStop() {
        Log.d(this.TAG, "onStop start");
        super.onStop();
        removeNewBie();
        this.mActivityStopped = true;
        closeCameraSetup();
        setSwitchingModule(false);
        this.mHasBeenSetupOnFocusChanged = false;
        BaseFragmentDelegate baseFragmentDelegate = this.mBaseFragmentDelegate;
        if (baseFragmentDelegate != null) {
            baseFragmentDelegate.getAnimationComposite().onStop();
        }
        if (!this.mReleaseByModule && this.mDelayReleaseCamera) {
            releaseAll(true, true);
        }
        unbindServices();
        if (!isGotoGallery() && !isGotoSettings()) {
            MiAlgoAsdSceneProfile.clearInitASDScenes();
        }
        if (getCurrentModule() != null && (getCurrentModule() instanceof Camera2Module)) {
            ((Camera2Module) getCurrentModule()).resetMagneticInfo();
        }
        Log.d(this.TAG, "onStop end");
    }

    public void onSurfaceTextureUpdated(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
        if (this.mStreamingSurfaceRenderThread != null) {
            CameraScreenNail cameraScreenNail = getCameraScreenNail();
            if (cameraScreenNail != null) {
                this.mStreamingSurfaceRenderThread.setStreamSize(cameraScreenNail.getWidth(), cameraScreenNail.getHeight());
                this.mStreamingSurfaceRenderThread.draw(drawExtTexAttribute, this.mOrientationCompensation);
            }
        }
    }

    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onTrimMemory: level=");
        sb.append(i);
        Log.v(str, sb.toString());
        MemoryHelper.setTrimLevel(i);
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        Log.d(this.TAG, "onUserInteraction");
        AutoLockManager.getInstance(this).onUserInteraction();
        if (isCurrentModuleAlive()) {
            this.mCurrentModule.onUserInteraction();
        }
    }

    public void onWindowFocusChanged(boolean z) {
        boolean isFromKeyguard = isFromKeyguard();
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onWindowFocusChanged: hasFocus: ");
        sb.append(z);
        sb.append(", isFromKeyguard: ");
        sb.append(isFromKeyguard);
        sb.append(", mHasBeenSetupOnFocusChanged ");
        sb.append(this.mHasBeenSetupOnFocusChanged);
        Log.d(str, sb.toString());
        this.mHasFocus = z;
        super.onWindowFocusChanged(z);
        if (isFromKeyguard && this.mHasFocus && this.mCameraSetupDisposable == null && !this.mHasBeenSetupOnFocusChanged && !isCurrentModuleAlive()) {
            Log.d(this.TAG, "onWindowFocusChanged: setupCamera: keyguard");
            this.mHasBeenSetupOnFocusChanged = true;
            setupCamera(this.mStartControl);
        }
        if (this.mCurrentModule != null) {
            if (((AutoHibernation) ModeCoordinatorImpl.getInstance().getAttachProtocol(936)) == null) {
                this.mCurrentModule.onWindowFocusChanged(z);
                CameraBrightness cameraBrightness = this.mCameraBrightness;
                if (cameraBrightness != null) {
                    cameraBrightness.onWindowFocusChanged(z);
                }
            }
            if (z) {
                Util.checkLockedOrientation(this);
                this.mCurrentModule.checkActivityOrientation();
                SensorStateManager sensorStateManager = this.mSensorStateManager;
                if (sensorStateManager != null) {
                    sensorStateManager.register();
                }
            } else {
                SensorStateManager sensorStateManager2 = this.mSensorStateManager;
                if (sensorStateManager2 != null) {
                    sensorStateManager2.unregister(SensorStateManager.SENSOR_ALL);
                }
            }
        }
    }

    @MainThread
    public void pauseActivity(boolean z) {
        Log.d(this.TAG, "onPause start");
        if (C0122O00000o.instance().OOOOoOO()) {
            this.mCameraRootView.onHostPause();
        }
        CameraSettings.setLensIndex(0);
        CameraSettings.setMacro2Sat(false);
        this.mAppStartTime = 0;
        this.mActivityPaused = true;
        this.mActivityStarted = false;
        this.mIsInRequestRuntimePermission = false;
        getContentResolver().unregisterContentObserver(this.mScreenSlideStatusObserver);
        switchEdgeFingerMode(false);
        this.mOrientationListener.disable();
        AutoLockManager.getInstance(this).onPause();
        hideHibernationFragment();
        AutoHibernation autoHibernation = (AutoHibernation) ModeCoordinatorImpl.getInstance().getAttachProtocol(936);
        if (autoHibernation != null) {
            autoHibernation.dismissAutoHibernation();
        }
        setBrightnessRampRate(-1);
        setScreenEffect(false);
        DialogFragment dialogFragment = this.lplDialog;
        if (dialogFragment != null) {
            dialogFragment.dismiss();
            this.lplDialog = null;
        }
        getWindow().clearFlags(128);
        if (this.mDidRegister) {
            unregisterReceiver(this.mReceiver);
            this.mDidRegister = false;
        }
        unregisterSDReceiver();
        super.onPause();
        getWindow().clearFlags(1024);
        if (this.mCameraScreenNail != null && !isShowBottomIntentDone()) {
            if ((C0122O00000o.instance().OOoO() || CameraSettings.isCameraParallelProcessEnable()) && this.mJumpFlag == 1) {
                this.mCameraScreenNail.doPreviewGaussianForever();
            } else if (!C0122O00000o.instance().OOOOOoO() && ModuleManager.getActiveModuleIndex() != 254) {
                Log.d(this.TAG, "onPause: readLastFrameGaussian...");
                this.mCameraScreenNail.readLastFrameGaussian();
            }
        }
        Disposable disposable = this.mGLCoverDisposable;
        if (disposable != null) {
            disposable.dispose();
        }
        AlertDialog alertDialog = this.mErrorDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        pauseIfNotRecording();
        if (startFromKeyguard() && this.mIsFinishInKeyguard) {
            boolean isChangingConfigurations = isChangingConfigurations();
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPause: clearFlag --> FLAG_TURN_SCREEN_ON and isChangingConfigurations is ");
            sb.append(isChangingConfigurations);
            Log.d(str, sb.toString());
            getWindow().clearFlags(2097152);
            if (this.mJumpFlag == 0 && !isChangingConfigurations) {
                boolean isStreaming = isStreaming();
                String str2 = this.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onPause: isStreaming = ");
                sb2.append(isStreaming);
                Log.d(str2, sb2.toString());
                if (!isStreaming) {
                    finish();
                }
            }
        }
        if (this.mJumpFlag == 0 && (startFromSecureKeyguard() || isGalleryLocked())) {
            this.mSecureUriList = null;
            this.mThumbnailUpdater.setThumbnail(null, true, false);
        } else if (this.mJumpFlag == 1) {
            clearNotification();
        }
        this.mHandler.removeMessages(1);
        if (C0124O00000oO.OOo0oOo()) {
            O00000o.getInstance().onPause();
        }
        if (getScreenHint() != null) {
            getScreenHint().cancelHint();
        }
        CameraRootView cameraRootView = this.mCameraRootView;
        if (cameraRootView != null) {
            cameraRootView.enableTouchEvent();
        }
        ImageSaver imageSaver = this.mImageSaver;
        if (imageSaver != null) {
            imageSaver.onHostPause();
        }
        MistatsWrapper.recordPageEnd("CameraActivity");
        this.mReleaseByModule = false;
        if (shouldReleaseLater()) {
            Log.d(this.TAG, "release by module");
            this.mReleaseByModule = true;
            DataRepository.dataItemGlobal().resetTimeOut();
            this.mCurrentModule.onHostStopAndNotifyActionStop();
        }
        this.mDelayReleaseCamera = true;
        if (!this.mReleaseByModule && isGotoGallery()) {
            this.mDelayReleaseCamera = false;
            releaseAll(false, 350, true, true);
        }
        if (ThermalDetector.getInstance().thermalConstrained()) {
            Log.w(this.TAG, "onThermalNotification finish activity now");
            finish();
        }
        ThermalDetector.getInstance().unregisterReceiver();
        triggerWatchDog(false);
        boostParallelServiceAdj();
        if (this.mRemoteControl != null) {
            boolean isStreaming2 = isStreaming();
            String str3 = this.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("onPause: stop rcs: isStreaming = ");
            sb3.append(isStreaming2);
            Log.d(str3, sb3.toString());
            if (!isStreaming2) {
                try {
                    this.mRemoteControl.stopStreaming(new Bundle());
                } catch (ServiceExitedException e) {
                    Log.d(this.TAG, "onPause: stopStreaming failed", (Throwable) e);
                }
            }
        }
        Log.d(this.TAG, "onPause end");
    }

    public void pauseIfNotRecording() {
        if (!isRecording()) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mCameraBrightness.onPause();
            Thread thread = this.mCloseActivityThread;
            if (thread != null) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.mCloseActivityThread = null;
            }
            LocationManager locationManager = this.mLocationManager;
            if (locationManager != null) {
                locationManager.recordLocation(false);
            }
            if (this.mPreviewThumbnail) {
                this.mThumbnailUpdater.setThumbnail(null, false, false);
                this.mPreviewThumbnail = false;
            } else {
                ThumbnailUpdater thumbnailUpdater = this.mThumbnailUpdater;
                if (thumbnailUpdater != null) {
                    thumbnailUpdater.saveThumbnailToFile();
                    this.mThumbnailUpdater.cancelTask();
                }
            }
        }
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.forceDestroy();
        ModeCoordinatorImpl.create(hashCode());
        EffectChangedListenerController.setHoldKey(hashCode());
        this.mImplFactory = new ImplFactory();
        int i = 3;
        this.mImplFactory.initBase(this, 171, 929, 933);
        if (C0122O00000o.instance().OOoOO0O()) {
            this.mImplFactory.initBase(this, 239);
        }
        if (C0124O00000oO.OOOOOo()) {
            this.mImplFactory.initBase(this, 255);
        }
        if (C0122O00000o.instance().OOOOO0()) {
            this.mImplFactory.initBase(this, 945);
        }
        if (C0122O00000o.instance().OOOOoO()) {
            this.mImplFactory.initBase(this, 938);
        }
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        getCameraIntentManager();
        DataItemGlobal dataItemGlobal2 = dataItemGlobal;
        dataItemGlobal2.parseIntent(getIntent(), Boolean.valueOf(this.mCameraIntentManager.checkCallerLegality()), startFromSecureKeyguard(), false, true);
        if (!dataItemGlobal.isTimeOut()) {
            i = 2;
        }
        onModeSelected(StartControl.create(dataItemGlobal.getCurrentMode()).setResetType(i));
    }

    public void releaseAll(boolean z, boolean z2) {
        if (isActivityStopped() || !z) {
            releaseAll(z, 0, z2, true);
        } else {
            this.mReleaseByModule = false;
        }
    }

    public void removeNewBie() {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("removeNewBie = ");
        sb.append(this.newbieDialogFragmentTag);
        Log.d(str, sb.toString());
        getCameraScreenNail().drawBlackFrame(false);
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (this.newbieDialogFragmentTag != null) {
            Fragment findFragmentByTag = getSupportFragmentManager().findFragmentByTag(this.newbieDialogFragmentTag);
            if (findFragmentByTag != null) {
                beginTransaction.remove(findFragmentByTag);
            }
        }
        beginTransaction.commitAllowingStateLoss();
    }

    public void restoreWindowBrightness() {
        float f;
        CameraBrightness cameraBrightness = this.mCameraBrightness;
        if (cameraBrightness != null) {
            int currentBrightness = cameraBrightness.getCurrentBrightness();
            if (currentBrightness > 0) {
                f = ((float) currentBrightness) / 255.0f;
                WindowManager.LayoutParams attributes = getWindow().getAttributes();
                attributes.screenBrightness = f;
                getWindow().setAttributes(attributes);
                setBrightnessRampRate(-1);
                setScreenEffect(false);
            }
        }
        f = -1.0f;
        WindowManager.LayoutParams attributes2 = getWindow().getAttributes();
        attributes2.screenBrightness = f;
        getWindow().setAttributes(attributes2);
        setBrightnessRampRate(-1);
        setScreenEffect(false);
    }

    @MainThread
    public void resumeActivity(boolean z) {
        this.mLastJumpFlag = this.mJumpFlag;
        Log.d(this.TAG, "onResume start");
        if (C0122O00000o.instance().OOOOoOO()) {
            this.mCameraRootView.onHostResume();
        }
        if (CompatibilityUtils.isInMultiWindowMode(this)) {
            ToastUtils.showToast((Context) this, C0122O00000o.instance().OOOOoo() ? R.string.only_mimoji_multi_window_mode_not_supported : R.string.multi_window_mode_not_supported);
            Log.d(this.TAG, "isInMultiWindowMode call finish");
            finish();
        }
        if (getKeyguardFlag() && !PermissionManager.checkCameraLaunchPermissions()) {
            Log.w(this.TAG, "start from keyguard, not grant lunch permission, finish activity");
            finish();
        }
        showBlurCover();
        if (!(Display.isNotchScreenHidden() == Display.isNotchScreenHidden(this) && Display.isFullScreenNavBarHidden() == Util.isFullScreenNavBarHidden(this))) {
            Util.initialize(this);
            Display.initStatusBarHeight(getApplicationContext());
            if (Display.isContentViewExtendToTopEdges()) {
                CompatibilityUtils.setCutoutModeShortEdges(getWindow());
            }
        }
        AutoLockManager.getInstance(this).onResume();
        ProximitySensorLock proximitySensorLock = this.mProximitySensorLock;
        if (proximitySensorLock != null) {
            proximitySensorLock.onResume();
        }
        getContentResolver().registerContentObserver(Util.SCREEN_SLIDE_STATUS_SETTING_URI, false, this.mScreenSlideStatusObserver);
        MistatsWrapper.recordPageStart("CameraActivity");
        Util.checkLockedOrientation(this);
        this.mActivityPaused = false;
        this.mActivityStopped = false;
        switchEdgeFingerMode(true);
        this.mFirstOrientationArrived = false;
        this.mOrientationListener.enable();
        super.onResume();
        this.mJumpFlag = 0;
        Util.clearSeamlessRotation(this);
        this.mCameraErrorShown = false;
        checkGalleryLock();
        checkKeyguardFlag();
        resumeIfNotRecording();
        this.mIsScreenSlideOff = Util.isScreenSlideOff(this);
        if (C0124O00000oO.OOo0oOo()) {
            O00000o.getInstance().onResume();
        }
        Storage.initStorage(this);
        if (Storage.isUseDocumentMode() && !FileCompat.hasStoragePermission(Storage.DIRECTORY)) {
            if (!getKeyguardFlag()) {
                Log.w(this.TAG, "start request documents permission");
                FileCompat.getStorageAccessForLOLLIPOP(this, Storage.DIRECTORY);
                return;
            }
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("documents permission not granted, getKeyguardFlag = ");
            sb.append(getKeyguardFlag());
            Log.w(str, sb.toString());
            PriorityStorageBroadcastReceiver.setPriorityStorage(false);
        }
        if (getScreenHint() != null) {
            getScreenHint().updateHint();
        }
        registerReceiver();
        registerSDReceiver();
        resumeCamera();
        this.mIsModeSwitched = false;
        ThermalDetector.getInstance().registerReceiver(this.mThermalNotificationListener);
        boolean z2 = this.mCameraIntentManager.isImageCaptureIntent() || this.mCameraIntentManager.isVideoCaptureIntent();
        if (this.mImageSaver == null) {
            this.mImageSaver = new ImageSaver(this, this.mHandler, z2);
        }
        this.mImageSaver.onHostResume(z2);
        bindServices();
        triggerWatchDog(true);
        Util.updateAccessibility(this);
        if (this.mIsInRequestRuntimePermission) {
            this.mCameraRootView.post(new C0128O0000OoO(this));
        } else if (!CameraSettings.isShowFirstUseHint() && DataRepository.dataItemGlobal().getBoolean(DataItemGlobal.DATA_COMMON_LPL_SELECTOR_USE_HINT_SHOWN, true)) {
            showLplSelectDialog();
        }
        this.mLockStreamOrientation = !CameraIntentManager.isLaunchByMiWatch(getIntent());
        if (isStreamingRequested(getIntent())) {
            if (this.mRemoteControl == null) {
                Log.d(this.TAG, "onResume: bind rcs");
                this.mRemoteControlConnected = false;
                this.mRemoteControl = RemoteControl.getRemoteControl(this, this, this, this);
            }
            String str2 = this.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onResume: start rcs: already connected = ");
            sb2.append(this.mRemoteControlConnected);
            Log.d(str2, sb2.toString());
            if (this.mRemoteControlConnected) {
                try {
                    Bundle bundle = new Bundle();
                    RemoteControlExtension.setIsGroupOwner(bundle, true);
                    this.mRemoteControl.startStreaming(bundle);
                } catch (ServiceExitedException e) {
                    Log.d(this.TAG, "onResume: startStreaming failed", (Throwable) e);
                }
            }
        }
        NotifyExternalManager.getInstance(getApplicationContext()).notifyCameraResume();
        Log.d(this.TAG, "onResume end");
    }

    public void resumeCurrentMode(int i) {
        ThreadUtils.assertMainThread();
        closeCameraSetup();
        setSwitchingModule(true);
        FunctionResumeModule functionResumeModule = new FunctionResumeModule(i);
        Single map = Single.just(NullHolder.ofNullable((BaseModule) this.mCurrentModule)).observeOn(CameraSchedulers.sCameraSetupScheduler).map(new O0000O0o(this));
        Single observeOn = Single.create(this.mCamera2OpenOnSubscribe).subscribeOn(CameraSchedulers.sCameraSetupScheduler).observeOn(CameraSchedulers.sCameraSetupScheduler);
        Log.d(this.TAG, "resumeCurrentMode: CameraSetupDisposable: E");
        this.mCameraSetupDisposable = map.zipWith(observeOn, this.mCameraOpenResult).map(functionResumeModule).observeOn(AndroidSchedulers.mainThread()).subscribe(this.mCameraSetupConsumer);
    }

    @Deprecated
    public void setBlockingLifeCycles(List list) {
    }

    public void setIntnetPhotoDone(boolean z) {
        this.mIntentPhotoDone = z;
    }

    public void setVideoStreamEffect(int i) {
        RenderThread renderThread = this.mStreamingSurfaceRenderThread;
        if (renderThread != null) {
            renderThread.setFilterId(i);
        }
    }

    public void setWindowBrightness(int i) {
        setBrightnessRampRate(0);
        setScreenEffect(true);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.screenBrightness = ((float) i) / 255.0f;
        getWindow().setAttributes(attributes);
    }

    public void showGuide() {
        showGuide(DataRepository.dataItemGlobal().getCurrentMode());
    }

    public void showGuide(int i) {
        boolean isCtsCall = getCameraIntentManager().isCtsCall();
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("showGuide = ");
        sb.append(i);
        sb.append("  isCtsCall = ");
        sb.append(isCtsCall);
        Log.d(str, sb.toString());
        if (!isCtsCall && !ThermalDetector.getInstance().thermalConstrained()) {
            DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
            O00000o o00000o = new O00000o(this, dataItemGlobal);
            BaseDialogFragment baseDialogFragment = null;
            if (i != 162 && i != 163 && i == 182 && C0122O00000o.instance().OOO0OoO() && !isNewBieAlive(8) && dataItemGlobal.getBoolean("pref_camera_first_id_card_mode_use_hint_shown_key", true)) {
                baseDialogFragment = showNewBie(8);
            }
            if (baseDialogFragment != null) {
                baseDialogFragment.setDismissCallback(o00000o);
            } else {
                o00000o.onDismiss();
            }
        }
    }

    public void showLensDirtyDetectedHint() {
        if (C0122O00000o.instance().OOO0o0()) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    FragmentTopConfig fragmentTopConfig = (FragmentTopConfig) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (fragmentTopConfig != null) {
                        fragmentTopConfig.alertAiDetectTipHint(0, R.string.dirty_tip_toast, 3000);
                    }
                }
            });
            return;
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        String str = LensDirtyDetectDialogFragment.TAG;
        if (supportFragmentManager.findFragmentByTag(str) == null) {
            LensDirtyDetectDialogFragment lensDirtyDetectDialogFragment = new LensDirtyDetectDialogFragment();
            lensDirtyDetectDialogFragment.setStyle(2, R.style.LensDirtyDetectDialogFragment);
            getSupportFragmentManager().beginTransaction().add((Fragment) lensDirtyDetectDialogFragment, str).commitAllowingStateLoss();
        }
    }

    public void showLplSelectDialog() {
        if (this.lplDialog == null && C0122O00000o.instance().OOO0oOo() && C0124O00000oO.Oo00Ooo()) {
            this.lplDialog = new LongPressLiveFragment();
            this.lplDialog.setStyle(2, R.style.DialogFragmentFullScreen);
            String str = "ThermalDialog";
            getSupportFragmentManager().beginTransaction().add((Fragment) this.lplDialog, str).commitAllowingStateLoss();
            this.newbieDialogFragmentTag = str;
        }
    }

    public BaseDialogFragment showNewBie(int i) {
        if (!(i == 2 || getCameraScreenNail() == null)) {
            getCameraScreenNail().drawBlackFrame(true);
        }
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("showNewBie newBieType=");
        sb.append(i);
        Log.u(str, sb.toString());
        if (i == 2) {
            FrontRotateNewbieDialogFragment frontRotateNewbieDialogFragment = new FrontRotateNewbieDialogFragment();
            frontRotateNewbieDialogFragment.setStyle(2, R.style.DialogFragmentFullScreen);
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            String str2 = FrontRotateNewbieDialogFragment.TAG;
            beginTransaction.add((Fragment) frontRotateNewbieDialogFragment, str2).commitAllowingStateLoss();
            this.newbieDialogFragmentTag = str2;
            return frontRotateNewbieDialogFragment;
        } else if (i != 8) {
            return null;
        } else {
            IDCardModeNewbieDialogFragment iDCardModeNewbieDialogFragment = new IDCardModeNewbieDialogFragment();
            iDCardModeNewbieDialogFragment.setStyle(2, R.style.IDCardModeNewBieFragment);
            FragmentTransaction beginTransaction2 = getSupportFragmentManager().beginTransaction();
            String str3 = IDCardModeNewbieDialogFragment.TAG;
            beginTransaction2.add((Fragment) iDCardModeNewbieDialogFragment, str3).commitAllowingStateLoss();
            this.newbieDialogFragmentTag = str3;
            DataRepository.dataItemGlobal().editor().putBoolean("pref_camera_first_id_card_mode_use_hint_shown_key", false).apply();
            return iDCardModeNewbieDialogFragment;
        }
    }

    public void showNewNotification() {
        if (!startFromSecureKeyguard() && !Util.isGlobalVersion() && !this.mIsLunchFromAutoTest && !TextUtils.isEmpty(C0122O00000o.instance().O0ooooO())) {
            DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
            String str = CameraSettings.KEY_CAMERA_FIRST_NOTIFICATION_SHOWN;
            if (!dataItemGlobal.getBoolean(str, false)) {
                LocaleList locales = getResources().getConfiguration().getLocales();
                if (locales != null && locales.size() >= 1) {
                    Locale locale = locales.get(0);
                    if (locale.getDisplayName().equals(Locale.CHINA.getDisplayName()) || locale.getDisplayName().equals(Locale.US.getDisplayName())) {
                        DataRepository.dataItemGlobal().editor().putBoolean(str, true).apply();
                        Intent intent = new Intent("com.miui.miservice.MISERVICE_NOTIFICATION");
                        intent.putExtra("intent_extra_key_flag", "2");
                        intent.putExtra("intent_extra_key_label", C0122O00000o.instance().O0ooooO());
                        intent.putExtra("intent_extra_key_title", getString(R.string.miservice_notification_title));
                        intent.putExtra("intent_extra_key_content", getString(R.string.miservice_notification_content));
                        intent.putExtra("intent_extra_key_is_to_main", true);
                        intent.addFlags(32);
                        intent.addFlags(16777216);
                        sendBroadcast(intent);
                    } else {
                        Log.w(this.TAG, "showNewNotification: locale does not match, return...");
                        return;
                    }
                }
                return;
            }
        }
        Log.w(this.TAG, "showNewNotification: return...");
    }

    public void startLensActivity() {
        if (this.mLensApi != null) {
            boolean checkLensAvailability = CameraSettings.checkLensAvailability(getApplicationContext());
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startLensActivity: isAvailable = ");
            sb.append(checkLensAvailability);
            Log.d(str, sb.toString());
            if (checkLensAvailability) {
                this.mLensApi.launchLensActivity((Activity) this, (LensLaunchStatusCallback) new C0126O00000oO(this));
            }
        }
    }

    public void streamingServerStatus(int i, Bundle bundle) {
        String str;
        StringBuilder sb;
        String str2;
        if (i == 1) {
            str = this.TAG;
            sb = new StringBuilder();
            str2 = "streaming server error occurred: ";
        } else if (i == 2) {
            i = RemoteControlContract.getStreamingServerPort(bundle);
            str = this.TAG;
            sb = new StringBuilder();
            str2 = "streaming server started, port = ";
        } else {
            return;
        }
        sb.append(str2);
        sb.append(i);
        Log.d(str, sb.toString());
    }

    public void streamingSessionStatus(int i, Bundle bundle) {
        String str;
        String str2;
        if (i == 2) {
            Log.d(this.TAG, "onStreamingStarted");
            if (bundle == null) {
                str = this.TAG;
                str2 = "onStreamingStarted: invalid session";
            } else {
                Surface codecInputSurface = RemoteControlContract.getCodecInputSurface(bundle);
                if (codecInputSurface == null) {
                    str = this.TAG;
                    str2 = "onStreamingStarted: invalid codec surface";
                } else if (this.mStreamingSurfaceRenderThread != null) {
                    str = this.TAG;
                    str2 = "onStreamingStarted: too many clients connected?";
                } else if (getCurrentModule() == null) {
                    str = this.TAG;
                    str2 = "onStreamingStarted: mode not available";
                } else {
                    CameraScreenNail cameraScreenNail = getCameraScreenNail();
                    if (cameraScreenNail == null) {
                        str = this.TAG;
                        str2 = "onStreamingStarted: screennail not available";
                    } else {
                        V6CameraGLSurfaceView gLView = getGLView();
                        if (gLView == null) {
                            str = this.TAG;
                            str2 = "onStreamingStarted: surfaceview not available";
                        } else {
                            EGLContext eGLContext14 = gLView.getEGLContext14();
                            if (eGLContext14 == null) {
                                str = this.TAG;
                                str2 = "onStreamingStarted: EGLContext not available";
                            } else {
                                RenderThread renderThread = new RenderThread("StreamingSurfaceRenderThread", cameraScreenNail.getWidth(), cameraScreenNail.getHeight(), eGLContext14, codecInputSurface, true);
                                this.mStreamingSurfaceRenderThread = renderThread;
                                this.mStreamingSurfaceRenderThread.setOrientationLocked(this.mLockStreamOrientation);
                                Size videoStreamSize = RemoteControlContract.getVideoStreamSize(bundle);
                                if (videoStreamSize.getWidth() <= 0 || videoStreamSize.getHeight() <= 0) {
                                    str = this.TAG;
                                    str2 = "onStreamingStarted: illegal video size";
                                } else {
                                    this.mStreamingSurfaceRenderThread.setCanvasSize(videoStreamSize.getWidth(), videoStreamSize.getHeight());
                                    String str3 = this.TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("onStreamingStarted: video quality: ");
                                    sb.append(videoStreamSize);
                                    Log.d(str3, sb.toString());
                                    this.mStreamingSurfaceRenderThread.start();
                                    this.mStreamingSurfaceRenderThread.waitUntilReady();
                                }
                            }
                        }
                    }
                }
            }
            Log.d(str, str2);
        } else if (i != 3) {
            String str4 = this.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unknown streaming status: ");
            sb2.append(i);
            Log.d(str4, sb2.toString());
        } else {
            Log.d(this.TAG, "onStreamingStopped");
            RenderThread renderThread2 = this.mStreamingSurfaceRenderThread;
            if (renderThread2 != null) {
                renderThread2.quit();
                this.mStreamingSurfaceRenderThread = null;
            }
        }
    }

    public void unRegisterProtocol() {
        ImplFactory implFactory = this.mImplFactory;
        if (implFactory != null) {
            implFactory.detachBase();
        }
        BaseFragmentDelegate baseFragmentDelegate = this.mBaseFragmentDelegate;
        if (baseFragmentDelegate != null) {
            baseFragmentDelegate.unRegisterProtocol();
            this.mBaseFragmentDelegate = null;
        }
    }

    public void updateSurfaceState(int i) {
        super.updateSurfaceState(i);
        if (i == 4) {
            this.mCamera2OpenOnSubscribe.onGlSurfaceCreated();
            if (ModuleManager.isCapture()) {
                Module module = this.mCurrentModule;
                if (module != null) {
                    ((BaseModule) module).updatePreviewSurface();
                } else {
                    Log.w(this.TAG, "updateSurfaceState: module has not been initialized");
                }
            }
        }
    }
}
