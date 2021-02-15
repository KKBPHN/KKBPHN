package com.android.camera.module.impl.component;

import com.android.camera.aiwatermark.lisenter.IASDListener;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.AIWatermarkDetect;
import com.android.camera.protocol.ModeProtocol.BaseProtocol;

public class AIWatermarkDetectImpl implements AIWatermarkDetect {
    private int mASDResult = 0;
    private IASDListener mListener = null;

    public static BaseProtocol create() {
        return new AIWatermarkDetectImpl();
    }

    public void onASDChange(int i) {
        if (this.mASDResult != i) {
            this.mASDResult = i;
            IASDListener iASDListener = this.mListener;
            if (iASDListener != null) {
                iASDListener.onASDChange(i);
            }
        }
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(254, this);
    }

    public void resetResult() {
        this.mASDResult = 0;
    }

    public void setListener(IASDListener iASDListener) {
        this.mListener = iASDListener;
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(254, this);
    }
}
