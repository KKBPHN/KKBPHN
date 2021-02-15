package com.android.camera.fragment.dual;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import androidx.core.view.ViewCompat;
import com.android.camera.ActivityBase;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.folme.FolmeAlphaInOnSubscribe;
import com.android.camera.animation.folme.FolmeAlphaOutOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.manually.ZoomValueListener;
import com.android.camera.fragment.manually.adapter.AbstractZoomSliderAdapter;
import com.android.camera.fragment.manually.adapter.ExtraSlideFNumberAdapter;
import com.android.camera.fragment.manually.adapter.sat.StopsZoomSliderAdapter;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CameraClickObservable;
import com.android.camera.protocol.ModeProtocol.CameraModuleSpecial;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ManuallyValueChanged;
import com.android.camera.protocol.ModeProtocol.MasterFilterProtocol;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.SnapShotIndicator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.PortraitAttr;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.ui.BaseHorizontalZoomView.onTouchUpStateListener;
import com.android.camera.ui.HorizontalZoomView;
import com.android.camera.ui.zoom.ZoomIndexButtonsLayout;
import com.android.camera.ui.zoom.ZoomIndexButtonsLayout.OnIndexButtonClickListener;
import com.android.camera.ui.zoom.ZoomRatioToggleView;
import com.android.camera.ui.zoom.ZoomRatioToggleView.ToggleStateListener;
import com.android.camera.ui.zoom.ZoomRatioToggleView.ViewSpec;
import com.android.camera.ui.zoom.ZoomRatioView;
import com.android.camera2.CameraCapabilities;
import io.reactivex.Completable;
import java.util.ArrayList;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public class FragmentDualCameraAdjust extends BaseFragment implements ToggleStateListener, ZoomValueListener, HandleBackTrace, DualController, SnapShotIndicator, OnClickListener, OnIndexButtonClickListener, onTouchUpStateListener {
    public static final int FRAGMENT_INFO = 4084;
    private static final int HIDE_POPUP = 1;
    private static final int STATE_BOKEH_SHOW = -100;
    private static final int STATE_ZOOM_SHOW = -200;
    private static final String TAG = "FragmentDualCameraAdjust";
    private int mButtonLayoutHeight;
    private int mCurrentState = -1;
    private ViewGroup mDualParentLayout;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                FragmentDualCameraAdjust.this.onBackEvent(5);
            }
        }
    };
    private ViewGroup mHorizontalSlideLayout;
    private HorizontalZoomView mHorizontalSlideView;
    private ImageView mImageBokehIndicator;
    private boolean mIsHiding;
    private boolean mIsRecordingOrPausing = false;
    /* access modifiers changed from: private */
    public boolean mIsZoomTo2X;
    private AbstractZoomSliderAdapter mSlidingAdapter;
    /* access modifiers changed from: private */
    public float mTargetZoomRatio;
    private int mUseSliderType = 0;
    private ZoomIndexButtonsLayout mZoomIndexButtons;
    private float mZoomRatio;
    /* access modifiers changed from: private */
    public ValueAnimator mZoomRatioToggleAnimator;
    private ZoomRatioToggleView mZoomRatioToggleView;
    private int mZoomSliderLayoutHeight;

    private void adjustViewBackground(View view, int i) {
        ZoomRatioToggleView zoomRatioToggleView = this.mZoomRatioToggleView;
        if (zoomRatioToggleView != null) {
            zoomRatioToggleView.setBackgroundColor(getResources().getColor(getZoomBackgroundColor(i)));
        }
        ZoomIndexButtonsLayout zoomIndexButtonsLayout = this.mZoomIndexButtons;
        if (zoomIndexButtonsLayout != null) {
            zoomIndexButtonsLayout.setBackgroundColor(getResources().getColor(getZoomBackgroundColor(i)));
        }
        view.setBackgroundDrawable(null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:112:0x0137, code lost:
        if (r3 == -1) goto L_0x007f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x0183, code lost:
        if (r1.isSupportLightTripartite() != false) goto L_0x0210;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:145:0x0189, code lost:
        if (com.android.camera.CameraSettings.isMacroModeEnabled(r11) != false) goto L_0x0210;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x01ab, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOOO0OO() != false) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:186:0x0206, code lost:
        if (r1.isSupportLightTripartite() != false) goto L_0x0210;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:188:0x020c, code lost:
        if (com.android.camera.CameraSettings.isMacroModeEnabled(r11) != false) goto L_0x0210;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x007d, code lost:
        if (r3 == -1) goto L_0x007f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0081, code lost:
        r0 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x009b, code lost:
        if (r3 == -1) goto L_0x007f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x0214  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static ViewSpec getViewSpecForCapturingMode(int i) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        int i2;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        boolean isNormalIntent = DataRepository.dataItemGlobal().isNormalIntent();
        CameraCapabilities currentCameraCapabilities = Camera2DataContainer.getInstance().getCurrentCameraCapabilities();
        int i3 = -200;
        int i4 = 0;
        if (DataRepository.dataItemGlobal().getCurrentCameraId() != 1 && !CameraSettings.isMacroModeEnabled(i) && !CameraSettings.isAutoZoomEnabled(i) && !CameraSettings.isSuperEISEnabled(i) && HybridZoomingSystem.IS_2_OR_MORE_SAT) {
            if (i == 188) {
                i2 = -200;
                z5 = false;
                z4 = true;
            } else {
                i2 = -1;
                z4 = false;
                z5 = true;
            }
            if (i != 175 || ((!C0122O00000o.instance().OOOOO00() && !C0122O00000o.instance().OOOO0oo()) || CameraSettings.isSRTo108mModeOn())) {
                if (i == 161) {
                    if (HybridZoomingSystem.IS_2_SAT || CameraSettings.isUltraWideConfigOpen(i)) {
                        i3 = -1;
                    }
                    z13 = i3 == -1;
                } else if (i == 174) {
                    if (HybridZoomingSystem.IS_2_SAT || CameraSettings.isUltraWideConfigOpen(i)) {
                        int i5 = -1;
                    }
                    z13 = i3 == -1;
                } else {
                    if (i == 183) {
                        i2 = (!HybridZoomingSystem.IS_2_SAT && !CameraSettings.isUltraWideConfigOpen(i)) ? -200 : -1;
                        z8 = i2 == -1;
                        z7 = i2 == -1;
                        z6 = false;
                    } else {
                        z6 = z4;
                        z7 = z5;
                        z8 = true;
                    }
                    if (i == 162) {
                        if (HybridZoomingSystem.IS_2_SAT || ((CameraSettings.isMacroModeEnabled(i) || CameraSettings.isUltraWideConfigOpen(i)) && !C0122O00000o.instance().OOoOO0o())) {
                            int i6 = -1;
                        }
                        z2 = i3 == -1 || (!isNormalIntent && currentCameraCapabilities != null && currentCameraCapabilities.isSupportLightTripartite()) || CameraSettings.isVideoQuality8KOpen(i) || CameraSettings.isVhdrOn(currentCameraCapabilities, i);
                        z3 = i3 == -1 || (!isNormalIntent && currentCameraCapabilities != null && currentCameraCapabilities.isSupportLightTripartite()) || CameraSettings.isVideoQuality8KOpen(i) || CameraSettings.isVhdrOn(currentCameraCapabilities, i);
                        if (CameraSettings.supportVideoSATForVideoQuality(i)) {
                            z = true;
                        }
                        z = false;
                    } else if (i == 169) {
                        if (HybridZoomingSystem.IS_2_SAT) {
                            int i7 = -1;
                        } else {
                            boolean OOO00O0 = C0122O00000o.instance().OOO00O0();
                        }
                        z13 = i3 == -1;
                    } else {
                        if (i == 163 || i == 186) {
                            if ((CameraSettings.isMacroModeEnabled(i) || CameraSettings.isUltraWideConfigOpen(i) || CameraSettings.isUltraPixelRearOn()) && !C0122O00000o.instance().OOoOO0o()) {
                                int i8 = -1;
                            }
                            z10 = HybridZoomingSystem.IS_2_SAT || (!isNormalIntent && currentCameraCapabilities != null && currentCameraCapabilities.isSupportLightTripartite());
                            z9 = HybridZoomingSystem.IS_2_SAT || (!isNormalIntent && currentCameraCapabilities != null && currentCameraCapabilities.isSupportLightTripartite());
                            if (!isNormalIntent) {
                                if (currentCameraCapabilities != null) {
                                }
                                z11 = false;
                                z3 = z9;
                            }
                        } else if (i == 165) {
                            if (CameraSettings.isMacroModeEnabled(i) || CameraSettings.isUltraWideConfigOpen(i)) {
                                int i9 = -1;
                            }
                            z10 = HybridZoomingSystem.IS_2_SAT || (!isNormalIntent && currentCameraCapabilities != null && currentCameraCapabilities.isSupportLightTripartite());
                            z9 = HybridZoomingSystem.IS_2_SAT || (!isNormalIntent && currentCameraCapabilities != null && currentCameraCapabilities.isSupportLightTripartite());
                            if (!isNormalIntent) {
                                if (currentCameraCapabilities != null) {
                                }
                                z11 = false;
                                z3 = z9;
                            }
                        } else if (i == 173) {
                            if (C0122O00000o.instance().OOOo0oo()) {
                                z = false;
                                z12 = false;
                                z2 = z3;
                            }
                        } else if (i != 166) {
                            if (i != 205) {
                                i3 = i2;
                                z2 = z8;
                                z3 = z7;
                                z = z6;
                            }
                        }
                        z11 = true;
                        z3 = z9;
                    }
                    if (z) {
                        i4 = (CameraSettings.isFakePartSAT() || !CameraSettings.isSupportedOpticalZoom()) ? 1 : 2;
                    }
                    return new ViewSpec(i3, z2, z3, i4);
                }
                z3 = true;
                z2 = z13;
                z = false;
                if (z) {
                }
                return new ViewSpec(i3, z2, z3, i4);
            }
            z = false;
            z12 = true;
            z2 = z3;
            if (z) {
            }
            return new ViewSpec(i3, z2, z3, i4);
        }
        i3 = -1;
        z = false;
        z12 = true;
        z2 = z3;
        if (z) {
        }
        return new ViewSpec(i3, z2, z3, i4);
    }

    private int getZoomBackgroundColor(int i) {
        return i != 165 ? i != 188 ? R.color.zoom_button_background_color : R.color.zoom_button_background_super_moon_color : Display.fitDisplayFull(1.3333333f) ? R.color.zoom_button_background_color : R.color.zoom_button_background_1_1_color;
    }

    private void initSlideFNumberView() {
        String readFNumber = CameraSettings.readFNumber();
        this.mSlidingAdapter = new ExtraSlideFNumberAdapter(getContext(), readFNumber, this);
        this.mHorizontalSlideView.setListener(this.mSlidingAdapter, this);
        this.mHorizontalSlideView.setDrawAdapter(this.mSlidingAdapter, this.mDegree, false);
        this.mHorizontalSlideView.setSelection((int) this.mSlidingAdapter.mapValueToPosition(readFNumber), true);
    }

    private void initSlideZoomView() {
        boolean z = true;
        this.mSlidingAdapter = new StopsZoomSliderAdapter(getContext(), this.mUseSliderType == 1, this.mCurrentMode, this);
        this.mHorizontalSlideView.setListener(this.mSlidingAdapter, this);
        this.mHorizontalSlideView.setDrawAdapter(this.mSlidingAdapter, this.mDegree, HybridZoomingSystem.SUPPORT_PROGRESS_ZOOM_EFFECT);
        this.mZoomRatioToggleView.setVisibility(8);
        if (this.mCurrentMode != 188) {
            z = false;
        }
        BaseModule baseModule = (BaseModule) ((ActivityBase) getActivity()).getCurrentModule();
        List O000000o2 = C0122O00000o.instance().O000000o(z, ModuleManager.isVideoCategory(this.mCurrentMode), HybridZoomingSystem.ZOOM_INDEXS_DEFAULT);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < O000000o2.size(); i++) {
            float floatValue = ((Float) O000000o2.get(i)).floatValue();
            if (floatValue >= baseModule.getMinZoomRatio() && floatValue <= baseModule.getMaxZoomRatio()) {
                arrayList.add(Float.valueOf(floatValue));
            }
        }
        if (!arrayList.contains(Float.valueOf(baseModule.getMaxZoomRatio()))) {
            arrayList.add(Float.valueOf(baseModule.getMaxZoomRatio()));
        }
        this.mZoomIndexButtons.setZoomIndexButtons(arrayList, this);
        this.mZoomIndexButtons.setSelect(this.mZoomRatio, false);
    }

    private void initiateZoomRatio() {
        String str;
        StringBuilder sb;
        float ultraTeleMinZoomRatio;
        boolean isZoomByCameraSwitchingSupported = CameraSettings.isZoomByCameraSwitchingSupported();
        String str2 = TAG;
        float f = 1.0f;
        if (isZoomByCameraSwitchingSupported) {
            String cameraLensType = CameraSettings.getCameraLensType(this.mCurrentMode);
            if (ComponentManuallyDualLens.LENS_ULTRA.equals(cameraLensType)) {
                ultraTeleMinZoomRatio = HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR;
            } else if (ComponentManuallyDualLens.LENS_WIDE.equals(cameraLensType)) {
                this.mZoomRatio = 1.0f;
                sb = new StringBuilder();
                str = "initiateZoomRatio(): lens-switch-zoom: ";
            } else if (ComponentManuallyDualLens.LENS_TELE.equals(cameraLensType)) {
                ultraTeleMinZoomRatio = HybridZoomingSystem.getTeleMinZoomRatio();
            } else if ("macro".equals(cameraLensType)) {
                ultraTeleMinZoomRatio = HybridZoomingSystem.sDefaultMacroOpticalZoomRatio;
            } else if (ComponentManuallyDualLens.LENS_ULTRA_TELE.equals(cameraLensType)) {
                ultraTeleMinZoomRatio = HybridZoomingSystem.getUltraTeleMinZoomRatio();
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("initiateZoomRatio(): Unknown camera lens type: ");
                sb2.append(cameraLensType);
                throw new IllegalStateException(sb2.toString());
            }
            this.mZoomRatio = ultraTeleMinZoomRatio;
            sb = new StringBuilder();
            str = "initiateZoomRatio(): lens-switch-zoom: ";
        } else {
            int i = this.mResetType;
            if (i != 3) {
                if (i == 4 || CameraSettings.isFrontCamera()) {
                    int i2 = this.mCurrentMode;
                    if (i2 == 188) {
                        f = HybridZoomingSystem.getMinimumOpticalZoomRatio(i2);
                    }
                } else {
                    this.mZoomRatio = CameraSettings.getRetainZoom(this.mCurrentMode);
                    sb = new StringBuilder();
                    str = "initiateZoomRatio(): zoom: ";
                }
            }
            this.mZoomRatio = f;
            sb = new StringBuilder();
            str = "initiateZoomRatio(): zoom: ";
        }
        sb.append(str);
        sb.append(this.mZoomRatio);
        Log.d(str2, sb.toString());
    }

    private boolean isVisible(View view) {
        return view.getVisibility() == 0 && view.getAlpha() != 0.0f;
    }

    private void notifyTipsMargin() {
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.directHideTipImage();
            bottomPopupTips.directlyHideTips();
            bottomPopupTips.directShowOrHideLeftTipImage(false);
        }
    }

    /* access modifiers changed from: private */
    public void notifyZoom2X(boolean z) {
        ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
        if (manuallyValueChanged == null) {
            return;
        }
        if (Util.isZoomAnimationEnabled() || z) {
            manuallyValueChanged.onDualZoomHappened(z);
        }
    }

    /* access modifiers changed from: private */
    public void notifyZooming(boolean z) {
        ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
        if (manuallyValueChanged != null) {
            manuallyValueChanged.onDualLensZooming(z);
        }
    }

    /* access modifiers changed from: private */
    public void requestZoomRatio(float f, int i) {
        ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
        if (manuallyValueChanged != null) {
            manuallyValueChanged.onDualZoomValueChanged(f, i);
        }
    }

    private void sendHideMessage() {
        if (this.mCurrentMode != 188) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 3000);
        }
    }

    private void showBokehPanel() {
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        String str = TAG;
        if (miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow()) {
            Log.u(str, "showBokehPanel");
            initSlideFNumberView();
            this.mSlidingAdapter.setEnable(true);
            FolmeAlphaInOnSubscribe.directSetResult(this.mDualParentLayout);
            this.mHorizontalSlideLayout.setVisibility(0);
            ((MarginLayoutParams) this.mDualParentLayout.getLayoutParams()).bottomMargin = Display.getBottomHeight() - this.mZoomSliderLayoutHeight;
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 1.0f, 1.0f, 1, 0.5f, 1, 0.5f);
            scaleAnimation.setDuration(300);
            scaleAnimation.setInterpolator(new CubicEaseOutInterpolator());
            this.mHorizontalSlideView.startAnimation(scaleAnimation);
            showOrHideBottomMenu(false, true);
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.hideTipImage();
                bottomPopupTips.hideLeftTipImage();
            }
            Completable.create(new FolmeAlphaOutOnSubscribe(this.mImageBokehIndicator).targetGone()).subscribe();
            return;
        }
        Log.v(str, "beauty panel shown. do not show the slide view.");
    }

    private void showOrHideBottomMenu(boolean z, boolean z2) {
        BottomMenuProtocol bottomMenuProtocol = (BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197);
        if (z) {
            bottomMenuProtocol.setModeLayoutVisibility(0, z2);
        } else {
            bottomMenuProtocol.setModeLayoutVisibility(8, false);
        }
    }

    private boolean showZoomPanel() {
        if (isZoomPanelVisible() || this.mUseSliderType == 0) {
            return false;
        }
        Log.u(TAG, "showZoomPanel");
        initSlideZoomView();
        this.mIsHiding = false;
        this.mSlidingAdapter.setEnable(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 1.0f, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setDuration(300);
        scaleAnimation.setInterpolator(new CubicEaseOutInterpolator());
        this.mZoomIndexButtons.startAnimation(scaleAnimation);
        this.mHorizontalSlideView.startAnimation(scaleAnimation);
        this.mZoomIndexButtons.setVisibility(0);
        this.mZoomIndexButtons.setDegree(this.mDegree);
        this.mHorizontalSlideLayout.setVisibility(0);
        ((MarginLayoutParams) this.mDualParentLayout.getLayoutParams()).bottomMargin = Display.getBottomHeight() - this.mZoomSliderLayoutHeight;
        showOrHideBottomMenu(false, true);
        notifyTipsMargin();
        if (this.mCurrentMode == 163) {
            CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
            if (cameraModuleSpecial != null) {
                cameraModuleSpecial.showOrHideChip(false);
            }
        }
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.directHideLyingDirectHint();
        }
        BottomPopupTips bottomPopupTips2 = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips2 != null) {
            bottomPopupTips2.hideQrCodeTip();
        }
        ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
        if (manuallyValueChanged != null) {
            manuallyValueChanged.updateSATIsZooming(true);
        }
        return true;
    }

    private void switchCameraLens() {
        float ultraTeleMinZoomRatio;
        ComponentManuallyDualLens manuallyDualLens = DataRepository.dataItemConfig().getManuallyDualLens();
        ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
        if (manuallyValueChanged != null) {
            manuallyValueChanged.onDualLensSwitch(manuallyDualLens, this.mCurrentMode);
            updateZoomRatio(0);
        }
        String componentValue = manuallyDualLens.getComponentValue(this.mCurrentMode);
        if (ComponentManuallyDualLens.LENS_ULTRA.equals(componentValue)) {
            ultraTeleMinZoomRatio = HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR;
        } else if (ComponentManuallyDualLens.LENS_WIDE.equals(componentValue)) {
            ultraTeleMinZoomRatio = 1.0f;
        } else if (ComponentManuallyDualLens.LENS_TELE.equals(componentValue)) {
            ultraTeleMinZoomRatio = HybridZoomingSystem.getTeleMinZoomRatio();
        } else if (ComponentManuallyDualLens.LENS_ULTRA_TELE.equals(componentValue)) {
            ultraTeleMinZoomRatio = HybridZoomingSystem.getUltraTeleMinZoomRatio();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("switchCameraLens(): Unknown camera lens type: ");
            sb.append(componentValue);
            throw new IllegalStateException(sb.toString());
        }
        CameraStatUtils.trackDualZoomChanged(this.mCurrentMode, HybridZoomingSystem.toString(ultraTeleMinZoomRatio));
    }

    private void toHideBokehPanel(boolean z) {
        Log.u(TAG, "toHideBokehPanel");
        AbstractZoomSliderAdapter abstractZoomSliderAdapter = this.mSlidingAdapter;
        if (abstractZoomSliderAdapter != null) {
            abstractZoomSliderAdapter.setEnable(false);
        }
        this.mHorizontalSlideLayout.setVisibility(8);
        showOrHideBottomMenu(true, z);
        if (DataRepository.dataItemGlobal().isNormalIntent()) {
            if (z) {
                Completable.create(new FolmeAlphaInOnSubscribe(this.mImageBokehIndicator)).subscribe();
            } else {
                FolmeAlphaInOnSubscribe.directSetResult(this.mImageBokehIndicator);
            }
        }
        ((MarginLayoutParams) this.mDualParentLayout.getLayoutParams()).bottomMargin = Display.getBottomHeight();
    }

    private void toHideZoomPanel(boolean z) {
        toHideZoomPanel(z, true);
    }

    private void toHideZoomPanel(boolean z, boolean z2) {
        this.mHandler.removeMessages(1);
        if (this.mHorizontalSlideLayout.getVisibility() == 0) {
            ((MarginLayoutParams) this.mDualParentLayout.getLayoutParams()).bottomMargin = Display.getBottomHeight();
            this.mIsHiding = false;
            this.mHorizontalSlideLayout.setVisibility(8);
            this.mZoomIndexButtons.setVisibility(8);
            if (z2) {
                this.mZoomRatioToggleView.setVisibility(0);
            }
            this.mSlidingAdapter.setEnable(false);
            showOrHideBottomMenu(true, z);
            if (z) {
                this.mZoomRatioToggleView.startTranslationAnimationShow();
                ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
                if (manuallyValueChanged != null) {
                    manuallyValueChanged.updateSATIsZooming(false);
                }
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                if (bottomPopupTips != null) {
                    bottomPopupTips.reInitTipImage();
                }
                if (bottomPopupTips != null) {
                    bottomPopupTips.updateLyingDirectHint(false, true);
                }
                if (this.mCurrentMode == 163) {
                    CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
                    if (cameraModuleSpecial != null) {
                        cameraModuleSpecial.showOrHideChip(true);
                    }
                }
            }
        }
    }

    private void updateZoomSlider() {
        AbstractZoomSliderAdapter abstractZoomSliderAdapter = this.mSlidingAdapter;
        if (abstractZoomSliderAdapter != null && this.mHorizontalSlideView != null && abstractZoomSliderAdapter.isEnable()) {
            updateZoomSliderPosition();
        }
    }

    private void updateZoomSliderPosition() {
        this.mHorizontalSlideView.setSelection(this.mSlidingAdapter.mapValueToPosition(String.valueOf(CameraSettings.getRetainZoom(this.mCurrentMode))));
    }

    public /* synthetic */ void O000oOOO() {
        this.mDualParentLayout.setVisibility(8);
    }

    public /* synthetic */ void O000oOOo() {
        this.mDualParentLayout.setVisibility(8);
    }

    public int getFragmentInto() {
        return 4084;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_dual_camera_adjust;
    }

    public void hideAllPanel() {
        int i = this.mCurrentState;
        if (i != -1) {
            if (i == -100) {
                toHideBokehPanel(true);
            } else if (i == -200) {
                toHideZoomPanel(true);
            }
        }
    }

    public void hideBokehButton(boolean z) {
        if (this.mCurrentState != -1) {
            this.mCurrentState = -1;
            toHideBokehPanel(z);
            Completable.create(new FolmeAlphaOutOnSubscribe(this.mImageBokehIndicator).setOnAnimationEnd(new O00000Oo(this))).subscribe();
        }
    }

    public void hideZoomButton() {
        if (this.mCurrentState == -200) {
            this.mCurrentState = -1;
            Completable.create(new FolmeAlphaOutOnSubscribe(this.mZoomRatioToggleView).setOnAnimationEnd(new O000000o(this))).subscribe();
            ViewGroup viewGroup = this.mHorizontalSlideLayout;
            if (viewGroup != null && viewGroup.getVisibility() == 0) {
                this.mHandler.removeMessages(1);
                this.mIsHiding = true;
                this.mSlidingAdapter.setEnable(false);
                this.mZoomIndexButtons.setVisibility(8);
                this.mHorizontalSlideLayout.setVisibility(8);
                showOrHideBottomMenu(true, true);
                ((MarginLayoutParams) this.mDualParentLayout.getLayoutParams()).bottomMargin = Display.getBottomHeight();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        ValueAnimator valueAnimator;
        long j;
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.bottomMargin = Display.getBottomHeight();
        marginLayoutParams.setMarginStart(Display.getStartMargin());
        marginLayoutParams.setMarginEnd(Display.getEndMargin());
        this.mDualParentLayout = (ViewGroup) view.findViewById(R.id.dual_layout_parent);
        this.mHorizontalSlideLayout = (ViewGroup) view.findViewById(R.id.dual_camera_zoom_slider_container);
        this.mZoomIndexButtons = (ZoomIndexButtonsLayout) view.findViewById(R.id.zoom_index_buttons_layout);
        this.mZoomRatioToggleView = (ZoomRatioToggleView) view.findViewById(R.id.zoom_ratio_toggle_button);
        this.mZoomRatioToggleView.setActionListener(this);
        this.mImageBokehIndicator = (ImageView) view.findViewById(R.id.dual_camera_bokeh_indicator);
        this.mImageBokehIndicator.setOnClickListener(this);
        View findViewById = view.findViewById(R.id.sat_optical_zoom_switch_simulator);
        findViewById.setOnClickListener(this.mZoomRatioToggleView);
        findViewById.setOnLongClickListener(this.mZoomRatioToggleView);
        this.mHorizontalSlideView = (HorizontalZoomView) view.findViewById(R.id.dual_camera_zoom);
        this.mButtonLayoutHeight = this.mZoomRatioToggleView.getLayoutParams().height;
        this.mZoomRatioToggleAnimator = new ValueAnimator();
        this.mZoomRatioToggleAnimator.setInterpolator(new LinearInterpolator());
        if (!Util.isZoomAnimationEnabled() || HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            valueAnimator = this.mZoomRatioToggleAnimator;
            j = 0;
        } else {
            valueAnimator = this.mZoomRatioToggleAnimator;
            j = 100;
        }
        valueAnimator.setDuration(j);
        this.mZoomRatioToggleAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FragmentDualCameraAdjust fragmentDualCameraAdjust;
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                StringBuilder sb = new StringBuilder();
                sb.append("onAnimationUpdate(): zoom ratio = ");
                sb.append(floatValue);
                Log.d(FragmentDualCameraAdjust.TAG, sb.toString());
                if (FragmentDualCameraAdjust.this.mTargetZoomRatio <= 0.0f || FragmentDualCameraAdjust.this.mZoomRatioToggleAnimator.getDuration() != 0) {
                    fragmentDualCameraAdjust = FragmentDualCameraAdjust.this;
                } else {
                    fragmentDualCameraAdjust = FragmentDualCameraAdjust.this;
                    floatValue = fragmentDualCameraAdjust.mTargetZoomRatio;
                }
                fragmentDualCameraAdjust.requestZoomRatio(floatValue, 0);
            }
        });
        this.mZoomRatioToggleAnimator.addListener(new AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
                FragmentDualCameraAdjust.this.notifyZooming(false);
                FragmentDualCameraAdjust.this.mIsZoomTo2X = false;
                FragmentDualCameraAdjust.this.notifyZoom2X(false);
            }

            public void onAnimationEnd(Animator animator) {
                FragmentDualCameraAdjust.this.notifyZooming(false);
                FragmentDualCameraAdjust.this.mIsZoomTo2X = false;
                FragmentDualCameraAdjust.this.notifyZoom2X(false);
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
                FragmentDualCameraAdjust.this.notifyZooming(true);
                boolean access$400 = FragmentDualCameraAdjust.this.mIsZoomTo2X;
                FragmentDualCameraAdjust fragmentDualCameraAdjust = FragmentDualCameraAdjust.this;
                if (access$400) {
                    fragmentDualCameraAdjust.notifyZoom2X(true);
                } else {
                    fragmentDualCameraAdjust.notifyZoom2X(false);
                }
            }
        });
        FolmeUtils.touchScaleTint(this.mImageBokehIndicator);
        provideAnimateElement(this.mCurrentMode, null, 2);
    }

    public boolean isButtonVisible() {
        int i = this.mCurrentState;
        boolean z = false;
        if (i == -1) {
            return false;
        }
        if (i == -200 || i != -100) {
            return true;
        }
        if (this.mImageBokehIndicator.getVisibility() == 0) {
            z = true;
        }
        return z;
    }

    public boolean isInteractive() {
        if (!isEnableClick()) {
            return false;
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        return cameraAction == null || (!cameraAction.isDoingAction() && !cameraAction.isRecording());
    }

    public boolean isZoomPanelVisible() {
        if (this.mCurrentState != -200) {
            return false;
        }
        AbstractZoomSliderAdapter abstractZoomSliderAdapter = this.mSlidingAdapter;
        if (abstractZoomSliderAdapter != null) {
            return abstractZoomSliderAdapter.isEnable();
        }
        return false;
    }

    public boolean isZoomSliderViewIdle() {
        HorizontalZoomView horizontalZoomView = this.mHorizontalSlideView;
        return horizontalZoomView == null || horizontalZoomView.isIdle();
    }

    public boolean isZoomVisible() {
        int i = this.mCurrentState;
        return i != -1 && i == -200 && this.mZoomRatioToggleView.getVisibility() == 0;
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        if (this.mCurrentState == -100) {
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            if (miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) {
                Completable.create(new FolmeAlphaOutOnSubscribe(this.mImageBokehIndicator)).subscribe();
                return;
            }
        }
        provideAnimateElement(this.mCurrentMode, null, 2);
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
        if (i == 3) {
            adjustViewBackground(this.mHorizontalSlideLayout, this.mCurrentMode);
        }
    }

    public boolean onBackEvent(int i) {
        if (!isVisible(this.mDualParentLayout)) {
            return false;
        }
        boolean z = i == 4;
        int i2 = this.mCurrentState;
        if (i2 == -1) {
            return false;
        }
        if (i2 == -200) {
            if (this.mIsHiding) {
                return false;
            }
            boolean z2 = i == 3 && this.mCurrentMode == 173 && !DataRepository.dataItemGlobal().isOnSuperNightAlgoUpAndQuickShot();
            if (!z2 && this.mHorizontalSlideLayout.getVisibility() != 0) {
                return false;
            }
            if (z2) {
                hideZoomButton();
            } else if (i == 3) {
                return false;
            } else {
                if (i == 8) {
                    toHideZoomPanel(true, false);
                    return false;
                } else if (i == 9) {
                    hideZoomButton();
                    return false;
                } else if (i == 2) {
                    toHideZoomPanel(!z);
                    return false;
                } else {
                    toHideZoomPanel(!z);
                }
            }
        } else if (i2 == -100) {
            if (this.mHorizontalSlideLayout.getVisibility() != 0 || i == 3) {
                return false;
            }
            toHideBokehPanel(!z);
            if (!z) {
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                if (bottomPopupTips != null) {
                    bottomPopupTips.reInitTipImage();
                }
            }
        }
        return true;
    }

    public void onClick(View view) {
        String str;
        boolean isEnableClick = isEnableClick();
        String str2 = TAG;
        if (!isEnableClick) {
            str = "ignore click due to disabled";
        } else {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction == null || !cameraAction.isDoingAction()) {
                if (this.mCurrentState != -1 && view.getId() == R.id.dual_camera_bokeh_indicator) {
                    Log.u(str2, "onClick dual_camera_bokeh_indicator");
                    MistatsWrapper.moduleUIClickEvent("M_portrait_", PortraitAttr.VALUE_BOKEH_ADJUST_ENTRY, (Object) Integer.valueOf(1));
                    ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
                    ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                    if (!(actionProcessing == null || !actionProcessing.isShowFilterView() || configChanges == null)) {
                        configChanges.showOrHideFilter();
                    }
                    showBokehPanel();
                }
                return;
            }
            str = "ignore click due to doing action";
        }
        Log.d(str2, str);
    }

    public void onClick(ZoomRatioView zoomRatioView) {
        float[] fArr;
        ValueAnimator valueAnimator;
        boolean isInteractive = isInteractive();
        String str = TAG;
        if (!isInteractive) {
            Log.w(str, "onClick(): ignored due to not interactive");
            return;
        }
        int zoomRatioIndex = zoomRatioView.getZoomRatioIndex();
        if (!isZoomPanelVisible()) {
            StringBuilder sb = new StringBuilder();
            sb.append("onClick(): current zoom ratio index = ");
            sb.append(zoomRatioIndex);
            Log.d(str, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onClick(): current zoom ratio value = ");
            sb2.append(this.mZoomRatio);
            Log.d(str, sb2.toString());
            if (this.mZoomRatioToggleView.isSuppressed()) {
                if (CameraSettings.isZoomByCameraSwitchingSupported()) {
                    switchCameraLens();
                } else {
                    float f = 2.0f;
                    if (DataRepository.dataItemGlobal().isNormalIntent()) {
                        f = HybridZoomingSystem.getTeleZoomRatio(this.mCurrentMode);
                    }
                    if (C0122O00000o.instance().OOOOoOo()) {
                        Camera2DataContainer instance = Camera2DataContainer.getInstance();
                        if (instance != null && CameraSettings.is8KCamcorderSupported(instance.getUltraTeleCameraId()) && CameraSettings.isVideoQuality8KOpen(this.mCurrentMode) && this.mCurrentMode == 162) {
                            f = HybridZoomingSystem.getUltraTeleMinZoomRatio();
                        }
                    }
                    float f2 = this.mZoomRatio;
                    if (f2 == 1.0f) {
                        CameraStatUtils.trackDualZoomChanged(this.mCurrentMode, HybridZoomingSystem.toString(f));
                        this.mIsZoomTo2X = true;
                        this.mTargetZoomRatio = f;
                        valueAnimator = this.mZoomRatioToggleAnimator;
                        fArr = new float[]{this.mZoomRatio, f};
                    } else if (f2 <= f) {
                        CameraStatUtils.trackDualZoomChanged(this.mCurrentMode, HybridZoomingSystem.toString(1.0f));
                        this.mIsZoomTo2X = false;
                        this.mTargetZoomRatio = 1.0f;
                        this.mZoomRatioToggleAnimator.setFloatValues(new float[]{this.mZoomRatio, 1.0f});
                        this.mZoomRatioToggleAnimator.start();
                    } else {
                        CameraStatUtils.trackDualZoomChanged(this.mCurrentMode, HybridZoomingSystem.toString(1.0f));
                        requestZoomRatio(f, 0);
                        requestZoomRatio(1.0f, 0);
                    }
                }
                ViberatorContext.getInstance(getContext().getApplicationContext()).performSelectZoomNormal();
            } else {
                if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                    if (((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)) != null) {
                        CameraClickObservable cameraClickObservable = (CameraClickObservable) ModeCoordinatorImpl.getInstance().getAttachProtocol(227);
                        if (cameraClickObservable != null) {
                            cameraClickObservable.subscribe(167);
                        }
                        float opticalZoomRatioAt = HybridZoomingSystem.getOpticalZoomRatioAt(this.mCurrentMode, zoomRatioIndex);
                        CameraStatUtils.trackDualZoomChanged(this.mCurrentMode, HybridZoomingSystem.toString(opticalZoomRatioAt));
                        this.mIsZoomTo2X = false;
                        this.mTargetZoomRatio = opticalZoomRatioAt;
                        valueAnimator = this.mZoomRatioToggleAnimator;
                        fArr = new float[]{this.mZoomRatio, opticalZoomRatioAt};
                    } else {
                        return;
                    }
                }
                ViberatorContext.getInstance(getContext().getApplicationContext()).performSelectZoomNormal();
            }
            valueAnimator.setFloatValues(fArr);
            this.mZoomRatioToggleAnimator.start();
            ViberatorContext.getInstance(getContext().getApplicationContext()).performSelectZoomNormal();
        }
        onBackEvent(5);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.reConfigQrCodeTip();
        }
    }

    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (this.mCurrentState == -1 && !z) {
            this.mDualParentLayout.setVisibility(8);
        }
    }

    public void onIndexButtonClick(View view) {
        if (this.mHorizontalSlideView != null) {
            this.mHorizontalSlideView.setSelection(Math.round(this.mSlidingAdapter.mapValueToPosition(String.valueOf(view.getTag()))), false);
            CameraStatUtils.trackDualZoomChanged(this.mCurrentMode, view.getTag().toString());
        }
    }

    public void onManuallyDataChanged(String str) {
        if (!isInModeChanging()) {
            ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
            if (manuallyValueChanged != null) {
                int i = this.mCurrentState;
                if (i == -200) {
                    manuallyValueChanged.onDualZoomValueChanged(Float.valueOf(str).floatValue(), 3);
                } else if (i == -100) {
                    manuallyValueChanged.onBokehFNumberValueChanged(str);
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (topAlert != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(SupportedConfigFactory.CLOSE_BY_BOKEH);
                        sb.append(str);
                        topAlert.alertUpdateValue(4, 0, sb.toString());
                    }
                }
            }
        }
    }

    public void onPause() {
        super.onPause();
        this.mHandler.removeMessages(1);
    }

    public void onScrollEnd() {
        BaseModule baseModule = (BaseModule) ((ActivityBase) getActivity()).getCurrentModule();
        CameraSettings.writeTargetZoom(0.0f);
        baseModule.updatePreferenceTrampoline(79);
    }

    public void onScrollStart(float f) {
        BaseModule baseModule = (BaseModule) ((ActivityBase) getActivity()).getCurrentModule();
        CameraSettings.writeTargetZoom(f);
        baseModule.updatePreferenceTrampoline(79);
    }

    public void onTouchUpState() {
        if (this.mCurrentState == -200) {
            BaseModule baseModule = (BaseModule) ((ActivityBase) getActivity()).getCurrentModule();
            CameraStatUtils.trackZoomAdjusted(BaseEvent.SLIDER, false);
            baseModule.onZoomingActionEnd(3);
        }
    }

    public void onZoomItemSlideOn(int i, boolean z) {
        int i2 = this.mCurrentState;
        if (i2 == -100) {
            if (canProvide()) {
                ((ActivityBase) getContext()).playCameraSound(6, 0.5f);
            }
            ViberatorContext.getInstance(getContext().getApplicationContext()).performBokehAdjust();
        } else if (i2 == -200) {
            ViberatorContext instance = ViberatorContext.getInstance(getContext());
            if (z) {
                instance.performSelectZoomNormal();
            } else {
                instance.performSelectZoomLight();
            }
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        FolmeAlphaInOnSubscribe folmeAlphaInOnSubscribe;
        Completable create;
        int i3 = this.mCurrentMode;
        super.provideAnimateElement(i, list, i2);
        LayoutParams layoutParams = this.mHorizontalSlideLayout.getLayoutParams();
        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.manual_popup_layout_height);
        this.mZoomSliderLayoutHeight = layoutParams.height;
        this.mHorizontalSlideLayout.setLayoutParams(layoutParams);
        this.mIsRecordingOrPausing = false;
        initiateZoomRatio();
        onBackEvent(i2 == 2 ? 5 : 4);
        ViewSpec viewSpecForCapturingMode = getViewSpecForCapturingMode(this.mCurrentMode);
        if (viewSpecForCapturingMode.visibility == -200) {
            StringBuilder sb = new StringBuilder();
            sb.append("provideAnimateElement(): initialized zoom ratio: ");
            sb.append(this.mZoomRatio);
            Log.d(TAG, sb.toString());
            this.mZoomRatioToggleView.setCapturingMode(this.mCurrentMode, viewSpecForCapturingMode.suppress);
            this.mZoomRatioToggleView.setRotation((float) this.mDegree);
            this.mZoomRatioToggleView.setZoomRatio(this.mZoomRatio, -1);
            this.mZoomRatioToggleView.setUseSliderAllowed(viewSpecForCapturingMode.useSliderType);
            this.mUseSliderType = viewSpecForCapturingMode.useSliderType;
        }
        adjustViewBackground(this.mHorizontalSlideLayout, this.mCurrentMode);
        int i4 = viewSpecForCapturingMode.visibility;
        if (i == 171) {
            if (!DataRepository.dataItemGlobal().isNormalIntent() || !C0122O00000o.instance().isSupportBokehAdjust()) {
                i4 = -1;
            } else {
                this.mImageBokehIndicator.setRotation((float) this.mDegree);
                i4 = -100;
            }
        }
        if (i4 != this.mCurrentState) {
            this.mCurrentState = i4;
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            MasterFilterProtocol masterFilterProtocol = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
            if ((miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) || (masterFilterProtocol != null && masterFilterProtocol.isShowing())) {
                this.mCurrentState = -1;
            }
            int i5 = this.mCurrentState;
            if (i5 != -200) {
                if (i5 == -100) {
                    this.mDualParentLayout.setVisibility(0);
                    FolmeAlphaOutOnSubscribe.directSetGone(this.mZoomRatioToggleView);
                    FolmeAlphaInOnSubscribe.directSetResult(this.mHorizontalSlideView);
                    FolmeAlphaInOnSubscribe.directSetResult(this.mImageBokehIndicator);
                    if (!isVisible(this.mImageBokehIndicator)) {
                        ImageView imageView = this.mImageBokehIndicator;
                        if (list == null) {
                            folmeAlphaInOnSubscribe = new FolmeAlphaInOnSubscribe(imageView);
                            Completable.create(folmeAlphaInOnSubscribe).subscribe();
                        } else {
                            folmeAlphaInOnSubscribe = new FolmeAlphaInOnSubscribe(imageView);
                        }
                    }
                } else if (i5 == -1 && isVisible(this.mDualParentLayout)) {
                    this.mDualParentLayout.setVisibility(8);
                }
            }
            this.mDualParentLayout.setVisibility(0);
            FolmeAlphaOutOnSubscribe.directSetGone(this.mImageBokehIndicator);
            FolmeAlphaInOnSubscribe.directSetResult(this.mZoomRatioToggleView);
            if (list == null || (i == 165 && i3 != 167)) {
                folmeAlphaInOnSubscribe = new FolmeAlphaInOnSubscribe(this.mZoomRatioToggleView);
                Completable.create(folmeAlphaInOnSubscribe).subscribe();
            }
            ZoomRatioToggleView zoomRatioToggleView = this.mZoomRatioToggleView;
            if (i3 == 167) {
                folmeAlphaInOnSubscribe = new FolmeAlphaInOnSubscribe(zoomRatioToggleView);
                create = Completable.create(folmeAlphaInOnSubscribe.setStartDelayTime(150));
                list.add(create);
            }
            folmeAlphaInOnSubscribe = new FolmeAlphaInOnSubscribe(zoomRatioToggleView);
            create = Completable.create(folmeAlphaInOnSubscribe);
            list.add(create);
        }
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        if (this.mCurrentState == -200) {
            ZoomRatioToggleView zoomRatioToggleView = this.mZoomRatioToggleView;
            if (zoomRatioToggleView != null) {
                list.add(zoomRatioToggleView);
            }
        }
        if (this.mCurrentState == -200) {
            ZoomIndexButtonsLayout zoomIndexButtonsLayout = this.mZoomIndexButtons;
            if (zoomIndexButtonsLayout != null) {
                zoomIndexButtonsLayout.provideRotateItem(list, i);
            }
        }
        if (this.mImageBokehIndicator.getVisibility() == 0) {
            list.add(this.mImageBokehIndicator);
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(182, this);
        registerBackStack(modeCoordinator, this);
        if (C0124O00000oO.isSupportedOpticalZoom() || HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            modeCoordinator.attachProtocol(184, this);
        }
    }

    public void setClickEnable(boolean z) {
        super.setClickEnable(z);
        ZoomRatioToggleView zoomRatioToggleView = this.mZoomRatioToggleView;
        if (zoomRatioToggleView != null) {
            zoomRatioToggleView.setEnabled(z);
        }
        ImageView imageView = this.mImageBokehIndicator;
        if (imageView != null) {
            imageView.setEnabled(z);
        }
    }

    public void setRecordingOrPausing(boolean z) {
        this.mIsRecordingOrPausing = z;
    }

    public void setSnapNumValue(int i) {
        this.mZoomRatioToggleView.setCaptureCount(i);
    }

    public void setSnapNumVisible(boolean z, boolean z2) {
        ViewGroup viewGroup = this.mHorizontalSlideLayout;
        if (viewGroup != null) {
            if (z) {
                if (!z2) {
                    ViewCompat.setRotation(this.mZoomRatioToggleView, (float) this.mDegree);
                    this.mZoomRatioToggleView.setVisibility(0);
                } else if (viewGroup.getVisibility() == 0) {
                    toHideZoomPanel(true);
                }
                this.mZoomRatioToggleView.setImmersive(true, false, z2);
            } else {
                updateZoomRatio(-1);
                this.mZoomRatioToggleView.setImmersive(false, false);
            }
        }
    }

    public void showBokehButton() {
        if (this.mCurrentState != -100 && DataRepository.dataItemGlobal().isNormalIntent() && C0122O00000o.instance().isSupportBokehAdjust()) {
            this.mCurrentState = -100;
            this.mDualParentLayout.setVisibility(0);
            Completable.create(new FolmeAlphaInOnSubscribe(this.mImageBokehIndicator)).subscribe();
        }
    }

    public void showZoomButton() {
        if (this.mCurrentState != -200 && !this.mIsRecordingOrPausing && getViewSpecForCapturingMode(this.mCurrentMode).visibility != -1) {
            this.mDualParentLayout.setVisibility(0);
            this.mCurrentState = -200;
            updateZoomRatio(-1);
            ViewCompat.setRotation(this.mZoomRatioToggleView, (float) this.mDegree);
            Completable.create(new FolmeAlphaInOnSubscribe(this.mZoomRatioToggleView)).subscribe();
        }
    }

    public boolean toShowSlideView(ZoomRatioView zoomRatioView) {
        boolean showZoomPanel = showZoomPanel();
        CameraStatUtils.trackShowZoomBarByScroll(showZoomPanel);
        updateZoomSlider();
        sendHideMessage();
        return showZoomPanel;
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        this.mHandler.removeCallbacksAndMessages(null);
        modeCoordinator.detachProtocol(182, this);
        unRegisterBackStack(modeCoordinator, this);
        if (C0124O00000oO.isSupportedOpticalZoom() || HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            modeCoordinator.detachProtocol(184, this);
        }
    }

    public boolean updateSlideAndZoomRatio(int i) {
        boolean showZoomPanel = (this.mUseSliderType == 0 || !(i == 2 || i == 1) || HybridZoomingSystem.toDecimal(CameraSettings.getRetainZoom(this.mCurrentMode)) < 2.0f) ? false : showZoomPanel();
        updateZoomRatio(i);
        if (isZoomPanelVisible()) {
            sendHideMessage();
        }
        return showZoomPanel;
    }

    public void updateZoomIndexsButton() {
        ZoomIndexButtonsLayout zoomIndexButtonsLayout = this.mZoomIndexButtons;
        if (zoomIndexButtonsLayout != null) {
            zoomIndexButtonsLayout.setSelect(this.mZoomRatio, true);
        }
    }

    public void updateZoomRatio(int i) {
        float f;
        if (CameraSettings.isZoomByCameraSwitchingSupported()) {
            String cameraLensType = CameraSettings.getCameraLensType(this.mCurrentMode);
            if (ComponentManuallyDualLens.LENS_ULTRA.equals(cameraLensType)) {
                f = HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR;
            } else if (ComponentManuallyDualLens.LENS_WIDE.equals(cameraLensType)) {
                f = 1.0f;
            } else if (ComponentManuallyDualLens.LENS_TELE.equals(cameraLensType)) {
                f = HybridZoomingSystem.getTeleMinZoomRatio();
            } else if (ComponentManuallyDualLens.LENS_ULTRA_TELE.equals(cameraLensType)) {
                f = HybridZoomingSystem.getUltraTeleMinZoomRatio();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("updateZoomRatio(): Unknown camera lens type: ");
                sb.append(cameraLensType);
                throw new IllegalStateException(sb.toString());
            }
        } else {
            f = CameraSettings.getRetainZoom(this.mCurrentMode);
        }
        this.mZoomRatio = f;
        if (!this.mZoomRatioToggleAnimator.isRunning() || HybridZoomingSystem.isOpticalZoomRatio(this.mCurrentMode, this.mZoomRatio) || (!DataRepository.dataItemGlobal().isNormalIntent() && this.mZoomRatio == 2.0f)) {
            this.mZoomRatioToggleView.setZoomRatio(this.mZoomRatio, i);
            if (i != 3) {
                updateZoomSlider();
            }
        }
    }

    public int visibleHeight() {
        int i = this.mCurrentState;
        if (i == -1 || (i == -100 && this.mImageBokehIndicator.getVisibility() == 8)) {
            return 0;
        }
        return this.mButtonLayoutHeight;
    }
}
