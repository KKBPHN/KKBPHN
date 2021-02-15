package com.android.camera.customization;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class TintShapeView extends View {
    private Paint mPaintInner;
    private Paint mPaintOutter;
    boolean mShowOutter = false;
    private int outerRadius;
    private int radius;
    private TintShape shape;

    public enum ShapeType {
        CIRCLE,
        RECTANGLE
    }

    public class TintShape {
        /* access modifiers changed from: private */
        public int cornerRadius;
        /* access modifiers changed from: private */
        public int extraPadding;
        /* access modifiers changed from: private */
        public int innerColor;
        /* access modifiers changed from: private */
        public int innerRadius;
        /* access modifiers changed from: private */
        public int outerColor;
        /* access modifiers changed from: private */
        public int outerRadius;
        /* access modifiers changed from: private */
        public float stroke;
        /* access modifiers changed from: private */
        public ShapeType type;

        public TintShape cornerRadius(int i) {
            this.cornerRadius = i;
            return this;
        }

        public TintShape extraPadding(int i) {
            this.extraPadding = i;
            return this;
        }

        public TintShape innerColor(int i) {
            this.innerColor = i;
            return this;
        }

        public TintShape innerRadius(int i) {
            this.innerRadius = i;
            return this;
        }

        public TintShape outerColor(int i) {
            this.outerColor = i;
            return this;
        }

        public TintShape outerRadius(int i) {
            this.outerRadius = i;
            return this;
        }

        public TintShape shape(ShapeType shapeType) {
            this.type = shapeType;
            return this;
        }

        public TintShape stroke(float f) {
            this.stroke = f;
            return this;
        }
    }

    public TintShapeView(Context context) {
        super(context);
        init();
    }

    public TintShapeView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public TintShapeView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public TintShapeView(Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    private void init() {
        this.mPaintInner = new Paint();
        this.mPaintOutter = new Paint();
        this.mPaintInner.setAntiAlias(true);
        this.mPaintInner.setStyle(Style.FILL);
        this.mPaintOutter.setAntiAlias(true);
        this.mPaintOutter.setStyle(Style.STROKE);
        this.mShowOutter = false;
    }

    public void config(TintShape tintShape) {
        this.shape = tintShape;
        this.mPaintInner.setColor(tintShape.innerColor);
        this.mPaintOutter.setColor(tintShape.outerColor);
        this.radius = tintShape.innerRadius;
        this.outerRadius = tintShape.outerRadius;
        this.mPaintOutter.setStrokeWidth(tintShape.stroke);
    }

    public void draw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int i = width / 2;
        int i2 = height / 2;
        if (this.shape.type == ShapeType.CIRCLE) {
            canvas.drawCircle((float) i, (float) i2, (float) this.radius, this.mPaintInner);
        } else {
            canvas.drawRoundRect(((float) this.shape.extraPadding) + this.shape.stroke, ((float) this.shape.extraPadding) + this.shape.stroke, ((float) (width - this.shape.extraPadding)) - this.shape.stroke, ((float) (height - this.shape.extraPadding)) - this.shape.stroke, (float) this.shape.cornerRadius, (float) this.shape.cornerRadius, this.mPaintInner);
        }
        if (this.mShowOutter) {
            if (this.shape.type == ShapeType.CIRCLE) {
                canvas.drawCircle((float) i, (float) i2, (float) this.outerRadius, this.mPaintOutter);
            } else {
                canvas.drawRoundRect(this.shape.stroke, this.shape.stroke, ((float) width) - this.shape.stroke, ((float) height) - this.shape.stroke, (float) (this.shape.cornerRadius + this.shape.extraPadding), (float) (this.shape.cornerRadius + this.shape.extraPadding), this.mPaintOutter);
            }
        }
        super.draw(canvas);
    }

    public void setEnabled(boolean z) {
        int i = 255;
        this.mPaintInner.setAlpha(z ? 255 : 200);
        Paint paint = this.mPaintOutter;
        if (!z) {
            i = 200;
        }
        paint.setAlpha(i);
        super.setEnabled(z);
    }

    public void setSelection(boolean z) {
        this.mShowOutter = z;
        invalidate();
    }
}
