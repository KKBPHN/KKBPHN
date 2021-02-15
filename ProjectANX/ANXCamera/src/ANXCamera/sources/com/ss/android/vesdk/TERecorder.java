package com.ss.android.vesdk;

import O00000o.O000000o.O000000o.O000000o;
import O00000o.O000000o.O000000o.O00000o0;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Surface;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ss.android.medialib.FaceBeautyInvoker;
import com.ss.android.medialib.FaceBeautyInvoker.OnPictureCallback;
import com.ss.android.medialib.FaceBeautyInvoker.OnPictureCallbackV2;
import com.ss.android.medialib.audio.AudioDataProcessThread.OnProcessDataListener;
import com.ss.android.medialib.camera.ImageFrame;
import com.ss.android.medialib.common.Common;
import com.ss.android.medialib.common.Common.IOnOpenGLCallback;
import com.ss.android.medialib.common.Common.IShotScreenCallback;
import com.ss.android.medialib.common.ImageUtils;
import com.ss.android.medialib.listener.NativeInitListener;
import com.ss.android.medialib.listener.SlamDetectListener;
import com.ss.android.ttve.monitor.MonitorUtils;
import com.ss.android.vesdk.VECameraSettings.CAMERA_FACING_ID;
import com.ss.android.vesdk.VEListener.VERecorderNativeInitListener;
import com.ss.android.vesdk.VEVideoEncodeSettings.ENCODE_PROFILE;
import com.ss.android.vesdk.keyvaluepair.VEKeyValue;
import com.ss.android.vesdk.runtime.VERecorderResManager;
import com.ss.android.vesdk.runtime.VERuntime;
import com.ss.android.vesdk.utils.VETextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.text.ExtraTextUtils;

public class TERecorder implements O000000o, OnProcessDataListener {
    public static final int PIC_STATUS_OUTPUT = 2;
    public static final int PIC_STATUS_RENDER = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_RECORDING = 3;
    public static final int STATE_RUNNING = 2;
    /* access modifiers changed from: private */
    public static final String TAG = "TERecorder";
    public static final int TYPE_PLAY_AUDIOTRACK = 2;
    public static final int TYPE_PLAY_OPENSL = 4;
    public static final int TYPE_RECORD_AUDIORECODER = 1;
    public static final int TYPE_RECORD_EMPTY = 0;
    public static final int TYPE_RECORD_PLAY_AUDIOTRACK = 3;
    public static final int TYPE_RECORD_PLAY_OPENSL = 5;
    private VEAudioEncodeSettings mAudioEncodeSettings;
    private O00000o0 mAudioRecorder;
    private boolean mBGMIsLoop;
    private String mBGMPath;
    private int mBGMTrimIn;
    private float mBGMVolume;
    /* access modifiers changed from: private */
    public VECameraSettings mCameraSettings;
    private Context mContext;
    /* access modifiers changed from: private */
    public Handler mExternalListenerInvokerHandler;
    /* access modifiers changed from: private */
    public VERecorderNativeInitListener mExternalNativeInitListener;
    private OnPictureRenderListener mExternalPictureRenderListener;
    /* access modifiers changed from: private */
    public IRenderCallback mExternalRenderCallback;
    /* access modifiers changed from: private */
    public List mExternalSlamDetectListeners;
    private IOnOpenGLCallback mGLCreateCallback;
    /* access modifiers changed from: private */
    public Texture mInputTexture;
    /* access modifiers changed from: private */
    public AtomicBoolean mIsCameraSettings;
    /* access modifiers changed from: private */
    public boolean mIsTextureCreatedBySelf;
    private int mLastAudioOption;
    private NativeInitListener mNativeInitListener;
    /* access modifiers changed from: private */
    public FaceBeautyInvoker mNativeInvoker;
    private VEPreviewSettings mPreviewSettings;
    private int mRecordCount;
    private VERecorderResManager mResManager;
    private SlamDetectListener mSlamDetectListener;
    private volatile int mState;
    private ExecutorService mThreadPool;
    private VERuntime mVERuntime;
    private VEVideoEncodeSettings mVideoEncodeSettings;

    class ConcatResultListenerInvoker implements Runnable {
        private OnConcatFinishedListener mListener;
        private int mRet;

        ConcatResultListenerInvoker(@NonNull OnConcatFinishedListener onConcatFinishedListener, int i) {
            this.mListener = onConcatFinishedListener;
            this.mRet = i;
        }

        public void run() {
            this.mListener.onConcatFinished(this.mRet);
        }
    }

    public interface IRenderCallback {
        Texture onCreateTexture();

        boolean onDestroy();

        void onTextureCreated(Texture texture);
    }

    class NativeHardEncoderInitListenerInvoker implements Runnable {
        private VERecorderNativeInitListener mListener;
        private boolean mRet;

        NativeHardEncoderInitListenerInvoker(@NonNull VERecorderNativeInitListener vERecorderNativeInitListener, boolean z) {
            this.mListener = vERecorderNativeInitListener;
            this.mRet = z;
        }

        public void run() {
            this.mListener.onHardEncoderInit(this.mRet);
        }
    }

    class NativeInitListenerInvoker implements Runnable {
        private VERecorderNativeInitListener mListener;
        private int mRet;

        NativeInitListenerInvoker(@NonNull VERecorderNativeInitListener vERecorderNativeInitListener, int i) {
            this.mListener = vERecorderNativeInitListener;
            this.mRet = i;
        }

        public void run() {
            this.mListener.onNativeInit(this.mRet, "Native init");
        }
    }

    public interface OnConcatFinishedListener {
        void onConcatFinished(int i);
    }

    public interface OnPictureRenderListener {
        void onImage(Bitmap bitmap);

        void onResult(int i, int i2);
    }

    public interface OnSlamDetectListener {
        void onSlam(boolean z);
    }

    class RenderPictureOnImageListenerInvoker implements Runnable {
        private Bitmap mBitmap;
        private OnPictureRenderListener mListener;

