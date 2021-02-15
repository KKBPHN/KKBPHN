package com.android.volley;

import com.android.volley.Cache.Entry;

public class Response {
    public final Entry cacheEntry;
    public final VolleyError error;
    public boolean intermediate;
    public final Object result;

    public interface ErrorListener {
        void onErrorResponse(VolleyError volleyError);
    }

    public interface Listener {
        void onResponse(Object obj);
    }

    private Response(VolleyError volleyError) {
        this.intermediate = false;
        this.result = null;
        this.cacheEntry = null;
        this.error = volleyError;
    }

    private Response(Object obj, Entry entry) {
        this.intermediate = false;
        this.result = obj;
        this.cacheEntry = entry;
        this.error = null;
    }

    public static Response error(VolleyError volleyError) {
        return new Response(volleyError);
    }

    public static Response success(Object obj, Entry entry) {
        return new Response(obj, entry);
    }

    public boolean isSuccess() {
        return this.error == null;
    }
}
