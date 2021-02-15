package com.android.camera.network.net.json;

import com.android.camera.network.net.base.Cacheable;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectRequest extends Request implements Cacheable {
    private String mCacheKey = null;
    private volatile byte[] mData = null;
    private Map mHeaders = null;
    private volatile boolean mIsFromCache = false;
    private Listener mListener;
    private Map mParams = null;

    public JsonObjectRequest(int i, String str, Listener listener, ErrorListener errorListener) {
        super(i, str, errorListener);
        this.mListener = listener;
    }

    public static String parseCharset(Map map, String str) {
        String str2 = (String) map.get("Content-Type");
        if (str2 != null) {
            String[] split = str2.split(";");
            for (int i = 1; i < split.length; i++) {
                String[] split2 = split[i].trim().split("=");
                if (split2.length == 2 && split2[0].equals(HttpRequest.PARAM_CHARSET)) {
                    return split2[1];
                }
            }
        }
        return str;
    }

    /* access modifiers changed from: protected */
    public void deliverResponse(JSONObject jSONObject) {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onResponse(jSONObject);
        }
    }

    public String getCacheKey() {
        String str = this.mCacheKey;
        return str == null ? super.getCacheKey() : str;
    }

    public byte[] getData() {
        return this.mData;
    }

    public Map getHeaders() {
        Map map = this.mHeaders;
        return map != null ? map : super.getHeaders();
    }

    public Map getParams() {
        Map map = this.mParams;
        return map != null ? map : super.getParams();
    }

    public boolean isFromCache() {
        return this.mIsFromCache;
    }

    /* access modifiers changed from: protected */
    public Response parseNetworkResponse(NetworkResponse networkResponse) {
        ParseError parseError;
        try {
            this.mIsFromCache = networkResponse.headers.containsKey(Cacheable.HEADER_FROM_CACHE);
            this.mData = networkResponse.data;
            return Response.success(new JSONObject(new String(networkResponse.data, parseCharset(networkResponse.headers, "utf-8"))), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            parseError = new ParseError((Throwable) e);
            return Response.error(parseError);
        } catch (JSONException e2) {
            parseError = new ParseError((Throwable) e2);
            return Response.error(parseError);
        }
    }

    public void setCacheKey(String str) {
        this.mCacheKey = str;
    }

    public void setHeaders(Map map) {
        this.mHeaders = map;
    }

    public void setParams(Map map) {
        this.mParams = map;
    }
}
