package miuix.view.animation;

import android.view.animation.Interpolator;

public class BackEaseInInterpolator implements Interpolator {
    private final float mOvershot;

    public BackEaseInInterpolator() {
        this(0.0f);
    }

    public BackEaseInInterpolator(float f) {
        this.mOvershot = f;
    }

    public float getInterpolation(float f) {
        float f2 = this.mOvershot;
        if (f2 == 0.0f) {
            f2 = 1.70158f;
        }
        return f * f * (((1.0f + f2) * f) - f2);
    }
}
