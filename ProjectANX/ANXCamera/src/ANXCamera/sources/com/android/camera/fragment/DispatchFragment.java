package com.android.camera.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.android.camera.data.DataRepository;
import com.android.camera.data.backup.DataBackUp;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.data.extra.DataItemLive;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.fragment.clone.Config;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera2.CameraCapabilities;
import java.util.List;

public class DispatchFragment extends BaseFragment {
    private static final String TAG = "FragmentDispatch";

    private void reInitData(int i, int i2, int i3, int i4) {
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        DataItemLive dataItemLive = DataRepository.dataItemLive();
        DataBackUp backUp = DataRepository.getInstance().backUp();
        DataRepository.dataItemLive().resetMimojiIfNeed(i);
        Config.resetIfNeed(i);
        if (i4 == 3 || i4 == 6) {
            DataRepository.dataItemLive().clearAll();
            dataItemRunning.clearArrayMap();
            backUp.clearBackUp();
        } else if (dataItemRunning.getBackupKey() > 0) {
            backUp.backupRunning(dataItemRunning, dataItemRunning.getBackupKey());
        }
        backUp.revertOrCreateRunning(dataItemRunning, dataItemGlobal.getDataBackUpKey(i, dataItemGlobal.getCurrentCameraId()));
        dataItemRunning.reInitSupport(i, i2);
        CameraCapabilities capabilitiesByBogusCameraId = Camera2DataContainer.getInstance().getCapabilitiesByBogusCameraId(i2, i);
        if (capabilitiesByBogusCameraId != null) {
            dataItemRunning.reInitComponent(i, i2, capabilitiesByBogusCameraId, i3);
            dataItemConfig.reInitComponent(i, i2, capabilitiesByBogusCameraId);
            dataItemLive.reInitComponent(i, i2, capabilitiesByBogusCameraId);
        }
    }

    public int getFragmentInto() {
        return 13;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
    }

    public boolean needViewClear() {
        return true;
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        Log.d(TAG, "frameAvailable");
        reCheck();
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.mCurrentMode = DataRepository.dataItemGlobal().getCurrentMode();
        provideAnimateElement(this.mCurrentMode, null, 2);
        return null;
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        StringBuilder sb = new StringBuilder();
        sb.append("reInit ,  resetType = ");
        sb.append(i2);
        Log.d(TAG, sb.toString());
        reInitData(this.mCurrentMode, DataRepository.dataItemGlobal().getCurrentCameraId(), DataRepository.dataItemGlobal().getIntentType(), i2);
    }

    public void reCheck() {
        int i = this.mCurrentMode;
        if (i != 254 && i != 209 && i != 210) {
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChanges != null) {
                configChanges.reCheckMutexConfigs(this.mCurrentMode);
                configChanges.reCheckFocusPeakConfig();
                configChanges.reCheckExposureFeedbackConfig();
                configChanges.reCheckParameterResetTip(true);
                configChanges.reCheckParameterDescriptionTip();
                configChanges.reCheckRaw();
                configChanges.reCheckDocumentMode();
                configChanges.reCheckDualVideoMode();
                configChanges.reCheckGradienter();
                configChanges.reCheckVideoUltraClearTip();
                configChanges.reCheckVideoHDR10Tip();
                configChanges.reCheckVideoLog();
                configChanges.reCheckAIWatermark(false);
                configChanges.reConfigSpeechShutter();
                configChanges.recheckVideoFps(true);
                configChanges.recheckFunAR();
                configChanges.reCheckFastMotion(true);
                configChanges.reCheckFilm();
            }
        }
    }
}
