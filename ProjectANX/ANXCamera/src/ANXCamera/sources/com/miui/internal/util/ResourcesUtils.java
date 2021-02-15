package com.miui.internal.util;

import android.content.res.AssetManager;
import android.content.res.Resources;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ResourcesUtils {
    private static Method ASSET_MANAGER_ADD_ASSET_PATH;
    private static Constructor ASSET_MANAGER_CONSTRUCTOR;

    static {
        try {
            ASSET_MANAGER_ADD_ASSET_PATH = AssetManager.class.getMethod("addAssetPath", new Class[]{String.class});
            ASSET_MANAGER_CONSTRUCTOR = AssetManager.class.getConstructor(new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private ResourcesUtils() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x003e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Resources createResources(Resources resources, String... strArr) {
        AssetManager assetManager;
        try {
            assetManager = (AssetManager) ASSET_MANAGER_CONSTRUCTOR.newInstance(new Object[0]);
            try {
                for (String str : strArr) {
                    ASSET_MANAGER_ADD_ASSET_PATH.invoke(assetManager, new Object[]{str});
                }
            } catch (InstantiationException e) {
                e = e;
                e.printStackTrace();
                if (resources == null) {
                }
            } catch (IllegalAccessException e2) {
                e = e2;
                e.printStackTrace();
                if (resources == null) {
                }
            } catch (InvocationTargetException e3) {
                e = e3;
                e.printStackTrace();
                if (resources == null) {
                }
            }
        } catch (InstantiationException e4) {
            e = e4;
            AssetManager assetManager2 = null;
            e.printStackTrace();
            if (resources == null) {
            }
        } catch (IllegalAccessException e5) {
            e = e5;
            AssetManager assetManager3 = null;
            e.printStackTrace();
            if (resources == null) {
            }
        } catch (InvocationTargetException e6) {
            e = e6;
            assetManager = null;
            e.printStackTrace();
            if (resources == null) {
            }
        }
        return resources == null ? new Resources(assetManager, null, null) : new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
    }

    public static Resources createResources(String... strArr) {
        return createResources(null, strArr);
    }
}
