package com.miui.internal.util;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import miui.util.Pools;

public class SimpleNumberFormatter {
    private static Locale sLocale = Locale.getDefault();
    private static char sZeroDigit = new DecimalFormatSymbols(sLocale).getZeroDigit();

    private static String convertInt(int i, int i2) {
        StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
        if (i2 < 0) {
            i2 = -i2;
            i--;
            sb.append('-');
        }
        if (i2 >= 10000) {
            String num = Integer.toString(i2);
            for (int length = num.length(); length < i; length++) {
                sb.append('0');
            }
            sb.append(num);
        } else {
            int i3 = i2 >= 1000 ? 4 : i2 >= 100 ? 3 : i2 >= 10 ? 2 : 1;
            while (i3 < i) {
                sb.append('0');
                i3++;
            }
            sb.append(i2);
        }
        String sb2 = sb.toString();
        Pools.getStringBuilderPool().release(sb);
        return sb2;
    }

    public static String format(int i) {
        return format(-1, i);
    }

    public static String format(int i, int i2) {
        char zeroDigit = getZeroDigit(Locale.getDefault());
        String convertInt = convertInt(i, i2);
        return zeroDigit != '0' ? localizeDigits(zeroDigit, convertInt) : convertInt;
    }

    private static char getZeroDigit(Locale locale) {
        if (locale != null) {
            if (!locale.equals(sLocale)) {
                sZeroDigit = new DecimalFormatSymbols(locale).getZeroDigit();
                sLocale = locale;
            }
            return sZeroDigit;
        }
        throw new NullPointerException("locale == null");
    }

    private static String localizeDigits(char c, String str) {
        int length = str.length();
        int i = c - '0';
        StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt >= '0' && charAt <= '9') {
                charAt = (char) (charAt + i);
            }
            sb.append(charAt);
        }
        String sb2 = sb.toString();
        Pools.getStringBuilderPool().release(sb);
        return sb2;
    }
}
