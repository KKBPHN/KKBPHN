package com.miui.internal.variable;

import android.app.Activity;
import android.os.Bundle;
import miui.util.SoftReferenceSingleton;

public abstract class Android_App_PreferenceActivity_class {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_App_PreferenceActivity_class Android_App_PreferenceActivity_class;

        private Factory() {
            this.Android_App_PreferenceActivity_class = (Android_App_PreferenceActivity_class) create("Android_App_PreferenceActivity_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_App_PreferenceActivity_class get() {
            return this.Android_App_PreferenceActivity_class;
        }
    }

    public abstract void onCreate(Activity activity, Bundle bundle);
}
