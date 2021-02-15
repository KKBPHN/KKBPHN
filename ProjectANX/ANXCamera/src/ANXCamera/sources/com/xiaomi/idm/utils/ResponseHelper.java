package com.xiaomi.idm.utils;

import com.google.protobuf.ByteString;
import com.xiaomi.idm.api.ResponseCode;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMRequest;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMResponse;

public class ResponseHelper {
    public static IDMResponse buildResponse(int i) {
        return buildResponse(i, ResponseCode.getResponseMsg(i));
    }

    public static IDMResponse buildResponse(int i, IDMRequest iDMRequest, byte[] bArr) {
        return buildResponse(i, ResponseCode.getResponseMsg(i), iDMRequest, bArr);
    }

    public static IDMResponse buildResponse(int i, String str) {
        return buildResponse(i, str, null, null);
    }

    public static IDMResponse buildResponse(int i, String str, IDMRequest iDMRequest, byte[] bArr) {
        String str2 = "";
        if (str == null) {
            str = str2;
        }
        String requestId = iDMRequest == null ? str2 : iDMRequest.getRequestId();
        String uuid = iDMRequest == null ? str2 : iDMRequest.getUuid();
        if (iDMRequest != null) {
            str2 = iDMRequest.getClientId();
        }
        if (bArr == null) {
            bArr = new byte[0];
        }
        return IDMResponse.newBuilder().setCode(i).setMsg(str).setRequestId(requestId).setUuid(uuid).setClientId(str2).setResponse(ByteString.copyFrom(bArr)).build();
    }

    public static IDMResponse buildResponse(IDMRequest iDMRequest, byte[] bArr) {
        return buildResponse(0, null, iDMRequest, bArr);
    }
}
