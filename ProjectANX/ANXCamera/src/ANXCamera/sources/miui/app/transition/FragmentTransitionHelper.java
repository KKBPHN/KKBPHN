package miui.app.transition;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import miui.animation.utils.EaseManager;
import miui.animation.utils.EaseManager.InterpolateEaseStyle;

class FragmentTransitionHelper {
    private static final int DEFAULT_APP_TRANSITION_ROUND_CORNER_RADIUS = 60;
    public static final int DEFAULT_FRAGMENT_TRANSITION_DUR = 350;
    public static final TimeInterpolator PHYSIC_BASED_INTERPOLATOR = EaseManager.getInterpolator(STYLE);
    private static InterpolateEaseStyle STYLE = new InterpolateEaseStyle(0);
    private static final String TAG = "FragmentTransition";
    private static int mTopRoundedCorner = -1;

    static {
        STYLE.setFactors(0.9f, 0.857f);
    }

    public FragmentTransitionHelper(Activity activity) {
        initDisplayTopRoundCorner(activity);
    }

    private static void initDisplayTopRoundCorner(Context context) {
        if (mTopRoundedCorner < 0) {
            int i = 0;
            Resources resources = context.getResources();
            int identifier = resources.getIdentifier("rounded_corner_radius_top", "dimen", "android");
            if (identifier > 0) {
                i = resources.getDimensionPixelSize(identifier);
            }
            if (i < 0) {
                i = 60;
                Log.d(TAG, "top radius:use default round corner Radius");
            }
            mTopRoundedCorner = i;
        }
    }

    public int getBottomRoundedCorner() {
        return 0;
    }

    public int getTopRoundedCorner() {
        return mTopRoundedCorner;
    }
}
