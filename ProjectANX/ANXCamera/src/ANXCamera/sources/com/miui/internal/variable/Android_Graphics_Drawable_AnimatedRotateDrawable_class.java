package com.miui.internal.variable;

import android.graphics.drawable.Drawable;
import miui.util.SoftReferenceSingleton;

public abstract class Android_Graphics_Drawable_AnimatedRotateDrawable_class extends Android_Graphics_Drawable_Drawable_class {
    public static final String NAME = "android.graphics.drawable.AnimatedRotateDrawable";

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Graphics_Drawable_AnimatedRotateDrawable_class Android_Graphics_Drawable_AnimatedRotateDrawable_class;

        private Factory() {
            this.Android_Graphics_Drawable_AnimatedRotateDrawable_class = (Android_Graphics_Drawable_AnimatedRotateDrawable_class) create("Android_Graphics_Drawable_AnimatedRotateDrawable_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Graphics_Drawable_AnimatedRotateDrawable_class get() {
            return this.Android_Graphics_Drawable_AnimatedRotateDrawable_class;
        }
    }

    public abstract void setFramesCount(Drawable drawable, int i);

    public abstract void setFramesDuration(Drawable drawable, int i);

    public abstract void start(Drawable drawable);

    public abstract void stop(Drawable drawable);
}
