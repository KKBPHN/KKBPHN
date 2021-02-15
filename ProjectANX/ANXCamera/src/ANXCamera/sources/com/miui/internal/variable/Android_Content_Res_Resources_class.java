package com.miui.internal.variable;

import android.content.res.AssetManager;
import android.content.res.Resources;
import com.miui.internal.util.ClassProxy;
import miui.reflect.Field;
import miui.util.SoftReferenceSingleton;

public abstract class Android_Content_Res_Resources_class extends ClassProxy implements IManagedClassProxy {
    protected static final Android_Graphics_Drawable_Drawable_class DrawableClass = com.miui.internal.variable.Android_Graphics_Drawable_Drawable_class.Factory.getInstance().get();
    protected static Field mAssets = null;

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Content_Res_Resources_class Android_Content_Res_Resources_class;

        private Factory() {
            this.Android_Content_Res_Resources_class = (Android_Content_Res_Resources_class) create("Android_Content_Res_Resources_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Content_Res_Resources_class get() {
            return this.Android_Content_Res_Resources_class;
        }
    }

    public Android_Content_Res_Resources_class() {
        super(Resources.class);
    }

    public abstract void setAssetManager(Resources resources, AssetManager assetManager);
}
