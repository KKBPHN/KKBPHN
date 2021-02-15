package com.android.camera.ui.drawable.snap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.core.view.ViewCompat;
import com.android.camera.constant.ColorConstant;
import com.android.camera.ui.drawable.CameraPaintBase;
import java.util.ArrayList;
import java.util.List;

public class CameraSnapPaintCircle extends CameraPaintBase {
    public boolean isInBeautyMode;
    private RectF mArcRectF;
    private int mBeautyColor = ColorConstant.COLOR_COMMON_SELECTED_BEAUTY;
    private boolean mNeedProcessShader;
    private boolean mNeedRing = false;
    private boolean mNoNeedArc;
    private Paint mOffsetPaint;
    private int mProcessColor;
    private Shader mProcessShader;
    private Paint mRecordingBgPaint;
    private RectF mRectF;
    private float mRingWidth;
    private List mSegmentRatios;
    private List mSegmentTimes;
    private float mSpaceAngle = 3.0f;

    public CameraSnapPaintCircle(Context context) {
        super(context);
    }

    private void drawElapsedArc(Canvas canvas, float f, float f2) {
        RectF rectF = this.mArcRectF;
        float f3 = -90.0f + f;
        float f4 = this.mSpaceAngle;
        float f5 = f3 + f4;
        float f6 = (f2 - f) - f4;
        canvas.drawArc(rectF, f5, f6, false, this.mPaint);
    }

    private void drawOffset(Canvas canvas, float f) {
        RectF rectF = this.mArcRectF;
        float f2 = f - 0.049804688f;
        float f3 = this.mSpaceAngle;
        canvas.drawArc(rectF, f2 - f3, f3 * 2.0f, false, this.mOffsetPaint);
    }

    public void addSegmentNow(long j) {
        if (this.mSegmentRatios == null) {
            this.mSegmentRatios = new ArrayList();
        }
        this.mSegmentRatios.add(Float.valueOf(this.timeAngle));
        if (this.mSegmentTimes == null) {
            this.mSegmentTimes = new ArrayList();
        }
        this.mSegmentTimes.add(Long.valueOf(j));
    }

    public void clearSegments() {
        List list = this.mSegmentRatios;
        if (list != null) {
            list.clear();
        }
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        if (this.mNoNeedArc) {
            this.isRecording = false;
        }
        if (this.isRecording) {
            float f = this.mBaseRadius * this.mCurrentWidthPercent;
            float f2 = this.mMiddleX;
            float f3 = f2 - f;
            float f4 = this.mMiddleY;
            float f5 = f4 - f;
            float f6 = f2 + f;
            float f7 = f4 + f;
            if (this.mNeedProcessShader) {
                this.mPaint.setShader(null);
            }
            this.mPaint.setAlpha(this.isInBeautyMode ? this.mCurrentAlpha : CameraPaintBase.ALPHA_HINT);
            this.mArcRectF.set(f3, f5, f6, f7);
            canvas.drawArc(this.mArcRectF, -90.0f, 360.0f, false, this.mPaint);
            this.mPaint.setAlpha(CameraPaintBase.ALPHA_OPAQUE);
            if (this.mNeedProcessShader) {
                this.mPaint.setColor(this.mProcessColor);
            }
            if (this.isInBeautyMode) {
                this.mPaint.setColor(this.mBeautyColor);
            }
            canvas.drawArc(this.mArcRectF, (this.isClockwise ? 0.0f : this.timeAngle) - 0.049804688f, Math.max(0.0f, this.isClockwise ? this.timeAngle : 360.0f - this.timeAngle), false, this.mPaint);
            this.mPaint.setColor(this.mCurrentColor);
            this.mOffsetPaint.setStrokeWidth(this.mPaint.getStrokeWidth() + 1.0f);
            if (this.timeAngle != 0.0f) {
                drawOffset(canvas, 0.0f);
                drawOffset(canvas, this.timeAngle);
            }
            List<Float> list = this.mSegmentRatios;
            if (list != null) {
                for (Float floatValue : list) {
                    drawOffset(canvas, floatValue.floatValue());
                }
            }
        } else if (this.mNeedRing) {
            this.mPaint.setAlpha(CameraPaintBase.ALPHA_OUTSTANDING);
            canvas.drawCircle(this.mMiddleX, this.mMiddleY, this.mBaseRadius * this.mRingWidth, this.mPaint);
            this.mPaint.setAlpha(this.mCurrentAlpha);
        } else {
            float f8 = this.mBaseRadius * this.mCurrentWidthPercent;
            float f9 = this.mMiddleX;
            float f10 = f9 - f8;
            float f11 = f9 + f8;
            float f12 = this.mMiddleY;
            this.mRectF.set(f10, f12 - f8, f11, f12 + f8);
            Paint paint = this.mRecordingBgPaint;
            if (paint != null) {
                canvas.drawRoundRect(this.mRectF, f8, f8, paint);
            }
            canvas.drawRoundRect(this.mRectF, f8, f8, this.mPaint);
        }
    }

