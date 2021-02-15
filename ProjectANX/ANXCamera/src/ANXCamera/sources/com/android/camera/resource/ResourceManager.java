package com.android.camera.resource;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Deprecated
public class ResourceManager {
    private static final String PREFIX_CLOUD_RESOURCE = "https://";
    private static final String PREFIX_EXTERNAL_RESOURCE = "sdcard/";
    private static final String PREFIX_NATIVE_RESOURCE = "assets://";
    private static ResourceManager sInstance;
    private SparseArray mStoragedResourceList = new SparseArray();

    public interface ResourceListener {
        void onExtraFinished(boolean z);
    }

    private void decompressNativeResource(Context context, BaseResourceItem baseResourceItem, String str, boolean z) {
    }

    private void decompressSdcardResource(Context context, String str, BaseResourceItem baseResourceItem, String str2) {
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x008f A[SYNTHETIC, Splitter:B:30:0x008f] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0095 A[SYNTHETIC, Splitter:B:34:0x0095] */
    /* JADX WARNING: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void decompressZip(String str, String str2) {
        ZipFile zipFile = null;
        try {
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdirs();
            }
            ZipFile zipFile2 = new ZipFile(str);
            try {
                Enumeration entries = zipFile2.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                    String name = zipEntry.getName();
                    if (zipEntry.isDirectory()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append(name);
                        new File(sb.toString()).mkdirs();
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str2);
                        sb2.append(name);
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(sb2.toString()));
                        byte[] bArr = new byte[1024];
                        InputStream inputStream = zipFile2.getInputStream(zipEntry);
                        while (true) {
                            int read = inputStream.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                            fileOutputStream.flush();
                        }
                        fileOutputStream.close();
                        inputStream.close();
                    }
                }
                try {
                    zipFile2.close();
                } catch (IOException unused) {
                }
            } catch (Exception e) {
                e = e;
                zipFile = zipFile2;
                try {
                    e.printStackTrace();
                    if (zipFile == null) {
                    }
                } catch (Throwable th) {
                    th = th;
                    zipFile2 = zipFile;
                    if (zipFile2 != null) {
                        try {
                            zipFile2.close();
                        } catch (IOException unused2) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                if (zipFile2 != null) {
                }
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            if (zipFile == null) {
                zipFile.close();
            }
        }
    }

    public static final void destroy() {
        ResourceManager resourceManager = sInstance;
        if (resourceManager != null) {
            resourceManager.mStoragedResourceList.clear();
            sInstance = null;
        }
    }

    public static ResourceManager getInstance() {
        if (sInstance == null) {
            synchronized (ResourceManager.class) {
                if (sInstance == null) {
                    sInstance = new ResourceManager();
                }
            }
        }
        return sInstance;
    }

    public String convertStreamToString(InputStream inputStream) {
        Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        return useDelimiter.hasNext() ? useDelimiter.next() : "";
    }

    public void decompressResource(Context context, BaseResourceList baseResourceList, String str, boolean z) {
        try {
            for (BaseResourceItem baseResourceItem : baseResourceList.getResourceList()) {
                String str2 = baseResourceItem.uri;
                if (str2.startsWith("assets://")) {
                    decompressNativeResource(context, baseResourceItem, str, z);
                } else if (str2.startsWith(PREFIX_EXTERNAL_RESOURCE)) {
                    decompressSdcardResource(context, baseResourceItem.uri, baseResourceItem, str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mStoragedResourceList.put(baseResourceList.getResourceType(), baseResourceList);
    }

    public void downloadCLoudResource(Context context, BaseResourceItem baseResourceItem, String str) {
    }

    public String getAssetCache(String str, Context context) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return convertStreamToString(context.getApplicationContext().getAssets().open(str));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BaseResourceList getResourceList(int i) {
        return (BaseResourceList) this.mStoragedResourceList.get(i);
    }
}
