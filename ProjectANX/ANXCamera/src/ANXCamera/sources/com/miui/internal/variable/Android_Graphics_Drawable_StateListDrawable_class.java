package com.miui.internal.variable;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import miui.util.SoftReferenceSingleton;

public abstract class Android_Graphics_Drawable_StateListDrawable_class extends Android_Graphics_Drawable_Drawable_class {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Graphics_Drawable_StateListDrawable_class Android_Graphics_Drawable_StateListDrawable_class;

        private Factory() {
            this.Android_Graphics_Drawable_StateListDrawable_class = (Android_Graphics_Drawable_StateListDrawable_class) create("Android_Graphics_Drawable_StateListDrawable_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Graphics_Drawable_StateListDrawable_class get() {
            return this.Android_Graphics_Drawable_StateListDrawable_class;
        }
    }

    public abstract int getStateCount(StateListDrawable stateListDrawable);

    public abstract Drawable getStateDrawable(StateListDrawable stateListDrawable, int i);

    public abstract int[] getStateSet(StateListDrawable stateListDrawable, int i);
}
