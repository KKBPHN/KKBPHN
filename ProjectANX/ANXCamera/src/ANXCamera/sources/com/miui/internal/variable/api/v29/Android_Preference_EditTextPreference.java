package com.miui.internal.variable.api.v29;

import android.preference.EditTextPreference;
import android.view.View;
import android.widget.EditText;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_Preference_EditTextPreference {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        void onAddEditTextToDialogView(EditTextPreference editTextPreference, View view, EditText editText);
    }
}
