package com.android.camera.ui.drawable.snap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import com.android.camera.Util;
import com.android.camera.ui.drawable.CameraPaintBase;

public class CameraSnapPaintSecond extends CameraPaintBase {
    protected float baseAngel;
    protected float baseLineWidth;
    protected float commonLineWidthCurrent;
    protected float commonLineWidthDst;
    protected float commonLineWidthSrc;
    private PaintPattern mExternalPattern;
    protected float motionLineWidthCurrent;
    protected float motionLineWidthDst;
    protected float motionLineWidthSrc;
    protected float quarterAngelCurrent;
    protected float quarterAngelDst;
    protected float quarterAngelSrc;
    protected float squashAngelCurrent;
    protected float squashAngelDst;
    protected float squashAngelSrc;

    public CameraSnapPaintSecond(Context context) {
        super(context);
    }

    public void clearPatternAndExternal() {
        this.mExternalPattern = null;
        float f = this.baseAngel;
        this.quarterAngelCurrent = f;
        this.squashAngelCurrent = f;
        float f2 = this.baseLineWidth;
        this.commonLineWidthCurrent = f2;
        this.motionLineWidthCurrent = f2;
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        PaintPattern paintPattern = this.mExternalPattern;
        if (paintPattern == null || !paintPattern.interceptDraw()) {
            float f = this.mBaseRadius * this.mCurrentWidthPercent;
            int i = 0;
            float f2 = 0.0f;
            while (i < 90) {
                canvas.save();
                float f3 = i == 0 ? 0.0f : (i <= 79 || i >= 90) ? this.squashAngelCurrent : this.quarterAngelCurrent;
                f2 += f3;
                canvas.rotate(f2, this.mMiddleX, this.mMiddleY);
                int i2 = this.mCurrentAlpha;
                if (this.isRecording) {
                    if (f2 != 0.0f || !this.needZero) {
                        if (f2 < this.timeAngle) {
                        }
                        i2 = CameraPaintBase.ALPHA_HINT;
                    }
                    i2 = CameraPaintBase.ALPHA_OUTSTANDING;
                }
                this.mPaint.setAlpha(i2);
                float f4 = this.commonLineWidthCurrent;
                if (i == 22 || i == 45 || i == 68 || i == 0) {
                    f4 = this.motionLineWidthCurrent;
                }
                float f5 = this.mMiddleX;
                float f6 = this.mMiddleY;
                canvas.drawLine(f5, f6 - f, f5, (f6 - f) + f4, this.mPaint);
                canvas.restore();
                i++;
            }
            return;
        }
        this.mExternalPattern.draw(canvas);
    }

    /* access modifiers changed from: protected */
    public void initPaint(Context context) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(3.0f);
        this.baseAngel = 4.0f;
        this.baseLineWidth = (float) Util.dpToPixel(4.5f);
        clearPatternAndExternal();
    }

    public void intoFastPattern() {
        this.mExternalPattern = new CameraSnapPaintSecondPatternFast(this);
    }

    public void intoProgressPattern() {
        this.mExternalPattern = new CameraSnapPaintSecondPatternProgress(this);
    }

    public void intoSlowPattern() {
        this.mExternalPattern = new CameraSnapPaintSecondPatternSlow(this);
    }

    public void removePatternOnly() {
        this.mExternalPattern = null;
    }

    public void setResult() {
        super.setResult();
        PaintPattern paintPattern = this.mExternalPattern;
        if (paintPattern != null) {
            paintPattern.directlyResult();
        }
    }

    public void updateValue(float f) {
        super.updateValue(f);
        PaintPattern paintPattern = this.mExternalPattern;
        if (paintPattern != null) {
            paintPattern.updateValue(f);
        }
    }
}
