package com.xiaomi.mi_connect_sdk.api;

public class PayloadConfig {
    private int endPointId;
    private byte[] payload;
    private int roleType;

    public int getEndPointId() {
        return this.endPointId;
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public int getRoleType() {
        return this.roleType;
    }

    public void setEndPointId(int i) {
        this.endPointId = i;
    }

    public void setPayload(byte[] bArr) {
        this.payload = bArr;
    }

    public void setRoleType(int i) {
        this.roleType = i;
    }
}
