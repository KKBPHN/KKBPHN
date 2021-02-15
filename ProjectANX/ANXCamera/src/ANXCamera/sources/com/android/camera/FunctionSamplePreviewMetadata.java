package com.android.camera;

abstract class FunctionSamplePreviewMetadata implements IFuncPreviewMetadataListener {
    FunctionSamplePreviewMetadata() {
    }

    public final Object apply(Object obj) {
        return onSamplePreviewMetadata(obj);
    }

    public long getSamplePeriod() {
        return 500;
    }

    public final Object onPreviewMetadata(Object obj) {
        return null;
    }
}
