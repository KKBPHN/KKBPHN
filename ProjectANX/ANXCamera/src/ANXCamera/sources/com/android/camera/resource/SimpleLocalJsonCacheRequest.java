package com.android.camera.resource;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public abstract class SimpleLocalJsonCacheRequest extends BaseObservableRequest {
    private String convertStreamToString(InputStream inputStream) {
        Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        return useDelimiter.hasNext() ? useDelimiter.next() : "";
    }

    /* access modifiers changed from: protected */
    public File getCacheFile(String str, Context context) {
        return new File(context.getCacheDir(), str);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0029 A[SYNTHETIC, Splitter:B:18:0x0029] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0035 A[SYNTHETIC, Splitter:B:25:0x0035] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String getCacheJsonString(String str, Context context) {
        FileInputStream fileInputStream;
        File cacheFile = getCacheFile(str, context);
        String str2 = null;
        if (cacheFile.exists()) {
            try {
                fileInputStream = new FileInputStream(cacheFile);
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

    public abstract boolean isCacheValid(BaseResourceCacheable baseResourceCacheable);

    public abstract void processRestore(BaseResourceCacheable baseResourceCacheable);

    /* access modifiers changed from: protected */
    public void scheduleRequest(ResponseListener responseListener, BaseResourceCacheable baseResourceCacheable) {
        if (!isCacheValid(baseResourceCacheable)) {
            if (responseListener != null) {
                responseListener.onResponse(null, true);
            }
            return;
        }
        try {
            processRestore(baseResourceCacheable);
            responseListener.onResponse(baseResourceCacheable, false);
        } catch (Exception e) {
            e.printStackTrace();
            responseListener.onResponse(null, true);
        }
    }
}
