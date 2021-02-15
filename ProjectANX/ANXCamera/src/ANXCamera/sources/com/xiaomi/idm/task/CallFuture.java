package com.xiaomi.idm.task;

import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import com.xiaomi.idm.api.ResponseCode;
import com.xiaomi.mi_connect_sdk.util.LogUtil;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CallFuture implements Future {
    public static final int DEFAULT_TIME_OUT = 10000;
    private static final String TAG = "CallFuture";
    private int mExitCode = 0;
    private String mExitMsg;
    private volatile boolean mIsDone = false;
    private Object mResult = null;
    private Object mSyncer = new Object();

    public class CallException extends ExecutionException {
        int code;
        String msg;

        CallException(int i, String str) {
            super(str);
            this.code = i;
            this.msg = str;
        }

        public int getCode() {
            return this.code;
        }

        public String getMsg() {
            return this.msg;
        }
    }

    public boolean cancel(boolean z) {
        setFailed(-11, ResponseCode.getResponseMsg(-11));
        return true;
    }

    public Object get() {
        return get(0, null);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r4.mIsDone = true;
        r4.mExitCode = -10;
        r4.mExitMsg = com.xiaomi.idm.api.ResponseCode.getResponseMsg(r4.mExitCode);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0024 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Object get(long j, TimeUnit timeUnit) {
        if (j > 0) {
            j = timeUnit.toMillis(j);
        }
        synchronized (this.mSyncer) {
            if (!this.mIsDone) {
                if (j > 0) {
                    this.mSyncer.wait(j);
                } else {
                    this.mSyncer.wait(FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME);
                }
            }
        }
        if (!this.mIsDone) {
            this.mIsDone = true;
            this.mExitCode = -9;
            this.mExitMsg = ResponseCode.getResponseMsg(this.mExitCode);
        }
        int i = this.mExitCode;
        if (i == 0) {
            return this.mResult;
        }
        throw new CallException(i, this.mExitMsg);
    }

    public boolean isCancelled() {
        return true;
    }

    public boolean isDone() {
        return this.mIsDone;
    }

    public void setDone(Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("result = ");
        sb.append(obj);
        LogUtil.d(TAG, sb.toString(), new Object[0]);
        synchronized (this.mSyncer) {
            this.mResult = obj;
            this.mIsDone = true;
            this.mSyncer.notifyAll();
        }
    }

    public void setFailed(int i, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("future task failed, code: ");
        sb.append(i);
        LogUtil.e(TAG, sb.toString(), new Object[0]);
        synchronized (this.mSyncer) {
            this.mExitCode = i;
            this.mExitMsg = str;
            this.mIsDone = true;
            this.mSyncer.notifyAll();
        }
    }
}
