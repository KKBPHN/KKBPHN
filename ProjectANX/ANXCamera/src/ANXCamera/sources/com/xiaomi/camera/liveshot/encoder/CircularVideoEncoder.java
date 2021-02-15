package com.xiaomi.camera.liveshot.encoder;

import android.media.MediaCodec;
import android.media.MediaCodec.CodecException;
import android.media.MediaFormat;
import android.opengl.EGLContext;
import android.os.Handler;
import android.view.Surface;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.log.Log;
import com.xiaomi.camera.liveshot.encoder.CircularMediaEncoder.Snapshot;
import com.xiaomi.camera.liveshot.gles.RenderThread;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

public class CircularVideoEncoder extends CircularMediaEncoder {
    private static final boolean DEBUG_FPS = true;
    private static final String TAG = "CircularVideoEncoder";
    protected long mFirstPresentationTimeUs;
    private int mFpsOutputInterval = 500;
    private long mFrameStartTimestampNs = 0;
    private int mFramesRendered = 0;
    private Surface mInputSurface;
    protected long mLastPresentationTimeUs;
    private long mMinFrameRenderPeriodNs;
    private long mNextFrameTimestampNs;
    private final int mPreviewHeight;
    private final int mPreviewWidth;
    private RenderThread mRenderThread;
    private EGLContext mSharedEGLContext;

