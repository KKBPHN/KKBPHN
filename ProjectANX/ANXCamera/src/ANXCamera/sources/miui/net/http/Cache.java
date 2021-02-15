package miui.net.http;

import java.io.InputStream;
import java.util.Map;

public interface Cache {

    public class Entry {
        public String contentEncoding;
        public String contentType;
        public InputStream data;
        public String etag;
        public long length;
        public Map responseHeaders;
        public long serverDate;
        public long softTtl;
        public long ttl;

        public boolean isExpired() {
            return this.ttl < System.currentTimeMillis();
        }

        public boolean refreshNeeded() {
            return this.softTtl < System.currentTimeMillis();
        }
    }

    void clear();

    Entry get(String str);

    void initialize();

    void invalidate(String str, boolean z);

    boolean put(String str, Entry entry);

    void remove(String str);
}
