package com.android.camera.module.encoder;

import android.content.ContentValues;
import android.opengl.EGLContext;
import android.os.SystemClock;
import com.android.camera.CameraSize;
import com.android.camera.FileCompat;
import com.android.camera.Util;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.encoder.MediaEncoder.MediaEncoderListener;
import com.android.camera.storage.Storage;
import com.android.gallery3d.ui.ExtTexture;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class LiveMediaRecorder {
    public static final int ERR_INIT_RECORD = 1;
    public static final int ERR_INVALID_ARGS = 0;
    public static final int ERR_START_RECORD = 2;
    public static final long START_OFFSET_MS = 450;
    /* access modifiers changed from: private */
    public static final String TAG = "LiveMediaRecorder";
    private String mCurrentVideoFilename;
    private ContentValues mCurrentVideoValues;
    private DrawExtTexAttribute mDrawExtTexAttribute = new DrawExtTexAttribute();
    private boolean mEnableAudio;
    private MediaMuxerWrapper mLastMuxer;
    private MediaAudioEncoder mMediaAudioEncoder;
    private MediaVideoEncoder mMediaVideoEncoder;
    private MediaMuxerWrapper mMuxer;
    private int mOrientationCompensation;
    private LiveMediaRecorderListener mRecorderListener;
    private CameraSize mVideoSize;

    public class EncoderListener implements MediaEncoderListener, LiveMediaRecorderListener {
        private WeakReference mModule;

        public EncoderListener(BaseModule baseModule) {
            this.mModule = new WeakReference(baseModule);
        }

        public void onError(int i) {
        }

        public void onPrepared(MediaEncoder mediaEncoder) {
            String access$000 = LiveMediaRecorder.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPrepared: encoder=");
            sb.append(mediaEncoder);
            Log.v(access$000, sb.toString());
        }

        public void onStop() {
            BaseModule baseModule = (BaseModule) this.mModule.get();
            if (baseModule != null) {
                baseModule.playVideoSound(true);
            }
        }

        public void onStopped(MediaEncoder mediaEncoder, boolean z) {
            String access$000 = LiveMediaRecorder.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onStopped: encoder=");
            sb.append(mediaEncoder);
            Log.v(access$000, sb.toString());
            if (z) {
                BaseModule baseModule = (BaseModule) this.mModule.get();
                if (baseModule != null) {
                    baseModule.executeSaveTask(true);
                }
            }
        }

        public void save(String str, ContentValues contentValues) {
            BaseModule baseModule = (BaseModule) this.mModule.get();
            if (baseModule != null) {
                baseModule.addSaveTask(str, contentValues);
            }
        }
    }

    public interface LiveMediaRecorderListener {
        void onError(int i);

        void onStop();

        void save(String str, ContentValues contentValues);
    }

    private void cleanupEmptyFile() {
        String str;
        StringBuilder sb;
        String str2;
        String str3 = this.mCurrentVideoFilename;
        if (str3 != null) {
            File file = new File(str3);
            if (!file.exists()) {
                str = TAG;
                sb = new StringBuilder();
                str2 = "no video file: ";
            } else if (file.length() == 0) {
                if (!Storage.isUseDocumentMode()) {
                    file.delete();
                } else {
                    FileCompat.deleteFile(this.mCurrentVideoFilename);
                }
                str = TAG;
                sb = new StringBuilder();
                str2 = "delete empty video file: ";
            } else {
                return;
            }
            sb.append(str2);
            sb.append(this.mCurrentVideoFilename);
            Log.d(str, sb.toString());
            this.mCurrentVideoFilename = null;
        }
    }

    private void releaseLastMediaRecorder() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("releaseLastMediaRecorder ");
        sb.append(this.mLastMuxer != null);
        Log.d(str, sb.toString());
        MediaMuxerWrapper mediaMuxerWrapper = this.mLastMuxer;
        if (mediaMuxerWrapper != null) {
            mediaMuxerWrapper.join();
            this.mLastMuxer = null;
        }
    }

    private void releaseMediaRecorder() {
        Log.v(TAG, "releaseMediaRecorder");
        MediaMuxerWrapper mediaMuxerWrapper = this.mMuxer;
        if (mediaMuxerWrapper != null) {
            this.mLastMuxer = mediaMuxerWrapper;
            cleanupEmptyFile();
        }
    }

    /* access modifiers changed from: protected */
    public void deleteVideoFile(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("delete invalid video ");
        sb.append(str);
        Log.v(str2, sb.toString());
        if (!new File(str).delete()) {
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("fail to delete ");
            sb2.append(str);
            Log.v(str3, sb2.toString());
        }
    }

    public boolean init(ContentValues contentValues, int i, EGLContext eGLContext, EncoderListener encoderListener) {
        return init(contentValues, i, eGLContext, encoderListener, true, 1.0f);
    }

    public boolean init(ContentValues contentValues, int i, EGLContext eGLContext, EncoderListener encoderListener, boolean z, float f) {
        this.mCurrentVideoValues = contentValues;
        this.mOrientationCompensation = i;
        this.mRecorderListener = encoderListener;
        this.mEnableAudio = z;
        this.mCurrentVideoFilename = this.mCurrentVideoValues.getAsString("_data");
        this.mVideoSize = Util.getResolution(contentValues);
        if (this.mVideoSize == null) {
            LiveMediaRecorderListener liveMediaRecorderListener = this.mRecorderListener;
            if (liveMediaRecorderListener != null) {
                liveMediaRecorderListener.onError(0);
            }
            return false;
        }
        try {
            releaseLastMediaRecorder();
            this.mMuxer = new MediaMuxerWrapper(this.mCurrentVideoFilename);
            MediaVideoEncoder mediaVideoEncoder = new MediaVideoEncoder(eGLContext, this.mMuxer, encoderListener, this.mVideoSize.width, this.mVideoSize.height);
            this.mMediaVideoEncoder = mediaVideoEncoder;
            this.mMediaVideoEncoder.setRecordSpeed(f);
            if (this.mEnableAudio) {
                this.mMediaAudioEncoder = new MediaAudioEncoder(this.mMuxer, encoderListener);
                this.mMediaAudioEncoder.setRecordSpeed(f);
            }
            this.mMuxer.prepare();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("rotation: ");
            sb.append(i);
            Log.d(str, sb.toString());
            this.mMuxer.setOrientationHint(this.mOrientationCompensation);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "initializeRecorder: ", (Throwable) e);
            LiveMediaRecorderListener liveMediaRecorderListener2 = this.mRecorderListener;
            if (liveMediaRecorderListener2 != null) {
                liveMediaRecorderListener2.onError(1);
            }
            return false;
        }
    }

    public void onSurfaceTextureUpdated(DrawAttribute drawAttribute, boolean z) {
        MediaVideoEncoder mediaVideoEncoder;
        synchronized (this) {
            mediaVideoEncoder = this.mMediaVideoEncoder;
        }
        if (mediaVideoEncoder != null && z) {
            mediaVideoEncoder.frameAvailableSoon(drawAttribute);
        }
    }

    public void onSurfaceTextureUpdated(DrawExtTexAttribute drawExtTexAttribute, boolean z) {
        MediaVideoEncoder mediaVideoEncoder;
        synchronized (this) {
            mediaVideoEncoder = this.mMediaVideoEncoder;
        }
        if (mediaVideoEncoder != null && z) {
            DrawExtTexAttribute drawExtTexAttribute2 = this.mDrawExtTexAttribute;
            ExtTexture extTexture = drawExtTexAttribute.mExtTexture;
            float[] fArr = drawExtTexAttribute.mTextureTransform;
            CameraSize cameraSize = this.mVideoSize;
            drawExtTexAttribute2.init(extTexture, fArr, 0, 0, cameraSize.width, cameraSize.height);
            mediaVideoEncoder.frameAvailableSoon(this.mDrawExtTexAttribute);
        }
    }

    public void release() {
        releaseMediaRecorder();
        releaseLastMediaRecorder();
    }

    public boolean startRecorder(long j) {
        long currentTimeMillis = 450 - (System.currentTimeMillis() - j);
        if (currentTimeMillis < 0) {
            currentTimeMillis = 0;
        }
        boolean startRecording = this.mMuxer.startRecording(currentTimeMillis);
        if (!startRecording) {
            this.mMuxer.stopRecording();
            LiveMediaRecorderListener liveMediaRecorderListener = this.mRecorderListener;
            if (liveMediaRecorderListener != null) {
                liveMediaRecorderListener.onError(2);
            }
            releaseMediaRecorder();
        }
        return startRecording;
    }

    public boolean stopRecorder(long j, boolean z) {
        long uptimeMillis = SystemClock.uptimeMillis() - j;
        MediaMuxerWrapper mediaMuxerWrapper = this.mMuxer;
        if (mediaMuxerWrapper != null) {
            mediaMuxerWrapper.stopRecording();
        }
        LiveMediaRecorderListener liveMediaRecorderListener = this.mRecorderListener;
        if (liveMediaRecorderListener != null) {
            liveMediaRecorderListener.onStop();
        }
        releaseMediaRecorder();
        boolean z2 = this.mCurrentVideoFilename == null;
        if (!z2 && (uptimeMillis < 1000 || z)) {
            deleteVideoFile(this.mCurrentVideoFilename);
            z2 = true;
        }
        if (!z2) {
            LiveMediaRecorderListener liveMediaRecorderListener2 = this.mRecorderListener;
            if (liveMediaRecorderListener2 != null) {
                liveMediaRecorderListener2.save(this.mCurrentVideoFilename, this.mCurrentVideoValues);
            }
        }
        return true;
    }
}
