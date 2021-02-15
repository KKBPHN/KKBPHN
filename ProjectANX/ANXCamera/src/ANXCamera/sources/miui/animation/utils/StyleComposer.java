package miui.animation.utils;

import android.util.Log;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class StyleComposer {
    private static final String TAG = "StyleComposer";

    public interface IInterceptor {
        Object onMethod(Method method, Object[] objArr, Object... objArr2);

        boolean shouldIntercept(Method method, Object[] objArr);
    }

    public static Object compose(final Class cls, final IInterceptor iInterceptor, final Object... objArr) {
        AnonymousClass1 r0 = new InvocationHandler() {
            public Object invoke(Object obj, Method method, Object[] objArr) {
                Object obj2;
                Object[] objArr2;
                IInterceptor iInterceptor = IInterceptor.this;
                if (iInterceptor == null || !iInterceptor.shouldIntercept(method, objArr)) {
                    Object obj3 = null;
                    for (Object obj4 : objArr) {
                        try {
                            obj3 = method.invoke(obj4, objArr);
                        } catch (Exception e) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("failed to invoke ");
                            sb.append(method);
                            sb.append(" for ");
                            sb.append(obj4);
                            Log.w(StyleComposer.TAG, sb.toString(), e.getCause());
                        }
                    }
                    obj2 = obj3;
                } else {
                    obj2 = IInterceptor.this.onMethod(method, objArr, objArr);
                }
                if (obj2 != null) {
                    Object[] objArr3 = objArr;
                    if (obj2 == objArr3[objArr3.length - 1]) {
                        return cls.cast(obj);
                    }
                }
                return obj2;
            }
        };
        Object newProxyInstance = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, r0);
        if (cls.isInstance(newProxyInstance)) {
            return cls.cast(newProxyInstance);
        }
        return null;
    }

    public static Object compose(Class cls, Object... objArr) {
        return compose(cls, null, objArr);
    }
}
