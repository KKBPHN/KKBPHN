package com.android.camera.storage;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.statistic.ScenarioTrackUtil;
import com.android.gallery3d.exif.ExifInterface;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.PictureInfo;

public final class ImageSaveRequest extends AbstractSaveRequest {
    private static final String TAG = "ImageSaveRequest";
    private String algorithmName;
    private Context context;
    private byte[] data;
    private ExifInterface exif;
    private boolean finalImage;
    private PictureInfo info;
    private boolean isHeic;
    private boolean isHide;
    private boolean isMap;
    private boolean isParallelProcess;
    private Location loc;
    private boolean mirror;
    private boolean needThumbnail;
    public String oldTitle;
    private int previewThumbnailHash;
    private SaverCallback saverCallback;
    private int size;
    public String title;
    private Uri uri;

    ImageSaveRequest() {
    }

    ImageSaveRequest(ParallelTaskData parallelTaskData, SaverCallback saverCallback2) {
        this.mParallelTaskData = parallelTaskData;
        setSaverCallback(saverCallback2);
        this.size = calculateMemoryUsed();
        this.isHeic = isHeicSavingRequest();
    }

    ImageSaveRequest(byte[] bArr, boolean z, String str, String str2, long j, Uri uri2, Location location, int i, int i2, ExifInterface exifInterface, int i3, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, String str3, PictureInfo pictureInfo, int i4) {
        reFillSaveRequest(bArr, z, str, str2, j, uri2, location, i, i2, exifInterface, i3, z2, z3, z4, z5, z6, str3, pictureInfo, i4);
    }

    private void trackScenarioAbort() {
        ParallelTaskData parallelTaskData = this.mParallelTaskData;
        if (parallelTaskData != null) {
            ScenarioTrackUtil.trackScenarioAbort(ScenarioTrackUtil.sShotToGalleryTimeScenario, String.valueOf(parallelTaskData.getCaptureTime()));
            ScenarioTrackUtil.trackScenarioAbort(ScenarioTrackUtil.sShotToViewTimeScenario, String.valueOf(this.mParallelTaskData.getCaptureTime()));
        }
    }

    public int getSize() {
        return this.size;
    }

    public boolean isFinal() {
        return this.finalImage;
    }

    public void onFinish() {
        this.data = null;
        ParallelTaskData parallelTaskData = this.mParallelTaskData;
        if (parallelTaskData != null) {
            parallelTaskData.releaseImageData();
            this.mParallelTaskData = null;
        }
        this.saverCallback.onSaveFinish(getSize());
    }

    /* access modifiers changed from: protected */
    public void reFillSaveRequest(byte[] bArr, boolean z, String str, String str2, long j, Uri uri2, Location location, int i, int i2, ExifInterface exifInterface, int i3, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, String str3, PictureInfo pictureInfo, int i4) {
        Location location2 = location;
        this.data = bArr;
        this.needThumbnail = z;
        this.date = j;
        this.uri = uri2;
        this.title = str;
        this.oldTitle = str2;
        this.loc = location2 == null ? null : new Location(location);
        this.width = i;
        this.height = i2;
        this.exif = exifInterface;
        this.orientation = i3;
        this.isHide = z2;
        this.isMap = z3;
        this.finalImage = z4;
        this.mirror = z5;
        this.isParallelProcess = z6;
        this.algorithmName = str3;
        this.info = pictureInfo;
        this.previewThumbnailHash = i4;
    }

    public void run() {
        save();
        onFinish();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0039, code lost:
        if (r15 != null) goto L_0x007f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0114  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void save() {
        Uri uri2;
        Uri uri3;
        Uri uri4;
        parserParallelTaskData();
        Uri uri5 = this.uri;
        if (uri5 != null) {
            uri4 = uri5;
            uri2 = Storage.updateImageWithExtraExif(this.context, this.data, this.isHeic, this.exif, uri5, this.title, this.loc, this.orientation, this.width, this.height, this.oldTitle, this.mirror, this.isParallelProcess, this.algorithmName, this.info);
        } else {
            uri4 = uri5;
            if (this.data != null) {
                String str = this.algorithmName;
                boolean z = str != null && str.equals(Util.ALGORITHM_NAME_MIMOJI_CAPTURE);
                this.uri = Storage.addImage(this.context, this.title, this.date, this.loc, this.orientation, this.data, this.isHeic, this.width, this.height, false, this.isHide, this.isMap, z, this.isParallelProcess, this.algorithmName, this.info);
                uri2 = this.uri;
                Storage.getAvailableSpace();
                boolean z2 = !this.needThumbnail && this.saverCallback.needThumbnail(isFinal());
                uri3 = this.uri;
                String str2 = TAG;
                if (uri3 == null) {
                    if (z2) {
                        int highestOneBit = Integer.highestOneBit((int) Math.ceil(Math.max((double) this.width, (double) this.height) / 512.0d));
                        Log.d(str2, "image save try to create thumbnail");
                        Thumbnail createThumbnailFromUri = this.isMap ? Thumbnail.createThumbnailFromUri(this.context, uri2, this.mirror) : Thumbnail.createThumbnail(this.data, this.orientation, highestOneBit, uri2, this.mirror);
                        if (createThumbnailFromUri != null) {
                            this.saverCallback.postUpdateThumbnail(createThumbnailFromUri, true);
                        } else {
                            this.saverCallback.postHideThumbnailProgressing();
                        }
                    } else {
                        this.saverCallback.updatePreviewThumbnailUri(this.previewThumbnailHash, uri2);
                    }
                    this.saverCallback.notifyNewMediaData(uri2, this.title, 2);
                    ParallelTaskData parallelTaskData = this.mParallelTaskData;
                    if (!(parallelTaskData == null || parallelTaskData.getCaptureTime() == 0)) {
                        ScenarioTrackUtil.trackShotToGalleryEnd(false, this.mParallelTaskData.getCaptureTime());
                        ScenarioTrackUtil.trackShotToViewEnd(false, this.mParallelTaskData.getCaptureTime());
                    }
                    Log.d(str2, "image save finished");
                    return;
                } else if (Storage.isSaveToHidenFolder(this.title)) {
                    this.saverCallback.notifyNewMediaData(null, this.title, 3);
                    return;
                } else {
                    Log.e(str2, "image save failed");
                    if (z2) {
                        this.saverCallback.postHideThumbnailProgressing();
                        return;
                    }
                    trackScenarioAbort();
                    Log.e(str2, "set mWaitingForUri is false");
                    this.saverCallback.updatePreviewThumbnailUri(this.previewThumbnailHash, null);
                    return;
                }
            }
        }
        uri2 = uri4;
        Storage.getAvailableSpace();
        if (!this.needThumbnail) {
        }
        uri3 = this.uri;
        String str22 = TAG;
        if (uri3 == null) {
        }
    }

    public void setContextAndCallback(Context context2, SaverCallback saverCallback2) {
        this.context = context2;
        this.saverCallback = saverCallback2;
    }
}
