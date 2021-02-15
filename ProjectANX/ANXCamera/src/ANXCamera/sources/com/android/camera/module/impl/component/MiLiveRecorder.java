package com.android.camera.module.impl.component;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import com.android.camera.ActivityBase;
import com.android.camera.Display;
import com.android.camera.SurfaceTextureScreenNail.SurfaceTextureScreenNailCallback;
import com.android.camera.data.DataRepository;
import com.android.camera.effect.FrameBuffer;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.framework.gles.FrameTexture;
import com.android.camera.effect.framework.gles.FullFrameTexture;
import com.android.camera.effect.framework.gles.FullFramenOESTexture;
import com.android.camera.effect.framework.gles.OpenGlUtils;
import com.android.camera.log.Log;
import com.android.camera.module.MiLiveModule;
import com.android.camera.module.impl.component.ILive.ILiveRecorder;
import com.android.camera.module.impl.component.ILive.ILiveRecorderStateListener;
import com.android.camera.module.impl.component.ILive.ILiveRecordingTimeListener;
import com.android.camera.module.impl.component.ILive.ILiveSegmentData;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.gallery3d.ui.GLCanvas;
import com.xiaomi.camera.rx.CameraSchedulers;
import com.xiaomi.camera.util.SystemProperties;
import com.xiaomi.recordmediaprocess.EffectCameraNotifier;
import com.xiaomi.recordmediaprocess.MediaEffectCamera;
import com.xiaomi.recordmediaprocess.OpenGlRender;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;
import miui.text.ExtraTextUtils;

public class MiLiveRecorder implements ILiveRecorder, SurfaceTextureScreenNailCallback {
    /* access modifiers changed from: private */
    public final String TAG;
    private ActivityBase mActivity;
    private String mAudioPath;
    private int mBitrate;
    private EffectCameraNotifier mCameraNotifier;
    private CountDownTimer mCountDownTimer;
    /* access modifiers changed from: private */
    public float mCurSpeed;
    private int[] mDebugFrameBuffer;
    private final boolean mDump;
    private String mFilterBitmapPath;
    private int mFps;
    private FrameBuffer mFrameBuffer;
    private FrameTexture mFullFrameTexture;
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public int mLiveState;
    private long mMaxDuration;
    private MediaEffectCamera mMediaEffectCamera;
    private OpenGlRender mOpenGlRender;
    /* access modifiers changed from: private */
    public ILiveRecordingTimeListener mRecordingTimeListener;
    /* access modifiers changed from: private */
    public Stack mSegments;
    private long mStartTime;
    private ILiveRecorderStateListener mStateListener;
    private final ReentrantLock mSurfaceLock;
    private int mVideoHeight;
    private String mVideoSaveDirPath;
    private int mVideoWidth;

    public class Builder {
        /* access modifiers changed from: private */
        public ActivityBase mActivityBase;
        /* access modifiers changed from: private */
        public int mBitrate;
        /* access modifiers changed from: private */
        public int mFps;
        /* access modifiers changed from: private */
        public Handler mHandler;
        /* access modifiers changed from: private */
        public long mMaxDuration;
        /* access modifiers changed from: private */
        public ILiveRecordingTimeListener mRecordingTimeListener;
        /* access modifiers changed from: private */
        public List mSegmentData;
        /* access modifiers changed from: private */
        public ILiveRecorderStateListener mStateListener;
        private int mVideoHeight;
        /* access modifiers changed from: private */
        public String mVideoSaveDirPath;
        private int mVideoWidth;

        public Builder(ActivityBase activityBase, int i, int i2) {
            this.mActivityBase = activityBase;
            this.mVideoWidth = i;
            this.mVideoHeight = i2;
        }

        public MiLiveRecorder build() {
            return new MiLiveRecorder(this);
        }

        public Builder setBitrate(int i) {
            this.mBitrate = i;
            return this;
        }

        public Builder setFps(int i) {
            this.mFps = i;
            return this;
        }

        public Builder setHandler(Handler handler) {
            this.mHandler = handler;
            return this;
        }

