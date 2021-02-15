package com.android.camera.module.impl.component;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Surface;
import com.android.camera.ActivityBase;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.SensorStateManager;
import com.android.camera.Util;
import com.android.camera.constant.DurationConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.effect.EffectController;
import com.android.camera.fragment.beauty.LiveBeautyFilterFragment.LiveFilterItem;
import com.android.camera.log.Log;
import com.android.camera.module.AudioController;
import com.android.camera.module.LiveModule;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.FilterProtocol;
import com.android.camera.protocol.ModeProtocol.FullScreenProtocol;
import com.android.camera.protocol.ModeProtocol.LiveConfigChanges;
import com.android.camera.protocol.ModeProtocol.OnShineChangedProtocol;
import com.android.camera.protocol.ModeProtocol.StickerProtocol;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.gallery3d.ui.ExtTexture;
import com.ss.android.vesdk.TERecorder;
import com.ss.android.vesdk.TERecorder.IRenderCallback;
import com.ss.android.vesdk.TERecorder.OnConcatFinishedListener;
import com.ss.android.vesdk.TERecorder.OnSlamDetectListener;
import com.ss.android.vesdk.TERecorder.Texture;
import com.ss.android.vesdk.VEAudioEncodeSettings;
import com.ss.android.vesdk.VECameraSettings;
import com.ss.android.vesdk.VECameraSettings.CAMERA_FACING_ID;
import com.ss.android.vesdk.VEListener.VERecorderNativeInitListener;
import com.ss.android.vesdk.VEPreviewSettings;
import com.ss.android.vesdk.VEPreviewSettings.Builder;
import com.ss.android.vesdk.VESDK;
import com.ss.android.vesdk.VESize;
import com.ss.android.vesdk.VEVideoEncodeSettings;
import com.ss.android.vesdk.VEVideoEncodeSettings.ENCODE_PROFILE;
import com.ss.android.vesdk.runtime.VERuntime;
import com.ss.android.vesdk.runtime.oauth.TEOAuthResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LiveConfigChangeTTImpl implements LiveConfigChanges {
    private static final long MIN_RECORD_TIME = 500;
    private static final long START_OFFSET_MS = 450;
    /* access modifiers changed from: private */
    public static final String TAG = "LiveConfigChangeTTImpl";
    private static final float WHITE_INTENSITY = 0.2f;
    private final float[] SPEEDS = {0.33f, 0.5f, 1.0f, 2.0f, 3.0f};
    /* access modifiers changed from: private */
    public ActivityBase mActivity;
    private AudioController mAudioController;
    private VEAudioEncodeSettings mAudioEncodeSettings;
    private TEOAuthResult mAuthResult;
    private String mBGMPath;
    private OnShineChangedProtocol mBeautyImpl;
    private String mConcatVideoPath;
    private String mConcatWavPath;
    /* access modifiers changed from: private */
    public Context mContext;
    private CountDownTimer mCountDownTimer;
    /* access modifiers changed from: private */
    public float mCurrentSpeed;
    private FilterProtocol mFilterImpl;
    private Handler mHandler;
    private boolean mInitialized;
    /* access modifiers changed from: private */
    public boolean mInputSurfaceReady;
    /* access modifiers changed from: private */
    public SurfaceTexture mInputSurfaceTexture;
    private boolean mIsFrontCamera;
    private int mMaxVideoDurationInMs;
    /* access modifiers changed from: private */
    public boolean mMediaRecorderRecording = false;
    private boolean mMediaRecorderRecordingPaused;
    private VEPreviewSettings mPreviewSettings;
    private List mRecordSegmentTimeInfo;
    /* access modifiers changed from: private */
    public TERecorder mRecorder;
    private final Object mRecorderLock = new Object();
    /* access modifiers changed from: private */
    public boolean mReleased;
    private long mStartTime;
    private StickerProtocol mStickerImpl;
    /* access modifiers changed from: private */
    public String mStickerPath;
    /* access modifiers changed from: private */
    public boolean mTTNativeIsInit = false;
    private long mTotalRecordingTime = 0;
    private VEVideoEncodeSettings mVideoEncodeSettings;

    /* renamed from: com.android.camera.module.impl.component.LiveConfigChangeTTImpl$5 reason: invalid class name */
    /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$ss$android$vesdk$runtime$oauth$TEOAuthResult = new int[TEOAuthResult.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$ss$android$vesdk$runtime$oauth$TEOAuthResult[TEOAuthResult.OK.ordinal()] = 1;
            $SwitchMap$com$ss$android$vesdk$runtime$oauth$TEOAuthResult[TEOAuthResult.TBD.ordinal()] = 2;
            $SwitchMap$com$ss$android$vesdk$runtime$oauth$TEOAuthResult[TEOAuthResult.EXPIRED.ordinal()] = 3;
            $SwitchMap$com$ss$android$vesdk$runtime$oauth$TEOAuthResult[TEOAuthResult.FAIL.ordinal()] = 4;
            $SwitchMap$com$ss$android$vesdk$runtime$oauth$TEOAuthResult[TEOAuthResult.NOT_MATCH.ordinal()] = 5;
        }
    }

    private LiveConfigChangeTTImpl(ActivityBase activityBase) {
        this.mActivity = activityBase;
        this.mContext = activityBase.getCameraAppImpl().getApplicationContext();
        this.mHandler = new Handler(this.mActivity.getMainLooper());
        this.mRecorder = new TERecorder(FileUtils.ROOT_DIR, this.mContext, this.mHandler);
        this.mFilterImpl = new LiveFilterChangeImpl(this.mRecorder);
        this.mBeautyImpl = new LiveBeautyChangeImpl(this.mRecorder);
        this.mStickerImpl = new LiveStickerChangeImpl(this.mRecorder);
        this.mInputSurfaceTexture = new SurfaceTexture(false);
        this.mMaxVideoDurationInMs = DurationConstant.DURATION_LIVE_RECORD;
        this.mAudioController = new AudioController(this.mActivity.getApplicationContext());
    }

    public static LiveConfigChangeTTImpl create(ActivityBase activityBase) {
        return new LiveConfigChangeTTImpl(activityBase);
    }

    private void deleteLastSegment() {
        TERecorder tERecorder = this.mRecorder;
        if (tERecorder != null) {
            tERecorder.deleteLastFrag();
        }
    }

    private double getTimestamp(SensorEvent sensorEvent) {
        long nanoTime = System.nanoTime();
        return (double) (nanoTime - Math.min(Math.abs(nanoTime - sensorEvent.timestamp), Math.abs(SystemClock.elapsedRealtimeNanos() - sensorEvent.timestamp)));
    }

    private boolean hasSegments() {
        List list = this.mRecordSegmentTimeInfo;
        return list != null && !list.isEmpty();
    }

    private void resumeEffect() {
        String[] currentLiveMusic = CameraSettings.getCurrentLiveMusic();
        if (!currentLiveMusic[0].isEmpty()) {
            setAudioPath(currentLiveMusic[0]);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtils.STICKER_RESOURCE_DIR);
        sb.append(CameraSettings.getCurrentLiveSticker());
        this.mStickerPath = sb.toString();
        setRecordSpeed(Integer.valueOf(CameraSettings.getCurrentLiveSpeed()).intValue());
    }

    /* access modifiers changed from: private */
    public void updateBeauty() {
        float faceBeautyRatio = ((float) CameraSettings.getFaceBeautyRatio("key_live_shrink_face_ratio")) / 100.0f;
        float faceBeautyRatio2 = ((float) CameraSettings.getFaceBeautyRatio("key_live_enlarge_eye_ratio")) / 100.0f;
        float faceBeautyRatio3 = ((float) CameraSettings.getFaceBeautyRatio("key_live_smooth_strength")) / 100.0f;
        if (faceBeautyRatio > 0.0f || faceBeautyRatio2 > 0.0f || faceBeautyRatio3 > 0.0f) {
            setBeautyFaceReshape(true, faceBeautyRatio2, faceBeautyRatio);
            setBeautify(true, faceBeautyRatio3);
            return;
        }
        setBeautyFaceReshape(false, 0.0f, 0.0f);
        setBeautify(false, 0.0f);
    }

    /* access modifiers changed from: private */
    public void updateRecordingTime(long j, float f) {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.updateRecordingTime(Util.millisecondToTimeString((long) ((((float) j) * 1.0f) / f), false));
        }
    }

    public boolean canRecordingStop() {
        return hasSegments();
    }

    public void clearAudio() {
        TERecorder tERecorder = this.mRecorder;
        if (tERecorder != null) {
            this.mBGMPath = null;
            tERecorder.setRecordBGM("", 0, 1);
            CameraSettings.setCurrentLiveMusic(null, null);
        }
    }

    public int getAuthResult() {
        TEOAuthResult tEOAuthResult = this.mAuthResult;
        if (tEOAuthResult == null) {
            return 3;
        }
        int i = AnonymousClass5.$SwitchMap$com$ss$android$vesdk$runtime$oauth$TEOAuthResult[tEOAuthResult.ordinal()];
        if (i == 1) {
            return 0;
        }
        if (i == 2) {
            return 1;
        }
        if (i != 3) {
            return (i == 4 || i != 5) ? 3 : 4;
        }
        return 2;
    }

    public Pair getConcatResult() {
        String str = this.mConcatWavPath;
        String str2 = this.mBGMPath;
        if (str2 != null) {
            str = str2;
        }
        return new Pair(this.mConcatVideoPath, str);
    }

    public SurfaceTexture getInputSurfaceTexture() {
        return this.mInputSurfaceTexture;
    }

    public float getRecordSpeed() {
        return this.mCurrentSpeed;
    }

    public int getSegmentSize() {
        List list = this.mRecordSegmentTimeInfo;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public long getStartRecordingTime() {
        return this.mStartTime;
    }

    public String getTimeValue() {
        return Util.millisecondToTimeString(Util.clamp(this.mTotalRecordingTime, 1000, 15000), false, true);
    }

    public long getTotalRecordingTime() {
        return this.mTotalRecordingTime;
    }

    public void initPreview(int i, int i2, boolean z, int i3) {
        long j;
        StringBuilder sb = new StringBuilder();
        sb.append(i3);
        sb.append("");
        Log.e("live initPreview:", sb.toString());
        this.mIsFrontCamera = z;
        this.mPreviewSettings = new Builder().setRenderSize(new VESize(i, i2)).build();
        this.mInputSurfaceTexture.setDefaultBufferSize(i2, i);
        VECameraSettings vECameraSettings = new VECameraSettings(z ? CAMERA_FACING_ID.FACING_FRONT : CAMERA_FACING_ID.FACING_BACK, i3, new VESize(i, i2));
        this.mRecordSegmentTimeInfo = DataRepository.dataItemLive().getRecordSegmentTimeInfo();
        if (this.mInitialized) {
            this.mRecorder.setCameraSettings(vECameraSettings);
            List list = this.mRecordSegmentTimeInfo;
            if (list != null) {
                j = TimeSpeedModel.calculateRealTime(list);
            } else {
                this.mRecordSegmentTimeInfo = new ArrayList();
                j = 0;
            }
            this.mTotalRecordingTime = j;
        } else {
            this.mVideoEncodeSettings = new VEVideoEncodeSettings.Builder(1).setHwEnc(true).setEncodeProfile(ENCODE_PROFILE.ENCODE_PROFILE_MAIN).setVideoRes(i, i2).build();
            this.mRecorder.init(this.mVideoEncodeSettings, null, this.mPreviewSettings, vECameraSettings);
            List list2 = this.mRecordSegmentTimeInfo;
            if (list2 != null) {
                this.mRecorder.tryRestore(list2.size());
                this.mTotalRecordingTime = TimeSpeedModel.calculateRealTime(this.mRecordSegmentTimeInfo);
                this.mMediaRecorderRecordingPaused = true;
                this.mMediaRecorderRecording = false;
            } else {
                this.mRecordSegmentTimeInfo = new ArrayList();
                this.mRecorder.clearEnv();
            }
            this.mInitialized = true;
        }
        FullScreenProtocol fullScreenProtocol = (FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196);
        if (fullScreenProtocol != null) {
            fullScreenProtocol.onRecordSegmentCreated();
        }
        resumeEffect();
    }

    public void initResource() {
        String str;
        StringBuilder sb;
        String str2;
        List<String> list;
        String str3;
        String str4 = "live/";
        VESDK.setExternalMonitorListener(MyOwnMonitor.Instance);
        VESDK.init(this.mContext, FileUtils.ROOT_DIR);
        String OOOoOO0 = C0122O00000o.instance().OOOoOO0();
        this.mAuthResult = VERuntime.activate(this.mContext, C0122O00000o.instance().OOOoOO(), OOOoOO0, DataRepository.dataItemLive().getActivation());
        TEOAuthResult tEOAuthResult = this.mAuthResult;
        if (tEOAuthResult == TEOAuthResult.OK || tEOAuthResult == TEOAuthResult.TBD) {
            DataRepository.dataItemLive().setActivation(VERuntime.getActivationCode());
            str2 = TAG;
            sb = new StringBuilder();
            str = "activation success: ";
        } else {
            str2 = TAG;
            sb = new StringBuilder();
            str = "activation failed: ";
        }
        sb.append(str);
        sb.append(this.mAuthResult.name());
        Log.d(str2, sb.toString());
        if (!FileUtils.hasDir(FileUtils.ROOT_DIR) || !FileUtils.makeSureNoMedia(FileUtils.RESOURCE_DIR)) {
            FileUtils.makeNoMediaDir(FileUtils.FILTER_DIR);
            FileUtils.makeNoMediaDir(FileUtils.RESOURCE_DIR);
            FileUtils.makeNoMediaDir(FileUtils.RESHAPE_DIR_NAME);
            FileUtils.makeNoMediaDir(FileUtils.VIDEO_TMP);
            FileUtils.makeNoMediaDir(FileUtils.CONCAT_VIDEO_DIR);
            FileUtils.makeNoMediaDir(FileUtils.MUSIC_LOCAL);
            FileUtils.makeNoMediaDir(FileUtils.MUSIC_ONLINE);
            FileUtils.makeNoMediaDir(FileUtils.ROOT_DIR);
        }
        try {
            if (Util.isGlobalVersion()) {
                str3 = "music_global.zip";
                list = FileUtils.RESOURCE_LIST_GLOBAL;
            } else {
                str3 = "music_cn.zip";
                list = FileUtils.RESOURCE_LIST_CN;
            }
            Context context = this.mContext;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str4);
            sb2.append(str3);
            Util.verifyAssetZip(context, sb2.toString(), FileUtils.MUSIC_LOCAL, 32768);
            FileUtils.makeDir(FileUtils.MUSIC_ONLINE);
            for (String str5 : list) {
                Context context2 = this.mContext;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str4);
                sb3.append(str5);
                sb3.append(".zip");
                String sb4 = sb3.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(FileUtils.STICKER_RESOURCE_DIR);
                sb5.append(str5);
                Util.verifyAssetZip(context2, sb4, sb5.toString(), 32768);
            }
            Util.verifyAssetZip(this.mContext, "live/Beauty_12.zip", FileUtils.BEAUTY_12_DIR, 32768);
            StringBuilder sb6 = new StringBuilder();
            sb6.append(FileUtils.RESOURCE_DIR);
            sb6.append("filter");
            Util.verifyAssetZip(this.mContext, "live/filter.zip", sb6.toString(), 32768);
            Util.verifyAssetZip(this.mContext, "live/FaceReshape_V2.zip", FileUtils.RESHAPE_DIR_NAME, 32768);
        } catch (Exception e) {
            Log.e(TAG, "verify asset zip failed...", (Throwable) e);
        }
        if (VESDK.needUpdateEffectModelFiles()) {
            VESDK.updateEffectModelFiles();
        }
        FileUtils.makeNoMediaDir(FileUtils.MODELS_DIR);
    }

    public boolean isRecording() {
        return this.mMediaRecorderRecording;
    }

    public boolean isRecordingPaused() {
        return this.mMediaRecorderRecordingPaused;
    }

    public void onDeviceRotationChange(float[] fArr) {
        synchronized (this.mRecorderLock) {
            if (this.mRecorder != null) {
                this.mRecorder.setDeviceRotation(fArr);
            }
        }
    }

    public boolean onRecordConcat(OnConcatFinishedListener onConcatFinishedListener) {
        if (!hasSegments()) {
            Log.e(TAG, "record segments is empty, stop concat");
            return false;
        }
        File file = new File(FileUtils.CONCAT_VIDEO_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        this.mConcatVideoPath = new File(FileUtils.CONCAT_VIDEO_DIR, "concat.mp4").getAbsolutePath();
        this.mConcatWavPath = new File(FileUtils.CONCAT_VIDEO_DIR, "concat.wav").getAbsolutePath();
        this.mRecorder.concatAsync(this.mConcatVideoPath, this.mConcatWavPath, onConcatFinishedListener);
        return true;
    }

    public void onRecordPause() {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        TERecorder tERecorder = this.mRecorder;
        if (tERecorder != null && !this.mMediaRecorderRecordingPaused) {
            int stopRecord = tERecorder.stopRecord();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("stopRecordResult onPause: ");
            sb.append(stopRecord);
            Log.d(str, sb.toString());
            long endFrameTime = this.mRecorder.getEndFrameTime() / 1000;
            if (endFrameTime > 500 || endFrameTime < 0) {
                this.mRecordSegmentTimeInfo.add(new TimeSpeedModel(endFrameTime, (double) this.mCurrentSpeed));
                this.mTotalRecordingTime = TimeSpeedModel.calculateRealTime(this.mRecordSegmentTimeInfo);
            } else {
                deleteLastSegment();
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("recording time = ");
                sb2.append(endFrameTime);
                sb2.append(", it's too short");
                Log.d(str2, sb2.toString());
            }
            DataRepository.dataItemLive().setRecordSegmentTimeInfo(this.mRecordSegmentTimeInfo);
            this.mMediaRecorderRecordingPaused = true;
            this.mMediaRecorderRecording = false;
        }
    }

    public void onRecordResume() {
        if (this.mRecorder != null && this.mMediaRecorderRecordingPaused) {
            this.mAudioController.silenceAudio();
            this.mMediaRecorderRecordingPaused = false;
            this.mMediaRecorderRecording = true;
            int startRecord = this.mRecorder.startRecord(this.mCurrentSpeed, this.mTotalRecordingTime);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startRecordResult onResume: ");
            sb.append(startRecord);
            Log.d(str, sb.toString());
            updateRecordingTime();
        }
    }

    public void onRecordReverse() {
        Log.d(TAG, "delete last frag !!!");
        if (this.mRecorder != null) {
            if (this.mRecordSegmentTimeInfo.size() > 0) {
                List list = this.mRecordSegmentTimeInfo;
                list.remove(list.size() - 1);
                this.mTotalRecordingTime = TimeSpeedModel.calculateRealTime(this.mRecordSegmentTimeInfo);
                updateRecordingTime(Math.min(((long) this.mMaxVideoDurationInMs) - this.mTotalRecordingTime, 15000), 1.0f);
            }
            this.mRecorder.deleteLastFrag();
        }
    }

    public void onRecordStart() {
        if (this.mRecorder != null) {
            this.mAudioController.silenceAudio();
            DataRepository.dataItemLive().setRecordSegmentTimeInfo(this.mRecordSegmentTimeInfo);
            String str = this.mBGMPath;
            if (str != null) {
                this.mRecorder.setRecordBGM(str, 0, 1);
            }
            int startRecord = this.mRecorder.startRecord(this.mCurrentSpeed, this.mTotalRecordingTime);
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startRecordResult onStart: ");
            sb.append(startRecord);
            Log.d(str2, sb.toString());
            this.mMediaRecorderRecordingPaused = false;
            this.mMediaRecorderRecording = true;
            updateRecordingTime();
        }
    }

    public void onRecordStop() {
        if (this.mRecorder != null) {
            this.mAudioController.restoreAudio();
            onRecordPause();
            DataRepository.dataItemLive().setRecordSegmentTimeInfo(null);
            this.mTotalRecordingTime = 0;
            this.mRecordSegmentTimeInfo.clear();
            this.mMediaRecorderRecordingPaused = false;
            this.mMediaRecorderRecording = false;
            this.mRecorder.clearEnv();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0086, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this.mRecorderLock) {
            if (this.mRecorder != null) {
                double timestamp = getTimestamp(sensorEvent);
                int type = sensorEvent.sensor.getType();
                if (type == 1) {
                    this.mRecorder.slamProcessIngestAcc((double) sensorEvent.values[0], (double) sensorEvent.values[1], (double) sensorEvent.values[2], timestamp);
                } else if (type == 4) {
                    this.mRecorder.slamProcessIngestGyr((double) sensorEvent.values[0], (double) sensorEvent.values[1], (double) sensorEvent.values[2], timestamp);
                } else if (type == 9) {
                    this.mRecorder.slamProcessIngestGra((double) sensorEvent.values[0], (double) sensorEvent.values[1], (double) sensorEvent.values[2], timestamp);
                } else if (type == 15) {
                    float[] fArr = new float[9];
                    SensorManager.getRotationMatrixFromVector(fArr, sensorEvent.values);
                    double[] dArr = new double[9];
                    for (int i = 0; i < fArr.length; i++) {
                        dArr[i] = (double) fArr[i];
                    }
                    this.mRecorder.slamProcessIngestOri(dArr, timestamp);
                }
            }
        }
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(201, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(232, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(243, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(244, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(211, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(245, this);
        this.mStickerImpl.registerProtocol();
    }

    public void release() {
        this.mReleased = true;
        synchronized (this.mRecorderLock) {
            Log.d(TAG, "release");
            if (this.mRecorder != null) {
                int stopRecord = this.mRecorder.stopRecord();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("stopRecordResult onRelease: ");
                sb.append(stopRecord);
                Log.d(str, sb.toString());
                int stopPreview = this.mRecorder.stopPreview();
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("stopPreviewResult onRelease: ");
                sb2.append(stopPreview);
                Log.d(str2, sb2.toString());
                this.mRecorder.setNativeInitListener(null);
                this.mRecorder.setRenderCallback(null);
                this.mRecorder.destroy();
                this.mInputSurfaceReady = false;
                this.mInputSurfaceTexture.release();
                this.mRecorder = null;
                this.mHandler.removeCallbacksAndMessages(null);
                this.mHandler = null;
            }
        }
    }

    public void setAudioPath(String str) {
        TERecorder tERecorder = this.mRecorder;
        if (tERecorder != null) {
            this.mBGMPath = str;
            tERecorder.setRecordBGM(str, 0, 1);
        }
    }

    public void setBeautify(boolean z, float f) {
        TERecorder tERecorder = this.mRecorder;
        if (tERecorder != null && this.mTTNativeIsInit) {
            if (z) {
                tERecorder.setBeautyFace(3, FileUtils.BEAUTY_12_DIR);
                this.mRecorder.setBeautyFaceIntensity(f, 0.2f);
                return;
            }
            tERecorder.setBeautyFace(0, "");
            this.mRecorder.setBeautyFaceIntensity(0.0f, 0.0f);
        }
    }

    public void setBeautyFaceReshape(boolean z, float f, float f2) {
        TERecorder tERecorder = this.mRecorder;
        if (tERecorder != null && this.mTTNativeIsInit) {
            if (z) {
                tERecorder.setFaceReshape(FileUtils.RESHAPE_DIR_NAME, f, f2);
            } else {
                tERecorder.setFaceReshape("", 0.0f, 0.0f);
            }
        }
    }

    public void setEffectAudio(boolean z) {
        TERecorder tERecorder = this.mRecorder;
        if (tERecorder != null && this.mTTNativeIsInit) {
            tERecorder.pauseEffectAudio(!z);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setFilter(boolean z, String str) {
        TERecorder tERecorder;
        String str2;
        synchronized (this.mRecorderLock) {
            if (this.mRecorder != null) {
                if (this.mTTNativeIsInit) {
                    if (!z || TextUtils.isEmpty(str)) {
                        tERecorder = this.mRecorder;
                        str2 = "";
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append(FileUtils.FILTER_DIR);
                        sb.append(str);
                        sb.append(File.separator);
                        str2 = sb.toString();
                        tERecorder = this.mRecorder;
                    }
                    tERecorder.setFilter(str2, 1.0f);
                }
            }
        }
    }

    public void setRecordSpeed(int i) {
        this.mCurrentSpeed = this.SPEEDS[i];
    }

    public void startPreview(Surface surface) {
        if (this.mInputSurfaceReady) {
            Log.d(TAG, "startPreview return");
            return;
        }
        Log.d(TAG, "startPreview");
        AnonymousClass1 r0 = new IRenderCallback() {
            public Texture onCreateTexture() {
                if (LiveConfigChangeTTImpl.this.mReleased) {
                    return null;
                }
                Log.d(LiveConfigChangeTTImpl.TAG, "TTRenderCallback, onCreateTexture");
                LiveConfigChangeTTImpl.this.mInputSurfaceReady = true;
                ExtTexture extTexture = new ExtTexture();
                extTexture.onBind(null);
                LiveConfigChangeTTImpl.this.mInputSurfaceTexture.attachToGLContext(extTexture.getId());
                return new Texture(extTexture.getId(), LiveConfigChangeTTImpl.this.mInputSurfaceTexture);
            }

            public boolean onDestroy() {
                Log.d(LiveConfigChangeTTImpl.TAG, "TTRenderCallback onDestroy");
                try {
                    LiveConfigChangeTTImpl.this.mInputSurfaceTexture.detachFromGLContext();
                } catch (Exception e) {
                    String access$100 = LiveConfigChangeTTImpl.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("detachFromGLContext exception ");
                    sb.append(e.getMessage());
                    Log.e(access$100, sb.toString());
                }
                LiveConfigChangeTTImpl.this.mInputSurfaceReady = false;
                return false;
            }

            public void onTextureCreated(Texture texture) {
            }
        };
        this.mRecorder.setNativeInitListener(new VERecorderNativeInitListener() {
            public void onHardEncoderInit(boolean z) {
            }

            public void onNativeInit(int i, String str) {
                TERecorder access$400 = LiveConfigChangeTTImpl.this.mRecorder;
                if (access$400 != null) {
                    LiveConfigChangeTTImpl.this.mTTNativeIsInit = true;
                    int slamDeviceConfig = access$400.slamDeviceConfig(true, true, true, true);
                    String access$100 = LiveConfigChangeTTImpl.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("slam config result = ");
                    sb.append(slamDeviceConfig);
                    Log.e(access$100, sb.toString());
                    access$400.setUseLargeMattingModel(true);
                    if (LiveConfigChangeTTImpl.this.mStickerPath != null) {
                        access$400.switchEffect(LiveConfigChangeTTImpl.this.mStickerPath);
                    }
                    LiveFilterItem findLiveFilter = EffectController.getInstance().findLiveFilter(LiveConfigChangeTTImpl.this.mContext, DataRepository.dataItemLive().getLiveFilter());
                    LiveConfigChangeTTImpl.this.setFilter(true, findLiveFilter != null ? findLiveFilter.directoryName : "");
                    LiveConfigChangeTTImpl.this.updateBeauty();
                }
            }
        });
        this.mRecorder.setRenderCallback(r0);
        int startPreview = this.mRecorder.startPreview(surface);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("previewResult: ");
        sb.append(startPreview);
        Log.d(str, sb.toString());
        this.mRecorder.addSlamDetectListener(new OnSlamDetectListener() {
            public void onSlam(boolean z) {
                boolean z2;
                SensorStateManager sensorStateManager;
                if (z) {
                    Log.d(LiveConfigChangeTTImpl.TAG, "onSlam open, register tt ar sensor");
                    sensorStateManager = ((Camera) LiveConfigChangeTTImpl.this.mActivity).getSensorStateManager();
                    z2 = true;
                } else {
                    Log.d(LiveConfigChangeTTImpl.TAG, "onSlam close, unregister tt ar sensor");
                    sensorStateManager = ((Camera) LiveConfigChangeTTImpl.this.mActivity).getSensorStateManager();
                    z2 = false;
                }
                sensorStateManager.setTTARSensorEnabled(z2);
            }
        });
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(245, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(211, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(244, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(243, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(232, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(201, this);
        this.mStickerImpl.unRegisterProtocol();
        release();
    }

    public void updateRecordingTime() {
        if (this.mMediaRecorderRecording) {
            CountDownTimer countDownTimer = this.mCountDownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            float f = (float) (((long) this.mMaxVideoDurationInMs) - this.mTotalRecordingTime);
            float f2 = this.mCurrentSpeed;
            long j = (long) (f2 * 1000.0f);
            AnonymousClass4 r1 = new CountDownTimer((long) (f * f2), j) {
                public void onFinish() {
                    if (LiveConfigChangeTTImpl.this.mMediaRecorderRecording && LiveConfigChangeTTImpl.this.mActivity != null && LiveConfigChangeTTImpl.this.mActivity.getCurrentModule() != null && (LiveConfigChangeTTImpl.this.mActivity.getCurrentModule() instanceof LiveModule)) {
                        ((LiveModule) LiveConfigChangeTTImpl.this.mActivity.getCurrentModule()).stopVideoRecording(false);
                    }
                }

                public void onTick(long j) {
                    if (LiveConfigChangeTTImpl.this.mMediaRecorderRecording) {
                        LiveConfigChangeTTImpl liveConfigChangeTTImpl = LiveConfigChangeTTImpl.this;
                        liveConfigChangeTTImpl.updateRecordingTime(j, liveConfigChangeTTImpl.mCurrentSpeed);
                    }
                }
            };
            this.mCountDownTimer = r1;
            this.mStartTime = System.currentTimeMillis();
            this.mCountDownTimer.start();
        }
    }

    public void updateRotation(float f, float f2, float f3) {
        TERecorder tERecorder = this.mRecorder;
        if (tERecorder != null) {
            tERecorder.updateRotation(f, f2, f3);
        }
    }
}
