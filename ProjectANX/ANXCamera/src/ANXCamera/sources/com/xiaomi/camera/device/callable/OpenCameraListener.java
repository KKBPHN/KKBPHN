package com.xiaomi.camera.device.callable;

public interface OpenCameraListener {
    void onClosed(String str);

    void onDisconnected(String str);

    void onError(String str, int i);

    void onOpened(String str);
}
