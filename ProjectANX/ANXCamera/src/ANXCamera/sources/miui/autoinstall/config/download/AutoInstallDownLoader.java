package miui.autoinstall.config.download;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.internal.util.ReflectUtils;

public class AutoInstallDownLoader {
    private DownloadManager mDownloaderManager;

    public AutoInstallDownLoader(Context context) {
        initialize(context);
    }

    private Request createRequest(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str)) {
            return null;
        }
        Request request = new Request(Uri.parse(str));
        request.setAllowedOverRoaming(true);
        request.setDestinationInExternalFilesDir(context, "autoinstall_downloads", str2);
        request.setNotificationVisibility(2);
        try {
            String format = String.format("{\"bypass_recommended_size_limit\":%s}", new Object[]{Boolean.valueOf(true)});
            ReflectUtils.invoke(request, ReflectUtils.getDeclaredMethod(Request.class, "setExtra2", String.class), format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    @TargetApi(9)
    private void initialize(Context context) {
        this.mDownloaderManager = (DownloadManager) context.getSystemService("download");
    }

    public long enqueue(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        Request createRequest = createRequest(context, str, str2);
        if (createRequest == null) {
            return -1;
        }
        return this.mDownloaderManager.enqueue(createRequest);
    }

    public String queryFileDir(long j) {
        Cursor query = this.mDownloaderManager.query(new Query().setFilterById(new long[]{j}));
        return (query == null || !query.moveToFirst()) ? "" : Uri.parse(query.getString(query.getColumnIndex("local_uri"))).getPath();
    }

    public int queryStatus(long j) {
        Query query = new Query();
        Cursor query2 = this.mDownloaderManager.query(query.setFilterById(new long[]{j}));
        if (query2 == null || !query2.moveToFirst()) {
            return -1;
        }
        return query2.getInt(query2.getColumnIndex("status"));
    }
}
