package com.android.camera.module.impl.component;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.Image;
import android.opengl.GLES20;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Surface;
import com.android.camera.ActivityBase;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.SurfaceTextureScreenNail.ExternalFrameProcessor;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.FilmDreamProcessing;
import com.android.camera.fragment.film.FilmItem;
import com.android.camera.fragment.vv.VVItem;
import com.android.camera.log.Log;
import com.android.camera.module.AudioController;
import com.android.camera.module.BaseModule;
import com.android.camera.module.FilmDreamModule;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.FilmDreamConfig;
import com.android.camera.protocol.ModeProtocol.FilmDreamProcess;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.ui.V6CameraGLSurfaceView;
import com.android.camera2.Camera2Proxy;
import com.xiaomi.inceptionmediaprocess.EffectCameraNotifier;
import com.xiaomi.inceptionmediaprocess.EffectMediaPlayer;
import com.xiaomi.inceptionmediaprocess.EffectMediaPlayer.SurfaceGravity;
import com.xiaomi.inceptionmediaprocess.EffectNotifier;
import com.xiaomi.inceptionmediaprocess.MediaComposeFile;
import com.xiaomi.inceptionmediaprocess.MediaEffectCamera;
import com.xiaomi.inceptionmediaprocess.MediaEffectGraph;
import com.xiaomi.inceptionmediaprocess.OpenGlRender;
import com.xiaomi.inceptionmediaprocess.SystemUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import miui.text.ExtraTextUtils;
import tv.danmaku.ijk.media.player.IjkMediaMeta;
import tv.danmaku.ijk.media.player.misc.IjkMediaFormat;

