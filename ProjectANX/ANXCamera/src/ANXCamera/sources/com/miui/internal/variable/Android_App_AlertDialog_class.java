package com.miui.internal.variable;

import android.app.AlertDialog;
import com.miui.internal.util.ClassProxy;
import miui.util.SoftReferenceSingleton;

public abstract class Android_App_AlertDialog_class extends ClassProxy implements IManagedClassProxy {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_App_AlertDialog_class Android_App_AlertDialog_class;

        private Factory() {
            this.Android_App_AlertDialog_class = (Android_App_AlertDialog_class) create("Android_App_AlertDialog_class");
            com.miui.internal.variable.Android_App_AlertDialog_Builder_class.Factory.getInstance().getClass();
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_App_AlertDialog_class get() {
            return this.Android_App_AlertDialog_class;
        }
    }

    public Android_App_AlertDialog_class() {
        super(AlertDialog.class);
    }
}
