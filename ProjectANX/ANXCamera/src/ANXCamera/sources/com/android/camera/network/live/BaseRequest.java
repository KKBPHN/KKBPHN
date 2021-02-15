package com.android.camera.network.live;

import com.android.camera.fragment.CtaNoticeFragment.CTA;
import com.android.camera.log.Log;
import com.android.camera.network.net.base.ErrorCode;
import com.android.camera.network.net.base.ResponseListener;
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
import okhttp3.Response;

public abstract class BaseRequest {
    protected static final OkHttpClient CLIENT = new Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
    protected static final String TAG = "BaseRequest";
    protected Map mParams = new HashMap();
    protected String mUrl;

    public BaseRequest(String str) {
        this.mUrl = str;
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

    /* access modifiers changed from: protected */
    public void addParam(String str, String str2) {
        this.mParams.put(str, str2);
    }

    public void execute(final ResponseListener responseListener) {
        if (!CTA.canConnectNetwork()) {
            responseListener.onResponseError(ErrorCode.NETWORK_NOT_CONNECTED, "CTA not confirmed.", null);
            return;
        }
        CLIENT.newCall(new Request.Builder().get().url(appendUrlParams()).build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                Log.e(BaseRequest.TAG, "execute failed", (Throwable) iOException);
                responseListener.onResponseError(ErrorCode.NET_ERROR, iOException.getMessage(), iOException);
            }

            public void onResponse(Call call, Response response) {
                ResponseListener responseListener;
                ErrorCode errorCode;
                String message;
                String str = "execute process failed";
                String str2 = BaseRequest.TAG;
                if (!response.isSuccessful()) {
                    responseListener = responseListener;
                    errorCode = ErrorCode.SERVER_ERROR;
                    message = response.message();
                } else {
                    try {
                        Object process = BaseRequest.this.process(response.body().string());
                        responseListener.onResponse(process);
                    } catch (BaseRequestException e) {
                        Log.e(str2, str, (Throwable) e);
                        responseListener = responseListener;
                        errorCode = e.getErrorCode();
                        message = e.getMessage();
                    } catch (IOException e2) {
                        Log.e(str2, str, (Throwable) e2);
                        responseListener = responseListener;
                        errorCode = ErrorCode.NET_ERROR;
                        message = e2.getMessage();
                    }
                    response.close();
                }
                responseListener.onResponseError(errorCode, message, response);
                response.close();
            }
        });
    }

    public abstract Object process(String str);
}
