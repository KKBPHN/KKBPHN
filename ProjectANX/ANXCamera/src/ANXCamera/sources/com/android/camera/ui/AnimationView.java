package com.android.camera.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.android.camera.log.Log;

public class AnimationView extends AppCompatImageView {
    private static final String TAG = "AnimationView";
    private final Rect bitmapRect;
    private float cornersState;
    private Bitmap drawBitmap;
    private final Path path;
    private final RectF viewRect;

    public AnimationView(Context context) {
        this(context, null);
    }

    public AnimationView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AnimationView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.viewRect = new RectF();
        this.bitmapRect = new Rect();
        this.path = new Path();
    }

    public void clear() {
        this.cornersState = 0.0f;
        this.path.reset();
    }

    public void draw(Canvas canvas) {
        if (this.cornersState == 0.0f) {
            super.draw(canvas);
            return;
        }
        canvas.clipPath(this.path);
        canvas.drawBitmap(this.drawBitmap, this.bitmapRect, this.viewRect, null);
    }

    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        this.drawBitmap = bitmap;
        this.bitmapRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        StringBuilder sb = new StringBuilder();
        sb.append("setImageBitmap: bitmapRect = ");
        sb.append(this.bitmapRect);
        sb.append(", view size = ");
        sb.append(getWidth());
        sb.append("x");
        sb.append(getHeight());
        Log.d(TAG, sb.toString());
    }

    public void setProgress(float f) {
        this.cornersState = f;
        float width = this.viewRect.width();
        float height = this.viewRect.height();
        float min = Math.min(width, height) * 0.5f * this.cornersState;
        this.path.reset();
        float abs = Math.abs(width - height) / 2.0f;
        if (width > height) {
            Path path2 = this.path;
            float f2 = this.cornersState;
            path2.addRoundRect(abs * f2, 0.0f, width - (abs * f2), height, min, min, Direction.CW);
        } else {
            Path path3 = this.path;
            float f3 = this.cornersState;
            float f4 = abs * f3;
            float f5 = height - (abs * f3);
            path3.addRoundRect(0.0f, f4, width, f5, min, min, Direction.CW);
        }
        this.path.close();
        invalidate();
    }

    public void setUp(RectF rectF) {
        this.viewRect.set(rectF);
        this.viewRect.offset(-rectF.left, -rectF.top);
        StringBuilder sb = new StringBuilder();
        sb.append("setUp: start = ");
        sb.append(this.viewRect);
        sb.append(", bitmapRect = ");
        sb.append(this.bitmapRect);
        Log.d(TAG, sb.toString());
    }
}
