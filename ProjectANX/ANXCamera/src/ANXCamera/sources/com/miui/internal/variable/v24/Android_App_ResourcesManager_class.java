package com.miui.internal.variable.v24;

import android.content.res.AssetManager;
import android.content.res.ResourcesImpl;
import android.content.res.ResourcesKey;
import com.miui.internal.variable.Android_Content_Res_AssetManager_class;
import com.miui.internal.variable.Android_Content_Res_AssetManager_class.Factory;
import com.miui.internal.variable.Android_Content_Res_ResourcesImpl_class;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_App_ResourcesManager.Extension;
import com.miui.internal.variable.api.v29.Android_App_ResourcesManager.Interface;

public class Android_App_ResourcesManager_class extends com.miui.internal.variable.Android_App_ResourcesManager_class implements Overridable {
    private static final Android_Content_Res_AssetManager_class AssetManagerClass = Factory.getInstance().get();
    private static final Android_Content_Res_ResourcesImpl_class ResourcesImplClass = Android_Content_Res_ResourcesImpl_class.Factory.getInstance().get();
    private Interface mImpl = new Interface() {
        public ResourcesImpl createResourcesImpl(Object obj, ResourcesKey resourcesKey) {
            return Android_App_ResourcesManager_class.this.handleCreateResourcesImpl(0, obj, resourcesKey);
        }
    };
    private Interface mOriginal;

    public Android_App_ResourcesManager_class() {
        try {
            Class.forName("android.app.ResourcesManager", true, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public Interface asInterface() {
        return this.mImpl;
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    public void buildProxy() {
        attachMethod("createResourcesImpl", "(Landroid/content/res/ResourcesKey;)Landroid/content/res/ResourcesImpl;");
    }

    /* access modifiers changed from: protected */
    public ResourcesImpl callOriginalCreateResourcesImpl(long j, Object obj, ResourcesKey resourcesKey) {
        Interface interfaceR = this.mOriginal;
        return interfaceR != null ? interfaceR.createResourcesImpl(obj, resourcesKey) : originalCreateResourcesImpl(j, obj, resourcesKey);
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleCreateResourcesImpl(0, null, null);
    }

    /* access modifiers changed from: protected */
    public ResourcesImpl handleCreateResourcesImpl(long j, Object obj, ResourcesKey resourcesKey) {
        ResourcesImpl callOriginalCreateResourcesImpl = callOriginalCreateResourcesImpl(j, obj, resourcesKey);
        if (callOriginalCreateResourcesImpl == null || com.miui.internal.variable.Android_App_ResourcesManager_class.mAppendedAssetPaths == null) {
            return callOriginalCreateResourcesImpl;
        }
        AssetManager assets = ResourcesImplClass.getAssets(callOriginalCreateResourcesImpl);
        for (String addAssetPath : com.miui.internal.variable.Android_App_ResourcesManager_class.mAppendedAssetPaths) {
            AssetManagerClass.addAssetPath(assets, addAssetPath);
        }
        return callOriginalCreateResourcesImpl;
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public ResourcesImpl originalCreateResourcesImpl(long j, Object obj, ResourcesKey resourcesKey) {
        throw new IllegalStateException("com.miui.internal.variable.v24.Android_App_ResourcesManager_class.originalCreateResourcesImpl(long, Object, ResourcesKey)");
    }
}
