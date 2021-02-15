package com.android.volley.toolbox;

import android.support.annotation.VisibleForTesting;
import com.android.volley.Header;
import com.android.volley.Request;
import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class HurlStack extends BaseHttpStack {
    private static final int HTTP_CONTINUE = 100;
    private final SSLSocketFactory mSslSocketFactory;
    private final UrlRewriter mUrlRewriter;

    class UrlConnectionInputStream extends FilterInputStream {
        private final HttpURLConnection mConnection;

        UrlConnectionInputStream(HttpURLConnection httpURLConnection) {
            super(HurlStack.inputStreamFromConnection(httpURLConnection));
            this.mConnection = httpURLConnection;
        }

        public void close() {
            super.close();
            this.mConnection.disconnect();
        }
    }

    public interface UrlRewriter {
        String rewriteUrl(String str);
    }

    public HurlStack() {
        this(null);
    }

    public HurlStack(UrlRewriter urlRewriter) {
        this(urlRewriter, null);
    }

    public HurlStack(UrlRewriter urlRewriter, SSLSocketFactory sSLSocketFactory) {
        this.mUrlRewriter = urlRewriter;
        this.mSslSocketFactory = sSLSocketFactory;
    }

    private static void addBody(HttpURLConnection httpURLConnection, Request request, byte[] bArr) {
        httpURLConnection.setDoOutput(true);
        String str = "Content-Type";
        if (!httpURLConnection.getRequestProperties().containsKey(str)) {
            httpURLConnection.setRequestProperty(str, request.getBodyContentType());
        }
        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
        dataOutputStream.write(bArr);
        dataOutputStream.close();
    }

    private static void addBodyIfExists(HttpURLConnection httpURLConnection, Request request) {
        byte[] body = request.getBody();
        if (body != null) {
            addBody(httpURLConnection, request, body);
        }
    }

    @VisibleForTesting
    static List convertHeaders(Map map) {
        ArrayList arrayList = new ArrayList(map.size());
        for (Entry entry : map.entrySet()) {
            if (entry.getKey() != null) {
                for (String header : (List) entry.getValue()) {
                    arrayList.add(new Header((String) entry.getKey(), header));
                }
            }
        }
        return arrayList;
    }

    private static boolean hasResponseBody(int i, int i2) {
        return (i == 4 || (100 <= i2 && i2 < 200) || i2 == 204 || i2 == 304) ? false : true;
    }

    /* JADX WARNING: type inference failed for: r0v1, types: [java.net.HttpURLConnection] */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static InputStream inputStreamFromConnection(HttpURLConnection httpURLConnection) {
        try {
            r0 = httpURLConnection;
            r0 = httpURLConnection.getInputStream();
            r0 = r0;
            return r0;
        } catch (IOException unused) {
            return r0.getErrorStream();
        }
    }

    private HttpURLConnection openConnection(URL url, Request request) {
        HttpURLConnection createConnection = createConnection(url);
        int timeoutMs = request.getTimeoutMs();
        createConnection.setConnectTimeout(timeoutMs);
        createConnection.setReadTimeout(timeoutMs);
        createConnection.setUseCaches(false);
        createConnection.setDoInput(true);
        if ("https".equals(url.getProtocol())) {
            SSLSocketFactory sSLSocketFactory = this.mSslSocketFactory;
            if (sSLSocketFactory != null) {
                ((HttpsURLConnection) createConnection).setSSLSocketFactory(sSLSocketFactory);
            }
        }
        return createConnection;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0022, code lost:
        r2.setRequestMethod(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0029, code lost:
        addBodyIfExists(r2, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002f, code lost:
        r2.setRequestMethod(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void setConnectionParametersForRequest(HttpURLConnection httpURLConnection, Request request) {
        String str;
        String str2;
        String str3 = "POST";
        switch (request.getMethod()) {
            case -1:
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    httpURLConnection.setRequestMethod(str3);
                    addBody(httpURLConnection, request, postBody);
                    return;
                }
                return;
            case 0:
                str = "GET";
                break;
            case 1:
                httpURLConnection.setRequestMethod(str3);
                break;
            case 2:
                str2 = "PUT";
                break;
            case 3:
                str = "DELETE";
                break;
            case 4:
                str = HttpRequest.METHOD_HEAD;
                break;
            case 5:
                str = HttpRequest.METHOD_OPTIONS;
                break;
            case 6:
                str = HttpRequest.METHOD_TRACE;
                break;
            case 7:
                str2 = "PATCH";
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection createConnection(URL url) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setInstanceFollowRedirects(HttpURLConnection.getFollowRedirects());
        return httpURLConnection;
    }

    public HttpResponse executeRequest(Request request, Map map) {
        String str;
        String url = request.getUrl();
        HashMap hashMap = new HashMap();
        hashMap.putAll(map);
        hashMap.putAll(request.getHeaders());
        UrlRewriter urlRewriter = this.mUrlRewriter;
        if (urlRewriter != null) {
            str = urlRewriter.rewriteUrl(url);
            if (str == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("URL blocked by rewriter: ");
                sb.append(url);
                throw new IOException(sb.toString());
            }
        } else {
            str = url;
        }
        HttpURLConnection openConnection = openConnection(new URL(str), request);
        boolean z = false;
        try {
            for (String str2 : hashMap.keySet()) {
                openConnection.setRequestProperty(str2, (String) hashMap.get(str2));
            }
            setConnectionParametersForRequest(openConnection, request);
            int responseCode = openConnection.getResponseCode();
            if (responseCode == -1) {
                throw new IOException("Could not retrieve response code from HttpUrlConnection.");
            } else if (!hasResponseBody(request.getMethod(), responseCode)) {
                HttpResponse httpResponse = new HttpResponse(responseCode, convertHeaders(openConnection.getHeaderFields()));
                openConnection.disconnect();
                return httpResponse;
            } else {
                z = true;
                return new HttpResponse(responseCode, convertHeaders(openConnection.getHeaderFields()), openConnection.getContentLength(), new UrlConnectionInputStream(openConnection));
            }
        } catch (Throwable th) {
            if (!z) {
                openConnection.disconnect();
            }
            throw th;
        }
    }
}
