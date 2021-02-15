package com.xiaomi.camera.ui.drawable;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@RequiresApi(21)
public class RoundRectDrawable extends Drawable {
    private ColorStateList mBackground;
    private final RectF mBoundsF;
    private final Rect mBoundsI;
    private float[] mCornerRadii;
    private final Paint mPaint;
    private final Path mPath;
    private ColorStateList mTint;
    private PorterDuffColorFilter mTintFilter;
    private Mode mTintMode;

    public RoundRectDrawable(@ColorInt int i, float f) {
        this(ColorStateList.valueOf(i), f);
    }

    public RoundRectDrawable(@ColorInt int i, @Nullable float[] fArr) {
        this(ColorStateList.valueOf(i), fArr);
    }

    public RoundRectDrawable(ColorStateList colorStateList, float f) {
        this(colorStateList, new float[]{f, f, f, f, f, f, f, f});
    }

    public RoundRectDrawable(ColorStateList colorStateList, @Nullable float[] fArr) {
        this.mTintMode = Mode.SRC_IN;
        if (fArr == null || fArr.length >= 8) {
            this.mCornerRadii = fArr;
            this.mPaint = new Paint(5);
            setBackground(colorStateList);
            this.mBoundsF = new RectF();
            this.mBoundsI = new Rect();
            this.mPath = new Path();
            return;
        }
        throw new ArrayIndexOutOfBoundsException("corner radii must have >= 8 values");
    }

    private PorterDuffColorFilter createTintFilter(ColorStateList colorStateList, Mode mode) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return new PorterDuffColorFilter(colorStateList.getColorForState(getState(), 0), mode);
    }

    private void setBackground(ColorStateList colorStateList) {
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);
        }
        this.mBackground = colorStateList;
        this.mPaint.setColor(this.mBackground.getColorForState(getState(), this.mBackground.getDefaultColor()));
    }

    private void updateBounds(Rect rect) {
        if (rect == null) {
            rect = getBounds();
        }
        this.mBoundsF.set((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom);
        this.mBoundsI.set(rect);
        this.mPath.reset();
        float[] fArr = this.mCornerRadii;
        if (fArr != null) {
            this.mPath.addRoundRect(this.mBoundsF, fArr, Direction.CW);
        } else {
            this.mPath.addRect(this.mBoundsF, Direction.CW);
        }
    }

    public void draw(@NonNull Canvas canvas) {
        boolean z;
        Paint paint = this.mPaint;
        if (this.mTintFilter == null || paint.getColorFilter() != null) {
            z = false;
        } else {
            paint.setColorFilter(this.mTintFilter);
            z = true;
        }
        canvas.drawPath(this.mPath, this.mPaint);
        if (z) {
            paint.setColorFilter(null);
        }
    }

    public ColorStateList getColor() {
        return this.mBackground;
    }

    public int getOpacity() {
        return -3;
    }

    public void getOutline(@NonNull Outline outline) {
        float f;
        float[] fArr = this.mCornerRadii;
        if (fArr != null) {
            f = fArr[0];
            for (int i = 1; i < 8; i++) {
                if (this.mCornerRadii[i] != f) {
                    outline.setConvexPath(this.mPath);
                    return;
                }
            }
        } else {
            f = 0.0f;
        }
        outline.setRoundRect(this.mBoundsI, f);
    }

    public boolean isStateful() {
        ColorStateList colorStateList = this.mTint;
        if (colorStateList == null || !colorStateList.isStateful()) {
            ColorStateList colorStateList2 = this.mBackground;
            if ((colorStateList2 == null || !colorStateList2.isStateful()) && !super.isStateful()) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        updateBounds(rect);
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] iArr) {
        ColorStateList colorStateList = this.mBackground;
        int colorForState = colorStateList.getColorForState(iArr, colorStateList.getDefaultColor());
        boolean z = colorForState != this.mPaint.getColor();
        if (z) {
            this.mPaint.setColor(colorForState);
        }
        ColorStateList colorStateList2 = this.mTint;
        if (colorStateList2 != null) {
            Mode mode = this.mTintMode;
            if (mode != null) {
                this.mTintFilter = createTintFilter(colorStateList2, mode);
                return true;
            }
        }
        return z;
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColor(@Nullable ColorStateList colorStateList) {
        setBackground(colorStateList);
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public void setRadius(float f) {
        boolean z;
        int i = 0;
        if (this.mCornerRadii == null) {
            this.mCornerRadii = new float[]{f, f, f, f, f, f, f, f};
            z = true;
        } else {
            z = false;
            while (i < 8) {
                if (!z && this.mCornerRadii[i] != f) {
                    z = true;
                }
                this.mCornerRadii[i] = f;
                i++;
            }
        }
        if (z) {
            updateBounds(null);
            invalidateSelf();
        }
    }

    public void setRadius(float[] fArr) {
        boolean z;
        if (fArr == null || fArr.length >= 8) {
            int i = 0;
            if (fArr == null || this.mCornerRadii == null) {
                z = this.mCornerRadii != null;
                this.mCornerRadii = fArr;
            } else {
                z = false;
                while (i < 8) {
                    if (!z && this.mCornerRadii[i] != fArr[i]) {
                        z = true;
                    }
                    this.mCornerRadii[i] = fArr[i];
                    i++;
                }
            }
            if (z) {
                updateBounds(null);
                invalidateSelf();
                return;
            }
            return;
        }
        throw new ArrayIndexOutOfBoundsException("corner radii must have >= 8 values");
    }

    public void setTintList(ColorStateList colorStateList) {
        this.mTint = colorStateList;
        this.mTintFilter = createTintFilter(this.mTint, this.mTintMode);
        invalidateSelf();
    }

    public void setTintMode(Mode mode) {
        this.mTintMode = mode;
        this.mTintFilter = createTintFilter(this.mTint, this.mTintMode);
        invalidateSelf();
    }
}
