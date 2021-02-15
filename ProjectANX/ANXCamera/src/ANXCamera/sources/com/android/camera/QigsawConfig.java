package com.android.camera;

import com.android.camera.data.observeable.VMFeature.FeatureModule;
import com.android.camera.statistic.MistatsConstants.BaseEvent;

public final class QigsawConfig {
    public static final String DEFAULT_SPLIT_INFO_VERSION = "5.0.0.0_2.0";
    public static final String[] DYNAMIC_FEATURES = {FeatureModule.MODULE_VLOG2, FeatureModule.MODULE_MILIVE, FeatureModule.MODULE_DOC2, FeatureModule.MODULE_CLONE, FeatureModule.MODULE_PANORAMA, "ambilight", FeatureModule.MODULE_MIMOJI2};
    public static final String QIGSAW_ID = "5.0.0.0";
    public static final boolean QIGSAW_MODE = Boolean.parseBoolean(BaseEvent.VALUE_TRUE);
    public static final String VERSION_NAME = "4.3";
}