        public Builder setMaxDuration(long j) {
            this.mMaxDuration = j;
            return this;
        }

        public Builder setRecordingTimeListener(ILiveRecordingTimeListener iLiveRecordingTimeListener) {
            this.mRecordingTimeListener = iLiveRecordingTimeListener;
            return this;
        }

        public Builder setSegmentData(List list) {
            this.mSegmentData = list;
            return this;
        }

        public Builder setStateListener(ILiveRecorderStateListener iLiveRecorderStateListener) {
            this.mStateListener = iLiveRecorderStateListener;
            return this;
        }

        public Builder setVideoSaveDirPath(String str) {
            this.mVideoSaveDirPath = str;
            return this;
        }
    }

    public class MiLiveItem implements ILiveSegmentData {
        private long mDuration;
        private long mNextPos;
        private String mPath;
        private float mSpeed;

        public MiLiveItem(String str, long j, long j2, float f) {
            this.mPath = str;
            this.mNextPos = j;
            this.mDuration = j2;
            this.mSpeed = f;
        }

        public long getDuration() {
            return this.mDuration;
        }

        public long getNextPos() {
            return this.mNextPos;
        }

        public String getPath() {
            String str = this.mPath;
            return str == null ? "" : str;
        }

        public float getSpeed() {
            return this.mSpeed;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("MiLiveItem{mPath='");
            sb.append(this.mPath);
            sb.append('\'');
            sb.append(", mNextPos=");
            sb.append(this.mNextPos);
            sb.append(", mDuration=");
            sb.append(this.mDuration);
            sb.append(", mSpeed=");
            sb.append(this.mSpeed);
            sb.append('}');
            return sb.toString();
        }
    }

    private MiLiveRecorder(Builder builder) {
        StringBuilder sb = new StringBuilder();
        sb.append(MiLiveRecorder.class.getSimpleName());
        sb.append("@");
        sb.append(hashCode());
        this.TAG = sb.toString();
        this.mSegments = new Stack();
        this.mLiveState = 0;
        this.mSurfaceLock = new ReentrantLock();
        this.mDebugFrameBuffer = new int[1];
        this.mCameraNotifier = new EffectCameraNotifier() {
            public /* synthetic */ void O00ooOOO() {
                if (MiLiveRecorder.this.mRecordingTimeListener != null) {
                    MiLiveRecorder.this.mRecordingTimeListener.onRecordingTimeFinish();
                }
            }

            public void OnNeedStopRecording() {
                if (MiLiveRecorder.this.mHandler != null) {
                    Log.d(MiLiveRecorder.this.TAG, "OnNeedStopRecording");
                    MiLiveRecorder.this.mHandler.post(new O000O0o0(this));
                    return;
                }
                Log.d(MiLiveRecorder.this.TAG, "OnNeedStopRecording fail~");
            }

            public void OnNotifyRender() {
                Log.d(MiLiveRecorder.this.TAG, "OnNotifyRender");
            }

            public void OnRecordFailed() {
                Log.k(3, MiLiveRecorder.this.TAG, "OnRecordFailed");
                MiLiveRecorder.this.mSegments.clear();
                MiLiveRecorder.this.setLiveState(9);
                MiLiveRecorder.this.resetToPreview();
            }

            public void OnRecordFinish(String str) {
            }

            public void OnRecordFinish(String str, long j, long j2) {
                Stack access$1100 = MiLiveRecorder.this.mSegments;
                MiLiveItem miLiveItem = new MiLiveItem(str, j, j2, MiLiveRecorder.this.mCurSpeed);
                access$1100.push(miLiveItem);
                if (ILive.getTotalDuration(MiLiveRecorder.this.mSegments) > 500 || MiLiveRecorder.this.mLiveState != 5) {
                    String access$900 = MiLiveRecorder.this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("OnRecordFinish segments = ");
                    sb.append(Arrays.toString(MiLiveRecorder.this.mSegments.toArray()));
                    Log.k(3, access$900, sb.toString());
                    if (MiLiveRecorder.this.mLiveState == 6) {
                        MiLiveRecorder.this.setLiveState(3);
                    } else if (MiLiveRecorder.this.mLiveState == 5) {
                        MiLiveRecorder.this.setLiveState(8);
                    }
                    return;
                }
                String access$9002 = MiLiveRecorder.this.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("recording time = ");
                sb2.append(j2);
                sb2.append(", it's too short");
                Log.d(access$9002, sb2.toString());
                OnRecordFailed();
            }
        };
        this.mDump = SystemProperties.getBoolean("camera.debug.dump_milive", false);
        this.mActivity = builder.mActivityBase;
        this.mBitrate = builder.mBitrate;
        this.mFps = builder.mFps;
        this.mVideoSaveDirPath = builder.mVideoSaveDirPath;
        this.mMaxDuration = builder.mMaxDuration;
        this.mStateListener = builder.mStateListener;
        this.mRecordingTimeListener = builder.mRecordingTimeListener;
        this.mHandler = builder.mHandler;
        if (builder.mSegmentData != null) {
            this.mSegments.addAll(builder.mSegmentData);
        }
        Log.d(this.TAG, String.format("MiLiveRecorder dump:{%s} mSegments:{%s}", new Object[]{Boolean.valueOf(this.mDump), Arrays.toString(this.mSegments.toArray())}));
        MiLiveModule.loadLibs(this.mActivity.getApplicationContext(), MiLiveModule.LIB_LOAD_CALLER_RECORDER);
    }

