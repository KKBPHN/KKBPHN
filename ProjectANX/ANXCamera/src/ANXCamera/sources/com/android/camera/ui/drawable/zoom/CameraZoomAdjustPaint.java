package com.android.camera.ui.drawable.zoom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.customization.TintColor;
import com.android.camera.ui.drawable.CameraPaintBase;

public class CameraZoomAdjustPaint extends CameraPaintBase {
    private static final int ANIM_NONE = 0;
    private static final int ANIM_SLIDER_BACK = 5;
    private static final int ANIM_ZOOM = 1;
    private static final int RECT_WIDTH = 30;
    private static final int SLIDER_IMAGE_GAP = 20;
    private int mAnimState = 0;
    public float mAvaliableWidth;
    private float mBigCircleRadius;
    private float mBreakpointWidth;
    private Paint mClearPaint;
    private float mCurrentCircleRadius;
    private int mDegree = 0;
    private Drawable mDrawableLeft;
    private Drawable mDrawableRight;
    private Point mLineEndPosition = new Point();
    private Paint mLinePaint;
    private Point mLineStartPosition = new Point();
    private float mMaxZoomRatio;
    private float mMinZoomRatio;
    private float mNormalCircleRadius;
    private int mOrientation;
    public Point mSliderOriginalPosition = new Point();
    private Paint mSliderPaint;
    private Point mSliderPosition = new Point();
    private Rect mSliderTouchRect = new Rect();
    private Point mSliderTouchUpPosition = new Point();
    private int mTextBaseLineY;
    private Paint mTextPaint;
    private int mViewHeight;
    private int mViewSliderWidth;
    public int mViewWidth = Display.getWindowWidth();
    private int mZoomLastColor;
    private Paint mZoomLinePaint;
    private int mZoomMidColor;
    private String mZoomRatio;
    private int mZoomStartColor;

    CameraZoomAdjustPaint(Context context, boolean z) {
        super(context);
        this.mViewSliderWidth = context.getResources().getDimensionPixelSize(R.dimen.pro_view_zoom_view_slider_height);
        this.mViewHeight = context.getResources().getDimensionPixelSize(R.dimen.pro_view_zoom_view_width);
        this.mBreakpointWidth = (float) Util.dpToPixel(2.181f);
        this.mNormalCircleRadius = (float) ((Util.dpToPixel(32.73f) / 2) - (Util.dpToPixel(1.818f) / 2));
        this.mBigCircleRadius = (float) ((Util.dpToPixel(36.36f) / 2) - (Util.dpToPixel(1.818f) / 2));
        float f = this.mNormalCircleRadius;
        this.mCurrentCircleRadius = f;
        int i = this.mViewSliderWidth;
        this.mAvaliableWidth = ((float) i) - (f * 2.0f);
        Point point = this.mLineStartPosition;
        Point point2 = this.mLineEndPosition;
        int i2 = this.mViewHeight;
        int i3 = i2 / 2;
        point2.y = i3;
        point.y = i3;
        int i4 = this.mViewWidth;
        point.x = (i4 / 2) - (i / 2);
        point2.x = i + point.x;
        Point point3 = this.mSliderOriginalPosition;
        Point point4 = this.mSliderPosition;
        int i5 = i2 / 2;
        point4.y = i5;
        point3.y = i5;
        int i6 = i4 / 2;
        point4.x = i6;
        point3.x = i6;
        this.mSliderTouchRect.set(Math.round(((float) point4.x) - this.mBigCircleRadius) - 1, 0, Math.round(((float) this.mSliderPosition.x) + this.mBigCircleRadius) + 1, this.mViewHeight);
        FontMetricsInt fontMetricsInt = this.mTextPaint.getFontMetricsInt();
        int i7 = this.mViewHeight / 2;
        int i8 = fontMetricsInt.bottom;
        this.mTextBaseLineY = (i7 + ((i8 - fontMetricsInt.top) / 2)) - i8;
        this.mZoomStartColor = TintColor.tintColor();
        this.mZoomMidColor = context.getResources().getColor(R.color.zoom_change_process_mid);
        this.mZoomLastColor = context.getResources().getColor(R.color.zoom_change_process_last);
        this.mDrawableLeft = context.getResources().getDrawable(R.drawable.ic_zoom_change_process_reduce);
        this.mDrawableRight = context.getResources().getDrawable(R.drawable.ic_zoom_change_process_add);
        Drawable drawable = this.mDrawableLeft;
        drawable.setBounds((this.mLineStartPosition.x - 20) - drawable.getIntrinsicWidth(), (this.mViewHeight / 2) - (this.mDrawableLeft.getIntrinsicHeight() / 2), this.mLineStartPosition.x - 20, (this.mViewHeight / 2) + (this.mDrawableLeft.getIntrinsicHeight() / 2));
        Drawable drawable2 = this.mDrawableRight;
        drawable2.setBounds(this.mLineEndPosition.x + 20, (this.mViewHeight / 2) - (drawable2.getIntrinsicHeight() / 2), this.mLineEndPosition.x + 20 + this.mDrawableRight.getIntrinsicWidth(), (this.mViewHeight / 2) + (this.mDrawableRight.getIntrinsicHeight() / 2));
    }

