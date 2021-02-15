package com.android.camera.fragment.top;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.android.camera.ActivityBase;
import com.android.camera.AudioMapMove;
import com.android.camera.AudioMapMove.OnAudioMapPressAnimatorListener;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.VolumeControlPanel;
import com.android.camera.VolumeControlPanel.OnVolumeControlListener;
import com.android.camera.VolumeControlPanel.OnVolumePressAnimatorListener;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.animation.type.AlphaOutOnSubscribe;
import com.android.camera.customization.BGTintTextView;
import com.android.camera.customization.TintColor;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.config.ComponentConfigRatio;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.data.runing.ComponentRunningDocument;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.effect.FilterInfo;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.FragmentMasterFilterDescription;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.fastmotion.FragmentFastMotionDescription;
import com.android.camera.fragment.manually.FragmentAmbilightDescription;
import com.android.camera.fragment.manually.FragmentParameterDescription;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.VideoModule;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.FastmotionProAdjust;
import com.android.camera.protocol.ModeProtocol.LiveAudioChanges;
import com.android.camera.protocol.ModeProtocol.LiveRecordState;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.ManuallyAdjust;
import com.android.camera.protocol.ModeProtocol.ManuallyValueChanged;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.TopAlertEvent;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.Ambilight;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsConstants.ModuleName;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.timerburst.TimerBurstController;
import com.android.camera.ui.AudioZoomIndicator;
import com.android.camera.ui.FastmotionIndicatorView;
import com.android.camera.ui.HistogramView;
import com.android.camera.ui.MutiStateButton;
import com.android.camera.ui.ToggleSwitch;
import com.android.camera.ui.ToggleSwitch.OnCheckedChangeListener;
import com.android.camera.ui.TopAlertSlideSwitchButton;
import com.android.camera.ui.TopAlertSlideSwitchButton.SlideSwitchListener;
import com.android.camera.ui.VideoTagView;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import miui.view.animation.SineEaseOutInterpolator;

public class FragmentTopAlert extends BaseFragment implements OnClickListener, OnVolumeControlListener, OnVolumePressAnimatorListener, OnAudioMapPressAnimatorListener {
    public static final int DESCRIPTION_FILTER = 1;
    public static final int DESCRIPTION_NORMAL = 0;
    public static final int FRAGMENT_INFO = 253;
    public static final long HINT_DELAY_TIME_3S = 3000;
    public static final float RESET_GAIN_VALUE = 50.0f;
    /* access modifiers changed from: private */
    public static final String TAG = "FragmentTopAlert";
    public static final String TIP_480FPS_960FPS_DESC = "960fps_desc";
    public static final String TIP_8K_DESC = "8k_desc";
    public static final String TIP_AI = "ai";
    public static final String TIP_AI_AUDIO = "ai_audio";
    public static final String TIP_AI_ENHANCED_VIDEO = "ai_enhanced_video";
    public static final String TIP_AI_WATERMARK = "ai_watermark";
    public static final String TIP_AUTO_HIBERNATION_DESC = "auto_hibernation_desc";
    public static final String TIP_AUTO_ZOOM = "auto_zoom";
    public static final String TIP_COLOR_ENHANCE = "color_enhance";
    public static final String TIP_COLOR_MODE = "color_mode";
    public static final String TIP_FLASH = "flash";
    public static final String TIP_HAND_GESTURE = "hand_gesture";
    public static final String TIP_HAND_GESTURE_DESC = "hand_gesture_desc";
    public static final String TIP_HDR = "hdr";
    public static final String TIP_LIVE_SHOT = "live_shot";
    public static final String TIP_MACRO = "macro";
    public static final String TIP_METER = "meter";
    public static final String TIP_RECOMMEND_ULTRA_WIDE_DESC = "recommend_ultra_wide_desc";
    public static final String TIP_SPEECH_SHUTTER_DESC = "speech_shutter_desc";
    public static final String TIP_SUBTITLE = "subtitle";
    public static final String TIP_SUPER_EIS = "super_eis";
    public static final String TIP_SUPER_EIS_PRO = "super_eis_pro";
    public static final String TIP_TIMER_BURST = "timer_burst";
    public static final String TIP_ULTRA_PIXEL = "ultra_pixel";
    public static final String TIP_ULTRA_PIXEL_PORTRAIT = "ultra_pixel_portrait";
    public static final String TIP_ULTRA_WIDE_BOKEH = "ultra_wide_bokeh";
    public static final String TIP_UNKNOW = "unknow";
    public static final String TIP_VIDEO_BEAUTIFY = "video_beautify";
    public static final String TIP_VIDEO_BOKEH = "video_bokeh";
    private static int sPendingRecordingTimeState;
    /* access modifiers changed from: private */
    public BGTintTextView mAiAudioBGTip;
    public final Runnable mAiAudioBGTipHideRunnable = new TopAlertRunnable() {
        /* access modifiers changed from: 0000 */
        public void runToSafe() {
            FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
            fragmentTopAlert.removeViewToToastLayout(fragmentTopAlert.mAiAudioBGTip);
        }
    };
    private TextView mAiAudioTip;
    private BGTintTextView mAiEnhancedVideoTip;
    /* access modifiers changed from: private */
    public ToggleSwitch mAiSceneSelectView;
    private Runnable mAlertAiDetectTipHitRunable = new TopAlertRunnable() {
        /* access modifiers changed from: 0000 */
        public void runToSafe() {
            FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
            fragmentTopAlert.removeViewToTipLayout(fragmentTopAlert.mRecommendTip);
        }
    };
    private long mAlertAiSceneSwitchHintTime;
    /* access modifiers changed from: private */
    public AlertDialog mAlertDialog;
    private int mAlertImageType = -1;
    private Runnable mAlertRecommendDescRunable = new AlertRecommendDescRunable();
    private TextView mAlertRecordingText;
    private TextView mAlertRecordingTextDenominator;
    private TextView mAlertRecordingTextNumerator;
    private Runnable mAlertTopHintHideRunnable = new Runnable() {
        public void run() {
            FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
            fragmentTopAlert.removeViewToToastLayout(fragmentTopAlert.mPermanentTip);
        }
    };
    private ObjectAnimator mAnimator1;
    /* access modifiers changed from: private */
    public AudioMapMove mAudioMapMove;
    private AudioZoomIndicator mAudioZoomIndicator = null;
    private AlphaAnimation mBlingAnimation;
    private float mClickRectMoveDistance;
    private LayoutTransition mCustomStubTransition;
    private LayoutTransition mCustomToastTransition;
    private MutiStateButton mDocumentStateButton;
    private LinkedHashMap mDocumentStateMaps;
    /* access modifiers changed from: private */
    public BGTintTextView mDualVideoTip;
    public final Runnable mDualVideoTipHideRunnable = new TopAlertRunnable() {
        /* access modifiers changed from: 0000 */
        public void runToSafe() {
            FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
            fragmentTopAlert.removeViewToToastLayout(fragmentTopAlert.mDualVideoTip);
        }
    };
    /* access modifiers changed from: private */
    public LinearLayout mEndGravityTipLayout;
    private FastmotionIndicatorView mFastmotionIndicatorView;
    private TextView mFastmotionProAlertRecordingText;
    private FastmotionIndicatorView mFastmotionProIndicatorView;
    /* access modifiers changed from: private */
    public LinearLayout mFastmotionProTip;
    private TextView mFastmotionProTipSaveTime;
    private TextView mFastmotionProTipSpeedDesc;
    private TextView mFastmotionProTipTitle;
    /* access modifiers changed from: private */
    public LinearLayout mFastmotionTip;
    public final Runnable mFastmotionTipAnnounceRunnable = new TopAlertRunnable() {
        /* access modifiers changed from: 0000 */
        public void runToSafe() {
            LinearLayout linearLayout;
            if (C0122O00000o.instance().OOO00Oo()) {
                if (Util.isAccessible() && FragmentTopAlert.this.isAdded() && FragmentTopAlert.this.mFastmotionProTip != null && !TextUtils.isEmpty(FragmentTopAlert.this.mFastmotionTipContentDescription)) {
                    linearLayout = FragmentTopAlert.this.mFastmotionProTip;
                } else {
                    return;
                }
            } else if (Util.isAccessible() && FragmentTopAlert.this.isAdded() && FragmentTopAlert.this.mFastmotionTip != null && !TextUtils.isEmpty(FragmentTopAlert.this.mFastmotionTipContentDescription)) {
                linearLayout = FragmentTopAlert.this.mFastmotionTip;
            } else {
                return;
            }
            linearLayout.announceForAccessibility(FragmentTopAlert.this.mFastmotionTipContentDescription);
        }
    };
    /* access modifiers changed from: private */
    public String mFastmotionTipContentDescription;
    private TextView mFastmotionTipDesc;
    private TextView mFastmotionTipTitle;
    public final Runnable mFastmotionTipToResetRunnable = new TopAlertRunnable() {
        /* access modifiers changed from: 0000 */
        public void runToSafe() {
            FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
            fragmentTopAlert.removeViewToTipLayout(fragmentTopAlert.mFastmotionTip);
            FragmentTopAlert fragmentTopAlert2 = FragmentTopAlert.this;
            fragmentTopAlert2.removeViewToTipLayout(fragmentTopAlert2.mFastmotionProTip);
        }
    };
    private FrameLayout mFrameLayoutAudioMap;
    private FrameLayout mFrameLayoutHistogram;
    private FrameLayout mFrameLayoutMicPanel;
    private BGTintTextView mHandGestureTip;
    private Handler mHandler;
    private HistogramView mHistogramView;
    private boolean mIsAlertAnim = true;
    private boolean mIsParameterDescriptionVisibleBeforeRecording;
    private boolean mIsParameterResetVisibleBeforeRecording;
    private boolean mIsVideoRecording;
    private boolean mIsVideoUltraClearTipVisibleBeforeProVideoSimple;
    private TextView mLiveMusiHintText;
    private ImageView mLiveMusicClose;
    /* access modifiers changed from: private */
    public LinearLayout mLiveMusicHintLayout;
    private LinearLayout mLlAlertRecordingTimeView;
    private TextView mLyingDirectHintText;
    /* access modifiers changed from: private */
    public BGTintTextView mMacroModeTip;
    public final Runnable mMacroModeTipHideRunnable = new TopAlertRunnable() {
        /* access modifiers changed from: 0000 */
        public void runToSafe() {
            FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
            fragmentTopAlert.removeViewToToastLayout(fragmentTopAlert.mMacroModeTip);
        }
    };
    private ImageView mManualParameterDescriptionTip;
    private ImageView mManualParameterResetTip;
    private int mMarginRightLog;
    private OnClickListener mOnClickListener = new C0319O0000OoO(this);
    /* access modifiers changed from: private */
    public float mPercentX;
    /* access modifiers changed from: private */
    public float mPercentY;
    /* access modifiers changed from: private */
    public BGTintTextView mPermanentTip;
    private BGTintTextView mProColourTip;
    /* access modifiers changed from: private */
    public LottieAnimationView mProVideoRecordingSimpleView;
    /* access modifiers changed from: private */
    public TextView mRecommendTip;
    public Runnable mRunnable = new Runnable() {
        public void run() {
            if (FragmentTopAlert.this.mAudioMapMove.getVisibility() == 8) {
                FragmentTopAlert.this.mAudioMapMove.setVisibility(0);
                FragmentTopAlert.this.mVolumeControlAnimationView.setVisibility(8);
                FragmentTopAlert.this.mVolumeControlPanel.setVisibility(8);
            }
        }
    };
    /* access modifiers changed from: private */
    public String mShortDurationDescriptionTip;
    /* access modifiers changed from: private */
    public String mShortDurationToastTip;
    private boolean mShow;
    private Runnable mShowAction = new TopAlertRunnable() {
        public void runToSafe() {
            LayoutParams layoutParams = new LayoutParams(-2, FragmentTopAlert.this.getResources().getDimensionPixelOffset(R.dimen.ai_scene_selector_layout_height));
            FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
            fragmentTopAlert.addViewToTipLayout(fragmentTopAlert.mAiSceneSelectView, true, layoutParams, -1);
        }
    };
    private Runnable mShowSlideSwitchLayout = new TopAlertRunnable() {
        public void runToSafe() {
            if (FragmentTopAlert.this.mSlideSwitchButton != null) {
                FragmentTopAlert.this.mSlideSwitchButton.setBackgroundColor(FragmentTopAlert.this.mCurrentMode == 165 ? 872415231 : 855638016);
            }
            LayoutParams layoutParams = new LayoutParams(-2, FragmentTopAlert.this.getResources().getDimensionPixelOffset(R.dimen.tilt_selector_layout_height));
            FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
            fragmentTopAlert.addViewToTipLayout(fragmentTopAlert.mSlideSwitchButton, true, layoutParams, -1);
        }
    };
    /* access modifiers changed from: private */
    public TopAlertSlideSwitchButton mSlideSwitchButton;
    private LinearLayout mStartGravityTipLayout;
    private BGTintTextView mSubtitleTip;
    private BGTintTextView mSuperNightSeTip;
    private boolean mSwitchAnimator = true;
    public final Runnable mTimerBurstRunnable = new TopAlertRunnable() {
        /* access modifiers changed from: 0000 */
        public void runToSafe() {
            FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
            fragmentTopAlert.removeViewToToastLayout(fragmentTopAlert.mTimerBurstTip);
        }
    };
    /* access modifiers changed from: private */
    public BGTintTextView mTimerBurstTip;
    /* access modifiers changed from: private */
    public BGTintTextView mToastSwitchTip;
    private ImageView mToastTipFlash;
    /* access modifiers changed from: private */
    public LinearLayout mToastTopTipLayout;
    private int mTopHintTextResource = 0;
    private LinearLayout mTopTipLayout;
    private VideoTagView mVideoTagView;
    private TextView mVideoUltraClearTip;
    public final Runnable mViewHideRunnable = new TopAlertRunnable() {
        /* access modifiers changed from: 0000 */
        public void runToSafe() {
            FragmentTopAlert.this.mShortDurationToastTip = FragmentTopAlert.TIP_UNKNOW;
            FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
            fragmentTopAlert.removeViewToToastLayout(fragmentTopAlert.mToastSwitchTip);
        }
    };
    /* access modifiers changed from: private */
    public LottieAnimationView mVolumeControlAnimationView;
    /* access modifiers changed from: private */
    public VolumeControlPanel mVolumeControlPanel;
    private float mVolumeControlValue;
    /* access modifiers changed from: private */
    public float mXMovePosition;
    /* access modifiers changed from: private */
    public TextView mZoomTip;
    public final Runnable mZoomTipAnnounceRunnable = new TopAlertRunnable() {
        /* access modifiers changed from: 0000 */
        public void runToSafe() {
            if (Util.isAccessible() && FragmentTopAlert.this.isAdded() && FragmentTopAlert.this.mZoomTip != null && !TextUtils.isEmpty(FragmentTopAlert.this.mZoomTipContentDescription)) {
                FragmentTopAlert.this.mZoomTip.announceForAccessibility(FragmentTopAlert.this.mZoomTipContentDescription);
            }
        }
    };
    /* access modifiers changed from: private */
    public String mZoomTipContentDescription;
    public final Runnable mZoomTipToResetRunnable = new TopAlertRunnable() {
        /* access modifiers changed from: 0000 */
        public void runToSafe() {
            FragmentTopAlert.this.alertZoom(true);
            FragmentTopAlert.this.alertAudioZoomIndicator(true);
        }
    };

    public class AlertRecommendDescRunable extends TopAlertRunnable {
        String tipType = null;

        public AlertRecommendDescRunable() {
            super();
        }

        public /* bridge */ /* synthetic */ void run() {
            super.run();
        }

        /* access modifiers changed from: 0000 */
        public void runToSafe() {
            FragmentTopAlert.this.mShortDurationDescriptionTip = FragmentTopAlert.TIP_UNKNOW;
            FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
            fragmentTopAlert.removeViewToTipLayout(fragmentTopAlert.mRecommendTip);
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                String str = this.tipType;
                if (str != null) {
                    topAlert.setTipsState(str, false);
                }
            }
            TopAlertEvent topAlertEvent = (TopAlertEvent) ModeCoordinatorImpl.getInstance().getAttachProtocol(673);
            if (topAlertEvent != null) {
                topAlertEvent.onRecommendDescDismiss();
            }
        }

