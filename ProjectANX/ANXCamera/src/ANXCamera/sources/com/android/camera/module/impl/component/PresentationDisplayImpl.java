package com.android.camera.module.impl.component;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.camera.ActivityBase;
import com.android.camera.Camera;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.PresentationDisplay;
import java.util.Optional;
import miui.hardware.display.IMiuiMultiDisplayManager.Stub;

public class PresentationDisplayImpl implements PresentationDisplay {
    private static final String MIUI_MULTI_DMS_NAME = "miui_multi_display";
    private static final String TAG = "PresentationDisplayImpl";
    private int SUB_SCREEN_DISPLAY_NUM = 1;
    private Camera mActivity;
    private com.android.camera.PresentationDisplay mPresentationDisplay;

    public PresentationDisplayImpl(ActivityBase activityBase) {
        this.mActivity = (Camera) activityBase;
        if (this.mPresentationDisplay == null) {
            this.mPresentationDisplay = new com.android.camera.PresentationDisplay(this.mActivity);
        }
    }

    public static PresentationDisplayImpl create(ActivityBase activityBase) {
        return new PresentationDisplayImpl(activityBase);
    }

    private Optional getActivity() {
        Camera camera = this.mActivity;
        return camera == null ? Optional.empty() : Optional.ofNullable(camera);
    }

    public void cancel() {
        if (C0122O00000o.instance().OOOOO0()) {
            com.android.camera.PresentationDisplay presentationDisplay = this.mPresentationDisplay;
            if (presentationDisplay != null && presentationDisplay.isShowing()) {
                this.mPresentationDisplay.dismiss();
                Log.d(TAG, "presentation display cancel");
            }
            closePresentationDisplay();
        }
    }

    public void closePresentationDisplay() {
        String str;
        String str2;
        Log.d(TAG, "E: closeSubDisplay");
        IBinder service = ServiceManager.getService(MIUI_MULTI_DMS_NAME);
        if (service != null) {
            try {
                Stub.asInterface(service).setDisplayStateIgnoreFold(this.SUB_SCREEN_DISPLAY_NUM, false);
                Log.d(TAG, "X: closeSubDisplay, multi display manager service Success!");
            } catch (RemoteException unused) {
                str = TAG;
                str2 = "open sub display manager service connect fail!";
            }
        } else {
            str = TAG;
            str2 = "multi display manager service no found! ";
            Log.d(str, str2);
        }
    }

    public boolean isModeSupportPresentation(int i) {
        return 163 == i || 165 == i;
    }

    public void openPresentationDisplay() {
        String str;
        String str2;
        Log.d(TAG, "E: openSubDisplay");
        IBinder service = ServiceManager.getService(MIUI_MULTI_DMS_NAME);
        if (service != null) {
            try {
                Stub.asInterface(service).setDisplayStateIgnoreFold(this.SUB_SCREEN_DISPLAY_NUM, true);
                Log.d(TAG, "X: openSubDisplay, open sub display sucess!");
            } catch (RemoteException unused) {
                str = TAG;
                str2 = "open sub display manager service connect fail!";
            }
        } else {
            str = TAG;
            str2 = "multi display manager service no found! ";
            Log.d(str, str2);
        }
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(945, this);
    }

    public void show(int i) {
        if (C0122O00000o.instance().OOOOO0() && getActivity().isPresent()) {
            boolean isModeSupportPresentation = isModeSupportPresentation(this.mActivity.getCurrentModuleIndex());
        }
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(945, this);
    }

    public void updateTextureSize() {
        com.android.camera.PresentationDisplay presentationDisplay = this.mPresentationDisplay;
        if (presentationDisplay != null) {
            presentationDisplay.updateTextureSize();
        }
    }
}
