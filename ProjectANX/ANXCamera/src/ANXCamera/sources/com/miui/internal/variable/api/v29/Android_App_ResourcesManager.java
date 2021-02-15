package com.miui.internal.variable.api.v29;

import android.content.res.ResourcesImpl;
import android.content.res.ResourcesKey;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_App_ResourcesManager {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        ResourcesImpl createResourcesImpl(Object obj, ResourcesKey resourcesKey);
    }
}
