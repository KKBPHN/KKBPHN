package com.android.volley.toolbox;

import android.support.annotation.Nullable;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import org.json.JSONException;

public class JsonArrayRequest extends JsonRequest {
    public JsonArrayRequest(int i, String str, @Nullable JSONArray jSONArray, Listener listener, @Nullable ErrorListener errorListener) {
        super(i, str, jSONArray == null ? null : jSONArray.toString(), listener, errorListener);
    }

    public JsonArrayRequest(String str, Listener listener, @Nullable ErrorListener errorListener) {
        super(0, str, null, listener, errorListener);
    }

    /* access modifiers changed from: protected */
    public Response parseNetworkResponse(NetworkResponse networkResponse) {
        ParseError parseError;
        try {
            return Response.success(new JSONArray(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"))), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            parseError = new ParseError((Throwable) e);
            return Response.error(parseError);
        } catch (JSONException e2) {
            parseError = new ParseError((Throwable) e2);
            return Response.error(parseError);
        }
    }
}
