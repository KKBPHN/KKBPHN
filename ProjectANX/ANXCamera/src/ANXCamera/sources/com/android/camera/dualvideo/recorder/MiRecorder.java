package com.android.camera.dualvideo.recorder;

import android.content.ContentValues;
import android.location.Location;
import android.media.CamcorderProfile;
import android.media.MediaCodec;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.SystemClock;
import android.view.Surface;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.FileCompat;
import com.android.camera.LocationManager;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.module.VideoModule;
import com.android.camera.storage.ImageSaver;
import com.android.camera.storage.Storage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import miui.reflect.Method;

public final class MiRecorder {
    private static final String TAG = "MiRecorder";
    private String mCurrentVideoFilename;
    private ContentValues mCurrentVideoValues;
    private final int mId;
    private boolean mIsPaused;
    /* access modifiers changed from: private */
    public boolean mIsRecording;
    /* access modifiers changed from: private */
    public final MiRecorderListener mListener;
    private final Location mLocation;
    private final long mMaxFileSize;
    private final int mOrientation;
    private CamcorderProfile mProfile;
    private long mRecordDuration;
    private long mRecordStartTime;
    private MediaRecorder mRecorder;
    private Surface mRecorderSurface;
    private final CameraSize mVideoSize;

    class ErrorListener implements OnErrorListener {
        ErrorListener() {
        }

        public void onError(MediaRecorder mediaRecorder, int i, int i2) {
            StringBuilder sb = new StringBuilder();
            sb.append("MediaRecorder error. what=");
            sb.append(i);
            sb.append(" extra=");
            sb.append(i2);
            Log.e(MiRecorder.TAG, sb.toString());
            if ((i == 1 || i == 100) && MiRecorder.this.mIsRecording) {
                MiRecorder.this.mListener.doStop();
            }
        }
    }

    class InfoListener implements OnInfoListener {
        InfoListener() {
        }

        public void onInfo(MediaRecorder mediaRecorder, int i, int i2) {
            boolean access$000 = MiRecorder.this.mIsRecording;
            String str = MiRecorder.TAG;
            if (!access$000) {
                StringBuilder sb = new StringBuilder();
                sb.append("onInfo: ignore event ");
                sb.append(i);
                Log.w(str, sb.toString());
                return;
            }
            if (i == 800 || i == 801) {
                Log.w(str, "reached max size.");
                MiRecorder.this.mListener.doStop();
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onInfo what : ");
                sb2.append(i);
                Log.w(str, sb2.toString());
            }
        }
    }

    public interface MiRecorderListener {
        void doStop();
    }

    public MiRecorder(int i, Location location, long j, int i2, MiRecorderListener miRecorderListener, CameraSize cameraSize) {
        this.mId = i;
        this.mLocation = location;
        this.mMaxFileSize = j;
        this.mOrientation = i2;
        this.mListener = miRecorderListener;
        this.mVideoSize = cameraSize;
        initRecorder();
    }

    private void cleanupEmptyFile() {
        StringBuilder sb;
        String str;
        String str2 = this.mCurrentVideoFilename;
        if (str2 != null) {
            File file = new File(str2);
            boolean exists = file.exists();
            String str3 = TAG;
            if (!exists) {
                sb = new StringBuilder();
                str = "no video file: ";
            } else if (file.length() == 0) {
                if (!Storage.isUseDocumentMode()) {
                    file.delete();
                } else {
                    FileCompat.deleteFile(this.mCurrentVideoFilename);
                }
                sb = new StringBuilder();
                str = "delete empty video file: ";
            } else {
                return;
            }
            sb.append(str);
            sb.append(this.mCurrentVideoFilename);
            Log.d(str3, sb.toString());
            this.mCurrentVideoFilename = null;
        }
    }

