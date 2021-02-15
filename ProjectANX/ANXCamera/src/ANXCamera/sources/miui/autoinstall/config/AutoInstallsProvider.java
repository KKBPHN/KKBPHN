package miui.autoinstall.config;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.miui.internal.R;
import java.util.List;
import miui.autoinstall.config.entity.LocalAppInfo;
import miui.autoinstall.config.pm.PackageManagerCompat;

public class AutoInstallsProvider extends ContentProvider {
    private static final String TAG = "AutoInstallsProvider";

    private String loadInstallList() {
        List<LocalAppInfo> loadSystemAppInfoLocal = new PackageManagerCompat(getContext()).loadSystemAppInfoLocal();
        StringBuilder sb = new StringBuilder();
        if (loadSystemAppInfoLocal == null || loadSystemAppInfoLocal.isEmpty()) {
            return "";
        }
        for (LocalAppInfo localAppInfo : loadSystemAppInfoLocal) {
            if (localAppInfo != null && !TextUtils.isEmpty(localAppInfo.packageName)) {
                sb.append(localAppInfo.packageName);
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public boolean onCreate() {
        return true;
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"packageName", "configName", "installList"});
        String resourceName = getContext().getResources().getResourceName(R.xml.default_auto_install);
        matrixCursor.addRow(new String[]{"com.miui.core", resourceName, loadInstallList()});
        StringBuilder sb = new StringBuilder();
        sb.append("config resource name:");
        sb.append(resourceName);
        Log.d(TAG, sb.toString());
        return matrixCursor;
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }
}