    private void clearAllSegments() {
        while (!this.mSegments.empty()) {
            File file = new File(((ILiveSegmentData) this.mSegments.pop()).getPath());
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0085  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void dump(int i, int i2, long j, int i3, int i4) {
        int i5;
        int i6;
        boolean z;
        int i7 = i2;
        if (this.mDump && !C0124O00000oO.O0o0ooO) {
            if (i7 == 3553) {
                if (!(this.mFullFrameTexture instanceof FullFrameTexture)) {
                    this.mFullFrameTexture = new FullFrameTexture();
                    i6 = i3;
                    i5 = i4;
                }
                i6 = i3;
                i5 = i4;
                z = true;
                if (!z) {
                }
                if (!FileUtils.hasDir(FileUtils.VIDEO_DUMP)) {
                }
                GLES20.glBindFramebuffer(36160, this.mDebugFrameBuffer[0]);
                GLES20.glViewport(0, 0, i6, i5);
                this.mFullFrameTexture.draw(i);
                String valueOf = String.valueOf(j);
                StringBuilder sb = new StringBuilder();
                sb.append(FileUtils.VIDEO_DUMP);
                sb.append(valueOf);
                sb.append("dump.jpg");
                String sb2 = sb.toString();
                String str = this.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("dump ");
                sb3.append(sb2);
                Log.d(str, sb3.toString());
                OpenGlUtils.dumpToJpg(0, 0, i6, i5, sb2);
                GLES20.glBindFramebuffer(36160, 0);
            } else if (i7 == 36197) {
                if (!(this.mFullFrameTexture instanceof FullFramenOESTexture)) {
                    this.mFullFrameTexture = new FullFramenOESTexture();
                    i5 = i3;
                    i6 = i4;
                }
                i6 = i3;
                i5 = i4;
                z = true;
                if (!z) {
                    int[] iArr = new int[1];
                    GLES20.glGenTextures(1, iArr, 0);
                    GLES20.glBindTexture(3553, iArr[0]);
                    GLES20.glTexImage2D(3553, 0, 6408, i3, i4, 0, 6408, 5121, null);
                    GLES20.glGenFramebuffers(1, this.mDebugFrameBuffer, 0);
                    GLES20.glBindFramebuffer(36160, this.mDebugFrameBuffer[0]);
                    GLES20.glFramebufferTexture2D(36160, 36064, 3553, iArr[0], 0);
                    GLES20.glBindFramebuffer(36160, 0);
                }
                if (!FileUtils.hasDir(FileUtils.VIDEO_DUMP)) {
                    FileUtils.makeNoMediaDir(FileUtils.VIDEO_DUMP);
                }
                GLES20.glBindFramebuffer(36160, this.mDebugFrameBuffer[0]);
                GLES20.glViewport(0, 0, i6, i5);
                this.mFullFrameTexture.draw(i);
                String valueOf2 = String.valueOf(j);
                StringBuilder sb4 = new StringBuilder();
                sb4.append(FileUtils.VIDEO_DUMP);
                sb4.append(valueOf2);
                sb4.append("dump.jpg");
                String sb22 = sb4.toString();
                String str2 = this.TAG;
                StringBuilder sb32 = new StringBuilder();
                sb32.append("dump ");
                sb32.append(sb22);
                Log.d(str2, sb32.toString());
                OpenGlUtils.dumpToJpg(0, 0, i6, i5, sb22);
                GLES20.glBindFramebuffer(36160, 0);
            } else {
                return;
            }
            z = false;
            if (!z) {
            }
            if (!FileUtils.hasDir(FileUtils.VIDEO_DUMP)) {
            }
            GLES20.glBindFramebuffer(36160, this.mDebugFrameBuffer[0]);
            GLES20.glViewport(0, 0, i6, i5);
            this.mFullFrameTexture.draw(i);
            String valueOf22 = String.valueOf(j);
            StringBuilder sb42 = new StringBuilder();
            sb42.append(FileUtils.VIDEO_DUMP);
            sb42.append(valueOf22);
            sb42.append("dump.jpg");
            String sb222 = sb42.toString();
            String str22 = this.TAG;
            StringBuilder sb322 = new StringBuilder();
            sb322.append("dump ");
            sb322.append(sb222);
            Log.d(str22, sb322.toString());
            OpenGlUtils.dumpToJpg(0, 0, i6, i5, sb222);
            GLES20.glBindFramebuffer(36160, 0);
        }
    }

    private long getNextAudioPos() {
        Stack stack = this.mSegments;
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        ILiveSegmentData iLiveSegmentData = (ILiveSegmentData) this.mSegments.peek();
        if (iLiveSegmentData != null) {
            return iLiveSegmentData.getNextPos();
        }
        return 0;
    }

    private String getStateString(int i) {
        switch (i) {
            case 0:
                return "IDLE";
            case 1:
                return "PREVIEWING";
            case 2:
                return "RECORDING";
            case 3:
                return "RECORDING_PAUSED";
            case 4:
                return "PENDING_START_RECORDING";
            case 5:
                return "PENDING_STOP_RECORDING";
            case 6:
                return "PENDING_PAUSE_RECORDING";
            case 7:
                return "PENDING_RESUME_RECORDING";
            case 8:
                return "RECORDING_DONE";
            case 9:
                return "RECORDING_ERROR";
            default:
                return "UNKNOWN";
        }
    }

    private void initMediaCamera() {
        try {
            this.mSurfaceLock.lock();
            if (this.mVideoHeight > 0) {
                if (this.mVideoWidth > 0) {
                    if (this.mMediaEffectCamera != null) {
                        this.mMediaEffectCamera.DestructMediaEffectCamera();
                    }
                    this.mMediaEffectCamera = new MediaEffectCamera();
                    this.mMediaEffectCamera.ConstructMediaEffectCamera(this.mVideoWidth, this.mVideoHeight, this.mBitrate, this.mFps, this.mCameraNotifier);
                    this.mSurfaceLock.unlock();
                    return;
                }
            }
            Log.e(this.TAG, "invalid video size.");
        } finally {
            this.mSurfaceLock.unlock();
        }
    }

    private void releaseBuffer() {
        FrameBuffer frameBuffer = this.mFrameBuffer;
        if (frameBuffer != null) {
            frameBuffer.getTexture().recycle();
            this.mFrameBuffer.delete();
            this.mFrameBuffer = null;
        }
    }

    /* access modifiers changed from: private */
    public void resetToPreview() {
        int i = this.mLiveState;
        if (i == 9 || i == 8) {
            setLiveState(1);
        }
    }

    /* access modifiers changed from: private */
    public void setLiveState(int i) {
        if (i != this.mLiveState) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("live state change from ");
            sb.append(getStateString(this.mLiveState));
            sb.append(" to ");
            sb.append(getStateString(i));
            Log.d(str, sb.toString());
            this.mLiveState = i;
            ILiveRecorderStateListener iLiveRecorderStateListener = this.mStateListener;
            if (iLiveRecorderStateListener != null) {
                iLiveRecorderStateListener.onStateChange(this.mLiveState);
            }
        }
    }

    private void startRecordingTime(ILiveRecordingTimeListener iLiveRecordingTimeListener) {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        float totalDuration = (float) (this.mMaxDuration - ILive.getTotalDuration(this.mSegments));
        float f = this.mCurSpeed;
        long j = (long) (totalDuration * f);
        final ILiveRecordingTimeListener iLiveRecordingTimeListener2 = iLiveRecordingTimeListener;
        AnonymousClass2 r2 = new CountDownTimer(j, (long) (f * 1000.0f)) {
            public void onFinish() {
                Log.d(MiLiveRecorder.this.TAG, "count down onFinish~");
            }

            public void onTick(long j) {
                ILiveRecordingTimeListener iLiveRecordingTimeListener = iLiveRecordingTimeListener2;
                if (iLiveRecordingTimeListener != null) {
                    iLiveRecordingTimeListener.onRecordingTimeUpdate(j, MiLiveRecorder.this.mCurSpeed);
                }
            }
        };
        this.mCountDownTimer = r2;
        this.mStartTime = System.currentTimeMillis();
        this.mCountDownTimer.start();
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startRecordingTime ");
        sb.append(j);
        Log.d(str, sb.toString());
    }

    public /* synthetic */ void O00ooOOo() {
        try {
            this.mSurfaceLock.lock();
            Log.d(this.TAG, "release");
            if (this.mMediaEffectCamera != null) {
                this.mMediaEffectCamera.DestructMediaEffectCamera();
                this.mMediaEffectCamera = null;
            }
            setLiveState(0);
            this.mActivity = null;
            MiLiveModule.unloadLibs(MiLiveModule.LIB_LOAD_CALLER_RECORDER);
        } finally {
            this.mSurfaceLock.unlock();
        }
    }

    public void deletePreSegment() {
        if (this.mLiveState == 3 && !this.mSegments.empty()) {
            ILiveSegmentData iLiveSegmentData = (ILiveSegmentData) this.mSegments.pop();
            DataRepository.dataItemLive().setMiLiveSegmentData(this.mSegments);
            long totalDuration = ILive.getTotalDuration(this.mSegments);
            ILiveRecordingTimeListener iLiveRecordingTimeListener = this.mRecordingTimeListener;
            if (iLiveRecordingTimeListener != null) {
                iLiveRecordingTimeListener.onRecordingTimeUpdate(Math.min(this.mMaxDuration - totalDuration, 15000), 1.0f);
            }
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("deletePreSegment = ");
            sb.append(this.mSegments.size());
            Log.d(str, sb.toString());
            if (!TextUtils.isEmpty(iLiveSegmentData.getPath())) {
                File file = new File(iLiveSegmentData.getPath());
                if (file.exists() && file.isFile()) {
                    boolean delete = file.delete();
                    String str2 = this.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("deletePreSegment success = ");
                    sb2.append(delete);
                    Log.k(3, str2, sb2.toString());
                }
            }
        }
    }

    public SurfaceTexture genInputSurfaceTexture() {
        setLiveState(0);
        return null;
    }

    public String getAudioPath() {
        return this.mAudioPath;
    }

    public List getLiveSegments() {
        return this.mSegments;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public void init(int i, int i2) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initPreview size ");
        sb.append(i);
        sb.append("x");
        sb.append(i2);
        Log.d(str, sb.toString());
        if (this.mVideoWidth != Math.max(i, i2) || this.mVideoHeight != Math.min(i, i2)) {
            this.mVideoWidth = Math.max(i, i2);
            this.mVideoHeight = Math.min(i, i2);
            initMediaCamera();
        }
    }

    public void onSurfaceTextureCreated(SurfaceTexture surfaceTexture) {
    }

    public void onSurfaceTextureReleased() {
        releaseBuffer();
        FrameTexture frameTexture = this.mFullFrameTexture;
        if (frameTexture != null) {
            frameTexture.release();
            this.mFullFrameTexture = null;
        }
    }

    public void onSurfaceTextureUpdated(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
        try {
            this.mSurfaceLock.lock();
            if (!(this.mActivity == null || this.mActivity.getCameraScreenNail() == null || this.mActivity.getCameraScreenNail().getExtTexture() == null || drawExtTexAttribute == null || drawExtTexAttribute.mWidth == 0)) {
                if (drawExtTexAttribute.mHeight != 0) {
                    if (this.mOpenGlRender == null) {
                        this.mOpenGlRender = new OpenGlRender();
                    }
                    if (this.mFrameBuffer == null) {
                        this.mFrameBuffer = new FrameBuffer(this.mActivity.getGLView().getGLCanvas(), drawExtTexAttribute.mHeight, drawExtTexAttribute.mWidth, 0);
                        this.mOpenGlRender.SetCurrentGLContext(this.mFrameBuffer.getTexture().getId());
                    }
                    if (this.mLiveState == 0) {
                        if (this.mSegments.isEmpty()) {
                            setLiveState(1);
                        } else {
                            setLiveState(3);
                        }
                    }
                    if (this.mLiveState == 2 || this.mLiveState == 7 || this.mLiveState == 4) {
                        gLCanvas.beginBindFrameBuffer(this.mFrameBuffer);
                        gLCanvas.getState().pushState();
                        gLCanvas.getState().translate(((float) drawExtTexAttribute.mWidth) / 2.0f, ((float) drawExtTexAttribute.mHeight) / 2.0f);
                        gLCanvas.getState().scale(1.0f, -1.0f, 1.0f);
                        gLCanvas.getState().rotate(-90.0f, 0.0f, 0.0f, 1.0f);
                        gLCanvas.getState().translate(((float) (-drawExtTexAttribute.mHeight)) / 2.0f, ((float) (-drawExtTexAttribute.mWidth)) / 2.0f);
                        if (Display.fitDisplayFull(1.3333333f)) {
                            gLCanvas.getState().translate((float) (-drawExtTexAttribute.mX), (float) drawExtTexAttribute.mY);
                        } else {
                            gLCanvas.getState().translate((float) drawExtTexAttribute.mX, (float) (-drawExtTexAttribute.mY));
                        }
                        gLCanvas.draw(drawExtTexAttribute);
                        gLCanvas.getState().popState();
                        gLCanvas.endBindFrameBuffer();
                        if (this.mLiveState == 4) {
                            dump(this.mFrameBuffer.getTexture().getId(), this.mFrameBuffer.getTexture().getTarget(), this.mActivity.getCameraScreenNail().getSurfaceTexture().getTimestamp(), drawExtTexAttribute.mWidth / 4, drawExtTexAttribute.mHeight / 4);
                        } else {
                            this.mMediaEffectCamera.NeedProcessTexture(this.mActivity.getCameraScreenNail().getSurfaceTexture().getTimestamp() / ExtraTextUtils.MB, this.mVideoWidth, this.mVideoHeight);
                        }
                        if (this.mLiveState == 7 || this.mLiveState == 4) {
                            setLiveState(2);
                        }
                    }
                    this.mSurfaceLock.unlock();
                }
            }
        } finally {
            this.mSurfaceLock.unlock();
        }
    }

    public void pauseRecording() {
        if (this.mLiveState == 2) {
            String str = "pauseRecording";
            Log.d(this.TAG, str);
            CountDownTimer countDownTimer = this.mCountDownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            Log.k(3, this.TAG, str);
            setLiveState(6);
            this.mMediaEffectCamera.StopRecording();
        }
    }

    public void release() {
        CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new O000O0o(this));
    }

