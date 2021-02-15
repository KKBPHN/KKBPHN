package okhttp3;

import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import java.net.URL;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpMethod;

public final class Request {
    @Nullable
    final RequestBody body;
    private volatile CacheControl cacheControl;
    final Headers headers;
    final String method;
    final Object tag;
    final HttpUrl url;

    public class Builder {
        RequestBody body;
        okhttp3.Headers.Builder headers;
        String method;
        Object tag;
        HttpUrl url;

        public Builder() {
            this.method = "GET";
            this.headers = new okhttp3.Headers.Builder();
        }

        Builder(Request request) {
            this.url = request.url;
            this.method = request.method;
            this.body = request.body;
            this.tag = request.tag;
            this.headers = request.headers.newBuilder();
        }

        public Builder addHeader(String str, String str2) {
            this.headers.add(str, str2);
            return this;
        }

        public Request build() {
            if (this.url != null) {
                return new Request(this);
            }
            throw new IllegalStateException("url == null");
        }

        public Builder cacheControl(CacheControl cacheControl) {
            String cacheControl2 = cacheControl.toString();
            boolean isEmpty = cacheControl2.isEmpty();
            String str = HttpRequest.HEADER_CACHE_CONTROL;
            return isEmpty ? removeHeader(str) : header(str, cacheControl2);
        }

        public Builder delete() {
            return delete(Util.EMPTY_REQUEST);
        }

        public Builder delete(@Nullable RequestBody requestBody) {
            return method("DELETE", requestBody);
        }

        public Builder get() {
            return method("GET", null);
        }

        public Builder head() {
            return method(HttpRequest.METHOD_HEAD, null);
        }

        public Builder header(String str, String str2) {
            this.headers.set(str, str2);
            return this;
        }

        public Builder headers(Headers headers2) {
            this.headers = headers2.newBuilder();
            return this;
        }

        public Builder method(String str, @Nullable RequestBody requestBody) {
            if (str == null) {
                throw new NullPointerException("method == null");
            } else if (str.length() != 0) {
                String str2 = "method ";
                if (requestBody != null && !HttpMethod.permitsRequestBody(str)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(str);
                    sb.append(" must not have a request body.");
                    throw new IllegalArgumentException(sb.toString());
                } else if (requestBody != null || !HttpMethod.requiresRequestBody(str)) {
                    this.method = str;
                    this.body = requestBody;
                    return this;
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str2);
                    sb2.append(str);
                    sb2.append(" must have a request body.");
                    throw new IllegalArgumentException(sb2.toString());
                }
            } else {
                throw new IllegalArgumentException("method.length() == 0");
            }
        }

        public Builder patch(RequestBody requestBody) {
            return method("PATCH", requestBody);
        }

        public Builder post(RequestBody requestBody) {
            return method("POST", requestBody);
        }

        public Builder put(RequestBody requestBody) {
            return method("PUT", requestBody);
        }

        public Builder removeHeader(String str) {
            this.headers.removeAll(str);
            return this;
        }

        public Builder tag(Object obj) {
            this.tag = obj;
            return this;
        }

        /* JADX WARNING: Removed duplicated region for block: B:10:0x0045  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x004a  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public Builder url(String str) {
            HttpUrl parse;
            StringBuilder sb;
            int i;
            if (str != null) {
                if (str.regionMatches(true, 0, "ws:", 0, 3)) {
                    sb = new StringBuilder();
                    sb.append("http:");
                    i = 3;
                } else {
                    if (str.regionMatches(true, 0, "wss:", 0, 4)) {
                        sb = new StringBuilder();
                        sb.append("https:");
                        i = 4;
                    }
                    parse = HttpUrl.parse(str);
                    if (parse == null) {
                        return url(parse);
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("unexpected url: ");
                    sb2.append(str);
                    throw new IllegalArgumentException(sb2.toString());
                }
                sb.append(str.substring(i));
                str = sb.toString();
                parse = HttpUrl.parse(str);
                if (parse == null) {
                }
            } else {
                throw new NullPointerException("url == null");
            }
        }

        public Builder url(URL url2) {
            if (url2 != null) {
                HttpUrl httpUrl = HttpUrl.get(url2);
                if (httpUrl != null) {
                    return url(httpUrl);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("unexpected url: ");
                sb.append(url2);
                throw new IllegalArgumentException(sb.toString());
            }
            throw new NullPointerException("url == null");
        }

        public Builder url(HttpUrl httpUrl) {
            if (httpUrl != null) {
                this.url = httpUrl;
                return this;
            }
            throw new NullPointerException("url == null");
        }
    }

    Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers.build();
        this.body = builder.body;
        Object obj = builder.tag;
        if (obj == 0) {
            obj = this;
        }
        this.tag = obj;
    }

    @Nullable
    public RequestBody body() {
        return this.body;
    }

    public CacheControl cacheControl() {
        CacheControl cacheControl2 = this.cacheControl;
        if (cacheControl2 != null) {
            return cacheControl2;
        }
        CacheControl parse = CacheControl.parse(this.headers);
        this.cacheControl = parse;
        return parse;
    }

    public String header(String str) {
        return this.headers.get(str);
    }

    public List headers(String str) {
        return this.headers.values(str);
    }

    public Headers headers() {
        return this.headers;
    }

    public boolean isHttps() {
        return this.url.isHttps();
    }

    public String method() {
        return this.method;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public Object tag() {
        return this.tag;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request{method=");
        sb.append(this.method);
        sb.append(", url=");
        sb.append(this.url);
        sb.append(", tag=");
        Object obj = this.tag;
        if (obj == this) {
            obj = null;
        }
        sb.append(obj);
        sb.append('}');
        return sb.toString();
    }

    public HttpUrl url() {
        return this.url;
    }
}
