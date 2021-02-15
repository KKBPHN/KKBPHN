package com.miui.internal.variable.api.v29;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_View_ViewGroup {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        void addInArray(ViewGroup viewGroup, View view, int i);

        void init(ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i, int i2);

        void onChildVisibilityChanged(ViewGroup viewGroup, View view, int i, int i2);

        void removeFromArray(ViewGroup viewGroup, int i);

        void removeFromArray(ViewGroup viewGroup, int i, int i2);

        boolean resolveLayoutDirection(ViewGroup viewGroup);
    }
}
