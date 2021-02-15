package com.android.camera.fragment.bottom.action;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.StringRes;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.airbnb.lottie.LottieAnimationView;
import com.android.camera.ActivityBase;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.Thumbnail;
import com.android.camera.ThumbnailUpdater;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.FragmentAnimationFactory;
import com.android.camera.animation.folme.FolmeAlphaInOnSubscribe;
import com.android.camera.animation.folme.FolmeAlphaOutOnSubscribe;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.animation.type.AlphaOutOnSubscribe;
import com.android.camera.animation.type.RotateOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentRunningMasterFilter;
import com.android.camera.data.data.global.ComponentModuleList;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;
import com.android.camera.data.data.runing.ComponentRunningFastMotion;
import com.android.camera.data.data.runing.ComponentRunningFastMotionPro;
import com.android.camera.data.data.runing.ComponentRunningShine;
import com.android.camera.data.observeable.RxData.DataWrap;
import com.android.camera.data.observeable.VMFeature;
import com.android.camera.dualvideo.DualVideoRecordModule;
import com.android.camera.dualvideo.recorder.RecordType;
import com.android.camera.dualvideo.render.LayoutType;
import com.android.camera.dualvideo.util.UserSelectData;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiAvatarEngine2;
import com.android.camera.features.mimoji2.widget.helper.MimojiStatusManager2;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.bottom.BottomActionMenu;
import com.android.camera.fragment.bottom.BottomAnimationConfig;
import com.android.camera.fragment.mode.FragmentMoreModePopup;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.FunModule;
import com.android.camera.module.ILiveModule;
import com.android.camera.module.LiveModule;
import com.android.camera.module.MiLiveModule;
import com.android.camera.module.Module;
import com.android.camera.module.VideoModule;
import com.android.camera.module.impl.component.MimojiStatusManager;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.permission.PermissionManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CameraActionTrack;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.HandleBeautyRecording;
import com.android.camera.protocol.ModeProtocol.HandlerSwitcher;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.MasterFilterProtocol;
import com.android.camera.protocol.ModeProtocol.MimojiAvatarEngine;
import com.android.camera.protocol.ModeProtocol.ModeChangeController;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.MoreModePopupController;
import com.android.camera.protocol.ModeProtocol.MultiFeatureManager;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.StandaloneRecorderProtocol;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.Live;
import com.android.camera.statistic.MistatsConstants.MiLive;
import com.android.camera.statistic.MistatsConstants.Mimoji;
import com.android.camera.statistic.MistatsConstants.MoreMode;
import com.android.camera.statistic.ScenarioTrackUtil;
import com.android.camera.timerburst.TimerBurstController;
import com.android.camera.ui.AdjustAnimationView;
import com.android.camera.ui.AnimationView;
import com.android.camera.ui.CameraSnapView;
import com.android.camera.ui.CameraSnapView.SnapListener;
import com.android.camera.ui.CapsuleLayout;
import com.android.camera.ui.DragLayout;
import com.android.camera.ui.DragLayout.OnDragListener;
import com.android.camera.ui.EdgeHorizonScrollView;
import com.android.camera.ui.ModeSelectView;
import com.android.camera.ui.ModeSelectView.onModeSelectedListener;
import com.android.camera.ui.PopupMenuLayout;
import com.android.camera.ui.ShapeBackGroundView;
import com.android.camera.ui.drawable.snap.PaintConditionReferred;
import com.xiaomi.stat.d;
import io.reactivex.Completable;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import miui.view.animation.CubicEaseInInterpolator;
import miui.view.animation.CubicEaseOutInterpolator;
import miui.view.animation.SineEaseOutInterpolator;

