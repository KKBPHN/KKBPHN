package com.miui.internal.variable.api.v29;

import android.app.AlertDialog.Builder;
import android.content.Context;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_App_AlertDialog_Builder {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        void init(Builder builder, Context context, int i);
    }
}
