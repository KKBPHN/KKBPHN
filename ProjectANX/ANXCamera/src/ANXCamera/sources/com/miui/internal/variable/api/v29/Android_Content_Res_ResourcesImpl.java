package com.miui.internal.variable.api.v29;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.ResourcesImpl;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_Content_Res_ResourcesImpl {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        Drawable loadDrawable(ResourcesImpl resourcesImpl, Resources resources, TypedValue typedValue, int i, int i2, Theme theme);
    }
}
