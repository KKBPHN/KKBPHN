package com.android.camera.module;

import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Surface;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.log.Log;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.StartControlFeatureDetail;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.ui.drawable.snap.PaintConditionReferred;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.gallery3d.ui.GLCanvas;
import java.lang.ref.WeakReference;

public class FakerModule extends BaseModule implements CameraPreviewCallback {
    /* access modifiers changed from: private */
    public static final String TAG = "FakerModule";
    private boolean mHookSurfaceTexturePending;
    private String mTargetFeatureName;

    class MainHandler extends Handler {
        private WeakReference mModule;

        public MainHandler(FakerModule fakerModule, Looper looper) {
            super(looper);
            this.mModule = new WeakReference(fakerModule);
        }

        public void handleMessage(Message message) {
            FakerModule fakerModule = (FakerModule) this.mModule.get();
            if (message.what == 45) {
                Log.d(FakerModule.TAG, "onMessage MSG_ABANDON_HANDLER setActivity null");
                FakerModule.this.setActivity(null);
            }
            if (!FakerModule.this.isCreated()) {
                removeCallbacksAndMessages(null);
            } else if (FakerModule.this.getActivity() != null) {
                int i = message.what;
                if (i == 2) {
                    fakerModule.getWindow().clearFlags(128);
                } else if (i == 17) {
                    removeMessages(17);
                    removeMessages(2);
                    fakerModule.getWindow().addFlags(128);
                    sendEmptyMessageDelayed(2, (long) fakerModule.getScreenDelay());
                } else if (i == 45) {
                    fakerModule.setActivity(null);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("no consumer for this message: ");
                    sb.append(message.what);
                    throw new RuntimeException(sb.toString());
                }
            }
        }
    }

    private void previewWhenSessionSuccess() {
        setCameraState(1);
        updatePreferenceInWorkThread(UpdateConstant.FAKER_TYPES_ON_PREVIEW_SUCCESS);
    }

    private void updateFilter() {
        int shaderEffect = CameraSettings.getShaderEffect();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateFilter: 0x");
        sb.append(Integer.toHexString(shaderEffect));
        Log.v(str, sb.toString());
        EffectController.getInstance().setEffect(shaderEffect);
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.resumePreview();
        }
    }

    private void updatePictureAndPreviewSize() {
        float f;
        int i;
        int i2;
        this.mCameraCapabilities.setOperatingMode(getOperatingMode());
        int i3 = this.mModuleIndex;
        if (i3 != 254) {
            this.mHookSurfaceTexturePending = false;
            f = CameraSettings.getPreviewAspectRatio(new PaintConditionReferred(i3).targetFrameRatio);
        } else {
            this.mHookSurfaceTexturePending = true;
            int uiStyle = DataRepository.dataItemRunning().getUiStyle();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getLastUiStyle = ");
            sb.append(uiStyle);
            Log.d(str, sb.toString());
            if (uiStyle != 0) {
                if (uiStyle == 1) {
                    i2 = 16;
                    i = 9;
                } else if (uiStyle == 3) {
                    i2 = Util.getScreenHeight(this.mActivity);
                    i = Util.getScreenWidth(this.mActivity);
                }
                f = CameraSettings.getPreviewAspectRatio(i2, i);
            }
            f = CameraSettings.getPreviewAspectRatio(4, 3);
        }
        this.mPreviewSize = Util.getOptimalPreviewSize(false, this.mBogusCameraId, this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class), (double) f, null);
        CameraSize cameraSize = this.mPreviewSize;
        if (cameraSize != null) {
            updateCameraScreenNailSize(cameraSize.width, cameraSize.height);
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("previewSize: ");
        sb2.append(this.mPreviewSize);
        Log.d(str2, sb2.toString());
    }

    public void closeCamera() {
    }

    /* access modifiers changed from: protected */
    public void consumePreference(int... iArr) {
        for (int i : iArr) {
            if (i == 1) {
                updatePictureAndPreviewSize();
            } else if (i == 2) {
                updateFilter();
            } else if (i == 24) {
                applyZoomRatio();
            } else if (i == 55) {
                updateModuleRelated();
            }
        }
    }

    public void fillFeatureControl(StartControl startControl) {
        int i;
        int i2;
        StartControlFeatureDetail featureDetail = startControl.getFeatureDetail();
        if (startControl.mTargetMode == 254) {
            i2 = R.id.rotation_full_screen_feature;
            i = BaseFragmentDelegate.FRAGMENT_MODES_MORE_NORMAL;
        } else if (!TextUtils.isEmpty(this.mTargetFeatureName)) {
            i2 = R.id.bottom_beauty;
            i = BaseFragmentDelegate.FRAGMENT_VV_FEATURE;
        } else {
            return;
        }
        featureDetail.addFragmentInfo(i2, i);
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        return 0;
    }

    public String getTag() {
        return TAG;
    }

    public boolean isDoingAction() {
        return false;
    }

    public boolean isUnInterruptable() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        updatePreferenceTrampoline(UpdateConstant.FAKER_TYPES_INIT);
        startPreview();
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        this.mHandler = new MainHandler(this, this.mActivity.getMainLooper());
        onCameraOpened();
    }

    public void onDestroy() {
        super.onDestroy();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessage(45);
        }
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 4) {
            if (i == 27 || i == 66) {
                return true;
            }
        } else if (((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).handleBackStackFromKeyBack()) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    public void onPreviewLayoutChanged(Rect rect) {
        this.mActivity.onLayoutChange(rect);
    }

    public void onPreviewSessionClosed(CameraCaptureSession cameraCaptureSession) {
    }

    public void onPreviewSessionFailed(CameraCaptureSession cameraCaptureSession) {
    }

    public void onPreviewSessionSuccess(CameraCaptureSession cameraCaptureSession) {
        if (cameraCaptureSession != null && isAlive()) {
            previewWhenSessionSuccess();
        }
    }

    public void onResume() {
        super.onResume();
        keepScreenOnAwhile();
    }

    public boolean onSurfaceTexturePending(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
        return this.mHookSurfaceTexturePending;
    }

    /* access modifiers changed from: protected */
    public void openSettingActivity() {
    }

    public void pausePreview() {
    }

    public void registerProtocol() {
        super.registerProtocol();
        Log.d(TAG, "registerProtocol");
        ModeCoordinatorImpl.getInstance().attachProtocol(2560, this);
        getActivity().getImplFactory().initAdditional(getActivity(), 164);
    }

    public void resumePreview() {
    }

    public void setFrameAvailable(boolean z) {
        super.setFrameAvailable(z);
        MainContentProtocol mainContentProtocol = this.mMainProtocol;
        if (mainContentProtocol != null) {
            mainContentProtocol.hideReferenceGradienter();
        }
    }

    public void setTargetFeatureName(String str) {
        this.mTargetFeatureName = str;
    }

    public void startPreview() {
        if (isDeviceAlive()) {
            this.mCamera2Device.setPreviewSize(this.mPreviewSize);
            this.mCamera2Device.startPreviewSession(new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture()), 0, 0, null, getOperatingMode(), false, this);
        }
    }

    public void unRegisterProtocol() {
        super.unRegisterProtocol();
        Log.d(TAG, "unRegisterProtocol");
        ModeCoordinatorImpl.getInstance().detachProtocol(2560, this);
        getActivity().getImplFactory().detachAdditional();
    }
}
