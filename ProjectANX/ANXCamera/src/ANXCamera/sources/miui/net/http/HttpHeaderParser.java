package miui.net.http;

import java.util.Map;
import miui.net.http.Cache.Entry;

class HttpHeaderParser {
    HttpHeaderParser() {
    }

    public static Entry parseCacheHeaders(HttpResponse httpResponse) {
        long j;
        long j2;
        long currentTimeMillis = System.currentTimeMillis();
        Map headers = httpResponse.getHeaders();
        String str = (String) headers.get("date");
        long parseDateAsEpoch = str != null ? parseDateAsEpoch(str) : 0;
        String str2 = (String) headers.get("cache-control");
        int i = 0;
        if (str2 != null) {
            String[] split = str2.split(",");
            int length = split.length;
            j = 0;
            while (i < length) {
                String trim = split[i].trim();
                if (trim.equals("no-cache") || trim.equals("no-store")) {
                    return null;
                }
                if (trim.startsWith("max-age=")) {
                    try {
                        j = Long.parseLong(trim.substring(8));
                    } catch (Exception unused) {
                    }
                } else if (trim.equals("must-revalidate") || trim.equals("proxy-revalidate")) {
                    j = 0;
                }
                i++;
            }
            i = 1;
        } else {
            j = 0;
        }
        String str3 = (String) headers.get("expires");
        long parseDateAsEpoch2 = str3 != null ? parseDateAsEpoch(str3) : 0;
        String str4 = (String) headers.get("etag");
        if (str4 == null) {
            str4 = null;
        }
        if (i != 0) {
            j2 = currentTimeMillis + (j * 1000);
        } else {
            if (parseDateAsEpoch > 0 && parseDateAsEpoch2 >= parseDateAsEpoch) {
                j2 = currentTimeMillis + (parseDateAsEpoch2 - parseDateAsEpoch);
            }
            return null;
        }
        if (httpResponse.getContent() != null) {
            Entry entry = new Entry();
            entry.data = httpResponse.getContent();
            entry.length = httpResponse.getContentLength();
            entry.etag = str4;
            entry.softTtl = j2;
            entry.ttl = j2;
            entry.serverDate = parseDateAsEpoch;
            entry.responseHeaders = httpResponse.getHeaders();
            return entry;
        }
        return null;
    }

    public static long parseDateAsEpoch(String str) {
        try {
            return DateUtils.parseDate(str).getTime();
        } catch (IllegalArgumentException unused) {
            return 0;
        }
    }
}
