package com.xiaomi.mi_connect_sdk.api;

public class ConnectionConfig {
    private byte[] commData;
    private int endPointId;
    private boolean endPointTrusted;
    private int roleType;

    public byte[] getCommData() {
        return this.commData;
    }

    public int getEndPointId() {
        return this.endPointId;
    }

    public int getRoleType() {
        return this.roleType;
    }

    public boolean isEndPointTrusted() {
        return this.endPointTrusted;
    }

    public void setCommData(byte[] bArr) {
        this.commData = bArr;
    }

    public void setEndPointId(int i) {
        this.endPointId = i;
    }

    public void setEndPointTrusted(boolean z) {
        this.endPointTrusted = z;
    }

    public void setRoleType(int i) {
        this.roleType = i;
    }
}
