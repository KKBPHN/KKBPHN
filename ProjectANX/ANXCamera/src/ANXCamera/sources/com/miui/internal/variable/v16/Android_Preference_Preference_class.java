package com.miui.internal.variable.v16;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.AttributeSet;
import android.view.View;
import com.miui.internal.variable.VariableExceptionHandler;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Preference_Preference.Extension;
import com.miui.internal.variable.api.v29.Android_Preference_Preference.Interface;
import miui.reflect.Field;

public class Android_Preference_Preference_class extends com.miui.internal.variable.Android_Preference_Preference_class implements Overridable {
    private static final Field mPreferenceScreen = Field.of(PreferenceManager.class, "mPreferenceScreen", "Landroid/preference/PreferenceScreen;");
    private Interface mImpl = new Interface() {
        public void init(Preference preference, Context context, AttributeSet attributeSet, int i) {
            Android_Preference_Preference_class.this.handle_init_(0, preference, context, attributeSet, i);
        }

        public void onBindView(Preference preference, View view) {
            Android_Preference_Preference_class.this.handleOnBindView(0, preference, view);
        }
    };
    private Interface mOriginal;

    public Interface asInterface() {
        return this.mImpl;
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    /* access modifiers changed from: protected */
    public void callOriginalOnBindView(long j, Preference preference, View view) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.onBindView(preference, view);
        } else {
            super.callOriginalOnBindView(j, preference, view);
        }
    }

    /* access modifiers changed from: protected */
    public void callOriginal_init_(long j, Preference preference, Context context, AttributeSet attributeSet, int i) {
        if (this.mOriginal == null) {
            super.callOriginal_init_(j, preference, context, attributeSet, i);
        }
    }

    /* access modifiers changed from: protected */
    public PreferenceScreen getPreferenceScreen(Preference preference) {
        try {
            return (PreferenceScreen) mPreferenceScreen.get(preference.getPreferenceManager());
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("mPreferenceScreen", e);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }
}
