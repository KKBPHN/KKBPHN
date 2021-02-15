package com.miui.internal.variable.api.v29;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_Widget_ListView {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        void fillGap(ListView listView, boolean z);

        void init(ListView listView, Context context, AttributeSet attributeSet, int i, int i2);

        void layoutChildren(ListView listView);
    }
}
