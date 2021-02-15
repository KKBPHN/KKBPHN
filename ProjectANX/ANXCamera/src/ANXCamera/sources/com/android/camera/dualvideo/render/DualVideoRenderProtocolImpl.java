package com.android.camera.dualvideo.render;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.res.Resources;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.DualVideoRenderProtocol;

public class DualVideoRenderProtocolImpl implements DualVideoRenderProtocol {
    private static final String TAG = "DualVideoRenderProtocol";
    private final RenderManager mRenderManager = new RenderManager();

    public DualVideoRenderProtocolImpl(Resources resources) {
        this.mRenderManager.setResources(resources);
    }

    public RenderManager getRenderManager() {
        return this.mRenderManager;
    }

    public void registerProtocol() {
        Log.d(TAG, "registerProtocol: ");
        ModeCoordinatorImpl.getInstance().attachProtocol(430, this);
    }

    public void unRegisterProtocol() {
        Log.d(TAG, "unRegisterProtocol: ");
        if (C0122O00000o.instance().OOO000o()) {
            DataRepository.dataItemRunning().getComponentRunningDualVideo().setmDrawSelectWindow(true);
            getRenderManager().release();
        }
        ModeCoordinatorImpl.getInstance().detachProtocol(430, this);
    }
}
