package com.android.camera.ui.drawable.focus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import com.android.camera.ui.drawable.CameraPaintBase;

public class CameraFocusPaintCenterIndicator extends CameraPaintBase {
    public CameraIndicatorState indicatorState;
    private Bitmap mAEAFLockBodyBitmap;
    private Bitmap mAEAFLockHeadBitmap;
    private Bitmap mCaptureIndicatorBitmap;
    private Matrix mCaptureIndicatorBitmapMatrix;
    public int mCenterFlagForAnimator;
    private Matrix mLockMatrix;
    private Paint mMinusMoonPaint;
    private Paint mSunPaint;

    public CameraFocusPaintCenterIndicator(Context context) {
        super(context);
    }

    private void drawCenterCaptureBitmap(Canvas canvas) {
        Matrix matrix = this.mCaptureIndicatorBitmapMatrix;
        if (matrix == null) {
            this.mCaptureIndicatorBitmapMatrix = new Matrix();
        } else {
            matrix.reset();
        }
        if (!this.mCaptureIndicatorBitmap.isRecycled()) {
            this.mCaptureIndicatorBitmapMatrix.postTranslate((float) ((-this.mCaptureIndicatorBitmap.getWidth()) / 2), (float) ((-this.mCaptureIndicatorBitmap.getHeight()) / 2));
            canvas.drawBitmap(this.mCaptureIndicatorBitmap, this.mCaptureIndicatorBitmapMatrix, this.mPaint);
        }
    }

    private void drawCenterCircle(Canvas canvas, float f, float f2, float f3, @NonNull Paint paint) {
        canvas.drawCircle(f, f2, f3, paint);
    }

    private void drawCenterLock(Canvas canvas) {
        if (this.mAEAFLockHeadBitmap != null && this.mAEAFLockBodyBitmap != null) {
            Matrix matrix = this.mLockMatrix;
            if (matrix == null) {
                this.mLockMatrix = new Matrix();
            } else {
                matrix.reset();
            }
            if (!this.mAEAFLockHeadBitmap.isRecycled() && !this.mAEAFLockBodyBitmap.isRecycled()) {
                this.mLockMatrix.postTranslate((float) ((-this.mAEAFLockHeadBitmap.getWidth()) / 2), ((float) ((-this.mAEAFLockHeadBitmap.getHeight()) / 2)) + this.indicatorState.mAEAFHeadPosition);
                canvas.drawBitmap(this.mAEAFLockHeadBitmap, this.mLockMatrix, this.mPaint);
                this.mLockMatrix.reset();
                this.mLockMatrix.postTranslate((float) ((-this.mAEAFLockBodyBitmap.getWidth()) / 2), (float) ((-this.mAEAFLockBodyBitmap.getHeight()) / 2));
                canvas.drawBitmap(this.mAEAFLockBodyBitmap, this.mLockMatrix, this.mPaint);
            }
        }
    }

    private void drawCenterMoon(Canvas canvas) {
        drawCenterCircle(canvas, 0.0f, 0.0f, (float) this.indicatorState.mCurrentRadius, this.mSunPaint);
        CameraIndicatorState cameraIndicatorState = this.indicatorState;
        int i = cameraIndicatorState.mCurrentMinusCircleCenter;
        drawCenterCircle(canvas, (float) (-i), (float) (-i), cameraIndicatorState.mCurrentMinusCircleRadius, this.mMinusMoonPaint);
    }

