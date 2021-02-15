package com.android.camera;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.storage.HDR10Thumbnail.HDR10ThumbnailUtil;
import com.android.camera.storage.Storage;
import com.android.gallery3d.exif.ExifInterface;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Thumbnail {
    private static final int BUFSIZE = 4096;
    private static final int FILE_VERSION = 1;
    private static final String LAST_THUMB_FILENAME = "last_thumb";
    private static final int RETRY_CREATE_THUMBNAIL_INTERVAL_TIME = 20;
    private static final long RETRY_CREATE_THUMBNAIL_TIME = 2000;
    private static final String TAG = "Thumbnail";
    public static final int THUMBNAIL_DELETED = 2;
    public static final int THUMBNAIL_FAIL_FROM_FILE = 3;
    public static final int THUMBNAIL_FOUND = 1;
    public static final int THUMBNAIL_NOT_FOUND = 0;
    public static final int THUMBNAIL_USE_FROM_FILE = -1;
    private static Object sLock = new Object();
    private int gifHeight = -1;
    private int gifWidth = -1;
    private Bitmap mBitmap;
    private boolean mFromFile = false;
    private boolean mIsGif = false;
    private Uri mUri;
    private boolean mWaitingForUri = false;

    class Media {
        public final long dateTaken;
        public final int height;
        public final long id;
        public final boolean isGif;
        public final int orientation;
        public final String path;
        public final Uri uri;
        public final int width;

        public Media(long j, int i, long j2, Uri uri2, String str, int i2, int i3) {
            this(j, i, j2, uri2, str, i2, i3, false);
        }

        public Media(long j, int i, long j2, Uri uri2, String str, int i2, int i3, boolean z) {
            this.id = j;
            this.orientation = i;
            this.dateTaken = j2;
            this.uri = uri2;
            this.path = str;
            this.width = i2;
            this.height = i3;
            this.isGif = z;
        }
    }

    private Thumbnail(Uri uri, Bitmap bitmap, int i, boolean z) {
        this.mUri = uri;
        this.mBitmap = adjustImage(bitmap, i, z);
    }

    private static Bitmap adjustImage(Bitmap bitmap, int i, boolean z) {
        int i2;
        int i3;
        String str = "Failed to rotate thumbnail";
        String str2 = TAG;
        if (i == 0 && !z && bitmap.getWidth() == bitmap.getHeight()) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        if (i % 180 != 0) {
            i3 = bitmap.getHeight();
            i2 = bitmap.getWidth();
        } else {
            i3 = bitmap.getWidth();
            i2 = bitmap.getHeight();
        }
        matrix.postTranslate(((float) (-bitmap.getWidth())) / 2.0f, ((float) (-bitmap.getHeight())) / 2.0f);
        matrix.postRotate((float) i);
        float f = ((float) i3) / 2.0f;
        float f2 = ((float) i2) / 2.0f;
        matrix.postTranslate(f, f2);
        matrix.postScale(z ? -1.0f : 1.0f, 1.0f, f, f2);
        int min = Math.min(i3, i2);
        matrix.postTranslate(((float) (min - i3)) / 2.0f, ((float) (min - i2)) / 2.0f);
        Bitmap bitmap2 = null;
        try {
            bitmap2 = Bitmap.createBitmap(min, min, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap2);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            canvas.drawBitmap(bitmap, matrix, paint);
            bitmap.recycle();
        } catch (Exception | OutOfMemoryError e) {
            Log.w(str2, str, e);
        }
        return bitmap2;
    }

    public static Bitmap createBitmap(byte[] bArr, int i, boolean z, int i2) {
        String str = TAG;
        Options options = new Options();
        options.inSampleSize = i2;
        options.inPurgeable = true;
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        int i3 = i % m.cQ;
        if (decodeByteArray != null && (i3 != 0 || z)) {
            Matrix matrix = new Matrix();
            Matrix matrix2 = new Matrix();
            if (i3 != 0) {
                matrix.setRotate((float) i3, ((float) decodeByteArray.getWidth()) * 0.5f, ((float) decodeByteArray.getHeight()) * 0.5f);
            }
            if (z) {
                matrix2.setScale(-1.0f, 1.0f, ((float) decodeByteArray.getWidth()) * 0.5f, ((float) decodeByteArray.getHeight()) * 0.5f);
                matrix.postConcat(matrix2);
            }
            try {
                Log.d(str, "createBitmap:createBitmap start ");
                Bitmap createBitmap = Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix, true);
                Log.d(str, "createBitmap: createBitmap end");
                if (createBitmap != decodeByteArray) {
                    decodeByteArray.recycle();
                }
                return createBitmap;
            } catch (Exception e) {
                Log.w(str, "Failed to rotate thumbnail", (Throwable) e);
            }
        }
        return decodeByteArray;
    }

    public static Thumbnail createThumbnail(Uri uri, Bitmap bitmap, int i, boolean z) {
        if (bitmap != null) {
            return new Thumbnail(uri, bitmap, i, z);
        }
        Log.e(TAG, "Failed to create thumbnail from null bitmap");
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x001c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Thumbnail createThumbnail(byte[] bArr, int i, int i2, Uri uri, boolean z) {
        Bitmap bitmap;
        if (11 <= i2) {
            ExifInterface exifInterface = new ExifInterface();
            try {
                exifInterface.readExif(bArr);
                bitmap = exifInterface.getThumbnailBitmap();
            } catch (IOException e) {
                Log.e(TAG, "failed to extract thumbnail from exif", (Throwable) e);
            }
            if (bitmap == null) {
                Options options = new Options();
                options.inSampleSize = i2;
                options.inPurgeable = true;
                bitmap = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            }
            return createThumbnail(uri, bitmap, i, z);
        }
        bitmap = null;
        if (bitmap == null) {
        }
        return createThumbnail(uri, bitmap, i, z);
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00b2  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ec  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Thumbnail createThumbnailFromUri(Context context, Uri uri, boolean z) {
        int i;
        int i2;
        long j;
        String str;
        int i3;
        boolean z2;
        int i4;
        Bitmap bitmap;
        boolean z3;
        Uri uri2 = uri;
        if (!(uri2 == null || uri.getPath() == null)) {
            boolean contains = uri.getPath().contains("/images/media");
            String str2 = "height";
            String str3 = "width";
            String str4 = "_data";
            String str5 = "_id";
            Cursor query = context.getContentResolver().query(uri, contains ? new String[]{str5, str4, "orientation", str3, str2} : new String[]{str5, str4, str3, str2, "resolution"}, null, null, null);
            int i5 = 0;
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        long j2 = query.getLong(0);
                        str = query.getString(1);
                        int i6 = contains ? query.getInt(2) : 0;
                        int i7 = contains ? query.getInt(3) : query.getInt(2);
                        int i8 = contains ? query.getInt(4) : query.getInt(3);
                        if ((i7 == 0 || i8 == 0) && !contains) {
                            String string = query.getString(4);
                            if (string != null) {
                                String[] split = string.split("x");
                                if (split.length == 2) {
                                    i2 = i6;
                                    i = Integer.parseInt(split[1]);
                                    j = j2;
                                    z2 = true;
                                    i3 = Integer.parseInt(split[0]);
                                    if (query != null) {
                                        query.close();
                                    }
                                    if (z2) {
                                        if (contains) {
                                            String fileTitleFromPath = Util.getFileTitleFromPath(str);
                                            if (VERSION.SDK_INT <= 28 || TextUtils.isEmpty(fileTitleFromPath)) {
                                                Context context2 = context;
                                            } else if (fileTitleFromPath.startsWith(context.getString(R.string.pano_file_name_prefix))) {
                                                i4 = i2;
                                                bitmap = getMiniKindThumbnailByP(context, j, i2, str, null);
                                                if (bitmap == null) {
                                                    bitmap = ThumbnailUtils.createImageThumbnail(str, 1);
                                                }
                                                if (bitmap == null && str != null && str.endsWith(Storage.HEIC_SUFFIX)) {
                                                    bitmap = getScaledBitmapFromFile(str, i3, i, i4);
                                                }
                                            }
                                            i4 = i2;
                                            bitmap = Thumbnails.getThumbnail(context.getContentResolver(), j, 1, null);
                                            if (bitmap == null) {
                                            }
                                            bitmap = getScaledBitmapFromFile(str, i3, i, i4);
                                        } else {
                                            i4 = i2;
                                            bitmap = createVideoThumbnailBitmap(str, i3, i);
                                            if (bitmap == null) {
                                                bitmap = ThumbnailUtils.createVideoThumbnail(str, 1);
                                            }
                                        }
                                        if (VERSION.SDK_INT > 28) {
                                            z3 = z;
                                        } else {
                                            z3 = z;
                                            i5 = i4;
                                        }
                                        return createThumbnail(uri2, bitmap, i5, z3);
                                    }
                                }
                            }
                        }
                        i2 = i6;
                        i = i8;
                        j = j2;
                        z2 = true;
                        i3 = i7;
                        if (query != null) {
                        }
                        if (z2) {
                        }
                    }
                } catch (Throwable th) {
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            j = -1;
            z2 = false;
            i3 = 0;
            i2 = 0;
            i = 0;
            str = null;
            if (query != null) {
            }
            if (z2) {
            }
        }
        return null;
    }

    public static Bitmap createVideoThumbnailBitmap(FileDescriptor fileDescriptor, int i, int i2) {
        return CompatibilityUtils.createVideoThumbnailBitmap(null, fileDescriptor, i, i2);
    }

    public static Bitmap createVideoThumbnailBitmap(String str, int i, int i2) {
        return CompatibilityUtils.createVideoThumbnailBitmap(str, null, i, i2);
    }

    private static String getImageBucketIds() {
        StringBuilder sb;
        if (Storage.secondaryStorageMounted()) {
            sb = new StringBuilder();
            sb.append("bucket_id IN (");
            sb.append(Storage.PRIMARY_BUCKET_ID);
            sb.append(",");
            sb.append(Storage.SECONDARY_BUCKET_ID);
            sb.append(")");
        } else {
            sb = new StringBuilder();
            sb.append("bucket_id=");
            sb.append(Storage.BUCKET_ID);
        }
        return sb.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x01cc, code lost:
        if (r2 == null) goto L_0x01d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x01ce, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x01e4, code lost:
        if (r2 != null) goto L_0x01ce;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x01e7, code lost:
        return null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0139 A[Catch:{ Exception -> 0x0131, all -> 0x012d }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x01c6  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x01c9  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x01e1  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01ec  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x01f1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Media getLastImageThumbnail(ContentResolver contentResolver) {
        Cursor cursor;
        Cursor cursor2;
        Cursor cursor3;
        Cursor cursor4;
        boolean z;
        String str = TAG;
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri build = uri.buildUpon().appendQueryParameter("limit", "1").build();
        String[] strArr = {"_id", "orientation", "datetaken", "_data", "width", "height"};
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append("(");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("mime_type='image/jpeg'");
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb4);
        String str2 = " OR ";
        sb5.append(str2);
        String sb6 = sb5.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(sb6);
        sb7.append("mime_type='image/heic'");
        String sb8 = sb7.toString();
        StringBuilder sb9 = new StringBuilder();
        sb9.append(sb8);
        sb9.append(str2);
        String sb10 = sb9.toString();
        StringBuilder sb11 = new StringBuilder();
        sb11.append(sb10);
        sb11.append("mime_type='image/gif'");
        String sb12 = sb11.toString();
        StringBuilder sb13 = new StringBuilder();
        sb13.append(sb12);
        sb13.append(") AND ");
        String sb14 = sb13.toString();
        StringBuilder sb15 = new StringBuilder();
        sb15.append(sb14);
        sb15.append(getImageBucketIds());
        sb15.append(" AND ");
        sb15.append("_size");
        sb15.append(" > 0");
        String sb16 = sb15.toString();
        String str3 = "datetaken DESC,_id DESC";
        try {
            cursor = contentResolver.query(build, strArr, sb16, null, str3);
            String str4 = ".gif";
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        String string = cursor.getString(3);
                        if (TextUtils.isEmpty(string) || !new File(string).exists()) {
                            Log.d(str, "getLastImageThumbnail first file is deleted");
                            z = true;
                            if (!z) {
                                String str5 = str4;
                                cursor2 = contentResolver.query(uri, strArr, sb16, null, str3);
                                if (cursor2 != null) {
                                    try {
                                        StringBuilder sb17 = new StringBuilder();
                                        sb17.append("getLastImageThumbnail count=");
                                        sb17.append(cursor2.getCount());
                                        Log.d(str, sb17.toString());
                                        while (cursor2.moveToNext()) {
                                            String string2 = cursor2.getString(3);
                                            if (!TextUtils.isEmpty(string2) && new File(string2).exists()) {
                                                long j = cursor2.getLong(0);
                                                long j2 = j;
                                                String str6 = string2;
                                                Media media = new Media(j2, cursor2.getInt(1), cursor2.getLong(2), ContentUris.withAppendedId(uri, j), str6, cursor.getInt(4), cursor.getInt(5), string2.substring(string2.lastIndexOf(46)).equalsIgnoreCase(str5));
                                                if (cursor != null) {
                                                    cursor.close();
                                                }
                                                if (cursor2 != null) {
                                                    cursor2.close();
                                                }
                                                return media;
                                            }
                                        }
                                    } catch (Exception e) {
                                        e = e;
                                        cursor3 = cursor;
                                        try {
                                            Log.w(str, "getLastImageThumbnail error", (Throwable) e);
                                            if (cursor3 != null) {
                                            }
                                        } catch (Throwable th) {
                                            th = th;
                                            cursor = cursor3;
                                            if (cursor != null) {
                                                cursor.close();
                                            }
                                            if (cursor2 != null) {
                                                cursor2.close();
                                            }
                                            throw th;
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        if (cursor != null) {
                                        }
                                        if (cursor2 != null) {
                                        }
                                        throw th;
                                    }
                                }
                            } else {
                                cursor2 = null;
                            }
                            if (cursor != null) {
                                cursor.close();
                            }
                        } else {
                            long j3 = cursor.getLong(0);
                            long j4 = j3;
                            String str7 = string;
                            Media media2 = new Media(j4, cursor.getInt(1), cursor.getLong(2), ContentUris.withAppendedId(uri, j3), str7, cursor.getInt(4), cursor.getInt(5), string.substring(string.lastIndexOf(46)).equalsIgnoreCase(str4));
                            if (cursor != null) {
                                cursor.close();
                            }
                            return media2;
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    cursor3 = cursor;
                    cursor4 = null;
                    Log.w(str, "getLastImageThumbnail error", (Throwable) e);
                    if (cursor3 != null) {
                    }
                } catch (Throwable th3) {
                    th = th3;
                    cursor2 = null;
                    if (cursor != null) {
                    }
                    if (cursor2 != null) {
                    }
                    throw th;
                }
            }
            z = false;
            if (!z) {
            }
            if (cursor != null) {
            }
        } catch (Exception e3) {
            e = e3;
            cursor4 = null;
            cursor3 = null;
            Log.w(str, "getLastImageThumbnail error", (Throwable) e);
            if (cursor3 != null) {
                cursor3.close();
            }
        } catch (Throwable th4) {
            th = th4;
            cursor2 = null;
            cursor = null;
            if (cursor != null) {
            }
            if (cursor2 != null) {
            }
            throw th;
        }
    }

    public static int getLastThumbnailFromContentResolver(Context context, Thumbnail[] thumbnailArr, Uri uri) {
        Bitmap bitmap;
        Bitmap bitmap2;
        Uri uri2 = uri;
        ContentResolver contentResolver = context.getContentResolver();
        Media lastImageThumbnail = getLastImageThumbnail(contentResolver);
        Media lastVideoThumbnail = getLastVideoThumbnail(contentResolver);
        if (lastImageThumbnail == null && lastVideoThumbnail == null) {
            return 0;
        }
        String str = TAG;
        if (lastImageThumbnail == null || (lastVideoThumbnail != null && lastImageThumbnail.dateTaken < lastVideoThumbnail.dateTaken)) {
            if (uri2 != null && uri2.equals(lastVideoThumbnail.uri)) {
                return -1;
            }
            Bitmap thumbnail = Video.Thumbnails.getThumbnail(contentResolver, lastVideoThumbnail.id, 1, null);
            if (thumbnail == null) {
                Bitmap bitmap3 = thumbnail;
                int i = 0;
                while (true) {
                    if (((long) i) >= RETRY_CREATE_THUMBNAIL_TIME) {
                        break;
                    }
                    try {
                        bitmap3 = ThumbnailUtils.createVideoThumbnail(lastVideoThumbnail.path, 1);
                        if (bitmap3 != null) {
                            break;
                        }
                        Thread.sleep(20);
                        i += 20;
                    } catch (Exception e) {
                        Log.e(str, "exception in createVideoThumbnail", (Throwable) e);
                    }
                }
                bitmap2 = bitmap3;
            } else {
                bitmap2 = thumbnail;
            }
            lastImageThumbnail = lastVideoThumbnail;
        } else if (uri2 != null && uri2.equals(lastImageThumbnail.uri)) {
            return -1;
        } else {
            String str2 = lastImageThumbnail.path;
            String fileTitleFromPath = Util.getFileTitleFromPath(str2);
            Bitmap thumbnail2 = (VERSION.SDK_INT <= 28 || TextUtils.isEmpty(fileTitleFromPath) || !fileTitleFromPath.startsWith(context.getString(R.string.pano_file_name_prefix))) ? Thumbnails.getThumbnail(contentResolver, lastImageThumbnail.id, 1, null) : getMiniKindThumbnailByP(context, lastImageThumbnail.id, lastImageThumbnail.orientation, str2, null);
            bitmap = thumbnail2;
            if (bitmap == null) {
                try {
                    bitmap = ThumbnailUtils.createImageThumbnail(str2, 1);
                } catch (Exception e2) {
                    Log.e(str, "exception in createImageThumbnail", (Throwable) e2);
                }
            }
            if (bitmap == null && str2 != null && str2.endsWith(Storage.HEIC_SUFFIX)) {
                bitmap = getScaledBitmapFromFile(str2, lastImageThumbnail.width, lastImageThumbnail.height, lastImageThumbnail.orientation);
            }
        }
        if (!Util.isUriValid(lastImageThumbnail.uri, contentResolver)) {
            return 2;
        }
        if (bitmap == null) {
            return 3;
        }
        thumbnailArr[0] = createThumbnail(lastImageThumbnail.uri, bitmap, VERSION.SDK_INT > 28 ? 0 : lastImageThumbnail.orientation, false);
        if (lastVideoThumbnail != null && lastImageThumbnail.id == lastVideoThumbnail.id && Util.isHDR10Video(lastImageThumbnail.path)) {
            thumbnailArr[0].setBitmap(HDR10ThumbnailUtil.getHdr10Bitmap(null, thumbnailArr[0].getBitmap()));
        }
        if (!(lastImageThumbnail == lastVideoThumbnail || thumbnailArr[0] == null || !lastImageThumbnail.isGif)) {
            thumbnailArr[0].setIsGif(true);
            thumbnailArr[0].setGifSize(lastImageThumbnail.width, lastImageThumbnail.height);
        }
        return 1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00eb, code lost:
        r13 = createThumbnail(r4, r14, 0, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ef, code lost:
        if (r13 == null) goto L_0x00fa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00f1, code lost:
        r13.setIsGif(r6);
        r13.setGifSize(r8, r9);
        r13.setFromFile(true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00fa, code lost:
        return r13;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Thumbnail getLastThumbnailFromFile(File file, ContentResolver contentResolver) {
        DataInputStream dataInputStream;
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream;
        int i;
        int i2;
        boolean z;
        File file2 = new File(file, LAST_THUMB_FILENAME);
        synchronized (sLock) {
            try {
                fileInputStream = new FileInputStream(file2);
                try {
                    bufferedInputStream = new BufferedInputStream(fileInputStream, 4096);
                    try {
                        dataInputStream = new DataInputStream(bufferedInputStream);
                    } catch (SQLiteException | IOException e) {
                        e = e;
                        dataInputStream = null;
                        String str = TAG;
                        try {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Fail to load bitmap. ");
                            sb.append(e);
                            Log.i(str, sb.toString());
                            Util.closeSilently(fileInputStream);
                            Util.closeSilently(bufferedInputStream);
                            Util.closeSilently(dataInputStream);
                            return null;
                        } catch (Throwable th) {
                            th = th;
                            Util.closeSilently(fileInputStream);
                            Util.closeSilently(bufferedInputStream);
                            Util.closeSilently(dataInputStream);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        dataInputStream = null;
                        Util.closeSilently(fileInputStream);
                        Util.closeSilently(bufferedInputStream);
                        Util.closeSilently(dataInputStream);
                        throw th;
                    }
                    try {
                        Uri parse = Uri.parse(dataInputStream.readUTF());
                        if (parse == null) {
                            Util.closeSilently(fileInputStream);
                            Util.closeSilently(bufferedInputStream);
                            Util.closeSilently(dataInputStream);
                            return null;
                        }
                        try {
                            String queryParameter = parse.getQueryParameter("version");
                            if (queryParameter == null) {
                                queryParameter = "0";
                            }
                            i = Integer.parseInt(queryParameter);
                        } catch (Exception unused) {
                            i = 0;
                        }
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("getLastThumbnailFromFile: version: ");
                        sb2.append(i);
                        Log.d(str2, sb2.toString());
                        int i3 = -1;
                        if (i == 1) {
                            z = dataInputStream.readBoolean();
                            String str3 = TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("getLastThumbnailFromFile: isGif:");
                            sb3.append(dataInputStream.readBoolean());
                            Log.d(str3, sb3.toString());
                            i3 = dataInputStream.readInt();
                            String str4 = TAG;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("getLastThumbnailFromFile: gifWidth:");
                            sb4.append(dataInputStream.readInt());
                            Log.d(str4, sb4.toString());
                            i2 = dataInputStream.readInt();
                            String str5 = TAG;
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("getLastThumbnailFromFile: gifHeight:");
                            sb5.append(dataInputStream.readInt());
                            Log.d(str5, sb5.toString());
                        } else {
                            z = false;
                            i2 = -1;
                        }
                        Uri build = parse.buildUpon().clearQuery().build();
                        if (!Util.isUriValid(build, contentResolver)) {
                            dataInputStream.close();
                            Util.closeSilently(fileInputStream);
                            Util.closeSilently(bufferedInputStream);
                            Util.closeSilently(dataInputStream);
                            return null;
                        }
                        Bitmap decodeStream = BitmapFactory.decodeStream(dataInputStream);
                        dataInputStream.close();
                        Util.closeSilently(fileInputStream);
                        Util.closeSilently(bufferedInputStream);
                        Util.closeSilently(dataInputStream);
                    } catch (SQLiteException | IOException e2) {
                        e = e2;
                        String str6 = TAG;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("Fail to load bitmap. ");
                        sb6.append(e);
                        Log.i(str6, sb6.toString());
                        Util.closeSilently(fileInputStream);
                        Util.closeSilently(bufferedInputStream);
                        Util.closeSilently(dataInputStream);
                        return null;
                    }
                } catch (SQLiteException | IOException e3) {
                    e = e3;
                    bufferedInputStream = null;
                    dataInputStream = null;
                    String str62 = TAG;
                    StringBuilder sb62 = new StringBuilder();
                    sb62.append("Fail to load bitmap. ");
                    sb62.append(e);
                    Log.i(str62, sb62.toString());
                    Util.closeSilently(fileInputStream);
                    Util.closeSilently(bufferedInputStream);
                    Util.closeSilently(dataInputStream);
                    return null;
                } catch (Throwable th3) {
                    th = th3;
                    bufferedInputStream = null;
                    dataInputStream = null;
                    Util.closeSilently(fileInputStream);
                    Util.closeSilently(bufferedInputStream);
                    Util.closeSilently(dataInputStream);
                    throw th;
                }
            } catch (SQLiteException | IOException e4) {
                e = e4;
                bufferedInputStream = null;
                fileInputStream = null;
                dataInputStream = null;
                String str622 = TAG;
                StringBuilder sb622 = new StringBuilder();
                sb622.append("Fail to load bitmap. ");
                sb622.append(e);
                Log.i(str622, sb622.toString());
                Util.closeSilently(fileInputStream);
                Util.closeSilently(bufferedInputStream);
                Util.closeSilently(dataInputStream);
                return null;
            } catch (Throwable th4) {
                th = th4;
                bufferedInputStream = null;
                fileInputStream = null;
                dataInputStream = null;
                Util.closeSilently(fileInputStream);
                Util.closeSilently(bufferedInputStream);
                Util.closeSilently(dataInputStream);
                throw th;
            }
        }
    }

    public static int getLastThumbnailFromUriList(Context context, Thumbnail[] thumbnailArr, ArrayList arrayList, Uri uri) {
        if (!(arrayList == null || arrayList.size() == 0)) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                Uri uri2 = (Uri) arrayList.get(size);
                if (Util.isUriValid(uri2, context.getContentResolver())) {
                    if (uri != null && uri.equals(uri2)) {
                        return -1;
                    }
                    thumbnailArr[0] = createThumbnailFromUri(context, uri2, false);
                    if (thumbnailArr[0] != null) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    public static Uri getLastThumbnailUri(ContentResolver contentResolver) {
        Media lastImageThumbnail = getLastImageThumbnail(contentResolver);
        Media lastVideoThumbnail = getLastVideoThumbnail(contentResolver);
        if (lastImageThumbnail != null && (lastVideoThumbnail == null || lastImageThumbnail.dateTaken >= lastVideoThumbnail.dateTaken)) {
            return lastImageThumbnail.uri;
        }
        if (lastVideoThumbnail == null || (lastImageThumbnail != null && lastVideoThumbnail.dateTaken < lastImageThumbnail.dateTaken)) {
            return null;
        }
        return lastVideoThumbnail.uri;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0127, code lost:
        if (r2 == null) goto L_0x0141;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0129, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x013e, code lost:
        if (r2 != null) goto L_0x0129;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0141, code lost:
        return null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00ac A[Catch:{ Exception -> 0x00a5, all -> 0x00a1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0124  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x013b  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0145  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x014a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Media getLastVideoThumbnail(ContentResolver contentResolver) {
        Cursor cursor;
        Cursor cursor2;
        Cursor cursor3;
        boolean z;
        String str = TAG;
        Uri uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Uri build = uri.buildUpon().appendQueryParameter("limit", "1").build();
        String[] strArr = {"_id", "_data", "datetaken", "width", "height"};
        StringBuilder sb = new StringBuilder();
        sb.append(getVideoBucketIds());
        sb.append(" AND ");
        sb.append("_size");
        sb.append(" > 0");
        String sb2 = sb.toString();
        String str2 = "datetaken DESC,_id DESC";
        try {
            cursor = contentResolver.query(build, strArr, sb2, null, str2);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        long j = cursor.getLong(0);
                        if (cursor.getString(1) == null || !new File(cursor.getString(1)).exists()) {
                            Log.d(str, "getLastVideoThumbnail first file is deleted");
                            z = true;
                            if (!z) {
                                cursor3 = contentResolver.query(uri, strArr, sb2, null, str2);
                                try {
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("getLastVideoThumbnail count=");
                                    sb3.append(cursor3.getCount());
                                    Log.d(str, sb3.toString());
                                    if (cursor3 != null) {
                                        while (cursor3.moveToNext()) {
                                            if (cursor3.getString(1) != null && new File(cursor3.getString(1)).exists()) {
                                                long j2 = cursor3.getLong(0);
                                                Media media = new Media(j2, 0, cursor3.getLong(2), ContentUris.withAppendedId(uri, j2), cursor3.getString(1), cursor.getInt(3), cursor.getInt(4));
                                                if (cursor != null) {
                                                    cursor.close();
                                                }
                                                if (cursor3 != null) {
                                                    cursor3.close();
                                                }
                                                return media;
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e = e;
                                    try {
                                        Log.w(str, "getLastVideoThumbnail error", (Throwable) e);
                                        if (cursor != null) {
                                            cursor.close();
                                        }
                                    } catch (Throwable th) {
                                        th = th;
                                        if (cursor != null) {
                                            cursor.close();
                                        }
                                        if (cursor2 != null) {
                                            cursor2.close();
                                        }
                                        throw th;
                                    }
                                }
                            } else {
                                cursor3 = null;
                            }
                            if (cursor != null) {
                                cursor.close();
                            }
                        } else {
                            Media media2 = new Media(j, 0, cursor.getLong(2), ContentUris.withAppendedId(uri, j), cursor.getString(1), cursor.getInt(3), cursor.getInt(4));
                            if (cursor != null) {
                                cursor.close();
                            }
                            return media2;
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    cursor3 = null;
                    Log.w(str, "getLastVideoThumbnail error", (Throwable) e);
                    if (cursor != null) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor2 = null;
                    if (cursor != null) {
                    }
                    if (cursor2 != null) {
                    }
                    throw th;
                }
            }
            z = false;
            if (!z) {
            }
            if (cursor != null) {
            }
        } catch (Exception e3) {
            e = e3;
            cursor3 = null;
            cursor = null;
            Log.w(str, "getLastVideoThumbnail error", (Throwable) e);
            if (cursor != null) {
            }
        } catch (Throwable th3) {
            th = th3;
            cursor2 = null;
            cursor = null;
            if (cursor != null) {
            }
            if (cursor2 != null) {
            }
            throw th;
        }
    }

    /* JADX WARNING: type inference failed for: r13v1 */
    /* JADX WARNING: type inference failed for: r2v1, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r2v2, types: [android.graphics.Bitmap] */
    /* JADX WARNING: type inference failed for: r2v3, types: [android.graphics.Bitmap] */
    /* JADX WARNING: type inference failed for: r2v4, types: [android.graphics.Bitmap] */
    /* JADX WARNING: type inference failed for: r13v2, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r10v3 */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r2v6 */
    /* JADX WARNING: type inference failed for: r10v4 */
    /* JADX WARNING: type inference failed for: r2v12, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r13v3 */
    /* JADX WARNING: type inference failed for: r10v5 */
    /* JADX WARNING: type inference failed for: r13v4 */
    /* JADX WARNING: type inference failed for: r13v5 */
    /* JADX WARNING: type inference failed for: r2v13 */
    /* JADX WARNING: type inference failed for: r13v6 */
    /* JADX WARNING: type inference failed for: r13v7 */
    /* JADX WARNING: type inference failed for: r13v8 */
    /* JADX WARNING: type inference failed for: r13v9 */
    /* JADX WARNING: type inference failed for: r13v10 */
    /* JADX WARNING: type inference failed for: r13v11, types: [android.graphics.Bitmap] */
    /* JADX WARNING: type inference failed for: r13v12 */
    /* JADX WARNING: type inference failed for: r2v14 */
    /* JADX WARNING: type inference failed for: r2v15 */
    /* JADX WARNING: type inference failed for: r13v13 */
    /* JADX WARNING: type inference failed for: r13v14 */
    /* JADX WARNING: type inference failed for: r13v15 */
    /* JADX WARNING: type inference failed for: r13v16 */
    /* JADX WARNING: type inference failed for: r13v17 */
    /* JADX WARNING: type inference failed for: r13v18 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r13v4
  assigns: [?[OBJECT, ARRAY], ?[int, float, boolean, short, byte, char, OBJECT, ARRAY], android.graphics.Bitmap]
  uses: [?[int, boolean, OBJECT, ARRAY, byte, short, char], android.database.Cursor, ?[OBJECT, ARRAY]]
  mth insns count: 107
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00dc  */
    /* JADX WARNING: Unknown variable types count: 10 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Bitmap getMiniKindThumbnailByP(Context context, long j, int i, String str, Options options) {
        ? r2;
        ? createBitmap;
        ? r13;
        ? r10;
        ? r132;
        ? r133;
        Uri uri;
        String sb;
        ? r134;
        String str2 = "; ";
        String str3 = TAG;
        ContentResolver contentResolver = context.getContentResolver();
        Uri mediaUri = Storage.getMediaUri(context, false, str);
        ? r135 = 0;
        try {
            ? query = contentResolver.query(mediaUri.buildUpon().appendQueryParameter("blocking", "1").appendQueryParameter("orig_id", String.valueOf(j)).appendQueryParameter("group_id", String.valueOf(0)).build(), null, null, null, null);
            if (query == 0) {
                if (query != 0) {
                    query.close();
                }
                return null;
            }
            try {
                if (query.moveToFirst()) {
                    try {
                        uri = ContentUris.withAppendedId(mediaUri, j);
                        try {
                            r135 = r135;
                            ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
                            r135 = BitmapFactory.decodeFileDescriptor(openFileDescriptor.getFileDescriptor(), null, options);
                            openFileDescriptor.close();
                            r135 = r135;
                        } catch (IOException e) {
                            e = e;
                            r133 = r135;
                        } catch (OutOfMemoryError e2) {
                            e = e2;
                            r132 = r135;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("failed to allocate memory for thumbnail ");
                            sb2.append(uri);
                            sb2.append(str2);
                            sb2.append(e);
                            sb = sb2.toString();
                            r134 = r132;
                            Log.e(str3, sb);
                            r135 = r134;
                            if (query != 0) {
                            }
                            createBitmap = r135;
                            int width = r2.getWidth();
                            int height = r2.getHeight();
                            Matrix matrix = new Matrix();
                            matrix.setRotate((float) i, (float) (width / 2), (float) (height / 2));
                            ? r22 = Bitmap.createBitmap(r22, 0, 0, width, height, matrix, false);
                            return r22;
                        }
                    } catch (IOException e3) {
                        e = e3;
                        uri = null;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("couldn't open thumbnail ");
                        sb3.append(uri);
                        sb3.append(str2);
                        sb3.append(e);
                        sb = sb3.toString();
                        r134 = r133;
                        Log.e(str3, sb);
                        r135 = r134;
                        if (query != 0) {
                        }
                        createBitmap = r135;
                        int width2 = r22.getWidth();
                        int height2 = r22.getHeight();
                        Matrix matrix2 = new Matrix();
                        matrix2.setRotate((float) i, (float) (width2 / 2), (float) (height2 / 2));
                        ? r222 = Bitmap.createBitmap(r222, 0, 0, width2, height2, matrix2, false);
                        return r222;
                    } catch (OutOfMemoryError e4) {
                        e = e4;
                        uri = null;
                        StringBuilder sb22 = new StringBuilder();
                        sb22.append("failed to allocate memory for thumbnail ");
                        sb22.append(uri);
                        sb22.append(str2);
                        sb22.append(e);
                        sb = sb22.toString();
                        r134 = r132;
                        Log.e(str3, sb);
                        r135 = r134;
                        if (query != 0) {
                        }
                        createBitmap = r135;
                        int width22 = r222.getWidth();
                        int height22 = r222.getHeight();
                        Matrix matrix22 = new Matrix();
                        matrix22.setRotate((float) i, (float) (width22 / 2), (float) (height22 / 2));
                        ? r2222 = Bitmap.createBitmap(r2222, 0, 0, width22, height22, matrix22, false);
                        return r2222;
                    }
                }
                if (query != 0) {
                    query.close();
                }
                createBitmap = r135;
            } catch (SQLiteException e5) {
                e = e5;
                r10 = r132;
                r13 = query;
                try {
                    Log.w(str3, (Throwable) e);
                    if (r13 != 0) {
                        r13.close();
                    }
                    createBitmap = r10;
                    int width222 = r2222.getWidth();
                    int height222 = r2222.getHeight();
                    Matrix matrix222 = new Matrix();
                    matrix222.setRotate((float) i, (float) (width222 / 2), (float) (height222 / 2));
                    ? r22222 = Bitmap.createBitmap(r22222, 0, 0, width222, height222, matrix222, false);
                    return r22222;
                } catch (Throwable th) {
                    th = th;
                    r2 = r13;
                    if (r2 != 0) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                r2 = query;
                if (r2 != 0) {
                    r2.close();
                }
                throw th;
            }
            if (!(i == 0 || r22222 == 0)) {
                int width2222 = r22222.getWidth();
                int height2222 = r22222.getHeight();
                Matrix matrix2222 = new Matrix();
                matrix2222.setRotate((float) i, (float) (width2222 / 2), (float) (height2222 / 2));
                ? r222222 = Bitmap.createBitmap(r222222, 0, 0, width2222, height2222, matrix2222, false);
            }
            return r222222;
        } catch (SQLiteException e6) {
            e = e6;
            r10 = 0;
            Log.w(str3, (Throwable) e);
            if (r13 != 0) {
            }
            createBitmap = r10;
            int width22222 = r222222.getWidth();
            int height22222 = r222222.getHeight();
            Matrix matrix22222 = new Matrix();
            matrix22222.setRotate((float) i, (float) (width22222 / 2), (float) (height22222 / 2));
            ? r2222222 = Bitmap.createBitmap(r2222222, 0, 0, width22222, height22222, matrix22222, false);
            return r2222222;
        }
    }

    private static Bitmap getScaledBitmapFromFile(String str, int i, int i2, int i3) {
        Bitmap bitmap;
        try {
            int ceil = (int) Math.ceil(((double) Math.max(i, i2)) / 512.0d);
            Options options = new Options();
            options.inSampleSize = Integer.highestOneBit(ceil);
            options.inPurgeable = true;
            bitmap = BitmapFactory.decodeFile(str);
            if (bitmap == null || i3 == 0) {
                return bitmap;
            }
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                Matrix matrix = new Matrix();
                matrix.setRotate((float) i3, (float) (width / 2), (float) (height / 2));
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
                return bitmap;
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            bitmap = null;
            StringBuilder sb = new StringBuilder();
            sb.append("decode fail from ");
            sb.append(str);
            Log.e(TAG, sb.toString(), (Throwable) e);
            return bitmap;
        }
    }

    private static String getVideoBucketIds() {
        StringBuilder sb;
        if (Storage.secondaryStorageMounted()) {
            sb = new StringBuilder();
            sb.append("bucket_id IN (");
            sb.append(Storage.PRIMARY_BUCKET_ID);
            sb.append(",");
            sb.append(Storage.SECONDARY_BUCKET_ID);
            sb.append(")");
        } else {
            sb = new StringBuilder();
            sb.append("bucket_id=");
            sb.append(Storage.BUCKET_ID);
        }
        return sb.toString();
    }

    public boolean fromFile() {
        return this.mFromFile;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public int getGifHeight() {
        return this.gifHeight;
    }

    public int getGifWidth() {
        return this.gifWidth;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public boolean isGif() {
        return this.mIsGif;
    }

    public boolean isWaitingForUri() {
        return this.mWaitingForUri;
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r3v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r4v1 */
    /* JADX WARNING: type inference failed for: r3v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r4v4, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r4v5 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r4v6 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r3v8, types: [java.io.OutputStream, java.io.Closeable, java.io.BufferedOutputStream] */
    /* JADX WARNING: type inference failed for: r4v8 */
    /* JADX WARNING: type inference failed for: r4v9 */
    /* JADX WARNING: type inference failed for: r4v10, types: [java.io.OutputStream, java.io.DataOutputStream] */
    /* JADX WARNING: type inference failed for: r4v11 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r3v10 */
    /* JADX WARNING: type inference failed for: r4v12 */
    /* JADX WARNING: type inference failed for: r3v11 */
    /* JADX WARNING: type inference failed for: r3v12 */
    /* JADX WARNING: type inference failed for: r4v13 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v1
  assigns: []
  uses: []
  mth insns count: 125
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 8 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveLastThumbnailToFile(File file) {
        ? r4;
        ? r3;
        FileOutputStream fileOutputStream;
        ? r32;
        ? r33;
        ? r34;
        ? r42;
        ? r43;
        if (this.mUri == null) {
            Log.w(TAG, "Fail to store bitmap. uri is null");
            return;
        }
        File file2 = new File(file, LAST_THUMB_FILENAME);
        synchronized (sLock) {
            FileOutputStream fileOutputStream2 = null;
            try {
                fileOutputStream = new FileOutputStream(file2);
                try {
                    r3 = new BufferedOutputStream(fileOutputStream, 4096);
                } catch (IOException e) {
                    e = e;
                    ? r35 = 0;
                    r43 = 0;
                    fileOutputStream2 = fileOutputStream;
                    r4 = r43;
                    r32 = r35;
                    String str = TAG;
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Fail to store bitmap. path=");
                        sb.append(file2.getPath());
                        Log.e(str, sb.toString(), (Throwable) e);
                        Util.closeSilently(fileOutputStream2);
                        Util.closeSilently(r32);
                        r42 = r4;
                        Util.closeSilently(r42);
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        r3 = r32;
                        Util.closeSilently(fileOutputStream);
                        Util.closeSilently(r3);
                        Util.closeSilently(r4);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    r34 = 0;
                    r4 = r33;
                    r3 = r33;
                    Util.closeSilently(fileOutputStream);
                    Util.closeSilently(r3);
                    Util.closeSilently(r4);
                    throw th;
                }
                try {
                    r4 = new DataOutputStream(r3);
                } catch (IOException e2) {
                    e = e2;
                    r43 = 0;
                    fileOutputStream2 = fileOutputStream;
                    r4 = r43;
                    r32 = r35;
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Fail to store bitmap. path=");
                    sb2.append(file2.getPath());
                    Log.e(str2, sb2.toString(), (Throwable) e);
                    Util.closeSilently(fileOutputStream2);
                    Util.closeSilently(r32);
                    r42 = r4;
                    Util.closeSilently(r42);
                } catch (Throwable th3) {
                    th = th3;
                    r4 = 0;
                    r3 = r3;
                    Util.closeSilently(fileOutputStream);
                    Util.closeSilently(r3);
                    Util.closeSilently(r4);
                    throw th;
                }
                try {
                    r4.writeUTF(this.mUri.buildUpon().appendQueryParameter("version", String.valueOf(1)).build().toString());
                    Log.d(TAG, "saveLastThumbnailToFile: version: 1");
                    r4.writeBoolean(isGif());
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("saveLastThumbnailToFile: isGif:");
                    sb3.append(isGif());
                    Log.d(str3, sb3.toString());
                    r4.writeInt(getGifWidth());
                    String str4 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("saveLastThumbnailToFile: gifWidth:");
                    sb4.append(getGifWidth());
                    Log.d(str4, sb4.toString());
                    r4.writeInt(getGifHeight());
                    String str5 = TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("saveLastThumbnailToFile: gifHeight:");
                    sb5.append(getGifHeight());
                    Log.d(str5, sb5.toString());
                    this.mBitmap.compress(CompressFormat.JPEG, 90, r4);
                    r4.close();
                    Util.closeSilently(fileOutputStream);
                    Util.closeSilently(r3);
                    r42 = r4;
                } catch (IOException e3) {
                    e = e3;
                    fileOutputStream2 = fileOutputStream;
                    r4 = r43;
                    r32 = r35;
                    String str22 = TAG;
                    StringBuilder sb22 = new StringBuilder();
                    sb22.append("Fail to store bitmap. path=");
                    sb22.append(file2.getPath());
                    Log.e(str22, sb22.toString(), (Throwable) e);
                    Util.closeSilently(fileOutputStream2);
                    Util.closeSilently(r32);
                    r42 = r4;
                    Util.closeSilently(r42);
                } catch (Throwable th4) {
                    th = th4;
                    Util.closeSilently(fileOutputStream);
                    Util.closeSilently(r3);
                    Util.closeSilently(r4);
                    throw th;
                }
            } catch (IOException e4) {
                e = e4;
                r32 = 0;
                r4 = 0;
                String str222 = TAG;
                StringBuilder sb222 = new StringBuilder();
                sb222.append("Fail to store bitmap. path=");
                sb222.append(file2.getPath());
                Log.e(str222, sb222.toString(), (Throwable) e);
                Util.closeSilently(fileOutputStream2);
                Util.closeSilently(r32);
                r42 = r4;
                Util.closeSilently(r42);
            } catch (Throwable th5) {
                th = th5;
                fileOutputStream = null;
                r34 = 0;
                r4 = r33;
                r3 = r33;
                Util.closeSilently(fileOutputStream);
                Util.closeSilently(r3);
                Util.closeSilently(r4);
                throw th;
            }
            Util.closeSilently(r42);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public void setFromFile(boolean z) {
        this.mFromFile = z;
    }

    public void setGifSize(int i, int i2) {
        this.gifWidth = i;
        this.gifHeight = i2;
    }

    public void setIsGif(boolean z) {
        this.mIsGif = z;
    }

    public void setUri(Uri uri) {
        if (this.mUri != null) {
            Log.d(TAG, "the uri for thumbnail is being updated unexpectedly..ignore.");
            return;
        }
        this.mUri = uri;
        this.mWaitingForUri = false;
    }

    public void startWaitingForUri() {
        this.mWaitingForUri = true;
    }

    public void updateUri(Uri uri) {
        if (uri != null) {
            this.mUri = uri;
        }
    }
}
