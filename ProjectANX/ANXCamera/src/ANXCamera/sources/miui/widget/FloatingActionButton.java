package miui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.miui.internal.widget.FloatingActionButtonHelper;
import java.lang.ref.WeakReference;
import miui.R;
import miui.util.AttributeResolver;
import miui.util.DrawableUtil;
import miui.util.HapticFeedbackUtil;
import miui.view.MiuiHapticFeedbackConstants;

public class FloatingActionButton extends ImageView {
    private static final int PRESSED_MASK_COLOR = 218103808;
    private static final int SHADOW_ALPHA = 25;
    private static final float SHADOW_RADIUS = 5.45f;
    private static final float X_OFFSET = 0.0f;
    private static final float Y_OFFSET = 5.45f;
    private Drawable mDefaultBackground;
    private int mFabColor;
    private boolean mHasFabColor;
    private final boolean mIsShadowEnabled;
    private final int mShadowRadius;
    private final int mShadowXOffset;
    private final int mShadowYOffset;

    class OvalShapeWithPadding extends OvalShape {
        private WeakReference mViewRef;

        public OvalShapeWithPadding() {
            this.mViewRef = new WeakReference(null);
        }

        public OvalShapeWithPadding(View view) {
            this.mViewRef = new WeakReference(view);
        }

        public void draw(Canvas canvas, Paint paint) {
            View view = (View) this.mViewRef.get();
            if (view != null) {
                int width = view.getWidth();
                int paddingLeft = view.getPaddingLeft();
                int paddingRight = ((width - paddingLeft) - view.getPaddingRight()) / 2;
                canvas.drawCircle((float) (paddingLeft + paddingRight), (float) (view.getPaddingTop() + paddingRight), (float) paddingRight, paint);
            }
        }
    }

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        float f = getContext().getResources().getDisplayMetrics().density;
        this.mShadowXOffset = (int) (0.0f * f);
        int i2 = (int) (f * 5.45f);
        this.mShadowYOffset = i2;
        this.mShadowRadius = i2;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FloatingActionButton, i, R.style.Widget_FloatingActionButton);
        this.mIsShadowEnabled = obtainStyledAttributes.getBoolean(R.styleable.FloatingActionButton_fabShadowEnabled, true);
        this.mHasFabColor = obtainStyledAttributes.hasValue(R.styleable.FloatingActionButton_fabColor);
        this.mFabColor = obtainStyledAttributes.getColor(R.styleable.FloatingActionButton_fabColor, AttributeResolver.resolveColor(getContext(), R.attr.colorAccent));
        obtainStyledAttributes.recycle();
        initBackground();
        updatePadding();
        FloatingActionButtonHelper.initFabTouchAnimation(this);
    }

    private int computeShadowColor(int i) {
        return Color.argb(25, Color.red(i), Math.max(0, Color.green(i) - 30), Color.blue(i));
    }

    private Drawable createFabBackground() {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShapeWithPadding(this));
        if (this.mIsShadowEnabled) {
            int computeShadowColor = computeShadowColor(this.mFabColor);
            setLayerType(VERSION.SDK_INT >= 28 ? 2 : 1, shapeDrawable.getPaint());
            shapeDrawable.getPaint().setShadowLayer((float) this.mShadowRadius, (float) this.mShadowXOffset, (float) this.mShadowYOffset, computeShadowColor);
        }
        shapeDrawable.getPaint().setColor(this.mFabColor);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(new OvalShapeWithPadding(this));
        shapeDrawable2.getPaint().setColor(PRESSED_MASK_COLOR);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shapeDrawable, shapeDrawable2});
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(ImageView.PRESSED_ENABLED_STATE_SET, layerDrawable);
        stateListDrawable.addState(ImageView.ENABLED_SELECTED_STATE_SET, layerDrawable);
        stateListDrawable.addState(ImageView.EMPTY_STATE_SET, shapeDrawable);
        return stateListDrawable;
    }

    private Drawable getDefaultBackground() {
        if (this.mDefaultBackground == null) {
            this.mFabColor = AttributeResolver.resolveColor(getContext(), R.attr.colorAccent);
            this.mHasFabColor = true;
            this.mDefaultBackground = createFabBackground();
        }
        return this.mDefaultBackground;
    }

    private void initBackground() {
        Drawable defaultBackground;
        if (getBackground() == null) {
            Drawable resolveDrawable = AttributeResolver.resolveDrawable(getContext(), com.miui.internal.R.attr.actionButtonMainBackground);
            if (!DrawableUtil.isPlaceholder(resolveDrawable)) {
                this.mDefaultBackground = resolveDrawable;
                this.mHasFabColor = false;
                defaultBackground = this.mDefaultBackground;
            } else {
                defaultBackground = this.mHasFabColor ? createFabBackground() : getDefaultBackground();
            }
            super.setBackground(defaultBackground);
            return;
        }
        this.mHasFabColor = false;
    }

    private void updatePadding() {
        if (!this.mIsShadowEnabled || !this.mHasFabColor) {
            setPadding(0, 0, 0, 0);
        } else {
            setPadding(Math.max(0, this.mShadowRadius - this.mShadowXOffset), Math.max(0, this.mShadowRadius - this.mShadowYOffset), Math.max(0, this.mShadowRadius + this.mShadowXOffset), Math.max(0, this.mShadowRadius + this.mShadowYOffset));
        }
    }

    public boolean performClick() {
        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TAP_LIGHT)) {
            performHapticFeedback(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TAP_LIGHT);
        }
        return super.performClick();
    }

    public void setBackground(Drawable drawable) {
        this.mHasFabColor = false;
        if (drawable == null) {
            drawable = getDefaultBackground();
        }
        super.setBackground(drawable);
        updatePadding();
    }

    public void setBackgroundColor(int i) {
        if (!this.mHasFabColor || this.mFabColor != i) {
            this.mFabColor = i;
            super.setBackground(createFabBackground());
            updatePadding();
        }
        this.mHasFabColor = true;
    }

    public void setBackgroundResource(int i) {
        this.mHasFabColor = false;
        if (i == 0) {
            super.setBackground(getDefaultBackground());
        } else {
            super.setBackgroundResource(i);
        }
        updatePadding();
    }
}
