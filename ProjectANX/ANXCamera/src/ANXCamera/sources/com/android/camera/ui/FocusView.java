package com.android.camera.ui;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Range;
import android.util.Rational;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import androidx.annotation.StringRes;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.EvChangedProtocol;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.ui.drawable.focus.CameraExposureDrawable;
import com.android.camera.ui.drawable.focus.CameraFocusAnimateDrawable;
import com.android.camera.ui.drawable.focus.CameraFocusSplitAnimateDrawable;
import com.android.camera.ui.drawable.focus.CameraIndicatorState;
import com.android.camera2.CameraCapabilities;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import miui.view.animation.CubicEaseOutInterpolator;

@TargetApi(21)
public class FocusView extends View implements FocusIndicator, V6FunctionUI, Rotatable {
    private static final int ALPHA_TIMEOUT = 1500;
    public static final int CENTER_CAPTURE = 2;
    public static final int CENTER_CIRCLE = 1;
    public static final int CENTER_LOCK = 5;
    public static final int CENTER_MOON = 4;
    public static final int CENTER_NONE = 0;
    public static final int CENTER_SUN = 3;
    public static final int CURSOR_GATHER = 1;
    public static final int CURSOR_NORMAL = 0;
    public static final int CURSOR_SLIDE_BACK = 2;
    private static final int DISAPPEAR_TIME = 1500;
    private static final int DRAG_MODE_ADJUST_EV = 1;
    private static final int DRAG_MODE_MOVE_EXPOSURE = 3;
    private static final int DRAG_MODE_MOVE_FOCUS = 2;
    private static final int DRAG_MODE_NONE = 0;
    private static final int END_DISAPPEAR_TIMEOUT = 800;
    private static final float GAP_NUM = C0124O00000oO.OOoo000();
    public static final int MAX_SLIDE_DISTANCE = Util.dpToPixel(110.3f);
    private static final int MODE_AUTO = 0;
    private static final int MODE_FLAG_ANY = 0;
    private static final int MODE_FLAG_EXPOSURE = 2;
    private static final int MODE_FLAG_FOCUS = 1;
    private static final int MODE_MANUAL_COMBO = 1;
    private static final int MODE_MANUAL_METERING_ONLY = 3;
    private static final int MODE_MANUAL_SPLIT = 2;
    private static final int MSG_ALPHA = 8;
    private static final int MSG_ANIMATE_EV_CENTER = 7;
    private static final int MSG_FINISH_DISAPPEAR = 5;
    private static final int MSG_RESET_CENTER = 6;
    private static final int MSG_START_DISAPPEAR = 4;
    public static final int NORMAL_CAPTURE = 0;
    public static final int NORMAL_EV = 1;
    public static final int SET_BY_AUTO_DISAPPEAR = 6;
    public static final int SET_BY_AUTO_FOCUS_MOVING = 3;
    public static final int SET_BY_CANCEL_PREVIOUS_FOCUS = 2;
    public static final int SET_BY_CLEAR_VIEW = 7;
    public static final int SET_BY_DEVICE_KEEP_MOVING = 4;
    public static final int SET_BY_INITIALIZE = 8;
    public static final int SET_BY_RESET_CENTER = 5;
    public static final int SET_BY_RESET_TO_FACE_AREA = 9;
    public static final int SET_BY_TOUCH_DOWN = 1;
    private static final int START_DISAPPEAR_TIMEOUT = 5000;
    private static final int STATE_CHANGE_TIME = 500;
    private static final int STATE_FAIL = 3;
    private static final int STATE_IDLE = 0;
    private static final int STATE_START = 1;
    private static final int STATE_SUCCESS = 2;
    /* access modifiers changed from: private */
    public static final String TAG = "FocusView";
    /* access modifiers changed from: private */
    public static final int TRIANGLE_BASE_DIS = Util.dpToPixel(3.0f);
    public static final int ZERO_MINUS = 3;
    public static final int ZERO_PLUS = 4;
    private final int MIN_DRAG_DISTANCE;
    /* access modifiers changed from: private */
    public Camera mActivity;
    /* access modifiers changed from: private */
    public RollAdapter mAdapter;
    private long mAdjustedDoneTime;
    private boolean mBeingDragged;
    /* access modifiers changed from: private */
    public boolean mBeingEvAdjusted;
    private int mBottomRelative;
    private CameraExposureDrawable mCameraExposureDrawable;
    /* access modifiers changed from: private */
    public CameraFocusAnimateDrawable mCameraFocusAnimateDrawable;
    private CameraFocusSplitAnimateDrawable mCameraFocusSplitAnimateDrawable;
    private Bitmap mCaptureBitmap;
    private Rect mCaptureTapableRect;
    private int mCenterFlag = 0;
    private int mCenterX;
    private int mCenterY;
    /* access modifiers changed from: private */
    public int mCurrentDistanceY;
    /* access modifiers changed from: private */
    public int mCurrentItem;
    private int mCurrentMinusCircleCenter;
    private float mCurrentMinusCircleRadius;
    /* access modifiers changed from: private */
    public int mCurrentMode;
    private int mCurrentRadius;
    private int mCurrentRayBottom;
    private int mCurrentRayHeight;
    private int mCurrentRayWidth;
    /* access modifiers changed from: private */
    public int mCurrentViewState = 0;
    /* access modifiers changed from: private */
    public int mCursorState = 0;
    private float mDeltaX;
    private float mDeltaY;
    /* access modifiers changed from: private */
    public Rect mDisplayRect;
    private int mDragMode;
    /* access modifiers changed from: private */
    public float mEVAnimationRatio;
    /* access modifiers changed from: private */
    public long mEVAnimationStartTime;
    private float mEVCaptureRatio = -1.0f;
    /* access modifiers changed from: private */
    public float mEvValue;
    /* access modifiers changed from: private */
    public ExposureViewListener mExposureViewListener;
    private int mExposureX;
    private int mExposureY;
    private long mFailTime;
    private String mFocusMode = "auto";
    private boolean mFocusMoving;
    private int mFocusX;
    private int mFocusY;
    private GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(Message message) {
            int i;
            long j;
            String access$000 = FocusView.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("msg=");
            sb.append(message.what);
            sb.append(" ");
            sb.append(FocusView.this.mAdapter != null);
            Log.d(access$000, sb.toString());
            if (FocusView.this.mAdapter != null) {
                switch (message.what) {
                    case 4:
                    case 5:
                        if (!FocusView.this.mIsDraw || !FocusView.this.mIsDown) {
                            FocusView.this.reset(6);
                            break;
                        } else {
                            FocusView.this.clearMessages();
                            i = 5;
                            j = 50;
                        }
                        break;
                    case 6:
                        FocusView.this.resetCenter();
                        break;
                    case 7:
                        long uptimeMillis = SystemClock.uptimeMillis() - FocusView.this.mEVAnimationStartTime;
                        if (uptimeMillis < 520) {
                            FocusView.this.mEVAnimationRatio = ((float) uptimeMillis) / 500.0f;
                            FocusView.this.calculateAttribute();
                            FocusView.this.invalidate();
                            i = 7;
                            j = 20;
                            sendEmptyMessageDelayed(i, j);
                            break;
                        } else {
                            FocusView.this.mCurrentViewState = 1;
                            FocusView.this.mCursorState = 0;
                            return;
                        }
                    case 8:
                        FocusView focusView = FocusView.this;
                        focusView.startAlphaAnimation(focusView);
                        FocusView.this.mCameraFocusAnimateDrawable.startEvAdjustAlphaAnim();
                        break;
                }
                sendEmptyMessageDelayed(i, j);
            }
        }
    };
    private int mHeight;
    private CameraIndicatorState mIndicatorState;
    private Interpolator mInterpolator;
    /* access modifiers changed from: private */
    public boolean mIsDown;
    /* access modifiers changed from: private */
    public volatile boolean mIsDraw;
    /* access modifiers changed from: private */
    public boolean mIsEvAdjustable;
    private boolean mIsRecording;
    private boolean mIsSplitFocusExposureDown = false;
    private boolean mIsTouchFocus;
    /* access modifiers changed from: private */
    public int mLastItem;
    private Bitmap mLockBodyBitmap;
    private Bitmap mLockExposureBitmap;
    private Bitmap mLockFocusBitmap;
    private Bitmap mLockHeadBitmap;
    private int mMode = 1;
    private Rect mRecordTapableRect;
    /* access modifiers changed from: private */
    public int mRotation;
    /* access modifiers changed from: private */
    public int mScrollDistanceY;
    private SimpleOnGestureListener mSimpleOnGestureListener = new SimpleOnGestureListener() {
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onDown(MotionEvent motionEvent) {
            boolean z;
            if (!FocusView.this.mIsDraw) {
                return false;
            }
            switch (FocusView.this.mCurrentMode) {
                case 163:
                case 165:
                case 167:
                case 171:
                case 173:
                case 175:
                case 177:
                case 184:
                case 186:
                case 188:
                case 205:
                    if (FocusView.this.mExposureViewListener != null && FocusView.this.mExposureViewListener.isSupportFocusShoot() && !FocusView.this.mExposureViewListener.isShowAeAfLockIndicator()) {
                        z = true;
                        break;
                    }
                default:
                    z = false;
                    break;
            }
            MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            if (mainContentProtocol != null && mainContentProtocol.isZoomViewMoving()) {
                return false;
            }
            if (FocusView.this.mCurrentViewState != 0 || !z || !FocusView.this.isInCircle(motionEvent.getX() - ((float) FocusView.this.mDisplayRect.left), motionEvent.getY() - ((float) FocusView.this.mDisplayRect.top), ((float) CameraFocusAnimateDrawable.BIG_RADIUS) * 0.5f)) {
                FocusView.this.mIsDown = true;
                FocusView.this.removeMessages();
                FocusView.this.setTouchDown();
            } else if (FocusView.this.mAdapter != null) {
                ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166)).removeTiltShiftMask();
                CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                if (cameraAction != null && !cameraAction.isDoingAction()) {
                    cameraAction.onShutterButtonClick(90);
                }
            }
            return true;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0135, code lost:
            if (r4 >= com.android.camera.ui.FocusView.access$100(r3.this$0).getCenterIndex()) goto L_0x00f4;
         */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x009a  */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x00c5  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            int i;
            int access$2900;
            float f3;
            if (!FocusView.this.mIsDown || motionEvent2 == null || !FocusView.this.mIsEvAdjustable) {
                return false;
            }
            int gestureOrientation = V6GestureRecognizer.getInstance(FocusView.this.mActivity).getGestureOrientation();
            if ((gestureOrientation != 200 || (FocusView.this.mRotation / 90) % 2 != 0) && (gestureOrientation != 100 || (FocusView.this.mRotation / 90) % 2 == 0)) {
                return false;
            }
            int access$2300 = FocusView.this.mScrollDistanceY;
            int access$2200 = FocusView.this.mRotation;
            if (access$2200 == 0) {
                f3 = ((float) access$2300) - f2;
            } else if (access$2200 == 90) {
                f3 = ((float) access$2300) + f;
            } else if (access$2200 != 180) {
                if (access$2200 == 270) {
                    f3 = ((float) access$2300) - f;
                }
                float windowHeight = ((float) Display.getWindowHeight()) / 2.0f;
                int i2 = FocusView.MAX_SLIDE_DISTANCE;
                i = (int) (((float) access$2300) / (windowHeight / (((float) i2) / 4.0f)));
                FocusView.this.mCurrentDistanceY = Util.clamp(i, ((-i2) / 2) - FocusView.TRIANGLE_BASE_DIS, FocusView.MAX_SLIDE_DISTANCE / 2);
                if (i == FocusView.this.mCurrentDistanceY) {
                    FocusView.this.mScrollDistanceY = access$2300;
                }
                FocusView.this.mBeingEvAdjusted = true;
                FocusView.this.resetAlpha();
                FocusView.this.mHandler.sendEmptyMessageDelayed(8, 1500);
                access$2900 = FocusView.this.getItemByCoordinate();
                if (access$2900 != FocusView.this.mCurrentItem) {
                    int i3 = 3;
                    if (FocusView.this.mCurrentViewState == 3 || access$2900 >= FocusView.this.mCurrentItem || FocusView.this.mCurrentItem < FocusView.this.mAdapter.getCenterIndex() || access$2900 >= FocusView.this.mAdapter.getCenterIndex()) {
                        i3 = 4;
                        if (FocusView.this.mCurrentViewState != 4) {
                            if (access$2900 > FocusView.this.mCurrentItem) {
                                if (FocusView.this.mCurrentItem < FocusView.this.mAdapter.getCenterIndex()) {
                                }
                            }
                        }
                        FocusView.this.setCurrentItem(access$2900, false);
                    }
                    FocusView.this.startAnimation();
                    FocusView focusView = FocusView.this;
                    focusView.mLastItem = focusView.mCurrentItem;
                    FocusView.this.mCurrentViewState = i3;
                    FocusView.this.setCurrentItem(access$2900, false);
                }
                FocusView.this.mCameraFocusAnimateDrawable.setEvChanged((float) FocusView.this.mCurrentDistanceY, FocusView.this.mEvValue);
                if (FocusView.this.mCurrentViewState == 0 || FocusView.this.mCurrentViewState == 1) {
                    FocusView.this.mCurrentViewState = 1;
                    FocusView.this.calculateAttribute();
                    FocusView.this.invalidate();
                }
                return true;
            } else {
                f3 = ((float) access$2300) + f2;
            }
            access$2300 = (int) f3;
            float windowHeight2 = ((float) Display.getWindowHeight()) / 2.0f;
            int i22 = FocusView.MAX_SLIDE_DISTANCE;
            i = (int) (((float) access$2300) / (windowHeight2 / (((float) i22) / 4.0f)));
            FocusView.this.mCurrentDistanceY = Util.clamp(i, ((-i22) / 2) - FocusView.TRIANGLE_BASE_DIS, FocusView.MAX_SLIDE_DISTANCE / 2);
            if (i == FocusView.this.mCurrentDistanceY) {
            }
            FocusView.this.mBeingEvAdjusted = true;
            FocusView.this.resetAlpha();
            FocusView.this.mHandler.sendEmptyMessageDelayed(8, 1500);
            access$2900 = FocusView.this.getItemByCoordinate();
            if (access$2900 != FocusView.this.mCurrentItem) {
            }
            FocusView.this.mCameraFocusAnimateDrawable.setEvChanged((float) FocusView.this.mCurrentDistanceY, FocusView.this.mEvValue);
            FocusView.this.mCurrentViewState = 1;
            FocusView.this.calculateAttribute();
            FocusView.this.invalidate();
            return true;
        }
    };
    private long mStartTime;
    private int mState;
    private long mSuccessTime;
    private int mWidth;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CenterFlag {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CursorState {
    }

    public interface ExposureViewListener {
        boolean isMeteringAreaOnly();

        boolean isNeedHapticFeedback();

        boolean isShowAeAfLockIndicator();

        boolean isShowCaptureButton();

        boolean isSupportAELockOnly();

        boolean isSupportFocusShoot();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SetPositionType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewState {
    }

    public FocusView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mActivity = (Camera) context;
        this.mInterpolator = new CubicEaseOutInterpolator();
        this.mGestureDetector = new GestureDetector(context, this.mSimpleOnGestureListener);
        this.mGestureDetector.setIsLongpressEnabled(false);
        this.mWidth = Display.getWindowWidth();
        this.mHeight = Display.getWindowHeight();
        setCenter(this.mWidth / 2, this.mHeight / 2);
        this.mCaptureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_capture);
        this.mIndicatorState = new CameraIndicatorState();
        this.mCameraFocusAnimateDrawable = new CameraFocusAnimateDrawable(getContext());
        this.mCameraFocusAnimateDrawable.setIndicatorData(this.mIndicatorState, this.mCaptureBitmap);
        this.mLockHeadBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_aeaf_lock_head);
        this.mLockBodyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_aeaf_lock_body);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.ic_exposure_sun);
        this.mLockFocusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_focus_locked);
        this.mLockExposureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_exposure_locked);
        this.mCameraFocusAnimateDrawable.setLockIndicatorData(this.mIndicatorState, this.mLockHeadBitmap, this.mLockBodyBitmap);
        this.mCameraFocusAnimateDrawable.setCallback(this);
        this.mCameraExposureDrawable = new CameraExposureDrawable(context);
        this.mCameraExposureDrawable.setIndicatorData(this.mIndicatorState, this.mCaptureBitmap);
        this.mCameraFocusSplitAnimateDrawable = new CameraFocusSplitAnimateDrawable(context);
        this.mCameraFocusSplitAnimateDrawable.setAeAfLockedBitmap(this.mCaptureBitmap, this.mLockFocusBitmap, decodeResource);
        int scaledTouchSlop = ViewConfiguration.get(this.mActivity).getScaledTouchSlop();
        this.MIN_DRAG_DISTANCE = scaledTouchSlop * scaledTouchSlop;
    }

    /* access modifiers changed from: private */
    public void calculateAttribute() {
        int i;
        float f;
        float itemRatio = getItemRatio(this.mCurrentItem);
        float itemRatio2 = getItemRatio(this.mLastItem);
        int i2 = this.mCurrentMode;
        if (i2 != 167 && i2 != 180) {
            int i3 = this.mCurrentViewState;
            int i4 = 1;
            if (i3 != 0) {
                if (i3 != 1) {
                    if (i3 == 3) {
                        float f2 = this.mEVAnimationRatio;
                        if (f2 <= 0.5f) {
                            float f3 = f2 * 2.0f;
                            this.mCurrentRayWidth = Util.dpToPixel(1.5f);
                            float f4 = 1.0f - f3;
                            float f5 = (itemRatio2 * f4) - f3;
                            this.mCurrentRayHeight = Util.dpToPixel((f5 * 2.0f) + 5.0f);
                            this.mCurrentRayBottom = Util.dpToPixel((f5 * 3.0f) + 7.5f);
                            i = Util.dpToPixel((f3 * 3.0f) + 5.0f + (itemRatio2 * 2.0f * f4));
                            this.mCurrentRadius = i;
                            this.mCenterFlag = 3;
                            this.mCameraFocusAnimateDrawable.cancelResetCenter();
                            CameraIndicatorState cameraIndicatorState = this.mIndicatorState;
                            cameraIndicatorState.mCenterFlag = this.mCenterFlag;
                            cameraIndicatorState.mCurrentRadius = this.mCurrentRadius;
                            cameraIndicatorState.mCurrentAngle = (float) getCurrentAngle();
                            CameraIndicatorState cameraIndicatorState2 = this.mIndicatorState;
                            cameraIndicatorState2.mCurrentRayWidth = this.mCurrentRayWidth;
                            cameraIndicatorState2.mCurrentRayHeight = this.mCurrentRayHeight;
                            cameraIndicatorState2.mCurrentRayBottom = this.mCurrentRayBottom;
                            cameraIndicatorState2.mCurrentMinusCircleCenter = this.mCurrentMinusCircleCenter;
                            cameraIndicatorState2.mCurrentMinusCircleRadius = this.mCurrentMinusCircleRadius;
                        }
                        float f6 = (f2 - 0.5f) * 2.0f;
                        this.mCurrentRadius = Util.dpToPixel(8.0f - (((1.0f - itemRatio) * f6) * 2.0f));
                        int i5 = this.mCurrentRadius;
                        float f7 = 1.0f - f6;
                        this.mCurrentMinusCircleCenter = (int) (((float) i5) * ((0.914f * f7) + 0.5f));
                        f = ((float) i5) * ((f7 * 0.2f) + 0.8f);
                    } else if (i3 == 4) {
                        float f8 = this.mEVAnimationRatio;
                        if (f8 < 0.5f) {
                            float f9 = f8 * 2.0f;
                            this.mCurrentRadius = Util.dpToPixel(((itemRatio2 + ((1.0f - itemRatio2) * f9)) * 2.0f) + 6.0f);
                            int i6 = this.mCurrentRadius;
                            this.mCurrentMinusCircleCenter = (int) (((float) i6) * ((0.914f * f9) + 0.5f));
                            f = ((float) i6) * ((f9 * 0.2f) + 0.8f);
                        } else {
                            float f10 = (f8 - 0.5f) * 2.0f;
                            this.mCurrentRayWidth = Util.dpToPixel(1.5f);
                            float f11 = 1.0f - f10;
                            float f12 = (itemRatio * f10) - f11;
                            this.mCurrentRayHeight = Util.dpToPixel((f12 * 2.0f) + 5.0f);
                            this.mCurrentRayBottom = Util.dpToPixel((f12 * 3.0f) + 7.5f);
                            i = Util.dpToPixel((f11 * 3.0f) + 5.0f + (itemRatio * 2.0f * f10));
                            this.mCurrentRadius = i;
                            this.mCenterFlag = 3;
                            this.mCameraFocusAnimateDrawable.cancelResetCenter();
                            CameraIndicatorState cameraIndicatorState3 = this.mIndicatorState;
                            cameraIndicatorState3.mCenterFlag = this.mCenterFlag;
                            cameraIndicatorState3.mCurrentRadius = this.mCurrentRadius;
                            cameraIndicatorState3.mCurrentAngle = (float) getCurrentAngle();
                            CameraIndicatorState cameraIndicatorState22 = this.mIndicatorState;
                            cameraIndicatorState22.mCurrentRayWidth = this.mCurrentRayWidth;
                            cameraIndicatorState22.mCurrentRayHeight = this.mCurrentRayHeight;
                            cameraIndicatorState22.mCurrentRayBottom = this.mCurrentRayBottom;
                            cameraIndicatorState22.mCurrentMinusCircleCenter = this.mCurrentMinusCircleCenter;
                            cameraIndicatorState22.mCurrentMinusCircleRadius = this.mCurrentMinusCircleRadius;
                        }
                    }
                } else if (this.mCurrentItem < this.mAdapter.getCenterIndex()) {
                    this.mCurrentRadius = Util.dpToPixel((itemRatio * 2.0f) + 6.0f);
                    int i7 = this.mCurrentRadius;
                    this.mCurrentMinusCircleCenter = (int) (((float) i7) * 0.5f);
                    f = ((float) i7) * 0.8f;
                } else {
                    this.mCurrentRayWidth = Util.dpToPixel(1.5f);
                    this.mCurrentRayHeight = Util.dpToPixel(5.0f);
                    this.mCurrentRayBottom = Util.dpToPixel(7.5f);
                    i = Util.dpToPixel(5.0f);
                    this.mCurrentRadius = i;
                    this.mCenterFlag = 3;
                    this.mCameraFocusAnimateDrawable.cancelResetCenter();
                    CameraIndicatorState cameraIndicatorState32 = this.mIndicatorState;
                    cameraIndicatorState32.mCenterFlag = this.mCenterFlag;
                    cameraIndicatorState32.mCurrentRadius = this.mCurrentRadius;
                    cameraIndicatorState32.mCurrentAngle = (float) getCurrentAngle();
                    CameraIndicatorState cameraIndicatorState222 = this.mIndicatorState;
                    cameraIndicatorState222.mCurrentRayWidth = this.mCurrentRayWidth;
                    cameraIndicatorState222.mCurrentRayHeight = this.mCurrentRayHeight;
                    cameraIndicatorState222.mCurrentRayBottom = this.mCurrentRayBottom;
                    cameraIndicatorState222.mCurrentMinusCircleCenter = this.mCurrentMinusCircleCenter;
                    cameraIndicatorState222.mCurrentMinusCircleRadius = this.mCurrentMinusCircleRadius;
                }
                this.mCurrentMinusCircleRadius = f;
                this.mCenterFlag = 4;
                this.mCameraFocusAnimateDrawable.cancelResetCenter();
                CameraIndicatorState cameraIndicatorState322 = this.mIndicatorState;
                cameraIndicatorState322.mCenterFlag = this.mCenterFlag;
                cameraIndicatorState322.mCurrentRadius = this.mCurrentRadius;
                cameraIndicatorState322.mCurrentAngle = (float) getCurrentAngle();
                CameraIndicatorState cameraIndicatorState2222 = this.mIndicatorState;
                cameraIndicatorState2222.mCurrentRayWidth = this.mCurrentRayWidth;
                cameraIndicatorState2222.mCurrentRayHeight = this.mCurrentRayHeight;
                cameraIndicatorState2222.mCurrentRayBottom = this.mCurrentRayBottom;
                cameraIndicatorState2222.mCurrentMinusCircleCenter = this.mCurrentMinusCircleCenter;
                cameraIndicatorState2222.mCurrentMinusCircleRadius = this.mCurrentMinusCircleRadius;
            } else if (this.mIsTouchFocus) {
                ExposureViewListener exposureViewListener = this.mExposureViewListener;
                if (exposureViewListener != null && exposureViewListener.isShowCaptureButton() && this.mExposureViewListener.isSupportFocusShoot()) {
                    i4 = 2;
                }
            }
            this.mCenterFlag = i4;
            this.mCameraFocusAnimateDrawable.cancelResetCenter();
            CameraIndicatorState cameraIndicatorState3222 = this.mIndicatorState;
            cameraIndicatorState3222.mCenterFlag = this.mCenterFlag;
            cameraIndicatorState3222.mCurrentRadius = this.mCurrentRadius;
            cameraIndicatorState3222.mCurrentAngle = (float) getCurrentAngle();
            CameraIndicatorState cameraIndicatorState22222 = this.mIndicatorState;
            cameraIndicatorState22222.mCurrentRayWidth = this.mCurrentRayWidth;
            cameraIndicatorState22222.mCurrentRayHeight = this.mCurrentRayHeight;
            cameraIndicatorState22222.mCurrentRayBottom = this.mCurrentRayBottom;
            cameraIndicatorState22222.mCurrentMinusCircleCenter = this.mCurrentMinusCircleCenter;
            cameraIndicatorState22222.mCurrentMinusCircleRadius = this.mCurrentMinusCircleRadius;
        }
    }

    /* access modifiers changed from: private */
    public void clearMessages() {
        this.mHandler.removeMessages(8);
        this.mHandler.removeMessages(4);
        this.mHandler.removeMessages(5);
        this.mHandler.removeMessages(6);
        this.mHandler.removeMessages(7);
    }

    private int getCurrentAngle() {
        int i;
        int i2;
        if (this.mCursorState == 2) {
            int i3 = this.mCurrentViewState;
            if (!(i3 == 3 || i3 == 4)) {
                if (this.mCurrentItem >= this.mAdapter.getCenterIndex()) {
                    i = ((this.mCurrentItem - this.mAdapter.getCenterIndex()) * m.cQ) / this.mAdapter.getCenterIndex();
                    return 360 - Util.clamp(i, 0, (int) m.cQ);
                }
                i = 0;
                return 360 - Util.clamp(i, 0, (int) m.cQ);
            }
        }
        int i4 = this.mCurrentViewState;
        if (i4 == 1) {
            int clamp = Util.clamp(this.mBottomRelative - this.mCurrentDistanceY, 0, MAX_SLIDE_DISTANCE);
            int i5 = MAX_SLIDE_DISTANCE;
            if (clamp >= i5 / 2) {
                i = ((clamp - (i5 / 2)) * m.cQ) / (i5 / 2);
                return 360 - Util.clamp(i, 0, (int) m.cQ);
            }
            i = 0;
            return 360 - Util.clamp(i, 0, (int) m.cQ);
        }
        if (i4 == 3) {
            i2 = (int) (this.mEVAnimationRatio * 2.0f * 135.0f);
        } else if (i4 != 4) {
            return 0;
        } else {
            i2 = (int) ((1.0f - ((this.mEVAnimationRatio - 0.5f) * 2.0f)) * 135.0f);
        }
        return Util.clamp(i2, 0, (int) af.au);
    }

    private float getInterpolation(float f) {
        float interpolation = this.mInterpolator.getInterpolation(f);
        if (((double) interpolation) > 1.0d) {
            return 1.0f;
        }
        return interpolation;
    }

    /* access modifiers changed from: private */
    public int getItemByCoordinate() {
        return Util.clamp((this.mAdapter.getMaxItem() * (this.mBottomRelative - this.mCurrentDistanceY)) / MAX_SLIDE_DISTANCE, 0, this.mAdapter.getMaxItem());
    }

    private float getItemRatio(int i) {
        float maxItem = ((float) i) / ((float) this.mAdapter.getMaxItem());
        if (maxItem >= 0.5f) {
            maxItem -= 0.5f;
        }
        return maxItem * 2.0f;
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00c4 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void handleSplitFocusExposureEvent(MotionEvent motionEvent) {
        float f;
        int i;
        int i2;
        Rect rect = this.mIsRecording ? this.mRecordTapableRect : this.mCaptureTapableRect;
        float x = motionEvent.getX() - ((float) this.mDisplayRect.left);
        float y = motionEvent.getY() - ((float) this.mDisplayRect.top);
        if (motionEvent.getActionMasked() == 0) {
            resetAlpha();
            this.mIsSplitFocusExposureDown = false;
            int i3 = this.mMode;
            if (i3 == 1) {
                if (isInCircle(x, y, (float) CameraFocusSplitAnimateDrawable.BIG_RADIUS)) {
                    this.mDragMode = 3;
                }
            } else if (i3 == 2) {
                if (isInCircle(x, y, (float) this.mFocusX, (float) this.mFocusY, (float) CameraFocusSplitAnimateDrawable.BIG_RADIUS)) {
                    this.mDragMode = 2;
                    this.mFocusMoving = false;
                } else {
                    if (isInCircle(x, y, (float) this.mExposureX, (float) this.mExposureY, (float) CameraFocusSplitAnimateDrawable.BIG_RADIUS)) {
                        this.mDragMode = 3;
                    } else {
                        this.mDragMode = 0;
                    }
                }
                this.mIsSplitFocusExposureDown = true;
            }
            int i4 = this.mDragMode;
            if (i4 == 2) {
                this.mDeltaX = x - ((float) this.mFocusX);
                i2 = this.mFocusY;
            } else if (i4 == 3) {
                this.mDeltaX = x - ((float) this.mExposureX);
                i2 = this.mExposureY;
            }
            this.mDeltaY = y - ((float) i2);
        } else if (motionEvent.getActionMasked() == 2) {
            float f2 = x - this.mDeltaX;
            float f3 = y - this.mDeltaY;
            if (!this.mBeingDragged) {
                float f4 = 0.0f;
                int i5 = this.mDragMode;
                if (i5 == 2) {
                    int i6 = this.mFocusX;
                    f = (((float) i6) - f2) * (((float) i6) - f2);
                    i = this.mFocusY;
                } else {
                    if (i5 == 3) {
                        int i7 = this.mExposureX;
                        f = (((float) i7) - f2) * (((float) i7) - f2);
                        i = this.mExposureY;
                    }
                    if (f4 < ((float) this.MIN_DRAG_DISTANCE)) {
                        return;
                    }
                }
                f4 = f + ((((float) i) - f3) * (((float) i) - f3));
                if (f4 < ((float) this.MIN_DRAG_DISTANCE)) {
                }
            }
            resetAlpha();
            this.mHandler.sendEmptyMessageDelayed(8, 1500);
            if (this.mMode == 1) {
                this.mMode = 2;
                MistatsWrapper.moduleUIClickEvent(this.mCurrentMode == 167 ? "M_manual_" : "M_proVideo_", Manual.METERING_FOCUS_SPLIT, (Object) "on");
            }
            if (this.mIsRecording) {
                getResources().getDimensionPixelSize(R.dimen.bottom_picker_width);
                getResources().getDimensionPixelSize(R.dimen.bottom_snap_width);
            }
            Rect rect2 = this.mDisplayRect;
            int i8 = rect2.right;
            int i9 = CameraFocusAnimateDrawable.BIG_RADIUS;
            float min = Math.min((float) (i8 - i9), Math.max((float) (rect2.left + i9), f2));
            int i10 = rect.bottom;
            int i11 = CameraFocusAnimateDrawable.BIG_RADIUS;
            float min2 = Math.min((float) ((i10 - i11) - this.mDisplayRect.top), Math.max((float) i11, f3));
            if (this.mDragMode == 2 && !this.mCameraFocusSplitAnimateDrawable.isFocusLocked()) {
                this.mBeingDragged = true;
                int i12 = (int) min;
                this.mCenterX = i12;
                this.mFocusX = i12;
                int i13 = (int) min2;
                this.mCenterY = i13;
                this.mFocusY = i13;
                if (!this.mFocusMoving) {
                    this.mFocusMoving = true;
                }
                this.mCameraFocusSplitAnimateDrawable.setState(2);
                this.mCameraFocusSplitAnimateDrawable.setFocusCenter(this.mFocusX, this.mFocusY);
            } else if (this.mDragMode == 3 && !this.mCameraFocusSplitAnimateDrawable.isExposureLocked()) {
                this.mBeingDragged = true;
                this.mExposureX = (int) min;
                this.mExposureY = (int) min2;
                this.mCameraFocusSplitAnimateDrawable.setState(2);
                this.mCameraFocusSplitAnimateDrawable.setExposureCenter(this.mExposureX, this.mExposureY);
                updateExposureArea();
            }
            invalidate();
        } else if (motionEvent.getActionMasked() == 1) {
            if (this.mDragMode == 2 && !this.mCameraFocusSplitAnimateDrawable.isFocusLocked()) {
                updateFocusArea();
            }
            this.mDragMode = 0;
            this.mBeingDragged = false;
            this.mIsSplitFocusExposureDown = false;
        }
    }

    private void hideTipMessage(@StringRes int i) {
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.directlyHideTips(i);
        }
    }

    private void initRect() {
        this.mDisplayRect = Util.getPreviewRect(getContext());
        boolean z = false;
        this.mCaptureTapableRect = Util.getDisplayRect(0);
        this.mRecordTapableRect = Util.getDisplayRect(1);
        this.mWidth = this.mDisplayRect.width();
        this.mHeight = this.mDisplayRect.height();
        this.mCenterX = this.mWidth / 2;
        this.mCenterY = this.mHeight / 2;
        setPivotX((float) this.mCenterX);
        setPivotY((float) this.mCenterY);
        CameraFocusAnimateDrawable cameraFocusAnimateDrawable = this.mCameraFocusAnimateDrawable;
        if (1 == getLayoutDirection()) {
            z = true;
        }
        cameraFocusAnimateDrawable.setRtlAndDisplayRect(z, this.mDisplayRect);
        this.mCameraFocusAnimateDrawable.setCenter(this.mCenterX, this.mCenterY);
        this.mCameraFocusSplitAnimateDrawable.setCenter(this.mCenterX, this.mCenterY);
    }

    /* access modifiers changed from: private */
    public boolean isInCircle(float f, float f2, float f3) {
        float f4 = f - ((float) this.mCenterX);
        float f5 = f2 - ((float) this.mCenterY);
        return Math.sqrt((double) ((f4 * f4) + (f5 * f5))) <= ((double) f3);
    }

    private boolean isInCircle(float f, float f2, float f3, float f4, float f5) {
        float f6 = f - f3;
        float f7 = f2 - f4;
        return Math.sqrt((double) ((f6 * f6) + (f7 * f7))) <= ((double) f5);
    }

    private boolean isStableStart() {
        if (!this.mIsTouchFocus) {
            return false;
        }
        ExposureViewListener exposureViewListener = this.mExposureViewListener;
        if (exposureViewListener == null) {
            return false;
        }
        return exposureViewListener.isMeteringAreaOnly();
    }

    private void processParameterIfNeeded(float f) {
        if (this.mIsTouchFocus && this.mEVCaptureRatio != -1.0f && this.mCenterFlag == 0) {
            this.mCenterFlag = 2;
        }
    }

    private void reload() {
        RollAdapter rollAdapter = this.mAdapter;
        if (rollAdapter != null) {
            this.mCurrentItem = rollAdapter.getItemIndexByValue(Integer.valueOf(CameraSettings.readExposure()));
            updateEV();
        }
    }

    /* access modifiers changed from: private */
    public void removeMessages() {
    }

    /* access modifiers changed from: private */
    public void reset(int i) {
        if (this.mIsDraw || i == 8 || i == 2) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("reset: type = ");
            sb.append(i);
            Log.d(str, sb.toString());
            if (i != 3) {
                clearMessages();
                this.mState = 0;
                setPosition(i, this.mWidth / 2, this.mHeight / 2);
                this.mCurrentViewState = 0;
                this.mCenterFlag = 0;
                hideTipMessage(R.string.hint_ae_af_lock);
                hideTipMessage(R.string.hint_ae_lock);
                this.mIsDown = false;
                stopEvAdjust();
                setDraw(false);
                invalidate();
                this.mMode = 1;
                this.mCameraFocusSplitAnimateDrawable.reset();
            }
        }
    }

    /* access modifiers changed from: private */
    public void resetAlpha() {
        if (this.mHandler.hasMessages(8)) {
            this.mHandler.removeMessages(8);
        }
        setAlpha(1.0f);
    }

    /* access modifiers changed from: private */
    public void resetCenter() {
        int i;
        this.mCurrentViewState = 0;
        this.mAdjustedDoneTime = System.currentTimeMillis();
        ExposureViewListener exposureViewListener = this.mExposureViewListener;
        if (exposureViewListener != null) {
            if (!exposureViewListener.isShowAeAfLockIndicator() || !CameraSettings.isAEAFLockSupport()) {
                if (this.mExposureViewListener.isShowCaptureButton() && this.mExposureViewListener.isSupportFocusShoot()) {
                    i = 2;
                }
            }
            i = 5;
            this.mCenterFlag = i;
            this.mIndicatorState.mCenterFlag = this.mCenterFlag;
            invalidate();
        }
    }

    private void resetEvValue() {
        this.mBeingEvAdjusted = false;
        this.mEvValue = 0.0f;
        this.mCurrentItem = 0;
        this.mScrollDistanceY = 0;
        this.mCurrentDistanceY = 0;
        this.mCameraFocusAnimateDrawable.reset();
    }

    private void setCenter(int i, int i2) {
        this.mExposureX = i;
        this.mFocusX = i;
        this.mCenterX = i;
        this.mExposureY = i2;
        this.mFocusY = i2;
        this.mCenterY = i2;
    }

    /* access modifiers changed from: private */
    public void setCurrentItem(int i, boolean z) {
        if (i != this.mCurrentItem) {
            this.mCurrentItem = i;
            if (this.mAdapter != null) {
                EvChangedProtocol evChangedProtocol = (EvChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(169);
                if (evChangedProtocol == null) {
                    Log.d(TAG, "needEvPresenter");
                } else {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onEvChanged: index=");
                    sb.append(i);
                    sb.append(", value=");
                    sb.append(this.mAdapter.getItemValue(i));
                    Log.u(str, sb.toString());
                    evChangedProtocol.onEvChanged(this.mAdapter.getItemValue(i), 1);
                }
            }
            updateEV();
        }
    }

    private void setDraw(boolean z) {
        if (z && this.mIsTouchFocus && this.mIsDraw != z) {
            reload();
        }
        setAlpha(1.0f);
        this.mIsDraw = z;
        if (!this.mIsDraw) {
            CameraFocusAnimateDrawable cameraFocusAnimateDrawable = this.mCameraFocusAnimateDrawable;
            if (cameraFocusAnimateDrawable != null) {
                cameraFocusAnimateDrawable.cancelFocusingAnimation();
            }
            CameraFocusSplitAnimateDrawable cameraFocusSplitAnimateDrawable = this.mCameraFocusSplitAnimateDrawable;
            if (cameraFocusSplitAnimateDrawable != null) {
                cameraFocusSplitAnimateDrawable.cancelFocusingAnimation();
            }
        }
    }

    /* access modifiers changed from: private */
    public void setTouchDown() {
        this.mBottomRelative = (int) (((float) MAX_SLIDE_DISTANCE) * 0.5f);
    }

    private void showTipMessage(int i, @StringRes int i2) {
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            if (bottomPopupTips.isPortraitHintVisible()) {
                bottomPopupTips.setPortraitHintVisible(8);
            }
            bottomPopupTips.showTips(i, i2, 2);
        }
    }

    /* access modifiers changed from: private */
    public void startAlphaAnimation(View view) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(new float[]{1.0f, 0.4f});
        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new CubicEaseOutInterpolator());
        valueAnimator.addUpdateListener(new O00000o(view));
        valueAnimator.start();
    }

    /* access modifiers changed from: private */
    public void startAnimation() {
        this.mEVAnimationStartTime = SystemClock.uptimeMillis();
        this.mHandler.removeMessages(7);
        this.mHandler.sendEmptyMessage(7);
    }

    private void stopEvAdjust() {
        if (this.mBeingEvAdjusted) {
            this.mBeingEvAdjusted = false;
            EvChangedProtocol evChangedProtocol = (EvChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(169);
            if (evChangedProtocol != null) {
                evChangedProtocol.onEvChanged(0, 2);
            }
        }
    }

    private void updateEV() {
        Rational exposureCompensationRational = Camera2DataContainer.getInstance().getCurrentCameraCapabilities().getExposureCompensationRational();
        RollAdapter rollAdapter = this.mAdapter;
        if (rollAdapter != null) {
            this.mEvValue = ((float) rollAdapter.getItemValue(this.mCurrentItem)) * exposureCompensationRational.floatValue();
        } else {
            this.mEvValue = 0.0f;
        }
    }

    private void updateExposureArea() {
        ((EvChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(169)).onMeteringAreaChanged(this.mExposureX, this.mExposureY);
    }

    private void updateFocusArea() {
        Log.d(TAG, "updateFocusArea");
        if (!this.mFocusMode.equals("manual")) {
            ((EvChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(169)).onFocusAreaChanged(this.mFocusX, this.mFocusY);
        }
    }

    public boolean canLongPressReset() {
        if (this.mMode == 2) {
            int i = this.mDragMode;
            if (i == 3 || i == 2) {
                return true;
            }
        }
        return false;
    }

    public boolean canRestFocus() {
        return !this.mCameraFocusSplitAnimateDrawable.isFocusOrExposureLocked();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x006c, code lost:
        if (isInCircle(r0, r3, (float) com.android.camera.ui.drawable.focus.CameraFocusSplitAnimateDrawable.BIG_RADIUS) != false) goto L_0x006e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0097, code lost:
        if (isInCircle(r0, r3, (float) r10.mExposureX, (float) r10.mExposureY, (float) com.android.camera.ui.drawable.focus.CameraFocusSplitAnimateDrawable.BIG_RADIUS) != false) goto L_0x006e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void checkTouchRegionContainSplitFocusExposure(MotionEvent motionEvent) {
        int i = this.mCurrentMode;
        if (i == 180 || i == 167) {
            Rect tapableRectWithEdgeSlop = Util.getTapableRectWithEdgeSlop(!this.mIsRecording, this.mDisplayRect, this.mCurrentMode, this.mActivity);
            if (this.mIsRecording) {
                tapableRectWithEdgeSlop.bottom = this.mDisplayRect.bottom - this.mActivity.getResources().getDimensionPixelSize(R.dimen.bottom_snap_width);
            }
            if (!tapableRectWithEdgeSlop.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                this.mIsSplitFocusExposureDown = false;
                return;
            }
            float x = motionEvent.getX() - ((float) this.mDisplayRect.left);
            float y = motionEvent.getY() - ((float) this.mDisplayRect.top);
            if (motionEvent.getActionMasked() == 0) {
                int i2 = this.mMode;
                if (i2 != 1) {
                    if (i2 == 2) {
                        if (!isInCircle(x, y, (float) this.mFocusX, (float) this.mFocusY, (float) CameraFocusSplitAnimateDrawable.BIG_RADIUS)) {
                        }
                    }
                }
                this.mIsSplitFocusExposureDown = true;
            } else if (motionEvent.getActionMasked() == 1) {
                this.mIsSplitFocusExposureDown = false;
            }
            return;
        }
        this.mIsSplitFocusExposureDown = false;
    }

    public void clear() {
        reset(7);
    }

    public void clear(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("clear: ");
        sb.append(i);
        Log.d(str, sb.toString());
        CameraFocusAnimateDrawable cameraFocusAnimateDrawable = this.mCameraFocusAnimateDrawable;
        if (cameraFocusAnimateDrawable != null) {
            cameraFocusAnimateDrawable.cancelFocusingAnimation();
        }
        reset(i);
    }

    public void enableControls(boolean z) {
    }

    public void initialize(ExposureViewListener exposureViewListener) {
        this.mExposureViewListener = exposureViewListener;
        reset(8);
    }

    public void invalidateDrawable(Drawable drawable) {
        invalidate();
    }

    public boolean isEvAdjusted() {
        return this.mBeingEvAdjusted;
    }

    public boolean isEvAdjustedTime() {
        return isShown() && this.mIsTouchFocus && (this.mBeingEvAdjusted || !Util.isTimeout(System.currentTimeMillis(), this.mAdjustedDoneTime, 1500));
    }

    public boolean isFocusViewMoving() {
        if (this.mMode == 2) {
            int i = this.mDragMode;
            if (i == 3 || i == 2) {
                return true;
            }
        }
        return false;
    }

    public boolean isSplitFocusExposureDown() {
        return this.mIsSplitFocusExposureDown;
    }

    public boolean isVisible() {
        return this.mIsDraw;
    }

    public void onCameraOpen() {
    }

    public void onCreate() {
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CameraFocusAnimateDrawable cameraFocusAnimateDrawable = this.mCameraFocusAnimateDrawable;
        if (cameraFocusAnimateDrawable != null) {
            cameraFocusAnimateDrawable.cancelFocusingAnimation();
            this.mCameraFocusAnimateDrawable.setCallback(null);
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mIsDraw) {
            int i = this.mCurrentMode;
            if (i == 180 || i == 167) {
                this.mCameraFocusSplitAnimateDrawable.draw(canvas);
            } else {
                CameraFocusAnimateDrawable cameraFocusAnimateDrawable = this.mCameraFocusAnimateDrawable;
                if (!(cameraFocusAnimateDrawable == null || this.mMode == 3)) {
                    cameraFocusAnimateDrawable.draw(canvas);
                }
            }
        }
    }

    public void onPause() {
        setDraw(false);
    }

    public void onResume() {
        setDraw(false);
    }

    public boolean onViewTouchEvent(MotionEvent motionEvent) {
        if (this.mAdapter == null || !this.mIsTouchFocus) {
            return false;
        }
        if (this.mState != 2 && !isStableStart()) {
            return false;
        }
        this.mGestureDetector.onTouchEvent(motionEvent);
        boolean z = this.mIsDown;
        if (motionEvent.getActionMasked() == 5 && this.mIsDown) {
            this.mIsDown = false;
        }
        int i = this.mCurrentMode;
        if (i == 167 || i == 180) {
            handleSplitFocusExposureEvent(motionEvent);
        }
        if (1 == motionEvent.getAction() || 3 == motionEvent.getAction()) {
            if (this.mBeingEvAdjusted) {
                CameraStatUtils.trackEvAdjusted(this.mEvValue);
                stopEvAdjust();
                this.mAdjustedDoneTime = System.currentTimeMillis();
                this.mHandler.removeMessages(6);
                this.mHandler.sendEmptyMessageDelayed(6, 1000);
            }
            if (this.mIsDraw) {
                this.mIsDown = false;
            }
        }
        return z || this.mIsDown;
    }

    public void processingFinish() {
        this.mIsRecording = false;
    }

    public void processingStart() {
        this.mIsRecording = true;
    }

    public void reInit() {
        CameraFocusAnimateDrawable cameraFocusAnimateDrawable;
        int i;
        Log.d(TAG, "onCameraOpen>>");
        initRect();
        CameraCapabilities currentCameraCapabilities = Camera2DataContainer.getInstance().getCurrentCameraCapabilities();
        if (currentCameraCapabilities != null) {
            Range exposureCompensationRange = currentCameraCapabilities.getExposureCompensationRange();
            int intValue = ((Integer) exposureCompensationRange.getLower()).intValue();
            int intValue2 = ((Integer) exposureCompensationRange.getUpper()).intValue();
            if (C0124O00000oO.O0o00oO) {
                intValue = -3;
                intValue2 = 4;
            }
            if (C0124O00000oO.O0o00Oo || C0124O00000oO.O0o00O) {
                intValue = -12;
                intValue2 = 12;
            }
            if (!(intValue2 == 0 || intValue2 == intValue)) {
                float f = GAP_NUM;
                this.mAdapter = new FloatSlideAdapter(intValue, intValue2, f == 0.0f ? 1.0f : ((float) (intValue2 - intValue)) / f);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCameraOpen: adapter=");
                sb.append(this.mAdapter);
                Log.w(str, sb.toString());
                if (this.mAdapter != null) {
                    resetEvValue();
                    this.mCurrentMode = DataRepository.dataItemGlobal().getCurrentMode();
                    setRotation((float) this.mRotation);
                    if (this.mCurrentMode != 167) {
                        cameraFocusAnimateDrawable = this.mCameraFocusAnimateDrawable;
                        i = 8;
                    } else {
                        cameraFocusAnimateDrawable = this.mCameraFocusAnimateDrawable;
                        i = 0;
                    }
                    cameraFocusAnimateDrawable.setEvTextVisible(i);
                    setEvAdjustable(CameraSettings.isEvAdjustable());
                    int itemIndexByValue = this.mAdapter.getItemIndexByValue(Integer.valueOf(CameraSettings.readExposure()));
                    if (itemIndexByValue < 0) {
                        itemIndexByValue = this.mAdapter.getMaxItem() / 2;
                    }
                    this.mCurrentItem = itemIndexByValue;
                    updateEV();
                }
            }
        }
    }

    public void releaseListener() {
        this.mExposureViewListener = null;
    }

    public void setEVVisible(boolean z) {
        this.mCameraFocusAnimateDrawable.setEvAdjustVisible(z ? 0 : 8);
        if (!z) {
            this.mCameraFocusAnimateDrawable.setEvTextVisible(8);
        }
        invalidate();
    }

    public void setEvAdjustable(boolean z) {
        if (this.mAdapter != null) {
            this.mIsEvAdjustable = z;
            resetEvValue();
            calculateAttribute();
            this.mCameraFocusAnimateDrawable.setEvAdjustVisible(z ? 0 : 8);
            if (!z) {
                this.mCameraFocusAnimateDrawable.setEvTextVisible(8);
            }
            invalidate();
        }
    }

    public void setFocusType(boolean z) {
        this.mIsTouchFocus = z;
    }

    public void setOrientation(int i, boolean z) {
        if (this.mRotation != i) {
            this.mRotation = i;
            this.mCameraFocusAnimateDrawable.setOrientation(i);
            this.mCameraFocusSplitAnimateDrawable.setOrientation(i);
            if (this.mIsDraw) {
                invalidate();
            }
        }
    }

    public void setPosition(int i, int i2, int i3) {
        this.mCenterX = i2;
        this.mCenterY = i3;
        setPivotX((float) i2);
        setPivotY((float) i3);
        setCenter(i2, i3);
        this.mCameraFocusAnimateDrawable.setCenter(i2, i3);
        this.mCameraFocusSplitAnimateDrawable.setCenter(i2, i3);
        removeMessages();
        if (this.mEvValue != 0.0f) {
            int i4 = this.mCurrentMode;
            if (!(i4 == 167 || i4 == 180 || ((ModuleManager.isFastmotionModulePro() && CameraSettings.isBackCamera()) || (i != 1 && i != 2 && i != 5)))) {
                EvChangedProtocol evChangedProtocol = (EvChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(169);
                if (evChangedProtocol != null) {
                    evChangedProtocol.resetEvValue();
                }
            }
        }
        if (this.mCurrentDistanceY != 0 && i != 7) {
            resetEvValue();
        }
    }

    public void setRotation(float f) {
        int i = this.mCurrentMode;
        if (i == 180 || i == 167) {
            super.setRotation(0.0f);
        } else {
            super.setRotation(f);
        }
    }

    public void showFail() {
        this.mCameraFocusAnimateDrawable.startFocusFailAnimation();
        Log.d(TAG, "showFail");
        if (this.mState == 1) {
            clearMessages();
            setDraw(true);
            this.mState = 3;
            this.mFailTime = SystemClock.uptimeMillis();
            this.mHandler.sendEmptyMessageDelayed(5, 800);
            invalidate();
        }
    }

    public void showStart() {
        Log.d(TAG, "showStart");
        clearMessages();
        this.mState = 1;
        this.mCursorState = 1;
        this.mStartTime = SystemClock.uptimeMillis();
        setDraw(true);
        this.mCameraFocusAnimateDrawable.startTouchDownAnimation();
        if (this.mMode == 1) {
            this.mCameraFocusSplitAnimateDrawable.startTouchDownAnimation(0);
        }
        if (isStableStart()) {
            this.mEVCaptureRatio = 1.0f;
            showSuccess();
        } else {
            this.mEVCaptureRatio = -1.0f;
            this.mCenterFlag = 0;
            processParameterIfNeeded(0.0f);
            this.mHandler.sendEmptyMessageDelayed(4, 5000);
        }
        invalidate();
    }

    public void showSuccess() {
        Log.d(TAG, "showSuccess");
        if (this.mState == 1) {
            clearMessages();
            setDraw(true);
            this.mState = 2;
            this.mSuccessTime = SystemClock.uptimeMillis();
            if (!this.mIsTouchFocus) {
                this.mHandler.sendEmptyMessageDelayed(5, 800);
            } else {
                this.mHandler.sendEmptyMessageDelayed(8, 1500);
            }
        }
        ExposureViewListener exposureViewListener = this.mExposureViewListener;
        if (exposureViewListener == null) {
            Log.d(TAG, "needExposurePresenter");
            return;
        }
        if (exposureViewListener.isShowAeAfLockIndicator() && CameraSettings.isAEAFLockSupport()) {
            boolean isSupportAELockOnly = this.mExposureViewListener.isSupportAELockOnly();
            if (C0124O00000oO.OOo0oOo()) {
                DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
                String str = CameraSettings.KEY_EN_FIRST_CHOICE_LOCK_AE_AF_TOAST;
                if (dataItemGlobal.getBoolean(str, false) && getResources().getConfiguration().getLocales().get(0).getLanguage().equalsIgnoreCase("en")) {
                    DataRepository.dataItemGlobal().editor().putBoolean(str, false).apply();
                }
            }
            this.mCenterFlag = 5;
            MistatsWrapper.commonKeyTriggerEvent(Manual.AE_AF_AWB_LOCKED, "CENTER_LOCK", null);
        } else if (!this.mExposureViewListener.isShowCaptureButton() || !this.mExposureViewListener.isSupportFocusShoot()) {
            this.mCenterFlag = 1;
        } else {
            this.mCenterFlag = 2;
        }
        int i = this.mCurrentMode;
        if (i != 180 && i != 167) {
            this.mCameraFocusAnimateDrawable.startFocusSuccessAnimation(this.mCenterFlag, this.mIsTouchFocus);
        } else if (this.mMode == 1) {
            this.mCameraFocusSplitAnimateDrawable.startFocusSuccessAnimation(this.mCenterFlag, this.mIsTouchFocus, 0);
        } else {
            int i2 = this.mCenterFlag;
            if (i2 == 5) {
                this.mMode = 1;
                this.mCameraFocusSplitAnimateDrawable.startFocusSuccessAnimation(i2, this.mIsTouchFocus, 0);
            }
        }
    }

    public void updateFocusMode(String str) {
        this.mFocusMode = str;
        this.mCameraFocusSplitAnimateDrawable.setFocusMode(str);
    }
}
