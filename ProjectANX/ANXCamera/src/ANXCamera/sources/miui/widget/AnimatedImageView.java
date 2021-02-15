package miui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.miui.internal.variable.Android_Graphics_Drawable_AnimatedRotateDrawable_class;
import com.miui.internal.variable.Android_Graphics_Drawable_AnimatedRotateDrawable_class.Factory;

public class AnimatedImageView extends ImageView {
    private static final Android_Graphics_Drawable_AnimatedRotateDrawable_class AnimatedRotateDrawableClass = Factory.getInstance().get();
    private static final int DEFAULT_FRAMES_COUNT = 56;
    private static final int DEFAULT_FRAMES_DURATION = 32;
    private Drawable mDrawable;
    private boolean mIsAnimating;

    public AnimatedImageView(Context context) {
        super(context);
    }

    public AnimatedImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void start() {
        Drawable drawable = this.mDrawable;
        if (drawable != null && drawable.getClass().getName().equals(Android_Graphics_Drawable_AnimatedRotateDrawable_class.NAME)) {
            AnimatedRotateDrawableClass.start(this.mDrawable);
        }
    }

    private void stop() {
        Drawable drawable = this.mDrawable;
        if (drawable != null && drawable.getClass().getName().equals(Android_Graphics_Drawable_AnimatedRotateDrawable_class.NAME)) {
            AnimatedRotateDrawableClass.stop(this.mDrawable);
        }
    }

    private void updateAnimating() {
        if (this.mDrawable == null) {
            return;
        }
        if (!isShown() || !this.mIsAnimating) {
            stop();
        } else {
            start();
        }
    }

    private void updateDrawable() {
        if (isShown()) {
            stop();
        }
        Drawable drawable = getDrawable();
        if (drawable.getClass().getName().equals(Android_Graphics_Drawable_AnimatedRotateDrawable_class.NAME)) {
            this.mDrawable = drawable;
            AnimatedRotateDrawableClass.setFramesCount(drawable, 56);
            AnimatedRotateDrawableClass.setFramesDuration(drawable, 32);
        } else {
            this.mDrawable = null;
        }
        updateAnimating();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateAnimating();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        updateAnimating();
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        updateAnimating();
    }

    public void setAnimating(boolean z) {
        this.mIsAnimating = z;
        updateAnimating();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        updateDrawable();
    }

    public void setImageResource(int i) {
        super.setImageResource(i);
        updateDrawable();
    }
}
