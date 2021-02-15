package com.android.volley.toolbox;

import android.support.annotation.GuardedBy;
import android.support.annotation.Nullable;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import java.io.UnsupportedEncodingException;

public abstract class JsonRequest extends Request {
    protected static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", new Object[]{PROTOCOL_CHARSET});
    @Nullable
    @GuardedBy("mLock")
    private Listener mListener;
    private final Object mLock;
    @Nullable
    private final String mRequestBody;

    public JsonRequest(int i, String str, @Nullable String str2, Listener listener, @Nullable ErrorListener errorListener) {
        super(i, str, errorListener);
        this.mLock = new Object();
        this.mListener = listener;
        this.mRequestBody = str2;
    }

    @Deprecated
    public JsonRequest(String str, String str2, Listener listener, ErrorListener errorListener) {
        this(-1, str, str2, listener, errorListener);
    }

    public void cancel() {
        super.cancel();
        synchronized (this.mLock) {
            this.mListener = null;
        }
    }

    /* access modifiers changed from: protected */
    public void deliverResponse(Object obj) {
        Listener listener;
        synchronized (this.mLock) {
            listener = this.mListener;
        }
        if (listener != null) {
            listener.onResponse(obj);
        }
    }

    public byte[] getBody() {
        String str = PROTOCOL_CHARSET;
        byte[] bArr = null;
        try {
            if (this.mRequestBody != null) {
                bArr = this.mRequestBody.getBytes(str);
            }
            return bArr;
        } catch (UnsupportedEncodingException unused) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", this.mRequestBody, str);
            return null;
        }
    }

    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Deprecated
    public byte[] getPostBody() {
        return getBody();
    }

    @Deprecated
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    public abstract Response parseNetworkResponse(NetworkResponse networkResponse);
}
