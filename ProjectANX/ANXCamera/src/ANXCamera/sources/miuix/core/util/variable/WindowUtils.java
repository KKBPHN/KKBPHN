package miuix.core.util.variable;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class WindowUtils {
    private WindowUtils() {
    }

    public static void changeWindowBackground(Activity activity, float f) {
        LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.alpha = f;
        activity.getWindow().setAttributes(attributes);
    }

    public static void setTranslucentStatus(Window window, int i) {
        WindowWrapper.setTranslucentStatus(window, i);
    }
}
