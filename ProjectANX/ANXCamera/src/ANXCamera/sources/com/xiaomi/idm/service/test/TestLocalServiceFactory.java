package com.xiaomi.idm.service.test;

import com.xiaomi.idm.api.IDMClientApi;
import com.xiaomi.idm.api.IDMService;
import com.xiaomi.idm.api.IDMServiceFactoryBase;
import com.xiaomi.idm.api.proto.IDMServiceProto;
import com.xiaomi.idm.service.test.TestLocalService.Stub;

public class TestLocalServiceFactory extends IDMServiceFactoryBase {
    private static final String TAG = "TestLocalServiceFactory";

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0026  */
    /* JADX WARNING: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public IDMService createIDMService(IDMClientApi iDMClientApi, IDMServiceProto.IDMService iDMService) {
        Stub stub;
        if (iDMService != null) {
            String type = iDMService.getType();
            char c = 65535;
            if (type.hashCode() == 740618908 && type.equals(TestLocalService.SERVICE_TYPE)) {
                c = 0;
            }
            if (c == 0) {
                stub = new Stub(iDMClientApi, iDMService);
                return stub != null ? super.createIDMService(iDMClientApi, iDMService) : stub;
            }
        }
        stub = null;
        if (stub != null) {
        }
    }
}
