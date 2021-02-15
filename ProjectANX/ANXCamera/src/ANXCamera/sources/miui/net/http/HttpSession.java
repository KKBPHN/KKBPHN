package miui.net.http;

import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import miui.net.http.Cache.Entry;
import miui.util.IOUtils;
import miui.util.Log;

public class HttpSession {
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    private static final String ENCODING_GZIP = "gzip";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String TAG = "HttpSession";
    private Cache mCache = DiskBasedCache.getDefault();
    private final Map mClientHeaders = new HashMap();
    private String mClientParams;
    private RetryStrategy mRetryStrategy = new BaseRetryStrategy();
    private int mTimeout;

    class CountingInputStream extends FilterInputStream {
        private long mContentLength;
        private long mPercentage = 0;
        private ProgressListener mProgressListener;
        private long mRead = 0;

        public CountingInputStream(InputStream inputStream, long j, String str, ProgressListener progressListener) {
            super(inputStream);
            this.mContentLength = j;
            this.mProgressListener = progressListener;
            if (str != null && str.length() > 0) {
                Matcher matcher = Pattern.compile("bytes\\s+(\\d+)-(\\d+)/(\\d+)").matcher(str);
                if (matcher.matches() && matcher.groupCount() == 3) {
                    this.mRead = Long.parseLong(matcher.group(1));
                    this.mContentLength = Long.parseLong(matcher.group(3));
                }
            }
        }

        private void reportProgress(int i) {
            long j = this.mContentLength;
            if (j > 0 && this.mProgressListener != null) {
                long j2 = (this.mRead * 10) / j;
                if (this.mPercentage != j2 || i > 1024) {
                    this.mPercentage = j2;
                    this.mProgressListener.onProgress(this.mContentLength, this.mRead);
                }
            }
        }

        public int read() {
            int read = super.read();
            if (read > 0) {
                this.mRead++;
                reportProgress(1);
            }
            return read;
        }

        public int read(byte[] bArr) {
            return read(bArr, 0, bArr.length);
        }

        public int read(byte[] bArr, int i, int i2) {
            int read = super.read(bArr, i, i2);
            if (read > 0) {
                this.mRead += (long) read;
                reportProgress(read);
            }
            return read;
        }
    }

    public interface ProgressListener {
        void onProgress(long j, long j2);
    }

    private void addCacheHeaders(Entry entry) {
        String str = entry.etag;
        if (str != null) {
            addHeader(HttpRequest.HEADER_IF_NONE_MATCH, str);
        }
        long j = entry.serverDate;
        if (j > 0) {
            addHeader("If-Modified-Since", DateUtils.formatDate(new Date(j)));
        }
    }

