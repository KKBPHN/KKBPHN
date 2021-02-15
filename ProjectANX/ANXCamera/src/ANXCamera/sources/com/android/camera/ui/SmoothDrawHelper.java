package com.android.camera.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.Op;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;

public class SmoothDrawHelper {
    private int mArcColor = 0;
    private Paint mArcPaint;
    private Path mArcPath;
    private Paint mClipPaint = new Paint(1);
    private Path mClipPath;
    private RectF mLayer;
    private Path mOutterPath;
    private SmoothPathProvider mPathProvider;
    private int mProgress;
    private float[] mRadii;
    private float mRadius;
    private int mStrokeColor = 0;
    private Paint mStrokePaint = new Paint(1);
    private int mStrokeWidth = 0;

    public SmoothDrawHelper() {
        this.mStrokePaint.setStyle(Style.STROKE);
        this.mArcPaint = new Paint(1);
        this.mArcPaint.setStyle(Style.FILL);
        this.mArcPaint.setColor(this.mArcColor);
        this.mOutterPath = new Path();
        this.mClipPath = new Path();
        this.mArcPath = new Path();
        this.mPathProvider = new SmoothPathProvider();
        this.mLayer = new RectF();
    }

    private Path getNormalPath(Path path, RectF rectF, float[] fArr, float f, float f2, float f3) {
        path.reset();
        rectF.set(rectF.left + f2, rectF.top + f3, rectF.right - f2, rectF.bottom - f3);
        Direction direction = Direction.CW;
        if (fArr == null) {
            path.addRoundRect(rectF, f, f, direction);
        } else {
            path.addRoundRect(rectF, fArr, direction);
        }
        path.close();
        return path;
    }

    private Path getSmoothPathFromProvider(Path path, RectF rectF, float[] fArr, float f, float f2, float f3) {
        if (fArr == null) {
            this.mPathProvider.buildSmoothData(rectF, f, f2, f3);
        } else {
            this.mPathProvider.buildSmoothData(rectF, fArr, f2, f3);
        }
        return this.mPathProvider.getSmoothPath(path);
    }

    public void drawArc(Canvas canvas, Xfermode xfermode) {
        int round = Math.round((((float) this.mProgress) / 100.0f) * 360.0f);
        this.mArcPaint.setXfermode(xfermode);
        this.mArcPaint.setColor(this.mArcColor);
        canvas.drawArc(this.mLayer, -90.0f, (float) round, true, this.mArcPaint);
    }

    public void drawMask(Canvas canvas, Xfermode xfermode) {
        this.mClipPaint.setXfermode(xfermode);
        canvas.drawPath(this.mClipPath, this.mClipPaint);
        this.mClipPaint.setXfermode(null);
    }

    public void drawMask(Canvas canvas, Xfermode xfermode, int i) {
        this.mClipPaint.setXfermode(xfermode);
        if (i != 0) {
            this.mClipPaint.setColor(i);
        }
        canvas.drawPath(this.mClipPath, this.mClipPaint);
        this.mClipPaint.setXfermode(null);
    }

    public void drawStroke(Canvas canvas, Xfermode xfermode) {
        boolean z = (this.mStrokeWidth == 0 || this.mStrokeColor == 0) ? false : true;
        if (z) {
            canvas.save();
            this.mStrokePaint.setXfermode(xfermode);
            this.mStrokePaint.setStrokeWidth((float) this.mStrokeWidth);
            this.mStrokePaint.setColor(this.mStrokeColor);
            canvas.drawPath(this.mOutterPath, this.mStrokePaint);
            canvas.restore();
        }
    }

    public int getArcColor() {
        return this.mArcColor;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public float[] getRadii() {
        return this.mRadii;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public int getStrokeColor() {
        return this.mStrokeColor;
    }

    public int getStrokeWidth() {
        return this.mStrokeWidth;
    }

    public void onBoundsChange(Rect rect, boolean z) {
        Path path;
        this.mLayer.set(rect);
        boolean z2 = (this.mStrokeWidth == 0 || this.mStrokeColor == 0) ? false : true;
        float f = z2 ? (float) this.mStrokeWidth : 0.0f;
        if (z) {
            path = getSmoothPathFromProvider(this.mOutterPath, this.mLayer, this.mRadii, this.mRadius, f, f);
        } else {
            path = this.mOutterPath;
            getNormalPath(path, this.mLayer, this.mRadii, this.mRadius, f, f);
        }
        this.mOutterPath = path;
        Path path2 = this.mClipPath;
        if (path2 != null) {
            path2.reset();
        } else {
            this.mClipPath = new Path();
        }
        this.mClipPath.addRect(this.mLayer, Direction.CW);
        this.mClipPath.op(this.mOutterPath, Op.DIFFERENCE);
    }

    public void setArcColor(int i) {
        this.mArcColor = i;
    }

    public void setProgress(int i) {
        this.mProgress = i;
    }

    public void setRadii(float[] fArr) {
        this.mRadii = fArr;
    }

    public void setRadius(float f) {
        this.mRadius = f;
    }

    public void setStrokeColor(int i) {
        this.mStrokeColor = i;
    }

    public void setStrokeWidth(int i) {
        this.mStrokeWidth = i;
    }
}
