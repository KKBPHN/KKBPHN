package com.miui.internal.variable;

import miui.util.SoftReferenceSingleton;

public abstract class Android_Os_Process_class {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Os_Process_class Android_Os_Process_class;

        private Factory() {
            this.Android_Os_Process_class = (Android_Os_Process_class) create("Android_Os_Process_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Os_Process_class get() {
            return this.Android_Os_Process_class;
        }
    }

    public abstract long getFreeMemory();

    public abstract long getTotalMemory();
}
