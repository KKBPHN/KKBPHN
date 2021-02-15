package com.miui.internal.component.module;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

public class ModuleClassLoader {
    private static final String DEX_ELEMENTS_FIELD_NAME = "dexElements";
    private static final String DEX_PATH_LIST_CLASS_NAME = "dalvik.system.DexPathList";
    private static final String DEX_PATH_LIST_ELEMENT_CLASS_NAME = "dalvik.system.DexPathList$Element";
    private static final String DEX_PATH_LIST_NATIVE_ELEMENT_CLASS_NAME = "dalvik.system.DexPathList$NativeLibraryElement";
    private static final String LIB_ELEMENTS_FIELD_NAME = "nativeLibraryPathElements";
    private static final String TAG = "ModuleClassLoader";

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.ClassLoader, code=java.lang.Object, for r6v0, types: [java.lang.Object, java.lang.ClassLoader] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Object getDexPathListVariable(Object obj) {
        if (obj instanceof BaseDexClassLoader) {
            Field[] declaredFields = BaseDexClassLoader.class.getDeclaredFields();
            int length = declaredFields.length;
            int i = 0;
            while (i < length) {
                Field field = declaredFields[i];
                if (DEX_PATH_LIST_CLASS_NAME.equals(field.getType().getName())) {
                    field.setAccessible(true);
                    try {
                        obj = field.get(obj);
                        return obj;
                    } catch (IllegalAccessException | IllegalArgumentException unused) {
                        continue;
                    }
                } else {
                    i++;
                }
            }
        }
        throw new NoSuchFieldException("dexPathList field not found.");
    }

    private static Field getElementsField(Object obj, String str, String str2) {
        Field[] declaredFields;
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.getName().equals(str)) {
                Class type = field.getType();
                if (type.isArray() && str2.equals(type.getComponentType().getName())) {
                    field.setAccessible(true);
                    return field;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" field not found.");
        throw new NoSuchFieldException(sb.toString());
    }

    private static Field getNativeLibraryDirectoriesField(Object obj) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            Field field = declaredFields[i];
            Class type = field.getType();
            if (!type.isArray() || type.getComponentType() != File.class) {
                i++;
            } else {
                field.setAccessible(true);
                return field;
            }
        }
        throw new NoSuchFieldException("nativeLibraryDirectories field not found.");
    }

    private static void invokeAddDexPath(ClassLoader classLoader, String str) {
        if (classLoader instanceof BaseDexClassLoader) {
            Method method = Class.forName(classLoader.getClass().getName()).getMethod("addDexPath", new Class[]{String.class});
            method.setAccessible(true);
            method.invoke(classLoader, new Object[]{str});
            return;
        }
        throw new NoSuchMethodException("addDexPath method not found.");
    }

    private static void invokeAddNativePath(ClassLoader classLoader, String str) {
        if (classLoader instanceof BaseDexClassLoader) {
            Method method = Class.forName(classLoader.getClass().getName()).getMethod("addNativePath", new Class[]{Collection.class});
            method.setAccessible(true);
            method.invoke(classLoader, new Object[]{Collections.singletonList(str)});
            return;
        }
        throw new NoSuchMethodException("addNativePath method not found.");
    }

    public static boolean load(String str, String str2, String str3, ClassLoader classLoader) {
        return load(str, str2, str3, classLoader, null);
    }

    static boolean load(String str, String str2, String str3, ClassLoader classLoader, Context context) {
        String str4;
        String str5;
        String str6;
        String str7 = TAG;
        if (str == null && (str3 == null || context == null)) {
            return false;
        }
        try {
            Object dexPathListVariable = getDexPathListVariable(classLoader);
            if (str != null) {
                str5 = str2;
                str6 = str;
            } else if (VERSION.SDK_INT < 23) {
                loadLibBeforeAndroidM(dexPathListVariable, str3);
                return true;
            } else {
                str6 = context.getApplicationInfo().sourceDir;
                str5 = null;
            }
            if (VERSION.SDK_INT < 28) {
                Object dexPathListVariable2 = getDexPathListVariable(str5 == null ? new PathClassLoader(str6, str3, classLoader.getParent()) : new DexClassLoader(str6, str5, str3, classLoader.getParent()));
                if (str != null) {
                    loadDex(dexPathListVariable, dexPathListVariable2);
                }
                if (str3 != null) {
                    loadLibrary(dexPathListVariable, dexPathListVariable2, str3);
                }
            } else {
                if (str6 != null) {
                    invokeAddDexPath(classLoader, str6);
                }
                if (str3 != null) {
                    invokeAddNativePath(classLoader, str3);
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            e = e;
            str4 = "Illegal arguments";
            Log.e(str7, str4, e);
            return false;
        } catch (IllegalAccessException e2) {
            e = e2;
            str4 = "Illegal access";
            Log.e(str7, str4, e);
            return false;
        } catch (ClassNotFoundException e3) {
            e = e3;
            str4 = "No class found";
            Log.e(str7, str4, e);
            return false;
        } catch (NoSuchFieldException e4) {
            e = e4;
            str4 = "No field found";
            Log.e(str7, str4, e);
            return false;
        } catch (NoSuchMethodException e5) {
            e = e5;
            str4 = "no method found";
            Log.e(str7, str4, e);
            return false;
        } catch (InvocationTargetException e6) {
            e = e6;
            str4 = "Invocation target";
            Log.e(str7, str4, e);
            return false;
        }
    }

    private static void loadDex(Object obj, Object obj2) {
        replaceElement(obj, obj2, DEX_ELEMENTS_FIELD_NAME, DEX_PATH_LIST_ELEMENT_CLASS_NAME);
    }

    private static void loadLibBeforeAndroidM(Object obj, String str) {
        Field nativeLibraryDirectoriesField = getNativeLibraryDirectoriesField(obj);
        File[] fileArr = (File[]) nativeLibraryDirectoriesField.get(obj);
        File[] fileArr2 = new File[(fileArr.length + 1)];
        fileArr2[0] = new File(str);
        System.arraycopy(fileArr, 0, fileArr2, 1, fileArr.length);
        nativeLibraryDirectoriesField.set(obj, fileArr2);
    }

    private static void loadLibrary(Object obj, Object obj2, String str) {
        String str2;
        int i = VERSION.SDK_INT;
        String str3 = LIB_ELEMENTS_FIELD_NAME;
        if (i >= 26) {
            str2 = DEX_PATH_LIST_NATIVE_ELEMENT_CLASS_NAME;
        } else if (i >= 23) {
            str2 = DEX_PATH_LIST_ELEMENT_CLASS_NAME;
        } else {
            loadLibBeforeAndroidM(obj, str);
            return;
        }
        replaceElement(obj, obj2, str3, str2);
    }

    private static void replaceElement(Object obj, Object obj2, String str, String str2) {
        Object[] objArr = (Object[]) getElementsField(obj2, str, str2).get(obj2);
        Field elementsField = getElementsField(obj, str, str2);
        Object[] objArr2 = (Object[]) elementsField.get(obj);
        Object[] objArr3 = (Object[]) Array.newInstance(Class.forName(str2), objArr2.length + 1);
        objArr3[0] = objArr[0];
        System.arraycopy(objArr2, 0, objArr3, 1, objArr2.length);
        elementsField.set(obj, objArr3);
    }
}
