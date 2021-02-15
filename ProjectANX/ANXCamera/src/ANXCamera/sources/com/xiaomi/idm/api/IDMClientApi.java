package com.xiaomi.idm.api;

import android.content.Context;
import android.os.RemoteException;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiaomi.idm.api.IDMService.Action;
import com.xiaomi.idm.api.IDMService.Event;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMRequest;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMResponse;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMService;
import com.xiaomi.idm.task.CallFuture;
import com.xiaomi.mi_connect_sdk.util.LogUtil;
import com.xiaomi.mi_connect_service.IIDMClientCallback;
import com.xiaomi.mi_connect_service.IIDMClientCallback.Stub;
import com.xiaomi.mi_connect_service.IPCLParam.ConnectService;
import com.xiaomi.mi_connect_service.IPCLParam.IdentifyParam;
import com.xiaomi.mi_connect_service.IPCLParam.OnEvent;
import com.xiaomi.mi_connect_service.IPCLParam.OnResponse;
import com.xiaomi.mi_connect_service.IPCLParam.OnServiceConnectStatus;
import com.xiaomi.mi_connect_service.IPCLParam.OnServiceFound;
import com.xiaomi.mi_connect_service.IPCLParam.Request;
import com.xiaomi.mi_connect_service.IPCLParam.SetEventCallback;
import com.xiaomi.mi_connect_service.IPCLParam.StartDiscovery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class IDMClientApi extends IDMApi {
    private static final String TAG = "IDMClientApi";
    /* access modifiers changed from: private */
    public ConcurrentHashMap mCalls;
    /* access modifiers changed from: private */
    public ConcurrentHashMap mEvents;
    /* access modifiers changed from: private */
    public IDMClientCallback mIDMCallback;
    private IIDMClientCallback mInnerCallback;
    private int mNextRequestId;
    private String mRemoteClientId;
    /* access modifiers changed from: private */
    public IDMServiceFactoryBase mServiceFactory;

    public class Call {
        Action action;
        CallFuture future = new CallFuture();
        IDMRequest request;

        Call(Action action2, IDMRequest iDMRequest) {
            this.action = action2;
            this.request = iDMRequest;
        }
    }

    public interface IDMClientCallback {
        void onServiceFound(IDMService iDMService);
    }

    public class ServiceFilter {
        /* access modifiers changed from: private */
        public List types = new ArrayList();
        /* access modifiers changed from: private */
        public List uuids = new ArrayList();

        public ServiceFilter addType(String str) {
            this.types.add(str);
            return this;
        }

        public ServiceFilter addUUID(String str) {
            this.uuids.add(str);
            return this;
        }
    }

    public IDMClientApi(Context context, IDMProcessCallback iDMProcessCallback) {
        this(context, new IDMServiceFactoryBase(), iDMProcessCallback);
    }

    public IDMClientApi(Context context, IDMServiceFactoryBase iDMServiceFactoryBase, IDMProcessCallback iDMProcessCallback) {
        super(context, iDMProcessCallback);
        this.mInnerCallback = new Stub() {
            public void onEvent(byte[] bArr) {
                OnEvent onEvent;
                String str = IDMClientApi.TAG;
                try {
                    onEvent = OnEvent.parseFrom(bArr);
                } catch (InvalidProtocolBufferException e) {
                    LogUtil.e(str, e.getMessage(), (Throwable) e);
                    onEvent = null;
                }
                if (onEvent == null) {
                    LogUtil.e(str, "onEvent eventParam is null", new Object[0]);
                    return;
                }
                IDMEvent idmEvent = onEvent.getIdmEvent();
                if (idmEvent != null) {
                    int eid = idmEvent.getEid();
                    ((Event) IDMClientApi.this.mEvents.get(IDMClientApi.this.generateEventKey(idmEvent.getUuid(), eid))).onEvent(idmEvent.getEvent().toByteArray());
                }
            }

            public void onResponse(byte[] bArr) {
                Call call;
                String str;
                int i;
                CallFuture callFuture;
                LogUtil.e(IDMClientApi.TAG, "onResponse", new Object[0]);
                if (bArr == null) {
                    LogUtil.e(IDMClientApi.TAG, "onResponse param is null", new Object[0]);
                    return;
                }
                OnResponse onResponse = null;
                try {
                    onResponse = OnResponse.parseFrom(bArr);
                } catch (InvalidProtocolBufferException e) {
                    LogUtil.e(IDMClientApi.TAG, e.getMessage(), (Throwable) e);
                }
                if (onResponse != null) {
                    IDMResponse idmResponse = onResponse.getIdmResponse();
                    if (idmResponse != null) {
                        String requestId = idmResponse.getRequestId();
                        synchronized (IDMClientApi.this.mCalls) {
                            call = (Call) IDMClientApi.this.mCalls.get(requestId);
                        }
                        if (call != null) {
                            if (idmResponse.getCode() == 0) {
                                try {
                                    call.future.setDone(call.action.parseResponse(idmResponse.getResponse().toByteArray()));
                                } catch (RmiException e2) {
                                    LogUtil.e(IDMClientApi.TAG, e2.getMessage(), (Throwable) e2);
                                    callFuture = call.future;
                                    i = e2.getResponseCode();
                                    str = e2.getMessage();
                                }
                            } else {
                                callFuture = call.future;
                                i = idmResponse.getCode();
                                str = idmResponse.getMsg();
                                callFuture.setFailed(i, str);
                            }
                        }
                    }
                } else {
                    LogUtil.e(IDMClientApi.TAG, "onResponse responseParam is null", new Object[0]);
                }
                IDMClientApi.this.clearCallCache();
            }

            public void onServiceConnectStatus(byte[] bArr) {
                OnServiceConnectStatus onServiceConnectStatus;
                try {
                    onServiceConnectStatus = OnServiceConnectStatus.parseFrom(bArr);
                } catch (InvalidProtocolBufferException e) {
                    LogUtil.e(IDMClientApi.TAG, e.getMessage(), (Throwable) e);
                    onServiceConnectStatus = null;
                }
                boolean connected = onServiceConnectStatus.getConnected();
                IDMService idmService = onServiceConnectStatus.getIdmService();
                if (IDMClientApi.this.miAppCallback != null) {
                    ((IDMProcessCallback) IDMClientApi.this.miAppCallback).onServiceConnectStatus(connected, idmService.getUuid());
                }
            }

            public void onServiceFound(byte[] bArr) {
                Object[] objArr = new Object[0];
                String str = IDMClientApi.TAG;
                LogUtil.d(str, "onServiceFound", objArr);
                if (IDMClientApi.this.mIDMCallback != null && IDMClientApi.this.mServiceFactory != null) {
                    OnServiceFound onServiceFound = null;
                    try {
                        onServiceFound = OnServiceFound.parseFrom(bArr);
                    } catch (InvalidProtocolBufferException e) {
                        LogUtil.e(str, e.getMessage(), (Throwable) e);
                    }
                    if (onServiceFound != null) {
                        IDMService createIDMService = IDMClientApi.this.mServiceFactory.createIDMService(IDMClientApi.this, onServiceFound.getIdmService());
                        if (createIDMService != null) {
                            IDMClientApi.this.mIDMCallback.onServiceFound(createIDMService);
                        }
                    }
                }
            }
        };
        this.mServiceFactory = iDMServiceFactoryBase;
        this.mNextRequestId = 0;
        this.mCalls = new ConcurrentHashMap();
        this.mEvents = new ConcurrentHashMap();
    }

    /* access modifiers changed from: private */
    public void clearCallCache() {
        synchronized (this.mCalls) {
            for (Entry entry : this.mCalls.entrySet()) {
                if (((Call) entry.getValue()).future.isDone()) {
                    this.mCalls.remove(entry.getKey());
                }
            }
        }
    }

    private byte[] doRequest(IDMRequest iDMRequest) {
        if (serviceAvailable()) {
            try {
                return this.mService.request(this.mInstanceId, Request.newBuilder().setIdmRequest(iDMRequest).build().toByteArray());
            } catch (RemoteException e) {
                LogUtil.e(TAG, e.getMessage(), (Throwable) e);
            }
        }
        return null;
    }

    private String getNextRequestId() {
        String valueOf;
        synchronized (IDMApi.class) {
            int i = this.mNextRequestId;
            this.mNextRequestId = i + 1;
            valueOf = String.valueOf(i);
        }
        return valueOf;
    }

    public void connectService(IDMService iDMService) {
        if (serviceAvailable()) {
            try {
                this.mService.connectService(this.mInstanceId, ConnectService.newBuilder().setIdmService(iDMService.getIDMServiceProto()).build().toByteArray());
            } catch (RemoteException e) {
                LogUtil.e(TAG, e.getMessage(), (Throwable) e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doDestroy() {
        super.doDestroy();
        boolean serviceAvailable = serviceAvailable();
        String str = TAG;
        if (serviceAvailable) {
            try {
                this.mService.unregisterIDMClient(this.mInstanceId);
            } catch (RemoteException e) {
                LogUtil.e(str, e.toString(), (Throwable) e);
            }
        } else {
            LogUtil.e(str, "destroy called, but service unavailable", new Object[0]);
        }
    }

    public String generateEventKey(String str, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("serviceId:");
        sb.append(str);
        sb.append("eid:");
        sb.append(i);
        return sb.toString();
    }

    public int registerIDM(IDMClientCallback iDMClientCallback, IdentifyParam identifyParam) {
        if (serviceAvailable()) {
            try {
                this.mIDMCallback = iDMClientCallback;
                byte[] bArr = null;
                if (identifyParam != null) {
                    bArr = identifyParam.toByteArray();
                }
                this.mRemoteClientId = this.mService.registerIDMClient(this.mInstanceId, bArr, this.mInnerCallback);
                return 0;
            } catch (RemoteException e) {
                LogUtil.e(TAG, e.toString(), (Throwable) e);
            }
        }
        return -1;
    }

    public CallFuture request(Action action) {
        int i;
        StringBuilder sb = new StringBuilder();
        sb.append("request action: ");
        sb.append(action.getAid());
        sb.append(" ");
        sb.append(action.getClass());
        LogUtil.d(TAG, sb.toString(), new Object[0]);
        IDMRequest build = IDMRequest.newBuilder().setUuid(action.getServiceUUID()).setAid(action.getAid()).setRequestId(getNextRequestId()).setClientId(this.mRemoteClientId).setRequest(ByteString.copyFrom(action.toBytes())).build();
        String requestId = build.getRequestId();
        Call call = new Call(action, build);
        synchronized (this.mCalls) {
            this.mCalls.put(requestId, call);
        }
        byte[] doRequest = doRequest(build);
        if (doRequest == null) {
            LogUtil.e(TAG, ResponseCode.ERR_RESPONSE_NULL_MSG, new Object[0]);
            i = -3;
        } else {
            IDMResponse iDMResponse = null;
            try {
                iDMResponse = IDMResponse.parseFrom(doRequest);
            } catch (InvalidProtocolBufferException e) {
                LogUtil.e(TAG, e.getMessage(), (Throwable) e);
            }
            if (iDMResponse == null) {
                LogUtil.e(TAG, ResponseCode.ERR_RESPONSE_PARSE_MSG, new Object[0]);
                i = -4;
            } else {
                i = iDMResponse.getCode();
            }
        }
        if (i < 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Error when do request responseCode = ");
            sb2.append(i);
            LogUtil.e(TAG, sb2.toString(), new Object[0]);
            call.future.setFailed(i, ResponseCode.getResponseMsg(i));
        }
        clearCallCache();
        return call.future;
    }

    public void setEventCallback(Event event, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("setEventCallback event = ");
        sb.append(event);
        sb.append(" ");
        sb.append(z);
        String sb2 = sb.toString();
        Object[] objArr = new Object[0];
        String str = TAG;
        LogUtil.d(str, sb2, objArr);
        String uuid = event.getUUID();
        int eid = event.getEid();
        String generateEventKey = generateEventKey(uuid, eid);
        if (z) {
            this.mEvents.put(generateEventKey, event);
        }
        if (serviceAvailable()) {
            IDMEvent build = IDMEvent.newBuilder().setUuid(uuid).setEid(eid).setEnable(z).build();
            int i = -1;
            try {
                i = this.mService.setEventCallback(this.mInstanceId, SetEventCallback.newBuilder().setIdmEvent(build).build().toByteArray());
            } catch (RemoteException e) {
                LogUtil.e(str, e.getMessage(), (Throwable) e);
            }
            if (i != 0 || !z) {
                this.mEvents.remove(generateEventKey);
            }
        }
    }

    public void startDiscoveryServices(ServiceFilter serviceFilter) {
        if (serviceAvailable()) {
            if (serviceFilter == null) {
                try {
                    this.mService.startDiscoveryIDM(this.mInstanceId, null);
                } catch (RemoteException e) {
                    LogUtil.e(TAG, e.toString(), (Throwable) e);
                }
            } else {
                this.mService.startDiscoveryIDM(this.mInstanceId, StartDiscovery.newBuilder().addAllServiceTypes(serviceFilter.types).addAllServiceUuids(serviceFilter.uuids).build().toByteArray());
            }
        }
    }
}
