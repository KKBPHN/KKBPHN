package com.android.volley.toolbox;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public final class HttpResponse {
    private final InputStream mContent;
    private final int mContentLength;
    private final List mHeaders;
    private final int mStatusCode;

    public HttpResponse(int i, List list) {
        this(i, list, -1, null);
    }

    public HttpResponse(int i, List list, int i2, InputStream inputStream) {
        this.mStatusCode = i;
        this.mHeaders = list;
        this.mContentLength = i2;
        this.mContent = inputStream;
    }

    public final InputStream getContent() {
        return this.mContent;
    }

    public final int getContentLength() {
        return this.mContentLength;
    }

    public final List getHeaders() {
        return Collections.unmodifiableList(this.mHeaders);
    }

    public final int getStatusCode() {
        return this.mStatusCode;
    }
}
