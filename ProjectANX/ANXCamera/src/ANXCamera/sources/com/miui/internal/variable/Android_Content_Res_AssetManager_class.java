package com.miui.internal.variable;

import android.content.res.AssetManager;
import miui.util.SoftReferenceSingleton;

public interface Android_Content_Res_AssetManager_class extends IManagedClassProxy {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Content_Res_AssetManager_class Android_Content_Res_AssetManager_class;

        private Factory() {
            this.Android_Content_Res_AssetManager_class = (Android_Content_Res_AssetManager_class) create("Android_Content_Res_AssetManager_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Content_Res_AssetManager_class get() {
            return this.Android_Content_Res_AssetManager_class;
        }
    }

    int addAssetPath(AssetManager assetManager, String str);

    AssetManager newInstance();
}
