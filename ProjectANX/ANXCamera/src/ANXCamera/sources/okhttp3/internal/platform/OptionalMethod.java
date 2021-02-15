package okhttp3.internal.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class OptionalMethod {
    private final String methodName;
    private final Class[] methodParams;
    private final Class returnType;

    OptionalMethod(Class cls, String str, Class... clsArr) {
        this.returnType = cls;
        this.methodName = str;
        this.methodParams = clsArr;
    }

    private Method getMethod(Class cls) {
        String str = this.methodName;
        if (str == null) {
            return null;
        }
        Method publicMethod = getPublicMethod(cls, str, this.methodParams);
        if (publicMethod != null) {
            Class cls2 = this.returnType;
            if (cls2 != null && !cls2.isAssignableFrom(publicMethod.getReturnType())) {
                return null;
            }
        }
        return publicMethod;
    }

    private static Method getPublicMethod(Class cls, String str, Class[] clsArr) {
        try {
            Method method = cls.getMethod(str, clsArr);
            try {
                if ((method.getModifiers() & 1) != 0) {
                    return method;
                }
            } catch (NoSuchMethodException unused) {
                return method;
            }
        } catch (NoSuchMethodException unused2) {
        }
        return null;
    }

    public Object invoke(Object obj, Object... objArr) {
        Method method = getMethod(obj.getClass());
        if (method != null) {
            try {
                return method.invoke(obj, objArr);
            } catch (IllegalAccessException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpectedly could not call: ");
                sb.append(method);
                AssertionError assertionError = new AssertionError(sb.toString());
                assertionError.initCause(e);
                throw assertionError;
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Method ");
            sb2.append(this.methodName);
            sb2.append(" not supported for object ");
            sb2.append(obj);
            throw new AssertionError(sb2.toString());
        }
    }

    public Object invokeOptional(Object obj, Object... objArr) {
        Method method = getMethod(obj.getClass());
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException unused) {
            return null;
        }
    }

    public Object invokeOptionalWithoutCheckedException(Object obj, Object... objArr) {
        try {
            return invokeOptional(obj, objArr);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            AssertionError assertionError = new AssertionError("Unexpected exception");
            assertionError.initCause(targetException);
            throw assertionError;
        }
    }

    public Object invokeWithoutCheckedException(Object obj, Object... objArr) {
        try {
            return invoke(obj, objArr);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            AssertionError assertionError = new AssertionError("Unexpected exception");
            assertionError.initCause(targetException);
            throw assertionError;
        }
    }

    public boolean isSupported(Object obj) {
        return getMethod(obj.getClass()) != null;
    }
}
