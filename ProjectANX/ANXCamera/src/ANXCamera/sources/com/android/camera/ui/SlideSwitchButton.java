package com.android.camera.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.view.ViewCompat;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.customization.TintColor;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public class SlideSwitchButton extends ViewGroup implements OnClickListener {
    private static final int DISABLE_COLOR = 1308622847;
    public static final int NONE = -1;
    private static final int NORMAL_COLOR = -1;
    /* access modifiers changed from: private */
    public int SELECT_COLOR;
    /* access modifiers changed from: private */
    public ArgbEvaluator mArgbEvaluator;
    private int mBackgroundColor;
    private Paint mBackgroundPaint;
    private float mChildMargin;
    private int mIndicatorColor;
    /* access modifiers changed from: private */
    public Paint mIndicatorPaint;
    private ValueAnimator mIndicatorValueAnimator;
    private boolean mIsRTL;
    private boolean mIsTextView;
    private List mItemDataList;
    private float mItemHeight;
    private float mItemWidth;
    private int mMaxHeight;
    private int mMaxWidth;
    /* access modifiers changed from: private */
    public int mSelectIndex;
    /* access modifiers changed from: private */
    public float mSelectMarginLeft;
    private SlideSwitchListener mSlideSwitchListener;
    private boolean mTextBold;
    private float mTextSize;
    private int mType;

    class ItemData {
        /* access modifiers changed from: private */
        @StringRes
        public int mContentDescriptionRes;
        private int mDisabledColor = SlideSwitchButton.DISABLE_COLOR;
        /* access modifiers changed from: private */
        @StringRes
        public int mDisplayNameRes;
        public String mDisplayNameStr;
        /* access modifiers changed from: private */
        @DrawableRes
        public int mIconRes;
        /* access modifiers changed from: private */
        public boolean mIsDisabled;
        /* access modifiers changed from: private */
        public boolean mIsShowText;
        /* access modifiers changed from: private */
        public int mOriginalSelectColor;
        /* access modifiers changed from: private */
        public String mValue;

        public ItemData() {
        }

        public ItemData(int i, String str, int i2, int i3, boolean z, boolean z2) {
            this.mIconRes = i;
            this.mValue = str;
            this.mDisplayNameRes = i2;
            this.mOriginalSelectColor = i3;
            this.mIsDisabled = z;
            this.mIsShowText = z2;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            ItemData itemData = (ItemData) obj;
            return itemData.mIconRes == this.mIconRes && itemData.mValue.equals(this.mValue) && itemData.mDisplayNameRes == this.mDisplayNameRes && itemData.mOriginalSelectColor == this.mOriginalSelectColor && itemData.mIsShowText == this.mIsShowText;
        }

        public int getContentDescriptionRes() {
            return this.mContentDescriptionRes;
        }

        public int getDisplayNameRes() {
            return this.mDisplayNameRes;
        }

        public int getIconRes() {
            return this.mIconRes;
        }

        public String getValue() {
            return this.mValue;
        }

        public boolean isIsDisabled() {
            return this.mIsDisabled;
        }

        public boolean isIsShowText() {
            return this.mIsShowText;
        }

        public void setContentDescriptionRes(int i) {
            this.mContentDescriptionRes = i;
        }

        public void setDisplayNameRes(int i) {
            this.mDisplayNameRes = i;
        }

        public void setDisplayNameStr(String str) {
            this.mDisplayNameStr = str;
        }

        public void setIconRes(int i) {
            this.mIconRes = i;
        }

        public void setIsDisabled(boolean z) {
            this.mIsDisabled = z;
        }

        public void setIsShowText(boolean z) {
            this.mIsShowText = z;
        }

        public void setOriginalSelectColor(int i) {
            this.mOriginalSelectColor = i;
        }

        public void setValue(String str) {
            this.mValue = str;
        }
    }

    public interface SlideSwitchListener {
        boolean enableSwitch();

        void toSlideSwitch(int i, String str, String str2);
    }

    public SlideSwitchButton(Context context) {
        this(context, null, 0);
    }

    public SlideSwitchButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlideSwitchButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBackgroundColor = 654311423;
        this.mSelectIndex = -1;
        this.mSelectMarginLeft = -1.0f;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.SlideSwitchButton, i, 0);
        this.mChildMargin = (float) obtainStyledAttributes.getDimensionPixelSize(0, 12);
        this.mIndicatorColor = TintColor.tintColor();
        this.SELECT_COLOR = obtainStyledAttributes.getColor(5, ViewCompat.MEASURED_STATE_MASK);
        this.mMaxWidth = obtainStyledAttributes.getDimensionPixelSize(4, Display.getWindowWidth());
        this.mMaxHeight = obtainStyledAttributes.getDimensionPixelSize(3, 160);
        this.mIsTextView = obtainStyledAttributes.getBoolean(2, false);
        this.mTextSize = (float) obtainStyledAttributes.getDimensionPixelSize(7, 48);
        this.mTextBold = obtainStyledAttributes.getBoolean(6, false);
        obtainStyledAttributes.recycle();
        setWillNotDraw(false);
        this.mBackgroundPaint = new Paint(1);
        this.mBackgroundPaint.setColor(this.mBackgroundColor);
        this.mBackgroundPaint.setStyle(Style.FILL);
        this.mIndicatorPaint = new Paint(1);
        this.mIndicatorPaint.setColor(this.mIndicatorColor);
        this.mIndicatorPaint.setStyle(Style.FILL);
        this.mArgbEvaluator = new ArgbEvaluator();
        this.mIsRTL = Util.isLayoutRTL(getContext());
    }

    private void addImageChild(String str, int i, ItemData itemData) {
        ColorImageView colorImageView = new ColorImageView(getContext());
        colorImageView.setImageResource(itemData.mIconRes);
        colorImageView.setTag(itemData.mValue);
        colorImageView.setScaleType(ScaleType.CENTER);
        FolmeUtils.touchTint((View) colorImageView);
        if (str.equals(itemData.mValue)) {
            this.mSelectIndex = i;
            if (itemData.mIsDisabled) {
                this.mIndicatorPaint.setColor(this.mBackgroundColor);
                colorImageView.setColorAndRefresh(DISABLE_COLOR);
            } else if (getSelectColor(i) != this.mIndicatorColor) {
                this.mIndicatorPaint.setColor(getSelectColor(i));
            } else {
                this.mIndicatorPaint.setColor(getSelectColor(i));
                colorImageView.setColorAndRefresh(this.SELECT_COLOR);
            }
            colorImageView.setEnabled(true);
            setAccessible(colorImageView, itemData, str.equals(itemData.mValue));
            colorImageView.setOnClickListener(this);
            addView(colorImageView);
        } else if (itemData.mIsDisabled) {
            colorImageView.setColorAndRefresh(DISABLE_COLOR);
            colorImageView.setEnabled(false);
            setAccessible(colorImageView, itemData, str.equals(itemData.mValue));
            colorImageView.setOnClickListener(this);
            addView(colorImageView);
        }
        colorImageView.setColorAndRefresh(-1);
        colorImageView.setEnabled(true);
        setAccessible(colorImageView, itemData, str.equals(itemData.mValue));
        colorImageView.setOnClickListener(this);
        addView(colorImageView);
    }

    private void addTextChild(String str, int i, ItemData itemData) {
        TextView textView = new TextView(getContext());
        textView.setGravity(17);
        textView.setTextSize(0, this.mTextSize);
        textView.setText(itemData.mDisplayNameRes);
        textView.setTag(itemData.mValue);
        textView.getPaint().setFakeBoldText(this.mTextBold);
        FolmeUtils.touchTint((View) textView);
        if (str.equals(itemData.mValue)) {
            textView.setTextColor(this.SELECT_COLOR);
            this.mSelectIndex = i;
            textView.setEnabled(true);
            this.mIndicatorPaint.setColor(getSelectColor(i));
        } else if (itemData.mIsDisabled) {
            textView.setTextColor(DISABLE_COLOR);
            textView.setEnabled(false);
        } else {
            textView.setTextColor(-1);
            textView.setEnabled(true);
        }
        setAccessible(textView, itemData, str.equals(itemData.mValue));
        textView.setOnClickListener(this);
        addView(textView);
    }

    private int getIndex(String str, List list) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (((ItemData) list.get(i)).mValue.equals(str)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public int getSelectColor(int i) {
        List list = this.mItemDataList;
        if (list == null || list.size() <= i || i < 0) {
            return 0;
        }
        int access$900 = ((ItemData) this.mItemDataList.get(i)).mOriginalSelectColor;
        return access$900 == 0 ? this.mIndicatorColor : access$900;
    }

    private boolean isDataEqual(List list, List list2) {
        return list != null && list2 != null && list.size() == list2.size() && list.containsAll(list2) && list2.containsAll(list);
    }

    private List mapItemData(List list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            ItemData itemData = new ItemData();
            itemData.setIconRes(((ComponentDataItem) list.get(i)).mIconRes);
            itemData.setDisplayNameRes(((ComponentDataItem) list.get(i)).mDisplayNameRes);
            itemData.setDisplayNameStr(((ComponentDataItem) list.get(i)).mDisplayNameStr);
            itemData.setContentDescriptionRes(((ComponentDataItem) list.get(i)).mContentDescriptionRes);
            itemData.setIsDisabled(((ComponentDataItem) list.get(i)).mIsDisabled);
            itemData.setIsShowText(((ComponentDataItem) list.get(i)).mIsShowText);
            itemData.setOriginalSelectColor(((ComponentDataItem) list.get(i)).mSelectColor);
            itemData.setValue(((ComponentDataItem) list.get(i)).mValue);
            arrayList.add(itemData);
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void setAccessible(int i, boolean z) {
        List list = this.mItemDataList;
        if (list != null && list.size() > i && i >= 0) {
            setAccessible(getChildAt(i), (ItemData) this.mItemDataList.get(i), z);
        }
    }

    private void setAccessible(View view, ItemData itemData, boolean z) {
        StringBuilder sb = new StringBuilder();
        String string = TextUtils.isEmpty(itemData.mDisplayNameStr) ? getResources().getString(itemData.mDisplayNameRes) : itemData.mDisplayNameStr;
        if (itemData.mContentDescriptionRes > 0) {
            string = getResources().getString(itemData.mContentDescriptionRes);
        }
        sb.append(string);
        if (z) {
            sb.append(", ");
            sb.append(getResources().getString(R.string.accessibility_selected));
        }
        view.setContentDescription(sb.toString());
    }

    /* access modifiers changed from: private */
    public void setChildColor(int i, int i2) {
        if (getChildAt(i) instanceof TextView) {
            ((TextView) getChildAt(i)).setTextColor(i2);
        } else if (getSelectColor(i) == this.mIndicatorColor) {
            ((ColorImageView) getChildAt(i)).setColorAndRefresh(i2);
        }
    }

    /* access modifiers changed from: private */
    public void setIndex(int i) {
        setChildColor(i, this.SELECT_COLOR);
        int i2 = this.mSelectIndex;
        if (i2 != -1) {
            setChildColor(i2, -1);
            setAccessible(this.mSelectIndex, false);
        }
        float paddingLeft = (float) getPaddingLeft();
        float f = this.mChildMargin;
        int i3 = (int) (paddingLeft + f + ((this.mItemWidth + f + f) * ((float) i)));
        this.mIndicatorPaint.setColor(getSelectColor(i));
        this.mSelectIndex = i;
        setAccessible(i, true);
        this.mSelectMarginLeft = (float) i3;
        invalidate();
    }

    private void setIndex(final int i, boolean z) {
        if (z) {
            ValueAnimator valueAnimator = this.mIndicatorValueAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mIndicatorValueAnimator.cancel();
            }
        }
        if (this.mSelectIndex != i) {
            float paddingLeft = (float) getPaddingLeft();
            float f = this.mChildMargin;
            final int i2 = (int) (paddingLeft + f + ((this.mItemWidth + f + f) * ((float) i)));
            if (z) {
                final float f2 = this.mSelectMarginLeft;
                this.mIndicatorValueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                this.mIndicatorValueAnimator.setDuration(300);
                this.mIndicatorValueAnimator.setInterpolator(new CubicEaseOutInterpolator() {
                    public float getInterpolation(float f) {
                        float interpolation = super.getInterpolation(f);
                        SlideSwitchButton slideSwitchButton = SlideSwitchButton.this;
                        float f2 = f2;
                        slideSwitchButton.mSelectMarginLeft = f2 + ((((float) i2) - f2) * interpolation);
                        Paint access$400 = SlideSwitchButton.this.mIndicatorPaint;
                        ArgbEvaluator access$300 = SlideSwitchButton.this.mArgbEvaluator;
                        SlideSwitchButton slideSwitchButton2 = SlideSwitchButton.this;
                        access$400.setColor(((Integer) access$300.evaluate(interpolation, Integer.valueOf(slideSwitchButton2.getSelectColor(slideSwitchButton2.mSelectIndex)), Integer.valueOf(SlideSwitchButton.this.getSelectColor(i)))).intValue());
                        SlideSwitchButton slideSwitchButton3 = SlideSwitchButton.this;
                        int i = i;
                        ArgbEvaluator access$3002 = slideSwitchButton3.mArgbEvaluator;
                        Integer valueOf = Integer.valueOf(-1);
                        slideSwitchButton3.setChildColor(i, ((Integer) access$3002.evaluate(interpolation, valueOf, Integer.valueOf(SlideSwitchButton.this.SELECT_COLOR))).intValue());
                        if (SlideSwitchButton.this.mSelectIndex != -1) {
                            SlideSwitchButton slideSwitchButton4 = SlideSwitchButton.this;
                            slideSwitchButton4.setChildColor(slideSwitchButton4.mSelectIndex, ((Integer) SlideSwitchButton.this.mArgbEvaluator.evaluate(interpolation, Integer.valueOf(SlideSwitchButton.this.SELECT_COLOR), valueOf)).intValue());
                            SlideSwitchButton slideSwitchButton5 = SlideSwitchButton.this;
                            slideSwitchButton5.setAccessible(slideSwitchButton5.mSelectIndex, false);
                        }
                        SlideSwitchButton.this.invalidate();
                        return interpolation;
                    }
                });
                this.mIndicatorValueAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationCancel(Animator animator) {
                        super.onAnimationCancel(animator);
                        SlideSwitchButton.this.setIndex(i);
                    }

                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        SlideSwitchButton.this.mSelectIndex = i;
                        SlideSwitchButton.this.setAccessible(i, true);
                        if (Util.isAccessible() && !DataRepository.dataItemRunning().isSwitchOn("pref_speech_shutter")) {
                            SlideSwitchButton.this.getChildAt(i).sendAccessibilityEvent(128);
                        }
                    }
                });
                this.mIndicatorValueAnimator.start();
            } else {
                setIndex(i);
            }
            SlideSwitchListener slideSwitchListener = this.mSlideSwitchListener;
            int intValue = ((Integer) getTag()).intValue();
            String str = (String) getChildAt(i).getTag();
            StringBuilder sb = new StringBuilder();
            sb.append(getChildAt(i).getContentDescription());
            sb.append(", ");
            sb.append(getResources().getString(R.string.accessibility_selected));
            slideSwitchListener.toSlideSwitch(intValue, str, sb.toString());
        }
    }

    private void setItemColor(View view, int i) {
        if (view instanceof ColorImageView) {
            ColorImageView colorImageView = (ColorImageView) view;
            if (colorImageView.getColor() != i) {
                colorImageView.setColorAndRefresh(i);
            }
        } else if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (textView.getCurrentTextColor() != i) {
                textView.setTextColor(i);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0048 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateViewsStatus(List list) {
        int i;
        for (int i2 = 0; i2 < list.size(); i2++) {
            View childAt = getChildAt(i2);
            if (((ItemData) list.get(i2)).mIsDisabled) {
                childAt.setEnabled(false);
                i = DISABLE_COLOR;
            } else if (this.mSelectIndex == i2) {
                this.mIndicatorPaint.setColor(getSelectColor(i2));
                childAt.setEnabled(true);
                i = this.SELECT_COLOR;
            } else {
                childAt.setEnabled(true);
                setItemColor(childAt, -1);
                if (getSelectColor(i2) == this.mIndicatorColor) {
                    setItemColor(childAt, -1);
                }
            }
            setItemColor(childAt, i);
            if (getSelectColor(i2) == this.mIndicatorColor) {
            }
        }
    }

    public int getIndicatorCount() {
        return getChildCount();
    }

    public int getType() {
        return this.mType;
    }

    public void onClick(View view) {
        if (isEnabled()) {
            SlideSwitchListener slideSwitchListener = this.mSlideSwitchListener;
            if (slideSwitchListener == null || slideSwitchListener.enableSwitch()) {
                int i = 0;
                int i2 = 0;
                while (true) {
                    if (getChildCount() <= 0) {
                        break;
                    } else if (!view.getTag().equals(getChildAt(i2).getTag())) {
                        i2++;
                    } else if (i2 == this.mSelectIndex) {
                        if (Util.isAccessible()) {
                            view.sendAccessibilityEvent(32768);
                        }
                        return;
                    } else {
                        i = i2;
                    }
                }
                setIndex(i, true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mSelectMarginLeft == -1.0f) {
            float paddingLeft = (float) getPaddingLeft();
            float f = this.mChildMargin;
            this.mSelectMarginLeft = (float) ((int) (paddingLeft + f + ((this.mItemWidth + f + f) * ((float) this.mSelectIndex))));
        }
        canvas.drawRoundRect(0.0f, 0.0f, (float) (getWidth() + 0), (float) (getHeight() + 0), (float) (getHeight() / 2), (float) (getHeight() / 2), this.mBackgroundPaint);
        float f2 = this.mSelectMarginLeft;
        float paddingTop = ((float) getPaddingTop()) + this.mChildMargin;
        float f3 = this.mSelectMarginLeft + this.mItemWidth;
        float paddingTop2 = ((float) getPaddingTop()) + this.mChildMargin;
        float f4 = this.mItemHeight;
        canvas.drawRoundRect(f2, paddingTop, f3, paddingTop2 + f4, f4 / 2.0f, f4 / 2.0f, this.mIndicatorPaint);
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int paddingLeft = (int) (((float) getPaddingLeft()) + this.mChildMargin);
        int paddingTop = (int) (((float) getPaddingTop()) + this.mChildMargin);
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            float f = (float) paddingLeft;
            getChildAt(i5).layout(paddingLeft, paddingTop, (int) (this.mItemWidth + f), (int) (((float) paddingTop) + this.mItemHeight));
            float f2 = this.mChildMargin;
            paddingLeft = (int) (f + this.mItemWidth + f2 + f2);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(ViewGroup.resolveSizeAndState(this.mMaxWidth, i, 0), ViewGroup.resolveSizeAndState(this.mMaxHeight, i2, 0));
        this.mItemWidth = (((float) ((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight())) - ((this.mChildMargin * 2.0f) * ((float) getChildCount()))) / ((float) getChildCount());
        this.mItemHeight = ((float) ((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom())) - (this.mChildMargin * 2.0f);
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            getChildAt(i3).measure(MeasureSpec.makeMeasureSpec((int) this.mItemWidth, 1073741824), MeasureSpec.makeMeasureSpec((int) this.mItemHeight, 1073741824));
        }
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
        this.mBackgroundPaint.setColor(i);
        invalidate();
    }

    public void setComponentData(ComponentData componentData, int i, boolean z) {
        List list;
        String str = null;
        try {
            list = mapItemData(componentData.getItems());
            try {
                if (this.mIsRTL) {
                    Collections.reverse(list);
                }
                str = componentData.getComponentValue(DataRepository.dataItemGlobal().getCurrentMode());
                if (isDataEqual(list, this.mItemDataList) && this.mIsTextView == z) {
                    int index = getIndex(str, list);
                    if ((this.mIndicatorValueAnimator == null || !this.mIndicatorValueAnimator.isRunning()) && index != this.mSelectIndex) {
                        setIndex(index);
                    }
                    updateViewsStatus(list);
                    return;
                }
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            list = null;
        }
        this.mItemDataList = list;
        this.mIsTextView = z;
        removeAllViews();
        setTag(Integer.valueOf(i));
        this.mSelectMarginLeft = -1.0f;
        for (int i2 = 0; i2 < this.mItemDataList.size(); i2++) {
            ItemData itemData = (ItemData) this.mItemDataList.get(i2);
            if (!this.mIsTextView && !itemData.mIsShowText) {
                addImageChild(str, i2, itemData);
            } else {
                addTextChild(str, i2, itemData);
            }
        }
    }

    public void setIndicatorColor(int i) {
        this.mIndicatorColor = i;
        this.mIndicatorPaint.setColor(this.mIndicatorColor);
        invalidate();
    }

    public void setSlideSwitchListener(SlideSwitchListener slideSwitchListener) {
        this.mSlideSwitchListener = slideSwitchListener;
    }

    public void setType(int i) {
        this.mType = i;
    }
}
