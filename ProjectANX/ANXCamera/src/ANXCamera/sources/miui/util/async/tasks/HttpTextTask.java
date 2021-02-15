package miui.util.async.tasks;

import java.nio.charset.Charset;
import java.util.Map;
import miui.net.http.HttpResponse;
import miui.net.http.HttpSession;
import miui.util.IOUtils;
import miui.util.async.tasks.HttpTask.Method;

public abstract class HttpTextTask extends HttpTask {
    public HttpTextTask(String str) {
        this(null, Method.Get, str, null);
    }

    public HttpTextTask(HttpSession httpSession, String str) {
        this(httpSession, Method.Get, str, null);
    }

    public HttpTextTask(HttpSession httpSession, Method method, String str, Map map) {
        super(httpSession, method, str, map);
    }

    static String guessEncoding(byte[] bArr) {
        String str;
        int i = 500;
        if (bArr.length <= 500) {
            i = bArr.length;
        }
        String str2 = "UTF-8";
        String upperCase = new String(bArr, 0, i, Charset.forName(str2)).toUpperCase();
        if (upperCase.indexOf("CONTENT-TYPE") >= 0) {
            int lastIndexOf = upperCase.lastIndexOf(10);
            if (lastIndexOf < 0) {
                lastIndexOf = 0;
            }
            int indexOf = upperCase.indexOf(10);
            if (indexOf < 0) {
                indexOf = upperCase.length();
            }
            str = upperCase.substring(lastIndexOf, indexOf);
        } else {
            str = null;
        }
        if (str != null) {
            String upperCase2 = str.toUpperCase();
            int indexOf2 = upperCase2.indexOf("CHARSET=");
            if (indexOf2 >= 0) {
                StringBuilder sb = new StringBuilder();
                for (int i2 = indexOf2 + 8; i2 < upperCase2.length(); i2++) {
                    char charAt = upperCase2.charAt(i2);
                    if (charAt == '\'' || charAt == '\"' || charAt == ' ') {
                        sb.append(charAt);
                    }
                }
                return sb.toString();
            }
        }
        return str2;
    }

    /* access modifiers changed from: protected */
    public final String requestText() {
        String str;
        HttpResponse request = request();
        String contentEncoding = request.getContentEncoding();
        if (contentEncoding == null || contentEncoding.length() <= 0) {
            byte[] byteArray = IOUtils.toByteArray(request.getContent());
            str = new String(byteArray, Charset.forName(guessEncoding(byteArray)));
        } else {
            str = IOUtils.toString(request.getContent(), contentEncoding.toUpperCase());
        }
        request.release();
        return str;
    }
}
