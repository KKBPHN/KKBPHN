package com.android.camera.storage;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.usage.ExternalStorageStats;
import android.app.usage.StorageStatsManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.DngCreator;
import android.location.Location;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StatFs;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Size;
import android.util.Xml;
import androidx.core.app.FrameMetricsAggregator;
import com.adobe.xmp.XMPMeta;
import com.android.camera.ActivityBase;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.FileCompat;
import com.android.camera.LocationManager;
import com.android.camera.R;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.XmpHelper;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.gallery3d.exif.ExifHelper;
import com.android.gallery3d.exif.ExifInterface;
import com.xiaomi.camera.core.PictureInfo;
import com.xiaomi.camera.parallelservice.util.ParallelUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import miui.os.Build;
import miui.reflect.Method;
import org.xmlpull.v1.XmlSerializer;

public class Storage {
    public static final String ATTR_DOC_PHOTO_CROP_POINTS = "cropPoints";
    public static final String ATTR_DOC_PHOTO_ENHANCE_TYPE = "enhanceType";
    public static final String ATTR_DOC_PHOTO_LENGTH = "rawLength";
    public static final String ATTR_DOC_PHOTO_VERSION = "version";
    public static final String AVOID_SCAN_FILE_NAME = ".nomedia";
    public static int BUCKET_ID = DIRECTORY.toLowerCase(Locale.ENGLISH).hashCode();
    private static final String CAMERA_STORAGE_PATH_SUFFIX = "/DCIM/Camera";
    private static final String CAMERA_STORAGE_PATH_TEMP = "/DCIM/Camera/.temp";
    public static String CAMERA_TEMP_DIRECTORY = null;
    public static String DIRECTORY = null;
    public static final String DOCUMENT_PICTURE = "DOCUMENT_PICTURE";
    public static String FIRST_CONSIDER_STORAGE_PATH = PRIMARY_STORAGE_PATH;
    public static final String GIF_SUFFIX = ".gif";
    public static final String HEIC_SUFFIX = ".HEIC";
    public static String HIDEDIRECTORY = null;
    private static final String HIDE_CAMERA_STORAGE_PATH_SUFFIX = "/DCIM/Camera/.ubifocus";
    public static final String HSR_120_SUFFIX = "_HSR_120";
    public static final String HSR_240_SUFFIX = "_HSR_240";
    public static final String HSR_480_SUFFIX = "_HSR_480";
    public static final String ID_CARD_PICTURE_1 = "ID_CARD_PICTURE_1";
    public static final String ID_CARD_PICTURE_2 = "ID_CARD_PICTURE_2";
    public static final String JPEG_SUFFIX = ".jpg";
    private static final AtomicLong LEFT_SPACE = new AtomicLong(0);
    public static final String LIVE_SHOT_PREFIX = "MV";
    public static final long LOW_STORAGE_THRESHOLD = 209715200;
    private static final int MAX_WRITE_RETRY = (Build.IS_ALPHA_BUILD ? 1 : 3);
    public static final String MIME_DNG = "image/x-adobe-dng";
    public static final String MIME_GIF = "image/gif";
    public static final String MIME_HEIC = "image/heic";
    public static final String MIME_JPEG = "image/jpeg";
    protected static final String PARALLEL_PROCESS_EXIF_PROCESS_TAG = "processing";
    public static final long PREPARING = -2;
    public static int PRIMARY_BUCKET_ID = 0;
    public static int PRIMARY_RAW_BUCKET_ID = 0;
    private static final String PRIMARY_STORAGE_PATH = Environment.getExternalStorageDirectory().toString();
    public static final float QUOTA_RATIO = 0.9f;
    public static String RAW_DIRECTORY = null;
    private static final String RAW_PATH_SUFFIX = "/Raw";
    public static final String RAW_SUFFIX = ".dng";
    private static final String SAVE_TO_CLOUD_ALBUM_ACTION = "com.miui.gallery.SAVE_TO_CLOUD";
    private static final String SAVE_TO_CLOUD_ALBUM_CACHE_LOCATION_LATITUDE_KEY = "extra_cache_location_latitude";
    private static final String SAVE_TO_CLOUD_ALBUM_CACHE_LOCATION_LONGITUDE_KEY = "extra_cache_location_longitude";
    private static final String SAVE_TO_CLOUD_ALBUM_CACHE_LOCATION_REALTIMENANOS_KEY = "extra_cache_location_elapsedrealtimenanos";
    private static final String SAVE_TO_CLOUD_ALBUM_FILE_LENGTH = "extra_file_length";
    private static final String SAVE_TO_CLOUD_ALBUM_NOGAUSSIAN = "extra_no_gaussian";
    private static final String SAVE_TO_CLOUD_ALBUM_PACKAGE = "com.miui.gallery";
    private static final String SAVE_TO_CLOUD_ALBUM_PATH_KAY = "extra_file_path";
    private static final String SAVE_TO_CLOUD_ALBUM_STORE_ID_KAY = "extra_media_store_id";
    private static final String SAVE_TO_CLOUD_ALBUM_TEMP_FILE_KAY = "extra_is_temp_file";
    public static int SECONDARY_BUCKET_ID = 0;
    public static int SECONDARY_RAW_BUCKET_ID = 0;
    private static String SECONDARY_STORAGE_PATH = System.getenv("SECONDARY_STORAGE");
    private static final String TAG = "Storage";
    public static final String TAG_DOC_PHOTO = "docPhoto";
    public static final String TIMER_BURST_SUFFIX = "_TIMEBURST";
    public static final String UBIFOCUS_SUFFIX = "_UBIFOCUS_";
    public static final long UNAVAILABLE = -1;
    public static final long UNKNOWN_SIZE = -3;
    public static final int VERSION = 1;
    public static final String VIDEO_8K_SUFFIX = "_8K";
    private static String sCurrentStoragePath = FIRST_CONSIDER_STORAGE_PATH;
    private static WeakReference sStorageListener;

