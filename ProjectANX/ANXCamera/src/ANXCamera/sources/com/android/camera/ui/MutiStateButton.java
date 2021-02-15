package com.android.camera.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.android.camera.R;
import com.android.camera.Util;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class MutiStateButton extends LinearLayout {
    private static final int DEFAULT_ANIMATION_DURATION = 200;
    private static final int DEFAULT_TEXT_SIZE = 30;
    private RectF mAniBGRectF = null;
    private int mAnimDuration;
    private boolean mAnimation;
    private ValueAnimator mAnimator;
    private int mBGColor;
    private Paint mBGPaint;
    private int mCurrentIndex = -1;
    private TextView mCurrentView = null;
    private int mItemLeftRightMargin;
    private int mItemLeftRightPaddingOther;
    private int mItemLeftRightPaddingZH;
    private LinkedList mItemLists = new LinkedList();
    private int mItemTopBottomMargin;
    private int mItemTopBottomPaddingOther;
    private int mItemTopBottomPaddingZH;
    private ArrayList mItems = new ArrayList();
    private float mRadius;
    private int mTextColor;
    private float mTextSize;

    public MutiStateButton(Context context) {
        super(context);
    }

    public MutiStateButton(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        dealWithAttributeSet(attributeSet);
    }

    public MutiStateButton(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    private void dealWithAttributeSet(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.MutiStateButton);
        this.mItemTopBottomPaddingZH = obtainStyledAttributes.getDimensionPixelSize(8, 0);
        this.mItemLeftRightPaddingZH = obtainStyledAttributes.getDimensionPixelSize(5, 0);
        this.mItemTopBottomPaddingOther = obtainStyledAttributes.getDimensionPixelSize(7, 0);
        this.mItemLeftRightPaddingOther = obtainStyledAttributes.getDimensionPixelSize(4, 0);
        this.mItemTopBottomMargin = obtainStyledAttributes.getDimensionPixelSize(6, 0);
        this.mItemLeftRightMargin = obtainStyledAttributes.getDimensionPixelSize(3, 0);
        this.mAnimDuration = obtainStyledAttributes.getInteger(2, 200);
        this.mRadius = (float) obtainStyledAttributes.getDimensionPixelSize(1, 0);
        this.mBGColor = obtainStyledAttributes.getColor(0, 0);
        this.mTextColor = obtainStyledAttributes.getColor(9, -1);
        this.mTextSize = (float) obtainStyledAttributes.getDimensionPixelSize(10, 30);
        obtainStyledAttributes.recycle();
    }

    private RectF getBGRectF() {
        RectF rectF = new RectF();
        rectF.left = (float) this.mCurrentView.getLeft();
        rectF.top = (float) this.mCurrentView.getTop();
        rectF.right = (float) this.mCurrentView.getRight();
        rectF.bottom = (float) this.mCurrentView.getBottom();
        return rectF;
    }

    private MarginLayoutParams getItemMarginLayoutParams(View view, boolean z, boolean z2) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        int i = this.mItemTopBottomMargin;
        marginLayoutParams.topMargin = i;
        marginLayoutParams.bottomMargin = i;
        int i2 = this.mItemLeftRightMargin;
        if (z) {
            marginLayoutParams.rightMargin = i2;
            if (!z2) {
                i2 = 0;
            }
            marginLayoutParams.leftMargin = i2;
        } else {
            marginLayoutParams.leftMargin = i2;
            if (!z2) {
                i2 = 0;
            }
            marginLayoutParams.rightMargin = i2;
        }
        return marginLayoutParams;
    }

    private void startSwtichAnimation(TextView textView, int i) {
        if (this.mAnimator == null) {
            this.mAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        }
        if (this.mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (this.mAniBGRectF == null) {
            this.mAniBGRectF = new RectF();
        }
        this.mAniBGRectF.left = (float) textView.getLeft();
        this.mAniBGRectF.top = (float) textView.getTop();
        this.mAniBGRectF.right = (float) textView.getRight();
        this.mAniBGRectF.bottom = (float) textView.getBottom();
        float left = (float) textView.getLeft();
        float right = (float) textView.getRight();
        float left2 = ((float) this.mCurrentView.getLeft()) - left;
        float right2 = ((float) this.mCurrentView.getRight()) - right;
        this.mAnimator.setDuration(i > 1 ? (long) (((double) this.mAnimDuration) * 1.5d) : (long) this.mAnimDuration);
        ValueAnimator valueAnimator = this.mAnimator;
        O0000O0o o0000O0o = new O0000O0o(this, left, left2, right, right2);
        valueAnimator.addUpdateListener(o0000O0o);
        this.mAnimator.start();
    }

    public /* synthetic */ void O000000o(float f, float f2, float f3, float f4, ValueAnimator valueAnimator) {
        this.mAniBGRectF.left = f + (((Float) valueAnimator.getAnimatedValue()).floatValue() * f2);
        this.mAniBGRectF.right = f3 + (((Float) valueAnimator.getAnimatedValue()).floatValue() * f4);
        invalidate();
    }

    public void initItems(LinkedHashMap linkedHashMap, OnClickListener onClickListener) {
        int i;
        int i2;
        boolean isLocaleChinese = Util.isLocaleChinese();
        boolean isLayoutRTL = Util.isLayoutRTL(getContext());
        this.mBGPaint = new Paint();
        this.mBGPaint.setColor(this.mBGColor);
        this.mBGPaint.setAntiAlias(true);
        for (Entry entry : linkedHashMap.entrySet()) {
            TextView textView = new TextView(getContext());
            textView.setTag(entry.getKey());
            textView.setText(((Integer) entry.getValue()).intValue());
            textView.setTextColor(this.mTextColor);
            textView.setGravity(17);
            if (isLocaleChinese) {
                i2 = this.mItemLeftRightPaddingZH;
                i = this.mItemTopBottomPaddingZH;
            } else {
                i2 = this.mItemLeftRightPaddingOther;
                i = this.mItemTopBottomPaddingOther;
            }
            textView.setPadding(i2, i, i2, i);
            boolean z = false;
            textView.setTextSize(0, this.mTextSize);
            textView.setOnClickListener(onClickListener);
            addView(textView);
            this.mItems.add(textView);
            this.mItemLists.add((String) entry.getKey());
            if (this.mItems.size() == linkedHashMap.size()) {
                z = true;
            }
            textView.setLayoutParams(getItemMarginLayoutParams(textView, isLayoutRTL, z));
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x000e, code lost:
        if (r0 != null) goto L_0x0015;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDraw(Canvas canvas) {
        RectF rectF;
        super.onDraw(canvas);
        if (this.mCurrentView != null) {
            if (this.mAnimation) {
                rectF = this.mAniBGRectF;
            }
            rectF = getBGRectF();
            float f = this.mRadius;
            canvas.drawRoundRect(rectF, f, f, this.mBGPaint);
        }
    }

    public void updateCurrentIndex(String str, boolean z) {
        int indexOf = this.mItemLists.indexOf(str);
        int i = this.mCurrentIndex;
        if (i != indexOf) {
            int abs = Math.abs(i - indexOf);
            TextView textView = this.mCurrentView;
            this.mCurrentIndex = indexOf;
            this.mCurrentView = (TextView) this.mItems.get(this.mCurrentIndex);
            this.mAnimation = z;
            if (z) {
                startSwtichAnimation(textView, abs);
            } else {
                invalidate();
            }
        }
    }
}
