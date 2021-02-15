package miui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.miui.internal.widget.VerticalSeekBarHelper;

public class VerticalSeekBar extends SeekBar {
    private final Rect mBounds;

    public VerticalSeekBar(Context context) {
        this(context, null);
    }

    public VerticalSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842875);
    }

    public VerticalSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBounds = new Rect();
        setLayoutDirection(0);
    }

    private void drawProgress(Canvas canvas) {
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable != null) {
            canvas.save();
            canvas.rotate(-90.0f, (float) (getWidth() / 2), (float) (getHeight() / 2));
            int width = getWidth();
            int height = getHeight();
            int i = height - width;
            int i2 = (height + width) / 2;
            progressDrawable.setBounds(((-i) / 2) + getPaddingBottom(), (i / 2) + getPaddingLeft(), i2 - getPaddingTop(), i2 - getPaddingRight());
            progressDrawable.draw(canvas);
            canvas.restore();
        }
    }

    private void drawThumb(Canvas canvas) {
        Drawable thumb = getThumb();
        if (thumb != null) {
            thumb.copyBounds(this.mBounds);
            int intrinsicWidth = thumb.getIntrinsicWidth();
            int intrinsicHeight = thumb.getIntrinsicHeight();
            int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
            int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
            int thumbOffset = getThumbOffset();
            int i = (height - intrinsicHeight) + (thumbOffset * 2);
            int max = getMax();
            int calcTop = VerticalSeekBarHelper.calcTop(max > 0 ? 1.0f - (((float) getProgress()) / ((float) max)) : 0.0f, i, getPaddingTop(), thumbOffset);
            int paddingLeft = ((width - intrinsicWidth) / 2) + getPaddingLeft();
            thumb.setBounds(paddingLeft, calcTop, intrinsicWidth + paddingLeft, intrinsicHeight + calcTop);
            thumb.draw(canvas);
            thumb.setBounds(this.mBounds);
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(Canvas canvas) {
        drawProgress(canvas);
        drawThumb(canvas);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        motionEvent.setLocation(((((((float) getHeight()) - motionEvent.getY()) - ((float) getPaddingBottom())) / ((float) ((getHeight() - getPaddingTop()) - getPaddingBottom()))) * ((float) ((getWidth() - getPaddingLeft()) - getPaddingRight()))) + ((float) getPaddingLeft()), motionEvent.getY());
        return super.onTouchEvent(motionEvent);
    }
}
