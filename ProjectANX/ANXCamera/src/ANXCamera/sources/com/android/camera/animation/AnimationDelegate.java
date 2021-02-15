package com.android.camera.animation;

import android.view.animation.Interpolator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public class AnimationDelegate {
    public static final int ANIMATE_TYPE_ALPHA_IN = 161;
    public static final int ANIMATE_TYPE_ALPHA_OUT = 162;
    public static final int ANIMATE_TYPE_NULL = -1;
    public static final int ANIMATE_TYPE_SCALE = 164;
    public static final int ANIMATE_TYPE_SLIDE_IN_BOTTOM = 167;
    public static final int ANIMATE_TYPE_SLIDE_LEFT = 165;
    public static final int ANIMATE_TYPE_SLIDE_OUT_BOTTOM = 168;
    public static final int ANIMATE_TYPE_SLIDE_RIGHT = 166;
    public static final int ANIMATE_TYPE_TRANSITION = 163;
    public static final int DEFAULT_ANIMATION_DURATION = 200;
    public static final Interpolator DEFAULT_INTERPOLATOR = new CubicEaseOutInterpolator();

    public interface AnimationResource {
        public static final int DATA_CHANGE_TYPE_CAMERA_ID = 2;
        public static final int DATA_CHANGE_TYPE_NULL = 1;
        public static final int DATA_CHANGE_TYPE_UI_STYLE = 3;
        public static final int LEFT_LANDSCAPE = 1;
        public static final int PORTRAIT = 0;
        public static final int RIGHT_LANDSCAPE = 2;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DataChangeType {
        }

        public @interface ScreenOrientation {
        }

        boolean canProvide();

        boolean isEnableClick();

        boolean needViewClear();

        void notifyAfterFrameAvailable(int i);

        void notifyDataChanged(int i, int i2);

        void provideAnimateElement(int i, List list, int i2);

        void provideOrientationChanged(@ScreenOrientation int i, List list, int i2);

        void provideRotateItem(List list, int i);

        void setClickEnable(boolean z);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }
}
