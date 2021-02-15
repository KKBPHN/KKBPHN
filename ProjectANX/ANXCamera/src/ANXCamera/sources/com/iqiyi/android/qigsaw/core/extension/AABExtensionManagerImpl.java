package com.iqiyi.android.qigsaw.core.extension;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

final class AABExtensionManagerImpl implements AABExtensionManager {
    private SplitComponentInfoProvider infoProvider;
    private List splitActivities;
    private Map splitActivitiesMap;
    private List splitReceivers;
    private List splitServices;

    AABExtensionManagerImpl(SplitComponentInfoProvider splitComponentInfoProvider) {
        this.infoProvider = splitComponentInfoProvider;
    }

    @SuppressLint({"DiscouragedPrivateApi"})
    public void activeApplication(Application application, Context context) {
        if (application != null) {
            Throwable e = null;
            try {
                Method declaredMethod = Application.class.getDeclaredMethod("attach", new Class[]{Context.class});
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(application, new Object[]{context});
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
                e = e2;
            }
            if (e != null) {
                throw new AABExtensionException(e);
            }
        }
    }

    @SuppressLint({"PrivateApi"})
    public Application createApplication(ClassLoader classLoader, String str) {
        Throwable e;
        String splitApplicationName = this.infoProvider.getSplitApplicationName(str);
        if (!TextUtils.isEmpty(splitApplicationName)) {
            try {
                return (Application) classLoader.loadClass(splitApplicationName).newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e2) {
                e = e2;
            }
        } else {
            e = null;
            if (e == null) {
                return null;
            }
            throw new AABExtensionException(e);
        }
    }

    public Map getSplitActivitiesMap() {
        if (this.splitActivitiesMap == null) {
            this.splitActivitiesMap = this.infoProvider.getSplitActivitiesMap();
        }
        return this.splitActivitiesMap;
    }

    public boolean isSplitActivity(String str) {
        if (this.splitActivities == null) {
            Collection<List> values = getSplitActivitiesMap().values();
            ArrayList arrayList = new ArrayList(0);
            if (!values.isEmpty()) {
                for (List addAll : values) {
                    arrayList.addAll(addAll);
                }
            }
            this.splitActivities = arrayList;
        }
        return this.splitActivities.contains(str);
    }

    public boolean isSplitReceiver(String str) {
        if (this.splitReceivers == null) {
            this.splitReceivers = this.infoProvider.getSplitReceivers();
        }
        return this.splitReceivers.contains(str);
    }

    public boolean isSplitService(String str) {
        if (this.splitServices == null) {
            this.splitServices = this.infoProvider.getSplitServices();
        }
        return this.splitServices.contains(str);
    }
}
