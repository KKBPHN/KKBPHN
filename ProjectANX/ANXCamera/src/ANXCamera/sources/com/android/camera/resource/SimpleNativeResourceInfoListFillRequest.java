package com.android.camera.resource;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.text.TextUtils;
import com.android.camera.CameraAppImpl;
import com.android.camera.Util;
import com.android.camera.module.impl.component.FileUtils;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import org.json.JSONObject;

public class SimpleNativeResourceInfoListFillRequest extends BaseObservableRequest {
    private static final String ARCHIVE_PATTERN = ".zip";
    private static final String PREFIX_CLOUD_RESOURCE = "https://";
    private static final String PREFIX_EXTERNAL_RESOURCE = "sdcard/";
    private static final String PREFIX_NATIVE_RESOURCE = "assets://";
    private String mAssetName;
    private String mOutPutFolder;

    public SimpleNativeResourceInfoListFillRequest(String str, String str2) {
        this.mAssetName = str;
        this.mOutPutFolder = str2;
    }

    private String convertStreamToString(InputStream inputStream) {
        Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        return useDelimiter.hasNext() ? useDelimiter.next() : "";
    }

    private void decompressNativeResource(Context context, BaseResourceItem baseResourceItem, String str, boolean z) {
        Util.verifyAssetZip(context, baseResourceItem.uri.split("assets://")[1], str, 32768);
        baseResourceItem.onDecompressFinished(str, true);
    }

    private void decompressSdcardResource(Context context, BaseResourceItem baseResourceItem, String str, String str2, boolean z) {
        String str3 = baseResourceItem.id;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str3);
        sb.append("_");
        sb.append(baseResourceItem.versionCode);
        sb.append(".zip");
        String sb2 = sb.toString();
        if (!new File(sb2).exists()) {
            baseResourceItem.onDecompressFailed(sb2, str2);
            return;
        }
        Util.verifySdcardZip(sb2, str2, 32768);
        baseResourceItem.onDecompressFinished(str2, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0031 A[SYNTHETIC, Splitter:B:23:0x0031] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x003c A[SYNTHETIC, Splitter:B:29:0x003c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String getAssetCache(String str, Context context) {
        InputStream inputStream;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            inputStream = context.getApplicationContext().getAssets().open(str);
            try {
                String convertStreamToString = convertStreamToString(inputStream);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return convertStreamToString;
            } catch (IOException e2) {
                e = e2;
                try {
                    e.printStackTrace();
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    return null;
                } catch (Throwable th) {
                    th = th;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        } catch (IOException e5) {
            e = e5;
            inputStream = null;
            e.printStackTrace();
            if (inputStream != null) {
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            if (inputStream != null) {
            }
            throw th;
        }
    }

    private AssetManager getAssetManager(Context context) {
        Context context2;
        try {
            context2 = context.createPackageContext(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            context2 = null;
        }
        return context2.getAssets();
    }

    public void decompressResource(Context context, BaseResourceList baseResourceList, String str, boolean z) {
        for (BaseResourceItem baseResourceItem : baseResourceList.getResourceList()) {
            String str2 = baseResourceItem.uri;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(baseResourceItem.id);
            sb.append(File.separator);
            String sb2 = sb.toString();
            if (baseResourceItem.simpleVerification(sb2)) {
                boolean versionVerification = baseResourceItem.versionVerification(sb2);
                baseResourceItem.onDecompressFinished(sb2, versionVerification);
                if (versionVerification && z) {
                }
            }
            if (str2.startsWith("assets://")) {
                decompressNativeResource(context, baseResourceItem, sb2, z);
            } else {
                decompressSdcardResource(context, baseResourceItem, str, sb2, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x002a A[SYNTHETIC, Splitter:B:18:0x002a] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0036 A[SYNTHETIC, Splitter:B:25:0x0036] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String getLocalCache(String str) {
        FileInputStream fileInputStream;
        File file = new File(str);
        String str2 = null;
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    str2 = convertStreamToString(fileInputStream);
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e2) {
                    e = e2;
                    try {
                        e.printStackTrace();
                        if (fileInputStream != null) {
                        }
                        return str2;
                    } catch (Throwable th) {
                        th = th;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }
            } catch (FileNotFoundException e4) {
                e = e4;
                fileInputStream = null;
                e.printStackTrace();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
                return str2;
            } catch (Throwable th2) {
                th = th2;
                fileInputStream = null;
                if (fileInputStream != null) {
                }
                throw th;
            }
        }
        return str2;
    }

    /* access modifiers changed from: protected */
    public Scheduler getWorkThread() {
        return Schedulers.io();
    }

    /* access modifiers changed from: protected */
    public void scheduleRequest(ResponseListener responseListener, BaseResourceList baseResourceList) {
        int i;
        String message;
        Context androidContext = CameraAppImpl.getAndroidContext();
        File file = new File(this.mOutPutFolder, this.mAssetName);
        try {
            JSONObject jSONObject = new JSONObject(file.exists() ? getLocalCache(file.getAbsolutePath()) : getAssetCache(this.mAssetName, androidContext));
            baseResourceList.parseInitialData(jSONObject);
            baseResourceList.createResourcesList(jSONObject);
            boolean z = true;
            String str = baseResourceList.version;
            if (!baseResourceList.getLocalVersion().equals(str)) {
                baseResourceList.setLocalVersion(str);
                z = false;
            }
            try {
                FileUtils.makeNoMediaDir(this.mOutPutFolder);
                decompressResource(androidContext, baseResourceList, this.mOutPutFolder, z);
                responseListener.onResponse(baseResourceList, false);
            } catch (IOException e) {
                e.printStackTrace();
                i = 3;
                message = e.getMessage();
                responseListener.onResponseError(i, message, null);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            i = 2;
            message = e2.getMessage();
            responseListener.onResponseError(i, message, null);
        }
    }
}
