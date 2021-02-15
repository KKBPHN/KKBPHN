package com.xiaomi.idm.api;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMRequest;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMResponse;
import com.xiaomi.mi_connect_sdk.util.LogUtil;

public abstract class IDMService implements IIDMServiceCallback {
    private static final String TAG = "IDMService";
    public static final String TYPE_IOT = "iot";
    public static final String TYPE_IPC = "ipcamera";
    protected IDMEventCallback mEventCallback;
    protected boolean mEventEnable;
    private com.xiaomi.idm.api.proto.IDMServiceProto.IDMService mService;
    int version;

    public abstract class Action {
        protected int aid;
        protected IDMService service;

        public Action(int i, IDMService iDMService) {
            this.aid = i;
            this.service = iDMService;
        }

        public int getAid() {
            return this.aid;
        }

        public String getServiceUUID() {
            return this.service.getUUID();
        }

        public abstract byte[] invoke();

        public abstract Object parseResponse(byte[] bArr);

        public abstract byte[] toBytes();
    }

    public abstract class Event {
        private int mEid;
        private IDMService mService;

        protected Event(IDMService iDMService, int i) {
            this.mService = iDMService;
            this.mEid = i;
        }

        public int getEid() {
            return this.mEid;
        }

        public String getUUID() {
            return this.mService.getUUID();
        }

        public abstract void onEvent(byte[] bArr);
    }

    public interface IDMEventCallback {
        void onEvent(IDMService iDMService, IDMEvent iDMEvent);
    }

    public abstract class LocalService extends IDMService {
        protected LocalService() {
        }

        protected LocalService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService iDMService) {
            super(iDMService);
        }

        protected LocalService(String str, String str2, String str3) {
            super(str, str2, str3);
        }
    }

    protected IDMService() {
        this.mService = com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.newBuilder().build();
    }

    protected IDMService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService iDMService) {
        this.mService = iDMService;
    }

    protected IDMService(String str, String str2, String str3) {
        this.mService = com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.newBuilder().setUuid(str).setName(str2).setType(str3).build();
    }

    public int enableEvent(int i, boolean z) {
        return -1;
    }

    public com.xiaomi.idm.api.proto.IDMServiceProto.IDMService getIDMServiceProto() {
        return this.mService;
    }

    public String getName() {
        return this.mService.getName();
    }

    public String getType() {
        return this.mService.getType();
    }

    public String getUUID() {
        return this.mService.getUuid();
    }

    public boolean isEventEnabled() {
        return this.mEventEnable;
    }

    /* access modifiers changed from: protected */
    public void notifyEvent(int i, byte[] bArr) {
        if (this.mEventCallback != null && this.mEventEnable) {
            this.mEventCallback.onEvent(this, IDMEvent.newBuilder().setEid(i).setUuid(getUUID()).setEvent(ByteString.copyFrom(bArr)).build());
        }
    }

    public byte[] onRequest(byte[] bArr) {
        IDMRequest iDMRequest;
        String str = TAG;
        if (bArr == null) {
            return null;
        }
        try {
            iDMRequest = IDMRequest.parseFrom(bArr);
        } catch (InvalidProtocolBufferException e) {
            LogUtil.e(str, e.getMessage(), (Throwable) e);
            iDMRequest = null;
        }
        if (iDMRequest == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("doRequest request: ");
        sb.append(iDMRequest);
        LogUtil.d(str, sb.toString(), new Object[0]);
        IDMResponse request = getUUID().equals(iDMRequest.getUuid()) ? request(iDMRequest) : null;
        if (request != null) {
            return request.toByteArray();
        }
        LogUtil.e(str, "doRequest response is null", new Object[0]);
        return null;
    }

    public abstract IDMResponse request(IDMRequest iDMRequest);

    public void setEventCallback(IDMEventCallback iDMEventCallback) {
        this.mEventCallback = iDMEventCallback;
    }

    public byte[] toByteArray() {
        com.xiaomi.idm.api.proto.IDMServiceProto.IDMService iDMService = this.mService;
        if (iDMService == null) {
            return null;
        }
        return iDMService.toByteArray();
    }
}
