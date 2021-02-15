package com.miui.internal.variable;

import android.graphics.drawable.Drawable;
import com.miui.internal.util.async.ConcurrentWeakHashMap;

public abstract class Android_Graphics_Drawable_Drawable_class {
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    private static final ConcurrentWeakHashMap mIdField = new ConcurrentWeakHashMap();

    public class Factory extends AbsClassFactory {
        private Android_Graphics_Drawable_Drawable_class Android_Graphics_Drawable_Drawable_class;

        class Holder {
            static final Factory INSTANCE = new Factory();

            private Holder() {
            }
        }

        private Factory() {
            this.Android_Graphics_Drawable_Drawable_class = (Android_Graphics_Drawable_Drawable_class) create("Android_Graphics_Drawable_Drawable_class");
        }

        public static Factory getInstance() {
            return Holder.INSTANCE;
        }

        public Android_Graphics_Drawable_Drawable_class get() {
            return this.Android_Graphics_Drawable_Drawable_class;
        }
    }

    public int getId(Drawable drawable) {
        Integer num = (Integer) mIdField.get(drawable);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public abstract int getLayoutDirection(Drawable drawable);

    public void setId(Drawable drawable, int i) {
        mIdField.put(drawable, Integer.valueOf(i));
    }
}
