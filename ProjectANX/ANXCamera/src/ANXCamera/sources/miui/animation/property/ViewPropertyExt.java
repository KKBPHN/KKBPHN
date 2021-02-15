package miui.animation.property;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;
import miui.animation.utils.CommonUtils;

public class ViewPropertyExt {
    public static final BackgroundProperty BACKGROUND = new BackgroundProperty();
    public static final ForegroundProperty FOREGROUND = new ForegroundProperty();

    public class BackgroundProperty extends ViewProperty implements IIntValueProperty {
        private BackgroundProperty() {
            super("background");
        }

        public int getIntValue(View view) {
            Drawable background = view.getBackground();
            if (background instanceof ColorDrawable) {
                return ((ColorDrawable) background).getColor();
            }
            return 0;
        }

        public float getValue(View view) {
            return 0.0f;
        }

        public void setIntValue(View view, int i) {
            view.setBackgroundColor(i);
        }

        public void setValue(View view, float f) {
        }
    }

    public class ForegroundProperty extends ViewProperty implements IIntValueProperty {
        private ForegroundProperty() {
            super("foreground");
        }

        public int getIntValue(View view) {
            Object tag = view.getTag(CommonUtils.KEY_FOLME_FORGROUND_COLOR);
            if (tag instanceof Integer) {
                return ((Integer) tag).intValue();
            }
            return 0;
        }

        public float getValue(View view) {
            return 0.0f;
        }

        public void setIntValue(View view, int i) {
            view.setTag(CommonUtils.KEY_FOLME_FORGROUND_COLOR, Integer.valueOf(i));
            if (VERSION.SDK_INT >= 23) {
                Drawable foreground = view.getForeground();
                if (foreground != null) {
                    foreground.invalidateSelf();
                }
            }
        }

        public void setValue(View view, float f) {
        }
    }

    private ViewPropertyExt() {
    }
}
