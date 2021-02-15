package com.android.camera;

import java.util.List;

public interface IPreviewMetadata {
    void onPartialPreviewMetadata(Object obj);

    void onPreviewMetadata(Object obj);

    void registerPreviewMeatedata(List list);

    void registerPreviewPartialMetadata(List list);

    void unregisterPreviewMetadata();
}
