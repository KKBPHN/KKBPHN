package com.android.camera.scene;

import android.content.res.Resources.NotFoundException;
import com.android.camera.CameraAppImpl;
import com.android.camera.module.Camera2Module;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.statistic.MistatsConstants.CaptureAttr;
import com.android.camera.statistic.MistatsConstants.FeatureName;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene.ASDScene;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class NoneSemanticsClassResultParse extends ASDResultParse {
    private static final int SUPER_NIGHT_VALUE_MASK = 255;

    public NoneSemanticsClassResultParse(WeakReference weakReference) {
        super(weakReference);
    }

    private void showSuperNightTip(int i, int i2) {
        int sceneTipResId = MiAlgoAsdSceneProfile.getSceneTipResId(i, i2);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (MiAlgoAsdSceneProfile.isTipEnable(i)) {
            try {
                String string = CameraAppImpl.getAndroidContext().getResources().getString(sceneTipResId);
                boolean z = false;
                if (bottomPopupTips != null && bottomPopupTips.isTipShowing() && string != null && string.equals(bottomPopupTips.getCurrentBottomTipMsg())) {
                    z = true;
                }
                if (z && !MiAlgoAsdSceneProfile.isCheckSceneEnable(i, i2)) {
                    closeTip();
                }
            } catch (NotFoundException unused) {
            }
        } else if (MiAlgoAsdSceneProfile.isCheckSceneEnable(i, i2) && sceneTipResId > 0) {
            if (showTip(14, sceneTipResId, 2)) {
                MiAlgoAsdSceneProfile.setTipEnable(i, true);
            }
            HashMap hashMap = new HashMap();
            hashMap.put(CaptureAttr.PARAM_ASD_DETECT_TIP, CaptureAttr.VALUE_ASD_SUPER_NIGHT_TIP);
            MistatsWrapper.mistatEvent(FeatureName.KEY_COMMON_TIPS, hashMap);
        }
    }

    private void updateASDScene(ASDScene aSDScene) {
        int i = aSDScene.type;
        int i2 = aSDScene.value;
        if (i == 8) {
            updateASDSceneHDR(i, i2);
        }
    }

    private void updateASDSceneHDR(int i, int i2) {
        WeakReference weakReference = this.mModule;
        if (weakReference != null && weakReference.get() != null && (this.mModule.get() instanceof Camera2Module)) {
            ((Camera2Module) this.mModule.get()).updateHDRTip(MiAlgoAsdSceneProfile.isCheckSceneEnable(i, i2));
        }
    }

    public void parseMiAlgoAsdResult(ASDScene[] aSDSceneArr) {
        if (aSDSceneArr != null && aSDSceneArr.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("(NoneSemantics)scenes size:");
            sb.append(aSDSceneArr.length);
            FunctionMiAlgoASDEngine.LOGD(sb.toString());
            for (ASDScene aSDScene : aSDSceneArr) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("(NoneSemantics)-->");
                sb2.append(aSDScene.toString());
                FunctionMiAlgoASDEngine.LOGD(sb2.toString());
                updateASDScene(aSDScene);
            }
        }
    }
}
