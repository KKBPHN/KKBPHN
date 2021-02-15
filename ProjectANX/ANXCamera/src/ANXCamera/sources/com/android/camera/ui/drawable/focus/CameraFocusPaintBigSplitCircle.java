package com.android.camera.ui.drawable.focus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import com.android.camera.Util;
import com.android.camera.ui.drawable.CameraPaintBase;

public class CameraFocusPaintBigSplitCircle extends CameraPaintBase {
    private static final int SPLIT_HEIGHT = 10;
    private Paint mDownPaint;
    private Paint mUpPaint;

    public CameraFocusPaintBigSplitCircle(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        this.mUpPaint.setAlpha(this.mCurrentAlpha);
        canvas.save();
        float strokeWidth = this.mUpPaint.getStrokeWidth();
        float f = this.mBaseRadius * this.mCurrentWidthPercent;
        float f2 = this.mMiddleX;
        float f3 = (f2 - f) - strokeWidth;
        float f4 = this.mMiddleY;
        canvas.clipRect(f3, (f4 - f) - strokeWidth, f2 + f + strokeWidth, f4 - 5.0f);
        canvas.drawCircle(this.mMiddleX, this.mMiddleY, f, this.mUpPaint);
        canvas.restore();
        this.mDownPaint.setAlpha(this.mCurrentAlpha);
        canvas.save();
        float strokeWidth2 = this.mDownPaint.getStrokeWidth();
        float f5 = this.mMiddleX;
        float f6 = (f5 - f) - strokeWidth2;
        float f7 = this.mMiddleY;
        canvas.clipRect(f6, 5.0f + f7, f5 + f + strokeWidth2, f7 + f + strokeWidth2);
        canvas.drawCircle(this.mMiddleX, this.mMiddleY, f, this.mDownPaint);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void initPaint(Context context) {
        this.mUpPaint = new Paint();
        this.mUpPaint.setAntiAlias(true);
        this.mUpPaint.setStrokeWidth((float) Util.dpToPixel(1.33f));
        this.mUpPaint.setStyle(Style.STROKE);
        this.mUpPaint.setColor(-1);
        this.mDownPaint = new Paint();
        this.mDownPaint.setAntiAlias(true);
        this.mDownPaint.setStrokeWidth((float) Util.dpToPixel(1.33f));
        this.mDownPaint.setStyle(Style.STROKE);
        this.mDownPaint.setColor(Color.rgb(255, 204, 0));
    }
}
