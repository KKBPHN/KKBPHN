package com.miui.internal.server;

import android.app.Application;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.google.android.apps.photos.api.PhotosOemApi;
import com.miui.internal.util.DataUpdateUtils;
import com.miui.internal.util.DirectIndexedFileExtractor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import miui.net.http.HttpSession;
import miui.os.FileUtils;
import miui.security.DigestUtils;
import miui.telephony.phonenumber.ChineseTelocationConverter;
import miui.text.ExtraTextUtils;
import miui.util.AppConstants;
import miui.util.IOUtils;
import miui.util.Patcher;
import miui.util.async.TaskManager;
import miui.util.async.tasks.HttpJsonObjectTask;
import org.json.JSONObject;

public class TelocationUpdateService extends IntentService {
    public static final String ACTION_TELOCATION_DOWNLOAD_COMPLETED = "com.miui.data.TELOCATION_DOWNLOAD_COMPLETED";
    public static final String ACTION_UPDATE_TELOCATION = "com.miui.data.UPDATE_TELOCATION";
    private static final String AES_KEY = "d101b17c77ff93cs";
    private static final String NEW_FILE_NAME = "telocation-new";
    private static final String OLD_FILE_NAME = "telocation-old";
    private static final String PARAM_MD5 = "m";
    private static final String PARAM_TYPE = "t";
    private static final String PARAM_VER = "ver";
    private static final String PATCH_FILE_NAME = "telocation-patch";
    private static final String SECRET = "77eb2e8a5755abd016c0d69ba74b219c";
    private static final String TAG = "TelocationUpdateService";
    private static final int TARGET_FILE_MODE = 436;
    private static final String TELOCATION_IDF_NAME = "telocation.idf";
    private static final int TYPE_TELOCATION = 3;
    private static final String UPDATE_URL;
    private DownloadManager mDownloadManager;
    private long mOldVersion;
    private SharedPreferences mSharedPreferences;

    final class DownloadInfo {
        private static final String KEY_DOWNLOADED_FILE_MD5 = "telocation_downloaded_file_md5";
        private static final String KEY_DOWNLOAD_ID = "telocation_download_id";
        private static final String KEY_NEW_FILE_MD5 = "telocation_new_file_md5";
        private static final String KEY_PATCH_TYPE = "telocation_patch_type";
        public String downloadFileMd5Sum;
        public long downloadId;
        public String newFileMd5Sum;
        public int patchType;

        public DownloadInfo() {
        }

        public DownloadInfo(long j, String str, String str2, int i) {
            this.downloadFileMd5Sum = str;
            this.downloadId = j;
            this.newFileMd5Sum = str2;
            this.patchType = i;
        }

        public static void deleteFromPreferences(SharedPreferences sharedPreferences) {
            sharedPreferences.edit().remove(KEY_DOWNLOAD_ID).remove(KEY_DOWNLOADED_FILE_MD5).remove(KEY_NEW_FILE_MD5).remove(KEY_PATCH_TYPE).commit();
        }

        public static DownloadInfo readFromPreferences(SharedPreferences sharedPreferences) {
            DownloadInfo downloadInfo = new DownloadInfo();
            downloadInfo.downloadId = sharedPreferences.getLong(KEY_DOWNLOAD_ID, -1);
            if (downloadInfo.downloadId == -1) {
                return null;
            }
            downloadInfo.downloadFileMd5Sum = sharedPreferences.getString(KEY_DOWNLOADED_FILE_MD5, null);
            if (downloadInfo.downloadFileMd5Sum == null) {
                return null;
            }
            downloadInfo.newFileMd5Sum = sharedPreferences.getString(KEY_NEW_FILE_MD5, null);
            if (downloadInfo.newFileMd5Sum == null) {
                return null;
            }
            downloadInfo.patchType = sharedPreferences.getInt(KEY_PATCH_TYPE, -1);
            if (downloadInfo.patchType == -1) {
                return null;
            }
            return downloadInfo;
        }