    private void addRequestHeaders(Map map) {
        if (map != null && map.size() > 0) {
            for (Map.Entry entry : map.entrySet()) {
                this.mClientHeaders.put((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    private static Map convertHeaders(Map map) {
        HashMap hashMap = new HashMap();
        if (map != null) {
            for (String str : map.keySet()) {
                String str2 = (String) ((List) map.get(str)).get(0);
                if (!(str == null || str2 == null)) {
                    hashMap.put(str.toLowerCase(), str2.toLowerCase());
                }
            }
        }
        return hashMap;
    }

    private DefaultHttpResponse executeGet(String str, Entry entry, ProgressListener progressListener) {
        String str2;
        InputStream inputStream;
        Entry entry2 = entry;
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setReadTimeout(this.mTimeout);
        httpURLConnection.setConnectTimeout(this.mTimeout);
        Map map = this.mClientHeaders;
        if (map != null && map.keySet().size() > 0) {
            for (String str3 : this.mClientHeaders.keySet()) {
                httpURLConnection.setRequestProperty(str3, (String) this.mClientHeaders.get(str3));
            }
        }
        httpURLConnection.connect();
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == 304) {
            DefaultHttpResponse defaultHttpResponse = new DefaultHttpResponse(200, entry2.responseHeaders, entry2.data, entry2.length, entry2.contentType, entry2.contentEncoding);
            return defaultHttpResponse;
        } else if (responseCode < 200 || responseCode > 299) {
            throw new IOException(httpURLConnection.getResponseMessage());
        } else {
            long contentLength = (long) httpURLConnection.getContentLength();
            String contentType = httpURLConnection.getContentType();
            String contentEncoding = httpURLConnection.getContentEncoding();
            InputStream inputStream2 = httpURLConnection.getInputStream();
            Map convertHeaders = convertHeaders(httpURLConnection.getHeaderFields());
            CountingInputStream countingInputStream = new CountingInputStream(inputStream2, contentLength, httpURLConnection.getHeaderField("content-range"), progressListener);
            if (contentType != null) {
                contentType = contentType.toLowerCase();
            }
            String str4 = contentType;
            if (contentEncoding != null) {
                String lowerCase = contentEncoding.toLowerCase();
                if (lowerCase.equalsIgnoreCase("gzip")) {
                    inputStream = new GZIPInputStream(countingInputStream);
                    str2 = "";
                    DefaultHttpResponse defaultHttpResponse2 = new DefaultHttpResponse(responseCode, convertHeaders, inputStream, contentLength, str4, str2);
                    putCacheEntry(httpURLConnection.getURL().toURI().toString(), defaultHttpResponse2);
                    return defaultHttpResponse2;
                }
                str2 = lowerCase;
            } else {
                str2 = contentEncoding;
            }
            inputStream = countingInputStream;
            DefaultHttpResponse defaultHttpResponse22 = new DefaultHttpResponse(responseCode, convertHeaders, inputStream, contentLength, str4, str2);
            try {
                putCacheEntry(httpURLConnection.getURL().toURI().toString(), defaultHttpResponse22);
            } catch (URISyntaxException e) {
                Log.d(TAG, e.getMessage());
            }
            return defaultHttpResponse22;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005a, code lost:
        return executeGet(r10, r11, r12);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private DefaultHttpResponse executeGet(String str, HttpRequestParams httpRequestParams, ProgressListener progressListener) {
        Entry cacheEntry = getCacheEntry(getUrlWithQueryString(str, httpRequestParams));
        if (cacheEntry == null || cacheEntry.softTtl <= System.currentTimeMillis()) {
            if (progressListener != null) {
                progressListener.onProgress(-1, -1);
            }
            if (cacheEntry != null) {
                addCacheHeaders(cacheEntry);
            }
            String str2 = "Accept-Encoding";
            if (!this.mClientHeaders.containsKey(str2)) {
                this.mClientHeaders.put(str2, "gzip");
            }
            RetryStrategy retryStrategy = this.mRetryStrategy;
            while (true) {
                if (retryStrategy == null) {
                    break;
                }
                try {
                    setTimeout(retryStrategy.getCurrentTimeout());
                    break;
                } catch (IOException e) {
                    if (retryStrategy == null || !retryStrategy.retry(e)) {
                        throw e;
                    }
                } catch (NullPointerException e2) {
                    if (retryStrategy == null || !retryStrategy.retry(e2)) {
                        throw e2;
                    }
                }
            }
            throw e2;
        }
        DefaultHttpResponse defaultHttpResponse = new DefaultHttpResponse(200, cacheEntry.responseHeaders, cacheEntry.data, cacheEntry.length, cacheEntry.contentType, cacheEntry.contentEncoding);
        if (progressListener != null) {
            long j = cacheEntry.length;
            progressListener.onProgress(j, j);
        }
        return defaultHttpResponse;
    }

    private Entry getCacheEntry(String str) {
        Cache cache = this.mCache;
        if (cache == null) {
            return null;
        }
        return cache.get(str.toString());
    }

    public static HttpSession getDefault() {
        return new HttpSession();
    }

    private static String getUrlWithQueryString(String str, HttpRequestParams httpRequestParams) {
        String str2;
        StringBuilder sb;
        if (httpRequestParams == null) {
            return str;
        }
        String paramString = httpRequestParams.getParamString();
        if (paramString == null || paramString.length() <= 0) {
            return str;
        }
        if (str.indexOf(63) >= 0) {
            sb = new StringBuilder();
            sb.append(str);
            str2 = "?";
        } else {
            sb = new StringBuilder();
            sb.append(str);
            str2 = "&";
        }
        sb.append(str2);
        sb.append(paramString);
        return sb.toString();
    }

    private void putCacheEntry(String str, DefaultHttpResponse defaultHttpResponse) {
        Cache cache = this.mCache;
        if (cache != null) {
            Entry parseCacheHeaders = HttpHeaderParser.parseCacheHeaders(defaultHttpResponse);
            if (parseCacheHeaders != null && cache.put(str, parseCacheHeaders)) {
                defaultHttpResponse.setContent(parseCacheHeaders.data, parseCacheHeaders.length);
            }
        }
    }

    public void addHeader(String str, String str2) {
        this.mClientHeaders.put(str, str2);
    }

    public void clearCacheContent() {
        Cache cache = this.mCache;
        if (cache != null) {
            cache.clear();
        }
    }

    public HttpResponse delete(String str, Map map, HttpRequestParams httpRequestParams, ProgressListener progressListener) {
        throw new UnsupportedOperationException();
    }

    public void download(File file, String str, Map map, HttpRequestParams httpRequestParams, ProgressListener progressListener) {
        FileOutputStream fileOutputStream;
        addRequestHeaders(map);
        if (httpRequestParams != null) {
            this.mClientParams = httpRequestParams.getParamString();
        }
        long j = 0;
        if (file.exists()) {
            j = file.length();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("bytes=");
        sb.append(j);
        sb.append("-");
        addHeader("RANGE", sb.toString());
        DefaultHttpResponse executeGet = executeGet(str, httpRequestParams, progressListener);
        RandomAccessFile randomAccessFile = null;
        try {
            String str2 = (String) executeGet.getHeaders().get("content-range");
            if (str2 != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("bytes ");
                sb2.append(j);
                if (str2.startsWith(sb2.toString())) {
                    RandomAccessFile randomAccessFile2 = new RandomAccessFile(file, "rw");
                    try {
                        randomAccessFile2.seek(j);
                        byte[] bArr = new byte[4096];
                        while (true) {
                            int read = executeGet.getContent().read(bArr);
                            if (read == -1) {
                                break;
                            }
                            randomAccessFile2.write(bArr, 0, read);
                        }
                        randomAccessFile2.close();
                        RandomAccessFile randomAccessFile3 = randomAccessFile2;
                        fileOutputStream = null;
                        randomAccessFile = randomAccessFile3;
                        IOUtils.closeQuietly((Closeable) randomAccessFile);
                        IOUtils.closeQuietly((OutputStream) fileOutputStream);
                        executeGet.release();
                    } catch (Throwable th) {
                        th = th;
                        RandomAccessFile randomAccessFile4 = randomAccessFile2;
                        fileOutputStream = null;
                        randomAccessFile = randomAccessFile4;
                        IOUtils.closeQuietly((Closeable) randomAccessFile);
                        IOUtils.closeQuietly((OutputStream) fileOutputStream);
                        executeGet.release();
                        throw th;
                    }
                }
            }
            fileOutputStream = new FileOutputStream(file);
            try {
                IOUtils.copy(executeGet.getContent(), (OutputStream) fileOutputStream);
                fileOutputStream.close();
                IOUtils.closeQuietly((Closeable) randomAccessFile);
                IOUtils.closeQuietly((OutputStream) fileOutputStream);
                executeGet.release();
            } catch (Throwable th2) {
                th = th2;
                IOUtils.closeQuietly((Closeable) randomAccessFile);
                IOUtils.closeQuietly((OutputStream) fileOutputStream);
                executeGet.release();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
            IOUtils.closeQuietly((Closeable) randomAccessFile);
            IOUtils.closeQuietly((OutputStream) fileOutputStream);
            executeGet.release();
            throw th;
        }
    }

    public HttpResponse get(String str, Map map, HttpRequestParams httpRequestParams, ProgressListener progressListener) {
        addRequestHeaders(map);
        if (httpRequestParams != null) {
            this.mClientParams = httpRequestParams.getParamString();
        }
        return executeGet(str, httpRequestParams, progressListener);
    }

    public HttpResponse post(String str, Map map, HttpRequestParams httpRequestParams, ProgressListener progressListener) {
        throw new UnsupportedOperationException();
    }

    public HttpResponse put(String str, Map map, HttpRequestParams httpRequestParams, ProgressListener progressListener) {
        throw new UnsupportedOperationException();
    }

    public void removeCacheContent(String str) {
        Cache cache = this.mCache;
        if (cache != null) {
            cache.remove(str);
        }
    }

    public void setBasicAuth(String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public void setCache(Cache cache) {
        if (this.mCache != cache) {
            this.mCache = cache;
        }
    }

    public void setRetryStrategy(RetryStrategy retryStrategy) {
        this.mRetryStrategy = retryStrategy;
    }

    public void setTimeout(int i) {
        this.mTimeout = i;
    }

    public void setUserAgent(String str) {
        throw new UnsupportedOperationException();
    }
}
