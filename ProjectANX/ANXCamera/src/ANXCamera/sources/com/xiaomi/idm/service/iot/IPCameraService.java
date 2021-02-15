package com.xiaomi.idm.service.iot;

import com.google.protobuf.InvalidProtocolBufferException;
import com.xiaomi.idm.api.IDMClientApi;
import com.xiaomi.idm.api.IDMService;
import com.xiaomi.idm.api.IDMService.Action;
import com.xiaomi.idm.api.IDMService.Event;
import com.xiaomi.idm.api.IDMService.LocalService;
import com.xiaomi.idm.api.ResponseCode;
import com.xiaomi.idm.api.RmiException;
import com.xiaomi.idm.api.proto.IDMServiceProto;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMRequest;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMResponse;
import com.xiaomi.idm.service.ipcamera.proto.DataProto.Response;
import com.xiaomi.idm.task.CallFuture;
import com.xiaomi.idm.utils.ResponseHelper;
import com.xiaomi.mi_connect_sdk.util.LogUtil;

public abstract class IPCameraService extends LocalService {
    private static final String TAG = "IPCameraService";

    public class Actions {
        public static final int AID_GETIPCSKELETON = 1;

        public class GetIpcSkeleton extends Action {
            private static final String TAG = "GetIpcSkeleton";
            com.xiaomi.idm.service.ipcamera.proto.ActionsProto.GetIpcSkeleton action;

            GetIpcSkeleton(IPCameraService iPCameraService, String str, String str2) {
                super(1, iPCameraService);
                this.action = com.xiaomi.idm.service.ipcamera.proto.ActionsProto.GetIpcSkeleton.newBuilder().setAid(getAid()).setServiceToken(str).setAppId(str2).build();
            }

            GetIpcSkeleton(IPCameraService iPCameraService, byte[] bArr) {
                super(1, iPCameraService);
                this.action = com.xiaomi.idm.service.ipcamera.proto.ActionsProto.GetIpcSkeleton.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((IPCameraService) this.service).getIpcSkeletonInfo(this.action.getAppId(), this.action.getServiceToken());
                } catch (RmiException e) {
                    e.printStackTrace();
                    response = null;
                }
                if (response == null) {
                    return null;
                }
                return response.toByteArray();
            }

            public Response parseResponse(byte[] bArr) {
                try {
                    return Response.parseFrom(bArr);
                } catch (InvalidProtocolBufferException e) {
                    LogUtil.e(TAG, e.getMessage(), (Throwable) e);
                    throw new RmiException(-8);
                }
            }

            public byte[] toBytes() {
                com.xiaomi.idm.service.ipcamera.proto.ActionsProto.GetIpcSkeleton getIpcSkeleton = this.action;
                if (getIpcSkeleton == null) {
                    return null;
                }
                return getIpcSkeleton.toByteArray();
            }
        }
    }

    public class Events {
        public static final int EID_SKELETONEVENT = 1;

        public class SkeletonEvent extends Event {
            Callback callback;

            public interface Callback {
                void onSkeletonEvent(byte[] bArr);
            }

            public SkeletonEvent(IDMService iDMService, Callback callback2) {
                super(iDMService, 1);
                this.callback = callback2;
            }

            /* access modifiers changed from: protected */
            public void onEvent(byte[] bArr) {
                this.callback.onSkeletonEvent(bArr);
            }
        }
    }

    public abstract class Skeleton extends IPCameraService {
        private boolean mSkeletonEventEnabled;

        public Skeleton(String str, String str2) {
            super(str, str2);
        }

        public int enableEvent(int i, boolean z) {
            if (i != -1 && i != 1) {
                return -1;
            }
            this.mSkeletonEventEnabled = z;
            return 0;
        }

        /* access modifiers changed from: protected */
        public void notifySkeletonEvent(byte[] bArr) {
            if (this.mSkeletonEventEnabled) {
                notifyEvent(1, bArr);
            }
        }
    }

    public class Stub extends IPCameraService {
        private IDMClientApi mIDMClientApi;

        public Stub(IDMClientApi iDMClientApi, IDMServiceProto.IDMService iDMService) {
            super(iDMService);
            this.mIDMClientApi = iDMClientApi;
        }

        public Response getIpcSkeletonInfo(String str, String str2) {
            getIpcSkeletonInfoAsync(str, str2);
            return null;
        }

        public CallFuture getIpcSkeletonInfoAsync(String str, String str2) {
            StringBuilder sb = new StringBuilder();
            sb.append("serviceToken = ");
            sb.append(str2);
            LogUtil.e(IPCameraService.TAG, sb.toString(), new Object[0]);
            return this.mIDMClientApi.request(new GetIpcSkeleton(this, str2, str));
        }

        public void setSkeletonEventCallback(Callback callback) {
            this.mIDMClientApi.setEventCallback(new SkeletonEvent(this, callback), callback != null);
        }
    }

    protected IPCameraService(IDMServiceProto.IDMService iDMService) {
        super(iDMService);
    }

    public IPCameraService(String str, String str2) {
        super(str, str2, IDMService.TYPE_IPC);
    }

    public abstract Response getIpcSkeletonInfo(String str, String str2);

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0051  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public IDMResponse request(IDMRequest iDMRequest) {
        GetIpcSkeleton getIpcSkeleton;
        int aid = iDMRequest.getAid();
        byte[] byteArray = iDMRequest.getRequest().toByteArray();
        if (byteArray == null) {
            return null;
        }
        if (aid == 1) {
            try {
                getIpcSkeleton = new GetIpcSkeleton(this, byteArray);
            } catch (InvalidProtocolBufferException e) {
                LogUtil.e(TAG, e.getMessage(), (Throwable) e);
            }
            if (getIpcSkeleton == null) {
                return ResponseHelper.buildResponse(iDMRequest, getIpcSkeleton.invoke());
            }
            StringBuilder sb = new StringBuilder();
            sb.append(ResponseCode.getResponseMsg(-7));
            sb.append(" for uuid: ");
            sb.append(getUUID());
            sb.append(" aid: ");
            sb.append(aid);
            return ResponseHelper.buildResponse(-7, sb.toString(), iDMRequest, null);
        }
        getIpcSkeleton = null;
        if (getIpcSkeleton == null) {
        }
    }
}
