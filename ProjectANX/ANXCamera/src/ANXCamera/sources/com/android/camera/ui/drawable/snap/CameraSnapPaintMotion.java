package com.android.camera.ui.drawable.snap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import com.android.camera.ui.drawable.CameraPaintBase;

@Deprecated
public class CameraSnapPaintMotion extends CameraPaintBase {
    private boolean mIsOutstandingRound = false;
    private float mLastAngle = 0.0f;

    public CameraSnapPaintMotion(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004d, code lost:
        if (r14.mIsOutstandingRound != false) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0057, code lost:
        if (r14.mIsOutstandingRound != false) goto L_0x0050;
     */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0067 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void draw(Canvas canvas) {
        Paint paint;
        int i;
        float f = this.mBaseRadius * this.mCurrentWidthPercent;
        if (this.timeAngle - this.mLastAngle < 0.0f) {
            this.mIsOutstandingRound = !this.mIsOutstandingRound;
        }
        for (int i2 = 0; i2 < 40; i2++) {
            canvas.save();
            float f2 = (float) (i2 * 9);
            canvas.rotate(f2, this.mMiddleX, this.mMiddleY);
            int i3 = this.mCurrentAlpha;
            int i4 = 19;
            if (this.isRecording) {
                if (f2 != 0.0f || !this.needZero) {
                    if (f2 < this.timeAngle) {
                        paint = this.mPaint;
                    } else {
                        paint = this.mPaint;
                    }
                    i = CameraPaintBase.ALPHA_HINT;
                    paint.setAlpha(i);
                    if (f2 % 90.0f == 0.0f) {
                        float f3 = this.mMiddleX;
                        float f4 = this.mMiddleY;
                        canvas.drawLine(f3, f4 - f, f3, (f4 - f) + ((float) i4), this.mPaint);
                        this.mPaint.setAlpha(i3);
                        canvas.restore();
                    }
                } else {
                    paint = this.mPaint;
                }
                i = CameraPaintBase.ALPHA_OUTSTANDING;
                paint.setAlpha(i);
                if (f2 % 90.0f == 0.0f) {
                }
            } else if (f2 % 90.0f == 0.0f) {
                float f32 = this.mMiddleX;
                float f42 = this.mMiddleY;
                canvas.drawLine(f32, f42 - f, f32, (f42 - f) + ((float) i4), this.mPaint);
                this.mPaint.setAlpha(i3);
                canvas.restore();
            }
            i4 = 12;
            float f322 = this.mMiddleX;
            float f422 = this.mMiddleY;
            canvas.drawLine(f322, f422 - f, f322, (f422 - f) + ((float) i4), this.mPaint);
            this.mPaint.setAlpha(i3);
            canvas.restore();
        }
        float f5 = this.timeAngle;
        this.mLastAngle = f5;
        if (this.isRecording) {
            canvas.rotate(f5, this.mMiddleX, this.mMiddleY);
            int i5 = this.mCurrentAlpha;
            this.mPaint.setAlpha(CameraPaintBase.ALPHA_OUTSTANDING);
            float f6 = this.mMiddleX;
            float f7 = this.mMiddleY;
            canvas.drawLine(f6, f7 - f, f6, (f7 - f) + 19.0f, this.mPaint);
            this.mPaint.setAlpha(i5);
        }
    }

    /* access modifiers changed from: protected */
    public void initPaint(Context context) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(3.0f);
    }
}
