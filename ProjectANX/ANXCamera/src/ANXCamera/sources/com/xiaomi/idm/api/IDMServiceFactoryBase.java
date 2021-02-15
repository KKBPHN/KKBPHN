package com.xiaomi.idm.api;

import com.xiaomi.idm.api.proto.IDMServiceProto.IDMService;
import com.xiaomi.idm.service.IDMServiceFactory;

public class IDMServiceFactoryBase {
    protected IDMServiceFactoryBase() {
    }

    public IDMService createIDMService(IDMClientApi iDMClientApi, IDMService iDMService) {
        return IDMServiceFactory.createIDMService(iDMClientApi, iDMService);
    }
}
