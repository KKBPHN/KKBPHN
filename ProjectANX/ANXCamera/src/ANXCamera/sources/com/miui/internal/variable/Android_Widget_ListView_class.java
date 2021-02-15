package com.miui.internal.variable;

import android.widget.ListView;
import com.miui.internal.util.ClassProxy;
import miui.util.SoftReferenceSingleton;

public abstract class Android_Widget_ListView_class extends ClassProxy implements IManagedClassProxy {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Widget_ListView_class Android_Widget_ListView_class;

        private Factory() {
            this.Android_Widget_ListView_class = (Android_Widget_ListView_class) create("Android_Widget_ListView_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Widget_ListView_class get() {
            return this.Android_Widget_ListView_class;
        }
    }

    public Android_Widget_ListView_class() {
        super(ListView.class);
    }
}