        public RenderPictureOnImageListenerInvoker(OnPictureRenderListener onPictureRenderListener, Bitmap bitmap) {
            this.mListener = onPictureRenderListener;
            this.mBitmap = bitmap;
        }

        public void run() {
            this.mListener.onImage(this.mBitmap);
        }
    }

    class RenderPictureOnResultListenerInvoker implements Runnable {
        private OnPictureRenderListener mListener;
        private int mRet;
        private int mState;

        public RenderPictureOnResultListenerInvoker(OnPictureRenderListener onPictureRenderListener, int i, int i2) {
            this.mListener = onPictureRenderListener;
            this.mState = i;
            this.mRet = i2;
        }

        public void run() {
            this.mListener.onResult(this.mState, this.mRet);
        }
    }

    class SlamDetectListenerInvoker implements Runnable {
        private List mListeners;
        private boolean mRet;

        SlamDetectListenerInvoker(@NonNull List list, boolean z) {
            this.mListeners = list;
            this.mRet = z;
        }

        public void run() {
            for (OnSlamDetectListener onSlam : this.mListeners) {
                onSlam.onSlam(this.mRet);
            }
        }
    }

    public class Texture {
        private SurfaceTexture mSurfaceTexture;
        private int mTextureID;

        public Texture(int i, SurfaceTexture surfaceTexture) {
            this.mTextureID = i;
            this.mSurfaceTexture = surfaceTexture;
        }

        public SurfaceTexture getSurfaceTexture() {
            return this.mSurfaceTexture;
        }

        public int getTextureID() {
            return this.mTextureID;
        }

        public void release() {
            SurfaceTexture surfaceTexture = this.mSurfaceTexture;
            if (surfaceTexture != null) {
                surfaceTexture.release();
                this.mSurfaceTexture = null;
            }
            int i = this.mTextureID;
            if (i != 0) {
                Common.deleteTextureID(i);
                this.mTextureID = 0;
            }
        }
    }

    public TERecorder(@NonNull String str, @NonNull Context context) {
        this(str, context, null);
    }

