package com.android.camera.ui;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.runing.ComponentRunningTiltValue;
import com.android.camera.effect.EffectController;
import miui.view.animation.CubicEaseOutInterpolator;

public class V6EffectCropView extends View implements V6FunctionUI {
    private static final int ANIMATE_RADIUS = 2;
    private static final int ANIMATE_RANGE = 1;
    private static final int ANIMATE_START_RADIUS = (Display.getWindowHeight() / 2);
    private static final int ANIMATE_START_RANGE = Display.getWindowHeight();
    private static final int ANIMATION_TIME = 600;
    private static final int CIRCLE_RESIZE_TOUCH_TOLERANCE = Util.dpToPixel(36.0f);
    private static final int CORNER_BALL_RADIUS = Util.dpToPixel(5.0f);
    private static final int DEFAULT_RADIUS = (Display.getWindowHeight() / 6);
    private static final int DEFAULT_RANGE = (Display.getWindowHeight() / 3);
    private static final int HIDE_TILT_SHIFT_MASK = 2;
    private static final int MIN_CROP_WIDTH_HEIGHT = Util.dpToPixel(64.0f);
    private static final float MIN_DIS_FOR_MOVE_POINT = ((float) (Util.dpToPixel(30.0f) * Util.dpToPixel(30.0f)));
    private static final int MIN_DIS_FOR_SLOPE = (Util.dpToPixel(10.0f) * Util.dpToPixel(10.0f));
    private static final int MIN_RANGE = Util.dpToPixel(20.0f);
    private static final int MOVE_BLOCK = 16;
    private static final int MOVE_BOTTOM = 8;
    private static final int MOVE_LEFT = 1;
    private static final int MOVE_POINT1 = 257;
    private static final int MOVE_POINT2 = 258;
    private static final int MOVE_RADIUS = 32;
    private static final int MOVE_RANGE = 260;
    private static final int MOVE_RIGHT = 4;
    private static final int MOVE_TOP = 2;
    private static final int SHOW_TILT_SHIFT_MASK = 1;
    private static final int TOUCH_TOLERANCE = Util.dpToPixel(18.0f);
    private Handler mAnimateHandler;
    /* access modifiers changed from: private */
    public int mAnimateRadius = 0;
    /* access modifiers changed from: private */
    public int mAnimateRangeWidth = 0;
    private HandlerThread mAnimateThread;
    /* access modifiers changed from: private */
    public int mAnimationStartRadius;
    /* access modifiers changed from: private */
    public int mAnimationStartRange;
    /* access modifiers changed from: private */
    public long mAnimationStartTime;
    /* access modifiers changed from: private */
    public long mAnimationTotalTime;
    private final Paint mBorderPaint = new Paint();
    private int mCenterLineSquare;
    private final Paint mCornerPaint;
    /* access modifiers changed from: private */
    public final RectF mCropBounds = new RectF();
    /* access modifiers changed from: private */
    public final RectF mDefaultCircleBounds = new RectF();
    private final RectF mDefaultRectBounds = new RectF();
    private final RectF mDisplayBounds = new RectF();
    private final PointF mEffectPoint1 = new PointF();
    private final PointF mEffectPoint2 = new PointF();
    private final RectF mEffectRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
    /* access modifiers changed from: private */
    public Interpolator mInterpolator = new CubicEaseOutInterpolator();
    private boolean mIsCircle;
    private boolean mIsInTapSlop;
    private boolean mIsRect;
    private double mLastMoveDis;
    private float mLastX;
    private float mLastY;
    private int mMaxRange;
    private int mMovingEdges;
    private float mNormalizedWidth = 0.0f;
    private final Point mPoint1 = new Point();
    private final Point mPoint2 = new Point();
    /* access modifiers changed from: private */
    public int mRadius = 0;
    /* access modifiers changed from: private */
    public int mRangeWidth = 0;
    private int mTapSlop;
    /* access modifiers changed from: private */
    public boolean mTiltShiftMaskAlive;
    /* access modifiers changed from: private */
    public ObjectAnimator mTiltShiftMaskFadeInAnimator;
    /* access modifiers changed from: private */
    public ObjectAnimator mTiltShiftMaskFadeOutAnimator;
    private AnimatorListenerAdapter mTiltShiftMaskFadeOutListener = new AnimatorListenerAdapter() {
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            if (V6EffectCropView.this.mTiltShiftMaskFadeOutAnimator.isRunning()) {
                V6EffectCropView.this.mTiltShiftMaskAlive = false;
            }
        }
    };
    private Handler mTiltShiftMaskHandler;
    private final Point mTouchCenter = new Point();
    private boolean mVisible;

    public V6EffectCropView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBorderPaint.setStyle(Style.STROKE);
        this.mBorderPaint.setColor(-1);
        this.mBorderPaint.setStrokeWidth((float) (C0124O00000oO.isPad() ? 4 : 2));
        this.mCornerPaint = new Paint();
        this.mCornerPaint.setAntiAlias(true);
        this.mCornerPaint.setColor(-1);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTapSlop = viewConfiguration.getScaledTouchSlop() * viewConfiguration.getScaledTouchSlop();
        this.mTiltShiftMaskFadeInAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(context, R.anim.tilt_shift_mask_fade_in);
        this.mTiltShiftMaskFadeOutAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(context, R.anim.tilt_shift_mask_fade_out);
        this.mTiltShiftMaskFadeInAnimator.setTarget(EffectController.getInstance());
        ObjectAnimator objectAnimator = this.mTiltShiftMaskFadeInAnimator;
        objectAnimator.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofKeyframe(objectAnimator.getPropertyName(), new Keyframe[]{Keyframe.ofFloat(0.0f), Keyframe.ofFloat(0.3f, 1.0f), Keyframe.ofFloat(1.0f, 1.0f)})});
        this.mTiltShiftMaskFadeOutAnimator.setTarget(EffectController.getInstance());
        this.mTiltShiftMaskFadeOutAnimator.addListener(this.mTiltShiftMaskFadeOutListener);
    }

    private void computeCertenLineCrossPoints(Point point, Point point2) {
        int i;
        int i2;
        int i3;
        if (squareOfPoints(point, point2) >= MIN_DIS_FOR_SLOPE) {
            int width = (int) this.mDisplayBounds.width();
            int height = (int) this.mDisplayBounds.height();
            int i4 = point.x;
            int i5 = point2.x;
            if (i4 == i5) {
                int clamp = Util.clamp(i4, 0, width);
                this.mPoint1.set(clamp, 0);
                this.mPoint2.set(clamp, height);
                return;
            }
            int i6 = point.y;
            int i7 = point2.y;
            if (i6 == i7) {
                int clamp2 = Util.clamp(i6, 0, height);
                this.mPoint1.set(0, clamp2);
                this.mPoint2.set(width, clamp2);
                return;
            }
            Point[] pointArr = new Point[2];
            float f = ((float) (i7 - i6)) / ((float) (i5 - i4));
            int i8 = (int) (((float) i4) - (((float) i6) / f));
            if (i8 < 0 || i8 > width) {
                i = 0;
            } else {
                pointArr[0] = new Point(i8, 0);
                i = 1;
            }
            int i9 = (int) (((float) point.x) + (((float) (height - point.y)) / f));
            if (i9 >= 0 && i9 <= width) {
                int i10 = i + 1;
                pointArr[i] = new Point(i9, height);
                i = i10;
            }
            int i11 = (int) (((float) point.y) - (((float) point.x) * f));
            if (i11 >= 0 && i11 <= height && !isContained(pointArr, 0, i11)) {
                int i12 = i + 1;
                pointArr[i] = new Point(0, i11);
                i = i12;
            }
            int i13 = (int) (((float) point.y) + (((float) (width - point.x)) * f));
            if (i13 < 0 || i13 > height || isContained(pointArr, width, i13)) {
                i2 = i;
            } else {
                i2 = i + 1;
                pointArr[i] = new Point(width, i13);
            }
            if (i2 == 1) {
                i3 = i2 + 1;
                pointArr[i2] = new Point(pointArr[0]);
            } else {
                i3 = i2;
            }
            if (i3 == 2) {
                int i14 = MIN_CROP_WIDTH_HEIGHT;
                if (i14 * i14 <= squareOfPoints(pointArr[0], pointArr[1])) {
                    this.mPoint1.set(pointArr[0].x, pointArr[0].y);
                    this.mPoint2.set(pointArr[1].x, pointArr[1].y);
                }
            }
        }
    }

    private Point computePointWithDistance(int i) {
        Point point = new Point();
        Point point2 = this.mPoint1;
        int i2 = point2.x;
        Point point3 = this.mPoint2;
        if (i2 == point3.x) {
            point.set(i2 - i, point2.y);
        } else {
            int i3 = point2.y;
            if (i3 == point3.y) {
                point.set(i2, i3 - i);
            } else {
                float sqrt = (float) Math.sqrt((double) this.mCenterLineSquare);
                Point point4 = this.mPoint1;
                int i4 = point4.y;
                Point point5 = this.mPoint2;
                int i5 = (int) (((float) ((i4 - point5.y) * i)) / sqrt);
                int i6 = point4.x;
                point.set(i6 + i5, i4 - ((int) (((float) ((i6 - point5.x) * i)) / sqrt)));
            }
        }
        return point;
    }

    private void detectMovingEdges(float f, float f2) {
        int i;
        int i2;
        int i3;
        boolean z = false;
        this.mMovingEdges = 0;
        if (this.mIsRect) {
            RectF rectF = this.mCropBounds;
            float f3 = rectF.bottom;
            int i4 = TOUCH_TOLERANCE;
            if (f2 <= f3 + ((float) i4) && rectF.top - ((float) i4) <= f2) {
                float abs = Math.abs(f - rectF.left);
                float abs2 = Math.abs(f - this.mCropBounds.right);
                if (abs <= ((float) TOUCH_TOLERANCE) && abs < abs2) {
                    i3 = this.mMovingEdges | 1;
                } else if (abs2 <= ((float) TOUCH_TOLERANCE)) {
                    i3 = this.mMovingEdges | 4;
                }
                this.mMovingEdges = i3;
            }
            RectF rectF2 = this.mCropBounds;
            float f4 = rectF2.right;
            int i5 = TOUCH_TOLERANCE;
            if (f <= f4 + ((float) i5) && rectF2.left - ((float) i5) <= f) {
                float abs3 = Math.abs(f2 - rectF2.top);
                float abs4 = Math.abs(f2 - this.mCropBounds.bottom);
                boolean z2 = abs3 <= ((float) TOUCH_TOLERANCE);
                if (abs3 < abs4) {
                    z = true;
                }
                if (z && z2) {
                    i2 = this.mMovingEdges | 2;
                } else if (abs4 <= ((float) TOUCH_TOLERANCE)) {
                    i2 = this.mMovingEdges | 8;
                }
                this.mMovingEdges = i2;
            }
            if (!this.mCropBounds.contains(f, f2) || this.mMovingEdges != 0) {
                return;
            }
        } else {
            boolean z3 = this.mIsCircle;
            showTiltShiftMask();
            if (z3) {
                float centerX = this.mCropBounds.centerX();
                float centerY = this.mCropBounds.centerY();
                float width = (this.mCropBounds.width() + this.mCropBounds.height()) / 4.0f;
                float f5 = width * width;
                int i6 = CIRCLE_RESIZE_TOUCH_TOLERANCE;
                float f6 = (((float) i6) + width) * (width + ((float) i6));
                float f7 = f - centerX;
                float f8 = f2 - centerY;
                float f9 = (f7 * f7) + (f8 * f8);
                if (f9 > f5 && f9 <= f6) {
                    this.mMovingEdges = 32;
                }
                if (!this.mCropBounds.contains(f, f2) || this.mMovingEdges != 0) {
                    return;
                }
            } else {
                Point point = new Point((int) f, (int) f2);
                Point point2 = this.mTouchCenter;
                Point point3 = this.mPoint1;
                int i7 = point3.x;
                Point point4 = this.mPoint2;
                point2.set((i7 + point4.x) / 2, (point3.y + point4.y) / 2);
                if (MIN_DIS_FOR_MOVE_POINT < ((float) this.mCenterLineSquare) && squareOfPoints(point, this.mPoint1) < this.mCenterLineSquare / 16) {
                    i = 257;
                } else if (MIN_DIS_FOR_MOVE_POINT >= ((float) this.mCenterLineSquare) || squareOfPoints(point, this.mPoint2) >= this.mCenterLineSquare / 16) {
                    float squareOfDistance = getSquareOfDistance(f, f2, new PointF(this.mPoint1), new PointF(this.mPoint2), false);
                    int i8 = this.mRangeWidth;
                    if (squareOfDistance >= ((float) ((i8 * i8) / 9))) {
                        this.mLastMoveDis = Math.sqrt((double) squareOfDistance);
                        i = 260;
                    }
                } else {
                    i = 258;
                }
                this.mMovingEdges = i;
                return;
            }
        }
        this.mMovingEdges = 16;
    }

    private float getSquareOfDistance(float f, float f2, PointF pointF, PointF pointF2, boolean z) {
        float f3 = pointF.x;
        float f4 = pointF.y;
        float f5 = pointF2.x;
        float f6 = pointF2.y;
        if (f3 == f5) {
            float f7 = f - f3;
            return f7 * f7;
        } else if (f4 == f6) {
            float f8 = f2 - f4;
            return f8 * f8;
        } else {
            float f9 = f5 - f3;
            float f10 = f - f3;
            float f11 = f6 - f4;
            float f12 = f2 - f4;
            float f13 = (f9 * f10) + (f11 * f12);
            if (z && ((double) f13) <= 0.0d) {
                return (f10 * f10) + (f12 * f12);
            }
            float f14 = (f9 * f9) + (f11 * f11);
            if (!z || f13 < f14) {
                float f15 = f13 / f14;
                float f16 = f - (f3 + (f9 * f15));
                float f17 = (f4 + (f11 * f15)) - f2;
                return (f16 * f16) + (f17 * f17);
            }
            float f18 = f - f5;
            float f19 = f2 - f6;
            return (f18 * f18) + (f19 * f19);
        }
    }

    /* access modifiers changed from: private */
    public void hideTiltShiftMask() {
        Handler handler = this.mTiltShiftMaskHandler;
        if (handler != null) {
            handler.sendEmptyMessage(2);
        }
    }

    private void initHandler() {
        if (this.mTiltShiftMaskHandler == null) {
            this.mTiltShiftMaskHandler = new Handler(Looper.getMainLooper()) {
                public void dispatchMessage(Message message) {
                    ObjectAnimator objectAnimator;
                    ObjectAnimator objectAnimator2;
                    long j;
                    int i = message.what;
                    if (i == 1) {
                        V6EffectCropView.this.mTiltShiftMaskFadeOutAnimator.cancel();
                        if (!V6EffectCropView.this.mTiltShiftMaskAlive) {
                            V6EffectCropView.this.mTiltShiftMaskAlive = true;
                            V6EffectCropView.this.mTiltShiftMaskFadeInAnimator.setupStartValues();
                            objectAnimator = V6EffectCropView.this.mTiltShiftMaskFadeInAnimator;
                        } else {
                            return;
                        }
                    } else if (i == 2) {
                        if (V6EffectCropView.this.mTiltShiftMaskFadeInAnimator.isRunning()) {
                            objectAnimator2 = V6EffectCropView.this.mTiltShiftMaskFadeOutAnimator;
                            j = V6EffectCropView.this.mTiltShiftMaskFadeInAnimator.getDuration() - V6EffectCropView.this.mTiltShiftMaskFadeInAnimator.getCurrentPlayTime();
                        } else {
                            objectAnimator2 = V6EffectCropView.this.mTiltShiftMaskFadeOutAnimator;
                            j = 0;
                        }
                        objectAnimator2.setStartDelay(j);
                        if (V6EffectCropView.this.mTiltShiftMaskAlive) {
                            objectAnimator = V6EffectCropView.this.mTiltShiftMaskFadeOutAnimator;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    objectAnimator.start();
                }
            };
        }
        if (this.mAnimateHandler == null) {
            this.mAnimateThread = new HandlerThread("animateThread");
            this.mAnimateThread.start();
            this.mAnimateHandler = new Handler(this.mAnimateThread.getLooper()) {
                public void dispatchMessage(Message message) {
                    long currentTimeMillis = System.currentTimeMillis() - V6EffectCropView.this.mAnimationStartTime;
                    int i = message.what;
                    float f = 1.0f;
                    if (i == 1) {
                        if (currentTimeMillis < 600) {
                            f = V6EffectCropView.this.mInterpolator.getInterpolation(((float) currentTimeMillis) / ((float) V6EffectCropView.this.mAnimationTotalTime));
                            sendEmptyMessageDelayed(1, 30);
                        } else {
                            V6EffectCropView.this.hideTiltShiftMask();
                        }
                        V6EffectCropView v6EffectCropView = V6EffectCropView.this;
                        v6EffectCropView.mRangeWidth = v6EffectCropView.mAnimationStartRange + ((int) (((float) V6EffectCropView.this.mAnimateRangeWidth) * f));
                    } else if (i == 2) {
                        if (currentTimeMillis < 600) {
                            f = V6EffectCropView.this.mInterpolator.getInterpolation(((float) currentTimeMillis) / ((float) V6EffectCropView.this.mAnimationTotalTime));
                            sendEmptyMessageDelayed(2, 30);
                        } else {
                            V6EffectCropView.this.hideTiltShiftMask();
                        }
                        float centerX = V6EffectCropView.this.mDefaultCircleBounds.centerX();
                        float centerY = V6EffectCropView.this.mDefaultCircleBounds.centerY();
                        V6EffectCropView v6EffectCropView2 = V6EffectCropView.this;
                        v6EffectCropView2.mRadius = v6EffectCropView2.mAnimationStartRadius + ((int) (((float) V6EffectCropView.this.mAnimateRadius) * f));
                        V6EffectCropView.this.mCropBounds.set(centerX - ((float) V6EffectCropView.this.mRadius), centerY - ((float) V6EffectCropView.this.mRadius), centerX + ((float) V6EffectCropView.this.mRadius), centerY + ((float) V6EffectCropView.this.mRadius));
                    } else {
                        return;
                    }
                    V6EffectCropView.this.onCropChange();
                }
            };
        }
    }

    private void innerShow() {
        int i = 1;
        boolean z = !isTiltShift();
        boolean isCircle = isCircle();
        if (!this.mVisible || this.mIsRect != z || this.mIsCircle != isCircle) {
            this.mVisible = true;
            this.mMovingEdges = 0;
            setVisibility(0);
            this.mIsRect = z;
            this.mIsCircle = isCircle;
            if (isTiltShift()) {
                this.mPoint1.set(0, ((int) this.mDisplayBounds.height()) / 2);
                this.mPoint2.set((int) this.mDisplayBounds.width(), ((int) this.mDisplayBounds.height()) / 2);
                this.mRangeWidth = ANIMATE_START_RANGE;
                this.mRadius = ANIMATE_START_RADIUS;
                this.mAnimationStartTime = System.currentTimeMillis();
                this.mAnimationTotalTime = 600;
                int i2 = DEFAULT_RANGE;
                int i3 = this.mRangeWidth;
                this.mAnimateRangeWidth = i2 - i3;
                this.mAnimationStartRange = i3;
                int i4 = DEFAULT_RADIUS;
                int i5 = this.mRadius;
                this.mAnimateRadius = i4 - i5;
                this.mAnimationStartRadius = i5;
                float centerX = this.mDefaultCircleBounds.centerX();
                float centerY = this.mDefaultCircleBounds.centerY();
                RectF rectF = this.mCropBounds;
                int i6 = this.mRadius;
                rectF.set(centerX - ((float) i6), centerY - ((float) i6), centerX + ((float) i6), centerY + ((float) i6));
                showTiltShiftMask();
                Handler handler = this.mAnimateHandler;
                if (handler != null) {
                    if (isCircle) {
                        i = 2;
                    }
                    handler.sendEmptyMessage(i);
                }
                invalidate();
            } else {
                this.mCropBounds.set(this.mDefaultRectBounds);
                setLayerType(2, null);
            }
            EffectController.getInstance().setInvertFlag(0);
            onCropChange();
        }
    }

    private static boolean isCircle() {
        if (CameraSettings.isTiltShiftOn()) {
            return DataRepository.dataItemRunning().getComponentRunningTiltValue().getComponentValue(160).equals(ComponentRunningTiltValue.TILT_CIRCLE);
        }
        return false;
    }

    private boolean isContained(Point[] pointArr, int i, int i2) {
        boolean z = false;
        if (pointArr != null && pointArr.length != 0) {
            int length = pointArr.length;
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    break;
                }
                Point point = pointArr[i3];
                if (point == null) {
                    return false;
                }
                if (point.x == i || point.y == i2) {
                    z = true;
                } else {
                    i3++;
                }
            }
        }
        return z;
    }

    private static boolean isTiltShift() {
        return CameraSettings.isTiltShiftOn();
    }

    private void moveCircle(float f, float f2, float f3, float f4) {
        if (this.mMovingEdges == 16) {
            this.mCropBounds.offset(f3 > 0.0f ? Math.min(this.mDisplayBounds.right - this.mCropBounds.right, f3) : Math.max(this.mDisplayBounds.left - this.mCropBounds.left, f3), f4 > 0.0f ? Math.min(this.mDisplayBounds.bottom - this.mCropBounds.bottom, f4) : Math.max(this.mDisplayBounds.top - this.mCropBounds.top, f4));
        } else {
            float f5 = (float) (MIN_CROP_WIDTH_HEIGHT / 2);
            float min = Math.min(this.mDisplayBounds.width(), this.mDisplayBounds.height()) / 2.0f;
            float centerX = this.mCropBounds.centerX();
            float centerY = this.mCropBounds.centerY();
            float f6 = f - centerX;
            float f7 = f2 - centerY;
            float min2 = Math.min(min, Math.max(f5, (float) Math.sqrt((double) ((f6 * f6) + (f7 * f7)))));
            this.mCropBounds.set(centerX - min2, centerY - min2, centerX + min2, centerY + min2);
        }
        onCropChange();
    }

    private void moveCrop(float f, float f2, float f3, float f4) {
        int i = this.mMovingEdges;
        if (i == 260) {
            double sqrt = Math.sqrt((double) getSquareOfDistance(f, f2, new PointF(this.mPoint1), new PointF(this.mPoint2), false));
            this.mRangeWidth = Util.clamp(this.mRangeWidth + ((int) (sqrt - this.mLastMoveDis)), MIN_RANGE, this.mMaxRange);
            this.mLastMoveDis = sqrt;
        } else if (i == 257 || i == 258) {
            computeCertenLineCrossPoints(this.mTouchCenter, new Point((int) f, (int) f2));
        } else if (i == 16) {
            Point point = this.mPoint1;
            int i2 = (int) f3;
            int i3 = (int) f4;
            Point point2 = new Point(point.x + i2, point.y + i3);
            Point point3 = this.mPoint2;
            computeCertenLineCrossPoints(point2, new Point(point3.x + i2, point3.y + i3));
        }
        onCropChange();
    }

    private void moveEdges(float f, float f2) {
        int i = this.mMovingEdges;
        if (i == 16) {
            this.mCropBounds.offset(f > 0.0f ? Math.min(this.mDisplayBounds.right - this.mCropBounds.right, f) : Math.max(this.mDisplayBounds.left - this.mCropBounds.left, f), f2 > 0.0f ? Math.min(this.mDisplayBounds.bottom - this.mCropBounds.bottom, f2) : Math.max(this.mDisplayBounds.top - this.mCropBounds.top, f2));
        } else {
            int i2 = MIN_CROP_WIDTH_HEIGHT;
            float f3 = (float) i2;
            float f4 = (float) i2;
            if ((i & 1) != 0) {
                RectF rectF = this.mCropBounds;
                rectF.left = Math.min(rectF.left + f, rectF.right - f3);
            }
            if ((this.mMovingEdges & 2) != 0) {
                RectF rectF2 = this.mCropBounds;
                rectF2.top = Math.min(rectF2.top + f2, rectF2.bottom - f4);
            }
            if ((this.mMovingEdges & 4) != 0) {
                RectF rectF3 = this.mCropBounds;
                rectF3.right = Math.max(rectF3.right + f, rectF3.left + f3);
            }
            if ((this.mMovingEdges & 8) != 0) {
                RectF rectF4 = this.mCropBounds;
                rectF4.bottom = Math.max(rectF4.bottom + f2, rectF4.top + f4);
            }
            this.mCropBounds.intersect(this.mDisplayBounds);
        }
        onCropChange();
    }

    private void normalizeRangeWidth() {
        Point computePointWithDistance = computePointWithDistance(this.mRangeWidth);
        this.mNormalizedWidth = (float) Math.sqrt((double) getSquareOfDistance(((float) computePointWithDistance.x) / this.mDisplayBounds.width(), ((float) computePointWithDistance.y) / this.mDisplayBounds.height(), this.mEffectPoint1, this.mEffectPoint2, false));
    }

    /* access modifiers changed from: private */
    public void onCropChange() {
        float width = this.mDisplayBounds.width();
        float height = this.mDisplayBounds.height();
        RectF rectF = this.mEffectRect;
        RectF rectF2 = this.mCropBounds;
        rectF.set(rectF2.left / width, rectF2.top / height, rectF2.right / width, rectF2.bottom / height);
        PointF pointF = this.mEffectPoint1;
        Point point = this.mPoint1;
        pointF.set(((float) point.x) / width, ((float) point.y) / height);
        PointF pointF2 = this.mEffectPoint2;
        Point point2 = this.mPoint2;
        pointF2.set(((float) point2.x) / width, ((float) point2.y) / height);
        this.mCenterLineSquare = squareOfPoints(this.mPoint1, this.mPoint2);
        normalizeRangeWidth();
        EffectController.getInstance().setEffectAttribute(this.mEffectRect, this.mEffectPoint1, this.mEffectPoint2, this.mNormalizedWidth);
        if (this.mIsRect) {
            invalidate();
        }
    }

    private void showTiltShiftMask() {
        Handler handler = this.mTiltShiftMaskHandler;
        if (handler != null) {
            handler.sendEmptyMessage(1);
        }
    }

    private int squareOfPoints(Point point, Point point2) {
        int i = point.x - point2.x;
        int i2 = point.y - point2.y;
        return (i * i) + (i2 * i2);
    }

    public void enableControls(boolean z) {
    }

    public void hide() {
        if (this.mVisible) {
            this.mVisible = false;
            setVisibility(4);
            EffectController.getInstance().clearEffectAttribute();
            EffectController.getInstance().setInvertFlag(0);
        }
    }

    public boolean isMoved() {
        return !this.mIsInTapSlop && this.mMovingEdges != 0;
    }

    public boolean isVisible() {
        return this.mVisible;
    }

    public void onCameraOpen() {
    }

    public void onCreate() {
        initHandler();
    }

    public void onDestroy() {
        HandlerThread handlerThread = this.mAnimateThread;
        if (handlerThread != null) {
            handlerThread.quit();
            this.mAnimateThread = null;
            this.mAnimateHandler = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mVisible && this.mIsRect) {
            canvas.drawRect(this.mCropBounds, this.mBorderPaint);
            RectF rectF = this.mCropBounds;
            canvas.drawCircle(rectF.left, rectF.top, (float) CORNER_BALL_RADIUS, this.mCornerPaint);
            RectF rectF2 = this.mCropBounds;
            canvas.drawCircle(rectF2.right, rectF2.top, (float) CORNER_BALL_RADIUS, this.mCornerPaint);
            RectF rectF3 = this.mCropBounds;
            canvas.drawCircle(rectF3.left, rectF3.bottom, (float) CORNER_BALL_RADIUS, this.mCornerPaint);
            RectF rectF4 = this.mCropBounds;
            canvas.drawCircle(rectF4.right, rectF4.bottom, (float) CORNER_BALL_RADIUS, this.mCornerPaint);
        }
    }

    public void onPause() {
        Handler handler = this.mAnimateHandler;
        if (handler != null && handler.hasMessages(1)) {
            if (this.mAnimateHandler.hasMessages(1)) {
                this.mAnimateHandler.removeMessages(1);
                this.mRangeWidth = this.mAnimationStartRange + this.mAnimateRangeWidth;
            }
            if (this.mAnimateHandler.hasMessages(2)) {
                this.mAnimateHandler.removeMessages(2);
                this.mRadius = this.mAnimationStartRadius + this.mAnimateRadius;
            }
        }
    }

    public void onResume() {
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        float f = (float) i;
        float f2 = (float) i2;
        this.mDisplayBounds.set(0.0f, 0.0f, f, f2);
        this.mDefaultRectBounds.set((float) ((i * 3) / 8), (float) ((i2 * 3) / 8), (float) ((i * 5) / 8), (float) ((i2 * 5) / 8));
        float f3 = (float) DEFAULT_RADIUS;
        float f4 = f / 2.0f;
        float f5 = f2 / 2.0f;
        this.mDefaultCircleBounds.set(f4 - f3, f5 - f3, f4 + f3, f5 + f3);
        this.mCropBounds.set(this.mIsRect ? this.mDefaultRectBounds : this.mDefaultCircleBounds);
        int i5 = i2 / 2;
        this.mPoint1.set(0, i5);
        this.mPoint2.set(i, i5);
        this.mMaxRange = (i2 * 2) / 3;
        this.mRangeWidth = this.mVisible ? DEFAULT_RANGE : ANIMATE_START_RANGE;
        onCropChange();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0026, code lost:
        if (r7 != 5) goto L_0x0087;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onViewTouchEvent(MotionEvent motionEvent) {
        if (!this.mVisible) {
            return false;
        }
        if (isEnabled()) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int action = motionEvent.getAction() & 255;
            if (action != 0) {
                if (action != 1) {
                    if (action == 2) {
                        float f = x - this.mLastX;
                        float f2 = y - this.mLastY;
                        if (this.mIsInTapSlop && ((float) this.mTapSlop) < (f * f) + (f2 * f2)) {
                            this.mIsInTapSlop = false;
                        }
                        if (!this.mIsInTapSlop) {
                            if (this.mMovingEdges != 0) {
                                if (this.mIsRect) {
                                    moveEdges(x - this.mLastX, y - this.mLastY);
                                } else if (this.mIsCircle) {
                                    moveCircle(x, y, x - this.mLastX, y - this.mLastY);
                                } else {
                                    moveCrop(x, y, x - this.mLastX, y - this.mLastY);
                                }
                            }
                        }
                    } else if (action != 3) {
                    }
                }
                this.mMovingEdges = 0;
                hideTiltShiftMask();
                invalidate();
            } else {
                detectMovingEdges(x, y);
                this.mIsInTapSlop = true;
            }
            this.mLastX = x;
            this.mLastY = y;
        }
        return true;
    }

    public void removeTiltShiftMask() {
        Handler handler = this.mTiltShiftMaskHandler;
        if (handler != null) {
            handler.removeMessages(1);
            this.mTiltShiftMaskHandler.removeMessages(2);
        }
    }

    public void show() {
        if (EffectController.getInstance().isNeedRect(EffectController.getInstance().getEffectForPreview(false)) || isTiltShift()) {
            innerShow();
        }
    }

    public void show(float f, float f2) {
        if (!this.mVisible) {
            this.mVisible = true;
            setVisibility(0);
            RectF rectF = this.mIsRect ? this.mDefaultRectBounds : this.mDefaultCircleBounds;
            this.mCropBounds.set(f - (rectF.width() / 2.0f), f2 - (rectF.height() / 2.0f), f + (rectF.width() / 2.0f), f2 + (rectF.height() / 2.0f));
            onCropChange();
        }
    }

    public void updateVisible() {
        if (EffectController.getInstance().isNeedRect(EffectController.getInstance().getEffectForPreview(false)) || isTiltShift()) {
            innerShow();
        } else {
            hide();
        }
    }

    public void updateVisible(int i) {
        if (EffectController.getInstance().isNeedRect(i)) {
            innerShow();
        } else {
            hide();
        }
    }
}
