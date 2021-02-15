package miui.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class ResourceHelper {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final String TAG = "ResourceHelper";

    private ResourceHelper() {
    }

    private static int getConfigResource(Context context, String str, String str2, String... strArr) {
        Bundle metaData = getMetaData(context);
        if (metaData == null) {
            Log.w(TAG, "Fail to load meta data");
        } else {
            int i = metaData.getInt(str);
            if (i > 0) {
                return i;
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            String str3 = "xml";
            int identifier = context.getResources().getIdentifier(str2, str3, context.getPackageName());
            if (identifier > 0) {
                return identifier;
            }
            for (String identifier2 : strArr) {
                int identifier3 = context.getResources().getIdentifier(str2, str3, identifier2);
                if (identifier3 > 0) {
                    return identifier3;
                }
            }
        }
        return -1;
    }

    private static Bundle getMetaData(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        if (applicationInfo.metaData == null) {
            try {
                applicationInfo.metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
            } catch (NameNotFoundException e) {
                Log.e(TAG, "Fail to load config for application not found", e);
            }
        }
        return applicationInfo.metaData;
    }

    public static XmlResourceParser loadXml(Context context, String str, String str2) {
        return loadXml(context, str, str2, EMPTY_STRING_ARRAY);
    }

    public static XmlResourceParser loadXml(Context context, String str, String str2, String... strArr) {
        int configResource = getConfigResource(context, str, str2, strArr);
        if (configResource > 0) {
            return context.getResources().getXml(configResource);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fail to load config for resource id is ");
        sb.append(configResource);
        Log.i(TAG, sb.toString());
        return null;
    }
}
