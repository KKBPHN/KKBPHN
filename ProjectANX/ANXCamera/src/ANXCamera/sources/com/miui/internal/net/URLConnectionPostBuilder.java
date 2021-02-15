package com.miui.internal.net;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.List;

public class URLConnectionPostBuilder extends URLConnectionBuilder {
    public URLConnectionPostBuilder(String str) {
        super(str);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.miui.internal.net.KeyValuePair>, for r5v0, types: [java.util.List, java.util.List<com.miui.internal.net.KeyValuePair>] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0075 A[SYNTHETIC, Splitter:B:25:0x0075] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0083 A[SYNTHETIC, Splitter:B:30:0x0083] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void post(OutputStream outputStream, List<KeyValuePair> list, String str) {
        String str2 = "URLConnectionBuilder";
        StringBuilder sb = new StringBuilder();
        for (KeyValuePair keyValuePair : list) {
            if (!TextUtils.isEmpty(sb)) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(keyValuePair.getKey(), str));
            sb.append("=");
            sb.append(URLEncoder.encode(keyValuePair.getValue(), str));
        }
        BufferedWriter bufferedWriter = null;
        try {
            BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(outputStream, str));
            try {
                bufferedWriter2.write(sb.toString());
                bufferedWriter2.flush();
                try {
                    bufferedWriter2.close();
                } catch (IOException e) {
                    Log.e(str2, e.getMessage());
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e2) {
                        Log.e(str2, e2.getMessage());
                    }
                }
            } catch (Throwable th) {
                th = th;
                bufferedWriter = bufferedWriter2;
                if (bufferedWriter != null) {
                }
                if (outputStream != null) {
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e3) {
                    Log.e(str2, e3.getMessage());
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e4) {
                    Log.e(str2, e4.getMessage());
                }
            }
            throw th;
        }
    }

    public void post(List list) {
        post(this.urlConnection.getOutputStream(), list, "UTF-8");
    }
}
