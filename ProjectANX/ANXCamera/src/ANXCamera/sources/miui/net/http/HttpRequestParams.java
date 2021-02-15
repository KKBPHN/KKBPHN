package miui.net.http;

import android.text.TextUtils;
import android.util.Pair;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRequestParams {
    public static String DEFAULT_ENCODING = "UTF-8";
    private String mEncoding;
    private boolean mHasStream;
    private ConcurrentHashMap mUrlParams;

    class FileWrapper {
        public long contentLength;
        public String contentType;
        public String fileName;
        public InputStream inputStream;

        public FileWrapper(InputStream inputStream2, long j, String str, String str2) {
            this.inputStream = inputStream2;
            this.contentLength = j;
            if (str == null) {
                str = "nofilename";
            }
            this.fileName = str;
            this.contentType = str2;
        }
    }

    public HttpRequestParams() {
        this.mEncoding = DEFAULT_ENCODING;
        this.mUrlParams = new ConcurrentHashMap();
        this.mHasStream = false;
    }

    public HttpRequestParams(Object... objArr) {
        this();
        int length = objArr.length;
        if (length % 2 == 0) {
            int i = 0;
            while (i < length) {
                if (objArr[i] instanceof String) {
                    String str = objArr[i];
                    String str2 = objArr[i + 1];
                    if (str2 instanceof String) {
                        add(str, str2);
                    } else if (str2 instanceof File) {
                        add(str, (File) str2);
                    } else if (str2 instanceof List) {
                        add(str, (List) str2);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unknown argument type : ");
                        sb.append(str2);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    i += 2;
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unknown argument name : ");
                    sb2.append(objArr[i]);
                    throw new IllegalArgumentException(sb2.toString());
                }
            }
            return;
        }
        throw new IllegalArgumentException("Supplied argument must be even");
    }

    private List getParamsList() {
        LinkedList linkedList = new LinkedList();
        for (Entry entry : this.mUrlParams.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                linkedList.add(new Pair((String) entry.getKey(), (String) value));
            } else if (value instanceof List) {
                for (String pair : (List) value) {
                    linkedList.add(new Pair((String) entry.getKey(), pair));
                }
            }
        }
        return linkedList;
    }

    public HttpRequestParams add(String str, File file) {
        if (!(str == null || file == null)) {
            try {
                ConcurrentHashMap concurrentHashMap = this.mUrlParams;
                FileWrapper fileWrapper = new FileWrapper(new FileInputStream(file), file.length(), file.getName(), null);
                concurrentHashMap.put(str, fileWrapper);
                this.mHasStream = true;
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }
        return this;
    }

    public HttpRequestParams add(String str, File file, String str2) {
        if (!(str == null || file == null)) {
            try {
                ConcurrentHashMap concurrentHashMap = this.mUrlParams;
                FileWrapper fileWrapper = new FileWrapper(new FileInputStream(file), file.length(), file.getName(), str2);
                concurrentHashMap.put(str, fileWrapper);
                this.mHasStream = true;
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }
        return this;
    }

    public HttpRequestParams add(String str, InputStream inputStream, long j, String str2) {
        if (!(str == null || inputStream == null || j < 0)) {
            ConcurrentHashMap concurrentHashMap = this.mUrlParams;
            FileWrapper fileWrapper = new FileWrapper(inputStream, j, str2, null);
            concurrentHashMap.put(str, fileWrapper);
            this.mHasStream = true;
        }
        return this;
    }

    public HttpRequestParams add(String str, InputStream inputStream, long j, String str2, String str3) {
        if (!(str == null || inputStream == null || j < 0)) {
            ConcurrentHashMap concurrentHashMap = this.mUrlParams;
            FileWrapper fileWrapper = new FileWrapper(inputStream, j, str2, str3);
            concurrentHashMap.put(str, fileWrapper);
            this.mHasStream = true;
        }
        return this;
    }

    public HttpRequestParams add(String str, String str2) {
        if (!(str == null || str2 == null)) {
            this.mUrlParams.put(str, str2);
        }
        return this;
    }

    public HttpRequestParams add(String str, List list) {
        if (!(str == null || list == null || list.size() <= 0)) {
            this.mUrlParams.put(str, list);
        }
        return this;
    }

    public HttpRequestParams add(Map map) {
        for (Entry entry : map.entrySet()) {
            add((String) entry.getKey(), (String) entry.getValue());
        }
        return this;
    }

    public String getParamString() {
        StringBuilder sb = new StringBuilder();
        for (Pair pair : getParamsList()) {
            if (!TextUtils.isEmpty(sb)) {
                sb.append("&");
            }
            try {
                sb.append(URLEncoder.encode((String) pair.first, this.mEncoding));
                sb.append("=");
                sb.append(URLEncoder.encode((String) pair.second, this.mEncoding));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public HttpRequestParams remove(String str) {
        this.mUrlParams.remove(str);
        return this;
    }

    public HttpRequestParams setEncoding(String str) {
        if (str == null) {
            str = DEFAULT_ENCODING;
        }
        this.mEncoding = str;
        return this;
    }
}
