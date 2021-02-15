package com.android.camera;

public interface IPreviewMetadataListener {
    long getSamplePeriod();

    Object onPreviewMetadata(Object obj);

    Object onSamplePreviewMetadata(Object obj);
}
