package com.miui.internal.variable.api.v29;

import android.widget.GridView;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_Widget_GridView {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        void fillGap(GridView gridView, boolean z);

        void layoutChildren(GridView gridView);
    }
}
