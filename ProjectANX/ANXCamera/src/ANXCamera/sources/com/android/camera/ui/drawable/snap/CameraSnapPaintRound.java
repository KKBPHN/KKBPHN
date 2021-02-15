package com.android.camera.ui.drawable.snap;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import com.android.camera.ui.drawable.CameraPaintBase;

public class CameraSnapPaintRound extends CameraPaintBase {
    private boolean isDown;
    public boolean isRecordingCircle;
    public boolean isRoundingCircle;
    private float mBaseRoundRectRadius;
    private float mCurrentRoundRectRadius = 0.65f;
    private PaintPattern mExternalPattern;
    private boolean mRapidly;
    private Paint mRecordingBgPaint;
    private RectF mRectF;
    private float mRoundingProgress = 1.0f;
    private boolean mShowCenterBitmap;
    private float mSrcRoundRectRadius = 0.65f;
    private float mSrcRoundingProgress = 1.0f;
    private float mTargetRoundRectRadius = 0.65f;
    private float mTargetRoundWidthPercent = 0.32f;
    private float mTargetRoundingProgress = 1.0f;

    public CameraSnapPaintRound(Context context) {
        super(context);
    }

    public void clearBitmap() {
        PaintPattern paintPattern = this.mExternalPattern;
        if (paintPattern != null) {
            paintPattern.recycle();
            this.mExternalPattern = null;
        }
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        if (!this.isRecording) {
            canvas.drawCircle(this.mMiddleX, this.mMiddleY, this.mBaseRadius * this.mCurrentWidthPercent, this.mPaint);
            PaintPattern paintPattern = this.mExternalPattern;
            if (paintPattern != null && this.mShowCenterBitmap) {
                paintPattern.draw(canvas);
                return;
            }
            return;
        }
        if (this.mRecordingBgPaint != null) {
            float f5 = this.mBaseRadius * this.mBaseWidthPercent;
            float f6 = this.mMiddleX;
            float f7 = f6 - f5;
            float f8 = f6 + f5;
            float f9 = this.mMiddleY;
            this.mRectF.set(f7, f9 - f5, f8, f9 + f5);
            RectF rectF = this.mRectF;
            float f10 = this.mBaseRadius;
            canvas.drawRoundRect(rectF, f10, f10, this.mRecordingBgPaint);
        }
        if (this.isRecordingCircle) {
            f = this.mMiddleX;
            f2 = this.mMiddleY;
            f3 = this.mBaseRadius;
            f4 = 0.55f;
        } else if (this.isRoundingCircle) {
            f = this.mMiddleX;
            f2 = this.mMiddleY;
            f3 = this.mBaseRadius * this.mCurrentWidthPercent;
            f4 = this.mRoundingProgress;
        } else {
            float f11 = this.mBaseRadius * this.mCurrentRoundRectRadius;
            float f12 = this.mMiddleX;
            float f13 = f12 - f11;
            float f14 = f12 + f11;
            float f15 = this.mMiddleY;
            this.mRectF.set(f13, f15 - f11, f14, f15 + f11);
            RectF rectF2 = this.mRectF;
            float f16 = this.mRoundingProgress;
            canvas.drawRoundRect(rectF2, f11 * f16, f11 * f16, this.mPaint);
            return;
        }
        canvas.drawCircle(f, f2, f3 * f4, this.mPaint);
    }

    /* access modifiers changed from: protected */
    public void initPaint(Context context) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
        this.mRectF = new RectF();
    }

    public void prepareRecord(boolean z, boolean z2, float f) {
        float f2;
        this.mRapidly = z;
        this.isDown = z2;
        if (f <= 0.0f) {
            f = this.mCurrentWidthPercent;
        }
        if (z2) {
            this.mSrcRoundRectRadius = this.mRapidly ? this.mCurrentWidthPercent * this.mTargetRoundWidthPercent * 1.2f : this.mCurrentRoundRectRadius;
            this.mTargetRoundRectRadius = f * this.mTargetRoundWidthPercent;
        } else {
            this.mSrcRoundRectRadius = this.mCurrentRoundRectRadius;
            this.mTargetRoundRectRadius = this.mRapidly ? this.mCurrentWidthPercent * this.mTargetRoundWidthPercent * 0.8f : this.mCurrentWidthPercent;
        }
        if (z2) {
            this.mSrcRoundingProgress = this.mRapidly ? 0.42000002f : this.mRoundingProgress;
            f2 = 0.35f;
        } else {
            f2 = this.mRoundingProgress;
            this.mSrcRoundingProgress = f2;
            if (!this.mRapidly) {
                f2 = 1.0f;
            }
        }
        this.mTargetRoundingProgress = f2;
    }

    public void resetRecordingState() {
        super.resetRecordingState();
        this.isRecording = false;
        this.isRecordingCircle = false;
        this.isRoundingCircle = false;
        this.mRapidly = false;
    }

    public void setBitmapPatternTargetScale(float f) {
        PaintPattern paintPattern = this.mExternalPattern;
        if (paintPattern != null) {
            ((CameraSnapPaintRoundPatternBitmap) paintPattern).setTargetScale(f);
        }
    }

    public void setRapidly(boolean z) {
        this.mRapidly = z;
    }

    public void setRecordingBgColor(@ColorInt int i) {
        if (i == 0) {
            this.mRecordingBgPaint = null;
            return;
        }
        if (this.mRecordingBgPaint == null) {
            this.mRecordingBgPaint = new Paint();
            this.mRecordingBgPaint.setAntiAlias(true);
            this.mRecordingBgPaint.setStyle(Style.FILL);
        }
        this.mRecordingBgPaint.setColor(i);
    }

    public void setShowCenterBitmap(boolean z) {
        this.mShowCenterBitmap = z;
    }

    public CameraPaintBase setTargetWidthPercent(float f) {
        float f2;
        float f3;
        super.setTargetWidthPercent(f);
        if (this.isDown) {
            this.mSrcRoundRectRadius = this.mCurrentRoundRectRadius;
            f2 = this.mDstWidthPercent * 0.28f;
        } else {
            this.mSrcRoundRectRadius = this.mCurrentRoundRectRadius;
            f2 = this.mDstWidthPercent;
        }
        this.mTargetRoundRectRadius = f2;
        if (this.isDown) {
            this.mSrcRoundingProgress = this.mRoundingProgress;
            f3 = 0.35f;
        } else {
            this.mSrcRoundingProgress = this.mRoundingProgress;
            f3 = 1.0f;
        }
        this.mTargetRoundingProgress = f3;
        return this;
    }

    public void showTargetBitmap(Context context, @DrawableRes int i) {
        this.mShowCenterBitmap = true;
        this.mExternalPattern = new CameraSnapPaintRoundPatternBitmap(this, BitmapFactory.decodeResource(context.getResources(), i));
    }

    public void updateValue(float f) {
        super.updateValue(f);
        PaintPattern paintPattern = this.mExternalPattern;
        if (paintPattern != null) {
            paintPattern.updateValue(f);
        }
        this.mRoundingProgress = calculateCurrentValue(this.mSrcRoundingProgress, this.mTargetRoundingProgress, f);
        this.mCurrentRoundRectRadius = calculateCurrentValue(this.mSrcRoundRectRadius, this.mTargetRoundRectRadius, f);
    }
}