public class FragmentBottomAction extends BaseFragment implements OnClickListener, ModeChangeController, ActionProcessing, HandleBeautyRecording, HandlerSwitcher, HandleBackTrace, onModeSelectedListener, SnapListener, BottomMenuProtocol, MoreModePopupController, OnDragListener {
    public static final int FRAGMENT_INFO = 241;
    private static final int MSG_SHOW_PROGRESS = 1;
    private static final String TAG = "FragmentBottomAction";
    /* access modifiers changed from: private */
    public boolean mBackEnable;
    private FrameLayout mBottomActionView;
    /* access modifiers changed from: private */
    public ValueAnimator mBottomAnimator;
    private View mBottomMenuLayout;
    private ViewGroup mBottomParentHorizontal;
    private ViewGroup mBottomParentVertical;
    private ImageView mBottomRecordingCameraPicker;
    private TextView mBottomRecordingTime;
    /* access modifiers changed from: private */
    public boolean mCameraPickEnable;
    /* access modifiers changed from: private */
    public ImageView mCameraPicker;
    private int mCaptureProgressDelay;
    private ComponentModuleList mComponentModuleList;
    private CubicEaseOutInterpolator mCubicEaseOut;
    private int mCurrentBeautyActionMenuType;
    private ViewGroup mCurrentBottomParent;
    private int mCurrentLiveActionMenuType;
    private View mDocumentContainer;
    /* access modifiers changed from: private */
    public DragLayout mDragUpLayout;
    private EdgeHorizonScrollView mEdgeHorizonScrollView;
    private ImageView mExternalModeTipIcon;
    private CapsuleLayout mExternalModeTipLayout;
    private TextView mExternalModeTipText;
    private int mFilterListHeight;
    private FragmentActionMimoji mFragmentActionMimoji;
    private FragmentLighting mFragmentLighting;
    @SuppressLint({"HandlerLeak"})
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                FragmentBottomAction.this.mThumbnailProgress.setVisibility(0);
            }
        }
    };
    private boolean mInLoading;
    private boolean mIsBottomRollDown = false;
    private boolean mIsIntentAction;
    private boolean mIsShowLighting = false;
    private boolean mIsShowMiMoji = false;
    private long mLastPauseTime;
    private boolean mLongPressBurst;
    /* access modifiers changed from: private */
    public ImageView mMimojiBack;
    /* access modifiers changed from: private */
    public ShapeBackGroundView mModeSelectBackgroundLayout;
    private BottomActionMenu mModeSelectLayout;
    private ModeSelectView mModeSelectView;
    private FragmentMoreModePopup mMoreModePopup;
    private int mMoreModeStyle = -1;
    private PopupMenuLayout mPopupMenuLayout;
    /* access modifiers changed from: private */
    public ProgressBar mPostProcess;
    private boolean mPreGifStatus;
    private int mRecordProgressDelay;
    private ImageView mRecordSaveButton;
    /* access modifiers changed from: private */
    public LottieAnimationView mRecordingPause;
    /* access modifiers changed from: private */
    public ImageView mRecordingReverse;
    /* access modifiers changed from: private */
    public ImageView mRecordingSnap;
    private LottieAnimationView mRecordingSwitch;
    /* access modifiers changed from: private */
    public AlertDialog mReverseDialog;
    private List mRotateViews = new ArrayList();
    /* access modifiers changed from: private */
    public CameraSnapView mShutterButton;
    private SineEaseOutInterpolator mSineEaseOut;
    private boolean mSupportOrientation;
    private ImageView mThumbnailImage;
    /* access modifiers changed from: private */
    public ViewGroup mThumbnailImageLayout;
    /* access modifiers changed from: private */
    public ProgressBar mThumbnailProgress;
    private VMFeature mVMFeature;
    /* access modifiers changed from: private */
    public boolean mVideoCaptureEnable;
    /* access modifiers changed from: private */
    public boolean mVideoPauseSupported;
    private boolean mVideoRecordingPaused;
    private boolean mVideoRecordingStarted;
    /* access modifiers changed from: private */
    public boolean mVideoReverseEnable;

    static /* synthetic */ void O000000o(AdjustAnimationView adjustAnimationView, View view) {
        adjustAnimationView.startBackgroundAnimator();
        view.setVisibility(4);
    }

    static /* synthetic */ void O000000o(AdjustAnimationView adjustAnimationView, AnimationView animationView, View view, float f, float f2) {
        adjustAnimationView.setVisibility(4);
        animationView.setVisibility(4);
        view.setVisibility(0);
        animationView.clear();
        animationView.setScaleX(1.0f);
        animationView.setScaleY(1.0f);
        animationView.setX(f);
        animationView.setY(f2);
    }

    static /* synthetic */ void O000000o(AnimationView animationView, ValueAnimator valueAnimator) {
        StringBuilder sb = new StringBuilder();
        sb.append("onAnimationUpdate: ");
        sb.append(valueAnimator.getAnimatedFraction());
        Log.d(TAG, sb.toString());
        animationView.setProgress(valueAnimator.getAnimatedFraction());
    }

    static /* synthetic */ boolean O000000o(UserSelectData userSelectData) {
        return userSelectData.getmRecordLayoutType() == LayoutType.DOWN_FULL;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0040, code lost:
        if (r5 != null) goto L_0x0042;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004b, code lost:
        if (r5 != null) goto L_0x0042;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0058, code lost:
        if (r5 != null) goto L_0x0042;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void animBottomCover(int i, List list, int i2, int i3) {
        ShapeBackGroundView shapeBackGroundView;
        Integer num = (Integer) this.mModeSelectBackgroundLayout.getTag();
        if (num == null || num.intValue() != i3) {
            this.mModeSelectBackgroundLayout.setTag(Integer.valueOf(i3));
            boolean z = false;
            if (i == 204 && i2 == 204 && !CameraSettings.getDualVideoConfig().ismDrawSelectWindow()) {
                this.mModeSelectBackgroundLayout.setMaskSpecificHeight(i3, false);
                return;
            }
            if (i3 > this.mModeSelectBackgroundLayout.getCurrentMaskHeight()) {
                shapeBackGroundView = this.mModeSelectBackgroundLayout;
            } else if (list == null) {
                shapeBackGroundView = this.mModeSelectBackgroundLayout;
            } else if (i2 == 165 && i == 254) {
                shapeBackGroundView = this.mModeSelectBackgroundLayout;
            }
            z = true;
            shapeBackGroundView.setMaskSpecificHeight(i3, z);
        }
    }

    /* access modifiers changed from: private */
    public void assertThumbnailViewRect() {
        ActivityBase activityBase = (ActivityBase) getContext();
        Rect viewRect = activityBase.getThumbnailUpdater().getViewRect();
        if (viewRect == null || viewRect.width() == 0) {
            Rect rect = new Rect();
            this.mThumbnailImageLayout.getGlobalVisibleRect(rect);
            StringBuilder sb = new StringBuilder();
            sb.append("ThumbnailGlobalRect: ");
            sb.append(rect.toString());
            Log.d(TAG, sb.toString());
            activityBase.getThumbnailUpdater().setViewRect(rect);
        }
    }

    private void changeModeByNewMode(int i, String str, int i2, boolean z) {
        if (i == 166 && CameraSettings.isFrontCamera() && C0122O00000o.instance().OOOo00()) {
            i = 176;
        }
        if (i == 163 || i == 165) {
            i = DataRepository.dataItemConfig().getComponentConfigRatio().getMappingModeByRatio(163);
        }
        if (i == 205 && this.mCurrentMode != 188) {
            DataRepository.dataItemRunning().getComponentRunningAIWatermark().resetAIWatermark(true);
        }
        if (i != this.mCurrentMode) {
            if (i == 167 && CameraSettings.isFromProVideoMudule()) {
                i = 180;
            }
            int i3 = this.mCurrentMode;
            if (i3 == 180) {
                CameraSettings.setIsFromProVideoMudule(true);
            } else if (i3 == 167) {
                CameraSettings.setIsFromProVideoMudule(false);
            }
            if (this.mCurrentMode == 163 && CameraSettings.isUltraPixelOn()) {
                ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                if (configChanges != null) {
                    configChanges.switchOffElementsSilent(209);
                }
            }
            if ((i == 174 || i == 183) && !CameraSettings.isLiveModuleClicked()) {
                CameraSettings.setLiveModuleClicked(true);
            }
            DataItemGlobal dataItemGlobal = (DataItemGlobal) DataRepository.provider().dataGlobal();
            if (!isThumbLoading()) {
                CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                if (cameraAction == null || !cameraAction.isDoingAction()) {
                    dataItemGlobal.setCurrentMode(i);
                    if (!z) {
                        ViberatorContext.getInstance(getActivity().getApplicationContext()).performModeSwitch();
                    }
                    ((Camera) getContext()).onModeSelected(StartControl.create(i).setStartDelay(i2).setResetType(4).setViewConfigType(2).setNeedBlurAnimation(true));
                    this.mShutterButton.postDelayed(new O0000OOo(this, str), 500);
                }
            }
        }
    }

    private void checkFeatureState() {
        String featureNameByLocalMode = VMFeature.getFeatureNameByLocalMode(this.mCurrentMode);
        if (!TextUtils.isEmpty(featureNameByLocalMode) && !((MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929)).hasFeatureInstalled(featureNameByLocalMode)) {
            this.mShutterButton.setAlpha(0.5f);
            this.mShutterButton.setTag(null);
            if (this.mVMFeature == null) {
                this.mVMFeature = (VMFeature) DataRepository.dataItemObservable().get(VMFeature.class);
                this.mVMFeature.startObservable(this, new O0000Oo0(this));
            }
        }
    }

    private void findViewByOrientation(boolean z) {
        ViewGroup viewGroup;
        int i;
        this.mCurrentBottomParent = z ? this.mBottomParentHorizontal : this.mBottomParentVertical;
        ViewGroup viewGroup2 = this.mBottomParentHorizontal;
        if (z) {
            viewGroup2.setVisibility(0);
            this.mBottomParentVertical.setVisibility(8);
        } else {
            viewGroup2.setVisibility(8);
            this.mBottomParentVertical.setVisibility(0);
        }
        if (z) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mBottomParentHorizontal.getLayoutParams();
            marginLayoutParams.height = Math.round(((float) Display.getBottomBarHeight()) * 0.7f);
            marginLayoutParams.bottomMargin = Display.getBottomMargin();
            marginLayoutParams.topMargin = Math.round(((float) Display.getBottomBarHeight()) * 0.3f);
            this.mThumbnailImageLayout = (ViewGroup) this.mBottomParentHorizontal.findViewById(R.id.v9_thumbnail_layout_horizontal);
            this.mThumbnailImage = (ImageView) this.mThumbnailImageLayout.findViewById(R.id.v9_thumbnail_image_horizontal);
            this.mThumbnailImageLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    FragmentBottomAction.this.mThumbnailImageLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    FragmentBottomAction.this.assertThumbnailViewRect();
                }
            });
            this.mThumbnailProgress = (ProgressBar) this.mBottomParentHorizontal.findViewById(R.id.v9_recording_progress_horizontal);
            this.mRecordingPause = (LottieAnimationView) this.mBottomParentHorizontal.findViewById(R.id.v9_recording_pause_horizontal);
            this.mShutterButton = (CameraSnapView) this.mBottomParentHorizontal.findViewById(R.id.shutter_button_horizontal);
            this.mCameraPicker = (ImageView) this.mBottomParentHorizontal.findViewById(R.id.v9_camera_picker_horizontal);
            this.mRecordingSnap = (ImageView) this.mBottomParentHorizontal.findViewById(R.id.v9_recording_snap_horizontal);
            this.mRecordingReverse = (ImageView) this.mBottomParentHorizontal.findViewById(R.id.v9_recording_reverse_horizontal);
            this.mRecordingSwitch = (LottieAnimationView) this.mBottomParentHorizontal.findViewById(R.id.v9_capture_recording_switch_horizontal);
            this.mPostProcess = (ProgressBar) this.mBottomParentHorizontal.findViewById(R.id.v9_post_processing_horizontal);
            this.mRecordSaveButton = (ImageView) this.mBottomParentHorizontal.findViewById(R.id.v9_save_processing_horizontal);
            viewGroup = this.mBottomParentHorizontal;
            i = R.id.mimoji_create_back_horizontal;
        } else {
            ((MarginLayoutParams) this.mBottomParentVertical.getLayoutParams()).width = (int) ((((float) Display.getWindowWidth()) - ((((float) Display.getWindowHeight()) / 16.0f) * 9.0f)) / 2.0f);
            this.mThumbnailImageLayout = (ViewGroup) this.mBottomParentVertical.findViewById(R.id.v9_thumbnail_layout_vertical);
            this.mThumbnailImage = (ImageView) this.mBottomParentVertical.findViewById(R.id.v9_thumbnail_image_vertical);
            this.mThumbnailImageLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    FragmentBottomAction.this.mThumbnailImageLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ActivityBase activityBase = (ActivityBase) FragmentBottomAction.this.getContext();
                    Rect rect = new Rect();
                    FragmentBottomAction.this.mThumbnailImageLayout.getGlobalVisibleRect(rect);
                    activityBase.getThumbnailUpdater().setViewRect(rect);
                }
            });
            this.mThumbnailProgress = (ProgressBar) this.mBottomParentVertical.findViewById(R.id.v9_recording_progress_vertical);
            this.mRecordingPause = (LottieAnimationView) this.mBottomParentVertical.findViewById(R.id.v9_recording_pause_vertical);
            this.mShutterButton = (CameraSnapView) this.mBottomParentVertical.findViewById(R.id.shutter_button_vertical);
            this.mCameraPicker = (ImageView) this.mBottomParentVertical.findViewById(R.id.v9_camera_picker_vertical);
            this.mRecordingSnap = (ImageView) this.mBottomParentVertical.findViewById(R.id.v9_recording_snap_vertical);
            this.mRecordingReverse = (ImageView) this.mBottomParentVertical.findViewById(R.id.v9_recording_reverse_vertical);
            this.mRecordingSwitch = (LottieAnimationView) this.mBottomParentVertical.findViewById(R.id.v9_capture_recording_switch_vertical);
            this.mPostProcess = (ProgressBar) this.mBottomParentVertical.findViewById(R.id.v9_post_processing_vertical);
            this.mRecordSaveButton = (ImageView) this.mBottomParentVertical.findViewById(R.id.v9_save_processing_vertical);
            viewGroup = this.mBottomParentVertical;
            i = R.id.mimoji_create_back_vertical;
        }
        this.mMimojiBack = (ImageView) viewGroup.findViewById(i);
    }

    private void hideModeOrExternalTipLayout() {
        View view;
        if (this.mExternalModeTipLayout.getTag() == null || ((Integer) this.mExternalModeTipLayout.getTag()).intValue() != 1) {
            View view2 = this.mModeSelectView;
            this.mDragUpLayout.setDragEnable(false);
            view = view2;
        } else {
            view = this.mExternalModeTipLayout;
        }
        if (view.getVisibility() != 8) {
            view.setEnabled(false);
            FolmeAlphaOutOnSubscribe.directSetResult(view);
        }
    }

    /* access modifiers changed from: private */
    public void initAnimation(AdjustAnimationView adjustAnimationView, AnimationView animationView, View view) {
        if (isAdded()) {
            animationView.clearAnimation();
            RectF locationOnScreen = Util.getLocationOnScreen(animationView);
            float dimension = getResources().getDimension(R.dimen.bottom_picker_width);
            float dimension2 = getResources().getDimension(R.dimen.bottom_picker_margin);
            float windowHeight = ((float) (Display.getWindowHeight() - Display.getBottomHeight())) + (((float) Display.getBottomBarHeight()) * 0.3f) + (((((float) Display.getBottomBarHeight()) * 0.7f) - dimension) / 2.0f);
            RectF rectF = new RectF(dimension2, windowHeight, dimension2 + dimension, dimension + windowHeight);
            animationView.setUp(locationOnScreen);
            float width = rectF.width() / Math.min(locationOnScreen.width(), locationOnScreen.height());
            float x = animationView.getX();
            float y = animationView.getY();
            float width2 = locationOnScreen.left + (locationOnScreen.width() / 2.0f);
            float height = locationOnScreen.top + (locationOnScreen.height() / 2.0f);
            float width3 = rectF.left + (rectF.width() / 2.0f);
            float height2 = rectF.top + (rectF.height() / 2.0f);
            StringBuilder sb = new StringBuilder();
            sb.append("showDocumentReviewViews: startViewBounds = ");
            sb.append(locationOnScreen);
            sb.append(", endViewBounds = ");
            sb.append(rectF);
            sb.append(", scale = ");
            sb.append(width);
            sb.append(", startCenter = ");
            sb.append(width2);
            String str = "x";
            sb.append(str);
            sb.append(height);
            sb.append(", endCenter = ");
            sb.append(width3);
            sb.append(str);
            sb.append(height2);
            Log.d(TAG, sb.toString());
            ViewPropertyAnimator withStartAction = animationView.animate().scaleX(width).scaleY(width).translationX(width3 - width2).translationY(height2 - height).setDuration(Util.getExitDuration()).setInterpolator(new FastOutSlowInInterpolator()).setStartDelay(Util.getSuspendDuration()).setUpdateListener(new O000000o(animationView)).withStartAction(new C0295O00000oo(adjustAnimationView, view));
            O00000o0 o00000o0 = new O00000o0(adjustAnimationView, animationView, view, x, y);
            withStartAction.withEndAction(o00000o0).start();
        }
    }

    private void initThumbLayout() {
        if (!shouldUseThumbnailAsExit()) {
            initThumbnailAsThumbnail(false);
        } else {
            initThumbnailAsExit();
        }
    }

    private void initThumbnailAsExit() {
        Log.d(TAG, "initThumbnailAsExit: ");
        ((MarginLayoutParams) this.mThumbnailImage.getLayoutParams()).setMargins(0, 0, 0, 0);
        this.mThumbnailImage.setScaleType(ScaleType.CENTER);
        this.mThumbnailImage.setImageResource(R.drawable.ic_vector_close_fill);
        if (Util.isAccessible()) {
            this.mThumbnailImage.setContentDescription(getString(R.string.accessibility_live_edit_exit_button));
        }
    }

    private void initThumbnailAsThumbnail(boolean z) {
        Log.d(TAG, "initThumbnailAsThumbnail: ");
        this.mThumbnailImage.setScaleType(ScaleType.CENTER_CROP);
        if (z) {
            this.mThumbnailImage.setImageResource(0);
        }
        if (Util.isAccessible()) {
            this.mThumbnailImage.setContentDescription(getString(R.string.accessibility_review_thumbnail));
        }
        ActivityBase activityBase = (ActivityBase) getContext();
        if ((activityBase.startFromSecureKeyguard() || activityBase.isGalleryLocked()) && !activityBase.isJumpBack()) {
            activityBase.getThumbnailUpdater().setThumbnail(null, true, false);
            return;
        }
        if (PermissionManager.checkStoragePermissions()) {
            activityBase.getThumbnailUpdater().getLastThumbnail();
        }
    }

    private boolean isFeatureEnable() {
        return this.mShutterButton.getAlpha() == 1.0f;
    }

    private boolean isFocusOrZoomMoving() {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        return mainContentProtocol != null && (mainContentProtocol.isFocusViewMoving() || mainContentProtocol.isZoomViewMoving());
    }

    private boolean isThumbLoading() {
        return this.mInLoading;
    }

    private void onInstallStateChanged(HashMap hashMap) {
        if (isAdded()) {
            for (Entry entry : hashMap.entrySet()) {
                if (((String) entry.getKey()).equals(VMFeature.getFeatureNameByLocalMode(this.mCurrentMode))) {
                    int intValue = ((Integer) entry.getValue()).intValue();
                    if (VMFeature.getScope(intValue) == 16) {
                        if (intValue == 21) {
                            this.mShutterButton.setAlpha(1.0f);
                            this.mShutterButton.setTag(Integer.valueOf(1));
                        }
                    }
                }
            }
        }
    }

    private void setPausePlaySwitchTarget(boolean z) {
        this.mRecordingPause.setScale(0.38f);
        this.mRecordingPause.O0000O0o(z ? R.raw.switch_pause_play : R.raw.switch_play_pause);
        this.mRecordingPause.setProgress(1.0f);
    }

    private void setProgressBarVisible(int i) {
        ValueAnimator valueAnimator;
        Resources resources;
        int i2;
        if (this.mPostProcess.getVisibility() != i) {
            if (i == 0) {
                this.mPostProcess.setAlpha(0.0f);
                if (this.mShutterButton.isBottomVisible()) {
                    resources = getResources();
                    i2 = R.drawable.post_process_progress_gray;
                } else {
                    resources = getResources();
                    i2 = R.drawable.post_process_progress_white;
                }
                Drawable drawable = resources.getDrawable(i2);
                if (this.mPostProcess.getIndeterminateDrawable() != null) {
                    drawable.setBounds(this.mPostProcess.getIndeterminateDrawable().getBounds());
                }
                this.mPostProcess.setIndeterminateDrawable(drawable);
                this.mPostProcess.setVisibility(0);
                valueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                valueAnimator.setDuration(300);
                valueAnimator.setStartDelay(160);
                valueAnimator.setInterpolator(new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f));
                valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        Float f = (Float) valueAnimator.getAnimatedValue();
                        FragmentBottomAction.this.mPostProcess.setAlpha(f.floatValue());
                        FragmentBottomAction.this.mPostProcess.setScaleX((f.floatValue() * 0.1f) + 0.9f);
                        FragmentBottomAction.this.mPostProcess.setScaleY((f.floatValue() * 0.1f) + 0.9f);
                    }
                });
            } else {
                valueAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
                valueAnimator.setDuration(300);
                valueAnimator.setInterpolator(new CubicEaseInInterpolator());
                valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        FragmentBottomAction.this.mPostProcess.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    }
                });
                valueAnimator.addListener(new AnimatorListener() {
                    public void onAnimationCancel(Animator animator) {
                        FragmentBottomAction.this.mPostProcess.setVisibility(8);
                    }

                    public void onAnimationEnd(Animator animator) {
                        FragmentBottomAction.this.mPostProcess.setVisibility(8);
                    }

                    public void onAnimationRepeat(Animator animator) {
                    }

                    public void onAnimationStart(Animator animator) {
                    }
                });
            }
            valueAnimator.start();
        }
    }

    private void setRecordingSwitchTarget(boolean z) {
        this.mRecordingSwitch.setScale(CameraSettings.getResourceFloat(R.dimen.switch_capture_record_scale_size, 0.35f));
        this.mRecordingSwitch.O0000O0o(z ? R.raw.switch_capture_record : R.raw.switch_record_capture);
        this.mRecordingSwitch.setProgress(1.0f);
    }

    private void setRecordingTimeState(int i) {
        CompletableOnSubscribe completableOnSubscribe;
        if (i == 1) {
            completableOnSubscribe = new AlphaInOnSubscribe(this.mBottomRecordingTime);
        } else if (i == 2) {
            if (this.mBottomRecordingTime.getVisibility() == 0) {
                Completable.create(new AlphaOutOnSubscribe(this.mBottomRecordingTime)).subscribe();
            }
            if (this.mBottomRecordingCameraPicker.getVisibility() == 0) {
                completableOnSubscribe = new AlphaOutOnSubscribe(this.mBottomRecordingCameraPicker);
            } else {
                return;
            }
        } else if (i == 3) {
            completableOnSubscribe = new AlphaInOnSubscribe(this.mBottomRecordingCameraPicker);
        } else if (i == 4 && this.mBottomRecordingTime.getVisibility() != 0) {
            this.mBottomRecordingTime.setVisibility(0);
            return;
        } else {
            return;
        }
        Completable.create(completableOnSubscribe).subscribe();
    }

    private boolean shouldUseThumbnailAsExit() {
        return this.mIsIntentAction || (this.mCurrentMode == 204 && !DataRepository.dataItemRunning().getComponentRunningDualVideo().ismDrawSelectWindow() && C0122O00000o.instance().OOO000o());
    }

    private void showModeOrExternalTipLayout(boolean z) {
        View view;
        if (this.mExternalModeTipLayout.getTag() == null || ((Integer) this.mExternalModeTipLayout.getTag()).intValue() != 1) {
            View view2 = this.mModeSelectView;
            switchMoreMode(true);
            view = view2;
        } else {
            view = this.mExternalModeTipLayout;
        }
        view.setEnabled(true);
        if (view.getVisibility() != 0) {
            if (z) {
                Completable.create(new FolmeAlphaInOnSubscribe(view)).subscribe();
            } else {
                FolmeAlphaInOnSubscribe.directSetResult(view);
            }
        }
    }

    private void showNormalMimoji2Bottom() {
        MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
        if (!mimojiStatusManager2.isInMimojiPreviewPlay()) {
            this.mShutterButton.onForceVideoStateChange(PaintConditionReferred.create(184));
            if (CameraSettings.isGifOn()) {
                mimojiStatusManager2.setMimojiRecordState(1);
                animateViews(-1, (List) null, (View) this.mRecordingSwitch);
            } else {
                animateViews(1, (List) null, (View) this.mRecordingSwitch);
            }
            animateViews(-1, (List) null, (View) this.mCameraPicker);
            animateViews(1, (List) null, this.mModeSelectLayout.getView());
            animateViews(1, (List) null, (View) this.mShutterButton);
            animateViews(-1, (List) null, (View) this.mMimojiBack);
            if (this.mIsShowMiMoji) {
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                if (bottomPopupTips != null) {
                    bottomPopupTips.showMimojiPanel(0);
                }
            }
        }
    }

    private void showReverseConfirmDialog() {
        if (this.mReverseDialog == null) {
            if (this.mCurrentMode == 174) {
                CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_CLICK_REVERSE);
            }
            this.mReverseDialog = RotateDialogController.showSystemAlertDialog(getContext(), null, getString(R.string.live_reverse_message), getString(R.string.live_reverse_confirm), new Runnable() {
                public void run() {
                    FragmentBottomAction.this.mShutterButton.removeLastSegment();
                    if (FragmentBottomAction.this.mCurrentMode == 174) {
                        CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_CLICK_REVERSE_CONFIRM);
                    }
                    String str = FragmentBottomAction.TAG;
                    Log.u(str, "showReverseConfirmDialog onClick positive");
                    ActivityBase activityBase = (ActivityBase) FragmentBottomAction.this.getContext();
                    if (activityBase == null || (!(activityBase.getCurrentModule() instanceof LiveModule) && !(activityBase.getCurrentModule() instanceof MiLiveModule))) {
                        Log.w(str, "showReverseConfirmDialog skip!!!");
                    } else {
                        ((ILiveModule) activityBase.getCurrentModule()).doReverse();
                    }
                }
            }, null, null, getString(R.string.snap_cancel), O00000Oo.INSTANCE);
            this.mReverseDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    FragmentBottomAction.this.mReverseDialog = null;
                }
            });
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x005f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void switchModeSelectViewStyle(int i) {
        Resources resources;
        int i2;
        if (getContext() != null) {
            int moreModeStyle = CameraSettings.getMoreModeStyle();
            if (moreModeStyle != this.mMoreModeStyle) {
                this.mMoreModeStyle = moreModeStyle;
                LayoutParams layoutParams = (LayoutParams) this.mModeSelectView.getLayoutParams();
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mBottomMenuLayout.getLayoutParams();
                int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.mode_select_layout_height);
                if (moreModeStyle != 0) {
                    if (moreModeStyle == 1) {
                        resources = getContext().getResources();
                        i2 = R.dimen.mode_select_popup_style_margin_top;
                    }
                    if (this.mModeSelectView.getAdapter() != null) {
                        this.mModeSelectView.getAdapter().notifyDataSetChanged();
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("switchModeSelectViewStyle f = ");
                    sb.append(this.mComponentModuleList.getCommonItems().size());
                    sb.append(", m = ");
                    sb.append(this.mComponentModuleList.getMoreItems().size());
                    Log.d(TAG, sb.toString());
                }
                resources = getContext().getResources();
                i2 = R.dimen.mode_select_margin_top;
                layoutParams.topMargin = resources.getDimensionPixelOffset(i2);
                marginLayoutParams.height = dimensionPixelSize + (layoutParams.topMargin * 2);
                if (this.mModeSelectView.getAdapter() != null) {
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("switchModeSelectViewStyle f = ");
                sb2.append(this.mComponentModuleList.getCommonItems().size());
                sb2.append(", m = ");
                sb2.append(this.mComponentModuleList.getMoreItems().size());
                Log.d(TAG, sb2.toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public void switchMoreMode(boolean z) {
        DragLayout.getAnimationConfig().calDragLayoutHeight(getContext(), this.mComponentModuleList.getMoreItems().size());
        ((MarginLayoutParams) this.mDragUpLayout.getDragChildren().getLayoutParams()).height = Display.getBottomHeight() + ((int) DragLayout.getAnimationConfig().getMaxDragDistance());
        int moreModeStyle = CameraSettings.getMoreModeStyle();
        if (moreModeStyle != 1 || !this.mComponentModuleList.isCommonMode(this.mCurrentMode) || !z) {
            FragmentMoreModePopup fragmentMoreModePopup = this.mMoreModePopup;
            if (fragmentMoreModePopup != null && fragmentMoreModePopup.isAdded()) {
                this.mMoreModePopup.unRegisterProtocol();
                ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).getAnimationComposite().remove(this.mMoreModePopup.getFragmentInto());
                FragmentUtils.removeFragmentByTag(getChildFragmentManager(), this.mMoreModePopup.getFragmentTag());
                this.mDragUpLayout.removeOnDragListener(this.mMoreModePopup);
            }
            this.mDragUpLayout.setDragEnable(false);
        } else {
            if (this.mMoreModePopup == null) {
                this.mMoreModePopup = new FragmentMoreModePopup();
            }
            BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
            if (!this.mMoreModePopup.isAdded()) {
                this.mMoreModePopup.registerProtocol();
                FragmentManager childFragmentManager = getChildFragmentManager();
                FragmentMoreModePopup fragmentMoreModePopup2 = this.mMoreModePopup;
                FragmentUtils.addFragmentWithTag(childFragmentManager, (int) R.id.popup_more_mode_layout, (Fragment) fragmentMoreModePopup2, fragmentMoreModePopup2.getFragmentTag());
                baseDelegate.getAnimationComposite().put(this.mMoreModePopup.getFragmentInto(), this.mMoreModePopup);
                this.mDragUpLayout.addOnDragListener(this.mMoreModePopup);
            }
            this.mMoreModePopup.setSupportOrientation(this.mSupportOrientation);
            if (this.mSupportOrientation) {
                this.mMoreModePopup.setDegree(0);
            } else {
                this.mMoreModePopup.setDegree(baseDelegate.getAnimationComposite().getTargetDegree());
            }
            this.mDragUpLayout.setDragEnable(true);
        }
        if (moreModeStyle != 1) {
            this.mMoreModePopup = null;
        }
    }

    /* JADX WARNING: type inference failed for: r3v0 */
    /* JADX WARNING: type inference failed for: r3v1, types: [int] */
    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r3v8 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v3
  assigns: []
  uses: []
  mth insns count: 78
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
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:28:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void switchVideoCapture(View view) {
        ? r3;
        Camera camera;
        StartControl startControl;
        Camera camera2;
        StartControl startControl2;
        StartControl startControl3;
        ? r32;
        ViberatorContext.getInstance(getContext()).performSwitchCamera();
        int i = this.mCurrentMode;
        ? r33 = 2131755990;
        if (i == 167) {
            DataRepository.dataItemGlobal().setCurrentMode(180);
            camera = (Camera) getContext();
            startControl = StartControl.create(180).setResetType(4);
        } else if (i != 173) {
            if (i == 180) {
                DataRepository.dataItemGlobal().setCurrentMode(167);
                camera2 = (Camera) getContext();
                startControl3 = StartControl.create(167).setResetType(4);
            } else if (i == 184) {
                MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
                if (mimojiStatusManager2.getMimojiRecordState() == 0) {
                    mimojiStatusManager2.setMimojiRecordState(1);
                    mimojiStatusManager2.setMimojiRecordStateFromGif(1);
                    r32 = R.string.module_name_video;
                } else {
                    mimojiStatusManager2.setMimojiRecordState(0);
                    mimojiStatusManager2.setMimojiRecordStateFromGif(0);
                    r32 = r33;
                }
                camera2 = (Camera) getContext();
                startControl2 = StartControl.create(this.mCurrentMode).setResetType(2).setViewConfigType(2).setNeedBlurAnimation(true);
                r33 = r32;
                camera2.onModeSelected(startControl2);
                r3 = r33;
                this.mRecordingSwitch.O0000oO();
                if (Util.isAccessible()) {
                    return;
                }
                return;
            } else if (i != 214) {
                r3 = 0;
                this.mRecordingSwitch.O0000oO();
                if (Util.isAccessible() && isAdded() && r3 > 0) {
                    this.mShutterButton.announceForAccessibility(getString(R.string.accessibility_module_picker_finish, getString(r3)));
                    return;
                }
                return;
            } else {
                DataRepository.dataItemGlobal().setCurrentMode(173);
                camera2 = (Camera) getContext();
                startControl3 = StartControl.create(173).setResetType(5);
            }
            startControl2 = startControl3.setNeedBlurAnimation(true).setViewConfigType(2);
            camera2.onModeSelected(startControl2);
            r3 = r33;
            this.mRecordingSwitch.O0000oO();
            if (Util.isAccessible()) {
            }
        } else {
            DataRepository.dataItemGlobal().setCurrentMode(214);
            camera = (Camera) getContext();
            startControl = StartControl.create(214).setResetType(5);
        }
        camera.onModeSelected(startControl.setNeedBlurAnimation(true).setViewConfigType(2));
        r3 = R.string.module_name_video;
        this.mRecordingSwitch.O0000oO();
        if (Util.isAccessible()) {
        }
    }

    private void updateBottomInRecording(final boolean z, boolean z2) {
        int i = -1;
        if (z) {
            this.mHandler.removeMessages(1);
            if (this.mThumbnailProgress.getVisibility() != 8) {
                this.mThumbnailProgress.setVisibility(8);
            }
            this.mRecordingSwitch.cancelAnimation();
            animateViews(-1, (List) null, (View) this.mRecordingSwitch);
        }
        final boolean z3 = this.mCurrentMode == 165 && CameraSettings.isTimerBurstEnable() && DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting();
        int i2 = this.mCurrentMode;
        final boolean z4 = i2 == 208 || i2 == 207;
        int i3 = this.mCurrentMode;
        if (i3 != 161) {
            if (i3 != 162) {
                if (!(i3 == 169 || i3 == 172)) {
                    if (i3 != 174) {
                        if (!(i3 == 204 || i3 == 207)) {
                            if (i3 != 214) {
                                if (i3 != 179) {
                                    if (i3 != 180) {
                                        if (i3 != 183) {
                                            if (i3 != 184) {
                                                this.mVideoPauseSupported = false;
                                                this.mVideoCaptureEnable = false;
                                                this.mVideoReverseEnable = false;
                                                this.mBackEnable = false;
                                            } else {
                                                this.mCameraPickEnable = false;
                                                this.mVideoPauseSupported = false;
                                                this.mVideoCaptureEnable = false;
                                                this.mVideoReverseEnable = false;
                                                if (DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate()) {
                                                    this.mBackEnable = true;
                                                    animateViews(-1, (List) null, (View) this.mRecordingSwitch);
                                                } else {
                                                    this.mBackEnable = false;
                                                    this.mCameraPickEnable = false;
                                                }
                                            }
                                        } else if (C0122O00000o.instance().OOOoOo0()) {
                                            this.mVideoCaptureEnable = true;
                                            this.mVideoPauseSupported = true;
                                            this.mVideoReverseEnable = true;
                                            this.mBackEnable = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    this.mVideoCaptureEnable = false;
                    this.mVideoPauseSupported = true;
                    this.mVideoReverseEnable = true;
                    this.mBackEnable = false;
                }
                this.mVideoReverseEnable = false;
                this.mVideoCaptureEnable = false;
                this.mVideoPauseSupported = false;
                this.mBackEnable = false;
            }
            if (!DataRepository.dataItemGlobal().isIntentAction()) {
                boolean z5 = CameraSettings.isVideoCaptureVisible() && !CameraSettings.isHdr10Alive(this.mCurrentMode) && !CameraSettings.isHdr10PlusAlive(this.mCurrentMode) && (!CameraSettings.isMasterFilterOn(this.mCurrentMode) || !DataRepository.dataItemRunning().isSwitchOn("pref_camera_peak_key") || !Camera2DataContainer.getInstance().getCurrentCameraCapabilities().isSupportVideoBeautyScreenshot());
                this.mVideoCaptureEnable = z5;
            }
            if (this.mCurrentMode == 207 && !z) {
                this.mCameraPickEnable = false;
            }
            boolean z6 = C0124O00000oO.Oo0O00O() && !CameraSettings.isVideoBokehOn();
            this.mVideoPauseSupported = z6;
            this.mVideoReverseEnable = false;
            this.mBackEnable = false;
        } else {
            this.mVideoPauseSupported = false;
            this.mVideoReverseEnable = false;
            this.mBackEnable = false;
            if (C0122O00000o.instance().OOOoOo0()) {
                this.mVideoCaptureEnable = true;
            } else {
                this.mVideoCaptureEnable = false;
            }
        }
        if (z) {
            if (this.mVideoCaptureEnable) {
                this.mRecordingSnap.setImageResource(R.drawable.ic_vector_recording_snap);
                this.mRecordingSnap.setSoundEffectsEnabled(false);
                this.mRecordingSnap.setVisibility(0);
                ViewCompat.setAlpha(this.mRecordingSnap, 0.0f);
            }
            if (this.mVideoPauseSupported) {
                setPausePlaySwitchTarget(false);
                this.mRecordingPause.setSoundEffectsEnabled(false);
                this.mRecordingPause.setVisibility(0);
                ViewCompat.setAlpha(this.mRecordingPause, 0.0f);
            }
            if (this.mVideoPauseSupported) {
                this.mRecordingReverse.setImageResource(R.drawable.ic_vector_reverse);
                this.mRecordingReverse.setSoundEffectsEnabled(false);
                this.mRecordingReverse.setVisibility(8);
            }
        }
        if (!z) {
            i = 1;
        }
        animateViews(i, (List) null, this.mModeSelectLayout.getView());
        ValueAnimator valueAnimator = this.mBottomAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mBottomAnimator.cancel();
        }
        this.mBottomAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mBottomAnimator.setDuration(z2 ? 200 : 0);
        this.mBottomAnimator.setInterpolator(new CubicEaseOutInterpolator() {
            public float getInterpolation(float f) {
                float interpolation = super.getInterpolation(f);
                float f2 = 0.0f;
                if (!z3) {
                    ShapeBackGroundView access$1200 = FragmentBottomAction.this.mModeSelectBackgroundLayout;
                    float f3 = z ? 1.0f - interpolation : FragmentBottomAction.this.mModeSelectBackgroundLayout.getAlpha() == 0.0f ? interpolation : 1.0f;
                    ViewCompat.setAlpha(access$1200, f3);
                }
                if (!z4) {
                    ViewGroup access$100 = FragmentBottomAction.this.mThumbnailImageLayout;
                    float f4 = z ? 1.0f - interpolation : FragmentBottomAction.this.mThumbnailImageLayout.getAlpha() == 0.0f ? interpolation : 1.0f;
                    ViewCompat.setAlpha(access$100, f4);
                }
                if (FragmentBottomAction.this.mVideoPauseSupported) {
                    ViewCompat.setAlpha(FragmentBottomAction.this.mRecordingPause, z ? interpolation : 1.0f - interpolation);
                }
                if (FragmentBottomAction.this.mCurrentMode == 204 && C0122O00000o.instance().OOO000o()) {
                    FragmentBottomAction.this.mCameraPicker.setAlpha(0.0f);
                } else if (FragmentBottomAction.this.mCameraPickEnable) {
                    ViewCompat.setAlpha(FragmentBottomAction.this.mCameraPicker, z ? 0.0f : 1.0f);
                }
                if (FragmentBottomAction.this.mVideoCaptureEnable) {
                    ImageView access$700 = FragmentBottomAction.this.mRecordingSnap;
                    if (z) {
                        f2 = 1.0f;
                    }
                    ViewCompat.setAlpha(access$700, f2);
                }
                if (FragmentBottomAction.this.mVideoReverseEnable) {
                    ViewCompat.setAlpha(FragmentBottomAction.this.mRecordingReverse, z ? interpolation : 1.0f - interpolation);
                }
                if (FragmentBottomAction.this.mBackEnable) {
                    ViewCompat.setAlpha(FragmentBottomAction.this.mMimojiBack, z ? interpolation : 1.0f - interpolation);
                }
                return interpolation;
            }
        });
        this.mBottomAnimator.addListener(new AnimatorListenerAdapter() {
            private boolean isMultiCameraDualVideo() {
                return FragmentBottomAction.this.mCurrentMode == 204 && C0122O00000o.instance().OOO000o();
            }

            public void onAnimationEnd(Animator animator) {
                if (FragmentBottomAction.this.canProvide()) {
                    if (FragmentBottomAction.this.mVideoPauseSupported) {
                        FragmentBottomAction.this.mRecordingPause.setVisibility(z ? 0 : 8);
                    }
                    if (FragmentBottomAction.this.mVideoCaptureEnable) {
                        FragmentBottomAction.this.mRecordingSnap.setVisibility(z ? 0 : 8);
                    }
                    if (FragmentBottomAction.this.mVideoReverseEnable && !z) {
                        FragmentBottomAction.this.mRecordingReverse.setVisibility(8);
                    }
                    if (FragmentBottomAction.this.mBackEnable) {
                        FragmentBottomAction.this.animateViews(z ? 1 : -1, (List) null, (View) FragmentBottomAction.this.mMimojiBack);
                    }
                    if (z) {
                        FragmentBottomAction fragmentBottomAction = FragmentBottomAction.this;
                        fragmentBottomAction.animateViews(-1, (List) null, (View) fragmentBottomAction.mThumbnailImageLayout);
                        if (FragmentBottomAction.this.mCameraPickEnable) {
                            FragmentBottomAction fragmentBottomAction2 = FragmentBottomAction.this;
                            fragmentBottomAction2.animateViews(-1, (List) null, (View) fragmentBottomAction2.mCameraPicker);
                        }
                    }
                    boolean z = z;
                    FragmentBottomAction fragmentBottomAction3 = FragmentBottomAction.this;
                    if (z) {
                        fragmentBottomAction3.mDragUpLayout.setDragEnable(false);
                    } else {
                        fragmentBottomAction3.switchMoreMode(true);
                    }
                }
            }

            public void onAnimationStart(Animator animator) {
                if (!z) {
                    if (FragmentBottomAction.this.mCameraPickEnable && !isMultiCameraDualVideo()) {
                        FragmentBottomAction fragmentBottomAction = FragmentBottomAction.this;
                        fragmentBottomAction.animateViews(1, (List) null, (View) fragmentBottomAction.mCameraPicker);
                    }
                    if (!z4) {
                        FragmentBottomAction fragmentBottomAction2 = FragmentBottomAction.this;
                        fragmentBottomAction2.animateViews(1, (List) null, (View) fragmentBottomAction2.mThumbnailImageLayout);
                    }
                }
            }
        });
        this.mBottomAnimator.start();
    }

    public /* synthetic */ void O00000o0(DataWrap dataWrap) {
        onInstallStateChanged((HashMap) dataWrap.get());
    }

    public /* synthetic */ void O0000OoO(int i) {
        if (isAdded()) {
            this.mShutterButton.announceForAccessibility(getString(i));
        }
    }

    public /* synthetic */ void O0000o0(String str) {
        if (isAdded()) {
            this.mShutterButton.announceForAccessibility(getString(R.string.accessibility_module_picker_finish, str));
        }
    }

    public /* synthetic */ void O000oO() {
        if (this.mModeSelectView.getAdapter() != null) {
            this.mModeSelectView.getAdapter().notifyDataSetChanged();
            this.mModeSelectView.moveToPosition(this.mCurrentMode);
        }
    }

    public void adjustViewBackground() {
        int uiStyle = DataRepository.dataItemRunning().getUiStyle();
        if (uiStyle == 1 || uiStyle == 3) {
            this.mCameraPicker.setBackgroundResource(R.drawable.bg_thumbnail_background_full);
            this.mRecordingSwitch.setBackgroundResource(R.drawable.bg_thumbnail_background_full);
            this.mMimojiBack.setBackgroundResource(R.drawable.bg_thumbnail_background_full);
            this.mThumbnailImage.setBackgroundResource(R.drawable.bg_thumbnail_background_full);
        }
        if (this.mCurrentMode == 204) {
            boolean z = CameraSettings.getDualVideoConfig().getRecordType() == RecordType.STANDALONE;
            boolean anyMatch = CameraSettings.getDualVideoConfig().getSelectedData().stream().anyMatch(O00000o.INSTANCE);
            if (!z || !anyMatch) {
                setBackgroundColor(2);
            } else {
                setBackgroundColor(1);
            }
        } else {
            this.mRecordingPause.setBackgroundResource(R.drawable.bg_thumbnail_background_full);
            this.mRecordingSnap.setBackgroundResource(R.drawable.bg_thumbnail_background_full);
        }
    }

    public void animBottomBlackCover() {
        if (((Integer) this.mModeSelectBackgroundLayout.getTag()).intValue() != this.mModeSelectBackgroundLayout.getCurrentMaskHeight()) {
            ShapeBackGroundView shapeBackGroundView = this.mModeSelectBackgroundLayout;
            shapeBackGroundView.setMaskSpecificHeight(((Integer) shapeBackGroundView.getTag()).intValue(), true);
        }
    }

    public void animateShineBeauty(boolean z) {
        hideModeOrExternalTipLayout();
        this.mModeSelectLayout.animateShineBeauty(z);
    }

    public void announceForAccessibility(@StringRes int i) {
        if (Util.isAccessible()) {
            this.mShutterButton.postDelayed(new O0000O0o(this, i), 100);
        }
    }

    public boolean canSnap() {
        boolean z = false;
        if (!isFeatureEnable()) {
            return false;
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null && !cameraAction.isBlockSnap()) {
            z = true;
        }
        return z;
    }

    public boolean canSwipeChangeMode() {
        if (!this.mVideoRecordingStarted && ((DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiPreview() || DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiPreview()) && isVisible())) {
            ComponentModuleList componentModuleList = this.mComponentModuleList;
            if (componentModuleList != null && componentModuleList.isCommonMode(this.mCurrentMode) && !isThumbLoading() && !isShowLightingView() && !isFocusOrZoomMoving()) {
                return true;
            }
        }
        return false;
    }

    public boolean catchDrag(int i, int i2) {
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction == null || cameraAction.isDoingAction()) {
            return true;
        }
        if (this.mBottomActionView.getVisibility() != 0) {
            return false;
        }
        if (this.mBottomActionView.getVisibility() == 0 && this.mShutterButton.inRegion(i, i2)) {
            return true;
        }
        if (this.mThumbnailImageLayout.getVisibility() == 0 && Util.isInViewRegion(this.mThumbnailImageLayout, i, i2)) {
            return true;
        }
        if (this.mCameraPicker.getVisibility() != 0 || !Util.isInViewRegion(this.mCameraPicker, i, i2)) {
            return this.mRecordingSwitch.getVisibility() == 0 && Util.isInViewRegion(this.mRecordingSwitch, i, i2);
        }
        return true;
    }

    public void changeCamera(View... viewArr) {
        StartControl startControl;
        Camera camera;
        StartControl startControl2;
        Camera camera2;
        StartControl startControl3;
        StartControl startControl4;
        Camera camera3;
        StartControl startControl5;
        int[] iArr;
        ViberatorContext.getInstance(getContext()).performSwitchCamera();
        DataItemGlobal dataItemGlobal = (DataItemGlobal) DataRepository.provider().dataGlobal();
        int currentCameraId = dataItemGlobal.getCurrentCameraId();
        int i = currentCameraId == 0 ? 1 : 0;
        if (this.mCurrentMode == 163 && CameraSettings.isUltraPixelOn()) {
            ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).switchOffElementsSilent(209);
        }
        CameraSettings.resetRetainZoom();
        dataItemGlobal.setCameraId(i);
        if (viewArr != null && viewArr.length > 0) {
            for (View animate : viewArr) {
                ViewCompat.animate(animate).rotationBy(i == 1 ? -180.0f : 180.0f).setDuration(300).start();
            }
        }
        String str = "switch camera from %d to %d, for module 0x%x";
        String format = String.format(Locale.ENGLISH, str, new Object[]{Integer.valueOf(currentCameraId), Integer.valueOf(i), Integer.valueOf(this.mCurrentMode)});
        String str2 = TAG;
        Log.u(str2, format);
        Log.k(3, str2, String.format(Locale.ENGLISH, str, new Object[]{Integer.valueOf(currentCameraId), Integer.valueOf(i), Integer.valueOf(this.mCurrentMode)}));
        ScenarioTrackUtil.trackSwitchCameraStart(currentCameraId == 1, i == 1, this.mCurrentMode);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        topAlert.removeExtraMenu(4);
        if (Util.isAccessible() && isAdded()) {
            this.mShutterButton.announceForAccessibility(getString(R.string.accessibility_camera_picker_finish));
        }
        int i2 = this.mCurrentMode;
        if (i2 != 162) {
            if (i2 != 166) {
                if (i2 != 169) {
                    if (i2 == 184) {
                        MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
                        if (mimojiAvatarEngine2 != null && mimojiAvatarEngine2.isOnCreateMimoji()) {
                            ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166)).mimojiStart();
                            iArr = new int[]{197, 193, 162};
                        }
                        ((Camera) getContext()).onModeSelected(StartControl.create(this.mCurrentMode).setResetType(5).setNeedBlurAnimation(true).setViewConfigType(2));
                        return;
                    } else if (i2 == 207) {
                        DataRepository.dataItemGlobal().setCurrentMode(207);
                        camera2 = (Camera) getContext();
                        startControl2 = StartControl.create(207);
                    } else if (i2 == 176) {
                        DataRepository.dataItemGlobal().setCurrentMode(166);
                        camera3 = (Camera) getContext();
                        startControl4 = StartControl.create(166);
                    } else if (i2 != 177) {
                        if (i2 == 163 || i2 == 165) {
                            i2 = DataRepository.dataItemConfig().getComponentConfigRatio().getMappingModeByRatio(163);
                            DataRepository.dataItemGlobal().setCurrentMode(i2);
                            if (viewArr != null) {
                                int length = viewArr.length;
                                for (int i3 = 0; i3 < length; i3++) {
                                    View view = viewArr[i3];
                                    if (view != null && view.getId() == this.mCameraPicker.getId()) {
                                        ComponentRunningAIWatermark componentRunningAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
                                        boolean z = currentCameraId != 0 ? currentCameraId != 1 ? false : componentRunningAIWatermark.getEnable(0) : componentRunningAIWatermark.getEnable(1);
                                        if (z) {
                                            i2 = 163;
                                        }
                                    }
                                }
                            }
                        }
                        camera = (Camera) getContext();
                        startControl5 = StartControl.create(i2);
                    } else {
                        MimojiAvatarEngine mimojiAvatarEngine = (MimojiAvatarEngine) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
                        if (mimojiAvatarEngine != null && mimojiAvatarEngine.isOnCreateMimoji()) {
                            ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166)).mimojiStart();
                            iArr = new int[]{197, 193};
                        }
                        ((Camera) getContext()).onModeSelected(StartControl.create(this.mCurrentMode).setResetType(5).setNeedBlurAnimation(true).setViewConfigType(2));
                        return;
                    }
                    topAlert.disableMenuItem(true, iArr);
                    ((Camera) getContext()).onModeSelected(StartControl.create(this.mCurrentMode).setResetType(5).setNeedBlurAnimation(true).setViewConfigType(2));
                    return;
                }
                DataRepository.dataItemGlobal().setCurrentMode(169);
                camera = (Camera) getContext();
                startControl5 = StartControl.create(169);
                startControl3 = startControl5.setResetType(5);
                startControl = startControl3.setViewConfigType(2).setNeedBlurAnimation(true);
                camera.onModeSelected(startControl);
            }
            DataRepository.dataItemGlobal().setCurrentMode(176);
            camera3 = (Camera) getContext();
            startControl4 = StartControl.create(176);
            startControl3 = startControl4.setResetType(4);
            startControl = startControl3.setViewConfigType(2).setNeedBlurAnimation(true);
            camera.onModeSelected(startControl);
        }
        DataRepository.dataItemGlobal().setCurrentMode(162);
        camera2 = (Camera) getContext();
        startControl2 = StartControl.create(162);
        startControl = startControl2.setResetType(5).setNeedBlurAnimation(true).setViewConfigType(2);
        camera.onModeSelected(startControl);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0021, code lost:
        if (r8 == 5) goto L_0x0018;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001a, code lost:
        if (r8 == 5) goto L_0x001f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0047 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0044 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void changeModeByGravity(int i, int i2) {
        int size;
        int i3;
        if (i != -1) {
            if (!Util.isLayoutRTL(getContext())) {
                if (i != 3) {
                }
                i = 8388611;
                int transferredMode = ComponentModuleList.getTransferredMode(this.mCurrentMode);
                size = this.mComponentModuleList.getCommonItems().size();
                i3 = 0;
                while (true) {
                    if (i3 < size) {
                        i3 = 0;
                        break;
                    } else if (this.mComponentModuleList.getMode(i3) == transferredMode) {
                        break;
                    } else {
                        i3++;
                    }
                }
                if (i == 8388611) {
                    if (i == 8388613 && i3 < size - 1) {
                        i3++;
                    }
                } else if (i3 > 0) {
                    i3--;
                }
                changeModeByNewMode(this.mComponentModuleList.getMode(i3), getString(this.mComponentModuleList.getDisplayNameRes(i3)), i2, false);
            } else if (i != 3) {
            }
            i = 8388613;
            int transferredMode2 = ComponentModuleList.getTransferredMode(this.mCurrentMode);
            size = this.mComponentModuleList.getCommonItems().size();
            i3 = 0;
            while (true) {
                if (i3 < size) {
                }
                i3++;
            }
            if (i == 8388611) {
            }
            changeModeByNewMode(this.mComponentModuleList.getMode(i3), getString(this.mComponentModuleList.getDisplayNameRes(i3)), i2, false);
        }
    }

    public void changeModeByNewMode(int i, String str, int i2) {
        if (this.mComponentModuleList.isCommonMode(i)) {
            this.mModeSelectView.moveToPosition(i);
        }
        changeModeByNewMode(i, str, i2, false);
    }

    public void enableStopButton(boolean z, boolean z2) {
        this.mShutterButton.setStopButtonEnable(z, z2);
    }

    public void entryOrExitMiMojiGif(boolean z) {
        this.mPreGifStatus = z;
    }

    public void expandAIWatermarkBottomMenu(ComponentRunningAIWatermark componentRunningAIWatermark) {
        if (this.mBottomRecordingTime.getVisibility() == 0) {
            this.mBottomRecordingTime.setVisibility(8);
        }
        hideModeOrExternalTipLayout();
        animateViews(1, (List) null, this.mModeSelectLayout.getView());
        this.mModeSelectLayout.expandAIWatermark(componentRunningAIWatermark, this.mCurrentMode);
    }

    public void expandMasterFilterBottomMenu(ComponentRunningMasterFilter componentRunningMasterFilter) {
        if (this.mBottomRecordingTime.getVisibility() == 0) {
            this.mBottomRecordingTime.setVisibility(8);
        }
        hideModeOrExternalTipLayout();
        animateViews(1, (List) null, this.mModeSelectLayout.getView());
        this.mModeSelectLayout.expandMasterFilter(componentRunningMasterFilter);
    }

    public void expandShineBottomMenu(ComponentRunningShine componentRunningShine) {
        if (this.mBottomRecordingTime.getVisibility() == 0) {
            this.mBottomRecordingTime.setVisibility(8);
        }
        hideModeOrExternalTipLayout();
        animateViews(1, (List) null, this.mModeSelectLayout.getView());
        this.mModeSelectLayout.expandShine(componentRunningShine, this.mCurrentMode);
    }

    public void filterUiChange() {
    }

    public boolean forceSwitchFront() {
        if (((DataItemGlobal) DataRepository.provider().dataGlobal()).getCurrentCameraId() == 1) {
            return false;
        }
        onClick(this.mCameraPicker);
        return true;
    }

    public int getBeautyActionMenuType() {
        return this.mCurrentBeautyActionMenuType;
    }

    public int getFragmentInto() {
        return 241;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_bottom_action;
    }

    public void hideDocumentReviewViews() {
    }

    public void hideExtra() {
        if (this.mIsShowMiMoji) {
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChanges != null) {
                configChanges.showOrHideMimoji();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mSupportOrientation = C0122O00000o.instance().O000OOoo(Display.getDisplayRatio());
        this.mBottomActionView = (FrameLayout) view.findViewById(R.id.bottom_action);
        ((MarginLayoutParams) this.mBottomActionView.getLayoutParams()).height = Display.getBottomHeight();
        this.mBottomParentHorizontal = (ViewGroup) view.findViewById(R.id.v9_bottom_parent_horizontal);
        this.mBottomParentVertical = (ViewGroup) view.findViewById(R.id.v9_bottom_parent_vertical);
        this.mComponentModuleList = DataRepository.dataItemGlobal().getComponentModuleList();
        this.mModeSelectBackgroundLayout = (ShapeBackGroundView) view.findViewById(R.id.mode_select_bg_layout);
        this.mModeSelectBackgroundLayout.setGravity(80);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mModeSelectBackgroundLayout.getLayoutParams();
        marginLayoutParams.height = Display.getBottomHeight() + Display.getSquareBottomCoverHeight();
        this.mModeSelectBackgroundLayout.initHeight(marginLayoutParams.height);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.mode_select_layout);
        this.mEdgeHorizonScrollView = (EdgeHorizonScrollView) frameLayout.findViewById(R.id.mode_select_scrollview);
        this.mBottomMenuLayout = frameLayout.findViewById(R.id.bottom_menu_layout);
        this.mModeSelectLayout = new BottomActionMenu(getContext(), frameLayout);
        this.mExternalModeTipLayout = (CapsuleLayout) view.findViewById(R.id.bottom_external_mode_root);
        this.mExternalModeTipText = (TextView) this.mExternalModeTipLayout.findViewById(R.id.bottom_external_mode_text);
        this.mExternalModeTipIcon = (ImageView) this.mExternalModeTipLayout.findViewById(R.id.bottom_external_mode_close);
        this.mExternalModeTipLayout.setOnClickListener(this);
        FolmeUtils.touchTint((View) this.mExternalModeTipLayout);
        MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mBottomMenuLayout.getLayoutParams();
        marginLayoutParams2.height = Math.round(((float) Display.getBottomBarHeight()) * 0.3f);
        marginLayoutParams2.bottomMargin = Display.getBottomMargin();
        marginLayoutParams2.setMarginStart(Display.getStartMargin());
        marginLayoutParams2.setMarginEnd(Display.getEndMargin());
        this.mModeSelectView = (ModeSelectView) frameLayout.findViewById(R.id.mode_select_view);
        this.mModeSelectView.init(this.mComponentModuleList, this.mCurrentMode, this);
        switchModeSelectViewStyle(this.mCurrentMode);
        this.mPopupMenuLayout = (PopupMenuLayout) getActivity().findViewById(R.id.popup_menu);
        this.mDragUpLayout = (DragLayout) view.findViewById(R.id.drag_layout);
        this.mDragUpLayout.addOnDragListener(this);
        this.mDragUpLayout.addOnDragListener(this.mPopupMenuLayout);
        MarginLayoutParams marginLayoutParams3 = (MarginLayoutParams) this.mDragUpLayout.getDragChildren().getLayoutParams();
        marginLayoutParams3.topMargin = Display.getDragLayoutTopMargin();
        marginLayoutParams3.height = Display.getBottomHeight() + ((int) DragLayout.getAnimationConfig().getMaxDragDistance());
        boolean z = C0122O00000o.instance().OOOOoO() && Display.getDisplayRatio() == Display.DISPLAY_RATIO_123;
        findViewByOrientation(!z);
        view.findViewById(R.id.bottom_control_bar).setMinimumHeight(Math.round(((float) Display.getBottomBarHeight()) * 0.3f));
        this.mBottomRecordingTime = (TextView) view.findViewById(R.id.bottom_recording_time_view);
        this.mBottomRecordingCameraPicker = (ImageView) view.findViewById(R.id.bottom_recording_camera_picker);
        this.mBottomRecordingCameraPicker.setOnClickListener(this);
        this.mShutterButton.setSnapListener(this);
        this.mShutterButton.setSnapClickEnable(false);
        this.mCaptureProgressDelay = getResources().getInteger(R.integer.capture_progress_delay_time);
        this.mRecordProgressDelay = getResources().getInteger(R.integer.record_progress_delay_time);
        this.mThumbnailImageLayout.setOnClickListener(this);
        this.mCameraPicker.setOnClickListener(this);
        this.mRecordingPause.setOnClickListener(this);
        this.mRecordingSnap.setOnClickListener(this);
        this.mRecordingReverse.setOnClickListener(this);
        this.mRecordingSwitch.setOnClickListener(this);
        this.mMimojiBack.setOnClickListener(this);
        this.mIsIntentAction = DataRepository.dataItemGlobal().isIntentAction();
        this.mRotateViews.add(this.mThumbnailImageLayout);
        this.mRotateViews.add(this.mShutterButton);
        this.mRotateViews.add(this.mCameraPicker);
        this.mRotateViews.add(this.mPostProcess);
        this.mRotateViews.add(this.mRecordingPause);
        this.mRotateViews.add(this.mBottomRecordingCameraPicker);
        this.mRotateViews.add(this.mRecordingReverse);
        this.mRotateViews.add(this.mRecordingSwitch);
        provideAnimateElement(this.mCurrentMode, null, 2);
        this.mCubicEaseOut = new CubicEaseOutInterpolator();
        this.mSineEaseOut = new SineEaseOutInterpolator();
        this.mFilterListHeight = getContext().getResources().getDimensionPixelSize(R.dimen.filter_still_height);
    }

    public boolean isExpanded() {
        if (!CameraSettings.isPopupMoreStyle()) {
            return false;
        }
        DragLayout dragLayout = this.mDragUpLayout;
        if (dragLayout != null) {
            return dragLayout.isExpanded();
        }
        return false;
    }

    public boolean isShowFilterView() {
        return false;
    }

    public boolean isShowLightingView() {
        return this.mIsShowLighting;
    }

    public boolean modeChanging() {
        return (!this.mDragUpLayout.isShrink() && !this.mDragUpLayout.isExpanded()) || this.mModeSelectView.getScrollState() != 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00b6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyAfterFrameAvailable(int i) {
        int i2;
        boolean z;
        super.notifyAfterFrameAvailable(i);
        FragmentLighting fragmentLighting = this.mFragmentLighting;
        if (fragmentLighting != null && fragmentLighting.isAdded()) {
            this.mFragmentLighting.reInitAdapterBgMode(true);
        }
        if (getActivity() != null && (getActivity() instanceof Camera)) {
            Camera camera = (Camera) getActivity();
            if (camera.getCurrentModule() != null) {
                i2 = camera.getCurrentModule().getPortraitLightingVersion();
                this.mModeSelectLayout.initBeautyMenuView(i2);
                adjustViewBackground();
                if (this.mCurrentMode != 204) {
                    animBottomBlackCover();
                }
                if (this.mShutterButton != null) {
                    if (this.mCurrentMode != 184 || DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate()) {
                        ProgressBar progressBar = this.mPostProcess;
                        if (progressBar != null && progressBar.getVisibility() == 0) {
                            Log.w(TAG, "notifyAfterFrameAvailable: shutter process bar is showing");
                            processingFinish();
                            this.mShutterButton.setParameters(PaintConditionReferred.create(this.mCurrentMode).setIsFPS960(CameraSettings.isAlgoFPS(this.mCurrentMode)));
                        }
                    } else {
                        showNormalMimoji2Bottom();
                    }
                }
                int i3 = this.mCurrentMode;
                z = i3 != 184 || i3 == 177;
                if (z) {
                    DataRepository.dataItemLive().getMimojiStatusManager().setMode(MimojiStatusManager.MIMOJI_PREVIEW);
                    DataRepository.dataItemLive().getMimojiStatusManager2().setMode(2);
                } else if (DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiCreate() || DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate()) {
                    ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166)).mimojiStart();
                }
                FolmeUtils.touchScaleTint(this.mBottomRecordingCameraPicker, this.mThumbnailImageLayout, this.mCameraPicker, this.mRecordingPause, this.mRecordingSnap, this.mRecordingReverse, this.mRecordingSwitch, this.mMimojiBack);
                checkFeatureState();
            }
        }
        i2 = 1;
        this.mModeSelectLayout.initBeautyMenuView(i2);
        adjustViewBackground();
        if (this.mCurrentMode != 204) {
        }
        if (this.mShutterButton != null) {
        }
        int i32 = this.mCurrentMode;
        if (i32 != 184) {
        }
        if (z) {
        }
        FolmeUtils.touchScaleTint(this.mBottomRecordingCameraPicker, this.mThumbnailImageLayout, this.mCameraPicker, this.mRecordingPause, this.mRecordingSnap, this.mRecordingReverse, this.mRecordingSwitch, this.mMimojiBack);
        checkFeatureState();
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:46:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyDataChanged(int i, int i2) {
        CameraSnapView cameraSnapView;
        String str;
        String str2;
        CameraSnapView cameraSnapView2;
        ImageView imageView;
        int i3;
        super.notifyDataChanged(i, i2);
        boolean isIntentAction = DataRepository.dataItemGlobal().isIntentAction();
        if (isIntentAction != this.mIsIntentAction) {
            this.mIsIntentAction = isIntentAction;
            this.mModeSelectView.setItems(this.mComponentModuleList.getCommonItems());
            if (this.mModeSelectView.getAdapter() != null) {
                this.mModeSelectView.getAdapter().notifyDataSetChanged();
                this.mModeSelectView.moveToPosition(this.mCurrentMode);
            }
            initThumbLayout();
        }
        this.mComponentModuleList.runChangeResetCb(new C0294O00000oO(this));
        if (this.mVideoRecordingStarted) {
            int i4 = this.mCurrentMode;
            if (i4 == 174 || i4 == 183) {
                Log.d(TAG, "onRecording dataChanged");
                this.mInLoading = false;
                FragmentLighting fragmentLighting = this.mFragmentLighting;
                if (fragmentLighting != null && fragmentLighting.isAdded()) {
                    this.mFragmentLighting.reInit();
                }
                FragmentActionMimoji fragmentActionMimoji = this.mFragmentActionMimoji;
                if (fragmentActionMimoji != null && fragmentActionMimoji.isAdded()) {
                    this.mFragmentActionMimoji.reInit(this.mCurrentMode);
                }
                if (!Util.isAccessible()) {
                    if (!this.mVideoRecordingStarted || this.mCurrentMode != 183) {
                        int i5 = this.mCurrentMode;
                        if (i5 != 204) {
                            switch (i5) {
                                case 209:
                                case 210:
                                case 211:
                                    cameraSnapView = this.mShutterButton;
                                    str = getString(R.string.accessibility_shutter_next_button);
                                    break;
                                default:
                                    this.mShutterButton.setContentDescription(getString(R.string.accessibility_shutter_button));
                                    imageView = this.mCameraPicker;
                                    i3 = R.string.accessibility_camera_picker;
                                    break;
                            }
                        } else {
                            if (!shouldUseThumbnailAsExit()) {
                                cameraSnapView2 = this.mShutterButton;
                                str2 = getString(R.string.accessibility_shutter_next_button);
                            } else {
                                cameraSnapView2 = this.mShutterButton;
                                str2 = getString(R.string.accessibility_shutter_button);
                            }
                            cameraSnapView2.setContentDescription(str2);
                            imageView = this.mCameraPicker;
                            i3 = R.string.accessibility_dual_preview_change;
                        }
                        imageView.setContentDescription(getString(i3));
                        return;
                    }
                    cameraSnapView = this.mShutterButton;
                    str = getString(R.string.accessibility_shutter_end_button);
                    cameraSnapView.setContentDescription(str);
                    return;
                }
                return;
            }
        }
        switchMoreMode(this.mModeSelectView.getVisibility() == 0);
        this.mInLoading = false;
        FragmentLighting fragmentLighting2 = this.mFragmentLighting;
        this.mFragmentLighting.reInit();
        FragmentActionMimoji fragmentActionMimoji2 = this.mFragmentActionMimoji;
        this.mFragmentActionMimoji.reInit(this.mCurrentMode);
        if (!Util.isAccessible()) {
        }
    }

    public void onAngleChanged(float f) {
    }

    public boolean onBackEvent(int i) {
        return false;
    }

    public void onBeautyRecordingStart() {
        ViewCompat.animate(this.mModeSelectView).alpha(0.0f).start();
    }

    public void onBeautyRecordingStop() {
        ViewCompat.animate(this.mModeSelectView).alpha(1.0f).start();
    }

    public void onBottomMenuAnimate(int i, int i2) {
        this.mModeSelectLayout.bottomMenuAnimate(i, i2);
    }

    public void onClick(View view) {
        View[] viewArr;
        boolean isEnableClick = isEnableClick();
        String str = TAG;
        if (!isEnableClick) {
            Log.d(str, "onClick: disabled");
            return;
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction == null) {
            Log.d(str, "onClick: null action");
            return;
        }
        Module currentModule = ((ActivityBase) getContext()).getCurrentModule();
        int id = view.getId();
        String str2 = BaseEvent.CREATE;
        switch (id) {
            case R.id.bottom_external_mode_root /*2131296382*/:
                Log.u(str, "onClick: bottom_external_mode_root");
                if (!cameraAction.isDoingAction() && !cameraAction.isRecording() && !cameraAction.isRecordingPaused()) {
                    this.mIsShowMiMoji = false;
                    resetToCommonMode();
                    break;
                }
            case R.id.bottom_recording_camera_picker /*2131296390*/:
                Log.u(str, "onClick: bottom_recording_camera_picker");
                if (this.mVideoRecordingPaused) {
                    int i = this.mCurrentMode;
                    if (i == 174) {
                        CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_CLICK_SWITCH);
                    } else if (i == 183) {
                        CameraStatUtils.trackMiLiveClick(MiLive.VALUE_MI_LIVE_CLICK_SWITCH);
                    }
                    viewArr = new View[]{view};
                    break;
                } else {
                    return;
                }
            case R.id.mimoji_create_back_horizontal /*2131296808*/:
            case R.id.mimoji_create_back_vertical /*2131296809*/:
                Log.u(str, "onClick: mimoji_create_back");
                animateViews(-1, (List) null, (View) this.mMimojiBack);
                if (this.mCurrentMode == 184) {
                    DataRepository.dataItemLive().getMimojiStatusManager2().setMode(2);
                } else {
                    DataRepository.dataItemLive().getMimojiStatusManager().setMode(MimojiStatusManager.MIMOJI_PREVIEW);
                }
                ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).onMimojiCreateBack();
                CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_CREATE_BACK, str2);
                break;
            case R.id.v9_camera_picker_horizontal /*2131297174*/:
            case R.id.v9_camera_picker_vertical /*2131297175*/:
                Log.u(str, "onClick: v9_camera_picker");
                if (!cameraAction.isDoingAction()) {
                    if ((!cameraAction.isRecording() || currentModule.getModuleIndex() == 204) && !isThumbLoading()) {
                        hideExtra();
                        if (DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiCreate() || DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate()) {
                            CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_CREATE_SWITCH, str2);
                        }
                        if (!cameraAction.onCameraPickerClicked(view)) {
                            viewArr = new View[]{view};
                            break;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case R.id.v9_capture_recording_switch_horizontal /*2131297176*/:
            case R.id.v9_capture_recording_switch_vertical /*2131297177*/:
                Log.u(str, "onClick: v9_capture_recording_switch");
                if (!cameraAction.isDoingAction() && !this.mVideoRecordingStarted && !CameraSettings.isGifOn()) {
                    switchVideoCapture(view);
                    break;
                } else {
                    return;
                }
            case R.id.v9_recording_pause_horizontal /*2131297181*/:
            case R.id.v9_recording_pause_vertical /*2131297182*/:
                Log.u(str, "onClick: v9_recording_pause");
                pauseRecording();
                break;
            case R.id.v9_recording_reverse_horizontal /*2131297185*/:
            case R.id.v9_recording_reverse_vertical /*2131297186*/:
                Log.u(str, "onClick: v9_recording_reverse");
                if (this.mVideoReverseEnable && this.mVideoRecordingStarted && this.mShutterButton.hasSegments()) {
                    showReverseConfirmDialog();
                    break;
                } else {
                    return;
                }
                break;
            case R.id.v9_recording_snap_horizontal /*2131297187*/:
            case R.id.v9_recording_snap_vertical /*2131297188*/:
                Log.u(str, "onClick: v9_recording_snap");
                if (this.mVideoCaptureEnable && this.mVideoRecordingStarted) {
                    Module currentModule2 = ((ActivityBase) getContext()).getCurrentModule();
                    if (getContext() != null) {
                        boolean z = currentModule2 instanceof VideoModule;
                        if (z || (currentModule2 instanceof FunModule) || (currentModule2 instanceof MiLiveModule) || (currentModule2 instanceof DualVideoRecordModule)) {
                            if (!z) {
                                if (!(currentModule2 instanceof FunModule)) {
                                    if (!(currentModule2 instanceof MiLiveModule)) {
                                        if (currentModule2 instanceof DualVideoRecordModule) {
                                            ((DualVideoRecordModule) currentModule2).takeVideoSnapShot();
                                            break;
                                        }
                                    } else {
                                        ((MiLiveModule) currentModule2).takePreviewSnapShoot();
                                        break;
                                    }
                                } else {
                                    ((FunModule) currentModule2).takePreviewSnapShoot();
                                    break;
                                }
                            } else {
                                ((VideoModule) currentModule2).takeVideoSnapShoot();
                                break;
                            }
                        }
                    }
                    Log.w(str, "onClick: recording snap is not allowed!!!");
                }
                return;
            case R.id.v9_thumbnail_layout_horizontal /*2131297193*/:
            case R.id.v9_thumbnail_layout_vertical /*2131297194*/:
                Log.u(str, "onClick: v9_thumbnail_layout");
                if (!currentModule.isIgnoreTouchEvent() || currentModule.isShot2GalleryOrEnableParallel()) {
                    if (!isThumbLoading()) {
                        if (shouldUseThumbnailAsExit()) {
                            Log.u(str, "onClick: v9_thumbnail_layout, onReviewCancelClicked");
                            cameraAction.onReviewCancelClicked();
                            break;
                        } else {
                            Log.u(str, "onClick: v9_thumbnail_layout, onThumbnailClicked");
                            assertThumbnailViewRect();
                            cameraAction.onThumbnailClicked(null);
                            break;
                        }
                    } else {
                        Log.w(str, "onClick: ignore thumbnail click event as loading thumbnail");
                        return;
                    }
                } else {
                    Log.w(str, "onClick: ignore click event, because module isn't ready");
                    return;
                }
                break;
        }
        changeCamera(viewArr);
    }

    public void onDragDone(boolean z) {
        if (z) {
            this.mBottomActionView.setAlpha(0.0f);
            this.mBottomActionView.setVisibility(8);
            CameraStatUtils.trackEnterMoreMode(MoreMode.VALUE_ENTER_MORE_MODE_BY_POP);
            return;
        }
        this.mBottomActionView.setAlpha(1.0f);
        this.mBottomActionView.setVisibility(0);
    }

    public void onDragProgress(int i, boolean z) {
        if (this.mBottomActionView != null) {
            this.mBottomActionView.setAlpha(1.0f - Math.min(1.0f, ((float) Math.abs(i)) / DragLayout.getAnimationConfig().getDisappearDistance()));
        }
    }

    public void onDragStart(boolean z) {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && topAlert.isExtraMenuShowing()) {
            topAlert.hideExtraMenu();
        }
        if (!z) {
            this.mBottomActionView.setAlpha(0.0f);
            this.mBottomActionView.setVisibility(0);
        }
    }

    public boolean onHandleSwitcher(int i) {
        if (!this.mIsShowLighting) {
            return false;
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null && !cameraAction.isDoingAction() && this.mIsShowLighting) {
            this.mFragmentLighting.switchLighting(i);
        }
        return true;
    }

    public boolean onInterceptDrag() {
        return true;
    }

    public void onModeSelected(int i, String str) {
        if (!isShowFilterView() && !isShowLightingView() && (this.mCurrentMode != 177 || DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiPreview())) {
            if (this.mCurrentMode == 184 && !DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiPreview()) {
                return;
            }
            if (this.mCurrentMode != 180 || i != 167) {
                if (i == 254) {
                    CameraStatUtils.trackEnterMoreMode(DataRepository.dataItemGlobal().useNewMoreTabStyle() ? MoreMode.VALUE_ENTER_MORE_MODE_BY_TAB_NEW : MoreMode.VALUE_ENTER_MORE_MODE_BY_TAB);
                }
                changeModeByNewMode(i, str, 0, true);
            }
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onRestoreCameraActionMenu(int i) {
        showModeOrExternalTipLayout(true);
        if (this.mIsShowMiMoji) {
            showOrHideMiMojiView();
        }
        this.mModeSelectLayout.switchMenuMode(0, true);
        if (this.mVideoRecordingStarted) {
            int i2 = this.mCurrentMode;
            if (i2 == 174 || i2 == 183) {
                this.mDragUpLayout.setDragEnable(false);
                animateViews(-1, (List) null, this.mModeSelectLayout.getView());
            }
        }
    }

    public void onResume() {
        super.onResume();
        initThumbLayout();
        this.mModeSelectView.refresh();
        DragLayout dragLayout = this.mDragUpLayout;
        if (dragLayout != null) {
            dragLayout.reset();
        }
    }

    public void onSnapClick() {
        String str;
        boolean isEnableClick = isEnableClick();
        String str2 = TAG;
        if (!isEnableClick) {
            str = "onSnapClick: disabled";
        } else if (getContext() == null) {
            str = "onSnapClick: no context";
        } else {
            Camera camera = (Camera) getContext();
            Module currentModule = camera.getCurrentModule();
            if (currentModule != null && currentModule.isIgnoreTouchEvent()) {
                str = "onSnapClick: ignore onSnapClick event, because module isn't ready";
            } else if (!CameraSettings.isFrontCamera() || !camera.isScreenSlideOff()) {
                CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                if (cameraAction == null) {
                    str = "onSnapClick: no camera action";
                } else {
                    Log.u(str2, "onSnapClick");
                    int i = this.mCurrentMode;
                    if (!(i == 161 || i == 162)) {
                        if (i != 166) {
                            if (!(i == 169 || i == 172 || i == 174 || i == 187)) {
                                if (i != 204) {
                                    if (!(i == 211 || i == 214)) {
                                        if (!(i == 176 || i == 177)) {
                                            if (!(i == 179 || i == 180 || i == 183)) {
                                                if (i != 184) {
                                                    switch (i) {
                                                        case 207:
                                                        case 208:
                                                        case 209:
                                                            break;
                                                        default:
                                                            TimerBurstController timerBurstController = DataRepository.dataItemLive().getTimerBurstController();
                                                            if (cameraAction.isBlockSnap() && !timerBurstController.isInTimerBurstShotting()) {
                                                                str = "onSnapClick: block snap";
                                                                break;
                                                            }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else if (!DataRepository.dataItemRunning().getComponentRunningDualVideo().ismDrawSelectWindow() && !this.mVideoRecordingStarted) {
                                    this.mVideoRecordingStarted = true;
                                }
                            }
                        }
                        if (cameraAction.isDoingAction() && this.mCurrentMode != 184) {
                            str = "onSnapClick: doing action";
                        }
                    }
                    cameraAction.onShutterButtonClick(10);
                    return;
                }
            } else {
                str = "onSnapClick: ignore onSnapClick event, because screen slide is off";
            }
        }
        Log.w(str2, str);
    }

    public void onSnapLongPress() {
        if (isEnableClick()) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction != null && !cameraAction.isDoingAction()) {
                if (getContext() != null) {
                    Camera camera = (Camera) getContext();
                    if (CameraSettings.isFrontCamera() && camera.isScreenSlideOff()) {
                        return;
                    }
                }
                Log.d(TAG, "onSnapLongPress");
                if (cameraAction.onShutterButtonLongClick()) {
                    this.mLongPressBurst = true;
                }
            }
        }
    }

    public void onSnapLongPressCancelIn() {
        if (isEnableClick()) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction != null) {
                Log.d(TAG, "onSnapLongPressCancelIn");
                cameraAction.onShutterButtonLongClickCancel(true);
                int i = this.mCurrentMode;
                if (i != 163) {
                    if (i == 165 || i == 167 || i == 171) {
                        onSnapClick();
                    }
                } else if (this.mLongPressBurst) {
                    this.mLongPressBurst = false;
                }
            }
        }
    }

    public void onSnapLongPressCancelOut() {
        if (isEnableClick()) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction != null) {
                Log.d(TAG, "onSnapLongPressCancelOut");
                cameraAction.onShutterButtonLongClickCancel(false);
                if (this.mLongPressBurst) {
                    this.mLongPressBurst = false;
                }
            }
        }
    }

    public void onSnapPrepare() {
        if (isEnableClick()) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction != null) {
                Log.d(TAG, "onSnapPrepare");
                cameraAction.onShutterButtonFocus(true, 2);
            }
        }
    }

    public void onSwitchCameraPicker() {
        ImageView imageView = this.mCameraPicker;
        if (imageView != null) {
            changeCamera(imageView);
        }
    }

    public void onSwitchFastMotionAction(ComponentRunningFastMotion componentRunningFastMotion) {
        if (this.mBottomRecordingTime.getVisibility() == 0) {
            this.mBottomRecordingTime.setVisibility(8);
        }
        hideModeOrExternalTipLayout();
        this.mModeSelectLayout.switchFastMotion(componentRunningFastMotion);
    }

    public void onSwitchFastMotionProAction(ComponentRunningFastMotionPro componentRunningFastMotionPro) {
        if (this.mBottomRecordingTime.getVisibility() == 0) {
            this.mBottomRecordingTime.setVisibility(8);
        }
        hideModeOrExternalTipLayout();
        this.mModeSelectLayout.switchFastMotionPro(componentRunningFastMotionPro);
    }

    public void onSwitchLiveActionMenu(int i) {
        if (this.mBottomRecordingTime.getVisibility() == 0) {
            this.mBottomRecordingTime.setVisibility(8);
        }
        hideModeOrExternalTipLayout();
        this.mCurrentLiveActionMenuType = i;
        animateViews(1, (List) null, this.mModeSelectLayout.getView());
        this.mModeSelectLayout.switchMenuMode(2, i, true);
    }

    public void onTrackSnapMissTaken(long j) {
        if (isEnableClick()) {
            CameraActionTrack cameraActionTrack = (CameraActionTrack) ModeCoordinatorImpl.getInstance().getAttachProtocol(186);
            if (cameraActionTrack != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("onTrackSnapMissTaken ");
                sb.append(j);
                sb.append(d.H);
                Log.d(TAG, sb.toString());
                cameraActionTrack.onTrackShutterButtonMissTaken(j);
            }
        }
    }

    public void onTrackSnapTaken(long j) {
        if (isEnableClick()) {
            CameraActionTrack cameraActionTrack = (CameraActionTrack) ModeCoordinatorImpl.getInstance().getAttachProtocol(186);
            if (cameraActionTrack != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("onTrackSnapTaken ");
                sb.append(j);
                sb.append(d.H);
                Log.d(TAG, sb.toString());
                cameraActionTrack.onTrackShutterButtonTaken(j);
            }
        }
    }

    public void pauseRecording() {
        if (this.mVideoPauseSupported && this.mVideoRecordingStarted) {
            int i = this.mCurrentMode;
            if (!(i == 162 || i == 169)) {
                if (i == 174) {
                    CameraStatUtils.trackLiveClick(this.mVideoRecordingPaused ? Live.LIVE_CLICK_RESUME : Live.LIVE_CLICK_PAUSE);
                } else if (i != 180) {
                    if (i != 183) {
                        if (i == 204) {
                            ((DualVideoRecordModule) ((ActivityBase) getContext()).getCurrentModule()).onPauseButtonClick();
                        } else if (i != 207) {
                            return;
                        }
                    }
                }
                long currentTimeMillis = System.currentTimeMillis() - this.mLastPauseTime;
                if (currentTimeMillis <= 0 || currentTimeMillis >= 500) {
                    this.mLastPauseTime = System.currentTimeMillis();
                    ActivityBase activityBase = (ActivityBase) getContext();
                    if (activityBase == null || (!(activityBase.getCurrentModule() instanceof LiveModule) && !(activityBase.getCurrentModule() instanceof MiLiveModule))) {
                        Log.w(TAG, "onClick: recording pause is not allowed!!!");
                        return;
                    }
                    ((ILiveModule) activityBase.getCurrentModule()).onPauseButtonClick();
                } else {
                    return;
                }
            }
            ((VideoModule) ((ActivityBase) getContext()).getCurrentModule()).onPauseButtonClick();
        }
    }

    public void processingAudioCapture(boolean z) {
        CameraSnapView cameraSnapView = this.mShutterButton;
        if (z) {
            cameraSnapView.startRing();
        } else {
            cameraSnapView.stopRing();
        }
    }

    public void processingFailed() {
        updateLoading(true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00a1, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOoOOOo() != false) goto L_0x0108;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0106, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOoO0o() != false) goto L_0x0108;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0108, code lost:
        setRecordingSwitchTarget(true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x010c, code lost:
        r2 = r10.mRecordingSwitch;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void processingFinish() {
        if (isAdded()) {
            animateViews(1, (List) null, (View) this.mShutterButton);
            FolmeUtils.animateShow(this.mEdgeHorizonScrollView);
            this.mModeSelectView.setEnabled(true);
            this.mVideoRecordingStarted = false;
            this.mVideoRecordingPaused = false;
            setProgressBarVisible(8);
            this.mShutterButton.showRoundPaintItem();
            if (Util.isAccessible()) {
                if (isAdded() && this.mCurrentMode != 184) {
                    this.mShutterButton.announceForAccessibility(getString(R.string.accessibility_camera_shutter_finish));
                }
                this.mShutterButton.setContentDescription(getString(R.string.accessibility_shutter_button));
            }
            int i = this.mCurrentMode;
            if (i != 163) {
                if (i != 167) {
                    if (i != 177) {
                        if (i != 180) {
                            if (i == 207) {
                                RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, (float) (this.mRecordSaveButton.getLayoutParams().width / 2), (float) (this.mRecordSaveButton.getLayoutParams().height / 2));
                                rotateAnimation.setDuration((long) getResources().getInteger(R.integer.post_process_duration));
                                rotateAnimation.setInterpolator(new LinearInterpolator());
                                rotateAnimation.setRepeatMode(1);
                                rotateAnimation.setRepeatCount(-1);
                                this.mRecordSaveButton.setAnimation(rotateAnimation);
                                this.mRecordSaveButton.setVisibility(0);
                                this.mShutterButton.setVisibility(8);
                            } else if (i != 214) {
                                if (i != 173) {
                                    if (i == 174 || i == 183) {
                                        if (this.mBottomRecordingCameraPicker.getVisibility() == 0) {
                                            Completable.create(new AlphaOutOnSubscribe(this.mBottomRecordingCameraPicker)).subscribe();
                                        }
                                    } else if (i == 184) {
                                        showNormalMimoji2Bottom();
                                    }
                                }
                            }
                        }
                        setRecordingSwitchTarget(false);
                    } else {
                        View view = this.mMimojiBack;
                        animateViews(-1, (List) null, view);
                    }
                }
                animateViews(1, (List) null, (View) this.mRecordingSwitch);
            }
            int i2 = this.mCurrentMode;
            BottomAnimationConfig configVariables = BottomAnimationConfig.generate(false, i2, false, CameraSettings.isAlgoFPS(i2), CameraSettings.isVideoBokehOn()).configVariables();
            configVariables.mIsInMimojiCreate = this.mCurrentMode == 184 ? DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate() : DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiCreate();
            this.mShutterButton.triggerAnimation(configVariables);
            updateBottomInRecording(false, false);
        }
    }

    public void processingLongExposePrepare() {
        int i = this.mCurrentMode;
        this.mShutterButton.longExposePrepare(BottomAnimationConfig.generate(false, i, true, CameraSettings.isAlgoFPS(i), CameraSettings.isVideoBokehOn()).configVariables());
    }

    public void processingLongExposeStart() {
        int i = this.mCurrentMode;
        this.mShutterButton.longExposeStart(BottomAnimationConfig.generate(false, i, true, CameraSettings.isAlgoFPS(i), CameraSettings.isVideoBokehOn()).configVariables());
    }

    public void processingMimojiBack() {
        DataRepository.dataItemLive().getMimojiStatusManager().setMode(MimojiStatusManager.MIMOJI_PREVIEW);
        DataRepository.dataItemLive().getMimojiStatusManager2().setMode(2);
        animateViews(-1, (List) null, (View) this.mCameraPicker);
        animateViews(-1, (List) null, (View) this.mMimojiBack);
        animateViews(1, (List) null, (View) this.mThumbnailImageLayout);
        showModeOrExternalTipLayout(false);
        animateViews(1, (List) null, this.mModeSelectLayout.getView());
        if (this.mCurrentMode == 184) {
            showNormalMimoji2Bottom();
            return;
        }
        animateViews(1, (List) null, (View) this.mCameraPicker);
        animateViews(-1, (List) null, (View) this.mRecordingSwitch);
    }

    public void processingMimojiPrepare() {
        hideModeOrExternalTipLayout();
        this.mDragUpLayout.setDragEnable(false);
        animateViews(-1, (List) null, (View) this.mThumbnailImageLayout);
        animateViews(1, (List) null, (View) this.mMimojiBack);
        if (this.mCurrentMode == 184) {
            DataRepository.dataItemLive().getMimojiStatusManager2().setMode(4);
            animateViews(1, (List) null, (View) this.mCameraPicker);
            this.mShutterButton.onForceVideoStateChange(PaintConditionReferred.create(184));
            animateViews(-1, (List) null, (View) this.mRecordingSwitch);
        }
    }

    public void processingPause() {
        CompletableOnSubscribe completableOnSubscribe;
        this.mVideoRecordingPaused = true;
        this.mShutterButton.pauseRecording();
        setPausePlaySwitchTarget(true);
        this.mRecordingPause.O0000oO();
        if (Util.isAccessible()) {
            this.mRecordingPause.setContentDescription(getString(R.string.accessibility_shutter_resume_button));
        }
        int i = this.mCurrentMode;
        if (i == 174) {
            this.mShutterButton.addSegmentNow();
            Completable.create(new AlphaInOnSubscribe(this.mBottomRecordingCameraPicker)).subscribe();
            completableOnSubscribe = new AlphaInOnSubscribe(this.mRecordingReverse);
        } else if (i == 183) {
            AnonymousClass6 r0 = new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    FragmentBottomAction.this.mShutterButton.addSegmentNow();
                    Completable.create(new AlphaInOnSubscribe(FragmentBottomAction.this.mRecordingReverse)).subscribe();
                    if (FragmentBottomAction.this.mCurrentMode == 183 && C0122O00000o.instance().OOOoOo0()) {
                        Completable.create(new AlphaOutOnSubscribe(FragmentBottomAction.this.mRecordingSnap)).subscribe();
                    }
                    FragmentBottomAction.this.mBottomAnimator.removeListener(this);
                }
            };
            ValueAnimator valueAnimator = this.mBottomAnimator;
            if (valueAnimator == null || !valueAnimator.isRunning()) {
                r0.onAnimationEnd(null);
                return;
            }
            this.mBottomAnimator.addListener(r0);
            this.mBottomAnimator.end();
            return;
        } else if (i == 204) {
            Completable.create(new AlphaInOnSubscribe(this.mCameraPicker)).subscribe();
            completableOnSubscribe = new AlphaOutOnSubscribe(this.mRecordingSnap);
        } else {
            return;
        }
        Completable.create(completableOnSubscribe).subscribe();
    }

    public void processingPostAction() {
        if (this.mShutterButton.getVisibility() != 0) {
            this.mShutterButton.setVisibility(0);
        }
        this.mShutterButton.hideRoundPaintItem();
        int i = this.mCurrentMode;
        this.mShutterButton.triggerAnimation(BottomAnimationConfig.generate(true, i, false, CameraSettings.isAlgoFPS(i), CameraSettings.isVideoBokehOn()).configVariables());
        if (Util.isAccessible()) {
            this.mShutterButton.setContentDescription(getString(R.string.accessibility_shutter_saving_button));
        }
        this.mEdgeHorizonScrollView.setVisibility(0);
        updateBottomInRecording(false, true);
        setProgressBarVisible(0);
    }

    public void processingPrepare() {
        int i = this.mCurrentMode;
        if (!(i == 161 || i == 162 || i == 169 || i == 172 || i == 174 || i == 187 || i == 211 || i == 179 || i == 180)) {
            switch (i) {
                case 207:
                case 208:
                case 209:
                    break;
            }
        }
        if (!this.mVideoRecordingStarted) {
            this.mVideoRecordingStarted = true;
        }
        int i2 = this.mCurrentMode;
        BottomAnimationConfig configVariables = BottomAnimationConfig.generate(false, i2, true, CameraSettings.isAlgoFPS(i2), CameraSettings.isVideoBokehOn()).configVariables();
        updateBottomInRecording(true, true);
        this.mShutterButton.prepareRecording(configVariables);
        DragLayout dragLayout = this.mDragUpLayout;
        if (dragLayout != null) {
            dragLayout.setDragEnable(false);
        }
    }

    public void processingResume() {
        CompletableOnSubscribe completableOnSubscribe;
        this.mVideoRecordingPaused = false;
        this.mShutterButton.resumeRecording();
        setPausePlaySwitchTarget(false);
        this.mRecordingPause.O0000oO();
        if (Util.isAccessible()) {
            this.mRecordingPause.setContentDescription(getString(R.string.accessibility_shutter_pause_button));
        }
        this.mDragUpLayout.setDragEnable(false);
        animateViews(-1, (List) null, this.mModeSelectLayout.getView());
        int i = this.mCurrentMode;
        if (i == 174) {
            Completable.create(new AlphaOutOnSubscribe(this.mRecordingReverse)).subscribe();
            completableOnSubscribe = new AlphaOutOnSubscribe(this.mBottomRecordingCameraPicker);
        } else if (i == 183) {
            Completable.create(new AlphaOutOnSubscribe(this.mRecordingReverse)).subscribe();
            if (C0122O00000o.instance().OOOoOo0()) {
                completableOnSubscribe = new AlphaInOnSubscribe(this.mRecordingSnap);
            } else {
                return;
            }
        } else if (i == 204) {
            Completable.create(new AlphaInOnSubscribe(this.mRecordingSnap)).subscribe();
            completableOnSubscribe = new AlphaOutOnSubscribe(this.mCameraPicker);
        } else {
            return;
        }
        Completable.create(completableOnSubscribe).subscribe();
    }

    public void processingSpeechShutter(boolean z) {
        CameraSnapView cameraSnapView = this.mShutterButton;
        if (z) {
            cameraSnapView.startSpeech();
        } else {
            cameraSnapView.stopSpeech();
        }
    }

    public void processingStart() {
        CameraSnapView cameraSnapView;
        String string;
        this.mEdgeHorizonScrollView.setVisibility(8);
        this.mModeSelectView.setEnabled(false);
        this.mModeSelectView.stopScroll();
        this.mVideoRecordingStarted = true;
        int i = this.mCurrentMode;
        BottomAnimationConfig configVariables = BottomAnimationConfig.generate(false, i, true, CameraSettings.isAlgoFPS(i), CameraSettings.isVideoBokehOn()).configVariables();
        this.mShutterButton.setStopButtonEnable(configVariables.mStopButtonEnabled, false);
        this.mShutterButton.triggerAnimation(configVariables);
        if (Util.isAccessible()) {
            int i2 = this.mCurrentMode;
            int i3 = 2131755182;
            if (i2 == 173) {
                cameraSnapView = this.mShutterButton;
                string = getString(R.string.accessibility_shutter_process_button);
            } else if (i2 != 187) {
                cameraSnapView = this.mShutterButton;
                string = getString(R.string.accessibility_shutter_end_button);
            } else {
                CameraSnapView cameraSnapView2 = this.mShutterButton;
                if (!configVariables.mIsRecordingCircle) {
                    i3 = R.string.accessibility_shutter_end_button;
                }
                cameraSnapView2.setContentDescription(getString(i3));
                return;
            }
            cameraSnapView.setContentDescription(string);
        }
    }

    public void processingWorkspace(boolean z) {
        if (z) {
            animateViews(1, (List) null, (View) this.mShutterButton);
            this.mBottomRecordingTime.setVisibility(0);
            if (this.mRecordingPause.getVisibility() == 8) {
                setPausePlaySwitchTarget(true);
                Completable.create(new AlphaInOnSubscribe(this.mRecordingPause).targetGone()).subscribe();
            }
            if (this.mRecordingReverse.getVisibility() == 8) {
                Completable.create(new AlphaInOnSubscribe(this.mRecordingReverse).targetGone()).subscribe();
            }
            if (this.mBottomRecordingCameraPicker.getVisibility() == 8) {
                Completable.create(new AlphaInOnSubscribe(this.mBottomRecordingCameraPicker).targetGone()).subscribe();
                return;
            }
            return;
        }
        if (!(this.mCurrentMode == 184 && DataRepository.dataItemLive().getMimojiStatusManager2().getMimojiRecordState() == 1)) {
            this.mShutterButton.pauseRecording();
            this.mShutterButton.addSegmentNow();
        }
        animateViews(-1, (List) null, (View) this.mShutterButton);
        this.mBottomRecordingTime.setVisibility(8);
        if (this.mRecordingPause.getVisibility() == 0) {
            AlphaOutOnSubscribe.directSetResult(this.mRecordingPause);
        }
        if (this.mRecordingReverse.getVisibility() == 0) {
            AlphaOutOnSubscribe.directSetResult(this.mRecordingReverse);
        }
        if (this.mBottomRecordingCameraPicker.getVisibility() == 0 && this.mCurrentMode != 183) {
            AlphaOutOnSubscribe.directSetResult(this.mBottomRecordingCameraPicker);
        }
        if (this.mRecordingSnap.getVisibility() == 0) {
            AlphaOutOnSubscribe.directSetResult(this.mRecordingSnap);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x01dd, code lost:
        setRecordingSwitchTarget(r4);
        r16 = false;
        r10 = 1;
        r11 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x01ee, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOo0Oo() != false) goto L_0x0134;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x01f8, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOoo00() == false) goto L_0x01c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x0204, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOOo0OO() != false) goto L_0x0134;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x0210, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOOo0Oo() != false) goto L_0x0134;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x021c, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOOo00() != false) goto L_0x0134;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x014a, code lost:
        r16 = false;
        r11 = 1;
        r13 = 1;
        r15 = 1;
        r17 = 1;
        r4 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0152, code lost:
        r10 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0155, code lost:
        r11 = 1;
        r13 = 1;
        r14 = 1;
        r16 = true;
        r17 = 1;
        r4 = -1;
        r10 = -1;
        r12 = -1;
        r15 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x01bd, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOOo0o() != false) goto L_0x0134;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x01d4, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOoOOOo() == false) goto L_0x01e6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x01d8, code lost:
        if (r1 != 173) goto L_0x01dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x01da, code lost:
        r4 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x01dc, code lost:
        r4 = false;
     */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x0297  */
    /* JADX WARNING: Removed duplicated region for block: B:137:0x0299  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x029e  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x02a0  */
    /* JADX WARNING: Removed duplicated region for block: B:150:0x02bd  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x03a6  */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x03ad A[LOOP:0: B:183:0x03ad->B:185:0x03b5, LOOP_START, PHI: r5 
  PHI: (r5v3 int) = (r5v0 int), (r5v4 int) binds: [B:182:0x03ab, B:185:0x03b5] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:186:0x03c5 A[LOOP:1: B:186:0x03c5->B:188:0x03cd, LOOP_START, PHI: r5 
  PHI: (r5v1 int) = (r5v0 int), (r5v2 int) binds: [B:182:0x03ab, B:188:0x03cd] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:191:0x03e2  */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x03e8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void provideAnimateElement(int i, List list, int i2) {
        int i3;
        boolean z;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        List list2;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        boolean z2;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22 = i;
        List list3 = list;
        int i23 = i2;
        int i24 = this.mCurrentMode;
        int i25 = 0;
        boolean z3 = i23 == 3;
        if (z3 || i24 != i22) {
            View view = getView();
            if (!(view == null || ViewCompat.getTranslationY(view) == 0.0f)) {
                ViewCompat.setTranslationY(view, 0.0f);
            }
            AlertDialog alertDialog = this.mReverseDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.mReverseDialog = null;
            }
            if (this.mIsShowMiMoji) {
                showOrHideMiMojiView();
            }
            this.mShutterButton.onTimeOut();
            if (this.mVideoRecordingStarted) {
                this.mVideoRecordingStarted = false;
                this.mPostProcess.setVisibility(8);
                AlphaInOnSubscribe.directSetResult(this.mModeSelectBackgroundLayout);
                this.mRecordingReverse.setVisibility(8);
                this.mRecordingPause.setVisibility(8);
                this.mRecordingSnap.setVisibility(8);
                this.mBottomRecordingCameraPicker.setVisibility(8);
            }
        }
        if (i23 != 2 && isShowLightingView()) {
            showOrHideLightingView();
        }
        if ((i24 != 174 && i24 != 183) || !this.mVideoRecordingStarted || z3) {
            switchModeSelectViewStyle(i);
            super.provideAnimateElement(i, list, i2);
            processingSpeechShutter(false);
            if (this.mRecordSaveButton.getVisibility() == 0) {
                this.mRecordSaveButton.clearAnimation();
                this.mRecordSaveButton.setVisibility(8);
                this.mShutterButton.setVisibility(0);
            }
            int i26 = this.mCurrentMode;
            PaintConditionReferred createModeChange = PaintConditionReferred.createModeChange(i26, CameraSettings.isAlgoFPS(i26), list3 != null);
            if (i22 == 254) {
                createModeChange.setNeedSnapButtonAnimation(false);
            }
            this.mShutterButton.setParameters(createModeChange);
            int bottomMaskTargetHeight = createModeChange.getBottomMaskTargetHeight(i22);
            if (createModeChange.bottomHalfScreen()) {
                this.mCameraPicker.setBackgroundResource(R.drawable.bg_thumbnail_background_half);
                this.mRecordingSwitch.setBackgroundResource(R.drawable.bg_thumbnail_background_half);
                this.mMimojiBack.setBackgroundResource(R.drawable.bg_thumbnail_background_half);
                this.mThumbnailImage.setBackgroundResource(R.drawable.bg_thumbnail_background_half);
            }
            animBottomCover(i22, list3, i24, bottomMaskTargetHeight);
            StringBuilder sb = new StringBuilder();
            sb.append("provideAnimateElement: newMode = ");
            sb.append(i22);
            sb.append(", mCurrentMode = ");
            sb.append(this.mCurrentMode);
            sb.append(", animateInElements = ");
            sb.append(list3);
            Log.d(TAG, sb.toString());
            if (i22 != 163) {
                if (i22 != 169) {
                    if (i22 != 182) {
                        if (i22 != 254) {
                            if (i22 != 179) {
                                if (i22 != 180) {
                                    if (i22 != 204) {
                                        if (i22 != 205) {
                                            switch (i22) {
                                                case 165:
                                                    break;
                                                case 166:
                                                    break;
                                                case 167:
                                                    break;
                                                default:
                                                    switch (i22) {
                                                        case 171:
                                                            break;
                                                        case 172:
                                                            break;
                                                        case 173:
                                                            break;
                                                        default:
                                                            switch (i22) {
                                                                case 175:
                                                                    break;
                                                                case 176:
                                                                    break;
                                                                case 177:
                                                                    if (DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiCreate()) {
                                                                        z = false;
                                                                        i10 = 1;
                                                                        i8 = 1;
                                                                        i19 = 1;
                                                                        i4 = 1;
                                                                        i3 = 1;
                                                                        i18 = -1;
                                                                        i6 = -1;
                                                                        break;
                                                                    }
                                                                    break;
                                                                default:
                                                                    switch (i22) {
                                                                        case 184:
                                                                            MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
                                                                            if (mimojiStatusManager2.isInMimojiCreate()) {
                                                                                i20 = 1;
                                                                                i7 = 1;
                                                                                i9 = -1;
                                                                                i21 = -1;
                                                                                i5 = -1;
                                                                            } else {
                                                                                if (CameraSettings.isGifOn()) {
                                                                                    i21 = 1;
                                                                                    i5 = 1;
                                                                                    i9 = -1;
                                                                                } else {
                                                                                    i9 = 1;
                                                                                    i21 = 1;
                                                                                    i5 = 1;
                                                                                }
                                                                                i20 = -1;
                                                                                i7 = -1;
                                                                            }
                                                                            if (i9 == 1 && i23 != 5) {
                                                                                setRecordingSwitchTarget(mimojiStatusManager2.isInMimojiPhoto());
                                                                            }
                                                                            z = false;
                                                                            i4 = 1;
                                                                            i3 = 1;
                                                                            i10 = i20;
                                                                            i8 = 1;
                                                                            break;
                                                                        case 185:
                                                                            break;
                                                                        case 186:
                                                                        case 187:
                                                                        case 188:
                                                                            break;
                                                                        case 189:
                                                                            break;
                                                                        default:
                                                                            switch (i22) {
                                                                                case 207:
                                                                                    z = false;
                                                                                    i10 = 1;
                                                                                    i8 = 1;
                                                                                    i6 = 1;
                                                                                    i4 = 1;
                                                                                    i3 = 1;
                                                                                    break;
                                                                                case 208:
                                                                                case 212:
                                                                                    break;
                                                                                case 209:
                                                                                case 210:
                                                                                case 211:
                                                                                    break;
                                                                                case 213:
                                                                                    break;
                                                                                case 214:
                                                                                    break;
                                                                            }
                                                                    }
                                                            }
                                                    }
                                            }
                                        }
                                    } else if (C0122O00000o.instance().OOO000o()) {
                                        if (!DataRepository.dataItemRunning().getComponentRunningDualVideo().ismDrawSelectWindow()) {
                                            z = false;
                                            i10 = 1;
                                            i8 = 1;
                                            i5 = 1;
                                            i4 = 1;
                                            i3 = 1;
                                            i9 = -1;
                                            i7 = -1;
                                            i6 = -1;
                                        }
                                    }
                                }
                                int i27 = C0122O00000o.instance().OOoO0o() ? 1 : -1;
                                setRecordingSwitchTarget(i22 == 167);
                                i16 = i27;
                                boolean z4 = false;
                                int i28 = 1;
                                i14 = i28;
                                i13 = i14;
                                i12 = i13;
                                i11 = i12;
                                i15 = -1;
                                i7 = -1;
                            }
                            z2 = false;
                            i17 = 1;
                            i14 = 1;
                            i13 = 1;
                            i12 = 1;
                            i11 = 1;
                            i15 = -1;
                            i16 = -1;
                            i7 = -1;
                        } else {
                            z = false;
                            i8 = 1;
                            i6 = 1;
                            i5 = 1;
                            i4 = 1;
                            i10 = -1;
                            i9 = -1;
                            i7 = -1;
                            i3 = -1;
                        }
                        this.mCameraPickEnable = i10 != 1;
                        this.mBackEnable = i7 != 1;
                        MasterFilterProtocol masterFilterProtocol = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
                        if (masterFilterProtocol != null && masterFilterProtocol.isShowing() && i23 == 7) {
                            i6 = -1;
                        }
                        if (i6 != 1) {
                            this.mModeSelectLayout.clearBottomMenu();
                            AlphaInOnSubscribe.directSetResult(this.mEdgeHorizonScrollView);
                            if (!this.mComponentModuleList.isCommonMode(i22)) {
                                this.mDragUpLayout.setDragEnable(false);
                                AlphaOutOnSubscribe.directSetResult(this.mModeSelectView);
                                String geItemStringName = DataRepository.dataItemGlobal().getComponentModuleList().geItemStringName(i22, true);
                                if (!TextUtils.isEmpty(geItemStringName)) {
                                    this.mExternalModeTipLayout.setEnabled(true);
                                    this.mExternalModeTipText.setText(geItemStringName);
                                    if (list3 == null || ((Integer) this.mExternalModeTipLayout.getTag()).intValue() == 1) {
                                        AlphaInOnSubscribe.directSetResult(this.mExternalModeTipLayout);
                                    } else {
                                        list3.add(Completable.create(new RotateOnSubscribe(this.mExternalModeTipIcon).setRotateDegree(-90, 0).setInterpolator(new miuix.view.animation.CubicEaseOutInterpolator()).setDurationTime(300)));
                                        this.mExternalModeTipLayout.start();
                                    }
                                    this.mExternalModeTipLayout.setTag(Integer.valueOf(1));
                                } else {
                                    this.mExternalModeTipLayout.setTag(Integer.valueOf(-1));
                                    AlphaOutOnSubscribe.directSetResult(this.mExternalModeTipLayout);
                                }
                                list2 = null;
                            } else {
                                this.mExternalModeTipLayout.setTag(Integer.valueOf(-1));
                                AlphaOutOnSubscribe.directSetResult(this.mExternalModeTipLayout);
                                StandaloneRecorderProtocol standaloneRecorderProtocol = (StandaloneRecorderProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(429);
                                list2 = null;
                                boolean z5 = standaloneRecorderProtocol != null && standaloneRecorderProtocol.getRecorderManager(null).isRecordingPaused();
                                if ((!this.mIsShowLighting || !this.mIsShowMiMoji) && !z5) {
                                    this.mModeSelectView.setEnabled(true);
                                    AlphaInOnSubscribe.directSetResult(this.mModeSelectView);
                                }
                            }
                            if (z3) {
                                if (this.mModeSelectView.getAdapter() != null) {
                                    this.mModeSelectView.getAdapter().notifyDataSetChanged();
                                }
                                this.mModeSelectView.moveToPosition(i22);
                            }
                        } else {
                            list2 = null;
                        }
                        if (!isLeftLandscapeMode()) {
                            while (i25 < this.mRotateViews.size()) {
                                ((View) this.mRotateViews.get(i25)).setRotation(90.0f);
                                i25++;
                            }
                        } else {
                            while (i25 < this.mRotateViews.size()) {
                                ((View) this.mRotateViews.get(i25)).setRotation((float) this.mDegree);
                                i25++;
                            }
                        }
                        if (i3 == 1) {
                            FolmeAlphaOutOnSubscribe.directSetResult(this.mCurrentBottomParent);
                            return;
                        }
                        FolmeAlphaInOnSubscribe.directSetResult(this.mCurrentBottomParent);
                        if (!z) {
                            list2 = list3;
                        }
                        animateViews(i4, list2, this.mShutterButton.getAlpha(), this.mShutterButton);
                        animateViews(i10, list3, (View) this.mCameraPicker);
                        animateViews(i8, list3, this.mModeSelectLayout.getView());
                        animateViews(i5, list3, (View) this.mThumbnailImageLayout);
                        animateViews(i9, list3, (View) this.mRecordingSwitch);
                        animateViews(i7, list3, (View) this.mMimojiBack);
                        return;
                    }
                    z = false;
                    i6 = 1;
                    i4 = 1;
                    i3 = 1;
                    i10 = -1;
                    i18 = -1;
                    i8 = -1;
                    i19 = -1;
                    i5 = -1;
                    this.mCameraPickEnable = i10 != 1;
                    this.mBackEnable = i7 != 1;
                    MasterFilterProtocol masterFilterProtocol2 = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
                    i6 = -1;
                    if (i6 != 1) {
                    }
                    if (!isLeftLandscapeMode()) {
                    }
                    if (i3 == 1) {
                    }
                } else {
                    i15 = C0122O00000o.instance().OOO0OOo() ? 1 : -1;
                    z2 = false;
                    i17 = 1;
                    i14 = i17;
                    i13 = i14;
                    i12 = i13;
                    i11 = i12;
                    i16 = -1;
                    i7 = -1;
                    this.mCameraPickEnable = i10 != 1;
                    this.mBackEnable = i7 != 1;
                    MasterFilterProtocol masterFilterProtocol22 = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
                    i6 = -1;
                    if (i6 != 1) {
                    }
                    if (!isLeftLandscapeMode()) {
                    }
                    if (i3 == 1) {
                    }
                }
            }
            z2 = false;
            i15 = 1;
            i17 = 1;
            i14 = i17;
            i13 = i14;
            i12 = i13;
            i11 = i12;
            i16 = -1;
            i7 = -1;
            this.mCameraPickEnable = i10 != 1;
            this.mBackEnable = i7 != 1;
            MasterFilterProtocol masterFilterProtocol222 = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
            i6 = -1;
            if (i6 != 1) {
            }
            if (!isLeftLandscapeMode()) {
            }
            if (i3 == 1) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        int[] iArr;
        if (i == 240) {
            return null;
        }
        if (i != 65530) {
            iArr = new int[]{161};
        } else {
            ViewCompat.setTranslationY(getView(), 0.0f);
            iArr = new int[]{161};
        }
        Animation wrapperAnimation = FragmentAnimationFactory.wrapperAnimation(iArr);
        wrapperAnimation.setStartOffset(150);
        return wrapperAnimation;
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation(int i) {
        if (i == 65530) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.vv_start_layout_height_extra) + Display.getBottomMargin() + getResources().getDimensionPixelSize(R.dimen.vv_list_height);
            ViewCompat.setTranslationY(getView(), (float) (dimensionPixelSize - getView().getHeight()));
        }
        return FragmentAnimationFactory.wrapperAnimation(162);
    }

    public void provideOrientationChanged(int i, List list, int i2) {
        super.provideOrientationChanged(i, list, i2);
        DragLayout.getAnimationConfig().calDragLayoutHeight(getContext(), this.mComponentModuleList.getMoreItems().size());
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mDragUpLayout.getDragChildren().getLayoutParams();
        marginLayoutParams.topMargin = Display.getDragLayoutTopMargin();
        marginLayoutParams.height = Display.getBottomHeight() + ((int) DragLayout.getAnimationConfig().getMaxDragDistance());
        DragLayout dragLayout = this.mDragUpLayout;
        if (dragLayout != null) {
            dragLayout.setScreenOrientation(i);
        }
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        if (!isLeftLandscapeMode()) {
            list.addAll(this.mRotateViews);
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(2561, this);
        modeCoordinator.attachProtocol(179, this);
        modeCoordinator.attachProtocol(162, this);
        modeCoordinator.attachProtocol(183, this);
        modeCoordinator.attachProtocol(197, this);
        registerBackStack(modeCoordinator, this);
    }

    public void resetModeSelectView(int i) {
        this.mModeSelectView.moveToPosition(i);
    }

    public void resetToCommonMode() {
        Log.u(TAG, "resetToCommonMode");
        if (CameraSettings.isPopupMoreStyle()) {
            int curSelectMode = this.mModeSelectView.getCurSelectMode();
            if (!this.mComponentModuleList.isCommonMode(curSelectMode)) {
                curSelectMode = 163;
            }
            changeModeByNewMode(curSelectMode, getString(R.string.module_name_capture), 0);
        } else if (CameraSettings.getMoreModeStyle() == 0) {
            this.mModeSelectView.moveToPosition(163);
            changeModeByNewMode(163, getString(R.string.module_name_capture), 0);
        }
    }

    public void setBackgroundColor(int i) {
        ImageView imageView;
        int i2;
        if (i == 1) {
            imageView = this.mCameraPicker;
            i2 = R.drawable.bg_mode_item;
        } else if (i == 2) {
            imageView = this.mCameraPicker;
            i2 = R.drawable.bg_thumbnail_background_full;
        } else {
            return;
        }
        imageView.setBackgroundResource(i2);
        this.mThumbnailImage.setBackgroundResource(i2);
        this.mRecordingPause.setBackgroundResource(i2);
        this.mRecordingSnap.setBackgroundResource(i2);
    }

    public void setClickEnable(boolean z) {
        super.setClickEnable(z);
        this.mShutterButton.setSnapClickEnable(z);
    }

    public void setLightingViewStatus(boolean z) {
        this.mIsShowLighting = z;
    }

    public void setModeLayoutVisibility(int i, boolean z) {
        View view = (this.mExternalModeTipLayout.getTag() == null || ((Integer) this.mExternalModeTipLayout.getTag()).intValue() != 1) ? this.mModeSelectView : this.mExternalModeTipLayout;
        if (view.getVisibility() != i) {
            view.setVisibility(i);
            if (z) {
                ModeSelectView modeSelectView = this.mModeSelectView;
                if (view == modeSelectView) {
                    ViewCompat.setScaleX(modeSelectView, 1.1f);
                    ViewCompat.animate(this.mModeSelectView).scaleX(1.0f).setDuration(400).setInterpolator(new CubicEaseOutInterpolator()).start();
                }
            }
            if (i == 0) {
                switchMoreMode(true);
            } else {
                this.mDragUpLayout.setDragEnable(false);
            }
        }
    }

    public void showCameraPicker(boolean z) {
        animateViews(z ? 1 : -1, (List) null, (View) this.mCameraPicker);
    }

    public void showDocumentReviewViews(Bitmap bitmap, float[] fArr, Size size) {
        if (getActivity() != null && bitmap != null && !bitmap.isRecycled() && fArr.length >= 8) {
            ActivityBase activityBase = (ActivityBase) getActivity();
            if (((BaseModule) activityBase.getCurrentModule()) != null) {
                if (this.mDocumentContainer == null) {
                    this.mDocumentContainer = LayoutInflater.from(getContext()).inflate(R.layout.layout_document_preview, null);
                    ((ViewGroup) activityBase.findViewById(R.id.camera_app_root)).addView(this.mDocumentContainer);
                }
                final AdjustAnimationView adjustAnimationView = (AdjustAnimationView) this.mDocumentContainer.findViewById(R.id.document_review_image);
                adjustAnimationView.setVisibility(0);
                adjustAnimationView.setPreviewSize(size);
                adjustAnimationView.setBitmap(bitmap, fArr);
                Rect imageRect = adjustAnimationView.getImageRect();
                final AnimationView animationView = (AnimationView) this.mDocumentContainer.findViewById(R.id.document_anim_image);
                animationView.setImageBitmap(bitmap);
                LayoutParams layoutParams = (LayoutParams) animationView.getLayoutParams();
                layoutParams.topMargin = imageRect.top;
                layoutParams.leftMargin = imageRect.left;
                layoutParams.width = imageRect.width();
                layoutParams.height = imageRect.height();
                animationView.setLayoutParams(layoutParams);
                this.mThumbnailImageLayout.setVisibility(4);
                final ViewGroup viewGroup = this.mThumbnailImageLayout;
                adjustAnimationView.startAnim(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        animationView.setVisibility(0);
                        adjustAnimationView.clearBitmap();
                        FragmentBottomAction.this.initAnimation(adjustAnimationView, animationView, viewGroup);
                    }
                }, Util.getEnterDuration());
            }
        }
    }

    public boolean showDragAnimation(int i, int i2) {
        boolean z = false;
        if (this.mBottomActionView.getVisibility() == 0 && this.mShutterButton.inRegion(i, i2)) {
            return false;
        }
        if (this.mThumbnailImageLayout.getVisibility() == 0 && Util.isInViewRegion(this.mThumbnailImageLayout, i, i2)) {
            return false;
        }
        if (this.mCameraPicker.getVisibility() == 0 && Util.isInViewRegion(this.mCameraPicker, i, i2)) {
            return false;
        }
        if (this.mRecordingSwitch.getVisibility() == 0 && Util.isInViewRegion(this.mRecordingSwitch, i, i2)) {
            return false;
        }
        if (this.mModeSelectView.getVisibility() == 0 && Util.isInViewRegion(this.mModeSelectView, i, i2)) {
            return false;
        }
        if (this.mBottomActionView.getVisibility() == 0) {
            z = true;
        }
        return z;
    }

    public void showOrHideBottom(boolean z) {
        Completable.create(z ? new AlphaInOnSubscribe(this.mCurrentBottomParent) : new AlphaOutOnSubscribe(this.mCurrentBottomParent)).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void showOrHideBottomViewWithAnim(boolean z) {
    }

    public boolean showOrHideFilterView() {
        return false;
    }

    public boolean showOrHideLightingView() {
        this.mIsShowLighting = !this.mIsShowLighting;
        return this.mIsShowLighting;
    }

    public void showOrHideLoadingProgress(boolean z, boolean z2) {
        if (z2) {
            CameraSnapView cameraSnapView = this.mShutterButton;
            if (z) {
                cameraSnapView.hideRoundPaintItem();
            } else {
                cameraSnapView.showRoundPaintItem();
            }
        }
        setProgressBarVisible(z ? 0 : 8);
    }

    public boolean showOrHideMiMojiView() {
        this.mIsShowMiMoji = !this.mIsShowMiMoji;
        return this.mIsShowMiMoji;
    }

    public void showOrHideMimojiProgress(boolean z) {
        int i = this.mCurrentMode;
        int i2 = 0;
        BottomAnimationConfig configVariables = BottomAnimationConfig.generate(true, i, false, CameraSettings.isAlgoFPS(i), CameraSettings.isVideoBokehOn()).configVariables();
        configVariables.mIsInMimojiCreate = true;
        this.mShutterButton.triggerAnimation(configVariables);
        if (!z) {
            i2 = 8;
        }
        setProgressBarVisible(i2);
    }

    public boolean shrink(boolean z) {
        DragLayout dragLayout = this.mDragUpLayout;
        if (dragLayout != null) {
            return dragLayout.shrink(z);
        }
        return false;
    }

    public void switchModeOrExternalTipLayout(boolean z) {
        if (isAdded()) {
            StringBuilder sb = new StringBuilder();
            sb.append("switchModeOrExternalTipLayout: ");
            sb.append(z);
            Log.d(TAG, sb.toString());
            if (z) {
                showModeOrExternalTipLayout(true);
            } else {
                hideModeOrExternalTipLayout();
            }
        }
    }

    public void switchThumbnailFunction(boolean z) {
        if (z) {
            initThumbnailAsThumbnail(true);
        } else {
            initThumbnailAsExit();
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        this.mHandler.removeCallbacksAndMessages(null);
        modeCoordinator.detachProtocol(2561, this);
        modeCoordinator.detachProtocol(179, this);
        modeCoordinator.detachProtocol(162, this);
        modeCoordinator.detachProtocol(183, this);
        modeCoordinator.detachProtocol(197, this);
        unRegisterBackStack(modeCoordinator, this);
    }

    public void updateInstallState(int i) {
        CameraSnapView cameraSnapView;
        int i2;
        int scope = VMFeature.getScope(i);
        if (scope != 16) {
            if (scope == 256) {
                processingFailed();
                return;
            } else if (scope == 4096) {
                i2 = VMFeature.getDownloadingProgress(i);
                cameraSnapView = this.mShutterButton;
            } else {
                return;
            }
        } else if (i == 17) {
            processingPrepare();
            return;
        } else if (i == 19) {
            cameraSnapView = this.mShutterButton;
            i2 = 100;
        } else if (i == 21) {
            processingFinish();
            return;
        } else {
            return;
        }
        cameraSnapView.setSpecificProgress(i2);
    }

    public void updateLoading(boolean z) {
        Handler handler;
        int i;
        if (z) {
            this.mInLoading = false;
            this.mHandler.removeCallbacksAndMessages(null);
            this.mThumbnailProgress.setVisibility(8);
        } else if (!Util.isSaveToHidenFolder(this.mCurrentMode) && !DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(this.mCurrentMode) && !DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting() && !this.mIsIntentAction) {
            this.mInLoading = true;
            int i2 = this.mCurrentMode;
            if (i2 == 161 || i2 == 162 || i2 == 166 || i2 == 172 || i2 == 174 || i2 == 176 || i2 == 183) {
                handler = this.mHandler;
                i = this.mRecordProgressDelay;
            } else {
                handler = this.mHandler;
                i = this.mCaptureProgressDelay;
            }
            handler.sendEmptyMessageDelayed(1, (long) i);
        }
    }

    public void updatePauseAndCaptureView(boolean z) {
        Completable completable;
        if (z) {
            if (this.mVideoPauseSupported) {
                Completable.create(new AlphaInOnSubscribe(this.mRecordingPause)).subscribe();
            }
            if (this.mVideoCaptureEnable) {
                completable = Completable.create(new AlphaInOnSubscribe(this.mRecordingSnap));
            } else {
                return;
            }
        } else {
            if (this.mVideoPauseSupported) {
                Completable.create(new AlphaOutOnSubscribe(this.mRecordingPause).targetGone()).subscribe();
            }
            if (this.mVideoCaptureEnable) {
                completable = Completable.create(new AlphaOutOnSubscribe(this.mRecordingSnap).targetGone());
            } else {
                return;
            }
        }
        completable.subscribe();
    }

    public void updateRecordingTime(String str) {
        this.mBottomRecordingTime.setText(str);
    }

    public void updateResourceState(int i) {
        CameraSnapView cameraSnapView;
        float f;
        int i2;
        if (i != 0) {
            if (i == 7) {
                i2 = 1;
                if (this.mShutterButton.getTag() == null || ((Integer) this.mShutterButton.getTag()).intValue() != 1) {
                    cameraSnapView = this.mShutterButton;
                    f = 1.0f;
                } else {
                    return;
                }
            }
        }
        i2 = -1;
        if (this.mShutterButton.getTag() == null || ((Integer) this.mShutterButton.getTag()).intValue() != -1) {
            cameraSnapView = this.mShutterButton;
            f = 0.5f;
        } else {
            return;
        }
        cameraSnapView.setAlpha(f);
        this.mShutterButton.setTag(Integer.valueOf(i2));
    }

    public void updateThumbnail(Thumbnail thumbnail, boolean z, int i) {
        if (isAdded()) {
            ActivityBase activityBase = (ActivityBase) getContext();
            if (!DataRepository.dataItemGlobal().getStartFromKeyguard() || i == activityBase.hashCode()) {
                ThumbnailUpdater thumbnailUpdater = activityBase.getThumbnailUpdater();
                if (!(thumbnailUpdater == null || thumbnailUpdater.getThumbnail() == thumbnail)) {
                    thumbnailUpdater.setThumbnail(thumbnail, false, false);
                    Log.d(TAG, "inconsistent thumbnail");
                }
                this.mHandler.removeCallbacksAndMessages(null);
                this.mInLoading = false;
                if (this.mThumbnailProgress.getVisibility() != 8) {
                    this.mThumbnailProgress.setVisibility(8);
                }
                if (!this.mIsIntentAction) {
                    if (thumbnail == null) {
                        this.mThumbnailImage.setImageDrawable(null);
                        return;
                    }
                    RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(getResources(), thumbnail.getBitmap());
                    create.getPaint().setAntiAlias(true);
                    create.setCircular(true);
                    this.mThumbnailImage.setImageDrawable(create);
                    if (z && !this.mVideoRecordingStarted) {
                        ViewCompat.setAlpha(this.mThumbnailImageLayout, 0.3f);
                        ViewCompat.setScaleX(this.mThumbnailImageLayout, 1.3f);
                        ViewCompat.setScaleY(this.mThumbnailImageLayout, 1.3f);
                        ViewCompat.animate(this.mThumbnailImageLayout).alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setListener(new ViewPropertyAnimatorListener() {
                            public void onAnimationCancel(View view) {
                                ViewCompat.setAlpha(FragmentBottomAction.this.mThumbnailImageLayout, 1.0f);
                                ViewCompat.setScaleX(FragmentBottomAction.this.mThumbnailImageLayout, 1.0f);
                                ViewCompat.setScaleY(FragmentBottomAction.this.mThumbnailImageLayout, 1.0f);
                            }

                            public void onAnimationEnd(View view) {
                            }

                            public void onAnimationStart(View view) {
                            }
                        }).setDuration(80).start();
                    }
                }
            }
        }
    }
}
