package com.iqiyi.android.qigsaw.core.splitload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitCompatResourcesLoader {
    private static final String TAG = "SplitCompatResourcesLoader";
    /* access modifiers changed from: private */
    public static final Object sLock = new Object();

    class V14 extends VersionCompat {
        private V14() {
            super();
        }

        private static void checkOrUpdateResourcesForContext(Context context, Resources resources, Resources resources2) {
            String str = "mResources";
            boolean z = context instanceof ContextThemeWrapper;
            String str2 = SplitCompatResourcesLoader.TAG;
            if (z && VERSION.SDK_INT >= 17 && ((Resources) VersionCompat.mResourcesInContextThemeWrapper().get(context)) == resources) {
                SplitLog.i(str2, "context %s type is @ContextThemeWrapper, and it has its own resources instance!", context.getClass().getSimpleName());
                VersionCompat.mResourcesInContextThemeWrapper().set(context, resources2);
                VersionCompat.mThemeInContextThemeWrapper().set(context, null);
            }
            Context baseContext = getBaseContext(context);
            if (!baseContext.getClass().getName().equals("android.app.ContextImpl")) {
                try {
                    if (((Resources) HiddenApiReflection.findField((Object) baseContext, str).get(baseContext)) == resources) {
                        HiddenApiReflection.findField((Object) baseContext, str).set(baseContext, resources2);
                        HiddenApiReflection.findField((Object) baseContext, "mTheme").set(baseContext, null);
                    }
                } catch (NoSuchFieldException e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Can not find mResources in ");
                    sb.append(baseContext.getClass().getName());
                    SplitLog.w(str2, sb.toString(), (Throwable) e);
                }
                if (((Resources) VersionCompat.mResourcesInContextImpl().get(baseContext)) != resources) {
                    return;
                }
            } else if (((Resources) VersionCompat.mResourcesInContextImpl().get(baseContext)) != resources) {
                return;
            }
            VersionCompat.mResourcesInContextImpl().set(baseContext, resources2);
            VersionCompat.mThemeInContentImpl().set(baseContext, null);
        }

        private static AssetManager createAssetManager() {
            return (AssetManager) AssetManager.class.newInstance();
        }

        private static Resources createResources(Context context, Resources resources, List list) {
            List<String> appResDirs = getAppResDirs(context.getPackageResourcePath(), resources.getAssets());
            appResDirs.addAll(0, list);
            AssetManager createAssetManager = createAssetManager();
            for (String str : appResDirs) {
                if (((Integer) VersionCompat.getAddAssetPathMethod().invoke(createAssetManager, new Object[]{str})).intValue() == 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Split Apk res path : ");
                    sb.append(str);
                    SplitLog.e(SplitCompatResourcesLoader.TAG, sb.toString(), new Object[0]);
                    throw new RuntimeException("invoke addAssetPath failure! apk format maybe incorrect");
                }
            }
            return newResources(resources, createAssetManager);
        }

        private static List getAppResDirs(String str, AssetManager assetManager) {
            boolean z;
            AssetManager assets = Resources.getSystem().getAssets();
            Object[] objArr = (Object[]) VersionCompat.mStringBlocksInAssetManager().get(assets);
            int length = ((Object[]) VersionCompat.mStringBlocksInAssetManager().get(assetManager)).length;
            int length2 = objArr.length;
            ArrayList arrayList = new ArrayList(length - length2);
            int i = length2 + 1;
            while (true) {
                z = true;
                if (i > length) {
                    break;
                }
                arrayList.add((String) VersionCompat.getGetCookieNameMethod().invoke(assetManager, new Object[]{Integer.valueOf(i)}));
                i++;
            }
            if (!arrayList.contains(str)) {
                int i2 = 1;
                while (true) {
                    if (i2 > length2) {
                        z = false;
                        break;
                    }
                    if (str.equals((String) VersionCompat.getGetCookieNameMethod().invoke(assets, new Object[]{Integer.valueOf(i2)}))) {
                        break;
                    }
                    i2++;
                }
                if (!z) {
                    arrayList.add(0, str);
                }
            }
            return arrayList;
        }

        private static Context getBaseContext(Context context) {
            while (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            }
            return context;
        }

        /* access modifiers changed from: private */
        @SuppressLint({"PrivateApi"})
        public static void installSplitResDirs(Context context, Resources resources, List list) {
            String str;
            Object obj;
            Resources createResources = createResources(context, resources, list);
            checkOrUpdateResourcesForContext(context, resources, createResources);
            Object activityThread = VersionCompat.getActivityThread();
            Iterator it = ((Map) VersionCompat.mActivitiesInActivityThread().get(activityThread)).entrySet().iterator();
            while (true) {
                boolean hasNext = it.hasNext();
                str = SplitCompatResourcesLoader.TAG;
                if (!hasNext) {
                    break;
                }
                Object value = ((Entry) it.next()).getValue();
                Activity activity = (Activity) HiddenApiReflection.findField(value, "activity").get(value);
                if (context != activity) {
                    SplitLog.i(str, "pre-resources found in @mActivities", new Object[0]);
                    checkOrUpdateResourcesForContext(activity, resources, createResources);
                }
            }
            if (VERSION.SDK_INT < 19) {
                obj = VersionCompat.mActiveResourcesInActivityThread().get(activityThread);
            } else {
                obj = VersionCompat.mActiveResourcesInResourcesManager().get(VersionCompat.getResourcesManager());
            }
            Map map = (Map) obj;
            Iterator it2 = map.entrySet().iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                Entry entry = (Entry) it2.next();
                Resources resources2 = (Resources) ((WeakReference) entry.getValue()).get();
                if (resources2 != null) {
                    if (resources2 == resources) {
                        map.put(entry.getKey(), new WeakReference(createResources));
                        SplitLog.i(str, "pre-resources found in @mActiveResources", new Object[0]);
                        break;
                    }
                }
            }
            for (Entry value2 : ((Map) VersionCompat.mPackagesInActivityThread().get(activityThread)).entrySet()) {
                Object obj2 = ((WeakReference) value2.getValue()).get();
                if (obj2 != null) {
                    if (((Resources) VersionCompat.mResourcesInLoadedApk().get(obj2)) == resources) {
                        SplitLog.i(str, "pre-resources found in @mPackages", new Object[0]);
                        VersionCompat.mResourcesInLoadedApk().set(obj2, createResources);
                    }
                }
            }
            for (Entry value3 : ((Map) VersionCompat.mResourcePackagesInActivityThread().get(activityThread)).entrySet()) {
                Object obj3 = ((WeakReference) value3.getValue()).get();
                if (obj3 != null) {
                    if (((Resources) VersionCompat.mResourcesInLoadedApk().get(obj3)) == resources) {
                        SplitLog.i(str, "pre-resources found in @mResourcePackages", new Object[0]);
                        VersionCompat.mResourcesInLoadedApk().set(obj3, createResources);
                    }
                }
            }
        }

        private static Resources newResources(Resources resources, AssetManager assetManager) {
            return (Resources) HiddenApiReflection.findConstructor((Object) resources, AssetManager.class, DisplayMetrics.class, Configuration.class).newInstance(new Object[]{assetManager, resources.getDisplayMetrics(), resources.getConfiguration()});
        }
    }

    class V21 extends VersionCompat {
        private V21() {
            super();
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.String>, for r6v0, types: [java.util.List, java.util.List<java.lang.String>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void installSplitResDirs(Resources resources, List<String> list) {
            Method addAssetPathMethod = VersionCompat.getAddAssetPathMethod();
            for (String str : list) {
                addAssetPathMethod.invoke(resources.getAssets(), new Object[]{str});
            }
        }
    }

    abstract class VersionCompat {
        private static Object activityThread;
        private static Class activityThreadClass;
        private static Method addAssetPathMethod;
        private static Class contextImplClass;
        private static Method getApkAssetsMethod;
        private static Method getAssetPathMethod;
        private static Method getCookieNameMethod;
        private static Class loadedApkClass;
        private static Field mActiveResourcesInActivityThread;
        private static Field mActiveResourcesInResourcesManager;
        private static Field mActivitiesInActivityThread;
        private static Field mPackagesInActivityThread;
        private static Field mResourcePackagesInActivityThread;
        private static Field mResourcesInContextImpl;
        private static Field mResourcesInContextThemeWrapper;
        private static Field mResourcesInLoadedApk;
        private static Field mStringBlocksField;
        private static Field mThemeInContentImpl;
        private static Field mThemeInContextThemeWrapper;
        private static Object resourcesManager;
        private static Class resourcesManagerClass;

        private VersionCompat() {
        }

        @SuppressLint({"PrivateApi"})
        static Object getActivityThread() {
            if (activityThread == null) {
                activityThread = HiddenApiReflection.findMethod(getActivityThreadClass(), "currentActivityThread", new Class[0]).invoke(null, new Object[0]);
            }
            return activityThread;
        }

        @SuppressLint({"PrivateApi"})
        static Class getActivityThreadClass() {
            if (activityThreadClass == null) {
                activityThreadClass = Class.forName("android.app.ActivityThread");
            }
            return activityThreadClass;
        }

        static Method getAddAssetPathMethod() {
            if (addAssetPathMethod == null) {
                addAssetPathMethod = HiddenApiReflection.findMethod(AssetManager.class, "addAssetPath", String.class);
            }
            return addAssetPathMethod;
        }

        @SuppressLint({"PrivateApi"})
        static Class getContextImplClass() {
            if (contextImplClass == null) {
                contextImplClass = Class.forName("android.app.ContextImpl");
            }
            return contextImplClass;
        }

        @RequiresApi(28)
        static Method getGetApkAssetsMethod() {
            if (getApkAssetsMethod == null) {
                getApkAssetsMethod = HiddenApiReflection.findMethod(AssetManager.class, "getApkAssets", new Class[0]);
            }
            return getApkAssetsMethod;
        }

        @RequiresApi(28)
        @SuppressLint({"PrivateApi"})
        static Method getGetAssetPathMethod() {
            if (getAssetPathMethod == null) {
                getAssetPathMethod = HiddenApiReflection.findMethod(Class.forName("android.content.res.ApkAssets"), "getAssetPath", new Class[0]);
            }
            return getAssetPathMethod;
        }

        static Method getGetCookieNameMethod() {
            if (getCookieNameMethod == null) {
                getCookieNameMethod = HiddenApiReflection.findMethod(AssetManager.class, "getCookieName", Integer.TYPE);
            }
            return getCookieNameMethod;
        }

        @SuppressLint({"PrivateApi"})
        static Class getLoadedApkClass() {
            if (loadedApkClass == null) {
                loadedApkClass = Class.forName("android.app.LoadedApk");
            }
            return loadedApkClass;
        }

        @SuppressLint({"PrivateApi"})
        static Object getResourcesManager() {
            if (resourcesManager == null) {
                resourcesManager = HiddenApiReflection.findMethod(getResourcesManagerClass(), "getInstance", new Class[0]).invoke(null, new Object[0]);
            }
            return resourcesManager;
        }

        @SuppressLint({"PrivateApi"})
        static Class getResourcesManagerClass() {
            if (resourcesManagerClass == null) {
                resourcesManagerClass = Class.forName("android.app.ResourcesManager");
            }
            return resourcesManagerClass;
        }

        static Field mActiveResourcesInActivityThread() {
            if (mActiveResourcesInActivityThread == null) {
                mActiveResourcesInActivityThread = HiddenApiReflection.findField(getActivityThreadClass(), "mActiveResources");
            }
            return mActiveResourcesInActivityThread;
        }

        static Field mActiveResourcesInResourcesManager() {
            if (mActiveResourcesInResourcesManager == null) {
                mActiveResourcesInResourcesManager = HiddenApiReflection.findField(getResourcesManagerClass(), "mActiveResources");
            }
            return mActiveResourcesInResourcesManager;
        }

        static Field mActivitiesInActivityThread() {
            if (mActivitiesInActivityThread == null) {
                mActivitiesInActivityThread = HiddenApiReflection.findField(getActivityThreadClass(), "mActivities");
            }
            return mActivitiesInActivityThread;
        }

        static Field mPackagesInActivityThread() {
            if (mPackagesInActivityThread == null) {
                mPackagesInActivityThread = HiddenApiReflection.findField(getActivityThreadClass(), "mPackages");
            }
            return mPackagesInActivityThread;
        }

        static Field mResourcePackagesInActivityThread() {
            if (mResourcePackagesInActivityThread == null) {
                mResourcePackagesInActivityThread = HiddenApiReflection.findField(getActivityThreadClass(), "mResourcePackages");
            }
            return mResourcePackagesInActivityThread;
        }

        static Field mResourcesInContextImpl() {
            if (mResourcesInContextImpl == null) {
                mResourcesInContextImpl = HiddenApiReflection.findField(getContextImplClass(), "mResources");
            }
            return mResourcesInContextImpl;
        }

        static Field mResourcesInContextThemeWrapper() {
            if (mResourcesInContextThemeWrapper == null) {
                mResourcesInContextThemeWrapper = HiddenApiReflection.findField(ContextThemeWrapper.class, "mResources");
            }
            return mResourcesInContextThemeWrapper;
        }

        static Field mResourcesInLoadedApk() {
            if (mResourcesInLoadedApk == null) {
                mResourcesInLoadedApk = HiddenApiReflection.findField(getLoadedApkClass(), "mResources");
            }
            return mResourcesInLoadedApk;
        }

        static Field mStringBlocksInAssetManager() {
            if (mStringBlocksField == null) {
                mStringBlocksField = HiddenApiReflection.findField(AssetManager.class, "mStringBlocks");
            }
            return mStringBlocksField;
        }

        static Field mThemeInContentImpl() {
            if (mThemeInContentImpl == null) {
                mThemeInContentImpl = HiddenApiReflection.findField(getContextImplClass(), "mTheme");
            }
            return mThemeInContentImpl;
        }

        static Field mThemeInContextThemeWrapper() {
            if (mThemeInContextThemeWrapper == null) {
                mThemeInContextThemeWrapper = HiddenApiReflection.findField(ContextThemeWrapper.class, "mTheme");
            }
            return mThemeInContextThemeWrapper;
        }
    }

    private static void checkOrUpdateResources(Context context, Resources resources) {
        try {
            List loadedResourcesDirs = getLoadedResourcesDirs(resources.getAssets());
            Collection<String> loadedSplitPaths = getLoadedSplitPaths();
            if (loadedSplitPaths != null && !loadedSplitPaths.isEmpty() && !loadedResourcesDirs.containsAll(loadedSplitPaths)) {
                ArrayList arrayList = new ArrayList();
                for (String str : loadedSplitPaths) {
                    if (!loadedResourcesDirs.contains(str)) {
                        arrayList.add(str);
                    }
                }
                try {
                    installSplitResDirs(context, resources, arrayList);
                } catch (Throwable th) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed to install resources ");
                    sb.append(arrayList.toString());
                    sb.append(" for ");
                    sb.append(context.getClass().getName());
                    throw new SplitCompatResourcesException(sb.toString(), th);
                }
            }
        } catch (Throwable th2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to get all loaded split resources for ");
            sb2.append(context.getClass().getName());
            throw new SplitCompatResourcesException(sb2.toString(), th2);
        }
    }

    private static List getLoadedResourcesDirs(AssetManager assetManager) {
        ArrayList arrayList = new ArrayList();
        if (VERSION.SDK_INT >= 28) {
            for (Object invoke : (Object[]) VersionCompat.getGetApkAssetsMethod().invoke(assetManager, new Object[0])) {
                arrayList.add((String) VersionCompat.getGetAssetPathMethod().invoke(invoke, new Object[0]));
            }
        } else {
            int length = ((Object[]) VersionCompat.mStringBlocksInAssetManager().get(assetManager)).length;
            for (int i = 1; i <= length; i++) {
                arrayList.add((String) VersionCompat.getGetCookieNameMethod().invoke(assetManager, new Object[]{Integer.valueOf(i)}));
            }
        }
        return arrayList;
    }

    private static Collection getLoadedSplitPaths() {
        SplitLoadManager instance = SplitLoadManagerService.getInstance();
        if (instance != null) {
            return instance.getLoadedSplitApkPaths();
        }
        return null;
    }

    private static void installSplitResDirs(final Context context, final Resources resources, final List list) {
        if (VERSION.SDK_INT >= 21) {
            V21.installSplitResDirs(resources, list);
        } else if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            SplitLog.i(TAG, "Install res on main thread", new Object[0]);
            V14.installSplitResDirs(context, resources, list);
        } else {
            synchronized (sLock) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        synchronized (SplitCompatResourcesLoader.sLock) {
                            try {
                                V14.installSplitResDirs(context, resources, list);
                                SplitCompatResourcesLoader.sLock.notify();
                            } catch (Throwable th) {
                                throw new RuntimeException(th);
                            }
                        }
                    }
                });
                sLock.wait();
            }
        }
    }

    public static void loadResources(Context context, Resources resources) {
        checkOrUpdateResources(context, resources);
    }

    static void loadResources(Context context, Resources resources, String str) {
        if (!getLoadedResourcesDirs(resources.getAssets()).contains(str)) {
            installSplitResDirs(context, resources, Collections.singletonList(str));
            SplitLog.d(TAG, "Install split %s resources for application.", str);
        }
    }
}
