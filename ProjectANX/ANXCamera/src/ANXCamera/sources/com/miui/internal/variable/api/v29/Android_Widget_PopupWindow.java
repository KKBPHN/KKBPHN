package com.miui.internal.variable.api.v29;

import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_Widget_PopupWindow {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        void invokePopup(PopupWindow popupWindow, LayoutParams layoutParams);
    }
}
