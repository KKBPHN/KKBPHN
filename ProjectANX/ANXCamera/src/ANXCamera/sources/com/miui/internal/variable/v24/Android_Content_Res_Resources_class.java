package com.miui.internal.variable.v24;

import android.content.res.AssetManager;
import android.content.res.Resources;
import com.miui.internal.variable.VariableExceptionHandler;
import miui.reflect.Field;

public class Android_Content_Res_Resources_class extends com.miui.internal.variable.v21.Android_Content_Res_Resources_class {
    protected static Field mResourcesImpl;

    public void buildProxy() {
    }

    /* access modifiers changed from: protected */
    public void handle() {
    }

    public void setAssetManager(Resources resources, AssetManager assetManager) {
        String str = "mAssets";
        try {
            if (mResourcesImpl == null) {
                mResourcesImpl = Field.of(Resources.class, "mResourcesImpl", "Landroid/content/res/ResourcesImpl;");
                com.miui.internal.variable.Android_Content_Res_Resources_class.mAssets = Field.of(Class.forName("android/content/res/ResourcesImpl"), str, "Landroid/content/res/AssetManager;");
            }
            com.miui.internal.variable.Android_Content_Res_Resources_class.mAssets.set(mResourcesImpl.get(resources), (Object) assetManager);
        } catch (ClassNotFoundException | RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow(str, e);
        }
    }
}