        public void writeToPreferences(SharedPreferences sharedPreferences) {
            sharedPreferences.edit().putLong(KEY_DOWNLOAD_ID, this.downloadId).putString(KEY_DOWNLOADED_FILE_MD5, this.downloadFileMd5Sum).putString(KEY_NEW_FILE_MD5, this.newFileMd5Sum).putInt(KEY_PATCH_TYPE, this.patchType).commit();
        }
    }

    class TelocationUpdateActionResponse {
        public static final int ACTION_NONE = 0;
        public static final int ACTION_UPDATE = 1;
        public static final int PATCH_TYPE_DIFF = 0;
        public static final int PATCH_TYPE_FULL = 1;
        public int action;
        public String description;
        public long fileSize;
        public String fileUrl;
        public String md5Sum;
        public String newMd5Sum;
        public long newVersion;
        public String oldMd5Sum;
        public long oldVersion;
        public int patchType;
        public boolean result;
        public int serviceType;

        private TelocationUpdateActionResponse() {
        }

        public static TelocationUpdateActionResponse fromJson(JSONObject jSONObject) {
            TelocationUpdateActionResponse telocationUpdateActionResponse = new TelocationUpdateActionResponse();
            telocationUpdateActionResponse.result = jSONObject.optBoolean("result");
            telocationUpdateActionResponse.description = jSONObject.optString("description");
            if (telocationUpdateActionResponse.result) {
                telocationUpdateActionResponse.action = jSONObject.optInt("action");
                JSONObject jSONObject2 = jSONObject.getJSONObject("info");
                telocationUpdateActionResponse.serviceType = jSONObject2.optInt("serviceType");
                telocationUpdateActionResponse.patchType = jSONObject2.optInt("patchType");
                telocationUpdateActionResponse.oldVersion = jSONObject2.optLong("oldVersion");
                telocationUpdateActionResponse.newVersion = jSONObject2.optLong("newVersion");
                telocationUpdateActionResponse.oldMd5Sum = jSONObject2.optString("oldMd5Sum");
                telocationUpdateActionResponse.newMd5Sum = jSONObject2.optString("newMd5Sum");
                telocationUpdateActionResponse.md5Sum = jSONObject2.optString("md5Sum");
                telocationUpdateActionResponse.fileUrl = jSONObject2.optString("fileURL");
                telocationUpdateActionResponse.fileSize = jSONObject2.optLong("fileSize");
            }
            return telocationUpdateActionResponse;
        }
    }

    class TelocationUpdateResponse {
        private static final String RESULT_OK = "ok";
        public int code;
        public TelocationUpdateActionResponse data;
        public String description;
        public String result;

        private TelocationUpdateResponse() {
        }

        private static TelocationUpdateResponse fromJson(JSONObject jSONObject) {
            TelocationUpdateResponse telocationUpdateResponse = new TelocationUpdateResponse();
            telocationUpdateResponse.result = jSONObject.optString("result");
            if (RESULT_OK.equals(telocationUpdateResponse.result)) {
                telocationUpdateResponse.code = jSONObject.optInt("code");
                telocationUpdateResponse.description = jSONObject.optString("description");
                String access$000 = TelocationUpdateService.decodeBase64AES(jSONObject.optString(PhotosOemApi.PATH_SPECIAL_TYPE_DATA), TelocationUpdateService.AES_KEY);
                if (!TextUtils.isEmpty(access$000)) {
                    telocationUpdateResponse.data = TelocationUpdateActionResponse.fromJson(new JSONObject(access$000));
                }
            }
            return telocationUpdateResponse;
        }

        public static TelocationUpdateResponse request(String str) {
            HttpJsonObjectTask httpJsonObjectTask = new HttpJsonObjectTask(new HttpSession(), str);
            TaskManager.getDefault().add(httpJsonObjectTask);
            return fromJson(httpJsonObjectTask.doLoad());
        }

