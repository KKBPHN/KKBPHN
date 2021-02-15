package com.miui.internal.variable;

import android.widget.PopupWindow;
import com.miui.internal.util.ClassProxy;
import miui.reflect.Method;
import miui.util.SoftReferenceSingleton;

public abstract class Android_Widget_PopupWindow_class extends ClassProxy implements IManagedClassProxy {
    protected static Method setLayoutInScreenEnabled;
    protected static Method setLayoutInsetDecor;

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Widget_PopupWindow_class Android_Widget_PopupWindow_class;

        private Factory() {
            this.Android_Widget_PopupWindow_class = (Android_Widget_PopupWindow_class) create("Android_Widget_PopupWindow_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Widget_PopupWindow_class get() {
            return this.Android_Widget_PopupWindow_class;
        }
    }

    static {
        String str = "(Z)V";
        try {
            setLayoutInScreenEnabled = Method.of(PopupWindow.class, "setLayoutInScreenEnabled", str);
            setLayoutInsetDecor = Method.of(PopupWindow.class, "setLayoutInsetDecor", str);
        } catch (Exception e) {
            VariableExceptionHandler.getInstance().onThrow("no such method", e);
        }
    }

    public Android_Widget_PopupWindow_class() {
        super(PopupWindow.class);
    }

    public void buildProxy() {
    }

    public abstract void setLayoutInScreenEnabled(PopupWindow popupWindow, boolean z);

    public abstract void setLayoutInsetDecor(PopupWindow popupWindow, boolean z);
}
