package miuix.internal.util;

import android.util.Log;
import java.lang.reflect.Method;

public class ReflectUtil {
    private static final String TAG = "ReflectUtil";

    public static Object callObjectMethod(Object obj, String str, Class[] clsArr, Object... objArr) {
        if (obj == null) {
            return null;
        }
        try {
            return obj.getClass().getDeclaredMethod(str, clsArr).invoke(obj, objArr);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to call method:");
            sb.append(str);
            Log.e(TAG, sb.toString(), e);
            return null;
        }
    }

    public static Object callStaticObjectMethod(Class cls, Class cls2, String str, Class[] clsArr, Object... objArr) {
        if (cls == null) {
            return null;
        }
        try {
            Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
            declaredMethod.setAccessible(true);
            return declaredMethod.invoke(null, objArr);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to call static method:");
            sb.append(str);
            Log.e(TAG, sb.toString(), e);
            return null;
        }
    }

    public static Class getClass(String str) {
        try {
            r3 = str;
            r3 = Class.forName(str);
            r3 = r3;
            return r3;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cant find class ");
            sb.append(r3);
            Log.e(TAG, sb.toString(), e);
            return null;
        }
    }
}
