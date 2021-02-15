package com.xiaomi.stat.d;

import android.text.TextUtils;
import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class i {
    public static final int a = 10000;
    public static final int b = 15000;
    private static final String c = "GET";
    private static final String d = "POST";
    private static final String e = "&";
    private static final String f = "=";
    private static final String g = "UTF-8";

    private i() {
    }

    public static String a(String str) {
        return a(str, null, false);
    }

    /* JADX WARNING: type inference failed for: r6v0 */
    /* JADX WARNING: type inference failed for: r6v1, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r1v1, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r6v2 */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r0v6, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r6v3 */
    /* JADX WARNING: type inference failed for: r1v8 */
    /* JADX WARNING: type inference failed for: r0v15 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static String a(String str, String str2, Map map, boolean z) {
        HttpURLConnection httpURLConnection;
        OutputStream outputStream;
        ? r1;
        OutputStream outputStream2;
        String str3;
        String str4;
        OutputStream outputStream3;
        ? r0;
        InputStream inputStream;
        String str5 = "POST";
        String str6 = "GET";
        ? r6 = 0;
        if (map == null) {
            str3 = null;
        } else {
            try {
                str3 = a(map, z);
            } catch (IOException e2) {
                e = e2;
                httpURLConnection = null;
                outputStream3 = 0;
                r1 = outputStream;
                outputStream2 = outputStream;
                try {
                    k.e(String.format("HttpUtil %s failed, url: %s, error: %s", new Object[]{str, str2, e.getMessage()}));
                    j.a((InputStream) r1);
                    j.a(outputStream2);
                    j.a(httpURLConnection);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    r6 = r1;
                    outputStream = outputStream2;
                    j.a((InputStream) r6);
                    j.a(outputStream);
                    j.a(httpURLConnection);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                httpURLConnection = null;
                outputStream = 0;
                j.a((InputStream) r6);
                j.a(outputStream);
                j.a(httpURLConnection);
                throw th;
            }
        }
        if (!str6.equals(str) || str3 == null) {
            str4 = str2;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append("? ");
            sb.append(str3);
            str4 = sb.toString();
        }
        httpURLConnection = (HttpURLConnection) new URL(str4).openConnection();
        try {
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            String str7 = "UTF-8";
            if (str6.equals(str)) {
                httpURLConnection.setRequestMethod(str6);
            } else if (str5.equals(str) && str3 != null) {
                httpURLConnection.setRequestMethod(str5);
                httpURLConnection.setRequestProperty("Content-Type", HttpRequest.CONTENT_TYPE_FORM);
                httpURLConnection.setDoOutput(true);
                byte[] bytes = str3.getBytes(str7);
                outputStream2 = httpURLConnection.getOutputStream();
                try {
                    outputStream2.write(bytes, 0, bytes.length);
                    outputStream2.flush();
                    r0 = outputStream2;
                    int responseCode = httpURLConnection.getResponseCode();
                    inputStream = httpURLConnection.getInputStream();
                    byte[] b2 = j.b(inputStream);
                    k.b(String.format("HttpUtil %s succeed url: %s, code: %s", new Object[]{str, str2, Integer.valueOf(responseCode)}));
                    String str8 = new String(b2, str7);
                    j.a(inputStream);
                    j.a((OutputStream) r0);
                    j.a(httpURLConnection);
                    return str8;
                } catch (IOException e3) {
                    e = e3;
                    r1 = 0;
                    k.e(String.format("HttpUtil %s failed, url: %s, error: %s", new Object[]{str, str2, e.getMessage()}));
                    j.a((InputStream) r1);
                    j.a(outputStream2);
                    j.a(httpURLConnection);
                    return null;
                } catch (Throwable th3) {
                    th = th3;
                    j.a((InputStream) r6);
                    j.a(outputStream);
                    j.a(httpURLConnection);
                    throw th;
                }
            }
            r0 = 0;
            int responseCode2 = httpURLConnection.getResponseCode();
            inputStream = httpURLConnection.getInputStream();
            try {
                byte[] b22 = j.b(inputStream);
                k.b(String.format("HttpUtil %s succeed url: %s, code: %s", new Object[]{str, str2, Integer.valueOf(responseCode2)}));
                String str82 = new String(b22, str7);
                j.a(inputStream);
                j.a((OutputStream) r0);
                j.a(httpURLConnection);
                return str82;
            } catch (IOException e4) {
                e = e4;
                outputStream2 = r0;
                r1 = inputStream;
                k.e(String.format("HttpUtil %s failed, url: %s, error: %s", new Object[]{str, str2, e.getMessage()}));
                j.a((InputStream) r1);
                j.a(outputStream2);
                j.a(httpURLConnection);
                return null;
            }
        } catch (IOException e5) {
            e = e5;
            outputStream3 = 0;
            r1 = outputStream;
            outputStream2 = outputStream;
            k.e(String.format("HttpUtil %s failed, url: %s, error: %s", new Object[]{str, str2, e.getMessage()}));
            j.a((InputStream) r1);
            j.a(outputStream2);
            j.a(httpURLConnection);
            return null;
        } catch (Throwable th4) {
            th = th4;
            outputStream = 0;
            r6 = r6;
            j.a((InputStream) r6);
            j.a(outputStream);
            j.a(httpURLConnection);
            throw th;
        }
    }

    public static String a(String str, Map map) {
        return a(str, map, true);
    }

    public static String a(String str, Map map, boolean z) {
        return a("GET", str, map, z);
    }

    public static String a(Map map) {
        StringBuilder sb = new StringBuilder();
        if (map != null) {
            ArrayList<String> arrayList = new ArrayList<>(map.keySet());
            Collections.sort(arrayList);
            for (String str : arrayList) {
                if (!TextUtils.isEmpty(str)) {
                    sb.append(str);
                    sb.append((String) map.get(str));
                }
            }
        }
        sb.append(ak.c());
        return g.c(sb.toString());
    }

    private static String a(Map map, boolean z) {
        String str;
        String str2;
        String str3;
        StringBuilder sb = new StringBuilder();
        Iterator it = map.entrySet().iterator();
        while (true) {
            try {
                boolean hasNext = it.hasNext();
                str = f;
                str2 = e;
                str3 = "UTF-8";
                if (!hasNext) {
                    break;
                }
                Entry entry = (Entry) it.next();
                if (!TextUtils.isEmpty((CharSequence) entry.getKey())) {
                    if (sb.length() > 0) {
                        sb.append(str2);
                    }
                    sb.append(URLEncoder.encode((String) entry.getKey(), str3));
                    sb.append(str);
                    sb.append(URLEncoder.encode(entry.getValue() == null ? "null" : (String) entry.getValue(), str3));
                }
            } catch (UnsupportedEncodingException unused) {
                k.e("format params failed");
            }
        }
        if (z) {
            String a2 = a(map);
            if (sb.length() > 0) {
                sb.append(str2);
            }
            sb.append(URLEncoder.encode(d.f, str3));
            sb.append(str);
            sb.append(URLEncoder.encode(a2, str3));
        }
        return sb.toString();
    }

    public static String b(String str, Map map) {
        return b(str, map, true);
    }

    public static String b(String str, Map map, boolean z) {
        return a("POST", str, map, z);
    }
}
