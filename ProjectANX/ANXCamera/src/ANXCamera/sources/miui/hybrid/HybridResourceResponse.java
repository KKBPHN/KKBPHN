package miui.hybrid;

import java.io.InputStream;

public class HybridResourceResponse {
    private String mEncoding;
    private InputStream mInputStream;
    private String mMimeType;

    public HybridResourceResponse(String str, String str2, InputStream inputStream) {
        this.mMimeType = str;
        this.mEncoding = str2;
        this.mInputStream = inputStream;
    }

    public InputStream getData() {
        return this.mInputStream;
    }

    public String getEncoding() {
        return this.mEncoding;
    }

    public String getMimeType() {
        return this.mMimeType;
    }

    public void setData(InputStream inputStream) {
        this.mInputStream = inputStream;
    }

    public void setEncoding(String str) {
        this.mEncoding = str;
    }

    public void setMimeType(String str) {
        this.mMimeType = str;
    }
}
