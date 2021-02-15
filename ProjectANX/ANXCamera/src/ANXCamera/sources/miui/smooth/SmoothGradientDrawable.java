package miui.smooth;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import miui.R;
import miui.smooth.internal.SmoothDrawHelper;
import org.xmlpull.v1.XmlPullParser;

public class SmoothGradientDrawable extends GradientDrawable {
    private static final PorterDuffXfermode XFERMODE_DST_OUT = new PorterDuffXfermode(Mode.DST_OUT);
    private SmoothDrawHelper mHelper;
    private GradientDrawable mParentDrawable;
    private RectF mSavedLayer;
    protected SmoothConstantState mSmoothConstantState;

    public class SmoothConstantState extends ConstantState {
        ConstantState mParent;
        float[] mRadii;
        float mRadius;
        int mStrokeColor = 0;
        int mStrokeWidth = 0;

        public SmoothConstantState() {
        }

        public SmoothConstantState(SmoothConstantState smoothConstantState) {
            this.mRadius = smoothConstantState.mRadius;
            this.mRadii = smoothConstantState.mRadii;
            this.mStrokeWidth = smoothConstantState.mStrokeWidth;
            this.mStrokeColor = smoothConstantState.mStrokeColor;
            this.mParent = smoothConstantState.mParent;
        }

        public boolean canApplyTheme() {
            return true;
        }

        public int getChangingConfigurations() {
            return this.mParent.getChangingConfigurations();
        }

        public Drawable newDrawable() {
            if (this.mParent == null) {
                return null;
            }
            return new SmoothGradientDrawable(this, null);
        }

        public Drawable newDrawable(Resources resources) {
            if (this.mParent == null) {
                return null;
            }
            return new SmoothGradientDrawable(new SmoothConstantState(this), resources);
        }

        public void setConstantState(ConstantState constantState) {
            this.mParent = constantState;
        }
    }

    public SmoothGradientDrawable() {
        this.mHelper = new SmoothDrawHelper();
        this.mSavedLayer = new RectF();
        this.mSmoothConstantState = new SmoothConstantState();
        this.mSmoothConstantState.setConstantState(super.getConstantState());
    }

    public SmoothGradientDrawable(Orientation orientation, int[] iArr) {
        super(orientation, iArr);
        this.mHelper = new SmoothDrawHelper();
        this.mSavedLayer = new RectF();
        this.mSmoothConstantState = new SmoothConstantState();
        this.mSmoothConstantState.setConstantState(super.getConstantState());
    }

    private SmoothGradientDrawable(SmoothConstantState smoothConstantState, Resources resources) {
        this.mHelper = new SmoothDrawHelper();
        this.mSavedLayer = new RectF();
        this.mSmoothConstantState = smoothConstantState;
        Drawable newDrawable = resources == null ? smoothConstantState.mParent.newDrawable() : smoothConstantState.mParent.newDrawable(resources);
        this.mSmoothConstantState.setConstantState(newDrawable.getConstantState());
        this.mParentDrawable = (GradientDrawable) newDrawable;
        this.mHelper.setRadii(smoothConstantState.mRadii);
        this.mHelper.setRadius(smoothConstantState.mRadius);
        this.mHelper.setStrokeWidth(smoothConstantState.mStrokeWidth);
        this.mHelper.setStrokeColor(smoothConstantState.mStrokeColor);
    }

    private TypedArray obtainAttributes(Resources resources, Theme theme, AttributeSet attributeSet, int[] iArr) {
        return theme == null ? resources.obtainAttributes(attributeSet, iArr) : theme.obtainStyledAttributes(attributeSet, iArr, 0, 0);
    }

    public void applyTheme(Theme theme) {
        super.applyTheme(theme);
        this.mSmoothConstantState.setConstantState(super.getConstantState());
    }

    public boolean canApplyTheme() {
        SmoothConstantState smoothConstantState = this.mSmoothConstantState;
        return (smoothConstantState != null && smoothConstantState.canApplyTheme()) || super.canApplyTheme();
    }

    public void draw(Canvas canvas) {
        int saveLayer = canvas.saveLayer(this.mSavedLayer, null, 31);
        GradientDrawable gradientDrawable = this.mParentDrawable;
        if (gradientDrawable != null) {
            gradientDrawable.draw(canvas);
        } else {
            super.draw(canvas);
        }
        this.mHelper.drawMask(canvas, XFERMODE_DST_OUT);
        canvas.restoreToCount(saveLayer);
        this.mHelper.drawStroke(canvas);
    }

    public ConstantState getConstantState() {
        return this.mSmoothConstantState;
    }

    public int getStrokeColor() {
        return this.mSmoothConstantState.mStrokeColor;
    }

    public int getStrokeWidth() {
        return this.mSmoothConstantState.mStrokeWidth;
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme) {
        TypedArray obtainAttributes = obtainAttributes(resources, theme, attributeSet, R.styleable.MiuiSmoothGradientDrawable);
        setStrokeWidth(obtainAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothGradientDrawable_miui_strokeWidth, 0));
        setStrokeColor(obtainAttributes.getColor(R.styleable.MiuiSmoothGradientDrawable_miui_strokeColor, 0));
        obtainAttributes.recycle();
        super.inflate(resources, xmlPullParser, attributeSet, theme);
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        GradientDrawable gradientDrawable = this.mParentDrawable;
        if (gradientDrawable != null) {
            gradientDrawable.setBounds(rect);
        }
        this.mHelper.onBoundsChange(rect);
        this.mSavedLayer.set(0.0f, 0.0f, (float) rect.width(), (float) rect.height());
    }

    public void setCornerRadii(float[] fArr) {
        super.setCornerRadii(fArr);
        this.mSmoothConstantState.mRadii = fArr;
        this.mHelper.setRadii(fArr);
        if (fArr == null) {
            this.mSmoothConstantState.mRadius = 0.0f;
            this.mHelper.setRadius(0.0f);
        }
    }

    public void setCornerRadius(float f) {
        super.setCornerRadius(f);
        if (f < 0.0f) {
            f = 0.0f;
        }
        SmoothConstantState smoothConstantState = this.mSmoothConstantState;
        smoothConstantState.mRadius = f;
        smoothConstantState.mRadii = null;
        this.mHelper.setRadius(f);
        this.mHelper.setRadii(null);
    }

    public void setStrokeColor(int i) {
        SmoothConstantState smoothConstantState = this.mSmoothConstantState;
        if (smoothConstantState.mStrokeColor != i) {
            smoothConstantState.mStrokeColor = i;
            this.mHelper.setStrokeColor(i);
            invalidateSelf();
        }
    }

    public void setStrokeWidth(int i) {
        SmoothConstantState smoothConstantState = this.mSmoothConstantState;
        if (smoothConstantState.mStrokeWidth != i) {
            smoothConstantState.mStrokeWidth = i;
            this.mHelper.setStrokeWidth(i);
            invalidateSelf();
        }
    }
}
