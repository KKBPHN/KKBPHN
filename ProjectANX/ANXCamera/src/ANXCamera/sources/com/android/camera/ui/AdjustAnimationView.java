package com.android.camera.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Size;
import android.view.View;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.data.runing.ComponentRunningDocument;
import com.android.camera.log.Log;
import java.util.Arrays;

public class AdjustAnimationView extends View {
    private static final ArgbEvaluator ARGB_EVALUATOR = new ArgbEvaluator();
    private static final int POINTS_COUNT = 8;
    private static final String TAG = "AdjustAnimationView";
    private Bitmap mBmp;
    private float[] mBmpPoints = new float[8];
    private float[] mCurrentPoints = new float[8];
    private float[] mEndPoints = new float[8];
    private int mEndTopMargin;
    private Matrix mMatrix = new Matrix();
    private Size mPreviewSize;
    private float[] mStartPoints = new float[8];

    public AdjustAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Rect displayRect = Util.getDisplayRect(0);
        this.mEndTopMargin = displayRect.top + Math.round(((float) (displayRect.height() - getResources().getDimensionPixelOffset(R.dimen.document_preview_max_height))) / 2.0f);
    }

    private void initBmpPoints() {
        float[] fArr = this.mBmpPoints;
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        fArr[2] = (float) this.mBmp.getWidth();
        float[] fArr2 = this.mBmpPoints;
        fArr2[3] = 0.0f;
        fArr2[4] = (float) this.mBmp.getWidth();
        this.mBmpPoints[5] = (float) this.mBmp.getHeight();
        float[] fArr3 = this.mBmpPoints;
        fArr3[6] = 0.0f;
        fArr3[7] = (float) this.mBmp.getHeight();
    }

    private void initEndPoints(int i, int i2, int i3, int i4) {
        float f;
        float f2;
        int i5 = i;
        int i6 = i2;
        int windowWidth = (Display.getWindowWidth() - i5) / 2;
        int i7 = this.mEndTopMargin;
        float f3 = (float) i5;
        float f4 = (float) i6;
        float f5 = (float) i3;
        float f6 = (float) i4;
        if (f3 / f4 > f5 / f6) {
            f2 = (f5 * f4) / f6;
            float f7 = (f3 - f2) / 2.0f;
            float[] fArr = this.mEndPoints;
            float f8 = ((float) windowWidth) + f7;
            fArr[0] = f8;
            float f9 = (float) i7;
            fArr[1] = f9;
            float f10 = f8 + f2;
            fArr[2] = f10;
            fArr[3] = f9;
            fArr[4] = f10;
            float f11 = (float) (i7 + i6);
            fArr[5] = f11;
            fArr[6] = f8;
            fArr[7] = f11;
            f = f4;
        } else {
            f = (f6 * f3) / f5;
            float f12 = (f4 - f) / 2.0f;
            float[] fArr2 = this.mEndPoints;
            float f13 = (float) windowWidth;
            fArr2[0] = f13;
            float f14 = ((float) i7) + f12;
            fArr2[1] = f14;
            float f15 = (float) (windowWidth + i5);
            fArr2[2] = f15;
            fArr2[3] = f14;
            fArr2[4] = f15;
            float f16 = f14 + f;
            fArr2[5] = f16;
            fArr2[6] = f13;
            fArr2[7] = f16;
            f2 = f3;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("endWidth:");
        sb.append(f2);
        sb.append(", endHeight:");
        sb.append(f);
        Log.d(str, sb.toString());
    }

    private void updateMatrix() {
        this.mMatrix.setPolyToPoly(this.mBmpPoints, 0, this.mCurrentPoints, 0, 4);
    }

    public /* synthetic */ void O00000Oo(ValueAnimator valueAnimator) {
        for (int i = 0; i < 8; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("point_");
            sb.append(i);
            this.mCurrentPoints[i] = ((Float) valueAnimator.getAnimatedValue(sb.toString())).floatValue();
        }
        updateMatrix();
        invalidate();
    }

    public void clearBitmap() {
        this.mBmp = null;
        postInvalidate();
    }

    public Rect getImageRect() {
        Rect rect = new Rect(Math.round(this.mEndPoints[0]), Math.round(this.mEndPoints[1]), Math.round(this.mEndPoints[4]), Math.round(this.mEndPoints[5]));
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getImageRect: ");
        sb.append(rect);
        Log.d(str, sb.toString());
        return rect;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = this.mBmp;
        if (bitmap == null || bitmap.isRecycled()) {
            canvas.drawColor(0);
        } else {
            canvas.drawBitmap(this.mBmp, this.mMatrix, null);
        }
    }

    public void setBitmap(Bitmap bitmap, float[] fArr) {
        Bitmap bitmap2 = this.mBmp;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            this.mBmp.recycle();
        }
        Rect displayRect = Util.getDisplayRect();
        Matrix matrix = new Matrix();
        float min = ((float) Math.min(displayRect.width(), displayRect.height())) / ((float) Math.min(this.mPreviewSize.getWidth(), this.mPreviewSize.getHeight()));
        if (min != 1.0f) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setBitmap: ratio = ");
            sb.append(min);
            sb.append(", points = ");
            sb.append(Arrays.toString(fArr));
            Log.d(str, sb.toString());
            int length = fArr.length;
            for (int i = 0; i < length; i++) {
                fArr[i] = fArr[i] * min;
            }
            matrix.postScale(min, min);
        }
        this.mBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        for (int i2 = 0; i2 < 8; i2++) {
            if (i2 % 2 == 0) {
                this.mStartPoints[i2] = fArr[i2];
            } else {
                this.mStartPoints[i2] = fArr[i2] + ((float) displayRect.top);
            }
        }
        System.arraycopy(this.mStartPoints, 0, this.mCurrentPoints, 0, 8);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.document_preview_max_width);
        int dimensionPixelOffset2 = getResources().getDimensionPixelOffset(R.dimen.document_preview_max_height);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("width:");
        sb2.append(dimensionPixelOffset);
        sb2.append(", height:");
        sb2.append(dimensionPixelOffset2);
        sb2.append(", bmpWidth:");
        sb2.append(width);
        sb2.append(", bmpHeight:");
        sb2.append(height);
        Log.d(str2, sb2.toString());
        initBmpPoints();
        initEndPoints(dimensionPixelOffset, dimensionPixelOffset2, width, height);
        updateMatrix();
    }

    public void setPreviewSize(Size size) {
        this.mPreviewSize = size;
    }

    public void startAnim(AnimatorListener animatorListener, long j) {
        AnimatorListener animatorListener2 = animatorListener;
        AnimatorSet animatorSet = new AnimatorSet();
        ColorDrawable colorDrawable = new ColorDrawable(0);
        setBackground(colorDrawable);
        ObjectAnimator ofObject = ObjectAnimator.ofObject(colorDrawable, ComponentRunningDocument.DOCUMENT_STRENGTHEN, ARGB_EVALUATOR, new Object[]{Integer.valueOf(1912602624)});
        ofObject.setDuration(Util.getEnterDuration() / 2);
        ValueAnimator valueAnimator = new ValueAnimator();
        PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[8];
        for (int i = 0; i < 8; i++) {
            float f = this.mStartPoints[i];
            float f2 = this.mEndPoints[i];
            StringBuilder sb = new StringBuilder();
            sb.append("point_");
            sb.append(i);
            propertyValuesHolderArr[i] = PropertyValuesHolder.ofFloat(sb.toString(), new float[]{f, f2});
        }
        valueAnimator.setValues(propertyValuesHolderArr);
        valueAnimator.addUpdateListener(new O000000o(this));
        valueAnimator.setDuration(j);
        if (animatorListener2 != null) {
            valueAnimator.addListener(animatorListener2);
        }
        animatorSet.playTogether(new Animator[]{ofObject, valueAnimator});
        animatorSet.start();
    }

    public void startBackgroundAnimator() {
        ObjectAnimator ofObject = ObjectAnimator.ofObject((ColorDrawable) getBackground(), ComponentRunningDocument.DOCUMENT_STRENGTHEN, ARGB_EVALUATOR, new Object[]{Integer.valueOf(0)});
        ofObject.setDuration(Util.getExitDuration());
        ofObject.start();
    }
}
