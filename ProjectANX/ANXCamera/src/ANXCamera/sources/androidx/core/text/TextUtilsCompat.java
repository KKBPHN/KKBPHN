package androidx.core.text;

import android.os.Build.VERSION;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Locale;

public final class TextUtilsCompat {
    private static final String ARAB_SCRIPT_SUBTAG = "Arab";
    private static final String HEBR_SCRIPT_SUBTAG = "Hebr";
    private static final Locale ROOT;

    static {
        String str = "";
        ROOT = new Locale(str, str);
    }

    private TextUtilsCompat() {
    }

    private static int getLayoutDirectionFromFirstChar(@NonNull Locale locale) {
        byte directionality = Character.getDirectionality(locale.getDisplayName(locale).charAt(0));
        return (directionality == 1 || directionality == 2) ? 1 : 0;
    }

    public static int getLayoutDirectionFromLocale(@Nullable Locale locale) {
        if (VERSION.SDK_INT >= 17) {
            return TextUtils.getLayoutDirectionFromLocale(locale);
        }
        if (locale != null && !locale.equals(ROOT)) {
            String maximizeAndGetScript = ICUCompat.maximizeAndGetScript(locale);
            if (maximizeAndGetScript == null) {
                return getLayoutDirectionFromFirstChar(locale);
            }
            if (maximizeAndGetScript.equalsIgnoreCase(ARAB_SCRIPT_SUBTAG) || maximizeAndGetScript.equalsIgnoreCase(HEBR_SCRIPT_SUBTAG)) {
                return 1;
            }
        }
        return 0;
    }

    @NonNull
    public static String htmlEncode(@NonNull String str) {
        String str2;
        if (VERSION.SDK_INT >= 17) {
            return TextUtils.htmlEncode(str);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == '\"') {
                str2 = "&quot;";
            } else if (charAt == '<') {
                str2 = "&lt;";
            } else if (charAt == '>') {
                str2 = "&gt;";
            } else if (charAt == '&') {
                str2 = "&amp;";
            } else if (charAt != '\'') {
                sb.append(charAt);
            } else {
                str2 = "&#39;";
            }
            sb.append(str2);
        }
        return sb.toString();
    }
}
