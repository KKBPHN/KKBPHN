package com.android.camera.module.impl;

import com.android.camera.ActivityBase;
import com.android.camera.bluetooth.BluetoothHeadsetImp;
import com.android.camera.dualvideo.recorder.StandaloneRecorderProtocolImpl;
import com.android.camera.dualvideo.remote.RemoteServiceProtocolImpl;
import com.android.camera.dualvideo.render.DualVideoRenderProtocolImpl;
import com.android.camera.features.mimoji2.module.impl.MimojiAvatarEngine2Impl;
import com.android.camera.features.mimoji2.module.impl.MimojiVideoEditorImpl;
import com.android.camera.module.impl.component.AIWatermarkDetectImpl;
import com.android.camera.module.impl.component.BackStackImpl;
import com.android.camera.module.impl.component.BeautyRecordingImpl;
import com.android.camera.module.impl.component.CameraClickObservableImpl;
import com.android.camera.module.impl.component.ConfigChangeImpl;
import com.android.camera.module.impl.component.DisplayFoldStatusImpl;
import com.android.camera.module.impl.component.FilmDreamImpl;
import com.android.camera.module.impl.component.GalleryConnectImpl;
import com.android.camera.module.impl.component.KeyEventImpl;
import com.android.camera.module.impl.component.LiveConfigChangeTTImpl;
import com.android.camera.module.impl.component.LiveSubVVImpl;
import com.android.camera.module.impl.component.LiveVideoEditorTTImpl;
import com.android.camera.module.impl.component.MagneticSensorDetectImp;
import com.android.camera.module.impl.component.ManuallyValueChangeImpl;
import com.android.camera.module.impl.component.MiAsdDetectImpl;
import com.android.camera.module.impl.component.MiLiveConfigChangesImpl;
import com.android.camera.module.impl.component.MimojiAvatarEngineImpl;
import com.android.camera.module.impl.component.MultiFeatureManagerImpl;
import com.android.camera.module.impl.component.PresentationDisplayImpl;
import com.android.camera.module.impl.component.RecordingStateChangeImpl;
import com.android.camera.module.impl.component.ShineChangeImpl;
import com.android.camera.module.impl.component.SpeechShutterImpl;
import com.android.camera.protocol.ModeProtocol.BaseProtocol;
import java.util.ArrayList;
import java.util.List;

public class ImplFactory {
    private List mAdditionalProtocolList;
    private List mBaseProtocolList;
    private List mPersistentProtocolList;
    private boolean mReleased;

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.protocol.ModeProtocol$BaseProtocol>, for r3v0, types: [java.util.List, java.util.List<com.android.camera.protocol.ModeProtocol$BaseProtocol>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void detach(List<BaseProtocol> list) {
        if (!this.mReleased) {
            if (list != null) {
                for (BaseProtocol unRegisterProtocol : list) {
                    unRegisterProtocol.unRegisterProtocol();
                }
                list.clear();
            }
        }
    }

    private void initTypes(ActivityBase activityBase, List list, int... iArr) {
        BaseProtocol baseProtocol;
        if (!this.mReleased) {
            for (int i : iArr) {
                switch (i) {
                    case 164:
                        baseProtocol = ConfigChangeImpl.create(activityBase);
                        break;
                    case 171:
                        baseProtocol = BackStackImpl.create(activityBase);
                        break;
                    case 173:
                        baseProtocol = BeautyRecordingImpl.create();
                        break;
                    case 174:
                        baseProtocol = ManuallyValueChangeImpl.create(activityBase);
                        break;
                    case 201:
                        baseProtocol = LiveConfigChangeTTImpl.create(activityBase);
                        break;
                    case 209:
                        baseProtocol = LiveVideoEditorTTImpl.create(activityBase);
                        break;
                    case 212:
                        baseProtocol = RecordingStateChangeImpl.create(activityBase);
                        break;
                    case 217:
                        baseProtocol = MimojiAvatarEngineImpl.create(activityBase);
                        break;
                    case 227:
                        baseProtocol = CameraClickObservableImpl.create();
                        break;
                    case 228:
                        baseProtocol = LiveSubVVImpl.create(activityBase);
                        break;
                    case 234:
                        baseProtocol = ShineChangeImpl.create(activityBase);
                        break;
                    case 235:
                        baseProtocol = MiAsdDetectImpl.create(activityBase);
                        break;
                    case 239:
                        baseProtocol = KeyEventImpl.create(activityBase);
                        break;
                    case 241:
                        baseProtocol = MiLiveConfigChangesImpl.create(activityBase);
                        break;
                    case 246:
                        baseProtocol = MimojiAvatarEngine2Impl.create(activityBase);
                        break;
                    case 252:
                        baseProtocol = MimojiVideoEditorImpl.create(activityBase);
                        break;
                    case 254:
                        baseProtocol = AIWatermarkDetectImpl.create();
                        break;
                    case 255:
                        baseProtocol = SpeechShutterImpl.create();
                        break;
                    case 422:
                        baseProtocol = new RemoteServiceProtocolImpl();
                        break;
                    case 429:
                        baseProtocol = new StandaloneRecorderProtocolImpl();
                        break;
                    case 430:
                        baseProtocol = new DualVideoRenderProtocolImpl(activityBase.getResources());
                        break;
                    case 929:
                        baseProtocol = MultiFeatureManagerImpl.create(activityBase);
                        break;
                    case 930:
                        baseProtocol = FilmDreamImpl.create(activityBase);
                        break;
                    case 933:
                        baseProtocol = BluetoothHeadsetImp.create(activityBase);
                        break;
                    case 937:
                        baseProtocol = GalleryConnectImpl.create(activityBase);
                        break;
                    case 938:
                        baseProtocol = DisplayFoldStatusImpl.create(activityBase);
                        break;
                    case 945:
                        baseProtocol = PresentationDisplayImpl.create(activityBase);
                        break;
                    case 2576:
                        baseProtocol = MagneticSensorDetectImp.create();
                        break;
                    default:
                        throw new RuntimeException("unknown protocol type");
                }
                baseProtocol.registerProtocol();
                list.add(baseProtocol);
            }
        }
    }

    public void detachAdditional() {
        detach(this.mAdditionalProtocolList);
    }

    public void detachBase() {
        detach(this.mBaseProtocolList);
    }

    public void detachModulePersistent() {
        detach(this.mPersistentProtocolList);
    }

    public void initAdditional(ActivityBase activityBase, int... iArr) {
        if (this.mAdditionalProtocolList == null) {
            this.mAdditionalProtocolList = new ArrayList();
        }
        initTypes(activityBase, this.mAdditionalProtocolList, iArr);
    }

    public void initBase(ActivityBase activityBase, int... iArr) {
        if (this.mBaseProtocolList == null) {
            this.mBaseProtocolList = new ArrayList();
        }
        initTypes(activityBase, this.mBaseProtocolList, iArr);
    }

    public void initModulePersistent(ActivityBase activityBase, int... iArr) {
        if (this.mPersistentProtocolList == null) {
            this.mPersistentProtocolList = new ArrayList();
        }
        initTypes(activityBase, this.mPersistentProtocolList, iArr);
    }

    @Deprecated
    public void release() {
        if (!this.mReleased) {
            detachAdditional();
            detachModulePersistent();
            detachBase();
            this.mReleased = true;
        }
    }
}
