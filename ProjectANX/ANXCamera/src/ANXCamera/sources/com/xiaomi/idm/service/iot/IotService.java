package com.xiaomi.idm.service.iot;

import com.google.protobuf.InvalidProtocolBufferException;
import com.xiaomi.idm.api.IDMClientApi;
import com.xiaomi.idm.api.IDMService.Action;
import com.xiaomi.idm.api.IDMService.LocalService;
import com.xiaomi.idm.api.ResponseCode;
import com.xiaomi.idm.api.RmiException;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMRequest;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMResponse;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMService;
import com.xiaomi.idm.service.iot.proto.DataProto.Response;
import com.xiaomi.idm.task.CallFuture;
import com.xiaomi.idm.task.CallFuture.CallException;
import com.xiaomi.idm.utils.ResponseHelper;
import com.xiaomi.mi_connect_sdk.util.LogUtil;
import java.util.UUID;
import java.util.concurrent.Future;

public abstract class IotService extends LocalService {
    private static final String TAG = "IotService";

    public class Actions {
        public static final int AID_EXESCENES = 10;
        public static final int AID_GETDEVICEINFORMATIONS = 4;
        public static final int AID_GETDEVICEPROPERTIES = 6;
        public static final int AID_GETDEVICES = 1;
        public static final int AID_GETHOMEFASTCOMMANDS = 5;
        public static final int AID_GETHOMES = 2;
        public static final int AID_GETSCENES = 3;
        public static final int AID_SETDEVICEPROPERTIES = 7;
        public static final int AID_SETTOKEN = 8;
        public static final int AID_STOPTOKEN = 9;

        public class ExeScenes extends Action {
            private static final String TAG = "ExeScenes";
            com.xiaomi.idm.service.iot.proto.ActionsProto.ExeScenes action;

