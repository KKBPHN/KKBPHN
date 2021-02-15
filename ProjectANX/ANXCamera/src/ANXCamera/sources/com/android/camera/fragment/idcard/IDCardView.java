package com.android.camera.fragment.idcard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.log.Log;

public class IDCardView extends View {
    private static final String TAG = "IDCardView";
    private int mBigColor;
    private Bitmap mBitmap1;
    private Bitmap mBitmap2;
    private Bitmap mBitmap3;
    private Bitmap mBitmap4;
    private float mBitmapHeight;
    private float mBitmapWidth;
    private Context mContext;
    private Rect mDisplayRect;
    private String mHint;
    private RectF mIDCardRectF;
    private float mMarginLeft;
    private float mMarginTop;
    private Paint mPaint;
    private Path mPath;
    private float mRectHeight;
    private Paint mRectPaint;
    private float mRectWidth;
    private Bitmap mSignBitmap;
    private RectF mSignRectF;
    private Paint mTextPaint;

    public IDCardView(Context context, boolean z) {
        super(context);
        init(context, z);
    }

    private Bitmap decodeResourceCatchOOM(Resources resources, int i) {
        try {
            return BitmapFactory.decodeResource(resources, i);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "decodeResourceCatchOOM", (Throwable) e);
            return null;
        }
    }

    private void init(Context context, boolean z) {
        this.mContext = context;
        Resources resources = this.mContext.getResources();
        this.mDisplayRect = Util.getDisplayRect();
        this.mTextPaint = new Paint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setColor(-1);
        this.mTextPaint.setTextSize(resources.getDimension(R.dimen.id_card_text_size));
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mRectPaint = new Paint();
        this.mRectPaint.setAntiAlias(true);
        this.mBigColor = resources.getColor(R.color.id_card_big_rect_color);
        this.mRectWidth = resources.getDimension(R.dimen.id_card_rect_width);
        this.mRectHeight = resources.getDimension(R.dimen.id_card_rect_height);
        Rect rect = this.mDisplayRect;
        this.mMarginLeft = (((float) rect.right) - this.mRectWidth) / 2.0f;
        this.mMarginTop = ((((float) rect.height()) - this.mRectHeight) / 2.0f) + ((float) this.mDisplayRect.top);
        float dimension = resources.getDimension(R.dimen.id_card_head_margin_left);
        float dimension2 = resources.getDimension(R.dimen.id_card_head_margin_top);
        float dimension3 = resources.getDimension(R.dimen.id_card_emblem_margin_left);
        float dimension4 = resources.getDimension(R.dimen.id_card_emblem_margin_top);
        this.mBitmap1 = decodeResourceCatchOOM(resources, R.drawable.id_card_1);
        this.mBitmap2 = decodeResourceCatchOOM(resources, R.drawable.id_card_2);
        this.mBitmap3 = decodeResourceCatchOOM(resources, R.drawable.id_card_3);
        this.mBitmap4 = decodeResourceCatchOOM(resources, R.drawable.id_card_4);
        Bitmap decodeResourceCatchOOM = decodeResourceCatchOOM(resources, R.drawable.id_card_head);
        Bitmap decodeResourceCatchOOM2 = decodeResourceCatchOOM(resources, R.drawable.id_card_emblem);
        this.mBitmapWidth = (float) this.mBitmap1.getWidth();
        this.mBitmapHeight = (float) this.mBitmap1.getHeight();
        String string = resources.getString(R.string.id_card_mode_hint_front);
        String string2 = resources.getString(R.string.id_card_mode_hint_back);
        this.mIDCardRectF = new RectF();
        RectF rectF = this.mIDCardRectF;
        float f = this.mMarginLeft;
        rectF.left = f;
        float f2 = this.mMarginTop;
        rectF.top = f2;
        rectF.right = f + this.mRectWidth;
        rectF.bottom = f2 + this.mRectHeight;
        if (z) {
            float dimension5 = resources.getDimension(R.dimen.id_card_head_rect_width);
            float dimension6 = resources.getDimension(R.dimen.id_card_head_rect_height);
            this.mHint = string;
            RectF rectF2 = this.mIDCardRectF;
            float f3 = rectF2.left + dimension;
            float f4 = rectF2.top + dimension2;
            this.mSignRectF = new RectF(f3, f4, dimension5 + f3, dimension6 + f4);
            this.mSignBitmap = decodeResourceCatchOOM;
        } else {
            float dimension7 = resources.getDimension(R.dimen.id_card_emblem_rect_width);
            float dimension8 = resources.getDimension(R.dimen.id_card_emblem_rect_height);
            this.mHint = string2;
            RectF rectF3 = this.mIDCardRectF;
            float f5 = rectF3.left + dimension3;
            float f6 = rectF3.top + dimension4;
            this.mSignRectF = new RectF(f5, f6, dimension7 + f5, dimension8 + f6);
            this.mSignBitmap = decodeResourceCatchOOM2;
        }
        this.mPath = new Path();
        Rect rect2 = new Rect();
        Paint paint = this.mTextPaint;
        String str = this.mHint;
        paint.getTextBounds(str, 0, str.length(), rect2);
        float height = (((((float) this.mDisplayRect.right) - this.mIDCardRectF.right) - ((float) rect2.height())) / 2.0f) + this.mIDCardRectF.right;
        Rect rect3 = this.mDisplayRect;
        this.mPath.moveTo(height, ((float) rect3.top) + (((float) (rect3.height() - rect2.width())) / 2.0f));
        this.mPath.lineTo(height, (float) this.mDisplayRect.bottom);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mRectPaint.setColor(this.mBigColor);
        this.mRectPaint.setXfermode(null);
        canvas.drawRect(this.mDisplayRect, this.mRectPaint);
        this.mRectPaint.setColor(0);
        this.mRectPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        canvas.drawRect(this.mIDCardRectF, this.mRectPaint);
        if (!Util.isAccessible()) {
            canvas.drawTextOnPath(this.mHint, this.mPath, 0.0f, 0.0f, this.mTextPaint);
        }
        Bitmap bitmap = this.mBitmap1;
        if (bitmap != null) {
            RectF rectF = this.mIDCardRectF;
            canvas.drawBitmap(bitmap, rectF.left, rectF.top, this.mPaint);
        }
        Bitmap bitmap2 = this.mBitmap2;
        if (bitmap2 != null) {
            RectF rectF2 = this.mIDCardRectF;
            canvas.drawBitmap(bitmap2, rectF2.right - this.mBitmapWidth, rectF2.top, this.mPaint);
        }
        Bitmap bitmap3 = this.mBitmap3;
        if (bitmap3 != null) {
            RectF rectF3 = this.mIDCardRectF;
            canvas.drawBitmap(bitmap3, rectF3.left, rectF3.bottom - this.mBitmapHeight, this.mPaint);
        }
        Bitmap bitmap4 = this.mBitmap4;
        if (bitmap4 != null) {
            RectF rectF4 = this.mIDCardRectF;
            canvas.drawBitmap(bitmap4, rectF4.right - this.mBitmapWidth, rectF4.bottom - this.mBitmapHeight, this.mPaint);
        }
        Bitmap bitmap5 = this.mSignBitmap;
        if (bitmap5 != null) {
            canvas.drawBitmap(bitmap5, null, this.mSignRectF, this.mPaint);
        }
    }
}
