package com.xiaomi.idm.service;

import com.xiaomi.idm.api.IDMClientApi;
import com.xiaomi.idm.api.IDMService;
import com.xiaomi.idm.api.proto.IDMServiceProto;
import com.xiaomi.idm.service.iot.IPCameraService;
import com.xiaomi.idm.service.iot.IotService.Stub;

public class IDMServiceFactory {
    private static final String TAG = "IDMServiceFactory";

    public static IDMService createIDMService(IDMClientApi iDMClientApi, IDMServiceProto.IDMService iDMService) {
        if (iDMService != null) {
            String type = iDMService.getType();
            char c = 65535;
            int hashCode = type.hashCode();
            if (hashCode != 104462) {
                if (hashCode == 1854871308 && type.equals(IDMService.TYPE_IPC)) {
                    c = 1;
                }
            } else if (type.equals(IDMService.TYPE_IOT)) {
                c = 0;
            }
            if (c == 0) {
                return new Stub(iDMClientApi, iDMService);
            }
            if (c == 1) {
                return new IPCameraService.Stub(iDMClientApi, iDMService);
            }
        }
        return null;
    }
}
