package com.miui.internal.variable;

import android.view.ViewGroup;
import com.miui.internal.util.ClassProxy;

public abstract class Android_View_ViewGroup_class extends ClassProxy implements IManagedClassProxy {

    public class Factory extends AbsClassFactory {
        private Android_View_ViewGroup_class Android_View_ViewGroup_class;

        class Holder {
            static final Factory INSTANCE = new Factory();

            private Holder() {
            }
        }

        private Factory() {
            this.Android_View_ViewGroup_class = (Android_View_ViewGroup_class) create("Android_View_ViewGroup_class");
        }

        public static Factory getInstance() {
            return Holder.INSTANCE;
        }

        public Android_View_ViewGroup_class get() {
            return this.Android_View_ViewGroup_class;
        }
    }

    public Android_View_ViewGroup_class() {
        super(ViewGroup.class);
    }

    public boolean getTagChildrenSequenceState(ViewGroup viewGroup) {
        return com.miui.internal.variable.Android_View_View_class.Factory.getInstance().get().getCustomizedValue(viewGroup).mTagChildrenSequenceState;
    }

    public void setTagChildrenSequenceState(ViewGroup viewGroup, boolean z) {
        com.miui.internal.variable.Android_View_View_class.Factory.getInstance().get().getCustomizedValue(viewGroup).mTagChildrenSequenceState = z;
    }
}
