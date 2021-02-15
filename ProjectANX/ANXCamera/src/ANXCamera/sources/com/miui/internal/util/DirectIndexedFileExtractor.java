package com.miui.internal.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import miui.os.FileUtils;
import miui.util.DirectIndexedFile;
import miui.util.DirectIndexedFile.Reader;

public class DirectIndexedFileExtractor {
    private static final String IDF_DIR_NAME = "idf";
    public static String IDF_FILES_PATH = null;
    private static final String IDF_FILE_EXT = ".idf";
    private static final String TAG = "DirectIndexedFileExtractor";
    private static final int TARGET_DIR_MODE = 505;
    private static final int TARGET_FILE_MODE = 436;
    private static final String TMP_FILE_NAME_POSTFIX = "-tmp";

    /* JADX WARNING: Can't wrap try/catch for region: R(8:2|3|4|5|(3:7|8|9)|10|11|(1:13)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0027 */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x002d A[Catch:{ NameNotFoundException -> 0x004e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void ensureIdfPath(Context context) {
        if (IDF_FILES_PATH == null) {
            try {
                Context createPackageContext = context.createPackageContext("com.miui.core", 2);
                if (VERSION.SDK_INT >= 24) {
                    createPackageContext = (Context) createPackageContext.getClass().getMethod("createDeviceProtectedStorageContext", new Class[0]).invoke(createPackageContext, new Object[0]);
                }
                if (createPackageContext.getFilesDir() != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(createPackageContext.getFilesDir().getAbsolutePath());
                    sb.append(File.separator);
                    sb.append(IDF_DIR_NAME);
                    IDF_FILES_PATH = sb.toString();
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        String str = IDF_FILES_PATH;
        if (str != null) {
            File file = new File(str);
            if (!file.exists() && file.mkdirs()) {
                FileUtils.chmod(IDF_FILES_PATH, TARGET_DIR_MODE);
                return;
            }
            return;
        }
        Log.w(TAG, "Error: Cannot locate IDF_FILES_PATH");
    }

    /* access modifiers changed from: private */
    public static void extractDirectIndexedFile(Context context, String str) {
        String directIndexedFilePath = getDirectIndexedFilePath(context, str);
        String str2 = TAG;
        if (directIndexedFilePath == null) {
            Log.w(str2, "directIndexedFilePath is null");
            return;
        }
        if (!TextUtils.isEmpty(str)) {
            AssetManager assets = context.getResources().getAssets();
            File file = new File(directIndexedFilePath);
            if (!file.exists() || shouldOverwrite(context, str, directIndexedFilePath)) {
                StringBuilder sb = new StringBuilder();
                sb.append(directIndexedFilePath);
                sb.append(TMP_FILE_NAME_POSTFIX);
                File file2 = new File(sb.toString());
                boolean z = null;
                try {
                    InputStream open = assets.open(str);
                    z = FileUtils.copyToFile(open, file2);
                    file.delete();
                    if (!z || !file2.renameTo(file)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(file2.getAbsolutePath());
                        sb2.append(" copy failed.");
                        Log.w(str2, sb2.toString());
                    } else {
                        FileUtils.chmod(directIndexedFilePath, 436);
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(file.getAbsolutePath());
                        sb3.append(" extraction completed.");
                        Log.v(str2, sb3.toString());
                    }
                    if (open != null) {
                        try {
                            open.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e2) {
                    e2.printStackTrace();
                    if (z != null) {
                        z.close();
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    if (z != null) {
                        z.close();
                    }
                } finally {
                    if (z != null) {
                        try {
                            z.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                }
            } else {
                Log.v(str2, " don't need overwrite");
            }
        }
    }

    public static String getDirectIndexedFilePath(Context context, String str) {
        ensureIdfPath(context);
        if (IDF_FILES_PATH == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(IDF_FILES_PATH);
        sb.append(File.separator);
        sb.append(str);
        return sb.toString();
    }

    public static boolean replaceDirectIndexedFile(Context context, String str, String str2) {
        String directIndexedFilePath = getDirectIndexedFilePath(context, str2);
        boolean isEmpty = TextUtils.isEmpty(str);
        boolean z = false;
        String str3 = TAG;
        if (isEmpty) {
            Log.w(str3, "replaceDirectIndexedFile srcPath is null");
            return false;
        } else if (TextUtils.isEmpty(directIndexedFilePath)) {
            Log.w(str3, "replaceDirectIndexedFile destPath is null");
            return false;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(directIndexedFilePath);
            sb.append(TMP_FILE_NAME_POSTFIX);
            File file = new File(sb.toString());
            File file2 = new File(directIndexedFilePath);
            boolean copyFile = FileUtils.copyFile(new File(str), file);
            if (!file2.delete()) {
                Log.w(str3, String.format("old idf file:%s delete fail", new Object[]{str2}));
            }
            if (!copyFile || !file.renameTo(file2)) {
                Log.w(str3, String.format("%s copy or rename failed , tmp file delete: %s", new Object[]{file.getAbsolutePath(), Boolean.valueOf(file.exists() ? file.delete() : true)}));
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(file2.getAbsolutePath());
                sb2.append(" copy and rename success.");
                Log.v(str3, sb2.toString());
                z = FileUtils.chmod(directIndexedFilePath, 436);
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("replaceDirectIndexedFile result is ");
            sb3.append(z);
            Log.v(str3, sb3.toString());
            return z;
        }
    }

    private static boolean shouldOverwrite(Context context, String str, String str2) {
        boolean z = false;
        try {
            Reader open = DirectIndexedFile.open(context.getAssets().open(str, 1));
            try {
                Reader open2 = DirectIndexedFile.open(str2);
                if (open.getDataVersion() > open2.getDataVersion()) {
                    z = true;
                }
                open.close();
                open2.close();
                return z;
            } catch (IOException e) {
                e.printStackTrace();
                open.close();
                return true;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static void tryExtractDirectIndexedFiles(final Context context) {
        ensureIdfPath(context);
        try {
            final String[] list = context.getAssets().list("");
            new AsyncTask() {
                /* access modifiers changed from: protected */
                public Void doInBackground(Void... voidArr) {
                    String[] strArr;
                    for (String str : list) {
                        if (str.endsWith(DirectIndexedFileExtractor.IDF_FILE_EXT)) {
                            DirectIndexedFileExtractor.extractDirectIndexedFile(context, str);
                        }
                    }
                    return null;
                }
            }.execute(new Void[0]);
        } catch (IOException e) {
            Log.w(TAG, "Error reading asset files, extraction abort");
            e.printStackTrace();
        }
    }
}
