package com.android.camera.module.impl.component;

import android.os.RemoteException;
import android.view.IDisplayFoldListener.Stub;
import com.android.camera.ActivityBase;
import com.android.camera.Display;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.DisplayFoldStatus;

public class DisplayFoldStatusImpl extends Stub implements DisplayFoldStatus {
    private static final String TAG = "DisplayFoldStatusImpl";
    private final ActivityBase mActivity;
    private boolean mIsFolded;

    public DisplayFoldStatusImpl(ActivityBase activityBase) {
        this.mActivity = activityBase;
    }

    public static DisplayFoldStatusImpl create(ActivityBase activityBase) {
        return new DisplayFoldStatusImpl(activityBase);
    }

    public boolean isDisplayFold() {
        return this.mIsFolded;
    }

    public void onDisplayFoldChanged(int i, boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("displayId:");
        sb.append(i);
        sb.append(",folded:");
        sb.append(z);
        Log.d(str, sb.toString());
        this.mIsFolded = z;
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(938, this);
        if (Display.getWindowManager() != null) {
            try {
                Display.getWindowManager().registerDisplayFoldListener(this);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(938, this);
        if (Display.getWindowManager() != null) {
            try {
                Display.getWindowManager().unregisterDisplayFoldListener(this);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