    private void updateTextAndCircle(float f) {
        this.mTextPaint.setAlpha(Math.round(255.0f * f));
        if (f > 0.8f) {
            this.mTextPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        } else {
            this.mTextPaint.clearShadowLayer();
        }
        float f2 = this.mBigCircleRadius;
        this.mCurrentCircleRadius = f2 - (f * (f2 - this.mNormalCircleRadius));
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate((float) this.mDegree, (float) ((this.mLineStartPosition.x - 20) - (this.mDrawableLeft.getIntrinsicWidth() / 2)), (float) (this.mViewHeight / 2));
        this.mDrawableLeft.draw(canvas);
        canvas.restore();
        int saveLayer = canvas.saveLayer(0.0f, 0.0f, (float) this.mViewWidth, (float) this.mViewHeight, null, 31);
        Point point = this.mLineStartPosition;
        float f = (float) point.x;
        float f2 = (float) point.y;
        Point point2 = this.mLineEndPosition;
        Canvas canvas2 = canvas;
        canvas2.drawLine(f, f2, (float) point2.x, (float) point2.y, this.mLinePaint);
        Point point3 = this.mSliderOriginalPosition;
        float f3 = (float) point3.x;
        float f4 = (float) point3.y;
        Point point4 = this.mSliderPosition;
        canvas2.drawLine(f3, f4, (float) point4.x, (float) point4.y, this.mZoomLinePaint);
        canvas.drawRect(((float) this.mSliderOriginalPosition.x) - (this.mBreakpointWidth / 2.0f), ((float) (this.mViewHeight / 2)) - this.mZoomLinePaint.getStrokeWidth(), ((float) this.mSliderOriginalPosition.x) + (this.mBreakpointWidth / 2.0f), ((float) (this.mViewHeight / 2)) + this.mZoomLinePaint.getStrokeWidth(), this.mClearPaint);
        float f5 = (float) this.mSliderPosition.x;
        float f6 = (float) this.mLineStartPosition.x;
        float f7 = this.mCurrentCircleRadius;
        float clamp = Util.clamp(f5, f6 + f7, ((float) this.mLineEndPosition.x) - f7);
        canvas.drawRect(((clamp - this.mCurrentCircleRadius) - (this.mSliderPaint.getStrokeWidth() / 2.0f)) - this.mBreakpointWidth, ((float) (this.mViewHeight / 2)) - this.mZoomLinePaint.getStrokeWidth(), this.mCurrentCircleRadius + clamp + (this.mSliderPaint.getStrokeWidth() / 2.0f) + this.mBreakpointWidth, ((float) (this.mViewHeight / 2)) + this.mSliderPaint.getStrokeWidth(), this.mClearPaint);
        canvas.drawCircle(clamp, (float) this.mSliderPosition.y, this.mCurrentCircleRadius, this.mSliderPaint);
        canvas.save();
        canvas.rotate((float) this.mDegree, clamp, (float) (this.mViewHeight / 2));
        canvas.drawText(this.mZoomRatio, clamp, (float) this.mTextBaseLineY, this.mTextPaint);
        canvas.restore();
        canvas.restoreToCount(saveLayer);
        canvas.save();
        canvas.rotate((float) this.mDegree, (float) (this.mLineEndPosition.x + 20 + (this.mDrawableRight.getIntrinsicWidth() / 2)), (float) (this.mViewHeight / 2));
        this.mDrawableRight.draw(canvas);
        canvas.restore();
    }

    public Rect getSliderTouchRect() {
        return this.mSliderTouchRect;
    }

