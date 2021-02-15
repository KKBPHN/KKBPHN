package miuix.internal.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.TypedValue;

public class AttributeResolver {
    private static final TypedValue TYPED_VALUE = new TypedValue();
    private static final ThreadLocal TYPED_VALUE_THREAD_LOCAL = new ThreadLocal();

    protected AttributeResolver() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    private static TypedValue getTypedValue(Context context) {
        if (context.getMainLooper().getThread() == Thread.currentThread()) {
            return TYPED_VALUE;
        }
        TypedValue typedValue = (TypedValue) TYPED_VALUE_THREAD_LOCAL.get();
        if (typedValue == null) {
            typedValue = new TypedValue();
            TYPED_VALUE_THREAD_LOCAL.set(typedValue);
        }
        return typedValue;
    }

    public static int resolve(Context context, int i) {
        TypedValue typedValue = getTypedValue(context);
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            return typedValue.resourceId;
        }
        return -1;
    }

    public static boolean resolveBoolean(Context context, int i, boolean z) {
        TypedValue typedValue = getTypedValue(context);
        return context.getTheme().resolveAttribute(i, typedValue, true) ? typedValue.type == 18 && typedValue.data != 0 : z;
    }

    public static int resolveColor(Context context, int i) {
        Resources resources;
        int i2;
        TypedValue typedValue = getTypedValue(context);
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            if (typedValue.resourceId > 0) {
                resources = context.getResources();
                i2 = typedValue.resourceId;
                return resources.getColor(i2);
            }
            int i3 = typedValue.type;
            if (i3 >= 28 && i3 <= 31) {
                return typedValue.data;
            }
        }
        resources = context.getResources();
        i2 = -1;
        return resources.getColor(i2);
    }

    public static float resolveDimension(Context context, int i) {
        return context.getResources().getDimension(resolve(context, i));
    }

    public static int resolveDimensionPixelSize(Context context, int i) {
        return context.getResources().getDimensionPixelSize(resolve(context, i));
    }

    public static Drawable resolveDrawable(Context context, int i) {
        TypedValue typedValue = getTypedValue(context);
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            if (typedValue.resourceId > 0) {
                return VERSION.SDK_INT >= 21 ? context.getResources().getDrawable(typedValue.resourceId, context.getTheme()) : context.getResources().getDrawable(typedValue.resourceId);
            }
            int i2 = typedValue.type;
            if (i2 >= 28 && i2 <= 31) {
                return new ColorDrawable(typedValue.data);
            }
        }
        return null;
    }

    public static float resolveFloat(Context context, int i, float f) {
        TypedValue typedValue = getTypedValue(context);
        return (!context.getTheme().resolveAttribute(i, typedValue, true) || typedValue.type != 4) ? f : (float) typedValue.data;
    }

    public static int resolveInt(Context context, int i, int i2) {
        TypedValue typedValue = getTypedValue(context);
        return (!context.getTheme().resolveAttribute(i, typedValue, true) || typedValue.type != 16) ? i2 : typedValue.data;
    }
}
