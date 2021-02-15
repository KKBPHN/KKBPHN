package com.android.camera.module.loader.camera2;

import android.hardware.camera2.CameraManager;
import android.util.SparseArray;
import com.android.camera2.CameraCapabilities;
import java.util.concurrent.ConcurrentHashMap;

public interface Camera2Compat {
    int getAuxCameraId();

    int getAuxFrontCameraId();

    int getBokehCameraId();

    int getBokehFrontCameraId();

    SparseArray getCapabilities();

    CameraCapabilities getCapabilities(int i);

    CameraCapabilities getCurrentCameraCapabilities();

    ConcurrentHashMap getDefaultDualVideoCameraIds();

    int getFrontCameraId();

    int getMainBackCameraId();

    int getMaxJpegSize();

    int getParallelVirtualCameraId();

    int getRoleIdByActualId(int i);

    int getSATCameraId();

    int getSATFrontCameraId();

    int getStandaloneMacroCameraId();

    int getUltraTeleCameraId();

    int getUltraWideBokehCameraId();

    int getUltraWideCameraId();

    int getVideoSATCameraId();

    int getVirtualBackCameraId();

    int getVirtualFrontCameraId();

    boolean hasBokehCamera();

    boolean hasPortraitCamera();

    boolean hasSATCamera();

    boolean hasTeleCamera();

    void init(CameraManager cameraManager);

    boolean isInitialized();

    boolean isPartSAT();

    void reset();

    void setCurrentOpenedCameraId(int i);
}
