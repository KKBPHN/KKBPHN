package com.iqiyi.android.qigsaw.core.splitload;

import android.content.Context;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.PathClassLoader;
import java.net.URL;
import java.util.Enumeration;

final class SplitDelegateClassloader extends PathClassLoader {
    private static BaseDexClassLoader originClassLoader;
    private ClassNotFoundInterceptor classNotFoundInterceptor;

    SplitDelegateClassloader(ClassLoader classLoader) {
        super("", classLoader);
        originClassLoader = (BaseDexClassLoader) classLoader;
    }

    static void inject(ClassLoader classLoader, Context context) {
        reflectPackageInfoClassloader(context, new SplitDelegateClassloader(classLoader));
    }

    private static void reflectPackageInfoClassloader(Context context, ClassLoader classLoader) {
        Object obj = HiddenApiReflection.findField((Object) context, "mPackageInfo").get(context);
        if (obj != null) {
            HiddenApiReflection.findField(obj, "mClassLoader").set(obj, classLoader);
        }
    }

    /* access modifiers changed from: protected */
    public Class findClass(String str) {
        try {
            this = this;
            this = originClassLoader.loadClass(str);
            r1 = this;
            return this;
        } catch (ClassNotFoundException e) {
            ClassNotFoundInterceptor classNotFoundInterceptor2 = r1.classNotFoundInterceptor;
            if (classNotFoundInterceptor2 != null) {
                Class findClass = classNotFoundInterceptor2.findClass(str);
                if (findClass != null) {
                    return findClass;
                }
            }
            throw e;
        }
    }

    public String findLibrary(String str) {
        String findLibrary = originClassLoader.findLibrary(str);
        if (findLibrary == null) {
            for (SplitDexClassLoader findLibraryItself : SplitApplicationLoaders.getInstance().getClassLoaders()) {
                findLibrary = findLibraryItself.findLibraryItself(str);
                if (findLibrary != null) {
                    break;
                }
            }
        }
        return findLibrary;
    }

    /* access modifiers changed from: protected */
    public URL findResource(String str) {
        URL findResource = super.findResource(str);
        if (findResource == null) {
            for (SplitDexClassLoader findResourceItself : SplitApplicationLoaders.getInstance().getClassLoaders()) {
                findResource = findResourceItself.findResourceItself(str);
                if (findResource != null) {
                    break;
                }
            }
        }
        return findResource;
    }

    /* access modifiers changed from: protected */
    public Enumeration findResources(String str) {
        Enumeration findResources = super.findResources(str);
        if (findResources == null) {
            for (SplitDexClassLoader findResourcesItself : SplitApplicationLoaders.getInstance().getClassLoaders()) {
                findResources = findResourcesItself.findResourcesItself(str);
                if (findResources != null) {
                    break;
                }
            }
        }
        return findResources;
    }

    public URL getResource(String str) {
        return originClassLoader.getResource(str);
    }

    public Enumeration getResources(String str) {
        return originClassLoader.getResources(str);
    }

    public Class loadClass(String str) {
        return findClass(str);
    }

    /* access modifiers changed from: 0000 */
    public void setClassNotFoundInterceptor(ClassNotFoundInterceptor classNotFoundInterceptor2) {
        this.classNotFoundInterceptor = classNotFoundInterceptor2;
    }
}
