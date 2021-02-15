package com.android.camera.module;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import com.android.camera.CameraApplicationDelegate;
import com.android.camera.FileCompat;
import com.android.camera.LocationManager;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.groupshot.GroupShot;
import com.android.camera.log.Log;
import com.android.camera.storage.SaverCallback;
import com.android.camera.storage.Storage;
import com.android.gallery3d.exif.ExifHelper;
import java.io.File;
import java.lang.ref.WeakReference;

public class SaveOutputImageTask extends AsyncTask {
    private static final String TAG = "SaveOutputImageTask";
    private GroupShot mGroupShotInternal;
    private int mHeight;
    private Location mLocation;
    private int mOrientation;
    private WeakReference mSaverCallbackWeakReference;
    private volatile long mStartTime;
    private long mTimeTaken;
    private String mTitle;
    private int mWidth;

    public SaveOutputImageTask(SaverCallback saverCallback, long j, Location location, int i, int i2, int i3, String str, GroupShot groupShot) {
        this.mSaverCallbackWeakReference = new WeakReference(saverCallback);
        this.mTimeTaken = j;
        this.mLocation = location;
        this.mWidth = i;
        this.mHeight = i2;
        this.mOrientation = i3;
        this.mTitle = str;
        this.mGroupShotInternal = groupShot;
    }

    private void finishGroupShot() {
        this.mGroupShotInternal.clearImages();
        this.mGroupShotInternal.finish();
        this.mGroupShotInternal = null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0031, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0032, code lost:
        if (r0 != null) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003c, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void saveGroupShotImage(String str) {
        if (!Storage.isUseDocumentMode()) {
            this.mGroupShotInternal.getImageAndSaveJpeg(str);
            ExifHelper.writeExifByFilePath(str, this.mOrientation, LocationManager.instance().getCurrentLocation(), this.mTimeTaken);
            return;
        }
        try {
            ParcelFileDescriptor parcelFileDescriptor = FileCompat.getParcelFileDescriptor(str, true);
            this.mGroupShotInternal.getImageAndSaveJpeg(parcelFileDescriptor.getFileDescriptor());
            if (parcelFileDescriptor != null) {
                parcelFileDescriptor.close();
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("open file failed, filePath ");
            sb.append(str);
            Log.e(TAG, sb.toString(), (Throwable) e);
        } catch (Throwable th) {
            r4.addSuppressed(th);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0153  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Thumbnail doInBackground(Void... voidArr) {
        String str;
        String str2 = TAG;
        Log.v(str2, "doInBackground start");
        Thumbnail thumbnail = null;
        try {
            Log.v(str2, String.format("attach_end() = 0x%08x", new Object[]{Integer.valueOf(this.mGroupShotInternal.attach_end())}));
            if (isCancelled()) {
                return null;
            }
            Log.v(str2, String.format("setBaseImage() = 0x%08x", new Object[]{Integer.valueOf(this.mGroupShotInternal.setBaseImage(0))}));
            this.mGroupShotInternal.setBestFace();
            StringBuilder sb = new StringBuilder();
            sb.append("groupshot attach end & setbestface cost ");
            sb.append(System.currentTimeMillis() - this.mStartTime);
            Log.v(str2, sb.toString());
            str = Storage.generateFilepath4Image(this.mTitle, false);
            try {
                saveGroupShotImage(str);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("groupshot finish group cost ");
                sb2.append(System.currentTimeMillis() - this.mStartTime);
                sb2.append(", path = ");
                sb2.append(str);
                Log.v(str2, sb2.toString());
                if (isCancelled()) {
                    return null;
                }
                if (Util.sIsDumpOrigJpg) {
                    String substring = str.substring(0, str.lastIndexOf(Storage.JPEG_SUFFIX));
                    new File(substring).mkdirs();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(substring);
                    sb3.append(File.separator);
                    this.mGroupShotInternal.saveInputImages(sb3.toString());
                }
                if (isCancelled() || isCancelled()) {
                    return null;
                }
                Context androidContext = CameraApplicationDelegate.getAndroidContext();
                Uri addImageForGroupOrPanorama = Storage.addImageForGroupOrPanorama(androidContext, str, this.mOrientation, this.mTimeTaken, this.mLocation, this.mWidth, this.mHeight);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("groupshot insert db cost ");
                sb4.append(System.currentTimeMillis() - this.mStartTime);
                sb4.append(", uri = ");
                sb4.append(addImageForGroupOrPanorama);
                Log.v(str2, sb4.toString());
                SaverCallback saverCallback = (SaverCallback) this.mSaverCallbackWeakReference.get();
                if (!(saverCallback == null || addImageForGroupOrPanorama == null)) {
                    saverCallback.notifyNewMediaData(addImageForGroupOrPanorama, this.mTitle, 2);
                    thumbnail = Thumbnail.createThumbnailFromUri(androidContext, addImageForGroupOrPanorama, false);
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("groupshot asynctask cost ");
                    sb5.append(System.currentTimeMillis() - this.mStartTime);
                    Log.v(str2, sb5.toString());
                }
                return thumbnail;
            } catch (Exception e) {
                e = e;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("SaveOutputImageTask exception occurs, ");
                sb6.append(e.getMessage());
                Log.e(str2, sb6.toString());
                if (str != null) {
                }
                return null;
            }
        } catch (Exception e2) {
            e = e2;
            str = null;
            StringBuilder sb62 = new StringBuilder();
            sb62.append("SaveOutputImageTask exception occurs, ");
            sb62.append(e.getMessage());
            Log.e(str2, sb62.toString());
            if (str != null) {
                new File(str).delete();
            }
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        Log.v(TAG, "SaveOutputImageTask onCancelled");
        finishGroupShot();
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Thumbnail thumbnail) {
        String str = TAG;
        Log.v(str, "SaveOutputImageTask onPostExecute");
        SaverCallback saverCallback = (SaverCallback) this.mSaverCallbackWeakReference.get();
        if (saverCallback != null) {
            if (thumbnail == null) {
                Log.e(str, "onPostExecute thumbnail is null");
                saverCallback.postHideThumbnailProgressing();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("onPostExecute thumbnail = ");
                sb.append(thumbnail);
                Log.v(str, sb.toString());
                saverCallback.postUpdateThumbnail(thumbnail, false);
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("groupshot image process cost ");
            sb2.append(System.currentTimeMillis() - this.mStartTime);
            Log.v(str, sb2.toString());
            finishGroupShot();
        }
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        this.mStartTime = System.currentTimeMillis();
    }
}
