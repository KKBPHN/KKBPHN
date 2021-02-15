package com.android.camera.fragment.subtitle.recog;

import com.android.camera.log.Log;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AuthUtils {
    public static String assembleRequestUrl(String str, String str2, String str3) {
        String str4 = "hmacsha256";
        String str5 = "\n";
        try {
            URL url = new URL(str.replace("ws://", "http://").replace("wss://", "https://"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            String format = simpleDateFormat.format(new Date());
            String host = url.getHost();
            StringBuilder sb = new StringBuilder("host: ");
            sb.append(host);
            sb.append(str5);
            sb.append("date: ");
            sb.append(format);
            sb.append(str5);
            sb.append("GET ");
            sb.append(url.getPath());
            sb.append(" HTTP/1.1");
            Charset forName = Charset.forName("UTF-8");
            Mac instance = Mac.getInstance(str4);
            System.out.println(sb.toString());
            instance.init(new SecretKeySpec(str3.getBytes(forName), str4));
            return String.format("%s?authorization=%s&host=%s&date=%s", new Object[]{str, URLEncoder.encode(Base64.getEncoder().encodeToString(String.format("hmac username=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", new Object[]{str2, "hmac-sha256", "host date request-line", Base64.getEncoder().encodeToString(instance.doFinal(sb.toString().getBytes(forName)))}).getBytes(forName))), URLEncoder.encode(host), URLEncoder.encode(format)});
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("assembleRequestUrl: ");
            sb2.append(e);
            Log.e("AuthUtils", sb2.toString());
            return "";
        }
    }
}
