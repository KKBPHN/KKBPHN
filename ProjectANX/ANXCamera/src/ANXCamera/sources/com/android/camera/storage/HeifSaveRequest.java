package com.android.camera.storage;

import android.content.ContentUris;
import android.content.Context;
import android.hardware.camera2.CaptureResult;
import android.location.Location;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageWriter;
import android.net.Uri;
import android.os.Handler;
import android.util.Size;
import androidx.annotation.NonNull;
import androidx.heifwriter.HeifWriter;
import androidx.heifwriter.HeifWriter.Builder;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.db.DbRepository;
import com.android.camera.db.element.SaveTask;
import com.android.camera.log.Log;
import com.android.gallery3d.exif.ExifInterface;
import com.android.gallery3d.exif.ExifOutputStream;
import com.xiaomi.camera.base.ImageUtil;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.ParallelTaskDataParameter;
import com.xiaomi.camera.parallelservice.util.ParallelUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class HeifSaveRequest implements SaveRequest {
    private static final String TAG = "HeifSaveRequest";
    private CaptureResult mCaptureResult;
    private Context mContext;
    private Handler mHandler;
    private int mHeight;
    private ImageWriter mImageWriter;
    private SaveHeifCallback mParent;
    private SaverCallback mSaverCallback;
    private int mSize;
    private ParallelTaskData mTaskData;
    private int mWidth;
    private Image mYuvImage;

    public interface SaveHeifCallback {
        void onSaveFinish(Image image, ParallelTaskData parallelTaskData);
    }

    public HeifSaveRequest(@NonNull Image image, @NonNull CaptureResult captureResult, @NonNull ParallelTaskData parallelTaskData, @NonNull SaveHeifCallback saveHeifCallback, Handler handler) {
        int i;
        this.mYuvImage = image;
        this.mCaptureResult = captureResult;
        this.mTaskData = parallelTaskData;
        this.mParent = saveHeifCallback;
        this.mHandler = handler;
        Size outputSize = parallelTaskData.getDataParameter().getOutputSize();
        if (outputSize.getWidth() == outputSize.getHeight()) {
            this.mWidth = outputSize.getWidth();
            i = outputSize.getHeight();
        } else {
            this.mWidth = image.getWidth();
            i = image.getHeight();
        }
        this.mHeight = i;
        this.mSize = ((this.mWidth * this.mHeight) * 3) / 2;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("HeifSaveRequest: size = ");
        sb.append(this.mWidth);
        sb.append("x");
        sb.append(this.mHeight);
        Log.c(str, sb.toString());
    }

    private byte[] buildExif(CaptureResult captureResult, int i, int i2, int i3, long j, Location location) {
        ExifInterface exifInterface = new ExifInterface();
        if (captureResult != null) {
            Util.appendExifInfo(i, i2, i3, j, location, captureResult, 0, exifInterface, false);
        }
        byte[] bArr = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStream exifWriterStream = exifInterface.getExifWriterStream((OutputStream) byteArrayOutputStream);
            if (exifWriterStream instanceof ExifOutputStream) {
                ((ExifOutputStream) exifWriterStream).writeExifForHeif();
                exifWriterStream.flush();
                bArr = byteArrayOutputStream.toByteArray();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("buildExif: resultLen = ");
                sb.append(bArr == null ? "null" : Integer.valueOf(bArr.length));
                Log.c(str, sb.toString());
            } else {
                Log.e(TAG, "buildExif: ExifOutputStream is required");
            }
            exifWriterStream.close();
        } catch (IOException e) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("buildExif: ");
            sb2.append(e.getMessage());
            Log.e(str2, sb2.toString(), (Throwable) e);
        }
        return bArr;
    }

    private byte[] imageToBuffer(Image image, Size size) {
        Plane[] planes = image.getPlanes();
        if (planes == null || planes.length < 3) {
            Log.e(TAG, "imageToBuffer: require 3 planes yuv image");
            return null;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        if (size.getWidth() == size.getHeight()) {
            return Util.getSubYuvImage(image, new int[]{((width - size.getWidth()) / 2) & -4, ((height - size.getHeight()) / 2) & -4, size.getWidth(), size.getHeight()});
        }
        int i = width * height;
        int i2 = (i * 3) / 2;
        int i3 = i2 - i;
        byte[] bArr = new byte[i2];
        ImageUtil.removePadding(planes[0], width, height).get(bArr, 0, i);
        ByteBuffer removePadding = ImageUtil.removePadding(planes[2], width / 2, height / 2);
        removePadding.get(bArr, i, Math.min(removePadding.limit(), i3));
        return bArr;
    }

    private boolean saveHeifFile(Image image, byte[] bArr, String str, Handler handler, int i, int i2, int i3) {
        boolean z;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            Builder builder = new Builder(str, i, i2, 1);
            HeifWriter build = builder.setHandler(handler).setQuality(i3).setRotation(this.mTaskData.getDataParameter().getJpegRotation()).build();
            this.mImageWriter = ImageWriter.newInstance(build.getInputSurface(), 2, image.getFormat());
            build.start();
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("HeifWriter.start cost ");
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            Log.c(str2, sb.toString());
            this.mImageWriter.queueInputImage(image);
            if (bArr == null || bArr.length <= 0) {
                Log.w(TAG, "saveHeif: no exif data");
            } else {
                long currentTimeMillis2 = System.currentTimeMillis();
                build.addExifData(0, bArr, 0, bArr.length);
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("HeifWriter.addExifData cost ");
                sb2.append(System.currentTimeMillis() - currentTimeMillis2);
                Log.c(str3, sb2.toString());
            }
            long currentTimeMillis3 = System.currentTimeMillis();
            build.stop(0);
            String str4 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("HeifWriter.stop cost ");
            sb3.append(System.currentTimeMillis() - currentTimeMillis3);
            Log.c(str4, sb3.toString());
            long currentTimeMillis4 = System.currentTimeMillis();
            build.close();
            String str5 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("HeifWriter.close cost ");
            sb4.append(System.currentTimeMillis() - currentTimeMillis4);
            Log.c(str5, sb4.toString());
            z = true;
        } catch (Exception e) {
            String str6 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("saveHeif exception: ");
            sb5.append(e.getMessage());
            Log.e(str6, sb5.toString(), (Throwable) e);
            z = false;
        }
        ImageWriter imageWriter = this.mImageWriter;
        if (imageWriter != null) {
            imageWriter.close();
            this.mImageWriter = null;
        }
        Log.d(TAG, "saveHeif: cost %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return z;
    }

    private boolean saveHeifFile(Image image, byte[] bArr, String str, Size size, Handler handler, int i, int i2, int i3) {
        boolean z;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            Builder builder = new Builder(str, i, i2, 0);
            HeifWriter build = builder.setHandler(handler).setQuality(i3).setRotation(this.mTaskData.getDataParameter().getJpegRotation()).build();
            build.start();
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("HeifWriter.start cost ");
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            Log.c(str2, sb.toString());
            long currentTimeMillis2 = System.currentTimeMillis();
            byte[] imageToBuffer = imageToBuffer(image, size);
            Log.d(TAG, "prepare buffer cost %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis2));
            long currentTimeMillis3 = System.currentTimeMillis();
            build.addYuvBuffer(35, imageToBuffer);
            Log.d(TAG, "HeifWriter.addYuvBuffer cost %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis3));
            if (bArr == null || bArr.length <= 0) {
                Log.w(TAG, "saveHeif: no exif data");
            } else {
                long currentTimeMillis4 = System.currentTimeMillis();
                build.addExifData(0, bArr, 0, bArr.length);
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("HeifWriter.addExifData cost ");
                sb2.append(System.currentTimeMillis() - currentTimeMillis4);
                Log.c(str3, sb2.toString());
            }
            long currentTimeMillis5 = System.currentTimeMillis();
            build.stop(0);
            String str4 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("HeifWriter.stop cost ");
            sb3.append(System.currentTimeMillis() - currentTimeMillis5);
            Log.c(str4, sb3.toString());
            long currentTimeMillis6 = System.currentTimeMillis();
            build.close();
            String str5 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("HeifWriter.close cost ");
            sb4.append(System.currentTimeMillis() - currentTimeMillis6);
            Log.c(str5, sb4.toString());
            z = true;
        } catch (Exception e) {
            String str6 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("saveHeif exception: ");
            sb5.append(e.getMessage());
            Log.e(str6, sb5.toString(), (Throwable) e);
            z = false;
        }
        Log.d(TAG, "saveHeif: cost %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return z;
    }

    private Uri saveHeifInfo(Context context, String str, long j, Location location, int i, int i2, int i3, boolean z) {
        Context context2 = context;
        String str2 = str;
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("saveHeifInfo: E. ");
        sb.append(i);
        String str4 = " | ";
        sb.append(str4);
        sb.append(i2);
        sb.append(str4);
        sb.append(i3);
        Log.d(str3, sb.toString());
        String fileTitleFromPath = Util.getFileTitleFromPath(str);
        SaveTask itemByPath = DbRepository.dbItemSaveTask().getItemByPath(str2);
        String str5 = "|";
        if (itemByPath == null || !itemByPath.isValid()) {
            String str6 = TAG;
            Object[] objArr = new Object[1];
            objArr[0] = itemByPath == null ? "null" : "invalid";
            Log.w(str6, "saveHeifInfo: %s task", objArr);
            Uri addHeifInfo = Storage.addHeifInfo(context, str, location, j, i, i2, i3, z);
            if (addHeifInfo != null) {
                if (this.mTaskData.isNeedThumbnail()) {
                    Thumbnail createThumbnailFromUri = Thumbnail.createThumbnailFromUri(context2, addHeifInfo, false);
                    if (createThumbnailFromUri != null) {
                        this.mSaverCallback.postUpdateThumbnail(createThumbnailFromUri, true);
                    } else {
                        this.mSaverCallback.postHideThumbnailProgressing();
                    }
                }
                this.mSaverCallback.notifyNewMediaData(addHeifInfo, fileTitleFromPath, 2);
                if (itemByPath != null) {
                    String str7 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("algo mark: ");
                    sb2.append(addHeifInfo.toString());
                    Log.d(str7, sb2.toString());
                    itemByPath.setMediaStoreId(Long.valueOf(ContentUris.parseId(addHeifInfo)));
                    ParallelUtil.markTaskFinish(this.mContext, itemByPath, false);
                }
                String str8 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("saveHeifInfo: X. added ");
                sb3.append(fileTitleFromPath);
                sb3.append(str5);
                sb3.append(addHeifInfo);
                Log.d(str8, sb3.toString());
            }
            return addHeifInfo;
        }
        Uri withAppendedId = ContentUris.withAppendedId(Storage.getMediaUri(context2, false, str2), itemByPath.getMediaStoreId().longValue());
        Storage.updateHeifInfo(context, withAppendedId, str, location, i, i2, i3);
        ParallelUtil.markTaskFinish(context2, itemByPath, false);
        String str9 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("saveHeifInfo: X. updated ");
        sb4.append(fileTitleFromPath);
        sb4.append(str5);
        sb4.append(withAppendedId);
        Log.d(str9, sb4.toString());
        return withAppendedId;
    }

    public int getSize() {
        return this.mSize;
    }

    public boolean isFinal() {
        return true;
    }

    public void onFinish() {
        this.mParent.onSaveFinish(this.mYuvImage, this.mTaskData);
        this.mYuvImage = null;
        this.mTaskData = null;
        this.mParent = null;
        this.mCaptureResult = null;
        this.mHandler = null;
        this.mSaverCallback.onSaveFinish(getSize());
        this.mSaverCallback = null;
    }

    public void run() {
        save();
        onFinish();
    }

    public void save() {
        boolean z;
        String savePath = this.mTaskData.getSavePath();
        StringBuilder sb = new StringBuilder();
        sb.append(savePath);
        sb.append(".tmp.HEIC");
        String sb2 = sb.toString();
        long dateTakenTime = this.mTaskData.getDateTakenTime();
        ParallelTaskDataParameter dataParameter = this.mTaskData.getDataParameter();
        int jpegQuality = dataParameter.getJpegQuality();
        int jpegRotation = dataParameter.getJpegRotation();
        Location location = dataParameter.getLocation();
        Log.d(TAG, "save: E. path=%s quality=%d jpegRotation=%d", savePath, Integer.valueOf(jpegQuality), Integer.valueOf(jpegRotation));
        long currentTimeMillis = System.currentTimeMillis();
        byte[] buildExif = buildExif(this.mCaptureResult, this.mWidth, this.mHeight, jpegRotation, dateTakenTime, location);
        String str = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("prepare exif cost ");
        sb3.append(System.currentTimeMillis() - currentTimeMillis);
        Log.c(str, sb3.toString());
        int i = this.mWidth;
        int i2 = this.mHeight;
        Image image = this.mYuvImage;
        if (i != i2) {
            z = saveHeifFile(image, buildExif, sb2, this.mHandler, i, i2, jpegQuality);
        } else {
            z = saveHeifFile(image, buildExif, sb2, this.mTaskData.getDataParameter().getOutputSize(), this.mHandler, this.mWidth, this.mHeight, jpegQuality);
        }
        File file = new File(sb2);
        if (z) {
            file.renameTo(new File(savePath));
            saveHeifInfo(this.mContext, savePath, dateTakenTime, location, jpegRotation, this.mWidth, this.mHeight, true);
            return;
        }
        String str2 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("failed to save heif: ");
        sb4.append(savePath);
        Log.w(str2, sb4.toString());
        file.delete();
    }

    public void setContextAndCallback(Context context, SaverCallback saverCallback) {
        this.mContext = context;
        this.mSaverCallback = saverCallback;
    }
}
