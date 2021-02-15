package com.miui.internal.variable;

import android.os.Build.VERSION;
import com.miui.internal.util.ClassProxy;

public abstract class Internal_Policy_Impl_PhoneWindow_class extends ClassProxy implements IManagedClassProxy {
    private static final String PhoneWindow_ClassName = (VERSION.SDK_INT >= 23 ? "com.android.internal.policy.PhoneWindow" : "com.android.internal.policy.impl.PhoneWindow");
    protected static final Class TARGET_CLASS = getTargetClass();

    public class Factory extends AbsClassFactory {
        private Internal_Policy_Impl_PhoneWindow_class Internal_Policy_Impl_PhoneWindow_class;

        class Holder {
            static final Factory INSTANCE = new Factory();

            private Holder() {
            }
        }

        private Factory() {
            this.Internal_Policy_Impl_PhoneWindow_class = (Internal_Policy_Impl_PhoneWindow_class) create("Internal_Policy_Impl_PhoneWindow_class");
        }

        public static Factory getInstance() {
            return Holder.INSTANCE;
        }

        public Internal_Policy_Impl_PhoneWindow_class get() {
            return this.Internal_Policy_Impl_PhoneWindow_class;
        }
    }

    public Internal_Policy_Impl_PhoneWindow_class() {
        super(TARGET_CLASS);
    }

    private static Class getTargetClass() {
        try {
            return Class.forName(PhoneWindow_ClassName);
        } catch (ClassNotFoundException e) {
            VariableExceptionHandler.getInstance().onThrow("com.android.internal.policy.impl.PhoneWindow", e);
            return null;
        }
    }
}
