package com.miui.internal.variable.api.v29;

import android.view.MotionEvent;
import android.widget.AbsSeekBar;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_Widget_AbsSeekBar {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        boolean onTouchEvent(AbsSeekBar absSeekBar, MotionEvent motionEvent);
    }
}