    public List getSegmentRatios() {
        return this.mSegmentRatios;
    }

    public boolean hasSegments() {
        List list = this.mSegmentRatios;
        return list != null && !list.isEmpty();
    }

    /* access modifiers changed from: protected */
    public boolean immediatelyColorChange() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void initPaint(Context context) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.STROKE);
        this.mArcRectF = new RectF();
        this.mOffsetPaint = new Paint();
        this.mOffsetPaint.setAntiAlias(true);
        this.mOffsetPaint.setStyle(Style.STROKE);
        this.mOffsetPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mOffsetPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        this.mRectF = new RectF();
    }

    public void initVVProcessShader() {
    }

    public long removeLastSegmentAndGetLastTime() {
        if (this.mSegmentRatios.isEmpty()) {
            return 0;
        }
        List list = this.mSegmentRatios;
        list.remove(list.size() - 1);
        List list2 = this.mSegmentTimes;
        list2.remove(list2.size() - 1);
        if (this.mSegmentRatios.isEmpty()) {
            this.timeAngle = 0.0f;
            return 0;
        }
        List list3 = this.mSegmentRatios;
        this.timeAngle = ((Float) list3.get(list3.size() - 1)).floatValue();
        if (this.mSegmentTimes.isEmpty()) {
            return 0;
        }
        List list4 = this.mSegmentTimes;
        return ((Long) list4.get(list4.size() - 1)).longValue();
    }

    public void resetRecordingState() {
        super.resetRecordingState();
        setNeedSplit(false);
        setNeedProcessShade(false, 0);
    }

    public void setNeedProcessShade(boolean z, int i) {
        this.mNeedProcessShader = z;
        if (z) {
            this.mProcessColor = i;
        } else {
            this.mPaint.setShader(null);
        }
    }

    public void setNeedSplit(boolean z) {
        this.mNoNeedArc = !z;
        setSpaceAngel(z ? 1.5f : 0.0f);
    }

    public void setRecordingBgColor(@ColorInt int i) {
        if (i == 0) {
            this.mRecordingBgPaint = null;
            return;
        }
        if (this.mRecordingBgPaint == null) {
            this.mRecordingBgPaint = new Paint();
            this.mRecordingBgPaint.setAntiAlias(true);
            this.mRecordingBgPaint.setStyle(Style.FILL);
        }
        this.mRecordingBgPaint.setColor(i);
    }

    public void setRingVisible(int i) {
        this.mNeedRing = i == 0;
    }

    public void setSpaceAngel(float f) {
        this.mSpaceAngle = f;
    }

    public void setSpecificProgress(@IntRange(from = 0, to = 100) int i) {
        this.timeAngle = (float) Math.round((((float) i) / 100.0f) * 360.0f);
    }

    public void updateValue(float f) {
        super.updateValue(f);
        float f2 = this.mCurrentWidthPercent;
        this.mRingWidth = f2 + (0.4f * f2 * f);
    }
}