    public TERecorder(@NonNull String str, @NonNull Context context, @Nullable Handler handler) {
        this.mInputTexture = null;
        this.mThreadPool = Executors.newSingleThreadExecutor();
        this.mAudioEncodeSettings = null;
        this.mVideoEncodeSettings = null;
        this.mPreviewSettings = null;
        this.mCameraSettings = null;
        this.mIsCameraSettings = new AtomicBoolean(false);
        this.mLastAudioOption = -1;
        this.mExternalRenderCallback = null;
        this.mExternalNativeInitListener = null;
        this.mExternalPictureRenderListener = null;
        this.mExternalSlamDetectListeners = new ArrayList();
        this.mState = 0;
        this.mIsTextureCreatedBySelf = false;
        this.mGLCreateCallback = new IOnOpenGLCallback() {
            /* access modifiers changed from: private */
            public float[] fetchTransformMatrixFromTexture(SurfaceTexture surfaceTexture) {
                float[] fArr = new float[16];
                surfaceTexture.getTransformMatrix(fArr);
                return fArr;
            }

            private double getDrawFrameTime(long j) {
                long nanoTime = System.nanoTime();
                return ((double) (nanoTime - Math.min(Math.min(Math.abs(nanoTime - j), VERSION.SDK_INT >= 17 ? Math.abs(SystemClock.elapsedRealtimeNanos() - j) : Long.MAX_VALUE), Math.abs((SystemClock.uptimeMillis() * ExtraTextUtils.MB) - j)))) / 1000000.0d;
            }

            private boolean verifyTexture(int i) {
                if (GLES20.glIsTexture(i)) {
                    return true;
                }
                VELogUtil.e(TERecorder.TAG, "OpenGL has created but encounter getting invalid textureID from render Callback");
                return false;
            }

            public void onOpenGLCreate() {
                if (TERecorder.this.mExternalRenderCallback == null) {
                    VELogUtil.e(TERecorder.TAG, "Couldn't find render callback. Consider set render callback first");
                    return;
                }
                Texture onCreateTexture = TERecorder.this.mExternalRenderCallback.onCreateTexture();
                if (onCreateTexture == null) {
                    int genSurfaceTextureID = Common.genSurfaceTextureID();
                    Texture texture = new Texture(genSurfaceTextureID, new SurfaceTexture(genSurfaceTextureID));
                    TERecorder.this.mIsTextureCreatedBySelf = true;
                    onCreateTexture = texture;
                }
                if (!verifyTexture(onCreateTexture.getTextureID())) {
                    VELogUtil.e(TERecorder.TAG, "Didn't attach render routine to texture due to invalid texture.");
                    return;
                }
                if (TERecorder.this.mInputTexture != null) {
                    TERecorder.this.mInputTexture.getSurfaceTexture().release();
                }
                TERecorder.this.mInputTexture = onCreateTexture;
                TERecorder.this.mInputTexture.getSurfaceTexture().setOnFrameAvailableListener(new OnFrameAvailableListener() {
                    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                        if (TERecorder.this.mNativeInvoker == null) {
                            VELogUtil.d(TERecorder.TAG, "OnFrameAvailable encounter mNativeInvoker == null");
                            return;
                        }
                        TERecorder.this.mNativeInvoker.onDrawFrame(TERecorder.this.mInputTexture.getTextureID(), AnonymousClass4.this.fetchTransformMatrixFromTexture(surfaceTexture));
                    }
                });
                TERecorder.this.mExternalRenderCallback.onTextureCreated(onCreateTexture);
            }

            public void onOpenGLDestroy() {
                if (TERecorder.this.mIsTextureCreatedBySelf || (TERecorder.this.mExternalRenderCallback != null && TERecorder.this.mExternalRenderCallback.onDestroy())) {
                    TERecorder.this.mInputTexture.release();
                }
            }

            public int onOpenGLRunning() {
                String access$000;
                String str;
                if (TERecorder.this.mInputTexture == null) {
                    access$000 = TERecorder.TAG;
                    str = "Couldn't find source texture";
                } else {
                    SurfaceTexture surfaceTexture = TERecorder.this.mInputTexture.getSurfaceTexture();
                    if (surfaceTexture == null) {
                        access$000 = TERecorder.TAG;
                        str = "Couldn't find surfaceTexture from source texture";
                    } else {
                        surfaceTexture.updateTexImage();
                        TERecorder.this.mNativeInvoker.onDrawFrameTime(getDrawFrameTime(surfaceTexture.getTimestamp()));
                        if (TERecorder.this.mIsCameraSettings.get() && TERecorder.this.mCameraSettings != null) {
                            FaceBeautyInvoker access$200 = TERecorder.this.mNativeInvoker;
                            TERecorder tERecorder = TERecorder.this;
                            access$200.updateRotation(tERecorder.getRotation(tERecorder.mCameraSettings.getFacingID()), TERecorder.this.mCameraSettings.getFacingID() == CAMERA_FACING_ID.FACING_FRONT);
                            VESize size = TERecorder.this.mCameraSettings.getSize();
                            TERecorder.this.mNativeInvoker.setPreviewSizeRatio(((float) size.height) / ((float) size.width));
                            TERecorder.this.mIsCameraSettings.set(false);
                        }
                        return 0;
                    }
                }
                VELogUtil.e(access$000, str);
                return -1;
            }
        };
        this.mNativeInitListener = new NativeInitListener() {
            public void onNativeInitCallBack(int i) {
                if (TERecorder.this.mExternalListenerInvokerHandler != null) {
                    TERecorder.this.mExternalListenerInvokerHandler.post(new NativeInitListenerInvoker(TERecorder.this.mExternalNativeInitListener, i));
                } else if (TERecorder.this.mExternalNativeInitListener != null) {
                    TERecorder.this.mExternalNativeInitListener.onNativeInit(i, "");
                }
            }

            public void onNativeInitHardEncoderRetCallback(int i, int i2) {
                boolean z = true;
                if (TERecorder.this.mExternalListenerInvokerHandler != null) {
                    Handler access$100 = TERecorder.this.mExternalListenerInvokerHandler;
                    VERecorderNativeInitListener access$1000 = TERecorder.this.mExternalNativeInitListener;
                    if (i == 0) {
                        z = false;
                    }
                    access$100.post(new NativeHardEncoderInitListenerInvoker(access$1000, z));
                } else if (TERecorder.this.mExternalNativeInitListener != null) {
                    VERecorderNativeInitListener access$10002 = TERecorder.this.mExternalNativeInitListener;
                    if (i == 0) {
                        z = false;
                    }
                    access$10002.onHardEncoderInit(z);
                }
            }
        };
        this.mSlamDetectListener = new SlamDetectListener() {
            public void onSlam(boolean z) {
                TERecorder.this.mExternalListenerInvokerHandler.post(new SlamDetectListenerInvoker(TERecorder.this.mExternalSlamDetectListeners, z));
            }
        };
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("workspace can't be null or empty String");
        } else if (context != null) {
            this.mExternalListenerInvokerHandler = handler;
            this.mVERuntime = VERuntime.getInstance();
            this.mVERuntime.init(context, str);
            this.mContext = context.getApplicationContext();
            this.mResManager = new VERecorderResManager(str);
            attachNativeInvoker();
        } else {
            throw new IllegalArgumentException("context can't be null");
        }
    }

    private void attachNativeInvoker() {
        this.mNativeInvoker = new FaceBeautyInvoker();
        this.mNativeInvoker.setOnOpenGLCallback(this.mGLCreateCallback);
        this.mNativeInvoker.setNativeInitListener2(this.mNativeInitListener);
        FaceBeautyInvoker.addSlamDetectListener(this.mSlamDetectListener);
    }

    private boolean checkCameraSettingsChange(VECameraSettings vECameraSettings) {
        if (vECameraSettings == null) {
            return false;
        }
        VECameraSettings vECameraSettings2 = this.mCameraSettings;
        if (vECameraSettings2 == null || vECameraSettings2.getOrientation() != vECameraSettings.getOrientation() || this.mCameraSettings.getFacingID() != vECameraSettings.getFacingID()) {
            return true;
        }
        VESize size = this.mCameraSettings.getSize();
        VESize size2 = vECameraSettings.getSize();
        return (size2.width == size.width && size2.height == size.height) ? false : true;
    }

    private void detachNavieInvoer() {
        this.mNativeInvoker.setOnOpenGLCallback(null);
        this.mNativeInvoker.setNativeInitListener2(null);
        FaceBeautyInvoker.removeSlamDetectListener(this.mSlamDetectListener);
    }

    private int determineCurrentAudioOption() {
        if (!this.mPreviewSettings.isAudioRecordEnabled()) {
            return 0;
        }
        return TextUtils.isEmpty(this.mBGMPath) ? 1 : 5;
    }

    /* access modifiers changed from: private */
    public int getRotation(CAMERA_FACING_ID camera_facing_id) {
        int rotation = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getRotation();
        int i = 0;
        if (rotation != 0) {
            if (rotation == 1) {
                i = 90;
            } else if (rotation == 2) {
                i = 180;
            } else if (rotation == 3) {
                i = 270;
            }
        }
        CAMERA_FACING_ID camera_facing_id2 = CAMERA_FACING_ID.FACING_FRONT;
        int orientation = this.mCameraSettings.getOrientation();
        return (camera_facing_id == camera_facing_id2 ? 540 - ((orientation + i) % m.cQ) : (orientation - i) + m.cQ) % m.cQ;
    }

    private void initAudioPlayerIfNeed(int i) {
        int i2;
        FaceBeautyInvoker faceBeautyInvoker;
        if (i != this.mLastAudioOption) {
            if ((i & 4) == 0) {
                this.mNativeInvoker.uninitAudioPlayer();
                faceBeautyInvoker = this.mNativeInvoker;
                i2 = 0;
            } else if (!TextUtils.isEmpty(this.mBGMPath)) {
                this.mNativeInvoker.setBGMVolume(this.mBGMVolume);
                this.mNativeInvoker.initAudioPlayer(this.mContext, this.mBGMPath, (long) this.mBGMTrimIn, this.mBGMIsLoop, false);
                faceBeautyInvoker = this.mNativeInvoker;
                i2 = 1;
            } else {
                VELogUtil.e(TAG, "Illegal state. BGM path is empty. when current audio option is PLAY_OPENSL");
                throw new IllegalStateException("Shouldn't reach here");
            }
            faceBeautyInvoker.setUseMusic(i2);
        }
    }

    private void initAudioRecorderIfNeed(int i) {
        if ((i & 1) != (this.mLastAudioOption & 1) || this.mAudioRecorder == null) {
            this.mAudioRecorder = new O00000o0(this);
            this.mAudioRecorder.init(1);
        }
    }

    private int initNativeInvokerWithSettings(VEVideoEncodeSettings vEVideoEncodeSettings, VEPreviewSettings vEPreviewSettings) {
        String effectModelResourceDirPath = this.mVERuntime.getEnv().getEffectModelResourceDirPath();
        String tempSegmentDirPath = this.mResManager.getTempSegmentDirPath();
        VESize videoRes = vEVideoEncodeSettings.getVideoRes();
        VESize renderSize = vEPreviewSettings.getRenderSize();
        int initFaceBeautyPlay = this.mNativeInvoker.initFaceBeautyPlay(renderSize.height, renderSize.width, tempSegmentDirPath, videoRes.width, videoRes.height, effectModelResourceDirPath, 0);
        if (initFaceBeautyPlay == 0) {
            return 0;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("NativeInvoker init Failed. ret = ");
        sb.append(initFaceBeautyPlay);
        VELogUtil.e(str, sb.toString());
        return initFaceBeautyPlay;
    }

    private void releaseAudioComponentIfNeed() {
        O00000o0 o00000o0 = this.mAudioRecorder;
        if (o00000o0 != null) {
            o00000o0.unInit();
            this.mAudioRecorder = null;
        }
    }

    private void startAudioRecorderIfNeed(float f, int i) {
        if ((i & 1) != 0) {
            this.mAudioRecorder.O000000o((double) f, true);
        }
    }

    private int updateAudioComponentIfNeedWithStartRecord(long j, float f) {
        int determineCurrentAudioOption = determineCurrentAudioOption();
        initAudioRecorderIfNeed(determineCurrentAudioOption);
        initAudioPlayerIfNeed(determineCurrentAudioOption);
        startAudioRecorderIfNeed(f, determineCurrentAudioOption);
        this.mNativeInvoker.setMusicTime((long) this.mBGMTrimIn, j);
        this.mLastAudioOption = determineCurrentAudioOption;
        return 0;
    }

    public int addPCMData(byte[] bArr, int i) {
        this.mNativeInvoker.onAudioCallback(bArr, i);
        return 0;
    }

    public synchronized void addSlamDetectListener(@NonNull OnSlamDetectListener onSlamDetectListener) {
        this.mExternalSlamDetectListeners.add(onSlamDetectListener);
    }

    public synchronized void chooseSlamFace(int i) {
        this.mNativeInvoker.chooseSlamFace(i);
    }

    public synchronized int clearEnv() {
        int clearFragFile;
        if (!(this.mState == 1 && this.mState == 2)) {
            VELogUtil.e(TAG, "clearEnv() can only be invoked when state : 1,or2");
        }
        clearFragFile = this.mNativeInvoker.clearFragFile();
        if (clearFragFile != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker clearFragFile() failed. ret = ");
            sb.append(clearFragFile);
            VELogUtil.e(str, sb.toString());
        } else {
            this.mRecordCount = 0;
        }
        return clearFragFile;
    }

    public synchronized void clearSlamDetectListener() {
        this.mExternalSlamDetectListeners.clear();
    }

    public int closeWavFile(boolean z) {
        return this.mNativeInvoker.closeWavFile(z);
    }

    public synchronized void concatAsync(@NonNull final String str, @NonNull final String str2, @NonNull final OnConcatFinishedListener onConcatFinishedListener) {
        if (!(this.mState == 1 || this.mState == 2)) {
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("concat() can only be called in error state when current state code is ");
            sb.append(this.mState);
            VELogUtil.e(str3, sb.toString());
        }
        if (this.mRecordCount <= 0) {
            String str4 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Can't invoke concat() when current recorded segment count = ");
            sb2.append(this.mRecordCount);
            VELogUtil.e(str4, sb2.toString());
        }
        this.mThreadPool.execute(new Runnable() {
            public void run() {
                String str = "";
                int concat = TERecorder.this.mNativeInvoker.concat(str, str2, str, str);
                if (concat < 0) {
                    String access$000 = TERecorder.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("NativeInvoker concat failed. ret = ");
                    sb.append(concat);
                    VELogUtil.e(access$000, sb.toString());
                }
                if (TERecorder.this.mExternalListenerInvokerHandler != null) {
                    TERecorder.this.mExternalListenerInvokerHandler.post(new ConcatResultListenerInvoker(onConcatFinishedListener, concat));
                    return;
                }
                OnConcatFinishedListener onConcatFinishedListener = onConcatFinishedListener;
                if (onConcatFinishedListener != null) {
                    onConcatFinishedListener.onConcatFinished(concat);
                }
            }
        });
    }

    public synchronized int deleteLastFrag() {
        int deleteLastFrag;
        if (!(this.mState == 1 && this.mState == 2)) {
            VELogUtil.e(TAG, "deleteLastFrag() can only be invoked when state : 1,or2");
        }
        if (this.mRecordCount <= 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("About to invoke deleteLastFrag when recordCount = ");
            sb.append(this.mRecordCount);
            VELogUtil.e(str, sb.toString());
        }
        deleteLastFrag = this.mNativeInvoker.deleteLastFrag();
        if (deleteLastFrag != 0) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("NativeInvoker deleteLastFrag() failed. ret = ");
            sb2.append(deleteLastFrag);
            VELogUtil.e(str2, sb2.toString());
        } else {
            this.mRecordCount--;
        }
        return deleteLastFrag;
    }

    public synchronized void destroy() {
        releaseAudioComponentIfNeed();
        int uninitFaceBeautyPlay = this.mNativeInvoker.uninitFaceBeautyPlay();
        if (uninitFaceBeautyPlay != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker uninitFaceBeautyPlay() failed. ret = ");
            sb.append(uninitFaceBeautyPlay);
            VELogUtil.e(str, sb.toString());
        }
        detachNavieInvoer();
        this.mExternalNativeInitListener = null;
        this.mExternalSlamDetectListeners.clear();
        this.mExternalRenderCallback = null;
        this.mExternalPictureRenderListener = null;
        this.mExternalListenerInvokerHandler = null;
        this.mNativeInitListener = null;
        this.mState = 0;
    }

    public synchronized long getEndFrameTime() {
        if (this.mState != 3) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getEndFrameTime() when current state is ");
            sb.append(this.mState);
            VELogUtil.e(str, sb.toString());
        }
        return this.mNativeInvoker.getEndFrameTime();
    }

    public synchronized int getSlamFaceCount() {
        int slamFaceCount;
        slamFaceCount = this.mNativeInvoker.getSlamFaceCount();
        if (slamFaceCount != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker getSlamFaceCount failed. ret = ");
            sb.append(slamFaceCount);
            VELogUtil.e(str, sb.toString());
        }
        return slamFaceCount;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0033, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int init(@NonNull VEVideoEncodeSettings vEVideoEncodeSettings, @NonNull VEAudioEncodeSettings vEAudioEncodeSettings, @NonNull VEPreviewSettings vEPreviewSettings, @Nullable VECameraSettings vECameraSettings) {
        if (!VERuntime.isValidAuthorization()) {
            return -1;
        }
        this.mVideoEncodeSettings = vEVideoEncodeSettings;
        this.mAudioEncodeSettings = vEAudioEncodeSettings;
        this.mPreviewSettings = vEPreviewSettings;
        this.mCameraSettings = vECameraSettings;
        int initNativeInvokerWithSettings = initNativeInvokerWithSettings(vEVideoEncodeSettings, vEPreviewSettings);
        if (initNativeInvokerWithSettings != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("init failed ret: ");
            sb.append(initNativeInvokerWithSettings);
            VELogUtil.e(str, sb.toString());
        } else {
            this.mState = 1;
        }
    }

    public int initAudioConfig(int i, int i2) {
        return this.mNativeInvoker.initAudioConfig(i, i2);
    }

    public int initWavFile(int i, int i2, double d) {
        return this.mNativeInvoker.initWavFile(i, i2, d);
    }

    public void lackPermission() {
        VELogUtil.e(TAG, "Failed. AudioRecorder open failed. Lack permission.");
    }

    public int onProcessData(byte[] bArr, int i) {
        return this.mNativeInvoker.addPCMData(bArr, i);
    }

    public synchronized void pauseEffectAudio(boolean z) {
        this.mNativeInvoker.pauseEffectAudio(z);
    }

    public synchronized int processTouchEvent(float f, float f2) {
        int processTouchEvent;
        processTouchEvent = this.mNativeInvoker.processTouchEvent(f, f2);
        if (processTouchEvent != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker processTouchEvent failed. ret = ");
            sb.append(processTouchEvent);
            VELogUtil.e(str, sb.toString());
        }
        return processTouchEvent;
    }

    public void recordStatus(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Failed. AudioRecorder record error. isRecording = ");
        sb.append(z);
        VELogUtil.e(str, sb.toString());
    }

    public synchronized void removeSlamDetectListener(@NonNull OnSlamDetectListener onSlamDetectListener) {
        this.mExternalSlamDetectListeners.remove(onSlamDetectListener);
    }

    public synchronized int renderPicture(@NonNull byte[] bArr, int i, int i2, int i3, @NonNull final OnPictureRenderListener onPictureRenderListener) {
        if (this.mState != 2) {
            VELogUtil.e(TAG, "renderPicture() can only be invoked after in state:2");
            return VEResult.TER_INVALID_STAT;
        }
        ImageFrame imageFrame = new ImageFrame(bArr, i, i2, i3);
        return this.mNativeInvoker.renderPicture(imageFrame, imageFrame.getWidth(), imageFrame.getHeight(), new OnPictureCallbackV2() {
            public void onImage(int[] iArr, int i, int i2) {
                Bitmap bitmap;
                if (iArr == null || iArr.length <= 0 || i <= 0 || i2 <= 0) {
                    String access$000 = TERecorder.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed. NativeInvoker renderPicture() return invalid data. data == null ? ");
                    sb.append(iArr == null);
                    sb.append(", data.length = ");
                    sb.append(iArr.length);
                    sb.append(", width = ");
                    sb.append(i);
                    sb.append(", height = ");
                    sb.append(i2);
                    VELogUtil.e(access$000, sb.toString());
                    bitmap = null;
                } else {
                    bitmap = Bitmap.createBitmap(iArr, i, i2, Config.ARGB_8888);
                }
                if (TERecorder.this.mExternalListenerInvokerHandler != null) {
                    TERecorder.this.mExternalListenerInvokerHandler.post(new RenderPictureOnImageListenerInvoker(onPictureRenderListener, null));
                    return;
                }
                OnPictureRenderListener onPictureRenderListener = onPictureRenderListener;
                if (onPictureRenderListener != null) {
                    onPictureRenderListener.onImage(bitmap);
                }
            }

            public void onResult(int i, int i2) {
                if (TERecorder.this.mExternalListenerInvokerHandler != null) {
                    TERecorder.this.mExternalListenerInvokerHandler.post(new RenderPictureOnResultListenerInvoker(onPictureRenderListener, i, i2));
                    return;
                }
                OnPictureRenderListener onPictureRenderListener = onPictureRenderListener;
                if (onPictureRenderListener != null) {
                    onPictureRenderListener.onResult(i, i2);
                }
            }
        });
    }

    public synchronized int setBeautyFace(int i, @Nullable String str) {
        int beautyFace;
        VEKeyValue vEKeyValue = new VEKeyValue();
        vEKeyValue.add("iesve_verecorder_set_beauty_algorithm", i).add("old", 0);
        MonitorUtils.monitorStatistics("iesve_verecorder_set_beauty_algorithm", 1, vEKeyValue);
        beautyFace = this.mNativeInvoker.setBeautyFace(i, str);
        if (beautyFace != 0) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker setBeautyFace failed. ret = ");
            sb.append(beautyFace);
            VELogUtil.e(str2, sb.toString());
        }
        return beautyFace;
    }

    public synchronized int setBeautyFaceIntensity(float f, float f2) {
        int beautyFaceIntensity;
        VEKeyValue vEKeyValue = new VEKeyValue();
        vEKeyValue.add("iesve_verecorder_set_beauty_level", f).add("old", 0);
        MonitorUtils.monitorStatistics("iesve_verecorder_set_beauty_level", 1, vEKeyValue);
        beautyFaceIntensity = this.mNativeInvoker.setBeautyFaceIntensity(f, f2);
        if (beautyFaceIntensity != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker setBeautyFaceIntensity failed. ret = ");
            sb.append(beautyFaceIntensity);
            VELogUtil.e(str, sb.toString());
        }
        return beautyFaceIntensity;
    }

    public synchronized void setCameraSettings(@NonNull VECameraSettings vECameraSettings) {
        if (checkCameraSettingsChange(vECameraSettings)) {
            this.mCameraSettings = vECameraSettings;
            this.mIsCameraSettings.set(true);
        }
    }

    public synchronized void setDeviceRotation(float[] fArr) {
        this.mNativeInvoker.setDeviceRotation(fArr);
    }

    public synchronized int setFaceReshape(@NonNull String str, float f, float f2) {
        int reshape;
        VEKeyValue vEKeyValue = new VEKeyValue();
        vEKeyValue.add("iesve_verecorder_set_bigeyes_smallface_level", f).add("old", 0);
        MonitorUtils.monitorStatistics("iesve_verecorder_set_bigeyes_smallface", 1, vEKeyValue);
        reshape = this.mNativeInvoker.setReshape(str, f, f2);
        if (reshape != 0) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker setFaceReshape failed. ret = ");
            sb.append(reshape);
            VELogUtil.e(str2, sb.toString());
        }
        return reshape;
    }

    public synchronized int setFilter(@NonNull String str) {
        int filter;
        VEKeyValue vEKeyValue = new VEKeyValue();
        vEKeyValue.add("iesve_verecorder_set_filter_click_idfilter_id", VETextUtils.parsePathSimpleName(str)).add("old", 0);
        MonitorUtils.monitorStatistics("iesve_verecorder_set_filter_click", 1, vEKeyValue);
        filter = this.mNativeInvoker.setFilter(str);
        if (filter != 0) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker setFilter failed ret = ");
            sb.append(filter);
            VELogUtil.e(str2, sb.toString());
        }
        return filter;
    }

    public synchronized int setFilter(@NonNull String str, float f) {
        int filter = setFilter(str);
        if (filter != 0) {
            return filter;
        }
        return setFilterIntensity(f);
    }

    public synchronized int setFilter(@NonNull String str, @NonNull String str2, float f) {
        int filter;
        VEKeyValue vEKeyValue = new VEKeyValue();
        vEKeyValue.add("iesve_verecorder_set_filter_slide_left_id", VETextUtils.parsePathSimpleName(str)).add("iesve_verecorder_set_filter_slide_right_id", VETextUtils.parsePathSimpleName(str2)).add("old", 0);
        MonitorUtils.monitorStatistics("iesve_verecorder_set_filter_slide", 1, vEKeyValue);
        filter = this.mNativeInvoker.setFilter(str, str2, f);
        if (filter != 0) {
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker setFilter with position failed. ret = ");
            sb.append(filter);
            VELogUtil.e(str3, sb.toString());
        }
        return filter;
    }

    public synchronized int setFilterIntensity(float f) {
        int filterIntensity;
        filterIntensity = this.mNativeInvoker.setFilterIntensity(f);
        if (filterIntensity != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker setFilterIntensity failed. ret = ");
            sb.append(filterIntensity);
            VELogUtil.e(str, sb.toString());
        }
        return filterIntensity;
    }

    public synchronized void setNativeInitListener(VERecorderNativeInitListener vERecorderNativeInitListener) {
        this.mExternalNativeInitListener = vERecorderNativeInitListener;
    }

    public synchronized int setRecordBGM(@NonNull String str, int i, int i2) {
        return setRecordBGM(str, i, i2, false);
    }

    public synchronized int setRecordBGM(@NonNull String str, int i, int i2, boolean z) {
        if (this.mState != 1) {
            VELogUtil.e(TAG, "stopPreview() can only be invoked after init()");
        }
        this.mBGMPath = str;
        int changeMusicPath = this.mNativeInvoker.changeMusicPath(str);
        if (changeMusicPath != 0) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker changeMusicPath() failed. ret = ");
            sb.append(changeMusicPath);
            VELogUtil.e(str2, sb.toString());
        }
        int musicTime = this.mNativeInvoker.setMusicTime((long) i, (long) i2);
        if (musicTime != 0) {
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("NativeInvoker setMusicTime() failed. ret = ");
            sb2.append(musicTime);
            VELogUtil.e(str3, sb2.toString());
        }
        this.mBGMTrimIn = i;
        this.mBGMIsLoop = z;
        return 0;
    }

    public synchronized void setRenderCallback(IRenderCallback iRenderCallback) {
        this.mExternalRenderCallback = iRenderCallback;
    }

    public synchronized int setSlamFace(Bitmap bitmap) {
        int slamFace;
        slamFace = this.mNativeInvoker.setSlamFace(bitmap);
        if (slamFace != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker setSlamFace failed. ret = ");
            sb.append(slamFace);
            VELogUtil.e(str, sb.toString());
        }
        return slamFace;
    }

    public synchronized void setUseLargeMattingModel(boolean z) {
        this.mNativeInvoker.useLargeMattingModel(z);
    }

    public synchronized int shotScreen(@NonNull final String str, @NonNull int[] iArr, boolean z, final CompressFormat compressFormat, IShotScreenCallback iShotScreenCallback) {
        int i = -1;
        if (compressFormat == CompressFormat.JPEG) {
            i = 1;
        } else if (compressFormat == CompressFormat.PNG) {
            i = 0;
        }
        int i2 = i;
        AnonymousClass1 r6 = new OnPictureCallback() {
            public void onResult(int[] iArr, int i, int i2) {
                Bitmap createBitmap = Bitmap.createBitmap(iArr, i, i2, Config.ARGB_8888);
                ImageUtils.saveBitmapWithPath(createBitmap, str, compressFormat);
                if (createBitmap != null) {
                    createBitmap.recycle();
                }
            }
        };
        if (this.mState == 2) {
            if (this.mState == 3) {
                return this.mNativeInvoker.shotScreen(str, iArr, z, i2, r6, iShotScreenCallback);
            }
        }
        VELogUtil.e(TAG, "shotScreen() can only be invoked after startRecord() invoked successfully");
        return VEResult.TER_INVALID_STAT;
    }

    public synchronized int slamDeviceConfig(boolean z, boolean z2, boolean z3, boolean z4) {
        int slamDeviceConfig;
        slamDeviceConfig = this.mNativeInvoker.slamDeviceConfig(z, z2, z3, z4);
        if (slamDeviceConfig != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker slamDeviceConfig failed. ret = ");
            sb.append(slamDeviceConfig);
            VELogUtil.e(str, sb.toString());
        }
        return slamDeviceConfig;
    }

    public synchronized int slamProcessIngestAcc(double d, double d2, double d3, double d4) {
        int slamProcessIngestAcc;
        synchronized (this) {
            slamProcessIngestAcc = this.mNativeInvoker.slamProcessIngestAcc(d, d2, d3, d4);
            if (slamProcessIngestAcc != 0) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("NativeInvoker slamProcessIngestAcc failed. ret = ");
                sb.append(slamProcessIngestAcc);
                VELogUtil.e(str, sb.toString());
            }
        }
        return slamProcessIngestAcc;
    }

    public synchronized int slamProcessIngestGra(double d, double d2, double d3, double d4) {
        int slamProcessIngestGra;
        synchronized (this) {
            slamProcessIngestGra = this.mNativeInvoker.slamProcessIngestGra(d, d2, d3, d4);
            if (slamProcessIngestGra != 0) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("NativeInvoker slamProcessIngestGra failed. ret = ");
                sb.append(slamProcessIngestGra);
                VELogUtil.e(str, sb.toString());
            }
        }
        return slamProcessIngestGra;
    }

    public synchronized int slamProcessIngestGyr(double d, double d2, double d3, double d4) {
        int slamProcessIngestGyr;
        synchronized (this) {
            slamProcessIngestGyr = this.mNativeInvoker.slamProcessIngestGyr(d, d2, d3, d4);
            if (slamProcessIngestGyr != 0) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("NativeInvoker slamProcessIngestGyr failed. ret = ");
                sb.append(slamProcessIngestGyr);
                VELogUtil.e(str, sb.toString());
            }
        }
        return slamProcessIngestGyr;
    }

    public synchronized int slamProcessIngestOri(double[] dArr, double d) {
        int slamProcessIngestOri;
        slamProcessIngestOri = this.mNativeInvoker.slamProcessIngestOri(dArr, d);
        if (slamProcessIngestOri != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker slamProcessIngestOri failed. ret = ");
            sb.append(slamProcessIngestOri);
            VELogUtil.e(str, sb.toString());
        }
        return slamProcessIngestOri;
    }

    public synchronized int slamProcessPanEvent(float f, float f2, float f3, float f4, float f5) {
        int slamProcessPanEvent;
        slamProcessPanEvent = this.mNativeInvoker.slamProcessPanEvent(f, f2, f3, f4, f5);
        if (slamProcessPanEvent != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker slamProcessPanEvent failed. ret = ");
            sb.append(slamProcessPanEvent);
            VELogUtil.e(str, sb.toString());
        }
        return slamProcessPanEvent;
    }

    public synchronized int slamProcessRotationEvent(float f, float f2) {
        int slamProcessRotationEvent;
        slamProcessRotationEvent = this.mNativeInvoker.slamProcessRotationEvent(f, f2);
        if (slamProcessRotationEvent != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker slamProcessRotationEvent failed. ret = ");
            sb.append(slamProcessRotationEvent);
            VELogUtil.e(str, sb.toString());
        }
        return slamProcessRotationEvent;
    }

    public synchronized int slamProcessScaleEvent(float f, float f2) {
        int slamProcessScaleEvent;
        slamProcessScaleEvent = this.mNativeInvoker.slamProcessScaleEvent(f, f2);
        if (slamProcessScaleEvent != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker slamProcessScaleEvent failed. ret = ");
            sb.append(slamProcessScaleEvent);
            VELogUtil.e(str, sb.toString());
        }
        return slamProcessScaleEvent;
    }

    public synchronized int slamProcessTouchEvent(float f, float f2) {
        int slamProcessTouchEvent;
        slamProcessTouchEvent = this.mNativeInvoker.slamProcessTouchEvent(f, f2);
        if (slamProcessTouchEvent != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker slamProcessTouchEvent failed. ret = ");
            sb.append(slamProcessTouchEvent);
            VELogUtil.e(str, sb.toString());
        }
        return slamProcessTouchEvent;
    }

    public synchronized int slamProcessTouchEventByType(int i, float f, float f2, int i2) {
        int slamProcessTouchEventByType;
        slamProcessTouchEventByType = this.mNativeInvoker.slamProcessTouchEventByType(i, f, f2, i2);
        if (slamProcessTouchEventByType != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker slamProcessTouchEventByType failed. ret = ");
            sb.append(slamProcessTouchEventByType);
            VELogUtil.e(str, sb.toString());
        }
        return slamProcessTouchEventByType;
    }

    public synchronized int startPreview(@NonNull Surface surface) {
        int startPlay;
        if (this.mState != 1) {
            VELogUtil.e(TAG, "startPreview() can only be invoked after init()");
        }
        int i = 0;
        int rotation = this.mCameraSettings != null ? getRotation(this.mCameraSettings.getFacingID()) : 0;
        if (this.mCameraSettings != null && this.mCameraSettings.getFacingID() == CAMERA_FACING_ID.FACING_FRONT) {
            i = 1;
        }
        startPlay = this.mNativeInvoker.startPlay(surface, Build.DEVICE, rotation, i);
        if (startPlay != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker startPlay() failed. ret = ");
            sb.append(startPlay);
            VELogUtil.e(str, sb.toString());
        } else {
            this.mState = 2;
        }
        initAudioRecorderIfNeed(determineCurrentAudioOption());
        return startPlay;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0089  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int startRecord(float f, long j) {
        int i;
        int startRecord;
        int i2 = 2;
        if (this.mState != 2) {
            VELogUtil.e(TAG, "startRecord() can only be invoked after startPreview()");
        }
        String genSegmentVideoPath = this.mResManager.genSegmentVideoPath(this.mRecordCount);
        this.mResManager.addSegmentAudioPath(this.mResManager.genSegmentAudioPath(this.mRecordCount));
        this.mResManager.addSegmentVideoPath(genSegmentVideoPath);
        float bps = (((float) this.mVideoEncodeSettings.getBps()) * 1.0f) / 4194304.0f;
        if (this.mVideoEncodeSettings.getEncodeProfile() != ENCODE_PROFILE.ENCODE_PROFILE_MAIN.ordinal()) {
            if (this.mVideoEncodeSettings.getEncodeProfile() == ENCODE_PROFILE.ENCODE_PROFILE_HIGH.ordinal()) {
                i2 = 8;
            } else {
                i = 1;
                updateAudioComponentIfNeedWithStartRecord(j, f);
                startRecord = this.mNativeInvoker.startRecord((double) f, this.mVideoEncodeSettings.isHwEnc(), bps, 1, i);
                if (startRecord == 0) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("NativeInvoker startRecord() failed. ret = ");
                    sb.append(startRecord);
                    VELogUtil.e(str, sb.toString());
                } else {
                    this.mState = 3;
                }
            }
        }
        i = i2;
        updateAudioComponentIfNeedWithStartRecord(j, f);
        startRecord = this.mNativeInvoker.startRecord((double) f, this.mVideoEncodeSettings.isHwEnc(), bps, 1, i);
        if (startRecord == 0) {
        }
        return startRecord;
    }

    public synchronized int stopPreview() {
        int stopPlay;
        if (this.mState == 3) {
            stopRecord();
        }
        if (this.mState != 2) {
            VELogUtil.e(TAG, "stopPreview() can only be invoked after init()");
        }
        stopPlay = this.mNativeInvoker.stopPlay();
        if (stopPlay != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker stopPlay() failed. ret = ");
            sb.append(stopPlay);
            VELogUtil.e(str, sb.toString());
        }
        return stopPlay;
    }

    public synchronized int stopRecord() {
        int stopRecord;
        if (this.mState != 3) {
            VELogUtil.e(TAG, "stopRecord() can only be invoked after startRecord() invoked successfully");
        }
        stopRecord = this.mNativeInvoker.stopRecord(false);
        if (this.mAudioRecorder != null) {
            this.mAudioRecorder.stopFeeding();
        }
        if (stopRecord < 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("NativeInvoker stopRecord failed. ret = ");
            sb.append(stopRecord);
            VELogUtil.e(str, sb.toString());
        } else {
            this.mRecordCount++;
            this.mState = 2;
        }
        return stopRecord;
    }

    public synchronized int switchEffect(String str) {
        int stickerPath;
        VEKeyValue vEKeyValue = new VEKeyValue();
        String parsePathSimpleName = VETextUtils.parsePathSimpleName(str);
        String str2 = "iesve_verecorder_set_sticker_id";
        if (TextUtils.isEmpty(parsePathSimpleName)) {
            parsePathSimpleName = "0";
        }
        vEKeyValue.add(str2, parsePathSimpleName).add("old", 0);
        MonitorUtils.monitorStatistics("iesve_verecorder_set_sticker", 1, vEKeyValue);
        stickerPath = this.mNativeInvoker.setStickerPath(str, 0, 0, false);
        if (stickerPath != 0) {
            VELogUtil.e(TAG, "NativeInvoker setStickerPath failed");
        }
        return stickerPath;
    }

    public synchronized int tryRestore(int i) {
        int tryRestore;
        if (this.mState != 1) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Can only invoke tryRestore() in idle state but current state code is ");
            sb.append(this.mState);
            VELogUtil.e(str, sb.toString());
        }
        this.mRecordCount = i;
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("About to invoke tryRestore(), record count has been overwrite with ");
        sb2.append(i);
        VELogUtil.w(str2, sb2.toString());
        tryRestore = this.mNativeInvoker.tryRestore(this.mRecordCount, this.mResManager.getTempSegmentDirPath());
        if (tryRestore != 0) {
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("NativeInvoker tryStore() failed. ret = ");
            sb3.append(tryRestore);
            VELogUtil.e(str3, sb3.toString());
        }
        return tryRestore;
    }

    public synchronized void updateRotation(float f, float f2, float f3) {
        this.mNativeInvoker.updateRotation(f, f2, f3);
    }
}
