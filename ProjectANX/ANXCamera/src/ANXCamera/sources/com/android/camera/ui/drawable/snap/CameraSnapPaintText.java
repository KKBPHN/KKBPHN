package com.android.camera.ui.drawable.snap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import com.android.camera.ui.drawable.CameraPaintBase;

public class CameraSnapPaintText extends CameraPaintBase {
    private static final float RECORDING_ROUND_WIDTH = 0.265f;
    public boolean isRecordingCircle;
    public boolean isRoundingCircle;
    private float mCurrentRoundRectRadius;
    private RectF mRectF;
    private float mRoundingProgress = 1.0f;

    public CameraSnapPaintText(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        if (!this.isRecording) {
            f = this.mMiddleX;
            f2 = this.mMiddleY;
            f3 = this.mBaseRadius;
            f4 = this.mCurrentWidthPercent;
        } else if (this.isRecordingCircle) {
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
            float f5 = this.mBaseRadius * this.mCurrentRoundRectRadius;
            float f6 = this.mMiddleX;
            float f7 = f6 - f5;
            float f8 = f6 + f5;
            float f9 = this.mMiddleY;
            this.mRectF.set(f7, f9 - f5, f8, f9 + f5);
            RectF rectF = this.mRectF;
            float f10 = this.mRoundingProgress;
            canvas.drawRoundRect(rectF, f5 * f10, f5 * f10, this.mPaint);
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

    public void prepareRecord() {
        this.mCurrentRoundRectRadius = this.mCurrentWidthPercent;
        this.mRoundingProgress = 1.0f;
    }

    public void resetRecordingState() {
        super.resetRecordingState();
        this.isRecordingCircle = false;
        this.isRoundingCircle = false;
    }

    public void updateRecordValue(float f, boolean z) {
        float f2;
        if (z) {
            this.mRoundingProgress = this.isRoundingCircle ? 1.0f - (0.45f * f) : 1.0f - (0.65f * f);
            float f3 = this.mCurrentWidthPercent;
            f2 = f3 - ((f3 - RECORDING_ROUND_WIDTH) * f);
        } else {
            if (this.isRoundingCircle) {
                this.mRoundingProgress = (0.45f * f) + 0.55f;
            } else {
                this.mRoundingProgress = (0.65f * f) + 0.35f;
            }
            f2 = ((this.mCurrentWidthPercent - RECORDING_ROUND_WIDTH) * f) + RECORDING_ROUND_WIDTH;
        }
        this.mCurrentRoundRectRadius = f2;
    }
}
