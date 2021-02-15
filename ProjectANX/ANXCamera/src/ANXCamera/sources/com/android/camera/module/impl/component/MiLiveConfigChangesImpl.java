package com.android.camera.module.impl.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.android.camera.ActivityBase;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.SurfaceTextureScreenNail.SurfaceTextureScreenNailCallback;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.fragment.beauty.LiveBeautyFilterFragment.LiveFilterItem;
import com.android.camera.log.Log;
import com.android.camera.module.AudioController;
import com.android.camera.module.MiLiveModule;
import com.android.camera.module.impl.component.ILive.ILiveRecorder;
import com.android.camera.module.impl.component.ILive.ILiveRecorderStateListener;
import com.android.camera.module.impl.component.ILive.ILiveRecordingTimeListener;
import com.android.camera.module.impl.component.MiLiveRecorder.Builder;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BluetoothHeadset;
import com.android.camera.protocol.ModeProtocol.MiLiveConfigChanges;
import com.android.camera.protocol.ModeProtocol.MiLiveRecorderControl.IRecorderListener;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera2.Camera2Proxy;
import com.xiaomi.camera.util.SystemProperties;
import java.io.File;
import java.io.IOException;
import java.util.List;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

public class MiLiveConfigChangesImpl implements MiLiveConfigChanges, ILiveRecorderStateListener {
    private static final int DEFAULT_FPS = 30;
    private static final int DEFAULT_RECORD_BITRATE = 31457280;
    private static final float DEFAULT_SPEED = 1.0f;
    private static final long RECORD_TIME_COMPENSATION = 100;
    private final float[] SPEEDS = {0.33f, 0.5f, 1.0f, 2.0f, 3.0f};
    private final String TAG;
    /* access modifiers changed from: private */
    public ActivityBase mActivity;
    private AudioController mAudioController;
    private Context mContext;
    private String mCurAudioPath;
    private float mCurSpeed;
    private int mCurrentOrientation = -1;
    private String mFilterBitmapPath;
    private int mRecordState = 0;
    private ILiveRecorder mRecorder;
    private IRecorderListener mRecorderListener;
    private ILiveRecordingTimeListener mRecordingTimeListener = new ILiveRecordingTimeListener() {
        public void onRecordingTimeFinish() {
            if (MiLiveConfigChangesImpl.this.mActivity != null && MiLiveConfigChangesImpl.this.mActivity.getCurrentModule() != null && (MiLiveConfigChangesImpl.this.mActivity.getCurrentModule() instanceof MiLiveModule)) {
                ((MiLiveModule) MiLiveConfigChangesImpl.this.mActivity.getCurrentModule()).stopVideoRecording(false, true);
            }
        }

        public void onRecordingTimeUpdate(long j, float f) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.updateRecordingTime(Util.millisecondToTimeString((long) ((((float) j) * 1.0f) / f), false));
            }
        }
    };
    private SurfaceTextureScreenNailCallback mRender;
    private Handler mUIHandler;

    private MiLiveConfigChangesImpl(ActivityBase activityBase) {
        StringBuilder sb = new StringBuilder();
        sb.append(MiLiveConfigChangesImpl.class.getSimpleName());
        sb.append("@");
        sb.append(hashCode());
        this.TAG = sb.toString();
        this.mActivity = activityBase;
        this.mContext = activityBase.getApplicationContext();
        this.mAudioController = new AudioController(this.mActivity.getApplicationContext());
    }

    public static MiLiveConfigChangesImpl create(ActivityBase activityBase) {
        return new MiLiveConfigChangesImpl(activityBase);
    }

    public /* synthetic */ void O00ooOO() {
        IRecorderListener iRecorderListener = this.mRecorderListener;
        if (iRecorderListener != null && this.mRecorder != null) {
            iRecorderListener.onRecorderError();
        }
    }

    public /* synthetic */ void O00ooOO0() {
        IRecorderListener iRecorderListener = this.mRecorderListener;
        if (iRecorderListener != null) {
            ILiveRecorder iLiveRecorder = this.mRecorder;
            if (iLiveRecorder != null) {
                iRecorderListener.onRecorderFinish(iLiveRecorder.getLiveSegments(), this.mCurAudioPath);
                this.mRecorder.getLiveSegments().clear();
                DataRepository.dataItemLive().setMiLiveSegmentData(null);
            }
        }
    }

    public /* synthetic */ void O00ooOo0() {
        IRecorderListener iRecorderListener = this.mRecorderListener;
        if (iRecorderListener != null) {
            ILiveRecorder iLiveRecorder = this.mRecorder;
            if (iLiveRecorder != null) {
                iRecorderListener.onRecorderPaused(iLiveRecorder.getLiveSegments());
            }
        }
    }

    public boolean canRecordingStop() {
        return this.mRecorder != null && ((float) (System.currentTimeMillis() - this.mRecorder.getStartTime())) > this.mCurSpeed * 500.0f;
    }

    public void clearAudio() {
        this.mCurAudioPath = "";
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            iLiveRecorder.setAudioPath(this.mCurAudioPath);
            CameraSettings.setCurrentLiveMusic(null, null);
            BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
            if (bluetoothHeadset != null) {
                bluetoothHeadset.startBluetoothSco(this.mActivity.getCurrentModuleIndex());
            }
        }
    }

    public void deleteLastFragment() {
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            iLiveRecorder.deletePreSegment();
            if (this.mRecorder.getLiveSegments().isEmpty()) {
                IRecorderListener iRecorderListener = this.mRecorderListener;
                if (iRecorderListener != null) {
                    iRecorderListener.onRecorderCancel();
                }
            }
        }
    }

    public CameraSize getAlgorithmPreviewSize(List list) {
        return Util.getOptimalVideoSnapshotPictureSize(list, (double) getPreviewRatio(), 176, IjkMediaMeta.FF_PROFILE_H264_HIGH_444);
    }

    public String getAudioPath() {
        return this.mCurAudioPath;
    }

    @SuppressLint({"WrongConstant"})
    public int getAuthResult() {
        return 0;
    }

    public int getCurState() {
        int i = this.mRecordState;
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                if (i != 3) {
                    if (i != 4) {
                        if (i != 6) {
                            if (i != 7) {
                                return 0;
                            }
                        }
                    }
                }
                return 3;
            }
        }
        return i2;
    }

    public SurfaceTexture getInputSurfaceTexture() {
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            return iLiveRecorder.genInputSurfaceTexture();
        }
        Log.e(this.TAG, "genInputSurfaceTexture null");
        return null;
    }

    public float getPreviewRatio() {
        return 1.7777777f;
    }

    public float getRecordSpeed() {
        return this.mCurSpeed;
    }

    public int getSegmentSize() {
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            return iLiveRecorder.getLiveSegments().size();
        }
        return 0;
    }

    public long getStartRecordingTime() {
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            return iLiveRecorder.getStartTime();
        }
        return 0;
    }

    public String getTimeValue() {
        return Util.millisecondToTimeString(Util.clamp(getTotalRecordingTime(), 1000, 15000), false, true);
    }

    public long getTotalRecordingTime() {
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            return ILive.getTotalDuration(iLiveRecorder.getLiveSegments());
        }
        return 0;
    }

    public void initPreview(int i, int i2, int i3, CameraScreenNail cameraScreenNail) {
        if (this.mRecorder == null) {
            MiLiveRecorder build = new Builder(this.mActivity, i, i2).setStateListener(this).setHandler(this.mUIHandler).setRecordingTimeListener(this.mRecordingTimeListener).setMaxDuration(15100).setBitrate(DEFAULT_RECORD_BITRATE).setFps(30).setVideoSaveDirPath(FileUtils.MI_LIVE_TMP).setSegmentData(DataRepository.dataItemLive().getMiLiveSegmentData()).build();
            this.mRender = build;
            this.mRecorder = build;
        }
        this.mRecorder.init(i, i2);
        LiveFilterItem findLiveFilter = EffectController.getInstance().findLiveFilter(this.mActivity, DataRepository.dataItemLive().getLiveFilter());
        String str = "";
        setFilter(true, findLiveFilter != null ? findLiveFilter.directoryName : str);
        setRecordSpeed(Integer.valueOf(CameraSettings.getCurrentLiveSpeed()).intValue());
        String[] currentLiveMusic = CameraSettings.getCurrentLiveMusic();
        if (!currentLiveMusic[0].isEmpty()) {
            str = currentLiveMusic[0];
        }
        setAudioPath(str);
    }

    public void initResource() {
        Log.d(this.TAG, "initResource");
        if (!FileUtils.hasDir(FileUtils.ROOT_DIR) || !FileUtils.makeSureNoMedia(FileUtils.RESOURCE_DIR) || !FileUtils.makeSureNoMedia(FileUtils.MI_LIVE_TMP) || !FileUtils.makeSureNoMedia(FileUtils.MUSIC_LOCAL) || !FileUtils.makeSureNoMedia(FileUtils.MUSIC_ONLINE)) {
            FileUtils.makeNoMediaDir(FileUtils.RESOURCE_DIR);
            FileUtils.makeNoMediaDir(FileUtils.MI_LIVE_TMP);
            FileUtils.makeNoMediaDir(FileUtils.MUSIC_LOCAL);
            FileUtils.makeNoMediaDir(FileUtils.MUSIC_ONLINE);
            FileUtils.makeNoMediaDir(FileUtils.ROOT_DIR);
        }
        String str = Util.isGlobalVersion() ? "mi_music_global.zip" : "mi_music_cn.zip";
        try {
            Context context = this.mContext;
            StringBuilder sb = new StringBuilder();
            sb.append("live/");
            sb.append(str);
            Util.verifyAssetZip(context, sb.toString(), FileUtils.MUSIC_LOCAL, 32768);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRecording() {
        return getCurState() == 2;
    }

    public boolean isRecordingPaused() {
        return getCurState() == 3;
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        if (this.mCurrentOrientation != i2 && !isRecording()) {
            this.mCurrentOrientation = i2;
        }
    }

    public boolean onPreviewFrame(Image image, Camera2Proxy camera2Proxy, int i) {
        return true;
    }

    public void onStateChange(int i) {
        Handler handler;
        Runnable runnable;
        this.mRecordState = i;
        int i2 = this.mRecordState;
        if (i2 == 3) {
            handler = this.mUIHandler;
            runnable = new O000O0OO(this);
        } else if (i2 == 8) {
            handler = this.mUIHandler;
            runnable = new C0405O000O0Oo(this);
        } else if (i2 == 9) {
            handler = this.mUIHandler;
            runnable = new O00oOoOo(this);
        } else {
            return;
        }
        handler.post(runnable);
    }

    public void onSurfaceTextureReleased() {
        SurfaceTextureScreenNailCallback surfaceTextureScreenNailCallback = this.mRender;
        if (surfaceTextureScreenNailCallback != null) {
            surfaceTextureScreenNailCallback.onSurfaceTextureReleased();
        }
    }

    public void onSurfaceTextureUpdated(DrawExtTexAttribute drawExtTexAttribute) {
        SurfaceTextureScreenNailCallback surfaceTextureScreenNailCallback = this.mRender;
        if (surfaceTextureScreenNailCallback != null) {
            surfaceTextureScreenNailCallback.onSurfaceTextureUpdated(this.mActivity.getGLView().getGLCanvas(), drawExtTexAttribute);
        }
    }

    public void pauseRecording() {
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            iLiveRecorder.pauseRecording();
            this.mAudioController.restoreAudio();
        }
    }

    public void prepare() {
        Log.d(this.TAG, "prepare");
        this.mUIHandler = new Handler(Looper.getMainLooper());
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(241, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(232, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(243, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(244, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(211, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(245, this);
    }

    public void release() {
        Log.d(this.TAG, "release");
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            iLiveRecorder.release();
        }
        Handler handler = this.mUIHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void reset() {
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            iLiveRecorder.reset();
        }
    }

    public void resumeRecording() {
        if (this.mRecorder != null && isRecordingPaused()) {
            this.mAudioController.silenceAudio();
            this.mRecorder.resumeRecording();
        }
    }

    public void setAudioPath(String str) {
        this.mCurAudioPath = str;
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            iLiveRecorder.setAudioPath(str);
        }
    }

    public void setFilter(boolean z, String str) {
        String str2;
        if (!z || TextUtils.isEmpty(str)) {
            str2 = "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(FileUtils.FILTER_DIR);
            sb.append(str);
            sb.append(File.separator);
            sb.append(str);
            sb.append(File.separator);
            sb.append(str);
            sb.append(FileUtils.FILTER_FILE_SUFFIX);
            str2 = sb.toString();
        }
        this.mFilterBitmapPath = str2;
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            iLiveRecorder.setFilterPath(this.mFilterBitmapPath);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0013  */
    /* JADX WARNING: Removed duplicated region for block: B:9:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setRecordSpeed(int i) {
        float f;
        ILiveRecorder iLiveRecorder;
        if (i >= 0) {
            float[] fArr = this.SPEEDS;
            if (i < fArr.length) {
                f = fArr[i];
                this.mCurSpeed = f;
                iLiveRecorder = this.mRecorder;
                if (iLiveRecorder == null) {
                    iLiveRecorder.setSpeed(this.mCurSpeed);
                    return;
                }
                return;
            }
        }
        f = 1.0f;
        this.mCurSpeed = f;
        iLiveRecorder = this.mRecorder;
        if (iLiveRecorder == null) {
        }
    }

    public void setRecorderListener(IRecorderListener iRecorderListener) {
        this.mRecorderListener = iRecorderListener;
    }

    public void startRecording() {
        if (this.mRecorder != null && !isRecording()) {
            FileUtils.deleteSubFiles(FileUtils.MI_LIVE_TMP);
            if (!SystemProperties.getBoolean("camera.debug.dump_milive", false)) {
                FileUtils.deleteSubFiles(FileUtils.VIDEO_DUMP);
            }
            BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
            if (bluetoothHeadset == null || !bluetoothHeadset.isSupportBluetoothSco(this.mActivity.getCurrentModuleIndex())) {
                this.mAudioController.silenceAudio();
            }
            this.mRecorder.setOrientation((this.mCurrentOrientation + 90) % m.cQ);
            this.mRecorder.startRecording();
        }
    }

    public void stopRecording() {
        ILiveRecorder iLiveRecorder = this.mRecorder;
        if (iLiveRecorder != null) {
            iLiveRecorder.stopRecording();
            this.mAudioController.restoreAudio();
        }
    }

    public void trackVideoParams() {
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(211, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(244, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(243, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(232, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(245, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(241, this);
        release();
    }
}
