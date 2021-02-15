package com.android.camera.module;

import android.hardware.camera2.CaptureResult;
import com.android.camera2.CaptureResultParser;
import com.android.camera2.vendortag.struct.MarshalQueryableDxoAsdScene.ASDScene;
import io.reactivex.functions.Function;
import java.lang.ref.WeakReference;

class FunctionDxoAsdSceneDetected implements Function {
    private ASDScene mCurrentAsdScene;
    private WeakReference mModuleCallback;

    interface IDxoAsdSceneDetected {
        void updateDxoAsdScene(ASDScene aSDScene);
    }

    public FunctionDxoAsdSceneDetected(IDxoAsdSceneDetected iDxoAsdSceneDetected) {
        this.mModuleCallback = new WeakReference(iDxoAsdSceneDetected);
    }

    public CaptureResult apply(CaptureResult captureResult) {
        if (this.mModuleCallback.get() == null) {
            return captureResult;
        }
        IDxoAsdSceneDetected iDxoAsdSceneDetected = (IDxoAsdSceneDetected) this.mModuleCallback.get();
        if (iDxoAsdSceneDetected instanceof Camera2Module) {
            ASDScene dxoAsdScene = CaptureResultParser.getDxoAsdScene(((Camera2Module) iDxoAsdSceneDetected).mCameraCapabilities, captureResult);
            if (dxoAsdScene.equals(this.mCurrentAsdScene)) {
                return captureResult;
            }
            this.mCurrentAsdScene = dxoAsdScene;
            iDxoAsdSceneDetected.updateDxoAsdScene(dxoAsdScene);
        }
        return captureResult;
    }
}
