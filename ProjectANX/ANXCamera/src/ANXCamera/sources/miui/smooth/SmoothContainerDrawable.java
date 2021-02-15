package miui.smooth;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import miui.R;
import miui.smooth.internal.SmoothDrawHelper;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SmoothContainerDrawable extends Drawable implements Callback {
    private static final PorterDuffXfermode XFERMODE_DST_OUT = new PorterDuffXfermode(Mode.DST_OUT);
    private ContainerState mContainerState;
    private SmoothDrawHelper mHelper;

    class ChildDrawable {
        Drawable mDrawable;

        ChildDrawable() {
        }

        ChildDrawable(ChildDrawable childDrawable, SmoothContainerDrawable smoothContainerDrawable, Resources resources, Theme theme) {
            Drawable drawable;
            Drawable drawable2 = childDrawable.mDrawable;
            if (drawable2 != null) {
                ConstantState constantState = drawable2.getConstantState();
                drawable = constantState == null ? drawable2 : resources == null ? constantState.newDrawable() : (theme != null && VERSION.SDK_INT >= 21) ? constantState.newDrawable(resources, theme) : constantState.newDrawable(resources);
                if (VERSION.SDK_INT >= 23) {
                    drawable.setLayoutDirection(drawable2.getLayoutDirection());
                }
                drawable.setBounds(drawable2.getBounds());
                drawable.setLevel(drawable2.getLevel());
                drawable.setCallback(smoothContainerDrawable);
            } else {
                drawable = null;
            }
            this.mDrawable = drawable;
        }
    }

    final class ContainerState extends ConstantState {
        ChildDrawable mChildDrawable;
        float[] mRadii;
        float mRadius;
        int mStrokeColor;
        int mStrokeWidth;

        public ContainerState() {
            this.mChildDrawable = new ChildDrawable();
        }

        public ContainerState(ContainerState containerState, SmoothContainerDrawable smoothContainerDrawable, Resources resources, Theme theme) {
            this.mChildDrawable = new ChildDrawable(containerState.mChildDrawable, smoothContainerDrawable, resources, theme);
            this.mRadius = containerState.mRadius;
            this.mRadii = containerState.mRadii;
            this.mStrokeWidth = containerState.mStrokeWidth;
            this.mStrokeColor = containerState.mStrokeColor;
        }

        public boolean canApplyTheme() {
            return true;
        }

        public int getAlpha() {
            return this.mChildDrawable.mDrawable.getAlpha();
        }

        public Rect getBounds() {
            return this.mChildDrawable.mDrawable.getBounds();
        }

        public int getChangingConfigurations() {
            return this.mChildDrawable.mDrawable.getChangingConfigurations();
        }

        public Rect getDirtyBounds() {
            return this.mChildDrawable.mDrawable.getDirtyBounds();
        }

        public int getIntrinsicHeight() {
            return this.mChildDrawable.mDrawable.getIntrinsicHeight();
        }

        public int getIntrinsicWidth() {
            return this.mChildDrawable.mDrawable.getIntrinsicWidth();
        }

        public int getOpacity() {
            return this.mChildDrawable.mDrawable.getOpacity();
        }

        public boolean getPadding(Rect rect) {
            return this.mChildDrawable.mDrawable.getPadding(rect);
        }

        public final boolean isStateful() {
            return this.mChildDrawable.mDrawable.isStateful();
        }

        public void jumpToCurrentState() {
            this.mChildDrawable.mDrawable.jumpToCurrentState();
        }

        public Drawable newDrawable() {
            return new SmoothContainerDrawable(null, null, this);
        }

        public Drawable newDrawable(Resources resources) {
            return new SmoothContainerDrawable(resources, null, this);
        }

        public Drawable newDrawable(Resources resources, Theme theme) {
            return new SmoothContainerDrawable(resources, theme, this);
        }

        public void onBoundsChange(Rect rect) {
            this.mChildDrawable.mDrawable.setBounds(rect);
        }

        public boolean onStateChange(int[] iArr) {
            return isStateful() && this.mChildDrawable.mDrawable.setState(iArr);
        }

        public void setAlpha(int i) {
            this.mChildDrawable.mDrawable.setAlpha(i);
        }

        public void setBounds(int i, int i2, int i3, int i4) {
            this.mChildDrawable.mDrawable.setBounds(i, i2, i3, i4);
        }

        public void setBounds(Rect rect) {
            this.mChildDrawable.mDrawable.setBounds(rect);
        }

        public void setChangingConfigurations(int i) {
            this.mChildDrawable.mDrawable.setChangingConfigurations(i);
        }

        public void setColorFilter(ColorFilter colorFilter) {
            this.mChildDrawable.mDrawable.setColorFilter(colorFilter);
        }

        public void setDither(boolean z) {
            this.mChildDrawable.mDrawable.setDither(z);
        }

        public void setFilterBitmap(boolean z) {
            this.mChildDrawable.mDrawable.setFilterBitmap(z);
        }
    }

    public SmoothContainerDrawable() {
        this.mHelper = new SmoothDrawHelper();
        this.mContainerState = new ContainerState();
    }

    private SmoothContainerDrawable(Resources resources, Theme theme, ContainerState containerState) {
        this.mHelper = new SmoothDrawHelper();
        this.mContainerState = new ContainerState(containerState, this, resources, theme);
        this.mHelper.setStrokeWidth(containerState.mStrokeWidth);
        this.mHelper.setStrokeColor(containerState.mStrokeColor);
        this.mHelper.setRadii(containerState.mRadii);
        this.mHelper.setRadius(containerState.mRadius);
    }

    private void inflateInnerDrawable(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme) {
        int next;
        int depth = xmlPullParser.getDepth() + 1;
        while (true) {
            int next2 = xmlPullParser.next();
            if (next2 == 1) {
                break;
            }
            int depth2 = xmlPullParser.getDepth();
            if (depth2 < depth && next2 == 3) {
                break;
            } else if (next2 == 2) {
                if (depth2 > depth) {
                    continue;
                } else if (xmlPullParser.getName().equals("child")) {
                    while (true) {
                        next = xmlPullParser.next();
                        if (next != 4) {
                            break;
                        }
                    }
                    if (next == 2) {
                        ChildDrawable childDrawable = new ChildDrawable();
                        childDrawable.mDrawable = VERSION.SDK_INT >= 21 ? Drawable.createFromXmlInner(resources, xmlPullParser, attributeSet, theme) : Drawable.createFromXmlInner(resources, xmlPullParser, attributeSet);
                        childDrawable.mDrawable.setCallback(this);
                        this.mContainerState.mChildDrawable = childDrawable;
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(xmlPullParser.getPositionDescription());
                    sb.append(": <child> tag requires a 'drawable' attribute or child tag defining a drawable");
                    throw new XmlPullParserException(sb.toString());
                }
            }
        }
    }

    private TypedArray obtainAttributes(Resources resources, Theme theme, AttributeSet attributeSet, int[] iArr) {
        return theme == null ? resources.obtainAttributes(attributeSet, iArr) : theme.obtainStyledAttributes(attributeSet, iArr, 0, 0);
    }

    public void applyTheme(Theme theme) {
        super.applyTheme(theme);
        if (VERSION.SDK_INT >= 21) {
            this.mContainerState.mChildDrawable.mDrawable.applyTheme(theme);
        }
    }

    public boolean canApplyTheme() {
        ContainerState containerState = this.mContainerState;
        return (containerState != null && containerState.canApplyTheme()) || super.canApplyTheme();
    }

    public void draw(Canvas canvas) {
        int saveLayer = canvas.saveLayer((float) getBoundsInner().left, (float) getBoundsInner().top, (float) getBoundsInner().right, (float) getBoundsInner().bottom, null, 31);
        this.mContainerState.mChildDrawable.mDrawable.draw(canvas);
        this.mHelper.drawMask(canvas, XFERMODE_DST_OUT);
        canvas.restoreToCount(saveLayer);
        this.mHelper.drawStroke(canvas);
    }

    public int getAlpha() {
        return this.mContainerState.getAlpha();
    }

    public final Rect getBoundsInner() {
        return this.mContainerState.getBounds();
    }

    public Drawable getChildDrawable() {
        ContainerState containerState = this.mContainerState;
        if (containerState != null) {
            return containerState.mChildDrawable.mDrawable;
        }
        return null;
    }

    public ConstantState getConstantState() {
        return this.mContainerState;
    }

    public float[] getCornerRadii() {
        float[] fArr = this.mContainerState.mRadii;
        if (fArr == null) {
            return null;
        }
        return (float[]) fArr.clone();
    }

    public float getCornerRadius() {
        return this.mContainerState.mRadius;
    }

    public Rect getDirtyBounds() {
        return this.mContainerState.getDirtyBounds();
    }

    public int getIntrinsicHeight() {
        return this.mContainerState.getIntrinsicHeight();
    }

    public int getIntrinsicWidth() {
        return this.mContainerState.getIntrinsicWidth();
    }

    public int getOpacity() {
        return this.mContainerState.getOpacity();
    }

    public void getOutline(Outline outline) {
        if (VERSION.SDK_INT >= 21) {
            outline.setRoundRect(getBoundsInner(), getCornerRadius());
        }
    }

    public boolean getPadding(Rect rect) {
        return this.mContainerState.getPadding(rect);
    }

    public int getStrokeColor() {
        return this.mContainerState.mStrokeColor;
    }

    public int getStrokeWidth() {
        return this.mContainerState.mStrokeWidth;
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme) {
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        TypedArray obtainAttributes = obtainAttributes(resources, theme, attributeSet, R.styleable.MiuiSmoothContainerDrawable);
        setCornerRadius((float) obtainAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothContainerDrawable_android_radius, 0));
        if (obtainAttributes.hasValue(R.styleable.MiuiSmoothContainerDrawable_android_topLeftRadius) || obtainAttributes.hasValue(R.styleable.MiuiSmoothContainerDrawable_android_topRightRadius) || obtainAttributes.hasValue(R.styleable.MiuiSmoothContainerDrawable_android_bottomRightRadius) || obtainAttributes.hasValue(R.styleable.MiuiSmoothContainerDrawable_android_bottomLeftRadius)) {
            float dimensionPixelSize = (float) obtainAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothContainerDrawable_android_topLeftRadius, 0);
            float dimensionPixelSize2 = (float) obtainAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothContainerDrawable_android_topRightRadius, 0);
            float dimensionPixelSize3 = (float) obtainAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothContainerDrawable_android_bottomRightRadius, 0);
            float dimensionPixelSize4 = (float) obtainAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothContainerDrawable_android_bottomLeftRadius, 0);
            setCornerRadii(new float[]{dimensionPixelSize, dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize2, dimensionPixelSize3, dimensionPixelSize3, dimensionPixelSize4, dimensionPixelSize4});
        }
        setStrokeWidth(obtainAttributes.getDimensionPixelSize(R.styleable.MiuiSmoothContainerDrawable_miui_strokeWidth, 0));
        setStrokeColor(obtainAttributes.getColor(R.styleable.MiuiSmoothContainerDrawable_miui_strokeColor, 0));
        obtainAttributes.recycle();
        inflateInnerDrawable(resources, xmlPullParser, attributeSet, theme);
    }

    public void invalidateDrawable(Drawable drawable) {
        invalidateSelf();
    }

    public boolean isStateful() {
        return this.mContainerState.isStateful();
    }

    public void jumpToCurrentState() {
        this.mContainerState.jumpToCurrentState();
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        this.mContainerState.onBoundsChange(rect);
        this.mHelper.onBoundsChange(rect);
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] iArr) {
        return this.mContainerState.onStateChange(iArr);
    }

    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        scheduleSelf(runnable, j);
    }

    public void setAlpha(int i) {
        this.mContainerState.setAlpha(i);
    }

    public void setChangingConfigurations(int i) {
        this.mContainerState.setChangingConfigurations(i);
    }

    public void setChildDrawable(Drawable drawable) {
        if (this.mContainerState != null) {
            ChildDrawable childDrawable = new ChildDrawable();
            childDrawable.mDrawable = drawable;
            childDrawable.mDrawable.setCallback(this);
            this.mContainerState.mChildDrawable = childDrawable;
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mContainerState.setColorFilter(colorFilter);
    }

    public void setCornerRadii(float[] fArr) {
        this.mContainerState.mRadii = fArr;
        this.mHelper.setRadii(fArr);
        if (fArr == null) {
            this.mContainerState.mRadius = 0.0f;
            this.mHelper.setRadius(0.0f);
        }
        invalidateSelf();
    }

    public void setCornerRadius(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        ContainerState containerState = this.mContainerState;
        containerState.mRadius = f;
        containerState.mRadii = null;
        this.mHelper.setRadius(f);
        this.mHelper.setRadii(null);
        invalidateSelf();
    }

    public void setDither(boolean z) {
        this.mContainerState.setDither(z);
    }

    public void setFilterBitmap(boolean z) {
        this.mContainerState.setFilterBitmap(z);
    }

    public void setStrokeColor(int i) {
        ContainerState containerState = this.mContainerState;
        if (containerState.mStrokeColor != i) {
            containerState.mStrokeColor = i;
            this.mHelper.setStrokeColor(i);
            invalidateSelf();
        }
    }

    public void setStrokeWidth(int i) {
        ContainerState containerState = this.mContainerState;
        if (containerState.mStrokeWidth != i) {
            containerState.mStrokeWidth = i;
            this.mHelper.setStrokeWidth(i);
            invalidateSelf();
        }
    }

    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        unscheduleSelf(runnable);
    }
}
