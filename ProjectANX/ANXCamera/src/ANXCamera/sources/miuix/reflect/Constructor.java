package miuix.reflect;

import miuix.os.Native;

public class Constructor {
    private long mPtr = 0;

    private Constructor() {
    }

    public static Constructor of(Class cls, String str) {
        return Native.getConstructor(cls, str);
    }

    public static Constructor of(Class cls, Class... clsArr) {
        return Native.getConstructor(cls, ReflectUtils.getSignature(clsArr, Void.TYPE));
    }

    public static Constructor of(String str, String str2) {
        return Native.getConstructor(str, str2);
    }

    public static Constructor of(java.lang.reflect.Constructor constructor) {
        return Native.getConstructor(constructor);
    }

    public Object newInstance(Object... objArr) {
        return Native.newInstance(this, objArr);
    }

    public java.lang.reflect.Constructor toRefelect() {
        return Native.getReflectConstructor(this);
    }
}
