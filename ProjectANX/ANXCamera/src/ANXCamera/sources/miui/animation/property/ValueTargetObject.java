package miui.animation.property;

import android.util.ArrayMap;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import miui.animation.utils.CommonUtils;

public class ValueTargetObject {
    private static final String GET = "get";
    private static final String SET = "set";
    private Map mFieldMap = new ArrayMap();
    private Map mGetterMap = new ArrayMap();
    private Map mGetterNameMap = new ArrayMap();
    private Map mMap = new ArrayMap();
    private WeakReference mRef;
    private Map mSetterMap = new ArrayMap();
    private Map mSetterNameMap = new ArrayMap();
    private Object mTempObj;

    class FieldInfo {
        Field field;

        private FieldInfo() {
        }
    }

    class MethodInfo {
        Method method;

        private MethodInfo() {
        }
    }

    public ValueTargetObject(Object obj) {
        if (CommonUtils.isBuiltInClass(obj.getClass())) {
            this.mTempObj = obj;
        } else {
            this.mRef = new WeakReference(obj);
        }
    }

    private Field getField(Object obj, String str, Class cls) {
        FieldInfo fieldInfo = (FieldInfo) this.mFieldMap.get(str);
        if (fieldInfo == null) {
            fieldInfo = new FieldInfo();
            fieldInfo.field = getFieldByType(obj, str, cls);
            this.mFieldMap.put(str, fieldInfo);
        }
        return fieldInfo.field;
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x000f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private Field getFieldByType(Object obj, String str, Class cls) {
        Field field;
        try {
            field = obj.getClass().getDeclaredField(str);
            field.setAccessible(true);
        } catch (NoSuchFieldException unused) {
            field = null;
        }
        try {
            field = obj.getClass().getField(str);
        } catch (NoSuchFieldException unused2) {
        }
        if (field == null || field.getType() == cls) {
            return field;
        }
        return null;
    }

    private Method getMethod(Object obj, String str, Map map, Class... clsArr) {
        MethodInfo methodInfo = (MethodInfo) map.get(str);
        if (methodInfo == null) {
            methodInfo = new MethodInfo();
            methodInfo.method = getMethod(obj, str, clsArr);
            map.put(str, methodInfo);
        }
        return methodInfo.method;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        return r1;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x000e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private Method getMethod(Object obj, String str, Class... clsArr) {
        Method method = null;
        method = obj.getClass().getDeclaredMethod(str, clsArr);
        method.setAccessible(true);
        try {
            method = obj.getClass().getMethod(str, clsArr);
            return method;
        } catch (NoSuchMethodException unused) {
            return method;
        }
    }

    private String getMethodName(String str, String str2, Map map) {
        String str3 = (String) map.get(str);
        if (str3 != null) {
            return str3;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(Character.toUpperCase(str.charAt(0)));
        sb.append(str.substring(1));
        String sb2 = sb.toString();
        map.put(str, sb2);
        return sb2;
    }

    private Object getRefObject() {
        WeakReference weakReference = this.mRef;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    private Object getValueByField(Object obj, Field field) {
        try {
            return field.get(obj);
        } catch (Exception unused) {
            return null;
        }
    }

    private Object invokeGetter(String str, Class cls, Object obj) {
        Method method = getMethod(obj, getMethodName(str, GET, this.mGetterNameMap), this.mGetterMap, new Class[0]);
        if (method == null) {
            return null;
        }
        return retToClz(invokeMethod(obj, method, new Object[0]), cls);
    }

    private Object invokeMethod(Object obj, Method method, Object... objArr) {
        if (method != null) {
            try {
                return method.invoke(obj, objArr);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("ValueProperty.invokeMethod failed, ");
                sb.append(method.getName());
                Log.d(CommonUtils.TAG, sb.toString(), e);
            }
        }
        return null;
    }

    private Object retToClz(Object obj, Class cls) {
        if (!(obj instanceof Number)) {
            return null;
        }
        Number number = (Number) obj;
        if (cls == Float.class || cls == Float.TYPE) {
            return Float.valueOf(number.floatValue());
        }
        if (cls == Integer.class || cls == Integer.TYPE) {
            return Integer.valueOf(number.intValue());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("getPropertyValue, clz must be float or int instead of ");
        sb.append(cls);
        throw new IllegalArgumentException(sb.toString());
    }

    private void setValueByField(Object obj, Field field, Object obj2) {
        try {
            field.set(obj, obj2);
        } catch (Exception unused) {
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != ValueTargetObject.class) {
            Object obj2 = this.mTempObj;
            if (obj2 != null) {
                return Objects.equals(obj2, obj);
            }
            Object refObject = getRefObject();
            if (refObject != null) {
                return Objects.equals(refObject, obj);
            }
            return false;
        }
        ValueTargetObject valueTargetObject = (ValueTargetObject) obj;
        Object obj3 = this.mTempObj;
        return obj3 != null ? Objects.equals(obj3, valueTargetObject.mTempObj) : Objects.equals(getRefObject(), valueTargetObject.getRefObject());
    }

    public Object getPropertyValue(String str, Class cls) {
        Object refObject = getRefObject();
        if (this.mTempObj == null && refObject != null) {
            Object invokeGetter = invokeGetter(str, cls, refObject);
            if (invokeGetter != null) {
                return invokeGetter;
            }
            Field field = getField(refObject, str, cls);
            if (field != null) {
                return getValueByField(refObject, field);
            }
        }
        return this.mMap.get(str);
    }

    public int hashCode() {
        Object obj = this.mTempObj;
        if (obj != null) {
            return obj.hashCode();
        }
        Object refObject = getRefObject();
        if (refObject != null) {
            return refObject.hashCode();
        }
        return 0;
    }

    public boolean isValid() {
        return (this.mTempObj == null && getRefObject() == null) ? false : true;
    }

    public void setPropertyValue(String str, Class cls, Object obj) {
        Object refObject = getRefObject();
        if (this.mTempObj == null && refObject != null) {
            Method method = getMethod(refObject, getMethodName(str, SET, this.mSetterNameMap), this.mSetterMap, cls);
            if (method != null) {
                invokeMethod(refObject, method, obj);
                return;
            }
            Field field = getField(refObject, str, cls);
            if (field != null) {
                setValueByField(refObject, field, obj);
                return;
            }
        }
        this.mMap.put(str, obj);
    }
}
