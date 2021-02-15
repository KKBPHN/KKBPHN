package com.miui.internal.variable;

import android.view.accessibility.AccessibilityEvent;
import miui.util.SoftReferenceSingleton;

public abstract class Android_View_Accessibility_AccessibilityEvent_class {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_View_Accessibility_AccessibilityEvent_class Android_View_Accessibility_AccessibilityEvent_class;

        private Factory() {
            this.Android_View_Accessibility_AccessibilityEvent_class = (Android_View_Accessibility_AccessibilityEvent_class) create("Android_View_Accessibility_AccessibilityEvent_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_View_Accessibility_AccessibilityEvent_class get() {
            return this.Android_View_Accessibility_AccessibilityEvent_class;
        }
    }

    public abstract void setContentChangeTypes(AccessibilityEvent accessibilityEvent, int i);
}
