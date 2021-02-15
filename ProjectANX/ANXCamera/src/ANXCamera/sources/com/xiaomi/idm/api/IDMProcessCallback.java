package com.xiaomi.idm.api;

import com.xiaomi.mi_connect_sdk.api.MiAppCallback;

public abstract class IDMProcessCallback implements MiAppCallback {
    public final void onAdvertingResult(int i, int i2) {
    }

    public final void onConnectionInitiated(int i, int i2, String str, byte[] bArr, byte[] bArr2) {
    }

    public final void onConnectionResult(int i, int i2, String str, int i3) {
    }

    public final void onDisconnection(int i, int i2) {
    }

    public final void onDiscoveryResult(int i, int i2) {
    }

    public final void onEndpointFound(int i, int i2, String str, byte[] bArr) {
    }

    public final void onEndpointLost(int i, int i2, String str) {
    }

    public final void onPayloadReceived(int i, int i2, byte[] bArr) {
    }

    public final void onPayloadSentResult(int i, int i2, int i3) {
    }

    public abstract void onProcessConnected();

    public abstract void onProcessConnectionError();

    public abstract void onProcessDisconnected();

    public final void onServiceBind() {
        onProcessConnected();
    }

    public void onServiceConnectStatus(boolean z, String str) {
    }

    public final void onServiceError(int i) {
        onProcessConnectionError();
    }

    public final void onServiceUnbind() {
        onProcessDisconnected();
    }
}
