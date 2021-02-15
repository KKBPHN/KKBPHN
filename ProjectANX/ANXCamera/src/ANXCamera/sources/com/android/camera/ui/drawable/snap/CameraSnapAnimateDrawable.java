package com.android.camera.ui.drawable.snap;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.bottom.BottomAnimationConfig;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.AmbilightProtocol;
import com.android.camera.protocol.ModeProtocol.BeautyRecording;
import com.android.camera.protocol.ModeProtocol.LiveSpeedChanges;
import com.android.camera.ui.drawable.CameraPaintBase;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import java.util.ArrayList;
import java.util.List;
import miui.view.animation.CubicEaseInInterpolator;
import miui.view.animation.CubicEaseOutInterpolator;

public class CameraSnapAnimateDrawable extends Drawable implements Animatable {
    private static final int ANIMATION_RANK = 1;
    private static final float SNAP_ROUND_ORIGINAL_WIDTH = 0.65f;
    private static final SpringConfig mCameraDownConfig = SpringConfig.fromOrigamiTensionAndFriction(100.0d, 15.0d);
    public static final SpringConfig mCameraUpConfig = SpringConfig.fromOrigamiTensionAndFriction(120.0d, 30.0d);
    public static final SpringConfig mCameraUpSplashConfig = SpringConfig.fromOrigamiTensionAndFriction(180.0d, 10.0d);
    public static final SpringConfig mRecordScaleConfig = SpringConfig.fromOrigamiTensionAndFriction(180.0d, 30.0d);
    /* access modifiers changed from: private */
    public CameraSnapPaintBottom mBottomPatinItem;
    /* access modifiers changed from: private */
    public CameraSnapPaintCenterVV mCenterVVPaintItem;
    /* access modifiers changed from: private */
    public CameraSnapPaintCircle mCirclePaintItem;
    private Context mContext;
    private Spring mDownSpring;
    /* access modifiers changed from: private */
    public float mLiveSpeed;
    /* access modifiers changed from: private */
    public long mLiveStartTime;
    /* access modifiers changed from: private */
    public long mLiveTotalTime;
    /* access modifiers changed from: private */
    public LongPressIncreaseListener mLongPressIncreaseListener = new LongPressIncreaseListener() {
        public void OnIncrease(float f) {
            CameraSnapAnimateDrawable.this.mRecordSpring.setEndValue(Math.min(1.1d, ((((double) f) - 0.6d) / 1.0d) + 0.6000000238418579d));
            CameraSnapAnimateDrawable.this.invalidateSelf();
        }

        public void OnTheValue(boolean z) {
            if (z) {
                CameraSnapAnimateDrawable.this.mUpSpring.setSpringConfig(CameraSnapAnimateDrawable.mCameraUpSplashConfig);
                CameraSnapAnimateDrawable.this.mUpSpring.setEndValue(1.0d);
                CameraSnapAnimateDrawable.this.mRecordSpring.setEndValue(0.6d);
            }
        }

        public void OnValueUp(float f) {
            double d = (double) f;
            CameraSnapAnimateDrawable.this.mUpSpring.setEndValue(d);
            CameraSnapAnimateDrawable.this.mRecordSpring.setEndValue(d);
        }
    };
    private ValueAnimator mModeChangeAnimator;
    private ValueAnimator mReboundAnimator;
    public Spring mRecordSpring;
    /* access modifiers changed from: private */
    public List mRecordingReferredPaints;
    private ValueAnimator mRingAnimator;
    /* access modifiers changed from: private */
    public CameraSnapPaintRound mRoundPaintItem;
    private ValueAnimator mScaleDownAnimator;
    /* access modifiers changed from: private */
    public ValueAnimator mScaleUpAnimator;
    /* access modifiers changed from: private */
    public CameraSnapPaintSecond mSecondPaintItem;
    private SpringSystem mSpringSystem;
    private ValueAnimator mStopAnimator;
    /* access modifiers changed from: private */
    public ValueAnimator mTimeAnimator;
    public Spring mUpSpring;

    public interface BeautyRecordingListener {
        void onAngleChanged(float f);
    }

    public interface LongPressIncreaseListener {
        void OnIncrease(float f);

        void OnTheValue(boolean z);

        void OnValueUp(float f);
    }

    public CameraSnapAnimateDrawable(Context context) {
        this.mContext = context;
        this.mCirclePaintItem = new CameraSnapPaintCircle(context);
        this.mRoundPaintItem = new CameraSnapPaintRound(context);
        this.mSecondPaintItem = new CameraSnapPaintSecond(context);
        this.mBottomPatinItem = new CameraSnapPaintBottom(context);
        this.mCenterVVPaintItem = new CameraSnapPaintCenterVV(context);
        this.mRecordingReferredPaints = new ArrayList();
    }

