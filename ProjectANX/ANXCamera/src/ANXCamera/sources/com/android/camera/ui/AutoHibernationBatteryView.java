package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.android.camera.R;
import com.android.camera.Util;

public class AutoHibernationBatteryView extends View {
    private String mBattery = "";
    private int mBatteryContentHeight;
    private Drawable mBatteryDrawable;
    private int mBatteryInt = 0;
    private int mBatteryLow;
    private int mBatteryMarginLeft;
    private int mBatteryMarginRight;
    private int mBatteryNormal;
    private int mGap;
    private int mHeight;
    private Paint mPaint;
    protected TextPaint mTextPaint;
    private int mTextSize;
    private int mWidth;

    public AutoHibernationBatteryView(Context context) {
        super(context);
        init(context);
    }

    public AutoHibernationBatteryView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public AutoHibernationBatteryView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public AutoHibernationBatteryView(Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    private void init(Context context) {
        this.mWidth = context.getResources().getDimensionPixelSize(R.dimen.auto_hibernation_battery_view_width);
        this.mHeight = context.getResources().getDimensionPixelSize(R.dimen.auto_hibernation_battery_view_height);
        this.mGap = context.getResources().getDimensionPixelSize(R.dimen.auto_hibernation_battery_view_gap);
        this.mBatteryMarginLeft = context.getResources().getDimensionPixelSize(R.dimen.auto_hibernation_battery_view_content_margin_left);
        this.mBatteryMarginRight = context.getResources().getDimensionPixelSize(R.dimen.auto_hibernation_battery_view_content_margin_right);
        this.mBatteryContentHeight = context.getResources().getDimensionPixelSize(R.dimen.auto_hibernation_battery_view_content_height);
        this.mBatteryNormal = context.getResources().getColor(R.color.auto_hibernation_battery_nornal);
        this.mBatteryLow = context.getResources().getColor(R.color.auto_hibernation_battery_low);
        this.mTextSize = context.getResources().getDimensionPixelSize(R.dimen.auto_hibernation_battery_view_number_text_size);
        this.mTextPaint = new TextPaint(1);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize((float) this.mTextSize);
        this.mTextPaint.setColor(this.mBatteryNormal);
        this.mTextPaint.setTypeface(Util.getMiuiTypeface());
        this.mBatteryDrawable = context.getResources().getDrawable(R.drawable.ic_vector_fastmotion_auto_hibernation_battery);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(this.mBatteryNormal);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i;
        Paint paint;
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(this.mBattery)) {
            Rect rect = new Rect();
            TextPaint textPaint = this.mTextPaint;
            String str = this.mBattery;
            textPaint.getTextBounds(str, 0, str.length(), rect);
            FontMetricsInt fontMetricsInt = this.mTextPaint.getFontMetricsInt();
            int width = (((this.mWidth - rect.width()) - this.mBatteryDrawable.getIntrinsicWidth()) - this.mGap) / 2;
            int intrinsicHeight = (this.mHeight - this.mBatteryDrawable.getIntrinsicHeight()) / 2;
            Drawable drawable = this.mBatteryDrawable;
            drawable.setBounds(width, intrinsicHeight, drawable.getIntrinsicWidth() + width, this.mBatteryDrawable.getIntrinsicHeight() + intrinsicHeight);
            this.mBatteryDrawable.draw(canvas);
            float intrinsicWidth = ((float) this.mBatteryDrawable.getIntrinsicWidth()) - ((float) this.mBatteryMarginRight);
            int i2 = this.mBatteryMarginLeft;
            float f = ((intrinsicWidth - ((float) i2)) * ((float) this.mBatteryInt)) / 100.0f;
            float f2 = (float) width;
            float f3 = ((float) i2) + f2;
            int i3 = this.mHeight;
            int i4 = this.mBatteryContentHeight;
            RectF rectF = new RectF(f3, ((float) (i3 - i4)) / 2.0f, f2 + ((float) i2) + f, ((float) (i3 + i4)) / 2.0f);
            if (this.mBatteryInt >= 20) {
                paint = this.mPaint;
                i = this.mBatteryNormal;
            } else {
                paint = this.mPaint;
                i = this.mBatteryLow;
            }
            paint.setColor(i);
            canvas.drawRoundRect(rectF, 2.0f, 2.0f, this.mPaint);
            canvas.drawText(this.mBattery, (float) (this.mGap + this.mBatteryDrawable.getIntrinsicWidth() + width), (float) (((this.mHeight - fontMetricsInt.bottom) - fontMetricsInt.top) / 2), this.mTextPaint);
        }
    }

    public void showBattery(int i) {
        this.mBatteryInt = i;
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("%");
        this.mBattery = sb.toString();
        invalidate();
    }
}
