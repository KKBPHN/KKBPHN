package com.miui.internal.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.miui.internal.R;

public class RoundFrameLayout extends FrameLayout {
    private Paint mCutPaint;
    private Bitmap mMaskBottomLeft;
    private Bitmap mMaskBottomRight;
    private Bitmap mMaskTopLeft;
    private Bitmap mMaskTopRight;

    public RoundFrameLayout(Context context) {
        this(context, null);
    }

    public RoundFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundFrameLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public RoundFrameLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mCutPaint = new Paint();
        this.mCutPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        this.mCutPaint.setAntiAlias(true);
        this.mMaskTopLeft = getBitmap(context.getDrawable(R.drawable.dialog_bg_mask_top_left));
        this.mMaskTopRight = getBitmap(context.getDrawable(R.drawable.dialog_bg_mask_top_right));
        this.mMaskBottomLeft = getBitmap(context.getDrawable(R.drawable.dialog_bg_mask_bottom_left));
        this.mMaskBottomRight = getBitmap(context.getDrawable(R.drawable.dialog_bg_mask_bottom_right));
    }

    private Bitmap getBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), null, 31);
        super.dispatchDraw(canvas);
        Bitmap bitmap = this.mMaskTopLeft;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.mCutPaint);
        }
        Bitmap bitmap2 = this.mMaskTopRight;
        if (bitmap2 != null) {
            canvas.drawBitmap(bitmap2, (float) (getWidth() - this.mMaskTopRight.getWidth()), 0.0f, this.mCutPaint);
        }
        Bitmap bitmap3 = this.mMaskBottomLeft;
        if (bitmap3 != null) {
            canvas.drawBitmap(bitmap3, 0.0f, (float) (getHeight() - this.mMaskBottomLeft.getHeight()), this.mCutPaint);
        }
        Bitmap bitmap4 = this.mMaskBottomRight;
        if (bitmap4 != null) {
            canvas.drawBitmap(bitmap4, (float) (getWidth() - this.mMaskTopRight.getWidth()), (float) (getHeight() - this.mMaskBottomLeft.getHeight()), this.mCutPaint);
        }
        canvas.restore();
    }
}
