package com.android.camera.resource;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {
    private BufferedSource bufferedSource;
    /* access modifiers changed from: private */
    public final ResponseBody responseBody;
    /* access modifiers changed from: private */
    public final ResponseListener responseListener;

    public ProgressResponseBody(ResponseBody responseBody2, ResponseListener responseListener2) {
        this.responseBody = responseBody2;
        this.responseListener = responseListener2;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long contentLength = 0;
            long totalBytesRead = 0;

            public long read(Buffer buffer, long j) {
                long read = super.read(buffer, j);
                this.totalBytesRead += read != -1 ? read : 0;
                if (this.contentLength == 0) {
                    this.contentLength = ProgressResponseBody.this.responseBody.contentLength();
                }
                if (ProgressResponseBody.this.responseListener != null) {
                    ProgressResponseBody.this.responseListener.onResponseProgress(this.totalBytesRead, this.contentLength);
                }
                return read;
            }
        };
    }

    public long contentLength() {
        return this.responseBody.contentLength();
    }

    public MediaType contentType() {
        return this.responseBody.contentType();
    }

    public BufferedSource source() {
        if (this.bufferedSource == null) {
            this.bufferedSource = Okio.buffer(source(this.responseBody.source()));
        }
        return this.bufferedSource;
    }
}
