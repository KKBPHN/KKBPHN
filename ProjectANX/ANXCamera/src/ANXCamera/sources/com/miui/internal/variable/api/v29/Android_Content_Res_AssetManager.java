package com.miui.internal.variable.api.v29;

import android.content.res.AssetManager;
import com.miui.internal.variable.api.AbstractExtension;

public interface Android_Content_Res_AssetManager {

    public class Extension extends AbstractExtension {
        private static final Extension INSTANCE = new Extension();

        public static Extension get() {
            return INSTANCE;
        }
    }

    public interface Interface {
        int addAssetPath(AssetManager assetManager, String str);
    }
}
