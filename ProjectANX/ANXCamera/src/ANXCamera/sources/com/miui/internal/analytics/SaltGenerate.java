package com.miui.internal.analytics;

import android.util.Base64;
import com.miui.internal.net.KeyValuePair;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import miui.security.DigestUtils;

public class SaltGenerate {
    private static final String SALT_P1 = "8007236f-";
    private static final String SALT_P2 = "a2d6-4847-ac83-";
    private static final String SALT_P3 = "c49395ad6d65";

    private SaltGenerate() {
    }

    private static byte[] getBytes(String str) {
        try {
            r1 = str;
            r1 = str.getBytes("UTF-8");
            r1 = r1;
            return r1;
        } catch (UnsupportedEncodingException unused) {
            return r1.getBytes();
        }
    }

    public static String getKeyFromParams(List list) {
        Collections.sort(list, new Comparator() {
            public int compare(KeyValuePair keyValuePair, KeyValuePair keyValuePair2) {
                return keyValuePair.getKey().compareTo(keyValuePair2.getKey());
            }
        });
        StringBuilder sb = new StringBuilder();
        Iterator it = list.iterator();
        boolean z = true;
        while (true) {
            String str = "&";
            if (it.hasNext()) {
                KeyValuePair keyValuePair = (KeyValuePair) it.next();
                if (!z) {
                    sb.append(str);
                }
                sb.append(keyValuePair.getKey());
                sb.append("=");
                sb.append(keyValuePair.getValue());
                z = false;
            } else {
                sb.append(str);
                sb.append(SALT_P1);
                sb.append(SALT_P2);
                sb.append(SALT_P3);
                return String.format("%1$032X", new Object[]{new BigInteger(1, DigestUtils.get((CharSequence) Base64.encodeToString(getBytes(sb.toString()), 2), "MD5"))});
            }
        }
    }
}
