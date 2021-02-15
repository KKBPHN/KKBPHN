package com.miui.internal.variable;

import android.widget.Spinner;
import com.miui.internal.util.ClassProxy;
import miui.util.SoftReferenceSingleton;
import miui.widget.ArrowPopupWindow;

public abstract class Android_Widget_Spinner_class extends ClassProxy implements IManagedClassProxy {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Widget_Spinner_class Android_Widget_Spinner_class;

        private Factory() {
            this.Android_Widget_Spinner_class = (Android_Widget_Spinner_class) create("Android_Widget_Spinner_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Widget_Spinner_class get() {
            return this.Android_Widget_Spinner_class;
        }
    }

    public Android_Widget_Spinner_class() {
        super(Spinner.class);
    }

    public void buildProxy() {
        attachMethod("setPrompt", "(Ljava/lang/CharSequence;)V");
    }

    /* access modifiers changed from: protected */
    public void callOriginalSetPrompt(long j, Spinner spinner, CharSequence charSequence) {
        originalSetPrompt(j, spinner, charSequence);
    }

    public abstract ArrowPopupWindow getArrowPopupWindow(Spinner spinner);

    /* access modifiers changed from: protected */
    public void handle() {
        handleSetPrompt(0, null, null);
    }

    /* access modifiers changed from: protected */
    public void handleSetPrompt(long j, Spinner spinner, CharSequence charSequence) {
        ArrowPopupWindow arrowPopupWindow = getArrowPopupWindow(spinner);
        if (arrowPopupWindow != null) {
            arrowPopupWindow.setTitle(charSequence);
        } else {
            callOriginalSetPrompt(j, spinner, charSequence);
        }
    }

    /* access modifiers changed from: protected */
    public void originalSetPrompt(long j, Spinner spinner, CharSequence charSequence) {
        throw new IllegalStateException("com.miui.internal.variable.Android_Widget_Spinner_class.originalSetPrompt(long, Spinner, CharSequence)");
    }
}
