package miuix.view.animation;

import android.view.animation.Interpolator;

public class ExponentialEaseOutInterpolator implements Interpolator {
    public float getInterpolation(float f) {
        if (f == 1.0f) {
            return 1.0f;
        }
        return (float) ((-Math.pow(2.0d, (double) (f * -10.0f))) + 1.0d);
    }
}