        public boolean shouldUpdate() {
            if (RESULT_OK.equals(this.result)) {
                TelocationUpdateActionResponse telocationUpdateActionResponse = this.data;
                if (telocationUpdateActionResponse != null && telocationUpdateActionResponse.result && telocationUpdateActionResponse.action == 1) {
                    return true;
                }
            }
            return false;
        }
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(DataUpdateManager.getBaseUrl());
        sb.append("/cspmisc/patch/info");
        UPDATE_URL = sb.toString();
    }

    public TelocationUpdateService() {
        super(TAG);
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00f8 A[SYNTHETIC, Splitter:B:41:0x00f8] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyUpdate(DownloadInfo downloadInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyUpdate: id=");
        sb.append(downloadInfo.downloadId);
        String sb2 = sb.toString();
        String str = TAG;
        Log.i(str, sb2);
        Query query = new Query();
        query.setFilterById(new long[]{downloadInfo.downloadId});
        Cursor query2 = this.mDownloadManager.query(query);
        if (query2 == null || query2.getCount() <= 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("applyUpdate: Null download info! Download_id=");
            sb3.append(downloadInfo.downloadId);
            Log.w(str, sb3.toString());
            return;
        }
        try {
            query2.moveToFirst();
            int i = query2.getInt(query2.getColumnIndex("status"));
            if (i != 8) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("applyUpdate: Download failed! Status=");
                sb4.append(i);
                Log.e(str, sb4.toString());
                return;
            }
            Log.i(str, "applyUpdate: Download Success!");
            query2.close();
            File file = new File(getCacheDir(), PATCH_FILE_NAME);
            FileInputStream fileInputStream = null;
            try {
                FileInputStream fileInputStream2 = new FileInputStream(this.mDownloadManager.openDownloadedFile(downloadInfo.downloadId).getFileDescriptor());
                try {
                    if (!FileUtils.copyToFile(fileInputStream2, file)) {
                        Log.e(str, "applyUpdate: File copy failed!");
                        try {
                            fileInputStream2.close();
                        } catch (Exception unused) {
                        }
                        return;
                    }
                    try {
                        fileInputStream2.close();
                    } catch (Exception unused2) {
                    }
                    String str2 = downloadInfo.newFileMd5Sum;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("applyUpdate: downloadInfo.patchType=");
                    sb5.append(downloadInfo.patchType);
                    Log.i(str, sb5.toString());
                    int i2 = downloadInfo.patchType;
                    boolean z = i2 == 0 ? updateWithDiffFile(downloadInfo.downloadFileMd5Sum, str2, file.getAbsolutePath()) : i2 == 1 ? updateWithFullFile(downloadInfo.downloadFileMd5Sum, file.getAbsolutePath()) : false;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("applyUpdate: result=");
                    sb6.append(z);
                    Log.i(str, sb6.toString());
                    if (z) {
                        this.mDownloadManager.remove(new long[]{downloadInfo.downloadId});
                        DownloadInfo.deleteFromPreferences(this.mSharedPreferences);
                    }
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception unused3) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                if (fileInputStream != null) {
                }
                throw th;
            }
        } finally {
            query2.close();
        }
    }

