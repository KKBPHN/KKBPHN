package com.miui.internal.variable.api.v29;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_View_View {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        void init(View view, Context context, AttributeSet attributeSet, int i, int i2);

        int[] onCreateDrawableState(View view, int i);

        void refreshDrawableState(View view);
    }
}
