package com.android.camera2.vendortag.struct;

import android.hardware.camera2.marshal.MarshalRegistry;
import com.android.camera2.vendortag.struct.AECFrameControl.MarshalQueryableAECFrameControl;
import com.android.camera2.vendortag.struct.AFFrameControl.MarshalQueryableAFFrameControl;
import com.android.camera2.vendortag.struct.AWBFrameControl.MarshalQueryableAWBFrameControl;
import com.android.camera2.vendortag.struct.SatFusionCalibrationData.SatFusionCalibrationInfoMarshalQueryable;
import com.android.camera2.vendortag.struct.SlowMotionVideoConfiguration.MarshalQueryableSlowMotionVideoConfiguration;

public class MarshalQueryableRegister {
    public static void preload() {
        MarshalRegistry.registerMarshalQueryable(new MarshalQueryableAWBFrameControl());
        MarshalRegistry.registerMarshalQueryable(new MarshalQueryableSlowMotionVideoConfiguration());
        MarshalRegistry.registerMarshalQueryable(new MarshalQueryableAECFrameControl());
        MarshalRegistry.registerMarshalQueryable(new MarshalQueryableAFFrameControl());
        MarshalRegistry.registerMarshalQueryable(new MarshalQueryableASDScene());
        MarshalRegistry.registerMarshalQueryable(new SatFusionCalibrationInfoMarshalQueryable());
        MarshalRegistry.registerMarshalQueryable(new MarshalQueryableChiRect());
        MarshalRegistry.registerMarshalQueryable(new MarshalQueryableDxoAsdScene());
    }
}
