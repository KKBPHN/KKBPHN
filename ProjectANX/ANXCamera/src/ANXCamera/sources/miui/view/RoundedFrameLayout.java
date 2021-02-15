package miui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RoundedFrameLayout extends FrameLayout implements ClipRoundedBounds {
    private Path mPath;

    public RoundedFrameLayout(Context context) {
        this(context, null);
    }

    public RoundedFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundedFrameLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public RoundedFrameLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void draw(Canvas canvas) {
        Path path = this.mPath;
        if (path != null) {
            canvas.clipPath(path);
        }
        super.draw(canvas);
    }

    public boolean isClipRoundedCorner() {
        Path path = this.mPath;
        return path != null && !path.isEmpty();
    }

    public void setClipRoundedBounds(RectF rectF, int i) {
        if (rectF != null) {
            this.mPath = new Path();
            float f = (float) i;
            this.mPath.addRoundRect(rectF, f, f, Direction.CCW);
        } else {
            this.mPath = null;
        }
        invalidate();
    }

    public void setClipRoundedBounds(RectF rectF, float[] fArr) {
        if (rectF != null) {
            this.mPath = new Path();
            this.mPath.addRoundRect(rectF, fArr, Direction.CCW);
        } else {
            this.mPath = null;
        }
        invalidate();
    }
}
