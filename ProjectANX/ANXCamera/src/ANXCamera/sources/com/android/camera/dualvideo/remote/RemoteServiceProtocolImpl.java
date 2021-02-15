package com.android.camera.dualvideo.remote;

import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.RemoteCameraServiceProtocol;

public class RemoteServiceProtocolImpl implements RemoteCameraServiceProtocol {
    private static final String TAG = "RemoteServiceProtocolIm";
    private RemoteService mRemoteService;

    public RemoteService getService() {
        Log.d(TAG, "getService: ");
        if (this.mRemoteService == null) {
            this.mRemoteService = new RemoteService();
        }
        return this.mRemoteService;
    }

    public void registerProtocol() {
        Log.d(TAG, "registerProtocol: ");
        ModeCoordinatorImpl.getInstance().attachProtocol(422, this);
    }

    public void unRegisterProtocol() {
        Log.d(TAG, "unRegisterProtocol: ");
        ModeCoordinatorImpl.getInstance().detachProtocol(422, this);
        RemoteService remoteService = this.mRemoteService;
        if (remoteService != null) {
            remoteService.stopStreaming();
            this.mRemoteService = null;
        }
    }
}
