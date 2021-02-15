package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;
import com.ss.android.vesdk.VEResult;

public class HistogramView extends View {
    private static final int BINS = 256;
    private static final int MODE_RGB = 16;
    private static final int MODE_Y = 1;
    private int mBlueColor = -16776961;
    private int[] mBlueHistogram = new int[256];
    private int mGreenColor = -16711936;
    private int[] mGreenHistogram = new int[256];
    private int mMode = 17;
    private int mOrientation;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private int mRedColor = -65536;
    private int[] mRedHistogram = new int[256];
    private int mYColor = -1;
    private int[] mYHistogram = new int[256];

    public HistogramView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void drawHistogram(Canvas canvas, int i, int[] iArr, int i2, Mode mode) {
        float width = (float) getWidth();
        float height = (float) getHeight();
        float length = width / ((float) iArr.length);
        float f = height / ((float) i);
        this.mPaint.reset();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(i2);
        this.mPaint.setXfermode(new PorterDuffXfermode(mode));
        this.mPath.reset();
        this.mPath.moveTo(0.0f, height);
        boolean z = false;
        float f2 = 0.0f;
        float f3 = 0.0f;
        for (int i3 = 0; i3 < iArr.length; i3++) {
            float f4 = (((float) i3) * length) + 0.0f;
            float f5 = ((float) iArr[i3]) * f;
            if (f5 != 0.0f) {
                float f6 = height - ((f3 + f5) / 2.0f);
                if (!z) {
                    this.mPath.lineTo(f4, height);
                    z = true;
                }
                this.mPath.lineTo(f4, f6);
                f2 = f4;
                f3 = f5;
            }
        }
        this.mPath.lineTo(f2, height);
        this.mPath.lineTo(width, height);
        this.mPath.close();
        canvas.drawPath(this.mPath, this.mPaint);
    }

    public void onDraw(Canvas canvas) {
        int i = 1;
        for (int i2 = 0; i2 < 256; i2++) {
            i = Math.max(this.mBlueHistogram[i2], Math.max(this.mGreenHistogram[i2], Math.max(this.mRedHistogram[i2], Math.max(this.mYHistogram[i2], i))));
        }
        if ((1 & this.mMode) != 0) {
            drawHistogram(canvas, i, this.mYHistogram, this.mYColor, Mode.SCREEN);
        }
    }

    public void refresh() {
        invalidate();
    }

    public void setOrientation(int i, boolean z) {
        int i2 = this.mOrientation;
        if (i2 != i) {
            int i3 = i - i2 > 90 ? i + VEResult.TER_EGL_BAD_MATCH : i;
            this.mOrientation = i;
            if (z) {
                ViewCompat.animate(this).setDuration(300).rotation((float) i3);
            } else {
                setRotation((float) this.mOrientation);
            }
        }
    }

    public void updateStats(int[] iArr) {
        System.arraycopy(iArr, 0, this.mYHistogram, 0, 256);
    }

    public void updateStats(int[] iArr, int[] iArr2, int[] iArr3) {
        System.arraycopy(iArr, 0, this.mRedHistogram, 0, 256);
        System.arraycopy(iArr2, 0, this.mGreenHistogram, 0, 256);
        System.arraycopy(iArr3, 0, this.mBlueHistogram, 0, 256);
    }
}
