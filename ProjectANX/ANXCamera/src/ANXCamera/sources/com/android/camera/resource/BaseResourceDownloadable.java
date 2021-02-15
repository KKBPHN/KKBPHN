package com.android.camera.resource;

public interface BaseResourceDownloadable {
    int getCurrentState();

    String getDownloadUrl();

    void setState(int i);
}
