package com.miui.internal.variable;

import com.miui.internal.util.ClassProxy;
import java.util.ArrayList;
import java.util.List;

public abstract class Android_App_ResourcesManager_class extends ClassProxy implements IManagedClassProxy {
    protected static final Class ResourcsManager_Class = getClass("android.app.ResourcesManager");
    protected static List mAppendedAssetPaths;

    public class Factory extends AbsClassFactory {
        private Android_App_ResourcesManager_class Android_App_ResourcesManager_class;

        final class Holder {
            static final Factory INSTANCE = new Factory();

            private Holder() {
            }
        }

        private Factory() {
            this.Android_App_ResourcesManager_class = (Android_App_ResourcesManager_class) create("Android_App_ResourcesManager_class");
        }

        public static Factory getInstance() {
            return Holder.INSTANCE;
        }

        public Android_App_ResourcesManager_class get() {
            return this.Android_App_ResourcesManager_class;
        }
    }

    public Android_App_ResourcesManager_class() {
        super(ResourcsManager_Class);
    }

    public static void appendAssetPath(String str) {
        if (mAppendedAssetPaths == null) {
            mAppendedAssetPaths = new ArrayList();
        }
        for (String equals : mAppendedAssetPaths) {
            if (equals.equals(str)) {
                return;
            }
        }
        mAppendedAssetPaths.add(str);
    }

    protected static Class getClass(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            VariableExceptionHandler.getInstance().onThrow("android.app.ResourcesManager", e);
            return null;
        }
    }
}
