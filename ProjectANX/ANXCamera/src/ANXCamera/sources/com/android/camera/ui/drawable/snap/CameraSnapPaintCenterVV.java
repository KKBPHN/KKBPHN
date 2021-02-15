package com.android.camera.ui.drawable.snap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import com.android.camera.R;
import com.android.camera.ui.drawable.CameraPaintBase;

public class CameraSnapPaintCenterVV extends CameraPaintBase {
    private static final float RECORDING_ROUND_WIDTH = 0.265f;
    private float mCurrentRoundRectRadius;
    private String mDurationText;
    private PaintPattern mExternalPattern;
    private RectF mRectF;
    private float mRoundingProgress = 1.0f;
    private Rect mTextBound;
    private Paint mTextPaint;

    public CameraSnapPaintCenterVV(Context context) {
        super(context);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(R.style.SettingStatusBarText, new int[]{16842901, 16842904});
        int color = obtainStyledAttributes.getColor(obtainStyledAttributes.getIndex(1), -1);
        obtainStyledAttributes.getDimensionPixelSize(obtainStyledAttributes.getIndex(0), 0);
        obtainStyledAttributes.recycle();
        this.mTextPaint = new Paint();
        this.mTextPaint.setColor(color);
        this.mTextPaint.setStyle(Style.FILL);
        this.mTextPaint.setTextSize((float) context.getResources().getDimensionPixelSize(R.dimen.vv_progress_center_text));
        this.mTextPaint.setTextAlign(Align.LEFT);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setAlpha(255);
        this.mTextPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        this.mTextBound = new Rect();
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        PaintPattern paintPattern = this.mExternalPattern;
        if (paintPattern != null && paintPattern.interceptDraw()) {
            this.mExternalPattern.draw(canvas);
        }
        float f = this.mBaseRadius * this.mCurrentRoundRectRadius;
        float f2 = this.mMiddleX;
        float f3 = f2 - f;
        float f4 = f2 + f;
        float f5 = this.mMiddleY;
        this.mRectF.set(f3, f5 - f, f4, f5 + f);
        if (!this.isRecording) {
            RectF rectF = this.mRectF;
            float f6 = this.mRoundingProgress;
            canvas.drawRoundRect(rectF, f * f6, f * f6, this.mPaint);
        }
        if (!TextUtils.isEmpty(this.mDurationText)) {
            Paint paint = this.mTextPaint;
            String str = this.mDurationText;
            paint.getTextBounds(str, 0, str.length(), this.mTextBound);
            canvas.drawText(this.mDurationText, this.mMiddleX - ((float) (this.mTextBound.width() / 2)), this.mMiddleY + ((float) (this.mTextBound.height() / 2)), this.mTextPaint);
        }
    }

    /* access modifiers changed from: protected */
    public void initPaint(Context context) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
        this.mRectF = new RectF();
    }

    public void intoLoadingPattern() {
    }

    public void intoNextPattern() {
    }

    public void intoStartShotPattern(Context context) {
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_vv_start);
        this.mExternalPattern = new CameraSnapPaintPatternBitmap(this, decodeResource);
        ((CameraSnapPaintPatternBitmap) this.mExternalPattern).setOffset((float) (decodeResource.getWidth() / 5), 0.0f);
    }

    public void prepareRecord() {
        this.mCurrentRoundRectRadius = this.mCurrentWidthPercent;
        this.mRoundingProgress = 1.0f;
    }

    public void resetRecordingState() {
        super.resetRecordingState();
        this.mDurationText = null;
    }

    public void setBitmapPatternTargetScale(float f) {
        ((CameraSnapPaintPatternBitmap) this.mExternalPattern).setTargetScale(f);
    }

    public void setDurationText(String str) {
        this.mDurationText = str;
    }

    public boolean showBitmapPattern() {
        return getVisible() == 0 && this.mExternalPattern != null;
    }

    public void updateRecordValue(float f, boolean z) {
        float f2;
        if (z) {
            this.mRoundingProgress = 1.0f - (0.65f * f);
            float f3 = this.mCurrentWidthPercent;
            f2 = f3 - ((f3 - RECORDING_ROUND_WIDTH) * f);
        } else {
            this.mRoundingProgress = (0.65f * f) + 0.35f;
            f2 = ((this.mCurrentWidthPercent - RECORDING_ROUND_WIDTH) * f) + RECORDING_ROUND_WIDTH;
        }
        this.mCurrentRoundRectRadius = f2;
    }

    public void updateValue(float f) {
        super.updateValue(f);
        PaintPattern paintPattern = this.mExternalPattern;
        if (paintPattern != null) {
            paintPattern.updateValue(f);
        }
    }
}
