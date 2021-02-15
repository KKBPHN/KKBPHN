package com.ss.android.vesdk;

import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class VEJavaCalls {
    private static final String LOG_TAG = "JavaCalls";
    private static final Map PRIMITIVE_MAP = new HashMap();

    public class JavaParam {
        public final Class clazz;
        public final Object obj;

        public JavaParam(Class cls, Object obj2) {
            this.clazz = cls;
            this.obj = obj2;
        }
    }

    static {
        PRIMITIVE_MAP.put(Boolean.class, Boolean.TYPE);
        PRIMITIVE_MAP.put(Byte.class, Byte.TYPE);
        PRIMITIVE_MAP.put(Character.class, Character.TYPE);
        PRIMITIVE_MAP.put(Short.class, Short.TYPE);
        PRIMITIVE_MAP.put(Integer.class, Integer.TYPE);
        PRIMITIVE_MAP.put(Float.class, Float.TYPE);
        PRIMITIVE_MAP.put(Long.class, Long.TYPE);
        PRIMITIVE_MAP.put(Double.class, Double.TYPE);
        Map map = PRIMITIVE_MAP;
        Class cls = Boolean.TYPE;
        map.put(cls, cls);
        Map map2 = PRIMITIVE_MAP;
        Class cls2 = Byte.TYPE;
        map2.put(cls2, cls2);
        Map map3 = PRIMITIVE_MAP;
        Class cls3 = Character.TYPE;
        map3.put(cls3, cls3);
        Map map4 = PRIMITIVE_MAP;
        Class cls4 = Short.TYPE;
        map4.put(cls4, cls4);
        Map map5 = PRIMITIVE_MAP;
        Class cls5 = Integer.TYPE;
        map5.put(cls5, cls5);
        Map map6 = PRIMITIVE_MAP;
        Class cls6 = Float.TYPE;
        map6.put(cls6, cls6);
        Map map7 = PRIMITIVE_MAP;
        Class cls7 = Long.TYPE;
        map7.put(cls7, cls7);
        Map map8 = PRIMITIVE_MAP;
        Class cls8 = Double.TYPE;
        map8.put(cls8, cls8);
    }

    public static Object callMethod(Object obj, String str, Object... objArr) {
        try {
            obj = callMethodOrThrow(obj, str, objArr);
            return obj;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Meet exception when call Method '");
            sb.append(str);
            sb.append("' in ");
            sb.append(obj);
            Log.w(LOG_TAG, sb.toString(), e);
            return null;
        }
    }

    public static Object callMethodOrThrow(Object obj, String str, Object... objArr) {
        return getDeclaredMethod(obj.getClass(), str, getParameterTypes(objArr)).invoke(obj, getParameters(objArr));
    }

    public static Object callStaticMethod(String str, String str2, Object... objArr) {
        try {
            r2 = str;
            r2 = callStaticMethodOrThrow(Class.forName(str), str2, objArr);
            r2 = r2;
            return r2;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Meet exception when call Method '");
            sb.append(str2);
            sb.append("' in ");
            sb.append(r2);
            Log.w(LOG_TAG, sb.toString(), e);
            return null;
        }
    }

    public static Object callStaticMethodOrThrow(Class cls, String str, Object... objArr) {
        return getDeclaredMethod(cls, str, getParameterTypes(objArr)).invoke(null, getParameters(objArr));
    }

    public static Object callStaticMethodOrThrow(String str, String str2, Object... objArr) {
        return getDeclaredMethod(Class.forName(str), str2, getParameterTypes(objArr)).invoke(null, getParameters(objArr));
    }

    private static boolean compareClassLists(Class[] clsArr, Class[] clsArr2) {
        boolean z = true;
        if (clsArr == null) {
            if (!(clsArr2 == null || clsArr2.length == 0)) {
                z = false;
            }
            return z;
        } else if (clsArr2 == null) {
            if (clsArr.length != 0) {
                z = false;
            }
            return z;
        } else if (clsArr.length != clsArr2.length) {
            return false;
        } else {
            int i = 0;
            while (i < clsArr.length) {
                if (!clsArr[i].isAssignableFrom(clsArr2[i]) && (!PRIMITIVE_MAP.containsKey(clsArr[i]) || !((Class) PRIMITIVE_MAP.get(clsArr[i])).equals(PRIMITIVE_MAP.get(clsArr2[i])))) {
                    return false;
                }
                i++;
            }
            return true;
        }
    }

    private static Method findMethodByName(Method[] methodArr, String str, Class[] clsArr) {
        if (str != null) {
            int length = methodArr.length;
            for (int i = 0; i < length; i++) {
                Method method = methodArr[i];
                if (method.getName().equals(str) && compareClassLists(method.getParameterTypes(), clsArr)) {
                    return method;
                }
            }
            return null;
        }
        throw new NullPointerException("Method name must not be null.");
    }

    private static Method getDeclaredMethod(Class cls, String str, Class... clsArr) {
        Method findMethodByName = findMethodByName(cls.getDeclaredMethods(), str, clsArr);
        if (findMethodByName != null) {
            findMethodByName.setAccessible(true);
            return findMethodByName;
        } else if (cls.getSuperclass() != null) {
            return getDeclaredMethod(cls.getSuperclass(), str, clsArr);
        } else {
            throw new NoSuchMethodException();
        }
    }

    private static Object getDefaultValue(Class cls) {
        if (Integer.TYPE.equals(cls) || Integer.class.equals(cls) || Byte.TYPE.equals(cls) || Byte.class.equals(cls) || Short.TYPE.equals(cls) || Short.class.equals(cls) || Long.TYPE.equals(cls) || Long.class.equals(cls) || Double.TYPE.equals(cls) || Double.class.equals(cls) || Float.TYPE.equals(cls) || Float.class.equals(cls)) {
            return Integer.valueOf(0);
        }
        if (Boolean.TYPE.equals(cls) || Boolean.class.equals(cls)) {
            return Boolean.valueOf(false);
        }
        if (Character.TYPE.equals(cls) || Character.class.equals(cls)) {
            return Character.valueOf(0);
        }
        return null;
    }

    public static Object getField(Object obj, String str) {
        try {
            return getFieldOrThrow(obj, str);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static Object getFieldOrThrow(Object obj, String str) {
        Class cls = obj.getClass();
        Field field = null;
        while (field == null) {
            try {
                field = cls.getDeclaredField(str);
                field.setAccessible(true);
            } catch (NoSuchFieldException unused) {
                cls = cls.getSuperclass();
            }
            if (cls == null) {
                throw new NoSuchFieldException();
            }
        }
        field.setAccessible(true);
        return field.get(obj);
    }

    private static Class[] getParameterTypes(Object... objArr) {
        if (objArr == null || objArr.length <= 0) {
            return null;
        }
        Class[] clsArr = new Class[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            JavaParam javaParam = objArr[i];
            if (javaParam == null || !(javaParam instanceof JavaParam)) {
                clsArr[i] = javaParam == null ? null : javaParam.getClass();
            } else {
                clsArr[i] = javaParam.clazz;
            }
        }
        return clsArr;
    }

    private static Object[] getParameters(Object... objArr) {
        if (objArr == null || objArr.length <= 0) {
            return null;
        }
        Object[] objArr2 = new Object[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            JavaParam javaParam = objArr[i];
            if (javaParam == null || !(javaParam instanceof JavaParam)) {
                objArr2[i] = javaParam;
            } else {
                objArr2[i] = javaParam.obj;
            }
        }
        return objArr2;
    }

    public static Object newEmptyInstance(Class cls) {
        try {
            r3 = cls;
            r3 = newEmptyInstanceOrThrow(cls);
            r3 = r3;
            return r3;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Meet exception when make instance as a ");
            sb.append(r3.getSimpleName());
            Log.w(LOG_TAG, sb.toString(), e);
            return null;
        }
    }

    public static Object newEmptyInstanceOrThrow(Class cls) {
        Constructor[] declaredConstructors = cls.getDeclaredConstructors();
        if (declaredConstructors == null || declaredConstructors.length == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't get even one available constructor for ");
            sb.append(cls);
            throw new IllegalArgumentException(sb.toString());
        }
        Constructor constructor = declaredConstructors[0];
        constructor.setAccessible(true);
        Class[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return constructor.newInstance(new Object[0]);
        }
        Object[] objArr = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            objArr[i] = getDefaultValue(parameterTypes[i]);
        }
        return constructor.newInstance(objArr);
    }

    public static Object newInstance(Class cls, Object... objArr) {
        try {
            r2 = cls;
            r2 = newInstanceOrThrow(cls, objArr);
            r2 = r2;
            return r2;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Meet exception when make instance as a ");
            sb.append(r2.getSimpleName());
            Log.w(LOG_TAG, sb.toString(), e);
            return null;
        }
    }

    public static Object newInstance(String str, Object... objArr) {
        try {
            r2 = str;
            r2 = newInstanceOrThrow(str, objArr);
            r2 = r2;
            return r2;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Meet exception when make instance as a ");
            sb.append(r2);
            Log.w(LOG_TAG, sb.toString(), e);
            return null;
        }
    }

    public static Object newInstanceOrThrow(Class cls, Object... objArr) {
        return cls.getConstructor(getParameterTypes(objArr)).newInstance(getParameters(objArr));
    }

    public static Object newInstanceOrThrow(String str, Object... objArr) {
        return newInstanceOrThrow(Class.forName(str), getParameters(objArr));
    }

    public static void setField(Object obj, String str, Object obj2) {
        try {
            setFieldOrThrow(obj, str, obj2);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    public static void setFieldOrThrow(Object obj, String str, Object obj2) {
        Class cls = obj.getClass();
        Field field = null;
        while (field == null) {
            try {
                field = cls.getDeclaredField(str);
            } catch (NoSuchFieldException unused) {
                cls = cls.getSuperclass();
            }
            if (cls == null) {
                throw new NoSuchFieldException();
            }
        }
        field.setAccessible(true);
        field.set(obj, obj2);
    }
}
