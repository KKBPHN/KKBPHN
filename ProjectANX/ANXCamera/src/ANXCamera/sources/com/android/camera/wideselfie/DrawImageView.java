package com.android.camera.wideselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.android.camera.R;
import com.android.camera.log.Log;

public class DrawImageView extends View {
    private static final String TAG = "DrawImageView";
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private Paint mBorderPaint;
    private Rect mBorderRect;
    private Rect mImageRect;
    private int mOrientation;
    private int mStillPreviewHeight;
    private int mStillPreviewWidth;
    private int mThumbBgHeightVertical;
    private int mThumbBgWidth;
    private int mThumbnailBackgroundWidth;

    public DrawImageView(Context context) {
        this(context, null);
    }

    public DrawImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOrientation = 0;
        this.mImageRect = null;
        this.mBitmap = null;
        this.mBorderRect = null;
        this.mBitmapPaint = new Paint();
        this.mBorderPaint = new Paint();
        this.mBorderPaint.setStyle(Style.STROKE);
        this.mBorderPaint.setStrokeWidth(context.getResources().getDimension(R.dimen.wide_selfie_thumbnail_border_size));
    }

    private Rect convertBorderRect(Rect rect) {
        Rect rect2 = new Rect();
        if (this.mOrientation % 180 == 0) {
            int i = this.mStillPreviewWidth / 2;
            rect2.left = (this.mThumbBgWidth - rect.bottom) - 1;
            rect2.top = rect.left;
            rect2.right = rect2.left + rect.height() + 1;
            rect2.bottom = rect.right;
            rect2.offset(i, i);
            rect2.top = i;
            rect2.bottom = i + this.mStillPreviewHeight + 1;
        } else {
            int i2 = this.mStillPreviewHeight;
            int i3 = (i2 / 2) + 1;
            rect2.left = rect.top;
            rect2.top = (rect.left - 1) + (((this.mThumbBgHeightVertical - 2) - i2) / 2);
            rect2.right = rect.bottom;
            rect2.bottom = rect2.top + rect.width() + 1;
            rect2.offset(i3, i3);
            rect2.left = i3;
            rect2.right = i3 + this.mStillPreviewWidth + 1;
        }
        return rect2;
    }

    private Rect convertImageRect(Rect rect) {
        Rect rect2 = new Rect();
        if (this.mOrientation % 180 == 0) {
            int i = this.mStillPreviewWidth / 2;
            rect2.left = this.mThumbBgWidth - rect.bottom;
            rect2.top = rect.left;
            rect2.right = rect2.left + rect.height();
            rect2.bottom = rect.right;
            rect2.offset(i, i);
        } else {
            int i2 = this.mStillPreviewHeight / 2;
            rect2.left = rect.top;
            rect2.top = rect.left;
            rect2.right = rect.bottom;
            rect2.bottom = rect.right;
            rect2.offset(i2, i2);
        }
        return rect2;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            Rect rect = this.mImageRect;
            if (rect != null) {
                canvas.drawBitmap(bitmap, null, rect, this.mBitmapPaint);
                Rect rect2 = this.mBorderRect;
                if (rect2 != null) {
                    canvas.drawRect(rect2, this.mBorderPaint);
                }
                releaseBitmap();
            }
        }
    }

    public void releaseBitmap() {
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }
    }

    public void setBorderColor(int i) {
        this.mBorderPaint.setColor(i);
    }

    public void setImageBitmap(Bitmap bitmap, Rect rect, Rect rect2) {
        this.mBitmap = bitmap;
        if (rect != null) {
            this.mImageRect = convertImageRect(rect);
            StringBuilder sb = new StringBuilder();
            sb.append("convertImageRect src = ");
            sb.append(rect);
            String str = ", dest = ";
            sb.append(str);
            sb.append(this.mImageRect);
            sb.append(", mOrientation ");
            sb.append(this.mOrientation);
            String sb2 = sb.toString();
            String str2 = TAG;
            Log.d(str2, sb2);
            this.mBorderRect = convertBorderRect(rect2);
            this.mBorderRect.inset(2, 2);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("convertBorderRect src = ");
            sb3.append(rect2);
            sb3.append(str);
            sb3.append(this.mBorderRect);
            Log.d(str2, sb3.toString());
        }
        invalidate();
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
    }

    public void setParams(int i, int i2, int i3, int i4) {
        this.mStillPreviewWidth = i;
        this.mStillPreviewHeight = i2;
        this.mThumbBgWidth = i3;
        this.mThumbBgHeightVertical = i4;
    }
}