public class FilmDreamImpl implements FilmDreamConfig, ExternalFrameProcessor, EffectCameraNotifier {
    public static final String FILM_DIR;
    public static final String SEGMENTS_PATH;
    /* access modifiers changed from: private */
    public static final String TAG = "FilmDreamImpl";
    public static final String TEMPLATE_PATH;
    public static final String WORKSPACE_PATH;
    /* access modifiers changed from: private */
    public ActivityBase mActivity;
    private AudioController mAudioController;
    /* access modifiers changed from: private */
    public V6CameraGLSurfaceView mCameraView;
    private MediaComposeFile mComposeFile;
    private Context mContext;
    private CountDownTimer mCountDownTimer;
    private FilmItem mCurrentFilmItem;
    private int mCurrentOrientation = 0;
    private EffectMediaPlayer mEffectMediaPlayer;
    /* access modifiers changed from: private */
    public FilmDreamProcess mFilmDreamProcess;
    private FilmDreamProcessing mFilmDreamProcessing;
    private Handler mHandler;
    private volatile boolean mInited;
    /* access modifiers changed from: private */
    public SurfaceTexture mInputSurfaceTexture;
    private MediaEffectCamera mMediaCamera;
    private MediaEffectGraph mMediaEffectGraph;
    private boolean mMediaRecorderRecording;
    /* access modifiers changed from: private */
    public MiFilmDreamGLSurfaceViewRender mMiGLSurfaceViewRender;
    private boolean mNeedRequestRender;
    /* access modifiers changed from: private */
    public boolean mNeedStop;
    private OpenGlRender mOpenGlRender;
    /* access modifiers changed from: private */
    public boolean mPlayFinished;
    private long mStartTime;
    private List mTempVideoList;
    private long mTotalRecordingTime = 10500;
    private long mTotalTime;
    private long mValidTime;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtils.ROOT_DIR);
        sb.append("/film/");
        FILM_DIR = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(FILM_DIR);
        sb2.append("template/");
        TEMPLATE_PATH = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(FILM_DIR);
        sb3.append("workspace/");
        WORKSPACE_PATH = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(WORKSPACE_PATH);
        sb4.append("segments");
        SEGMENTS_PATH = sb4.toString();
    }

    private FilmDreamImpl(ActivityBase activityBase) {
        this.mActivity = activityBase;
        this.mContext = activityBase.getCameraAppImpl().getApplicationContext();
        this.mCameraView = activityBase.getGLView();
        this.mHandler = new Handler(this.mActivity.getMainLooper());
        this.mAudioController = new AudioController(this.mActivity.getApplicationContext());
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:15|16|17|18|21|22) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002a, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002b, code lost:
        com.android.camera.log.Log.w(TAG, r4, (java.lang.Throwable) r5);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0026 */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x004a A[SYNTHETIC, Splitter:B:37:0x004a] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x004f A[SYNTHETIC, Splitter:B:41:0x004f] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x005e A[SYNTHETIC, Splitter:B:49:0x005e] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0063 A[SYNTHETIC, Splitter:B:53:0x0063] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean copyFile(String str, String str2) {
        FileOutputStream fileOutputStream;
        FileInputStream fileInputStream;
        String str3 = "copyFile error";
        if (str == null || str2 == null) {
            return false;
        }
        FileInputStream fileInputStream2 = null;
        try {
            fileInputStream = new FileInputStream(str);
            try {
                fileOutputStream = new FileOutputStream(str2);
            } catch (Exception e) {
                e = e;
                fileOutputStream = null;
                fileInputStream2 = fileInputStream;
                try {
                    Log.w(TAG, str3, (Throwable) e);
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (Exception unused) {
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e2) {
                            Log.w(TAG, str3, (Throwable) e2);
                        }
                    }
                    return false;
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                    }
                    if (fileOutputStream != null) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = null;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception unused2) {
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e3) {
                        Log.w(TAG, str3, (Throwable) e3);
                    }
                }
                throw th;
            }
            try {
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read != -1) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        fileInputStream.close();
                        fileOutputStream.close();
                        return true;
                    }
                }
            } catch (Exception e4) {
                e = e4;
                fileInputStream2 = fileInputStream;
                Log.w(TAG, str3, (Throwable) e);
                if (fileInputStream2 != null) {
                }
                if (fileOutputStream != null) {
                }
                return false;
            } catch (Throwable th3) {
                th = th3;
                if (fileInputStream != null) {
                }
                if (fileOutputStream != null) {
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            fileOutputStream = null;
            Log.w(TAG, str3, (Throwable) e);
            if (fileInputStream2 != null) {
            }
            if (fileOutputStream != null) {
            }
            return false;
        } catch (Throwable th4) {
            th = th4;
            fileOutputStream = null;
            fileInputStream = null;
            if (fileInputStream != null) {
            }
            if (fileOutputStream != null) {
            }
            throw th;
        }
    }

    public static FilmDreamImpl create(ActivityBase activityBase) {
        return new FilmDreamImpl(activityBase);
    }

    private void makeSureFilmDreamProcess() {
        if (this.mFilmDreamProcess == null) {
            this.mFilmDreamProcess = (FilmDreamProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(931);
        }
    }

    /* access modifiers changed from: private */
    public void notifyModuleRecordingFinish() {
        resetFlag();
        BaseModule baseModule = (BaseModule) this.mActivity.getCurrentModule();
        if (baseModule.getModuleIndex() == 212) {
            ((FilmDreamModule) baseModule).stopVideoRecording(false, false);
        }
    }

    private void prepareEffectGraph() {
        String[] strArr = (String[]) new ArrayList(this.mTempVideoList).toArray(new String[0]);
        this.mMediaEffectGraph = new MediaEffectGraph();
        this.mMediaEffectGraph.ConstructMediaEffectGraph();
        this.mMediaEffectGraph.AddSourcesAndEffectBySourcesPath(strArr, new float[]{1.0f});
    }

    private void resetFlag() {
        this.mMediaRecorderRecording = false;
        this.mNeedRequestRender = false;
        this.mNeedStop = false;
    }

    private void setTotalTime() {
        if (this.mTotalTime == 0) {
            this.mTotalTime = System.currentTimeMillis() - this.mStartTime;
        }
        this.mFilmDreamProcessing.setTotalTime(this.mTotalTime);
    }

    private void startRecordingTime() {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        final TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            AnonymousClass3 r1 = new CountDownTimer(this.mTotalRecordingTime, 1000) {
                public void onFinish() {
                    Log.d(FilmDreamImpl.TAG, "count down onFinish~");
                }

                public void onTick(long j) {
                    topAlert.updateRecordingTime(Util.millisecondToTimeString(j, false));
                }
            };
            this.mCountDownTimer = r1;
            this.mStartTime = System.currentTimeMillis();
            this.mCountDownTimer.start();
        }
    }

    public void OnNeedStopRecording() {
        Log.d(TAG, "OnNeedStopRecording");
        this.mHandler.post(new Runnable() {
            public void run() {
                FilmDreamImpl.this.mNeedStop = true;
                FilmDreamImpl.this.stopRecording();
            }
        });
    }

    public void OnNotifyRender() {
        Log.d(TAG, "OnNotifyRender");
        this.mNeedRequestRender = true;
    }

    public void OnRecordFailed() {
        Log.d(TAG, "OnRecordFailed");
    }

    public void OnRecordFinish(String str) {
        this.mValidTime = System.currentTimeMillis();
        setTotalTime();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("OnRecordFinish : ");
        sb.append(str);
        sb.append("  mTotalTime=");
        sb.append(this.mTotalTime);
        Log.d(str2, sb.toString());
        this.mTempVideoList.add(str);
        this.mHandler.post(new Runnable() {
            public void run() {
                FilmDreamImpl.this.mFilmDreamProcess.resumePlay();
                FilmDreamImpl.this.notifyModuleRecordingFinish();
            }
        });
    }

    public void OnRecordFinish(String str, long j, long j2) {
        OnRecordFinish(str);
    }

    public boolean canFinishRecording() {
        List list = this.mTempVideoList;
        return list != null && list.size() > 0;
    }

    public boolean canRecordingStop() {
        return this.mMediaRecorderRecording && System.currentTimeMillis() - this.mStartTime > 500;
    }

    public void combineVideoAudio(String str) {
        EffectMediaPlayer effectMediaPlayer = this.mEffectMediaPlayer;
        if (effectMediaPlayer != null) {
            effectMediaPlayer.StopPreView();
        }
        this.mFilmDreamProcessing.updateState(copyFile(this.mTempVideoList.size() > 0 ? (String) this.mTempVideoList.get(0) : null, str) ? 7 : 8);
    }

    public CameraSize getAlgorithmPreviewSize(List list) {
        return Util.getOptimalVideoSnapshotPictureSize(list, (double) getPreviewRatio(), 176, IjkMediaMeta.FF_PROFILE_H264_HIGH_444);
    }

    public int getAuthResult() {
        return 0;
    }

    public SurfaceTexture getInputSurfaceTexture() {
        return this.mInputSurfaceTexture;
    }

    public float getPreviewRatio() {
        return 1.7777777f;
    }

    public float getRecordSpeed() {
        return 1.0f;
    }

    public String getSegmentPath(int i) {
        File[] listFiles = new File(SEGMENTS_PATH).listFiles();
        return (listFiles == null || listFiles.length <= 0) ? "" : listFiles[0].getPath();
    }

    public long getStartRecordingTime() {
        return this.mStartTime;
    }

    public long getTotalRecordingTime() {
        return this.mFilmDreamProcessing.getTotalTime();
    }

    public void initPreview(int i, int i2, int i3, CameraScreenNail cameraScreenNail) {
        SurfaceTexture surfaceTexture = this.mInputSurfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.release();
            this.mOpenGlRender = null;
            this.mValidTime = 0;
        }
        this.mInited = false;
        this.mInputSurfaceTexture = new SurfaceTexture(false);
        this.mInputSurfaceTexture.setDefaultBufferSize(i, i2);
        this.mInputSurfaceTexture.setOnFrameAvailableListener(new OnFrameAvailableListener() {
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                FilmDreamImpl.this.mActivity.getCameraScreenNail().notifyFrameAvailable(4);
                FilmDreamImpl.this.mCameraView.requestRender();
            }
        });
        cameraScreenNail.setExternalFrameProcessor(this);
    }

    public void initResource() {
        FileUtils.makeDir(FILM_DIR);
        FileUtils.makeNoMediaDir(TEMPLATE_PATH);
        FileUtils.makeNoMediaDir(WORKSPACE_PATH);
        FileUtils.makeNoMediaDir(SEGMENTS_PATH);
    }

    public boolean isInited() {
        return this.mInited;
    }

    public boolean isProcessorReady() {
        return false;
    }

    public boolean isRecording() {
        return this.mMediaRecorderRecording;
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onDrawFrame(Rect rect, int i, int i2, boolean z) {
        SurfaceTexture surfaceTexture = this.mInputSurfaceTexture;
        if (surfaceTexture != null && !surfaceTexture.isReleased()) {
            if (this.mOpenGlRender == null) {
                this.mOpenGlRender = new OpenGlRender();
                this.mMiGLSurfaceViewRender = new MiFilmDreamGLSurfaceViewRender(this.mOpenGlRender);
                this.mMiGLSurfaceViewRender.init(this.mInputSurfaceTexture);
                OpenGlRender openGlRender = this.mOpenGlRender;
                int windowHeight = Display.getWindowHeight();
                int i3 = rect.bottom;
                openGlRender.SetWindowSize(0, windowHeight - i3, rect.right, i3 - rect.top);
                this.mInited = true;
            }
            this.mMiGLSurfaceViewRender.updateTexImage();
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            if (this.mValidTime <= 0) {
                this.mValidTime = System.currentTimeMillis();
            }
            if (!isRecording() || this.mNeedStop) {
                MiFilmDreamGLSurfaceViewRender miFilmDreamGLSurfaceViewRender = this.mMiGLSurfaceViewRender;
                int i4 = rect.left;
                int windowHeight2 = Display.getWindowHeight();
                int i5 = rect.bottom;
                miFilmDreamGLSurfaceViewRender.DrawCameraPreview(i4, windowHeight2 - i5, rect.right - rect.left, i5 - rect.top);
            } else {
                this.mMiGLSurfaceViewRender.bind(rect, i, i2);
                this.mMediaCamera.NeedProcessTexture(this.mInputSurfaceTexture.getTimestamp() / ExtraTextUtils.MB, 3840, 2160);
                this.mOpenGlRender.RenderFrame();
            }
        }
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        int i4 = (i2 + i3) % m.cQ;
        if (!(this.mCurrentOrientation == i4 || this.mMediaCamera == null || isRecording())) {
            this.mCurrentOrientation = i4;
            MiFilmDreamGLSurfaceViewRender miFilmDreamGLSurfaceViewRender = this.mMiGLSurfaceViewRender;
            if (miFilmDreamGLSurfaceViewRender != null) {
                miFilmDreamGLSurfaceViewRender.SetRotateAngle(this.mCurrentOrientation);
            }
        }
    }

    public boolean onPreviewFrame(Image image, Camera2Proxy camera2Proxy, int i) {
        return true;
    }

    public void pausePlay() {
        EffectMediaPlayer effectMediaPlayer = this.mEffectMediaPlayer;
        if (effectMediaPlayer != null) {
            effectMediaPlayer.PausePreView();
        }
    }

    public void prepare() {
        System.loadLibrary("vvc++_shared");
        System.loadLibrary("inception_video");
        SystemUtil.Init(this.mContext, 123);
        this.mCurrentFilmItem = DataRepository.dataItemLive().getCurrentFilmItem();
        prepare(null);
        this.mMediaCamera = new MediaEffectCamera();
        try {
            this.mMediaCamera.ConstructMediaEffectCamera(1920, 1080, 20971520, 30, this);
        } catch (Exception unused) {
        }
        this.mMediaCamera.SetEncoderType(2 == CameraSettings.getVideoEncoder() ? IjkMediaFormat.CODEC_NAME_H264 : "h265");
    }

    public void prepare(VVItem vVItem) {
        if (this.mFilmDreamProcessing == null) {
            this.mFilmDreamProcessing = (FilmDreamProcessing) DataRepository.dataItemObservable().get(FilmDreamProcessing.class);
        }
        this.mTempVideoList = this.mFilmDreamProcessing.getTempVideoList();
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(930, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(232, this);
    }

    public void release() {
        if (isRecording()) {
            resetFlag();
            stopRecording();
        }
        if (this.mMediaCamera != null) {
            Log.d(TAG, "release mediaCamera");
            this.mMediaCamera.DestructMediaEffectCamera();
            this.mMediaCamera = null;
        }
        if (this.mEffectMediaPlayer != null) {
            Log.d(TAG, "release mediaPlayer");
            this.mEffectMediaPlayer.StopPreView();
            this.mEffectMediaPlayer.DestructMediaPlayer();
            this.mEffectMediaPlayer = null;
        }
        if (this.mMediaEffectGraph != null) {
            Log.d(TAG, "release mediaEffectGraph");
            this.mMediaEffectGraph.DestructMediaEffectGraph();
            this.mMediaEffectGraph = null;
        }
        this.mHandler.removeCallbacksAndMessages(null);
        SystemUtil.UnInit();
        this.mCameraView.queueEvent(new Runnable() {
            public void run() {
                if (FilmDreamImpl.this.mInputSurfaceTexture != null) {
                    FilmDreamImpl.this.mInputSurfaceTexture.release();
                }
                if (FilmDreamImpl.this.mMiGLSurfaceViewRender != null) {
                    Log.d(FilmDreamImpl.TAG, "release render");
                    FilmDreamImpl.this.mMiGLSurfaceViewRender.release();
                }
            }
        });
    }

    public void releaseRender() {
    }

    public void resumePlay() {
        EffectMediaPlayer effectMediaPlayer = this.mEffectMediaPlayer;
        if (effectMediaPlayer != null) {
            effectMediaPlayer.ResumePreView();
        }
    }

    public void setRecordSpeed(int i) {
    }

    public void startPlay(Surface surface) {
        makeSureFilmDreamProcess();
        prepareEffectGraph();
        this.mPlayFinished = false;
        this.mEffectMediaPlayer = new EffectMediaPlayer(this.mMediaEffectGraph);
        this.mEffectMediaPlayer.ConstructMediaPlayer();
        this.mEffectMediaPlayer.SetPlayerNotify(new EffectNotifier() {
            public void OnReceiveFailed() {
                Log.d("OnReceiveFailed:", "yes");
                FilmDreamImpl.this.mPlayFinished = true;
                FilmDreamImpl.this.mFilmDreamProcess.onResultPreviewFinished(false);
            }

            public void OnReceiveFinish() {
                Log.d("OnReceiveFinish:", "yes");
                FilmDreamImpl.this.mPlayFinished = true;
                FilmDreamImpl.this.mFilmDreamProcess.onResultPreviewFinished(true);
            }
        });
        this.mEffectMediaPlayer.SetViewSurface(surface);
        this.mEffectMediaPlayer.setGravity(SurfaceGravity.SurfaceGravityResizeAspectFit, 1080, 1920);
        this.mEffectMediaPlayer.EnableUserAdjustRotatePlay(true);
        this.mEffectMediaPlayer.SetGraphLoop(true);
        this.mEffectMediaPlayer.StartPreView();
    }

    public void startRecording() {
        this.mMediaRecorderRecording = true;
        this.mFilmDreamProcessing.updateState(1);
        makeSureFilmDreamProcess();
        FileUtils.deleteSubFiles(SEGMENTS_PATH);
        this.mAudioController.stopAudio();
        String str = this.mCurrentFilmItem.configJsonPath;
        boolean isCinematicAspectRatioEnabled = CameraSettings.isCinematicAspectRatioEnabled(212);
        this.mMediaCamera.SetOrientation(this.mCurrentOrientation);
        this.mFilmDreamProcess.setThumbnailOrientation(this.mCurrentOrientation - 90);
        MiFilmDreamGLSurfaceViewRender miFilmDreamGLSurfaceViewRender = this.mMiGLSurfaceViewRender;
        if (miFilmDreamGLSurfaceViewRender != null) {
            miFilmDreamGLSurfaceViewRender.SetRotateAngle(this.mCurrentOrientation);
            this.mMiGLSurfaceViewRender.EnableFilmPicture(isCinematicAspectRatioEnabled);
        }
        this.mMediaCamera.EnableFilmPicture(isCinematicAspectRatioEnabled);
        String str2 = "";
        this.mMediaCamera.StartRecording(SEGMENTS_PATH, str, str2, str2);
        startRecordingTime();
    }

    public void stopRecording() {
        setTotalTime();
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        this.mMediaRecorderRecording = false;
        this.mMediaCamera.StopRecording();
        this.mAudioController.restoreAudio();
    }

    public void trackVideoParams() {
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(930, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(232, this);
        release();
    }
}
