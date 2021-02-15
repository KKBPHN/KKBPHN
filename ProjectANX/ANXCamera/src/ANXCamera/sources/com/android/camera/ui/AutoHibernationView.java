package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.android.camera.ui.drawable.AutoHibernationDrawable;

public class AutoHibernationView extends View {
    private AutoHibernationDrawable mAutoHibernationDrawable;
    private int mHeight;
    private int mWidth;

    public AutoHibernationView(Context context) {
        super(context);
        initView();
    }

    public AutoHibernationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public AutoHibernationView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    private void initView() {
        this.mAutoHibernationDrawable = new AutoHibernationDrawable(getContext());
        this.mAutoHibernationDrawable.setCallback(this);
    }

    public void invalidateDrawable(Drawable drawable) {
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AutoHibernationDrawable autoHibernationDrawable = this.mAutoHibernationDrawable;
        if (autoHibernationDrawable != null) {
            autoHibernationDrawable.cancelAnimation();
            this.mAutoHibernationDrawable.setCallback(null);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        AutoHibernationDrawable autoHibernationDrawable = this.mAutoHibernationDrawable;
        if (autoHibernationDrawable != null) {
            autoHibernationDrawable.draw(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (MeasureSpec.getMode(i) != 1073741824) {
            super.onMeasure(i, i2);
            return;
        }
        this.mWidth = MeasureSpec.getSize(i);
        this.mHeight = MeasureSpec.getSize(i2);
        setMeasuredDimension(this.mWidth, this.mHeight);
        AutoHibernationDrawable autoHibernationDrawable = this.mAutoHibernationDrawable;
        if (autoHibernationDrawable != null) {
            autoHibernationDrawable.setWidthHeight((float) this.mWidth, (float) this.mHeight);
        }
    }

    public void reset() {
        AutoHibernationDrawable autoHibernationDrawable = this.mAutoHibernationDrawable;
        if (autoHibernationDrawable != null) {
            autoHibernationDrawable.reset();
        }
    }

    public void startRecord(long j, float f, boolean z) {
        AutoHibernationDrawable autoHibernationDrawable = this.mAutoHibernationDrawable;
        if (autoHibernationDrawable != null) {
            autoHibernationDrawable.startRecord(j, f, z);
        }
    }

    public void startRecordForFastmotion(int i, float f, boolean z) {
        AutoHibernationDrawable autoHibernationDrawable = this.mAutoHibernationDrawable;
        if (autoHibernationDrawable != null) {
            autoHibernationDrawable.startRecordForFastmotion(i, f, z);
        }
    }
}
