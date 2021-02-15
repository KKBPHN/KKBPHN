package com.miui.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.miui.internal.R;

public class ArrowPopupContentWrapper extends LinearLayout {
    private Bitmap mMask1;
    private Bitmap mMask2;
    private Bitmap mMask3;
    private Bitmap mMask4;
    private Paint mPaint;

    public ArrowPopupContentWrapper(Context context) {
        this(context, null);
    }

    public ArrowPopupContentWrapper(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ArrowPopupContentWrapper(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPaint = new Paint();
        this.mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        this.mPaint.setAntiAlias(true);
        Resources resources = getResources();
        this.mMask1 = BitmapFactory.decodeResource(resources, R.drawable.popup_mask_1);
        this.mMask2 = BitmapFactory.decodeResource(resources, R.drawable.popup_mask_2);
        this.mMask3 = BitmapFactory.decodeResource(resources, R.drawable.popup_mask_3);
        this.mMask4 = BitmapFactory.decodeResource(resources, R.drawable.popup_mask_4);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), null, 31);
        super.dispatchDraw(canvas);
        canvas.drawBitmap(this.mMask1, (float) (getPaddingLeft() + 0), (float) (getPaddingTop() + 0), this.mPaint);
        canvas.drawBitmap(this.mMask2, (float) ((getWidth() - this.mMask2.getWidth()) - getPaddingRight()), (float) (getPaddingTop() + 0), this.mPaint);
        canvas.drawBitmap(this.mMask3, (float) (getPaddingLeft() + 0), (float) ((getHeight() - this.mMask3.getHeight()) - getPaddingBottom()), this.mPaint);
        canvas.drawBitmap(this.mMask4, (float) ((getWidth() - this.mMask4.getWidth()) - getPaddingRight()), (float) ((getHeight() - this.mMask4.getHeight()) - getPaddingBottom()), this.mPaint);
        canvas.restore();
    }
}
