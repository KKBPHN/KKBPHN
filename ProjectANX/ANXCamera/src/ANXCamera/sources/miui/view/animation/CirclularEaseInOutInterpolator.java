package miui.view.animation;

import android.view.animation.Interpolator;

public class CirclularEaseInOutInterpolator implements Interpolator {
    public float getInterpolation(float f) {
        float f2;
        double sqrt;
        float f3 = f * 2.0f;
        if (f3 < 1.0f) {
            f2 = -0.5f;
            sqrt = Math.sqrt((double) (1.0f - (f3 * f3))) - 1.0d;
        } else {
            float f4 = f3 - 2.0f;
            f2 = 0.5f;
            sqrt = Math.sqrt((double) (1.0f - (f4 * f4))) + 1.0d;
        }
        return ((float) sqrt) * f2;
    }
}
