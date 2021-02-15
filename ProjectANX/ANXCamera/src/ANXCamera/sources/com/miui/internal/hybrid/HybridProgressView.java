package com.miui.internal.hybrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.miui.internal.R;

public class HybridProgressView extends ImageView {
    private static final int DELAY = 40;
    private static final int MAX_CUR_PROGRESS = 9500;
    private static final int MAX_PROGRESS = 10000;
    private static final int MIN_CUR_PROGRESS = 800;
    private static final int MSG_UPDATE = 42;
    private static final int STEPS = 10;
    /* access modifiers changed from: private */
    public Rect mBounds;
    private Context mContext;
    /* access modifiers changed from: private */
    public int mCurrentProgress;
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public int mIncrement;
    private Rect mReverseBounds;
    private Drawable mReverseDrawable;
    /* access modifiers changed from: private */
    public int mTargetProgress;

    public HybridProgressView(Context context) {
        super(context);
        init(context);
    }

    public HybridProgressView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public HybridProgressView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mBounds = new Rect(0, 0, 0, 0);
        this.mCurrentProgress = 0;
        this.mTargetProgress = 0;
        this.mReverseDrawable = this.mContext.getResources().getDrawable(R.drawable.hybrid_progress_reverse);
        this.mHandler = new Handler() {
            public void handleMessage(Message message) {
                HybridProgressView hybridProgressView;
                int access$000;
                if (message.what == 42) {
                    if (HybridProgressView.this.mCurrentProgress < HybridProgressView.this.mTargetProgress) {
                        hybridProgressView = HybridProgressView.this;
                        access$000 = Math.min(hybridProgressView.mTargetProgress, HybridProgressView.this.mCurrentProgress + HybridProgressView.this.mIncrement);
                    } else if (HybridProgressView.this.mCurrentProgress <= HybridProgressView.MAX_CUR_PROGRESS && HybridProgressView.this.mCurrentProgress >= 800) {
                        hybridProgressView = HybridProgressView.this;
                        access$000 = hybridProgressView.mCurrentProgress + 30;
                    } else {
                        return;
                    }
                    hybridProgressView.mCurrentProgress = access$000;
                    HybridProgressView.this.mBounds.right = (HybridProgressView.this.getWidth() * HybridProgressView.this.mCurrentProgress) / 10000;
                    HybridProgressView.this.invalidate();
                    sendMessageDelayed(HybridProgressView.this.mHandler.obtainMessage(42), 40);
                }
            }
        };
        this.mReverseBounds = new Rect(0, 0, 0, 0);
    }

    public int getMax() {
        return 100;
    }

    public void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        drawable.setBounds(this.mBounds);
        drawable.draw(canvas);
        float width = ((float) getWidth()) - ((((float) getWidth()) * ((float) this.mCurrentProgress)) / 10000.0f);
        canvas.save();
        canvas.translate(-width, 0.0f);
        this.mReverseBounds.set(0, 0, getWidth(), getHeight());
        this.mReverseDrawable.setBounds(this.mReverseBounds);
        this.mReverseDrawable.draw(canvas);
        canvas.translate(width, 0.0f);
        canvas.restore();
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        Rect rect = this.mBounds;
        rect.left = 0;
        rect.right = ((i3 - i) * this.mCurrentProgress) / 10000;
        rect.top = 0;
        rect.bottom = i4 - i2;
    }

    public void setProgress(int i) {
        int i2 = i * 100;
        int i3 = this.mTargetProgress;
        if (i3 <= 800) {
            this.mCurrentProgress = i3;
        }
        this.mTargetProgress = i2;
        this.mIncrement = (this.mTargetProgress - this.mCurrentProgress) / 10;
        this.mHandler.removeMessages(42);
        this.mHandler.sendEmptyMessage(42);
    }
}