            ExeScenes(IotService iotService, String str, String str2, String str3) {
                super(10, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.ExeScenes.newBuilder().setAid(getAid()).setServiceToken(str).setSceneId(str3).setAppId(str2).build();
            }

            ExeScenes(IotService iotService, byte[] bArr) {
                super(10, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.ExeScenes.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((IotService) this.service).exeScenes(this.action.getServiceToken(), this.action.getAppId(), this.action.getSceneId());
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
                com.xiaomi.idm.service.iot.proto.ActionsProto.ExeScenes exeScenes = this.action;
                if (exeScenes == null) {
                    return null;
                }
                return exeScenes.toByteArray();
            }
        }

        public class GetDeviceInformations extends Action {
            private static final String TAG = "GetDeviceInformations";
            com.xiaomi.idm.service.iot.proto.ActionsProto.GetDeviceInformations action;

            GetDeviceInformations(IotService iotService, String str, String str2, String str3) {
                super(4, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetDeviceInformations.newBuilder().setAid(getAid()).setServiceToken(str).setDeviceId(str3).setAppId(str2).build();
            }

            GetDeviceInformations(IotService iotService, byte[] bArr) {
                super(4, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetDeviceInformations.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((IotService) this.service).getDeviceInformations(this.action.getServiceToken(), this.action.getAppId(), this.action.getDeviceId());
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
                com.xiaomi.idm.service.iot.proto.ActionsProto.GetDeviceInformations getDeviceInformations = this.action;
                if (getDeviceInformations == null) {
                    return null;
                }
                return getDeviceInformations.toByteArray();
            }
        }

        public class GetDeviceProperties extends Action {
            private static final String TAG = "GetDeviceProperties";
            com.xiaomi.idm.service.iot.proto.ActionsProto.GetDeviceProperties action;

            GetDeviceProperties(IotService iotService, String str, String str2, String str3) {
                super(6, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetDeviceProperties.newBuilder().setAid(getAid()).setServiceToken(str).setPropertyId(str3).setAppId(str2).build();
            }

            GetDeviceProperties(IotService iotService, byte[] bArr) {
                super(6, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetDeviceProperties.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((IotService) this.service).getDeviceProperties(this.action.getServiceToken(), this.action.getAppId(), this.action.getPropertyId());
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
                com.xiaomi.idm.service.iot.proto.ActionsProto.GetDeviceProperties getDeviceProperties = this.action;
                if (getDeviceProperties == null) {
                    return null;
                }
                return getDeviceProperties.toByteArray();
            }
        }

        public class GetDevices extends Action {
            private static final String TAG = "GetDevices";
            com.xiaomi.idm.service.iot.proto.ActionsProto.GetDevices action;

            GetDevices(IotService iotService, String str, String str2, boolean z) {
                super(1, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetDevices.newBuilder().setAid(getAid()).setServiceToken(str).setIsLocal(z).setAppId(str2).build();
            }

            GetDevices(IotService iotService, byte[] bArr) {
                super(1, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetDevices.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((IotService) this.service).getDevices(this.action.getServiceToken(), this.action.getAppId(), this.action.getIsLocal());
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
                com.xiaomi.idm.service.iot.proto.ActionsProto.GetDevices getDevices = this.action;
                if (getDevices == null) {
                    return null;
                }
                return getDevices.toByteArray();
            }
        }

        public class GetHomeFastCommands extends Action {
            private static final String TAG = "GetHomeFastCommands";
            com.xiaomi.idm.service.iot.proto.ActionsProto.GetHomeFastCommands action;

            GetHomeFastCommands(IotService iotService, String str, String str2) {
                super(5, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetHomeFastCommands.newBuilder().setAid(getAid()).setServiceToken(str).setAppId(str2).build();
            }

            GetHomeFastCommands(IotService iotService, byte[] bArr) {
                super(5, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetHomeFastCommands.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((IotService) this.service).getHomeFastCommands(this.action.getServiceToken(), this.action.getAppId());
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
                com.xiaomi.idm.service.iot.proto.ActionsProto.GetHomeFastCommands getHomeFastCommands = this.action;
                if (getHomeFastCommands == null) {
                    return null;
                }
                return getHomeFastCommands.toByteArray();
            }
        }

        public class GetHomes extends Action {
            private static final String TAG = "GetHomes";
            com.xiaomi.idm.service.iot.proto.ActionsProto.GetHomes action;

            GetHomes(IotService iotService, String str, String str2) {
                super(2, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetHomes.newBuilder().setAid(getAid()).setServiceToken(str).setAppId(str2).build();
            }

            GetHomes(IotService iotService, byte[] bArr) {
                super(2, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetHomes.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((IotService) this.service).getHomes(this.action.getServiceToken(), this.action.getAppId());
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
                com.xiaomi.idm.service.iot.proto.ActionsProto.GetHomes getHomes = this.action;
                if (getHomes == null) {
                    return null;
                }
                return getHomes.toByteArray();
            }
        }

        public class GetScenes extends Action {
            private static final String TAG = "GetScenes";
            com.xiaomi.idm.service.iot.proto.ActionsProto.GetScenes action;

            GetScenes(IotService iotService, String str, String str2) {
                super(3, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetScenes.newBuilder().setAid(getAid()).setServiceToken(str).setAppId(str2).build();
            }

            GetScenes(IotService iotService, byte[] bArr) {
                super(3, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.GetScenes.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((IotService) this.service).getScenes(this.action.getServiceToken(), this.action.getAppId());
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
                com.xiaomi.idm.service.iot.proto.ActionsProto.GetScenes getScenes = this.action;
                if (getScenes == null) {
                    return null;
                }
                return getScenes.toByteArray();
            }
        }

        public class SetDeviceProperties extends Action {
            private static final String TAG = "GetDeviceProperties";
            com.xiaomi.idm.service.iot.proto.ActionsProto.SetDeviceProperties action;

            SetDeviceProperties(IotService iotService, String str, String str2, String str3, boolean z) {
                super(7, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.SetDeviceProperties.newBuilder().setAid(getAid()).setServiceToken(str).setPropertyBody(str3).setIsSort(z).setAppId(str2).build();
            }

            SetDeviceProperties(IotService iotService, byte[] bArr) {
                super(7, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.SetDeviceProperties.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((IotService) this.service).setDeviceProperties(this.action.getServiceToken(), this.action.getAppId(), this.action.getPropertyBody(), this.action.getIsSort());
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
                com.xiaomi.idm.service.iot.proto.ActionsProto.SetDeviceProperties setDeviceProperties = this.action;
                if (setDeviceProperties == null) {
                    return null;
                }
                return setDeviceProperties.toByteArray();
            }
        }

        public class SetToken extends Action {
            private static final String TAG = "SetToken";
            com.xiaomi.idm.service.iot.proto.ActionsProto.SetToken action;

            SetToken(IotService iotService, String str, String str2) {
                super(8, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.SetToken.newBuilder().setAid(getAid()).setTokenParams(str).setAppId(str2).build();
            }

            SetToken(IotService iotService, byte[] bArr) {
                super(8, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.SetToken.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((IotService) this.service).setToken(this.action.getAppId(), this.action.getTokenParams());
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
                com.xiaomi.idm.service.iot.proto.ActionsProto.SetToken setToken = this.action;
                if (setToken == null) {
                    return null;
                }
                return setToken.toByteArray();
            }
        }

        public class StopToken extends Action {
            private static final String TAG = "StopToken";
            com.xiaomi.idm.service.iot.proto.ActionsProto.StopToken action;

            StopToken(IotService iotService, String str, String str2) {
                super(8, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.StopToken.newBuilder().setAid(getAid()).setTokenParams(str).setAppId(str2).build();
            }

            StopToken(IotService iotService, byte[] bArr) {
                super(8, iotService);
                this.action = com.xiaomi.idm.service.iot.proto.ActionsProto.StopToken.parseFrom(bArr);
            }

            public byte[] invoke() {
                Response response;
                try {
                    response = ((IotService) this.service).stop(this.action.getAppId(), this.action.getTokenParams());
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
                com.xiaomi.idm.service.iot.proto.ActionsProto.StopToken stopToken = this.action;
                if (stopToken == null) {
                    return null;
                }
                return stopToken.toByteArray();
            }
        }
    }

    public abstract class Skeleton extends IotService {
        public Skeleton(String str, String str2) {
            super(str, str2);
        }
    }

    public class Stub extends IotService {
        private IDMClientApi mIDMClientApi;

        public Stub(IDMClientApi iDMClientApi, IDMService iDMService) {
            super(iDMService);
            this.mIDMClientApi = iDMClientApi;
        }

        public Response exeScenes(String str, String str2, String str3) {
            try {
                return (Response) ((CallFuture) exeScenesAsync(str2, str, str3)).get();
            } catch (CallException e) {
                throw new RmiException(e.getCode(), e.getMsg());
            }
        }

        public Future exeScenesAsync(String str, String str2, String str3) {
            return this.mIDMClientApi.request(new StopToken(this, str2, str));
        }

        public Response getDeviceInformations(String str, String str2, String str3) {
            try {
                return (Response) ((CallFuture) getDeviceInformationsAsync(str, str2, str3)).get();
            } catch (CallException e) {
                throw new RmiException(e.getCode(), e.getMsg());
            }
        }

        public Future getDeviceInformationsAsync(String str, String str2, String str3) {
            return this.mIDMClientApi.request(new GetDeviceInformations(this, str, str2, str3));
        }

        public Response getDeviceProperties(String str, String str2, String str3) {
            try {
                return (Response) ((CallFuture) getDevicePropertiesAsync(str, str2, str3)).get();
            } catch (CallException e) {
                throw new RmiException(e.getCode(), e.getMsg());
            }
        }

        public Future getDevicePropertiesAsync(String str, String str2, String str3) {
            return this.mIDMClientApi.request(new GetDeviceProperties(this, str, str2, str3));
        }

        public Response getDevices(String str, String str2, boolean z) {
            try {
                return (Response) ((CallFuture) getDevicesAsync(str, str2, z)).get();
            } catch (CallException e) {
                throw new RmiException(e.getCode(), e.getMsg());
            }
        }

        public Future getDevicesAsync(String str, String str2, boolean z) {
            return this.mIDMClientApi.request(new GetDevices(this, str, str2, z));
        }

        public Future getHomeFastCommandAsync(String str, String str2) {
            return this.mIDMClientApi.request(new GetHomeFastCommands(this, str, str2));
        }

        public Response getHomeFastCommands(String str, String str2) {
            try {
                return (Response) ((CallFuture) getHomeFastCommandAsync(str, str2)).get();
            } catch (CallException e) {
                throw new RmiException(e.getCode(), e.getMsg());
            }
        }

        public Response getHomes(String str, String str2) {
            try {
                return (Response) ((CallFuture) getHomesAsync(str, str2)).get();
            } catch (CallException e) {
                throw new RmiException(e.getCode(), e.getMsg());
            }
        }

        public Future getHomesAsync(String str, String str2) {
            return this.mIDMClientApi.request(new GetHomes(this, str, str2));
        }

        public Response getScenes(String str, String str2) {
            try {
                return (Response) ((CallFuture) getScenesAsync(str, str2)).get();
            } catch (CallException e) {
                throw new RmiException(e.getCode(), e.getMsg());
            }
        }

        public Future getScenesAsync(String str, String str2) {
            return this.mIDMClientApi.request(new GetScenes(this, str, str2));
        }

        public Response setDeviceProperties(String str, String str2, String str3, boolean z) {
            try {
                return (Response) ((CallFuture) setDevicePropertiesAsync(str, str2, str3, z)).get();
            } catch (CallException e) {
                throw new RmiException(e.getCode(), e.getMsg());
            }
        }

        public Future setDevicePropertiesAsync(String str, String str2, String str3, boolean z) {
            IDMClientApi iDMClientApi = this.mIDMClientApi;
            SetDeviceProperties setDeviceProperties = new SetDeviceProperties(this, str, str2, str3, z);
            return iDMClientApi.request(setDeviceProperties);
        }

        public Response setToken(String str, String str2) {
            try {
                return (Response) ((CallFuture) setTokenAsync(str, str2)).get();
            } catch (CallException e) {
                throw new RmiException(e.getCode(), e.getMsg());
            }
        }

        public Future setTokenAsync(String str, String str2) {
            return this.mIDMClientApi.request(new SetToken(this, str2, str));
        }

        public Response stop(String str, String str2) {
            try {
                return (Response) ((CallFuture) stopAsync(str, str2)).get();
            } catch (CallException e) {
                throw new RmiException(e.getCode(), e.getMsg());
            }
        }

        public Future stopAsync(String str, String str2) {
            return this.mIDMClientApi.request(new StopToken(this, str2, str));
        }
    }

    private IotService() {
        super(UUID.randomUUID().toString(), IotService.class.getSimpleName(), com.xiaomi.idm.api.IDMService.TYPE_IOT);
    }

    protected IotService(IDMService iDMService) {
        super(iDMService);
    }

    public IotService(String str) {
        super(UUID.randomUUID().toString(), str, com.xiaomi.idm.api.IDMService.TYPE_IOT);
    }

    public IotService(String str, String str2) {
        super(str, str2, com.xiaomi.idm.api.IDMService.TYPE_IOT);
    }

    public abstract Response exeScenes(String str, String str2, String str3);

    public abstract Response getDeviceInformations(String str, String str2, String str3);

    public abstract Response getDeviceProperties(String str, String str2, String str3);

    public abstract Response getDevices(String str, String str2, boolean z);

    public abstract Response getHomeFastCommands(String str, String str2);

    public abstract Response getHomes(String str, String str2);

    public abstract Response getScenes(String str, String str2);

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public IDMResponse request(IDMRequest iDMRequest) {
        Action action;
        int aid = iDMRequest.getAid();
        byte[] byteArray = iDMRequest.getRequest().toByteArray();
        if (byteArray == null) {
            return null;
        }
        switch (aid) {
            case 1:
                action = new GetDevices(this, byteArray);
                break;
            case 2:
                action = new GetHomes(this, byteArray);
                break;
            case 3:
                action = new GetScenes(this, byteArray);
                break;
            case 4:
                action = new GetDeviceInformations(this, byteArray);
                break;
            case 5:
                action = new GetHomeFastCommands(this, byteArray);
                break;
            case 6:
                action = new GetDeviceProperties(this, byteArray);
                break;
            case 7:
                action = new SetDeviceProperties(this, byteArray);
                break;
            case 8:
                action = new SetToken(this, byteArray);
                break;
            case 9:
                action = new StopToken(this, byteArray);
                break;
            case 10:
                try {
                    action = new ExeScenes(this, byteArray);
                    break;
                } catch (InvalidProtocolBufferException e) {
                    LogUtil.e(TAG, e.getMessage(), (Throwable) e);
                }
            default:
                action = null;
                break;
        }
        if (action != null) {
            return ResponseHelper.buildResponse(iDMRequest, action.invoke());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ResponseCode.getResponseMsg(-7));
        sb.append(" for uuid: ");
        sb.append(getUUID());
        sb.append(" aid: ");
        sb.append(aid);
        return ResponseHelper.buildResponse(-7, sb.toString(), iDMRequest, null);
    }

    public abstract Response setDeviceProperties(String str, String str2, String str3, boolean z);

    public abstract Response setToken(String str, String str2);

    public abstract Response stop(String str, String str2);
}
