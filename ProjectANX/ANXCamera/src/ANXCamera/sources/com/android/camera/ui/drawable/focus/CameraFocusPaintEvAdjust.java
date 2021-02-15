package com.android.camera.ui.drawable.focus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import com.android.camera.Util;
import com.android.camera.ui.FocusView;
import com.android.camera.ui.drawable.CameraPaintBase;

public class CameraFocusPaintEvAdjust extends CameraPaintBase {
    private static final int MARGIN = Util.dpToPixel(12.0f);
    private static final int TRIANGLE_BASE_DIS = Util.dpToPixel(3.0f);
    private static final int TRIANGLE_BASE_HEIGHT = Util.dpToPixel(5.0f);
    private static final int TRIANGLE_BASE_LEN = Util.dpToPixel(8.0f);
    private static final int TRIANGLE_MIN_MARGIN = Util.dpToPixel(25.0f);
    private float mCurrentDistanceY;
    private float mCurrentOffsetY;
    private Rect mDisplayRect;
    private float mEvValue = -1.0f;
    private int mLineAlpha = 204;
    private int mLineHeight = FocusView.MAX_SLIDE_DISTANCE;
    private int mLineMargin = Util.dpToPixel(2.0f);
    private Paint mLinePaint;
    private int mRotation;
    private boolean mRtl = false;
    private boolean mShowLine = true;
    private float mStartOffsetY;

    public CameraFocusPaintEvAdjust(Context context) {
        super(context);
    }

    private boolean isNearlyOutOfEdge() {
        boolean z;
        boolean z2;
        int width = this.mDisplayRect.width();
        int height = this.mDisplayRect.height();
        int i = this.mRotation;
        boolean z3 = true;
        if ((i / 90) % 2 == 0) {
            if (this.mRtl) {
                if ((this.mMiddleX - ((float) CameraFocusAnimateDrawable.BIG_RADIUS)) - ((float) MARGIN) < ((float) TRIANGLE_MIN_MARGIN)) {
                    z3 = false;
                }
                return z3;
            }
            if (((((float) width) - this.mMiddleX) - ((float) CameraFocusAnimateDrawable.BIG_RADIUS)) - ((float) MARGIN) >= ((float) TRIANGLE_MIN_MARGIN)) {
                z3 = false;
            }
            return z3;
        } else if (i == 90) {
            if (((((float) height) - this.mMiddleY) - ((float) CameraFocusAnimateDrawable.BIG_RADIUS)) - ((float) MARGIN) >= ((float) TRIANGLE_MIN_MARGIN)) {
                z2 = false;
            }
            return z2;
        } else if (i != 270) {
            return false;
        } else {
            if ((this.mMiddleY - ((float) CameraFocusAnimateDrawable.BIG_RADIUS)) - ((float) MARGIN) >= ((float) TRIANGLE_MIN_MARGIN)) {
                z = false;
            }
            return z;
        }
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        Path path = new Path();
        float f = (isNearlyOutOfEdge() ? (this.mMiddleX - ((float) CameraFocusAnimateDrawable.BIG_RADIUS)) - ((float) MARGIN) : (this.mMiddleX + ((float) CameraFocusAnimateDrawable.BIG_RADIUS)) + ((float) MARGIN)) - ((float) (TRIANGLE_BASE_LEN / 2));
        float f2 = ((this.mMiddleY - this.mCurrentOffsetY) + this.mCurrentDistanceY) - ((float) (TRIANGLE_BASE_DIS / 2));
        path.moveTo(f, f2);
        path.lineTo(((float) TRIANGLE_BASE_LEN) + f, f2);
        path.lineTo(((float) (TRIANGLE_BASE_LEN / 2)) + f, f2 - ((float) TRIANGLE_BASE_HEIGHT));
        path.lineTo(f, f2);
        int i = TRIANGLE_BASE_LEN;
        float f3 = ((float) (i / 2)) + f;
        float f4 = ((float) (i / 2)) + f;
        float f5 = this.mMiddleY - ((float) (this.mLineHeight / 2));
        if (this.mShowLine) {
            int i2 = TRIANGLE_BASE_HEIGHT;
            float f6 = (f2 - ((float) i2)) - f5;
            int i3 = this.mLineMargin;
            if (f6 > ((float) i3)) {
                float f7 = (f2 - ((float) i2)) - ((float) i3);
                this.mLinePaint.setColor(this.mCurrentColor);
                this.mLinePaint.setAlpha(this.mLineAlpha);
                this.mLinePaint.setStrokeWidth(2.0f);
                canvas.drawLine(f3, f5, f4, f7, this.mLinePaint);
            }
        }
        float f8 = this.mMiddleY + this.mCurrentOffsetY + this.mCurrentDistanceY + ((float) (TRIANGLE_BASE_DIS / 2));
        path.moveTo(f, f8);
        path.lineTo(((float) TRIANGLE_BASE_LEN) + f, f8);
        path.lineTo(((float) (TRIANGLE_BASE_LEN / 2)) + f, ((float) TRIANGLE_BASE_HEIGHT) + f8);
        path.lineTo(f, f8);
        canvas.drawPath(path, this.mPaint);
        if (this.mShowLine) {
            float f9 = this.mMiddleY + ((float) (this.mLineHeight / 2));
            int i4 = this.mLineMargin;
            float f10 = f9 - ((float) i4);
            int i5 = TRIANGLE_BASE_HEIGHT;
            if (f10 > ((float) i5) + f8) {
                float f11 = f8 + ((float) i5) + ((float) i4);
                this.mLinePaint.setColor(this.mCurrentColor);
                this.mLinePaint.setAlpha(this.mLineAlpha);
                this.mLinePaint.setStrokeWidth(2.0f);
                canvas.drawLine(f3, f11, f4, f9, this.mLinePaint);
            }
        }
    }

    public float getEvValue() {
        return this.mEvValue;
    }

    /* access modifiers changed from: protected */
    public void initPaint(Context context) {
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setAntiAlias(true);
        this.mLinePaint = new Paint();
        this.mLinePaint.setAntiAlias(true);
        this.mLinePaint.setStyle(Style.FILL);
        this.mLinePaint.setColor(Color.argb(102, 255, 255, 255));
    }

    public void setDistanceY(float f) {
        this.mCurrentDistanceY = f;
    }

    public void setEvValue(float f) {
        this.mEvValue = f;
    }

    public void setLineAlpha(int i) {
        this.mLineAlpha = i;
    }

    public void setOrientation(int i) {
        this.mRotation = i;
    }

    public void setRtlAndDisplayRect(boolean z, Rect rect) {
        this.mRtl = z;
        this.mDisplayRect = rect;
    }

    public void setShowLine(boolean z) {
        this.mShowLine = z;
    }

    public void setStartOffsetY(float f) {
        this.mStartOffsetY = f;
    }

    public void updateValue(float f) {
        super.updateValue(f);
        float f2 = this.mStartOffsetY;
        this.mCurrentOffsetY = f2 - (f * f2);
    }
}