    public interface StorageListener {
        void onStoragePathChanged();
    }

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

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(FIRST_CONSIDER_STORAGE_PATH);
        String str = CAMERA_STORAGE_PATH_SUFFIX;
        sb.append(str);
        DIRECTORY = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(DIRECTORY);
        String str2 = RAW_PATH_SUFFIX;
        sb2.append(str2);
        RAW_DIRECTORY = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(FIRST_CONSIDER_STORAGE_PATH);
        sb3.append(HIDE_CAMERA_STORAGE_PATH_SUFFIX);
        HIDEDIRECTORY = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(FIRST_CONSIDER_STORAGE_PATH);
        sb4.append(CAMERA_STORAGE_PATH_TEMP);
        CAMERA_TEMP_DIRECTORY = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(PRIMARY_STORAGE_PATH);
        sb5.append(str);
        PRIMARY_BUCKET_ID = sb5.toString().toLowerCase(Locale.ENGLISH).hashCode();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(SECONDARY_STORAGE_PATH);
        sb6.append(str);
        SECONDARY_BUCKET_ID = sb6.toString().toLowerCase(Locale.ENGLISH).hashCode();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(PRIMARY_STORAGE_PATH);
        sb7.append(str);
        sb7.append(str2);
        PRIMARY_RAW_BUCKET_ID = sb7.toString().toLowerCase(Locale.ENGLISH).hashCode();
        StringBuilder sb8 = new StringBuilder();
        sb8.append(SECONDARY_STORAGE_PATH);
        sb8.append(str);
        sb8.append(str2);
        SECONDARY_RAW_BUCKET_ID = sb8.toString().toLowerCase(Locale.ENGLISH).hashCode();
        StringBuilder sb9 = new StringBuilder();
        sb9.append(DIRECTORY);
        sb9.append(File.separator);
        sb9.append(AVOID_SCAN_FILE_NAME);
        File file = new File(sb9.toString());
        if (file.exists()) {
            file.delete();
        }
    }

    public static Uri addDNGToDataBase(Context context, String str, long j, int i, int i2, int i3) {
        String name = new File(str).getName();
        String substring = name.substring(0, name.lastIndexOf(46) - 1);
        ContentValues contentValues = new ContentValues(7);
        contentValues.put("title", substring);
        contentValues.put("_display_name", name);
        contentValues.put("media_type", Integer.valueOf(1));
        contentValues.put("datetaken", Long.valueOf(j));
        contentValues.put("mime_type", MIME_DNG);
        contentValues.put("_data", str);
        contentValues.put("width", Integer.valueOf(i));
        contentValues.put("height", Integer.valueOf(i2));
        contentValues.put("orientation", Integer.valueOf(i3));
        try {
            return context.getContentResolver().insert(getMediaUri(context, false, str), contentValues);
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to write MediaStore, path ");
            sb.append(str);
            Log.e(str2, sb.toString(), (Throwable) e);
            return null;
        }
    }

    public static Uri addGifToDataBase(Context context, String str, String str2, long j, int i, int i2) {
        String name = new File(str).getName();
        ContentValues contentValues = new ContentValues(7);
        contentValues.put("title", str2);
        contentValues.put("_display_name", name);
        contentValues.put("datetaken", Long.valueOf(j));
        contentValues.put("mime_type", MIME_GIF);
        contentValues.put("_data", str);
        contentValues.put("width", Integer.valueOf(i));
        contentValues.put("height", Integer.valueOf(i2));
        try {
            return context.getContentResolver().insert(getMediaUri(context, false, str), contentValues);
        } catch (Exception e) {
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to write MediaStore, path ");
            sb.append(str);
            Log.e(str3, sb.toString(), (Throwable) e);
            return null;
        }
    }

    public static Uri addHeifInfo(Context context, String str, Location location, long j, int i, int i2, int i3, boolean z) {
        String str2 = str;
        long length = new File(str2).length();
        String fileTitleFromPath = Util.getFileTitleFromPath(str);
        StringBuilder sb = new StringBuilder();
        sb.append(fileTitleFromPath);
        sb.append(HEIC_SUFFIX);
        Uri insertToMediaStore = insertToMediaStore(context, fileTitleFromPath, sb.toString(), j, MIME_HEIC, i, str, length, i2, i3, location, z);
        if (insertToMediaStore == null) {
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("addHeifInfo: failed to insert to DB: ");
            sb2.append(str2);
            Log.e(str3, sb2.toString());
            return null;
        }
        saveToCloudAlbum(context, str, length, z, ContentUris.parseId(insertToMediaStore), location == null, false);
        return insertToMediaStore;
    }

    public static Uri addImage(Context context, String str, long j, Location location, int i, byte[] bArr, boolean z, int i2, int i3, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, String str2, PictureInfo pictureInfo) {
        return addImage(context, str, j, location, i, bArr, z, i2, i3, z2, z3, z4, z5, z6, str2, pictureInfo, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:140:0x01b1  */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x01ca  */
    /* JADX WARNING: Removed duplicated region for block: B:168:0x024b  */
    /* JADX WARNING: Removed duplicated region for block: B:175:0x0260 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:176:0x0261  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Uri addImage(Context context, String str, long j, Location location, int i, byte[] bArr, boolean z, int i2, int i3, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, String str2, PictureInfo pictureInfo, boolean z7) {
        boolean z8;
        boolean z9;
        String str3;
        String str4;
        boolean z10;
        Throwable th;
        OutputStream outputStream;
        OutputStream outputStream2;
        Throwable th2;
        Throwable th3;
        Throwable th4;
        Context context2 = context;
        String str5 = str;
        long j2 = j;
        Location location2 = location;
        int i4 = i;
        boolean z11 = z;
        boolean z12 = z3;
        boolean z13 = z4;
        String str6 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("addImage: parallel=");
        sb.append(z6);
        sb.append(" appendExif=");
        sb.append(z5);
        Log.d(str6, sb.toString());
        boolean z14 = z13;
        byte[] updateExif = updateExif(bArr, z6, str2, pictureInfo, i, i2, i3);
        String generateFilepath4Image = generateFilepath4Image(str5, z12, z14, z11);
        String str7 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("addImage: path=");
        sb2.append(generateFilepath4Image);
        Log.d(str7, sb2.toString());
        boolean z15 = z5;
        int i5 = 0;
        while (true) {
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(updateExif));
                try {
                    if (isUseDocumentMode()) {
                        try {
                            outputStream = FileCompat.getFileOutputStream(generateFilepath4Image, true);
                        } catch (Throwable th5) {
                            th = th5;
                        }
                    } else {
                        outputStream = new FileOutputStream(generateFilepath4Image);
                    }
                    outputStream2 = outputStream;
                    try {
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream2);
                        if (!z2) {
                            z10 = z15;
                            byte[] bArr2 = new byte[4096];
                            while (true) {
                                int read = bufferedInputStream.read(bArr2);
                                if (read == -1) {
                                    break;
                                }
                                bufferedOutputStream.write(bArr2, 0, read);
                            }
                        } else {
                            try {
                                boolean z16 = i4 % 180 == 0;
                                z10 = z15;
                                Bitmap flipJpeg = flipJpeg(updateExif, z16, !z16);
                                if (flipJpeg == null) {
                                    byte[] bArr3 = new byte[4096];
                                    while (true) {
                                        int read2 = bufferedInputStream.read(bArr3);
                                        if (read2 == -1) {
                                            break;
                                        }
                                        bufferedOutputStream.write(bArr3, 0, read2);
                                    }
                                } else {
                                    ExifInterface exif = Util.getExif(updateExif);
                                    byte[] thumbnailBytes = exif.getThumbnailBytes();
                                    if (thumbnailBytes != null) {
                                        Bitmap flipJpeg2 = flipJpeg(thumbnailBytes, z16, !z16);
                                        if (flipJpeg2 != null) {
                                            exif.setCompressedThumbnail(flipJpeg2);
                                            flipJpeg2.recycle();
                                        }
                                        z10 = false;
                                    }
                                    exif.writeExif(flipJpeg, (OutputStream) bufferedOutputStream);
                                    flipJpeg.recycle();
                                }
                            } catch (Throwable th6) {
                                th3 = th6;
                                z15 = z10;
                                throw th3;
                            }
                        }
                        z15 = z10;
                        if (z15) {
                            try {
                                bufferedOutputStream.flush();
                                bufferedOutputStream.close();
                                if (!isUseDocumentMode()) {
                                    ExifHelper.writeExifByFilePath(generateFilepath4Image, i4, location2, j2);
                                } else {
                                    try {
                                        ParcelFileDescriptor parcelFileDescriptor = FileCompat.getParcelFileDescriptor(generateFilepath4Image, false);
                                        try {
                                            ExifHelper.writeExifByFd(parcelFileDescriptor.getFileDescriptor(), i4, location2, j2);
                                            if (parcelFileDescriptor != null) {
                                                $closeResource(null, parcelFileDescriptor);
                                            }
                                        } catch (Throwable th7) {
                                            Throwable th8 = th7;
                                            if (parcelFileDescriptor == null) {
                                                break;
                                            }
                                            $closeResource(th4, parcelFileDescriptor);
                                            break;
                                            throw th8;
                                        }
                                    } catch (Exception e) {
                                        String str8 = TAG;
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("write exif failed, file is ");
                                        sb3.append(generateFilepath4Image);
                                        Log.e(str8, sb3.toString(), (Throwable) e);
                                    }
                                }
                            } catch (Throwable th9) {
                                th = th9;
                            }
                        }
                        try {
                            $closeResource(null, bufferedOutputStream);
                            if (outputStream2 != null) {
                                $closeResource(null, outputStream2);
                            }
                            try {
                                $closeResource(null, bufferedInputStream);
                                z8 = false;
                                Context context3 = context;
                                break;
                            } catch (Exception e2) {
                                e = e2;
                            } catch (Throwable th10) {
                                Throwable th11 = th10;
                                $closeResource(th, bufferedInputStream);
                                throw th11;
                            }
                        } catch (Throwable th12) {
                            th = th12;
                            th2 = th;
                            throw th2;
                        }
                    } catch (Throwable th13) {
                        th = th13;
                        z10 = z15;
                        th2 = th;
                        throw th2;
                    }
                } catch (Throwable th14) {
                    th = th14;
                    z10 = z15;
                    th = th;
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                z10 = z15;
                dumpExceptionEnv(e, generateFilepath4Image);
                Log.e(TAG, "Failed to write image", (Throwable) e);
                i5++;
                final Context context4 = context;
                if (Util.isQuotaExceeded(e) && (context4 instanceof ActivityBase)) {
                    ActivityBase activityBase = (ActivityBase) context4;
                    if (!activityBase.isActivityPaused()) {
                        activityBase.runOnUiThread(new Runnable() {
                            public void run() {
                                ToastUtils.showToast(context4, (int) R.string.spaceIsLow_content_primary_storage_priority);
                            }
                        });
                    }
                    i5 = MAX_WRITE_RETRY;
                } else if (i5 < MAX_WRITE_RETRY) {
                    System.gc();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException unused) {
                    }
                }
                if (i5 >= MAX_WRITE_RETRY) {
                    z8 = true;
                    if (z8) {
                    }
                    if (!z4) {
                    }
                    if (!z9) {
                    }
                    if (!isSaveToHidenFolder(str3)) {
                    }
                } else {
                    int i6 = i2;
                    location2 = location;
                    boolean z17 = z;
                    boolean z18 = z3;
                    boolean z19 = z4;
                }
            }
            int i62 = i2;
            location2 = location;
            boolean z172 = z;
            boolean z182 = z3;
            boolean z192 = z4;
        }
        if (z8) {
            File file = new File(generateFilepath4Image);
            if (!file.exists() || file.length() <= 0) {
                return null;
            }
        }
        if (!z4) {
            boolean isProduceFocusInfoSuccess = Util.isProduceFocusInfoSuccess(updateExif);
            int centerFocusDepthIndex = Util.getCenterFocusDepthIndex(updateExif, i2, i3);
            String str9 = "_";
            String str10 = UBIFOCUS_SUFFIX;
            String substring = str5.substring(0, isProduceFocusInfoSuccess ? str5.lastIndexOf(str9) : str5.lastIndexOf(str10));
            String generateFilepath4Image2 = generateFilepath4Image(substring, false, false, false);
            StringBuilder sb4 = new StringBuilder();
            sb4.append(substring);
            if (!isProduceFocusInfoSuccess) {
                str9 = str10;
            }
            sb4.append(str9);
            sb4.append(centerFocusDepthIndex);
            z9 = z3;
            String generateFilepath4Image3 = generateFilepath4Image(sb4.toString(), z9, false, false);
            if (generateFilepath4Image2 == null || generateFilepath4Image3 == null) {
                String str11 = TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("oldPath: ");
                String str12 = "null";
                if (generateFilepath4Image3 == null) {
                    generateFilepath4Image3 = str12;
                }
                sb5.append(generateFilepath4Image3);
                sb5.append(" newPath: ");
                if (generateFilepath4Image2 != null) {
                    str12 = generateFilepath4Image2;
                }
                sb5.append(str12);
                Log.e(str11, sb5.toString());
            } else {
                new File(generateFilepath4Image3).renameTo(new File(generateFilepath4Image2));
            }
            if (!isProduceFocusInfoSuccess) {
                deleteImage(substring);
            }
            str3 = substring;
            str4 = generateFilepath4Image2;
        } else {
            int i7 = i2;
            int i8 = i3;
            z9 = z3;
            str3 = str5;
            str4 = generateFilepath4Image;
        }
        if (!z9 && !z4) {
            return null;
        }
        if (!isSaveToHidenFolder(str3)) {
            return null;
        }
        String str13 = z ? HEIC_SUFFIX : JPEG_SUFFIX;
        String str14 = z ? MIME_HEIC : MIME_JPEG;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(str3);
        sb6.append(str13);
        boolean z20 = false;
        byte[] bArr4 = updateExif;
        Uri insertToMediaStore = insertToMediaStore(context, str3, sb6.toString(), j, str14, i, str4, new File(str4).length(), i2, i3, location, z6);
        if (insertToMediaStore == null) {
            String str15 = TAG;
            StringBuilder sb7 = new StringBuilder();
            sb7.append("failed to insert to DB: ");
            sb7.append(str4);
            Log.e(str15, sb7.toString());
            return null;
        }
        long length = (long) bArr4.length;
        long parseId = ContentUris.parseId(insertToMediaStore);
        if (location == null) {
            z20 = true;
        }
        saveToCloudAlbum(context, str4, length, z6, parseId, z20, z7);
        return insertToMediaStore;
    }

    public static Uri addImageForEffect(Context context, String str, long j, Location location, int i, byte[] bArr, int i2, int i3, boolean z, boolean z2, String str2, PictureInfo pictureInfo) {
        return addImage(context, str, j, location, i, bArr, false, i2, i3, z, false, false, false, z2, str2, pictureInfo);
    }

    public static Uri addImageForGroupOrPanorama(Context context, String str, int i, long j, Location location, int i2, int i3) {
        File file;
        String str2 = str;
        if (!(context == null || str2 == null)) {
            try {
                file = new File(str2);
            } catch (Exception e) {
                String str3 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to open panorama file: ");
                sb.append(e.getMessage());
                Log.e(str3, sb.toString(), (Throwable) e);
                file = null;
            }
            if (file != null && file.exists()) {
                String name = file.getName();
                String str4 = str2;
                Uri insertToMediaStore = insertToMediaStore(context, name, name, j, MIME_JPEG, i, str, file.length(), i2, i3, location, false);
                saveToCloudAlbum(context, str4, -1, location == null);
                return insertToMediaStore;
            }
        }
        return null;
    }

    public static Uri addImageForSnapCamera(Context context, String str, long j, Location location, int i, byte[] bArr, int i2, int i3, boolean z, boolean z2, boolean z3, String str2, PictureInfo pictureInfo) {
        return addImage(context, str, j, location, i, bArr, false, i2, i3, z, z2, z3, false, false, str2, pictureInfo);
    }

    /* JADX INFO: finally extract failed */
    public static Uri addRawImage(Context context, String str, CameraCharacteristics cameraCharacteristics, CaptureResult captureResult, byte[] bArr, long j, int i, int i2, int i3) {
        OutputStream outputStream;
        Throwable th;
        String generateRawFilepath = generateRawFilepath(str);
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        try {
            DngCreator dngCreator = new DngCreator(cameraCharacteristics, captureResult);
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(bArr));
                try {
                    boolean z = true;
                    th = isUseDocumentMode() ? FileCompat.getFileOutputStream(generateRawFilepath, true) : new FileOutputStream(generateRawFilepath);
                    try {
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                        try {
                            dngCreator.setOrientation(ExifInterface.getExifOrientationValue(i3));
                            dngCreator.writeInputStream(bufferedOutputStream, new Size(i, i2), bufferedInputStream, 0);
                            Uri addDNGToDataBase = addDNGToDataBase(context, generateRawFilepath, j, i, i2, i3);
                            String str2 = TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("addRawImage path ");
                            sb.append(generateRawFilepath);
                            sb.append(", uri = ");
                            sb.append(addDNGToDataBase);
                            Log.d(str2, sb.toString());
                            if (currentLocation != null) {
                                z = false;
                            }
                            saveToCloudAlbum(context, generateRawFilepath, -1, z);
                            $closeResource(null, bufferedOutputStream);
                            if (outputStream != null) {
                                $closeResource(null, outputStream);
                            }
                            $closeResource(null, bufferedInputStream);
                            $closeResource(null, dngCreator);
                            return addDNGToDataBase;
                        } catch (Throwable th2) {
                            Throwable th3 = th2;
                            $closeResource(th, bufferedOutputStream);
                            throw th3;
                        }
                    } catch (Throwable th4) {
                        Throwable th5 = th4;
                        try {
                            throw th5;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            if (outputStream != null) {
                                $closeResource(th5, outputStream);
                            }
                            throw th7;
                        }
                    }
                } finally {
                    outputStream = th;
                    try {
                    } catch (Throwable th8) {
                        Throwable th9 = th8;
                        $closeResource(outputStream, bufferedInputStream);
                        throw th9;
                    }
                }
            } finally {
                Throwable th10 = th;
                try {
                } catch (Throwable th11) {
                    Throwable th12 = th11;
                    $closeResource(th10, dngCreator);
                    throw th12;
                }
            }
        } catch (Throwable th13) {
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("addRawImage failed, path ");
            sb2.append(generateRawFilepath);
            Log.w(str3, sb2.toString(), th13);
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004f, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        $closeResource(r8, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0053, code lost:
        throw r9;
     */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0066 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0067 A[SYNTHETIC, Splitter:B:21:0x0067] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] addXmpData(byte[] bArr, long j, float[] fArr, String str) {
        CharSequence charSequence;
        byte[] bArr2;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayInputStream byteArrayInputStream;
        String str2 = TAG_DOC_PHOTO;
        byte[] bArr3 = null;
        try {
            StringWriter stringWriter = new StringWriter();
            XmlSerializer newSerializer = Xml.newSerializer();
            newSerializer.setOutput(stringWriter);
            newSerializer.startDocument("UTF-8", Boolean.valueOf(true));
            newSerializer.startTag(null, str2);
            newSerializer.attribute(null, "version", String.valueOf(1));
            newSerializer.attribute(null, ATTR_DOC_PHOTO_ENHANCE_TYPE, str);
            newSerializer.attribute(null, ATTR_DOC_PHOTO_CROP_POINTS, convertToString(fArr));
            newSerializer.attribute(null, ATTR_DOC_PHOTO_LENGTH, String.valueOf(j));
            newSerializer.endTag(null, str2);
            newSerializer.endDocument();
            charSequence = stringWriter.toString();
            try {
                $closeResource(null, stringWriter);
            } catch (IOException e) {
                e = e;
            }
        } catch (IOException e2) {
            e = e2;
            charSequence = null;
            e.printStackTrace();
            Log.d(TAG, "build xmp string error");
            if (!TextUtils.isEmpty(charSequence)) {
            }
        }
        if (!TextUtils.isEmpty(charSequence)) {
            return bArr;
        }
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byteArrayInputStream = new ByteArrayInputStream(bArr);
                try {
                    XMPMeta createXMPMeta = XmpHelper.createXMPMeta();
                    createXMPMeta.setProperty(XmpHelper.XIAOMI_XMP_METADATA_NAMESPACE, XmpHelper.XIAOMI_XMP_METADATA_PROPERTY_NAME, charSequence);
                    XmpHelper.writeXMPMeta(byteArrayInputStream, byteArrayOutputStream, createXMPMeta);
                    bArr2 = byteArrayOutputStream.toByteArray();
                    try {
                        Log.d(TAG, " built xmp data complete");
                        try {
                            $closeResource(null, byteArrayInputStream);
                            try {
                                $closeResource(null, byteArrayOutputStream);
                            } catch (Exception e3) {
                                e = e3;
                            }
                            return bArr2;
                        } catch (Throwable th) {
                            th = th;
                            bArr3 = bArr2;
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        bArr3 = bArr2;
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            bArr2 = bArr3;
            e.printStackTrace();
            Log.e(TAG, "build xmp bytes error");
            return bArr2;
        } catch (Throwable th5) {
            $closeResource(th, byteArrayOutputStream);
            throw th5;
        }
    }

    private static String convertToString(float[] fArr) {
        if (fArr == null || fArr.length != 8) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (float append : fArr) {
            sb.append(append);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static boolean createHideFile() {
        StringBuilder sb = new StringBuilder();
        sb.append(CAMERA_TEMP_DIRECTORY);
        sb.append(File.separator);
        sb.append(AVOID_SCAN_FILE_NAME);
        return Util.createFile(new File(sb.toString()));
    }

    public static void deleteImage(String str) {
        File[] listFiles;
        File file = new File(HIDEDIRECTORY);
        if (file.exists() && file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                if (file2.getName().indexOf(str) != -1) {
                    file2.delete();
                }
            }
        }
    }

    private static boolean deleteSdcardFile(String str) {
        boolean deleteFile;
        int i = 0;
        do {
            i++;
            deleteFile = FileCompat.deleteFile(str);
            if (deleteFile) {
                break;
            }
        } while (i < 5);
        return deleteFile;
    }

    private static void dumpExceptionEnv(Exception exc, String str) {
        boolean z;
        if (exc instanceof FileNotFoundException) {
            long maxMemory = Runtime.getRuntime().maxMemory();
            long j = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            File file = new File(str);
            String filesState = Util.getFilesState(str);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(".ex");
                File file2 = new File(sb.toString());
                z = file2.createNewFile();
                file2.delete();
            } catch (IOException unused) {
                z = false;
            }
            String str2 = TAG;
            Locale locale = Locale.ENGLISH;
            Object[] objArr = new Object[8];
            objArr[0] = Long.valueOf(maxMemory);
            objArr[1] = Long.valueOf(j);
            objArr[2] = Long.valueOf(freeMemory);
            objArr[3] = file.exists() ? "exists" : "not exists";
            objArr[4] = file.isDirectory() ? "isDirectory" : "isNotDirectory";
            objArr[5] = file.canWrite() ? "canWrite" : "canNotWrite";
            objArr[6] = z ? "testFileCanWrite" : "testFileCannotWrite";
            objArr[7] = filesState;
            Log.e(str2, String.format(locale, "Failed to write image, memory state(max:%d, total:%d, free:%d), file state(%s;%s;%s;%s,%s)", objArr), (Throwable) exc);
        }
    }

    private static void extractDateTaken(Cursor cursor, ContentValues contentValues) {
        if (cursor != null && cursor.moveToFirst()) {
            String str = "datetaken";
            contentValues.put(str, Long.valueOf(cursor.getLong(cursor.getColumnIndex(str))));
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("extractDateTaken ");
            sb.append(contentValues);
            Log.d(str2, sb.toString());
        }
    }

    public static Bitmap flipJpeg(byte[] bArr, boolean z, boolean z2) {
        if (bArr == null) {
            return null;
        }
        Options options = new Options();
        options.inPurgeable = true;
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        Matrix matrix = new Matrix();
        float f = -1.0f;
        float f2 = z ? -1.0f : 1.0f;
        if (!z2) {
            f = 1.0f;
        }
        matrix.setScale(f2, f, ((float) decodeByteArray.getWidth()) * 0.5f, ((float) decodeByteArray.getHeight()) * 0.5f);
        try {
            Bitmap createBitmap = Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix, true);
            if (createBitmap != decodeByteArray) {
                decodeByteArray.recycle();
            }
            if (createBitmap.getWidth() == -1 || createBitmap.getHeight() == -1) {
                return null;
            }
            return createBitmap;
        } catch (Exception e) {
            Log.w(TAG, "Failed to rotate thumbnail", (Throwable) e);
            return null;
        }
    }

    public static String generateFilepath(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(DIRECTORY);
        sb.append('/');
        sb.append(str);
        return sb.toString();
    }

    public static String generateFilepath(String str, String str2) {
        StringBuilder sb;
        String str3;
        if (isDocumentPicture(str) || isIdCardPicture(str)) {
            sb = new StringBuilder();
            str3 = CAMERA_TEMP_DIRECTORY;
        } else {
            sb = new StringBuilder();
            str3 = DIRECTORY;
        }
        sb.append(str3);
        sb.append('/');
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    public static String generateFilepath4Image(String str, boolean z) {
        return generateFilepath(str, z ? HEIC_SUFFIX : JPEG_SUFFIX);
    }

    public static String generateFilepath4Image(String str, boolean z, boolean z2, boolean z3) {
        String str2 = z2 ? ".y" : z3 ? HEIC_SUFFIX : JPEG_SUFFIX;
        if (z && isLowStorageSpace(HIDEDIRECTORY)) {
            return null;
        }
        if (!z) {
            return generateFilepath(str, str2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(HIDEDIRECTORY);
        sb.append('/');
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    public static String generatePrimaryDirectoryPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(PRIMARY_STORAGE_PATH);
        sb.append(CAMERA_STORAGE_PATH_SUFFIX);
        return sb.toString();
    }

    public static String generatePrimaryFilepath(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(generatePrimaryDirectoryPath());
        sb.append('/');
        sb.append(str);
        return sb.toString();
    }

    public static String generatePrimaryTempFile() {
        StringBuilder sb = new StringBuilder();
        sb.append(PRIMARY_STORAGE_PATH);
        sb.append(CAMERA_STORAGE_PATH_TEMP);
        return sb.toString();
    }

    public static String generateRawFilepath(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(RAW_DIRECTORY);
        sb.append("/");
        sb.append(str);
        sb.append(RAW_SUFFIX);
        return sb.toString();
    }

    public static String generateTempFilepath() {
        StringBuilder sb = new StringBuilder();
        sb.append(DIRECTORY);
        sb.append("/.temp");
        return sb.toString();
    }

    public static long getAvailableSpace() {
        return getAvailableSpace(DIRECTORY);
    }

    public static long getAvailableSpace(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.w(TAG, "getAvailableSpace path is empty");
            return -1;
        }
        File file = new File(str);
        boolean mkdirs = isUseDocumentMode() ? FileCompat.mkdirs(str) : Util.mkdirs(file, FrameMetricsAggregator.EVERY_DURATION, -1, -1);
        if (!file.exists() || !file.isDirectory()) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getAvailableSpace path = ");
            sb.append(str);
            sb.append(", exists = ");
            sb.append(file.exists());
            sb.append(", isDirectory = ");
            sb.append(file.isDirectory());
            sb.append(", canWrite = ");
            sb.append(file.canWrite());
            Log.w(str2, sb.toString());
            return -1;
        }
        if (mkdirs && str.endsWith(CAMERA_STORAGE_PATH_SUFFIX)) {
            if (MediaProviderUtil.insertCameraDirectory(CameraAppImpl.getAndroidContext(), str) != null) {
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("insertDirectory success, path ");
                sb2.append(str);
                Log.d(str3, sb2.toString());
            } else {
                String str4 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("insertDirectory fail, path ");
                sb3.append(str);
                Log.w(str4, sb3.toString());
            }
        }
        long j = -3;
        try {
            if (HIDEDIRECTORY.equals(str)) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(HIDEDIRECTORY);
                sb4.append(File.separator);
                sb4.append(AVOID_SCAN_FILE_NAME);
                Util.createFile(new File(sb4.toString()));
            }
            if (DIRECTORY.equals(str)) {
                j = getQuotaInfo(str);
            }
            return j;
        } catch (Exception e) {
            Log.i(TAG, "Fail to access external storage", e);
            return -3;
        }
    }

    public static long getLeftSpace() {
        long j = LEFT_SPACE.get();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getLeftSpace() return ");
        sb.append(j);
        Log.i(str, sb.toString());
        return j;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0046, code lost:
        if (isLowStorageSpace(r5.toString()) != false) goto L_0x004a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Uri getMediaUri(Context context, boolean z, String str) {
        boolean z2 = true;
        if (hasSecondaryStorage()) {
            if (str == null || !str.startsWith(SECONDARY_STORAGE_PATH)) {
                StringBuilder sb = new StringBuilder();
                sb.append(SECONDARY_STORAGE_PATH);
                String str2 = CAMERA_STORAGE_PATH_SUFFIX;
                sb.append(str2);
                if (!isLowStorageSpace(sb.toString())) {
                    if (!CameraSettings.getPriorityStoragePreference()) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(PRIMARY_STORAGE_PATH);
                        sb2.append(str2);
                    }
                }
            }
            Uri mediaUri = CompatibilityUtils.getMediaUri(context, z, z2);
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("getMediaUri isSecondaryStorage=");
            sb3.append(z2);
            sb3.append(", uri=");
            sb3.append(mediaUri);
            Log.d(str3, sb3.toString());
            return mediaUri;
        }
        z2 = false;
        Uri mediaUri2 = CompatibilityUtils.getMediaUri(context, z, z2);
        String str32 = TAG;
        StringBuilder sb32 = new StringBuilder();
        sb32.append("getMediaUri isSecondaryStorage=");
        sb32.append(z2);
        sb32.append(", uri=");
        sb32.append(mediaUri2);
        Log.d(str32, sb32.toString());
        return mediaUri2;
    }

    public static String getPrimaryStoragePath() {
        if (PRIMARY_STORAGE_PATH == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(PRIMARY_STORAGE_PATH);
        sb.append(CAMERA_STORAGE_PATH_SUFFIX);
        return sb.toString();
    }

    private static long getQuotaInfo(String str) {
        Context androidContext = CameraAppImpl.getAndroidContext();
        StatFs statFs = new StatFs(str);
        long availableBytes = statFs.getAvailableBytes();
        if (isUsePhoneStorage()) {
            StorageStatsManager storageStatsManager = (StorageStatsManager) androidContext.getSystemService(StorageStatsManager.class);
            Class[] clsArr = {StorageStatsManager.class};
            Method method = Util.getMethod(clsArr, "isQuotaSupported", "(Ljava/util/UUID;)Z");
            if (method != null) {
                boolean invokeBoolean = method.invokeBoolean(clsArr[0], storageStatsManager, StorageManager.UUID_DEFAULT);
                ApplicationInfo applicationInfo = androidContext.getApplicationInfo();
                if (invokeBoolean) {
                    try {
                        ExternalStorageStats queryExternalStatsForUser = storageStatsManager.queryExternalStatsForUser(applicationInfo.storageUuid, UserHandle.getUserHandleForUid(applicationInfo.uid));
                        long totalBytes = statFs.getTotalBytes();
                        long j = (long) (((float) totalBytes) * 0.9f);
                        long j2 = totalBytes - j;
                        String str2 = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("quota: ");
                        sb.append(j);
                        sb.append("|");
                        sb.append(j2);
                        Log.d(str2, sb.toString());
                        if (j > 0 && availableBytes < j2) {
                            long totalBytes2 = j - queryExternalStatsForUser.getTotalBytes();
                            if (totalBytes2 < 0) {
                                totalBytes2 = 0;
                            }
                            if (totalBytes2 < availableBytes) {
                                availableBytes = totalBytes2;
                            }
                        }
                    } catch (Exception e) {
                        String str3 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("getQuotaInfo: ");
                        sb2.append(e.getMessage());
                        Log.d(str3, sb2.toString());
                        e.printStackTrace();
                    }
                }
            }
        }
        setLeftSpace(availableBytes);
        return availableBytes;
    }

    private static Intent getSaveToCloudIntent(Context context, String str, long j, boolean z, long j2, boolean z2, boolean z3) {
        Intent intent = new Intent(SAVE_TO_CLOUD_ALBUM_ACTION);
        String str2 = "com.miui.gallery";
        intent.setPackage(str2);
        List queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 0);
        if (queryBroadcastReceivers != null && queryBroadcastReceivers.size() > 0) {
            intent.setComponent(new ComponentName(str2, ((ResolveInfo) queryBroadcastReceivers.get(0)).activityInfo.name));
        }
        intent.putExtra(SAVE_TO_CLOUD_ALBUM_PATH_KAY, str);
        intent.putExtra(SAVE_TO_CLOUD_ALBUM_FILE_LENGTH, j);
        intent.putExtra(SAVE_TO_CLOUD_ALBUM_NOGAUSSIAN, z3);
        String str3 = SAVE_TO_CLOUD_ALBUM_TEMP_FILE_KAY;
        if (z) {
            intent.putExtra(str3, true);
            intent.putExtra(SAVE_TO_CLOUD_ALBUM_STORE_ID_KAY, j2);
        } else {
            intent.putExtra(str3, false);
        }
        if (z2) {
            Location lastKnownLocation = LocationManager.instance().getLastKnownLocation();
            if (lastKnownLocation != null) {
                intent.putExtra(SAVE_TO_CLOUD_ALBUM_CACHE_LOCATION_REALTIMENANOS_KEY, lastKnownLocation.getElapsedRealtimeNanos());
                intent.putExtra(SAVE_TO_CLOUD_ALBUM_CACHE_LOCATION_LATITUDE_KEY, lastKnownLocation.getLatitude());
                intent.putExtra(SAVE_TO_CLOUD_ALBUM_CACHE_LOCATION_LONGITUDE_KEY, lastKnownLocation.getLongitude());
            }
            Log.d(TAG, "broadcast last location to gallery");
        }
        return intent;
    }

    public static String getSecondaryStoragePath() {
        if (SECONDARY_STORAGE_PATH == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(SECONDARY_STORAGE_PATH);
        sb.append(CAMERA_STORAGE_PATH_SUFFIX);
        return sb.toString();
    }

    public static boolean hasSecondaryStorage() {
        boolean z = C0124O00000oO.Oo00oo0() && SECONDARY_STORAGE_PATH != null;
        if (VERSION.SDK_INT >= 28) {
            z = UserHandle.myUserId() == 0 && z;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("hasSecondaryStorage=");
        sb.append(z);
        Log.d(str, sb.toString());
        return z;
    }

    public static void initStorage(Context context) {
        if (C0124O00000oO.Oo00oo0()) {
            FileCompat.updateSDPath();
            String sdcardPath = CompatibilityUtils.getSdcardPath(context);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("initStorage sd=");
            sb.append(sdcardPath);
            Log.v(str, sb.toString());
            if (sdcardPath != null) {
                SECONDARY_STORAGE_PATH = sdcardPath;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(SECONDARY_STORAGE_PATH);
                sb2.append(CAMERA_STORAGE_PATH_SUFFIX);
                SECONDARY_BUCKET_ID = sb2.toString().toLowerCase(Locale.ENGLISH).hashCode();
            } else {
                SECONDARY_STORAGE_PATH = null;
            }
            readSystemPriorityStorage();
        }
    }

    private static Uri insertToMediaStore(Context context, String str, String str2, long j, String str3, int i, String str4, long j2, int i2, int i3, Location location, boolean z) {
        String str5;
        String sb;
        ContentValues contentValues = new ContentValues(11);
        contentValues.put("title", str);
        contentValues.put("_display_name", str2);
        contentValues.put("datetaken", Long.valueOf(j));
        contentValues.put("mime_type", str3);
        contentValues.put("orientation", Integer.valueOf(i));
        contentValues.put("_data", str4);
        contentValues.put("_size", Long.valueOf(j2));
        contentValues.put("width", Integer.valueOf(i2));
        contentValues.put("height", Integer.valueOf(i3));
        if (location != null) {
            contentValues.put("latitude", Double.valueOf(location.getLatitude()));
            contentValues.put("longitude", Double.valueOf(location.getLongitude()));
        }
        Uri uri = null;
        String str6 = ", uri = ";
        String str7 = ", orientation = ";
        if (!z) {
            try {
                uri = context.getContentResolver().insert(getMediaUri(context, false, str4), contentValues);
                str5 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("insert: title ");
                sb2.append(str);
                sb2.append(str7);
                sb2.append(i);
                sb2.append(str6);
                sb2.append(uri);
                sb = sb2.toString();
            } catch (Exception e) {
                String str8 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Failed to write MediaStore: ");
                sb3.append(e.getMessage());
                Log.e(str8, sb3.toString());
            }
        } else {
            uri = context.getContentResolver().insert(getMediaUri(context, false, str4), contentValues);
            ParallelUtil.insertImageToParallelService(context, ContentUris.parseId(uri), str4);
            str5 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("parallel insert: title ");
            sb4.append(str);
            sb4.append(str7);
            sb4.append(i);
            sb4.append(str6);
            sb4.append(uri);
            sb = sb4.toString();
        }
        Log.d(str5, sb);
        return uri;
    }

    public static boolean isCurrentStorageIsSecondary() {
        String str = SECONDARY_STORAGE_PATH;
        return str != null && str.equals(sCurrentStoragePath);
    }

    public static boolean isDirectoryExistsAndCanWrite(String str) {
        File file = new File(str);
        return file.exists() && file.isDirectory() && file.canWrite();
    }

    public static boolean isDocumentPicture(String str) {
        return TextUtils.equals(DOCUMENT_PICTURE, str);
    }

    public static boolean isIdCardPicture(String str) {
        return isIdCardPictureOne(str) || isIdCardPictureTwo(str);
    }

    public static boolean isIdCardPictureOne(String str) {
        return TextUtils.equals(ID_CARD_PICTURE_1, str);
    }

    public static boolean isIdCardPictureTwo(String str) {
        return TextUtils.equals(ID_CARD_PICTURE_2, str);
    }

    public static boolean isLowStorageAtLastPoint() {
        return getLeftSpace() < LOW_STORAGE_THRESHOLD;
    }

    public static boolean isLowStorageSpace(String str) {
        return getAvailableSpace(str) < LOW_STORAGE_THRESHOLD;
    }

    public static boolean isPhoneStoragePriority() {
        return PRIMARY_STORAGE_PATH.equals(FIRST_CONSIDER_STORAGE_PATH);
    }

    public static boolean isRelatedStorage(Uri uri) {
        if (uri == null) {
            return false;
        }
        String path = uri.getPath();
        if (path != null) {
            return path.equals(PRIMARY_STORAGE_PATH) || path.equals(SECONDARY_STORAGE_PATH);
        }
        return false;
    }

    public static boolean isSaveToHidenFolder(String str) {
        return isDocumentPicture(str) || isIdCardPicture(str);
    }

    public static boolean isSecondPhoneStorage(String str) {
        return str != null && !TextUtils.isEmpty(SECONDARY_STORAGE_PATH) && str.startsWith(SECONDARY_STORAGE_PATH);
    }

    public static boolean isUseDocumentMode() {
        if (VERSION.SDK_INT >= 28 && !isUsePhoneStorage()) {
            return !C0122O00000o.instance().OO0oO0() || !isDirectoryExistsAndCanWrite(sCurrentStoragePath);
        }
        return false;
    }

    public static boolean isUsePhoneStorage() {
        return PRIMARY_STORAGE_PATH.equals(sCurrentStoragePath);
    }

    public static Uri newImage(Context context, String str, long j, int i, int i2, int i3, boolean z) {
        String generateFilepath4Image = generateFilepath4Image(str, z);
        ContentValues contentValues = new ContentValues(6);
        contentValues.put("datetaken", Long.valueOf(j));
        contentValues.put("orientation", Integer.valueOf(i));
        contentValues.put("_data", generateFilepath4Image);
        contentValues.put("width", Integer.valueOf(i2));
        contentValues.put("height", Integer.valueOf(i3));
        contentValues.put("mime_type", z ? MIME_HEIC : MIME_JPEG);
        Uri uri = null;
        try {
            uri = context.getContentResolver().insert(getMediaUri(context, false, generateFilepath4Image), contentValues);
            return uri;
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to new image");
            sb.append(e);
            Log.e(str2, sb.toString());
            return uri;
        }
    }

    public static void readSystemPriorityStorage() {
        boolean z;
        if (hasSecondaryStorage()) {
            z = PriorityStorageBroadcastReceiver.isPriorityStorage();
            CameraSettings.setPriorityStoragePreference(z);
        } else {
            z = false;
        }
        FIRST_CONSIDER_STORAGE_PATH = z ? SECONDARY_STORAGE_PATH : PRIMARY_STORAGE_PATH;
        sCurrentStoragePath = FIRST_CONSIDER_STORAGE_PATH;
        updateDirectory();
    }

    private static boolean renameSdcardFile(String str, String str2) {
        int i = 0;
        boolean z = false;
        do {
            i++;
            try {
                z = FileCompat.renameFile(str, str2);
                if (z) {
                    break;
                }
            } catch (IOException e) {
                Log.e(TAG, "renameSdcardFile failed", (Throwable) e);
            }
        } while (i < 5);
        return z;
    }

    public static void saveMorphoPanoramaOriginalPic(ByteBuffer byteBuffer, int i, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(DIRECTORY);
        sb.append(File.separator);
        sb.append(str);
        sb.append(File.separator);
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(File.separator);
        sb2.append(str);
        sb2.append("_");
        sb2.append(i);
        String generateFilepath4Image = generateFilepath4Image(sb2.toString(), false);
        FileChannel fileChannel = null;
        try {
            File file2 = new File(generateFilepath4Image);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            fileChannel = new FileOutputStream(file2, false).getChannel();
            fileChannel.write(byteBuffer);
            if (fileChannel == null) {
                return;
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("saveMorphoPanoramaOriginalPic  ");
            sb3.append(e.toString());
            Log.e(str2, sb3.toString());
            if (fileChannel == null) {
                return;
            }
        } finally {
            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (Exception unused) {
                }
            }
        }
        try {
            fileChannel.close();
        } catch (Exception unused2) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x007a A[SYNTHETIC, Splitter:B:22:0x007a] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0083 A[SYNTHETIC, Splitter:B:26:0x0083] */
    /* JADX WARNING: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveOriginalPic(byte[] bArr, int i, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(DIRECTORY);
        sb.append(File.separator);
        sb.append(str);
        sb.append(File.separator);
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(File.separator);
        sb2.append(str);
        sb2.append("_");
        sb2.append(i);
        FileOutputStream fileOutputStream = null;
        try {
            File file2 = new File(generateFilepath4Image(sb2.toString(), false));
            if (!file2.exists()) {
                file2.createNewFile();
            }
            FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
            try {
                fileOutputStream2.write(bArr);
            } catch (Exception e) {
                e = e;
                fileOutputStream = fileOutputStream2;
                try {
                    Log.e(TAG, "saveMorphoPanoramaOriginalPic exception occurs", (Throwable) e);
                    if (fileOutputStream == null) {
                    }
                } catch (Throwable th) {
                    th = th;
                    if (fileOutputStream != null) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception unused) {
                    }
                }
                throw th;
            }
            try {
                fileOutputStream2.flush();
                fileOutputStream2.close();
            } catch (Exception unused2) {
            }
        } catch (Exception e2) {
            e = e2;
            Log.e(TAG, "saveMorphoPanoramaOriginalPic exception occurs", (Throwable) e);
            if (fileOutputStream == null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
    }

    public static void saveToCloudAlbum(Context context, String str, long j, boolean z) {
        saveToCloudAlbum(context, str, j, false, -1, z);
    }

    public static void saveToCloudAlbum(Context context, String str, long j, boolean z, long j2, boolean z2) {
        saveToCloudAlbum(context, str, j, z, j2, z2, false);
    }

    public static void saveToCloudAlbum(Context context, String str, long j, boolean z, long j2, boolean z2, boolean z3) {
        context.sendBroadcast(getSaveToCloudIntent(context, str, j, z, j2, z2, z3));
    }

    public static boolean secondaryStorageMounted() {
        StringBuilder sb = new StringBuilder();
        sb.append(SECONDARY_STORAGE_PATH);
        sb.append(File.separator);
        sb.append(Environment.DIRECTORY_DCIM);
        return hasSecondaryStorage() && getAvailableSpace(sb.toString()) > 0;
    }

    private static void setLeftSpace(long j) {
        LEFT_SPACE.set(j);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setLeftSpace(");
        sb.append(j);
        sb.append(")");
        Log.i(str, sb.toString());
    }

    public static void setStorageListener(StorageListener storageListener) {
        if (storageListener != null) {
            sStorageListener = new WeakReference(storageListener);
        }
    }

    public static void switchStoragePathIfNeeded() {
        if (hasSecondaryStorage()) {
            String str = FIRST_CONSIDER_STORAGE_PATH;
            String str2 = SECONDARY_STORAGE_PATH;
            if (str.equals(str2)) {
                str2 = PRIMARY_STORAGE_PATH;
            }
            String str3 = sCurrentStoragePath;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            String str4 = CAMERA_STORAGE_PATH_SUFFIX;
            sb.append(str4);
            if (!isLowStorageSpace(sb.toString())) {
                sCurrentStoragePath = str;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(str4);
                if (!isLowStorageSpace(sb2.toString())) {
                    sCurrentStoragePath = str2;
                } else {
                    return;
                }
            }
            if (!sCurrentStoragePath.equals(str3)) {
                updateDirectory();
                WeakReference weakReference = sStorageListener;
                if (!(weakReference == null || weakReference.get() == null)) {
                    ((StorageListener) sStorageListener.get()).onStoragePathChanged();
                }
            }
            String str5 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Storage path is switched path = ");
            sb3.append(DIRECTORY);
            sb3.append(", FIRST_CONSIDER_STORAGE_PATH = ");
            sb3.append(FIRST_CONSIDER_STORAGE_PATH);
            sb3.append(", SECONDARY_STORAGE_PATH = ");
            sb3.append(SECONDARY_STORAGE_PATH);
            Log.d(str5, sb3.toString());
        }
    }

    public static void switchToPhoneStorage() {
        String str = PRIMARY_STORAGE_PATH;
        FIRST_CONSIDER_STORAGE_PATH = str;
        if (!str.equals(sCurrentStoragePath)) {
            Log.v(TAG, "switchToPhoneStorage");
            sCurrentStoragePath = PRIMARY_STORAGE_PATH;
            updateDirectory();
            WeakReference weakReference = sStorageListener;
            if (weakReference != null && weakReference.get() != null) {
                ((StorageListener) sStorageListener.get()).onStoragePathChanged();
            }
        }
    }

    private static void updateDirectory() {
        StringBuilder sb = new StringBuilder();
        sb.append(sCurrentStoragePath);
        sb.append(CAMERA_STORAGE_PATH_SUFFIX);
        DIRECTORY = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(DIRECTORY);
        sb2.append(RAW_PATH_SUFFIX);
        RAW_DIRECTORY = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sCurrentStoragePath);
        sb3.append(HIDE_CAMERA_STORAGE_PATH_SUFFIX);
        HIDEDIRECTORY = sb3.toString();
        BUCKET_ID = DIRECTORY.toLowerCase(Locale.ENGLISH).hashCode();
    }

    private static byte[] updateExif(byte[] bArr, boolean z, String str, PictureInfo pictureInfo, int i, int i2, int i3) {
        if (!z && TextUtils.isEmpty(str) && pictureInfo == null && TextUtils.isEmpty(Util.MARKET_NAME)) {
            return bArr;
        }
        long currentTimeMillis = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ExifInterface exifInterface = new ExifInterface();
            exifInterface.readExif(bArr);
            if (z) {
                exifInterface.addParallelProcessComment("processing", i, i2, i3);
            }
            if (!TextUtils.isEmpty(str)) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("save algorithm: ");
                sb.append(str);
                Log.d(str2, sb.toString());
                exifInterface.addAlgorithmComment(str);
            }
            if (pictureInfo != null) {
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("save xiaomi comment: ");
                sb2.append(pictureInfo.getInfoString());
                sb2.append(", aiType = ");
                sb2.append(pictureInfo.getAiType());
                Log.d(str3, sb2.toString());
                exifInterface.addAiType(pictureInfo.getAiType());
                if (pictureInfo.isBokehFrontCamera()) {
                    exifInterface.addFrontMirror(pictureInfo.isFrontMirror() ? 1 : 0);
                }
                pictureInfo.setAfRoi(i, i2, i3);
                exifInterface.addXpComment(pictureInfo.getXpCommentBytes());
                exifInterface.addXiaomiComment(pictureInfo.getInfoString());
            }
            if (!TextUtils.isEmpty(Util.MARKET_NAME)) {
                exifInterface.addXiaomiProduct(Util.MARKET_NAME);
            }
            exifInterface.writeExif(bArr, (OutputStream) byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            bArr = byteArray;
        } catch (Exception e) {
            String str4 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("updateExif error ");
            sb3.append(e.getMessage());
            Log.e(str4, sb3.toString(), (Throwable) e);
        }
        String str5 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("update exif cost=");
        sb4.append(System.currentTimeMillis() - currentTimeMillis);
        Log.v(str5, sb4.toString());
        return bArr;
    }

    public static boolean updateHeifInfo(Context context, Uri uri, String str, Location location, int i, int i2, int i3) {
        long length = new File(str).length();
        String fileTitleFromPath = Util.getFileTitleFromPath(str);
        ContentValues contentValues = new ContentValues(10);
        contentValues.put("_data", str);
        contentValues.put("title", fileTitleFromPath);
        StringBuilder sb = new StringBuilder();
        sb.append(fileTitleFromPath);
        sb.append(HEIC_SUFFIX);
        contentValues.put("_display_name", sb.toString());
        contentValues.put("mime_type", MIME_HEIC);
        contentValues.put("orientation", Integer.valueOf(i));
        contentValues.put("_size", Long.valueOf(length));
        contentValues.put("width", Integer.valueOf(i2));
        contentValues.put("height", Integer.valueOf(i3));
        if (location != null) {
            contentValues.put("latitude", Double.valueOf(location.getLatitude()));
            contentValues.put("longitude", Double.valueOf(location.getLongitude()));
        }
        context.getContentResolver().update(uri, contentValues, null, null);
        saveToCloudAlbum(context, str, length, location == null);
        return true;
    }

    public static Uri updateImage(Context context, byte[] bArr, boolean z, ExifInterface exifInterface, Uri uri, String str, Location location, int i, int i2, int i3, String str2) {
        return updateImage(context, bArr, z, exifInterface, uri, str, location, i, i2, i3, str2, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:147:0x0248, code lost:
        if (r7 != null) goto L_0x024a;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00a5 */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x016e  */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x017d  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x01dc  */
    /* JADX WARNING: Removed duplicated region for block: B:134:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:137:0x01f3  */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x01f5  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x01fa  */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x0248  */
    /* JADX WARNING: Removed duplicated region for block: B:155:0x025c A[SYNTHETIC, Splitter:B:155:0x025c] */
    /* JADX WARNING: Removed duplicated region for block: B:162:0x02b3  */
    /* JADX WARNING: Removed duplicated region for block: B:168:0x02d9  */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x0301  */
    /* JADX WARNING: Removed duplicated region for block: B:175:0x0327  */
    /* JADX WARNING: Removed duplicated region for block: B:179:0x0341  */
    /* JADX WARNING: Removed duplicated region for block: B:189:0x0350  */
    /* JADX WARNING: Removed duplicated region for block: B:198:0x035e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Uri updateImage(Context context, byte[] bArr, boolean z, ExifInterface exifInterface, Uri uri, String str, Location location, int i, int i2, int i3, String str2, boolean z2) {
        byte[] bArr2;
        boolean z3;
        String str3;
        boolean z4;
        String str4;
        Cursor cursor;
        Cursor query;
        Cursor cursor2;
        Cursor cursor3;
        boolean z5;
        String sb;
        boolean z6;
        String str5;
        boolean z7;
        String str6;
        Throwable th;
        OutputStream bufferedOutputStream;
        OutputStream outputStream;
        Throwable th2;
        Throwable th3;
        final Context context2 = context;
        bArr2 = bArr;
        boolean z8 = z;
        ExifInterface exifInterface2 = exifInterface;
        Uri uri2 = uri;
        String str7 = str;
        String str8 = str2;
        String str9 = "datetaken";
        boolean z9 = true;
        z3 = str8 == null && isUseDocumentMode();
        String generateFilepath4Image = generateFilepath4Image(str7, z8);
        if (z3) {
            str3 = generateFilepath4Image;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str8 != null ? generateFilepath4Image(str8, z8) : generateFilepath4Image);
            sb2.append(".tmp");
            str3 = sb2.toString();
        }
        File file = new File(str3);
        String str10 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("updateImage uri-> ");
        sb3.append(uri2);
        sb3.append(", overwrite -> ");
        sb3.append(z3);
        sb3.append(", tmpFile ");
        sb3.append(str3);
        Log.d(str10, sb3.toString());
        if (bArr2 != null) {
            int i4 = 0;
            while (true) {
                try {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(bArr2));
                    try {
                        if (isUseDocumentMode()) {
                            try {
                                bufferedOutputStream = FileCompat.getFileOutputStream(str3, z9);
                            } catch (Throwable th4) {
                                th = th4;
                                z4 = z3;
                                str6 = str3;
                                throw th;
                            }
                        } else {
                            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                        }
                        outputStream = bufferedOutputStream;
                        if (exifInterface2 == null) {
                            str6 = str3;
                            byte[] bArr3 = new byte[4096];
                            while (true) {
                                int read = bufferedInputStream.read(bArr3);
                                z4 = z3;
                                if (read == -1) {
                                    break;
                                }
                                try {
                                    outputStream.write(bArr3, 0, read);
                                    z3 = z4;
                                } catch (Throwable th5) {
                                    th = th5;
                                    th2 = th;
                                    throw th2;
                                }
                            }
                        } else {
                            try {
                                exifInterface2.writeExif(bArr2, outputStream);
                                z4 = z3;
                                str6 = str3;
                            } catch (IOException ) {
                                try {
                                    str6 = str3;
                                    Log.e(TAG, "Failed to rewrite Exif");
                                    outputStream.write(bArr2);
                                    z4 = z3;
                                } catch (Throwable th6) {
                                    th = th6;
                                    z4 = z3;
                                    th2 = th;
                                    throw th2;
                                }
                            } catch (Throwable th7) {
                                th = th7;
                                z4 = z3;
                                str6 = str3;
                                th2 = th;
                                throw th2;
                            }
                        }
                        if (outputStream != null) {
                            th3 = null;
                            try {
                                $closeResource(null, outputStream);
                            } catch (Throwable th8) {
                                th = th8;
                            }
                        } else {
                            th3 = null;
                        }
                        try {
                            $closeResource(th3, bufferedInputStream);
                            z7 = false;
                            break;
                        } catch (Exception e) {
                            e = e;
                        } catch (Throwable th9) {
                            Throwable th10 = th9;
                            $closeResource(th, bufferedInputStream);
                            throw th10;
                        }
                    } catch (Throwable th11) {
                        th = th11;
                        z4 = z3;
                        str6 = str3;
                    }
                } catch (Exception e2) {
                    e = e2;
                    z4 = z3;
                    str6 = str3;
                    dumpExceptionEnv(e, generateFilepath4Image);
                    Log.e(TAG, "Failed to write image", (Throwable) e);
                    i4++;
                    if (Util.isQuotaExceeded(e) && (context2 instanceof ActivityBase)) {
                        ActivityBase activityBase = (ActivityBase) context2;
                        if (!activityBase.isActivityPaused()) {
                            activityBase.runOnUiThread(new Runnable() {
                                public void run() {
                                    ToastUtils.showToast(context2, (int) R.string.spaceIsLow_content_primary_storage_priority);
                                }
                            });
                        }
                        i4 = MAX_WRITE_RETRY;
                    } else if (i4 < MAX_WRITE_RETRY) {
                        System.gc();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException unused) {
                        }
                    }
                    if (i4 >= MAX_WRITE_RETRY) {
                        z7 = true;
                        if (z7) {
                            return null;
                        }
                        str4 = str5;
                        ContentValues contentValues = new ContentValues();
                        query = context.getContentResolver().query(uri2, new String[]{str9}, null, null);
                        try {
                            extractDateTaken(query, contentValues);
                            if (query != null) {
                            }
                            long length = file.length();
                            String str11 = "renameTo failed, tmpPath = ";
                            if (!isUseDocumentMode()) {
                            }
                            ContentValues contentValues2 = new ContentValues(10);
                            contentValues2.put("title", str7);
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str7);
                            sb4.append(z8 ? HEIC_SUFFIX : JPEG_SUFFIX);
                            contentValues2.put("_display_name", sb4.toString());
                            if (isUseDocumentMode()) {
                            }
                            String str12 = "_data";
                            if (bArr2 != null) {
                            }
                            contentValues2.put(str12, r4);
                            try {
                                try {
                                    cursor2 = context.getContentResolver().query(uri2, new String[]{str9}, null, null);
                                    if (cursor2 != null) {
                                    }
                                    contentValues.putAll(contentValues2);
                                    z5 = false;
                                    Uri insert = context.getContentResolver().insert(getMediaUri(context2, false, generateFilepath4Image), contentValues);
                                    String str13 = TAG;
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append("updateImage create new uri ");
                                    sb5.append(insert);
                                    Log.d(str13, sb5.toString());
                                    uri2 = insert;
                                    if (cursor2 != null) {
                                    }
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append(generateFilepath4Image);
                                    sb6.append(".mid");
                                    sb = sb6.toString();
                                    String str14 = " to ";
                                    String str15 = "fail to rename ";
                                    if (!renameSdcardFile(generateFilepath4Image, sb)) {
                                    }
                                    FileCompat.removeDocumentFileForPath(str4);
                                    FileCompat.removeDocumentFileForPath(generateFilepath4Image);
                                    if (!z6) {
                                    }
                                    long length2 = (long) bArr2.length;
                                    if (location == null) {
                                    }
                                    saveToCloudAlbum(context2, generateFilepath4Image, length2, z5);
                                    return uri2;
                                } catch (Throwable th12) {
                                    th = th12;
                                    cursor3 = null;
                                    cursor2 = cursor3;
                                    if (cursor2 != null) {
                                    }
                                    throw th;
                                }
                            } catch (Throwable th13) {
                                th = th13;
                                cursor3 = null;
                                cursor2 = cursor3;
                                if (cursor2 != null) {
                                }
                                throw th;
                            }
                        } catch (Throwable th14) {
                            th = th14;
                            cursor = query;
                            if (cursor != null) {
                                cursor.close();
                            }
                            throw th;
                        }
                    } else {
                        z9 = true;
                        str3 = str6;
                        z3 = z4;
                    }
                }
                z9 = true;
                str3 = str6;
                z3 = z4;
            }
            throw r13;
        }
        z4 = z3;
        str5 = str3;
        if (str8 != null) {
            str4 = generateFilepath4Image(str8, z8);
            ContentValues contentValues3 = new ContentValues();
            query = context.getContentResolver().query(uri2, new String[]{str9}, null, null);
            extractDateTaken(query, contentValues3);
            if (query != null) {
                query.close();
            }
            long length3 = file.length();
            String str112 = "renameTo failed, tmpPath = ";
            if (!isUseDocumentMode()) {
                boolean renameTo = file.renameTo(new File(generateFilepath4Image));
                if (!(exifInterface2 == null || str8 == null)) {
                    try {
                        new File(generateFilepath4Image(str8, z8)).delete();
                    } catch (Exception e3) {
                        String str16 = TAG;
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("Exception when delete old file ");
                        sb7.append(str8);
                        Log.e(str16, sb7.toString(), (Throwable) e3);
                    }
                }
                if (!renameTo) {
                    String str17 = TAG;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(str112);
                    sb8.append(str4);
                    Log.w(str17, sb8.toString());
                    return null;
                }
            }
            ContentValues contentValues22 = new ContentValues(10);
            contentValues22.put("title", str7);
            StringBuilder sb42 = new StringBuilder();
            sb42.append(str7);
            sb42.append(z8 ? HEIC_SUFFIX : JPEG_SUFFIX);
            contentValues22.put("_display_name", sb42.toString());
            String str18 = isUseDocumentMode() ? str4 : generateFilepath4Image;
            String str122 = "_data";
            if (bArr2 != null) {
                contentValues22.put("mime_type", z8 ? MIME_HEIC : MIME_JPEG);
                contentValues22.put("orientation", Integer.valueOf(i));
                contentValues22.put("_size", Long.valueOf(length3));
                contentValues22.put("width", Integer.valueOf(i2));
                contentValues22.put("height", Integer.valueOf(i3));
                if (location != null) {
                    contentValues22.put("latitude", Double.valueOf(location.getLatitude()));
                    contentValues22.put("longitude", Double.valueOf(location.getLongitude()));
                }
            }
            contentValues22.put(str122, str18);
            cursor2 = context.getContentResolver().query(uri2, new String[]{str9}, null, null);
            if (cursor2 != null) {
                try {
                    if (cursor2.getCount() > 0) {
                        int update = context.getContentResolver().update(uri2, contentValues22, null, null);
                        String str19 = TAG;
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("updateImage update uri ");
                        sb9.append(uri2);
                        sb9.append(", ret->");
                        sb9.append(update);
                        Log.d(str19, sb9.toString());
                        z5 = false;
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        if (!z4 && isUseDocumentMode()) {
                            StringBuilder sb62 = new StringBuilder();
                            sb62.append(generateFilepath4Image);
                            sb62.append(".mid");
                            sb = sb62.toString();
                            String str142 = " to ";
                            String str152 = "fail to rename ";
                            if (!renameSdcardFile(generateFilepath4Image, sb)) {
                                z6 = renameSdcardFile(str4, generateFilepath4Image);
                                if (z6) {
                                    deleteSdcardFile(sb);
                                } else {
                                    String str20 = TAG;
                                    StringBuilder sb10 = new StringBuilder();
                                    sb10.append(str152);
                                    sb10.append(str4);
                                    sb10.append(str142);
                                    sb10.append(generateFilepath4Image);
                                    Log.w(str20, sb10.toString());
                                    deleteSdcardFile(str4);
                                }
                            } else {
                                String str21 = TAG;
                                StringBuilder sb11 = new StringBuilder();
                                sb11.append(str152);
                                sb11.append(generateFilepath4Image);
                                sb11.append(str142);
                                sb11.append(sb);
                                Log.w(str21, sb11.toString());
                                deleteSdcardFile(sb);
                                z6 = z5;
                            }
                            FileCompat.removeDocumentFileForPath(str4);
                            FileCompat.removeDocumentFileForPath(generateFilepath4Image);
                            if (!z6) {
                                String str22 = TAG;
                                StringBuilder sb12 = new StringBuilder();
                                sb12.append(str112);
                                sb12.append(str4);
                                Log.w(str22, sb12.toString());
                                return null;
                            }
                        }
                        long length22 = (long) bArr2.length;
                        if (location == null) {
                            z5 = true;
                        }
                        saveToCloudAlbum(context2, generateFilepath4Image, length22, z5);
                        return uri2;
                    }
                } catch (Throwable th15) {
                    th = th15;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            }
            contentValues3.putAll(contentValues22);
            z5 = false;
            Uri insert2 = context.getContentResolver().insert(getMediaUri(context2, false, generateFilepath4Image), contentValues3);
            String str132 = TAG;
            StringBuilder sb52 = new StringBuilder();
            sb52.append("updateImage create new uri ");
            sb52.append(insert2);
            Log.d(str132, sb52.toString());
            uri2 = insert2;
            if (cursor2 != null) {
            }
            StringBuilder sb622 = new StringBuilder();
            sb622.append(generateFilepath4Image);
            sb622.append(".mid");
            sb = sb622.toString();
            String str1422 = " to ";
            String str1522 = "fail to rename ";
            if (!renameSdcardFile(generateFilepath4Image, sb)) {
            }
            FileCompat.removeDocumentFileForPath(str4);
            FileCompat.removeDocumentFileForPath(generateFilepath4Image);
            if (!z6) {
            }
            long length222 = (long) bArr2.length;
            if (location == null) {
            }
            saveToCloudAlbum(context2, generateFilepath4Image, length222, z5);
            return uri2;
        }
        str4 = str5;
        ContentValues contentValues32 = new ContentValues();
        try {
            try {
                query = context.getContentResolver().query(uri2, new String[]{str9}, null, null);
                extractDateTaken(query, contentValues32);
                if (query != null) {
                }
                long length32 = file.length();
                String str1122 = "renameTo failed, tmpPath = ";
                if (!isUseDocumentMode()) {
                }
                ContentValues contentValues222 = new ContentValues(10);
                contentValues222.put("title", str7);
                StringBuilder sb422 = new StringBuilder();
                sb422.append(str7);
                sb422.append(z8 ? HEIC_SUFFIX : JPEG_SUFFIX);
                contentValues222.put("_display_name", sb422.toString());
                if (isUseDocumentMode()) {
                }
                String str1222 = "_data";
                if (bArr2 != null) {
                }
                contentValues222.put(str1222, str18);
                cursor2 = context.getContentResolver().query(uri2, new String[]{str9}, null, null);
                if (cursor2 != null) {
                }
                contentValues32.putAll(contentValues222);
                z5 = false;
                Uri insert22 = context.getContentResolver().insert(getMediaUri(context2, false, generateFilepath4Image), contentValues32);
                String str1322 = TAG;
                StringBuilder sb522 = new StringBuilder();
                sb522.append("updateImage create new uri ");
                sb522.append(insert22);
                Log.d(str1322, sb522.toString());
                uri2 = insert22;
                if (cursor2 != null) {
                }
                StringBuilder sb6222 = new StringBuilder();
                sb6222.append(generateFilepath4Image);
                sb6222.append(".mid");
                sb = sb6222.toString();
                String str14222 = " to ";
                String str15222 = "fail to rename ";
                if (!renameSdcardFile(generateFilepath4Image, sb)) {
                }
                FileCompat.removeDocumentFileForPath(str4);
                FileCompat.removeDocumentFileForPath(generateFilepath4Image);
                if (!z6) {
                }
                long length2222 = (long) bArr2.length;
                if (location == null) {
                }
                saveToCloudAlbum(context2, generateFilepath4Image, length2222, z5);
                return uri2;
            } catch (Throwable th16) {
                th = th16;
                cursor = null;
                if (cursor != null) {
                }
                throw th;
            }
        } catch (Throwable th17) {
            th = th17;
            cursor = null;
            if (cursor != null) {
            }
            throw th;
        }
        th = th;
        throw th;
    }

    public static boolean updateImageSize(ContentResolver contentResolver, Uri uri, long j) {
        ContentValues contentValues = new ContentValues(1);
        contentValues.put("_size", Long.valueOf(j));
        try {
            contentResolver.update(uri, contentValues, null, null);
            return true;
        } catch (Exception e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to updateMediaStore");
            sb.append(e);
            Log.e(str, sb.toString());
            return false;
        }
    }

    public static Uri updateImageWithExtraExif(Context context, byte[] bArr, boolean z, ExifInterface exifInterface, Uri uri, String str, Location location, int i, int i2, int i3, String str2, boolean z2, boolean z3, String str3, PictureInfo pictureInfo) {
        return updateImage(context, updateExif(bArr, z3, str3, pictureInfo, i, i2, i3), z, exifInterface, uri, str, location, i, i2, i3, str2);
    }
}
