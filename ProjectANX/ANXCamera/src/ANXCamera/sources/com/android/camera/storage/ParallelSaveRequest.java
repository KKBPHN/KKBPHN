package com.android.camera.storage;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.db.DbRepository;
import com.android.camera.db.element.SaveTask;
import com.android.camera.log.Log;
import com.android.camera.statistic.ScenarioTrackUtil;
import com.android.gallery3d.exif.ExifHelper;
import com.android.gallery3d.exif.ExifInterface;
import com.android.zxing.DocumentDecoder;
import com.android.zxing.PreviewImage;
import com.xiaomi.camera.base.PerformanceTracker;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.ParallelTaskDataParameter;
import com.xiaomi.camera.core.PictureInfo;
import com.xiaomi.camera.parallelservice.util.ParallelUtil;
import com.xiaomi.ocr.sdk.imgprocess.DocumentProcess;
import com.xiaomi.ocr.sdk.imgprocess.DocumentProcess.DocumentType;
import com.xiaomi.ocr.sdk.imgprocess.DocumentProcess.EnhanceType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Locale;

public final class ParallelSaveRequest extends AbstractSaveRequest {
    private static final String TAG = "ParallelSaveRequest";
    private boolean isHeic = isHeicSavingRequest();
    private String mAlgorithmName;
    private Context mContext;
    private byte[] mData;
    private PictureInfo mInfo;
    private int mJpegRotation;
    private Location mLocation;
    private boolean mNeedThumbnail;
    private String mSavePath;
    private SaverCallback mSaverCallback;
    private int mSize = calculateMemoryUsed();
    private long mTimestamp;

    public ParallelSaveRequest(ParallelTaskData parallelTaskData, SaverCallback saverCallback) {
        this.mParallelTaskData = parallelTaskData;
        setSaverCallback(saverCallback);
    }

