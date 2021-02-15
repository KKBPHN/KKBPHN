package com.android.camera.module.loader;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.hardware.camera2.CaptureResult;
import com.android.camera.module.BaseModule;
import com.android.camera.module.Camera2Module;
import com.android.camera.module.VideoModule;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera2.CaptureResultParser;
import io.reactivex.functions.Function;
import java.lang.ref.WeakReference;

public class FunctionParseHistogramStats implements Function {
    private WeakReference mModule;
    private int[] mStats = new int[0];
    private WeakReference mTopAlertProtocolWeakReference;

    public FunctionParseHistogramStats(BaseModule baseModule, TopAlert topAlert) {
        this.mModule = new WeakReference(baseModule);
        this.mTopAlertProtocolWeakReference = new WeakReference(topAlert);
    }

    public int[] apply(CaptureResult captureResult) {
        BaseModule baseModule;
        WeakReference weakReference = this.mTopAlertProtocolWeakReference;
        if (weakReference == null) {
            return this.mStats;
        }
        TopAlert topAlert = (TopAlert) weakReference.get();
        if (topAlert == null) {
            return this.mStats;
        }
        if (this.mModule.get() == null) {
            return this.mStats;
        }
        if (this.mModule.get() instanceof VideoModule) {
            baseModule = (VideoModule) this.mModule.get();
        } else if (!(this.mModule.get() instanceof Camera2Module)) {
            return this.mStats;
        } else {
            baseModule = (Camera2Module) this.mModule.get();
        }
        if (C0124O00000oO.isMTKPlatform() && baseModule != null && !CaptureResultParser.isHistogramStatsEnabled(baseModule.getCameraCapabilities(), captureResult)) {
            return this.mStats;
        }
        int[] histogramStats = CaptureResultParser.getHistogramStats(captureResult);
        if (histogramStats != null) {
            this.mStats = histogramStats;
            int[] iArr = new int[256];
            int i = 0;
            while (i < 256) {
                iArr[i] = this.mStats[C0124O00000oO.isMTKPlatform() ? i : i * 3];
                i++;
            }
            topAlert.updateHistogramStatsData(iArr);
        }
        return this.mStats;
    }
}
