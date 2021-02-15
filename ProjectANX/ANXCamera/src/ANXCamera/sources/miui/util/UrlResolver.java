package miui.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Build.VERSION;
import com.android.server.pm.PackageManagerService;
import com.miui.internal.util.UrlResolverHelper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UrlResolver {
    private static Method mResolveIntent;

    private UrlResolver() {
    }

    public static ResolveInfo checkMiuiIntent(Context context, PackageManager packageManager, Intent intent) {
        return checkMiuiIntent(context, true, packageManager, null, intent, null, 0, null, 0);
    }

    public static ResolveInfo checkMiuiIntent(Context context, Object obj, Intent intent, String str, int i, List list, int i2) {
        return checkMiuiIntent(context, false, context.getPackageManager(), obj, intent, str, i, list, i2);
    }

    private static ResolveInfo checkMiuiIntent(Context context, boolean z, PackageManager packageManager, Object obj, Intent intent, String str, int i, List list, int i2) {
        boolean z2;
        ResolveInfo resolveInfo;
        Context context2 = context;
        PackageManager packageManager2 = packageManager;
        Object obj2 = obj;
        Intent intent2 = intent;
        String str2 = str;
        List queryIntentActivities = z ? packageManager2.queryIntentActivities(intent2, 0) : list;
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < queryIntentActivities.size(); i3++) {
            ResolveInfo resolveInfo2 = (ResolveInfo) queryIntentActivities.get(i3);
            if (!resolveInfo2.activityInfo.packageName.equals("com.android.browser") && !resolveInfo2.activityInfo.packageName.equals("com.mi.globalbrowser")) {
                if (!UrlResolverHelper.isWhiteListPackage(resolveInfo2.activityInfo.packageName)) {
                    try {
                        long clearCallingIdentity = Binder.clearCallingIdentity();
                        PackageInfo packageInfo = packageManager2.getPackageInfo(resolveInfo2.activityInfo.packageName, 0);
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                        if (applicationInfo != null) {
                            if ((1 & applicationInfo.flags) != 0) {
                                if (packageInfo.packageName.equals("com.android.chrome")) {
                                }
                            }
                        }
                    } catch (NameNotFoundException unused) {
                    }
                }
                arrayList.add(resolveInfo2);
            }
        }
        if (arrayList.size() == 1) {
            return (ResolveInfo) arrayList.get(0);
        }
        if (arrayList.size() > 1) {
            return null;
        }
        Uri data = intent.getData();
        if (data == null) {
            return null;
        }
        String host = data.getHost();
        if (host == null || !UrlResolverHelper.isMiHost(host)) {
            return null;
        }
        String fallbackParameter = UrlResolverHelper.getFallbackParameter(data);
        if (fallbackParameter == null) {
            return null;
        }
        Uri parse = Uri.parse(fallbackParameter);
        if (UrlResolverHelper.isBrowserFallbackScheme(parse.getScheme())) {
            parse = UrlResolverHelper.getBrowserFallbackUri(fallbackParameter);
            z2 = true;
        } else {
            z2 = false;
        }
        intent2.setData(parse);
        if (!z) {
            if (VERSION.SDK_INT > 20) {
                try {
                    if (mResolveIntent == null) {
                        mResolveIntent = context.getClassLoader().loadClass("com.android.server.pm.PackageManagerService").getDeclaredMethod("resolveIntent", new Class[]{Intent.class, String.class, Integer.TYPE, Integer.TYPE});
                    }
                    resolveInfo = (ResolveInfo) mResolveIntent.invoke(obj2, new Object[]{intent2, str2, Integer.valueOf(i), Integer.valueOf(i2)});
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                resolveInfo = ((PackageManagerService) obj2).resolveIntent(intent2, str2, i, i2);
            }
            return resolveInfo;
        } else if (!z2) {
            return checkMiuiIntent(context, packageManager2, intent2);
        } else {
            context.startActivity(intent2);
            return new ResolveInfo();
        }
    }
}
