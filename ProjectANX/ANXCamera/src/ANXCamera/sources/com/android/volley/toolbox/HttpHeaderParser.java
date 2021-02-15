package com.android.volley.toolbox;

import com.android.volley.Cache.Entry;
import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyLog;
import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class HttpHeaderParser {
    private static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";
    static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String RFC1123_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

    static String formatEpochAsRfc1123(long j) {
        return newRfc1123Formatter().format(new Date(j));
    }

    private static SimpleDateFormat newRfc1123Formatter() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat;
    }

    public static Entry parseCacheHeaders(NetworkResponse networkResponse) {
        boolean z;
        long j;
        long j2;
        long j3;
        long j4;
        NetworkResponse networkResponse2 = networkResponse;
        long currentTimeMillis = System.currentTimeMillis();
        Map map = networkResponse2.headers;
        String str = (String) map.get("Date");
        long parseDateAsEpoch = str != null ? parseDateAsEpoch(str) : 0;
        String str2 = (String) map.get(HttpRequest.HEADER_CACHE_CONTROL);
        int i = 0;
        if (str2 != null) {
            String[] split = str2.split(",", 0);
            int i2 = 0;
            j2 = 0;
            j = 0;
            while (i < split.length) {
                String trim = split[i].trim();
                if (trim.equals("no-cache") || trim.equals("no-store")) {
                    return null;
                }
                if (trim.startsWith("max-age=")) {
                    try {
                        j2 = Long.parseLong(trim.substring(8));
                    } catch (Exception unused) {
                    }
                } else if (trim.startsWith("stale-while-revalidate=")) {
                    j = Long.parseLong(trim.substring(23));
                } else if (trim.equals("must-revalidate") || trim.equals("proxy-revalidate")) {
                    i2 = 1;
                }
                i++;
            }
            i = i2;
            z = true;
        } else {
            z = false;
            j2 = 0;
            j = 0;
        }
        String str3 = (String) map.get(HttpRequest.HEADER_EXPIRES);
        long parseDateAsEpoch2 = str3 != null ? parseDateAsEpoch(str3) : 0;
        String str4 = (String) map.get(HttpRequest.HEADER_LAST_MODIFIED);
        long parseDateAsEpoch3 = str4 != null ? parseDateAsEpoch(str4) : 0;
        String str5 = (String) map.get(HttpRequest.HEADER_ETAG);
        if (z) {
            j4 = currentTimeMillis + (j2 * 1000);
            if (i == 0) {
                j3 = (j * 1000) + j4;
                Entry entry = new Entry();
                entry.data = networkResponse2.data;
                entry.etag = str5;
                entry.softTtl = j4;
                entry.ttl = j3;
                entry.serverDate = parseDateAsEpoch;
                entry.lastModified = parseDateAsEpoch3;
                entry.responseHeaders = map;
                entry.allResponseHeaders = networkResponse2.allHeaders;
                return entry;
            }
        } else if (parseDateAsEpoch <= 0 || parseDateAsEpoch2 < parseDateAsEpoch) {
            j4 = 0;
        } else {
            j3 = (parseDateAsEpoch2 - parseDateAsEpoch) + currentTimeMillis;
            j4 = j3;
            Entry entry2 = new Entry();
            entry2.data = networkResponse2.data;
            entry2.etag = str5;
            entry2.softTtl = j4;
            entry2.ttl = j3;
            entry2.serverDate = parseDateAsEpoch;
            entry2.lastModified = parseDateAsEpoch3;
            entry2.responseHeaders = map;
            entry2.allResponseHeaders = networkResponse2.allHeaders;
            return entry2;
        }
        j3 = j4;
        Entry entry22 = new Entry();
        entry22.data = networkResponse2.data;
        entry22.etag = str5;
        entry22.softTtl = j4;
        entry22.ttl = j3;
        entry22.serverDate = parseDateAsEpoch;
        entry22.lastModified = parseDateAsEpoch3;
        entry22.responseHeaders = map;
        entry22.allResponseHeaders = networkResponse2.allHeaders;
        return entry22;
    }

    public static String parseCharset(Map map) {
        return parseCharset(map, "ISO-8859-1");
    }

    public static String parseCharset(Map map, String str) {
        String str2 = (String) map.get("Content-Type");
        if (str2 != null) {
            String[] split = str2.split(";", 0);
            for (int i = 1; i < split.length; i++) {
                String[] split2 = split[i].trim().split("=", 0);
                if (split2.length == 2 && split2[0].equals(HttpRequest.PARAM_CHARSET)) {
                    return split2[1];
                }
            }
        }
        return str;
    }

    public static long parseDateAsEpoch(String str) {
        try {
            return newRfc1123Formatter().parse(str).getTime();
        } catch (ParseException e) {
            VolleyLog.e(e, "Unable to parse dateStr: %s, falling back to 0", str);
            return 0;
        }
    }

    static List toAllHeaderList(Map map) {
        ArrayList arrayList = new ArrayList(map.size());
        for (Map.Entry entry : map.entrySet()) {
            arrayList.add(new Header((String) entry.getKey(), (String) entry.getValue()));
        }
        return arrayList;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.volley.Header>, for r3v0, types: [java.util.List<com.android.volley.Header>, java.util.List] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static Map toHeaderMap(List<Header> list) {
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (Header header : list) {
            treeMap.put(header.getName(), header.getValue());
        }
        return treeMap;
    }
}
