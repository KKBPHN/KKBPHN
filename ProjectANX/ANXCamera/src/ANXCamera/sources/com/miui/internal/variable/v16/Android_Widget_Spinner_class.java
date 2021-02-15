package com.miui.internal.variable.v16;

import android.widget.ListPopupWindow;
import android.widget.Spinner;
import com.miui.internal.variable.VariableExceptionHandler;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Widget_Spinner.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_Spinner.Interface;
import miui.reflect.Field;
import miui.widget.ArrowPopupWindow;

public class Android_Widget_Spinner_class extends com.miui.internal.variable.Android_Widget_Spinner_class implements Overridable {
    private static final Field mPopup;
    private static final Field mPopupWindow;
    private Interface mImpl = new Interface() {
        public void setPrompt(Spinner spinner, CharSequence charSequence) {
            Android_Widget_Spinner_class.this.handleSetPrompt(0, spinner, charSequence);
        }
    };
    private Interface mOriginal;

    static {
        String str = "mPopup";
        mPopup = Field.of(Spinner.class, str, "Landroid/widget/Spinner$SpinnerPopup;");
        mPopupWindow = Field.of(ListPopupWindow.class, str, "Landroid/widget/PopupWindow;");
    }

    public Interface asInterface() {
        return this.mImpl;
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    /* access modifiers changed from: protected */
    public void callOriginalSetPrompt(long j, Spinner spinner, CharSequence charSequence) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.setPrompt(spinner, charSequence);
        } else {
            super.callOriginalSetPrompt(j, spinner, charSequence);
        }
    }

    /* access modifiers changed from: protected */
    public ArrowPopupWindow getArrowPopupWindow(Spinner spinner) {
        try {
            Object obj = mPopup.get(spinner);
            if (obj instanceof ListPopupWindow) {
                Object obj2 = mPopupWindow.get(obj);
                if (obj2 instanceof ArrowPopupWindow) {
                    return (ArrowPopupWindow) obj2;
                }
            }
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("mPopup", e);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }
}
