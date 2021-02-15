package com.airbnb.lottie.network;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.util.Pair;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.C0086O000Ooo;
import com.airbnb.lottie.C0096O00oOooo;
import com.airbnb.lottie.O00000o.O00000o;
import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipInputStream;

public class O00000Oo {
    private final O000000o O0O00o;
    private final Context appContext;
    private final String url;

    private O00000Oo(Context context, String str) {
        this.appContext = context.getApplicationContext();
        this.url = str;
        this.O0O00o = new O000000o(this.appContext, str);
    }

    private String O000000o(HttpURLConnection httpURLConnection) {
        httpURLConnection.getResponseCode();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                    sb.append(10);
                } else {
                    try {
                        break;
                    } catch (Exception unused) {
                    }
                }
            } catch (Exception e) {
                throw e;
            } catch (Throwable th) {
                try {
                    bufferedReader.close();
                } catch (Exception unused2) {
                }
                throw th;
            }
        }
        bufferedReader.close();
        return sb.toString();
    }

    @Nullable
    private C0086O000Ooo O00000Oo(HttpURLConnection httpURLConnection) {
        C0086O000Ooo o000Ooo;
        FileExtension fileExtension;
        String contentType = httpURLConnection.getContentType();
        if (contentType == null) {
            contentType = HttpRequest.CONTENT_TYPE_JSON;
        }
        if (contentType.contains("application/zip")) {
            O00000o.debug("Handling zip response.");
            fileExtension = FileExtension.ZIP;
            o000Ooo = C0096O00oOooo.O00000Oo(new ZipInputStream(new FileInputStream(this.O0O00o.O000000o(httpURLConnection.getInputStream(), fileExtension))), this.url);
        } else {
            O00000o.debug("Received json response.");
            fileExtension = FileExtension.JSON;
            o000Ooo = C0096O00oOooo.O00000o0((InputStream) new FileInputStream(new File(this.O0O00o.O000000o(httpURLConnection.getInputStream(), fileExtension).getAbsolutePath())), this.url);
        }
        if (o000Ooo.getValue() != null) {
            this.O0O00o.O000000o(fileExtension);
        }
        return o000Ooo;
    }

    public static C0086O000Ooo O00000oo(Context context, String str) {
        return new O00000Oo(context, str).O00o0O00();
    }

    @WorkerThread
    @Nullable
    private C0064O0000o0O Oo0ooOO() {
        Pair fetch = this.O0O00o.fetch();
        if (fetch == null) {
            return null;
        }
        FileExtension fileExtension = (FileExtension) fetch.first;
        InputStream inputStream = (InputStream) fetch.second;
        C0086O000Ooo O00000Oo2 = fileExtension == FileExtension.ZIP ? C0096O00oOooo.O00000Oo(new ZipInputStream(inputStream), this.url) : C0096O00oOooo.O00000o0(inputStream, this.url);
        if (O00000Oo2.getValue() != null) {
            return (C0064O0000o0O) O00000Oo2.getValue();
        }
        return null;
    }

    @WorkerThread
    private C0086O000Ooo Oo0ooOo() {
        try {
            return Oo0ooo0();
        } catch (IOException e) {
            return new C0086O000Ooo((Throwable) e);
        }
    }

    @WorkerThread
    private C0086O000Ooo Oo0ooo0() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fetching ");
        sb.append(this.url);
        O00000o.debug(sb.toString());
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.url).openConnection();
        httpURLConnection.setRequestMethod("GET");
        try {
            httpURLConnection.connect();
            if (httpURLConnection.getErrorStream() == null) {
                if (httpURLConnection.getResponseCode() == 200) {
                    C0086O000Ooo O00000Oo2 = O00000Oo(httpURLConnection);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Completed fetch from network. Success: ");
                    sb2.append(O00000Oo2.getValue() != null);
                    O00000o.debug(sb2.toString());
                    httpURLConnection.disconnect();
                    return O00000Oo2;
                }
            }
            String O000000o2 = O000000o(httpURLConnection);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Unable to fetch ");
            sb3.append(this.url);
            sb3.append(". Failed with ");
            sb3.append(httpURLConnection.getResponseCode());
            sb3.append("\n");
            sb3.append(O000000o2);
            return new C0086O000Ooo((Throwable) new IllegalArgumentException(sb3.toString()));
        } catch (Exception e) {
            return new C0086O000Ooo((Throwable) e);
        } finally {
            httpURLConnection.disconnect();
        }
    }

    @WorkerThread
    public C0086O000Ooo O00o0O00() {
        C0064O0000o0O Oo0ooOO = Oo0ooOO();
        if (Oo0ooOO != null) {
            return new C0086O000Ooo((Object) Oo0ooOO);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Animation for ");
        sb.append(this.url);
        sb.append(" not found in cache. Fetching from network.");
        O00000o.debug(sb.toString());
        return Oo0ooOo();
    }
}
