package com.xiaomi.stat.d;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.camera.Util;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.TimeZone;

public class m {
    public static final int a = 28;
    private static final String b = "OSUtil";
    private static final String c = "";
    private static Method d;
    private static Class e;
    private static Method f;
    private static Boolean g;

    static {
        try {
            d = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
        } catch (Exception unused) {
        }
        try {
            e = Class.forName("miui.os.Build");
        } catch (Exception unused2) {
        }
        try {
            f = Class.forName("android.provider.MiuiSettings$Secure").getDeclaredMethod("isUserExperienceProgramEnable", new Class[]{ContentResolver.class});
            f.setAccessible(true);
        } catch (Exception unused3) {
        }
    }

    public static String a(int i) {
        try {
            int i2 = i / 60000;
            char c2 = '+';
            if (i2 < 0) {
                c2 = '-';
                i2 = -i2;
            }
            StringBuilder sb = new StringBuilder(9);
            sb.append("GMT");
            sb.append(c2);
            a(sb, i2 / 60);
            sb.append(Util.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            a(sb, i2 % 60);
            return sb.toString();
        } catch (Exception unused) {
            return null;
        }
    }

    public static String a(Context context) {
        String a2 = a("gsm.operator.numeric");
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(a2)) {
            String str = ",";
            String[] split = a2.split(str);
            int length = split.length;
            for (int i = 0; i < length; i++) {
                String str2 = split[i];
                if (!TextUtils.isEmpty(str2) && !"00000".equals(str2)) {
                    if (sb.length() > 0) {
                        sb.append(str);
                    }
                    sb.append(str2);
                }
            }
        }
        String sb2 = sb.toString();
        if (TextUtils.isEmpty(sb2)) {
            sb2 = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperator();
        }
        return sb2 == null ? "" : sb2;
    }

    private static String a(String str) {
        try {
            if (d != null) {
                return String.valueOf(d.invoke(null, new Object[]{str}));
            }
        } catch (Exception e2) {
            StringBuilder sb = new StringBuilder();
            sb.append("getProp failed ex: ");
            sb.append(e2.getMessage());
            k.b(b, sb.toString());
        }
        return null;
    }

    private static void a(StringBuilder sb, int i) {
        String num = Integer.toString(i);
        for (int i2 = 0; i2 < 2 - num.length(); i2++) {
            sb.append('0');
        }
        sb.append(num);
    }

    public static boolean a() {
        Boolean bool = g;
        if (bool != null) {
            return bool.booleanValue();
        }
        g = Boolean.valueOf(!TextUtils.isEmpty(a("ro.miui.ui.version.code")));
        return g.booleanValue();
    }

    public static String b() {
        return "Android";
    }

    public static boolean b(Context context) {
        Method method = f;
        if (method == null) {
            return true;
        }
        try {
            return ((Boolean) method.invoke(null, new Object[]{context.getContentResolver()})).booleanValue();
        } catch (Exception e2) {
            StringBuilder sb = new StringBuilder();
            sb.append("isUserExperiencePlanEnabled failed: ");
            sb.append(e2.toString());
            Log.e(b, sb.toString());
            return true;
        }
    }

    public static String c() {
        return VERSION.RELEASE;
    }

    public static String d() {
        return VERSION.INCREMENTAL;
    }

    public static String e() {
        try {
            return TimeZone.getDefault().getDisplayName(false, 0);
        } catch (AssertionError e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return a(TimeZone.getDefault().getRawOffset());
    }

    public static String f() {
        Locale locale = Locale.getDefault();
        StringBuilder sb = new StringBuilder();
        sb.append(locale.getLanguage());
        sb.append("_");
        sb.append(locale.getCountry());
        return sb.toString();
    }

    public static String g() {
        String a2 = a("ro.miui.region");
        if (TextUtils.isEmpty(a2)) {
            a2 = Locale.getDefault().getCountry();
        }
        return a2 == null ? "" : a2;
    }

    public static String h() {
        Class cls = e;
        if (cls != null) {
            try {
                if (((Boolean) cls.getField("IS_ALPHA_BUILD").get(null)).booleanValue()) {
                    return "A";
                }
                if (((Boolean) e.getField("IS_DEVELOPMENT_VERSION").get(null)).booleanValue()) {
                    return "D";
                }
                if (((Boolean) e.getField("IS_STABLE_VERSION").get(null)).booleanValue()) {
                    return "S";
                }
            } catch (Exception e2) {
                StringBuilder sb = new StringBuilder();
                sb.append("getRomBuildCode failed: ");
                sb.append(e2.toString());
                Log.e(b, sb.toString());
            }
        }
        return "";
    }

    public static boolean i() {
        Class cls = e;
        if (cls != null) {
            try {
                return ((Boolean) cls.getField("IS_INTERNATIONAL_BUILD").get(null)).booleanValue();
            } catch (Exception unused) {
            }
        }
        return false;
    }
}
