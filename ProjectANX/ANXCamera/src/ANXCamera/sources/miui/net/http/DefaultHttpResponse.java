package miui.net.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

class DefaultHttpResponse implements HttpResponse {
    private InputStream mContent;
    private String mContentEncoding;
    private long mContentLength;
    private String mContentType;
    private Map mHeaders;
    private int mStatusCode;

    public DefaultHttpResponse(int i, Map map, InputStream inputStream, long j, String str, String str2) {
        this.mStatusCode = i;
        this.mContent = inputStream;
        this.mContentLength = j;
        this.mContentType = str;
        this.mContentEncoding = str2;
        this.mHeaders = map;
    }

    public InputStream getContent() {
        return this.mContent;
    }

    public String getContentEncoding() {
        return this.mContentEncoding;
    }

    public long getContentLength() {
        return this.mContentLength;
    }

    public String getContentType() {
        return this.mContentType;
    }

    public Map getHeaders() {
        return this.mHeaders;
    }

    public int getStatusCode() {
        return this.mStatusCode;
    }

    public void release() {
        try {
            if (this.mContent != null) {
                this.mContent.close();
            }
        } catch (IOException unused) {
        }
        this.mContent = null;
    }

    public void setContent(InputStream inputStream, long j) {
        this.mContent = inputStream;
        this.mContentLength = j;
    }
}
