package com.android.camera.resource;

public interface BaseResourceUpdatable {
    void compareAndMarkDeparted(Object obj);

    boolean isDeparted();

    void setDeparted();
}
