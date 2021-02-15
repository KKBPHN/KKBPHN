package com.iqiyi.android.qigsaw.core.common;

import android.os.Build;
import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.Arrays;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public class AbiUtil {
    private static List abis = null;
    private static final String armv5 = "armeabi";
    private static final String armv7 = "armeabi-v7a";
    private static final String armv8 = "arm64-v8a";
    private static final String x86 = "x86";
    private static final String x86_64 = "x86_64";

    public static String findBasePrimaryAbi(@Nullable List list) {
        List<String> supportedAbis = getSupportedAbis();
        if (list == null) {
            return (String) supportedAbis.get(0);
        }
        for (String str : supportedAbis) {
            if (list.contains(str)) {
                return str;
            }
        }
        throw new RuntimeException("No supported abi for this device.");
    }

    public static String findSplitPrimaryAbi(@NonNull String str, @NonNull List list) {
        if (list.contains(str)) {
            return str;
        }
        String str2 = armv8;
        if (str.contains(str2)) {
            if (!list.contains(str2)) {
                str2 = null;
            }
            return str2;
        }
        String str3 = x86_64;
        if (str.contains(str3)) {
            if (!list.contains(str3)) {
                str3 = null;
            }
            return str3;
        }
        String str4 = x86;
        boolean contains = str.contains(str4);
        String str5 = armv5;
        if (!contains) {
            String str6 = armv7;
            if (str.contains(str6)) {
                if (list.contains(str6)) {
                    return str6;
                }
                if (list.contains(str5)) {
                    return str5;
                }
            } else if (str.contains(str5)) {
                if (list.contains(str5)) {
                    return str5;
                }
                if (!getSupportedAbis().contains(str6) || !list.contains(str6)) {
                    return null;
                }
                return str6;
            }
        } else if (list.contains(str4)) {
            return str4;
        } else {
            if (list.contains(str5)) {
                return str5;
            }
        }
        return null;
    }

    private static List getSupportedAbis() {
        List list = abis;
        if (list != null) {
            return list;
        }
        abis = Arrays.asList(VERSION.SDK_INT >= 21 ? Build.SUPPORTED_ABIS : new String[]{Build.CPU_ABI, Build.CPU_ABI2});
        return abis;
    }
}
