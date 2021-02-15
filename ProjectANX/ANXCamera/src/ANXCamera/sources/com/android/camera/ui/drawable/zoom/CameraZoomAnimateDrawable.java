package com.android.camera.ui.drawable.zoom;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Spline;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.Util;
import com.android.camera.ui.ZoomView.zoomValueChangeListener;
import miui.view.animation.CubicEaseOutInterpolator;

public class CameraZoomAnimateDrawable extends Drawable implements Animatable {
    private static final float[] VIDEO_SAT_ZOOM_RATIO_DELTA_X = {0.6f, 1.0f, 3.0f, 6.0f, 9.0f, 10.0f, 12.0f, 14.0f, 15.0f};
    private static final float[] VIDEO_SAT_ZOOM_RATIO_DELTA_Y = {0.2f, 0.3f, 0.6f, 1.1f, 1.8f, 1.9f, 2.0f, 2.5f, 4.0f};
    /* access modifiers changed from: private */
    public CameraZoomAdjustPaint mCameraZoomAdjustPaint;
    private Context mContext;
    private int mDegree = 0;
    private boolean mIsHorizontal;
    /* access modifiers changed from: private */
    public float mMaxZoomRatio;
    /* access modifiers changed from: private */
    public float mMinZoomRatio;
    /* access modifiers changed from: private */
    public float mSpeed = 0.0f;
    private boolean mSupportVideoSat;
    private ValueAnimator mTouchDownAnimator;
    private ValueAnimator mTouchDownScaleAnimator;
    private ValueAnimator mTouchUpAnimator;
    /* access modifiers changed from: private */
    public Spline mVideoSATZoomSpline;
    /* access modifiers changed from: private */
    public float mZoomRatio = 1.0f;

    public CameraZoomAnimateDrawable(Context context, boolean z) {
        this.mContext = context;
        this.mIsHorizontal = z;
        this.mCameraZoomAdjustPaint = new CameraZoomAdjustPaint(context, z);
        this.mVideoSATZoomSpline = Spline.createMonotoneCubicSpline(VIDEO_SAT_ZOOM_RATIO_DELTA_X, VIDEO_SAT_ZOOM_RATIO_DELTA_Y);
    }

    static /* synthetic */ float access$216(CameraZoomAnimateDrawable cameraZoomAnimateDrawable, float f) {
        float f2 = cameraZoomAnimateDrawable.mZoomRatio + f;
        cameraZoomAnimateDrawable.mZoomRatio = f2;
        return f2;
    }

    private boolean isAnimatorRunning(Animator animator) {
        return animator != null && animator.isRunning();
    }

