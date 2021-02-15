package com.android.camera.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.Size;
import android.view.View;
import androidx.annotation.Nullable;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.ColorConstant;
import com.android.camera.log.Log;
import com.xiaomi.ocr.sdk.imgprocess.DocumentProcess.QuadStatus;
import java.util.Arrays;

public class DocumentView extends View {
    private static final String TAG = "DocumentView";
    private static final int THRESHOLD = 18;
    private boolean isHidePath;
    private Size mDisplaySize;
    AnimatorSet mFlickerAnimatorSet = new AnimatorSet();
    private Paint mLayerPaint = new Paint();
    private Paint mPaint;
    private Path mPath = new Path();
    private Size mPreviewSize;

    public DocumentView(Context context) {
        super(context);
        initPaint();
    }

    public DocumentView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initPaint();
    }

    private void initPaint() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeJoin(Join.ROUND);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth((float) getResources().getDimensionPixelOffset(R.dimen.document_preview_rect_width));
        this.mPaint.setColor(ColorConstant.COLOR_COMMON_SELECTED);
        this.mLayerPaint = new Paint();
        this.mLayerPaint.setStyle(Style.FILL);
        this.mLayerPaint.setColor(268422678);
    }

    public void clear() {
        this.mPath.reset();
        postInvalidate();
    }

    public void hideOrShowPath(boolean z) {
        this.isHidePath = !z;
        clear();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.drawPath(this.mPath, this.mLayerPaint);
    }

    public void setDisplaySize(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("setDisplaySize: ");
        sb.append(i);
        sb.append("x");
        sb.append(i2);
        Log.d(TAG, sb.toString());
        this.mDisplaySize = new Size(i, i2);
    }

    public void setPreviewSize(int i, int i2) {
        this.mPreviewSize = new Size(i, i2);
    }

    public void updateDocument(Pair pair) {
        if (pair != null && ((float[]) pair.second).length == 8 && !this.isHidePath) {
            Object obj = pair.first;
            if (obj != QuadStatus.QUAD_NOSHOW) {
                if (obj != QuadStatus.QUAD_UPDATE) {
                    this.mFlickerAnimatorSet.end();
                } else if (!this.mFlickerAnimatorSet.isRunning()) {
                    String str = "alpha";
                    this.mFlickerAnimatorSet.playSequentially(new Animator[]{ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(str, new float[]{0.0f})}), ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(str, new float[]{1.0f})})});
                    this.mFlickerAnimatorSet.setDuration(Util.getFlickerDuration());
                    this.mFlickerAnimatorSet.start();
                }
                float[] fArr = (float[]) pair.second;
                Size size = this.mDisplaySize;
                int measuredWidth = size == null ? getMeasuredWidth() : size.getWidth();
                Size size2 = this.mDisplaySize;
                int measuredHeight = size2 == null ? getMeasuredHeight() : size2.getHeight();
                float min = ((float) Math.min(measuredWidth, measuredHeight)) / ((float) Math.min(this.mPreviewSize.getWidth(), this.mPreviewSize.getHeight()));
                if (min != 1.0f) {
                    int length = fArr.length;
                    for (int i = 0; i < length; i++) {
                        fArr[i] = fArr[i] * min;
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append("updateDocument: width = ");
                sb.append(measuredWidth);
                sb.append(", height = ");
                sb.append(measuredHeight);
                sb.append(", ratio = ");
                sb.append(min);
                sb.append(", points = ");
                sb.append(Arrays.toString(fArr));
                String sb2 = sb.toString();
                String str2 = TAG;
                Log.d(str2, sb2);
                if (fArr[0] < 18.0f && fArr[1] < 18.0f) {
                    float f = (float) measuredWidth;
                    if (f - fArr[2] < 18.0f && fArr[3] < 18.0f && f - fArr[4] < 18.0f) {
                        float f2 = (float) measuredHeight;
                        if (f2 - fArr[5] < 18.0f && fArr[6] < 18.0f && f2 - fArr[7] < 18.0f) {
                            Log.d(str2, "updateDocument: reset path");
                            this.mPath.reset();
                            postInvalidate();
                            return;
                        }
                    }
                }
                this.mPath.reset();
                this.mPath.moveTo(fArr[0], fArr[1]);
                int length2 = fArr.length / 2;
                for (int i2 = 1; i2 < length2; i2++) {
                    int i3 = i2 * 2;
                    this.mPath.lineTo(fArr[i3], fArr[i3 + 1]);
                }
                this.mPath.close();
                postInvalidate();
                return;
            }
        }
        clear();
    }
}
