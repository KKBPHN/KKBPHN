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
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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

public class TopAlertSlideSwitchButton extends LinearLayout implements OnClickListener {
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
    public float mSelectChildWidth;
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
        private int mDisabledColor = TopAlertSlideSwitchButton.DISABLE_COLOR;
        /* access modifiers changed from: private */
        @StringRes
        public int mDisplayNameRes;
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

        void toSlideSwitch(int i, String str);
    }

    public TopAlertSlideSwitchButton(Context context) {
        this(context, null, 0);
    }

    public TopAlertSlideSwitchButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TopAlertSlideSwitchButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBackgroundColor = 855638016;
        this.mSelectIndex = 0;
        this.mSelectMarginLeft = -1.0f;
        this.mSelectChildWidth = -1.0f;
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
            this.mIndicatorPaint.setColor(getSelectColor(i));
            if (getSelectColor(i) == this.mIndicatorColor) {
                colorImageView.setColorAndRefresh(this.SELECT_COLOR);
                colorImageView.setEnabled(true);
                setAccessible(colorImageView, itemData, str.equals(itemData.mValue));
                colorImageView.setOnClickListener(this);
                addView(colorImageView);
                LayoutParams layoutParams = (LayoutParams) colorImageView.getLayoutParams();
                layoutParams.width = getResources().getDimensionPixelSize(R.dimen.slide_switch_button_item_image_max_width);
                layoutParams.height = getResources().getDimensionPixelSize(R.dimen.slide_switch_button_item_image_max_height);
                float f = this.mChildMargin;
                layoutParams.setMargins((int) f, (int) f, (int) f, (int) f);
                colorImageView.setLayoutParams(layoutParams);
            }
        } else if (itemData.mIsDisabled) {
            colorImageView.setColorAndRefresh(DISABLE_COLOR);
            colorImageView.setEnabled(false);
            setAccessible(colorImageView, itemData, str.equals(itemData.mValue));
            colorImageView.setOnClickListener(this);
            addView(colorImageView);
            LayoutParams layoutParams2 = (LayoutParams) colorImageView.getLayoutParams();
            layoutParams2.width = getResources().getDimensionPixelSize(R.dimen.slide_switch_button_item_image_max_width);
            layoutParams2.height = getResources().getDimensionPixelSize(R.dimen.slide_switch_button_item_image_max_height);
            float f2 = this.mChildMargin;
            layoutParams2.setMargins((int) f2, (int) f2, (int) f2, (int) f2);
            colorImageView.setLayoutParams(layoutParams2);
        }
        colorImageView.setColorAndRefresh(-1);
        colorImageView.setEnabled(true);
        setAccessible(colorImageView, itemData, str.equals(itemData.mValue));
        colorImageView.setOnClickListener(this);
        addView(colorImageView);
        LayoutParams layoutParams22 = (LayoutParams) colorImageView.getLayoutParams();
        layoutParams22.width = getResources().getDimensionPixelSize(R.dimen.slide_switch_button_item_image_max_width);
        layoutParams22.height = getResources().getDimensionPixelSize(R.dimen.slide_switch_button_item_image_max_height);
        float f22 = this.mChildMargin;
        layoutParams22.setMargins((int) f22, (int) f22, (int) f22, (int) f22);
        colorImageView.setLayoutParams(layoutParams22);
    }

    private void addTextChild(String str, int i, ItemData itemData) {
        AdaptiveTextView adaptiveTextView = new AdaptiveTextView(getContext());
        adaptiveTextView.setGravity(17);
        adaptiveTextView.setMaxWidth(getResources().getDimensionPixelSize(R.dimen.slide_switch_button_item_max_width));
        adaptiveTextView.setEllipsize(TruncateAt.MARQUEE);
        adaptiveTextView.setMarqueeRepeatLimit(-1);
        adaptiveTextView.setSingleLine();
        adaptiveTextView.setFocusable(true);
        adaptiveTextView.setFocusableInTouchMode(true);
        adaptiveTextView.setHorizontallyScrolling(true);
        adaptiveTextView.setClickable(true);
        adaptiveTextView.setTextSize(0, this.mTextSize);
        adaptiveTextView.setText(itemData.mDisplayNameRes);
        adaptiveTextView.setTag(itemData.mValue);
        adaptiveTextView.getPaint().setFakeBoldText(this.mTextBold);
        adaptiveTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.slide_switch_button_item_text_padding), 0, getResources().getDimensionPixelSize(R.dimen.slide_switch_button_item_text_padding), 0);
        FolmeUtils.touchTint((View) adaptiveTextView);
        if (str.equals(itemData.mValue)) {
            adaptiveTextView.setTextColor(this.SELECT_COLOR);
            this.mSelectIndex = i;
        } else if (itemData.mIsDisabled) {
            adaptiveTextView.setTextColor(DISABLE_COLOR);
            adaptiveTextView.setEnabled(false);
            setAccessible(adaptiveTextView, itemData, str.equals(itemData.mValue));
            adaptiveTextView.setOnClickListener(this);
            addView(adaptiveTextView);
            LayoutParams layoutParams = (LayoutParams) adaptiveTextView.getLayoutParams();
            layoutParams.width = -2;
            layoutParams.height = -1;
            float f = this.mChildMargin;
            layoutParams.setMargins((int) f, (int) f, (int) f, (int) f);
            adaptiveTextView.setLayoutParams(layoutParams);
        } else {
            adaptiveTextView.setTextColor(-1);
        }
        adaptiveTextView.setEnabled(true);
        setAccessible(adaptiveTextView, itemData, str.equals(itemData.mValue));
        adaptiveTextView.setOnClickListener(this);
        addView(adaptiveTextView);
        LayoutParams layoutParams2 = (LayoutParams) adaptiveTextView.getLayoutParams();
        layoutParams2.width = -2;
        layoutParams2.height = -1;
        float f2 = this.mChildMargin;
        layoutParams2.setMargins((int) f2, (int) f2, (int) f2, (int) f2);
        adaptiveTextView.setLayoutParams(layoutParams2);
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
        int access$1000 = ((ItemData) this.mItemDataList.get(i)).mOriginalSelectColor;
        return access$1000 == 0 ? this.mIndicatorColor : access$1000;
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
        sb.append(getResources().getString(itemData.mContentDescriptionRes > 0 ? itemData.mContentDescriptionRes : itemData.mDisplayNameRes));
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
        int left = getChildAt(i).getLeft();
        int width = getChildAt(i).getWidth();
        this.mIndicatorPaint.setColor(getSelectColor(i));
        this.mSelectIndex = i;
        setAccessible(i, true);
        this.mSelectMarginLeft = (float) left;
        this.mSelectChildWidth = (float) width;
        invalidate();
    }

    private void setIndex(final int i, boolean z) {
        if (this.mSelectIndex != i) {
            final int left = getChildAt(i).getLeft();
            final int width = getChildAt(i).getWidth();
            if (z) {
                ValueAnimator valueAnimator = this.mIndicatorValueAnimator;
                if (valueAnimator != null && valueAnimator.isRunning()) {
                    this.mIndicatorValueAnimator.cancel();
                }
                final float f = this.mSelectMarginLeft;
                final float f2 = this.mSelectChildWidth;
                this.mIndicatorValueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                this.mIndicatorValueAnimator.setDuration(300);
                ValueAnimator valueAnimator2 = this.mIndicatorValueAnimator;
                final int i2 = i;
                AnonymousClass1 r1 = new CubicEaseOutInterpolator() {
                    public float getInterpolation(float f) {
                        float interpolation = super.getInterpolation(f);
                        TopAlertSlideSwitchButton topAlertSlideSwitchButton = TopAlertSlideSwitchButton.this;
                        float f2 = f;
                        topAlertSlideSwitchButton.mSelectMarginLeft = f2 + ((((float) left) - f2) * interpolation);
                        TopAlertSlideSwitchButton topAlertSlideSwitchButton2 = TopAlertSlideSwitchButton.this;
                        float f3 = f2;
                        topAlertSlideSwitchButton2.mSelectChildWidth = f3 + ((((float) width) - f3) * interpolation);
                        Paint access$500 = TopAlertSlideSwitchButton.this.mIndicatorPaint;
                        ArgbEvaluator access$400 = TopAlertSlideSwitchButton.this.mArgbEvaluator;
                        TopAlertSlideSwitchButton topAlertSlideSwitchButton3 = TopAlertSlideSwitchButton.this;
                        access$500.setColor(((Integer) access$400.evaluate(interpolation, Integer.valueOf(topAlertSlideSwitchButton3.getSelectColor(topAlertSlideSwitchButton3.mSelectIndex)), Integer.valueOf(TopAlertSlideSwitchButton.this.getSelectColor(i2)))).intValue());
                        TopAlertSlideSwitchButton topAlertSlideSwitchButton4 = TopAlertSlideSwitchButton.this;
                        int i = i2;
                        ArgbEvaluator access$4002 = topAlertSlideSwitchButton4.mArgbEvaluator;
                        Integer valueOf = Integer.valueOf(-1);
                        topAlertSlideSwitchButton4.setChildColor(i, ((Integer) access$4002.evaluate(interpolation, valueOf, Integer.valueOf(TopAlertSlideSwitchButton.this.SELECT_COLOR))).intValue());
                        if (TopAlertSlideSwitchButton.this.mSelectIndex != -1) {
                            TopAlertSlideSwitchButton topAlertSlideSwitchButton5 = TopAlertSlideSwitchButton.this;
                            topAlertSlideSwitchButton5.setChildColor(topAlertSlideSwitchButton5.mSelectIndex, ((Integer) TopAlertSlideSwitchButton.this.mArgbEvaluator.evaluate(interpolation, Integer.valueOf(TopAlertSlideSwitchButton.this.SELECT_COLOR), valueOf)).intValue());
                            TopAlertSlideSwitchButton topAlertSlideSwitchButton6 = TopAlertSlideSwitchButton.this;
                            topAlertSlideSwitchButton6.setAccessible(topAlertSlideSwitchButton6.mSelectIndex, false);
                        }
                        TopAlertSlideSwitchButton.this.invalidate();
                        return interpolation;
                    }
                };
                valueAnimator2.setInterpolator(r1);
                this.mIndicatorValueAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationCancel(Animator animator) {
                        super.onAnimationCancel(animator);
                        TopAlertSlideSwitchButton.this.setEnabled(true);
                        TopAlertSlideSwitchButton.this.setIndex(i);
                    }

                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        TopAlertSlideSwitchButton.this.setEnabled(true);
                        TopAlertSlideSwitchButton.this.mSelectIndex = i;
                        TopAlertSlideSwitchButton.this.setAccessible(i, true);
                    }
                });
                this.mIndicatorValueAnimator.start();
            } else {
                setEnabled(true);
                setIndex(i);
            }
            this.mSlideSwitchListener.toSlideSwitch(((Integer) getTag()).intValue(), (String) getChildAt(i).getTag());
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

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003c A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateViewsStatus(List list) {
        int i;
        for (int i2 = 0; i2 < list.size(); i2++) {
            View childAt = getChildAt(i2);
            if (((ItemData) list.get(i2)).mIsDisabled) {
                childAt.setEnabled(false);
                i = DISABLE_COLOR;
            } else {
                int i3 = this.mSelectIndex;
                childAt.setEnabled(true);
                if (i3 == i2) {
                    i = this.SELECT_COLOR;
                } else {
                    setItemColor(childAt, -1);
                    if (getSelectColor(i2) == this.mIndicatorColor) {
                        setItemColor(childAt, -1);
                    }
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
                while (true) {
                    if (getChildCount() <= 0) {
                        i = 0;
                        break;
                    } else if (!view.getTag().equals(getChildAt(i).getTag())) {
                        i++;
                    } else if (i == this.mSelectIndex) {
                        if (Util.isAccessible()) {
                            view.sendAccessibilityEvent(32768);
                        }
                        return;
                    }
                }
                setEnabled(false);
                setIndex(i, true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mSelectMarginLeft == -1.0f) {
            this.mSelectMarginLeft = (float) getChildAt(this.mSelectIndex).getLeft();
        }
        if (this.mSelectChildWidth == -1.0f) {
            this.mSelectChildWidth = (float) getChildAt(this.mSelectIndex).getWidth();
        }
        canvas.drawRoundRect(0.0f, 0.0f, (float) (getWidth() + 0), (float) (getHeight() + 0), (float) (getHeight() / 2), (float) (getHeight() / 2), this.mBackgroundPaint);
        for (int i = 0; i < getChildCount(); i++) {
            if (i == this.mSelectIndex) {
                canvas.drawRoundRect(this.mSelectMarginLeft, (float) getChildAt(i).getTop(), this.mSelectMarginLeft + this.mSelectChildWidth, (float) getChildAt(i).getBottom(), (float) (getHeight() / 2), (float) (getHeight() / 2), this.mIndicatorPaint);
            }
        }
        super.onDraw(canvas);
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
        this.mBackgroundPaint.setColor(i);
        invalidate();
    }

    public void setComponentData(ComponentData componentData, int i, boolean z) {
        List list;
        this.mSelectMarginLeft = -1.0f;
        this.mSelectChildWidth = -1.0f;
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
