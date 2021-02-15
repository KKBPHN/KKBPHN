package com.android.camera.protocol;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.SensorEvent;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.net.Uri;
import android.util.Pair;
import android.util.Size;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSize;
import com.android.camera.Thumbnail;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.aiwatermark.lisenter.IASDListener;
import com.android.camera.aiwatermark.lisenter.IPermissionListener;
import com.android.camera.ambilight.AmbilightEngine.AmbilightSceneMode;
import com.android.camera.animation.AnimationComposite;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.data.data.config.ComponentManuallyET;
import com.android.camera.data.data.config.ComponentManuallyEV;
import com.android.camera.data.data.config.ComponentManuallyFocus;
import com.android.camera.data.data.config.ComponentManuallyISO;
import com.android.camera.data.data.config.ComponentManuallyWB;
import com.android.camera.data.data.config.ComponentRunningMasterFilter;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;
import com.android.camera.data.data.runing.ComponentRunningFastMotion;
import com.android.camera.data.data.runing.ComponentRunningFastMotionPro;
import com.android.camera.data.data.runing.ComponentRunningShine;
import com.android.camera.data.data.runing.ComponentRunningShine.ShineType;
import com.android.camera.dualvideo.recorder.MultiRecorderManager;
import com.android.camera.dualvideo.remote.RemoteService;
import com.android.camera.dualvideo.render.RenderManager;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.fragment.clone.Status;
import com.android.camera.fragment.film.FilmItem;
import com.android.camera.fragment.lifeCircle.BaseLifecycleListener;
import com.android.camera.fragment.manually.ManuallyListener;
import com.android.camera.fragment.mimoji.MimojiInfo;
import com.android.camera.fragment.vv.VVItem;
import com.android.camera.fragment.vv.VVWorkspaceItem;
import com.android.camera.module.loader.StartControl;
import com.android.camera.storage.ImageSaver;
import com.android.camera.ui.FocusView.ExposureViewListener;
import com.android.camera.ui.ObjectView.ObjectViewListener;
import com.android.camera.ui.VideoTagView;
import com.android.camera2.Camera2Proxy.PreviewCallback;
import com.android.camera2.CameraHardwareFace;
import com.android.camera2.autozoom.AutoZoomCaptureResult;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.ss.android.vesdk.TERecorder.OnConcatFinishedListener;
import com.ss.android.vesdk.VECommonCallback;
import com.xiaomi.fenshen.FenShenCam.Mode;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public interface ModeProtocol {
    public static final int ADJUST_EV_START = 1;
    public static final int ADJUST_EV_STOP = 2;
    public static final int APPLY_EV_IMMEDIATELY = 3;
    public static final int EVENT_AI_WATERMARK = 21;
    public static final int EVENT_AMBILIGHT = 24;
    public static final int EVENT_BEAUTY = 2;
    public static final int EVENT_BLANK_BEAUTY = 10;
    public static final int EVENT_CLONE_USE_GUIDE = 23;
    public static final int EVENT_DOLLY_ZOOM_USE_GUIDE = 39;
    public static final int EVENT_FAST_MOTION = 34;
    public static final int EVENT_FAST_MOTION_PRO = 36;
    public static final int EVENT_GIF_EDIT = 20;
    public static final int EVENT_INTENT_SHOT_FINISH = 6;
    public static final int EVENT_KALEIDOSCOPE = 18;
    public static final int EVENT_LIVE_REVIEW = 11;
    public static final int EVENT_LIVE_SPEED = 13;
    public static final int EVENT_LIVE_STICKER = 12;
    public static final int EVENT_MAKEUP = 3;
    public static final int EVENT_MASTER_FILTER = 37;
    public static final int EVENT_MIMOJI = 14;
    public static final int EVENT_MIMOJI_EMOTICON = 19;
    public static final int EVENT_MODE_EDIT = 32;
    public static final int EVENT_MODE_MORE_NORMAL = 30;
    public static final int EVENT_MODE_MORE_POPUP = 31;
    public static final int EVENT_PORTAIT_LIGHTING = 33;
    public static final int EVENT_RESTORE_BOTTOM_ACTION = 7;
    public static final int EVENT_RESTORE_FRAGMENT = 16;
    public static final int EVENT_SLOWSHUTTER_USE_GUIDE = 35;
    public static final int EVENT_SUBTITLE = 17;
    public static final int EVENT_TIMEFREEZE_USE_GUIDE = 38;
    public static final int EVENT_VV = 15;
    public static final int INDICATOR_FACE_VIEW = 1;
    public static final int INDICATOR_FOCUS_VIEW = 2;
    public static final int INDICATOR_OBJECT_VIEW = 3;
    public static final int PROTOCOL_ACTION = 161;
    public static final int PROTOCOL_ACTION_PROCESSING = 162;
    public static final int PROTOCOL_ACTION_TRACK = 186;
    public static final int PROTOCOL_AI_WATERMARK = 253;
    public static final int PROTOCOL_AI_WATERMARK_DETECT = 254;
    public static final int PROTOCOL_AMBILIGHT = 420;
    public static final int PROTOCOL_AMBILIGHT_SELECTOR = 421;
    public static final int PROTOCOL_AUTO_HIBERNATION = 936;
    public static final int PROTOCOL_AUTO_ZOOM_MODULE = 215;
    public static final int PROTOCOL_AUTO_ZOOM_VIEW = 214;
    public static final int PROTOCOL_BACK_STACK = 171;
    public static final int PROTOCOL_BEAUTY_RECORDING = 173;
    public static final int PROTOCOL_BEAUTY_SHOW_STATUS = 213;
    public static final int PROTOCOL_BLUETOOTH_HEADSET = 933;
    public static final int PROTOCOL_BOKEH_F_NUMBER = 210;
    public static final int PROTOCOL_BOTTOM_MENU = 197;
    public static final int PROTOCOL_BOTTOM_POPUP_TIPS = 175;
    public static final int PROTOCOL_CAMERA_CLICK_OBSERVABLE = 227;
    public static final int PROTOCOL_CAMERA_MODULE_SPEC = 195;
    public static final int PROTOCOL_CHANGE_GAIN = 935;
    public static final int PROTOCOL_CLONE_ACTION = 419;
    public static final int PROTOCOL_CLONE_CHOOSER = 416;
    public static final int PROTOCOL_CLONE_PROCESS = 418;
    public static final int PROTOCOL_CONFIG_CHANGE = 164;
    public static final int PROTOCOL_DELEGATE = 160;
    public static final int PROTOCOL_DISPLAY_FOLD_STATUS = 938;
    public static final int PROTOCOL_DOLLY_ZOOM_ACTION = 677;
    public static final int PROTOCOL_DOLLY_ZOOM_PROCESS = 676;
    public static final int PROTOCOL_DUAL_CONTROLLER = 182;
    public static final int PROTOCOL_DUAL_VIDEO_RENDER = 430;
    public static final int PROTOCOL_EFFECT_CONTROL = 165;
    public static final int PROTOCOL_EV_CHANGED = 169;
    public static final int PROTOCOL_FACE_BEAUTY = 185;
    public static final int PROTOCOL_FASTMOTION_PRO_ADJUST = 932;
    public static final int PROTOCOL_FAST_MOTION = 674;
    public static final int PROTOCOL_FILM_CHOOSER = 675;
    public static final int PROTOCOL_FILM_DREAM_CONFIG = 930;
    public static final int PROTOCOL_FILM_FREAM_PROCESS = 931;
    public static final int PROTOCOL_FILTER_SWITCHER = 183;
    public static final int PROTOCOL_FRONT_BEAUTY = 194;
    public static final int PROTOCOL_GALLERY_CONNECTION = 937;
    public static final int PROTOCOL_ID_CARD = 233;
    public static final int PROTOCOL_KALEIDOSCOPE = 236;
    public static final int PROTOCOL_KEY_EVENT = 239;
    public static final int PROTOCOL_LIVE_BEAUTY = 244;
    public static final int PROTOCOL_LIVE_CONFIG = 201;
    public static final int PROTOCOL_LIVE_CONFIG_VV = 228;
    public static final int PROTOCOL_LIVE_FILTER = 243;
    public static final int PROTOCOL_LIVE_MUSIC = 211;
    public static final int PROTOCOL_LIVE_SPEED = 232;
    public static final int PROTOCOL_LIVE_STATE = 245;
    public static final int PROTOCOL_LIVE_VIDEO_EDITOR = 209;
    public static final int PROTOCOL_LIVE_VV_CHOOSER = 229;
    public static final int PROTOCOL_LIVE_VV_PROCESS = 230;
    public static final int PROTOCOL_MAGNETIC_SENSOR_DETECT = 2576;
    public static final int PROTOCOL_MAIN_CONTENT = 166;
    public static final int PROTOCOL_MAKEUP_CONTROL = 180;
    public static final int PROTOCOL_MANUALLY_ADJUST = 181;
    public static final int PROTOCOL_MANUALLY_CHANGE = 174;
    public static final int PROTOCOL_MASTER_FILTER = 934;
    public static final int PROTOCOL_MIMOJI = 217;
    public static final int PROTOCOL_MIMOJI2 = 246;
    public static final int PROTOCOL_MIMOJI_ALERT = 226;
    public static final int PROTOCOL_MIMOJI_BOTTOM_LIST = 248;
    public static final int PROTOCOL_MIMOJI_DATA_BASE = 225;
    public static final int PROTOCOL_MIMOJI_EDITOR = 224;
    public static final int PROTOCOL_MIMOJI_EDITOR2 = 247;
    public static final int PROTOCOL_MIMOJI_EMOTICON = 250;
    public static final int PROTOCOL_MIMOJI_FULL_SCREEN = 249;
    public static final int PROTOCOL_MIMOJI_GIF_EDITOR = 251;
    public static final int PROTOCOL_MIMOJI_GIF_RECORD = 237;
    public static final int PROTOCOL_MIMOJI_VIDEO_EDITOR = 252;
    public static final int PROTOCOL_MI_ASD_DETECT = 235;
    public static final int PROTOCOL_MI_LIVE_CONFIG = 241;
    public static final int PROTOCOL_MI_LIVE_PLAYER = 242;
    public static final int PROTOCOL_MODE_CHANGE_CONTROLLER = 179;
    public static final int PROTOCOL_MODE_LIST_MANAGER = 2560;
    public static final int PROTOCOL_MODULE_CONTENT = 431;
    public static final int PROTOCOL_MORE_MODE_POPUP = 2561;
    public static final int PROTOCOL_MULTI_FEATURE_MANAGER = 929;
    public static final int PROTOCOL_ON_FACE_BEAUTY_CHANGED = 199;
    public static final int PROTOCOL_ON_SHINE_CHANGED = 234;
    public static final int PROTOCOL_OPTICAL_ZOOM_RATIO = 203;
    public static final int PROTOCOL_PANORAMA = 176;
    public static final int PROTOCOL_PLAY_VIDEO = 167;
    public static final int PROTOCOL_PORTRAIT_LIGHTING = 672;
    public static final int PROTOCOL_PRESENTATION_DISPLAY = 945;
    public static final int PROTOCOL_PREVIEW_CHANGED = 168;
    public static final int PROTOCOL_RECORDING_STATE = 212;
    public static final int PROTOCOL_REMOTE_CAMERA_SERVICE = 422;
    public static final int PROTOCOL_SCREEN_LIGHT = 196;
    public static final int PROTOCOL_SNAP_INDICATOR = 184;
    public static final int PROTOCOL_SPEECH_SHUTTER_DETECT = 255;
    public static final int PROTOCOL_STANDALONE_MACRO = 202;
    public static final int PROTOCOL_STANDALONE_RECORDER = 429;
    public static final int PROTOCOL_STICKER_CONTROL = 178;
    public static final int PROTOCOL_SUBTITLE = 231;
    public static final int PROTOCOL_TOP_ALERT = 172;
    public static final int PROTOCOL_TOP_CONFIG = 193;
    public static final int PROTOCOL_ULTRA_WIDE = 200;
    public static final int PROTOCOL_VERTICAL = 198;
    public static final int PROTOCOL_WIDE_SELFIE = 216;
    public static final int PROTOCOL_ZOOM_CHANGED = 170;
    public static final int SHOW_FAIL = 3;
    public static final int SHOW_START = 1;
    public static final int SHOW_SUCCESS = 2;
    public static final int TOP_ALERT_EVENT = 673;

    public interface AIWatermarkDetect extends BaseProtocol {
        public static final int TYPE_TAG = 254;

        void onASDChange(int i);

        void resetResult();

        void setListener(IASDListener iASDListener);
    }

    public interface ActionProcessing extends BaseProtocol {
        public static final int TYPE_TAG = 162;

        void adjustViewBackground();

        void animBottomBlackCover();

        void announceForAccessibility(@StringRes int i);

        void enableStopButton(boolean z, boolean z2);

        void entryOrExitMiMojiGif(boolean z);

        void filterUiChange();

        boolean forceSwitchFront();

        void hideDocumentReviewViews();

        void hideExtra();

        boolean isShowFilterView();

        boolean isShowLightingView();

        void processingAudioCapture(boolean z);

        void processingFailed();

        void processingFinish();

        void processingLongExposePrepare();

        void processingLongExposeStart();

        void processingMimojiBack();

        void processingMimojiPrepare();

        void processingPause();

        void processingPostAction();

        void processingPrepare();

        void processingResume();

        void processingSpeechShutter(boolean z);

        void processingStart();

        void processingWorkspace(boolean z);

        void setBackgroundColor(int i);

        void setLightingViewStatus(boolean z);

        void showCameraPicker(boolean z);

        void showDocumentReviewViews(Bitmap bitmap, float[] fArr, Size size);

        void showOrHideBottom(boolean z);

        void showOrHideBottomViewWithAnim(boolean z);

        boolean showOrHideFilterView();

        boolean showOrHideLightingView();

        void showOrHideLoadingProgress(boolean z, boolean z2);

        boolean showOrHideMiMojiView();

        void showOrHideMimojiProgress(boolean z);

        void switchModeOrExternalTipLayout(boolean z);

        void switchThumbnailFunction(boolean z);

        void updateInstallState(int i);

        void updateLoading(boolean z);

        void updatePauseAndCaptureView(boolean z);

        void updateRecordingTime(String str);

        void updateResourceState(int i);

        void updateThumbnail(Thumbnail thumbnail, boolean z, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AdjustEvState {
    }

    public interface AmbilightProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 420;

        boolean getAutoFinish();

        int getDuration();

        boolean getPressAnimationEnabled();

        void onSceneModeSelect(@AmbilightSceneMode int i);

        void onShutterAnimationEnd();

        boolean shouldDisableStopButton();
    }

    public interface AmbilightSelector extends BaseProtocol {
        public static final int TYPE_TAG = 421;

        void onRecordingPrepare();

        void onRecordingStop();

        void setSelectorLayoutVisible(boolean z);

        void updateDebugInfo(String str);

        void updateTips();
    }

    public interface AutoHibernation extends BaseProtocol {
        public static final int TYPE_TAG = 936;

        void dismissAutoHibernation();

        void setOrientation(int i);

        void startAutoHibernationCaptureDelayNumber();

        void stopAutoHibernationCaptureDelayNumber();

        void updateAutoHibernationFirstRecordingTime(String str, String str2);

        void updateAutoHibernationRecordingTimeOrCaptureCount(String str, String str2);

        void updateTimerBurstAnimator();
    }

    public interface AutoZoomModuleProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 215;

        void onTrackLost();

        void onTrackLosting();

        void setAutoZoomMode(int i);

        void setAutoZoomStartCapture(RectF rectF);

        void setAutoZoomStopCapture(int i);

        void startAutoZoom();

        void startTracking(RectF rectF);

        void stopAutoZoom();

        void stopTracking(int i);
    }

    public interface AutoZoomViewProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 214;

        void feedData(AutoZoomCaptureResult autoZoomCaptureResult);

        boolean isAutoZoomActive();

        boolean isAutoZoomEnabled();

        void onAutoZoomStarted();

        void onAutoZoomStopped();

        void onTrackingStarted(RectF rectF);

        void onTrackingStopped(int i);
    }

    public interface BackStack extends BaseProtocol {
        public static final int TYPE_TAG = 171;

        void addInBackStack(HandleBackTrace handleBackTrace);

        boolean handleBackStackFromKeyBack();

        void handleBackStackFromLongPressShutter();

        void handleBackStackFromShutter();

        boolean handleBackStackFromTapDown(int i, int i2);

        void handleBackStackFromTimerBurstShutter();

        void removeBackStack(HandleBackTrace handleBackTrace);
    }

    public interface BaseDelegate extends BaseProtocol {
        public static final int TYPE_TAG = 160;

        void delegateEvent(int i);

        Disposable delegateMode(@Nullable Completable completable, StartControl startControl, BaseLifecycleListener baseLifecycleListener);

        int getActiveFragment(@IdRes int i);

        AnimationComposite getAnimationComposite();

        int getOriginalFragment(@IdRes int i);

        void init(FragmentManager fragmentManager, int i, BaseLifecycleListener baseLifecycleListener);

        void updateCurrentFragments(@IdRes int i, int i2, int i3);
    }

    public interface BaseProtocol {
        public static final int TYPE_TAG = -1;

        void registerProtocol();

        void unRegisterProtocol();
    }

    public interface BeautyRecording extends BaseProtocol {
        public static final int TYPE_TAG = 173;

        void addBeautyStack(HandleBeautyRecording handleBeautyRecording);

        void handleAngleChang(float f);

        void handleBeautyRecordingStart();

        void handleBeautyRecordingStop();

        void removeBeautyStack(HandleBeautyRecording handleBeautyRecording);
    }

    public interface BluetoothHeadset extends BaseProtocol {
        public static final int TYPE_TAG = 933;

        boolean isBluetoothScoOn();

        boolean isSupportBluetoothSco(int i);

        void startBluetoothSco(int i);

        void stopBluetoothSco(int i);
    }

    public interface BottomMenuProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 197;

        void animateShineBeauty(boolean z);

        void expandAIWatermarkBottomMenu(ComponentRunningAIWatermark componentRunningAIWatermark);

        void expandMasterFilterBottomMenu(ComponentRunningMasterFilter componentRunningMasterFilter);

        void expandShineBottomMenu(ComponentRunningShine componentRunningShine);

        int getBeautyActionMenuType();

        void onBottomMenuAnimate(int i, int i2);

        void onRestoreCameraActionMenu(int i);

        void onSwitchCameraPicker();

        void onSwitchFastMotionAction(ComponentRunningFastMotion componentRunningFastMotion);

        void onSwitchFastMotionProAction(ComponentRunningFastMotionPro componentRunningFastMotionPro);

        void onSwitchLiveActionMenu(int i);

        void pauseRecording();

        void setModeLayoutVisibility(int i, boolean z);
    }

    public interface BottomPopupTips extends BaseProtocol, LyingDirectHint {
        public static final int FILTER_CLOSE_TYPE = 1;
        public static final int LIGHT_CLOSE_TYPE = 2;
        public static final int NONE_CLOSE_TYPE = 0;
        public static final int TIP_DURATION_2S = 5;
        public static final int TIP_DURATION_3S = 6;
        public static final int TIP_DURATION_6S = 7;
        public static final int TIP_DURATION_LONG = 2;
        public static final int TIP_DURATION_PERSISTED = 4;
        public static final int TIP_DURATION_SHORT = 1;
        public static final int TIP_DURATION_STABLE = 3;
        public static final int TIP_SHOW_ALL = 0;
        public static final int TIP_SHOW_LANDSCAPE = 1;
        public static final int TIP_SHOW_PORTRAIT = 2;
        public static final int TIP_TYPE_3A_LOCK = 8;
        public static final int TIP_TYPE_AI_FOOD = 20;
        public static final int TIP_TYPE_CINEMATIC_ASPECT_RATIO = 21;
        public static final int TIP_TYPE_DUAL_CAMERA = 6;
        public static final int TIP_TYPE_DUAL_CAMERA_SUCCESS = 7;
        public static final int TIP_TYPE_EYE_LIGHT = 10;
        public static final int TIP_TYPE_GROUP_SWITCH = 17;
        public static final int TIP_TYPE_HAND_GESTURE = 16;
        public static final int TIP_TYPE_HINT = 4;
        public static final int TIP_TYPE_LIGHTING = 12;
        public static final int TIP_TYPE_MACRO_MODE = 18;
        public static final int TIP_TYPE_MAX_VIDEO_DURATION = 22;
        public static final int TIP_TYPE_MIMOJI = 19;
        public static final int TIP_TYPE_NEW_SLOW_MOTION = 9;
        public static final int TIP_TYPE_SPEECH_SHUTTER = 23;
        public static final int TIP_TYPE_SUPER_NIGHT = 11;
        public static final int TIP_TYPE_ULTRA_WIDE = 13;
        public static final int TIP_TYPE_ULTRA_WIDE_RECOMMEND = 14;
        public static final int TIP_TYPE_WARNING = 5;
        public static final int TIP_ULTRA_PIXEL_NO_SUPPORT_ZOOM = 15;
        public static final int TYPE_TAG = 175;

        @Retention(RetentionPolicy.SOURCE)
        public @interface CloseType {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface TipDuration {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface TipShowOrientation {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface TipType {
        }

        boolean containTips(@StringRes int i);

        void directHideCenterTipImage();

        void directHideLeftImageIntro();

        void directHideTipImage();

        void directShowLeftImageIntro();

        void directShowOrHideLeftTipImage(boolean z);

        void directlyHideTips();

        void directlyHideTips(@StringRes int i);

        void directlyHideTipsForce();

        void directlyShowTips(int i, @StringRes int i2);

        String getCurrentBottomTipMsg();

        int getCurrentBottomTipType();

        void hideCenterTipImage();

        void hideLeftTipImage();

        void hideNearRangeTip();

        void hideQrCodeTip();

        void hideRightTipImage();

        void hideTipImage();

        boolean isNearRangeTipShowing();

        boolean isPortraitHintVisible();

        boolean isQRTipVisible();

        boolean isTipShowing();

        void reConfigBottomTipOfUltraWide();

        boolean reConfigQrCodeTip();

        void reInitTipImage();

        void setPortraitHintVisible(int i);

        void showFastMotionPanel();

        void showIDCardTip(boolean z);

        boolean showMimojiPanel(int i);

        void showNearRangeTip();

        void showOrHideMimojiPanel();

        void showOrHideVideoBeautyPanel();

        void showQrCodeTip();

        void showTips(int i, @StringRes int i2, int i3);

        void showTips(int i, @StringRes int i2, int i3, int i4);

        void showTips(int i, String str, int i2);

        void showTipsWithOrientation(int i, @StringRes int i2, int i3, int i4, int i5);

        void updateLeftTipImage();

        void updateMimojiBottomTipImage();

        void updateTipBottomMargin(int i, boolean z);

        void updateTipImage();
    }

    public interface CameraAction extends BaseProtocol {
        public static final int TYPE_TAG = 161;

        boolean isBlockSnap();

        boolean isDoingAction();

        boolean isRecording();

        boolean isRecordingPaused() {
            return false;
        }

        boolean onCameraPickerClicked(View view) {
            return false;
        }

        void onReviewCancelClicked();

        void onReviewDoneClicked();

        void onShutterButtonClick(int i);

        void onShutterButtonFocus(boolean z, int i);

        boolean onShutterButtonLongClick();

        void onShutterButtonLongClickCancel(boolean z);

        void onThumbnailClicked(View view);
    }

    public interface CameraActionTrack extends BaseProtocol {
        public static final int TYPE_TAG = 186;

        void onTrackShutterButtonMissTaken(long j);

        void onTrackShutterButtonTaken(long j);
    }

    public interface CameraClickObservable extends BaseProtocol {
        public static final int OBSERVABLE_AI_SCENE = 166;
        public static final int OBSERVABLE_CINEMATIC_ASPECT_RATIO = 169;
        public static final int OBSERVABLE_EIS = 170;
        public static final int OBSERVABLE_FLASH = 161;
        public static final int OBSERVABLE_FOCUS = 165;
        public static final int OBSERVABLE_HDR = 162;
        public static final int OBSERVABLE_LIVEPHOTO = 163;
        public static final int OBSERVABLE_MORE = 164;
        public static final int OBSERVABLE_SAT = 167;
        public static final int OBSERVABLE_SHUTTER = 171;
        public static final int OBSERVABLE_ZOOM = 168;
        public static final int OBSERVER_ASD_RCOMM = 161;
        public static final int TYPE_TAG = 227;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ClickObservableType {
        }

        public interface ClickObserver {
            void action();

            int getObserver();
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface ClickObserverType {
        }

        void addObservable(int[] iArr, ClickObserver clickObserver, int... iArr2);

        void subscribe(int i);
    }

    public interface CameraModuleSpecial extends BaseProtocol {
        public static final int TYPE_TAG = 195;

        boolean isStartCountCapture();

        void showOrHideChip(boolean z);

        void showQRCodeResult();
    }

    public interface ChangeGainProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 935;

        void setGainValue(String str);
    }

    public interface CloneAction extends BaseProtocol {
        public static final int TYPE_TAG = 419;

        void onAdjustClicked();

        void onCancelClicked();

        void onCloneGuideClicked();

        void onExitClicked();

        void onFragmentResume();

        void onGiveUpClicked();

        void onGiveUpEditClicked();

        void onPlayClicked();

        void onResetEditClicked();

        void onSaveClicked();

        void onSaveEditClicked();

        boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onStopClicked();

        boolean onTapUp(float f, float f2);

        void onTimeFreezeClicked();

        boolean onTouchDown(float f, float f2);

        boolean onTouchUp(float f, float f2);
    }

    public interface CloneChooser extends BaseProtocol {
        public static final int TYPE_TAG = 416;

        void hide();

        boolean isShow();

        void show(int i);

        boolean startShot();
    }

    public interface CloneProcess extends BaseProtocol {
        public static final int TYPE_TAG = 418;

        boolean canSnap();

        Mode getMode();

        Status getStatus();

        boolean isAdded();

        void onBackPress();

        void onCountDownFinished();

        void onFilmRatioChanged(boolean z, boolean z2);

        void onFrameAvailable();

        void onPreviewPrepare(ContentValues contentValues);

        void onReplayPause();

        void onReplayResume();

        void onSaveFinish(Uri uri);

        void prepare(Mode mode, boolean z);

        void processingFinish();

        void processingPrepare();

        void processingStart();

        void quit();

        void setDetectedPersonInPreview(boolean z);

        void showExitConfirm(boolean z);

        void showPlayButton();

        void showResetAndSaveEdit();

        void showSaveAndGiveUp(boolean z);

        void showStopAndCancel();

        void stopCaptureToPreviewResult(boolean z);

        void updateCaptureMessage(int i, boolean z);

        void updateCaptureMessage(String str, boolean z);
    }

    public interface ConfigChanges extends BaseProtocol {
        public static final int CHECK_TYPE_FORCE_CLOSE = 4;
        public static final int CHECK_TYPE_MANUALLY = 1;
        public static final int CHECK_TYPE_MUTEX = 3;
        public static final int CHECK_TYPE_WATCH = 2;
        public static final int TYPE_TAG = 164;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ConfigCheckType {
        }

        void closeMutexElement(String str, int... iArr);

        void configBackSoftLightSwitch(String str);

        void configBeautySwitch(int i);

        void configBokeh(String str);

        void configCinematicAspectRatio(int i);

        void configClone(Mode mode, boolean z);

        void configDualWaterMarkSwitch();

        void configExposureFeedbackSwitch(int i);

        void configFilm(FilmItem filmItem, boolean z, boolean z2);

        void configFlash(String str, boolean z);

        void configFocusPeakSwitch(int i);

        void configGenderAgeSwitch(int i);

        void configGradienterSwitch(int i);

        void configGroupSwitch(int i);

        void configHHTSwitch(int i);

        void configHdr(String str);

        void configLiveReview();

        void configLiveShotSwitch(int i);

        void configLiveVV(VVItem vVItem, VVWorkspaceItem vVWorkspaceItem, boolean z, boolean z2);

        void configMagicFocusSwitch();

        void configMagicMirrorSwitch(int i);

        void configMeter(String str);

        void configModeEdit();

        void configModeMore(boolean z);

        void configNearRangeMode();

        void configPortraitSwitch(int i);

        void configRGBHistogramSwitch(int i);

        void configRawSwitch(int i);

        void configRotationChange(int i, int i2);

        void configScene(int i);

        void configSuperResolutionSwitch(int i);

        void configSwitchUltraPixel(int i);

        void configTiltSwitch(int i);

        void configTimeFreeze(Mode mode);

        void configTimerSwitch(String str);

        void configVideoFast();

        void configVideoLogSwitch(int i);

        void findBestWatermarkItem(int i);

        void onConfigChanged(int i);

        void onConfigValueChanged(int i, String str);

        void onShineDismiss(int i);

        void onThermalNotification(int i);

        void reCheckAIWatermark(boolean z);

        void reCheckAiAudio();

        void reCheckAiEnhancedVideo();

        void reCheckAiScene();

        void reCheckAmbilight();

        void reCheckColorEnhance();

        void reCheckDocumentMode();

        void reCheckDualVideoMode();

        void reCheckExposureFeedbackConfig();

        void reCheckEyeLight();

        void reCheckFastMotion(boolean z);

        void reCheckFilm();

        void reCheckFocusPeakConfig();

        void reCheckFrontBokenTip();

        void reCheckGradienter();

        void reCheckHanGestureDescTip();

        void reCheckHandGesture();

        void reCheckLighting();

        void reCheckLiveShot();

        void reCheckLiveVV();

        void reCheckMacroMode();

        void reCheckModuleUltraPixel();

        void reCheckMutexConfigs(int i);

        void reCheckMutexEarly(int i);

        void reCheckParameterDescriptionTip();

        void reCheckParameterResetTip(boolean z);

        void reCheckRaw();

        void reCheckSpeechShutterDescTip();

        void reCheckSubtitleMode();

        void reCheckSuperEIS();

        void reCheckSuperEISPro();

        void reCheckTilt();

        void reCheckTimerBurst();

        void reCheckUltraWideBokeh();

        void reCheckVideoBeautify();

        void reCheckVideoBeautyPipeline();

        void reCheckVideoHDR10Tip();

        void reCheckVideoHdr();

        void reCheckVideoLog();

        void reCheckVideoUltraClearTip();

        void reConfigAiAudio(Context context, int i, boolean z);

        void reConfigSpeechShutter();

        void recheckFunAR();

        void recheckVideoFps(boolean z);

        void resetMeter(int i);

        void restoreAllMutexElement(String str);

        void restoreMutexFlash(String str);

        void setEyeLight(String str);

        void setFilter(int i);

        void setKaleidoscope(String str);

        void setLighting(boolean z, String str, String str2, boolean z2);

        void setMasterFilter(int i);

        void setWatermarkEnable(boolean z);

        void showOrHideAIWatermark();

        void showOrHideFilter();

        void showOrHideLighting(boolean z);

        void showOrHideMasterFilter();

        void showOrHideMimoji();

        void showOrHideShine();

        void showSetting();

        void showWatermarkSample(boolean z);

        void switchOffElementsSilent(int... iArr);

        void updateASDForWatermark();
    }

    public interface DisplayFoldStatus extends BaseProtocol {
        public static final int TYPE_TAG = 938;

        boolean isDisplayFold();
    }

    public interface DollyZoomAction extends BaseProtocol {
        public static final int TYPE_TAG = 677;

        void onCancelClicked();

        void onExitClicked();

        void onFragmentResume();

        void onGiveUpClicked();

        void onGuideClicked();

        void onSaveClicked();

        void onStopClicked();
    }

    public interface DollyZoomProcess extends BaseProtocol {
        public static final int TYPE_TAG = 676;

        boolean canSnap();

        boolean isAdded();

        void onBackPressed();

        void onPreviewPrepare(ContentValues contentValues);

        void onSaveFinish(Uri uri);

        void prepare(boolean z);

        void processingFinish();

        void processingPrepare();

        void processingStart();

        void quit();

        void showSaveAndGiveUp();

        void stopCaptureToPreviewResult(boolean z);

        void updateCaptureMessage(int i, boolean z);

        void updateZoomRatioHint(float f);
    }

    public interface DualController extends BaseProtocol {
        public static final int TYPE_TAG = 182;

        void hideAllPanel();

        void hideBokehButton(boolean z);

        void hideZoomButton();

        boolean isButtonVisible();

        boolean isZoomPanelVisible();

        boolean isZoomSliderViewIdle();

        boolean isZoomVisible();

        void setRecordingOrPausing(boolean z);

        void showBokehButton();

        void showZoomButton();

        boolean updateSlideAndZoomRatio(int i);

        void updateZoomIndexsButton();

        void updateZoomRatio(int i);

        int visibleHeight();
    }

    public interface DualVideoRenderProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 430;

        RenderManager getRenderManager();
    }

    public interface EffectCropViewController {
        void destroyEffectCropView();

        void initEffectCropView();

        boolean isAutoZoomViewEnabled();

        boolean isEffectViewMoved();

        boolean isEffectViewVisible();

        boolean isZoomAdjustVisible();

        boolean onEffectViewTouchEvent(MotionEvent motionEvent);

        void removeTiltShiftMask();

        void setEffectViewVisible(boolean z);

        void setZoomViewVisible(boolean z);

        void updateEffectViewVisible();

        void updateEffectViewVisible(int i);
    }

    public interface EvChangedProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 169;

        void onEvChanged(int i, int i2);

        void onFocusAreaChanged(int i, int i2);

        void onMeteringAreaChanged(int i, int i2);

        void resetEvValue();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EventType {
    }

    public interface FastMotionProtocol extends BaseProtocol {
        public static final int DISMISS_ALPHA = 3;
        public static final int DISMISS_SILENT = 1;
        public static final int DISMISS_SLIDE = 2;
        public static final int KEEP = 0;
        public static final int TYPE_TAG = 674;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissType {
        }

        boolean dismiss(int i, int i2);

        boolean isShowing();

        void show();

        void switchType(String str, boolean z);
    }

    public interface FastmotionProAdjust extends BaseProtocol {
        public static final int ADJUST_MANUAL = 1;
        public static final int ADJUST_NOT_SPECIFIED = -1;
        public static final int ADJUST_NULL = 0;
        public static final int DISMISS_ALPHA = 3;
        public static final int DISMISS_SILENT = 1;
        public static final int DISMISS_SLIDE = 2;
        public static final int KEEP = 0;
        public static final int TYPE_TAG = 932;

        @Retention(RetentionPolicy.SOURCE)
        public @interface AdjustType {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissType {
        }

        boolean dismiss(int i, int i2);

        boolean isShowing();

        void resetManually();

        void setManuallyLayoutVisible(boolean z, int i);

        void show();

        int visibleHeight();
    }

    public interface FilmDreamConfig extends BaseProtocol, LiveGenericControl, FilmDreamRecorderControl, LiveSpeedChanges, LiveModuleSub, LiveVVExternal, PreviewCallback {
        public static final int TYPE_TAG = 930;
    }

    public interface FilmDreamProcess extends BaseProtocol {
        public static final int TYPE_TAG = 931;

        ContentValues getSaveContentValues();

        void onCombinePrepare(ContentValues contentValues);

        void onKeyCodeCamera();

        void onLiveSaveToLocalFinished(Uri uri, String str);

        void onResultCombineFinished(boolean z);

        void onResultPreviewFinished(boolean z);

        void processingFinish();

        void processingPause();

        void processingPrepare();

        void processingStart();

        void resumePlay();

        void setThumbnailOrientation(int i);

        void showExitConfirm();

        void updateRecordingTime(String str);
    }

    public interface FilmDreamRecorderControl {
        boolean canFinishRecording();

        boolean canRecordingStop();

        boolean isInited();

        boolean isRecording();

        void startRecording();

        void stopRecording();
    }

    public interface FilterProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 165;

        void onFilterChanged(int i, int i2);
    }

    public interface FullScreenProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 196;

        ContentValues getSaveContentValues();

        void hideScreenLight();

        boolean isLiveRecordPreviewShown();

        boolean isLiveRecordSaving();

        void onLiveSaveToLocalFinished(Uri uri, String str);

        void onRecordSegmentCreated();

        void quitLiveRecordPreview(boolean z);

        void setScreenLightColor(int i);

        boolean showScreenLight();

        void startLiveRecordPreview(ContentValues contentValues);

        void startLiveRecordSaving();
    }

    public interface GalleryConnect extends BaseProtocol {
        public static final int TYPE_TAG = 937;

        void setJumpTime(long j);
    }

    public interface GestureDetected {
        void onPrepare();

        void onSliding(int i);

        void onTapCapture();

        void onTapDown(float f, float f2);

        void onZoom(float f);
    }

    public interface HandleBackTrace {
        public static final int CALLING_KEY_BACK = 1;
        public static final int CALLING_LONG_PRESS_SHUTTER = 8;
        public static final int CALLING_RECEIVER_NOT_SPECIFIED = 1;
        public static final int CALLING_RESTART_MODE = 7;
        public static final int CALLING_SELF = 5;
        public static final int CALLING_SHUTTER = 3;
        public static final int CALLING_SWITCH_MODE = 4;
        public static final int CALLING_TAP_DOWN = 2;
        public static final int CALLING_TIMER_BURST_SHUTTER = 9;
        public static final int CALLING_USER = 6;

        @Retention(RetentionPolicy.SOURCE)
        public @interface CallingFrom {
        }

        boolean canProvide();

        boolean onBackEvent(int i);
    }

    public interface HandleBeautyRecording {
        void onAngleChanged(float f);

        void onBeautyRecordingStart();

        void onBeautyRecordingStop();
    }

    public interface HandlerSwitcher extends BaseProtocol {
        public static final int TYPE_TAG = 183;

        boolean onHandleSwitcher(int i);
    }

    public interface IDCardModeProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 233;

        void callBackEvent();

        String getCurrentPictureName();

        void switchNextPage();
    }

    public interface IndicatorProtocol {
        void clearFocusView(int i);

        void clearIndicator(int i);

        int getActiveIndicator();

        CameraHardwareFace[] getFaces();

        RectF getFocusRect(int i);

        RectF getFocusRectInPreviewFrame();

        RectF[] getViewRects(CameraSize cameraSize);

        void hideFaceAnimator();

        void hideOrShowDocument(boolean z);

        void initializeFocusView(ExposureViewListener exposureViewListener);

        boolean initializeObjectTrack(RectF rectF, boolean z);

        boolean initializeObjectView(RectF rectF, boolean z);

        boolean isAdjustingObjectView();

        boolean isEvAdjusted(boolean z);

        boolean isFaceExists(int i);

        boolean isFaceStable(int i);

        boolean isFocusViewVisible();

        boolean isIndicatorVisible(int i);

        boolean isNeedExposure(int i);

        boolean isObjectTrackFailed();

        void lightingCancel();

        void lightingDetectFace(CameraHardwareFace[] cameraHardwareFaceArr, boolean z);

        void lightingFocused();

        void lightingStart();

        void mimojiEnd();

        void mimojiFaceDetect(int i);

        void mimojiStart();

        void onStopObjectTrack();

        void reShowFaceRect();

        void setActiveIndicator(int i);

        void setAfRegionView(MeteringRectangle[] meteringRectangleArr, Rect rect, float f);

        void setCameraDisplayOrientation(int i);

        void setDisplaySize(int i, int i2);

        void setEvAdjustVisible(boolean z);

        void setEvAdjustable(boolean z);

        boolean setFaces(int i, CameraHardwareFace[] cameraHardwareFaceArr, Rect rect, Rect rect2);

        void setFocusViewPosition(int i, int i2, int i3);

        void setFocusViewType(boolean z);

        void setObjectViewListener(ObjectViewListener objectViewListener);

        void setPreviewSize(int i, int i2);

        void setShowGenderAndAge(boolean z);

        void setShowMagicMirror(boolean z);

        void setSkipDrawFace(boolean z);

        void showIndicator(int i, int i2);

        void updateDocument(Pair pair);

        void updateFaceView(boolean z, boolean z2, boolean z3, boolean z4, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IndicatorType {
    }

    public interface KaleidoscopeProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 236;

        void onKaleidoscopeChanged(String str);
    }

    public interface KeyEvent extends BaseProtocol {
        public static final int TYPE_TAG = 239;

        boolean onKeyDown(int i, android.view.KeyEvent keyEvent);

        boolean onKeyUp(int i, android.view.KeyEvent keyEvent);

        void resetZoomKeyEvent();
    }

    public interface LensProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 200;

        void onSwitchLens(boolean z, boolean z2);
    }

    public interface LiveAudioChanges extends BaseProtocol {
        public static final int TYPE_TAG = 211;

        void clearAudio();

        String getAudioPath() {
            return null;
        }

        void setAudioPath(String str);
    }

    public interface LiveBeautyChanges extends BaseProtocol {
        public static final int TYPE_TAG = 244;
    }

    public interface LiveConfigChanges extends BaseProtocol, LiveSpeedChanges, LiveAudioChanges, LiveFilterChangers, LiveBeautyChanges, LiveRecordState {
        public static final int ACTIVATE_EXPIRED = 2;
        public static final int ACTIVATE_FAIL = 3;
        public static final int ACTIVATE_NOT_MATCH = 4;
        public static final int ACTIVATE_OK = 0;
        public static final int ACTIVATE_TBD = 1;
        public static final int TYPE_TAG = 201;

        @Retention(RetentionPolicy.SOURCE)
        public @interface AuthResult {
        }

        int getAuthResult();

        Pair getConcatResult();

        SurfaceTexture getInputSurfaceTexture();

        void initPreview(int i, int i2, boolean z, int i3);

        void initResource();

        void onDeviceRotationChange(float[] fArr);

        boolean onRecordConcat(OnConcatFinishedListener onConcatFinishedListener);

        void onRecordPause();

        void onRecordResume();

        void onRecordReverse();

        void onRecordStart();

        void onRecordStop();

        void onSensorChanged(SensorEvent sensorEvent);

        void release();

        void setBeautify(boolean z, float f);

        void setBeautyFaceReshape(boolean z, float f, float f2);

        void setEffectAudio(boolean z);

        void startPreview(Surface surface);

        void updateRecordingTime();

        void updateRotation(float f, float f2, float f3);
    }

    public interface LiveConfigVV extends BaseProtocol, LiveGenericControl, LiveRecordControl, LiveModuleSub, LiveVVExternal, PreviewCallback {
        public static final int TYPE_TAG = 228;
    }

    public interface LiveEditor2 extends BaseProtocol {
        void combineVideoAudio(String str);

        void onDestroy();

        void pausePlay();

        void resumePlay();

        void startPlay();
    }

    public interface LiveFilmChooser extends BaseProtocol {
        public static final int TYPE_TAG = 675;

        void hide();

        boolean isPreviewShow();

        boolean isShow();

        void show(int i);

        void startShot();
    }

    public interface LiveFilterChangers extends BaseProtocol {
        public static final int TYPE_TAG = 243;

        void setFilter(boolean z, String str);
    }

    public interface LiveGenericControl extends BaseProtocol {
        CameraSize getAlgorithmPreviewSize(List list);

        int getAuthResult();

        float getPreviewRatio();

        void initResource();

        void onOrientationChanged(int i, int i2, int i3);

        void prepare();

        void release();

        void reset() {
        }
    }

    public interface LiveModuleSub extends BaseProtocol {
        SurfaceTexture getInputSurfaceTexture();

        void initPreview(int i, int i2, int i3, CameraScreenNail cameraScreenNail);

        boolean onBackPressed();

        void trackVideoParams();
    }

    public interface LiveRecordControl extends BaseProtocol {
        public static final int STEP_IGNORE = 1;
        public static final int STEP_START = 2;
        public static final int STEP_STOP = 3;

        boolean canFinishRecording();

        void deleteLastFragment();

        int getNextRecordStep();

        boolean isRecording();

        boolean isRecordingPaused();

        void onRecordingNewFragmentFinished();

        void startRecordingNewFragment();

        void startRecordingNextFragment();

        void stopRecording();
    }

    public interface LiveRecordState extends BaseProtocol {
        public static final int TYPE_TAG = 245;

        boolean canRecordingStop();

        int getSegmentSize();

        boolean isRecording();

        boolean isRecordingPaused();
    }

    public interface LiveSpeedChanges extends BaseProtocol {
        public static final int TYPE_TAG = 232;

        float getRecordSpeed();

        long getStartRecordingTime() {
            return 0;
        }

        String getTimeValue() {
            return null;
        }

        long getTotalRecordingTime() {
            return 0;
        }

        void setRecordSpeed(int i);
    }

    public interface LiveVVChooser extends BaseProtocol {
        public static final int TYPE_TAG = 229;

        void hide();

        boolean isPreviewShow();

        boolean isShow();

        void show(int i);

        boolean startShot();
    }

    public interface LiveVVExternal extends BaseProtocol {
        void combineVideoAudio(String str);

        String getSegmentPath(int i);

        void pausePlay();

        void prepare(VVItem vVItem);

        void resumePlay();

        void startPlay(Surface surface);
    }

    public interface LiveVVProcess extends BaseProtocol {
        public static final int TYPE_TAG = 230;

        ContentValues getSaveContentValues();

        void onCombinePrepare(ContentValues contentValues);

        void onKeyCodeCamera();

        void onLiveSaveToLocalFinished(Uri uri, String str);

        void onRecordingFragmentUpdate(int i, long j);

        void onRecordingNewFragmentStart(int i, long j, VVWorkspaceItem vVWorkspaceItem);

        void onResultCombineFinished(boolean z);

        void onResultPreviewFinished(boolean z);

        void prepare(VVItem vVItem, VVWorkspaceItem vVWorkspaceItem);

        void processingFinish();

        void processingPause();

        void processingPrepare();

        void processingResume();

        void processingStart();

        void quit();

        void showExitConfirm();

        void updateRecordingTime(String str);
    }

    public interface LiveVideoEditor extends BaseProtocol {
        public static final int TYPE_TAG = 209;

        void combineVideoAudio(String str, VECommonCallback vECommonCallback, VECommonCallback vECommonCallback2);

        boolean init(TextureView textureView, String str, String str2, VECommonCallback vECommonCallback, VECommonCallback vECommonCallback2);

        void onDestory();

        void pausePlay();

        void resumePlay();

        void setRecordParameter(int i, int i2, int i3);

        void startPlay();
    }

    public interface LyingDirectHint {
        void directHideLyingDirectHint();

        void updateLyingDirectHint(boolean z, boolean z2);
    }

    public interface MagneticSensorDetect extends BaseProtocol {
        public static final int TYPE_TAG = 2576;

        boolean isLockHDRChecker(String str);

        void onMagneticSensorChanged(SensorEvent sensorEvent);

        void recordMagneticInfo();

        void resetMagneticInfo();

        void updateMagneticDetection();

        void updatePreview(CaptureResult captureResult);
    }

    public interface MainContentProtocol extends BaseProtocol, EffectCropViewController, IndicatorProtocol {
        public static final int TYPE_TAG = 166;

        void checkTouchRegionContainSplitFocusExposure(MotionEvent motionEvent);

        List getFaceWaterMarkInfos();

        View getReferenceLine();

        void hideReferenceGradienter();

        void hideReviewViews();

        boolean isFaceLocationOK();

        boolean isFocusViewMoving();

        boolean isShowReviewViews();

        boolean isSplitFocusExposureDown();

        boolean isZoomViewMoving();

        void moveWatermarkLayout(int i, int i2);

        void onKeyEventSmoothZoom(int i, android.view.KeyEvent keyEvent);

        boolean onViewTouchEvent(int i, MotionEvent motionEvent);

        void performHapticFeedback(int i);

        void processingFinish();

        void processingStart(String str);

        void setCenterHint(int i, String str, String str2, int i2);

        void setIdPhotoBoxVisible(boolean z);

        void setMimojiDetectTipType(int i);

        void setPreviewAspectRatio(float f);

        void setWatermarkVisible(int i);

        void showReviewViews(Bitmap bitmap);

        void updateCinematicAspectRatioSwitched(boolean z);

        void updateContentDescription();

        void updateCurrentZoomRatio(float f);

        void updateFocusMode(String str);

        void updateMimojiFaceDetectResultTip(boolean z);

        void updateReferenceGradienterSwitched();

        void updateWatermarkSample(WatermarkItem watermarkItem);

        void updateWatermarkSample(WatermarkItem watermarkItem, boolean z);

        void updateZoomRatio(float f, float f2);
    }

    public interface MakeupProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 180;

        boolean isSeekBarVisible();

        void onMakeupItemSelected(String str, @StringRes int i, boolean z);
    }

    public interface ManuallyAdjust extends BaseProtocol {
        public static final int ADJUST_MANUAL = 1;
        public static final int ADJUST_NOT_SPECIFIED = -1;
        public static final int ADJUST_NULL = 0;
        public static final int ADJUST_SCENE = 2;
        public static final int ADJUST_TILT = 3;
        public static final int MANUALLY_ANIMATE_IN = 1;
        public static final int MANUALLY_ANIMATE_OUT = 2;
        public static final int MANUALLY_GONE = 4;
        public static final int MANUALLY_INVISIBLE = 3;
        public static final int MANUALLY_NOT_SPECIFIED = -1;
        public static final int TYPE_TAG = 181;

        @Retention(RetentionPolicy.SOURCE)
        public @interface AdjustType {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface ManuallyVisible {
        }

        void forceUpdateManualView(boolean z);

        void onRecordingPrepare();

        void onRecordingStop();

        void resetManually();

        void setManuallyLayoutVisible(boolean z);

        int setManuallyVisible(int i, int i2, ManuallyListener manuallyListener);

        int visibleHeight();
    }

    public interface ManuallyValueChanged extends BaseProtocol {
        public static final int TYPE_TAG = 174;

        void onBokehFNumberValueChanged(String str);

        void onDualLensSwitch(ComponentManuallyDualLens componentManuallyDualLens, int i);

        void onDualLensZooming(boolean z);

        void onDualZoomHappened(boolean z);

        void onDualZoomValueChanged(float f, int i);

        void onETValueChanged(ComponentManuallyET componentManuallyET, String str, String str2);

        void onEVValueChanged(ComponentManuallyEV componentManuallyEV, String str);

        void onFocusValueChanged(ComponentManuallyFocus componentManuallyFocus, String str, String str2);

        void onISOValueChanged(ComponentManuallyISO componentManuallyISO, String str, String str2);

        void onWBValueChanged(ComponentManuallyWB componentManuallyWB, String str, boolean z);

        void resetManuallyParameters(List list);

        void updateSATIsZooming(boolean z);
    }

    public interface MasterFilterProtocol extends BaseProtocol {
        public static final int DISMISS_ALPHA = 3;
        public static final int DISMISS_SILENT = 1;
        public static final int DISMISS_SLIDE = 2;
        public static final int KEEP = 0;
        public static final int TYPE_TAG = 934;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissType {
        }

        boolean dismiss(int i, int i2);

        boolean isShowing();

        void show();
    }

    public interface MiAsdDetect extends BaseProtocol {
        public static final int TYPE_TAG = 235;

        void updateUltraWide(boolean z, int i);
    }

    public interface MiBeautyProtocol extends BaseProtocol {
        public static final int DISMISS_ALPHA = 3;
        public static final int DISMISS_SILENT = 1;
        public static final int DISMISS_SLIDE = 2;
        public static final int TYPE_TAG = 194;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissType {
        }

        void clearBeauty();

        void dismiss(int i);

        int getDistanceForWM();

        List getSupportedBeautyItems(@ShineType String str);

        boolean isBeautyPanelShow();

        void onStateChanged();

        void resetBeauty();

        void show();

        void switchShineType(@ShineType String str, boolean z);
    }

    public interface MiLiveConfigChanges extends BaseProtocol, LiveGenericControl, LiveSpeedChanges, LiveAudioChanges, LiveFilterChangers, LiveBeautyChanges, MiLiveRecorderControl, LiveModuleSub, LiveRecordState, PreviewCallback {
        public static final int TYPE_TAG = 241;
    }

    public interface MiLivePlayerControl extends BaseProtocol {
        public static final int TYPE_TAG = 242;

        ContentValues getSaveContentValues();

        boolean isShowing();

        void onLiveSaveToLocalFinished(Uri uri, String str);

        void prepare(ContentValues contentValues, List list, String str);

        void release();

        void show();

        void startLiveRecordSaving();
    }

    public interface MiLiveRecorderControl {
        public static final int STATE_IDLE = 0;
        public static final int STATE_PAUSED = 3;
        public static final int STATE_PREVIEW = 1;
        public static final int STATE_RECORDING = 2;

        public interface IRecorderListener {
            void onRecorderCancel();

            void onRecorderError();

            void onRecorderFinish(List list, String str);

            void onRecorderPaused(List list);
        }

        void deleteLastFragment();

        int getCurState();

        void onSurfaceTextureReleased();

        void onSurfaceTextureUpdated(DrawExtTexAttribute drawExtTexAttribute);

        void pauseRecording();

        void resumeRecording();

        void setRecorderListener(IRecorderListener iRecorderListener);

        void startRecording();

        void stopRecording();
    }

    public interface MimojiAlert extends BaseProtocol {
        public static final int TYPE_TAG = 226;

        void firstProgressShow(boolean z);

        int refreshMimojiList();
    }

    public interface MimojiAvatarEngine extends BaseProtocol {
        public static final int TYPE_TAG = 217;

        void backToPreview(boolean z, boolean z2);

        void initAvatarEngine(int i, int i2, int i3, int i4, boolean z);

        boolean isOnCreateMimoji();

        boolean isRecordStopping();

        boolean isRecording();

        void onCaptureImage();

        boolean onCreateCapture();

        void onDeviceRotationChange(int i);

        void onDrawFrame(Rect rect, int i, int i2, boolean z);

        void onMimojiCreate();

        void onMimojiDeleted();

        void onMimojiInitFinish();

        void onMimojiSelect(MimojiInfo mimojiInfo);

        void onRecordStart(ContentValues contentValues);

        void onRecordStop(boolean z);

        void onResume();

        void release();

        void releaseRender();

        void setDetectSuccess(boolean z);

        void setDisableSingleTapUp(boolean z);
    }

    public interface MimojiEditor extends BaseProtocol {
        public static final int TYPE_TAG = 224;

        void directlyEnterEditMode(MimojiInfo mimojiInfo, int i);

        void goBack(boolean z, boolean z2);

        void onDeviceRotationChange(int i);

        void onTypeConfigSelect(int i);

        void releaseRender();

        void requestRender(boolean z);

        void resetClickEnable(boolean z);

        void resetConfig();

        void startMimojiEdit(boolean z, int i);
    }

    public interface MimojiGifEditor extends BaseProtocol {
        public static final int TYPE_TAG = 251;

        void combineVideoAudio(String str, long j);

        void coverGifError();

        void coverGifSuccess(String str);

        void operateGifPannelVisibleState(int i);

        void reconfigPreviewRadio(int i);

        void setPreviewCover(Bitmap bitmap);

        void showPreviewCover(boolean z);
    }

    public interface ModeChangeController extends BaseProtocol {
        public static final int TYPE_TAG = 179;

        boolean canSwipeChangeMode();

        void changeCamera(View... viewArr);

        void changeModeByGravity(int i, int i2);

        void changeModeByNewMode(int i, String str, int i2);

        boolean modeChanging();

        void resetModeSelectView(int i);

        void resetToCommonMode();
    }

    public interface ModeCoordinator {
        void attachProtocol(int i, BaseProtocol baseProtocol);

        void detachProtocol(int i, BaseProtocol baseProtocol);

        BaseProtocol getAttachProtocol(int i);
    }

    public interface ModeListManager extends BaseProtocol {
        public static final int TYPE_TAG = 2560;

        void hideMoreMode(boolean z);

        boolean isMoreModeShowing(boolean z);

        void showMoreMode(boolean z);
    }

    public interface ModuleContent extends BaseProtocol {
        public static final int TYPE_TAG = 431;

        ViewGroup getParent();
    }

    public interface MoreModePopupController extends BaseProtocol {
        public static final int TYPE_TAG = 2561;

        boolean isExpanded();

        boolean shrink(boolean z);
    }

    public interface MultiFeatureManager extends BaseProtocol {
        public static final int TYPE_TAG = 929;

        void cancelInstallFeature(String str);

        @Nullable
        AlertDialog checkAndShowDownloadConfirmDialog(String str, Context context, boolean z, Runnable runnable);

        long getFeatureSize(String str);

        List getInstalledFeatures();

        boolean hasFeatureInstalled(String str);

        void init();

        void loadLibrary(Context context, String str);

        AlertDialog showDownloadCancelDialog(String str, Context context, boolean z, Runnable runnable);

        void startInstallFeature(String str, SplitInstallStateUpdatedListener splitInstallStateUpdatedListener);
    }

    public interface OnShineChangedProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 234;

        void onShineChanged(boolean z, int i);
    }

    public interface PanoramaProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 176;

        int getCurrentDirection();

        ViewGroup getPreivewContainer();

        void initPreviewLayout(CameraSize cameraSize);

        boolean isShown();

        void onCaptureDirectionDecided(int i, int i2, int i3);

        void onPreviewMoving();

        void requestRender();

        void resetShootUI();

        void setDirectionPosition(Point point, int i);

        void setDirectionTooFast(boolean z, int i);

        void setDisplayPreviewBitmap(Bitmap bitmap);

        void setShootUI();

        void showSmallPreview(boolean z);

        void toggleCaptureDirection();
    }

    public interface PlayVideoProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 167;

        void playVideo();
    }

    public interface PortraitLighting extends BaseProtocol {
        public static final int TYPE_TAG = 672;

        void dismiss(int i);
    }

    public interface PresentationDisplay extends BaseProtocol {
        public static final int SHOW_TYPE_MODULE = 2;
        public static final int SHOW_TYPE_SWITCH = 1;
        public static final int TYPE_TAG = 945;

        void cancel();

        void closePresentationDisplay();

        boolean isModeSupportPresentation(int i);

        void openPresentationDisplay();

        void show(int i);

        void updateTextureSize();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProtocolType {
    }

    public interface RecordState extends BaseProtocol {
        public static final int TYPE_TAG = 212;

        void onFailed();

        void onFinish();

        void onLongExposePrepare();

        void onLongExposeStart();

        void onMimojiCreateBack();

        void onPause();

        void onPostPreview();

        void onPostPreviewBack();

        void onPostSavingFinish();

        void onPostSavingStart();

        void onPrepare();

        void onResume();

        void onStart();

        void prepareCreateMimoji();
    }

    public interface RemoteCameraServiceProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 422;

        RemoteService getService();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ShowType {
    }

    public interface SnapShotIndicator extends BaseProtocol {
        public static final int TYPE_TAG = 184;

        void setSnapNumValue(int i);

        void setSnapNumVisible(boolean z, boolean z2);
    }

    public interface SpeechShutterDetect extends BaseProtocol {
        public static final int TYPE_TAG = 255;

        void onDestroy();

        void processingSpeechShutter(boolean z);
    }

    public interface StandaloneRecorderProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 429;

        MultiRecorderManager getRecorderManager(ImageSaver imageSaver);
    }

    public interface StickerProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 178;

        void onStickerChanged(String str);
    }

    public interface SubtitleRecording extends BaseProtocol {
        public static final int TYPE_TAG = 231;

        public interface Listener {
            void onResult(String str);

            void onTimeout();
        }

        void checkNetWorkStatus();

        void getSubtitleContentAsync(Listener listener, long j);

        String getSubtitleContentSync();

        void handleSubtitleRecordingPause();

        void handleSubtitleRecordingResume();

        void handleSubtitleRecordingStart();

        void handleSubtitleRecordingStop();

        void initPermission();
    }

    public interface TopAlert extends BaseProtocol, LyingDirectHint {
        public static final int TYPE_TAG = 172;

        void addView(View view, LayoutParams layoutParams);

        void alertAiAudio(int i, @StringRes int i2);

        void alertAiAudioBGHint(int i, @StringRes int i2);

        void alertAiAudioMutexToastIfNeed(Context context, @StringRes int i);

        void alertAiDetectTipHint(int i, @StringRes int i2, long j);

        void alertAiEnhancedVideoHint(int i, @StringRes int i2);

        void alertAiSceneSelector(int i);

        void alertAutoHibernationDescTip(String str, int i, @StringRes int i2, long j);

        void alertDocumentTip(@StringRes int i);

        void alertDualVideoHint(int i, @StringRes int i2);

        void alertFastmotionIndicator(int i, String str, String str2, String str3, boolean z);

        void alertFastmotionProValue(String str, String str2, String str3, boolean z, boolean z2);

        void alertFastmotionValue(String str, String str2);

        void alertFlash(int i, String str, boolean z);

        void alertHDR(int i, boolean z, boolean z2);

        void alertHandGestureHint(@StringRes int i);

        void alertLightingTip(int i);

        void alertLiveShotHint(int i, @StringRes int i2);

        void alertMacroModeHint(int i, @StringRes int i2);

        void alertMimojiFaceDetect(boolean z, @StringRes int i);

        void alertMoonModeSelector(int i, boolean z);

        void alertMusicClose(boolean z);

        void alertParameterDescriptionTip(int i, int i2);

        void alertParameterResetTip(boolean z, int i, @StringRes int i2);

        void alertProColourHint(int i, @StringRes int i2);

        void alertRecommendDescTip(String str, int i, @StringRes int i2);

        void alertRecommendDescTip(String str, int i, @StringRes int i2, long j);

        void alertSlideSwitchLayout(boolean z, int i);

        void alertSubtitleHint(int i, @StringRes int i2);

        void alertSuperNightSeTip(int i);

        void alertSwitchTip(String str, int i, @StringRes int i2);

        void alertSwitchTip(String str, int i, int i2, @StringRes int i3, long j);

        void alertSwitchTip(String str, int i, int i2, String str2, long j);

        void alertSwitchTip(String str, int i, String str2);

        void alertTimerBurstHint(int i, @StringRes int i2);

        void alertTopHint(int i, @StringRes int i2);

        void alertTopHint(int i, @StringRes int i2, long j);

        void alertTopHint(int i, String str);

        void alertTopHint(int i, String str, long j);

        void alertUpdateValue(int i, @StringRes int i2, String str);

        void alertVideoUltraClear(int i, @StringRes int i2);

        void alertVideoUltraClear(int i, String str);

        void animTopBlackCover();

        boolean canProvide();

        void clearAlertStatus();

        void clearAllTipsState();

        void clearFastmotionValue();

        void clearVideoUltraClear();

        boolean containShortDurationDescriptionTips(String str);

        void disableMenuItem(boolean z, int... iArr);

        void enableMenuItem(boolean z, int... iArr);

        boolean getAlertIsShow();

        int getCurrentAiSceneLevel();

        boolean getTipsState(String str);

        VideoTagView getVideoTag();

        void hideAlert();

        void hideConfigMenu();

        void hideDelayNumber();

        void hideExtraMenu();

        void hideRecommendDescTip(String str);

        void hideSwitchTip();

        boolean isContainAlertLightingTip(int i);

        boolean isContainAlertRecommendTip(@StringRes int... iArr);

        boolean isCurrentRecommendTipText(@StringRes int i);

        boolean isExtraMenuShowing();

        boolean isHDRShowing();

        boolean isShowBacklightSelector();

        void reInitAlert(boolean z);

        void refreshExtraMenu();

        void refreshHistogramStatsView();

        void removeExtraMenu(int i);

        boolean reverseExpandTopBar(boolean z);

        void setAiSceneImageLevel(int i);

        void setAlertAnim(boolean z);

        void setConfigMenuResetWhenRestartmode();

        void setRecordingTimeState(int i);

        void setRecordingTimeState(int i, boolean z);

        void setShow(boolean z);

        void setTipsState(String str, boolean z);

        void setVolumeValue(float[] fArr);

        void showConfigMenu();

        void showDelayNumber(int i);

        void showDocumentStateButton(int i);

        void startLiveShotAnimation();

        void updateConfigItem(int... iArr);

        void updateContentDescription();

        void updateFastmotionProRecordingTime(String str, String str2);

        void updateHistogramStatsData(int[] iArr);

        void updateHistogramStatsData(int[] iArr, int[] iArr2, int[] iArr3);

        void updateProVideoRecordingSimpleView(boolean z);

        void updateRGBHistogramSwitched(boolean z);

        void updateRecordingTime(String str);

        void updateRecordingTimeStyle(boolean z);

        void updateTopAlertLayout();
    }

    public interface TopAlertEvent extends BaseProtocol {
        public static final int TYPE_TAG = 673;

        void onRecommendDescDismiss();
    }

    public interface TopConfigProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 193;

        boolean getAutoHDRTargetState() {
            return false;
        }

        void onExtraMenuVisibilityChange(boolean z) {
        }

        void reShowMoon() {
        }

        void startAiLens() {
        }
    }

    public interface VerticalProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 198;

        void alertLightingHint(int i);

        boolean isAnyViewVisible();
    }

    public interface WatermarkProtocol extends BaseProtocol {
        public static final int DISMISS_ALPHA = 3;
        public static final int DISMISS_SILENT = 1;
        public static final int DISMISS_SLIDE = 2;
        public static final int KEEP = 0;
        public static final int TYPE_TAG = 253;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissType {
        }

        boolean dismiss(int i, int i2);

        int getDistanceForWM();

        boolean isWatermarkPanelShow();

        void requestLocationPermission(IPermissionListener iPermissionListener);

        void show();

        void startDottedLineAnimation(View view);

        void switchType(String str, boolean z);
    }

    public interface WideSelfieProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 216;

        void initPreviewLayout(int i, int i2, int i3, int i4);

        void requestRender();

        void resetShootUI();

        void setShootingUI();

        void updateHintText(@StringRes int i);

        void updatePreviewBitmap(Bitmap bitmap, Rect rect, Rect rect2);

        void updateThumbBackgroudLayout(boolean z, boolean z2, int i);
    }

    public interface ZoomProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 170;

        float getMaxZoomRatio();

        float getMinZoomRatio();

        float getZoomRatio();

        void notifyDualZoom(boolean z);

        void notifyZooming(boolean z);

        void onZoomRatioChanged(float f, int i);

        void onZoomSwitchCamera();

        void setMaxZoomRatio(float f);

        void setMinZoomRatio(float f);

        boolean zoomIn(float f);

        boolean zoomOut(float f);
    }
}