    private void cancelScaleDownAnimation() {
        ValueAnimator valueAnimator = this.mScaleDownAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mScaleDownAnimator.cancel();
            this.mScaleDownAnimator = null;
        }
    }

    private void cancelScaleUpAnimation() {
        ValueAnimator valueAnimator = this.mScaleUpAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mScaleUpAnimator.cancel();
            this.mScaleUpAnimator = null;
        }
    }

    private void initReboundSystem() {
        if (this.mSpringSystem == null) {
            this.mSpringSystem = SpringSystem.create();
            this.mUpSpring = this.mSpringSystem.createSpring();
            this.mUpSpring.setSpringConfig(mCameraUpConfig);
            this.mUpSpring.addListener(new SimpleSpringListener() {
                public void onSpringAtRest(Spring spring) {
                    if (spring.getCurrentValue() == 0.0d) {
                        CameraSnapAnimateDrawable.this.mUpSpring.setSpringConfig(CameraSnapAnimateDrawable.mCameraUpConfig);
                    }
                }

                public void onSpringUpdate(Spring spring) {
                    float mapValueFromRangeToRange = (float) SpringUtil.mapValueFromRangeToRange((double) ((float) spring.getCurrentValue()), 0.0d, 1.0d, 1.0d, 1.2d);
                    CameraSnapAnimateDrawable.this.mCirclePaintItem.isRecording = true;
                    CameraSnapAnimateDrawable.this.mCirclePaintItem.isInBeautyMode = true;
                    CameraSnapAnimateDrawable.this.mCirclePaintItem.mCurrentWidthPercent = CameraSnapAnimateDrawable.this.mCirclePaintItem.mSrcWidthPercent * mapValueFromRangeToRange;
                    CameraSnapAnimateDrawable.this.invalidateSelf();
                }
            });
            this.mRecordSpring = this.mSpringSystem.createSpring();
            this.mRecordSpring.setSpringConfig(mRecordScaleConfig);
            this.mRecordSpring.setCurrentValue(1.0d);
            this.mRecordSpring.addListener(new SimpleSpringListener() {
                public void onSpringUpdate(Spring spring) {
                    float currentValue = (float) spring.getCurrentValue();
                    CameraSnapAnimateDrawable.this.mRoundPaintItem.mCurrentWidthPercent = CameraSnapAnimateDrawable.this.mRoundPaintItem.mSrcWidthPercent * currentValue;
                    CameraSnapAnimateDrawable.this.invalidateSelf();
                }
            });
        }
    }

    private boolean isInLongExpose(BottomAnimationConfig bottomAnimationConfig) {
        if (bottomAnimationConfig.mIsLongExpose && bottomAnimationConfig.mIsTimerBurstCircle) {
            ValueAnimator valueAnimator = this.mTimeAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                return true;
            }
        }
        return false;
    }

    private void onLongExposeFinish(BottomAnimationConfig bottomAnimationConfig) {
        CameraSnapPaintBottom cameraSnapPaintBottom = this.mBottomPatinItem;
        cameraSnapPaintBottom.setTargetAlpha(cameraSnapPaintBottom.mBaseAlpha);
        if (isFullScreen()) {
            this.mRoundPaintItem.setTargetAlpha(0);
            this.mRoundPaintItem.prepareRecord(true, false, SNAP_ROUND_ORIGINAL_WIDTH);
            CameraSnapPaintCircle cameraSnapPaintCircle = this.mCirclePaintItem;
            cameraSnapPaintCircle.setTargetStrokeWidth(cameraSnapPaintCircle.mBaseStokeWidth);
            CameraSnapPaintCircle cameraSnapPaintCircle2 = this.mCirclePaintItem;
            cameraSnapPaintCircle2.setTargetWidthPercent(cameraSnapPaintCircle2.mBaseWidthPercent);
            return;
        }
        CameraSnapPaintRound cameraSnapPaintRound = this.mRoundPaintItem;
        cameraSnapPaintRound.setTargetAlpha(cameraSnapPaintRound.mBaseAlpha);
        CameraSnapPaintRound cameraSnapPaintRound2 = this.mRoundPaintItem;
        cameraSnapPaintRound2.setTargetWidthPercent(cameraSnapPaintRound2.mBaseWidthPercent);
        this.mCirclePaintItem.setBaseWidthPercent(0.61f);
        CameraSnapPaintCircle cameraSnapPaintCircle3 = this.mCirclePaintItem;
        cameraSnapPaintCircle3.setTargetWidthPercent(cameraSnapPaintCircle3.mBaseWidthPercent);
        this.mCirclePaintItem.setTargetAlpha(0);
        this.mCirclePaintItem.setResult();
    }

    private void onLongExposePrepare(BottomAnimationConfig bottomAnimationConfig) {
        this.mBottomPatinItem.setTargetAlpha(0);
        this.mBottomPatinItem.setResult();
        if (bottomAnimationConfig.mIsTimerBurstCircle) {
            this.mRoundPaintItem.prepareRecord(true, true, SNAP_ROUND_ORIGINAL_WIDTH);
        } else {
            this.mRoundPaintItem.setTargetAlpha(0);
            this.mRoundPaintItem.setResult();
        }
        if (!isFullScreen()) {
            this.mCirclePaintItem.setBaseWidthPercent(0.69f);
            this.mCirclePaintItem.setTargetWidthPercent(0.69f);
            CameraSnapPaintCircle cameraSnapPaintCircle = this.mCirclePaintItem;
            cameraSnapPaintCircle.setTargetStrokeWidth(cameraSnapPaintCircle.mBaseStokeWidth);
        }
        this.mCirclePaintItem.setNeedSplit(true);
        this.mCirclePaintItem.clearSegments();
        this.mCirclePaintItem.setTargetAlpha(CameraPaintBase.ALPHA_HINT);
    }

    private void updateLiveAnimationConfig() {
        LiveSpeedChanges liveSpeedChanges = (LiveSpeedChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(232);
        if (liveSpeedChanges != null) {
            this.mLiveSpeed = liveSpeedChanges.getRecordSpeed();
            this.mLiveTotalTime = liveSpeedChanges.getTotalRecordingTime();
            this.mLiveStartTime = liveSpeedChanges.getStartRecordingTime();
        }
    }

    public void addSegmentNow() {
        ValueAnimator valueAnimator = this.mTimeAnimator;
        if (valueAnimator != null) {
            this.mCirclePaintItem.addSegmentNow(valueAnimator.getCurrentPlayTime());
            invalidateSelf();
        }
    }

    public void cancelAnimation() {
        ValueAnimator valueAnimator = this.mTimeAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mTimeAnimator = null;
        }
        ValueAnimator valueAnimator2 = this.mRingAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
            this.mRingAnimator = null;
        }
    }

    public void cancelRebound() {
        ValueAnimator valueAnimator = this.mReboundAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            CameraSnapPaintCircle cameraSnapPaintCircle = this.mCirclePaintItem;
            cameraSnapPaintCircle.isInBeautyMode = false;
            cameraSnapPaintCircle.resetRecordingState();
            this.mUpSpring.setEndValue(0.0d);
            this.mRecordSpring.setEndValue(1.0d);
        }
    }

    public void clearBitmap() {
        this.mRoundPaintItem.clearBitmap();
        invalidateSelf();
    }

    public void directFinishRecord() {
        cancelAnimation();
        List<CameraPaintBase> list = this.mRecordingReferredPaints;
        if (list != null) {
            this.mRoundPaintItem.isRecording = false;
            for (CameraPaintBase cameraPaintBase : list) {
                cameraPaintBase.setCurrentValues(cameraPaintBase.mCurrentWidthPercent, cameraPaintBase.mCurrentColor, CameraPaintBase.ALPHA_OPAQUE, (float) Util.dpToPixel(1.0f));
                cameraPaintBase.isRecording = false;
                cameraPaintBase.resetRecordingState();
            }
            this.mRecordingReferredPaints.clear();
        }
    }

    public void draw(Canvas canvas) {
        if (canvas != null) {
            canvas.save();
            this.mBottomPatinItem.drawCanvas(canvas);
            canvas.restore();
            canvas.save();
            this.mRoundPaintItem.drawCanvas(canvas);
            canvas.restore();
            canvas.save();
            this.mCirclePaintItem.drawCanvas(canvas);
            canvas.restore();
            canvas.save();
            this.mCenterVVPaintItem.drawCanvas(canvas);
            canvas.restore();
            canvas.save();
            this.mSecondPaintItem.drawCanvas(canvas);
            canvas.restore();
        }
    }

    public void finishRecord(BottomAnimationConfig bottomAnimationConfig) {
        if (this.mRecordingReferredPaints == null || bottomAnimationConfig.mIsInMimojiCreate) {
            invalidateSelf();
            return;
        }
        if (bottomAnimationConfig.mNeedFinishRecord) {
            cancelAnimation();
            for (CameraPaintBase cameraPaintBase : this.mRecordingReferredPaints) {
                cameraPaintBase.resetRecordingState();
                cameraPaintBase.setTargetAlpha(255);
            }
            this.mRoundPaintItem.setTargetAlpha(255);
            this.mTimeAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mTimeAnimator.setStartDelay(200);
            this.mTimeAnimator.setDuration(260);
            this.mTimeAnimator.setInterpolator(new CubicEaseInInterpolator() {
                public float getInterpolation(float f) {
                    float interpolation = super.getInterpolation(f);
                    CameraSnapAnimateDrawable.this.mRoundPaintItem.updateValue(interpolation);
                    for (CameraPaintBase updateValue : CameraSnapAnimateDrawable.this.mRecordingReferredPaints) {
                        updateValue.updateValue(interpolation);
                    }
                    CameraSnapAnimateDrawable.this.invalidateSelf();
                    return interpolation;
                }
            });
            this.mTimeAnimator.removeAllListeners();
            this.mTimeAnimator.addListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    CameraSnapAnimateDrawable.this.mRoundPaintItem.isRecording = false;
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }
            });
            this.mTimeAnimator.start();
        } else {
            stopRecord(bottomAnimationConfig);
        }
    }

    public int getOpacity() {
        return -1;
    }

    public boolean hasSegments() {
        return this.mCirclePaintItem.hasSegments();
    }

    public void hideCirclePaintItem() {
        this.mCirclePaintItem.setVisible(8);
    }

    public void hidePaintCenterVVItem() {
        this.mCenterVVPaintItem.setVisible(8);
    }

    public void hideRoundPaintItem() {
        this.mRoundPaintItem.setVisible(8);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0068, code lost:
        if (r18.bottomHalfScreen() != false) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006a, code lost:
        r0.mCirclePaintItem.setTargetValues(0.61f, -1, com.android.camera.ui.drawable.CameraPaintBase.ALPHA_OPAQUE, (float) com.android.camera.Util.dpToPixel(5.0f));
        r0.mRoundPaintItem.setTargetValues(0.6637f, -1, com.android.camera.ui.drawable.CameraPaintBase.ALPHA_OPAQUE, 15.0f);
        r0.mSecondPaintItem.setTargetValues(0.813f, -1, 0, 3.0f);
        r0.mSecondPaintItem.clearPatternAndExternal();
        r0.mBottomPatinItem.setTargetValues(0.813f, -13750738, com.android.camera.ui.drawable.CameraPaintBase.ALPHA_OPAQUE, 3.0f);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0093, code lost:
        r0.mCirclePaintItem.setTargetValues(0.69f, -1, com.android.camera.ui.drawable.CameraPaintBase.ALPHA_OPAQUE, (float) com.android.camera.Util.dpToPixel(5.0f));
        r0.mRoundPaintItem.setTargetValues(SNAP_ROUND_ORIGINAL_WIDTH, -1, 0, 15.0f);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00e5, code lost:
        if (r18.bottomHalfScreen() != false) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0124, code lost:
        if (com.android.camera.data.DataRepository.dataItemRunning().getComponentRunningDualVideo().ismDrawSelectWindow() != false) goto L_0x00a6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void intoPattern(PaintConditionReferred paintConditionReferred) {
        PaintConditionReferred paintConditionReferred2 = paintConditionReferred;
        cancelAnimation();
        this.mCenterVVPaintItem.setTargetAlpha(0);
        this.mCenterVVPaintItem.setVisible(8);
        int i = paintConditionReferred2.targetMode;
        if (!(i == 161 || i == 162)) {
            if (i != 169) {
                if (i != 187) {
                    if (i != 189) {
                        if (i != 204) {
                            if (i != 254) {
                                if (!(i == 179 || i == 180)) {
                                    switch (i) {
                                        case 172:
                                            this.mCirclePaintItem.setTargetValues(0.51f, -50630, 0, (float) Util.dpToPixel(5.0f));
                                            this.mRoundPaintItem.setTargetValues(0.56f, -50630, CameraPaintBase.ALPHA_OPAQUE, 15.0f);
                                            this.mSecondPaintItem.setTargetValues(0.751f, -1, CameraPaintBase.ALPHA_OUTSTANDING, (float) Util.dpToPixel(1.0f));
                                            if (!paintConditionReferred2.isFPS960) {
                                                this.mSecondPaintItem.intoProgressPattern();
                                                break;
                                            } else {
                                                this.mSecondPaintItem.intoSlowPattern();
                                                break;
                                            }
                                        case 173:
                                            break;
                                        case 174:
                                            break;
                                        default:
                                            switch (i) {
                                                case 183:
                                                case 185:
                                                    break;
                                                case 184:
                                                    if (!paintConditionReferred2.forceVideoPattern) {
                                                        break;
                                                    }
                                                    break;
                                                default:
                                                    switch (i) {
                                                        case 207:
                                                        case 212:
                                                        case 213:
                                                        case 214:
                                                            break;
                                                        case 208:
                                                            break;
                                                        case 209:
                                                        case 210:
                                                        case 211:
                                                            break;
                                                    }
                                            }
                                    }
                                }
                            } else {
                                return;
                            }
                        }
                        this.mCirclePaintItem.setTargetValues(0.69f, -1, CameraPaintBase.ALPHA_OPAQUE, (float) Util.dpToPixel(5.0f));
                        this.mRoundPaintItem.setTargetValues(SNAP_ROUND_ORIGINAL_WIDTH, -1, 0, 15.0f);
                        this.mSecondPaintItem.setTargetValues(0.813f, -1, 0, 3.0f);
                        this.mSecondPaintItem.clearPatternAndExternal();
                        this.mBottomPatinItem.setTargetValues(SNAP_ROUND_ORIGINAL_WIDTH, -13750738, 0, 3.0f);
                        this.mCenterVVPaintItem.setTargetAlpha(CameraPaintBase.ALPHA_OPAQUE);
                        this.mCenterVVPaintItem.setVisible(0);
                        this.mCenterVVPaintItem.intoStartShotPattern(this.mContext);
                        return;
                    }
                }
                this.mCirclePaintItem.setTargetValues(0.51f, -1, 0, (float) Util.dpToPixel(5.0f));
                this.mRoundPaintItem.setTargetValues(0.56f, -1, CameraPaintBase.ALPHA_OPAQUE, 15.0f);
                this.mSecondPaintItem.setTargetValues(0.751f, -1, CameraPaintBase.ALPHA_OUTSTANDING, (float) Util.dpToPixel(1.0f));
                this.mSecondPaintItem.intoProgressPattern();
                this.mBottomPatinItem.setTargetValues(0.751f, -13750738, 0, (float) Util.dpToPixel(1.0f));
                return;
            }
            this.mCirclePaintItem.setTargetValues(0.51f, -50630, 0, (float) Util.dpToPixel(5.0f));
            this.mRoundPaintItem.setTargetValues(0.56f, -50630, CameraPaintBase.ALPHA_OPAQUE, 15.0f);
            this.mSecondPaintItem.setTargetValues(0.751f, -1, CameraPaintBase.ALPHA_OPAQUE, (float) Util.dpToPixel(1.0f));
            this.mSecondPaintItem.intoFastPattern();
            this.mBottomPatinItem.setTargetValues(SNAP_ROUND_ORIGINAL_WIDTH, -13750738, 0, (float) Util.dpToPixel(1.0f));
            return;
        }
        this.mCirclePaintItem.setTargetValues(0.69f, -1, CameraPaintBase.ALPHA_OPAQUE, (float) Util.dpToPixel(5.0f));
        this.mRoundPaintItem.setTargetValues(SNAP_ROUND_ORIGINAL_WIDTH, -50630, CameraPaintBase.ALPHA_OPAQUE, 15.0f);
        this.mSecondPaintItem.setTargetValues(0.813f, -1, 0, 3.0f);
        this.mSecondPaintItem.clearPatternAndExternal();
        this.mBottomPatinItem.setTargetValues(SNAP_ROUND_ORIGINAL_WIDTH, -13750738, 0, 3.0f);
    }

    public void intoPatternFromParameters(PaintConditionReferred paintConditionReferred) {
        intoPattern(paintConditionReferred);
        this.mCirclePaintItem.setResult();
        this.mRoundPaintItem.setResult();
        this.mSecondPaintItem.setResult();
        this.mBottomPatinItem.setResult();
        this.mCenterVVPaintItem.setResult();
    }

    public boolean isBottomVisible() {
        return this.mBottomPatinItem.mBaseAlpha != 0;
    }

    public boolean isFullScreen() {
        return this.mRoundPaintItem.mBaseAlpha == 0;
    }

    public boolean isRunning() {
        ValueAnimator valueAnimator = this.mModeChangeAnimator;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            ValueAnimator valueAnimator2 = this.mTimeAnimator;
            if (valueAnimator2 == null || !valueAnimator2.isRunning()) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
    }

    public void pauseRecording() {
        ValueAnimator valueAnimator = this.mTimeAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mTimeAnimator.pause();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00f4, code lost:
        if (r10.mSecondPaintHintEnable != false) goto L_0x0068;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00f8, code lost:
        r0 = r9.mRecordingReferredPaints;
        r1 = r9.mSecondPaintItem;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x01b7, code lost:
        r0 = r9.mRecordingReferredPaints;
        r1 = r9.mCirclePaintItem;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x01bb, code lost:
        r0.add(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x01f3, code lost:
        r0 = new float[2];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x01fa, code lost:
        if (r10.mIsInMimojiCreate == false) goto L_0x01fd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x01fc, code lost:
        r5 = 1.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x01fd, code lost:
        r0[0] = r5;
        r0[1] = 1.0f;
        r10 = android.animation.ValueAnimator.ofFloat(r0);
        r10.setDuration(300);
        r10.setInterpolator(new com.android.camera.ui.drawable.snap.CameraSnapAnimateDrawable.AnonymousClass6(r9));
        r10.start();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0215, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void prepareRecording(BottomAnimationConfig bottomAnimationConfig) {
        this.mRecordingReferredPaints.clear();
        cancelAnimation();
        cancelScaleDownAnimation();
        cancelScaleUpAnimation();
        this.mRoundPaintItem.prepareRecord(false, true, SNAP_ROUND_ORIGINAL_WIDTH);
        CameraSnapPaintRound cameraSnapPaintRound = this.mRoundPaintItem;
        cameraSnapPaintRound.isRecording = true;
        cameraSnapPaintRound.setTargetAlpha(255);
        if (bottomAnimationConfig.mIsTimerBurstCircle && !bottomAnimationConfig.mIsLongExpose) {
            bottomAnimationConfig.mCurrentMode = isFullScreen() ? 166 : 176;
        }
        float f = 0.0f;
        switch (bottomAnimationConfig.mCurrentMode) {
            case 161:
            case 174:
            case 183:
            case 184:
            case 185:
            case 189:
            case 212:
            case 213:
                this.mCirclePaintItem.setNeedSplit(true);
                CameraSnapPaintCircle cameraSnapPaintCircle = this.mCirclePaintItem;
                cameraSnapPaintCircle.setTargetStrokeWidth(cameraSnapPaintCircle.mBaseStokeWidth * 0.8f);
                this.mCirclePaintItem.clearSegments();
                int i = bottomAnimationConfig.mCurrentMode;
                if (i == 177 || i == 184) {
                    CameraSnapPaintCircle cameraSnapPaintCircle2 = this.mCirclePaintItem;
                    cameraSnapPaintCircle2.mCurrentWidthPercent *= cameraSnapPaintCircle2.mBaseWidthPercent * 1.2f;
                }
                this.mCirclePaintItem.setTargetAlpha(CameraPaintBase.ALPHA_HINT);
                break;
            case 162:
            case 180:
            case 204:
            case 207:
            case 214:
                this.mCirclePaintItem.setNeedSplit(false);
                this.mRoundPaintItem.setRecordingBgColor(-1);
                CameraSnapPaintCircle cameraSnapPaintCircle3 = this.mCirclePaintItem;
                cameraSnapPaintCircle3.setTargetWidthPercent(cameraSnapPaintCircle3.mBaseWidthPercent * 0.93f);
                break;
            case 163:
            case 177:
                float f2 = bottomAnimationConfig.mIsVertical ? 1.05f : 1.3f;
                boolean z = this.mRoundPaintItem.mBaseAlpha == 0;
                float f3 = f2 * 0.813f;
                this.mBottomPatinItem.setTargetWidthPercent(f3);
                CameraSnapPaintBottom cameraSnapPaintBottom = this.mBottomPatinItem;
                if (z) {
                    cameraSnapPaintBottom.setFixCircleOffset(false);
                    this.mBottomPatinItem.setTargetAlpha(0);
                } else {
                    cameraSnapPaintBottom.setFixCircleOffset(true);
                    this.mBottomPatinItem.setTargetAlpha(255);
                }
                this.mRoundPaintItem.setShowCenterBitmap(false);
                if (z) {
                    CameraSnapPaintRound cameraSnapPaintRound2 = this.mRoundPaintItem;
                    cameraSnapPaintRound2.isRecording = false;
                    cameraSnapPaintRound2.setTargetAlpha(0);
                } else {
                    CameraSnapPaintRound cameraSnapPaintRound3 = this.mRoundPaintItem;
                    cameraSnapPaintRound3.isRecording = false;
                    cameraSnapPaintRound3.setCurrentWidthPercent(this.mCirclePaintItem.mCurrentWidthPercent);
                    this.mRoundPaintItem.setTargetAlpha(255);
                    this.mRoundPaintItem.setTargetWidthPercent(this.mCirclePaintItem.mBaseWidthPercent * 0.68f);
                }
                this.mCirclePaintItem.setNeedSplit(false);
                CameraSnapPaintCircle cameraSnapPaintCircle4 = this.mCirclePaintItem;
                cameraSnapPaintCircle4.setTargetStrokeWidth(cameraSnapPaintCircle4.mBaseStokeWidth);
                if (z) {
                    this.mCirclePaintItem.setTargetWidthPercent(f3);
                    this.mCirclePaintItem.setTargetAlpha(51);
                } else {
                    CameraSnapPaintCircle cameraSnapPaintCircle5 = this.mCirclePaintItem;
                    cameraSnapPaintCircle5.setTargetWidthPercent(cameraSnapPaintCircle5.mBaseWidthPercent * 0.68f);
                }
                this.mRecordingReferredPaints.add(this.mBottomPatinItem);
                break;
            case 166:
                this.mBottomPatinItem.setTargetAlpha(0);
                this.mBottomPatinItem.setResult();
                this.mRoundPaintItem.prepareRecord(true, true, SNAP_ROUND_ORIGINAL_WIDTH);
                break;
            case 167:
                if (bottomAnimationConfig.mIsLongExpose) {
                    onLongExposePrepare(bottomAnimationConfig);
                    break;
                }
            case 169:
            case 172:
            case 173:
            case 208:
                this.mSecondPaintItem.removePatternOnly();
                break;
            case 176:
                this.mBottomPatinItem.setTargetAlpha(0);
                CameraSnapPaintBottom cameraSnapPaintBottom2 = this.mBottomPatinItem;
                cameraSnapPaintBottom2.setTargetWidthPercent(cameraSnapPaintBottom2.mBaseWidthPercent * 0.85f);
                this.mRecordingReferredPaints.add(this.mBottomPatinItem);
                this.mRoundPaintItem.prepareRecord(true, true, SNAP_ROUND_ORIGINAL_WIDTH);
                this.mCirclePaintItem.setBaseWidthPercent(0.69f);
                this.mCirclePaintItem.setTargetWidthPercent(0.69f);
                CameraSnapPaintCircle cameraSnapPaintCircle6 = this.mCirclePaintItem;
                cameraSnapPaintCircle6.setTargetStrokeWidth(cameraSnapPaintCircle6.mBaseStokeWidth);
                this.mCirclePaintItem.setNeedSplit(false);
                break;
            case 179:
                this.mCirclePaintItem.setNeedSplit(true);
                this.mCirclePaintItem.setSpaceAngel(0.0f);
                this.mCirclePaintItem.clearSegments();
                CameraSnapPaintCircle cameraSnapPaintCircle7 = this.mCirclePaintItem;
                cameraSnapPaintCircle7.setTargetStrokeWidth(cameraSnapPaintCircle7.mBaseStokeWidth * 0.7f);
                CameraSnapPaintCircle cameraSnapPaintCircle8 = this.mCirclePaintItem;
                cameraSnapPaintCircle8.setTargetWidthPercent(cameraSnapPaintCircle8.mBaseWidthPercent * 1.18f);
                this.mCirclePaintItem.setNeedProcessShade(true, this.mRoundPaintItem.mCurrentColor);
                this.mRecordingReferredPaints.add(this.mCirclePaintItem);
                this.mCenterVVPaintItem.setTargetAlpha(0);
                CameraSnapPaintCenterVV cameraSnapPaintCenterVV = this.mCenterVVPaintItem;
                cameraSnapPaintCenterVV.isRecording = true;
                this.mRecordingReferredPaints.add(cameraSnapPaintCenterVV);
                break;
            case 187:
                if (bottomAnimationConfig.mDuration > 600) {
                    this.mSecondPaintItem.removePatternOnly();
                    this.mSecondPaintItem.setTargetAlpha(CameraPaintBase.ALPHA_HINT);
                    break;
                }
                break;
            case 209:
                this.mCirclePaintItem.setNeedSplit(true);
                this.mCirclePaintItem.setSpaceAngel(0.0f);
                this.mCirclePaintItem.clearSegments();
                this.mCirclePaintItem.setTargetAlpha(CameraPaintBase.ALPHA_HINT);
                CameraSnapPaintRound cameraSnapPaintRound4 = this.mRoundPaintItem;
                cameraSnapPaintRound4.isRecording = false;
                cameraSnapPaintRound4.setTargetAlpha(0);
                break;
        }
    }

    public void removeLastSegment() {
        if (!this.mCirclePaintItem.getSegmentRatios().isEmpty()) {
            this.mCirclePaintItem.removeLastSegmentAndGetLastTime();
            invalidateSelf();
        }
    }

    public void resetRecordingState() {
        this.mCirclePaintItem.resetRecordingState();
        this.mRoundPaintItem.resetRecordingState();
        this.mSecondPaintItem.resetRecordingState();
        this.mBottomPatinItem.resetRecordingState();
        this.mCenterVVPaintItem.resetRecordingState();
    }

    public void resumeRecording() {
        updateLiveAnimationConfig();
        ValueAnimator valueAnimator = this.mTimeAnimator;
        if (valueAnimator != null && valueAnimator.isPaused()) {
            this.mTimeAnimator.resume();
        }
    }

    public void setAlpha(int i) {
    }

    public void setBounds(Rect rect) {
        super.setBounds(rect);
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void setDurationText(String str) {
        this.mCenterVVPaintItem.setDurationText(str);
        invalidateSelf();
    }

    public void setSpecificProgress(@IntRange(from = 0, to = 100) int i) {
        CameraSnapPaintCircle cameraSnapPaintCircle = this.mCirclePaintItem;
        cameraSnapPaintCircle.isRecording = true;
        cameraSnapPaintCircle.setSpecificProgress(i);
        invalidateSelf();
    }

    public void setStopButtonEnable(boolean z, boolean z2) {
        if (z) {
            this.mRoundPaintItem.setCurrentAlpha(255);
            if (z2) {
                this.mRoundPaintItem.setTargetAlpha(255);
                return;
            }
            return;
        }
        this.mRoundPaintItem.setCurrentAlpha(77);
        this.mRoundPaintItem.setTargetAlpha(77);
    }

    public void setWidthHeight(float f, float f2) {
        float f3 = f / 2.0f;
        float f4 = f2 / 2.0f;
        float min = Math.min(f, f2) / 2.0f;
        this.mCirclePaintItem.setMiddle(f3, f4, min);
        this.mRoundPaintItem.setMiddle(f3, f4, min);
        this.mSecondPaintItem.setMiddle(f3, f4, min);
        this.mBottomPatinItem.setMiddle(f3, f4, min);
        this.mCenterVVPaintItem.setMiddle(f3, f4, min);
    }

    public void showBitmap(Context context, @DrawableRes int i) {
        this.mRoundPaintItem.showTargetBitmap(context, i);
        invalidateSelf();
    }

    public void showCirclePaintItem() {
        this.mCirclePaintItem.setVisible(0);
    }

    public void showPaintCenterVVItem() {
        CameraSnapPaintCenterVV cameraSnapPaintCenterVV = this.mCenterVVPaintItem;
        CameraSnapPaintRound cameraSnapPaintRound = this.mRoundPaintItem;
        cameraSnapPaintCenterVV.setTargetValues(cameraSnapPaintRound.mCurrentWidthPercent, cameraSnapPaintRound.mCurrentColor, cameraSnapPaintRound.mCurrentAlpha, cameraSnapPaintRound.mCurrentStrokeWidth);
        this.mCenterVVPaintItem.setResult();
        this.mCenterVVPaintItem.prepareRecord();
        this.mCenterVVPaintItem.setVisible(0);
    }

    public void showRoundPaintItem() {
        this.mRoundPaintItem.setVisible(0);
    }

    public void start() {
    }

    public void startModeChangeAnimation() {
        this.mModeChangeAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mModeChangeAnimator.setDuration(300);
        this.mModeChangeAnimator.setInterpolator(new DecelerateInterpolator() {
            public float getInterpolation(float f) {
                float interpolation = super.getInterpolation(f);
                CameraSnapAnimateDrawable.this.mCirclePaintItem.updateValue(interpolation);
                CameraSnapAnimateDrawable.this.mRoundPaintItem.updateValue(interpolation);
                CameraSnapAnimateDrawable.this.mSecondPaintItem.updateValue(interpolation);
                CameraSnapAnimateDrawable.this.mBottomPatinItem.updateValue(interpolation);
                CameraSnapAnimateDrawable.this.mCenterVVPaintItem.updateValue(interpolation);
                CameraSnapAnimateDrawable.this.invalidateSelf();
                return interpolation;
            }
        });
        this.mModeChangeAnimator.setupEndValues();
        this.mModeChangeAnimator.start();
    }

    public void startRebound() {
        initReboundSystem();
        this.mReboundAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mReboundAnimator.setStartDelay(300);
        this.mReboundAnimator.setDuration(8500);
        this.mReboundAnimator.setInterpolator(new LinearInterpolator() {
            public float getInterpolation(float f) {
                LongPressIncreaseListener access$1100;
                boolean z;
                float interpolation = super.getInterpolation(f);
                float f2 = 8500.0f * interpolation;
                if (f2 > 500.0f) {
                    CameraSnapAnimateDrawable.this.mCirclePaintItem.timeAngle = ((f2 - 500.0f) / 8000.0f) * 360.0f;
                }
                if (f2 <= 1000.0f) {
                    CameraSnapAnimateDrawable.this.mLongPressIncreaseListener.OnValueUp((((f2 / 31.25f) * 2.0f) - 10.0f) / 90.0f);
                    access$1100 = CameraSnapAnimateDrawable.this.mLongPressIncreaseListener;
                    z = false;
                } else {
                    int i = ((f2 - 1000.0f) > 30.0f ? 1 : ((f2 - 1000.0f) == 30.0f ? 0 : -1));
                    access$1100 = CameraSnapAnimateDrawable.this.mLongPressIncreaseListener;
                    if (i < 0) {
                        z = true;
                    } else {
                        access$1100.OnIncrease((((f2 / 31.25f) * 2.0f) - 10.0f) / 90.0f);
                        return interpolation;
                    }
                }
                access$1100.OnTheValue(z);
                return interpolation;
            }
        });
        this.mReboundAnimator.addListener(new AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                ((BeautyRecording) ModeCoordinatorImpl.getInstance().getAttachProtocol(173)).handleBeautyRecordingStop();
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
                ((BeautyRecording) ModeCoordinatorImpl.getInstance().getAttachProtocol(173)).handleBeautyRecordingStart();
            }
        });
        this.mReboundAnimator.start();
    }

    public void startRecord(final BottomAnimationConfig bottomAnimationConfig) {
        if (this.mRecordingReferredPaints != null) {
            cancelAnimation();
            this.mTimeAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            int i = bottomAnimationConfig.mCurrentMode;
            if (i == 174 || i == 183 || i == 212) {
                updateLiveAnimationConfig();
            }
            this.mTimeAnimator.setDuration((long) bottomAnimationConfig.mDuration);
            this.mTimeAnimator.setInterpolator(new LinearInterpolator() {
                public float getInterpolation(float f) {
                    float f2 = f * 360.0f;
                    int i = bottomAnimationConfig.mCurrentMode;
                    if (i == 174 || i == 183 || i == 212) {
                        f2 = (((float) (((long) ((((double) (System.currentTimeMillis() - CameraSnapAnimateDrawable.this.mLiveStartTime)) * 1.0d) / ((double) CameraSnapAnimateDrawable.this.mLiveSpeed))) + CameraSnapAnimateDrawable.this.mLiveTotalTime)) * 360.0f) / ((float) bottomAnimationConfig.mDuration);
                        if (f2 > 360.0f) {
                            f2 = 360.0f;
                        }
                    }
                    for (CameraPaintBase cameraPaintBase : CameraSnapAnimateDrawable.this.mRecordingReferredPaints) {
                        cameraPaintBase.timeAngle = f2;
                        int i2 = bottomAnimationConfig.mCurrentMode;
                        if (i2 == 169 || i2 == 187) {
                            DataRepository.dataItemRunning().setCameraSnapPaintSecondTimeAngleRunning(f2);
                        }
                    }
                    float interpolation = super.getInterpolation(f);
                    CameraSnapAnimateDrawable.this.invalidateSelf();
                    return interpolation;
                }
            });
            this.mTimeAnimator.removeAllListeners();
            this.mTimeAnimator.addListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    if (bottomAnimationConfig.mCurrentMode == 187) {
                        AmbilightProtocol ambilightProtocol = (AmbilightProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(420);
                        if (ambilightProtocol != null) {
                            ambilightProtocol.onShutterAnimationEnd();
                        }
                    }
                    if (bottomAnimationConfig.mIsLongExpose) {
                        CameraSnapAnimateDrawable.this.mCirclePaintItem.setTargetAlpha(CameraSnapAnimateDrawable.this.mCirclePaintItem.mBaseAlpha);
                        CameraSnapAnimateDrawable.this.mCirclePaintItem.setResult();
                        CameraSnapAnimateDrawable.this.mCirclePaintItem.setNeedSplit(false);
                        CameraSnapAnimateDrawable.this.mCirclePaintItem.clearSegments();
                    }
                }

                public void onAnimationRepeat(Animator animator) {
                    int i = bottomAnimationConfig.mCurrentMode;
                    if (i != 174 && i != 183) {
                        for (CameraPaintBase cameraPaintBase : CameraSnapAnimateDrawable.this.mRecordingReferredPaints) {
                            cameraPaintBase.reverseClock();
                            int i2 = bottomAnimationConfig.mCurrentMode;
                            if (i2 == 169 || i2 == 187) {
                                DataRepository.dataItemRunning().setCameraSnapPaintSecondClockWiseRunning(cameraPaintBase.isClockwise);
                            }
                        }
                    }
                }

                public void onAnimationStart(Animator animator) {
                    for (CameraPaintBase cameraPaintBase : CameraSnapAnimateDrawable.this.mRecordingReferredPaints) {
                        cameraPaintBase.isRecording = true;
                    }
                    CameraSnapAnimateDrawable.this.mRoundPaintItem.isRecordingCircle = bottomAnimationConfig.mIsRecordingCircle;
                    CameraSnapPaintRound access$100 = CameraSnapAnimateDrawable.this.mRoundPaintItem;
                    BottomAnimationConfig bottomAnimationConfig = bottomAnimationConfig;
                    access$100.isRoundingCircle = bottomAnimationConfig.mIsRoundingCircle;
                    if (bottomAnimationConfig.mIsLongExpose) {
                        CameraSnapAnimateDrawable.this.mCirclePaintItem.setTargetAlpha(CameraPaintBase.ALPHA_HINT);
                        CameraSnapAnimateDrawable.this.mCirclePaintItem.setNeedSplit(true);
                        CameraSnapAnimateDrawable.this.mCirclePaintItem.clearSegments();
                    }
                }
            });
            if (bottomAnimationConfig.mShouldRepeat) {
                this.mTimeAnimator.setRepeatMode(1);
                this.mTimeAnimator.setRepeatCount(-1);
            }
            this.mTimeAnimator.start();
        }
    }

    public void startRecordAnimation(BottomAnimationConfig bottomAnimationConfig) {
        if (bottomAnimationConfig.mIsStart) {
            startRecord(bottomAnimationConfig);
        } else {
            finishRecord(bottomAnimationConfig);
        }
    }

    public void startRingAnimation() {
        cancelAnimation();
        this.mCirclePaintItem.setRingVisible(0);
        this.mRoundPaintItem.setVisible(8);
        this.mRingAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mRingAnimator.setDuration(500);
        this.mRingAnimator.setRepeatCount(-1);
        this.mRingAnimator.setRepeatMode(1);
        this.mRingAnimator.setInterpolator(new BounceInterpolator() {
            public float getInterpolation(float f) {
                float interpolation = super.getInterpolation(f);
                CameraSnapAnimateDrawable.this.mCirclePaintItem.updateValue(interpolation);
                CameraSnapAnimateDrawable.this.invalidateSelf();
                return interpolation;
            }
        });
        this.mRingAnimator.start();
    }

    public void startScaleDownAnimation(int i) {
        cancelScaleUpAnimation();
        if (isFullScreen()) {
            CameraSnapPaintCircle cameraSnapPaintCircle = this.mCirclePaintItem;
            cameraSnapPaintCircle.setTargetWidthPercent(cameraSnapPaintCircle.mBaseWidthPercent * 0.88f);
            CameraSnapPaintRound cameraSnapPaintRound = this.mRoundPaintItem;
            cameraSnapPaintRound.setTargetWidthPercent(cameraSnapPaintRound.mBaseWidthPercent * 0.88f);
            CameraSnapPaintCircle cameraSnapPaintCircle2 = this.mCirclePaintItem;
            cameraSnapPaintCircle2.setTargetStrokeWidth(cameraSnapPaintCircle2.mBaseStokeWidth * 1.1f);
        } else {
            CameraSnapPaintCircle cameraSnapPaintCircle3 = this.mCirclePaintItem;
            cameraSnapPaintCircle3.setTargetWidthPercent(cameraSnapPaintCircle3.mBaseWidthPercent * 0.88f);
            CameraSnapPaintRound cameraSnapPaintRound2 = this.mRoundPaintItem;
            cameraSnapPaintRound2.setTargetWidthPercent(cameraSnapPaintRound2.mBaseWidthPercent * 0.88f);
            CameraSnapPaintBottom cameraSnapPaintBottom = this.mBottomPatinItem;
            if (cameraSnapPaintBottom.mCurrentAlpha != 0) {
                cameraSnapPaintBottom.setTargetWidthPercent(cameraSnapPaintBottom.mBaseWidthPercent * 1.02f);
            }
        }
        if (this.mCenterVVPaintItem.showBitmapPattern()) {
            this.mCenterVVPaintItem.setBitmapPatternTargetScale(0.9f);
        }
        this.mRoundPaintItem.setBitmapPatternTargetScale(0.9f);
        this.mScaleDownAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mScaleDownAnimator.setDuration(200);
        this.mScaleDownAnimator.setInterpolator(new CubicEaseOutInterpolator() {
            public float getInterpolation(float f) {
                float interpolation = super.getInterpolation(f);
                CameraSnapAnimateDrawable.this.mRoundPaintItem.updateValue(interpolation);
                CameraSnapAnimateDrawable.this.mCirclePaintItem.updateValue(interpolation);
                if (CameraSnapAnimateDrawable.this.mBottomPatinItem.mCurrentAlpha != 0) {
                    CameraSnapAnimateDrawable.this.mBottomPatinItem.updateValue(interpolation);
                }
                if (CameraSnapAnimateDrawable.this.mCenterVVPaintItem.showBitmapPattern()) {
                    CameraSnapAnimateDrawable.this.mCenterVVPaintItem.updateValue(interpolation);
                }
                CameraSnapAnimateDrawable.this.invalidateSelf();
                return interpolation;
            }
        });
        this.mScaleDownAnimator.setupEndValues();
        this.mScaleDownAnimator.start();
    }

    public void startScaleUpAnimation(long j, AnimatorListener animatorListener) {
        cancelScaleDownAnimation();
        CameraSnapPaintCircle cameraSnapPaintCircle = this.mCirclePaintItem;
        cameraSnapPaintCircle.setTargetWidthPercent(cameraSnapPaintCircle.mBaseWidthPercent);
        CameraSnapPaintCircle cameraSnapPaintCircle2 = this.mCirclePaintItem;
        cameraSnapPaintCircle2.setTargetStrokeWidth(cameraSnapPaintCircle2.mBaseStokeWidth);
        CameraSnapPaintRound cameraSnapPaintRound = this.mRoundPaintItem;
        cameraSnapPaintRound.setTargetWidthPercent(cameraSnapPaintRound.mBaseWidthPercent);
        CameraSnapPaintBottom cameraSnapPaintBottom = this.mBottomPatinItem;
        if (cameraSnapPaintBottom.mCurrentAlpha != 0) {
            cameraSnapPaintBottom.setTargetWidthPercent(cameraSnapPaintBottom.mBaseWidthPercent);
        }
        if (this.mCenterVVPaintItem.showBitmapPattern()) {
            this.mCenterVVPaintItem.setBitmapPatternTargetScale(1.0f);
        }
        this.mRoundPaintItem.setBitmapPatternTargetScale(1.0f);
        this.mScaleUpAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mScaleUpAnimator.setStartDelay(j);
        this.mScaleUpAnimator.setDuration(400);
        this.mScaleUpAnimator.setInterpolator(new CubicEaseOutInterpolator() {
            public float getInterpolation(float f) {
                float interpolation = super.getInterpolation(f);
                CameraSnapAnimateDrawable.this.mRoundPaintItem.updateValue(interpolation);
                CameraSnapAnimateDrawable.this.mCirclePaintItem.updateValue(interpolation);
                if (CameraSnapAnimateDrawable.this.mBottomPatinItem.mCurrentAlpha != 0) {
                    CameraSnapAnimateDrawable.this.mBottomPatinItem.updateValue(interpolation);
                }
                if (CameraSnapAnimateDrawable.this.mCenterVVPaintItem.showBitmapPattern()) {
                    CameraSnapAnimateDrawable.this.mCenterVVPaintItem.updateValue(interpolation);
                }
                CameraSnapAnimateDrawable.this.invalidateSelf();
                return interpolation;
            }
        });
        this.mScaleUpAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                CameraSnapAnimateDrawable.this.mScaleUpAnimator = null;
            }

            public void onAnimationEnd(Animator animator) {
                CameraSnapAnimateDrawable.this.mScaleUpAnimator = null;
            }

            public void onAnimationStart(Animator animator) {
            }
        });
        if (animatorListener != null) {
            this.mScaleUpAnimator.addListener(animatorListener);
        }
        this.mScaleUpAnimator.setupEndValues();
        this.mScaleUpAnimator.start();
    }

    public void stop() {
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stopRecord(final BottomAnimationConfig bottomAnimationConfig) {
        if (this.mRecordingReferredPaints != null) {
            boolean z = false;
            if (!isInLongExpose(bottomAnimationConfig) || this.mTimeAnimator == null) {
                cancelAnimation();
                cancelScaleDownAnimation();
                cancelScaleUpAnimation();
                this.mRoundPaintItem.prepareRecord(false, false, SNAP_ROUND_ORIGINAL_WIDTH);
                if (bottomAnimationConfig.mIsTimerBurstCircle && !bottomAnimationConfig.mIsLongExpose) {
                    bottomAnimationConfig.mCurrentMode = isFullScreen() ? 166 : 176;
                }
                switch (bottomAnimationConfig.mCurrentMode) {
                    case 161:
                    case 174:
                    case 183:
                    case 184:
                    case 185:
                    case 189:
                    case 212:
                    case 213:
                        for (CameraPaintBase cameraPaintBase : this.mRecordingReferredPaints) {
                            cameraPaintBase.setTargetStrokeWidth(cameraPaintBase.mBaseStokeWidth);
                            cameraPaintBase.setTargetWidthPercent(cameraPaintBase.mBaseWidthPercent).setTargetAlpha(cameraPaintBase.mBaseAlpha);
                        }
                        break;
                    case 162:
                    case 172:
                    case 173:
                    case 180:
                    case 204:
                    case 207:
                    case 214:
                        if (bottomAnimationConfig.mIsPostProcessing) {
                            this.mRoundPaintItem.setTargetAlpha(0);
                        }
                        CameraSnapPaintRound cameraSnapPaintRound = this.mRoundPaintItem;
                        cameraSnapPaintRound.setTargetWidthPercent(cameraSnapPaintRound.mBaseWidthPercent);
                        for (CameraPaintBase cameraPaintBase2 : this.mRecordingReferredPaints) {
                            cameraPaintBase2.setTargetWidthPercent(cameraPaintBase2.mBaseWidthPercent);
                            cameraPaintBase2.setTargetAlpha(255);
                        }
                        break;
                    case 163:
                    case 165:
                    case 177:
                        for (CameraPaintBase cameraPaintBase3 : this.mRecordingReferredPaints) {
                            cameraPaintBase3.setTargetStrokeWidth(cameraPaintBase3.mBaseStokeWidth);
                            cameraPaintBase3.setTargetWidthPercent(cameraPaintBase3.mBaseWidthPercent).setTargetAlpha(cameraPaintBase3.mBaseAlpha);
                        }
                        if (this.mRoundPaintItem.mBaseAlpha == 0) {
                            z = true;
                        }
                        CameraSnapPaintRound cameraSnapPaintRound2 = this.mRoundPaintItem;
                        if (!z) {
                            cameraSnapPaintRound2.setTargetWidthPercent(cameraSnapPaintRound2.mBaseWidthPercent);
                            break;
                        } else {
                            cameraSnapPaintRound2.setTargetWidthPercent(cameraSnapPaintRound2.mCurrentWidthPercent);
                            CameraSnapPaintRound cameraSnapPaintRound3 = this.mRoundPaintItem;
                            cameraSnapPaintRound3.setTargetAlpha(cameraSnapPaintRound3.mBaseAlpha);
                            break;
                        }
                    case 166:
                        this.mRoundPaintItem.setTargetAlpha(0);
                        this.mRoundPaintItem.prepareRecord(true, false, SNAP_ROUND_ORIGINAL_WIDTH);
                        for (CameraPaintBase cameraPaintBase4 : this.mRecordingReferredPaints) {
                            cameraPaintBase4.setTargetWidthPercent(cameraPaintBase4.mBaseWidthPercent).setTargetAlpha(cameraPaintBase4.mBaseAlpha);
                        }
                        break;
                    case 167:
                        if (bottomAnimationConfig.mIsLongExpose) {
                            onLongExposeFinish(bottomAnimationConfig);
                            this.mRecordingReferredPaints.add(this.mBottomPatinItem);
                            break;
                        }
                    case 176:
                        this.mRoundPaintItem.prepareRecord(true, false, SNAP_ROUND_ORIGINAL_WIDTH);
                        CameraSnapPaintRound cameraSnapPaintRound4 = this.mRoundPaintItem;
                        cameraSnapPaintRound4.setTargetWidthPercent(cameraSnapPaintRound4.mBaseWidthPercent);
                        for (CameraPaintBase cameraPaintBase5 : this.mRecordingReferredPaints) {
                            cameraPaintBase5.setTargetWidthPercent(cameraPaintBase5.mBaseWidthPercent).setTargetAlpha(cameraPaintBase5.mBaseAlpha);
                        }
                        this.mBottomPatinItem.setCurrentAlpha(255);
                        this.mBottomPatinItem.setTargetAlpha(255);
                        CameraSnapPaintBottom cameraSnapPaintBottom = this.mBottomPatinItem;
                        cameraSnapPaintBottom.setTargetWidthPercent(cameraSnapPaintBottom.mBaseWidthPercent);
                        this.mCirclePaintItem.setBaseWidthPercent(0.61f);
                        CameraSnapPaintCircle cameraSnapPaintCircle = this.mCirclePaintItem;
                        cameraSnapPaintCircle.setTargetWidthPercent(cameraSnapPaintCircle.mBaseWidthPercent);
                        this.mCirclePaintItem.setTargetAlpha(0);
                        this.mCirclePaintItem.setResult();
                        break;
                    case 179:
                        this.mCenterVVPaintItem.setDurationText(null);
                        for (CameraPaintBase cameraPaintBase6 : this.mRecordingReferredPaints) {
                            cameraPaintBase6.setTargetStrokeWidth(cameraPaintBase6.mBaseStokeWidth);
                            cameraPaintBase6.setTargetWidthPercent(cameraPaintBase6.mBaseWidthPercent).setTargetAlpha(cameraPaintBase6.mBaseAlpha);
                        }
                        break;
                    default:
                        for (CameraPaintBase cameraPaintBase7 : this.mRecordingReferredPaints) {
                            cameraPaintBase7.setTargetValues(cameraPaintBase7.mCurrentWidthPercent, cameraPaintBase7.mCurrentColor, CameraPaintBase.ALPHA_OUTSTANDING, cameraPaintBase7.mCurrentStrokeWidth);
                        }
                        break;
                }
                this.mTimeAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                this.mTimeAnimator.setDuration(300);
                this.mTimeAnimator.setInterpolator(new CubicEaseOutInterpolator() {
                    public float getInterpolation(float f) {
                        float interpolation = super.getInterpolation(f);
                        CameraSnapAnimateDrawable.this.mRoundPaintItem.updateValue(interpolation);
                        for (CameraPaintBase updateValue : CameraSnapAnimateDrawable.this.mRecordingReferredPaints) {
                            updateValue.updateValue(interpolation);
                        }
                        CameraSnapAnimateDrawable.this.invalidateSelf();
                        return interpolation;
                    }
                });
                this.mTimeAnimator.removeAllListeners();
                this.mTimeAnimator.addListener(new AnimatorListener() {
                    public void onAnimationCancel(Animator animator) {
                    }

                    public void onAnimationEnd(Animator animator) {
                        for (CameraPaintBase cameraPaintBase : CameraSnapAnimateDrawable.this.mRecordingReferredPaints) {
                            cameraPaintBase.needZero = false;
                        }
                        CameraSnapAnimateDrawable.this.mRoundPaintItem.setShowCenterBitmap(true);
                        CameraSnapAnimateDrawable.this.mRoundPaintItem.setRecordingBgColor(0);
                        CameraSnapAnimateDrawable.this.mRoundPaintItem.resetRecordingState();
                        CameraSnapAnimateDrawable.this.mRoundPaintItem.setCurrentWidthPercent(CameraSnapAnimateDrawable.this.mRoundPaintItem.mBaseWidthPercent);
                        CameraSnapAnimateDrawable.this.mCirclePaintItem.setTargetAlpha(CameraSnapAnimateDrawable.this.mCirclePaintItem.mBaseAlpha);
                        CameraSnapAnimateDrawable.this.mCirclePaintItem.setResult();
                    }

                    public void onAnimationRepeat(Animator animator) {
                    }

                    public void onAnimationStart(Animator animator) {
                        for (CameraPaintBase cameraPaintBase : CameraSnapAnimateDrawable.this.mRecordingReferredPaints) {
                            cameraPaintBase.resetRecordingState();
                            cameraPaintBase.needZero = false;
                        }
                    }
                });
                this.mTimeAnimator.start();
                return;
            }
            this.mRoundPaintItem.setTargetAlpha(0);
            this.mRoundPaintItem.setResult();
            this.mTimeAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    CameraSnapAnimateDrawable.this.mTimeAnimator = null;
                    CameraSnapAnimateDrawable.this.stopRecord(bottomAnimationConfig);
                }
            });
        }
    }

    public void stopRingAnimation() {
        ValueAnimator valueAnimator = this.mRingAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mCirclePaintItem.setRingVisible(8);
            this.mRoundPaintItem.setVisible(0);
            invalidateSelf();
        }
    }

    public void test() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(1000);
        ofFloat.setInterpolator(new CubicEaseOutInterpolator() {
            public float getInterpolation(float f) {
                float interpolation = super.getInterpolation(f);
                CameraSnapAnimateDrawable.this.mSecondPaintItem.updateValue(interpolation);
                CameraSnapAnimateDrawable.this.invalidateSelf();
                return interpolation;
            }
        });
        ofFloat.setupEndValues();
        ofFloat.start();
    }
}
