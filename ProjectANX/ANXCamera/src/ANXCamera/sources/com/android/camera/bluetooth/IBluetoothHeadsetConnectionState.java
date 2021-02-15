package com.android.camera.bluetooth;

public interface IBluetoothHeadsetConnectionState {
    void onBluetoothHeadsetConnected();

    void onBluetoothHeadsetConnecting();

    void onBluetoothHeadsetDisconnected();
}