    private void drawCenterSun(Canvas canvas) {
        canvas.rotate(this.indicatorState.mCurrentAngle);
        for (int i = 0; i < 2; i++) {
            if (i > 0) {
                canvas.rotate(45.0f);
            }
            CameraIndicatorState cameraIndicatorState = this.indicatorState;
            int i2 = cameraIndicatorState.mCurrentRayWidth;
            float f = (float) ((-i2) / 2);
            int i3 = cameraIndicatorState.mCurrentRayBottom;
            Canvas canvas2 = canvas;
            canvas2.drawRect(f, (float) ((-i3) - cameraIndicatorState.mCurrentRayHeight), (float) (i2 / 2), (float) (-i3), this.mSunPaint);
            CameraIndicatorState cameraIndicatorState2 = this.indicatorState;
            int i4 = cameraIndicatorState2.mCurrentRayWidth;
            float f2 = (float) ((-i4) / 2);
            int i5 = cameraIndicatorState2.mCurrentRayBottom;
            canvas2.drawRect(f2, (float) i5, (float) (i4 / 2), (float) (i5 + cameraIndicatorState2.mCurrentRayHeight), this.mSunPaint);
            CameraIndicatorState cameraIndicatorState3 = this.indicatorState;
            int i6 = cameraIndicatorState3.mCurrentRayBottom;
            float f3 = (float) ((-i6) - cameraIndicatorState3.mCurrentRayHeight);
            int i7 = cameraIndicatorState3.mCurrentRayWidth;
            canvas.drawRect(f3, (float) ((-i7) / 2), (float) (-i6), (float) (i7 / 2), this.mSunPaint);
            CameraIndicatorState cameraIndicatorState4 = this.indicatorState;
            int i8 = cameraIndicatorState4.mCurrentRayBottom;
            float f4 = (float) i8;
            int i9 = cameraIndicatorState4.mCurrentRayWidth;
            canvas.drawRect(f4, (float) ((-i9) / 2), (float) (i8 + cameraIndicatorState4.mCurrentRayHeight), (float) (i9 / 2), this.mSunPaint);
        }
        canvas.drawCircle(0.0f, 0.0f, (float) this.indicatorState.mCurrentRadius, this.mSunPaint);
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        canvas.translate(this.mMiddleX, this.mMiddleY);
        int i = this.indicatorState.mCenterFlag;
        if (i == 2) {
            this.mPaint.setAlpha(this.mCurrentAlpha);
            drawCenterCaptureBitmap(canvas);
        } else if (i == 3) {
            this.mSunPaint.setColor(this.mCurrentColor);
            this.mSunPaint.setAlpha(this.mCurrentAlpha);
            drawCenterSun(canvas);
        } else if (i == 4) {
            this.mSunPaint.setColor(this.mCurrentColor);
            this.mSunPaint.setAlpha(this.mCurrentAlpha);
            drawCenterMoon(canvas);
        } else if (i != 5) {
            this.mPaint.setStrokeWidth(this.mCurrentStrokeWidth);
            this.mPaint.setAlpha(this.mCurrentAlpha);
            drawCenterCircle(canvas, 0.0f, 0.0f, this.mBaseRadius * this.mCurrentWidthPercent, this.mPaint);
        } else {
            this.mPaint.setAlpha(this.mCurrentAlpha);
            drawCenterLock(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void initPaint(Context context) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.STROKE);
        this.mSunPaint = new Paint();
        this.mSunPaint.setColor(-1);
        this.mSunPaint.setStyle(Style.FILL);
        this.mSunPaint.setAntiAlias(true);
        this.mMinusMoonPaint = new Paint();
        this.mMinusMoonPaint.setColor(-1);
        this.mMinusMoonPaint.setStyle(Style.FILL);
        this.mMinusMoonPaint.setAntiAlias(true);
        this.mMinusMoonPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
    }

    public void setAEAFIndicatorData(CameraIndicatorState cameraIndicatorState, Bitmap bitmap, Bitmap bitmap2) {
        this.indicatorState = cameraIndicatorState;
        this.mAEAFLockHeadBitmap = bitmap;
        this.mAEAFLockBodyBitmap = bitmap2;
    }

    public void setCenterFlag(int i) {
        this.indicatorState.mCenterFlag = i;
        this.mCenterFlagForAnimator = i;
    }

    public void setIndicatorData(CameraIndicatorState cameraIndicatorState, Bitmap bitmap) {
        this.indicatorState = cameraIndicatorState;
        this.mCaptureIndicatorBitmap = bitmap;
    }

    public void updateValue(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        super.updateValue(f);
        if (this.mCenterFlagForAnimator == 5) {
            this.indicatorState.mAEAFHeadPosition = f * 5.0f;
        }
    }
}
