package com.xiaomi.idm.api;

import android.content.Context;
import android.os.RemoteException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiaomi.idm.api.IDMService.IDMEventCallback;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMRequest;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMResponse;
import com.xiaomi.idm.utils.ResponseHelper;
import com.xiaomi.mi_connect_sdk.util.LogUtil;
import com.xiaomi.mi_connect_service.IIDMServiceProcCallback;
import com.xiaomi.mi_connect_service.IIDMServiceProcCallback.Stub;
import com.xiaomi.mi_connect_service.IPCLParam.Event;
import com.xiaomi.mi_connect_service.IPCLParam.OnRequest;
import com.xiaomi.mi_connect_service.IPCLParam.OnSetEventCallback;
import com.xiaomi.mi_connect_service.IPCLParam.Response;
import com.xiaomi.mi_connect_service.IPCLParam.StartAdvertisingIDM;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IDMServiceApi extends IDMApi {
    private static final String TAG = "IDMServiceApi";
    IDMEventCallback mEventCallback = new IDMEventCallback() {
        public void onEvent(IDMService iDMService, IDMEvent iDMEvent) {
            StringBuilder sb = new StringBuilder();
            sb.append("onEvent event = ");
            sb.append(iDMEvent);
            String sb2 = sb.toString();
            Object[] objArr = new Object[0];
            String str = IDMServiceApi.TAG;
            LogUtil.d(str, sb2, objArr);
            if (IDMServiceApi.this.serviceAvailable()) {
                try {
                    IDMServiceApi.this.mService.event(IDMServiceApi.this.mInstanceId, Event.newBuilder().setIdmService(iDMService.getIDMServiceProto()).setIdmEvent(iDMEvent).build().toByteArray());
                } catch (RemoteException e) {
                    LogUtil.e(str, e.getMessage(), (Throwable) e);
                }
            } else {
                LogUtil.e(str, "onEvent, but service unavailable", new Object[0]);
            }
        }
    };
    private IIDMServiceProcCallback mProcCallback = new Stub() {
        public void onRequest(byte[] bArr) {
            OnRequest onRequest;
            IDMResponse iDMResponse;
            Object[] objArr;
            String str;
            Object[] objArr2 = new Object[0];
            String str2 = IDMServiceApi.TAG;
            LogUtil.d(str2, "onRequest", objArr2);
            if (bArr == null) {
                objArr = new Object[0];
                str = "onRequest called but param is null. Ignore request.";
            } else {
                IDMService iDMService = null;
                try {
                    onRequest = OnRequest.parseFrom(bArr);
                } catch (InvalidProtocolBufferException e) {
                    LogUtil.e(str2, e.getMessage(), (Throwable) e);
                    onRequest = null;
                }
                if (onRequest == null) {
                    objArr = new Object[0];
                    str = "onRequest onRequestParam is null";
                } else {
                    IDMRequest idmRequest = onRequest.getIdmRequest();
                    if (idmRequest == null) {
                        objArr = new Object[0];
                        str = "onRequest called but parse failed. Ignore request.";
                    } else {
                        Iterator it = IDMServiceApi.this.mServices.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            IDMService iDMService2 = (IDMService) it.next();
                            if (iDMService2.getUUID().equals(idmRequest.getUuid())) {
                                iDMService = iDMService2;
                                break;
                            }
                        }
                        if (iDMService != null) {
                            iDMResponse = iDMService.request(idmRequest);
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("onRequest service not found: ");
                            sb.append(idmRequest.getUuid());
                            LogUtil.e(str2, sb.toString(), new Object[0]);
                            iDMResponse = ResponseHelper.buildResponse(-6);
                        }
                        if (iDMResponse == null) {
                            LogUtil.e(str2, "onRequest response null", new Object[0]);
                            iDMResponse = ResponseHelper.buildResponse(-3);
                        }
                        if (IDMServiceApi.this.serviceAvailable()) {
                            try {
                                IDMServiceApi.this.mService.response(IDMServiceApi.this.mInstanceId, Response.newBuilder().setIdmResponse(iDMResponse).build().toByteArray());
                            } catch (RemoteException e2) {
                                LogUtil.e(str2, e2.getMessage(), (Throwable) e2);
                            }
                        } else {
                            LogUtil.e(str2, "onRequest, service unavailable", new Object[0]);
                        }
                        return;
                    }
                }
            }
            LogUtil.e(str2, str, objArr);
        }

        public int onSetEventCallback(byte[] bArr) {
            OnSetEventCallback onSetEventCallback;
            IDMService iDMService;
            Object[] objArr;
            String str;
            String str2 = IDMServiceApi.TAG;
            try {
                onSetEventCallback = OnSetEventCallback.parseFrom(bArr);
            } catch (InvalidProtocolBufferException e) {
                LogUtil.e(str2, e.getMessage(), (Throwable) e);
                onSetEventCallback = null;
            }
            int i = -1;
            if (onSetEventCallback == null) {
                objArr = new Object[0];
                str = "onSetEventCallback eventParam is null";
            } else {
                IDMEvent idmEvent = onSetEventCallback.getIdmEvent();
                if (idmEvent == null) {
                    objArr = new Object[0];
                    str = "onSetEventCallback idmEvent is null";
                } else {
                    String uuid = idmEvent.getUuid();
                    int eid = idmEvent.getEid();
                    boolean enable = idmEvent.getEnable();
                    Iterator it = IDMServiceApi.this.mServices.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            iDMService = null;
                            break;
                        }
                        iDMService = (IDMService) it.next();
                        if (iDMService.getUUID().equals(uuid)) {
                            break;
                        }
                    }
                    if (iDMService != null) {
                        i = iDMService.enableEvent(eid, enable);
                        if (iDMService.isEventEnabled()) {
                            iDMService.setEventCallback(IDMServiceApi.this.mEventCallback);
                        } else {
                            iDMService.setEventCallback(null);
                        }
                    }
                    return i;
                }
            }
            LogUtil.e(str2, str, objArr);
            return -1;
        }
    };
    /* access modifiers changed from: private */
    public List mServices = new ArrayList();

    public IDMServiceApi(Context context, IDMProcessCallback iDMProcessCallback) {
        super(context, iDMProcessCallback);
    }

    /* access modifiers changed from: protected */
    public void doDestroy() {
        super.doDestroy();
        if (serviceAvailable()) {
            try {
                this.mService.unregisterProc(this.mInstanceId);
            } catch (RemoteException e) {
                LogUtil.e(TAG, e.getMessage(), (Throwable) e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onServiceConnected() {
        super.onServiceConnected();
        try {
            this.mService.registerProc(this.mInstanceId, null, this.mProcCallback);
        } catch (RemoteException e) {
            LogUtil.e(TAG, e.getMessage(), (Throwable) e);
        }
    }

    public int registerService(IDMService iDMService) {
        if (serviceAvailable()) {
            try {
                int startAdvertisingIDM = this.mService.startAdvertisingIDM(this.mInstanceId, StartAdvertisingIDM.newBuilder().setIdmService(iDMService.getIDMServiceProto()).build().toByteArray());
                if (startAdvertisingIDM == 0) {
                    this.mServices.add(iDMService);
                }
                return startAdvertisingIDM;
            } catch (RemoteException e) {
                LogUtil.e(TAG, e.getMessage(), (Throwable) e);
            }
        }
        return -1;
    }
}