        public void setTipType(String str) {
            this.tipType = str;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PermanentToastTip {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ShortDurationDescriptionTip {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ShortDurationToastTip {
    }

    abstract class TopAlertRunnable implements Runnable {
        private TopAlertRunnable() {
        }

        public void run() {
            if (FragmentTopAlert.this.isAdded()) {
                runToSafe();
            }
        }

        public abstract void runToSafe();
    }

    public FragmentTopAlert() {
        String str = TIP_UNKNOW;
        this.mShortDurationToastTip = str;
        this.mShortDurationDescriptionTip = str;
    }

    static /* synthetic */ void O000000o(ToggleSwitch toggleSwitch, boolean z) {
        int i;
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (z) {
            if (configChanges != null) {
                i = 248;
            } else {
                return;
            }
        } else if (configChanges != null) {
            i = 249;
        } else {
            return;
        }
        configChanges.onConfigChanged(i);
    }

    static /* synthetic */ void O00000Oo(ToggleSwitch toggleSwitch, boolean z) {
        int i;
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (z) {
            if (configChanges != null) {
                i = 246;
            } else {
                return;
            }
        } else if (configChanges != null) {
            i = 247;
        } else {
            return;
        }
        configChanges.onConfigChanged(i);
    }

    static /* synthetic */ void O00O00o() {
        Log.u(TAG, "onClick trackManuallyResetDialogCancel");
        CameraStatUtils.trackManuallyResetDialogCancel();
    }

    private void addTextViewShadowStyle(View view) {
        if (!(view instanceof BGTintTextView) && (view instanceof TextView)) {
            ((TextView) view).setTextAppearance(R.style.ShadowStyle);
        }
    }

    private void addViewToTipLayout(View view) {
        addViewToTipLayout(view, true, null, -1);
    }

    private void addViewToTipLayout(View view, Runnable runnable, Runnable runnable2) {
        addViewToTipLayout(view, true, null, runnable, runnable2);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0033 A[SYNTHETIC, Splitter:B:16:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0039 A[Catch:{ Exception -> 0x003f }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0057  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addViewToTipLayout(View view, boolean z, LayoutParams layoutParams, int i) {
        LinearLayout linearLayout;
        LayoutTransition layoutTransition;
        if (view != null) {
            LinearLayout linearLayout2 = this.mTopTipLayout;
            if (linearLayout2 != null && linearLayout2.indexOfChild(view) == -1) {
                if (!z || !this.mIsAlertAnim) {
                    linearLayout = this.mTopTipLayout;
                    layoutTransition = null;
                } else {
                    if (this.mTopTipLayout.getLayoutTransition() != customStubViewTransition()) {
                        linearLayout = this.mTopTipLayout;
                        layoutTransition = customStubViewTransition();
                    }
                    addTextViewShadowStyle(view);
                    if (i >= 0) {
                        try {
                            this.mTopTipLayout.addView(view);
                        } catch (Exception unused) {
                            Log.w(TAG, "The specified child already has a parent. You must call removeView() on the child's parent first");
                        }
                    } else {
                        this.mTopTipLayout.addView(view, i);
                    }
                    if (layoutParams == null) {
                        layoutParams = new LayoutParams(-2, -2);
                    }
                    view.setLayoutParams(layoutParams);
                    if (Util.isAccessible()) {
                        view.setOnClickListener(new C0322O0000o0o(this));
                    }
                    checkChildMargin();
                }
                linearLayout.setLayoutTransition(layoutTransition);
                addTextViewShadowStyle(view);
                if (i >= 0) {
                }
                if (layoutParams == null) {
                }
                view.setLayoutParams(layoutParams);
                if (Util.isAccessible()) {
                }
                checkChildMargin();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0030  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0059  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addViewToTipLayout(View view, boolean z, LayoutParams layoutParams, Runnable runnable, Runnable runnable2) {
        LinearLayout linearLayout;
        LayoutTransition layoutTransition;
        if (view != null) {
            LinearLayout linearLayout2 = this.mTopTipLayout;
            if (linearLayout2 != null && linearLayout2.indexOfChild(view) == -1) {
                if (!z || !this.mIsAlertAnim) {
                    linearLayout = this.mTopTipLayout;
                    layoutTransition = null;
                } else {
                    if (this.mTopTipLayout.getLayoutTransition() != customStubViewTransition()) {
                        linearLayout = this.mTopTipLayout;
                        layoutTransition = customStubViewTransition();
                    }
                    if (runnable != null) {
                        runnable.run();
                    }
                    addTextViewShadowStyle(view);
                    this.mTopTipLayout.addView(view);
                    if (layoutParams == null) {
                        layoutParams = new LayoutParams(-2, -2);
                    }
                    view.setLayoutParams(layoutParams);
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                    if (Util.isAccessible()) {
                        view.setOnClickListener(new C0320O0000Ooo(this));
                    }
                    checkChildMargin();
                }
                linearLayout.setLayoutTransition(layoutTransition);
                if (runnable != null) {
                }
                addTextViewShadowStyle(view);
                try {
                    this.mTopTipLayout.addView(view);
                } catch (IllegalStateException unused) {
                    Log.w(TAG, "The specified child already has a parent. You must call removeView() on the child's parent first");
                }
                if (layoutParams == null) {
                }
                view.setLayoutParams(layoutParams);
                if (runnable2 != null) {
                }
                if (Util.isAccessible()) {
                }
                checkChildMargin();
            }
        }
    }

    private void addViewToToastLayout(View view) {
        addViewToToastLayout(view, true, -1);
    }

    private void addViewToToastLayout(View view, boolean z) {
        addViewToToastLayout(view, z, -1);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x007d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addViewToToastLayout(View view, boolean z, int i) {
        LinearLayout linearLayout;
        LayoutTransition layoutTransition;
        if (view != null) {
            LinearLayout linearLayout2 = this.mToastTopTipLayout;
            if (linearLayout2 != null && linearLayout2.indexOfChild(view) == -1) {
                if (!z || !this.mIsAlertAnim) {
                    setToastTipLayoutParams(false);
                    linearLayout = this.mTopTipLayout;
                    layoutTransition = null;
                } else {
                    if (this.mToastTopTipLayout.getLayoutTransition() == null || this.mToastTopTipLayout.getLayoutTransition() != customToastLayoutTransition()) {
                        setToastTipLayoutParams(true);
                    }
                    if (this.mTopTipLayout.getLayoutTransition() != customStubViewTransition()) {
                        linearLayout = this.mTopTipLayout;
                        layoutTransition = customStubViewTransition();
                    }
                    addTextViewShadowStyle(view);
                    LinearLayout linearLayout3 = this.mToastTopTipLayout;
                    if (i >= 0) {
                        linearLayout3.addView(view);
                    } else {
                        linearLayout3.addView(view, i);
                    }
                    LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                    layoutParams.width = -2;
                    layoutParams.height = -2;
                    view.setLayoutParams(layoutParams);
                    if (Util.isAccessible()) {
                        view.setOnClickListener(new O0000o0(this));
                    }
                    if (this.mToastTopTipLayout.getChildCount() > 0) {
                        this.mToastTopTipLayout.setVisibility(0);
                    }
                    checkChildMargin();
                }
                linearLayout.setLayoutTransition(layoutTransition);
                addTextViewShadowStyle(view);
                LinearLayout linearLayout32 = this.mToastTopTipLayout;
                if (i >= 0) {
                }
                LayoutParams layoutParams2 = (LayoutParams) view.getLayoutParams();
                layoutParams2.width = -2;
                layoutParams2.height = -2;
                view.setLayoutParams(layoutParams2);
                if (Util.isAccessible()) {
                }
                if (this.mToastTopTipLayout.getChildCount() > 0) {
                }
                checkChildMargin();
            }
        }
    }

    private void alertAiSceneSelector(String str, String str2, int i, OnCheckedChangeListener onCheckedChangeListener, boolean z) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            this.mAiSceneSelectView.setTextOnAndOff(str, str2);
        }
        if (i == 0) {
            long currentTimeMillis = 3000 - (System.currentTimeMillis() - this.mAlertAiSceneSwitchHintTime);
            if (CameraSettings.getShaderEffect() == FilterInfo.FILTER_ID_NONE) {
                Handler handler = this.mHandler;
                Runnable runnable = this.mShowAction;
                if (currentTimeMillis < 0) {
                    currentTimeMillis = 0;
                }
                handler.postDelayed(runnable, currentTimeMillis);
            }
        } else {
            this.mTopTipLayout.removeCallbacks(this.mShowAction);
            removeViewToTipLayout(this.mAiSceneSelectView);
        }
        this.mAiSceneSelectView.setOnCheckedChangeListener(onCheckedChangeListener);
        this.mAiSceneSelectView.setChecked(z);
    }

    /* access modifiers changed from: private */
    public void alertAudioZoomIndicator(boolean z) {
        if (C0122O00000o.instance().OO0oO0O()) {
            if (z) {
                removeViewToTipLayout(this.mAudioZoomIndicator);
            } else if (!Util.isWiredHeadsetOn()) {
                BaseModule baseModule = (BaseModule) ((ActivityBase) getActivity()).getCurrentModule();
                if (baseModule != null && baseModule.isAlive()) {
                    VideoModule videoModule = null;
                    if (baseModule instanceof VideoModule) {
                        videoModule = (VideoModule) baseModule;
                    }
                    if (videoModule != null && videoModule.isNeedAlertAudioZoomIndicator()) {
                        if (this.mAudioZoomIndicator == null) {
                            this.mAudioZoomIndicator = new AudioZoomIndicator(getContext());
                        }
                        float minZoomRatio = videoModule.getMinZoomRatio();
                        float maxZoomRatio = videoModule.getMaxZoomRatio();
                        float zoomRatio = videoModule.getZoomRatio();
                        boolean z2 = this.mTopTipLayout.indexOfChild(this.mAudioZoomIndicator) != -1 && this.mAudioZoomIndicator.getVisibility() == 0;
                        if (z2) {
                            this.mAudioZoomIndicator.update(minZoomRatio, maxZoomRatio, zoomRatio);
                        } else {
                            LayoutParams layoutParams = new LayoutParams(-2, -2);
                            layoutParams.gravity = 1;
                            addViewToTipLayout(this.mAudioZoomIndicator, true, layoutParams, -1);
                        }
                    }
                }
            }
        }
    }

    private int alertTintColor() {
        return TintColor.isYellowTintColor() ? TintColor.tintColor() : getResources().getColor(R.color.white);
    }

    /* access modifiers changed from: private */
    public void alertZoom(boolean z) {
        boolean isMacroModeEnabled = CameraSettings.isMacroModeEnabled(this.mCurrentMode);
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null && ((dualController == null || !dualController.isButtonVisible()) && (mainContentProtocol == null || !mainContentProtocol.isZoomAdjustVisible()))) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if ((169 != DataRepository.dataItemGlobal().getCurrentMode() || cameraAction == null || cameraAction.isRecording() || (!C0122O00000o.instance().OOO00O0() && !C0122O00000o.instance().OOO00Oo())) && 204 != this.mCurrentMode) {
                String zoomRatioTipText = getZoomRatioTipText(isMacroModeEnabled);
                if (zoomRatioTipText == null || TextUtils.isEmpty(zoomRatioTipText)) {
                    removeViewToTipLayout(this.mZoomTip);
                } else {
                    this.mZoomTip.setText(zoomRatioTipText);
                    if (z) {
                        ObjectAnimator.ofFloat(this.mZoomTip, "textSize", new float[]{Util.pixelToXxhdp(70.0f), Util.pixelToXxhdp(43.0f)}).setDuration(300).start();
                    } else {
                        this.mZoomTip.setTextSize(Util.pixelToXxhdp(43.0f));
                    }
                    if (this.mTopTipLayout.indexOfChild(this.mZoomTip) == -1 || this.mZoomTip.getVisibility() != 0) {
                        addViewToTipLayout(this.mZoomTip);
                    } else {
                        return;
                    }
                }
                return;
            }
        }
        removeViewToTipLayout(this.mZoomTip);
    }

    private void checkChildMargin() {
        if (!((!C0122O00000o.instance().OO0ooo0() && !C0122O00000o.instance().OO0ooo()) || this.mTopTipLayout == null || this.mDocumentStateButton == null || this.mToastTopTipLayout == null)) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.top_tip_vertical_divider_support_document);
            int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.top_tip_margin_top_support_document);
            boolean z = true;
            boolean z2 = this.mToastTopTipLayout.getChildCount() > 0;
            if (this.mTopTipLayout.indexOfChild(this.mDocumentStateButton) == -1) {
                z = false;
            }
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mTopTipLayout.getLayoutParams();
            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mDocumentStateButton.getLayoutParams();
            if (!z) {
                marginLayoutParams.topMargin = getAlertTopMargin();
            } else if (z2) {
                marginLayoutParams.topMargin = getAlertTopMargin();
                marginLayoutParams2.topMargin = dimensionPixelSize2 - dimensionPixelSize;
            } else {
                marginLayoutParams.topMargin = getAlertTopMarginSupportDocument();
                marginLayoutParams2.topMargin = 0;
            }
        }
    }

    private void checkDependingVisible() {
        if (this.mFrameLayoutHistogram.getVisibility() != 0 && this.mFrameLayoutAudioMap.getVisibility() == 0) {
            this.mFrameLayoutHistogram.setVisibility(8);
        }
    }

    private void clearHandlerCallbacks() {
        this.mHandler.removeCallbacks(this.mAlertRecommendDescRunable);
    }

    private LayoutTransition customStubViewTransition() {
        if (this.mCustomStubTransition == null) {
            this.mCustomStubTransition = new LayoutTransition();
            String str = "alpha";
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(null, str, new float[]{0.0f, 1.0f});
            ofFloat.setInterpolator(new SineEaseOutInterpolator());
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(null, str, new float[]{1.0f, 0.0f});
            ofFloat2.setInterpolator(new SineEaseOutInterpolator());
            this.mCustomStubTransition.setStartDelay(2, 0);
            this.mCustomStubTransition.setDuration(2, 300);
            this.mCustomStubTransition.setAnimator(2, ofFloat);
            this.mCustomStubTransition.setStartDelay(3, 0);
            this.mCustomStubTransition.setDuration(3, 200);
            this.mCustomStubTransition.setAnimator(3, ofFloat2);
        }
        return this.mCustomStubTransition;
    }

    private LayoutTransition customToastLayoutTransition() {
        if (this.mCustomToastTransition == null) {
            String str = "alpha";
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(null, str, new float[]{0.0f, 1.0f});
            ofFloat.setInterpolator(new SineEaseOutInterpolator());
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(null, str, new float[]{1.0f, 0.0f});
            ofFloat2.setInterpolator(new SineEaseOutInterpolator());
            ofFloat2.addListener(new AnimatorListenerAdapter() {
                public void onAnimationCancel(Animator animator) {
                    if (FragmentTopAlert.this.mToastTopTipLayout.getChildCount() <= 0) {
                        FragmentTopAlert.this.mToastTopTipLayout.setVisibility(8);
                    }
                }

                public void onAnimationEnd(Animator animator) {
                    if (FragmentTopAlert.this.mToastTopTipLayout.getChildCount() <= 0) {
                        FragmentTopAlert.this.mToastTopTipLayout.setVisibility(8);
                    }
                }
            });
            this.mCustomToastTransition = new LayoutTransition();
            this.mCustomToastTransition.setStartDelay(2, 0);
            this.mCustomToastTransition.setDuration(2, 300);
            this.mCustomToastTransition.setAnimator(2, ofFloat);
            this.mCustomToastTransition.setStartDelay(3, 0);
            this.mCustomToastTransition.setDuration(3, 200);
            this.mCustomToastTransition.setAnimator(3, ofFloat2);
        }
        return this.mCustomToastTransition;
    }

    private void forceResetFastmotionProManually() {
        ArrayList arrayList = new ArrayList();
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        arrayList.add(dataItemConfig.getmComponentManuallyWB());
        arrayList.add(dataItemConfig.getManuallyFocus());
        arrayList.add(dataItemConfig.getmComponentManuallyET());
        arrayList.add(dataItemConfig.getmComponentManuallyISO());
        arrayList.add(dataItemConfig.getComponentManuallyEV());
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            ComponentData componentData = (ComponentData) arrayList.get(i);
            if (componentData.isModified(169)) {
                arrayList2.add(componentData);
            }
            componentData.reset(169);
        }
        ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
        if (manuallyValueChanged != null) {
            manuallyValueChanged.resetManuallyParameters(arrayList2);
        }
    }

    private int getAlertStartGravityTipLayoutTopMargin() {
        return Display.getTopHeight() + getResources().getDimensionPixelSize(R.dimen.manual_description_tip_top);
    }

    private int getAlertTopMargin() {
        return Display.getTipsMarginTop();
    }

    private int getAlertTopMarginSupportDocument() {
        return Util.getDisplayRect(DataRepository.dataItemGlobal().getDisplayMode() == 2 ? 1 : 0).top + getResources().getDimensionPixelSize(R.dimen.top_tip_vertical_divider_support_document);
    }

    private Drawable getDividerDrawable() {
        Resources resources;
        int i;
        if (C0122O00000o.instance().OO0ooo0() || C0122O00000o.instance().OO0ooo()) {
            resources = getResources();
            i = R.drawable.top_tip_vertical_divider_support_document;
        } else {
            resources = getResources();
            i = R.drawable.top_tip_vertical_divider;
        }
        return resources.getDrawable(i);
    }

    private Drawable getEndGravityTipDividerDrawable() {
        return getResources().getDrawable(R.drawable.top_tip_end_vertical_divider);
    }

    private int getUiStyle() {
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        boolean z = cameraAction != null && CameraSettings.isSubtitleEnabled(this.mCurrentMode) && cameraAction.isRecording();
        int i = (z || this.mCurrentMode == 179 || isLeftLandscapeMode() || isBothLandscapeMode()) ? 1 : 0;
        return this.mCurrentMode == 204 ? CameraSettings.getDualVideoConfig().ismDrawSelectWindow() ? 0 : 1 : i;
    }

    private float getVolumeControlTranslationRotation() {
        return (isLeftLandScape() || isFlipRotate()) ? this.mClickRectMoveDistance : this.mClickRectMoveDistance;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00f0, code lost:
        if (com.android.camera.HybridZoomingSystem.sDefaultMacroOpticalZoomRatio == 1.0f) goto L_0x00f2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String getZoomRatioTipText(boolean z) {
        float decimal = HybridZoomingSystem.toDecimal(CameraSettings.getRetainZoom(this.mCurrentMode));
        int actualOpenCameraId = Camera2DataContainer.getInstance().getActualOpenCameraId(DataRepository.dataItemGlobal().getCurrentCameraId(), this.mCurrentMode);
        if (C0122O00000o.instance().OO0oo0O() && CameraSettings.isFrontCamera() && ModuleManager.isSupportCropFrontMode()) {
            return null;
        }
        if (decimal == 1.0f) {
            if (actualOpenCameraId == Camera2DataContainer.getInstance().getMainBackCameraId() || actualOpenCameraId == Camera2DataContainer.getInstance().getSATCameraId() || actualOpenCameraId == Camera2DataContainer.getInstance().getBokehCameraId() || actualOpenCameraId == Camera2DataContainer.getInstance().getUltraWideBokehCameraId() || actualOpenCameraId == Camera2DataContainer.getInstance().getFrontCameraId() || actualOpenCameraId == Camera2DataContainer.getInstance().getBokehFrontCameraId() || actualOpenCameraId == Camera2DataContainer.getInstance().getStandaloneMacroCameraId()) {
                return null;
            }
            if (C0122O00000o.instance().OOoOo() && actualOpenCameraId == Camera2DataContainer.getInstance().getVideoSATCameraId()) {
                return null;
            }
            if ((HybridZoomingSystem.IS_2_SAT || (!HybridZoomingSystem.IS_3_OR_MORE_SAT && !CameraSettings.isSupportedOpticalZoom())) && actualOpenCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                return null;
            }
            int i = this.mCurrentMode;
            if (i == 167 || i == 180 || i == 166 || i == 175 || i == 179) {
                return null;
            }
            if (C0122O00000o.instance().OOOOOo0() && this.mCurrentMode == 172) {
                return null;
            }
            if (actualOpenCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                if (z) {
                }
            }
        }
        if (decimal == HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR && ((HybridZoomingSystem.IS_3_OR_MORE_SAT && actualOpenCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId()) || actualOpenCameraId == Camera2DataContainer.getInstance().getMainBackCameraId())) {
            return null;
        }
        if (actualOpenCameraId == Camera2DataContainer.getInstance().getAuxCameraId() && decimal <= HybridZoomingSystem.getTeleMinZoomRatio()) {
            return null;
        }
        if ((actualOpenCameraId == Camera2DataContainer.getInstance().getUltraTeleCameraId() && decimal <= HybridZoomingSystem.getUltraTeleMinZoomRatio()) || DataRepository.dataItemRunning().getComponentRunningAIWatermark().getAIWatermarkEnable(this.mCurrentMode)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(decimal);
        sb.append("X");
        return sb.toString();
    }

    /* access modifiers changed from: private */
    /* renamed from: handleProVideoRecordingSimple */
    public void O0000oO(View view) {
        LottieAnimationView lottieAnimationView;
        int i;
        StringBuilder sb;
        this.mProVideoRecordingSimpleView.cancelAnimation();
        this.mProVideoRecordingSimpleView.setScale(0.37f);
        this.mProVideoRecordingSimpleView.O0000O0o((int) R.raw.pro_video_recording_simple_anim);
        this.mProVideoRecordingSimpleView.setProgress(1.0f);
        this.mProVideoRecordingSimpleView.O0000oO();
        this.mProVideoRecordingSimpleView.O000000o((AnimatorListener) new AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
                FragmentTopAlert.this.mProVideoRecordingSimpleView.setBackgroundResource(R.drawable.ic_vector_pro_video_recording_simple);
                FragmentTopAlert.this.mProVideoRecordingSimpleView.setImageDrawable(null);
            }

            public void onAnimationEnd(Animator animator) {
                FragmentTopAlert.this.mProVideoRecordingSimpleView.setBackgroundResource(R.drawable.ic_vector_pro_video_recording_simple);
                FragmentTopAlert.this.mProVideoRecordingSimpleView.setImageDrawable(null);
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
                FragmentTopAlert.this.mProVideoRecordingSimpleView.setBackground(null);
            }
        });
        boolean proVideoRecordingSimpleRunning = DataRepository.dataItemRunning().getProVideoRecordingSimpleRunning();
        String str = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("handleProVideoRecordingSimple ");
        sb2.append(!proVideoRecordingSimpleRunning);
        Log.u(str, sb2.toString());
        DataRepository.dataItemRunning().setProVideoRecordingSimpleRunning(!proVideoRecordingSimpleRunning);
        ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        String str2 = ",";
        if (!proVideoRecordingSimpleRunning) {
            if (manuallyAdjust != null) {
                manuallyAdjust.forceUpdateManualView(false);
            }
            if (mainContentProtocol != null) {
                mainContentProtocol.setZoomViewVisible(false);
            }
            if (actionProcessing != null) {
                actionProcessing.updatePauseAndCaptureView(false);
            }
            removeViewToTipLayout(this.mZoomTip);
            if (CameraSettings.isProVideoAudioMapOpen(this.mCurrentMode)) {
                animateViews(-1, true, (View) this.mFrameLayoutAudioMap);
            }
            if (CameraSettings.isProVideoHistogramOpen(this.mCurrentMode)) {
                animateViews(-1, true, (View) this.mFrameLayoutHistogram);
            }
            if (this.mVideoUltraClearTip.getVisibility() == 0) {
                this.mIsVideoUltraClearTipVisibleBeforeProVideoSimple = true;
                Completable.create(new AlphaOutOnSubscribe(this.mVideoUltraClearTip).targetGone()).subscribe();
            }
            lottieAnimationView = this.mProVideoRecordingSimpleView;
            sb = new StringBuilder();
            sb.append(getString(R.string.accessibility_pro_video_simple_mode));
            sb.append(str2);
            i = R.string.accessibility_open;
        } else {
            if (manuallyAdjust != null) {
                manuallyAdjust.forceUpdateManualView(true);
            }
            if (mainContentProtocol != null) {
                mainContentProtocol.setZoomViewVisible(true);
            }
            if (actionProcessing != null) {
                actionProcessing.updatePauseAndCaptureView(true);
            }
            if (CameraSettings.isProVideoAudioMapOpen(this.mCurrentMode)) {
                animateViews(1, true, (View) this.mFrameLayoutAudioMap);
            }
            if (CameraSettings.isProVideoHistogramOpen(this.mCurrentMode)) {
                animateViews(1, true, (View) this.mFrameLayoutHistogram);
            }
            checkDependingVisible();
            if (this.mIsVideoUltraClearTipVisibleBeforeProVideoSimple) {
                this.mIsVideoUltraClearTipVisibleBeforeProVideoSimple = false;
                Completable.create(new AlphaInOnSubscribe(this.mVideoUltraClearTip)).subscribe();
            }
            lottieAnimationView = this.mProVideoRecordingSimpleView;
            sb = new StringBuilder();
            sb.append(getString(R.string.accessibility_pro_video_simple_mode));
            sb.append(str2);
            i = R.string.accessibility_closed;
        }
        sb.append(getString(i));
        lottieAnimationView.setContentDescription(sb.toString());
        if (Util.isAccessible() && isAdded()) {
            this.mProVideoRecordingSimpleView.sendAccessibilityEvent(32768);
        }
    }

    private BGTintTextView initAiAudioBGTip() {
        return (BGTintTextView) LayoutInflater.from(getContext()).inflate(R.layout.ai_audio_top_tip_layout, null);
    }

    private BGTintTextView initAiEnhancedVideoTip() {
        return (BGTintTextView) LayoutInflater.from(getContext()).inflate(R.layout.ai_enhanced_video_top_tip_layout, null);
    }

    private void initDocumentStateButton() {
        this.mDocumentStateButton = (MutiStateButton) LayoutInflater.from(getContext()).inflate(R.layout.document_state_button, null);
        this.mDocumentStateMaps = new LinkedHashMap();
        this.mDocumentStateMaps.put("raw", Integer.valueOf(R.string.document_origin));
        this.mDocumentStateMaps.put(ComponentRunningDocument.DOCUMENT_BLACK_WHITE, Integer.valueOf(R.string.document_blackwhite));
        this.mDocumentStateMaps.put(ComponentRunningDocument.DOCUMENT_STRENGTHEN, Integer.valueOf(R.string.document_strengthen));
        this.mDocumentStateButton.initItems(this.mDocumentStateMaps, this.mOnClickListener);
    }

    private BGTintTextView initDualVideoTip() {
        return (BGTintTextView) LayoutInflater.from(getContext()).inflate(R.layout.dual_video_top_tip_layout, null);
    }

    /* access modifiers changed from: private */
    public void initEndGravityTipLayout(int i) {
        float rotation = this.mEndGravityTipLayout.getRotation();
        float f = (float) i;
        this.mEndGravityTipLayout.setRotation(f);
        if (f != rotation) {
            Completable.create(new AlphaInOnSubscribe(this.mEndGravityTipLayout).setDurationTime(300)).subscribe();
        }
        int width = (this.mEndGravityTipLayout.getWidth() - this.mEndGravityTipLayout.getHeight()) / 2;
        if (isLandScape()) {
            if (isLeftLandScape()) {
                this.mEndGravityTipLayout.setGravity(GravityCompat.START);
            } else {
                this.mEndGravityTipLayout.setGravity(GravityCompat.END);
            }
            setViewTopMargin(this.mEndGravityTipLayout, getAlertTopMargin() + width);
            setViewEndMargin(this.mEndGravityTipLayout, getResources().getDimensionPixelOffset(R.dimen.video_ultra_tip_margin_end) - width);
            return;
        }
        if (isFlipRotate()) {
            this.mEndGravityTipLayout.setGravity(GravityCompat.START);
        } else {
            this.mEndGravityTipLayout.setGravity(GravityCompat.END);
        }
        setViewTopMargin(this.mEndGravityTipLayout, getAlertTopMargin());
        setViewEndMargin(this.mEndGravityTipLayout, getResources().getDimensionPixelSize(R.dimen.video_ultra_tip_margin_end));
    }

    private LinearLayout initFastmotionProTip() {
        return (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.fastmotion_pro_top_tip_layout, null);
    }

    private LinearLayout initFastmotionTip() {
        return (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.fastmotion_top_tip_layout, null);
    }

    private BGTintTextView initHandGestureTip() {
        return (BGTintTextView) LayoutInflater.from(getContext()).inflate(R.layout.hand_gesture_top_tip_layout, null);
    }

    private void initHandler() {
        this.mHandler = new Handler();
    }

    private TextView initHorizonDirectTipText() {
        return (TextView) LayoutInflater.from(getContext()).inflate(R.layout.top_tip_lying_direct_hint_layout, null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0083, code lost:
        if (isLeftBothLandScape() != false) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00ad, code lost:
        if (isRightLandScape() != false) goto L_0x0086;
     */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00d2  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00e1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initLandscapeTopTipLayout() {
        int i;
        LinearLayout linearLayout = this.mTopTipLayout;
        if (linearLayout != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
            String componentValue = DataRepository.dataItemConfig().getComponentConfigRatio().getComponentValue(this.mCurrentMode);
            int uiStyle = getUiStyle();
            int i2 = Util.getDisplayRect(uiStyle).top;
            int height = Util.getDisplayRect(uiStyle).height();
            if (componentValue == ComponentConfigRatio.RATIO_1X1) {
                i2 = Display.getTopCoverHeight();
                height = (height - Display.getSquareBottomCoverHeight()) - (Display.getTopCoverHeight() - Display.getTopHeight());
            }
            layoutParams.topMargin = ((height - Display.getWindowWidth()) / 2) + i2;
            int i3 = 0;
            if (CameraSettings.isCinematicAspectRatioEnabled(this.mCurrentMode)) {
                i = (int) ((((double) Util.getDisplayRect(1).width()) - (((double) Util.getDisplayRect(1).height()) / Util.getCinematicAspectRatio())) / 2.0d);
            } else {
                i = 0;
            }
            if (!isBothLandscapeMode()) {
                if (!isLeftLandscapeMode() && !isLeftLandScape() && (!Util.isAccessible() || this.mCurrentMode != 182)) {
                }
                layoutParams.leftMargin = 0;
                i3 = getResources().getDimensionPixelSize(R.dimen.video_ultra_tip_margin_top) + i;
                layoutParams.rightMargin = i3;
                int windowWidth = Display.getWindowWidth();
                layoutParams.height = windowWidth;
                layoutParams.width = windowWidth;
                this.mTopTipLayout.setLayoutParams(layoutParams);
                float f = 90.0f;
                if (!isBothLandscapeMode()) {
                    LinearLayout linearLayout2 = this.mTopTipLayout;
                    if (!isLeftBothLandScape()) {
                        f = 270.0f;
                    }
                    linearLayout2.setRotation(f);
                } else if (isLeftLandscapeMode() || isLeftLandScape() || (Util.isAccessible() && this.mCurrentMode == 182)) {
                    this.mTopTipLayout.setRotation(90.0f);
                } else {
                    this.mTopTipLayout.setRotation((float) this.mDegree);
                }
            }
            layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.video_ultra_tip_margin_top) + i;
            layoutParams.rightMargin = i3;
            int windowWidth2 = Display.getWindowWidth();
            layoutParams.height = windowWidth2;
            layoutParams.width = windowWidth2;
            this.mTopTipLayout.setLayoutParams(layoutParams);
            float f2 = 90.0f;
            if (!isBothLandscapeMode()) {
            }
        }
    }

    private BGTintTextView initMacroModeTip() {
        return (BGTintTextView) LayoutInflater.from(getContext()).inflate(R.layout.macro_mode_top_tip_layout, null);
    }

    private LinearLayout initMusicTipText() {
        return (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.top_tip_music_layout, null);
    }

    private BGTintTextView initPermanentTip() {
        return (BGTintTextView) LayoutInflater.from(getContext()).inflate(R.layout.permanent_top_tip_layout, null);
    }

    private void initPortraitTopTipLayout() {
        LinearLayout linearLayout = this.mTopTipLayout;
        if (linearLayout != null) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) linearLayout.getLayoutParams();
            marginLayoutParams.topMargin = getAlertTopMargin();
            marginLayoutParams.leftMargin = 0;
            marginLayoutParams.rightMargin = 0;
            marginLayoutParams.height = (int) (((float) (Display.getWindowWidth() * 4)) / 3.0f);
            this.mTopTipLayout.setLayoutParams(marginLayoutParams);
            this.mTopTipLayout.setDividerDrawable(getDividerDrawable());
            this.mTopTipLayout.setShowDividers(2);
            this.mTopTipLayout.setRotation(0.0f);
        }
    }

    private BGTintTextView initProColourTip() {
        return (BGTintTextView) LayoutInflater.from(getContext()).inflate(R.layout.procolour_top_tip_layout, null);
    }

    private TextView initRecommendTipText() {
        return (TextView) LayoutInflater.from(getContext()).inflate(R.layout.recommend_top_tip_layout, null);
    }

    private BGTintTextView initSubtitleTip() {
        return (BGTintTextView) LayoutInflater.from(getContext()).inflate(R.layout.subtitle_top_tip_layout, null);
    }

    private BGTintTextView initTimerBurstTip() {
        return (BGTintTextView) LayoutInflater.from(getContext()).inflate(R.layout.timer_burst_top_tip_layout, null);
    }

    private void initToastTipLayout() {
        setToastTipLayoutParams(true);
        this.mToastTopTipLayout.setVisibility(8);
        this.mToastTipFlash = initToastTopTipImage();
        this.mToastSwitchTip = initToastTopTipText();
        this.mSuperNightSeTip = initToastTopTipText();
    }

    private ImageView initToastTopTipImage() {
        return (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.toast_top_tip_image_layout, null);
    }

    private BGTintTextView initToastTopTipText() {
        return (BGTintTextView) LayoutInflater.from(getContext()).inflate(R.layout.toast_top_tip_text_layout, null);
    }

    private TopAlertSlideSwitchButton initTopSlideSwitchButton() {
        return (TopAlertSlideSwitchButton) LayoutInflater.from(getContext()).inflate(R.layout.top_tip_slideswitch_layout, null);
    }

    private ToggleSwitch initTopTipToggleSwitch() {
        return (ToggleSwitch) LayoutInflater.from(getContext()).inflate(R.layout.top_tip_toggleswitch_layout, null);
    }

    private TextView initZoomTipText() {
        return (TextView) LayoutInflater.from(getContext()).inflate(R.layout.zoom_top_tip_layout, null);
    }

    private void quickEnterAutoHibernation() {
        int i = this.mCurrentMode;
        if ((i == 169 || i == 163 || i == 165 || i == 167 || i == 180 || i == 162 || i == 187) && this.mIsVideoRecording) {
            ((Camera) getActivity()).getCurrentModule().quickEnterAutoHibernation();
        }
    }

    /* access modifiers changed from: private */
    public void removeViewToTipLayout(View view) {
        removeViewToTipLayout(view, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x003b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void removeViewToTipLayout(View view, boolean z) {
        LinearLayout linearLayout;
        LayoutTransition layoutTransition;
        if (view != null) {
            LinearLayout linearLayout2 = this.mTopTipLayout;
            if (linearLayout2 != null && linearLayout2.indexOfChild(view) != -1) {
                if (!z || !this.mIsAlertAnim) {
                    linearLayout = this.mTopTipLayout;
                    layoutTransition = null;
                } else {
                    if (this.mTopTipLayout.getLayoutTransition() != customStubViewTransition()) {
                        linearLayout = this.mTopTipLayout;
                        layoutTransition = customStubViewTransition();
                    }
                    this.mTopTipLayout.removeView(view);
                    if (this.mTopTipLayout.getChildCount() <= 0) {
                        this.mTopTipLayout.removeAllViews();
                    }
                    checkChildMargin();
                }
                linearLayout.setLayoutTransition(layoutTransition);
                this.mTopTipLayout.removeView(view);
                if (this.mTopTipLayout.getChildCount() <= 0) {
                }
                checkChildMargin();
            }
        }
    }

    /* access modifiers changed from: private */
    public void removeViewToToastLayout(View view) {
        removeViewToToastLayout(view, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0057  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void removeViewToToastLayout(View view, boolean z) {
        LayoutTransition layoutTransition;
        LinearLayout linearLayout;
        if (view != null) {
            LinearLayout linearLayout2 = this.mToastTopTipLayout;
            if (linearLayout2 != null && linearLayout2.indexOfChild(view) != -1) {
                if (!z || !this.mIsAlertAnim) {
                    setToastTipLayoutParams(false);
                    linearLayout = this.mTopTipLayout;
                    layoutTransition = null;
                } else {
                    if (this.mToastTopTipLayout.getLayoutTransition() == null || this.mToastTopTipLayout.getLayoutTransition() != customToastLayoutTransition()) {
                        setToastTipLayoutParams(true);
                    }
                    if (this.mTopTipLayout.getLayoutTransition() != customStubViewTransition()) {
                        linearLayout = this.mTopTipLayout;
                        layoutTransition = customStubViewTransition();
                    }
                    this.mToastTopTipLayout.removeView(view);
                    if (this.mToastTopTipLayout.getChildCount() <= 0) {
                        this.mToastTopTipLayout.removeAllViews();
                        if (!z || !this.mIsAlertAnim) {
                            this.mToastTopTipLayout.setVisibility(8);
                        }
                    }
                    checkChildMargin();
                }
                linearLayout.setLayoutTransition(layoutTransition);
                this.mToastTopTipLayout.removeView(view);
                if (this.mToastTopTipLayout.getChildCount() <= 0) {
                }
                checkChildMargin();
            }
        }
    }

    public static void setPendingRecordingState(int i) {
        sPendingRecordingTimeState = i;
    }

    private void setToastTipLayoutParams(boolean z) {
        LinearLayout linearLayout;
        LayoutTransition layoutTransition;
        LinearLayout linearLayout2 = this.mToastTopTipLayout;
        if (linearLayout2 != null) {
            linearLayout2.setDividerDrawable(getResources().getDrawable(R.drawable.top_tip_horizontal_divider));
            this.mToastTopTipLayout.setShowDividers(2);
            if (z) {
                linearLayout = this.mToastTopTipLayout;
                layoutTransition = customToastLayoutTransition();
            } else {
                linearLayout = this.mToastTopTipLayout;
                layoutTransition = null;
            }
            linearLayout.setLayoutTransition(layoutTransition);
            this.mToastTopTipLayout.setGravity(17);
        }
    }

    private void setViewEndMargin(View view, int i) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMarginEnd(i);
        view.setLayoutParams(marginLayoutParams);
    }

    private void setViewTopMargin(View view, int i) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.topMargin = i;
        marginLayoutParams.setMarginStart((Display.getWindowWidth() - Display.getTopBarWidth()) / 2);
        marginLayoutParams.setMarginEnd((Display.getWindowWidth() - Display.getTopBarWidth()) / 2);
        view.setLayoutParams(marginLayoutParams);
        ViewCompat.setTranslationY(view, 0.0f);
    }

    private void showCloseConfirm() {
        Log.u(TAG, "showCloseConfirm");
        if (this.mAlertDialog == null) {
            this.mAlertDialog = RotateDialogController.showSystemAlertDialog(getContext(), null, getString(R.string.live_music_close_message), getString(R.string.live_music_close_sure_message), new Runnable() {
                public void run() {
                    Log.u(FragmentTopAlert.TAG, "showCloseConfirm onClick positive");
                    FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
                    fragmentTopAlert.removeViewToTipLayout(fragmentTopAlert.mLiveMusicHintLayout);
                    LiveAudioChanges liveAudioChanges = (LiveAudioChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(211);
                    LiveRecordState liveRecordState = (LiveRecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(245);
                    if (liveAudioChanges != null && liveRecordState != null && !liveRecordState.isRecording() && !liveRecordState.isRecordingPaused()) {
                        liveAudioChanges.clearAudio();
                        ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)).updateConfigItem(245);
                    }
                }
            }, null, null, getString(R.string.live_music_close_cancel_message), O00000o.INSTANCE);
            this.mAlertDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    FragmentTopAlert.this.mAlertDialog = null;
                }
            });
        }
    }

