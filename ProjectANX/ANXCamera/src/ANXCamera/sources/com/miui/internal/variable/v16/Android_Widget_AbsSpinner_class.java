package com.miui.internal.variable.v16;

import android.widget.AbsSpinner;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import com.miui.internal.R;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Widget_AbsSpinner.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_AbsSpinner.Interface;
import miui.util.AttributeResolver;

public class Android_Widget_AbsSpinner_class extends com.miui.internal.variable.Android_Widget_AbsSpinner_class implements Overridable {
    private Interface mImpl = new Interface() {
        public void setAdapter(AbsSpinner absSpinner, SpinnerAdapter spinnerAdapter) {
            Android_Widget_AbsSpinner_class.this.handleSetAdapter(0, absSpinner, spinnerAdapter);
        }
    };
    private Interface mOriginal;

    public Interface asInterface() {
        return this.mImpl;
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    public void buildProxy() {
        attachMethod("setAdapter", "(Landroid/widget/SpinnerAdapter;)V");
    }

    /* access modifiers changed from: protected */
    public void callOriginalSetAdapter(long j, AbsSpinner absSpinner, SpinnerAdapter spinnerAdapter) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.setAdapter(absSpinner, spinnerAdapter);
        } else {
            originalSetAdapter(j, absSpinner, spinnerAdapter);
        }
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleSetAdapter(0, null, null);
    }

    /* access modifiers changed from: protected */
    public void handleSetAdapter(long j, AbsSpinner absSpinner, SpinnerAdapter spinnerAdapter) {
        if ((spinnerAdapter instanceof ArrayAdapter) && AttributeResolver.isUsingMiuiTheme(absSpinner.getContext())) {
            ((ArrayAdapter) spinnerAdapter).setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        }
        callOriginalSetAdapter(j, absSpinner, spinnerAdapter);
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public void originalSetAdapter(long j, AbsSpinner absSpinner, SpinnerAdapter spinnerAdapter) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Widget_AbsSpinner_class.original_init_(long, AbsSpinner, Context, AttributeSet, int)");
    }
}
