package com.miui.internal.variable.api.v29;

import android.widget.AbsSpinner;
import android.widget.SpinnerAdapter;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_Widget_AbsSpinner {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        void setAdapter(AbsSpinner absSpinner, SpinnerAdapter spinnerAdapter);
    }
}
