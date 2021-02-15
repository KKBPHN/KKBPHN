package com.xiaomi.idm.service.test;

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
import com.xiaomi.idm.service.test.localetestservice.proto.DataProto.Response;
import com.xiaomi.idm.service.test.localetestservice.proto.EventsProto.SomeEvent;
import com.xiaomi.idm.task.CallFuture;
import com.xiaomi.idm.task.CallFuture.CallException;
import com.xiaomi.idm.utils.ResponseHelper;
import com.xiaomi.mi_connect_sdk.util.LogUtil;
import java.util.UUID;

public abstract class TestLocalService extends LocalService {
    public static final String SERVICE_TYPE = "TestLocalService";
    private static final String TAG = "TestLocalService";

    public class Actions {
        public static final int AID_GETSOMESTRING = 1;

        public class GetSomeString extends Action {
            private static final String TAG = "GetSomeString";
            com.xiaomi.idm.service.test.localetestservice.proto.ActionsProto.GetSomeString action;

            public GetSomeString(TestLocalService testLocalService, String str) {
                super(1, testLocalService);
                this.action = com.xiaomi.idm.service.test.localetestservice.proto.ActionsProto.GetSomeString.newBuilder().setAid(getAid()).setParam(str).build();
            }

            public GetSomeString(TestLocalService testLocalService, byte[] bArr) {
                super(1, testLocalService);
                this.action = com.xiaomi.idm.service.test.localetestservice.proto.ActionsProto.GetSomeString.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((TestLocalService) this.service).getSomeString(this.action.getParam());
                } catch (RmiException e) {
                    LogUtil.e(TAG, e.getMessage(), (Throwable) e);
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
                com.xiaomi.idm.service.test.localetestservice.proto.ActionsProto.GetSomeString getSomeString = this.action;
                if (getSomeString == null) {
                    return null;
                }
                return getSomeString.toByteArray();
            }
        }
    }

    public class Events {
        public static final int EID_SOMEEVENT = 1;

        public class SomeEvent extends Event {
            Callback callback;

            public interface Callback {
                void onSome(int i);
            }

            SomeEvent(IDMService iDMService, Callback callback2) {
                super(iDMService, 1);
                this.callback = callback2;
            }

            /* access modifiers changed from: protected */
            public void onEvent(byte[] bArr) {
                try {
                    this.callback.onSome(com.xiaomi.idm.service.test.localetestservice.proto.EventsProto.SomeEvent.parseFrom(bArr).getParam());
                } catch (InvalidProtocolBufferException e) {
                    LogUtil.e("TestLocalService", e.getMessage(), (Throwable) e);
                }
            }
        }
    }

    public abstract class Skeleton extends TestLocalService {
        private boolean mSomeEventEnabled;

        public int enableEvent(int i, boolean z) {
            if (i != -1 && i != 1) {
                return -1;
            }
            this.mSomeEventEnabled = z;
            return 0;
        }

        /* access modifiers changed from: protected */
        public void notifySomeEventEvent(int i) {
            if (this.mSomeEventEnabled) {
                notifyEvent(1, SomeEvent.newBuilder().setParam(i).build().toByteArray());
            }
        }
    }

    public class Stub extends TestLocalService {
        private IDMClientApi mIDMClientApi;

        public Stub(IDMClientApi iDMClientApi, IDMServiceProto.IDMService iDMService) {
            super(iDMService);
            this.mIDMClientApi = iDMClientApi;
        }

        public Response getSomeString(String str) {
            try {
                return (Response) getSomeStringAsync(str).get();
            } catch (CallException e) {
                throw new RmiException(e.getCode(), e.getMsg());
            }
        }

        public CallFuture getSomeStringAsync(String str) {
            return this.mIDMClientApi.request(new GetSomeString((TestLocalService) this, str));
        }

        public void setSomeEventCallback(Callback callback) {
            this.mIDMClientApi.setEventCallback(new SomeEvent(this, callback), callback != null);
        }
    }

    public TestLocalService() {
        super(UUID.randomUUID().toString(), TestLocalService.class.getName(), "TestLocalService");
    }

    protected TestLocalService(IDMServiceProto.IDMService iDMService) {
        super(iDMService);
    }

    public abstract Response getSomeString(String str);

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0051  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public IDMResponse request(IDMRequest iDMRequest) {
        GetSomeString getSomeString;
        int aid = iDMRequest.getAid();
        byte[] byteArray = iDMRequest.getRequest().toByteArray();
        if (byteArray == null) {
            return null;
        }
        if (aid == 1) {
            try {
                getSomeString = new GetSomeString(this, byteArray);
            } catch (InvalidProtocolBufferException e) {
                LogUtil.e("TestLocalService", e.getMessage(), (Throwable) e);
            }
            if (getSomeString == null) {
                return ResponseHelper.buildResponse(iDMRequest, getSomeString.invoke());
            }
            StringBuilder sb = new StringBuilder();
            sb.append(ResponseCode.getResponseMsg(-7));
            sb.append(" for uuid: ");
            sb.append(getUUID());
            sb.append(" aid: ");
            sb.append(aid);
            return ResponseHelper.buildResponse(-7, sb.toString(), iDMRequest, null);
        }
        getSomeString = null;
        if (getSomeString == null) {
        }
    }
}
