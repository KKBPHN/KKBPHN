package com.android.camera.storage;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import com.android.camera.log.Log;
import com.arcsoft.camera.wideselfie.WideSelfieEngine;
import java.io.File;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

public class MediaProviderUtil {
    public static final String TAG = "MediaProviderUtil";

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    private MediaProviderUtil() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0067, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0068, code lost:
        if (r10 != null) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        $closeResource(r9, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006d, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Uri getContentUriFromPath(Context context, String str) {
        String str2 = "external";
        try {
            Cursor query = context.getContentResolver().query(Files.getContentUri(str2), new String[]{"_id", "media_type"}, "_data = ?", new String[]{str}, "");
            if (query.moveToNext()) {
                long j = (long) query.getInt(0);
                int i = query.getInt(1);
                if (i == 1) {
                    Uri withAppendedId = ContentUris.withAppendedId(Media.getContentUri(str2), j);
                    if (query != null) {
                        $closeResource(null, query);
                    }
                    return withAppendedId;
                } else if (i == 3) {
                    Uri withAppendedId2 = ContentUris.withAppendedId(Video.Media.getContentUri(str2), j);
                    if (query != null) {
                        $closeResource(null, query);
                    }
                    return withAppendedId2;
                } else {
                    Uri withAppendedId3 = ContentUris.withAppendedId(Files.getContentUri(str2), j);
                    if (query != null) {
                        $closeResource(null, query);
                    }
                    return withAppendedId3;
                }
            } else {
                if (query != null) {
                    $closeResource(null, query);
                }
                return null;
            }
        } catch (Exception unused) {
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0040, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0041, code lost:
        if (r8 != null) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        $closeResource(r7, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0046, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static long getParent(Context context, String str) {
        try {
            Cursor query = context.getContentResolver().query(Files.getContentUri("external"), new String[]{"_id"}, "_data = ?", new String[]{new File(str).getParent()}, "");
            if (query.moveToNext()) {
                long j = query.getLong(0);
                if (query != null) {
                    $closeResource(null, query);
                }
                return j;
            }
            if (query != null) {
                $closeResource(null, query);
            }
            return 0;
        } catch (Exception unused) {
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002c, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002d, code lost:
        if (r7 != null) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        $closeResource(r8, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0032, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String getPathFromUri(Context context, Uri uri) {
        try {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            if (query.moveToNext()) {
                String string = query.getString(0);
                if (query != null) {
                    $closeResource(null, query);
                }
                return string;
            }
            if (query != null) {
                $closeResource(null, query);
            }
            return null;
        } catch (Exception unused) {
        }
    }

    public static Uri insertCameraDirectory(Context context, String str) {
        long parent = getParent(context, str);
        if (parent > 0) {
            return insertDirectory(context, str, parent);
        }
        Uri insertDirectory = insertDirectory(context, new File(str).getParent(), 0);
        if (insertDirectory == null) {
            return null;
        }
        return insertDirectory(context, str, ContentUris.parseId(insertDirectory));
    }

    public static Uri insertDirectory(Context context, String str, long j) {
        Uri contentUri = Files.getContentUri("external");
        ContentValues contentValues = new ContentValues();
        contentValues.put(IjkMediaMeta.IJKM_KEY_FORMAT, Integer.valueOf(WideSelfieEngine.HORIZ_FLIP));
        contentValues.put("media_type", Integer.valueOf(0));
        contentValues.put("_data", str);
        contentValues.put("parent", Long.valueOf(j));
        File file = new File(str);
        if (file.exists()) {
            contentValues.put("date_modified", Long.valueOf(file.lastModified() / 1000));
        }
        try {
            return context.getContentResolver().insert(contentUri, contentValues);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("insertDirectory fail, path = ");
            sb.append(str);
            Log.w(TAG, sb.toString(), (Throwable) e);
            return null;
        }
    }
}
