package com.miui.internal.view;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.miui.internal.R;
import com.miui.internal.view.CheckWidgetAnimatedStateListDrawable.CheckWidgetConstantState;

public class CheckBoxAnimatedStateListDrawable extends CheckWidgetAnimatedStateListDrawable {
    protected static final int FULL_ALPHA = 255;
    protected static final int HALF_ALPHA = 127;
    private CheckWidgetDrawableAnims mCheckWidgetDrawableAnims;
    private float mContentAlpha = 1.0f;
    private boolean mIsEnabled;
    private boolean mPreChecked = false;
    private boolean mPrePressed = false;
    private float mScale = 1.0f;

    public class CheckBoxConstantState extends CheckWidgetConstantState {
        protected CheckBoxConstantState() {
        }

        /* access modifiers changed from: protected */
        public Drawable newAnimatedStateListDrawable(Resources resources, Theme theme, CheckWidgetConstantState checkWidgetConstantState) {
            return new CheckBoxAnimatedStateListDrawable(resources, theme, checkWidgetConstantState);
        }
    }

    public CheckBoxAnimatedStateListDrawable() {
    }

    public CheckBoxAnimatedStateListDrawable(Resources resources, Theme theme, CheckWidgetConstantState checkWidgetConstantState) {
        super(resources, theme, checkWidgetConstantState);
        CheckWidgetDrawableAnims checkWidgetDrawableAnims = new CheckWidgetDrawableAnims(this, isSingleSelectionWidget(), checkWidgetConstantState.grayColor, checkWidgetConstantState.blackColor, checkWidgetConstantState.backGroundColor, checkWidgetConstantState.backgroundNormalAlpha, checkWidgetConstantState.backgroundDisableAlpha, checkWidgetConstantState.strokeColor, checkWidgetConstantState.strokeNormalAlpha, checkWidgetConstantState.strokeDisableAlpha);
        this.mCheckWidgetDrawableAnims = checkWidgetDrawableAnims;
    }

    public void applyTheme(Theme theme) {
        super.applyTheme(theme);
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(getCheckWidgetDrawableStyle(), R.styleable.CheckWidgetDrawable);
        this.mCheckWidgetConstantState.grayColor = obtainStyledAttributes.getColor(R.styleable.CheckWidgetDrawable_checkwidget_disableBackgroundColor, 0);
        this.mCheckWidgetConstantState.blackColor = obtainStyledAttributes.getColor(R.styleable.CheckWidgetDrawable_checkwidget_checkOnAlphaBackgroundColor, 0);
        this.mCheckWidgetConstantState.backGroundColor = obtainStyledAttributes.getColor(R.styleable.CheckWidgetDrawable_checkwidget_checkOnBackgroundColor, 0);
        this.mCheckWidgetConstantState.strokeColor = obtainStyledAttributes.getColor(R.styleable.CheckWidgetDrawable_checkwidget_strokeColor, 0);
        this.mCheckWidgetConstantState.backgroundNormalAlpha = obtainStyledAttributes.getInt(R.styleable.CheckWidgetDrawable_checkwidget_backgroundNormalAlpha, 0);
        this.mCheckWidgetConstantState.backgroundDisableAlpha = obtainStyledAttributes.getInt(R.styleable.CheckWidgetDrawable_checkwidget_backgroundDisableAlpha, 0);
        this.mCheckWidgetConstantState.strokeNormalAlpha = obtainStyledAttributes.getInt(R.styleable.CheckWidgetDrawable_checkwidget_strokeNormalAlpha, 0);
        this.mCheckWidgetConstantState.strokeDisableAlpha = obtainStyledAttributes.getInt(R.styleable.CheckWidgetDrawable_checkwidget_strokeDisableAlpha, 0);
        this.mCheckWidgetConstantState.touchAnimEnable = obtainStyledAttributes.getBoolean(R.styleable.CheckWidgetDrawable_checkwidget_touchAnimEnable, false);
        obtainStyledAttributes.recycle();
        boolean isSingleSelectionWidget = isSingleSelectionWidget();
        CheckWidgetConstantState checkWidgetConstantState = this.mCheckWidgetConstantState;
        CheckWidgetDrawableAnims checkWidgetDrawableAnims = new CheckWidgetDrawableAnims(this, isSingleSelectionWidget, checkWidgetConstantState.grayColor, checkWidgetConstantState.blackColor, checkWidgetConstantState.backGroundColor, checkWidgetConstantState.backgroundNormalAlpha, checkWidgetConstantState.backgroundDisableAlpha, checkWidgetConstantState.strokeColor, checkWidgetConstantState.strokeNormalAlpha, checkWidgetConstantState.strokeDisableAlpha);
        this.mCheckWidgetDrawableAnims = checkWidgetDrawableAnims;
    }

