package com.xiaomi.idm.api;

import android.content.Context;
import android.os.RemoteException;
import com.xiaomi.idm.api.conn.ConnParam;
import com.xiaomi.idm.api.conn.WifiConfig;
import com.xiaomi.mi_connect_sdk.api.DefaultMiApp;
import com.xiaomi.mi_connect_sdk.util.LogUtil;
import com.xiaomi.mi_connect_service.IConnectionCallback.Stub;

public class IDMApi extends DefaultMiApp {
    private static int NEXT_INSTANCEID = 1;
    private static final String TAG = "IDMApi";
    protected int mInstanceId;

    public interface IDMConnectionCallback {
        void onFailure(ConnParam connParam, int i, String str);

        void onSuccess(ConnParam connParam, Object obj);
    }

    class InternalCallback extends Stub {
        private IDMConnectionCallback mIDMConnectionCallback;

        private InternalCallback(IDMConnectionCallback iDMConnectionCallback) {
            this.mIDMConnectionCallback = iDMConnectionCallback;
        }

        public void onFailure(byte[] bArr) {
            ConnParam buildFromProto = ConnParam.buildFromProto(bArr);
            if (buildFromProto != null) {
                this.mIDMConnectionCallback.onFailure(buildFromProto, buildFromProto.getErrCode(), buildFromProto.getErrMsg());
            }
        }

        public void onSuccess(byte[] bArr) {
            ConnParam buildFromProto = ConnParam.buildFromProto(bArr);
            Object config = buildFromProto == null ? null : buildFromProto.getConfig();
            if (buildFromProto != null && config != null) {
                this.mIDMConnectionCallback.onSuccess(buildFromProto, config);
            }
        }
    }

    public IDMApi(Context context, IDMProcessCallback iDMProcessCallback) {
        super(context, iDMProcessCallback, 0);
        int i = NEXT_INSTANCEID;
        NEXT_INSTANCEID = i + 1;
        this.mInstanceId = i;
    }

    public int createConnection(ConnParam connParam, IDMConnectionCallback iDMConnectionCallback) {
        if (serviceAvailable()) {
            try {
                return this.mService.createConnection(this.mInstanceId, connParam.toProto().toByteArray(), new InternalCallback(iDMConnectionCallback));
            } catch (RemoteException e) {
                LogUtil.e(TAG, e.getMessage(), (Throwable) e);
            }
        }
        return -1;
    }

    public int createWifiConfigConnectionByQCode(String str, IDMConnectionCallback iDMConnectionCallback) {
        WifiConfig buildFromQRCode = WifiConfig.buildFromQRCode(str);
        if (buildFromQRCode == null) {
            return -1;
        }
        ConnParam connParam = new ConnParam();
        connParam.setConnType(1);
        connParam.setConfig(buildFromQRCode);
        return createConnection(connParam, iDMConnectionCallback);
    }

    public void destroy() {
        destroy(2);
    }

    public int destroyConnection(ConnParam connParam) {
        if (serviceAvailable()) {
            try {
                return this.mService.destroyConnection(this.mInstanceId, connParam.toProto().toByteArray());
            } catch (RemoteException e) {
                LogUtil.e(TAG, e.getMessage(), (Throwable) e);
            }
        }
        return -1;
    }
}
