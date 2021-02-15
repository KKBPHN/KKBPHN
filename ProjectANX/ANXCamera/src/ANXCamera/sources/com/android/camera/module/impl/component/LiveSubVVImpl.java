package com.android.camera.module.impl.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.opengl.GLES20;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Surface;
import com.android.camera.ActivityBase;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.SurfaceTextureScreenNail.ExternalFrameProcessor;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.VMProcessing;
import com.android.camera.fragment.vv.VVItem;
import com.android.camera.fragment.vv.VVWorkspaceItem;
import com.android.camera.log.Log;
import com.android.camera.module.AudioController;
import com.android.camera.module.BaseModule;
import com.android.camera.module.LiveModuleSubVV;
import com.android.camera.module.loader.NullHolder;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.LiveConfigVV;
import com.android.camera.protocol.ModeProtocol.LiveVVProcess;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.ui.V6CameraGLSurfaceView;
import com.android.camera2.Camera2Proxy;
import com.google.android.play.core.splitinstall.SplitInstallHelper;
import com.xiaomi.camera.rx.CameraSchedulers;
import com.xiaomi.mediaprocess.EffectCameraNotifier;
import com.xiaomi.mediaprocess.EffectMediaPlayer;
import com.xiaomi.mediaprocess.EffectMediaPlayer.SurfaceGravity;
import com.xiaomi.mediaprocess.EffectNotifier;
import com.xiaomi.mediaprocess.MediaComposeFile;
import com.xiaomi.mediaprocess.MediaEffectCamera;
import com.xiaomi.mediaprocess.MediaEffectGraph;
import com.xiaomi.mediaprocess.OpenGlRender;
import com.xiaomi.vlog.SystemUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import miui.text.ExtraTextUtils;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