    public CircularVideoEncoder(MediaFormat mediaFormat, EGLContext eGLContext, long j, long j2, Queue queue) {
        super(mediaFormat, j, j2, queue);
        float f = this.mDesiredMediaFormat.getFloat("i-frame-interval");
        long millis = TimeUnit.MICROSECONDS.toMillis(this.mBufferingDurationUs);
        float f2 = f * 1000.0f * 2.0f;
        if (((float) millis) < f2) {
            StringBuilder sb = new StringBuilder();
            sb.append("Requested time span is too short: ");
            sb.append(millis);
            sb.append(" vs. ");
            sb.append(f2);
            throw new IllegalArgumentException(sb.toString());
        } else if (eGLContext != null) {
            this.mSharedEGLContext = eGLContext;
            int integer = this.mDesiredMediaFormat.getInteger("width");
            int integer2 = this.mDesiredMediaFormat.getInteger("height");
            this.mPreviewWidth = Math.min(integer, integer2);
            this.mPreviewHeight = Math.max(integer, integer2);
            try {
                this.mMediaCodec = MediaCodec.createEncoderByType(this.mDesiredMediaFormat.getString(IMediaFormat.KEY_MIME));
                this.mIsInitialized = true;
            } catch (IOException e) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to configure MediaCodec: ");
                sb2.append(e);
                throw new IllegalStateException(sb2.toString());
            }
        } else {
            throw new IllegalArgumentException("The shared EGLContext must not be null");
        }
    }

    public void doRelease() {
        if (this.mIsInitialized) {
            super.doRelease();
            this.mIsInitialized = false;
        }
    }

    public void doStart() {
        String str;
        String str2;
        Log.d(TAG, "start(): E");
        if (!this.mIsInitialized) {
            str = TAG;
            str2 = "start(): not initialized yet";
        } else if (this.mIsBuffering) {
            str = TAG;
            str2 = "start(): encoder is already running";
        } else {
            this.mCyclicBuffer.clear();
            try {
                this.mMediaCodec.configure(this.mDesiredMediaFormat, null, null, 1);
            } catch (CodecException e) {
                String str3 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configure failed due to codec error: ");
                sb.append(e);
                Log.d(str3, sb.toString());
            }
            this.mInputSurface = this.mMediaCodec.createInputSurface();
            RenderThread renderThread = new RenderThread(TAG, this.mPreviewWidth, this.mPreviewHeight, this.mSharedEGLContext, this.mInputSurface, true);
            this.mRenderThread = renderThread;
            this.mRenderThread.start();
            this.mRenderThread.waitUntilReady();
            this.mMediaCodec.setCallback(this, new Handler(this.mEncodingThread.getLooper()));
            this.mCurrentPresentationTimeUs = 0;
            this.mFirstPresentationTimeUs = 0;
            this.mLastPresentationTimeUs = 0;
            super.doStart();
            this.mIsBuffering = true;
            str = TAG;
            str2 = "start(): X";
        }
        Log.d(str, str2);
    }

    public synchronized void doStop() {
        Log.d(TAG, "stop(): E");
        if (this.mIsInitialized) {
            if (this.mIsBuffering) {
                this.mIsBuffering = false;
                if (this.mRenderThread != null) {
                    this.mRenderThread.quit();
                    this.mRenderThread = null;
                }
                if (this.mInputSurface != null) {
                    this.mInputSurface.release();
                    this.mInputSurface = null;
                }
                super.doStop();
                Log.d(TAG, "clear pending snapshot requests: E");
                ArrayList<Snapshot> arrayList = new ArrayList<>();
                synchronized (this.mSnapshots) {
                    arrayList.addAll(this.mSnapshots);
                    this.mSnapshots.clear();
                }
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("cleared ");
                sb.append(arrayList.size());
                sb.append(" snapshot requests.");
                Log.d(str, sb.toString());
                for (Snapshot putEos : arrayList) {
                    try {
                        putEos.putEos();
                    } catch (InterruptedException e) {
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Failed to putEos: ");
                        sb2.append(e);
                        Log.d(str2, sb2.toString());
                    }
                }
                Log.d(TAG, "clear pending snapshot requests: X");
                Log.d(TAG, "stop(): X");
            }
        }
    }

    /* access modifiers changed from: protected */
    public long getNextPresentationTimeUs(long j) {
        long j2 = this.mFirstPresentationTimeUs;
        if (j2 == 0) {
            this.mFirstPresentationTimeUs = j;
            return 0;
        }
        long j3 = j - j2;
        long j4 = this.mLastPresentationTimeUs;
        if (j4 >= j3) {
            this.mLastPresentationTimeUs = j4 + 9643;
            return this.mLastPresentationTimeUs;
        }
        this.mLastPresentationTimeUs = j3;
        return j3;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0083, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void onSurfaceTextureUpdated(DrawExtTexAttribute drawExtTexAttribute) {
        if (this.mIsInitialized) {
            if (this.mIsBuffering) {
                if (this.mMinFrameRenderPeriodNs > 0) {
                    long nanoTime = System.nanoTime();
                    if (nanoTime < this.mNextFrameTimestampNs) {
                        Log.d(TAG, "Dropping frame - fps reduction is active.");
                        return;
                    } else {
                        this.mNextFrameTimestampNs += this.mMinFrameRenderPeriodNs;
                        this.mNextFrameTimestampNs = Math.max(this.mNextFrameTimestampNs, nanoTime);
                    }
                }
                this.mRenderThread.syncDraw(drawExtTexAttribute);
                long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
                if (this.mFrameStartTimestampNs > 0) {
                    long j = millis - this.mFrameStartTimestampNs;
                    this.mFramesRendered++;
                    if (j > ((long) this.mFpsOutputInterval)) {
                        double d = ((double) (this.mFramesRendered * 1000)) / ((double) j);
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onSurfaceTextureUpdated(): ");
                        sb.append(d);
                        Log.d(str, sb.toString());
                        this.mFrameStartTimestampNs = millis;
                        this.mFramesRendered = 0;
                    }
                } else {
                    this.mFrameStartTimestampNs = millis;
                }
            }
        }
    }

    public void setCinematicEnable(boolean z) {
        if (this.mIsInitialized && this.mIsBuffering) {
            this.mRenderThread.setCinematicEnable(z);
        }
    }

    public synchronized void setFilterId(int i) {
        if (this.mIsInitialized) {
            if (this.mIsBuffering) {
                this.mRenderThread.setFilterId(i);
            }
        }
    }

    public void setFpsReduction(float f) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setFpsReduction: ");
        sb.append(f);
        Log.d(str, sb.toString());
        this.mMinFrameRenderPeriodNs = f <= 0.0f ? Long.MAX_VALUE : (long) (((float) TimeUnit.SECONDS.toNanos(1)) / f);
    }
}
