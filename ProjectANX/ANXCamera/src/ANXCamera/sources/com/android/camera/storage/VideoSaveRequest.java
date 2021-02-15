package com.android.camera.storage;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.android.camera.FileCompat;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.jcodec.MCoverBox;
import com.android.camera.jcodec.MP4UtilEx;
import com.android.camera.jcodec.MP4UtilEx.VideoTag;
import com.android.camera.log.Log;
import com.android.camera.storage.HDR10Thumbnail.HDR10ThumbnailUtil;
import com.xiaomi.stat.d;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoSaveRequest implements SaveRequest {
    private static final String TAG = "VideoSaveRequest";
    private Context context;
    private ContentValues mContentValues;
    private boolean mIsFinal;
    private List mTags;
    public Uri mUri;
    private String mVideoPath;
    private SaverCallback saverCallback;

    public VideoSaveRequest(String str, ContentValues contentValues, boolean z) {
        this.mVideoPath = str;
        this.mContentValues = contentValues;
        this.mIsFinal = z;
    }

    private Uri addVideoToMediaStore(String str, ContentValues contentValues) {
        StringBuilder sb;
        String str2 = "Current video URI: ";
        long videoDuration = getVideoDuration(str);
        int i = (videoDuration > 0 ? 1 : (videoDuration == 0 ? 0 : -1));
        Uri uri = null;
        String str3 = TAG;
        if (i == 0) {
            boolean delete = new File(str).delete();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("delete invalid video: ");
            sb2.append(str);
            sb2.append(", deleted : ");
            sb2.append(delete);
            Log.e(str3, sb2.toString());
            return null;
        }
        updateContentValue(contentValues, new File(str).length(), videoDuration);
        try {
            long currentTimeMillis = System.currentTimeMillis();
            uri = this.context.getContentResolver().insert(Storage.getMediaUri(this.context, true, str), contentValues);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("addVideoToMediaStore: insert video cost: ");
            sb3.append(System.currentTimeMillis() - currentTimeMillis);
            sb3.append(d.H);
            Log.d(str3, sb3.toString());
            sb = new StringBuilder();
        } catch (Exception e) {
            Log.e(str3, "failed to add video to media store", (Throwable) e);
            sb = new StringBuilder();
        } catch (Throwable th) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str2);
            sb4.append(uri);
            Log.d(str3, sb4.toString());
            throw th;
        }
        sb.append(str2);
        sb.append(uri);
        Log.d(str3, sb.toString());
        return uri;
    }

    private boolean checkExternalStorageThumbnailInterupt(String str) {
        boolean isSecondPhoneStorage = Storage.isSecondPhoneStorage(str);
        boolean isUsePhoneStorage = Storage.isUsePhoneStorage();
        if (!isSecondPhoneStorage || !isUsePhoneStorage) {
            return true;
        }
        Log.w(TAG, "save video: sd card was ejected");
        return false;
    }

    private long getVideoDuration(String str) {
        ParcelFileDescriptor parcelFileDescriptor;
        if (!Storage.isUseDocumentMode()) {
            return Util.getDuration(str);
        }
        ParcelFileDescriptor parcelFileDescriptor2 = null;
        try {
            parcelFileDescriptor2 = FileCompat.getParcelFileDescriptor(str, false);
            long duration = Util.getDuration(parcelFileDescriptor2.getFileDescriptor());
            Util.closeSafely(parcelFileDescriptor2);
            FileCompat.removeDocumentFileForPath(str);
            return duration;
        } catch (Exception unused) {
            Util.closeSafely(parcelFileDescriptor);
            FileCompat.removeDocumentFileForPath(str);
            return 0;
        } catch (Throwable th) {
            Util.closeSafely(parcelFileDescriptor2);
            FileCompat.removeDocumentFileForPath(str);
            throw th;
        }
    }

    private ContentValues updateContentValue(ContentValues contentValues, long j, long j2) {
        contentValues.put("_size", Long.valueOf(j));
        contentValues.put("duration", Long.valueOf(j2));
        contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        return contentValues;
    }

    private int updateVideoToMediaStore(Uri uri, String str, ContentValues contentValues) {
        StringBuilder sb;
        String str2 = "Current video URI: ";
        long videoDuration = getVideoDuration(str);
        int i = (videoDuration > 0 ? 1 : (videoDuration == 0 ? 0 : -1));
        String str3 = TAG;
        int i2 = 0;
        if (i == 0) {
            boolean delete = new File(str).delete();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("delete invalid video: ");
            sb2.append(str);
            sb2.append(", deleted : ");
            sb2.append(delete);
            Log.e(str3, sb2.toString());
            return 0;
        }
        updateContentValue(contentValues, new File(str).length(), videoDuration);
        try {
            long currentTimeMillis = System.currentTimeMillis();
            i2 = this.context.getContentResolver().update(uri, contentValues, null, null);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("updateVideoToMediaStore: insert video cost: ");
            sb3.append(System.currentTimeMillis() - currentTimeMillis);
            sb3.append(d.H);
            Log.d(str3, sb3.toString());
            sb = new StringBuilder();
        } catch (Exception e) {
            Log.e(str3, "failed to add video to media store", (Throwable) e);
            sb = new StringBuilder();
        } catch (Throwable th) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str2);
            sb4.append(uri);
            Log.d(str3, sb4.toString());
            throw th;
        }
        sb.append(str2);
        sb.append(uri);
        Log.d(str3, sb.toString());
        return i2;
    }

    public int getSize() {
        return 0;
    }

    public boolean isFinal() {
        return this.mIsFinal;
    }

    public void onFinish() {
        Log.d(TAG, "onFinish: runnable process finished");
        this.saverCallback.onSaveFinish(getSize());
    }

    public void run() {
        save();
        onFinish();
    }

    public void save() {
        String str = TAG;
        Log.d(str, "save video: start");
        String str2 = "_data";
        String asString = this.mContentValues.getAsString(str2);
        if (!asString.equals(this.mVideoPath)) {
            if (new File(this.mVideoPath).renameTo(new File(asString))) {
                this.mVideoPath = asString;
            } else {
                this.mContentValues.put(str2, this.mVideoPath);
            }
        }
        boolean needThumbnail = this.saverCallback.needThumbnail(isFinal());
        this.mUri = addVideoToMediaStore(this.mVideoPath, this.mContentValues);
        if (this.mUri == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("insert MediaProvider failed, attempt to find uri by path, ");
            sb.append(this.mVideoPath);
            Log.w(str, sb.toString());
            this.mUri = MediaProviderUtil.getContentUriFromPath(this.context, this.mVideoPath);
            if (this.mUri != null) {
                Log.d(str, "insert MediaProvider failed, need update mContentValues by Uri");
                updateVideoToMediaStore(this.mUri, this.mVideoPath, this.mContentValues);
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("save video: media has been stored, Uri: ");
        sb2.append(this.mUri);
        sb2.append(", has thumbnail: ");
        sb2.append(needThumbnail);
        Log.k(3, str, sb2.toString());
        if (this.mUri != null && checkExternalStorageThumbnailInterupt(this.mVideoPath)) {
            boolean z = true;
            if (needThumbnail) {
                Bitmap[] bitmapArr = new Bitmap[1];
                if (!MP4UtilEx.getCover(this.mVideoPath, Util.getResolution(this.mContentValues), bitmapArr) && bitmapArr[0] != null) {
                    if (this.mTags == null) {
                        this.mTags = new ArrayList();
                    }
                    this.mTags.add(new VideoTag(null, Util.getBitmapData(bitmapArr[0], 100), MCoverBox.fourcc()));
                }
                MP4UtilEx.writeTags(this.mVideoPath, this.mTags);
                if (bitmapArr[0] != null) {
                    Thumbnail createThumbnail = Thumbnail.createThumbnail(this.mUri, bitmapArr[0], 0, false);
                    if (Util.isHDR10Video(this.mVideoPath)) {
                        createThumbnail.setBitmap(HDR10ThumbnailUtil.getHdr10Bitmap(null, createThumbnail.getBitmap()));
                    }
                    this.saverCallback.postUpdateThumbnail(createThumbnail, true);
                } else {
                    this.saverCallback.postHideThumbnailProgressing();
                }
            }
            this.saverCallback.notifyNewMediaData(this.mUri, this.mContentValues.getAsString("title"), 1);
            Context context2 = this.context;
            String str3 = this.mVideoPath;
            if (!(this.mContentValues.get("latitude") == null && this.mContentValues.get("longitude") == null)) {
                z = false;
            }
            Storage.saveToCloudAlbum(context2, str3, -1, z);
        } else if (needThumbnail) {
            this.saverCallback.postHideThumbnailProgressing();
        }
        Log.d(str, "save video: end");
    }

    public void setContextAndCallback(Context context2, SaverCallback saverCallback2) {
        this.context = context2;
        this.saverCallback = saverCallback2;
    }

    public void setTags(List list) {
        this.mTags = list;
    }
}
