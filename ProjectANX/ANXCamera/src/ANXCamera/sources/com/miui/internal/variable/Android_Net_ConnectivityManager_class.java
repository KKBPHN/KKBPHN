package com.miui.internal.variable;

import android.net.ConnectivityManager;
import miui.util.SoftReferenceSingleton;

public abstract class Android_Net_ConnectivityManager_class {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Net_ConnectivityManager_class Android_Net_ConnectivityManager_class;

        private Factory() {
            this.Android_Net_ConnectivityManager_class = (Android_Net_ConnectivityManager_class) create("Android_Net_ConnectivityManager_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Net_ConnectivityManager_class get() {
            return this.Android_Net_ConnectivityManager_class;
        }
    }

    public abstract boolean isNetworkSupported(ConnectivityManager connectivityManager, int i);
}