    private void showManualParameterResetDialog() {
        if (this.mAlertDialog == null) {
            this.mAlertDialog = RotateDialogController.showSystemAlertDialog(getContext(), null, getString(ModuleManager.isFastmotionModulePro() ? R.string.confirm_fastmotion_reset_manually_parameter_message : R.string.confirm_reset_manually_parameter_message), getString(R.string.reset_manually_parameter_confirmed), new O0000O0o(this), null, null, getString(17039360), O00000o0.INSTANCE);
            this.mAlertDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    FragmentTopAlert.this.mAlertDialog = null;
                }
            });
        }
    }

    private void updateDocumentState(boolean z) {
        ComponentRunningDocument componentRunningDocument = DataRepository.dataItemRunning().getComponentRunningDocument();
        if (componentRunningDocument != null && this.mDocumentStateButton != null) {
            this.mDocumentStateButton.updateCurrentIndex(componentRunningDocument.getComponentValue(this.mCurrentMode), z);
        }
    }

    private void updateFlashForPhotoRecording(boolean z) {
        if (this.mCurrentMode == 163) {
            LinearLayout linearLayout = this.mTopTipLayout;
            if (linearLayout != null) {
                int i = this.mAlertImageType;
                if (!(i == 2 || i == 5)) {
                    linearLayout.setVisibility(z ? 4 : 0);
                }
            }
        }
    }

    private void updateTopHint(int i, String str, long j) {
        this.mHandler.removeCallbacks(this.mAlertTopHintHideRunnable);
        if (i == 0) {
            this.mPermanentTip.setText(str);
            this.mPermanentTip.setContentDescription(str);
            this.mPermanentTip.setBGColor(alertTintColor());
            addViewToToastLayout(this.mPermanentTip);
            if (j > 0) {
                this.mHandler.postDelayed(this.mAlertTopHintHideRunnable, j);
                return;
            }
            return;
        }
        removeViewToToastLayout(this.mPermanentTip);
    }

    public /* synthetic */ void O000000o(float[] fArr) {
        AudioMapMove audioMapMove = this.mAudioMapMove;
        if (audioMapMove != null) {
            audioMapMove.setValue((float) ((int) fArr[0]), (float) ((int) fArr[1]));
        }
    }

    public /* synthetic */ void O0000o(View view) {
        quickEnterAutoHibernation();
    }

    public /* synthetic */ void O0000o0(View view) {
        if (isAdded()) {
            view.sendAccessibilityEvent(32768);
        }
    }

    public /* synthetic */ void O0000o0O(View view) {
        if (isAdded()) {
            view.sendAccessibilityEvent(32768);
        }
    }

    public /* synthetic */ void O0000o0o(View view) {
        if (isAdded()) {
            view.sendAccessibilityEvent(32768);
        }
    }

    public /* synthetic */ void O0000oO0(View view) {
        showCloseConfirm();
    }

    public /* synthetic */ void O0000oOO(View view) {
        if (isEnableClick()) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction == null || !cameraAction.isDoingAction()) {
                String str = (String) view.getTag();
                if (str != null) {
                    LinkedHashMap linkedHashMap = this.mDocumentStateMaps;
                    if (linkedHashMap != null && linkedHashMap.containsKey(str)) {
                        DataRepository.dataItemRunning().getComponentRunningDocument().setComponentValue(this.mCurrentMode, str);
                        CameraStatUtils.trackDocumentModeChanged(str);
                        updateDocumentState(true);
                    }
                }
                return;
            }
            Log.d(TAG, "cameraAction.isDoingAction return");
        }
    }

    public /* synthetic */ void O00O00o0() {
        if (ModuleManager.isFastmotionModulePro()) {
            FastmotionProAdjust fastmotionProAdjust = (FastmotionProAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(932);
            if (fastmotionProAdjust == null || !fastmotionProAdjust.isShowing()) {
                forceResetFastmotionProManually();
            } else {
                fastmotionProAdjust.resetManually();
            }
        } else {
            ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
            if (manuallyAdjust != null) {
                manuallyAdjust.resetManually();
            }
        }
        if (CameraSettings.isFlashSupportedInManualMode()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.updateConfigItem(193);
            }
        }
        this.mVolumeControlPanel.setGainValueReset(50.0f);
        alertParameterResetTip(false, 8, 0, 0, false);
        Log.u(TAG, "onClick trackManuallyResetDialogOk");
        CameraStatUtils.trackManuallyResetDialogOk();
    }

    public /* synthetic */ void O00O00oO() {
        this.mTopTipLayout.setLayoutTransition(null);
    }

    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        ((FrameLayout) getView()).addView(view, layoutParams);
    }

    public void alertAiAudio(int i, @StringRes int i2, boolean z) {
        if (this.mAiAudioTip.getVisibility() != 8 || i != 8) {
            String string = getString(i2);
            if (i != 0 || !z) {
                this.mAiAudioTip.setVisibility(i);
            } else {
                this.mAiAudioTip.setVisibility(i);
                ViewCompat.setAlpha(this.mAiAudioTip, 0.0f);
                ViewCompat.animate(this.mAiAudioTip).alpha(1.0f).setDuration(320).start();
            }
            if (i == 0) {
                this.mAiAudioTip.setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.top_left_english_tip_size));
                this.mAiAudioTip.setText(string);
                this.mAiAudioTip.setContentDescription(string);
            }
            updateEndGravityTip();
        }
    }

    public void alertAiAudioBGHint(int i, int i2) {
        if (i == 0) {
            this.mAiAudioBGTip.setBGColor(alertTintColor());
            this.mAiAudioBGTip.setText(getString(i2));
            this.mAiAudioBGTip.setContentDescription(getString(i2));
            addViewToToastLayout(this.mAiAudioBGTip);
            this.mHandler.removeCallbacks(this.mAiAudioBGTipHideRunnable);
            this.mHandler.postDelayed(this.mAiAudioBGTipHideRunnable, 3000);
            alertAiAudioMutexToastIfNeed(getContext(), i2);
            return;
        }
        removeViewToToastLayout(this.mAiAudioBGTip);
    }

    public void alertAiAudioMutexToastIfNeed(Context context, int i) {
        int i2;
        if (Util.isWiredHeadsetOn() && i != -1) {
            switch (i) {
                case R.string.pref_camera_rec_type_3d_record /*2131756437*/:
                    i2 = R.string.unsupported_microphones_3d_record_tips;
                    break;
                case R.string.pref_camera_rec_type_audio_zoom /*2131756438*/:
                    i2 = R.string.unsupported_microphones_audio_zoom_tips;
                    break;
                default:
                    i2 = -1;
                    break;
            }
            if (i2 != -1) {
                ToastUtils.showToast(context, i2, true);
            }
        }
    }

    public void alertAiEnhancedVideoHint(int i, int i2) {
        if (i == 0) {
            this.mAiEnhancedVideoTip.setText(getString(i2));
            this.mAiEnhancedVideoTip.setContentDescription(getString(i2));
            this.mAiEnhancedVideoTip.setBGColor(alertTintColor());
            addViewToToastLayout(this.mAiEnhancedVideoTip);
            return;
        }
        removeViewToToastLayout(this.mAiEnhancedVideoTip);
    }

    public void alertAiSceneSelector(int i) {
        alertAiSceneSelector(getResources().getString(R.string.text_ai_scene_selector_text_on), getResources().getString(R.string.text_ai_scene_selector_text_off), i, i == 0 ? C0317O00000oO.INSTANCE : null, false);
    }

    public void alertAudioMap(int i) {
        animateViews(i == 0 ? 1 : -1, false, (View) this.mFrameLayoutAudioMap);
        checkDependingVisible();
        updateEndGravityTip();
    }

    public void alertDocumentTip(@StringRes int i) {
        this.mHandler.removeCallbacks(this.mAlertAiDetectTipHitRunable);
        if (i != -1) {
            this.mRecommendTip.setText(i);
            this.mRecommendTip.setContentDescription(getString(i));
            addViewToTipLayout(this.mRecommendTip);
            return;
        }
        removeViewToTipLayout(this.mRecommendTip);
    }

    public void alertDualVideoHint(int i, int i2, boolean z) {
        if (i == 0) {
            this.mDualVideoTip.setBGColor(alertTintColor());
            this.mDualVideoTip.setText(getString(i2));
            this.mDualVideoTip.setContentDescription(getString(i2));
            addViewToToastLayout(this.mDualVideoTip);
            this.mHandler.removeCallbacks(this.mDualVideoTipHideRunnable);
            if (z) {
                this.mHandler.postDelayed(this.mDualVideoTipHideRunnable, 3000);
                return;
            }
            return;
        }
        removeViewToToastLayout(this.mDualVideoTip);
    }

    public void alertFastmotionIndicator(int i, String str, String str2, String str3, boolean z) {
        if (this.mFastmotionIndicatorView.getVisibility() != 8 || i != 8) {
            if (i != 0 || !z) {
                this.mFastmotionIndicatorView.setVisibility(i);
            } else {
                this.mFastmotionIndicatorView.setVisibility(i);
                ViewCompat.setAlpha(this.mFastmotionIndicatorView, 0.0f);
                ViewCompat.animate(this.mFastmotionIndicatorView).alpha(1.0f).setDuration(320).start();
            }
            if (i == 0) {
                this.mFastmotionIndicatorView.showFastmotion(str, str2, str3, false, true);
            }
            updateEndGravityTip();
        }
    }

    public void alertFastmotionProTip(String str, String str2, String str3, boolean z, boolean z2) {
        removeViewToTipLayout(this.mZoomTip);
        removeFastmotionTipResetRunnable();
        this.mFastmotionProTipTitle.setText(str);
        if (z) {
            this.mFastmotionProIndicatorView.setVisibility(0);
            this.mFastmotionProIndicatorView.showFastmotion(str2, "", str3, z2, false);
            this.mFastmotionProTipSpeedDesc.setVisibility(8);
            this.mFastmotionProTipSaveTime.setVisibility(8);
        } else {
            this.mFastmotionProIndicatorView.setVisibility(8);
            this.mFastmotionProTipSpeedDesc.setVisibility(0);
            this.mFastmotionProTipSaveTime.setVisibility(0);
            this.mFastmotionProTipSpeedDesc.setText(str2);
            this.mFastmotionProTipSaveTime.setText(str3);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        this.mFastmotionTipContentDescription = sb.toString();
        this.mHandler.postDelayed(this.mFastmotionTipAnnounceRunnable, 500);
        this.mHandler.postDelayed(this.mFastmotionTipToResetRunnable, 1000);
        if (this.mTopTipLayout.indexOfChild(this.mFastmotionProTip) == -1 || this.mFastmotionProTip.getVisibility() != 0) {
            addViewToTipLayout(this.mFastmotionProTip);
        }
    }

    public void alertFastmotionTip(String str, String str2) {
        removeViewToTipLayout(this.mZoomTip);
        removeFastmotionTipResetRunnable();
        this.mFastmotionTipTitle.setText(str);
        this.mFastmotionTipDesc.setText(str2);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        this.mFastmotionTipContentDescription = sb.toString();
        this.mHandler.postDelayed(this.mFastmotionTipAnnounceRunnable, 500);
        this.mHandler.postDelayed(this.mFastmotionTipToResetRunnable, 1000);
        if (this.mTopTipLayout.indexOfChild(this.mFastmotionTip) == -1 || this.mFastmotionTip.getVisibility() != 0) {
            addViewToTipLayout(this.mFastmotionTip);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0047 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0048  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void alertFlash(int i, String str) {
        ImageView imageView;
        boolean z;
        int i2;
        ImageView imageView2;
        int i3 = -1;
        if (i == 0) {
            int hashCode = str.hashCode();
            if (hashCode != 49) {
                if (hashCode != 50) {
                    if (hashCode == 53 && str.equals("5")) {
                        z = true;
                        if (z) {
                            i3 = 1;
                        } else if (z) {
                            i3 = 2;
                        } else if (z) {
                            i3 = 5;
                        }
                        if (this.mAlertImageType == i3) {
                            this.mAlertImageType = i3;
                            if (CameraSettings.isFrontCamera() && C0124O00000oO.Oo0o0Oo()) {
                                i3 = 1;
                            }
                            Drawable drawable = getResources().getDrawable(R.drawable.toast_top_tip_bg);
                            DrawableCompat.setTint(drawable, alertTintColor());
                            this.mToastTipFlash.setBackground(drawable);
                            if (Util.isAccessible() && isAdded()) {
                                this.mToastTipFlash.setAccessibilityDelegate(new AccessibilityDelegate() {
                                    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                                        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                                        accessibilityNodeInfo.setClassName(TextView.class.getName());
                                    }
                                });
                            }
                            if (i3 != 1) {
                                imageView = this.mToastTipFlash;
                                if (i3 == 2) {
                                    imageView.setImageResource(R.drawable.ic_alert_flash_torch);
                                    imageView2 = this.mToastTipFlash;
                                    i2 = R.string.accessibility_flash_torch;
                                } else if (i3 == 5) {
                                    imageView.setImageResource(R.drawable.ic_alert_flash_back_soft_light);
                                    imageView2 = this.mToastTipFlash;
                                    i2 = R.string.accessibility_flash_back_soft_light;
                                }
                            } else {
                                this.mToastTipFlash.setImageResource(R.drawable.ic_alert_flash);
                                imageView2 = this.mToastTipFlash;
                                i2 = R.string.accessibility_flash_on;
                            }
                            imageView2.setContentDescription(getString(i2));
                            addViewToToastLayout(this.mToastTipFlash, true, 0);
                        }
                        return;
                    }
                } else if (str.equals("2")) {
                    z = true;
                    if (z) {
                    }
                    if (this.mAlertImageType == i3) {
                    }
                }
            } else if (str.equals("1")) {
                z = false;
                if (z) {
                }
                if (this.mAlertImageType == i3) {
                }
            }
            z = true;
            if (z) {
            }
            if (this.mAlertImageType == i3) {
            }
        } else {
            int i4 = this.mAlertImageType;
            if (i4 == 2 || i4 == 1 || i4 == 5) {
                this.mAlertImageType = -1;
                imageView = this.mToastTipFlash;
            } else {
                return;
            }
        }
        removeViewToToastLayout(imageView);
    }

    public void alertHandGestureHint(int i) {
        String string = getString(i);
        this.mHandGestureTip.setText(string);
        this.mHandGestureTip.setContentDescription(string);
        this.mHandGestureTip.setBGColor(alertTintColor());
        addViewToToastLayout(this.mHandGestureTip);
    }

    public void alertHistogram(int i) {
        animateViews(i == 0 ? 1 : -1, false, (View) this.mFrameLayoutHistogram);
        checkDependingVisible();
        updateEndGravityTip();
    }

    public void alertLightingTip(int i) {
        int parseLightingRes = parseLightingRes(i);
        alertRecommendTipHint(parseLightingRes == -1 ? 8 : 0, parseLightingRes, -1);
    }

    public void alertMacroModeHint(int i, int i2, boolean z) {
        if (i == 0) {
            this.mMacroModeTip.setBGColor(alertTintColor());
            this.mMacroModeTip.setText(getString(i2));
            this.mMacroModeTip.setContentDescription(getString(i2));
            addViewToToastLayout(this.mMacroModeTip);
            this.mHandler.removeCallbacks(this.mMacroModeTipHideRunnable);
            if (z) {
                this.mHandler.postDelayed(this.mMacroModeTipHideRunnable, 3000);
                return;
            }
            return;
        }
        removeViewToToastLayout(this.mMacroModeTip);
    }

    public void alertMimojiFaceDetect(boolean z, int i) {
        if (z) {
            this.mToastSwitchTip.setText(i);
            this.mToastSwitchTip.setContentDescription(getString(i));
            this.mToastSwitchTip.setBGColor(alertTintColor());
            this.mToastSwitchTip.setVisibility(0);
            addViewToToastLayout(this.mToastSwitchTip);
        } else if (this.mToastSwitchTip.getVisibility() != 8) {
            this.mTopTipLayout.removeCallbacks(this.mViewHideRunnable);
            removeViewToToastLayout(this.mToastSwitchTip);
        }
    }

    public void alertMoonSelector(String str, String str2, int i, boolean z) {
        alertAiSceneSelector(str, str2, i, i == 0 ? O0000OOo.INSTANCE : null, z);
    }

    public void alertMusicClose(boolean z) {
        ImageView imageView = this.mLiveMusicClose;
        if (imageView == null) {
            return;
        }
        if (z) {
            imageView.setAlpha(1.0f);
            this.mLiveMusicHintLayout.setClickable(true);
            FolmeUtils.touchTint((View) this.mLiveMusicHintLayout);
            return;
        }
        imageView.setAlpha(0.4f);
        this.mLiveMusicHintLayout.setClickable(false);
        FolmeUtils.clean(this.mLiveMusicHintLayout);
    }

    public void alertMusicTip(int i, String str) {
        if (i != 0 || TextUtils.isEmpty(str)) {
            removeViewToTipLayout(this.mLiveMusicHintLayout);
            return;
        }
        this.mLiveMusiHintText.setText(str);
        addViewToTipLayout(this.mLiveMusicHintLayout);
    }

    public void alertParameterDescriptionTip(int i, int i2, boolean z) {
        ImageView imageView;
        int i3;
        if (!this.mIsVideoRecording) {
            this.mManualParameterDescriptionTip.setVisibility(i);
        }
        this.mManualParameterDescriptionTip.setTag(Integer.valueOf(i2));
        if (i2 == 0 || i2 == 1) {
            this.mManualParameterDescriptionTip.setImageResource(R.drawable.ic_parameter_description_entrance_normal);
            this.mManualParameterDescriptionTip.setBackgroundResource(R.drawable.ic_parameter_description_entrance_normal_shadow);
        }
        if (i == 0) {
            if (i2 == 0) {
                int i4 = this.mCurrentMode;
                if (i4 == 187) {
                    imageView = this.mManualParameterDescriptionTip;
                    i3 = R.string.ambilight_description_title;
                } else if (i4 == 180) {
                    imageView = this.mManualParameterDescriptionTip;
                    i3 = R.string.parameter_description_pro_video_title;
                } else if (i4 == 169) {
                    imageView = this.mManualParameterDescriptionTip;
                    i3 = R.string.camera_fastmotion_title;
                } else {
                    imageView = this.mManualParameterDescriptionTip;
                    i3 = R.string.parameter_description_pro_capture_title;
                }
            } else {
                imageView = this.mManualParameterDescriptionTip;
                i3 = R.string.pref_camera_new_coloreffect_title;
            }
            imageView.setContentDescription(getString(i3));
        }
        if (!(i == 0 && this.mManualParameterDescriptionTip.getVisibility() == 0 && this.mManualParameterDescriptionTip.getAlpha() > 0.0f) && i == 0 && z) {
            ViewCompat.setAlpha(this.mManualParameterDescriptionTip, 0.0f);
            ViewCompat.animate(this.mManualParameterDescriptionTip).alpha(1.0f).setDuration(320).start();
        }
    }

    public void alertParameterResetTip(boolean z, int i, @StringRes int i2, int i3, boolean z2) {
        if (this.mManualParameterResetTip.getVisibility() != i && !this.mIsVideoRecording) {
            if (i != 0 || !z2) {
                this.mManualParameterResetTip.setVisibility(i);
            } else {
                this.mManualParameterResetTip.setVisibility(i);
                ViewCompat.setAlpha(this.mManualParameterResetTip, 0.0f);
                ViewCompat.animate(this.mManualParameterResetTip).alpha(1.0f).setDuration(320).start();
            }
            if (i == 0) {
                this.mManualParameterResetTip.setContentDescription(getString(i2));
                if (!z) {
                    CameraStatUtils.trackManuallyResetShow();
                }
            }
        }
    }

    public void alertProColourHint(int i, int i2) {
        if (i == 0) {
            this.mProColourTip.setText(getString(i2));
            this.mProColourTip.setContentDescription(getString(i2));
            this.mProColourTip.setBGColor(alertTintColor());
            addViewToToastLayout(this.mProColourTip);
            return;
        }
        removeViewToToastLayout(this.mProColourTip);
    }

    public void alertRecommendDescTip(String str, int i, @StringRes int i2, long j) {
        if (i2 > 0) {
            if (i == 0 || str.equals(this.mShortDurationDescriptionTip)) {
                String str2 = TIP_UNKNOW;
                if (i == 0 && !this.mShortDurationDescriptionTip.equals(str2)) {
                    this.mShortDurationDescriptionTip = str2;
                    removeViewToTipLayout(this.mRecommendTip);
                }
                this.mHandler.removeCallbacks(this.mAlertRecommendDescRunable);
                if (i == 0) {
                    this.mShortDurationDescriptionTip = str;
                    this.mRecommendTip.setText(i2);
                    this.mRecommendTip.setContentDescription(getString(i2));
                    addViewToTipLayout(this.mRecommendTip);
                    if (j >= 0) {
                        ((AlertRecommendDescRunable) this.mAlertRecommendDescRunable).setTipType(str);
                        this.mHandler.postDelayed(this.mAlertRecommendDescRunable, j);
                    }
                } else if (getResources().getString(i2).equals(this.mRecommendTip.getText())) {
                    this.mShortDurationDescriptionTip = str2;
                    removeViewToTipLayout(this.mRecommendTip);
                }
            }
        }
    }

    public void alertRecommendTipHint(int i, @StringRes int i2, long j) {
        this.mHandler.removeCallbacks(this.mAlertAiDetectTipHitRunable);
        if (i == 0) {
            this.mRecommendTip.setText(i2);
            this.mRecommendTip.setContentDescription(getString(i2));
            addViewToTipLayout(this.mRecommendTip);
            if (j >= 0) {
                this.mHandler.postDelayed(this.mAlertAiDetectTipHitRunable, j);
                return;
            }
            return;
        }
        removeViewToTipLayout(this.mRecommendTip);
    }

    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v1, types: [com.android.camera.ui.TopAlertSlideSwitchButton$SlideSwitchListener] */
    /* JADX WARNING: type inference failed for: r1v2, types: [com.android.camera.data.data.ComponentData] */
    /* JADX WARNING: type inference failed for: r1v3, types: [com.android.camera.fragment.top.FragmentTopAlert$25] */
    /* JADX WARNING: type inference failed for: r1v4, types: [com.android.camera.data.data.runing.ComponentRunningDocument] */
    /* JADX WARNING: type inference failed for: r1v5, types: [com.android.camera.data.data.runing.ComponentRunningDualVideo] */
    /* JADX WARNING: type inference failed for: r1v6, types: [com.android.camera.data.data.runing.ComponentRunningTiltValue] */
    /* JADX WARNING: type inference failed for: r1v7, types: [com.android.camera.data.data.runing.ComponentRunningMoon] */
    /* JADX WARNING: type inference failed for: r1v8 */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: type inference failed for: r1v10 */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: type inference failed for: r1v12 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v8
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.android.camera.data.data.runing.ComponentRunningDocument, com.android.camera.data.data.runing.ComponentRunningDualVideo, com.android.camera.data.data.runing.ComponentRunningTiltValue, com.android.camera.data.data.runing.ComponentRunningMoon]
  uses: [com.android.camera.ui.TopAlertSlideSwitchButton$SlideSwitchListener, com.android.camera.data.data.ComponentData]
  mth insns count: 38
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
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 6 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void alertSlideSwitchLayout(boolean z, int i) {
        ? r1;
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        ? r12 = 0;
        if (z) {
            boolean z2 = true;
            if (i == 221) {
                r12 = dataItemRunning.getComponentRunningDocument();
            } else if (i == 222) {
                r12 = dataItemRunning.getComponentRunningDualVideo();
            } else if (i == 228) {
                z2 = false;
                r12 = dataItemRunning.getComponentRunningTiltValue();
            } else if (i == 246) {
                r12 = dataItemRunning.getComponentRunningMoon();
            }
            this.mSlideSwitchButton.setType(i);
            this.mSlideSwitchButton.setIndicatorColor(getResources().getColor(R.color.white));
            this.mSlideSwitchButton.setComponentData(r12, i, z2);
            ? r13 = new SlideSwitchListener() {
                public boolean enableSwitch() {
                    CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                    return cameraAction == null || !cameraAction.isDoingAction();
                }

                public void toSlideSwitch(int i, String str) {
                    String access$2600 = FragmentTopAlert.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onSlideSwitch: ");
                    sb.append(i);
                    sb.append(" | ");
                    sb.append(str);
                    Log.k(3, access$2600, sb.toString());
                    ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                    if (configChanges != null) {
                        configChanges.onConfigValueChanged(i, str);
                    }
                }
            };
            this.mHandler.post(this.mShowSlideSwitchLayout);
            r1 = r13;
        } else {
            this.mTopTipLayout.removeCallbacks(this.mShowSlideSwitchLayout);
            removeViewToTipLayout(this.mSlideSwitchButton);
        }
        this.mSlideSwitchButton.setSlideSwitchListener(r1);
    }

    public void alertSubtitleHint(int i, int i2) {
        if (i == 0) {
            this.mSubtitleTip.setText(getString(i2));
            this.mSubtitleTip.setContentDescription(getString(i2));
            this.mSubtitleTip.setBGColor(alertTintColor());
            addViewToToastLayout(this.mSubtitleTip);
            return;
        }
        removeViewToToastLayout(this.mSubtitleTip);
    }

    public void alertSuperNightSeTip(int i) {
        if (i == 0) {
            this.mSuperNightSeTip.setBGColor(alertTintColor());
            this.mSuperNightSeTip.setText(R.string.ai_scene_top_moon_off);
            this.mSuperNightSeTip.setContentDescription(getString(R.string.ai_scene_top_moon_off));
            addViewToToastLayout(this.mSuperNightSeTip);
            return;
        }
        removeViewToToastLayout(this.mSuperNightSeTip);
    }

    public void alertSwitchTip(String str, int i, int i2, String str2, long j) {
        if (i == 0 || str.equals(this.mShortDurationToastTip)) {
            String str3 = TIP_UNKNOW;
            if (i == 0 && !this.mShortDurationToastTip.equals(str3)) {
                this.mShortDurationToastTip = str3;
                removeViewToToastLayout(this.mToastSwitchTip);
            }
            this.mToastSwitchTip.setTag(Integer.valueOf(i2));
            if (str.equals(TIP_HDR)) {
                this.mToastSwitchTip.setContentDescription(getString(R.string.accessibility_hdr_on));
            } else {
                this.mToastSwitchTip.setContentDescription(str2);
            }
            this.mToastSwitchTip.setBGColor(alertTintColor());
            this.mAlertAiSceneSwitchHintTime = System.currentTimeMillis();
            this.mHandler.removeCallbacks(this.mViewHideRunnable);
            if (i != 0) {
                this.mShortDurationToastTip = str3;
                removeViewToToastLayout(this.mToastSwitchTip);
            } else {
                this.mShortDurationToastTip = str;
                this.mToastSwitchTip.setText(str2);
                addViewToToastLayout(this.mToastSwitchTip);
                if (j > 0) {
                    this.mHandler.postDelayed(this.mViewHideRunnable, j);
                }
            }
        }
    }

    public void alertTimerBurstHint(int i, int i2, boolean z) {
        if (i == 0) {
            this.mTimerBurstTip.setBGColor(alertTintColor());
            this.mTimerBurstTip.setText(getString(i2));
            this.mTimerBurstTip.setContentDescription(getString(i2));
            addViewToToastLayout(this.mTimerBurstTip);
            this.mHandler.removeCallbacks(this.mTimerBurstRunnable);
            if (z) {
                this.mHandler.postDelayed(this.mTimerBurstRunnable, 3000);
                return;
            }
            return;
        }
        removeViewToToastLayout(this.mTimerBurstTip);
    }

    public void alertTopHint(int i, @StringRes int i2) {
        alertTopHint(i, i2, 0);
    }

    public void alertTopHint(int i, @StringRes int i2, long j) {
        if (i2 > 0 && i == 0) {
            this.mTopHintTextResource = i2;
        } else if (i == 8) {
            this.mTopHintTextResource = 0;
        }
        String str = null;
        if (this.mTopHintTextResource == 0) {
            i = 8;
        } else {
            str = getString(i2);
        }
        updateTopHint(i, str, j);
    }

    public void alertTopHint(int i, String str) {
        if (TextUtils.isEmpty(str) && i == 0) {
            i = 8;
        }
        updateTopHint(i, str, 0);
    }

    public void alertTopHint(int i, String str, long j) {
        if (TextUtils.isEmpty(str) && i == 0) {
            i = 8;
        }
        updateTopHint(i, str, j);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00ed, code lost:
        r6.append(getString(r7));
        r6.append(r4.mZoomTip.getText());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00fd, code lost:
        r6 = r6.toString();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void alertUpdateValue(int i, @StringRes int i2, String str) {
        String string;
        StringBuilder sb;
        int i3;
        removeZoomTipRestRunnable();
        if (i == 0) {
            alertZoom(false);
            return;
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("alertUpdateValue: type=");
        sb2.append(i);
        sb2.append(", value=");
        sb2.append(str);
        Log.u(str2, sb2.toString());
        if (str == null || TextUtils.isEmpty(str)) {
            removeViewToToastLayout(this.mToastSwitchTip);
        }
        removeViewToTipLayout(this.mFastmotionTip);
        removeViewToTipLayout(this.mFastmotionProTip);
        this.mZoomTip.setTextSize(Util.pixelToXxhdp(70.0f));
        String str3 = "";
        if (i != 9) {
            this.mZoomTip.setText(str);
        } else if (!TextUtils.isEmpty(getZoomRatioTipText(CameraSettings.isMacroModeEnabled(this.mCurrentMode)))) {
            alertZoom(false);
        } else {
            this.mZoomTip.setText(str3);
        }
        if (Util.isAccessible()) {
            if (i2 <= 0) {
                switch (i) {
                    case 1:
                        string = getString(R.string.accessibility_focus_status, String.valueOf(HybridZoomingSystem.toDecimal(CameraSettings.getRetainZoom(this.mCurrentMode))));
                        break;
                    case 2:
                        sb = new StringBuilder();
                        i3 = R.string.pref_camera_beauty;
                        break;
                    case 3:
                        sb = new StringBuilder();
                        i3 = R.string.fragment_tab_name_bokeh;
                        break;
                    case 4:
                        sb = new StringBuilder();
                        i3 = R.string.portrait_mode_depth_effect_success_hint;
                        break;
                    case 5:
                        sb = new StringBuilder();
                        i3 = R.string.parameter_wb_title;
                        break;
                    case 6:
                        sb = new StringBuilder();
                        i3 = R.string.parameter_et_title;
                        break;
                    case 7:
                        sb = new StringBuilder();
                        i3 = R.string.parameter_iso_title;
                        break;
                    case 8:
                        sb = new StringBuilder();
                        i3 = R.string.parameter_exposure_title;
                        break;
                    case 9:
                        sb = new StringBuilder();
                        sb.append(getString(R.string.parameter_focus_title));
                        sb.append(str);
                        break;
                    default:
                        this.mZoomTipContentDescription = str3;
                        break;
                }
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(getString(i2));
                sb3.append(this.mZoomTip.getText());
                string = sb3.toString();
            }
            this.mZoomTipContentDescription = string;
            this.mZoomTip.setContentDescription(this.mZoomTipContentDescription);
            if (i != 1) {
                this.mHandler.postDelayed(this.mZoomTipAnnounceRunnable, 500);
            }
        }
        this.mHandler.postDelayed(this.mZoomTipToResetRunnable, 1000);
        if (this.mTopTipLayout.indexOfChild(this.mZoomTip) == -1 || this.mZoomTip.getVisibility() != 0) {
            if (this.mCurrentMode != 204) {
                addViewToTipLayout(this.mZoomTip);
            }
            alertAudioZoomIndicator(false);
            return;
        }
        alertAudioZoomIndicator(false);
    }

    public void alertVideoUltraClear(int i, @StringRes int i2, boolean z) {
        alertVideoUltraClear(i, getString(i2), z);
    }

    public void alertVideoUltraClear(int i, String str, boolean z) {
        if (this.mVideoUltraClearTip.getVisibility() != 8 || i != 8) {
            if (i != 0 || !z) {
                this.mVideoUltraClearTip.setVisibility(i);
            } else {
                this.mVideoUltraClearTip.setVisibility(i);
                ViewCompat.setAlpha(this.mVideoUltraClearTip, 0.0f);
                ViewCompat.animate(this.mVideoUltraClearTip).alpha(1.0f).setDuration(320).start();
            }
            if (i == 0) {
                this.mVideoUltraClearTip.setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.top_left_english_tip_size));
                this.mVideoUltraClearTip.setText(str);
                this.mVideoUltraClearTip.setContentDescription(str);
            }
            updateEndGravityTip();
        }
    }

    public void alertVolumeControlAnimationView(int i) {
        LottieAnimationView lottieAnimationView = this.mVolumeControlAnimationView;
        if (lottieAnimationView != null) {
            lottieAnimationView.setVisibility(i);
        }
        updateEndGravityTip();
    }

    public void clear(boolean z) {
        removeZoomTipRestRunnable();
        clearAlertStatus();
        if (z) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.clearAllTipsState();
            }
            clearHandlerCallbacks();
        }
        this.mAlertImageType = -1;
        int childCount = this.mToastTopTipLayout.getChildCount();
        ArrayList<View> arrayList = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.mToastTopTipLayout.getChildAt(i);
            Object tag = childAt.getTag();
            if (tag == null || !(tag == null || !(tag instanceof Integer) || ((Integer) tag).intValue() == 2)) {
                arrayList.add(childAt);
            }
            if (z) {
                arrayList.add(childAt);
            }
        }
        for (View removeView : arrayList) {
            this.mToastTopTipLayout.removeView(removeView);
        }
        if (this.mToastTopTipLayout.getChildCount() <= 0) {
            this.mToastTopTipLayout.removeAllViews();
            setToastTipLayoutParams(true);
            this.mToastTopTipLayout.setVisibility(8);
        }
        arrayList.clear();
        int childCount2 = this.mTopTipLayout.getChildCount();
        for (int i2 = 0; i2 < childCount2; i2++) {
            View childAt2 = this.mTopTipLayout.getChildAt(i2);
            if (i2 != 0) {
                arrayList.add(childAt2);
            } else {
                setToastTipLayoutParams(true);
            }
        }
        for (View removeView2 : arrayList) {
            this.mTopTipLayout.removeView(removeView2);
        }
        clearVideoUltraClear();
        if (z) {
            ImageView imageView = this.mManualParameterResetTip;
            if (!(imageView == null || imageView.getVisibility() == 8)) {
                this.mManualParameterResetTip.setVisibility(8);
            }
        }
        if (z) {
            ImageView imageView2 = this.mManualParameterDescriptionTip;
            if (!(imageView2 == null || imageView2.getVisibility() == 8)) {
                this.mManualParameterDescriptionTip.setVisibility(8);
            }
        }
        if (z) {
            animateViews(-1, false, (View) this.mFrameLayoutAudioMap);
        }
        if (z) {
            animateViews(-1, false, (View) this.mFrameLayoutHistogram);
        }
        this.mShortDurationToastTip = TIP_UNKNOW;
    }

    public void clearAlertStatus() {
        removeViewToTipLayout(this.mZoomTip);
    }

    public void clearFastmotionTip() {
        removeViewToTipLayout(this.mFastmotionTip, false);
        removeViewToTipLayout(this.mFastmotionProTip, false);
    }

    public void clearVideoUltraClear() {
        TextView textView = this.mVideoUltraClearTip;
        if (!(textView == null || textView.getVisibility() == 8)) {
            this.mVideoUltraClearTip.setText("");
            this.mVideoUltraClearTip.setVisibility(8);
        }
        FastmotionIndicatorView fastmotionIndicatorView = this.mFastmotionIndicatorView;
        if (!(fastmotionIndicatorView == null || fastmotionIndicatorView.getVisibility() == 8)) {
            this.mFastmotionIndicatorView.setVisibility(8);
        }
        updateEndGravityTip();
    }

    public boolean containShortDurationDescriptionTips(String str) {
        return this.mShortDurationDescriptionTip.equals(str);
    }

    public int getAudioMapVisibilityState() {
        AudioMapMove audioMapMove = this.mAudioMapMove;
        if (audioMapMove != null) {
            return audioMapMove.getVisibility();
        }
        return 0;
    }

    public int getFragmentInto() {
        return 253;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_top_alert;
    }

    public VideoTagView getVideoTag() {
        return this.mVideoTagView;
    }

    public void hideRecommendDescTip(String str) {
        if (containShortDurationDescriptionTips(str)) {
            removeViewToTipLayout(this.mRecommendTip);
        }
    }

    public void hideRecordingTime() {
        if (this.mAlertRecordingText != null && this.mIsVideoRecording) {
            AlphaAnimation alphaAnimation = this.mBlingAnimation;
            if (alphaAnimation != null) {
                alphaAnimation.cancel();
            }
            this.mAlertRecordingText.setVisibility(4);
        }
    }

    public void hideSwitchTip() {
        removeViewToToastLayout(this.mToastSwitchTip);
        this.mShortDurationToastTip = TIP_UNKNOW;
        if (this.mTopTipLayout.getVisibility() == 0) {
            this.mTopTipLayout.removeCallbacks(this.mViewHideRunnable);
        }
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        initHandler();
        this.mLlAlertRecordingTimeView = (LinearLayout) view.findViewById(R.id.ll_alert_recording_time_view);
        this.mLlAlertRecordingTimeView.setOnClickListener(new C0321O0000o0O(this));
        this.mAlertRecordingText = (TextView) view.findViewById(R.id.alert_recording_time_view);
        this.mFastmotionProAlertRecordingText = (TextView) view.findViewById(R.id.fastmotion_pro_alert_recording_time_view);
        this.mAlertRecordingTextNumerator = (TextView) view.findViewById(R.id.alert_recording_numerator_view);
        this.mAlertRecordingTextDenominator = (TextView) view.findViewById(R.id.alert_recording_time_denominator_view);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mLlAlertRecordingTimeView.getLayoutParams();
        marginLayoutParams.height = Display.getTopBarHeight();
        marginLayoutParams.topMargin = Display.getTopMargin();
        setViewTopMargin(this.mLlAlertRecordingTimeView, Display.getTopMargin());
        this.mAlertRecordingText.setVisibility(8);
        this.mFastmotionProAlertRecordingText.setVisibility(8);
        this.mAlertRecordingTextNumerator.setVisibility(8);
        this.mAlertRecordingTextDenominator.setVisibility(8);
        this.mVideoTagView = new VideoTagView();
        this.mVideoTagView.init(view, getContext());
        this.mVideoUltraClearTip = (TextView) view.findViewById(R.id.video_ultra_clear_tip);
        this.mFastmotionIndicatorView = (FastmotionIndicatorView) view.findViewById(R.id.fastmotion_indicator);
        this.mAiAudioTip = (TextView) view.findViewById(R.id.ai_audio_tip);
        this.mEndGravityTipLayout = (LinearLayout) view.findViewById(R.id.end_gravity_tip_layout);
        this.mEndGravityTipLayout.setDividerDrawable(getEndGravityTipDividerDrawable());
        this.mEndGravityTipLayout.setShowDividers(2);
        setViewTopMargin(this.mEndGravityTipLayout, getAlertTopMargin());
        this.mStartGravityTipLayout = (LinearLayout) view.findViewById(R.id.start_gravity_layout);
        setViewTopMargin(this.mStartGravityTipLayout, getAlertStartGravityTipLayoutTopMargin());
        this.mManualParameterResetTip = (ImageView) view.findViewById(R.id.reset_manually_parameter_tip);
        this.mManualParameterResetTip.setOnClickListener(this);
        this.mManualParameterDescriptionTip = (ImageView) view.findViewById(R.id.manually_parameter_description_tip);
        this.mManualParameterDescriptionTip.setOnClickListener(this);
        FolmeUtils.touchTint(this.mManualParameterResetTip, this.mManualParameterDescriptionTip);
        int i = sPendingRecordingTimeState;
        if (i != 0) {
            setRecordingTimeState(i, false);
            setPendingRecordingState(0);
        }
        this.mTopTipLayout = (LinearLayout) view.findViewById(R.id.top_tip_layout);
        initPortraitTopTipLayout();
        this.mToastTopTipLayout = (LinearLayout) this.mTopTipLayout.findViewById(R.id.top_toast_layout);
        initToastTipLayout();
        this.mAiSceneSelectView = initTopTipToggleSwitch();
        this.mSlideSwitchButton = initTopSlideSwitchButton();
        this.mRecommendTip = initRecommendTipText();
        this.mZoomTip = initZoomTipText();
        this.mLiveMusicHintLayout = initMusicTipText();
        this.mLiveMusiHintText = (TextView) this.mLiveMusicHintLayout.findViewById(R.id.live_music_title_hint);
        this.mLiveMusicClose = (ImageView) this.mLiveMusicHintLayout.findViewById(R.id.live_music_close);
        FolmeUtils.touchTint((View) this.mLiveMusicHintLayout);
        this.mLiveMusicHintLayout.setOnClickListener(new O0000o00(this));
        this.mPermanentTip = initPermanentTip();
        this.mSubtitleTip = initSubtitleTip();
        this.mProColourTip = initProColourTip();
        this.mLyingDirectHintText = initHorizonDirectTipText();
        this.mMacroModeTip = initMacroModeTip();
        this.mDualVideoTip = initDualVideoTip();
        this.mTimerBurstTip = initTimerBurstTip();
        this.mAiAudioBGTip = initAiAudioBGTip();
        this.mAiEnhancedVideoTip = initAiEnhancedVideoTip();
        this.mHandGestureTip = initHandGestureTip();
        this.mAudioMapMove = (AudioMapMove) view.findViewById(R.id.audio_map_display);
        this.mAudioMapMove.setOnAudioMapPressAnimatorListener(this);
        this.mAudioMapMove.setOnClickListener(this);
        this.mFrameLayoutAudioMap = (FrameLayout) view.findViewById(R.id.audio_map_volume_panel);
        this.mFrameLayoutMicPanel = (FrameLayout) view.findViewById(R.id.volume_control_mic_panel);
        this.mFrameLayoutHistogram = (FrameLayout) view.findViewById(R.id.histogram_panel);
        this.mFrameLayoutAudioMap.setRotation(90.0f);
        this.mFrameLayoutHistogram.setTag(Integer.valueOf(-1));
        this.mFrameLayoutAudioMap.setTag(Integer.valueOf(-1));
        MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mFrameLayoutAudioMap.getLayoutParams();
        marginLayoutParams2.width = getResources().getDimensionPixelSize(R.dimen.audio_map_move_layout_height);
        marginLayoutParams2.height = getResources().getDimensionPixelSize(R.dimen.audio_map_move_layout_height);
        this.mVolumeControlPanel = (VolumeControlPanel) view.findViewById(R.id.volume_control_panel);
        this.mVolumeControlPanel.setOnVolumeControlListener(this);
        this.mVolumeControlPanel.setOnVolumePressAnimatorListener(this);
        VolumeControlPanel volumeControlPanel = this.mVolumeControlPanel;
        volumeControlPanel.setBackground(volumeControlPanel.setVolumeControlBackGround());
        this.mClickRectMoveDistance = (float) getResources().getDimensionPixelSize(R.dimen.volume_control_click_rect_move_distance);
        this.mHistogramView = (HistogramView) view.findViewById(R.id.rgb_histogram);
        this.mFastmotionTip = initFastmotionTip();
        this.mFastmotionTipTitle = (TextView) this.mFastmotionTip.findViewById(R.id.fastmotion_top_tip_layout_title);
        this.mFastmotionTipDesc = (TextView) this.mFastmotionTip.findViewById(R.id.fastmotion_top_tip_layout_desc);
        this.mFastmotionTipTitle.setTypeface(Typeface.create("mipro-regular", 0));
        this.mFastmotionTipDesc.setTypeface(Typeface.create("mipro-medium", 0));
        this.mFastmotionProTip = initFastmotionProTip();
        this.mFastmotionProTipTitle = (TextView) this.mFastmotionProTip.findViewById(R.id.fastmotion_pro_top_tip_layout_title);
        this.mFastmotionProIndicatorView = (FastmotionIndicatorView) this.mFastmotionProTip.findViewById(R.id.fastmotion_pro_top_tip_layout_indicator);
        this.mFastmotionProTipSpeedDesc = (TextView) this.mFastmotionProTip.findViewById(R.id.fastmotion_pro_top_tip_layout_speed_desc);
        this.mFastmotionProTipSaveTime = (TextView) this.mFastmotionProTip.findViewById(R.id.fastmotion_pro_top_tip_layout_save_time);
        this.mProVideoRecordingSimpleView = (LottieAnimationView) view.findViewById(R.id.pro_video_recording_simple_view);
        this.mVolumeControlAnimationView = (LottieAnimationView) view.findViewById(R.id.volume_silence_strengthen_switch);
        this.mVolumeControlAnimationView.setRotation(270.0f);
        FolmeUtils.touchTint((View) this.mProVideoRecordingSimpleView);
        MarginLayoutParams marginLayoutParams3 = (MarginLayoutParams) this.mProVideoRecordingSimpleView.getLayoutParams();
        marginLayoutParams3.height = getResources().getDimensionPixelSize(R.dimen.pro_video_recording_simple_width);
        marginLayoutParams3.topMargin = Display.getTopMargin() + ((Display.getTopBarHeight() - getResources().getDimensionPixelSize(R.dimen.pro_video_recording_simple_width)) / 2);
        this.mProVideoRecordingSimpleView.setOnClickListener(new O0000Oo0(this));
    }

    public boolean isContainAlertRecommendTip(@StringRes int... iArr) {
        LinearLayout linearLayout = this.mTopTipLayout;
        if (linearLayout != null) {
            TextView textView = this.mRecommendTip;
            if (!(textView == null || linearLayout.indexOfChild(textView) == -1 || iArr == null || iArr.length <= 0)) {
                int length = iArr.length;
                for (int i = 0; i < length; i++) {
                    int i2 = iArr[i];
                    if (i2 > 0 && getResources().getString(i2).equals(this.mRecommendTip.getText())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCurrentRecommendTipText(int i) {
        if (i <= 0) {
            return false;
        }
        String string = getResources().getString(i);
        return !TextUtils.isEmpty(string) && isShowTopLayoutSpecifyTip(this.mRecommendTip) && string.equals(this.mRecommendTip.getText());
    }

    public boolean isFlashShowing() {
        boolean z = false;
        if (!(this.mToastTopTipLayout == null || this.mToastTipFlash == null)) {
            if (!TIP_FLASH.equals(this.mShortDurationToastTip)) {
                return false;
            }
            if (this.mToastTipFlash.getVisibility() == 0) {
                z = true;
            }
        }
        return z;
    }

    public boolean isHDRShowing() {
        boolean z = false;
        if (!(this.mToastTopTipLayout == null || this.mToastTipFlash == null)) {
            if (this.mShortDurationToastTip != TIP_HDR) {
                return false;
            }
            if (this.mToastSwitchTip.getVisibility() == 0) {
                z = true;
            }
        }
        return z;
    }

    public boolean isShow() {
        return this.mShow;
    }

    public boolean isShowBacklightSelector() {
        LinearLayout linearLayout = this.mTopTipLayout;
        return (linearLayout == null || linearLayout.indexOfChild(this.mAiSceneSelectView) == -1 || !getResources().getString(R.string.text_ai_scene_selector_text_on).equals(this.mAiSceneSelectView.getTextOn())) ? false : true;
    }

    public boolean isShowMoonSelector() {
        LinearLayout linearLayout = this.mTopTipLayout;
        return (linearLayout == null || linearLayout.indexOfChild(this.mAiSceneSelectView) == -1 || !getResources().getString(R.string.ai_scene_top_tip).equals(this.mAiSceneSelectView.getTextOn())) ? false : true;
    }

    public boolean isShowTopLayoutSpecifyTip(View view) {
        if (view != null) {
            LinearLayout linearLayout = this.mTopTipLayout;
            return (linearLayout == null || linearLayout.indexOfChild(view) == -1) ? false : true;
        }
        return false;
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        if (CameraSettings.isProVideoHistogramOpen(this.mCurrentMode) || CameraSettings.isProPhotoHistogramOpen(this.mCurrentMode)) {
            animateViews(1, true, (View) this.mFrameLayoutHistogram);
        }
        if (CameraSettings.isProVideoAudioMapOpen(this.mCurrentMode)) {
            animateViews(1, true, (View) this.mFrameLayoutAudioMap);
            setVolumeControlPanelGone(8);
            alertAudioMap(0);
            alertVolumeControlAnimationView(8);
        }
        checkDependingVisible();
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onClick(View view) {
        String str;
        DialogFragment dialogFragment;
        if (isEnableClick()) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction == null || !cameraAction.isDoingAction()) {
                int id = view.getId();
                if (id == R.id.audio_map_display) {
                    this.mAudioMapMove.setVisibility(8);
                    this.mVolumeControlAnimationView.setVisibility(0);
                    this.mVolumeControlPanel.setVisibility(0);
                    this.mHandler.postDelayed(this.mRunnable, 3000);
                } else if (id == R.id.manually_parameter_description_tip) {
                    Log.u(TAG, String.format(Locale.ENGLISH, "onClick manually_parameter_description_tip: currentMode=0x%x", new Object[]{Integer.valueOf(this.mCurrentMode)}));
                    int i = this.mCurrentMode;
                    String str2 = i == 180 ? "M_proVideo_" : i == 169 ? "M_fastMotion_" : i == 162 ? "M_recordVideo_" : i == 187 ? ModuleName.AMBILIGHT : "M_manual_";
                    MistatsWrapper.moduleUIClickEvent(str2, Manual.PARAMETER_DESCRIPTION, (Object) "on");
                    if (((Integer) view.getTag()).intValue() == 0) {
                        int i2 = this.mCurrentMode;
                        str = i2 == 187 ? FragmentAmbilightDescription.TAG : i2 == 169 ? FragmentFastMotionDescription.TAG : FragmentParameterDescription.TAG;
                    } else {
                        str = FragmentMasterFilterDescription.TAG;
                    }
                    if (FragmentUtils.getFragmentByTag(getFragmentManager(), str) == null) {
                        if (((Integer) view.getTag()).intValue() == 0) {
                            int i3 = this.mCurrentMode;
                            if (i3 == 187) {
                                dialogFragment = new FragmentAmbilightDescription();
                                CameraStatUtils.trackAmbilightClick(Ambilight.PARAM_AMBILIGHT_ABOUT_BUTTON_CLICK);
                            } else {
                                dialogFragment = i3 == 169 ? new FragmentFastMotionDescription() : new FragmentParameterDescription();
                            }
                        } else {
                            dialogFragment = new FragmentMasterFilterDescription();
                        }
                        dialogFragment.setStyle(2, R.style.ManuallyDescriptionFragment);
                        getFragmentManager().beginTransaction().add((Fragment) dialogFragment, str).commitAllowingStateLoss();
                    }
                } else if (id == R.id.reset_manually_parameter_tip) {
                    Log.u(TAG, "onClick reset_manually_parameter_tip");
                    CameraStatUtils.trackManuallyResetClick();
                    showManualParameterResetDialog();
                }
                return;
            }
            Log.d(TAG, "cameraAction.isDoingAction return");
        }
    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        showRecordingTime();
    }

    public void onStop() {
        super.onStop();
        clear(true);
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mAlertDialog = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        AlphaAnimation alphaAnimation = this.mBlingAnimation;
        if (alphaAnimation != null) {
            alphaAnimation.cancel();
            this.mBlingAnimation = null;
        }
        TextView textView = this.mAlertRecordingText;
        if (textView != null) {
            textView.setVisibility(8);
        }
        TextView textView2 = this.mFastmotionProAlertRecordingText;
        if (textView2 != null) {
            textView2.setVisibility(8);
        }
        this.mIsAlertAnim = true;
        TextView textView3 = this.mAlertRecordingTextNumerator;
        if (textView3 != null) {
            textView3.setVisibility(8);
        }
        TextView textView4 = this.mAlertRecordingTextDenominator;
        if (textView4 != null) {
            textView4.setVisibility(8);
        }
    }

    public int parseLightingRes(int i) {
        if (i == -1) {
            return -1;
        }
        if (i == 3) {
            return R.string.lighting_hint_too_close;
        }
        if (i == 4) {
            return R.string.lighting_hint_too_far;
        }
        if (i != 5) {
            return -1;
        }
        return R.string.lighting_hint_needed;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0073  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void provideAnimateElement(int i, List list, int i2) {
        int i3;
        TextView textView;
        int i4 = this.mCurrentMode;
        this.mIsAlertAnim = true;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("provideAnimateElement ");
        sb.append(i2);
        Log.d(str, sb.toString());
        boolean z = false;
        if (i2 == 3) {
            this.mIsVideoRecording = false;
            this.mAlertRecordingText.setAnimation(null);
            this.mAlertRecordingText.setVisibility(8);
            this.mFastmotionProAlertRecordingText.setVisibility(8);
            this.mAlertRecordingTextDenominator.setVisibility(8);
            textView = this.mAlertRecordingTextNumerator;
        } else if (this.mIsVideoRecording) {
            setRecordingTimeState(3, false);
            super.provideAnimateElement(i, list, i2);
            if (!(i4 == i || ((i4 == 163 && i == 165) || (i4 == 165 && i == 163)))) {
                z = true;
            }
            clear(z);
            setShow(true);
            updateEndGravityTip();
            updateTopAlertLayout();
            if (this.mCurrentMode != 180) {
                animateViews(-1, list, (View) this.mFrameLayoutAudioMap);
            }
            i3 = this.mCurrentMode;
            if (i3 != 180 && i3 != 167) {
                animateViews(-1, list, (View) this.mFrameLayoutHistogram);
                return;
            }
        } else {
            this.mAlertRecordingText.setVisibility(8);
            textView = this.mFastmotionProAlertRecordingText;
        }
        textView.setVisibility(8);
        super.provideAnimateElement(i, list, i2);
        z = true;
        clear(z);
        setShow(true);
        updateEndGravityTip();
        updateTopAlertLayout();
        if (this.mCurrentMode != 180) {
        }
        i3 = this.mCurrentMode;
        if (i3 != 180) {
        }
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        initEndGravityTipLayout(i);
        list.add(this.mManualParameterDescriptionTip);
        list.add(this.mManualParameterResetTip);
        updateTopAlertLayout();
    }

    public void putVolumeControlValue(float f) {
        this.mVolumeControlValue = f;
    }

    @MainThread
    public void refreshHistogramStatsView() {
        FrameLayout frameLayout = this.mFrameLayoutHistogram;
        if (frameLayout != null && frameLayout.getVisibility() == 0) {
            this.mHistogramView.refresh();
        }
    }

    public void removeFastmotionTipResetRunnable() {
        this.mFastmotionTipContentDescription = "";
        this.mHandler.removeCallbacks(this.mFastmotionTipAnnounceRunnable);
        this.mHandler.removeCallbacks(this.mFastmotionTipToResetRunnable);
    }

    public void removeHandlerCallBack() {
        this.mHandler.removeCallbacks(this.mRunnable);
    }

    public void removePostDelayedTime() {
        this.mHandler.removeCallbacks(this.mRunnable);
    }

    public void removeZoomTipRestRunnable() {
        this.mZoomTipContentDescription = "";
        this.mHandler.removeCallbacks(this.mZoomTipAnnounceRunnable);
        this.mHandler.removeCallbacks(this.mZoomTipToResetRunnable);
    }

    public void setAlertAnim(boolean z) {
        this.mIsAlertAnim = z;
    }

    public void setAudioMapMoveVolumeValue(float[] fArr) {
        AndroidSchedulers.mainThread().scheduleDirect(new C0318O00000oo(this, fArr));
    }

    public void setAudioMapVisibility(int i) {
        AudioMapMove audioMapMove = this.mAudioMapMove;
        if (audioMapMove != null) {
            audioMapMove.setVisibility(i);
        }
    }

    public void setPostDelayedTime() {
        this.mHandler.postDelayed(this.mRunnable, 3000);
    }

    public void setPressAnimatorX() {
        this.mAnimator1 = ObjectAnimator.ofFloat(this.mFrameLayoutAudioMap, "scaleX", new float[]{1.0f, 1.54f});
        this.mAnimator1.setDuration(300);
        this.mAnimator1.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FragmentTopAlert.this.mPercentX = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            }
        });
        this.mAnimator1.start();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mFrameLayoutAudioMap, "translationX", new float[]{0.0f, getVolumeControlTranslationRotation()});
        ofFloat.setDuration(300);
        ofFloat.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FragmentTopAlert.this.mXMovePosition = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            }
        });
        ofFloat.start();
    }

    public void setPressAnimatorY() {
        this.mAnimator1 = ObjectAnimator.ofFloat(this.mFrameLayoutAudioMap, "scaleY", new float[]{1.0f, 2.0f});
        this.mAnimator1.setDuration(300);
        this.mAnimator1.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FragmentTopAlert.this.mPercentY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            }
        });
        this.mAnimator1.start();
    }

    public void setPressAudioMapMoveAnimator(AudioMapMove audioMapMove) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.99f, 0.2f);
        alphaAnimation.setDuration(100);
        if (audioMapMove.getVisibility() == 0) {
            audioMapMove.startAnimation(alphaAnimation);
            audioMapMove.setVisibility(8);
            this.mVolumeControlPanel.setVisibility(0);
        }
    }

    public void setPressAudioMapPressAnimator() {
        this.mHandler.postDelayed(this.mRunnable, 3000);
        setPressAudioMapMoveAnimator(this.mAudioMapMove);
        this.mVolumeControlAnimationView.setVisibility(0);
        this.mVolumeControlPanel.setVisibility(0);
        removePostDelayedTime();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00b2, code lost:
        if (r0.mIsParameterDescriptionVisibleBeforeRecording != false) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00c2, code lost:
        if (r0.mIsParameterDescriptionVisibleBeforeRecording != false) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0217, code lost:
        if (r0.mManualParameterDescriptionTip.getVisibility() == 0) goto L_0x0231;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x022f, code lost:
        if (r0.mManualParameterDescriptionTip.getVisibility() == 0) goto L_0x0231;
     */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x023c  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x0246  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x02de  */
    /* JADX WARNING: Removed duplicated region for block: B:127:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00cf  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setRecordingTimeState(int i, boolean z) {
        int timerBurstTotalCount;
        TextView textView;
        StringBuilder sb;
        String str;
        TextView textView2;
        AlphaAnimation alphaAnimation;
        int i2 = i;
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" setRecordingTimeState ");
        sb2.append(i2);
        sb2.append("   mCurrentMode: ");
        sb2.append(this.mCurrentMode);
        Log.d(str2, sb2.toString());
        if (i2 == 1) {
            this.mIsVideoRecording = true;
            this.mLlAlertRecordingTimeView.setVisibility(0);
            TimerBurstController timerBurstController = DataRepository.dataItemLive().getTimerBurstController();
            int i3 = this.mCurrentMode;
            String str3 = "00:00";
            String str4 = "/";
            if (i3 == 165) {
                if (timerBurstController.isInTimerBurstShotting()) {
                    this.mAlertRecordingText.setVisibility(8);
                    this.mFastmotionProAlertRecordingText.setVisibility(8);
                    this.mAlertRecordingTextNumerator.setVisibility(0);
                    timerBurstTotalCount = CameraSettings.getTimerBurstTotalCount();
                    this.mAlertRecordingTextNumerator.setText(String.valueOf(timerBurstController.getCaptureIndex()));
                    this.mAlertRecordingTextDenominator.setVisibility(0);
                    this.mAlertRecordingTextDenominator.setTextColor(getContext().getResources().getColor(R.color.vv_progress_bg_color));
                    textView = this.mAlertRecordingTextDenominator;
                    sb = new StringBuilder();
                }
                this.mFastmotionProAlertRecordingText.setText("");
                if (z) {
                }
            } else if (i3 != 167) {
                if (!(i3 == 169 || i3 == 172)) {
                    String str5 = "00:15";
                    if (!(i3 == 174 || i3 == 177)) {
                        if (!(i3 == 180 || i3 == 187)) {
                            if (i3 == 212) {
                                textView2 = this.mAlertRecordingText;
                                str = "00:10";
                            } else if (i3 != 214) {
                                if (i3 != 183) {
                                    if (i3 != 184) {
                                        if (!(i3 == 207 || i3 == 208)) {
                                            switch (i3) {
                                                case 161:
                                                    break;
                                                case 162:
                                                    break;
                                                case 163:
                                                    if (!timerBurstController.isInTimerBurstShotting()) {
                                                        this.mAlertRecordingText.setText(str5);
                                                        updateFlashForPhotoRecording(true);
                                                        break;
                                                    } else {
                                                        this.mAlertRecordingText.setVisibility(8);
                                                        this.mFastmotionProAlertRecordingText.setVisibility(8);
                                                        this.mAlertRecordingTextNumerator.setVisibility(0);
                                                        timerBurstTotalCount = CameraSettings.getTimerBurstTotalCount();
                                                        this.mAlertRecordingTextNumerator.setText(String.valueOf(timerBurstController.getCaptureIndex()));
                                                        this.mAlertRecordingTextDenominator.setVisibility(0);
                                                        this.mAlertRecordingTextDenominator.setTextColor(getContext().getResources().getColor(R.color.vv_progress_bg_color));
                                                        textView = this.mAlertRecordingTextDenominator;
                                                        sb = new StringBuilder();
                                                    }
                                            }
                                        }
                                    } else if (CameraSettings.isGifOn()) {
                                        textView2 = this.mAlertRecordingText;
                                        str = "00:05";
                                    }
                                }
                            }
                            textView2.setText(str);
                            this.mFastmotionProAlertRecordingText.setText("");
                            if (z && !timerBurstController.isInTimerBurstShotting()) {
                                Completable.create(new AlphaInOnSubscribe(this.mAlertRecordingText)).subscribe();
                                if ((this.mCurrentMode == 169 && C0122O00000o.instance().OOO00Oo()) || this.mCurrentMode == 208) {
                                    this.mFastmotionProAlertRecordingText.setText(str3);
                                    Completable.create(new AlphaInOnSubscribe(this.mFastmotionProAlertRecordingText)).subscribe();
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                    }
                    this.mAlertRecordingText.setText(str5);
                    this.mFastmotionProAlertRecordingText.setText("");
                    if (z) {
                        return;
                    }
                    return;
                }
                int i4 = this.mCurrentMode;
                if (i4 != 180 && (i4 != 169 || (!C0122O00000o.instance().OOO00O0() && !C0122O00000o.instance().OOO00Oo()))) {
                    if (this.mCurrentMode == 187) {
                        this.mAlertRecordingText.setTypeface(Typeface.create("monospace", 1));
                    }
                    TextView textView3 = this.mAlertRecordingText;
                    if (!z) {
                        textView3.setVisibility(8);
                        this.mFastmotionProAlertRecordingText.setVisibility(8);
                    } else {
                        textView3.setText(str3);
                    }
                    this.mFastmotionProAlertRecordingText.setText("");
                    if (z) {
                    }
                } else if (this.mManualParameterResetTip.getVisibility() == 0) {
                    this.mManualParameterResetTip.setVisibility(4);
                    this.mIsParameterResetVisibleBeforeRecording = true;
                }
                this.mManualParameterDescriptionTip.setVisibility(4);
                this.mIsParameterDescriptionVisibleBeforeRecording = true;
                TextView textView32 = this.mAlertRecordingText;
                if (!z) {
                }
                this.mFastmotionProAlertRecordingText.setText("");
                if (z) {
                }
            } else {
                if (timerBurstController.isInTimerBurstShotting()) {
                    this.mAlertRecordingText.setVisibility(8);
                    this.mFastmotionProAlertRecordingText.setVisibility(8);
                    this.mAlertRecordingTextNumerator.setVisibility(0);
                    timerBurstTotalCount = CameraSettings.getTimerBurstTotalCount();
                    this.mAlertRecordingTextNumerator.setText(String.valueOf(timerBurstController.getCaptureIndex()));
                    this.mAlertRecordingTextDenominator.setVisibility(0);
                    this.mAlertRecordingTextDenominator.setTextColor(getContext().getResources().getColor(R.color.vv_progress_bg_color));
                    textView = this.mAlertRecordingTextDenominator;
                    sb = new StringBuilder();
                }
                this.mFastmotionProAlertRecordingText.setText("");
                if (z) {
                }
            }
            sb.append(str4);
            sb.append(timerBurstTotalCount);
            textView.setText(sb.toString());
            this.mFastmotionProAlertRecordingText.setText("");
            if (z) {
            }
        } else if (i2 == 2) {
            this.mIsVideoRecording = false;
            this.mLlAlertRecordingTimeView.setVisibility(8);
            this.mAlertRecordingTextNumerator.setVisibility(8);
            this.mAlertRecordingTextDenominator.setVisibility(8);
            int i5 = this.mCurrentMode;
            if (i5 != 180 && (i5 != 169 || (!C0122O00000o.instance().OOO00O0() && !C0122O00000o.instance().OOO00Oo()))) {
                if (this.mCurrentMode == 187) {
                }
                alphaAnimation = this.mBlingAnimation;
                if (alphaAnimation != null) {
                    alphaAnimation.cancel();
                }
                if (this.mAlertRecordingText.getVisibility() == 0 || this.mAlertRecordingText.getAlpha() == 0.0f) {
                    AlphaOutOnSubscribe.directSetResult(this.mAlertRecordingText);
                } else {
                    Completable.create(new AlphaOutOnSubscribe(this.mAlertRecordingText)).subscribe();
                }
                if ((this.mCurrentMode == 169 && C0122O00000o.instance().OOO00Oo()) || this.mCurrentMode == 208) {
                    Completable.create(new AlphaOutOnSubscribe(this.mFastmotionProAlertRecordingText).targetGone()).subscribe();
                }
                updateFlashForPhotoRecording(false);
            } else if (this.mIsParameterResetVisibleBeforeRecording) {
                this.mIsParameterResetVisibleBeforeRecording = false;
                this.mManualParameterResetTip.setVisibility(0);
            }
            this.mIsParameterDescriptionVisibleBeforeRecording = false;
            this.mManualParameterDescriptionTip.setVisibility(0);
            alphaAnimation = this.mBlingAnimation;
            if (alphaAnimation != null) {
            }
            if (this.mAlertRecordingText.getVisibility() == 0) {
            }
            AlphaOutOnSubscribe.directSetResult(this.mAlertRecordingText);
            Completable.create(new AlphaOutOnSubscribe(this.mFastmotionProAlertRecordingText).targetGone()).subscribe();
            updateFlashForPhotoRecording(false);
        } else if (i2 == 3) {
            if (this.mBlingAnimation == null) {
                this.mBlingAnimation = new AlphaAnimation(1.0f, 0.0f);
                this.mBlingAnimation.setDuration(400);
                this.mBlingAnimation.setStartOffset(100);
                this.mBlingAnimation.setInterpolator(new DecelerateInterpolator());
                this.mBlingAnimation.setRepeatMode(2);
                this.mBlingAnimation.setRepeatCount(-1);
            }
            this.mAlertRecordingText.startAnimation(this.mBlingAnimation);
        } else if (i2 == 4) {
            AlphaAnimation alphaAnimation2 = this.mBlingAnimation;
            if (alphaAnimation2 != null) {
                alphaAnimation2.cancel();
            }
        }
    }

    public void setShow(boolean z) {
        this.mShow = z;
    }

    public void setSilenceUpSwitchTarget(boolean z) {
        this.mVolumeControlAnimationView.cancelAnimation();
        this.mVolumeControlAnimationView.setScale(0.34f);
        this.mVolumeControlAnimationView.O0000O0o(z ? R.raw.volume_silence : R.raw.volume_up);
        this.mVolumeControlAnimationView.setProgress(0.9f);
        this.mVolumeControlAnimationView.O0000oO();
        if (z) {
            this.mVolumeControlAnimationView.setVisibility(0);
        }
    }

    public void setUpAnimatorX() {
        this.mAnimator1 = ObjectAnimator.ofFloat(this.mFrameLayoutAudioMap, "scaleX", new float[]{this.mPercentX, 1.0f});
        this.mAnimator1.setDuration(300);
        this.mAnimator1.start();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mFrameLayoutAudioMap, "translationX", new float[]{this.mXMovePosition, 0.0f});
        ofFloat.setDuration(300);
        ofFloat.start();
    }

    public void setUpAnimatorY() {
        this.mAnimator1 = ObjectAnimator.ofFloat(this.mFrameLayoutAudioMap, "scaleY", new float[]{this.mPercentY, 1.0f});
        this.mAnimator1.setDuration(300);
        this.mAnimator1.start();
    }

    public void setUpAudioMapPressAnimator() {
        setPostDelayedTime();
    }

    public void setVolumeControlAnimationViewVisibility(int i) {
        LottieAnimationView lottieAnimationView = this.mVolumeControlAnimationView;
        if (lottieAnimationView != null) {
            lottieAnimationView.setVisibility(i);
        }
    }

    public void setVolumeControlPanelGone(int i) {
        VolumeControlPanel volumeControlPanel = this.mVolumeControlPanel;
        if (volumeControlPanel != null) {
            volumeControlPanel.setVisibility(i);
        }
    }

    public void setVolumeControlPanelVisibility(int i) {
        VolumeControlPanel volumeControlPanel = this.mVolumeControlPanel;
        if (volumeControlPanel != null) {
            volumeControlPanel.setVisibility(i);
        }
    }

    public void setVolumeControlValue(float f) {
        if (this.mVolumeControlValue - f >= ((float) getResources().getDimensionPixelSize(R.dimen.volume_control_rect_bottom)) && this.mSwitchAnimator) {
            this.mSwitchAnimator = false;
            setSilenceUpSwitchTarget(true);
        } else if (this.mVolumeControlValue - f < ((float) getResources().getDimensionPixelSize(R.dimen.volume_control_rect_bottom)) && !this.mSwitchAnimator) {
            this.mSwitchAnimator = true;
            setSilenceUpSwitchTarget(false);
        }
        this.mVolumeControlPanel.setValue(f);
    }

    public void showDocumentStateButton(int i) {
        if (this.mDocumentStateButton == null) {
            initDocumentStateButton();
        }
        if (i == 0) {
            addViewToTipLayout(this.mDocumentStateButton, true, null, this.mTopTipLayout.getChildCount() > 0 ? 1 : -1);
            updateDocumentState(false);
            return;
        }
        removeViewToTipLayout(this.mDocumentStateButton);
    }

    public void showRecordingTime() {
        TextView textView = this.mAlertRecordingText;
        if (textView != null && this.mIsVideoRecording) {
            AlphaAnimation alphaAnimation = this.mBlingAnimation;
            if (alphaAnimation != null) {
                textView.startAnimation(alphaAnimation);
            }
            this.mAlertRecordingText.setVisibility(0);
        }
    }

    public void updateEndGravityTip() {
        LinearLayout linearLayout = this.mEndGravityTipLayout;
        if (linearLayout != null) {
            linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    FragmentTopAlert.this.mEndGravityTipLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if (FragmentTopAlert.this.isAdded()) {
                        FragmentTopAlert fragmentTopAlert = FragmentTopAlert.this;
                        fragmentTopAlert.initEndGravityTipLayout(fragmentTopAlert.mDegree);
                    }
                }
            });
        }
    }

    public void updateFastmotionProRecordingTime(String str, String str2) {
        this.mAlertRecordingText.setText(str);
        this.mFastmotionProAlertRecordingText.setText(str2);
    }

    public void updateHistogramStatsData(int[] iArr) {
        HistogramView histogramView = this.mHistogramView;
        if (histogramView != null) {
            histogramView.updateStats(iArr);
        }
    }

    public void updateHistogramStatsData(int[] iArr, int[] iArr2, int[] iArr3) {
        this.mHistogramView.updateStats(iArr, iArr2, iArr3);
    }

    public void updateLyingDirectHint(boolean z) {
        if (z && this.mTopTipLayout.indexOfChild(this.mLyingDirectHintText) == -1) {
            addViewToTipLayout(this.mLyingDirectHintText, new O00000Oo(this), O0000Oo.INSTANCE);
        } else if (!z && this.mTopTipLayout.indexOfChild(this.mLyingDirectHintText) != -1) {
            removeViewToTipLayout(this.mLyingDirectHintText);
        }
    }

    public void updateProVideoRecordingSimpleView(boolean z) {
        LottieAnimationView lottieAnimationView = this.mProVideoRecordingSimpleView;
        if (lottieAnimationView == null) {
            return;
        }
        if (z) {
            lottieAnimationView.setVisibility(0);
            LottieAnimationView lottieAnimationView2 = this.mProVideoRecordingSimpleView;
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.accessibility_pro_video_simple_mode));
            sb.append(",");
            sb.append(getString(R.string.accessibility_closed));
            lottieAnimationView2.setContentDescription(sb.toString());
            return;
        }
        lottieAnimationView.setVisibility(8);
        if (DataRepository.dataItemRunning().getProVideoRecordingSimpleRunning()) {
            DataRepository.dataItemRunning().setProVideoRecordingSimpleRunning(false);
            ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
            if (manuallyAdjust != null) {
                manuallyAdjust.forceUpdateManualView(true);
            }
            if (CameraSettings.isProVideoHistogramOpen(this.mCurrentMode)) {
                animateViews(1, true, (View) this.mFrameLayoutHistogram);
            }
            if (CameraSettings.isProVideoAudioMapOpen(this.mCurrentMode)) {
                animateViews(1, true, (View) this.mFrameLayoutAudioMap);
            }
            checkDependingVisible();
            if (this.mIsVideoUltraClearTipVisibleBeforeProVideoSimple) {
                this.mIsVideoUltraClearTipVisibleBeforeProVideoSimple = false;
                Completable.create(new AlphaInOnSubscribe(this.mVideoUltraClearTip)).subscribe();
            }
        }
    }

    public void updateRecordingTime(String str) {
        this.mAlertRecordingText.setText(str);
    }

    public void updateRecordingTimeStyle(boolean z) {
        if (!z) {
            this.mAlertRecordingText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (getContext() == null || !Util.isLayoutRTL(getContext())) {
            this.mAlertRecordingText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.v6_ic_video_recordtime_indicator, 0, 0, 0);
        } else {
            this.mAlertRecordingText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.v6_ic_video_recordtime_indicator, 0);
        }
    }

    public void updateTopAlertLayout() {
        if (isBothLandscapeMode() || isLeftLandscapeMode() || isLandScape() || (Util.isAccessible() && this.mCurrentMode == 182)) {
            initLandscapeTopTipLayout();
        } else {
            initPortraitTopTipLayout();
        }
    }
}
