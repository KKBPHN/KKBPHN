package com.android.camera.network.net.base;

import android.os.Looper;
import com.android.camera.network.net.HttpManager;
import com.android.camera.network.threadpool.ThreadManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import java.util.concurrent.CountDownLatch;

public abstract class VolleyRequest extends BaseRequest implements Listener, ErrorListener {
    private Request mRequest;
    private CountDownLatch mSyncExecuteLock = null;

    private void releaseSyncExecuteLock() {
        CountDownLatch countDownLatch = this.mSyncExecuteLock;
        if (countDownLatch != null) {
            countDownLatch.countDown();
            this.mSyncExecuteLock = null;
        }
    }

    public final void cancel() {
        releaseSyncExecuteLock();
        setOnResponseListener(null);
        Request request = this.mRequest;
        if (request != null) {
            request.cancel();
        }
    }

    public abstract Request createVolleyRequest(Listener listener, ErrorListener errorListener);

    public void execute() {
        this.mRequest = createVolleyRequest(this, this);
        Request request = this.mRequest;
        if (request != null) {
            request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
            this.mRequest.setShouldCache(isUseCache());
            Object tag = getTag();
            if (this.mRequest.getTag() == null && tag != null) {
                this.mRequest.setTag(tag);
            }
            HttpManager.getInstance().addToRequestQueue(this.mRequest);
        }
    }

    public Object[] executeSync() {
        if (Looper.myLooper() == Looper.getMainLooper() || Looper.myLooper() == ThreadManager.getRequestThreadLooper()) {
            throw new RuntimeException("executeSync could not call on main thread or request thread.");
        }
        this.mSyncExecuteLock = new CountDownLatch(1);
        execute();
        try {
            this.mSyncExecuteLock.await();
        } catch (InterruptedException unused) {
        }
        RequestError requestError = this.mRequestError;
        if (requestError == null) {
            return this.mResponse;
        }
        throw requestError;
    }

    /* access modifiers changed from: protected */
    public final void handleError(ErrorCode errorCode, String str, Object obj) {
        this.mCacheExpires = -1;
        onRequestError(errorCode, str, obj);
    }

    public abstract void handleResponse(Object obj);

    /* JADX WARNING: type inference failed for: r1v1, types: [java.lang.Throwable] */
    /* JADX WARNING: type inference failed for: r3v1, types: [java.lang.Throwable, java.lang.Object] */
    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onErrorResponse(VolleyError volleyError) {
        ErrorCode errorCode = ErrorCode.NET_ERROR;
        if (volleyError instanceof RequestError) {
            errorCode = ((RequestError) volleyError).getErrorCode();
        }
        ? cause = volleyError.getCause();
        if (cause != 0) {
            volleyError = cause;
        }
        handleError(errorCode, volleyError.getMessage(), volleyError);
        releaseSyncExecuteLock();
    }

    public final void onResponse(Object obj) {
        handleResponse(obj);
        releaseSyncExecuteLock();
        Request request = this.mRequest;
        if (request instanceof Cacheable) {
            Cacheable cacheable = (Cacheable) request;
            if (this.mCacheExpires > 0 && !cacheable.isFromCache()) {
                HttpManager.getInstance().putToCache(this.mRequest.getCacheKey(), cacheable.getData(), this.mCacheExpires, this.mCacheSoftTtl);
            }
        }
    }
}
