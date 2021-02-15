package miui.smooth.internal;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.Op;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import com.miui.internal.smooth.SmoothPathProvider;

public class SmoothDrawHelper {
    private static final float EXTRA_PADDING = 0.5f;
    private Paint mClipPaint = new Paint(1);
    private Path mClipPath;
    private RectF mLayer;
    private Path mOutterPath;
    private SmoothPathProvider mPathProvider;
    private float[] mRadii;
    private float mRadius;
    private int mStrokeColor = 0;
    private Paint mStrokePaint = new Paint(1);
    private int mStrokeWidth = 0;

    public SmoothDrawHelper() {
        this.mStrokePaint.setStyle(Style.STROKE);
        this.mOutterPath = new Path();
        this.mClipPath = new Path();
        this.mPathProvider = new SmoothPathProvider();
        this.mLayer = new RectF();
    }

    private Path getSmoothPathFromProvider(Path path, RectF rectF, float[] fArr, float f, float f2, float f3) {
        if (fArr == null) {
            this.mPathProvider.buildSmoothData(rectF, f, f2, f3);
        } else {
            this.mPathProvider.buildSmoothData(rectF, fArr, f2, f3);
        }
        return this.mPathProvider.getSmoothPath(path);
    }

    public void drawMask(Canvas canvas, Xfermode xfermode) {
        this.mClipPaint.setXfermode(xfermode);
        canvas.drawPath(this.mClipPath, this.mClipPaint);
        this.mClipPaint.setXfermode(null);
    }

    public void drawStroke(Canvas canvas) {
        boolean z = (this.mStrokeWidth == 0 || this.mStrokeColor == 0) ? false : true;
        if (z) {
            canvas.save();
            this.mStrokePaint.setStrokeWidth((float) this.mStrokeWidth);
            this.mStrokePaint.setColor(this.mStrokeColor);
            canvas.drawPath(this.mOutterPath, this.mStrokePaint);
            canvas.restore();
        }
    }

    public float[] getRadii() {
        return this.mRadii;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public int getStrokeColor() {
        return this.mStrokeColor;
    }

    public int getStrokeWidth() {
        return this.mStrokeWidth;
    }

    public void onBoundsChange(Rect rect) {
        float f = 0.5f;
        this.mLayer.set(((float) rect.left) - 0.5f, ((float) rect.top) - 0.5f, ((float) rect.right) + 0.5f, ((float) rect.bottom) + 0.5f);
        boolean z = (this.mStrokeWidth == 0 || this.mStrokeColor == 0) ? false : true;
        if (z) {
            f = 0.5f + (((float) this.mStrokeWidth) / 2.0f);
        }
        float f2 = f;
        this.mOutterPath = getSmoothPathFromProvider(this.mOutterPath, this.mLayer, this.mRadii, this.mRadius, f2, f2);
        Path path = this.mClipPath;
        if (path != null) {
            path.reset();
        } else {
            this.mClipPath = new Path();
        }
        this.mClipPath.addRect(this.mLayer, Direction.CW);
        this.mClipPath.op(this.mOutterPath, Op.DIFFERENCE);
    }

    public void setRadii(float[] fArr) {
        this.mRadii = fArr;
    }

    public void setRadius(float f) {
        this.mRadius = f;
    }

    public void setStrokeColor(int i) {
        this.mStrokeColor = i;
    }

    public void setStrokeWidth(int i) {
        this.mStrokeWidth = i;
    }
}
