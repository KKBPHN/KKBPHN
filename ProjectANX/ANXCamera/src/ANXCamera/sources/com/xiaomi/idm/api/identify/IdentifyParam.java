package com.xiaomi.idm.api.identify;

import com.google.protobuf.InvalidProtocolBufferException;
import com.xiaomi.mi_connect_sdk.util.LogUtil;
import com.xiaomi.mi_connect_service.IPCLParam.IdentifyParam.Builder;

public class IdentifyParam {
    private static final String TAG = "IdentifyParam";
    String cUserId;
    String domain;
    String serviceToken;
    String sid;
    String ssecurity;
    String timeDiff;
    String userId;

    public static IdentifyParam buildFromProto(com.xiaomi.mi_connect_service.IPCLParam.IdentifyParam identifyParam) {
        if (identifyParam == null) {
            return null;
        }
        IdentifyParam identifyParam2 = new IdentifyParam();
        identifyParam2.serviceToken = identifyParam.getServiceToken();
        identifyParam2.timeDiff = identifyParam.getTimeDiff();
        identifyParam2.domain = identifyParam.getDomain();
        identifyParam2.sid = identifyParam.getSid();
        identifyParam2.userId = identifyParam.getUserId();
        identifyParam2.ssecurity = identifyParam.getSsecurity();
        identifyParam2.cUserId = identifyParam.getCUserId();
        return identifyParam2;
    }

    public static IdentifyParam buildFromProto(byte[] bArr) {
        com.xiaomi.mi_connect_service.IPCLParam.IdentifyParam identifyParam = null;
        if (bArr == null) {
            return null;
        }
        try {
            identifyParam = com.xiaomi.mi_connect_service.IPCLParam.IdentifyParam.parseFrom(bArr);
        } catch (InvalidProtocolBufferException e) {
            LogUtil.e(TAG, e.getMessage(), (Throwable) e);
        }
        return buildFromProto(identifyParam);
    }

    public String getDomain() {
        return this.domain;
    }

    public String getServiceToken() {
        return this.serviceToken;
    }

    public String getSid() {
        return this.sid;
    }

    public String getSsecurity() {
        return this.ssecurity;
    }

    public String getTimeDiff() {
        return this.timeDiff;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getcUserId() {
        return this.cUserId;
    }

    public void setDomain(String str) {
        this.domain = str;
    }

    public void setServiceToken(String str) {
        this.serviceToken = str;
    }

    public void setSid(String str) {
        this.sid = str;
    }

    public void setSsecurity(String str) {
        this.ssecurity = str;
    }

    public void setTimeDiff(String str) {
        this.timeDiff = str;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public void setcUserId(String str) {
        this.cUserId = str;
    }

    public com.xiaomi.mi_connect_service.IPCLParam.IdentifyParam toProto() {
        Builder newBuilder = com.xiaomi.mi_connect_service.IPCLParam.IdentifyParam.newBuilder();
        String str = this.serviceToken;
        if (str != null) {
            newBuilder.setServiceToken(str);
        }
        String str2 = this.userId;
        if (str2 != null) {
            newBuilder.setUserId(str2);
        }
        String str3 = this.sid;
        if (str3 != null) {
            newBuilder.setSid(str3);
        }
        String str4 = this.cUserId;
        if (str4 != null) {
            newBuilder.setCUserId(str4);
        }
        String str5 = this.ssecurity;
        if (str5 != null) {
            newBuilder.setSsecurity(str5);
        }
        String str6 = this.domain;
        if (str6 != null) {
            newBuilder.setDomain(str6);
        }
        String str7 = this.timeDiff;
        if (str7 != null) {
            newBuilder.setTimeDiff(str7);
        }
        return newBuilder.build();
    }
}
