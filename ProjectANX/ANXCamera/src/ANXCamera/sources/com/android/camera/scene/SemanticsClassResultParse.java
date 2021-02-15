package com.android.camera.scene;

import android.os.Handler;
import com.android.camera.module.BaseModule;
import com.android.camera.module.Camera2Module;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.MistatsConstants.CaptureAttr;
import com.android.camera.statistic.MistatsConstants.FeatureName;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene.ASDScene;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Objects;

public class SemanticsClassResultParse extends ASDResultParse {
    public SemanticsClassResultParse(WeakReference weakReference) {
        super(weakReference);
    }

    private void eventTrack(int i) {
        String str;
        HashMap hashMap = new HashMap();
        String str2 = CaptureAttr.VALUE_ASD_PORTRAIT_TIP;
        String str3 = CaptureAttr.PARAM_ASD_DETECT_TIP;
        if (i != 1) {
            if (i != 2) {
                if (i == 6) {
                    str = CaptureAttr.VALUE_ASD_DIRTY_TIP;
                } else if (i == 7) {
                    str = CaptureAttr.VALUE_ASD_MACRO_TIP;
                } else {
                    return;
                }
                hashMap.put(str3, str);
                MistatsWrapper.mistatEvent(FeatureName.KEY_COMMON_TIPS, hashMap);
            }
            hashMap.put(str3, CaptureAttr.VALUE_ASD_BACKLIT_TIP);
        }
        hashMap.put(str3, str2);
        MistatsWrapper.mistatEvent(FeatureName.KEY_COMMON_TIPS, hashMap);
    }

    private void updateASDScene(ASDScene aSDScene) {
        int i = aSDScene.type;
        int i2 = aSDScene.value;
        BaseModule baseModule = (BaseModule) this.mModule.get();
        if (baseModule != null) {
            if (baseModule.getModuleIndex() == 163 || baseModule.getModuleIndex() == 165) {
                Camera2Module camera2Module = (Camera2Module) baseModule;
                MiAlgoAsdSceneProfile.getSceneTipResId(i, i2);
                if (MiAlgoAsdSceneProfile.isSceneChange(i, i2)) {
                    BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                    if (i == 6 && MiAlgoAsdSceneProfile.isCheckSceneEnable(i, i2) && !isGeneralInterception()) {
                        baseModule.showLensDirtyTip();
                        eventTrack(i);
                    }
                }
            }
        }
    }

    private void updateBacklitScene(int i, int i2, Camera2Module camera2Module) {
        if (MiAlgoAsdSceneProfile.isCheckSceneEnable(i, i2)) {
            if (!camera2Module.isShowBacklightTip()) {
                Handler handler = camera2Module.getHandler();
                Objects.requireNonNull(camera2Module);
                handler.post(new O00000o0(camera2Module));
                eventTrack(i);
            }
        } else if (camera2Module.isShowBacklightTip()) {
            camera2Module.getHandler().post(new O00000o((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)));
        }
    }

    public void parseMiAlgoAsdResult(ASDScene[] aSDSceneArr) {
        if (aSDSceneArr != null && aSDSceneArr.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("(Semantics)scenes size:");
            sb.append(aSDSceneArr.length);
            FunctionMiAlgoASDEngine.LOGD(sb.toString());
            for (ASDScene aSDScene : aSDSceneArr) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("(Semantics)-->");
                sb2.append(aSDScene.toString());
                FunctionMiAlgoASDEngine.LOGD(sb2.toString());
                updateASDScene(aSDScene);
            }
        }
    }
}