    public void draw(Canvas canvas) {
        int i;
        Drawable current = getCurrent();
        if (current != null && (current instanceof BitmapDrawable)) {
            super.draw(canvas);
        } else if (!this.mCheckWidgetConstantState.touchAnimEnable) {
            CheckWidgetDrawableAnims checkWidgetDrawableAnims = this.mCheckWidgetDrawableAnims;
            if (checkWidgetDrawableAnims != null) {
                checkWidgetDrawableAnims.draw(canvas);
            }
            super.draw(canvas);
        } else {
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 2));
            if (this.mIsEnabled) {
                CheckWidgetDrawableAnims checkWidgetDrawableAnims2 = this.mCheckWidgetDrawableAnims;
                if (checkWidgetDrawableAnims2 != null) {
                    checkWidgetDrawableAnims2.draw(canvas);
                }
                i = (int) (this.mContentAlpha * 255.0f);
            } else {
                i = 127;
            }
            setAlpha(i);
            canvas.save();
            Rect bounds = getBounds();
            float f = this.mScale;
            canvas.scale(f, f, (float) ((bounds.left + bounds.right) / 2), (float) ((bounds.top + bounds.bottom) / 2));
            super.draw(canvas);
            canvas.restore();
        }
    }

    /* access modifiers changed from: protected */
    public int getCheckWidgetDrawableStyle() {
        return R.style.CheckWidgetDrawable_CheckBox;
    }

    public float getContentAlpha() {
        return this.mContentAlpha;
    }

    public float getScale() {
        return this.mScale;
    }

    /* access modifiers changed from: protected */
    public boolean isSingleSelectionWidget() {
        return false;
    }

    /* access modifiers changed from: protected */
    public CheckWidgetConstantState newCheckWidgetConstantState() {
        return new CheckBoxConstantState();
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] iArr) {
        boolean onStateChange = super.onStateChange(iArr);
        if (this.mCheckWidgetDrawableAnims == null) {
            return onStateChange;
        }
        Drawable current = getCurrent();
        if (current != null && (current instanceof BitmapDrawable)) {
            return super.onStateChange(iArr);
        }
        this.mIsEnabled = false;
        boolean z = false;
        boolean z2 = false;
        for (int i : iArr) {
            if (i == 16842919) {
                z = true;
            } else if (i == 16842912) {
                z2 = true;
            } else if (i == 16842910) {
                this.mIsEnabled = true;
            }
        }
        if (z) {
            startPressedAnim(z2);
        }
        if (!this.mPrePressed && !z) {
            verifyChecked(z2, this.mIsEnabled);
        }
        if (!z && (this.mPrePressed || z2 != this.mPreChecked)) {
            startUnPressedAnim(z2);
        }
        this.mPrePressed = z;
        this.mPreChecked = z2;
        return onStateChange;
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        setCheckWidgetDrawableBounds(i, i2, i3, i4);
    }

    public void setBounds(Rect rect) {
        super.setBounds(rect);
        setCheckWidgetDrawableBounds(rect);
    }

    /* access modifiers changed from: protected */
    public void setCheckWidgetDrawableBounds(int i, int i2, int i3, int i4) {
        CheckWidgetDrawableAnims checkWidgetDrawableAnims = this.mCheckWidgetDrawableAnims;
        if (checkWidgetDrawableAnims != null) {
            checkWidgetDrawableAnims.setBounds(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: protected */
    public void setCheckWidgetDrawableBounds(Rect rect) {
        CheckWidgetDrawableAnims checkWidgetDrawableAnims = this.mCheckWidgetDrawableAnims;
        if (checkWidgetDrawableAnims != null) {
            checkWidgetDrawableAnims.setBounds(rect);
        }
    }

    public void setContentAlpha(float f) {
        this.mContentAlpha = f;
    }

    public void setScale(float f) {
        this.mScale = f;
    }

    /* access modifiers changed from: protected */
    public void startPressedAnim(boolean z) {
        CheckWidgetDrawableAnims checkWidgetDrawableAnims = this.mCheckWidgetDrawableAnims;
        if (checkWidgetDrawableAnims != null) {
            checkWidgetDrawableAnims.startPressedAnim(z, this.mCheckWidgetConstantState.touchAnimEnable);
        }
    }

    /* access modifiers changed from: protected */
    public void startUnPressedAnim(boolean z) {
        CheckWidgetDrawableAnims checkWidgetDrawableAnims = this.mCheckWidgetDrawableAnims;
        if (checkWidgetDrawableAnims != null) {
            checkWidgetDrawableAnims.startUnPressedAnim(z, this.mCheckWidgetConstantState.touchAnimEnable);
        }
    }

    /* access modifiers changed from: protected */
    public void verifyChecked(boolean z, boolean z2) {
        CheckWidgetDrawableAnims checkWidgetDrawableAnims = this.mCheckWidgetDrawableAnims;
        if (checkWidgetDrawableAnims != null) {
            checkWidgetDrawableAnims.verifyChecked(z, z2);
            invalidateSelf();
        }
    }
}
