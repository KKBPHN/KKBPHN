package com.miui.internal.variable.v16;

import android.content.res.AssetManager;
import com.miui.internal.util.ClassProxy;
import com.miui.internal.util.PackageConstants;
import com.miui.internal.variable.VariableExceptionHandler;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Content_Res_AssetManager.Extension;
import com.miui.internal.variable.api.v29.Android_Content_Res_AssetManager.Interface;
import miui.reflect.Constructor;
import miui.reflect.Method;

public class Android_Content_Res_AssetManager_class extends ClassProxy implements com.miui.internal.variable.Android_Content_Res_AssetManager_class, Overridable {
    private static final Method mAddAssetPath = Method.of(AssetManager.class, "addAssetPath", "(Ljava/lang/String;)I");
    private Interface mImpl = new Interface() {
        public int addAssetPath(AssetManager assetManager, String str) {
            return Android_Content_Res_AssetManager_class.this.handleAddAssetPath(0, assetManager, str);
        }
    };
    private Interface mOriginal;

    public Android_Content_Res_AssetManager_class() {
        super(AssetManager.class);
    }

    public int addAssetPath(AssetManager assetManager, String str) {
        int i = 0;
        try {
            i = mAddAssetPath.invokeInt(null, assetManager, str);
            return i;
        } catch (Exception e) {
            VariableExceptionHandler.getInstance().onThrow("android.content.res.AssetManager.addAssetPath", e);
            return i;
        }
    }

    public Interface asInterface() {
        return this.mImpl;
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    public void buildProxy() {
        try {
            attachMethod("addAssetPath", "(Ljava/lang/String;)I");
        } catch (Exception e) {
            VariableExceptionHandler.getInstance().onThrow("android.content.res.AssetManager.addAssetPath", e);
        }
    }

    /* access modifiers changed from: protected */
    public int callOriginalAddAssetPath(long j, AssetManager assetManager, String str) {
        Interface interfaceR = this.mOriginal;
        return interfaceR != null ? interfaceR.addAssetPath(assetManager, str) : originalAddAssetPath(j, assetManager, str);
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleAddAssetPath(0, null, null);
    }

    /* access modifiers changed from: protected */
    public int handleAddAssetPath(long j, AssetManager assetManager, String str) {
        if (!PackageConstants.RESOURCE_PATH.equals(str)) {
            callOriginalAddAssetPath(j, assetManager, PackageConstants.RESOURCE_PATH);
        }
        return callOriginalAddAssetPath(j, assetManager, str);
    }

    public AssetManager newInstance() {
        return (AssetManager) Constructor.of(AssetManager.class, "()V").newInstance(new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public int originalAddAssetPath(long j, AssetManager assetManager, String str) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Content_Res_AssetManager_class.originalAddAssetPath(long, AssetManager, String)");
    }
}
