package com.android.camera.fragment;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.StringRes;
import androidx.core.view.ViewCompat;
import com.android.camera.ActivityBase;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.FragmentAnimationFactory;
import com.android.camera.animation.folme.FolmeAlphaInOnSubscribe;
import com.android.camera.animation.folme.FolmeAlphaOutOnSubscribe;
import com.android.camera.animation.folme.FolmeTranslateYOnSubscribe;
import com.android.camera.constant.ColorConstant;
import com.android.camera.constant.DurationConstant;
import com.android.camera.customization.TintColor;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.data.data.runing.ComponentRunningFastMotion;
import com.android.camera.data.data.runing.ComponentRunningFastMotionPro;
import com.android.camera.data.data.runing.ComponentRunningShine;
import com.android.camera.features.mimoji2.widget.helper.MimojiStatusManager2;
import com.android.camera.log.Log;
import com.android.camera.permission.PermissionManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CameraModuleSpecial;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.FastMotionProtocol;
import com.android.camera.protocol.ModeProtocol.FastmotionProAdjust;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.MakeupProtocol;
import com.android.camera.protocol.ModeProtocol.ManuallyAdjust;
import com.android.camera.protocol.ModeProtocol.MasterFilterProtocol;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.ModeChangeController;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.WatermarkProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.BeautyAttr;
import com.android.camera.statistic.MistatsConstants.CaptureAttr;
import com.android.camera.statistic.MistatsConstants.Fastmotion;
import com.android.camera.statistic.MistatsConstants.FeatureName;
import com.android.camera.statistic.MistatsConstants.Live;
import com.android.camera.statistic.MistatsConstants.MiLive;
import com.android.camera.statistic.MistatsConstants.Mimoji;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.timerburst.TimerBurstController;
import com.android.camera.ui.ColorImageView;
import io.reactivex.Completable;
import io.reactivex.CompletableOnSubscribe;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FragmentBottomPopupTips extends BaseFragment implements OnClickListener, BottomPopupTips, HandleBackTrace {
    private static final int ANIM_DELAY_SHOW = 3;
    private static final int ANIM_DIRECT_HIDE = 1;
    private static final int ANIM_DIRECT_SHOW = 0;
    private static final int ANIM_HIDE = 4;
    private static final int ANIM_SHOW = 2;
    private static final int CALL_TYPE_NOTIFY = 1;
    private static final int CALL_TYPE_PROVIDE = 0;
    public static final int FRAGMENT_INFO = 65529;
    private static final int LEFT_TIP_IMAGE_AI_WATERMARK = 23;
    private static final int LEFT_TIP_IMAGE_CLOSE = 20;
    private static final int LEFT_TIP_IMAGE_FAST_MOTION = 25;
    private static final int LEFT_TIP_IMAGE_KALEIDOSCOPE = 22;
    private static final int LEFT_TIP_IMAGE_LIGHTING = 19;
    private static final int LEFT_TIP_IMAGE_SPEED = 33;
    private static final int LEFT_TIP_IMAGE_STICKER = 18;
    private static final int LEFT_TIP_IMAGE_SUPER_MOON = 32;
    private static final int LEFT_TIP_IMAGE_ULTRA_WIDE = 21;
    private static final int LEFT_TIP_IMAGE_VIDEO_BEAUTY = 24;
    private static final long MAX_RED_DOT_TIME = 86400000;
    private static final int MSG_HIDE_TIP = 1;
    private static final String TAG = "FragmentBottomPopupTips";
    private static final int TIP_FASTMOTION_PRO = 37;
    private static final int TIP_ID_CARD = 4;
    private static final int TIP_IMAGE_INVALID = -1;
    private static final int TIP_IMAGE_STICKER = 2;
    private static final int TIP_MIMOJI = 34;
    private static final int TIP_SHINE = 3;
    private static final int TIP_SWITCH_CAMERA = 36;
    private boolean INIT_TAG = false;
    private int mBottomTipHeight;
    private View mCenterRedDot;
    private ImageView mCenterTipImage;
    private int mCloseType = 0;
    private String mCurrentTipMessage;
    /* access modifiers changed from: private */
    public int mCurrentTipType;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                FragmentBottomPopupTips.this.mTipMessage.setVisibility(8);
                if (FragmentBottomPopupTips.this.mCurrentMode == 163) {
                    CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
                    if (cameraModuleSpecial != null) {
                        cameraModuleSpecial.showOrHideChip(true);
                    }
                }
                if ((FragmentBottomPopupTips.this.mLastTipType == 6 && FragmentBottomPopupTips.this.mCurrentTipType != 8 && !FragmentBottomPopupTips.this.isPortraitHintVisible()) || (FragmentBottomPopupTips.this.mLastTipType == 10 && CameraSettings.isEyeLightOpen())) {
                    FragmentBottomPopupTips fragmentBottomPopupTips = FragmentBottomPopupTips.this;
                    fragmentBottomPopupTips.showTips(fragmentBottomPopupTips.mLastTipType, FragmentBottomPopupTips.this.mLastTipMessage, 4);
                } else if ((FragmentBottomPopupTips.this.mLastTipType != 18 || !CameraSettings.isMacroModeEnabled(FragmentBottomPopupTips.this.mCurrentMode)) && (FragmentBottomPopupTips.this.mLastTipType != 8 || !CameraSettings.isMacroModeEnabled(FragmentBottomPopupTips.this.mCurrentMode))) {
                    FragmentBottomPopupTips.this.updateLyingDirectHint(false, true);
                }
                FragmentBottomPopupTips.this.mLastTipType = 4;
            }
        }
    };
    private boolean mIsShowLeftImageIntro;
    private boolean mIsShowLyingDirectHint;
    /* access modifiers changed from: private */
    public String mLastTipMessage;
    /* access modifiers changed from: private */
    public int mLastTipType;
    /* access modifiers changed from: private */
    public FrameLayout mLeftImageIntro;
    private AnimatorSet mLeftImageIntroAnimator;
    private TextView mLeftImageIntroContent;
    private int mLeftImageIntroRadius;
    private int mLeftImageIntroWidth;
    private ImageView mLeftTipExtraImage;
    private ImageView mLeftTipImage;
    private TextView mLyingDirectHint;
    private LinearLayout mNearRangeModeButton;
    private boolean mNeedShowIDCardTip;
    private View mPortraitSuccessHint;
    private ImageView mQrCodeButton;
    private ImageView mRightTipExtraImage;
    private ImageView mRightTipImage;
    private View mRootView;
    private ImageView mTipImage;
    /* access modifiers changed from: private */
    public TextView mTipMessage;

    @Retention(RetentionPolicy.SOURCE)
    @interface TipImageType {
    }

    private void adjustViewBackground(int i) {
        if (DataRepository.dataItemRunning().getUiStyle() == 1) {
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002e, code lost:
        if (r6 != true) goto L_0x005a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0007, code lost:
        if (r9 != false) goto L_0x0009;
     */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyAnimType(List list, int i, boolean z, View view) {
        boolean z2;
        CompletableOnSubscribe completableOnSubscribe;
        if (list != null) {
            if (!z) {
                if (!(i == 165 || this.mCurrentMode == 165)) {
                    z2 = true;
                }
                z2 = true;
            } else if (i != 165) {
                z2 = true;
            } else if (!C0124O00000oO.isSupportedOpticalZoom()) {
                z2 = true;
            }
            if (!z2) {
                if (!z2) {
                    if (z2) {
                        completableOnSubscribe = new FolmeAlphaInOnSubscribe(view);
                    } else if (z2) {
                        completableOnSubscribe = new FolmeAlphaInOnSubscribe(view).setStartDelayTime(150);
                    }
                    list.add(Completable.create(completableOnSubscribe));
                }
                FolmeAlphaOutOnSubscribe.directSetResult(view);
            } else {
                Completable.create(new FolmeAlphaInOnSubscribe(view)).subscribe();
            }
            FolmeUtils.touchScaleTint(view);
        }
        z2 = false;
        if (!z2) {
        }
        FolmeUtils.touchScaleTint(view);
    }

    private void checkAIWatermarkTip() {
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        String str = WatermarkConstant.FIRST_USE;
        if (dataItemGlobal.getBoolean(str, true)) {
            DataRepository.dataItemGlobal().putBoolean(str, false);
            if (!PermissionManager.checkCameraLocationPermissions()) {
                Context context = getContext();
                Toast.makeText(context, context.getString(R.string.aiwatermark_location_tips), 0).show();
            }
        }
    }

    private void clickAIWatermarkListEnter() {
        WatermarkProtocol watermarkProtocol = (WatermarkProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(253);
        if (watermarkProtocol == null || !watermarkProtocol.isWatermarkPanelShow()) {
            hideAllTipImage();
            showAIWatermark(this.mCurrentMode);
        }
    }

    private void closeFilter() {
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges != null) {
            configChanges.showOrHideFilter();
        }
    }

    private void closeLight() {
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges != null) {
            configChanges.showOrHideLighting(false);
        }
        updateLeftTipImage();
    }

    private boolean currentIsIDCardShow() {
        ImageView imageView = this.mTipImage;
        return (imageView == null || imageView.getTag() == null || ((Integer) this.mTipImage.getTag()).intValue() != 4) ? false : true;
    }

    private int getLeftImageIntroWide() {
        this.mLeftImageIntroContent.measure(0, 0);
        int measuredWidth = this.mLeftImageIntroContent.getMeasuredWidth();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.popup_tip_beauty_intro_left_padding);
        return measuredWidth + dimensionPixelSize + getResources().getDimensionPixelSize(R.dimen.popup_tip_beauty_intro_right_padding) + ((this.mLeftImageIntroRadius - dimensionPixelSize) * 2);
    }

    private int getMarginEnd() {
        return getResources().getDimensionPixelSize(C0122O00000o.instance().OOO0OoO() ? R.dimen.popup_indicator_button_extra_margin_end_support_idcard : R.dimen.popup_indicator_button_extra_margin_end);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00e7, code lost:
        if (r2.isBeautyPanelShow() != false) goto L_0x008e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00f0, code lost:
        if (r1.isShowLightingView() != false) goto L_0x008e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0046, code lost:
        if (r0.getVisibility() != 0) goto L_0x004c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x004a, code lost:
        if (com.android.camera.HybridZoomingSystem.IS_3_OR_MORE_SAT != false) goto L_0x004c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getTipBottomMargin(int i) {
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.beauty_extra_tip_bottom_margin);
        int i2 = this.mCurrentMode;
        if (i2 == 165) {
            if (CameraSettings.isUltraWideConfigOpen(i2)) {
                ImageView imageView = this.mCenterTipImage;
                if (imageView != null) {
                }
            }
            return getResources().getDimensionPixelSize(R.dimen.tips_margin_bottom_normal) + Display.getSquareBottomCoverHeight();
        } else if (manuallyAdjust != null && manuallyAdjust.visibleHeight() > 0) {
            return manuallyAdjust.visibleHeight();
        } else {
            if (this.mCenterTipImage.getVisibility() == 0) {
                if (miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow()) {
                    return this.mTipImage.getHeight();
                }
            } else if (dualController != null && dualController.isButtonVisible()) {
                return dualController.visibleHeight();
            } else {
                MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
                ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
                if (makeupProtocol == null || !makeupProtocol.isSeekBarVisible()) {
                    if (miBeautyProtocol != null) {
                    }
                    if (actionProcessing != null) {
                    }
                } else {
                    return getResources().getDimensionPixelSize(R.dimen.beautycamera_popup_fragment_height) + getResources().getDimensionPixelSize(R.dimen.beauty_fragment_height);
                }
            }
            return getResources().getDimensionPixelSize(R.dimen.beauty_fragment_height) + dimensionPixelSize;
        }
        return (getResources().getDimensionPixelSize(R.dimen.manually_indicator_layout_height) / 2) - (i / 2);
    }

    private void hideAllTipImage() {
        hidePanelEnter();
        directHideLeftImageIntro();
        hideTip(this.mTipMessage);
        hideZoomTipImage(this.mCurrentMode);
    }

    private void hidePanelEnter() {
        hideLeftTipImage();
        hideRightTipImage();
        hideTipImage();
        hideCenterTipImage();
    }

    private boolean hideTip(View view) {
        if (view.getVisibility() == 8) {
            return false;
        }
        view.setVisibility(8);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002d, code lost:
        if (com.android.camera.HybridZoomingSystem.IS_3_OR_MORE_SAT == false) goto L_0x0054;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void hideZoomTipImage(int i) {
        if (i != 165) {
            if (i != 166) {
                if (!(i == 169 || i == 183)) {
                    if (!(i == 188 || i == 173)) {
                        if (i != 174) {
                            switch (i) {
                                case 161:
                                case 162:
                                    break;
                                case 163:
                                    break;
                            }
                        }
                    }
                }
            } else if (!C0122O00000o.instance().OOOO0OO()) {
                return;
            }
        }
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null) {
            dualController.hideZoomButton();
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.alertUpdateValue(0, 0, null);
            }
        }
    }

    private boolean ignoreClick() {
        if (!isEnableClick()) {
            return true;
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction == null || !cameraAction.isDoingAction()) {
            return CameraSettings.isFrontCamera() && ((Camera) getContext()).isScreenSlideOff();
        }
        return true;
    }

    private boolean isPortraitSuccessHintVisible() {
        return this.mPortraitSuccessHint.getVisibility() == 0;
    }

    private void onLeftImageClick(View view) {
        int i;
        int intValue = ((Integer) view.getTag()).intValue();
        Log.u(TAG, String.format(Locale.ENGLISH, "onClick onLeftImageClick: type=0x%x", new Object[]{Integer.valueOf(intValue)}));
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (intValue != 3) {
            switch (intValue) {
                case 19:
                    if (configChanges != null) {
                        hideAllTipImage();
                        i = 203;
                        break;
                    } else {
                        return;
                    }
                case 20:
                    int i2 = this.mCloseType;
                    if (i2 == 1) {
                        closeFilter();
                        return;
                    } else if (i2 == 2) {
                        closeLight();
                        return;
                    } else {
                        return;
                    }
                case 21:
                    if (configChanges != null && !HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                        i = 205;
                        break;
                    } else {
                        return;
                    }
                    break;
                case 22:
                    hideAllTipImage();
                    showKaleidoscope();
                    return;
                case 23:
                    clickAIWatermarkListEnter();
                    return;
                case 24:
                    showOrHideVideoBeautyPanel();
                    return;
                case 25:
                    if (C0122O00000o.instance().OOO00O0() || C0122O00000o.instance().OOO00Oo()) {
                        showFastMotionPanel();
                        return;
                    }
                    return;
                default:
                    switch (intValue) {
                        case 32:
                            hideAllTipImage();
                            showSuperMoonEffect(this.mCurrentMode);
                            return;
                        case 33:
                            hideAllTipImage();
                            CameraStatUtils.trackMiLiveClick(MiLive.VALUE_MI_LIVE_CLICK_SPEED);
                            showLiveSpeed();
                            return;
                        case 34:
                            if (this.mCurrentMode != 184) {
                                return;
                            }
                            if (DataRepository.dataItemLive().getMimojiStatusManager2().getMimojiRecordState() == 1) {
                                showMimojiPanel(3);
                                return;
                            } else {
                                showMimojiPanel(2);
                                return;
                            }
                        default:
                            return;
                    }
            }
            configChanges.onConfigChanged(i);
            return;
        }
        hideAllTipImage();
        showBeauty(this.mCurrentMode);
    }

    private void reConfigBottomTipOfMimoji() {
        if (this.mCurrentMode == 184) {
            updateMimojiBottomTipImage();
        }
    }

    private void reIntTipViewMarginBottom(View view, int i) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        int tipBottomMargin = getTipBottomMargin(i);
        if (marginLayoutParams.bottomMargin != tipBottomMargin) {
            marginLayoutParams.bottomMargin = tipBottomMargin;
            view.setLayoutParams(marginLayoutParams);
        }
    }

    /* access modifiers changed from: private */
    public void setBeautyIntroButtonWidth(View view, int i) {
        if (view != null) {
            LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = i;
            view.setLayoutParams(layoutParams);
        }
    }

    private void showAIWatermark(int i) {
        if (i == 163 || i == 205) {
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChanges != null) {
                configChanges.showOrHideAIWatermark();
            }
        }
    }

    private void showBeauty(int i) {
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges != null) {
            configChanges.showOrHideShine();
        }
    }

    private void showKaleidoscope() {
    }

    private void showLiveSpeed() {
        BottomMenuProtocol bottomMenuProtocol = (BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197);
        if (bottomMenuProtocol != null) {
            bottomMenuProtocol.onSwitchLiveActionMenu(165);
        }
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            baseDelegate.delegateEvent(13);
        }
    }

    private void showLiveSticker() {
        CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_STICKER);
        BottomMenuProtocol bottomMenuProtocol = (BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197);
        if (bottomMenuProtocol != null) {
            bottomMenuProtocol.onSwitchLiveActionMenu(164);
        }
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            baseDelegate.delegateEvent(12);
        }
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.directlyHideTips();
        }
    }

    private void showSuperMoonEffect(int i) {
        Log.d(TAG, "showSuperMoonEffect E");
        if (i == 188) {
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChanges != null) {
                Log.d(TAG, "prepare to call showOrHideAIWatermark");
                configChanges.showOrHideAIWatermark();
            }
            Log.d(TAG, "showSuperMoonEffect X");
        }
    }

    private void startLeftImageIntroAnim(int i) {
        if (i != 1) {
            directShowOrHideLeftTipImage(false);
            this.mLeftImageIntro.setVisibility(0);
            if (this.mLeftImageIntroAnimator == null) {
                ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.mLeftImageIntroWidth, this.mLeftImageIntroRadius * 2});
                ofInt.setDuration(300);
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mLeftImageIntroContent, "alpha", new float[]{1.0f, 0.0f});
                ofFloat.setDuration(250);
                ofInt.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        FragmentBottomPopupTips fragmentBottomPopupTips = FragmentBottomPopupTips.this;
                        fragmentBottomPopupTips.setBeautyIntroButtonWidth(fragmentBottomPopupTips.mLeftImageIntro, ((Integer) valueAnimator.getAnimatedValue()).intValue());
                    }
                });
                this.mLeftImageIntroAnimator = new AnimatorSet();
                this.mLeftImageIntroAnimator.playTogether(new Animator[]{ofInt, ofFloat});
                this.mLeftImageIntroAnimator.setInterpolator(new PathInterpolator(0.25f, 0.1f, 0.25f, 0.1f));
                this.mLeftImageIntroAnimator.setStartDelay(2000);
                this.mLeftImageIntroAnimator.addListener(new AnimatorListenerAdapter() {
                    private boolean cancelled;

                    public void onAnimationCancel(Animator animator) {
                        this.cancelled = true;
                    }

                    public void onAnimationEnd(Animator animator) {
                        if (FragmentBottomPopupTips.this.canProvide() && !this.cancelled) {
                            FragmentBottomPopupTips.this.directHideLeftImageIntro();
                            FragmentBottomPopupTips.this.updateLeftTipImage();
                        }
                    }

                    public void onAnimationStart(Animator animator) {
                        this.cancelled = false;
                    }
                });
            } else {
                this.mLeftImageIntro.setAlpha(1.0f);
                this.mLeftImageIntroContent.clearAnimation();
                this.mLeftImageIntroAnimator.cancel();
            }
            this.mLeftImageIntroAnimator.start();
            CameraSettings.addPopupUltraWideIntroShowTimes();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0045, code lost:
        if (com.android.camera.data.DataRepository.dataItemLive().getMimojiStatusManager().getMimojiPannelState() == false) goto L_0x0047;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0028, code lost:
        if (r2.getMimojiPanelState() == 0) goto L_0x0047;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateCenterTipImage(int i, int i2, List list) {
        int i3;
        int i4;
        int i5 = i;
        if (i5 != 174) {
            if (i5 != 177) {
                if (i5 == 184) {
                    MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
                    if (mimojiStatusManager2.isInMimojiPreview()) {
                    }
                }
                i3 = -1;
            } else {
                if (DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiPreview()) {
                }
                i3 = -1;
            }
            i3 = 34;
        } else {
            i3 = 18;
        }
        FrameLayout frameLayout = (FrameLayout) this.mRootView.findViewById(R.id.popup_center_tip_layout);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
        int i6 = R.drawable.ic_vector_live_sticker;
        boolean z = true;
        boolean z2 = false;
        if (i3 == -1) {
            View view = this.mCenterRedDot;
            if (view != null) {
                view.setVisibility(8);
            }
            i4 = 0;
            i6 = 0;
            z = false;
        } else if (i3 == 18) {
            boolean z3 = !"".equals(CameraSettings.getCurrentLiveSticker());
            if (this.mCenterRedDot != null) {
                boolean tTLiveStickerNeedRedDot = CameraSettings.getTTLiveStickerNeedRedDot();
                long liveStickerRedDotTime = CameraSettings.getLiveStickerRedDotTime();
                long currentTimeMillis = System.currentTimeMillis();
                if ((liveStickerRedDotTime <= 0 || currentTimeMillis - liveStickerRedDotTime < 86400000) && tTLiveStickerNeedRedDot) {
                    this.mCenterRedDot.setVisibility(0);
                }
            }
            if (!HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                layoutParams.gravity = 81;
            } else {
                layoutParams.gravity = 8388691;
            }
            i4 = 0;
            z2 = z3;
        } else if (i3 != 34) {
            i4 = 0;
            i6 = 0;
        } else {
            String currentMimojiState = this.mCurrentMode == 184 ? DataRepository.dataItemLive().getMimojiStatusManager2().getCurrentMimojiState() : DataRepository.dataItemLive().getMimojiStatusManager().getCurrentMimojiState();
            i4 = R.string.accessibility_mimoji_image_panel_on;
            if (!"add_state".equals(currentMimojiState) && !"close_state".equals(currentMimojiState)) {
                z2 = true;
            }
            layoutParams.gravity = 81;
        }
        if (i6 > 0) {
            frameLayout.requestLayout();
            ((ColorImageView) this.mCenterTipImage).setColor(z2 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
            this.mCenterTipImage.setImageResource(i6);
        }
        if (i4 > 0) {
            this.mCenterTipImage.setContentDescription(getString(i4));
        }
        updateImageBgColor(i5, this.mCenterTipImage);
        if (this.mCenterTipImage.getTag() == null || ((Integer) this.mCenterTipImage.getTag()).intValue() != i3) {
            this.mCenterTipImage.setTag(Integer.valueOf(i3));
            applyAnimType(list, i2, z, this.mCenterTipImage);
        }
    }

    private void updateImageBgColor(int i, View view) {
        int i2 = R.drawable.square_module_bg_popup_indicator_no_stroke;
        if (i != 165) {
            int i3 = R.drawable.bg_popup_indicator_no_stroke;
            if (i != 184) {
                if (i == 188) {
                    i2 = R.drawable.bg_popup_indicator_super_moon;
                }
            } else if (CameraSettings.isGifOn()) {
                i3 = R.drawable.bg_popup_indicator_gif;
            }
            view.setBackgroundResource(i3);
            return;
        }
        view.setBackgroundResource(i2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00a1  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00a9 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00aa  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateLeftTipExtraImage(int i, int i2, List list) {
        int i3;
        boolean z;
        boolean z2;
        int i4;
        int i5;
        boolean z3;
        ImageView imageView = this.mLeftTipExtraImage;
        boolean z4 = true;
        if (i == 184) {
            MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
            if (mimojiStatusManager2.getMimojiPanelState() == 0 && !mimojiStatusManager2.isInMimojiCreate() && !mimojiStatusManager2.isInMimojiPreviewPlay() && mimojiStatusManager2.getCurrentMimojiInfo() != null && !CameraSettings.isGifOn() && CameraSettings.isFrontCamera() && mimojiStatusManager2.getMimojiRecordState() == 1) {
                i3 = 34;
                if (i3 != -1) {
                    z = true;
                    z3 = false;
                    i5 = 0;
                    i4 = 0;
                    z2 = false;
                } else if (i3 == 22) {
                    z2 = true;
                    i4 = 0;
                    z = false;
                    boolean isSwitchOn = DataRepository.dataItemRunning().getComponentRunningKaleidoscope().isSwitchOn();
                    i5 = R.drawable.ic_vector_kaleidoscope;
                    z3 = isSwitchOn;
                } else if (i3 != 34) {
                    z2 = true;
                    z = true;
                    z3 = false;
                    i5 = 0;
                    i4 = 0;
                } else {
                    z3 = DataRepository.dataItemLive().getMimojiStatusManager2().getCurrentMimojiBgInfo() != null;
                    i4 = R.string.accessibility_mimoji_image_panel_on;
                    z2 = true;
                    z = true;
                    i5 = 2131231631;
                }
                if (imageView.getTag() != null && ((Integer) imageView.getTag()).intValue() == i3) {
                    z4 = false;
                }
                ((ColorImageView) imageView).setColorAndRefresh(!z3 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                if (!z4) {
                    if (i5 > 0) {
                        imageView.setImageResource(i5);
                    }
                    if (i4 > 0) {
                        imageView.setContentDescription(getString(i4));
                    }
                    float f = (!z2 || !z) ? 0.0f : (float) this.mDegree;
                    ViewCompat.setRotation(imageView, f);
                    imageView.setTag(Integer.valueOf(i3));
                    applyAnimType(list, i2, z2, imageView);
                    return;
                }
                return;
            }
        }
        i3 = -1;
        if (i3 != -1) {
        }
        z4 = false;
        ((ColorImageView) imageView).setColorAndRefresh(!z3 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
        if (!z4) {
        }
    }

    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: type inference failed for: r4v3, types: [int] */
    /* JADX WARNING: type inference failed for: r3v3, types: [int] */
    /* JADX WARNING: type inference failed for: r3v13 */
    /* JADX WARNING: type inference failed for: r4v5 */
    /* JADX WARNING: type inference failed for: r6v5 */
    /* JADX WARNING: type inference failed for: r4v6 */
    /* JADX WARNING: type inference failed for: r3v14 */
    /* JADX WARNING: type inference failed for: r6v7, types: [int] */
    /* JADX WARNING: type inference failed for: r4v12 */
    /* JADX WARNING: type inference failed for: r3v15 */
    /* JADX WARNING: type inference failed for: r4v14 */
    /* JADX WARNING: type inference failed for: r4v15 */
    /* JADX WARNING: type inference failed for: r3v16 */
    /* JADX WARNING: type inference failed for: r3v17 */
    /* JADX WARNING: type inference failed for: r4v16 */
    /* JADX WARNING: type inference failed for: r3v20 */
    /* JADX WARNING: type inference failed for: r3v21 */
    /* JADX WARNING: type inference failed for: r3v22 */
    /* JADX WARNING: type inference failed for: r4v18 */
    /* JADX WARNING: type inference failed for: r4v19 */
    /* JADX WARNING: type inference failed for: r3v25 */
    /* JADX WARNING: type inference failed for: r3v26 */
    /* JADX WARNING: type inference failed for: r4v20 */
    /* JADX WARNING: type inference failed for: r4v21 */
    /* JADX WARNING: type inference failed for: r3v27 */
    /* JADX WARNING: type inference failed for: r3v28 */
    /* JADX WARNING: type inference failed for: r4v25 */
    /* JADX WARNING: type inference failed for: r17v2 */
    /* JADX WARNING: type inference failed for: r4v26 */
    /* JADX WARNING: type inference failed for: r3v29 */
    /* JADX WARNING: type inference failed for: r4v27 */
    /* JADX WARNING: type inference failed for: r3v30 */
    /* JADX WARNING: type inference failed for: r17v3 */
    /* JADX WARNING: type inference failed for: r4v28 */
    /* JADX WARNING: type inference failed for: r4v29 */
    /* JADX WARNING: type inference failed for: r3v31 */
    /* JADX WARNING: type inference failed for: r4v30 */
    /* JADX WARNING: type inference failed for: r6v10 */
    /* JADX WARNING: type inference failed for: r4v31 */
    /* JADX WARNING: type inference failed for: r17v4 */
    /* JADX WARNING: type inference failed for: r6v12 */
    /* JADX WARNING: type inference failed for: r4v32 */
    /* JADX WARNING: type inference failed for: r6v13 */
    /* JADX WARNING: type inference failed for: r4v35 */
    /* JADX WARNING: type inference failed for: r3v42 */
    /* JADX WARNING: type inference failed for: r4v37 */
    /* JADX WARNING: type inference failed for: r6v16 */
    /* JADX WARNING: type inference failed for: r4v39 */
    /* JADX WARNING: type inference failed for: r6v17 */
    /* JADX WARNING: type inference failed for: r3v49 */
    /* JADX WARNING: type inference failed for: r4v41 */
    /* JADX WARNING: type inference failed for: r3v63 */
    /* JADX WARNING: type inference failed for: r4v45 */
    /* JADX WARNING: type inference failed for: r6v18 */
    /* JADX WARNING: type inference failed for: r3v64 */
    /* JADX WARNING: type inference failed for: r4v46 */
    /* JADX WARNING: type inference failed for: r3v65 */
    /* JADX WARNING: type inference failed for: r3v66 */
    /* JADX WARNING: type inference failed for: r4v47 */
    /* JADX WARNING: type inference failed for: r3v67 */
    /* JADX WARNING: type inference failed for: r3v68 */
    /* JADX WARNING: type inference failed for: r4v48 */
    /* JADX WARNING: type inference failed for: r4v49 */
    /* JADX WARNING: type inference failed for: r3v69 */
    /* JADX WARNING: type inference failed for: r4v50 */
    /* JADX WARNING: type inference failed for: r4v51 */
    /* JADX WARNING: type inference failed for: r4v52 */
    /* JADX WARNING: type inference failed for: r3v70 */
    /* JADX WARNING: type inference failed for: r4v53 */
    /* JADX WARNING: type inference failed for: r4v54 */
    /* JADX WARNING: type inference failed for: r6v19 */
    /* JADX WARNING: type inference failed for: r4v55 */
    /* JADX WARNING: type inference failed for: r6v20 */
    /* JADX WARNING: type inference failed for: r4v56 */
    /* JADX WARNING: type inference failed for: r3v71 */
    /* JADX WARNING: type inference failed for: r4v57 */
    /* JADX WARNING: type inference failed for: r6v21 */
    /* JADX WARNING: type inference failed for: r4v58 */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x018a, code lost:
        if (r7.isBeautyPanelShow() != false) goto L_0x014f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:0x0192, code lost:
        if (r8.isShowing() != false) goto L_0x014f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x01cb, code lost:
        if (r2 != false) goto L_0x0129;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:159:0x0251, code lost:
        r17 = r6;
        r6 = r3;
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x0260, code lost:
        r17 = r4;
        r4 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x0263, code lost:
        r3 = r17;
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x027e, code lost:
        if (com.android.camera.data.DataRepository.dataItemRunning().getComponentRunningKaleidoscope().isSwitchOn() != false) goto L_0x02c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x02ae, code lost:
        if (com.android.camera.data.DataRepository.dataItemRunning().getComponentRunningLighting().getComponentValue(171) != "0") goto L_0x02c2;
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v6
  assigns: []
  uses: []
  mth insns count: 316
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x01d4  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x02e6  */
    /* JADX WARNING: Removed duplicated region for block: B:187:0x02eb  */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x02f6  */
    /* JADX WARNING: Removed duplicated region for block: B:191:0x02fb  */
    /* JADX WARNING: Removed duplicated region for block: B:194:0x0303  */
    /* JADX WARNING: Removed duplicated region for block: B:197:0x0319  */
    /* JADX WARNING: Removed duplicated region for block: B:201:0x032a  */
    /* JADX WARNING: Unknown variable types count: 29 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateLeftTipImage(int i, int i2, int i3, List list) {
        int i4;
        ? r4;
        ? r3;
        boolean z;
        ? r6;
        ? r42;
        ? r32;
        ? r33;
        ? r43;
        ? r34;
        ? r62;
        ? r44;
        boolean z2;
        ? r63;
        boolean z3;
        int i5 = i2;
        int currentCameraId = DataRepository.dataItemGlobal().getCurrentCameraId();
        boolean isNormalIntent = DataRepository.dataItemGlobal().isNormalIntent();
        boolean z4 = true;
        boolean z5 = 0;
        boolean z6 = C0122O00000o.instance().isSupportUltraWide() && !HybridZoomingSystem.IS_3_OR_MORE_SAT;
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        MasterFilterProtocol masterFilterProtocol = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
        int i6 = 21;
        if (i5 != 165) {
            if (i5 == 169) {
                if (currentCameraId != 0 || !isNormalIntent || !z6) {
                    i6 = (!isNormalIntent || (!C0122O00000o.instance().OOO00O0() && !C0122O00000o.instance().OOO00Oo())) ? -1 : 25;
                } else {
                    if (!CameraSettings.isAutoZoomEnabled(i2)) {
                        if (this.mIsShowLeftImageIntro) {
                            startLeftImageIntroAnim(i);
                            return;
                        }
                    }
                    i4 = -1;
                    ? r35 = 2131755046;
                    if (i4 != -1) {
                    }
                    if (r3 > 0) {
                    }
                    ((ColorImageView) this.mLeftTipImage).setColor(z5 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                    if (r4 > 0) {
                    }
                    updateImageBgColor(i5, this.mLeftTipImage);
                    if (this.mLeftTipImage.getTag() != null) {
                    }
                    if (z4) {
                    }
                    this.mLeftTipImage.setTag(Integer.valueOf(i4));
                    applyAnimType(list, i3, z4, this.mLeftTipImage);
                }
                if (miBeautyProtocol != null) {
                }
                if (masterFilterProtocol != null) {
                }
                i4 = i6;
                ? r352 = 2131755046;
                if (i4 != -1) {
                }
                if (r3 > 0) {
                }
                ((ColorImageView) this.mLeftTipImage).setColor(z5 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                if (r4 > 0) {
                }
                updateImageBgColor(i5, this.mLeftTipImage);
                if (this.mLeftTipImage.getTag() != null) {
                }
                if (z4) {
                }
                this.mLeftTipImage.setTag(Integer.valueOf(i4));
                applyAnimType(list, i3, z4, this.mLeftTipImage);
            } else if (i5 != 171) {
                if (i5 == 188) {
                    i4 = 32;
                } else if (i5 == 205) {
                    checkAIWatermarkTip();
                    i4 = 23;
                } else if (i5 == 173) {
                    if (isNormalIntent && currentCameraId == 1 && C0122O00000o.instance().OOoOOOo()) {
                        i4 = 3;
                    }
                    i4 = -1;
                } else if (i5 == 174) {
                    if (z6 && currentCameraId == 0) {
                        if (this.mIsShowLeftImageIntro) {
                            startLeftImageIntroAnim(i);
                            return;
                        }
                        i4 = i6;
                    }
                    i4 = -1;
                } else if (i5 == 183) {
                    i4 = 33;
                } else if (i5 != 184) {
                    switch (i5) {
                        case 161:
                            if (z6 && currentCameraId == 0) {
                                if (this.mIsShowLeftImageIntro) {
                                    startLeftImageIntroAnim(i);
                                    return;
                                }
                            }
                            break;
                        case 162:
                            if (!CameraSettings.isMacroModeEnabled(i2) && ((miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow()) && (masterFilterProtocol == null || !masterFilterProtocol.isShowing()))) {
                                if (currentCameraId != 0 || !isNormalIntent || !z6) {
                                    i6 = -1;
                                } else if (!CameraSettings.isAutoZoomEnabled(i2) && !CameraSettings.isSuperEISEnabled(i2)) {
                                    if (this.mIsShowLeftImageIntro) {
                                        startLeftImageIntroAnim(i);
                                        return;
                                    }
                                }
                                ComponentRunningShine componentRunningShine = DataRepository.dataItemRunning().getComponentRunningShine();
                                boolean z7 = !componentRunningShine.isTopBeautyEntry() && CameraSettings.isFaceBeautyOn(i5, null);
                                if (z7 && !componentRunningShine.isTargetShow() && (miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow())) {
                                    i4 = 24;
                                    break;
                                }
                            }
                            break;
                        case 163:
                            break;
                    }
                } else {
                    MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
                    if (mimojiStatusManager2.getMimojiPanelState() == 0 && !CameraSettings.isGifOn() && !mimojiStatusManager2.isInMimojiCreate()) {
                        i4 = 34;
                    }
                    i4 = -1;
                }
                ? r3522 = 2131755046;
                if (i4 != -1) {
                    if (i4 != 3) {
                        if (i4 == 18) {
                            if (!"".equals(CameraSettings.getCurrentLiveSticker())) {
                                r32 = R.drawable.ic_vector_live_sticker;
                            } else {
                                r32 = R.drawable.ic_vector_live_sticker;
                                r4 = 0;
                                r3 = r32;
                            }
                        } else if (i4 != 19) {
                            switch (i4) {
                                case 21:
                                    boolean isUltraWideConfigOpen = CameraSettings.isUltraWideConfigOpen(this.mCurrentMode);
                                    r43 = isUltraWideConfigOpen ? 2131231907 : 2131231906;
                                    if (!isUltraWideConfigOpen) {
                                        r43 = r43;
                                        r34 = 2131755200;
                                        break;
                                    } else {
                                        r34 = 2131755201;
                                        break;
                                    }
                                case 22:
                                    r32 = 2131231613;
                                    break;
                                case 23:
                                    r3 = 2131231132;
                                    r4 = 2131755042;
                                    break;
                                case 24:
                                    r34 = r3522;
                                    r43 = 2131231625;
                                    break;
                                case 25:
                                    r3 = 2131231599;
                                    r4 = 2131755096;
                                    break;
                                default:
                                    switch (i4) {
                                        case 32:
                                            z2 = DataRepository.dataItemRunning().getComponentRunningAIWatermark().isSwitchOn();
                                            r62 = 2131231553;
                                            r44 = 2131755191;
                                            break;
                                        case 33:
                                            z2 = !CameraSettings.getCurrentLiveSpeed().equals(String.valueOf(2));
                                            r62 = 2131231619;
                                            r44 = 2131755136;
                                            break;
                                        case 34:
                                            MimojiStatusManager2 mimojiStatusManager22 = DataRepository.dataItemLive().getMimojiStatusManager2();
                                            if (mimojiStatusManager22.getMimojiRecordState() != 0) {
                                                if (mimojiStatusManager22.getCurrentMimojiTimbreInfo() != null) {
                                                    boolean z8 = true;
                                                }
                                                r3 = 2131231633;
                                                r4 = 2131755142;
                                                break;
                                            } else {
                                                if (mimojiStatusManager22.getCurrentMimojiInfo() == null || !CameraSettings.isFrontCamera()) {
                                                    z3 = false;
                                                    z4 = false;
                                                    int i7 = -1;
                                                } else {
                                                    if (mimojiStatusManager22.getCurrentMimojiBgInfo() == null) {
                                                        z3 = true;
                                                        z4 = false;
                                                    } else {
                                                        z3 = true;
                                                    }
                                                    z5 = R.drawable.ic_vector_mimoji_change_bg;
                                                }
                                                z = z4;
                                                boolean z9 = z3;
                                                r6 = r63;
                                                r42 = 2131755138;
                                            }
                                            break;
                                        default:
                                            r3 = 0;
                                            r4 = 0;
                                            break;
                                    }
                            }
                        } else {
                            r3 = 2131231640;
                            r4 = 2131755129;
                        }
                        r4 = 0;
                        r3 = r33;
                        z5 = true;
                        r4 = r4;
                        r3 = r3;
                    } else {
                        z5 = DataRepository.dataItemRunning().getComponentRunningShine().getBottomEntryRes(i5);
                        z = DataRepository.dataItemRunning().getComponentRunningShine().determineStatus(i5);
                        r42 = R.string.accessibility_beauty_function_panel_on;
                        r6 = z5;
                    }
                    r3 = r6;
                    z5 = z;
                    r4 = r42;
                } else {
                    r3 = 0;
                    r4 = 0;
                    z4 = false;
                }
                if (r3 > 0) {
                    this.mLeftTipImage.setImageResource(r3);
                }
                ((ColorImageView) this.mLeftTipImage).setColor(z5 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                if (r4 > 0) {
                    this.mLeftTipImage.setContentDescription(getString(r4));
                }
                updateImageBgColor(i5, this.mLeftTipImage);
                if (this.mLeftTipImage.getTag() != null || ((Integer) this.mLeftTipImage.getTag()).intValue() != i4) {
                    if (z4) {
                        ViewCompat.setRotation(this.mLeftTipImage, i4 == 24 ? 0.0f : (float) this.mDegree);
                    }
                    this.mLeftTipImage.setTag(Integer.valueOf(i4));
                    applyAnimType(list, i3, z4, this.mLeftTipImage);
                }
                return;
            } else {
                if (isNormalIntent && (currentCameraId == 0 ? C0122O00000o.instance().OOoO00o() : !(currentCameraId != 1 || !C0122O00000o.instance().OOoO0()))) {
                    i4 = 19;
                    ? r35222 = 2131755046;
                    if (i4 != -1) {
                    }
                    if (r3 > 0) {
                    }
                    ((ColorImageView) this.mLeftTipImage).setColor(z5 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                    if (r4 > 0) {
                    }
                    updateImageBgColor(i5, this.mLeftTipImage);
                    if (this.mLeftTipImage.getTag() != null) {
                    }
                    if (z4) {
                    }
                    this.mLeftTipImage.setTag(Integer.valueOf(i4));
                    applyAnimType(list, i3, z4, this.mLeftTipImage);
                }
                i4 = -1;
                ? r352222 = 2131755046;
                if (i4 != -1) {
                }
                if (r3 > 0) {
                }
                ((ColorImageView) this.mLeftTipImage).setColor(z5 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                if (r4 > 0) {
                }
                updateImageBgColor(i5, this.mLeftTipImage);
                if (this.mLeftTipImage.getTag() != null) {
                }
                if (z4) {
                }
                this.mLeftTipImage.setTag(Integer.valueOf(i4));
                applyAnimType(list, i3, z4, this.mLeftTipImage);
            }
        }
        if (!CameraSettings.isMacroModeEnabled(i2) && !CameraSettings.isUltraPixelRearOn()) {
            if (!z6 || currentCameraId != 0) {
                int i8 = -1;
            } else if (this.mIsShowLeftImageIntro) {
                startLeftImageIntroAnim(i);
                return;
            }
            boolean aIWatermarkEnable = DataRepository.dataItemRunning().getComponentRunningAIWatermark().getAIWatermarkEnable();
            if (!DataRepository.dataItemRunning().getComponentRunningAIWatermark().needForceDisable(i5)) {
            }
            i4 = i6;
            ? r3522222 = 2131755046;
            if (i4 != -1) {
            }
            if (r3 > 0) {
            }
            ((ColorImageView) this.mLeftTipImage).setColor(z5 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
            if (r4 > 0) {
            }
            updateImageBgColor(i5, this.mLeftTipImage);
            if (this.mLeftTipImage.getTag() != null) {
            }
            if (z4) {
            }
            this.mLeftTipImage.setTag(Integer.valueOf(i4));
            applyAnimType(list, i3, z4, this.mLeftTipImage);
        }
        i4 = -1;
        ? r35222222 = 2131755046;
        if (i4 != -1) {
        }
        if (r3 > 0) {
        }
        ((ColorImageView) this.mLeftTipImage).setColor(z5 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
        if (r4 > 0) {
        }
        updateImageBgColor(i5, this.mLeftTipImage);
        if (this.mLeftTipImage.getTag() != null) {
        }
        if (z4) {
        }
        this.mLeftTipImage.setTag(Integer.valueOf(i4));
        applyAnimType(list, i3, z4, this.mLeftTipImage);
    }

    private void updateRightExtraTipImage(int i, int i2, List list) {
        boolean z;
        boolean z2;
        int i3;
        int i4;
        ImageView imageView = this.mRightTipExtraImage;
        int i5 = (i == 183 && DataRepository.dataItemRunning().supportPopShineEntry() && DataRepository.dataItemLive().getMiLiveSegmentData() != null) ? 3 : -1;
        boolean z3 = true;
        if (i5 == -1) {
            z = false;
            i4 = 0;
            i3 = 0;
            z2 = false;
        } else if (i5 != 3) {
            z2 = true;
            z = false;
            i4 = 0;
            i3 = 0;
        } else {
            i4 = DataRepository.dataItemRunning().getComponentRunningShine().getBottomEntryRes(i);
            i3 = R.string.accessibility_beauty_function_panel_on;
            z = DataRepository.dataItemRunning().getComponentRunningShine().determineStatus(i);
            z2 = true;
        }
        if (imageView.getTag() != null && ((Integer) imageView.getTag()).intValue() == i5) {
            z3 = false;
        }
        if (i4 > 0) {
            ((ColorImageView) imageView).setColor(z ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
            imageView.setImageResource(i4);
        }
        if (i3 > 0) {
            imageView.setContentDescription(getString(i3));
        }
        ViewCompat.setRotation(imageView, z2 ? (float) this.mDegree : 0.0f);
        if (z3) {
            imageView.setTag(Integer.valueOf(i5));
            applyAnimType(list, i2, z2, imageView);
        }
    }

    private void updateRightTipImage(int i, int i2, List list) {
        boolean z;
        int i3;
        int i4;
        ImageView imageView = this.mRightTipImage;
        int i5 = (i == 173 ? !C0122O00000o.instance().OOoOOOo() : i != 183 || !DataRepository.dataItemRunning().supportPopShineEntry() || DataRepository.dataItemLive().getMiLiveSegmentData() == null) ? -1 : 36;
        boolean z2 = true;
        if (i5 == -1) {
            i4 = 0;
            i3 = 0;
            z = false;
        } else if (i5 != 36) {
            z = true;
            i4 = 0;
            i3 = 0;
        } else {
            i4 = R.drawable.ic_vector_live_camera_picker;
            i3 = R.string.accessibility_camera_picker;
            z = true;
        }
        if (imageView.getTag() != null && ((Integer) imageView.getTag()).intValue() == i5) {
            z2 = false;
        }
        if (i4 > 0) {
            imageView.setImageResource(i4);
        }
        if (i3 > 0) {
            imageView.setContentDescription(getString(i3));
        }
        ViewCompat.setRotation(imageView, z ? (float) this.mDegree : 0.0f);
        if (z2) {
            imageView.setTag(Integer.valueOf(i5));
            applyAnimType(list, i2, z, imageView);
        }
    }

    /* JADX WARNING: type inference failed for: r8v0 */
    /* JADX WARNING: type inference failed for: r8v1, types: [int] */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r3v10 */
    /* JADX WARNING: type inference failed for: r8v2 */
    /* JADX WARNING: type inference failed for: r8v4 */
    /* JADX WARNING: type inference failed for: r3v16 */
    /* JADX WARNING: type inference failed for: r8v8 */
    /* JADX WARNING: type inference failed for: r3v17 */
    /* JADX WARNING: type inference failed for: r3v18 */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0050, code lost:
        if (com.android.camera.CameraSettings.isSuperEISEnabled(r12) == false) goto L_0x00a9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0096, code lost:
        if (com.android.camera.data.DataRepository.dataItemLive().getMiLiveSegmentData() == null) goto L_0x00b3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00a6, code lost:
        if (com.android.camera.data.DataRepository.dataItemRunning().supportPopShineEntry() != false) goto L_0x00b3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00b1, code lost:
        if (com.android.camera.data.DataRepository.dataItemRunning().supportPopShineEntry() == false) goto L_0x0143;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00b3, code lost:
        r1 = 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00c6, code lost:
        if (r0.isBeautyPanelShow() == false) goto L_0x00c8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00d0, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOoOOOo() == false) goto L_0x00b3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00db, code lost:
        if (com.android.camera.data.DataRepository.dataItemRunning().supportPopShineEntry() != false) goto L_0x00b3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0103, code lost:
        if (r0.isBeautyPanelShow() != false) goto L_0x0143;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x010b, code lost:
        if (r8.isShowing() != false) goto L_0x0143;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x013f, code lost:
        if (com.android.camera.data.DataRepository.dataItemRunning().supportPopShineEntry() != false) goto L_0x00b3;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x01b1  */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x01b8  */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x01c7  */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x01cc  */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x01f0  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0148  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x01ac  */
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateTipImage(int i, int i2, List list) {
        int i3;
        boolean z;
        ? r8;
        int i4;
        int i5;
        int i6;
        int i7;
        ? r3;
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        if (i != 165) {
            if (i == 169) {
                i3 = (!C0122O00000o.instance().OOO00Oo() || !CameraSettings.isBackCamera()) ? -1 : 37;
                MasterFilterProtocol masterFilterProtocol = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
                if (miBeautyProtocol != null) {
                }
                if (masterFilterProtocol != null) {
                }
                z = true;
                r8 = 0;
                if (i3 == -1) {
                }
                i4 = i5;
                r8 = r8;
                if (i5 > 0) {
                }
                if (r8 > 0) {
                }
                ((ColorImageView) this.mTipImage).setColor(i4 == 0 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                updateImageBgColor(i, this.mTipImage);
                if (this.mTipImage.getTag() != null) {
                }
                if (z) {
                }
                this.mTipImage.setTag(Integer.valueOf(i3));
                applyAnimType(list, i2, z, this.mTipImage);
            } else if (i != 171) {
                if (i != 205) {
                    if (i == 173) {
                        if (DataRepository.dataItemRunning().supportPopShineEntry()) {
                            if (miBeautyProtocol != null) {
                            }
                        }
                        i3 = -1;
                        z = true;
                        r8 = 0;
                        if (i3 == -1) {
                        }
                        i4 = i5;
                        r8 = r8;
                        if (i5 > 0) {
                        }
                        if (r8 > 0) {
                        }
                        ((ColorImageView) this.mTipImage).setColor(i4 == 0 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                        updateImageBgColor(i, this.mTipImage);
                        if (this.mTipImage.getTag() != null) {
                        }
                        if (z) {
                        }
                        this.mTipImage.setTag(Integer.valueOf(i3));
                        applyAnimType(list, i2, z, this.mTipImage);
                    } else if (i != 174) {
                        if (i != 176) {
                            if (i == 177) {
                                updateCenterTipImage(i, i2, list);
                            } else if (i != 183) {
                                if (i != 184) {
                                    switch (i) {
                                        case 162:
                                            if (!CameraSettings.isAutoZoomEnabled(i)) {
                                                if (!CameraSettings.isMacroModeEnabled(i)) {
                                                    break;
                                                }
                                            }
                                            break;
                                        case 161:
                                            break;
                                        case 163:
                                            break;
                                    }
                                } else {
                                    updateCenterTipImage(i, i2, list);
                                    if (DataRepository.dataItemLive().getMimojiStatusManager2().getMimojiPanelState() == 0 && !DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiPreviewPlay() && !DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate()) {
                                        i3 = 34;
                                        z = true;
                                        r8 = 0;
                                        if (i3 == -1) {
                                            if (i3 != 34) {
                                                if (i3 == 37) {
                                                    i5 = R.drawable.ic_vector_fastmotion_pro_adjust_indicator;
                                                } else if (i3 == 2) {
                                                    i5 = R.drawable.ic_beauty_sticker;
                                                } else if (i3 == 3) {
                                                    int bottomEntryRes = DataRepository.dataItemRunning().getComponentRunningShine().getBottomEntryRes(i);
                                                    i4 = DataRepository.dataItemRunning().getComponentRunningShine().determineStatus(i);
                                                    int i8 = bottomEntryRes;
                                                    r8 = R.string.accessibility_beauty_function_panel_on;
                                                    i5 = i8;
                                                    if (i5 > 0) {
                                                        this.mTipImage.setImageResource(i5);
                                                    }
                                                    if (r8 > 0) {
                                                        this.mTipImage.setContentDescription(getString(r8));
                                                    }
                                                    ((ColorImageView) this.mTipImage).setColor(i4 == 0 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                                                    updateImageBgColor(i, this.mTipImage);
                                                    if (this.mTipImage.getTag() != null || ((Integer) this.mTipImage.getTag()).intValue() != i3) {
                                                        if (z) {
                                                            ViewCompat.setRotation(this.mTipImage, i3 == 4 ? 0.0f : (float) this.mDegree);
                                                        }
                                                        this.mTipImage.setTag(Integer.valueOf(i3));
                                                        applyAnimType(list, i2, z, this.mTipImage);
                                                    }
                                                    return;
                                                } else if (i3 != 4) {
                                                    i6 = 0;
                                                } else {
                                                    i7 = R.drawable.id_card_mode;
                                                    r3 = 2131755264;
                                                }
                                                i4 = 0;
                                                if (i5 > 0) {
                                                }
                                                if (r8 > 0) {
                                                }
                                                ((ColorImageView) this.mTipImage).setColor(i4 == 0 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                                                updateImageBgColor(i, this.mTipImage);
                                                if (this.mTipImage.getTag() != null) {
                                                }
                                                if (z) {
                                                }
                                                this.mTipImage.setTag(Integer.valueOf(i3));
                                                applyAnimType(list, i2, z, this.mTipImage);
                                            } else if (this.mCurrentMode == 184) {
                                                i7 = R.drawable.ic_vector_mimoji_change_camera;
                                                r3 = 2131755059;
                                            } else {
                                                i5 = DataRepository.dataItemRunning().getComponentRunningShine().getBottomEntryRes(i);
                                                i4 = DataRepository.dataItemRunning().getComponentRunningShine().determineStatus(i);
                                                if (i5 > 0) {
                                                }
                                                if (r8 > 0) {
                                                }
                                                ((ColorImageView) this.mTipImage).setColor(i4 == 0 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                                                updateImageBgColor(i, this.mTipImage);
                                                if (this.mTipImage.getTag() != null) {
                                                }
                                                if (z) {
                                                }
                                                this.mTipImage.setTag(Integer.valueOf(i3));
                                                applyAnimType(list, i2, z, this.mTipImage);
                                            }
                                            r8 = r3;
                                            i4 = 0;
                                            if (i5 > 0) {
                                            }
                                            if (r8 > 0) {
                                            }
                                            ((ColorImageView) this.mTipImage).setColor(i4 == 0 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                                            updateImageBgColor(i, this.mTipImage);
                                            if (this.mTipImage.getTag() != null) {
                                            }
                                            if (z) {
                                            }
                                            this.mTipImage.setTag(Integer.valueOf(i3));
                                            applyAnimType(list, i2, z, this.mTipImage);
                                        }
                                        z = false;
                                        i6 = 0;
                                        i4 = i5;
                                        r8 = r8;
                                        if (i5 > 0) {
                                        }
                                        if (r8 > 0) {
                                        }
                                        ((ColorImageView) this.mTipImage).setColor(i4 == 0 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                                        updateImageBgColor(i, this.mTipImage);
                                        if (this.mTipImage.getTag() != null) {
                                        }
                                        if (z) {
                                        }
                                        this.mTipImage.setTag(Integer.valueOf(i3));
                                        applyAnimType(list, i2, z, this.mTipImage);
                                    }
                                }
                            } else if (DataRepository.dataItemRunning().supportPopShineEntry()) {
                            }
                            i3 = -1;
                            z = true;
                            r8 = 0;
                            if (i3 == -1) {
                            }
                            i4 = i5;
                            r8 = r8;
                            if (i5 > 0) {
                            }
                            if (r8 > 0) {
                            }
                            ((ColorImageView) this.mTipImage).setColor(i4 == 0 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                            updateImageBgColor(i, this.mTipImage);
                            if (this.mTipImage.getTag() != null) {
                            }
                            if (z) {
                            }
                            this.mTipImage.setTag(Integer.valueOf(i3));
                            applyAnimType(list, i2, z, this.mTipImage);
                        }
                    }
                }
            }
        }
        if (!CameraSettings.isMacroModeEnabled(i) && !CameraSettings.isUltraPixelPortraitFrontOn()) {
            TimerBurstController timerBurstController = DataRepository.dataItemLive().getTimerBurstController();
            if (!timerBurstController.isInTimerBurstShotting() || timerBurstController.isPendingStopTimerBurst()) {
                if (this.mNeedShowIDCardTip) {
                    i3 = 4;
                    z = true;
                    r8 = 0;
                    if (i3 == -1) {
                    }
                    i4 = i5;
                    r8 = r8;
                    if (i5 > 0) {
                    }
                    if (r8 > 0) {
                    }
                    ((ColorImageView) this.mTipImage).setColor(i4 == 0 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
                    updateImageBgColor(i, this.mTipImage);
                    if (this.mTipImage.getTag() != null) {
                    }
                    if (z) {
                    }
                    this.mTipImage.setTag(Integer.valueOf(i3));
                    applyAnimType(list, i2, z, this.mTipImage);
                }
            }
        }
        i3 = -1;
        z = true;
        r8 = 0;
        if (i3 == -1) {
        }
        i4 = i5;
        r8 = r8;
        if (i5 > 0) {
        }
        if (r8 > 0) {
        }
        ((ColorImageView) this.mTipImage).setColor(i4 == 0 ? TintColor.tintColor() : ColorConstant.IMAGE_NORMAL_COLOR);
        updateImageBgColor(i, this.mTipImage);
        if (this.mTipImage.getTag() != null) {
        }
        if (z) {
        }
        this.mTipImage.setTag(Integer.valueOf(i3));
        applyAnimType(list, i2, z, this.mTipImage);
    }

    public /* synthetic */ void O000oO0() {
        if (isAdded()) {
            this.mTipMessage.sendAccessibilityEvent(4);
        }
    }

    public /* synthetic */ void O000oO00() {
        if (isAdded()) {
            this.mQrCodeButton.sendAccessibilityEvent(128);
        }
    }

    public boolean containTips(@StringRes int i) {
        TextView textView = this.mTipMessage;
        return textView != null && textView.getVisibility() == 0 && getString(i).equals(this.mTipMessage.getText().toString());
    }

    public void directHideCenterTipImage() {
        ImageView imageView = this.mCenterTipImage;
        if (imageView != null) {
            imageView.setTag(Integer.valueOf(-1));
            FolmeAlphaOutOnSubscribe.directSetGone(this.mCenterTipImage);
        }
    }

    public void directHideLeftImageIntro() {
        this.mIsShowLeftImageIntro = false;
        AnimatorSet animatorSet = this.mLeftImageIntroAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (this.mLeftImageIntro.getVisibility() == 0) {
            FolmeAlphaOutOnSubscribe.directSetResult(this.mLeftImageIntro);
        }
    }

    public void directHideLyingDirectHint() {
        this.mLyingDirectHint.setVisibility(8);
    }

    public void directHideTipImage() {
        if (this.mTipImage.getVisibility() != 4) {
            this.mTipImage.setTag(Integer.valueOf(-1));
            FolmeAlphaOutOnSubscribe.directSetResult(this.mTipImage);
        }
    }

    public void directShowLeftImageIntro() {
        if (CameraSettings.isShowUltraWideIntro()) {
            this.mIsShowLeftImageIntro = true;
        }
        int i = this.mCurrentMode;
        updateLeftTipImage(0, i, i, null);
    }

    public void directShowOrHideLeftTipImage(boolean z) {
        int i;
        ImageView imageView;
        ImageView imageView2 = this.mLeftTipImage;
        if (imageView2 != null) {
            if (z) {
                updateLeftTipImage();
                imageView = this.mLeftTipImage;
                i = 0;
            } else {
                imageView2.setTag(Integer.valueOf(-1));
                imageView = this.mLeftTipImage;
                i = 4;
            }
            imageView.setVisibility(i);
        }
    }

    public void directlyHideTips() {
        ViewCompat.animate(this.mTipMessage).cancel();
        this.mHandler.removeCallbacksAndMessages(null);
        if (this.mTipMessage.getVisibility() == 0) {
            this.mTipMessage.setVisibility(8);
            CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
            if (cameraModuleSpecial != null) {
                cameraModuleSpecial.showOrHideChip(true);
            }
            if (this.mLastTipType == 6 && !isPortraitHintVisible()) {
                showTips(this.mLastTipType, this.mLastTipMessage, 4);
            }
            this.mLastTipType = 4;
        }
    }

    public void directlyHideTips(@StringRes int i) {
        if (i <= 0 || containTips(i)) {
            directlyHideTips();
        }
    }

    public void directlyHideTipsForce() {
        ViewCompat.animate(this.mTipMessage).cancel();
        this.mHandler.removeCallbacksAndMessages(null);
        this.mLastTipMessage = null;
        this.mTipMessage.setVisibility(8);
        this.mLastTipType = 4;
    }

    public void directlyShowTips(int i, @StringRes int i2) {
        ViewCompat.animate(this.mTipMessage).cancel();
        this.mHandler.removeCallbacksAndMessages(null);
        if (this.mTipMessage.getVisibility() != 0) {
            this.mLastTipType = this.mCurrentTipType;
            this.mLastTipMessage = this.mCurrentTipMessage;
            this.mCurrentTipType = i;
            this.mCurrentTipMessage = getString(i2);
            FolmeAlphaInOnSubscribe.directSetResult(this.mTipMessage);
            this.mTipMessage.setText(i2);
        }
    }

    public int getBottomMargin() {
        return getResources().getDimensionPixelSize(C0122O00000o.instance().OOO0OoO() ? R.dimen.popup_indicator_button_extra_margin_bottom_support_idcard : R.dimen.popup_indicator_button_extra_margin_bottom);
    }

    public String getCurrentBottomTipMsg() {
        return this.mCurrentTipMessage;
    }

    public int getCurrentBottomTipType() {
        return this.mCurrentTipType;
    }

    public int getFragmentInto() {
        return 65529;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_bottom_popup_tips;
    }

    public void hideCenterTipImage() {
        ImageView imageView = this.mCenterTipImage;
        if (imageView != null) {
            imageView.setTag(Integer.valueOf(-1));
            if (this.mCenterTipImage.getVisibility() != 4) {
                FolmeAlphaOutOnSubscribe.directSetResult(this.mCenterTipImage);
                View view = this.mCenterRedDot;
                if (view != null) {
                    view.setVisibility(8);
                }
            }
        }
    }

    public void hideLeftTipImage() {
        ImageView imageView = this.mLeftTipImage;
        Integer valueOf = Integer.valueOf(-1);
        if (imageView != null && imageView.getVisibility() == 0) {
            this.mLeftTipImage.setTag(valueOf);
            FolmeAlphaOutOnSubscribe.directSetResult(this.mLeftTipImage);
        }
        ImageView imageView2 = this.mLeftTipExtraImage;
        if (imageView2 != null && imageView2.getVisibility() == 0) {
            this.mLeftTipExtraImage.setTag(valueOf);
            FolmeAlphaOutOnSubscribe.directSetResult(this.mLeftTipExtraImage);
        }
    }

    public void hideNearRangeTip() {
        if (this.mNearRangeModeButton.getVisibility() != 8) {
            this.mNearRangeModeButton.setVisibility(8);
        }
    }

    public void hideQrCodeTip() {
        if (this.mQrCodeButton.getVisibility() != 8) {
            this.mQrCodeButton.setVisibility(8);
            String tag = getTag();
            StringBuilder sb = new StringBuilder();
            sb.append("  hideQrCodeTip  time  : ");
            sb.append(System.currentTimeMillis());
            Log.w(tag, sb.toString());
        }
    }

    public void hideRightTipImage() {
        ImageView imageView = this.mRightTipImage;
        Integer valueOf = Integer.valueOf(-1);
        if (imageView != null && imageView.getVisibility() == 0) {
            this.mRightTipImage.setTag(valueOf);
            FolmeAlphaOutOnSubscribe.directSetResult(this.mRightTipImage);
        }
        ImageView imageView2 = this.mRightTipExtraImage;
        if (imageView2 != null && imageView2.getVisibility() == 0) {
            this.mRightTipExtraImage.setTag(valueOf);
            FolmeAlphaOutOnSubscribe.directSetResult(this.mRightTipExtraImage);
        }
    }

    public void hideTipImage() {
        ImageView imageView = this.mTipImage;
        if (imageView != null && imageView.getVisibility() != 4) {
            this.mTipImage.setTag(Integer.valueOf(-1));
            FolmeAlphaOutOnSubscribe.directSetResult(this.mTipImage);
        }
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mRootView = view;
        this.mTipImage = (ImageView) view.findViewById(R.id.popup_tip_image);
        this.mLeftTipImage = (ImageView) view.findViewById(R.id.popup_left_tip_image);
        int i = 8388691;
        ((FrameLayout.LayoutParams) this.mLeftTipImage.getLayoutParams()).gravity = 8388691;
        this.mLeftTipImage.setImageResource(R.drawable.ic_new_effect_button_normal);
        this.mLeftTipImage.setOnClickListener(this);
        this.mLeftTipExtraImage = (ImageView) view.findViewById(R.id.popup_left_tip_extra_image);
        this.mLeftTipExtraImage.setImageResource(R.drawable.ic_vector_mimoji_change_bg);
        this.mLeftTipExtraImage.setOnClickListener(this);
        this.mRightTipImage = (ImageView) view.findViewById(R.id.popup_right_tip_image);
        this.mRightTipImage.setOnClickListener(this);
        this.mRightTipExtraImage = (ImageView) view.findViewById(R.id.popup_right_tip_extra_image);
        this.mRightTipExtraImage.setOnClickListener(this);
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.popup_center_tip_layout);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
        if (!HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            i = 81;
        }
        layoutParams.gravity = i;
        this.mCenterTipImage = (ImageView) viewGroup.findViewById(R.id.popup_center_tip_image);
        this.mCenterTipImage.setOnClickListener(this);
        this.mCenterRedDot = viewGroup.findViewById(R.id.popup_center_red_dot);
        this.mLeftImageIntro = (FrameLayout) view.findViewById(R.id.popup_left_tip_intro);
        this.mLeftImageIntro.setOnClickListener(this);
        this.mLeftImageIntroContent = (TextView) view.findViewById(R.id.popup_left_tip_intro_text);
        this.mLeftImageIntroRadius = getResources().getDimensionPixelSize(R.dimen.popup_tip_beauty_intro_radius);
        this.mLeftImageIntroWidth = getLeftImageIntroWide();
        this.mQrCodeButton = (ImageView) view.findViewById(R.id.popup_tips_qrcode);
        this.mNearRangeModeButton = (LinearLayout) view.findViewById(R.id.popup_tips_near_range_mode);
        this.mLyingDirectHint = (TextView) view.findViewById(R.id.bottom_lying_direct_hint_text);
        this.mTipMessage = (TextView) view.findViewById(R.id.popup_tips_message);
        this.mPortraitSuccessHint = view.findViewById(R.id.portrait_success_hint);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.bottomMargin = Display.getBottomHeight();
        marginLayoutParams.setMarginStart(Display.getStartMargin());
        marginLayoutParams.setMarginEnd(Display.getEndMargin());
        this.mTipImage.setOnClickListener(this);
        this.mQrCodeButton.setOnClickListener(this);
        this.mNearRangeModeButton.setOnClickListener(this);
        adjustViewBackground(this.mCurrentMode);
        FolmeUtils.touchTint((View) this.mNearRangeModeButton);
        provideAnimateElement(this.mCurrentMode, null, 2);
        if (((ActivityBase) getContext()).getCameraIntentManager().isFromScreenSlide().booleanValue()) {
            Util.startScreenSlideAlphaInAnimation(this.mTipImage);
        }
        this.mBottomTipHeight = getResources().getDimensionPixelSize(R.dimen.portrait_hint_height);
    }

    public boolean isNearRangeTipShowing() {
        return this.mNearRangeModeButton.getVisibility() == 0;
    }

    public boolean isPortraitHintVisible() {
        return this.mPortraitSuccessHint.getVisibility() == 0;
    }

    public boolean isQRTipVisible() {
        ImageView imageView = this.mQrCodeButton;
        return imageView != null && imageView.getVisibility() == 0;
    }

    public boolean isTipShowing() {
        TextView textView = this.mTipMessage;
        return textView != null && textView.getVisibility() == 0;
    }

    public boolean needViewClear() {
        if (DataRepository.dataItemGlobal().getCurrentMode() == 162) {
            return true;
        }
        return super.needViewClear();
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        reConfigBottomTipOfMimoji();
        if (CameraSettings.isCinematicAspectRatioEnabled(this.mCurrentMode)) {
            updateLeftRightTipImageForCinematic(true);
        }
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
        if (i == 2) {
            directlyHideTips();
        } else if (i == 3) {
            adjustViewBackground(this.mCurrentMode);
        }
        int i3 = this.mCurrentMode;
        updateTipImage(i3, i3, null);
        int i4 = this.mCurrentMode;
        updateLeftTipImage(1, i4, i4, null);
        int i5 = this.mCurrentMode;
        updateLeftTipExtraImage(i5, i5, null);
        int i6 = this.mCurrentMode;
        updateCenterTipImage(i6, i6, null);
    }

    public boolean onBackEvent(int i) {
        if (i != 1) {
            if (i != 2) {
                if (i == 3) {
                    int i2 = this.mCurrentTipType;
                    if (i2 == 6 || i2 == 7 || i2 == 11 || i2 == 9 || i2 == 12 || i2 == 18 || i2 == 10) {
                        return false;
                    }
                }
                hideTip(this.mTipMessage);
                hideTip(this.mPortraitSuccessHint);
                hideTip(this.mQrCodeButton);
                hideTip(this.mNearRangeModeButton);
                this.mHandler.removeCallbacksAndMessages(null);
            }
            return false;
        }
        if (this.mCurrentTipType == 9) {
            return false;
        }
        hideTip(this.mTipMessage);
        hideTip(this.mPortraitSuccessHint);
        hideTip(this.mQrCodeButton);
        hideTip(this.mNearRangeModeButton);
        this.mHandler.removeCallbacksAndMessages(null);
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:71:0x01af, code lost:
        if (r1 != 3) goto L_0x0255;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x01c2, code lost:
        onLeftImageClick(r18);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x01f7, code lost:
        r1 = ((java.lang.Integer) r18.getTag()).intValue();
        com.android.camera.log.Log.u(TAG, java.lang.String.format(java.util.Locale.ENGLISH, "onClick popup_center_tip_image: type=0x%x", new java.lang.Object[]{java.lang.Integer.valueOf(r1)}));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0218, code lost:
        if (r1 == 18) goto L_0x0229;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x021a, code lost:
        if (r1 == 34) goto L_0x021d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x021f, code lost:
        if (r0.mCurrentMode != 184) goto L_0x0225;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0221, code lost:
        showMimojiPanel(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0225, code lost:
        showOrHideMimojiPanel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0229, code lost:
        hideAllTipImage();
        showLiveSticker();
        r0 = r0.mCenterRedDot;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0231, code lost:
        if (r0 == null) goto L_0x023b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x0233, code lost:
        r0.setVisibility(8);
        com.android.camera.CameraSettings.setTTLiveStickerNeedRedDot(false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x023b, code lost:
        r0 = (com.android.camera.protocol.ModeProtocol.ConfigChanges) com.android.camera.protocol.ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0247, code lost:
        if (r0 == null) goto L_0x0255;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x0249, code lost:
        com.android.camera.log.Log.u(TAG, "NearRangeMode:Exit near range mode");
        r1 = 167;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0252, code lost:
        r0.onConfigChanged(r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        View[] viewArr;
        View view2 = view;
        if (isEnableClick()) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction != null && cameraAction.isDoingAction()) {
                return;
            }
            if (!CameraSettings.isFrontCamera() || !((Camera) getContext()).isScreenSlideOff()) {
                int id = view.getId();
                String str = BeautyAttr.VALUE_BEAUTY_SHOW_BOTTOM_BUTTON;
                switch (id) {
                    case R.id.popup_center_tip_image /*2131296930*/:
                        break;
                    case R.id.popup_left_tip_extra_image /*2131296933*/:
                        int intValue = ((Integer) view.getTag()).intValue();
                        Log.u(TAG, String.format(Locale.ENGLISH, "onClick popup_left_tip_extra_image: type=0x%x", new Object[]{Integer.valueOf(intValue)}));
                        if (intValue != 22) {
                            if (intValue == 34) {
                                showMimojiPanel(2);
                                break;
                            }
                        } else {
                            hideAllTipImage();
                            showKaleidoscope();
                            break;
                        }
                        break;
                    case R.id.popup_left_tip_image /*2131296934*/:
                        break;
                    case R.id.popup_left_tip_intro /*2131296935*/:
                        view2.setTag(Integer.valueOf(21));
                        CameraSettings.setPopupUltraWideIntroClicked();
                        directHideLeftImageIntro();
                        break;
                    case R.id.popup_right_tip_extra_image /*2131296940*/:
                        int intValue2 = ((Integer) view.getTag()).intValue();
                        Log.u(TAG, String.format(Locale.ENGLISH, "onClick popup_right_tip_extra_image: type=0x%x", new Object[]{Integer.valueOf(intValue2)}));
                        break;
                    case R.id.popup_right_tip_image /*2131296941*/:
                        int intValue3 = ((Integer) view.getTag()).intValue();
                        Log.u(TAG, String.format(Locale.ENGLISH, "onClick popup_right_tip_image: type=0x%x", new Object[]{Integer.valueOf(intValue3)}));
                        ModeChangeController modeChangeController = (ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179);
                        if (intValue3 != 3) {
                            if (intValue3 == 36) {
                                int i = this.mCurrentMode;
                                if (i == 183) {
                                    if (cameraAction != null && !cameraAction.isDoingAction() && !cameraAction.isRecording()) {
                                        CameraStatUtils.trackMiLiveClick(MiLive.VALUE_MI_LIVE_CLICK_SWITCH);
                                        if (modeChangeController != null) {
                                            viewArr = new View[]{view2};
                                        }
                                    } else {
                                        return;
                                    }
                                } else if (i == 173 && cameraAction != null && !cameraAction.isDoingAction() && !cameraAction.isRecording() && modeChangeController != null) {
                                    viewArr = new View[]{view2};
                                }
                                modeChangeController.changeCamera(viewArr);
                                break;
                            }
                        }
                        break;
                    case R.id.popup_tip_image /*2131296942*/:
                        int intValue4 = ((Integer) view.getTag()).intValue();
                        Log.u(TAG, String.format(Locale.ENGLISH, "onClick popup_tip_image: type=0x%x", new Object[]{Integer.valueOf(intValue4)}));
                        CameraSettings.setPopupTipBeautyIntroClicked();
                        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                        ModeChangeController modeChangeController2 = (ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179);
                        if (intValue4 != 2) {
                            if (intValue4 != 3) {
                                if (intValue4 != 4) {
                                    if (intValue4 == 34) {
                                        if (this.mCurrentMode == 184 && cameraAction != null && !cameraAction.isDoingAction() && !cameraAction.isRecording()) {
                                            if (DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate()) {
                                                CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_CREATE_SWITCH, BaseEvent.CREATE);
                                            }
                                            if (modeChangeController2 != null) {
                                                modeChangeController2.changeCamera(view2);
                                                break;
                                            }
                                        } else {
                                            return;
                                        }
                                    } else if (intValue4 == 37) {
                                        showFastMotionProPanel();
                                        break;
                                    }
                                } else {
                                    hideAllTipImage();
                                    ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                                    int i2 = 166;
                                    break;
                                }
                            } else {
                                hideAllTipImage();
                                MistatsWrapper.commonKeyTriggerEvent(str, null, null);
                                showBeauty(this.mCurrentMode);
                                break;
                            }
                        } else {
                            hideAllTipImage();
                            break;
                        }
                        break;
                    case R.id.popup_tips_near_range_mode /*2131296944*/:
                        break;
                    case R.id.popup_tips_qrcode /*2131296945*/:
                        Log.u(TAG, "onClick qrcode");
                        hideQrCodeTip();
                        HashMap hashMap = new HashMap();
                        hashMap.put(CaptureAttr.PARAM_ASD_DETECT_TIP, FeatureName.VALUE_QRCODE_DETECTED);
                        MistatsWrapper.mistatEvent(FeatureName.KEY_COMMON_TIPS, hashMap);
                        CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
                        if (cameraModuleSpecial != null) {
                            cameraModuleSpecial.showQRCodeResult();
                            break;
                        }
                        break;
                }
            }
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        if (i2 == 3 || this.mCurrentMode != i) {
            this.mCloseType = 0;
        }
        int i3 = this.mCurrentMode;
        super.provideAnimateElement(i, list, i2);
        if (isInModeChanging() || i2 == 3) {
            this.mIsShowLyingDirectHint = false;
            directHideLyingDirectHint();
        }
        onBackEvent(4);
        updateTipBottomMargin(0, false);
        updateTipImage(i, i3, list);
        updateLeftTipImage(0, i, i3, list);
        updateLeftTipExtraImage(i, i3, list);
        updateRightTipImage(i, i3, list);
        updateRightExtraTipImage(i, i3, list);
        updateCenterTipImage(i, i3, list);
        if (!CameraSettings.isCinematicAspectRatioEnabled(this.mCurrentMode)) {
            updateLeftRightTipImageForCinematic(false);
        }
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        if (i == 240 || i == getFragmentInto()) {
            return null;
        }
        return FragmentAnimationFactory.wrapperAnimation(161);
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        if (this.mTipImage.getVisibility() == 0 && !currentIsIDCardShow()) {
            list.add(this.mTipImage);
        }
        ImageView imageView = this.mLeftTipImage;
        if (!(imageView == null || imageView.getVisibility() != 0 || ((Integer) this.mLeftTipImage.getTag()).intValue() == 24)) {
            list.add(this.mLeftTipImage);
        }
        ImageView imageView2 = this.mCenterTipImage;
        if (imageView2 != null) {
            list.add(imageView2);
        }
        ImageView imageView3 = this.mLeftTipExtraImage;
        if (imageView3 != null) {
            list.add(imageView3);
        }
    }

    public void reConfigBottomTipOfUltraWide() {
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            int i = this.mCurrentMode;
            if (!(163 == i || 165 == i || 162 == i || 169 == i || 174 == i || 161 == i || 183 == i)) {
                return;
            }
        } else {
            int i2 = this.mCurrentMode;
            if (!(163 == i2 || 165 == i2 || 162 == i2)) {
                return;
            }
        }
        boolean isAutoZoomEnabled = CameraSettings.isAutoZoomEnabled(this.mCurrentMode);
        if ((162 != this.mCurrentMode || !isAutoZoomEnabled) && !CameraSettings.isSuperEISEnabled(this.mCurrentMode) && !CameraSettings.isMacroModeEnabled(this.mCurrentMode)) {
            if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                if (CameraSettings.getRetainZoom(this.mCurrentMode) >= 1.0f) {
                    return;
                }
            } else if (!CameraSettings.isUltraWideConfigOpen(this.mCurrentMode)) {
                return;
            }
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            if ((miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow()) && CameraSettings.shouldShowUltraWideStickyTip(this.mCurrentMode) && !isTipShowing()) {
                directlyShowTips(13, R.string.ultra_wide_open_tip);
            }
        }
    }

    public boolean reConfigQrCodeTip() {
        if (this.mCurrentMode == 163) {
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            boolean z = bottomPopupTips != null && bottomPopupTips.isTipShowing() && (TextUtils.equals(this.mCurrentTipMessage, getString(R.string.ultra_wide_recommend_tip_hint)) || TextUtils.equals(this.mCurrentTipMessage, getString(R.string.ultra_wide_recommend_tip_hint_sat)));
            boolean z2 = HybridZoomingSystem.toDecimal(CameraSettings.getRetainZoom(this.mCurrentMode)) == HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR || CameraSettings.isUltraWideConfigOpen(this.mCurrentMode);
            MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
            boolean z3 = makeupProtocol != null && makeupProtocol.isSeekBarVisible();
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            boolean z4 = miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow();
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            int currentAiSceneLevel = topAlert.getCurrentAiSceneLevel();
            DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
            boolean z5 = dualController != null && dualController.isZoomPanelVisible();
            boolean z6 = topAlert.getAlertIsShow() && (currentAiSceneLevel == -1 || currentAiSceneLevel == 23 || currentAiSceneLevel == 24 || currentAiSceneLevel == 35 || currentAiSceneLevel == -35);
            if (CameraSettings.isTiltShiftOn() || CameraSettings.isGroupShotOn() || CameraSettings.isGradienterOn() || z2 || z4 || z3 || z || z6 || z5) {
                hideQrCodeTip();
                return true;
            }
        }
        return false;
    }

    public void reInitTipImage() {
        provideAnimateElement(this.mCurrentMode, null, 2);
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(175, this);
        registerBackStack(modeCoordinator, this);
        boolean z = DataRepository.dataItemGlobal().getBoolean("pref_camera_first_ultra_wide_use_hint_shown_key", true);
        if (CameraSettings.isShowUltraWideIntro() && !z) {
            this.mIsShowLeftImageIntro = true;
        }
    }

    public void setPortraitHintVisible(int i) {
        if (i != 0 && this.mCurrentTipType != 21) {
            this.mLastTipType = i == 0 ? 7 : 4;
            directlyHideTips();
            if (i == 0) {
                reIntTipViewMarginBottom(this.mPortraitSuccessHint, this.mBottomTipHeight);
            }
            this.mPortraitSuccessHint.setVisibility(i);
        }
    }

    public void showFastMotionPanel() {
        hideAllTipImage();
        ComponentRunningFastMotion componentRunningFastMotion = DataRepository.dataItemRunning().getComponentRunningFastMotion();
        FastMotionProtocol fastMotionProtocol = (FastMotionProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(674);
        BottomMenuProtocol bottomMenuProtocol = (BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197);
        if (componentRunningFastMotion.isClosed()) {
            componentRunningFastMotion.setClosed(false);
            bottomMenuProtocol.onSwitchFastMotionAction(componentRunningFastMotion);
            if (fastMotionProtocol != null) {
                fastMotionProtocol.show();
            } else {
                BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                if (baseDelegate != null) {
                    baseDelegate.delegateEvent(34);
                }
            }
        } else {
            componentRunningFastMotion.setClosed(true);
            if (fastMotionProtocol != null) {
                fastMotionProtocol.dismiss(2, 6);
            }
        }
        MistatsWrapper.moduleUIClickEvent("M_fastMotion_", Fastmotion.PARAM_SPEED_DURATION, (Object) "on");
    }

    public void showFastMotionProPanel() {
        hideAllTipImage();
        ComponentRunningFastMotionPro componentRunningFastMotionPro = DataRepository.dataItemRunning().getComponentRunningFastMotionPro();
        FastmotionProAdjust fastmotionProAdjust = (FastmotionProAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(932);
        BottomMenuProtocol bottomMenuProtocol = (BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197);
        if (componentRunningFastMotionPro.isClosed()) {
            componentRunningFastMotionPro.setClosed(false);
            bottomMenuProtocol.onSwitchFastMotionProAction(componentRunningFastMotionPro);
            if (fastmotionProAdjust != null) {
                fastmotionProAdjust.show();
            } else {
                BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                if (baseDelegate != null) {
                    baseDelegate.delegateEvent(36);
                }
            }
        } else {
            componentRunningFastMotionPro.setClosed(true);
            if (fastmotionProAdjust != null) {
                fastmotionProAdjust.dismiss(2, 6);
            }
        }
        MistatsWrapper.moduleUIClickEvent("M_fastMotion_", Fastmotion.PARAM_MANUAL_ADJUST, (Object) "on");
    }

    public void showIDCardTip(boolean z) {
        this.mNeedShowIDCardTip = z;
        if (currentIsIDCardShow() || z) {
            int i = this.mCurrentMode;
            updateTipImage(i, i, null);
        }
    }

    public boolean showMimojiPanel(int i) {
        MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
        if (mimojiStatusManager2.getMimojiPanelState() == i) {
            return false;
        }
        mimojiStatusManager2.setMimojiPanelState(i);
        hideAllTipImage();
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges != null) {
            configChanges.showOrHideMimoji();
        }
        return true;
    }

    public void showNearRangeTip() {
        if (isAdded() && this.mNearRangeModeButton.getVisibility() != 0) {
            directHideLyingDirectHint();
            hideTip(this.mTipMessage);
            hideQrCodeTip();
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mNearRangeModeButton.getLayoutParams();
            int tipBottomMargin = getTipBottomMargin(this.mNearRangeModeButton.getBackground().getIntrinsicHeight());
            if (this.mCurrentMode != 165) {
                tipBottomMargin += getResources().getDimensionPixelSize(R.dimen.btn_bottom_capsule_tip_bottom_margin);
            }
            marginLayoutParams.bottomMargin = tipBottomMargin;
            this.mNearRangeModeButton.setLayoutParams(marginLayoutParams);
            FolmeAlphaInOnSubscribe.directSetResult(this.mNearRangeModeButton);
        }
    }

    public void showOrHideMimojiPanel() {
        DataRepository.dataItemLive().getMimojiStatusManager().setMimojiPannelState(true);
        hideAllTipImage();
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges != null) {
            configChanges.showOrHideMimoji();
        }
    }

    public void showOrHideVideoBeautyPanel() {
        hideAllTipImage();
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        if (miBeautyProtocol != null) {
            miBeautyProtocol.show();
        } else {
            BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
            if (baseDelegate != null) {
                baseDelegate.delegateEvent(2);
            }
        }
        ((BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197)).expandShineBottomMenu(DataRepository.dataItemRunning().getComponentRunningShine());
    }

    public void showQrCodeTip() {
        if (!reConfigQrCodeTip() && this.mQrCodeButton.getVisibility() != 0) {
            FolmeAlphaInOnSubscribe.directSetResult(this.mQrCodeButton);
            this.mQrCodeButton.setContentDescription(getString(R.string.see_qrcode_detals));
            if (Util.isAccessible()) {
                this.mQrCodeButton.postDelayed(new O0000OOo(this), 100);
            }
        }
    }

    public void showTips(int i, @StringRes int i2, int i3) {
        showTips(i, getString(i2), i3);
    }

    public void showTips(final int i, final int i2, final int i3, int i4) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                FragmentBottomPopupTips.this.showTips(i, i2, i3);
            }
        }, (long) i4);
    }

    public void showTips(int i, String str, int i2) {
        int i3;
        if (!isNearRangeTipShowing()) {
            if (!isResumed()) {
                Log.w(getTag(), "current fragment is not resumed");
            } else if (i != 6 || (this.mCurrentMode == 171 && CameraSettings.getCameraId() != 1)) {
                int i4 = this.mCurrentMode;
                if ((i4 != 171 && i4 != 169 && i4 != 162 && i4 != 180 && i4 != 214) || this.mTipMessage.getVisibility() == 8 || this.mCurrentTipType != 21) {
                    if (i != 10 || CameraSettings.getBogusCameraId() != 0) {
                        if (isPortraitSuccessHintVisible()) {
                            hideTip(this.mPortraitSuccessHint);
                        }
                        this.mLastTipType = this.mCurrentTipType;
                        this.mLastTipMessage = this.mCurrentTipMessage;
                        this.mCurrentTipType = i;
                        this.mCurrentTipMessage = str;
                        hideTip(this.mQrCodeButton);
                        directHideLyingDirectHint();
                        reIntTipViewMarginBottom(this.mTipMessage, this.mBottomTipHeight);
                        FolmeAlphaInOnSubscribe.directSetResult(this.mTipMessage);
                        this.mTipMessage.setText(str);
                        this.mTipMessage.setContentDescription(this.mCurrentTipMessage);
                        if (Util.isAccessible()) {
                            this.mTipMessage.postDelayed(new O0000Oo0(this), 3000);
                        }
                        switch (i2) {
                            case 1:
                                i3 = 1000;
                                break;
                            case 2:
                                i3 = 5000;
                                break;
                            case 3:
                                i3 = 15000;
                                break;
                            case 5:
                                i3 = 2000;
                                break;
                            case 6:
                                i3 = 3000;
                                break;
                            case 7:
                                i3 = DurationConstant.DURATION_LANDSCAPE_HINT;
                                break;
                            default:
                                i3 = 0;
                                break;
                        }
                        this.mHandler.removeCallbacksAndMessages(null);
                        if (i3 > 0) {
                            this.mHandler.sendEmptyMessageDelayed(1, (long) i3);
                        }
                        if (this.mCurrentMode == 163) {
                            CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
                            if (cameraModuleSpecial != null) {
                                cameraModuleSpecial.showOrHideChip(false);
                            }
                        }
                    }
                }
            }
        }
    }

    public void showTipsWithOrientation(int i, int i2, int i3, int i4, int i5) {
        if (i5 != 0) {
            if (i5 != 1) {
                if (i5 != 2 || isLandScape()) {
                    return;
                }
            } else if (!isLandScape()) {
                return;
            }
        }
        showTips(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        this.mHandler.removeCallbacksAndMessages(null);
        modeCoordinator.detachProtocol(175, this);
        unRegisterBackStack(modeCoordinator, this);
        this.mIsShowLeftImageIntro = false;
    }

    public void updateLeftRightTipImageForCinematic(boolean z) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mTipImage.getLayoutParams();
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mLeftTipImage.getLayoutParams();
        if (z) {
            if (layoutParams.getMarginEnd() != Util.getCinematicAspectRatioMargin()) {
                layoutParams.setMarginEnd(Util.getCinematicAspectRatioMargin());
                this.mTipImage.setLayoutParams(layoutParams);
            }
            if (layoutParams2.getMarginStart() != Util.getCinematicAspectRatioMargin()) {
                layoutParams2.setMarginStart(Util.getCinematicAspectRatioMargin());
            } else {
                return;
            }
        } else {
            if (layoutParams.getMarginEnd() != 0) {
                layoutParams.setMarginEnd(0);
                this.mTipImage.setLayoutParams(layoutParams);
            }
            if (layoutParams2.getMarginStart() != 0) {
                layoutParams2.setMarginStart(0);
            } else {
                return;
            }
        }
        this.mLeftTipImage.setLayoutParams(layoutParams2);
    }

    public void updateLeftTipImage() {
        int i = this.mCurrentMode;
        updateLeftTipImage(1, i, i, null);
    }

    public void updateLyingDirectHint(boolean z, boolean z2) {
        if (!z2) {
            this.mIsShowLyingDirectHint = z;
        }
        if (this.mIsShowLyingDirectHint) {
            DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
            boolean z3 = true;
            boolean z4 = dualController != null && dualController.isZoomPanelVisible();
            MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
            boolean z5 = makeupProtocol != null && makeupProtocol.isSeekBarVisible();
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            if (miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow()) {
                z3 = false;
            }
            if (!isNearRangeTipShowing() && !isTipShowing() && !z4 && !z5 && !z3 && this.mLyingDirectHint.getVisibility() != 0) {
                this.mLyingDirectHint.setRotation(180.0f);
                reIntTipViewMarginBottom(this.mLyingDirectHint, this.mBottomTipHeight);
                this.mLyingDirectHint.setVisibility(0);
                CameraStatUtils.trackLyingDirectShow(180);
                return;
            }
        } else if (this.mLyingDirectHint.getVisibility() != 0) {
            return;
        }
        this.mLyingDirectHint.setVisibility(8);
    }

    public void updateMimojiBottomTipImage() {
        int i = this.mCurrentMode;
        updateLeftTipImage(1, i, i, null);
        int i2 = this.mCurrentMode;
        updateLeftTipExtraImage(i2, i2, null);
        updateTipImage();
    }

    public void updateTipBottomMargin(int i, boolean z) {
        if (this.mRootView.getPaddingTop() < i) {
            this.mRootView.setPadding(0, (int) (((float) i) * 1.2f), 0, 0);
        }
        if (z) {
            Completable.create(((float) i) < ViewCompat.getTranslationY(this.mRootView) ? new FolmeTranslateYOnSubscribe(this.mRootView, -i) : new FolmeTranslateYOnSubscribe(this.mRootView, -i)).subscribe();
        } else {
            FolmeTranslateYOnSubscribe.directSetResult(this.mRootView, -i);
        }
    }

    public void updateTipImage() {
        int i = this.mCurrentMode;
        updateTipImage(i, i, null);
    }
}