    /* access modifiers changed from: private */
    public static String decodeBase64AES(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            byte[] bytes = str2.getBytes();
            if (bytes != null && bytes.length == 16) {
                SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, "AES");
                try {
                    Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    instance.init(2, secretKeySpec, new IvParameterSpec("0102030405060708".getBytes()));
                    byte[] decode = Base64.decode(str, 0);
                    if (decode == null) {
                        return null;
                    }
                    return new String(instance.doFinal(decode));
                } catch (Exception unused) {
                }
            }
        }
        return null;
    }

    private long downloadFile(String str) {
        Request request = new Request(Uri.parse(str));
        request.setAllowedOverMetered(false);
        request.setAllowedOverRoaming(false);
        request.setAllowedNetworkTypes(2);
        request.setNotificationVisibility(2);
        request.setVisibleInDownloadsUi(false);
        return this.mDownloadManager.enqueue(request);
    }

    private static String getMd5Digest(InputStream inputStream) {
        try {
            String hexReadable = ExtraTextUtils.toHexReadable(DigestUtils.get(inputStream, "MD5"));
            if (inputStream == null) {
                return hexReadable;
            }
            try {
                inputStream.close();
                return hexReadable;
            } catch (IOException e) {
                e.printStackTrace();
                return hexReadable;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    private static String getMd5Digest(String str) {
        try {
            return getMd5Digest((InputStream) new FileInputStream(str));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private InputStream getTelocationInputStream() {
        Application currentApplication = AppConstants.getCurrentApplication();
        String str = TELOCATION_IDF_NAME;
        String directIndexedFilePath = DirectIndexedFileExtractor.getDirectIndexedFilePath(currentApplication, str);
        return (directIndexedFilePath == null || !new File(directIndexedFilePath).exists()) ? AppConstants.getCurrentApplication().getResources().getAssets().open(str) : new FileInputStream(new File(directIndexedFilePath));
    }

    private void requestUpdate(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("requestUpdate: ");
        sb.append(z);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        HashMap hashMap = new HashMap(3);
        hashMap.put(PARAM_VER, String.valueOf(this.mOldVersion));
        hashMap.put(PARAM_TYPE, String.valueOf(3));
        String md5Digest = getMd5Digest(getTelocationInputStream());
        if (md5Digest != null) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Local file md5 : ");
            sb3.append(md5Digest);
            Log.v(str, sb3.toString());
            hashMap.put("m", z ? "-1" : md5Digest.toLowerCase());
            StringBuilder sb4 = new StringBuilder();
            sb4.append(UPDATE_URL);
            sb4.append("?");
            sb4.append(DataUpdateUtils.getSignedUrl(hashMap, getPackageName(), SECRET));
            TelocationUpdateResponse request = TelocationUpdateResponse.request(sb4.toString());
            if (request.shouldUpdate()) {
                TelocationUpdateActionResponse telocationUpdateActionResponse = request.data;
                String str2 = telocationUpdateActionResponse.fileUrl;
                String str3 = telocationUpdateActionResponse.md5Sum;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Patch file md5: ");
                sb5.append(str3);
                Log.d(str, sb5.toString());
                DownloadInfo readFromPreferences = DownloadInfo.readFromPreferences(this.mSharedPreferences);
                if (readFromPreferences != null) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("oldDownloadInfo.id= ");
                    sb6.append(readFromPreferences.downloadId);
                    Log.d(str, sb6.toString());
                    this.mDownloadManager.remove(new long[]{readFromPreferences.downloadId});
                }
                long downloadFile = downloadFile(str2);
                StringBuilder sb7 = new StringBuilder();
                sb7.append("newDownloadId= ");
                sb7.append(downloadFile);
                Log.d(str, sb7.toString());
                DownloadInfo downloadInfo = new DownloadInfo(downloadFile, telocationUpdateActionResponse.md5Sum, telocationUpdateActionResponse.newMd5Sum, telocationUpdateActionResponse.patchType);
                downloadInfo.writeToPreferences(this.mSharedPreferences);
            }
        }
    }

    private boolean tryReplaceTelocationFile(String str, String str2) {
        String md5Digest = getMd5Digest(str);
        String str3 = TAG;
        if (md5Digest == null || !md5Digest.equalsIgnoreCase(str2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error: new telocation file md5 mismatch: ");
            sb.append(md5Digest);
            Log.w(str3, sb.toString());
            return false;
        }
        boolean replaceDirectIndexedFile = DirectIndexedFileExtractor.replaceDirectIndexedFile(this, str, TELOCATION_IDF_NAME);
        if (replaceDirectIndexedFile) {
            ChineseTelocationConverter.getInstance().reloadInstance();
        } else {
            new File(str).delete();
            Log.w(str3, "replaceTelocationFile fail");
        }
        return replaceDirectIndexedFile;
    }

    private boolean updateWithDiffFile(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateWithDiffFile: patchFileMd5=");
        sb.append(str);
        sb.append(" newFileMd5=");
        sb.append(str2);
        sb.append(" diffPath=");
        sb.append(str3);
        String sb2 = sb.toString();
        String str4 = TAG;
        Log.d(str4, sb2);
        String md5Digest = getMd5Digest(str3);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("updateWithDiffFile: diffMD5Local=");
        sb3.append(md5Digest);
        Log.d(str4, sb3.toString());
        boolean z = false;
        if (md5Digest == null || !md5Digest.equalsIgnoreCase(str)) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Error: patch file md5 mismatch: ");
            sb4.append(md5Digest);
            Log.d(str4, sb4.toString());
        } else {
            File file = new File(getCacheDir().getAbsolutePath(), OLD_FILE_NAME);
            File file2 = new File(getCacheDir().getAbsolutePath(), NEW_FILE_NAME);
            int i = 3;
            while (true) {
                if (i <= 0) {
                    break;
                }
                InputStream inputStream = null;
                try {
                    inputStream = getTelocationInputStream();
                    FileUtils.copyToFile(inputStream, file);
                    try {
                        if (new Patcher().applyPatch(file.getAbsolutePath(), file2.getAbsolutePath(), str3) == 0) {
                            z = tryReplaceTelocationFile(file2.getAbsolutePath(), str2);
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("Telocation Patch Update from ");
                            sb5.append(this.mOldVersion);
                            sb5.append(" to ");
                            sb5.append(ChineseTelocationConverter.getInstance().getVersion());
                            sb5.append(", result : ");
                            sb5.append(z);
                            Log.v(str4, sb5.toString());
                        } else {
                            Log.w(str4, "updateWithDiffFile: ApplyPatch failed.");
                        }
                        if (z) {
                            Log.d(str4, "updateWithDiffFile: success");
                            break;
                        }
                        i--;
                    } finally {
                        file.delete();
                        file2.delete();
                    }
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
            }
            new File(str3).delete();
        }
        if (!z) {
            try {
                requestUpdate(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    private boolean updateWithFullFile(String str, String str2) {
        boolean tryReplaceTelocationFile = tryReplaceTelocationFile(str2, str);
        StringBuilder sb = new StringBuilder();
        sb.append("Telocation Full Update from ");
        sb.append(this.mOldVersion);
        sb.append(" to ");
        sb.append(ChineseTelocationConverter.getInstance().getVersion());
        sb.append(", result : ");
        sb.append(tryReplaceTelocationFile);
        Log.v(TAG, sb.toString());
        return tryReplaceTelocationFile;
    }

    public void onCreate() {
        super.onCreate();
        this.mDownloadManager = (DownloadManager) getSystemService("download");
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        String str;
        String action = intent.getAction();
        this.mOldVersion = (long) ChineseTelocationConverter.getInstance().getVersion();
        StringBuilder sb = new StringBuilder();
        sb.append("onHandleIntent mOldVersion=");
        sb.append(this.mOldVersion);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.i(str2, sb2);
        boolean z = false;
        if (ACTION_UPDATE_TELOCATION.equals(action)) {
            try {
                requestUpdate(false);
                return;
            } catch (Exception e) {
                e = e;
                str = "Telocation requestUpdate failed";
            }
        } else if (ACTION_TELOCATION_DOWNLOAD_COMPLETED.equals(action)) {
            DownloadInfo readFromPreferences = DownloadInfo.readFromPreferences(this.mSharedPreferences);
            long longExtra = intent.getLongExtra("extra_download_id", 0);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Local download.id=");
            sb3.append(longExtra);
            sb3.append(" downloadInfo=");
            if (readFromPreferences == null) {
                z = true;
            }
            sb3.append(z);
            sb3.append(" | System download.id=");
            sb3.append(longExtra);
            Log.i(str2, sb3.toString());
            if (readFromPreferences == null || longExtra != readFromPreferences.downloadId) {
                Log.i(str2, "download info error!");
                return;
            }
            try {
                applyUpdate(readFromPreferences);
                return;
            } catch (Exception e2) {
                e = e2;
                str = "Telocation applyUpdate failed";
            }
        } else {
            return;
        }
        Log.v(str2, str);
        e.printStackTrace();
    }
}
