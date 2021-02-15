package miui.smooth;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import miui.R;
import miui.smooth.internal.SmoothDrawHelper;

public class SmoothFrameLayout extends FrameLayout {
    private static final PorterDuffXfermode XFERMODE_DST_OUT = new PorterDuffXfermode(Mode.DST_OUT);
    private static final PorterDuffXfermode XFERMODE_SRC_OUT = new PorterDuffXfermode(Mode.SRC_OUT);
    private SmoothDrawHelper mHelper;
    private Rect mLayer;
    private RectF mSavedLayer;

    public SmoothFrameLayout(Context context) {
        this(context, null);
    }

    public SmoothFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SmoothFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mLayer = new Rect();
        this.mSavedLayer = new RectF();
        this.mHelper = new SmoothDrawHelper();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.MiuiSmoothFrameLayout);
        setCornerRadius((float) obtainStyledAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothFrameLayout_android_radius, 0));
        if (obtainStyledAttributes.hasValue(R.styleable.MiuiSmoothFrameLayout_android_topLeftRadius) || obtainStyledAttributes.hasValue(R.styleable.MiuiSmoothFrameLayout_android_topRightRadius) || obtainStyledAttributes.hasValue(R.styleable.MiuiSmoothFrameLayout_android_bottomRightRadius) || obtainStyledAttributes.hasValue(R.styleable.MiuiSmoothFrameLayout_android_bottomLeftRadius)) {
            float dimensionPixelSize = (float) obtainStyledAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothFrameLayout_android_topLeftRadius, 0);
            float dimensionPixelSize2 = (float) obtainStyledAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothFrameLayout_android_topRightRadius, 0);
            float dimensionPixelSize3 = (float) obtainStyledAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothFrameLayout_android_bottomRightRadius, 0);
            float dimensionPixelSize4 = (float) obtainStyledAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothFrameLayout_android_bottomLeftRadius, 0);
            setCornerRadii(new float[]{dimensionPixelSize, dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize2, dimensionPixelSize3, dimensionPixelSize3, dimensionPixelSize4, dimensionPixelSize4});
        }
        setStrokeWidth(obtainStyledAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothFrameLayout_miui_strokeWidth, 0));
        setStrokeColor(obtainStyledAttributes.getColor(R.styleable.MiuiSmoothFrameLayout_miui_strokeColor, 0));
        obtainStyledAttributes.recycle();
    }

    private void updateBackground() {
        updateBounds();
        if (VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
        invalidate();
    }

    private void updateBounds() {
        this.mHelper.onBoundsChange(this.mLayer);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        int saveLayer = canvas.saveLayer(this.mSavedLayer, null, 31);
        super.dispatchDraw(canvas);
        this.mHelper.drawMask(canvas, XFERMODE_DST_OUT);
        canvas.restoreToCount(saveLayer);
        this.mHelper.drawStroke(canvas);
    }

    public void draw(Canvas canvas) {
        int saveLayer = canvas.saveLayer(this.mSavedLayer, null, 31);
        super.draw(canvas);
        this.mHelper.drawMask(canvas, XFERMODE_SRC_OUT);
        canvas.restoreToCount(saveLayer);
        this.mHelper.drawStroke(canvas);
    }

    public float[] getCornerRadii() {
        if (this.mHelper.getRadii() == null) {
            return null;
        }
        return (float[]) this.mHelper.getRadii().clone();
    }

    public float getCornerRadius() {
        return this.mHelper.getRadius();
    }

    public int getStrokeColor() {
        return this.mHelper.getStrokeColor();
    }

    public int getStrokeWidth() {
        return this.mHelper.getStrokeWidth();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mLayer.set(0, 0, i, i2);
        this.mSavedLayer.set(0.0f, 0.0f, (float) i, (float) i2);
        updateBounds();
    }

    public void setCornerRadii(float[] fArr) {
        this.mHelper.setRadii(fArr);
        if (fArr == null) {
            this.mHelper.setRadius(0.0f);
        }
        updateBackground();
    }

    public void setCornerRadius(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        this.mHelper.setRadius(f);
        this.mHelper.setRadii(null);
        updateBackground();
    }

    public void setStrokeColor(int i) {
        if (this.mHelper.getStrokeColor() != i) {
            this.mHelper.setStrokeColor(i);
            updateBackground();
        }
    }

    public void setStrokeWidth(int i) {
        if (this.mHelper.getStrokeWidth() != i) {
            this.mHelper.setStrokeWidth(i);
            updateBackground();
        }
    }
}
