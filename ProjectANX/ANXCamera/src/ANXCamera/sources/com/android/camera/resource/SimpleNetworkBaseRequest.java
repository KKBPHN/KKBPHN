package com.android.camera.resource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.log.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class SimpleNetworkBaseRequest extends BaseObservableRequest {
    protected static final OkHttpClient CLIENT = new Builder().connectTimeout(15, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build();
    protected static final String TAG = "SimpleNetworkBaseRequest";
    private Map mHeaders;
    protected Map mParams;
    protected String mUrl;

    public SimpleNetworkBaseRequest(String str) {
        setUrl(str);
    }

    private void addHeaders(Request.Builder builder) {
        Map map = this.mHeaders;
        if (map != null && !map.isEmpty()) {
            for (Entry entry : this.mHeaders.entrySet()) {
                builder.addHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0033, code lost:
        if (r4.mUrl.endsWith(r3) == false) goto L_0x0044;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String appendUrlParams() {
        if (this.mUrl != null) {
            Map map = this.mParams;
            if (map != null && !map.isEmpty()) {
                StringBuilder sb = new StringBuilder(this.mUrl);
                String str = "UTF-8";
                String str2 = "?";
                if (this.mUrl.indexOf(63) > 0) {
                    if (!this.mUrl.endsWith(str2)) {
                        str2 = "&";
                    }
                    sb.append(encodeParameters(this.mParams, str));
                    return sb.toString();
                }
                sb.append(str2);
                sb.append(encodeParameters(this.mParams, str));
                return sb.toString();
            }
        }
        return this.mUrl;
    }

    private String encodeParameters(Map map, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            for (Entry entry : map.entrySet()) {
                sb.append(URLEncoder.encode((String) entry.getKey(), str));
                sb.append('=');
                sb.append(URLEncoder.encode((String) entry.getValue(), str));
                sb.append('&');
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Encoding not supported: ");
            sb2.append(str);
            throw new RuntimeException(sb2.toString(), e);
        }
    }

    public final void addHeaders(String str, String str2) {
        if (this.mHeaders == null) {
            this.mHeaders = new HashMap();
        }
        this.mHeaders.put(str, str2);
    }

    /* access modifiers changed from: protected */
    public void addParam(String str, String str2) {
        if (this.mParams == null) {
            this.mParams = new HashMap();
        }
        this.mParams.put(str, str2);
    }

    /* access modifiers changed from: protected */
    public RequestBody generatePostBody() {
        return null;
    }

    public abstract Object process(String str, @Nullable Object obj);

    /* access modifiers changed from: protected */
    public void scheduleRequest(final ResponseListener responseListener, @NonNull final Object obj) {
        Request.Builder url = new Request.Builder().get().url(appendUrlParams());
        addHeaders(url);
        RequestBody generatePostBody = generatePostBody();
        if (generatePostBody != null) {
            url = url.post(generatePostBody);
        }
        CLIENT.newCall(url.build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                Log.e(SimpleNetworkBaseRequest.TAG, "scheduleRequest onFailure", (Throwable) iOException);
                ResponseListener responseListener = responseListener;
                if (responseListener != null) {
                    responseListener.onResponseError(0, iOException.getMessage(), iOException);
                }
            }

            public void onResponse(Call call, Response response) {
                int i;
                ResponseListener responseListener;
                String message;
                if (!response.isSuccessful()) {
                    responseListener = responseListener;
                    if (responseListener != null) {
                        i = 1;
                        message = response.message();
                    }
                    response.close();
                }
                i = 0;
                try {
                    SimpleNetworkBaseRequest.this.process(response.body().string(), obj);
                    if (responseListener != null) {
                        responseListener.onResponse(obj, false);
                    }
                } catch (BaseRequestException e) {
                    ResponseListener responseListener2 = responseListener;
                    if (responseListener2 != null) {
                        responseListener2.onResponseError(e.getErrorCode(), e.getMessage(), response);
                    }
                } catch (IOException e2) {
                    responseListener = responseListener;
                    if (responseListener != null) {
                        message = e2.getMessage();
                    }
                }
                response.close();
                responseListener.onResponseError(i, message, response);
                response.close();
            }
        });
    }

    public void setUrl(String str) {
        this.mUrl = str;
    }
}
