package com.android.camera.resource;

import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Response;

public class DownloadInterceptor implements Interceptor {
    private ResponseListener responseListener;

    public DownloadInterceptor(ResponseListener responseListener2) {
        this.responseListener = responseListener2;
    }

    public Response intercept(Chain chain) {
        Response proceed = chain.proceed(chain.request());
        return proceed.newBuilder().body(new ProgressResponseBody(proceed.body(), this.responseListener)).build();
    }
}
