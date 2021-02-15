package com.bumptech.glide.load.data.mediastore;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

class ThumbnailStreamOpener {
    private static final FileService DEFAULT_SERVICE = new FileService();
    private static final String TAG = "ThumbStreamOpener";
    private final ArrayPool byteArrayPool;
    private final ContentResolver contentResolver;
    private final List parsers;
    private final ThumbnailQuery query;
    private final FileService service;

    ThumbnailStreamOpener(List list, FileService fileService, ThumbnailQuery thumbnailQuery, ArrayPool arrayPool, ContentResolver contentResolver2) {
        this.service = fileService;
        this.query = thumbnailQuery;
        this.byteArrayPool = arrayPool;
        this.contentResolver = contentResolver2;
        this.parsers = list;
    }

    ThumbnailStreamOpener(List list, ThumbnailQuery thumbnailQuery, ArrayPool arrayPool, ContentResolver contentResolver2) {
        this(list, DEFAULT_SERVICE, thumbnailQuery, arrayPool, contentResolver2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0030 A[Catch:{ all -> 0x004a }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Nullable
    private String getPath(@NonNull Uri uri) {
        Cursor cursor;
        String str = TAG;
        try {
            cursor = this.query.query(uri);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        String string = cursor.getString(0);
                        if (cursor != null) {
                            cursor.close();
                        }
                        return string;
                    }
                } catch (SecurityException e) {
                    e = e;
                    try {
                        if (Log.isLoggable(str, 3)) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Failed to query for thumbnail for Uri: ");
                            sb.append(uri);
                            Log.d(str, sb.toString(), e);
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                        return null;
                    } catch (Throwable th) {
                        th = th;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } catch (SecurityException e2) {
            e = e2;
            cursor = null;
            if (Log.isLoggable(str, 3)) {
            }
            if (cursor != null) {
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
            cursor = null;
            if (cursor != null) {
            }
            throw th;
        }
    }

    private boolean isValid(File file) {
        return this.service.exists(file) && 0 < this.service.length(file);
    }

    /* access modifiers changed from: 0000 */
    public int getOrientation(Uri uri) {
        String str = TAG;
        boolean z = null;
        try {
            z = this.contentResolver.openInputStream(uri);
            int orientation = ImageHeaderParserUtils.getOrientation(this.parsers, z, this.byteArrayPool);
            if (z != null) {
                try {
                    z.close();
                } catch (IOException unused) {
                }
            }
            return orientation;
        } catch (IOException | NullPointerException e) {
            z = Log.isLoggable(str, 3);
            if (z) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to open uri: ");
                sb.append(uri);
                Log.d(str, sb.toString(), e);
            }
            if (z != null) {
                try {
                    z.close();
                } catch (IOException unused2) {
                }
            }
            return -1;
        } finally {
            if (z != null) {
                try {
                    z.close();
                } catch (IOException unused3) {
                }
            }
        }
    }

    public InputStream open(Uri uri) {
        String path = getPath(uri);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = this.service.get(path);
        if (!isValid(file)) {
            return null;
        }
        Uri fromFile = Uri.fromFile(file);
        try {
            return this.contentResolver.openInputStream(fromFile);
        } catch (NullPointerException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("NPE opening uri: ");
            sb.append(uri);
            sb.append(" -> ");
            sb.append(fromFile);
            throw ((FileNotFoundException) new FileNotFoundException(sb.toString()).initCause(e));
        }
    }
}
