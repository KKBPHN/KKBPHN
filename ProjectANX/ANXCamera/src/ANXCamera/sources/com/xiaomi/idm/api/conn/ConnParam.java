package com.xiaomi.idm.api.conn;

import com.google.protobuf.InvalidProtocolBufferException;
import com.xiaomi.mi_connect_sdk.util.LogUtil;
import com.xiaomi.mi_connect_service.IPCLParam.ConnParam.Builder;
import com.xiaomi.mi_connect_service.IPCLParam.ConnParam.ConnType;

public class ConnParam {
    public static final int CONNTYPE_WIFI_P2P_GC = 1;
    public static final int CONNTYPE_WIFI_P2P_GO = 0;
    public static final int CONNTYPE_WIFI_SOFTAP = 2;
    public static final int CONNTYPE_WIFI_STATION = 3;
    private static final String TAG = "ConnParam";
    private Object config;
    private int connType;
    private int errCode;
    private String errMsg;

    public static ConnParam buildFromProto(com.xiaomi.mi_connect_service.IPCLParam.ConnParam connParam) {
        if (connParam == null) {
            return null;
        }
        ConnParam connParam2 = new ConnParam();
        int connTypeValue = connParam.getConnTypeValue();
        if (connTypeValue == 0 || connTypeValue == 1 || connTypeValue == 2 || connTypeValue == 3) {
            connParam2.connType = connParam.getConnTypeValue();
            connParam2.config = WifiConfig.buildFromProto(connParam.getConfig().toByteArray());
            connParam2.errCode = connParam.getErrCode();
            connParam2.errMsg = connParam.getErrMsg();
            return connParam2;
        }
        throw new IllegalArgumentException("ConnParam: Unrecognized connType");
    }

    public static ConnParam buildFromProto(byte[] bArr) {
        com.xiaomi.mi_connect_service.IPCLParam.ConnParam connParam = null;
        if (bArr == null) {
            return null;
        }
        try {
            connParam = com.xiaomi.mi_connect_service.IPCLParam.ConnParam.parseFrom(bArr);
        } catch (InvalidProtocolBufferException e) {
            LogUtil.e(TAG, e.getMessage(), (Throwable) e);
        }
        return buildFromProto(connParam);
    }

    public Object getConfig() {
        return this.config;
    }

    public int getConnType() {
        return this.connType;
    }

    public int getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setConfig(Object obj) {
        this.config = obj;
    }

    public void setConnType(int i) {
        this.connType = i;
    }

    public com.xiaomi.mi_connect_service.IPCLParam.ConnParam toProto() {
        ConnType connType2;
        Builder newBuilder = com.xiaomi.mi_connect_service.IPCLParam.ConnParam.newBuilder();
        int i = this.connType;
        if (i == 0) {
            connType2 = ConnType.WIFI_P2P_GO;
        } else if (i == 1) {
            connType2 = ConnType.WIFI_P2P_GC;
        } else if (i == 2) {
            connType2 = ConnType.WIFI_SOFTAP;
        } else if (i == 3) {
            connType2 = ConnType.WIFI_STATION;
        } else {
            throw new IllegalArgumentException("ConnParam: Unrecognized connType");
        }
        newBuilder.setConnType(connType2);
        WifiConfig wifiConfig = (WifiConfig) this.config;
        if (wifiConfig != null) {
            newBuilder.setConfig(wifiConfig.toProto().toByteString());
        }
        newBuilder.setErrCode(this.errCode);
        String str = this.errMsg;
        if (str != null) {
            newBuilder.setErrMsg(str);
        }
        return newBuilder.build();
    }
}
