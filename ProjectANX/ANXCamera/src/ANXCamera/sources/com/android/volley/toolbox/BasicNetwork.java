package com.android.volley.toolbox;

import android.os.SystemClock;
import com.android.volley.Cache.Entry;
import com.android.volley.Header;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class BasicNetwork implements Network {
    protected static final boolean DEBUG = VolleyLog.DEBUG;
    private static final int DEFAULT_POOL_SIZE = 4096;
    private static final int SLOW_REQUEST_THRESHOLD_MS = 3000;
    private final BaseHttpStack mBaseHttpStack;
    @Deprecated
    protected final HttpStack mHttpStack;
    protected final ByteArrayPool mPool;

    public BasicNetwork(BaseHttpStack baseHttpStack) {
        this(baseHttpStack, new ByteArrayPool(4096));
    }

    public BasicNetwork(BaseHttpStack baseHttpStack, ByteArrayPool byteArrayPool) {
        this.mBaseHttpStack = baseHttpStack;
        this.mHttpStack = baseHttpStack;
        this.mPool = byteArrayPool;
    }

    @Deprecated
    public BasicNetwork(HttpStack httpStack) {
        this(httpStack, new ByteArrayPool(4096));
    }

    @Deprecated
    public BasicNetwork(HttpStack httpStack, ByteArrayPool byteArrayPool) {
        this.mHttpStack = httpStack;
        this.mBaseHttpStack = new AdaptedHttpStack(httpStack);
        this.mPool = byteArrayPool;
    }

    private static void attemptRetryOnException(String str, Request request, VolleyError volleyError) {
        RetryPolicy retryPolicy = request.getRetryPolicy();
        int timeoutMs = request.getTimeoutMs();
        try {
            retryPolicy.retry(volleyError);
            request.addMarker(String.format("%s-retry [timeout=%s]", new Object[]{str, Integer.valueOf(timeoutMs)}));
        } catch (VolleyError e) {
            request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{str, Integer.valueOf(timeoutMs)}));
            throw e;
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.volley.Header>, for r4v0, types: [java.util.List<com.android.volley.Header>, java.util.List, java.util.Collection] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static List combineHeaders(List<Header> list, Entry entry) {
        TreeSet treeSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        if (!list.isEmpty()) {
            for (Header name : list) {
                treeSet.add(name.getName());
            }
        }
        ArrayList arrayList = new ArrayList(list);
        List list2 = entry.allResponseHeaders;
        if (list2 != null) {
            if (!list2.isEmpty()) {
                for (Header header : entry.allResponseHeaders) {
                    if (!treeSet.contains(header.getName())) {
                        arrayList.add(header);
                    }
                }
            }
        } else if (!entry.responseHeaders.isEmpty()) {
            for (Map.Entry entry2 : entry.responseHeaders.entrySet()) {
                if (!treeSet.contains(entry2.getKey())) {
                    arrayList.add(new Header((String) entry2.getKey(), (String) entry2.getValue()));
                }
            }
        }
        return arrayList;
    }

    @Deprecated
    protected static Map convertHeaders(Header[] headerArr) {
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < headerArr.length; i++) {
            treeMap.put(headerArr[i].getName(), headerArr[i].getValue());
        }
        return treeMap;
    }

    private Map getCacheHeaders(Entry entry) {
        if (entry == null) {
            return Collections.emptyMap();
        }
        HashMap hashMap = new HashMap();
        String str = entry.etag;
        if (str != null) {
            hashMap.put(HttpRequest.HEADER_IF_NONE_MATCH, str);
        }
        long j = entry.lastModified;
        if (j > 0) {
            hashMap.put("If-Modified-Since", HttpHeaderParser.formatEpochAsRfc1123(j));
        }
        return hashMap;
    }

    private byte[] inputStreamToBytes(InputStream inputStream, int i) {
        PoolingByteArrayOutputStream poolingByteArrayOutputStream = new PoolingByteArrayOutputStream(this.mPool, i);
        String str = "Error occurred when closing InputStream";
        byte[] bArr = null;
        if (inputStream != null) {
            try {
                bArr = this.mPool.getBuf(1024);
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    poolingByteArrayOutputStream.write(bArr, 0, read);
                }
                byte[] byteArray = poolingByteArrayOutputStream.toByteArray();
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException unused) {
                        VolleyLog.v(str, new Object[0]);
                    }
                }
                return byteArray;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException unused2) {
                        VolleyLog.v(str, new Object[0]);
                    }
                }
                this.mPool.returnBuf(bArr);
                poolingByteArrayOutputStream.close();
            }
        } else {
            throw new ServerError();
        }
    }

    private void logSlowRequests(long j, Request request, byte[] bArr, int i) {
        if (DEBUG || j > 3000) {
            Object[] objArr = new Object[5];
            objArr[0] = request;
            objArr[1] = Long.valueOf(j);
            objArr[2] = bArr != null ? Integer.valueOf(bArr.length) : "null";
            objArr[3] = Integer.valueOf(i);
            objArr[4] = Integer.valueOf(request.getRetryPolicy().getCurrentRetryCount());
            VolleyLog.d("HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]", objArr);
        }
    }

    /* access modifiers changed from: protected */
    public void logError(String str, String str2, long j) {
        VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", str, Long.valueOf(SystemClock.elapsedRealtime() - j), str2);
    }

    /* JADX INFO: used method not loaded: com.android.volley.AuthFailureError.<init>(com.android.volley.NetworkResponse):null, types can be incorrect */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x005d, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005e, code lost:
        r15 = null;
        r19 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00a9, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00b2, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00b3, code lost:
        r1 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00b5, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00b6, code lost:
        r19 = r1;
        r15 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00ba, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00bb, code lost:
        r19 = r1;
        r12 = null;
        r15 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00c1, code lost:
        r0 = r12.getStatusCode();
        com.android.volley.VolleyLog.e("Unexpected response code %d for %s", java.lang.Integer.valueOf(r0), r29.getUrl());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00da, code lost:
        if (r15 != null) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00dc, code lost:
        r13 = new com.android.volley.NetworkResponse(r0, r15, false, android.os.SystemClock.elapsedRealtime() - r9, r19);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00ed, code lost:
        if (r0 == 401) goto L_0x0125;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00f6, code lost:
        if (r0 < 400) goto L_0x0103;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0102, code lost:
        throw new com.android.volley.ClientError(r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0105, code lost:
        if (r0 < 500) goto L_0x011f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x010f, code lost:
        if (r29.shouldRetryServerErrors() != false) goto L_0x0111;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0111, code lost:
        r0 = new com.android.volley.ServerError(r13);
        r1 = "server";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x011e, code lost:
        throw new com.android.volley.ServerError(r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0124, code lost:
        throw new com.android.volley.ServerError(r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0125, code lost:
        r0 = new com.android.volley.AuthFailureError(r13);
        r1 = "auth";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x012d, code lost:
        r0 = new com.android.volley.NetworkError();
        r1 = "network";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x013a, code lost:
        throw new com.android.volley.NoConnectionError(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x013b, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x013c, code lost:
        r2 = new java.lang.StringBuilder();
        r2.append("Bad URL ");
        r2.append(r29.getUrl());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0156, code lost:
        throw new java.lang.RuntimeException(r2.toString(), r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0157, code lost:
        r0 = new com.android.volley.TimeoutError();
        r1 = "socket";
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00c1  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x013b A[ExcHandler: MalformedURLException (r0v2 'e' java.net.MalformedURLException A[CUSTOM_DECLARE]), Splitter:B:2:0x000e] */
    /* JADX WARNING: Removed duplicated region for block: B:79:? A[ExcHandler: SocketTimeoutException (unused java.net.SocketTimeoutException), SYNTHETIC, Splitter:B:2:0x000e] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0135 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public NetworkResponse performRequest(Request request) {
        String str;
        VolleyError volleyError;
        HttpResponse httpResponse;
        List headers;
        byte[] inputStreamToBytes;
        List list;
        Request request2 = request;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        while (true) {
            List emptyList = Collections.emptyList();
            try {
                httpResponse = this.mBaseHttpStack.executeRequest(request2, getCacheHeaders(request.getCacheEntry()));
                int statusCode = httpResponse.getStatusCode();
                headers = httpResponse.getHeaders();
                if (statusCode == 304) {
                    Entry cacheEntry = request.getCacheEntry();
                    if (cacheEntry == null) {
                        NetworkResponse networkResponse = new NetworkResponse((int) af.bj, (byte[]) null, true, SystemClock.elapsedRealtime() - elapsedRealtime, headers);
                        return networkResponse;
                    }
                    NetworkResponse networkResponse2 = new NetworkResponse((int) af.bj, cacheEntry.data, true, SystemClock.elapsedRealtime() - elapsedRealtime, combineHeaders(headers, cacheEntry));
                    return networkResponse2;
                }
                InputStream content = httpResponse.getContent();
                inputStreamToBytes = content != null ? inputStreamToBytes(content, httpResponse.getContentLength()) : new byte[0];
                logSlowRequests(SystemClock.elapsedRealtime() - elapsedRealtime, request, inputStreamToBytes, statusCode);
                if (statusCode < 200 || statusCode > 299) {
                    List list2 = headers;
                } else {
                    list = headers;
                    r13 = r13;
                    NetworkResponse networkResponse3 = new NetworkResponse(statusCode, inputStreamToBytes, false, SystemClock.elapsedRealtime() - elapsedRealtime, list);
                    return networkResponse3;
                }
            } catch (SocketTimeoutException unused) {
            } catch (MalformedURLException e) {
            } catch (IOException e2) {
                e = e2;
                list = headers;
                List list3 = list;
                byte[] bArr = inputStreamToBytes;
                if (httpResponse == null) {
                }
            }
            attemptRetryOnException(str, request2, volleyError);
        }
        List list22 = headers;
        throw new IOException();
    }
}
