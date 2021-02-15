package com.miui.internal.variable.v23;

import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import com.miui.internal.R;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Widget_PopupWindow.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_PopupWindow.Interface;
import miui.reflect.Field;
import miui.util.AttributeResolver;

public class Android_Widget_PopupWindow_class extends com.miui.internal.variable.v21.Android_Widget_PopupWindow_class implements Overridable {
    private static final int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 8192;
    private static final Field mBackgroundViewField = Field.of(PopupWindow.class, "mBackgroundView", "Landroid/view/View;");
    private static final Field mDecorViewField = Field.of(PopupWindow.class, "mDecorView", "Landroid/widget/PopupWindow$PopupDecorView;");
    private static final Field mLayoutInsetDecorField = Field.of(PopupWindow.class, "mLayoutInsetDecor", "Z");
    private Interface mImpl = new Interface() {
        public void invokePopup(PopupWindow popupWindow, LayoutParams layoutParams) {
            Android_Widget_PopupWindow_class.this.handleInvokePopup(0, popupWindow, layoutParams);
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
    public void callOriginalInvokePopup(long j, PopupWindow popupWindow, LayoutParams layoutParams) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.invokePopup(popupWindow, layoutParams);
        } else {
            super.callOriginalInvokePopup(j, popupWindow, layoutParams);
        }
    }

    /* access modifiers changed from: protected */
    public void handleInvokePopup(long j, PopupWindow popupWindow, LayoutParams layoutParams) {
        super.handleInvokePopup(j, popupWindow, layoutParams);
        int resolveInt = AttributeResolver.resolveInt(popupWindow.getContentView().getContext(), R.attr.windowTranslucentStatus, 1);
        boolean z = mLayoutInsetDecorField.getBoolean(popupWindow);
        View view = (View) mBackgroundViewField.get(popupWindow);
        if (resolveInt != 0 && z && view != null) {
            ((View) mDecorViewField.get(popupWindow)).setFitsSystemWindows(!z);
            view.setFitsSystemWindows(z);
            view.setSystemUiVisibility(resolveInt == 1 ? 8192 : view.getSystemUiVisibility() & -8193);
        }
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }
}
