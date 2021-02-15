package com.android.camera.module.encoder;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.ParcelFileDescriptor;
import com.android.camera.FileCompat;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.storage.Storage;
import java.nio.ByteBuffer;

public class MediaMuxerWrapper {
    private static final String TAG = "MediaMuxerWrapper";
    private MediaEncoder mAudioEncoder;
    private int mEncoderCount;
    private boolean mIsStarted;
    private MediaMuxer mMediaMuxer;
    private int mStartedCount;
    private MediaEncoder mVideoEncoder;

    public MediaMuxerWrapper(String str) {
        if (!Storage.isUseDocumentMode()) {
            this.mMediaMuxer = new MediaMuxer(str, 0);
        } else {
            ParcelFileDescriptor parcelFileDescriptor = null;
            try {
                parcelFileDescriptor = FileCompat.getParcelFileDescriptor(str, true);
                this.mMediaMuxer = new MediaMuxer(parcelFileDescriptor.getFileDescriptor(), 0);
            } catch (Exception e) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("open file failed, path = ");
                sb.append(str);
                Log.w(str2, sb.toString(), (Throwable) e);
            } catch (Throwable th) {
                Util.closeSafely(parcelFileDescriptor);
                throw th;
            }
            Util.closeSafely(parcelFileDescriptor);
        }
        this.mEncoderCount = 0;
        this.mStartedCount = 0;
        this.mIsStarted = false;
    }

    /* access modifiers changed from: 0000 */
    public void addEncoder(MediaEncoder mediaEncoder) {
        if (mediaEncoder instanceof MediaVideoEncoder) {
            if (this.mVideoEncoder == null) {
                this.mVideoEncoder = mediaEncoder;
            } else {
                throw new IllegalArgumentException("video encoder already added!");
            }
        } else if (!(mediaEncoder instanceof MediaAudioEncoder)) {
            throw new IllegalArgumentException("unsupported encoder!");
        } else if (this.mAudioEncoder == null) {
            this.mAudioEncoder = mediaEncoder;
        } else {
            throw new IllegalArgumentException("audio encoder already added!");
        }
        int i = 1;
        int i2 = this.mVideoEncoder != null ? 1 : 0;
        if (this.mAudioEncoder == null) {
            i = 0;
        }
        this.mEncoderCount = i2 + i;
    }

    /* access modifiers changed from: 0000 */
    public synchronized int addTrack(MediaFormat mediaFormat) {
        int addTrack;
        if (!this.mIsStarted) {
            addTrack = this.mMediaMuxer.addTrack(mediaFormat);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("addTrack: trackNum=");
            sb.append(this.mEncoderCount);
            sb.append(" trackIndex=");
            sb.append(addTrack);
            sb.append(" format=");
            sb.append(mediaFormat);
            Log.v(str, sb.toString());
        } else {
            throw new IllegalStateException("muxer already started");
        }
        return addTrack;
    }

    public synchronized boolean isStarted() {
        return this.mIsStarted;
    }

    public void join() {
        Log.d(TAG, "join>>>");
        MediaEncoder mediaEncoder = this.mAudioEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.join();
            this.mAudioEncoder = null;
        }
        MediaEncoder mediaEncoder2 = this.mVideoEncoder;
        if (mediaEncoder2 != null) {
            mediaEncoder2.join();
            this.mVideoEncoder = null;
        }
        Log.d(TAG, "join<<<");
    }

    public void prepare() {
        MediaEncoder mediaEncoder = this.mVideoEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.prepare();
        }
        MediaEncoder mediaEncoder2 = this.mAudioEncoder;
        if (mediaEncoder2 != null) {
            mediaEncoder2.prepare();
        }
    }

    public void setLocation(float f, float f2) {
        this.mMediaMuxer.setLocation(f, f2);
    }

    public void setOrientationHint(int i) {
        this.mMediaMuxer.setOrientationHint(i);
    }

    /* access modifiers changed from: 0000 */
    public synchronized boolean start() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("start: startedCount=");
        sb.append(this.mStartedCount);
        Log.d(str, sb.toString());
        this.mStartedCount++;
        if (this.mEncoderCount > 0 && this.mStartedCount == this.mEncoderCount) {
            this.mMediaMuxer.start();
            this.mIsStarted = true;
            notifyAll();
            Log.d(TAG, "MediaMuxer started");
        }
        return this.mIsStarted;
    }

    public boolean startRecording(long j) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startRecording: offset=");
        sb.append(j);
        Log.d(str, sb.toString());
        MediaEncoder mediaEncoder = this.mVideoEncoder;
        boolean z = mediaEncoder == null || mediaEncoder.startRecording(j);
        MediaEncoder mediaEncoder2 = this.mAudioEncoder;
        return mediaEncoder2 != null ? mediaEncoder2.startRecording(j) && z : z;
    }

    /* access modifiers changed from: 0000 */
    public synchronized boolean stop() {
        boolean z;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("stop: startedCount=");
        sb.append(this.mStartedCount);
        Log.d(str, sb.toString());
        z = true;
        this.mStartedCount--;
        if (this.mEncoderCount > 0 && this.mStartedCount <= 0) {
            this.mMediaMuxer.stop();
            this.mMediaMuxer.release();
            this.mIsStarted = false;
            Log.v(TAG, "MediaMuxer stopped");
        }
        if (this.mStartedCount > 0) {
            z = false;
        }
        return z;
    }

    public void stopRecording() {
        Log.d(TAG, "stopRecording>>>");
        MediaEncoder mediaEncoder = this.mAudioEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.stopRecording();
        }
        MediaEncoder mediaEncoder2 = this.mVideoEncoder;
        if (mediaEncoder2 != null) {
            mediaEncoder2.stopRecording();
        }
        Log.d(TAG, "stopRecording<<<");
    }

    /* access modifiers changed from: 0000 */
    public synchronized void writeSampleData(int i, ByteBuffer byteBuffer, BufferInfo bufferInfo) {
        if (this.mStartedCount > 0) {
            this.mMediaMuxer.writeSampleData(i, byteBuffer, bufferInfo);
        }
    }
}
