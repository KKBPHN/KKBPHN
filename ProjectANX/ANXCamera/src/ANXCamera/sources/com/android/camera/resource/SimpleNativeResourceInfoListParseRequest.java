package com.android.camera.resource;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import org.json.JSONObject;

public class SimpleNativeResourceInfoListParseRequest extends BaseObservableRequest {
    private String mFilePath;

    public SimpleNativeResourceInfoListParseRequest(String str) {
        this.mFilePath = str;
    }

    private String convertStreamToString(InputStream inputStream) {
        Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        return useDelimiter.hasNext() ? useDelimiter.next() : "";
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
    public void scheduleRequest(ResponseListener responseListener, BaseResourceList baseResourceList) {
        File file = new File(this.mFilePath);
        try {
            baseResourceList.parseInitialData(new JSONObject(file.exists() ? getLocalCache(file.getAbsolutePath()) : null));
            responseListener.onResponse(baseResourceList, false);
        } catch (Exception e) {
            e.printStackTrace();
            responseListener.onResponseError(2, e.getMessage(), null);
        }
    }
}
