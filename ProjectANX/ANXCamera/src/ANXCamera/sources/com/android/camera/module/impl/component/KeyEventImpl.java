package com.android.camera.module.impl.component;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.view.InputDevice;
import com.android.camera.ActivityBase;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.KeyEvent;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.ModeChangeController;
import com.android.camera.protocol.ModeProtocol.ZoomProtocol;
import java.util.Optional;

public class KeyEventImpl implements KeyEvent {
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    public static final int KEYCODE_MEDIA_PLAY = 126;
    public static final int KEYCODE_MODE_SWITCH = 259;
    public static final int KEYCODE_SHUTTER = 24;
    public static final int KEYCODE_SWITCH_LENS = 119;
    public static final int KEYCODE_ZOOM_IN = 168;
    public static final int KEYCODE_ZOOM_OUT = 169;
    private static final String TAG = "KeyEventImpl";
    private final ActivityBase mActivity;
    private boolean mIsZoomInDown;
    private boolean mIsZoomOutDown;
    private final boolean mSupportedFrontFPS120 = C0122O00000o.instance().OOOo0OO();

    public KeyEventImpl(ActivityBase activityBase) {
        this.mActivity = activityBase;
    }

    private void changeMode() {
        ActivityBase activityBase = this.mActivity;
        if (activityBase != null && DataRepository.dataItemGlobal().isNormalIntent()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent() && ((BaseModule) baseModule.get()).isCreated() && !((BaseModule) baseModule.get()).isBlockSnap()) {
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                int i = 162;
                if (!(moduleIndex == 161 || moduleIndex == 162 || moduleIndex == 166 || moduleIndex == 172 || moduleIndex == 174 || moduleIndex == 176 || moduleIndex == 183 || moduleIndex == 214)) {
                    switch (moduleIndex) {
                        case 179:
                        case 180:
                        case 181:
                            break;
                    }
                }
                if (((BaseModule) baseModule.get()).isRecording()) {
                    return;
                }
                ModeChangeController modeChangeController = (ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179);
                if (modeChangeController != null) {
                    int i2 = R.string.module_name_capture;
                    int mappingModeByRatio = DataRepository.dataItemConfig().getComponentConfigRatio().getMappingModeByRatio(DataRepository.dataItemGlobal().getCurrentMode());
                    if (mappingModeByRatio == 163 || mappingModeByRatio == 165) {
                        i2 = R.string.module_name_video;
                    } else {
                        i = 163;
                    }
                    modeChangeController.changeModeByNewMode(i, activityBase.getResources().getString(i2), 0);
                }
            }
        }
    }

    private void changeZoom(android.view.KeyEvent keyEvent, int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent() && ((BaseModule) baseModule.get()).isCreated() && ((BaseModule) baseModule.get()).isZoomEnabled() && DataRepository.dataItemGlobal().getCurrentCameraId() != Camera2DataContainer.getInstance().getFrontCameraId()) {
            boolean O000O00o = C0122O00000o.instance().O000O00o(((BaseModule) baseModule.get()).getModuleIndex());
            int repeatCount = keyEvent.getRepeatCount();
            if ((!O000O00o || !((BaseModule) baseModule.get()).isRecording() || repeatCount <= 0) && !this.mIsZoomInDown && !this.mIsZoomOutDown) {
                if (keyEvent.getAction() == 0) {
                    ZoomProtocol zoomProtocol = (ZoomProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(170);
                    if (zoomProtocol != null) {
                        if (i == 168) {
                            zoomProtocol.zoomIn(0.1f);
                        } else if (i == 169) {
                            zoomProtocol.zoomOut(0.1f);
                        }
                    }
                }
            }
            if (keyEvent.getAction() == 0) {
                if (i != 168) {
                    if (i == 169) {
                        if (!this.mIsZoomOutDown) {
                            this.mIsZoomOutDown = true;
                        } else {
                            return;
                        }
                    }
                } else if (!this.mIsZoomInDown) {
                    this.mIsZoomInDown = true;
                } else {
                    return;
                }
            } else if (keyEvent.getAction() == 1) {
                if (i == 168) {
                    this.mIsZoomInDown = false;
                } else if (i == 169) {
                    this.mIsZoomOutDown = false;
                }
            }
            MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            if (mainContentProtocol != null) {
                mainContentProtocol.onKeyEventSmoothZoom(i, keyEvent);
            }
        }
    }

    public static KeyEvent create(ActivityBase activityBase) {
        return new KeyEventImpl(activityBase);
    }

    private Optional getBaseModule() {
        ActivityBase activityBase = this.mActivity;
        return activityBase == null ? Optional.empty() : Optional.ofNullable((BaseModule) activityBase.getCurrentModule());
    }

    public static final boolean isKeyEventFromMiWatch(android.view.KeyEvent keyEvent) {
        InputDevice device = keyEvent.getDevice();
        boolean z = false;
        if (device == null) {
            return false;
        }
        int vendorId = device.getVendorId();
        int productId = device.getProductId();
        if (vendorId == 224 && productId == 4608) {
            z = true;
        }
        return z;
    }

    private void pauseRecording() {
        BottomMenuProtocol bottomMenuProtocol = (BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197);
        if (bottomMenuProtocol != null) {
            bottomMenuProtocol.pauseRecording();
        }
    }

    private void switchCameraLens() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent() && ((BaseModule) baseModule.get()).isCreated() && !((BaseModule) baseModule.get()).isBlockSnap()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (moduleIndex != 165) {
                if (moduleIndex != 166) {
                    if (moduleIndex != 169) {
                        if (!(moduleIndex == 181 || moduleIndex == 205)) {
                            if (moduleIndex != 176) {
                                if (!(moduleIndex == 177 || moduleIndex == 183 || moduleIndex == 184)) {
                                    switch (moduleIndex) {
                                        case 161:
                                        case 162:
                                        case 163:
                                            break;
                                        default:
                                            switch (moduleIndex) {
                                                case 171:
                                                case 174:
                                                    break;
                                                case 172:
                                                    if (((BaseModule) baseModule.get()).isRecording() || !this.mSupportedFrontFPS120) {
                                                        return;
                                                    }
                                                case 173:
                                                    if (!C0122O00000o.instance().OOo0Oo() && !C0122O00000o.instance().OOoo00()) {
                                                        return;
                                                    }
                                                default:
                                                    return;
                                            }
                                            break;
                                    }
                                }
                            } else if (!C0122O00000o.instance().OOOo0o()) {
                                return;
                            }
                        }
                    } else if (!C0122O00000o.instance().OOO0OOo()) {
                        return;
                    }
                } else if (!C0122O00000o.instance().OOOo00()) {
                    return;
                }
            }
            if (!((BaseModule) baseModule.get()).isRecording()) {
                BottomMenuProtocol bottomMenuProtocol = (BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197);
                if (bottomMenuProtocol != null) {
                    bottomMenuProtocol.onSwitchCameraPicker();
                }
            }
        }
    }

    public boolean onKeyDown(int i, android.view.KeyEvent keyEvent) {
        Optional baseModule = getBaseModule();
        if (!baseModule.isPresent() || !((BaseModule) baseModule.get()).isCreated() || ((BaseModule) baseModule.get()).isIgnoreTouchEvent()) {
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("KeyEventImpl-onKeyDown:");
        sb.append(i);
        Log.d(str, sb.toString());
        if (i == 168) {
            changeZoom(keyEvent, 168);
            return true;
        } else if (i == 169) {
            changeZoom(keyEvent, 169);
            return true;
        } else if (i != 259) {
            return false;
        } else {
            changeMode();
            return true;
        }
    }

    public boolean onKeyUp(int i, android.view.KeyEvent keyEvent) {
        Optional baseModule = getBaseModule();
        if (!baseModule.isPresent() || !((BaseModule) baseModule.get()).isCreated() || ((BaseModule) baseModule.get()).isIgnoreTouchEvent()) {
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("KeyEventImpl-onKeyUp:");
        sb.append(i);
        Log.d(str, sb.toString());
        if (i == 119) {
            switchCameraLens();
            return true;
        } else if (i == 126) {
            pauseRecording();
            return true;
        } else if (i == 127) {
            pauseRecording();
            return true;
        } else if (i == 168) {
            changeZoom(keyEvent, 168);
            return true;
        } else if (i != 169) {
            return false;
        } else {
            changeZoom(keyEvent, 169);
            return true;
        }
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(239, this);
    }

    public void resetZoomKeyEvent() {
        this.mIsZoomOutDown = false;
        this.mIsZoomInDown = false;
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(239, this);
    }
}