public class LiveSubVVImpl implements LiveConfigVV, ExternalFrameProcessor, EffectCameraNotifier {
    /* access modifiers changed from: private */
    public static final String TAG = "LiveSubVVImpl";
    public static final String TEMPLATE_PATH;
    public static final String VV_DIR;
    public static final String WATERMARK_PATH;
    public static final String WORKSPACE_PATH;
    private ByteBuffer RGBColor = null;
    private final int TABLESIZE = 512;
    /* access modifiers changed from: private */
    public ActivityBase mActivity;
    private AudioController mAudioController;
    private int mCameraFacing;
    /* access modifiers changed from: private */
    public V6CameraGLSurfaceView mCameraView;
    private MediaComposeFile mComposeFile;
    private Context mContext;
    private int mCurrentIndex;
    private int mCurrentOrientation = -1;
    /* access modifiers changed from: private */
    public VVItem mCurrentVVItem;
    private VVWorkspaceItem mCurrentWorkspaceItem;
    private EffectMediaPlayer mEffectMediaPlayer;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public SurfaceTexture mInputSurfaceTexture;
    /* access modifiers changed from: private */
    public LiveVVProcess mLiveVVProcess;
    private MediaEffectCamera mMediaCamera;
    private MediaEffectGraph mMediaEffectGraph;
    private boolean mMediaRecorderRecording;
    private boolean mMediaRecorderRecordingPaused;
    /* access modifiers changed from: private */
    public MiGLSurfaceViewRender mMiGLSurfaceViewRender;
    private boolean mNeedRequestRender;
    /* access modifiers changed from: private */
    public boolean mNeedStop;
    private OpenGlRender mOpenGlRender;
    /* access modifiers changed from: private */
    public boolean mPlayFinished;
    /* access modifiers changed from: private */
    public Disposable mRecordingTimerDisposable;
    private long mValidTime;
    /* access modifiers changed from: private */
    public VMProcessing mVmProcessing;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtils.ROOT_DIR);
        sb.append("vv/");
        VV_DIR = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(VV_DIR);
        sb2.append("template/");
        TEMPLATE_PATH = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(VV_DIR);
        sb3.append("workspace/");
        WORKSPACE_PATH = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(TEMPLATE_PATH);
        sb4.append("watermark.mp4");
        WATERMARK_PATH = sb4.toString();
    }

    private LiveSubVVImpl(ActivityBase activityBase) {
        this.mActivity = activityBase;
        this.mContext = activityBase.getCameraAppImpl().getApplicationContext();
        this.mCameraView = activityBase.getGLView();
        this.mHandler = new Handler(this.mActivity.getMainLooper());
        this.mAudioController = new AudioController(this.mActivity.getApplicationContext());
    }

    public static LiveSubVVImpl create(ActivityBase activityBase) {
        return new LiveSubVVImpl(activityBase);
    }

    private void initFilter(String str) {
        Bitmap bitmap;
        if (!TextUtils.isEmpty(str) && new File(str).exists()) {
            try {
                Options options = new Options();
                options.inPreferredConfig = Config.ARGB_8888;
                options.outWidth = 512;
                options.outHeight = 512;
                bitmap = BitmapFactory.decodeFile(str, options);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                bitmap = null;
            }
            if (bitmap != null) {
                try {
                    int[] iArr = new int[262144];
                    for (int i = 0; i < 512; i++) {
                        for (int i2 = 0; i2 < 512; i2++) {
                            int pixel = bitmap.getPixel(i2, i);
                            iArr[(i * 512) + i2] = ((((((bitmap.hasAlpha() ? Color.alpha(pixel) : 255) * 256) + Color.blue(pixel)) * 256) + Color.green(pixel)) * 256) + Color.red(pixel);
                        }
                    }
                    this.RGBColor = ByteBuffer.allocateDirect(iArr.length * 32);
                    this.RGBColor.order(ByteOrder.nativeOrder());
                    this.RGBColor.asIntBuffer().put(iArr);
                    this.RGBColor.position(0);
                } catch (OutOfMemoryError e2) {
                    e2.printStackTrace();
                    this.RGBColor = null;
                }
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
    }

    private void makeSureVVProcess() {
        if (this.mLiveVVProcess == null) {
            this.mLiveVVProcess = (LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230);
        }
    }

    /* access modifiers changed from: private */
    public void notifyModuleRecordingFinish() {
        resetFlag();
        BaseModule baseModule = (BaseModule) this.mActivity.getCurrentModule();
        if (baseModule.getModuleIndex() == 179) {
            ((LiveModuleSubVV) baseModule).stopVideoRecording(false);
        }
    }

    private void prepareEffectGraph(VVItem vVItem, VVWorkspaceItem vVWorkspaceItem) {
        String str = vVItem.musicPath;
        ArrayList arrayList = new ArrayList(vVWorkspaceItem.getFragments());
        arrayList.add(WATERMARK_PATH);
        String[] strArr = (String[]) arrayList.toArray(new String[0]);
        this.mMediaEffectGraph = new MediaEffectGraph();
        this.mMediaEffectGraph.ConstructMediaEffectGraph();
        this.mMediaEffectGraph.SetAudioMute(true);
        this.mMediaEffectGraph.AddSourceAndEffectByTemplate(strArr, vVItem.baseArchivesFolder);
        this.mMediaEffectGraph.AddAudioTrack(str, false);
    }

    private void resetFlag() {
        this.mMediaRecorderRecording = false;
        this.mNeedRequestRender = false;
        this.mNeedStop = false;
    }

    private void restoreWorkSpace() {
    }

    private void saveWorkSpace() {
    }

    private void startCountDown(long j) {
        long j2 = j / 100;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startCountDown: ");
        sb.append(j);
        Log.d(str, sb.toString());
        final long j3 = (j2 * 100) - 100;
        Observable.intervalRange(0, j2, 0, 100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe((Observer) new Observer() {
            public void onComplete() {
                Log.d(LiveSubVVImpl.TAG, "onFinish");
            }

            public void onError(Throwable th) {
            }

            public void onNext(Long l) {
                LiveSubVVImpl.this.updateRecordingTime(j3 - (l.longValue() * 100));
            }

            public void onSubscribe(Disposable disposable) {
                LiveSubVVImpl.this.mRecordingTimerDisposable = disposable;
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateRecordingTime(long j) {
        float f = ((float) j) / 1000.0f;
        this.mLiveVVProcess.updateRecordingTime(String.format(Locale.ENGLISH, "%.1fS", new Object[]{Float.valueOf(Math.abs(f))}));
    }

    public void OnNeedStopRecording() {
        Log.d(TAG, "OnNeedStopRecording");
        this.mHandler.post(new Runnable() {
            public void run() {
                LiveSubVVImpl.this.mNeedStop = true;
                LiveSubVVImpl.this.stopRecording();
            }
        });
    }

    public void OnNotifyRender() {
        Log.d(TAG, "OnNotifyRender");
        this.mNeedRequestRender = true;
        this.mCameraView.requestRender();
    }

    public void OnRecordFailed() {
        Log.d(TAG, "OnRecordFailed");
    }

    public void OnRecordFinish(String str) {
        this.mValidTime = System.currentTimeMillis();
        if (!isRecording()) {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
            return;
        }
        this.mCurrentWorkspaceItem.add(str);
        final String targetThumbPath = this.mCurrentWorkspaceItem.getTargetThumbPath();
        final String rawInfoPath = this.mCurrentWorkspaceItem.getRawInfoPath();
        Single.just(str).observeOn(CameraSchedulers.sCameraSetupScheduler).map(new Function() {
            public NullHolder apply(String str) {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(str);
                return NullHolder.ofNullable(mediaMetadataRetriever.getFrameAtTime(0));
            }
        }).map(new Function() {
            public String apply(NullHolder nullHolder) {
                Bitmap bitmap = (Bitmap) nullHolder.get();
                if (bitmap == null) {
                    return "";
                }
                Util.saveToFile(bitmap, targetThumbPath, 90, CompressFormat.JPEG);
                File file = new File(rawInfoPath);
                if (!file.exists() || file.length() < 20) {
                    Util.saveToFile(LiveSubVVImpl.this.mCurrentVVItem.rawInfo, file);
                }
                return targetThumbPath;
            }
        }).subscribe();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("OnRecordFinish | s: ");
        sb.append(str);
        sb.append(" | ");
        sb.append(Thread.currentThread().getName());
        Log.d(str2, sb.toString());
        this.mHandler.post(new Runnable() {
            public void run() {
                LiveSubVVImpl.this.notifyModuleRecordingFinish();
            }
        });
    }

    public boolean canFinishRecording() {
        return this.mCurrentWorkspaceItem.completeSize() >= this.mCurrentVVItem.getEssentialFragmentSize();
    }

    public void combineVideoAudio(String str) {
        EffectMediaPlayer effectMediaPlayer = this.mEffectMediaPlayer;
        if (effectMediaPlayer != null) {
            effectMediaPlayer.StopPreView();
        }
        prepareEffectGraph(this.mCurrentVVItem, this.mCurrentWorkspaceItem);
        this.mComposeFile = new MediaComposeFile(this.mMediaEffectGraph);
        this.mComposeFile.ConstructMediaComposeFile(1920, 1088, 20971520, 30);
        this.mComposeFile.SetComposeNotify(new EffectNotifier() {
            public void OnReceiveFailed() {
                Log.d(LiveSubVVImpl.TAG, "ComposeCameraRecord OnReceiveFailed");
                LiveSubVVImpl.this.mVmProcessing.updateState(9);
            }

            public void OnReceiveFinish() {
                Log.d(LiveSubVVImpl.TAG, "ComposeCameraRecord OnReceiveFinish");
                LiveSubVVImpl.this.mVmProcessing.updateState(8);
            }
        });
        this.mComposeFile.SetComposeFileName(str);
        this.mComposeFile.BeginComposeFile();
    }

    public void deleteLastFragment() {
        if (!this.mCurrentWorkspaceItem.isEmpty()) {
            int completeSize = this.mCurrentWorkspaceItem.completeSize() - 1;
            VVWorkspaceItem vVWorkspaceItem = this.mCurrentWorkspaceItem;
            vVWorkspaceItem.remove(vVWorkspaceItem.completeSize() - 1);
            this.mCurrentIndex = this.mCurrentWorkspaceItem.completeSize();
            makeSureVVProcess();
            this.mLiveVVProcess.onRecordingFragmentUpdate(completeSize, -this.mCurrentVVItem.getDuration(completeSize));
        }
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

    public int getNextRecordStep() {
        if (!isRecording() && this.mValidTime != 0 && System.currentTimeMillis() - this.mValidTime >= 200) {
            return canFinishRecording() ? 3 : 2;
        }
        return 1;
    }

    public float getPreviewRatio() {
        return 1.7777777f;
    }

    public String getSegmentPath(int i) {
        VVWorkspaceItem vVWorkspaceItem = this.mCurrentWorkspaceItem;
        return vVWorkspaceItem != null ? vVWorkspaceItem.getFragmentAt(i) : "";
    }

    public void initPreview(int i, int i2, int i3, CameraScreenNail cameraScreenNail) {
        this.mCameraFacing = i3;
        SurfaceTexture surfaceTexture = this.mInputSurfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.release();
            this.mOpenGlRender = null;
            this.mValidTime = 0;
        }
        this.mInputSurfaceTexture = new SurfaceTexture(false);
        this.mInputSurfaceTexture.setDefaultBufferSize(i, i2);
        this.mInputSurfaceTexture.setOnFrameAvailableListener(new OnFrameAvailableListener() {
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                LiveSubVVImpl.this.mActivity.getCameraScreenNail().notifyFrameAvailable(4);
                LiveSubVVImpl.this.mCameraView.requestRender();
            }
        });
        cameraScreenNail.setExternalFrameProcessor(this);
    }

    public void initResource() {
        FileUtils.makeDir(VV_DIR);
        FileUtils.makeNoMediaDir(TEMPLATE_PATH);
        FileUtils.makeNoMediaDir(WORKSPACE_PATH);
    }

    public boolean isProcessorReady() {
        return false;
    }

    public boolean isRecording() {
        return this.mMediaRecorderRecording;
    }

    public boolean isRecordingPaused() {
        return this.mMediaRecorderRecordingPaused;
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onDrawFrame(Rect rect, int i, int i2, boolean z) {
        SurfaceTexture surfaceTexture = this.mInputSurfaceTexture;
        if (surfaceTexture != null && !surfaceTexture.isReleased()) {
            if (this.mOpenGlRender == null) {
                this.mOpenGlRender = new OpenGlRender();
                OpenGlRender openGlRender = this.mOpenGlRender;
                int windowHeight = Display.getWindowHeight();
                int i3 = rect.bottom;
                openGlRender.SetWindowSize(0, windowHeight - i3, rect.right, i3 - rect.top);
                this.mMiGLSurfaceViewRender = new MiGLSurfaceViewRender(this.mOpenGlRender);
                this.mMiGLSurfaceViewRender.setFilterRGBColor(this.RGBColor);
                this.mMiGLSurfaceViewRender.init(this.mInputSurfaceTexture);
            }
            this.mMiGLSurfaceViewRender.updateTexImage();
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            if (this.mValidTime <= 0) {
                this.mValidTime = System.currentTimeMillis();
            }
            if (!isRecording() || this.mNeedStop) {
                MiGLSurfaceViewRender miGLSurfaceViewRender = this.mMiGLSurfaceViewRender;
                int i4 = rect.left;
                int windowHeight2 = Display.getWindowHeight();
                int i5 = rect.bottom;
                miGLSurfaceViewRender.DrawCameraPreview(i4, windowHeight2 - i5, rect.right - rect.left, i5 - rect.top);
            } else {
                this.mMiGLSurfaceViewRender.bind(rect, i, i2);
                MediaEffectCamera mediaEffectCamera = this.mMediaCamera;
                if (mediaEffectCamera != null) {
                    mediaEffectCamera.NeedProcessTexture(this.mInputSurfaceTexture.getTimestamp() / ExtraTextUtils.MB);
                }
                this.mOpenGlRender.RenderFrame();
            }
        }
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        if (!(this.mCurrentOrientation == i2 || this.mMediaCamera == null || isRecording())) {
            this.mCurrentOrientation = i2;
        }
    }

    public boolean onPreviewFrame(Image image, Camera2Proxy camera2Proxy, int i) {
        return true;
    }

    public void onRecordingNewFragmentFinished() {
        long j;
        int i;
        LiveVVProcess liveVVProcess;
        makeSureVVProcess();
        if (isRecording()) {
            resetFlag();
            stopRecording();
            Disposable disposable = this.mRecordingTimerDisposable;
            if (disposable != null && !disposable.isDisposed()) {
                this.mRecordingTimerDisposable.dispose();
            }
            liveVVProcess = this.mLiveVVProcess;
            i = this.mCurrentIndex;
            j = -this.mCurrentVVItem.getDuration(i);
        } else {
            liveVVProcess = this.mLiveVVProcess;
            i = this.mCurrentIndex;
            j = this.mCurrentVVItem.getDuration(i);
        }
        liveVVProcess.onRecordingFragmentUpdate(i, j);
    }

    public void pausePlay() {
        this.mEffectMediaPlayer.PausePreView();
    }

    public void prepare() {
        SplitInstallHelper.loadLibrary(this.mContext.getApplicationContext(), "vvc++_shared");
        SplitInstallHelper.loadLibrary(this.mContext.getApplicationContext(), "vvffmpeg");
        SplitInstallHelper.loadLibrary(this.mContext.getApplicationContext(), "vvmediaeditor");
        SystemUtil.Init(this.mContext, 123);
        try {
            Util.verifyAssetZip(this.mContext, "vv/watermark.zip", TEMPLATE_PATH, 8192);
        } catch (IOException e) {
            e.printStackTrace();
        }
        prepare(DataRepository.dataItemLive().getCurrentVVItem());
        this.mMediaCamera = new MediaEffectCamera();
        this.mMediaCamera.SetOrientation(90);
        this.mMediaCamera.ConstructMediaEffectCamera(1920, 1080, 31457280, 30, this);
    }

    public void prepare(VVItem vVItem) {
        this.mCurrentVVItem = vVItem;
        if (this.mVmProcessing == null) {
            this.mVmProcessing = (VMProcessing) DataRepository.dataItemObservable().get(VMProcessing.class);
        }
        this.mCurrentWorkspaceItem = this.mVmProcessing.getCurrentWorkspaceItem(WORKSPACE_PATH, this.mCurrentVVItem.id);
        initFilter(vVItem.filterPath);
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(228, this);
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
        if (this.mComposeFile != null) {
            Log.d(TAG, "release composeFile");
            this.mComposeFile.CancelComposeFile();
            this.mComposeFile.DestructMediaComposeFile();
            this.mComposeFile = null;
        }
        if (this.mMediaEffectGraph != null) {
            Log.d(TAG, "release mediaEffectGraph");
            this.mMediaEffectGraph.DestructMediaEffectGraph();
            this.mMediaEffectGraph = null;
        }
        saveWorkSpace();
        this.mHandler.removeCallbacksAndMessages(null);
        Disposable disposable = this.mRecordingTimerDisposable;
        if (disposable != null) {
            disposable.dispose();
        }
        SystemUtil.UnInit();
        this.mCameraView.queueEvent(new Runnable() {
            public void run() {
                if (LiveSubVVImpl.this.mInputSurfaceTexture != null) {
                    LiveSubVVImpl.this.mInputSurfaceTexture.release();
                }
                if (LiveSubVVImpl.this.mMiGLSurfaceViewRender != null) {
                    Log.d(LiveSubVVImpl.TAG, "release render");
                    LiveSubVVImpl.this.mMiGLSurfaceViewRender.release();
                }
            }
        });
    }

    public void releaseRender() {
    }

    public void resumePlay() {
        this.mEffectMediaPlayer.ResumePreView();
    }

    public void startPlay(Surface surface) {
        prepareEffectGraph(this.mCurrentVVItem, this.mCurrentWorkspaceItem);
        this.mPlayFinished = false;
        this.mEffectMediaPlayer = new EffectMediaPlayer(this.mMediaEffectGraph);
        this.mEffectMediaPlayer.ConstructMediaPlayer();
        this.mEffectMediaPlayer.SetPlayerNotify(new EffectNotifier() {
            public void OnReceiveFailed() {
                Log.d("OnReceiveFailed:", "yes");
                LiveSubVVImpl.this.mPlayFinished = true;
                LiveSubVVImpl.this.mLiveVVProcess.onResultPreviewFinished(false);
            }

            public void OnReceiveFinish() {
                Log.d("OnReceiveFinish:", "yes");
                LiveSubVVImpl.this.mPlayFinished = true;
                LiveSubVVImpl.this.mLiveVVProcess.onResultPreviewFinished(true);
            }
        });
        this.mEffectMediaPlayer.SetViewSurface(surface);
        this.mEffectMediaPlayer.setGravity(SurfaceGravity.SurfaceGravityResizeAspectFit, 1920, 1080);
        this.mEffectMediaPlayer.SetPlayLoop(true);
        this.mEffectMediaPlayer.SetGraphLoop(true);
        this.mEffectMediaPlayer.StartPreView();
    }

    public void startRecordingNewFragment() {
        startRecordingNextFragment();
    }

    public void startRecordingNextFragment() {
        this.mMediaRecorderRecordingPaused = false;
        this.mAudioController.stopAudio();
        makeSureVVProcess();
        this.mCurrentIndex = this.mCurrentWorkspaceItem.completeSize();
        if (this.mCurrentIndex == 0) {
            FileUtils.makeNoMediaDir(this.mCurrentWorkspaceItem.mFolderPath);
        }
        long duration = this.mCurrentVVItem.getDuration(this.mCurrentIndex);
        VVItem vVItem = this.mCurrentVVItem;
        String str = vVItem.musicPath;
        String str2 = vVItem.configJsonPath;
        String str3 = vVItem.filterPath;
        this.mLiveVVProcess.onRecordingNewFragmentStart(this.mCurrentIndex, duration, this.mCurrentWorkspaceItem);
        long j = 0;
        for (int i = 0; i < this.mCurrentIndex; i++) {
            List list = this.mCurrentVVItem.speedList;
            j = (long) (((float) j) + (((float) this.mCurrentVVItem.getDuration(i)) / (list == null ? 1.0f : ((Float) list.get(i)).floatValue())));
        }
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("start : ");
        sb.append(duration);
        String str5 = " | ";
        sb.append(str5);
        sb.append(str);
        sb.append(str5);
        sb.append(str2);
        sb.append(str5);
        sb.append(str3);
        sb.append(str5);
        sb.append(j);
        Log.d(str4, sb.toString());
        MediaEffectCamera mediaEffectCamera = this.mMediaCamera;
        if (mediaEffectCamera != null) {
            mediaEffectCamera.SetOrientation(90);
            this.mMediaCamera.StartRecording(this.mCurrentIndex, this.mCurrentWorkspaceItem.mFolderPath, this.mCurrentVVItem.baseArchivesFolder, j);
        }
        this.mMediaRecorderRecording = true;
        startCountDown(duration);
    }

    public void stopRecording() {
        this.mMediaCamera.StopRecording();
        this.mAudioController.restoreAudio();
    }

    public void trackVideoParams() {
        String str = this.mCurrentVVItem.name;
        boolean z = true;
        if (this.mCameraFacing != 1) {
            z = false;
        }
        CameraStatUtils.trackVVRecordingParams(str, z);
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(228, this);
        release();
    }
}
