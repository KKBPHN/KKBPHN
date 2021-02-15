package com.android.camera.ui.drawable.snap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import com.android.camera.Util;
import com.android.camera.ui.drawable.CameraPaintBase;

public class CameraSnapPaintBottom extends CameraPaintBase {
    private RectF mArcRectF;
    private boolean mFixCircleOffset;
    private Paint mOffsetPaint;
    private RectF mRectF;
    private float mStokeWidth;

    public CameraSnapPaintBottom(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        RectF rectF;
        float f = this.mBaseRadius * this.mCurrentWidthPercent;
        float f2 = this.mMiddleX;
        float f3 = f2 - f;
        float f4 = f2 + f;
        float f5 = this.mMiddleY;
        float f6 = f5 - f;
        float f7 = f5 + f;
        this.mRectF.set(f3, f6, f4, f7);
        canvas.drawRoundRect(this.mRectF, f, f, this.mPaint);
        if (this.isRecording) {
            if (this.mFixCircleOffset) {
                rectF = this.mArcRectF;
                float f8 = this.mStokeWidth;
                f3 += f8 / 2.0f;
                f6 += f8 / 2.0f;
                f4 -= f8 / 2.0f;
                f7 -= f8 / 2.0f;
            } else {
                rectF = this.mArcRectF;
            }
            rectF.set(f3, f6, f4, f7);
            canvas.drawArc(this.mArcRectF, (this.isClockwise ? 0.0f : this.timeAngle) - 0.049804688f, Math.max(0.0f, this.isClockwise ? this.timeAngle : 360.0f - this.timeAngle), false, this.mOffsetPaint);
        }
    }

    /* access modifiers changed from: protected */
    public void initPaint(Context context) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
        this.mRectF = new RectF();
        this.mArcRectF = new RectF();
        this.mOffsetPaint = new Paint();
        this.mOffsetPaint.setAntiAlias(true);
        this.mStokeWidth = (float) Util.dpToPixel(4.0f);
        this.mOffsetPaint.setStrokeWidth(this.mStokeWidth);
        this.mOffsetPaint.setStyle(Style.STROKE);
        this.mOffsetPaint.setColor(-1);
    }

    public void setFixCircleOffset(boolean z) {
        this.mFixCircleOffset = z;
    }
}
