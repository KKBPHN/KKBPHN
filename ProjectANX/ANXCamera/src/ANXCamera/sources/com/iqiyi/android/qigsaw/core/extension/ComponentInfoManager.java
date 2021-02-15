package com.iqiyi.android.qigsaw.core.extension;

import java.lang.reflect.Field;

final class ComponentInfoManager {
    private static final String ACTIVITIES_SUFFIX = "_ACTIVITIES";
    private static final String APPLICATION_SUFFIX = "_APPLICATION";
    private static final String CLASS_ComponentInfo = "com.iqiyi.android.qigsaw.core.extension.ComponentInfo";
    private static final String RECEIVERS_SUFFIX = "_RECEIVERS";
    private static final String SERVICES_SUFFIX = "_SERVICES";

    ComponentInfoManager() {
    }

    private static Class getComponentInfoClass() {
        return Class.forName(CLASS_ComponentInfo);
    }

    static String[] getSplitActivities(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(ACTIVITIES_SUFFIX);
        try {
            Field field = getComponentInfoClass().getField(sb.toString());
            field.setAccessible(true);
            String str2 = (String) field.get(null);
            if (str2 != null) {
                return str2.split(",");
            }
        } catch (IllegalAccessException | NoSuchFieldException unused) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String getSplitApplication(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(APPLICATION_SUFFIX);
        try {
            Field field = getComponentInfoClass().getField(sb.toString());
            field.setAccessible(true);
            return (String) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException unused) {
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String[] getSplitReceivers(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(RECEIVERS_SUFFIX);
        try {
            Field field = getComponentInfoClass().getField(sb.toString());
            field.setAccessible(true);
            String str2 = (String) field.get(null);
            if (str2 != null) {
                return str2.split(",");
            }
        } catch (IllegalAccessException | NoSuchFieldException unused) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String[] getSplitServices(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(SERVICES_SUFFIX);
        try {
            Field field = getComponentInfoClass().getField(sb.toString());
            field.setAccessible(true);
            String str2 = (String) field.get(null);
            if (str2 != null) {
                return str2.split(",");
            }
        } catch (IllegalAccessException | NoSuchFieldException unused) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