    public float getSpeed(int i) {
        float f = ((float) (i - this.mLineStartPosition.x)) - this.mNormalCircleRadius;
        float f2 = this.mAvaliableWidth;
        return ((f - (f2 / 2.0f)) / f2) * 0.1f;
    }

    /* access modifiers changed from: protected */
    public void initPaint(Context context) {
        this.mClearPaint = new Paint();
        this.mClearPaint.setAntiAlias(true);
        this.mClearPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        this.mSliderPaint = new Paint();
        this.mSliderPaint.setAntiAlias(true);
        this.mSliderPaint.setStrokeWidth((float) Util.dpToPixel(1.818f));
        this.mSliderPaint.setStyle(Style.STROKE);
        this.mSliderPaint.setColor(-1);
        this.mSliderPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        this.mLinePaint = new Paint();
        this.mLinePaint.setAntiAlias(true);
        this.mLinePaint.setStyle(Style.FILL);
        this.mLinePaint.setStrokeCap(Cap.ROUND);
        this.mLinePaint.setColor(-1);
        this.mLinePaint.setStrokeWidth((float) Util.dpToPixel(1.818f));
        this.mLinePaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        this.mZoomLinePaint = new Paint();
        this.mZoomLinePaint.setAntiAlias(true);
        this.mZoomLinePaint.setStrokeWidth((float) Util.dpToPixel(1.818f));
        this.mZoomLinePaint.setStyle(Style.FILL);
        this.mZoomLinePaint.setColor(this.mZoomStartColor);
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setColor(-1);
        this.mTextPaint.setTextAlign(Align.CENTER);
        this.mTextPaint.setTextSize((float) Util.dpToPixel(11.636f));
        this.mTextPaint.setFakeBoldText(true);
        this.mTextPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
    }

    public void move(Point point) {
        this.mAnimState = 1;
        this.mSliderPosition.x = Util.clamp(point.x, this.mLineStartPosition.x, this.mLineEndPosition.x);
        this.mSliderTouchRect.set(Math.round(((float) this.mSliderPosition.x) - this.mBigCircleRadius) - 1, 0, Math.round(((float) this.mSliderPosition.x) + this.mBigCircleRadius) + 1, this.mViewHeight);
    }

    public void reset() {
        this.mTextPaint.setAlpha(255);
        this.mTextPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        this.mCurrentCircleRadius = this.mNormalCircleRadius;
        Point point = this.mSliderPosition;
        point.x = this.mViewWidth / 2;
        this.mSliderTouchRect.set(Math.round(((float) point.x) - this.mBigCircleRadius) - 1, 0, Math.round(((float) this.mSliderPosition.x) + this.mBigCircleRadius) + 1, this.mViewHeight);
        this.mAnimState = 0;
    }

    public void setCurrentZoomRatio(float f) {
        StringBuilder sb = new StringBuilder();
        float decimal = HybridZoomingSystem.toDecimal(f);
        int i = (int) decimal;
        if (((int) ((10.0f * decimal) - ((float) (i * 10)))) == 0) {
            sb.append(i);
        } else {
            sb.append(decimal);
        }
        sb.append("X");
        this.mZoomRatio = sb.toString();
        this.mZoomStartColor = TintColor.tintColor();
        this.mZoomLinePaint.setColor(this.mZoomStartColor);
    }

    public void setOrientation(int i) {
        this.mDegree = i;
    }

    public void setZoomRange(float f, float f2) {
        this.mMinZoomRatio = f;
        this.mMaxZoomRatio = f2;
    }

    public void startSliderBackAnimation() {
        this.mAnimState = 5;
        this.mSliderTouchUpPosition.x = this.mSliderPosition.x;
    }

    public void updateSliderPositionForBackAnim(float f, boolean z) {
        if (this.mAnimState == 5) {
            if (z) {
                updateTextAndCircle(f);
            }
            Point point = this.mSliderPosition;
            point.x = (int) (((1.0f - f) * ((float) this.mSliderTouchUpPosition.x)) + (((float) this.mSliderOriginalPosition.x) * f));
            this.mSliderTouchRect.set(Math.round(((float) point.x) - this.mBigCircleRadius) - 1, 0, Math.round(((float) this.mSliderPosition.x) + this.mBigCircleRadius) + 1, this.mViewHeight);
            if (f >= 1.0f) {
                this.mAnimState = 0;
            }
        }
    }

    public void updateSliderTouchScaleAnimation(float f) {
        updateTextAndCircle(f);
    }
}