    private void parseDocAndSave(int i, int i2, PreviewImage previewImage, float[] fArr, String str, Uri uri) {
        int i3 = i;
        String str2 = str;
        byte[] bArr = this.mData;
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("parse document E: orientation = ");
        sb.append(i3);
        sb.append(", photo bitmap = ");
        sb.append(decodeByteArray);
        sb.append(", previewImage data length = ");
        sb.append(previewImage.getData().length);
        sb.append(", previewImage size = ");
        sb.append(previewImage.getWidth());
        sb.append("x");
        sb.append(previewImage.getHeight());
        sb.append(", previewPoints = ");
        sb.append(Arrays.toString(fArr));
        Log.d(str3, sb.toString());
        float[] doAlginDocument = DocumentProcess.getInstance().doAlginDocument(previewImage.getData(), decodeByteArray, previewImage.getWidth(), previewImage.getHeight(), fArr, DocumentDecoder.getRotateFlag(i));
        String str4 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("parse document: points = ");
        sb2.append(Arrays.toString(doAlginDocument));
        Log.d(str4, sb2.toString());
        String componentValue = DataRepository.dataItemRunning().getComponentRunningDocument().getComponentValue(163);
        Bitmap bitmap = decodeByteArray;
        float[] fArr2 = doAlginDocument;
        Bitmap doCropAndEnhance = DocumentProcess.getInstance().doCropAndEnhance(bitmap, fArr2, EnhanceType.valueOf(componentValue.toUpperCase(Locale.ENGLISH)), DocumentType.PPT, false);
        if (doCropAndEnhance == null || doCropAndEnhance.isRecycled()) {
            String str5 = str2;
            String str6 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("parseDocAndSave: save ");
            sb3.append(str5);
            sb3.append(", but doCropAndEnhance bitmap is null!!!");
            Log.w(str6, sb3.toString());
            return;
        }
        ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(this.mData);
            exifInterface.removeCompressedThumbnail();
        } catch (IOException e) {
            String str7 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("readExif: error ");
            sb4.append(e.getMessage());
            Log.w(str7, sb4.toString(), (Throwable) e);
        }
        exifInterface.addXiaomiDocPhotoVersion(1);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        doCropAndEnhance.compress(CompressFormat.JPEG, 100, exifInterface.getExifWriterStream((OutputStream) byteArrayOutputStream));
        byte[] addXmpData = Storage.addXmpData(byteArrayOutputStream.toByteArray(), (long) this.mData.length, doAlginDocument, componentValue);
        byte[] bArr2 = new byte[(addXmpData.length + this.mData.length)];
        System.arraycopy(addXmpData, 0, bArr2, 0, addXmpData.length);
        byte[] bArr3 = this.mData;
        System.arraycopy(bArr3, 0, bArr2, addXmpData.length, bArr3.length);
        Log.d(TAG, "parse document X: ");
        Context context = this.mContext;
        Location location = this.mLocation;
        int width = doCropAndEnhance.getWidth();
        int height = doCropAndEnhance.getHeight();
        String str8 = this.mAlgorithmName;
        String str9 = str8;
        Uri updateImageWithExtraExif = Storage.updateImageWithExtraExif(context, bArr2, false, null, uri, str, location, i, width, height, null, false, false, str9, this.mInfo);
        if (updateImageWithExtraExif != null && !updateImageWithExtraExif.equals(uri)) {
            refreshThumbnail(i, str, updateImageWithExtraExif);
        }
    }

    private void refreshThumbnail(int i, String str, Uri uri) {
        boolean z = this.mNeedThumbnail && this.mSaverCallback.needThumbnail(isFinal());
        if (z) {
            int highestOneBit = Integer.highestOneBit((int) Math.ceil(Math.max((double) this.width, (double) this.height) / 512.0d));
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Uri changed, so must try to create thumbnail: ");
            sb.append(uri);
            Log.d(str2, sb.toString());
            Thumbnail createThumbnail = Thumbnail.createThumbnail(this.mData, i, highestOneBit, uri, false);
            if (createThumbnail != null) {
                this.mSaverCallback.postUpdateThumbnail(createThumbnail, false);
            }
        } else {
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Just update the uri silently ");
            sb2.append(uri);
            Log.d(str3, sb2.toString());
            this.mSaverCallback.updatePreviewThumbnailUri(-1, uri);
        }
        this.mSaverCallback.notifyNewMediaData(uri, str, 2);
    }

    public int getSize() {
        return this.mSize;
    }

    public boolean isFinal() {
        return true;
    }

    public void onFinish() {
        ParallelTaskData parallelTaskData = this.mParallelTaskData;
        if (!(parallelTaskData == null || parallelTaskData.getCaptureTime() == 0)) {
            ScenarioTrackUtil.trackShotToViewEnd(true, this.mParallelTaskData.getCaptureTime());
        }
        PerformanceTracker.trackImageSaver(this.mData, 1);
        this.mData = null;
        ParallelTaskData parallelTaskData2 = this.mParallelTaskData;
        if (parallelTaskData2 != null) {
            parallelTaskData2.releaseImageData();
            this.mParallelTaskData = null;
        }
        this.mSaverCallback.onSaveFinish(this.mSize);
    }

    /* access modifiers changed from: protected */
    public void reFillSaveRequest(byte[] bArr, long j, long j2, Location location, int i, String str, int i2, int i3, boolean z, String str2, PictureInfo pictureInfo) {
        this.mData = bArr;
        this.mTimestamp = j;
        this.date = j2;
        this.mLocation = location == null ? null : new Location(location);
        this.mJpegRotation = i;
        this.mSavePath = str;
        this.width = i2;
        this.height = i3;
        this.mNeedThumbnail = z;
        this.mAlgorithmName = str2;
        this.mInfo = pictureInfo;
    }

    public void run() {
        save();
        onFinish();
    }

    /* JADX WARNING: Removed duplicated region for block: B:49:0x0208  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0236  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void save() {
        boolean z;
        parserParallelTaskData();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("save: ");
        sb.append(this.mSavePath);
        sb.append(" | ");
        sb.append(this.mTimestamp);
        Log.d(str, sb.toString());
        synchronized (this.mSavePath.intern()) {
            SaveTask itemByPath = DbRepository.dbItemSaveTask().getItemByPath(this.mSavePath);
            if (itemByPath == null && !Storage.isSaveToHidenFolder(Util.getFileTitleFromPath(this.mSavePath))) {
                SaveTask saveTask = (SaveTask) DbRepository.dbItemSaveTask().generateItem(System.currentTimeMillis());
                saveTask.setPath(this.mSavePath);
                saveTask.setStartTime(Long.valueOf(-1));
                DbRepository.dbItemSaveTask().endItemAndInsert(saveTask, 0);
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("insert full size picture:");
                sb2.append(this.mSavePath);
                Log.w(str2, sb2.toString());
            }
            int i = this.width;
            int i2 = this.height;
            int orientation = ExifHelper.getOrientation(this.mData);
            if ((this.mJpegRotation + orientation) % 180 != 0) {
                int i3 = i2;
                i2 = i;
                i = i3;
            }
            if (itemByPath != null) {
                if (itemByPath.isValid()) {
                    String fileTitleFromPath = Util.getFileTitleFromPath(this.mSavePath);
                    Uri resultUri = ParallelUtil.getResultUri(itemByPath.getMediaStoreId().longValue());
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("algo mark: uri: ");
                    sb3.append(resultUri.toString());
                    sb3.append(" | ");
                    sb3.append(itemByPath.getPath());
                    Log.d(str3, sb3.toString());
                    String str4 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("algo mark: ");
                    sb4.append(this.mJpegRotation);
                    sb4.append(" | ");
                    sb4.append(i);
                    sb4.append(" | ");
                    sb4.append(i2);
                    sb4.append(" | ");
                    sb4.append(orientation);
                    Log.d(str4, sb4.toString());
                    Uri withAppendedId = ContentUris.withAppendedId(Storage.getMediaUri(this.mContext, false, this.mSavePath), itemByPath.getMediaStoreId().longValue());
                    if (this.mParallelTaskData == null || this.mParallelTaskData.getDataParameter() == null || !this.mParallelTaskData.getDataParameter().isDocumentShot()) {
                        Uri updateImageWithExtraExif = Storage.updateImageWithExtraExif(this.mContext, this.mData, this.isHeic, null, withAppendedId, fileTitleFromPath, this.mLocation, orientation, i, i2, null, false, false, this.mAlgorithmName, this.mInfo);
                        if (updateImageWithExtraExif != null && !updateImageWithExtraExif.equals(withAppendedId)) {
                            refreshThumbnail(orientation, fileTitleFromPath, updateImageWithExtraExif);
                        }
                    } else {
                        ParallelTaskDataParameter dataParameter = this.mParallelTaskData.getDataParameter();
                        parseDocAndSave(orientation, dataParameter.getSensorOrientation(), dataParameter.getDocumentPreview(), dataParameter.getDocumentPoints(), fileTitleFromPath, withAppendedId);
                    }
                    ParallelUtil.markTaskFinish(this.mContext, itemByPath, false);
                }
            }
            String fileTitleFromPath2 = this.mSavePath != null ? Util.getFileTitleFromPath(this.mSavePath) : Util.createJpegName(this.date);
            SaveTask saveTask2 = itemByPath;
            Uri addImage = Storage.addImage(this.mContext, fileTitleFromPath2, this.date, this.mLocation, orientation, this.mData, this.isHeic, i, i2, false, false, false, false, itemByPath != null, this.mAlgorithmName, this.mInfo);
            if (addImage != null) {
                if (this.mNeedThumbnail) {
                    Thumbnail createThumbnail = Thumbnail.createThumbnail(this.mData, orientation, Integer.highestOneBit((int) Math.ceil(Math.max((double) i, (double) i2) / 512.0d)), addImage, false);
                    if (createThumbnail != null) {
                        this.mSaverCallback.postUpdateThumbnail(createThumbnail, true);
                        z = true;
                        this.mSaverCallback.notifyNewMediaData(addImage, fileTitleFromPath2, 2);
                        if (saveTask2 == null) {
                            String str5 = TAG;
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("algo mark: ");
                            sb5.append(addImage.toString());
                            Log.d(str5, sb5.toString());
                            SaveTask saveTask3 = saveTask2;
                            saveTask3.setMediaStoreId(Long.valueOf(ContentUris.parseId(addImage)));
                            ParallelUtil.markTaskFinish(this.mContext, saveTask3, false);
                        } else {
                            if (!z) {
                                Thumbnail createThumbnail2 = Thumbnail.createThumbnail(this.mData, orientation, Integer.highestOneBit((int) Math.ceil(Math.max((double) i, (double) i2) / 512.0d)), addImage, false);
                                if (createThumbnail2 != null) {
                                    this.mSaverCallback.postUpdateThumbnail(createThumbnail2, true);
                                }
                            }
                            ParallelUtil.insertImageToParallelService(this.mContext, ContentUris.parseId(addImage), this.mSavePath);
                        }
                    } else {
                        this.mSaverCallback.postHideThumbnailProgressing();
                    }
                }
                z = false;
                this.mSaverCallback.notifyNewMediaData(addImage, fileTitleFromPath2, 2);
                if (saveTask2 == null) {
                }
            } else if (Storage.isSaveToHidenFolder(fileTitleFromPath2)) {
                this.mSaverCallback.notifyNewMediaData(null, fileTitleFromPath2, 3);
            }
        }
    }

    public void setContextAndCallback(Context context, SaverCallback saverCallback) {
        this.mContext = context;
        this.mSaverCallback = saverCallback;
    }
}
