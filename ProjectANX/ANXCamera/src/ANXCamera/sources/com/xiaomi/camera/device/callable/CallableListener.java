package com.xiaomi.camera.device.callable;

public interface CallableListener {
    void onFailure(Exception exc);

    void onSuccess(Object obj);
}
