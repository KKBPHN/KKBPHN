package com.xiaomi.mi_connect_sdk.api;

public interface MiApp {
    void acceptConnection(ConnectionConfig connectionConfig);

    byte[] deviceInfoIDM();

    void disconnectFromEndPoint(ConnectionConfig connectionConfig);

    byte[] getIdHash();

    void rejectConnection(ConnectionConfig connectionConfig);

    void requestConnection(ConnectionConfig connectionConfig);

    void sendPayload(PayloadConfig payloadConfig);

    void startAdvertising(AppConfig appConfig);

    void startDiscovery(AppConfig appConfig);

    void stopAdvertising();

    void stopDiscovery();
}
