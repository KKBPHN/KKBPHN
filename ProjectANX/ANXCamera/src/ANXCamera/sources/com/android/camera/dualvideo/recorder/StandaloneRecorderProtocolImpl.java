package com.android.camera.dualvideo.recorder;

import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.StandaloneRecorderProtocol;
import com.android.camera.storage.ImageSaver;

public class StandaloneRecorderProtocolImpl implements StandaloneRecorderProtocol {
    private static final String TAG = "DualVideoRecorderProtocol";
    private MultiRecorderManager mRecordManager;

    public MultiRecorderManager getRecorderManager(ImageSaver imageSaver) {
        if (this.mRecordManager == null) {
            if (imageSaver == null) {
                return null;
            }
            this.mRecordManager = new MultiRecorderManager(imageSaver);
        }
        return this.mRecordManager;
    }

    public void registerProtocol() {
        Log.d(TAG, "registerProtocol: ");
        ModeCoordinatorImpl.getInstance().attachProtocol(429, this);
    }

    public void unRegisterProtocol() {
        Log.d(TAG, "unRegisterProtocol: ");
        ModeCoordinatorImpl.getInstance().detachProtocol(429, this);
        MultiRecorderManager multiRecorderManager = this.mRecordManager;
        if (multiRecorderManager != null) {
            multiRecorderManager.stopRecorder(null);
            this.mRecordManager = null;
        }
    }
}
