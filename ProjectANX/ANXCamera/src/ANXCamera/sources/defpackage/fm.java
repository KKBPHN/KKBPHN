package defpackage;

import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;

/* renamed from: fm reason: default package */
final class fm implements PrivilegedExceptionAction {
    public static final Unsafe a() {
        Field[] declaredFields;
        Class<Unsafe> cls = Unsafe.class;
        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            Object obj = field.get(null);
            if (cls.isInstance(obj)) {
                return (Unsafe) cls.cast(obj);
            }
        }
        return null;
    }

    public final /* bridge */ /* synthetic */ Object run() {
        return a();
    }
}