    private String createName(long j, int i) {
        if (i > 0) {
            return "mBaseFileName";
        }
        return new SimpleDateFormat("'VID'_yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date(j));
    }

    private ContentValues genContentValues(int i, int i2, int i3) {
        String createName = createName(System.currentTimeMillis(), i2);
        String str = "_%d";
        if (i2 > 0) {
            String format = String.format(Locale.ENGLISH, str, new Object[]{Integer.valueOf(i2)});
            StringBuilder sb = new StringBuilder();
            sb.append(createName);
            sb.append(format);
            createName = sb.toString();
        }
        String format2 = String.format(Locale.ENGLISH, str, new Object[]{Integer.valueOf(i3)});
        StringBuilder sb2 = new StringBuilder();
        sb2.append(createName);
        sb2.append(format2);
        String sb3 = sb2.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(sb3);
        sb4.append(Util.convertOutputFormatToFileExt(i));
        String sb5 = sb4.toString();
        String convertOutputFormatToMimeType = Util.convertOutputFormatToMimeType(i);
        String generateFilepath = Storage.generateFilepath(sb5);
        StringBuilder sb6 = new StringBuilder();
        sb6.append("genContentValues: path=");
        sb6.append(generateFilepath);
        Log.d(TAG, sb6.toString());
        ContentValues contentValues = new ContentValues(8);
        contentValues.put("title", sb3);
        contentValues.put("_display_name", sb5);
        contentValues.put("mime_type", convertOutputFormatToMimeType);
        contentValues.put("_data", generateFilepath);
        StringBuilder sb7 = new StringBuilder();
        sb7.append(this.mVideoSize.width);
        sb7.append("x");
        sb7.append(this.mVideoSize.height);
        contentValues.put("resolution", sb7.toString());
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        if (!(currentLocation == null || (currentLocation.getLatitude() == 0.0d && currentLocation.getLongitude() == 0.0d))) {
            contentValues.put("latitude", Double.valueOf(currentLocation.getLatitude()));
            contentValues.put("longitude", Double.valueOf(currentLocation.getLongitude()));
        }
        return contentValues;
    }

    private int getHevcVideoEncoderBitRate(CamcorderProfile camcorderProfile) {
        StringBuilder sb = new StringBuilder();
        sb.append(camcorderProfile.videoFrameWidth);
        sb.append("x");
        sb.append(camcorderProfile.videoFrameHeight);
        sb.append(":");
        sb.append(camcorderProfile.videoFrameRate);
        String sb2 = sb.toString();
        if (RecorderUtil.HEVC_VIDEO_ENCODER_BITRATE.containsKey(sb2)) {
            return ((Integer) RecorderUtil.HEVC_VIDEO_ENCODER_BITRATE.get(sb2)).intValue();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("no pre-defined bitrate for ");
        sb3.append(sb2);
        Log.d(TAG, sb3.toString());
        return camcorderProfile.videoBitRate;
    }

    private void initRecorder() {
        this.mRecorder = new MediaRecorder();
        this.mRecorderSurface = MediaCodec.createPersistentInputSurface();
        this.mProfile = CamcorderProfile.get(0, 6);
        this.mProfile.videoCodec = CameraSettings.getVideoEncoder();
        this.mCurrentVideoValues = genContentValues(this.mProfile.fileFormat, 0, this.mId);
        this.mCurrentVideoFilename = this.mCurrentVideoValues.getAsString("_data");
        setupRecorder();
    }

    private void setParameterExtra(MediaRecorder mediaRecorder, String str) {
        Class[] clsArr = {MediaRecorder.class};
        Method method = Util.getMethod(clsArr, "setParameter", "(Ljava/lang/String;)V");
        if (method != null) {
            method.invoke(clsArr[0], mediaRecorder, str);
        }
    }

    private void setupRecorder() {
        int i;
        String sb;
        String str = TAG;
        try {
            this.mRecorder.setVideoSource(2);
            this.mRecorder.setAudioSource(5);
            this.mRecorder.setOutputFormat(this.mProfile.fileFormat);
            this.mRecorder.setVideoEncoder(this.mProfile.videoCodec);
            this.mRecorder.setVideoSize(this.mProfile.videoFrameHeight, this.mProfile.videoFrameWidth);
            this.mRecorder.setVideoFrameRate(this.mProfile.videoFrameRate);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("setVideoFrameRate: ");
            sb2.append(this.mProfile.videoFrameRate);
            Log.d(str, sb2.toString());
            if (5 == this.mProfile.videoCodec) {
                i = getHevcVideoEncoderBitRate(this.mProfile);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("H265 bitrate: ");
                sb3.append(i);
                sb = sb3.toString();
            } else {
                i = this.mProfile.videoBitRate;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("H264 bitrate: ");
                sb4.append(i);
                sb = sb4.toString();
            }
            Log.d(str, sb);
            this.mRecorder.setVideoEncodingBitRate(i);
            this.mRecorder.setAudioEncodingBitRate(this.mProfile.audioBitRate);
            this.mRecorder.setAudioChannels(this.mProfile.audioChannels);
            this.mRecorder.setAudioSamplingRate(this.mProfile.audioSampleRate);
            this.mRecorder.setAudioEncoder(this.mProfile.audioCodec);
            this.mRecorder.setMaxDuration(0);
            if (this.mLocation != null) {
                this.mRecorder.setLocation((float) this.mLocation.getLatitude(), (float) this.mLocation.getLongitude());
            }
            if (this.mMaxFileSize > 0) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("maxFileSize=");
                sb5.append(this.mMaxFileSize);
                Log.v(str, sb5.toString());
                this.mRecorder.setMaxFileSize(this.mMaxFileSize);
                if (this.mMaxFileSize > VideoModule.VIDEO_MAX_SINGLE_FILE_SIZE) {
                    setParameterExtra(this.mRecorder, "param-use-64bit-offset=1");
                }
            }
            this.mRecorder.setOrientationHint(this.mOrientation);
            this.mRecorder.setOutputFile(this.mCurrentVideoFilename);
            this.mRecorder.setInputSurface(this.mRecorderSurface);
            this.mRecorder.prepare();
            this.mRecorder.setOnErrorListener(new ErrorListener());
            this.mRecorder.setOnInfoListener(new InfoListener());
        } catch (Exception e) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("prepare failed for ");
            sb6.append(this.mCurrentVideoFilename);
            Log.e(str, sb6.toString(), (Throwable) e);
            release();
        }
    }

