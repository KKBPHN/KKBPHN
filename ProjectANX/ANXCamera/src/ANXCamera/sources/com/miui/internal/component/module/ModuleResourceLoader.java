package com.miui.internal.component.module;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.ResourcesImpl;
import android.os.Build.VERSION;
import com.miui.internal.util.PackageConstants;
import com.miui.internal.variable.Android_App_ActivityThread_class;
import com.miui.internal.variable.Android_App_LoadedApk_class;
import com.miui.internal.variable.Android_App_ResourcesManager_class;
import com.miui.internal.variable.Android_Content_Res_AssetManager_class;
import com.miui.internal.variable.Android_Content_Res_AssetManager_class.Factory;
import com.miui.internal.variable.Android_Content_Res_ResourcesImpl_class;
import com.miui.internal.variable.Android_Content_Res_Resources_class;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ModuleResourceLoader {
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.String>, for r8v0, types: [java.util.List, java.util.List<java.lang.String>] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.String>, for r9v0, types: [java.util.List, java.util.List<java.lang.String>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void addAssetPath(Resources resources, String str, List<String> list, List<String> list2) {
        Map map;
        Android_Content_Res_AssetManager_class android_Content_Res_AssetManager_class = Factory.getInstance().get();
        AssetManager assets = resources.getAssets();
        if (VERSION.SDK_INT < 21) {
            assets = android_Content_Res_AssetManager_class.newInstance();
            android_Content_Res_AssetManager_class.addAssetPath(assets, str);
            if (list != null) {
                for (String addAssetPath : list) {
                    android_Content_Res_AssetManager_class.addAssetPath(assets, addAssetPath);
                }
            }
        }
        for (String str2 : list2) {
            try {
                Android_Content_Res_ResourcesImpl_class android_Content_Res_ResourcesImpl_class = Android_Content_Res_ResourcesImpl_class.Factory.getInstance().get();
                Object resourcesManagerInstance = getResourcesManagerInstance();
                Field declaredField = resourcesManagerInstance.getClass().getDeclaredField("mResourceImpls");
                declaredField.setAccessible(true);
                synchronized (resourcesManagerInstance) {
                    map = (Map) declaredField.get(resourcesManagerInstance);
                    Android_App_ResourcesManager_class.appendAssetPath(str2);
                }
                if (map != null) {
                    for (Entry value : map.entrySet()) {
                        WeakReference weakReference = (WeakReference) value.getValue();
                        if (weakReference.get() != null) {
                            AssetManager assets2 = android_Content_Res_ResourcesImpl_class.getAssets((ResourcesImpl) weakReference.get());
                            if (assets2 != assets) {
                                android_Content_Res_AssetManager_class.addAssetPath(assets2, str2);
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException unused) {
            }
            android_Content_Res_AssetManager_class.addAssetPath(assets, str2);
        }
        if (VERSION.SDK_INT < 21) {
            replaceAssetManager(resources, assets);
        }
    }

    private static Object getResourcesManagerInstance() {
        Method declaredMethod = Class.forName("android.app.ResourcesManager").getDeclaredMethod("getInstance", new Class[0]);
        declaredMethod.setAccessible(true);
        return declaredMethod.invoke(null, new Object[0]);
    }

    public static void load(List list) {
        load(list, null);
    }

    public static void load(List list, List list2) {
        if (PackageConstants.sSdkStatus < 2) {
            load2Initial(list, list2);
        } else {
            load2Current(list, list2, PackageConstants.sApplication);
        }
    }

    public static void load2Current(List list, List list2, Context context) {
        addAssetPath(context.getResources(), context.getApplicationInfo().sourceDir, list2, list);
    }

    public static void load2Initial(List list, List list2) {
        Android_App_ActivityThread_class android_App_ActivityThread_class = Android_App_ActivityThread_class.Factory.getInstance().get();
        Object currentActivityThread = android_App_ActivityThread_class.currentActivityThread();
        Android_App_LoadedApk_class android_App_LoadedApk_class = Android_App_LoadedApk_class.Factory.getInstance().get();
        Object initialLoadedApk = android_App_ActivityThread_class.getInitialLoadedApk(currentActivityThread);
        addAssetPath(android_App_LoadedApk_class.getResources(initialLoadedApk), android_App_LoadedApk_class.getApplicationInfo(initialLoadedApk).sourceDir, list2, list);
    }

    public static void load2System(List list, List list2) {
        addAssetPath(Resources.getSystem(), null, list2, list);
    }

    private static void replaceAssetManager(Resources resources, AssetManager assetManager) {
        Resources resources2 = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        AssetManager assets = resources.getAssets();
        Android_Content_Res_Resources_class android_Content_Res_Resources_class = Android_Content_Res_Resources_class.Factory.getInstance().get();
        android_Content_Res_Resources_class.setAssetManager(resources, assetManager);
        android_Content_Res_Resources_class.setAssetManager(resources2, null);
        assets.close();
        resources.updateConfiguration(resources.getConfiguration(), resources.getDisplayMetrics());
    }
}