    public boolean containsSliderTouchRect(Point point) {
        return this.mCameraZoomAdjustPaint.getSliderTouchRect().contains(point.x, point.y);
    }

    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        this.mCameraZoomAdjustPaint.drawCanvas(canvas);
        canvas.restore();
    }

    public float getCurrentZoomRatio() {
        return this.mZoomRatio;
    }

    public int getOpacity() {
        return -1;
    }

    public boolean isRunning() {
        ValueAnimator valueAnimator = this.mTouchDownAnimator;
        return valueAnimator != null && valueAnimator.isRunning();
    }

    public void move(Point point) {
        if (isAnimatorRunning(this.mTouchDownAnimator)) {
            this.mSpeed = this.mCameraZoomAdjustPaint.getSpeed(point.x);
            this.mCameraZoomAdjustPaint.move(point);
            invalidateSelf();
        }
    }

    public void reset() {
        this.mSpeed = 0.0f;
        this.mZoomRatio = 1.0f;
        if (isAnimatorRunning(this.mTouchDownAnimator)) {
            this.mTouchDownAnimator.cancel();
            this.mTouchDownAnimator = null;
        }
        if (isAnimatorRunning(this.mTouchDownScaleAnimator)) {
            this.mTouchDownScaleAnimator.cancel();
            this.mTouchDownScaleAnimator = null;
        }
        if (isAnimatorRunning(this.mTouchUpAnimator)) {
            this.mTouchUpAnimator.cancel();
            this.mTouchUpAnimator = null;
        }
        CameraZoomAdjustPaint cameraZoomAdjustPaint = this.mCameraZoomAdjustPaint;
        if (cameraZoomAdjustPaint != null) {
            cameraZoomAdjustPaint.reset();
        }
    }

    public void setAlpha(@IntRange(from = 0, to = 255) int i) {
    }

    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    public void setCurrentZoomRatio(float f) {
        this.mZoomRatio = f;
        this.mCameraZoomAdjustPaint.setCurrentZoomRatio(f);
    }

    public void setOrientation(int i) {
        if (i != this.mDegree) {
            this.mCameraZoomAdjustPaint.setOrientation(i);
            this.mDegree = i;
            invalidateSelf();
        }
    }

    public void setSmoothSpeed(float f) {
        this.mSpeed = f;
    }

    public void setSupportVideoSat(boolean z) {
        this.mSupportVideoSat = z;
    }

    public void start() {
    }

    public boolean startTouchDownAnimation(final zoomValueChangeListener zoomvaluechangelistener, boolean z) {
        if (z) {
            if (isAnimatorRunning(this.mTouchDownScaleAnimator)) {
                this.mTouchDownScaleAnimator.cancel();
            }
            this.mTouchDownScaleAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            this.mTouchDownScaleAnimator.setInterpolator(new CubicEaseOutInterpolator());
            this.mTouchDownScaleAnimator.setDuration(200);
            this.mTouchDownScaleAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    CameraZoomAnimateDrawable.this.mCameraZoomAdjustPaint.updateSliderTouchScaleAnimation(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    CameraZoomAnimateDrawable.this.invalidateSelf();
                }
            });
            this.mTouchDownScaleAnimator.start();
        }
        if (isAnimatorRunning(this.mTouchDownAnimator)) {
            this.mTouchDownAnimator.cancel();
            this.mTouchDownAnimator = null;
        }
        this.mTouchDownAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mTouchDownAnimator.setRepeatCount(-1);
        this.mTouchDownAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CameraZoomAnimateDrawable.access$216(CameraZoomAnimateDrawable.this, CameraZoomAnimateDrawable.this.mSpeed * CameraZoomAnimateDrawable.this.mVideoSATZoomSpline.interpolate(CameraZoomAnimateDrawable.this.mZoomRatio));
                CameraZoomAnimateDrawable cameraZoomAnimateDrawable = CameraZoomAnimateDrawable.this;
                cameraZoomAnimateDrawable.mZoomRatio = Util.clamp(cameraZoomAnimateDrawable.mZoomRatio, CameraZoomAnimateDrawable.this.mMinZoomRatio, CameraZoomAnimateDrawable.this.mMaxZoomRatio);
                zoomValueChangeListener zoomvaluechangelistener = zoomvaluechangelistener;
                if (zoomvaluechangelistener != null) {
                    zoomvaluechangelistener.onZoomValueChanged(CameraZoomAnimateDrawable.this.mZoomRatio);
                }
                CameraZoomAnimateDrawable.this.invalidateSelf();
            }
        });
        this.mTouchDownAnimator.overrideDurationScale(1.0f);
        this.mTouchDownAnimator.start();
        return true;
    }

    public void stop() {
    }

    public void stopTouchUpAnimation(final boolean z) {
        if (isAnimatorRunning(this.mTouchDownAnimator)) {
            this.mTouchDownAnimator.cancel();
            this.mTouchDownAnimator = null;
        }
        if (isAnimatorRunning(this.mTouchDownScaleAnimator)) {
            this.mTouchDownScaleAnimator.cancel();
        }
        if (isAnimatorRunning(this.mTouchUpAnimator)) {
            this.mTouchUpAnimator.cancel();
        }
        this.mCameraZoomAdjustPaint.startSliderBackAnimation();
        this.mTouchUpAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mTouchUpAnimator.setInterpolator(new CubicEaseOutInterpolator());
        this.mTouchUpAnimator.setDuration(400);
        this.mTouchUpAnimator.setInterpolator(new CubicEaseOutInterpolator() {
            public float getInterpolation(float f) {
                float interpolation = super.getInterpolation(f);
                CameraZoomAnimateDrawable.this.mCameraZoomAdjustPaint.updateSliderPositionForBackAnim(interpolation, z);
                CameraZoomAnimateDrawable.this.invalidateSelf();
                return interpolation;
            }
        });
        this.mSpeed = 0.0f;
        this.mTouchUpAnimator.start();
    }

    public void updateZoomRatio(float f, float f2) {
        this.mMinZoomRatio = f;
        this.mMaxZoomRatio = f2;
        this.mCameraZoomAdjustPaint.setZoomRange(this.mMinZoomRatio, this.mMaxZoomRatio);
    }
}
