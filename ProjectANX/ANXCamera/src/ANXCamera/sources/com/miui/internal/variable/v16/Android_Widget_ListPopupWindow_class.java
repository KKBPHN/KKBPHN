package com.miui.internal.variable.v16;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Spinner;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.variable.VariableExceptionHandler;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Widget_ListPopupWindow.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_ListPopupWindow.Interface;
import miui.R;
import miui.reflect.Field;
import miui.reflect.ReflectionException;
import miui.util.AttributeResolver;
import miui.widget.ArrowPopupWindow;

public class Android_Widget_ListPopupWindow_class extends com.miui.internal.variable.Android_Widget_ListPopupWindow_class implements Overridable {
    private static final Field mDropDownHorizontalOffset = Field.of(ListPopupWindow.class, "mDropDownHorizontalOffset", "I");
    private static final Field mPopup = Field.of(ListPopupWindow.class, "mPopup", "Landroid/widget/PopupWindow;");
    private Interface mImpl = new Interface() {
        public void init(ListPopupWindow listPopupWindow, Context context, AttributeSet attributeSet, int i, int i2) {
            Android_Widget_ListPopupWindow_class.this.handle_init_(0, listPopupWindow, context, attributeSet, i, i2);
        }

        public void show(ListPopupWindow listPopupWindow) {
            Android_Widget_ListPopupWindow_class.this.handleShow(0, listPopupWindow);
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
        attachConstructor("(Landroid/content/Context;Landroid/util/AttributeSet;II)V");
        attachMethod("show", "()V");
    }

    /* access modifiers changed from: protected */
    public void callOriginalShow(long j, ListPopupWindow listPopupWindow) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.show(listPopupWindow);
        } else {
            originalShow(j, listPopupWindow);
        }
    }

    /* access modifiers changed from: protected */
    public void callOriginal_init_(long j, ListPopupWindow listPopupWindow, Context context, AttributeSet attributeSet, int i, int i2) {
        if (this.mOriginal == null) {
            original_init_(j, listPopupWindow, context, attributeSet, i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handle_init_(0, null, null, null, 0, 0);
        handleShow(0, null);
    }

    /* access modifiers changed from: protected */
    public void handleShow(long j, ListPopupWindow listPopupWindow) {
        boolean z;
        int i = mDropDownHorizontalOffset.getInt(listPopupWindow);
        View anchorView = listPopupWindow.getAnchorView();
        if (i == 0 || anchorView == null || !(anchorView instanceof Spinner)) {
            z = false;
        } else {
            z = true;
            mDropDownHorizontalOffset.set((Object) listPopupWindow, i - anchorView.getPaddingLeft());
        }
        callOriginalShow(j, listPopupWindow);
        if (z) {
            mDropDownHorizontalOffset.set((Object) listPopupWindow, i);
        }
    }

    /* access modifiers changed from: protected */
    public void handle_init_(long j, ListPopupWindow listPopupWindow, Context context, AttributeSet attributeSet, int i, int i2) {
        Context context2 = context;
        if (!DeviceHelper.IS_TABLET || AttributeResolver.resolve(context, R.attr.windowActionBar) < 0 || AttributeResolver.resolveBoolean(context, com.miui.internal.R.attr.hidePopupArrow, false)) {
            ListPopupWindow listPopupWindow2 = listPopupWindow;
            AttributeSet attributeSet2 = attributeSet;
            int i3 = i;
            int i4 = i2;
            callOriginal_init_(j, listPopupWindow, context, attributeSet, i, i2);
            return;
        }
        callOriginal_init_(j, listPopupWindow, context, attributeSet, 0, 0);
        try {
            AttributeSet attributeSet3 = attributeSet;
            int i5 = i;
            ArrowPopupWindow arrowPopupWindow = new ArrowPopupWindow(context, attributeSet, i, i2);
            arrowPopupWindow.setInputMethodMode(1);
            ListPopupWindow listPopupWindow3 = listPopupWindow;
            setPopupWindow(listPopupWindow, arrowPopupWindow);
        } catch (ReflectionException e) {
            VariableExceptionHandler.getInstance().onThrow("ListPopupWindow constructor", e);
        }
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public void originalShow(long j, ListPopupWindow listPopupWindow) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Widget_ListPopupWindow_class.originalShow(long, ListPopupWindow)");
    }

    /* access modifiers changed from: protected */
    public void original_init_(long j, ListPopupWindow listPopupWindow, Context context, AttributeSet attributeSet, int i, int i2) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Widget_ListPopupWindow_class.original_init_(long, ListPopupWindow, Context, AttributeSet, int, int)");
    }

    public void setPopupWindow(ListPopupWindow listPopupWindow, PopupWindow popupWindow) {
        mPopup.set((Object) listPopupWindow, (Object) popupWindow);
    }
}
