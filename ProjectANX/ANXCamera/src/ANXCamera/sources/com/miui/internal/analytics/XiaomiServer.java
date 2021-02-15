package com.miui.internal.analytics;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.miui.internal.net.KeyValuePair;
import com.miui.internal.util.DeviceHelper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import miui.util.SoftReferenceSingleton;
import org.json.JSONObject;

public class XiaomiServer {
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public XiaomiServer createInstance() {
            return new XiaomiServer();
        }
    };
    private static final String SERVER_URL = "http://tracking.miui.com/tracks";
    private static final String SERVER_URL_GLOBAL = "https://tracking.miui.com/tracks";
    private static final String SIGNITURE = "s";
    private static final String SUBJECT = "miui_apps";
    private static final String TAG = "XIAOMI_SERVER";
    private static final String TYPE = "t";
    private static final String VALUE = "value";

    private XiaomiServer() {
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.miui.internal.net.KeyValuePair>, for r4v0, types: [java.util.List, java.util.List<com.miui.internal.net.KeyValuePair>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String fromParamListToString(List<KeyValuePair> list) {
        String str = "UTF-8";
        StringBuffer stringBuffer = new StringBuffer();
        if (list != null) {
            for (KeyValuePair keyValuePair : list) {
                try {
                    if (keyValuePair.getValue() != null) {
                        stringBuffer.append(URLEncoder.encode(keyValuePair.getKey(), str));
                        stringBuffer.append("=");
                        stringBuffer.append(URLEncoder.encode(keyValuePair.getValue(), str));
                        stringBuffer.append("&");
                    }
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed to convert from param list to string: ");
                    sb.append(e.toString());
                    String sb2 = sb.toString();
                    String str2 = TAG;
                    Log.i(str2, sb2);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("pair: ");
                    sb3.append(keyValuePair.toString());
                    Log.i(str2, sb3.toString());
                    return null;
                }
            }
        }
        if (stringBuffer.length() > 0) {
            stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    public static XiaomiServer getInstance() {
        return (XiaomiServer) INSTANCE.get();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:14|(2:27|28)|29|30|31) */
    /* JADX WARNING: Can't wrap try/catch for region: R(8:15|16|17|(2:19|20)|21|22|23|25) */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:0|1|2|(2:3|(1:5)(1:33))|6|(2:8|9)|10|11|12) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0024 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0033 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x003d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String readResponseContent(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr, 0, 1024);
                if (read <= 0) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            String str = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
            if (inputStream != null) {
                inputStream.close();
            }
            byteArrayOutputStream.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            if (inputStream != null) {
                inputStream.close();
            }
            byteArrayOutputStream.close();
            return null;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            byteArrayOutputStream.close();
        }
    }

    public void close() {
    }

    public void init() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00c3, code lost:
        if (r1 != null) goto L_0x00c5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00d8, code lost:
        if (r1 != null) goto L_0x00c5;
     */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00d1 A[Catch:{ UnsupportedEncodingException -> 0x00d2, IOException -> 0x00c9, Exception -> 0x00bd, all -> 0x00ba }] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00de A[SYNTHETIC, Splitter:B:58:0x00de] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean send(Map map) {
        HttpURLConnection httpURLConnection;
        HttpURLConnection httpURLConnection2;
        OutputStream outputStream;
        String str = TAG;
        boolean z = false;
        if (map == null) {
            return false;
        }
        HttpURLConnection httpURLConnection3 = null;
        try {
            String encodeToString = Base64.encodeToString(new JSONObject(map).toString().getBytes(), 2);
            httpURLConnection = (HttpURLConnection) new URL(DeviceHelper.IS_INTERNATIONAL_BUILD ? SERVER_URL_GLOBAL : SERVER_URL).openConnection();
            try {
                LinkedList linkedList = new LinkedList();
                linkedList.add(new KeyValuePair(TYPE, SUBJECT));
                linkedList.add(new KeyValuePair("value", encodeToString));
                linkedList.add(new KeyValuePair(SIGNITURE, SaltGenerate.getKeyFromParams(linkedList)));
                httpURLConnection.setRequestMethod("POST");
                String fromParamListToString = fromParamListToString(linkedList);
                if (!TextUtils.isEmpty(fromParamListToString)) {
                    httpURLConnection.setDoOutput(true);
                    byte[] bytes = fromParamListToString.getBytes();
                    outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(bytes, 0, bytes.length);
                    outputStream.flush();
                    outputStream.close();
                }
                if (httpURLConnection.getResponseCode() == 200) {
                    String string = new JSONObject(readResponseContent(httpURLConnection.getInputStream())).getString("status");
                    if (string.equals("ok")) {
                        z = true;
                    } else {
                        Log.i(str, string);
                    }
                }
                if (httpURLConnection != null) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception unused) {
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e = e;
                HttpURLConnection httpURLConnection4 = httpURLConnection;
                Log.e(str, "UnsupportedEncodingException catched when sending data", e);
            } catch (IOException e2) {
                e = e2;
                httpURLConnection3 = httpURLConnection;
                Log.e(str, "IOException catched when sending data", e);
                if (httpURLConnection3 != null) {
                    httpURLConnection3.disconnect();
                }
                return z;
            } catch (Exception e3) {
                e = e3;
                httpURLConnection3 = httpURLConnection;
                Log.e(str, "Exception catched when sending data", e);
            } catch (Throwable th) {
                th = th;
                if (httpURLConnection != null) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception unused2) {
                    }
                }
                throw th;
            }
        } catch (UnsupportedEncodingException e4) {
            e = e4;
            Log.e(str, "UnsupportedEncodingException catched when sending data", e);
        } catch (IOException e5) {
            e = e5;
            Log.e(str, "IOException catched when sending data", e);
            if (httpURLConnection3 != null) {
            }
            return z;
        } catch (Exception e6) {
            e = e6;
            Log.e(str, "Exception catched when sending data", e);
        } catch (Throwable th2) {
            th = th2;
            httpURLConnection = httpURLConnection2;
            if (httpURLConnection != null) {
            }
            throw th;
        }
        return z;
    }
}
