package com.android.camera;

import java.lang.ref.WeakReference;

public abstract class FuncPreviewMetadata implements IFuncPreviewMetadataListener {
    protected final WeakReference mModuleReference;

    public FuncPreviewMetadata(WeakReference weakReference) {
        this.mModuleReference = weakReference;
    }

    public void accept(Object obj) {
    }

    public final Object apply(Object obj) {
        return onPreviewMetadata(obj);
    }

    public long getSamplePeriod() {
        return 0;
    }

    public final Object onSamplePreviewMetadata(Object obj) {
        return null;
    }
}
