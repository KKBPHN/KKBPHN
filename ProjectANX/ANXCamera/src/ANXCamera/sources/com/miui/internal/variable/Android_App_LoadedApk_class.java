package com.miui.internal.variable;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import miui.util.SoftReferenceSingleton;

public interface Android_App_LoadedApk_class {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_App_LoadedApk_class Android_App_LoadedApk_class;

        private Factory() {
            this.Android_App_LoadedApk_class = (Android_App_LoadedApk_class) create("Android_App_LoadedApk_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_App_LoadedApk_class get() {
            return this.Android_App_LoadedApk_class;
        }
    }

    ApplicationInfo getApplicationInfo(Object obj);

    Resources getResources(Object obj);
}
