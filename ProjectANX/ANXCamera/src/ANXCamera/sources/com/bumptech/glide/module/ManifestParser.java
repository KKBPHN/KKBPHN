package com.bumptech.glide.module;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class ManifestParser {
    private static final String GLIDE_MODULE_VALUE = "GlideModule";
    private static final String TAG = "ManifestParser";
    private final Context context;

    public ManifestParser(Context context2) {
        this.context = context2;
    }

    private static GlideModule parseModule(String str) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        try {
            Class cls5 = Class.forName(str);
            try {
                cls4 = cls5;
                Object newInstance = cls5.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                cls4 = newInstance;
                if (newInstance instanceof GlideModule) {
                    return (GlideModule) newInstance;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Expected instanceof GlideModule, but found: ");
                sb.append(newInstance);
                throw new RuntimeException(sb.toString());
            } catch (InstantiationException e) {
                throwInstantiateGlideModuleException(cls, e);
                throw null;
            } catch (IllegalAccessException e2) {
                throwInstantiateGlideModuleException(cls2, e2);
                throw null;
            } catch (NoSuchMethodException e3) {
                throwInstantiateGlideModuleException(cls3, e3);
                throw null;
            } catch (InvocationTargetException e4) {
                throwInstantiateGlideModuleException(cls4, e4);
                throw null;
            }
        } catch (ClassNotFoundException e5) {
            throw new IllegalArgumentException("Unable to find GlideModule implementation", e5);
        }
    }

    private static void throwInstantiateGlideModuleException(Class cls, Exception exc) {
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to instantiate GlideModule implementation for ");
        sb.append(cls);
        throw new RuntimeException(sb.toString(), exc);
    }

    public List parse() {
        String str = TAG;
        if (Log.isLoggable(str, 3)) {
            Log.d(str, "Loading Glide modules");
        }
        ArrayList arrayList = new ArrayList();
        try {
            ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
            if (applicationInfo.metaData == null) {
                if (Log.isLoggable(str, 3)) {
                    Log.d(str, "Got null app info metadata");
                }
                return arrayList;
            }
            if (Log.isLoggable(str, 2)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Got app info metadata: ");
                sb.append(applicationInfo.metaData);
                Log.v(str, sb.toString());
            }
            for (String str2 : applicationInfo.metaData.keySet()) {
                if (GLIDE_MODULE_VALUE.equals(applicationInfo.metaData.get(str2))) {
                    arrayList.add(parseModule(str2));
                    if (Log.isLoggable(str, 3)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Loaded Glide module: ");
                        sb2.append(str2);
                        Log.d(str, sb2.toString());
                    }
                }
            }
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "Finished loading Glide modules");
            }
            return arrayList;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse GlideModules", e);
        }
    }
}