    public long getDuration() {
        return isPaused() ? this.mRecordDuration : (this.mRecordDuration + SystemClock.uptimeMillis()) - this.mRecordStartTime;
    }

    public int getId() {
        return this.mId;
    }

    public Surface getSurface() {
        return this.mRecorderSurface;
    }

    public boolean isPaused() {
        return this.mIsPaused;
    }

    public boolean isRecording() {
        return this.mIsRecording;
    }

    public void pause() {
        Log.d(TAG, "pause: ");
        if (this.mIsRecording) {
            this.mRecorder.pause();
            this.mIsPaused = true;
            this.mRecordDuration += SystemClock.uptimeMillis() - this.mRecordStartTime;
        }
    }

    public void release() {
        String str = TAG;
        Log.d(str, "release: ");
        long currentTimeMillis = System.currentTimeMillis();
        this.mRecorder.reset();
        StringBuilder sb = new StringBuilder();
        sb.append("reset: t1=");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        Log.v(str, sb.toString());
        long currentTimeMillis2 = System.currentTimeMillis();
        this.mRecorder.release();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("release: t2=");
        sb2.append(System.currentTimeMillis() - currentTimeMillis2);
        Log.v(str, sb2.toString());
        this.mRecorderSurface.release();
        this.mIsRecording = false;
        this.mIsPaused = false;
        cleanupEmptyFile();
    }

    public void resume() {
        Log.d(TAG, "resume:  ");
        if (this.mIsRecording) {
            this.mRecorder.resume();
            this.mRecordStartTime = SystemClock.uptimeMillis();
            this.mIsPaused = false;
        }
    }

    public void save(ImageSaver imageSaver) {
        StringBuilder sb = new StringBuilder();
        sb.append("save: ");
        sb.append(this.mCurrentVideoFilename);
        Log.d(TAG, sb.toString());
        String str = this.mCurrentVideoFilename;
        if (str != null) {
            RecorderUtil.saveVideo(imageSaver, str, this.mCurrentVideoValues, true, false);
        }
    }

    public void start() {
        Log.d(TAG, "start:  ");
        if (!isRecording() && !isPaused()) {
            this.mRecorder.start();
            this.mIsRecording = true;
            this.mIsPaused = false;
            this.mRecordDuration = 0;
            this.mRecordStartTime = SystemClock.uptimeMillis();
        }
    }

    public void stop() {
        String str = TAG;
        Log.d(str, "stop: ");
        if (this.mIsRecording) {
            this.mIsRecording = false;
            this.mIsPaused = false;
            try {
                this.mRecorder.setOnErrorListener(null);
                this.mRecorder.setOnInfoListener(null);
                this.mRecorder.stop();
            } catch (RuntimeException unused) {
                Log.e(str, "failed to stop media recorder");
                String str2 = this.mCurrentVideoFilename;
                if (str2 != null) {
                    Util.deleteFile(str2);
                    this.mCurrentVideoFilename = null;
                }
            }
        }
        this.mRecordDuration += SystemClock.uptimeMillis() - this.mRecordStartTime;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MiRecorder{mId=");
        sb.append(this.mId);
        sb.append(", mCurrentVideoFilename='");
        sb.append(this.mCurrentVideoFilename);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
