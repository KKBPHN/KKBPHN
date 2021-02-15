package com.miui.internal.variable;

import android.content.res.AssetManager;
import android.content.res.ResourcesImpl;
import com.miui.internal.util.ClassProxy;

public abstract class Android_Content_Res_ResourcesImpl_class extends ClassProxy implements IManagedClassProxy {

    public class Factory extends AbsClassFactory {
        private Android_Content_Res_ResourcesImpl_class Android_Content_Res_ResourcesImpl_class;

        final class Holder {
            static final Factory INSTANCE = new Factory();

            private Holder() {
            }
        }

        private Factory() {
            this.Android_Content_Res_ResourcesImpl_class = (Android_Content_Res_ResourcesImpl_class) create("Android_Content_Res_ResourcesImpl_class");
        }

        public static Factory getInstance() {
            return Holder.INSTANCE;
        }

        public Android_Content_Res_ResourcesImpl_class get() {
            return this.Android_Content_Res_ResourcesImpl_class;
        }
    }

    public Android_Content_Res_ResourcesImpl_class() {
        super(ResourcesImpl.class);
    }

    public abstract AssetManager getAssets(ResourcesImpl resourcesImpl);
}
