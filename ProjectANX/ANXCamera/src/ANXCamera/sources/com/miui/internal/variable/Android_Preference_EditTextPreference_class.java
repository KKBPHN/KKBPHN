package com.miui.internal.variable;

import android.preference.EditTextPreference;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.miui.internal.R;
import com.miui.internal.util.ClassProxy;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Preference_EditTextPreference.Extension;
import com.miui.internal.variable.api.v29.Android_Preference_EditTextPreference.Interface;
import miui.util.SoftReferenceSingleton;

public class Android_Preference_EditTextPreference_class extends ClassProxy implements IManagedClassProxy, Overridable {
    private Interface mImpl = new Interface() {
        public void onAddEditTextToDialogView(EditTextPreference editTextPreference, View view, EditText editText) {
            Android_Preference_EditTextPreference_class.this.handleOnAddEditTextToDialogView(0, editTextPreference, view, editText);
        }
    };
    private Interface mOriginal;

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Preference_EditTextPreference_class Android_Preference_EditTextPreference_class;

        private Factory() {
            this.Android_Preference_EditTextPreference_class = new Android_Preference_EditTextPreference_class();
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Preference_EditTextPreference_class get() {
            return this.Android_Preference_EditTextPreference_class;
        }
    }

    public Android_Preference_EditTextPreference_class() {
        super(EditTextPreference.class);
    }

    public Interface asInterface() {
        return this.mImpl;
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    public void buildProxy() {
        attachMethod("onAddEditTextToDialogView", "(Landroid/view/View;Landroid/widget/EditText;)V");
    }

    /* access modifiers changed from: protected */
    public void callOriginalOnAddEditTextToDialogView(long j, EditTextPreference editTextPreference, View view, EditText editText) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.onAddEditTextToDialogView(editTextPreference, view, editText);
        } else {
            originalOnAddEditTextToDialogView(j, editTextPreference, view, editText);
        }
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleOnAddEditTextToDialogView(0, null, null, null);
    }

    /* access modifiers changed from: protected */
    public void handleOnAddEditTextToDialogView(long j, EditTextPreference editTextPreference, View view, EditText editText) {
        callOriginalOnAddEditTextToDialogView(j, editTextPreference, view, editText);
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.edittext_container);
        if (viewGroup != null) {
            viewGroup.addView(editText, -1, -2);
        }
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public void originalOnAddEditTextToDialogView(long j, EditTextPreference editTextPreference, View view, EditText editText) {
        throw new IllegalStateException("com.miui.internal.variable.Android_Preference_EditTextPreference_class.originalOnAddEditTextToDialogView(long, EditTextPreference, View, EditText)");
    }
}
