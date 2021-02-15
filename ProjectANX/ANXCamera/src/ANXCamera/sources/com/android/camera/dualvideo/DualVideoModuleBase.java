package com.android.camera.dualvideo;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraCaptureSession;
import android.os.Looper;
import android.util.Range;
import android.util.Size;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.SurfaceTextureScreenNail.ExternalFrameProcessor;
import com.android.camera.Util;
import com.android.camera.constant.AutoFocus;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.dualvideo.recorder.MultiRecorderManager;
import com.android.camera.dualvideo.remote.RemoteService;
import com.android.camera.dualvideo.render.LayoutType;
import com.android.camera.dualvideo.render.RenderManager;
import com.android.camera.dualvideo.render.RenderManager.DualVideoRenderListener;
import com.android.camera.dualvideo.render.RenderTrigger;
import com.android.camera.dualvideo.render.RenderUtil;
import com.android.camera.dualvideo.util.DualVideoConfigManager;
import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import com.android.camera.dualvideo.util.RenderSourceType;
import com.android.camera.dualvideo.util.UserSelectData;
import com.android.camera.dualvideo.view.RotateAnimator;
import com.android.camera.dualvideo.view.TouchEventView;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.log.Log;
import com.android.camera.module.VideoBase;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.FocusManager2;
import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.DualVideoRenderProtocol;
import com.android.camera.protocol.ModeProtocol.ModuleContent;
import com.android.camera.protocol.ModeProtocol.RemoteCameraServiceProtocol;
import com.android.camera.protocol.ModeProtocol.StandaloneRecorderProtocol;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.TopConfigProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.DualVideoAttr;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.FocusCallback;
import com.android.camera2.CameraCapabilities;
import com.xiaomi.camera.device.CameraHandlerThread.Cookie;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DualVideoModuleBase extends VideoBase implements TopConfigProtocol {
    private static final boolean CONNECT_DIRECTLY_WITHOUT_IDM = true;
    private boolean mCoverAnimNeed = true;
    private volatile int mCurrentTapId;
    private final FocusCallback mFocusCallback = new C0140O0000OoO(this);
    protected boolean mKeepRecorderWhenSwitching;
    protected boolean mMainFrameIsAvailable;
    private boolean mMainFrameReady = false;
    protected MultiRecorderManager mMultiRecorderManager;
    private RemoteService mRemoteService;
    private RenderManager mRenderManager;
    /* access modifiers changed from: private */
    public RenderTrigger mRenderTrigger;
    protected RotateAnimator mRotateAnimator;
    private Camera2Proxy mSubCamera2Device;
    private boolean mSubFocusAreaSupported;
    private FocusManager2 mSubFocusManager;
    /* access modifiers changed from: private */
    public boolean mSubFrameReady = false;
    private boolean mSubMeteringAreaSupported;

    class MyDualVideoRenderListener implements DualVideoRenderListener {
        MyDualVideoRenderListener() {
        }

        public void onAuxSourceImageAvailable() {
            DualVideoModuleBase.this.mRenderTrigger.subFrameAvailable();
            DualVideoModuleBase.this.mSubFrameReady = true;
            DualVideoModuleBase.this.tryAnimBlackCover();
        }

        public void onLayoutTypeChanged() {
            if (DualVideoModuleBase.this.isRecording() && !DualVideoModuleBase.this.isRecordingPaused()) {
                String str = ((RenderManager) DualVideoModuleBase.this.getRenderManager().get()).hasMiniComposeType() ? DualVideoAttr.VALUE_PREVIEW_MINI : DualVideoAttr.VALUE_PREVIEW_EQUAL;
                CameraStatUtils.trackDualVideoCommonAttr(DualVideoAttr.ATTR_LAYOUT_TYPE, str);
                CameraStatUtils.mLayoutType = str;
            }
        }

        public void onRenderRequestNeeded() {
            DualVideoModuleBase.this.getActivity().getGLView().requestRender();
        }
    }

    public DualVideoModuleBase(String str) {
        super(str);
    }

    static /* synthetic */ boolean O000000o(Camera2Proxy camera2Proxy, UserSelectData userSelectData) {
        return userSelectData.getCameraId() == camera2Proxy.getId();
    }

    static /* synthetic */ void O00000Oo(Camera2Proxy camera2Proxy) {
        if (camera2Proxy.isFacingFront()) {
            camera2Proxy.startFaceDetection();
        }
    }

    static /* synthetic */ void O00000o0(Camera2Proxy camera2Proxy) {
        camera2Proxy.setExposureCompensation(0);
        camera2Proxy.setAWBLock(false);
        CameraSettings.resetExposure();
        camera2Proxy.resumePreview();
    }

    static /* synthetic */ boolean O00000o0(UserSelectData userSelectData, ConfigItem configItem) {
        return configItem.mLayoutType == userSelectData.getmSelectWindowLayoutType();
    }

    static /* synthetic */ boolean O0000o0O(ConfigItem configItem) {
        return configItem.mCameraId == 1000;
    }

    /* access modifiers changed from: private */
    /* renamed from: addViewForGestureRecognize */
    public void O000000o(ViewGroup viewGroup) {
        TouchEventView touchEventView = new TouchEventView(viewGroup.getContext());
        touchEventView.setListener(new O000O0OO(this));
        viewGroup.addView(touchEventView, new LayoutParams(-1, -1));
    }

    /* access modifiers changed from: private */
    public void applyZoomForDevices(@NonNull Camera2Proxy camera2Proxy) {
        CameraSettings.getDualVideoConfig().getSelectedData().stream().filter(new O0000o0(camera2Proxy)).forEach(new O000O00o(camera2Proxy));
    }

    private Optional getActionProcess() {
        return Optional.ofNullable((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162));
    }

    private Optional getFocusManager(int i) {
        FocusManager2 focusManager2;
        if (i == this.mCamera2Device.getId()) {
            focusManager2 = this.mFocusManager;
        } else {
            Camera2Proxy camera2Proxy = this.mSubCamera2Device;
            if (camera2Proxy == null || i != camera2Proxy.getId()) {
                return Optional.empty();
            }
            focusManager2 = this.mSubFocusManager;
        }
        return Optional.ofNullable(focusManager2);
    }

    private void initDualVideoController() {
        ModuleContent moduleContent = (ModuleContent) ModeCoordinatorImpl.getInstance().getAttachProtocol(431);
        if (moduleContent != null) {
            getActivity().runOnUiThread(new O000OOo0(this, moduleContent.getParent()));
        }
    }

    private void initRenderTrigger() {
        this.mRenderTrigger = new RenderTrigger(getActivity().getGLView(), C0124O00000oO.O0o0o0 ? 24 : 30);
    }

    private void reStartCurrentModule() {
        Log.d(VideoBase.TAG, "switchSelecteWindowToRecord: ");
        if (!isSwitching() && this.mMainFrameIsAvailable) {
            getRenderManager().ifPresent(C0167O000Oooo.INSTANCE);
            getRenderManager().ifPresent(O000OO0o.INSTANCE);
            getActivity().getGLView().requestRender();
            switchCameraLens(false, false, false, 7);
        }
    }

    private void registerRecorderManager() {
        if (((StandaloneRecorderProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(429)) == null) {
            getActivity().getImplFactory().initModulePersistent(getActivity(), 429);
        }
        this.mMultiRecorderManager = ((StandaloneRecorderProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(429)).getRecorderManager(this.mActivity.getImageSaver());
        this.mMediaRecorderRecording = this.mMultiRecorderManager.isRecording();
        this.mMediaRecorderRecordingPaused = this.mMultiRecorderManager.isRecordingPaused();
    }

    private void registerRemoteService() {
        if (((RemoteCameraServiceProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(422)) == null) {
            getActivity().getImplFactory().initModulePersistent(getActivity(), 422);
        }
        this.mRemoteService = ((RemoteCameraServiceProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(422)).getService();
    }

    @SuppressLint({"CheckResult"})
    private void registerRenderManager() {
        if (((DualVideoRenderProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(430)) == null) {
            getActivity().getImplFactory().initModulePersistent(getActivity(), 430);
        }
        this.mRenderManager = ((DualVideoRenderProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(430)).getRenderManager();
        getRenderManager().ifPresent(new C0151O0000ooo(this));
        Single.create(new C0144O0000oO(this)).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new O000OOOo(this));
        getActivity().getCameraScreenNail().setExternalFrameProcessor((ExternalFrameProcessor) getRenderManager().get());
    }

    private void startMainPreviewSession() {
        Log.d(VideoBase.TAG, "startPreviewSession");
        if (isDeviceAlive()) {
            checkDisplayOrientation();
            this.mCamera2Device.setFocusCallback(this);
            this.mCamera2Device.setErrorCallback(this.mErrorCallback);
            this.mCamera2Device.setPictureSize(this.mPreviewSize);
            Surface surface = new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture());
            this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
            this.mCamera2Device.startVideoPreviewSession(surface, 0, 0, null, getOperatingMode(), false, this);
            this.mFocusManager.resetFocused();
            this.mPreviewing = true;
        }
    }

    private void startPreviewSession() {
        startMainPreviewSession();
        initDualVideoController();
        getSubCamera2Device().ifPresent(new O000O0o0(this));
    }

    private void updateFpsRange() {
        if (C0124O00000oO.O0o0o0) {
            Range create = Range.create(Integer.valueOf(24), Integer.valueOf(24));
            this.mCamera2Device.setFpsRange(create);
            getSubCamera2Device().ifPresent(new C0146O0000oOO(create));
        }
    }

    private void updateSubCameraFocusMode(int i) {
        getSubCamera2Device().ifPresent(new C0145O0000oO0(this, i));
    }

    public /* synthetic */ void O000000o(int i, Camera2Proxy camera2Proxy) {
        if (getSubCamera2Device() == null || camera2Proxy.getCapabilities() == null) {
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updateSubCameraFocusMode: focusMode = ");
            sb.append(i);
            sb.append(", but device is null...");
            Log.w(str, sb.toString());
            return;
        }
        if (Util.isSupported(i, camera2Proxy.getCapabilities().getSupportedFocusModes())) {
            camera2Proxy.setFocusMode(i);
        }
    }

    public /* synthetic */ void O000000o(LayoutType layoutType, Point point, boolean z, FocusManager2 focusManager2) {
        focusManager2.setRenderComposeType(layoutType);
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onSingleTapUp: id = ");
        sb.append(this.mCurrentTapId);
        sb.append(", is main camera = ");
        sb.append(this.mCurrentTapId == this.mCamera2Device.getId());
        sb.append(", type = ");
        sb.append(layoutType);
        Log.d(str, sb.toString());
        focusManager2.onSingleTapUp(point.x, point.y, z);
    }

    public /* synthetic */ void O000000o(FocusTask focusTask) {
        if (1 == focusTask.getFocusTrigger()) {
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("focusTime=");
            sb.append(focusTask.getElapsedTime());
            sb.append("ms focused=");
            sb.append(focusTask.isSuccess());
            sb.append(" waitForRecording=");
            sb.append(this.mSubFocusManager.isFocusingSnapOnFinish());
            Log.d(str, sb.toString());
            this.mMainProtocol.setFocusViewType(true);
            this.mSubFocusManager.onFocusResult(focusTask);
            this.mActivity.getSensorStateManager().reset();
            if (focusTask.isSuccess() && this.m3ALocked) {
                getSubCamera2Device().ifPresent(C0141O0000Ooo.INSTANCE);
            }
        }
    }

    public /* synthetic */ void O000000o(SingleEmitter singleEmitter) {
        singleEmitter.onSuccess(Integer.valueOf(this.mOrientation));
    }

    public /* synthetic */ void O000000o(boolean z, ActionProcessing actionProcessing) {
        this.mActivity.runOnUiThread(new C0149O0000oo0(actionProcessing, z));
    }

    public /* synthetic */ boolean O000000o(MotionEvent motionEvent) {
        return ((Boolean) getRenderManager().map(new O000OO(motionEvent)).orElse(Boolean.valueOf(false))).booleanValue();
    }

    public /* synthetic */ void O00000Oo(RenderManager renderManager) {
        renderManager.setListener(new MyDualVideoRenderListener());
    }

    public /* synthetic */ void O00000o0(RenderManager renderManager) {
        renderManager.setOrientation(this.mOrientation);
    }

    public /* synthetic */ void O00000o0(boolean z, ActionProcessing actionProcessing) {
        this.mActivity.runOnUiThread(new C0148O0000oo(actionProcessing, z));
    }

    public /* synthetic */ void O00000oO(Camera2Proxy camera2Proxy) {
        int i = this.mEvState;
        if (i != 2) {
            if (i == 1 || i == 3) {
                camera2Proxy.setExposureCompensation(this.mEvValue);
                if (this.mEvState == 1) {
                    if (this.mEvValue != 0) {
                        camera2Proxy.setAWBLock(true);
                    }
                }
            }
            camera2Proxy.resumePreview();
        }
        camera2Proxy.setAWBLock(false);
        camera2Proxy.resumePreview();
    }

    public /* synthetic */ void O00000oo(Camera2Proxy camera2Proxy) {
        camera2Proxy.startVideoPreviewSession((Surface) getRenderManager().map(C0142O0000o0O.INSTANCE).orElse(null), 0, 0, null, Camera2DataContainer.getInstance().isFrontCameraId(camera2Proxy.getId()) ? CameraCapabilities.SESSION_OPERATION_MODE_VIDEO_BEAUTY : 32772, false, this);
    }

    public /* synthetic */ void O0000O0o(Camera2Proxy camera2Proxy) {
        this.mSubFocusManager.setAeAwbLock(false);
        camera2Proxy.setAELock(false);
    }

    public /* synthetic */ void O0000OOo(Camera2Proxy camera2Proxy) {
        if (camera2Proxy.isFacingFront()) {
            camera2Proxy.setBeautyValues(this.mBeautyValues);
        }
    }

    public /* synthetic */ void O0000OOo(Integer num) {
        ((RenderManager) getRenderManager().get()).setOrientation(num.intValue());
    }

    public /* synthetic */ void O0000Oo0(Camera2Proxy camera2Proxy) {
        camera2Proxy.setModuleParameter(this.mModuleIndex, Camera2DataContainer.getInstance().isFrontCameraId(camera2Proxy.getId()) ? 1 : 0);
    }

    public /* synthetic */ void O00oo() {
        synchronized (getActivity()) {
            try {
                getActivity().wait(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        getTopAlert().ifPresent(O000000o.INSTANCE);
        getActionProcess().ifPresent(O00000o0.INSTANCE);
    }

    /* access modifiers changed from: protected */
    public void applyZoomRatio() {
        if (!isMultiCameraMode()) {
            super.applyZoomRatio();
            return;
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            applyZoomForDevices(camera2Proxy);
            getSubCamera2Device().ifPresent(new O000OOo(this));
        }
    }

    public void cancelFocus(boolean z) {
        if (isDeviceAlive()) {
            if (!isFrameAvailable()) {
                Log.e(VideoBase.TAG, "cancelFocus: frame not available");
                return;
            }
            if (this.mCurrentTapId == this.mCamera2Device.getId()) {
                super.cancelFocus(z);
            } else if (getSubCamera2Device().isPresent()) {
                String str = VideoBase.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("cancelFocus: sub camera resetFocusMode = ");
                sb.append(z);
                Log.d(str, sb.toString());
                if (z) {
                    updateSubCameraFocusMode(3);
                }
                this.mActivity.getSensorStateManager().setFocusSensorEnabled(false);
                ((Camera2Proxy) getSubCamera2Device().get()).cancelFocus(this.mModuleIndex);
                if (getCameraState() != 3) {
                    setCameraState(1);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"SwitchIntDef"})
    public void consumePreference(int... iArr) {
        for (int i : iArr) {
            if (i == 1) {
                updatePictureAndPreviewSize();
            } else if (i == 3) {
                updateFocusArea();
            } else if (i == 19) {
                updateFpsRange();
            } else if (i == 24) {
                applyZoomRatio();
            } else if (i == 31) {
                updateVideoStabilization();
            } else if (i == 55) {
                updateModuleRelated();
            } else if (i == 9) {
                updateAntiBanding(C0122O00000o.instance().OO00ooO() ? "0" : CameraSettings.getAntiBanding());
            } else if (i != 10) {
                switch (i) {
                    case 12:
                        setEvValue();
                        break;
                    case 13:
                        updateBeauty();
                        break;
                    case 14:
                        updateVideoFocusMode();
                        break;
                }
            } else {
                updateFlashPreference();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void forceTrackLayoutType(boolean z) {
        String str = ((RenderManager) getRenderManager().get()).hasMiniComposeType() ? DualVideoAttr.VALUE_PREVIEW_MINI : DualVideoAttr.VALUE_PREVIEW_EQUAL;
        if (z || !str.equals(CameraStatUtils.mLayoutType)) {
            CameraStatUtils.trackDualVideoCommonAttr(DualVideoAttr.ATTR_LAYOUT_TYPE, str);
            CameraStatUtils.mLayoutType = str;
        }
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        return 32772;
    }

    /* access modifiers changed from: protected */
    public Optional getRenderManager() {
        return Optional.ofNullable(this.mRenderManager);
    }

    /* access modifiers changed from: protected */
    public Optional getSubCamera2Device() {
        return Optional.ofNullable(this.mSubCamera2Device);
    }

    public String getTag() {
        return VideoBase.TAG;
    }

    /* access modifiers changed from: protected */
    public Optional getTopAlert() {
        return Optional.ofNullable((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172));
    }

    /* access modifiers changed from: protected */
    public void initializeFocusManager() {
        super.initializeFocusManager();
        if (getSubCamera2Device().isPresent()) {
            CameraCapabilities capabilities = ((Camera2Proxy) getSubCamera2Device().get()).getCapabilities();
            this.mSubFocusManager = new FocusManager2(capabilities, this, ((Camera2Proxy) getSubCamera2Device().get()).isFacingFront(), Looper.getMainLooper());
            Size size = ((Camera2Proxy) getSubCamera2Device().get()).isFacingFront() ? RenderUtil.FRONT_PREVIEW : RenderUtil.BACK_PREVIEW;
            this.mSubFocusManager.setRenderSize(size.getHeight(), size.getWidth());
            this.mSubFocusManager.setPreviewSize(size.getHeight(), size.getWidth());
            ((Camera2Proxy) getSubCamera2Device().get()).setFocusCallback(this.mFocusCallback);
            this.mSubFocusAreaSupported = capabilities.isAFRegionSupported();
            this.mSubMeteringAreaSupported = capabilities.isAERegionSupported();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isMultiCameraMode() {
        return C0122O00000o.instance().OOO000o();
    }

    /* access modifiers changed from: protected */
    public boolean isNeedMute() {
        if (!this.mMediaRecorderRecording || this.mMediaRecorderRecordingPaused) {
            return super.isNeedMute();
        }
        return true;
    }

    public boolean isRecording() {
        MultiRecorderManager multiRecorderManager = this.mMultiRecorderManager;
        return multiRecorderManager != null && multiRecorderManager.isRecording();
    }

    public boolean isRecordingPaused() {
        return isRecording() && this.mMultiRecorderManager.isRecordingPaused();
    }

    /* access modifiers changed from: protected */
    public boolean isSwitching() {
        return ((Boolean) getRenderManager().map(O000o0.INSTANCE).orElse(Boolean.valueOf(false))).booleanValue();
    }

    public boolean isZoomEnabled() {
        if (!getRenderManager().isPresent() || isMultiCameraMode()) {
            return false;
        }
        return ((Boolean) getRenderManager().map(O0000O0o.INSTANCE).orElse(Boolean.valueOf(false))).booleanValue();
    }

    /* access modifiers changed from: protected */
    public boolean judgeTapableRectByUiStyle() {
        return false;
    }

    public void notifyFocusAreaUpdate() {
        if (this.mCamera2Device != null) {
            if (this.mCurrentTapId == this.mCamera2Device.getId()) {
                super.notifyFocusAreaUpdate();
            } else {
                Camera camera = this.mActivity;
                if (camera != null && !camera.isActivityPaused() && isAlive()) {
                    if (!getSubCamera2Device().isPresent()) {
                        Log.w(VideoBase.TAG, "updateFocusArea: sub camera device is null!");
                        return;
                    }
                    Rect activeArraySize = ((Camera2Proxy) getSubCamera2Device().get()).getCapabilities().getActiveArraySize();
                    Rect cropRegion = HybridZoomingSystem.toCropRegion(this.mZoomRatio, activeArraySize);
                    this.mActivity.getSensorStateManager().setFocusSensorEnabled(this.mSubFocusManager.getMeteringAreas(cropRegion, activeArraySize) != null);
                    ((Camera2Proxy) getSubCamera2Device().get()).setAERegions(this.mSubFocusManager.getMeteringAreas(cropRegion, activeArraySize));
                    if (this.mSubFocusAreaSupported) {
                        ((Camera2Proxy) getSubCamera2Device().get()).setAFRegions(this.mSubFocusManager.getFocusAreas(cropRegion, activeArraySize));
                    }
                    String focusMode = CameraSettings.getFocusMode();
                    if (!this.mSubFocusAreaSupported || "manual".equals(focusMode)) {
                        ((Camera2Proxy) getSubCamera2Device().get()).resumePreview();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        if (C0124O00000oO.isMTKPlatform()) {
            ConcurrentHashMap defaultDualVideoCameraIds = Camera2DataContainer.getInstance().getDefaultDualVideoCameraIds();
            int[] iArr = {((Integer) defaultDualVideoCameraIds.get(RenderSourceType.SUB)).intValue(), ((Integer) defaultDualVideoCameraIds.get(RenderSourceType.MAIN)).intValue()};
            getSubCamera2Device().ifPresent(new C0143O0000o0o(iArr));
            this.mCamera2Device.setMtkPipDevices(iArr);
        }
        getSubCamera2Device().ifPresent(O000O0o.INSTANCE);
        updateBeauty();
        initializeFocusManager();
        updatePreferenceTrampoline(UpdateConstant.VIDEO_TYPES_INIT);
        super.onCameraOpened();
        startPreviewSession();
        this.mCurrentTapId = this.mCamera2Device.getId();
    }

    public void onCreate(int i, int i2) {
        initRenderTrigger();
        for (Cookie cookie : getCookieStore().getCookies()) {
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("enumerating: ");
            sb.append(cookie.mCamera2Device);
            Log.d(str, sb.toString());
            ConcurrentHashMap localCameraId = CameraSettings.getDualVideoConfig().getLocalCameraId();
            if (cookie.mCamera2Device != null && localCameraId.size() == 2 && cookie.mCamera2Device.getId() == ((Integer) CameraSettings.getDualVideoConfig().getIds().get(RenderSourceType.SUB)).intValue()) {
                String str2 = VideoBase.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("setCameraDevice: ");
                sb2.append(cookie.mCamera2Device);
                Log.d(str2, sb2.toString());
                this.mSubCamera2Device = cookie.mCamera2Device;
            }
        }
        this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
        registerRenderManager();
        registerRecorderManager();
        registerRemoteService();
        super.onCreate(i, i2);
        this.mVideoFocusMode = AutoFocus.LEGACY_CONTINUOUS_VIDEO;
        onCameraOpened();
    }

    public void onHostStopAndNotifyActionStop() {
        if (!this.mKeepRecorderWhenSwitching) {
            super.onHostStopAndNotifyActionStop();
        }
    }

    public void onLongPress(float f, float f2) {
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        super.onOrientationChanged(i, i2, i3);
        RotateAnimator rotateAnimator = this.mRotateAnimator;
        if (rotateAnimator != null) {
            rotateAnimator.onOrientationChanged(i2);
        }
    }

    public void onPause() {
        ViewGroup parent = ((ModuleContent) ModeCoordinatorImpl.getInstance().getAttachProtocol(431)).getParent();
        Camera activity = getActivity();
        Objects.requireNonNull(parent);
        activity.runOnUiThread(new C0168O000o00O(parent));
        if (!this.mKeepRecorderWhenSwitching) {
            showOrHideBottom(true);
        }
        RotateAnimator rotateAnimator = this.mRotateAnimator;
        if (rotateAnimator != null) {
            rotateAnimator.clear();
            this.mRotateAnimator = null;
        }
        super.onPause();
        this.mRenderTrigger.release();
    }

    public void onPreviewLayoutChanged(Rect rect) {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPreviewLayoutChanged: ");
        sb.append(rect);
        Log.v(str, sb.toString());
        this.mActivity.onLayoutChange(rect);
        if (this.mFocusManager != null && this.mActivity.getCameraScreenNail() != null) {
            this.mActivity.getCameraScreenNail().setDisplayArea(rect);
            this.mFocusManager.setRenderSize(this.mActivity.getCameraScreenNail().getRenderWidth(), this.mActivity.getCameraScreenNail().getRenderHeight());
            this.mFocusManager.setPreviewSize(rect.width(), rect.height());
        }
    }

    public void onPreviewSessionSuccess(CameraCaptureSession cameraCaptureSession) {
        if (this.mSubCamera2Device == null || Integer.parseInt(cameraCaptureSession.getDevice().getId()) != this.mSubCamera2Device.getId()) {
            updatePreferenceInWorkThread(UpdateConstant.VIDEO_TYPES_ON_PREVIEW_SUCCESS);
            super.onPreviewSessionSuccess(cameraCaptureSession);
            return;
        }
        updateSubCameraFocusMode(3);
        getSubCamera2Device().ifPresent(O00000Oo.INSTANCE);
    }

    /* access modifiers changed from: protected */
    @MainThread
    public void onPreviewStart() {
        if (this.mPreviewing) {
            this.mMainProtocol.initializeFocusView(this);
        }
    }

    public void onResume() {
        super.onResume();
        registerRenderManager();
        this.mRotateAnimator = new RotateAnimator(360 - this.mOrientationCompensation);
    }

    public void onSingleTapUp(int i, int i2, boolean z) {
        if (!this.mPaused && this.mCamera2Device != null && getSubCamera2Device().isPresent() && !hasCameraException() && this.mCamera2Device.isPreviewReady() && isInTapableRect(i, i2) && getRenderManager().isPresent() && !isSwitching() && !CameraSettings.getDualVideoConfig().ismDrawSelectWindow()) {
            BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
            if (backStack != null && !backStack.handleBackStackFromTapDown(i, i2)) {
                float f = (float) i;
                float f2 = (float) i2;
                LayoutType renderComposeTypeByPosition = ((RenderManager) getRenderManager().get()).getRenderComposeTypeByPosition(f, f2);
                if (renderComposeTypeByPosition != LayoutType.UNDEFINED) {
                    this.mMainProtocol.setFocusViewType(true);
                    this.mTouchFocusStartingTime = System.currentTimeMillis();
                    Point point = new Point(i, i2);
                    mapTapCoordinate(point);
                    unlockAEAF();
                    setCameraState(2);
                    this.mCurrentTapId = ((RenderManager) getRenderManager().get()).getIdByPosition(f, f2);
                    getFocusManager(this.mCurrentTapId).ifPresent(new C0152O000O0Oo(this, renderComposeTypeByPosition, point, z));
                }
            }
        }
    }

    public void pausePreview() {
    }

    public void registerProtocol() {
        super.registerProtocol();
        ModeCoordinatorImpl.getInstance().attachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(193, this);
        getActivity().getImplFactory().initAdditional(getActivity(), 164, 174, 234, 212);
    }

    public void requestRender() {
        if (this.mRenderTrigger == null || !getSubCamera2Device().isPresent()) {
            this.mActivity.getGLView().requestRender();
        } else {
            this.mRenderTrigger.mainFrameAvailable();
        }
        this.mMainFrameReady = true;
        tryAnimBlackCover();
    }

    public void resetEvValue() {
        super.resetEvValue();
        getSubCamera2Device().ifPresent(O000OO00.INSTANCE);
    }

    /* access modifiers changed from: protected */
    public void resetFocusState(double d) {
        super.resetFocusState(d);
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("resetFocusState: isRecording=");
        sb.append(isRecording());
        sb.append(", isNeedCancelAutoFocus = ");
        FocusManager2 focusManager2 = this.mSubFocusManager;
        boolean z = focusManager2 != null && focusManager2.isNeedCancelAutoFocus();
        sb.append(z);
        Log.v(str, sb.toString());
        FocusManager2 focusManager22 = this.mSubFocusManager;
        if (focusManager22 != null && focusManager22.isNeedCancelAutoFocus() && !isRecording()) {
            this.mSubFocusManager.onDeviceKeepMoving(d);
        }
    }

    public void resumePreview() {
    }

    /* access modifiers changed from: protected */
    public void setEvValue() {
        if (isAlive()) {
            if (this.mCurrentTapId == this.mCamera2Device.getId()) {
                getSubCamera2Device().ifPresent(O0000o00.INSTANCE);
                super.setEvValue();
            } else {
                this.mCamera2Device.setAWBLock(false);
                getSubCamera2Device().ifPresent(new O0000Oo0(this));
            }
        }
    }

    public void setFrameAvailable(boolean z) {
        super.setFrameAvailable(z);
        this.mMainFrameIsAvailable = z;
    }

    /* access modifiers changed from: protected */
    public void setOrientationParameter() {
        super.setOrientationParameter();
        getRenderManager().ifPresent(new C0171O00oOooo(this));
    }

    /* access modifiers changed from: protected */
    public void showModeSwitchLayout(boolean z) {
        getActionProcess().ifPresent(new C0150O0000ooO(this, z));
    }

    /* access modifiers changed from: protected */
    public void showOrHideBottom(boolean z) {
        getActionProcess().ifPresent(new C0147O0000oOo(z));
    }

    public void startFocus() {
        if (isDeviceAlive()) {
            if (!isFrameAvailable()) {
                Log.e(VideoBase.TAG, "cancelFocus: frame not available");
                return;
            }
            if (this.mCurrentTapId == this.mCamera2Device.getId()) {
                super.startFocus();
            } else if (getSubCamera2Device().isPresent()) {
                Log.d(VideoBase.TAG, "startFocus: sub camera");
                this.mActivity.getSensorStateManager().setFocusSensorEnabled(true);
                ((Camera2Proxy) getSubCamera2Device().get()).setFocusCallback(this.mFocusCallback);
                ((Camera2Proxy) getSubCamera2Device().get()).startFocus(FocusTask.create(1), this.mModuleIndex);
            }
        }
    }

    public void startPreview() {
    }

    public void switchRemoteCamera(boolean z) {
        Log.d(VideoBase.TAG, "switchRemoteCamera ");
        if (DualVideoConfigManager.instance().getConfigs().stream().anyMatch(O0000Oo.INSTANCE)) {
            DualVideoConfigManager.instance().removeExternalConfig(1000);
        } else {
            DualVideoConfigManager.instance().addExternalConfig(1000, DualVideoConfigManager.REMOTE_NAME);
        }
        CameraSettings.getDualVideoConfig().refreshSelectData();
        getRenderManager().ifPresent(C0139O00000oo.INSTANCE);
        if (!C0122O00000o.instance().OOO000o()) {
            reStartCurrentModule();
        }
    }

    /* access modifiers changed from: protected */
    public void switchThumbnailFunction(boolean z) {
        getActionProcess().ifPresent(new C0153O000O0oO(this, z));
    }

    /* access modifiers changed from: 0000 */
    public synchronized void tryAnimBlackCover() {
        if (this.mSubFrameReady && this.mMainFrameReady && this.mCoverAnimNeed) {
            this.mCoverAnimNeed = false;
            getActivity().runOnUiThread(new O0000o(this));
        }
    }

    public void unRegisterModulePersistProtocol() {
        getActivity().getCameraScreenNail().setExternalFrameProcessor(null);
        super.unRegisterModulePersistProtocol();
        getRenderManager().ifPresent(O00000o.INSTANCE);
        getActivity().getImplFactory().detachModulePersistent();
    }

    public void unRegisterProtocol() {
        super.unRegisterProtocol();
        ModeCoordinatorImpl.getInstance().detachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(193, this);
        getActivity().getImplFactory().detachAdditional();
    }

    /* access modifiers changed from: protected */
    public void unlockAEAF() {
        super.unlockAEAF();
        getSubCamera2Device().ifPresent(new C0155O000OOoO(this));
    }

    /* access modifiers changed from: protected */
    public void updateAntiBanding(String str) {
        super.updateAntiBanding(str);
        if (isDeviceAlive() && getSubCamera2Device().isPresent()) {
            ((Camera2Proxy) getSubCamera2Device().get()).setAntiBanding(this.mCamera2Device.getCameraConfigs().getAntiBanding());
        }
    }

    /* access modifiers changed from: protected */
    public void updateBeauty() {
        if (this.mBeautyValues == null) {
            this.mBeautyValues = new BeautyValues();
        }
        BeautyValues beautyValues = this.mBeautyValues;
        beautyValues.mBeautySkinSmooth = 40;
        beautyValues.mBeautySlimFace = 40;
        if (this.mCamera2Device.isFacingFront()) {
            this.mCamera2Device.setBeautyValues(this.mBeautyValues);
        } else {
            getSubCamera2Device().ifPresent(new O0000OOo(this));
        }
    }

    /* access modifiers changed from: protected */
    public void updateModuleRelated() {
        super.updateModuleRelated();
        getSubCamera2Device().ifPresent(new O00oOoOo(this));
    }

    /* access modifiers changed from: protected */
    public void updatePictureAndPreviewSize() {
        this.mPreviewSize = ModuleUtil.getUIStyle() == 0 ? new CameraSize(1440, 1080) : new CameraSize(1920, 1080);
        this.mVideoSize = new CameraSize(1920, 1080);
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updatePictureAndPreviewSize ");
        sb.append(this.mPreviewSize.toString());
        Log.d(str, sb.toString());
        CameraSize cameraSize = this.mPreviewSize;
        updateCameraScreenNailSize(cameraSize.width, cameraSize.height);
    }

    /* access modifiers changed from: protected */
    public void updateVideoStabilization() {
        if (isDeviceAlive()) {
            if (CameraSettings.isMovieSolidOn()) {
                Log.d(VideoBase.TAG, "videoStabilization: EIS");
                this.mCamera2Device.setEnableOIS(false);
                this.mCamera2Device.setEnableEIS(true);
                if (!this.mCameraCapabilities.isEISPreviewSupported()) {
                    this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(true);
                }
            } else {
                Log.d(VideoBase.TAG, "videoStabilization: OIS");
                this.mCamera2Device.setEnableEIS(false);
                this.mCamera2Device.setEnableOIS(true);
                this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(false);
            }
        }
    }
}
