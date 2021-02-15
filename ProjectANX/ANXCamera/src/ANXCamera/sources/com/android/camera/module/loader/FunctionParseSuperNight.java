package com.android.camera.module.loader;

import android.hardware.camera2.CaptureResult;
import com.android.camera.CameraSettings;
import com.android.camera.scene.FunctionMiAlgoASDEngine;
import com.android.camera2.Camera2Proxy.SuperNightCallback;
import com.android.camera2.CaptureResultParser;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTagHelper;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene.ASDScene;
import com.android.camera2.vendortag.struct.MarshalQueryableSuperNightExif.SuperNightExif;
import com.xiaomi.camera.util.SystemProperties;
import io.reactivex.functions.Function;
import java.lang.ref.WeakReference;

public class FunctionParseSuperNight implements Function {
    public static final int SUPER_NIGHT = 3;
    public static final String TAG = "FunctionParseSuperNight";
    private boolean mIsSuperNight;
    private boolean mIsSupportSuperNightExif;
    private boolean mSkip;
    private WeakReference mSuperNightCallback;

    public FunctionParseSuperNight(SuperNightCallback superNightCallback, boolean z, boolean z2) {
        CameraSettings.setSuperNightOn(false);
        this.mSuperNightCallback = new WeakReference(superNightCallback);
        this.mSkip = z;
        this.mIsSupportSuperNightExif = z2;
    }

    private void updateASDScene(CaptureResult captureResult, SuperNightCallback superNightCallback, ASDScene aSDScene) {
        int i = aSDScene.type;
        int i2 = aSDScene.value & 256;
        boolean z = false;
        if (i2 != 0 || SystemProperties.getBoolean("se", false)) {
            z = true;
        }
        if (i == 3) {
            StringBuilder sb = new StringBuilder();
            sb.append("Algo reporting super night se enable:");
            sb.append(z);
            FunctionMiAlgoASDEngine.LOGD(sb.toString());
            SuperNightExif superNightInfo = CaptureResultParser.getSuperNightInfo(captureResult, this.mIsSupportSuperNightExif);
            if (superNightInfo != null) {
                superNightInfo.result = (float) i2;
            }
            superNightCallback.onSuperNightExif(superNightInfo);
            if (z != this.mIsSuperNight) {
                this.mIsSuperNight = z;
                superNightCallback.onSuperNightChanged(this.mIsSuperNight);
            }
        }
    }

    public CaptureResult apply(CaptureResult captureResult) {
        SuperNightCallback superNightCallback = (SuperNightCallback) this.mSuperNightCallback.get();
        if (superNightCallback == null) {
            return captureResult;
        }
        if (this.mSkip || !superNightCallback.isSupportSuperNight()) {
            this.mIsSuperNight = false;
            superNightCallback.onSuperNightExif(CaptureResultParser.getSuperNightInfo(captureResult, this.mIsSupportSuperNightExif));
            superNightCallback.onSuperNightChanged(false);
            return captureResult;
        }
        ASDScene[] aSDSceneArr = (ASDScene[]) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.NON_SEMANTIC_SCENE);
        if (aSDSceneArr == null || aSDSceneArr.length <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(CaptureResultVendorTags.NON_SEMANTIC_SCENE.getName());
            sb.append(") asd scene result null!");
            FunctionMiAlgoASDEngine.LOGD(sb.toString());
            return captureResult;
        }
        for (ASDScene aSDScene : aSDSceneArr) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("(NoneSemantics)-->");
            sb2.append(aSDScene.toString());
            FunctionMiAlgoASDEngine.LOGD(sb2.toString());
            updateASDScene(captureResult, superNightCallback, aSDScene);
        }
        return captureResult;
    }
}
