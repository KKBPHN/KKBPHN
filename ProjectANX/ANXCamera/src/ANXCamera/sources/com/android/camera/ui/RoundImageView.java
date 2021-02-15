package com.android.camera.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.core.view.ViewCompat;
import com.android.camera.R;

public class RoundImageView extends ImageView {
    private final PaintFlagsDrawFilter mDrawFilter;
    private Bitmap mMaskBitmap;
    private Paint mPaint;
    private float mRadius;
    private RectF mSrcRectF;
    private float mSrcSize;
    private Xfermode mXfermode;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDrawFilter = new PaintFlagsDrawFilter(0, 3);
        this.mPaint = new Paint();
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setAntiAlias(true);
        this.mSrcSize = getResources().getDimension(R.dimen.live_filter_item_size);
        float f = this.mSrcSize;
        this.mSrcRectF = new RectF(0.0f, 0.0f, f, f);
        this.mRadius = (float) getResources().getDimensionPixelSize(R.dimen.live_filter_item_corners_size);
        this.mXfermode = new PorterDuffXfermode(Mode.DST_IN);
        float f2 = this.mSrcSize;
        this.mMaskBitmap = Bitmap.createBitmap((int) f2, (int) f2, Config.ARGB_8888);
        Canvas canvas = new Canvas(this.mMaskBitmap);
        new Paint().setColor(ViewCompat.MEASURED_STATE_MASK);
        RectF rectF = this.mSrcRectF;
        float f3 = this.mRadius;
        canvas.drawRoundRect(rectF, f3, f3, this.mPaint);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int saveLayer = canvas.saveLayer(this.mSrcRectF, null, 31);
        super.onDraw(canvas);
        canvas.setDrawFilter(this.mDrawFilter);
        this.mPaint.setXfermode(this.mXfermode);
        canvas.drawBitmap(this.mMaskBitmap, 0.0f, 0.0f, this.mPaint);
        this.mPaint.setXfermode(null);
        canvas.restoreToCount(saveLayer);
    }

    public void setRoundRadius(float f) {
        this.mRadius = f;
    }
}