    public void reset() {
        Log.d(this.TAG, BaseEvent.RESET);
        if (this.mLiveState == 8) {
            setLiveState(1);
        }
    }

    public void resumeRecording() {
        if (!(this.mLiveState != 3 || this.mVideoSaveDirPath == null || this.mFilterBitmapPath == null || this.mAudioPath == null)) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("resumeRecording path = ");
            sb.append(this.mVideoSaveDirPath);
            sb.append(",mFilterBitmapPath = ");
            sb.append(this.mFilterBitmapPath);
            sb.append(",mAudioPath = ");
            sb.append(this.mAudioPath);
            sb.append(",mCurSpeed = ");
            sb.append(this.mCurSpeed);
            sb.append(",segments = ");
            sb.append(Arrays.toString(this.mSegments.toArray()));
            Log.k(3, str, sb.toString());
            setLiveState(7);
            DataRepository.dataItemLive().setMiLiveSegmentData(this.mSegments);
            long totalDuration = this.mMaxDuration - ILive.getTotalDuration(this.mSegments);
            if (totalDuration < 0) {
                String str2 = this.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("resumeRecording error,last duration : ");
                sb2.append(totalDuration);
                Log.w(str2, sb2.toString());
            }
            this.mMediaEffectCamera.StartRecording(this.mVideoSaveDirPath, this.mFilterBitmapPath, this.mAudioPath, getNextAudioPos(), this.mCurSpeed, totalDuration < 500 ? 500 : totalDuration);
            startRecordingTime(this.mRecordingTimeListener);
        }
    }

    public void setAudioPath(String str) {
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setAudioPath = ");
        sb.append(str);
        Log.d(str2, sb.toString());
        this.mAudioPath = str;
    }

    public void setFilterPath(String str) {
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setFilterPath = ");
        sb.append(str);
        Log.d(str2, sb.toString());
        this.mFilterBitmapPath = str;
    }

    public void setOrientation(int i) {
        MediaEffectCamera mediaEffectCamera = this.mMediaEffectCamera;
        if (mediaEffectCamera != null) {
            mediaEffectCamera.SetOrientation(i);
        }
    }

    public void setSpeed(float f) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setSpeed = ");
        sb.append(f);
        Log.k(3, str, sb.toString());
        this.mCurSpeed = f;
    }

    public void startRecording() {
        int i = this.mLiveState;
        if (!((i != 1 && i != 8) || this.mVideoSaveDirPath == null || this.mFilterBitmapPath == null || this.mAudioPath == null)) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startRecording path = ");
            sb.append(this.mVideoSaveDirPath);
            sb.append(",mFilterBitmapPath = ");
            sb.append(this.mFilterBitmapPath);
            sb.append(",mAudioPath = ");
            sb.append(this.mAudioPath);
            sb.append(",mCurSpeed = ");
            sb.append(this.mCurSpeed);
            Log.k(3, str, sb.toString());
            this.mSegments.clear();
            DataRepository.dataItemLive().setMiLiveSegmentData(this.mSegments);
            this.mMediaEffectCamera.StartRecording(this.mVideoSaveDirPath, this.mFilterBitmapPath, this.mAudioPath, 0, this.mCurSpeed, this.mMaxDuration);
            setLiveState(4);
            startRecordingTime(this.mRecordingTimeListener);
        }
    }

    public void stopRecording() {
        int i = this.mLiveState;
        if (i == 2 || i == 3) {
            Log.k(3, this.TAG, "stopRecording");
            CountDownTimer countDownTimer = this.mCountDownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            if (this.mLiveState == 2) {
                setLiveState(5);
                this.mMediaEffectCamera.StopRecording();
            } else if (this.mSegments.isEmpty()) {
                setLiveState(1);
            } else {
                setLiveState(8);
                resetToPreview();
            }
        }
    }
}
