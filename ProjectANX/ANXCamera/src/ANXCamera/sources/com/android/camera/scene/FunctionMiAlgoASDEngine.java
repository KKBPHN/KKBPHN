package com.android.camera.scene;

import android.hardware.camera2.CaptureResult;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTag;
import com.android.camera2.vendortag.VendorTagHelper;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene.ASDScene;
import io.reactivex.functions.Function;
import java.lang.ref.WeakReference;

public class FunctionMiAlgoASDEngine implements Function {
    private static final boolean DEBUG = SystemProperties.getBoolean(TAG, false);
    public static final String TAG = "MI_ALGO_ASD_SCENE";
    private WeakReference mModule;
    private SparseArray mResultParseList = new SparseArray();
    private SparseArray mVendorTagArray = new SparseArray();

    public FunctionMiAlgoASDEngine(BaseModule baseModule) {
        this.mVendorTagArray.put(0, CaptureResultVendorTags.SEMANTIC_SCENE);
        this.mVendorTagArray.put(1, CaptureResultVendorTags.NON_SEMANTIC_SCENE);
        this.mVendorTagArray.put(2, CaptureResultVendorTags.STATE_SCENE);
        this.mModule = new WeakReference(baseModule);
    }

    public static void LOGD(String str) {
        if (DEBUG && !TextUtils.isEmpty(str)) {
            Log.d(TAG, str);
        }
    }

    private void parseCaptureResult(CaptureResult captureResult) {
        for (int i = 0; i < this.mVendorTagArray.size(); i++) {
            int keyAt = this.mVendorTagArray.keyAt(i);
            VendorTag vendorTag = (VendorTag) this.mVendorTagArray.valueAt(i);
            ASDScene[] aSDSceneArr = (ASDScene[]) VendorTagHelper.getValueQuietly(captureResult, vendorTag);
            if (aSDSceneArr == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("(");
                sb.append(vendorTag.getName());
                sb.append(") asd scene result null!");
                LOGD(sb.toString());
            } else {
                parseMiAlgoASDSceneResult(keyAt, aSDSceneArr);
            }
        }
    }

    private void parseMiAlgoASDSceneResult(int i, ASDScene[] aSDSceneArr) {
        IResultParse iResultParse = (IResultParse) this.mResultParseList.get(i);
        if (i != 0) {
            if (i != 1) {
                if (i == 2 && iResultParse == null) {
                    iResultParse = new StateClassResultParse(this.mModule);
                }
            } else if (iResultParse == null) {
                iResultParse = new NoneSemanticsClassResultParse(this.mModule);
            }
        } else if (iResultParse == null) {
            iResultParse = new SemanticsClassResultParse(this.mModule);
        }
        if (iResultParse != null) {
            this.mResultParseList.put(i, iResultParse);
            iResultParse.parseMiAlgoAsdResult(aSDSceneArr);
        }
    }

    public CaptureResult apply(CaptureResult captureResult) {
        BaseModule baseModule = (BaseModule) this.mModule.get();
        if (baseModule == null) {
            return captureResult;
        }
        if (baseModule.getModuleIndex() == 163 || baseModule.getModuleIndex() == 165 || baseModule.getModuleIndex() == 205 || baseModule.getModuleIndex() == 187) {
            CameraCapabilities cameraCapabilities = baseModule.getCameraCapabilities();
            if (cameraCapabilities == null) {
                return captureResult;
            }
            float miAlgoASDVersion = cameraCapabilities.getMiAlgoASDVersion();
            StringBuilder sb = new StringBuilder();
            sb.append("mi algo asd version:");
            sb.append(miAlgoASDVersion);
            LOGD(sb.toString());
            if (miAlgoASDVersion < 2.0f) {
                return captureResult;
            }
            parseCaptureResult(captureResult);
            return captureResult;
        }
        LOGD("no capture mode!");
        return captureResult;
    }
}
