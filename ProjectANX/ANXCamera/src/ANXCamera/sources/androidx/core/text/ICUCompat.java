package androidx.core.text;

import android.icu.util.ULocale;
import android.os.Build.VERSION;
import android.util.Log;
import androidx.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class ICUCompat {
    private static final String TAG = "ICUCompat";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    static {
        int i = VERSION.SDK_INT;
        String str = "addLikelySubtags";
        if (i < 21) {
            try {
                Class cls = Class.forName("libcore.icu.ICU");
                if (cls != null) {
                    sGetScriptMethod = cls.getMethod("getScript", new Class[]{String.class});
                    sAddLikelySubtagsMethod = cls.getMethod(str, new Class[]{String.class});
                }
            } catch (Exception e) {
                sGetScriptMethod = null;
                sAddLikelySubtagsMethod = null;
                Log.w(TAG, e);
            }
        } else if (i < 24) {
            try {
                sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod(str, new Class[]{Locale.class});
            } catch (Exception e2) {
                throw new IllegalStateException(e2);
            }
        }
    }

    private ICUCompat() {
    }

    private static String addLikelySubtags(Locale locale) {
        String str = TAG;
        String locale2 = locale.toString();
        try {
            if (sAddLikelySubtagsMethod != null) {
                return (String) sAddLikelySubtagsMethod.invoke(null, new Object[]{locale2});
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            Log.w(str, e);
        }
        return locale2;
    }

    private static String getScript(String str) {
        String str2 = TAG;
        try {
            if (sGetScriptMethod != null) {
                return (String) sGetScriptMethod.invoke(null, new Object[]{str});
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            Log.w(str2, e);
        }
        return null;
    }

    @Nullable
    public static String maximizeAndGetScript(Locale locale) {
        String str = TAG;
        int i = VERSION.SDK_INT;
        if (i >= 24) {
            return ULocale.addLikelySubtags(ULocale.forLocale(locale)).getScript();
        }
        if (i >= 21) {
            try {
                r4 = locale;
                r4 = ((Locale) sAddLikelySubtagsMethod.invoke(null, new Object[]{locale})).getScript();
                r4 = r4;
                return r4;
            } catch (IllegalAccessException | InvocationTargetException e) {
                Log.w(str, e);
                return r4.getScript();
            }
        } else {
            String addLikelySubtags = addLikelySubtags(locale);
            if (addLikelySubtags != null) {
                return getScript(addLikelySubtags);
            }
            return null;
        }
    }
}
