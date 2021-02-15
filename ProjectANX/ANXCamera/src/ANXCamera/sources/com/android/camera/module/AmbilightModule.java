package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import com.android.camera.AutoLockManager;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.ImageHelper;
import com.android.camera.LocationManager;
import com.android.camera.PictureSizeManager;
import com.android.camera.R;
import com.android.camera.SensorStateManager.SensorStateAdapter;
import com.android.camera.SensorStateManager.SensorStateListener;
import com.android.camera.SettingUiState;
import com.android.camera.SurfaceTextureScreenNail.ExternalFrameProcessor;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.ambilight.AmbilightEngine;
import com.android.camera.ambilight.AmbilightEngine.AmbilightSceneMode;
import com.android.camera.ambilight.AmbilightRoi;
import com.android.camera.constant.DurationConstant;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentConfigAmbilight;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.MiYuvImage;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.camera.effect.draw_mode.DrawYuvAttribute;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.top.FragmentTopAlert;
import com.android.camera.log.Log;
import com.android.camera.module.encoder.LiveMediaRecorder;
import com.android.camera.module.encoder.LiveMediaRecorder.EncoderListener;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.camera2.FocusManager2;
import com.android.camera.module.loader.camera2.FocusManager2.Listener;
import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.AmbilightProtocol;
import com.android.camera.protocol.ModeProtocol.AmbilightSelector;
import com.android.camera.protocol.ModeProtocol.AutoHibernation;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.SpeechShutterDetect;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.scene.FunctionMiAlgoASDEngine;
import com.android.camera.scene.MiAlgoAsdSceneProfile;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.sticker.FileUtils;
import com.android.camera.storage.MediaProviderUtil;
import com.android.camera.storage.Storage;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.Camera2Proxy.FocusCallback;
import com.android.camera2.Camera2Proxy.PictureCallbackWrapper;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTagHelper;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene.ASDScene;
import com.android.gallery3d.exif.ExifHelper;
import com.android.gallery3d.ui.GLCanvasImpl;
import com.arcsoft.camera.utils.O00000o0;
import com.xiaomi.camera.core.BoostFrameworkImpl;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.util.SystemProperties;
import com.xiaomi.stat.d;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import miui.text.ExtraTextUtils;

public class AmbilightModule extends BaseModule implements Listener, FocusCallback, CameraPreviewCallback, CameraAction, AmbilightProtocol, ExternalFrameProcessor {
    public static final String ALGO_XML_FILENAME = "ambilight_parameter.xml";
    public static final String ALGO_XML_FOLDER_NAME = "ambilight";
    private static final int COUNTDOWN_INTERVAL = 1;
    private static final boolean DEBUG = true;
    private static final int MAX_DROP_COUNT = 5;
    private static final int MAX_IMAGES_COUNT = 6;
    private static final int MIN_RECORD_TIME = 1800000;
    private static final int MIN_SHOOTING_TIME = 600;
    public static final float STAR_TRACK_VIDEO_SPEED = 0.0027777778f;
    public static final String TAG = "AmbilightModule";
    public static boolean mSupportAutoAe;
    private boolean m3ALocked;
    /* access modifiers changed from: private */
    public final AtomicInteger mAcquiredCount = new AtomicInteger(0);
    /* access modifiers changed from: private */
    public AmbilightEngine mAmbilightEngine;
    /* access modifiers changed from: private */
    public int mAmbilightHeight;
    private AmbilightRoi mAmbilightRoi = null;
    /* access modifiers changed from: private */
    public int mAmbilightWidth;
    private ASDScene[] mAsdScenes;
    /* access modifiers changed from: private */
    public BackgroundHandler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    /* access modifiers changed from: private */
    public CaptureResult mCaptureResult;
    private long mCaptureStartTime;
    private long mCaptureStopTime;
    private ComponentConfigAmbilight mComponentConfigAmbilight;
    private CountDownTimer mCountDownTimer;
    /* access modifiers changed from: private */
    public Disposable mCountdownDisposable;
    private BoostFrameworkImpl mCpuBoost;
    private Rect mCropRegion;
    /* access modifiers changed from: private */
    public int mDropCount = 0;
    /* access modifiers changed from: private */
    public int mDropFrameCount = 0;
    /* access modifiers changed from: private */
    public long mExposureTime = 0;
    private String mFileNameTemplate;
    private float mFocusDistance;
    private FocusManager2 mFocusManager;
    private MiYuvImage mImage = null;
    /* access modifiers changed from: private */
    public byte[] mImagedata = null;
    /* access modifiers changed from: private */
    public boolean mInDebugMode = false;
    private volatile boolean mIsPrepareSaveTask = false;
    /* access modifiers changed from: private */
    public boolean mIsPrepared;
    private boolean mIsRegisterSensorListener = false;
    /* access modifiers changed from: private */
    public volatile boolean mIsShooting = false;
    private int mIso = 0;
    private int mJpegRotation;
    private LiveMediaRecorder mLiveMediaRecorder;
    private final EncoderListener mMediaEncoderListener = new EncoderListener(this);
    private Disposable mMetaDataDisposable;
    /* access modifiers changed from: private */
    public FlowableEmitter mMetaDataFlowableEmitter;
    private ArrayList mPendingSaveTaskList = new ArrayList();
    /* access modifiers changed from: private */
    public ByteBuffer mPhotoBuffer = null;
    private int mPreviewHeight;
    private int mPreviewWidth;
    /* access modifiers changed from: private */
    public int mReceivedPicturesCount;
    private long mRecordingStartTime;
    private String mRecordingTime = "";
    /* access modifiers changed from: private */
    public final AtomicInteger mReleaseedCount = new AtomicInteger(0);
    private final Object mRenderLock = new Object();
    private long mRequestStartTime;
    private SaveOutputImageTask mSaveOutputImageTask;
    /* access modifiers changed from: private */
    @AmbilightSceneMode
    public int mSceneMode = 4;
    private SensorStateListener mSensorStateListener = new SensorStateAdapter() {
        public boolean isWorking() {
            return AmbilightModule.this.isAlive() && AmbilightModule.this.getCameraState() != 0;
        }

        public void onDeviceOrientationChanged(float f, boolean z) {
            AmbilightModule ambilightModule = AmbilightModule.this;
            if (z) {
                f = (float) ambilightModule.mOrientation;
            }
            ambilightModule.mDeviceRotation = f;
            if (AmbilightModule.this.getCameraState() != 3 || CameraSettings.isGradienterOn()) {
                EffectController instance = EffectController.getInstance();
                AmbilightModule ambilightModule2 = AmbilightModule.this;
                instance.setDeviceRotation(z, Util.getShootRotation(ambilightModule2.mActivity, ambilightModule2.mDeviceRotation));
            }
        }
    };
    private long mShootingStartTime;
    /* access modifiers changed from: private */
    public TopAlert mTopAlert;
    private float[] mTransform = new float[16];
    private int mTranslateY;
    private int mTripodMode = 0;
    private ByteBuffer mUBuffer;
    private ByteBuffer mVBuffer;
    private Size mVideoSize = new Size(1440, 1920);
    private ByteBuffer mYBuffer = null;
    private DrawYuvAttribute mYuvAttribute;
    private DrawYuvAttribute mYuvAttributeForVideo;
    private float mZoom;
    private long prevOutputPTSUs = 0;

    public class BackgroundHandler extends Handler {
        private static final int MSG_NEW_IMAGE_ARRIVE = 0;
        private static final int MSG_START_CAPTURE = 2;
        private static final int MSG_STOP_CAPTURE = 1;
        private boolean mCaptureStarted = false;
        private WeakReference mModule;

        BackgroundHandler(Looper looper, AmbilightModule ambilightModule) {
            super(looper);
            this.mModule = new WeakReference(ambilightModule);
        }

        private void processImage(AmbilightModule ambilightModule, ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3) {
            ambilightModule.mAmbilightEngine.prepareData(byteBuffer, byteBuffer2, byteBuffer3);
            if (ambilightModule.mAmbilightEngine.frameProc() == 0) {
                ambilightModule.mHandler.obtainMessage(100).sendToTarget();
            }
        }

        public void handleMessage(@NonNull Message message) {
            int i = message.what;
            if (i == 0) {
                AmbilightModule ambilightModule = (AmbilightModule) this.mModule.get();
                Image image = (Image) message.obj;
                if (!this.mCaptureStarted || ambilightModule == null) {
                    image.close();
                    return;
                }
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                ByteBuffer buffer2 = C0124O00000oO.Oo000OO() ? image.getPlanes()[1].getBuffer() : null;
                if (image.getTimestamp() == 1) {
                    String str = AmbilightModule.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("stride: ");
                    sb.append(image.getPlanes()[0].getRowStride());
                    Log.d(str, sb.toString());
                    ambilightModule.mAmbilightEngine.initData(image.getPlanes()[0].getRowStride());
                }
                processImage(ambilightModule, buffer, buffer2, null);
                image.close();
                ambilightModule.mReleaseedCount.incrementAndGet();
                int i2 = ambilightModule.mAcquiredCount.get() - ambilightModule.mReleaseedCount.get();
                String str2 = AmbilightModule.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Wait process count: << ");
                sb2.append(i2);
                Log.d(str2, sb2.toString());
            } else if (i == 1) {
                this.mCaptureStarted = false;
                removeCallbacksAndMessages(Integer.valueOf(0));
                if (message.obj != null) {
                    AmbilightModule ambilightModule2 = (AmbilightModule) this.mModule.get();
                    if (ambilightModule2 != null) {
                        ambilightModule2.mHandler.obtainMessage(106, ambilightModule2.mPhotoBuffer.array()).sendToTarget();
                    }
                    AmbilightEngine ambilightEngine = (AmbilightEngine) message.obj;
                    int postProc = ambilightEngine.postProc();
                    Log.d(AmbilightModule.TAG, "on PostProc done.");
                    if (!(postProc != 0 || ambilightModule2 == null || ambilightModule2.mPhotoBuffer == null)) {
                        ambilightModule2.mHandler.obtainMessage(101, ambilightModule2.mPhotoBuffer.array()).sendToTarget();
                    }
                    ambilightEngine.destroy();
                }
            } else if (i == 2) {
                this.mCaptureStarted = true;
            }
        }
    }

    class MainHandler extends Handler {
        public static final int MSG_CHECK_REPARE = 108;
        public static final int MSG_FRAME_PROC_DONE = 100;
        public static final int MSG_MAGIC_STAR_DONE = 102;
        public static final int MSG_ON_PAUSE = 105;
        public static final int MSG_POST_PROC_DONE = 101;
        public static final int MSG_POST_PROC_START = 106;
        public static final int MSG_RECEIVE_EXPOSURE_TIME = 107;
        public static final int MSG_TAKE_SHOT = 104;
        public static final int MSG_UPDATE_DEBUG_INFO = 103;
        private WeakReference mModule;
        private boolean mReceivedImage = false;
        private boolean showPreview = false;

        public MainHandler(Looper looper, AmbilightModule ambilightModule) {
            super(looper);
            this.mModule = new WeakReference(ambilightModule);
        }

        private void handleDoneMsg(Message message) {
            byte[] bArr = (byte[]) message.obj;
            AmbilightModule ambilightModule = (AmbilightModule) this.mModule.get();
            if (ambilightModule == null) {
                Log.e(AmbilightModule.TAG, "handleDoneMsg module is null!");
                return;
            }
            if (bArr != null) {
                ambilightModule.startSaveTask(bArr, ambilightModule.mAmbilightWidth, ambilightModule.mAmbilightHeight, ambilightModule.mCaptureResult);
            } else {
                ambilightModule.onSaveFinish();
            }
            this.showPreview = false;
            ambilightModule.mActivity.getCameraScreenNail().setExternalFrameProcessor(null);
        }

        public void handleMessage(Message message) {
            AmbilightModule ambilightModule = (AmbilightModule) this.mModule.get();
            int i = message.what;
            if (i != 9) {
                if (i != 42) {
                    if (i == 65) {
                        sendEmptyMessageDelayed(66, 5000);
                        ambilightModule.showAutoHibernationTip();
                    } else if (i != 66) {
                        switch (i) {
                            case 100:
                                if (ambilightModule != null && ambilightModule.mIsShooting) {
                                    ambilightModule.handleFrameProcessDone();
                                    if (!this.showPreview) {
                                        ambilightModule.mActivity.getCameraScreenNail().setExternalFrameProcessor(ambilightModule);
                                        ambilightModule.onAmbilightPreviewAvailable();
                                        this.showPreview = true;
                                        return;
                                    }
                                    return;
                                }
                                return;
                            case 101:
                                break;
                            case 102:
                                if (ambilightModule != null) {
                                    ambilightModule.playCameraSound(0);
                                    ambilightModule.stopAmbilightShooting();
                                }
                                this.mReceivedImage = true;
                                break;
                            case 103:
                                AmbilightSelector ambilightSelector = (AmbilightSelector) ModeCoordinatorImpl.getInstance().getAttachProtocol(421);
                                if (ambilightSelector != null) {
                                    ambilightSelector.updateDebugInfo((String) message.obj);
                                    return;
                                }
                                return;
                            case 104:
                                if (ambilightModule != null && !ambilightModule.mIsShooting) {
                                    ambilightModule.takeShot();
                                    return;
                                }
                                return;
                            case 105:
                                if (ambilightModule != null) {
                                    ambilightModule.closeCamera();
                                    ambilightModule.clearBuffer();
                                    return;
                                }
                                return;
                            case 106:
                                if (ambilightModule != null && isInRendering()) {
                                    ambilightModule.playCameraSound(0);
                                    return;
                                }
                                return;
                            case 107:
                            case 108:
                                if (ambilightModule != null && ambilightModule.mTopAlert != null) {
                                    ambilightModule.mExposureTime = ((Long) message.obj).longValue();
                                    RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                                    if (recordState != null && !ambilightModule.mIsPrepared && !this.mReceivedImage) {
                                        ambilightModule.mIsPrepared = true;
                                        recordState.onPrepare();
                                        recordState.onStart();
                                        return;
                                    }
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                        handleDoneMsg(message);
                    } else {
                        ambilightModule.enterAutoHibernation();
                    }
                } else if (ambilightModule != null) {
                    ambilightModule.updateRecordingTime();
                }
            } else if (ambilightModule != null) {
                ambilightModule.mMainProtocol.initializeFocusView(ambilightModule);
            }
        }

        public boolean isInRendering() {
            return this.showPreview;
        }
    }

    public class PictureCallback extends PictureCallbackWrapper {
        private WeakReference ambilightModule;

        PictureCallback(AmbilightModule ambilightModule2) {
            this.ambilightModule = new WeakReference(ambilightModule2);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0115, code lost:
            if (com.android.camera.module.AmbilightModule.access$1300(r5).sendMessage(com.android.camera.module.AmbilightModule.access$1300(r5).obtainMessage(0, r6)) == false) goto L_0x0079;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void handleImage(Image image, TotalCaptureResult totalCaptureResult) {
            String str = AmbilightModule.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPictureTaken>>image=");
            sb.append(image);
            Log.k(2, str, sb.toString());
            AmbilightModule ambilightModule2 = (AmbilightModule) this.ambilightModule.get();
            if (image != null) {
                if (ambilightModule2 == null) {
                    image.close();
                    return;
                }
                if (totalCaptureResult != null && ambilightModule2.mSceneMode == 3) {
                    Float f = (Float) totalCaptureResult.get(CaptureResult.LENS_FOCUS_DISTANCE);
                    if (f != null && ((double) f.floatValue()) > 0.15d && ambilightModule2.mDropFrameCount < 5) {
                        String str2 = AmbilightModule.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Focus does not reach infinity : ");
                        sb2.append(f);
                        Log.d(str2, sb2.toString());
                        ambilightModule2.mDropFrameCount = ambilightModule2.mDropFrameCount + 1;
                        image.close();
                        return;
                    }
                }
                ambilightModule2.mReceivedPicturesCount = ambilightModule2.mReceivedPicturesCount + 1;
                ambilightModule2.mCaptureResult = totalCaptureResult;
                if (ambilightModule2.mIsShooting) {
                    int i = ambilightModule2.mAcquiredCount.get() - ambilightModule2.mReleaseedCount.get();
                    if (i > 4) {
                        image.close();
                        ambilightModule2.mDropCount = ambilightModule2.mDropCount + 1;
                        if (ambilightModule2.mInDebugMode) {
                            Handler handler = ambilightModule2.mHandler;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("L: ");
                            sb3.append(ambilightModule2.mDropCount);
                            handler.obtainMessage(103, sb3.toString()).sendToTarget();
                        }
                        String str3 = AmbilightModule.TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Proc is busy, drop this frame, All drop count:");
                        sb4.append(ambilightModule2.mDropCount);
                        Log.e(str3, sb4.toString());
                    } else {
                        ambilightModule2.mAcquiredCount.incrementAndGet();
                        String str4 = AmbilightModule.TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("Wait process count: >> ");
                        sb5.append(i);
                        Log.d(str4, sb5.toString());
                        if (ambilightModule2.mBackgroundHandler != null) {
                            image.setTimestamp((long) ambilightModule2.mReceivedPicturesCount);
                        }
                    }
                }
                image.close();
            }
        }

        public void onPictureTakenFinished(boolean z) {
            String str = AmbilightModule.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPictureBurstFinished success = ");
            sb.append(z);
            Log.k(3, str, sb.toString());
        }

        public boolean onPictureTakenImageConsumed(Image image, TotalCaptureResult totalCaptureResult) {
            handleImage(image, totalCaptureResult);
            return true;
        }
    }

    public class SaveOutputImageTask extends AsyncTask {
        private WeakReference mActivityRef;
        private SaveStateCallback mCallback;
        private CaptureResult mCaptureResult;
        private final long mCaptureTime;
        private final String mFileName;
        private int mHeight;
        private byte[] mNv21Data;
        private int mOrientation;
        private int mWidth;

        public SaveOutputImageTask(@Nullable Camera camera, String str, byte[] bArr, int i, int i2, int i3, long j, CaptureResult captureResult, SaveStateCallback saveStateCallback) {
            this.mFileName = str;
            this.mNv21Data = bArr;
            this.mWidth = i;
            this.mHeight = i2;
            this.mOrientation = i3;
            this.mCallback = saveStateCallback;
            this.mCaptureResult = captureResult;
            this.mActivityRef = new WeakReference(camera);
            this.mCaptureTime = j;
        }

        private void addImageAsApplication(String str, String str2, byte[] bArr, int i, int i2, int i3, long j) {
            Uri uri;
            Location location;
            String str3 = str;
            long currentTimeMillis = System.currentTimeMillis();
            Location currentLocation = LocationManager.instance().getCurrentLocation();
            if (bArr != null) {
                uri = Storage.addImage(CameraAppImpl.getAndroidContext(), str2, currentTimeMillis, currentLocation, i3, bArr, false, i, i2, false, false, false, true, false, "", null);
            } else {
                CaptureResult captureResult = this.mCaptureResult;
                if (captureResult != null) {
                    location = currentLocation;
                    Util.appendCaptureResultToExif(str, i, i2, i3, currentTimeMillis, currentLocation, j, captureResult);
                    int i4 = i3;
                } else {
                    location = currentLocation;
                    ExifHelper.writeExifByFilePath(str3, i3, location, currentTimeMillis);
                }
                uri = Storage.addImageForGroupOrPanorama(CameraAppImpl.getAndroidContext(), str, i3, currentTimeMillis, location, i, i2);
            }
            if (uri == null) {
                String str4 = AmbilightModule.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("insert MediaProvider failed, attempt to find uri by path, ");
                sb.append(str3);
                Log.w(str4, sb.toString());
                uri = MediaProviderUtil.getContentUriFromPath(CameraAppImpl.getAndroidContext(), str3);
            }
            String str5 = AmbilightModule.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("addImageAsApplication uri = ");
            sb2.append(uri);
            sb2.append(", path = ");
            sb2.append(str3);
            Log.d(str5, sb2.toString());
            Camera camera = (Camera) this.mActivityRef.get();
            if (camera != null && uri != null) {
                camera.onNewUriArrived(uri, str2);
                Thumbnail createThumbnailFromUri = Thumbnail.createThumbnailFromUri(camera, uri, false);
                String str6 = AmbilightModule.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("addImageAsApplication Thumbnail = ");
                sb3.append(createThumbnailFromUri);
                Log.d(str6, sb3.toString());
                Util.broadcastNewPicture(camera, uri);
                camera.getThumbnailUpdater().setThumbnail(createThumbnailFromUri, true, false);
            }
        }

        /* access modifiers changed from: protected */
        public Integer doInBackground(Void... voidArr) {
            String str;
            byte[] encodeNv21ToJpeg = ImageHelper.encodeNv21ToJpeg(this.mNv21Data, this.mWidth, this.mHeight, CameraSettings.getEncodingQuality(false).toInteger(false));
            this.mNv21Data = null;
            if (encodeNv21ToJpeg == null) {
                Log.w(AmbilightModule.TAG, "jpegData is null, can't save");
                return null;
            }
            String generateFilepath4Image = Storage.generateFilepath4Image(this.mFileName, false);
            if (Storage.isUseDocumentMode()) {
                str = this.mFileName;
            } else {
                O00000o0.O000000o(generateFilepath4Image, encodeNv21ToJpeg);
                str = this.mFileName;
                encodeNv21ToJpeg = null;
            }
            addImageAsApplication(generateFilepath4Image, str, encodeNv21ToJpeg, this.mWidth, this.mHeight, this.mOrientation, this.mCaptureTime);
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Integer num) {
            super.onPostExecute(num);
            SaveStateCallback saveStateCallback = this.mCallback;
            if (saveStateCallback != null) {
                saveStateCallback.onSaveCompleted();
            }
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            Log.w(AmbilightModule.TAG, "onPreExecute");
        }
    }

    public interface SaveStateCallback {
        void onSaveCompleted();
    }

    final class SaveVideoTask {
        public ContentValues contentValues;
        public String videoPath;

        public SaveVideoTask(String str, ContentValues contentValues2) {
            this.videoPath = str;
            this.contentValues = contentValues2;
        }
    }

    private void ambilightEngineInit() {
        startCpuBoost(3);
        this.mAmbilightEngine.init(this.mSceneMode, this.mAmbilightWidth, this.mAmbilightHeight, this.mTripodMode);
        if (this.mPhotoBuffer == null) {
            this.mPhotoBuffer = ByteBuffer.allocateDirect(((this.mAmbilightWidth * this.mAmbilightHeight) * 3) / 2);
        }
        this.mAmbilightEngine.prepare(this.mPhotoBuffer, this.mPhotoBuffer.arrayOffset());
        if (this.mZoom != 1.0f) {
            AmbilightEngine ambilightEngine = this.mAmbilightEngine;
            Rect rect = this.mCropRegion;
            ambilightEngine.setZoomRoi(rect.left, rect.top, rect.right, rect.bottom);
        }
        this.mPreviewWidth = this.mAmbilightEngine.getPreviewWidth();
        this.mPreviewHeight = this.mAmbilightEngine.getPreviewHeight();
        Log.d(TAG, "update preview size [%d, %d]", Integer.valueOf(this.mPreviewWidth), Integer.valueOf(this.mPreviewHeight));
        if (this.mSceneMode == 2) {
            int i = this.mJpegRotation;
            if (i == 0 || i == 180) {
                this.mAmbilightEngine.setFlipMode(1);
            } else {
                this.mAmbilightEngine.setFlipMode(2);
            }
        }
        updateBuffers(this.mPreviewWidth * this.mPreviewHeight);
        this.mAcquiredCount.set(0);
        this.mReleaseedCount.set(0);
        this.mReceivedPicturesCount = 0;
    }

    private boolean checkShutterCondition() {
        if (isBlockSnap() || isIgnoreTouchEvent()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("checkShutterCondition: blockSnap=");
            sb.append(isBlockSnap());
            sb.append(" ignoreTouchEvent=");
            sb.append(isIgnoreTouchEvent());
            Log.w(str, sb.toString());
            return false;
        } else if (Storage.isLowStorageAtLastPoint()) {
            Log.w(TAG, "checkShutterCondition: low storage");
            return false;
        } else if (!isFrontCamera() || !this.mActivity.isScreenSlideOff()) {
            BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
            if (backStack != null) {
                backStack.handleBackStackFromShutter();
            }
            return true;
        } else {
            Log.w(TAG, "checkShutterCondition: screen is slide off");
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void clearBuffer() {
        this.mYBuffer = null;
        this.mUBuffer = null;
        this.mVBuffer = null;
        this.mPhotoBuffer = null;
        MiYuvImage miYuvImage = this.mImage;
        if (miYuvImage != null) {
            miYuvImage.updateData(null, null, null);
            this.mImage = null;
        }
    }

    private void doLaterReleaseIfNeed() {
        Camera camera = this.mActivity;
        if (camera != null && camera.isActivityPaused()) {
            this.mActivity.pauseIfNotRecording();
            this.mActivity.releaseAll(true, true);
        }
    }

    private File getAlgoXmlFile() {
        String str;
        File filesDir = CameraAppImpl.getAndroidContext().getFilesDir();
        if (filesDir != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(filesDir.getAbsolutePath());
            sb.append(File.separator);
            sb.append("ambilight");
            str = sb.toString();
        } else {
            str = null;
        }
        return new File(str, ALGO_XML_FILENAME);
    }

    private void handleFocusStateChange(FocusTask focusTask) {
        String str = focusTask.isFocusing() ? "onAutoFocusMoving start" : null;
        if (Util.sIsDumpLog && str != null) {
            Log.v(TAG, str);
        }
        if (this.m3ALocked) {
            return;
        }
        if (getCameraState() != 3 || focusTask.getFocusTrigger() == 3) {
            this.mFocusManager.onFocusResult(focusTask);
        }
    }

    private void initMetaParser() {
        this.mMetaDataDisposable = Flowable.create(new FlowableOnSubscribe() {
            public void subscribe(FlowableEmitter flowableEmitter) {
                AmbilightModule.this.mMetaDataFlowableEmitter = flowableEmitter;
            }
        }, BackpressureStrategy.DROP).map(new FunctionMiAlgoASDEngine(this)).subscribe();
    }

    private void initializeFocusManager() {
        this.mFocusManager = new FocusManager2(this.mCameraCapabilities, this, isFrontCamera(), this.mActivity.getMainLooper());
        Rect renderRect = this.mActivity.getCameraScreenNail() != null ? this.mActivity.getCameraScreenNail().getRenderRect() : null;
        if (renderRect == null || renderRect.width() <= 0) {
            this.mFocusManager.setRenderSize(Display.getWindowWidth(), Display.getWindowHeight());
            this.mFocusManager.setPreviewSize(Display.getWindowWidth(), Display.getWindowHeight());
            return;
        }
        this.mFocusManager.setRenderSize(this.mActivity.getCameraScreenNail().getRenderWidth(), this.mActivity.getCameraScreenNail().getRenderHeight());
        this.mFocusManager.setPreviewSize(renderRect.width(), renderRect.height());
    }

    private boolean isCannotGotoGallery() {
        return this.mPaused || this.isZooming || getCameraState() == 0 || isInCountDown();
    }

    private boolean isInCountDown() {
        Disposable disposable = this.mCountdownDisposable;
        return disposable != null && !disposable.isDisposed();
    }

    private boolean isProcessingSaveTask() {
        SaveOutputImageTask saveOutputImageTask = this.mSaveOutputImageTask;
        return (saveOutputImageTask == null || saveOutputImageTask.getStatus() == Status.FINISHED) ? false : true;
    }

    private boolean isShootingTooShort() {
        boolean z = true;
        if (!((MainHandler) this.mHandler).isInRendering()) {
            return true;
        }
        if (SystemClock.elapsedRealtime() - this.mShootingStartTime >= 600) {
            z = false;
        }
        return z;
    }

    private boolean needAutoHibernationScene() {
        int i = this.mSceneMode;
        return i == 0 || i == 1 || i == 2 || i == 3;
    }

    /* access modifiers changed from: private */
    public void onAmbilightPreviewAvailable() {
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            recordState.onResume();
        }
    }

    /* access modifiers changed from: private */
    public void onSaveFinish() {
        Log.k(3, TAG, "onSaveFinish E");
        stopCpuBoost();
        updateRecordingTimeStyle(true);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Photo generation cost = ");
        sb.append(System.currentTimeMillis() - this.mCaptureStopTime);
        sb.append(d.H);
        Log.d(str, sb.toString());
        if (isAlive() && this.mCamera2Device != null) {
            enableCameraControls(true);
            if (this.mAeLockSupported) {
                this.mCamera2Device.setAELock(false);
            }
            if (this.mAwbLockSupported) {
                this.mCamera2Device.setAWBLock(false);
            }
            this.mCamera2Device.setFocusMode(4);
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onPostSavingFinish();
            }
            this.mIsPrepareSaveTask = false;
            Log.d(TAG, "onSaveFinish X");
        }
    }

    private void registerSensorListener() {
        if (!this.mIsRegisterSensorListener) {
            this.mIsRegisterSensorListener = true;
            this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
        }
    }

    private void resetParameters() {
        this.mCamera2Device.setExposureTime(0);
        this.mCamera2Device.setExposureCompensation(0);
        this.mCamera2Device.setISO(0);
        int i = this.mSceneMode;
        Integer valueOf = Integer.valueOf(30);
        if (i == 5 || i == 3) {
            this.mCamera2Device.setFpsRange(new Range(Integer.valueOf(0), valueOf));
        } else {
            this.mCamera2Device.setFpsRange(new Range(Integer.valueOf(5), valueOf));
        }
        updateAutoAeParameters(true);
        unlockAEAF();
        this.mCamera2Device.setFocusMode(4);
        this.mCamera2Device.cancelFocus(this.mModuleIndex);
    }

    private boolean shouldHideTimeLabel() {
        int i = this.mSceneMode;
        return i == 4 || (mSupportAutoAe && i == 5);
    }

    private void startAmbilightShooting() {
        float f;
        Camera2Proxy camera2Proxy;
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState == null) {
            Log.w(TAG, "startAmbilightShooting: recordState is null");
            return;
        }
        this.mRequestStartTime = System.currentTimeMillis();
        this.prevOutputPTSUs = 0;
        if (this.mSceneMode == 3) {
            startVideoRecording();
        }
        TopAlert topAlert = this.mTopAlert;
        boolean z = false;
        if (topAlert != null) {
            topAlert.setShow(true);
            this.mTopAlert.clearAllTipsState();
            this.mTopAlert.alertAiDetectTipHint(0, R.string.super_night_toast, -1);
        }
        TopAlert topAlert2 = this.mTopAlert;
        if (topAlert2 != null) {
            topAlert2.setRecordingTimeState(1, shouldHideTimeLabel());
        }
        updateSpeechShutter(false);
        if (this.mInDebugMode) {
            this.mHandler.obtainMessage(103, "").sendToTarget();
        }
        this.mJpegRotation = Util.getJpegRotation(this.mBogusCameraId, this.mOrientation);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startAmbilightShooting mJpegRotation = ");
        sb.append(this.mJpegRotation);
        Log.k(3, str, sb.toString());
        if (this.mSceneMode != 5) {
            ambilightEngineInit();
        }
        this.mIsShooting = true;
        updateExposureParameters();
        updateAutoAeParameters(false);
        this.mCamera2Device.setFocusMode(0);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("focus distance:");
        sb2.append(this.mFocusDistance);
        Log.d(str2, sb2.toString());
        int i = this.mSceneMode;
        if (i == 5 || i == 3) {
            camera2Proxy = this.mCamera2Device;
            f = 0.0f;
        } else {
            camera2Proxy = this.mCamera2Device;
            f = this.mFocusDistance;
        }
        camera2Proxy.setFocusDistance(f);
        this.mCamera2Device.setGpsLocation(LocationManager.instance().getCurrentLocation());
        this.mCamera2Device.setNeedPausePreview(true);
        if (this.mIso != 0 && this.mAwbLockSupported) {
            this.mCamera2Device.setAWBLock(true);
        }
        if (this.mSceneMode == 5) {
            this.mCamera2Device.takePicture(new PictureCallbackWrapper() {
                public void onCaptureProgress(boolean z, boolean z2, boolean z3, boolean z4, CaptureResult captureResult) {
                    super.onCaptureProgress(z, z2, z3, z4, captureResult);
                    if (C0124O00000oO.Oo000OO() && AmbilightModule.mSupportAutoAe) {
                        Long l = (Long) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.AMBILIGHT_AE_EXPOSURE);
                        if (l != null) {
                            String str = AmbilightModule.TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onCaptureProgress: [exposureTime] ");
                            sb.append(l);
                            Log.k(3, str, sb.toString());
                            AmbilightModule.this.mHandler.obtainMessage(107, l).sendToTarget();
                        }
                    }
                }

                public ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4) {
                    Log.k(3, AmbilightModule.TAG, String.format("onCaptureStart pictureSize= %s", new Object[]{cameraSize}));
                    return parallelTaskData;
                }

                public void onPictureTaken(byte[] bArr, CaptureResult captureResult) {
                    Log.k(3, AmbilightModule.TAG, "onPictureTaken");
                    if (AmbilightModule.this.mCaptureResult == null && AmbilightModule.this.mImagedata != null) {
                        AmbilightModule.this.mCaptureResult = captureResult;
                        AmbilightModule ambilightModule = AmbilightModule.this;
                        ambilightModule.mHandler.obtainMessage(102, ambilightModule.mImagedata).sendToTarget();
                    }
                }

                public boolean onPictureTakenImageConsumed(Image image, TotalCaptureResult totalCaptureResult) {
                    if (image != null) {
                        AmbilightModule.this.mImagedata = O00000o0.getDataFromImage(image, 2);
                        image.close();
                        if (totalCaptureResult != null) {
                            AmbilightModule.this.mCaptureResult = totalCaptureResult;
                            AmbilightModule ambilightModule = AmbilightModule.this;
                            ambilightModule.mHandler.obtainMessage(102, ambilightModule.mImagedata).sendToTarget();
                        }
                    }
                    return true;
                }
            }, null);
        } else {
            this.mCamera2Device.setEnableZsl(isZslPreferred());
            this.mCamera2Device.setZoomRatioForCapture(1.0f);
            this.mCamera2Device.setShotType(12);
            if (this.mIso == 0 && (!mSupportAutoAe || this.mSceneMode != 3)) {
                z = true;
            }
            this.mCamera2Device.setMFLockAfAe(z);
            this.mCamera2Device.takePicture(new PictureCallback(this), null);
        }
        if (recordState != null) {
            if (mSupportAutoAe && this.mSceneMode == 5 && this.mExposureTime == 0) {
                Handler handler = this.mHandler;
                handler.sendMessageDelayed(handler.obtainMessage(108, Long.valueOf(30000000000L)), 600);
            } else {
                this.mIsPrepared = true;
                recordState.onPrepare();
                recordState.onStart();
            }
        }
        keepScreenOn();
        AutoLockManager.getInstance(this.mActivity).removeMessage();
        BackgroundHandler backgroundHandler = this.mBackgroundHandler;
        if (backgroundHandler != null) {
            backgroundHandler.obtainMessage(2, this.mAmbilightEngine).sendToTarget();
        }
        if (this.mIsAutoHibernationSupported) {
            keepAutoHibernation();
        }
    }

    private void startBackgroundThread() {
        this.mBackgroundThread = new HandlerThread("AmbilightBackground");
        this.mBackgroundThread.start();
        this.mBackgroundHandler = new BackgroundHandler(this.mBackgroundThread.getLooper(), this);
    }

    private void startCount(final int i, int i2) {
        if (checkShutterCondition()) {
            setTriggerMode(i2);
            tryRemoveCountDownMessage();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startCount: ");
            sb.append(i);
            Log.d(str, sb.toString());
            Observable.interval(1, TimeUnit.SECONDS).take((long) i).observeOn(AndroidSchedulers.mainThread()).subscribe((Observer) new Observer() {
                public /* synthetic */ void O00oO0Oo() {
                    AmbilightModule.this.mTopAlert.hideAlert();
                }

                public void onComplete() {
                    if (AmbilightModule.this.isAlive()) {
                        Camera camera = AmbilightModule.this.mActivity;
                        if (camera != null && !camera.isActivityPaused()) {
                            AmbilightModule.this.onShutterButtonFocus(true, 3);
                            Handler handler = AmbilightModule.this.mHandler;
                            if (handler != null) {
                                handler.obtainMessage(104).sendToTarget();
                            }
                            AmbilightModule.this.onShutterButtonFocus(false, 0);
                        }
                    }
                    AmbilightModule.this.tryRemoveCountDownMessage();
                }

                public void onError(Throwable th) {
                }

                public void onNext(Long l) {
                    int intValue = i - (l.intValue() + 1);
                    if (intValue > 0) {
                        AmbilightModule.this.playCameraSound(5);
                        AmbilightModule.this.mTopAlert.showDelayNumber(intValue);
                    }
                }

                public void onSubscribe(Disposable disposable) {
                    AmbilightModule.this.mCountdownDisposable = disposable;
                    AmbilightModule.this.mMainProtocol.clearFocusView(7);
                    AmbilightModule.this.playCameraSound(7);
                    if (AmbilightModule.this.mTopAlert != null) {
                        AndroidSchedulers.mainThread().scheduleDirect(new O000000o(this), 120, TimeUnit.MILLISECONDS);
                        AmbilightModule.this.mTopAlert.showDelayNumber(i);
                    }
                }
            });
        }
    }

    private void startCpuBoost(int i) {
        if (this.mCpuBoost == null) {
            this.mCpuBoost = new BoostFrameworkImpl();
        }
        this.mCpuBoost.startBoost(0, i);
    }

    private void startPreviewSession() {
        Log.k(3, TAG, "startPreviewSession");
        if (this.mCamera2Device == null) {
            Log.e(TAG, "startPreview: camera has been closed");
            return;
        }
        int i = this.mSceneMode;
        if (i == 5 || i == 3) {
            this.mCamera2Device.setFpsRange(new Range(Integer.valueOf(0), Integer.valueOf(30)));
        }
        this.mCamera2Device.setFocusCallback(this);
        this.mCamera2Device.setDualCamWaterMarkEnable(false);
        this.mCamera2Device.setErrorCallback(this.mErrorCallback);
        this.mCamera2Device.setPreviewSize(this.mPreviewSize);
        this.mCamera2Device.setAlgorithmPreviewSize(this.mPreviewSize);
        this.mCamera2Device.setPictureSize(this.mPictureSize);
        this.mCamera2Device.setPictureMaxImages(6);
        this.mCamera2Device.setPictureFormat(35);
        this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
        this.mCamera2Device.startPreviewSession(new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture()), 0, 0, null, getOperatingMode(), false, this);
    }

    /* access modifiers changed from: private */
    @UiThread
    public void startSaveTask(byte[] bArr, int i, int i2, CaptureResult captureResult) {
        Log.v(TAG, "startSaveTask stitchResult ");
        keepScreenOnAwhile();
        String charSequence = DateFormat.format(this.mFileNameTemplate, System.currentTimeMillis()).toString();
        long j = this.mCaptureStopTime - this.mCaptureStartTime;
        CameraStatUtils.trackAmbilightCapture(this.mSceneMode, j, this.mIsAutoHibernationSupported, this.mEnterAutoHibernationCount);
        if (this.mSceneMode == 5) {
            j = 0;
        }
        byte[] bArr2 = bArr;
        int i3 = i;
        int i4 = i2;
        SaveOutputImageTask saveOutputImageTask = new SaveOutputImageTask(this.mActivity, charSequence, bArr2, i3, i4, this.mJpegRotation, j, captureResult, new O00000o0(this));
        this.mSaveOutputImageTask = saveOutputImageTask;
        this.mSaveOutputImageTask.execute(new Void[0]);
    }

    /* access modifiers changed from: private */
    public void stopAmbilightShooting() {
        String str;
        String str2;
        if (this.mCamera2Device != null) {
            updateSpeechShutter(true);
            this.mCaptureStopTime = System.currentTimeMillis();
            if (!this.mIsShooting) {
                str = TAG;
                str2 = "stopAmbilightShooting return, is not shooting";
            } else {
                TopAlert topAlert = this.mTopAlert;
                if (topAlert != null) {
                    topAlert.setRecordingTimeState(2);
                }
                Log.k(3, TAG, "stopAmbilightShooting");
                CountDownTimer countDownTimer = this.mCountDownTimer;
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if (this.mMediaRecorderRecording) {
                    LiveMediaRecorder liveMediaRecorder = this.mLiveMediaRecorder;
                    if (liveMediaRecorder != null) {
                        liveMediaRecorder.stopRecorder(this.mRecordingStartTime, this.mCaptureStopTime - this.mCaptureStartTime < 1800000);
                    }
                }
                this.mMediaRecorderRecording = false;
                this.mIsPrepareSaveTask = true;
                this.mIsShooting = false;
                this.mCamera2Device.captureAbortBurst();
                resetParameters();
                if (this.mSceneMode != 5) {
                    this.mBackgroundHandler.obtainMessage(1, this.mAmbilightEngine).sendToTarget();
                }
                RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                if (recordState == null) {
                    str = TAG;
                    str2 = "onPreExecute recordState is null";
                } else {
                    recordState.onFinish();
                    recordState.onPostSavingStart();
                    int i = this.mSceneMode;
                    if (i == 2 || i == 3 || i == 0 || i == 5 || i == 4 || i == 1) {
                        this.mActivity.onModeSelected(StartControl.create(187).setViewConfigType(2).setNeedBlurAnimation(true).setNeedReConfigureCamera(true));
                    }
                    if (this.mIsAutoHibernationSupported) {
                        exitAutoHibernation();
                    }
                    return;
                }
            }
            Log.w(str, str2);
        }
    }

    private void stopBackgroundThread() {
        HandlerThread handlerThread = this.mBackgroundThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            try {
                this.mBackgroundThread.join();
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage());
            }
            this.mBackgroundThread = null;
            this.mBackgroundHandler = null;
        }
    }

    private void stopCpuBoost() {
        BoostFrameworkImpl boostFrameworkImpl = this.mCpuBoost;
        if (boostFrameworkImpl != null) {
            boostFrameworkImpl.stopBoost();
            this.mCpuBoost = null;
        }
    }

    /* access modifiers changed from: private */
    public void takeShot() {
        if (this.mCamera2Device != null) {
            this.mActivity.getScreenHint().updateHint();
            if (Storage.isLowStorageAtLastPoint()) {
                RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                if (recordState != null) {
                    recordState.onFailed();
                }
                Log.w(TAG, "onShutterButtonClick return, isLowStorageAtLastPoint");
                return;
            }
            this.mCaptureStartTime = System.currentTimeMillis();
            this.mShootingStartTime = SystemClock.elapsedRealtime();
            this.mRecordingStartTime = SystemClock.uptimeMillis();
            startAmbilightShooting();
            updateRecordingTimeStyle(false);
            updateRecordingTime();
        }
    }

    private void unregisterSensor() {
        if (CameraSettings.isGradienterOn()) {
            this.mActivity.getSensorStateManager().setGradienterEnabled(false);
        }
        if (this.mIsRegisterSensorListener) {
            this.mIsRegisterSensorListener = false;
            this.mActivity.getSensorStateManager().setSensorStateListener(null);
        }
    }

    private void updateASD() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setASDEnable(true);
            Log.d(TAG, "updateASD call setASDEnable with true");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
        if (r7 != false) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0043, code lost:
        if (r7 != false) goto L_0x002c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean updateAutoAeParameters(boolean z) {
        Camera2Proxy camera2Proxy;
        if (this.mCamera2Device == null) {
            return false;
        }
        int i = this.mInDebugMode ? SystemProperties.getInt("ambi_auto_ae_target", 40) : 40;
        int i2 = this.mSceneMode;
        int i3 = 3;
        boolean z2 = true;
        if (i2 == 5) {
            Camera2Proxy camera2Proxy2 = this.mCamera2Device;
            if (!C0124O00000oO.isMTKPlatform()) {
                i3 = 1;
            }
            camera2Proxy2.setAmbilightMode(i3);
            camera2Proxy = this.mCamera2Device;
        } else if (i2 == 3) {
            this.mCamera2Device.setAmbilightMode(C0124O00000oO.isMTKPlatform() ? 4 : 2);
            camera2Proxy = this.mCamera2Device;
        } else {
            this.mCamera2Device.setAmbilightMode(0);
            z2 = false;
            return z2;
        }
        i = 0;
        camera2Proxy.setAmbilightAeTarget(i);
        return z2;
    }

    private void updateExposureParameters() {
        long j;
        int i;
        float f;
        int i2 = 0;
        this.mIso = 0;
        int i3 = this.mSceneMode;
        if (i3 != 0) {
            if (i3 == 1) {
                i = 15;
                f = 0.0f;
                i2 = 3;
            } else if (i3 != 2) {
                if (i3 != 3) {
                    if (i3 == 4) {
                        i = 0;
                        i2 = 1;
                    } else if (i3 != 5) {
                        i = 0;
                    } else if (!mSupportAutoAe) {
                        j = ((long) getDuration()) * ExtraTextUtils.MB;
                        this.mIso = 800;
                        i = 0;
                        f = 0.0f;
                        i2 = 5;
                    } else {
                        i = 0;
                        f = 0.0f;
                        i2 = 5;
                    }
                } else if (!mSupportAutoAe) {
                    j = ((long) getDuration()) * ExtraTextUtils.MB;
                    this.mIso = 800;
                    i = 0;
                    i2 = 6;
                    f = 0.0f;
                } else {
                    i = 0;
                    i2 = 6;
                }
                f = 0.0f;
            } else {
                long j2 = C0124O00000oO.isMTKPlatform() ? 500000000 : 333333333;
                this.mIso = 50;
                j = j2;
                f = 0.0f;
                i = 0;
                i2 = 4;
            }
            j = 0;
        } else {
            f = -1.5f;
            j = 50000000;
            i = 0;
            i2 = 2;
        }
        if (this.mInDebugMode) {
            StringBuilder sb = new StringBuilder();
            sb.append("ambi_ev_");
            sb.append(i2);
            String str = SystemProperties.get(sb.toString());
            if (!TextUtils.isEmpty(str)) {
                try {
                    f = Float.parseFloat(str);
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("set ev:");
                    sb2.append(f);
                    Log.k(3, str2, sb2.toString());
                } catch (NumberFormatException unused) {
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("wrong ev:");
                    sb3.append(str);
                    Log.k(6, str3, sb3.toString());
                }
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append("ambi_fps_");
            sb4.append(i2);
            int i4 = SystemProperties.getInt(sb4.toString(), -1);
            if (i4 != -1) {
                i = i4;
            }
            StringBuilder sb5 = new StringBuilder();
            sb5.append("ambi_ms_");
            sb5.append(i2);
            int i5 = SystemProperties.getInt(sb5.toString(), -1);
            if (i5 != -1) {
                j = ((long) i5) * ExtraTextUtils.MB;
            }
            StringBuilder sb6 = new StringBuilder();
            sb6.append("ambi_iso_");
            sb6.append(i2);
            int i6 = SystemProperties.getInt(sb6.toString(), -1);
            if (i6 != -1) {
                this.mIso = i6;
            }
        }
        if (f != 0.0f) {
            Rational exposureCompensationRational = this.mCameraCapabilities.getExposureCompensationRational();
            this.mCamera2Device.setExposureCompensation((int) ((f * ((float) exposureCompensationRational.getDenominator())) / ((float) exposureCompensationRational.getNumerator())));
        }
        int i7 = this.mIso;
        if (i7 != 0) {
            this.mCamera2Device.setISO(i7);
        }
        if (j != 0) {
            this.mCamera2Device.setExposureTime(j);
        }
        if (i != 0) {
            this.mCamera2Device.setFpsRange(C0124O00000oO.isMTKPlatform() ? new Range(Integer.valueOf(5), Integer.valueOf(i)) : new Range(Integer.valueOf(i), Integer.valueOf(i)));
        }
    }

    private void updateFilter() {
        int shaderEffect = CameraSettings.getShaderEffect();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateFilter: 0x");
        sb.append(Integer.toHexString(shaderEffect));
        Log.v(str, sb.toString());
        EffectController.getInstance().setEffect(shaderEffect);
    }

    private void updateFocusMode() {
        setFocusMode(this.mFocusManager.setFocusMode(CameraSettings.getFocusMode()));
    }

    private void updatePictureAndPreviewSize() {
        List supportedOutputSizeWithAssignedMode = this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(35);
        Size O0oooO = C0122O00000o.instance().O0oooO();
        if (O0oooO == null) {
            O0oooO = new Size(4184, 3138);
        }
        PictureSizeManager.initialize(supportedOutputSizeWithAssignedMode, O0oooO.getHeight() * O0oooO.getWidth(), 187, this.mBogusCameraId);
        CameraSize bestPictureSize = PictureSizeManager.getBestPictureSize(this.mModuleIndex);
        this.mPreviewSize = Util.getOptimalPreviewSize(false, this.mBogusCameraId, this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class), (double) CameraSettings.getPreviewAspectRatio(bestPictureSize.width, bestPictureSize.height));
        this.mPictureSize = bestPictureSize;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("pictureSize= ");
        sb.append(bestPictureSize.width);
        String str2 = "X";
        sb.append(str2);
        sb.append(bestPictureSize.height);
        sb.append(" previewSize=");
        sb.append(this.mPreviewSize.width);
        sb.append(str2);
        sb.append(this.mPreviewSize.height);
        Log.d(str, sb.toString());
        if (this.mAmbilightEngine == null) {
            String str3 = SystemProperties.get("ro.product.mod_device");
            if (str3 != null) {
                String str4 = "_";
                if (str3.contains(str4)) {
                    str3 = str3.split(str4)[0];
                }
            }
            StringBuilder sb2 = new StringBuilder();
            String str5 = "ambilight";
            sb2.append(str5);
            sb2.append(File.separator);
            sb2.append(str3);
            sb2.append(File.separator);
            String str6 = ALGO_XML_FILENAME;
            sb2.append(str6);
            if (!FileUtils.copyFileIfNeed(CameraAppImpl.getAndroidContext(), getAlgoXmlFile(), sb2.toString())) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str5);
                sb3.append(File.separator);
                sb3.append(str6);
                FileUtils.copyFileIfNeed(CameraAppImpl.getAndroidContext(), getAlgoXmlFile(), sb3.toString());
            }
            this.mAmbilightEngine = new AmbilightEngine();
        }
        this.mAmbilightWidth = bestPictureSize.width;
        this.mAmbilightHeight = bestPictureSize.height;
        CameraSize cameraSize = this.mPreviewSize;
        updateCameraScreenNailSize(cameraSize.width, cameraSize.height);
    }

    private void updateRecordingTimeStyle(boolean z) {
        if (this.mTopAlert != null) {
            AndroidSchedulers.mainThread().scheduleDirect(new O00000Oo(this, z));
        }
    }

    private void updateSpeechShutter(boolean z) {
        if (CameraSettings.isSpeechShutterOpen()) {
            SpeechShutterDetect speechShutterDetect = (SpeechShutterDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(255);
            if (speechShutterDetect != null) {
                speechShutterDetect.processingSpeechShutter(z);
            }
            ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
            if (actionProcessing != null) {
                actionProcessing.processingSpeechShutter(z);
            }
        }
    }

    private void updateTransform(float f, float f2, float f3, float f4, float f5) {
        float[] texMatrix = getActivity().getGLView().getGLCanvas().getState().getTexMatrix();
        int length = texMatrix.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = i2 + 1;
            this.mTransform[i2] = texMatrix[i];
            i++;
            i2 = i3;
        }
        Matrix.translateM(this.mTransform, 0, f, f2, 0.0f);
        Matrix.translateM(this.mTransform, 0, f4, f5, 0.0f);
        Matrix.scaleM(this.mTransform, 0, f3, f3, 1.0f);
        Matrix.translateM(this.mTransform, 0, -f, -f2, 0.0f);
        DrawYuvAttribute drawYuvAttribute = this.mYuvAttribute;
        if (drawYuvAttribute != null) {
            drawYuvAttribute.updateZoom(this.mTransform);
        }
        DrawYuvAttribute drawYuvAttribute2 = this.mYuvAttributeForVideo;
        if (drawYuvAttribute2 != null) {
            drawYuvAttribute2.updateZoom(this.mTransform);
            this.mYuvAttributeForVideo.updatePosition(0, 0);
        }
    }

    public /* synthetic */ void O0000ooO(boolean z) {
        this.mTopAlert.updateRecordingTimeStyle(z);
    }

    public /* synthetic */ void O00oOO0o() {
        Log.d(TAG, "onSaveCompleted");
        onSaveFinish();
    }

    public void addSaveTask(String str, ContentValues contentValues) {
        contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        SaveVideoTask saveVideoTask = new SaveVideoTask(str, contentValues);
        synchronized (this) {
            this.mPendingSaveTaskList.add(saveVideoTask);
        }
    }

    /* access modifiers changed from: protected */
    public void applyZoomRatio() {
        if (this.mCamera2Device != null) {
            float deviceBasedZoomRatio = getDeviceBasedZoomRatio();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyZoomRatio(): apply zoom ratio to device = ");
            sb.append(deviceBasedZoomRatio);
            Log.d(str, sb.toString());
            this.mCamera2Device.setZoomRatio(deviceBasedZoomRatio);
            this.mZoom = 1.0f / deviceBasedZoomRatio;
            float f = this.mZoom;
            if (f != 1.0f) {
                updateTransform(0.5f, 0.5f, f, 0.0f, 0.0f);
            }
            this.mCropRegion = HybridZoomingSystem.toCropRegion(deviceBasedZoomRatio, new Rect(0, 0, this.mAmbilightWidth, this.mAmbilightHeight));
        }
    }

    public void cancelFocus(boolean z) {
        if (isAlive() && isFrameAvailable() && getCameraState() != 0) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                if (z) {
                    camera2Proxy.setFocusMode(4);
                }
                this.mCamera2Device.cancelFocus(this.mModuleIndex);
            }
            if (getCameraState() != 3) {
                setCameraState(1);
            }
        }
    }

    public void checkDisplayOrientation() {
        if (isCreated()) {
            super.checkDisplayOrientation();
            MainContentProtocol mainContentProtocol = this.mMainProtocol;
            if (mainContentProtocol != null) {
                mainContentProtocol.setCameraDisplayOrientation(this.mCameraDisplayOrientation);
            }
            FocusManager2 focusManager2 = this.mFocusManager;
            if (focusManager2 != null) {
                focusManager2.setDisplayOrientation(this.mCameraDisplayOrientation);
            }
        }
    }

    public void closeCamera() {
        Log.d(TAG, "closeCamera: start");
        FlowableEmitter flowableEmitter = this.mMetaDataFlowableEmitter;
        if (flowableEmitter != null) {
            flowableEmitter.onComplete();
        }
        Disposable disposable = this.mMetaDataDisposable;
        if (disposable != null) {
            disposable.dispose();
        }
        Camera camera = this.mActivity;
        if (camera != null) {
            camera.getCameraScreenNail().setExternalFrameProcessor(null);
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setASDEnable(false);
            this.mCamera2Device.releaseCameraPreviewCallback(null);
            this.mCamera2Device.setFocusCallback(null);
            this.mCamera2Device.setErrorCallback(null);
            this.mCamera2Device.setMetaDataCallback(null);
            this.mCamera2Device = null;
        }
        stopCpuBoost();
        Log.d(TAG, "closeCamera: end");
    }

    /* access modifiers changed from: protected */
    public void consumePreference(int... iArr) {
        for (int i : iArr) {
            if (i == 1) {
                updatePictureAndPreviewSize();
            } else if (i == 2) {
                updateFilter();
            } else if (i == 3) {
                updateFocusArea();
            } else if (i == 9) {
                updateAntiBanding(CameraSettings.getAntiBanding());
            } else if (i == 10) {
                updateFlashPreference();
            } else if (i == 12) {
                setEvValue();
            } else if (i == 14) {
                updateFocusMode();
            } else if (i == 19) {
                updateAutoAeParameters(true);
            } else if (i == 29) {
                updateExposureMeteringMode();
            } else if (i == 55) {
                updateModuleRelated();
            } else if (i == 59) {
                updateOnTripMode();
            } else if (i == 70) {
                updateASD();
            } else if (i == 24) {
                applyZoomRatio();
            } else if (i == 25) {
                focusCenter();
            }
        }
    }

    public void executeSaveTask(boolean z) {
        synchronized (this) {
            do {
                if (this.mPendingSaveTaskList.isEmpty()) {
                    break;
                }
                SaveVideoTask saveVideoTask = (SaveVideoTask) this.mPendingSaveTaskList.remove(0);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("executeSaveTask: ");
                sb.append(saveVideoTask.videoPath);
                Log.d(str, sb.toString());
                this.mActivity.getImageSaver().addVideo(saveVideoTask.videoPath, saveVideoTask.contentValues, true);
                CameraStatUtils.trackAmbilightGenerateVideo();
            } while (!z);
            doLaterReleaseIfNeed();
        }
    }

    public void exitAutoHibernation() {
        super.exitAutoHibernation();
        if (needAutoHibernationScene() && this.mIsShooting) {
            TopAlert topAlert = this.mTopAlert;
            if (topAlert != null) {
                topAlert.alertAiDetectTipHint(0, R.string.super_night_toast, -1);
            }
        }
    }

    public void fillFeatureControl(StartControl startControl) {
        startControl.getFeatureDetail().addFragmentInfo(R.id.bottom_beauty, BaseFragmentDelegate.FRAGMENT_AMBILIGHT);
    }

    public boolean getAutoFinish() {
        int i = this.mSceneMode;
        return i == 4 || i == 5;
    }

    public int getDuration() {
        int i = this.mSceneMode;
        if (i == 3) {
            return DurationConstant.DURATION_AMBILIGHT_STAR_TRACK_CAPTURE;
        }
        if (i != 4) {
            if (i != 5) {
                return 2000;
            }
            return mSupportAutoAe ? (int) (this.mExposureTime / ExtraTextUtils.MB) : DurationConstant.DURATION_AMBILIGHT_MAGIC_STAR_CAPTURE;
        } else if (!this.mInDebugMode) {
            return 2000;
        } else {
            int i2 = SystemProperties.getInt("ambi_crowd_duration", -1);
            if (i2 != -1) {
                return i2;
            }
            return 2000;
        }
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        if (C0124O00000oO.isMTKPlatform() || !mSupportAutoAe) {
            return 0;
        }
        return CameraCapabilities.SESSION_OPERATION_MODE_AMBILIGHT;
    }

    /* access modifiers changed from: protected */
    public long getPTSUs() {
        long nanoTime = System.nanoTime() / 1000;
        long j = this.prevOutputPTSUs;
        return nanoTime < j ? nanoTime + (j - nanoTime) : nanoTime;
    }

    public boolean getPressAnimationEnabled() {
        return mSupportAutoAe && this.mSceneMode == 5;
    }

    public String getTag() {
        return TAG;
    }

    public void handleFrameProcessDone() {
        synchronized (this.mRenderLock) {
            this.mAmbilightEngine.updatePreview(this.mYBuffer, this.mUBuffer, this.mVBuffer, this.mAmbilightRoi);
            if (this.mAmbilightRoi.zoomRatio < 1.0f) {
                Log.d(TAG, "update preview roi:[%f - %f, zoom:%f] ", Float.valueOf(this.mAmbilightRoi.xOffset), Float.valueOf(this.mAmbilightRoi.yOffset), Float.valueOf(this.mAmbilightRoi.zoomRatio));
                updateTransform(this.mAmbilightRoi.xOffset, this.mAmbilightRoi.yOffset, this.mAmbilightRoi.zoomRatio * this.mZoom, this.mAmbilightRoi.xTrans, this.mAmbilightRoi.yTrans);
            }
            this.mActivity.getGLView().requestRender();
        }
    }

    public boolean isDoingAction() {
        return this.mPaused || this.isZooming || getCameraState() == 0 || isInCountDown() || isProcessingSaveTask() || this.mIsPrepareSaveTask || this.mIsShooting;
    }

    public boolean isProcessorReady() {
        return ((MainHandler) this.mHandler).isInRendering();
    }

    public boolean isRecording() {
        return this.mIsShooting || this.mIsPrepareSaveTask;
    }

    public boolean isUnInterruptable() {
        return false;
    }

    public boolean isZoomEnabled() {
        if (this.mIsShooting) {
            return false;
        }
        return super.isZoomEnabled();
    }

    /* access modifiers changed from: protected */
    public boolean isZslPreferred() {
        return C0124O00000oO.isMTKPlatform();
    }

    /* access modifiers changed from: protected */
    public void keepScreenOn() {
        this.mHandler.removeMessages(17);
        this.mHandler.removeMessages(2);
        getWindow().addFlags(128);
    }

    public boolean multiCapture() {
        return false;
    }

    public void notifyFocusAreaUpdate() {
        updatePreferenceTrampoline(3);
    }

    public boolean onBackPressed() {
        if (!this.mIsShooting) {
            return super.onBackPressed();
        }
        if (!isProcessingSaveTask()) {
            stopAmbilightShooting();
        }
        return true;
    }

    public void onBroadcastReceived(Context context, Intent intent) {
        if (intent != null && isAlive()) {
            if (CameraIntentManager.ACTION_SPEECH_SHUTTER.equals(intent.getAction())) {
                Log.d(TAG, "on Receive speech shutter broadcast action intent");
                if (!this.mIsShooting) {
                    onShutterButtonClick(110);
                }
            }
            super.onBroadcastReceived(context, intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        Log.k(3, TAG, "Ambilight Moduleon CameraOpened");
        super.onCameraOpened();
        initializeFocusManager();
        updatePreferenceTrampoline(UpdateConstant.FUN_TYPES_INIT);
        startPreviewSession();
        initMetaParser();
        updateAutoHibernation();
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        mSupportAutoAe = this.mCameraCapabilities.isSupportAmbilightAutoAeTag();
        this.mHandler = new MainHandler(this.mActivity.getMainLooper(), this);
        this.mComponentConfigAmbilight = DataRepository.dataItemRunning().getComponentConfigAmbilight();
        this.mSceneMode = this.mComponentConfigAmbilight.getSceneModeByIndex(this.mComponentConfigAmbilight.getAmbilightModeIndex());
        onCameraOpened();
        EffectController.getInstance().setEffect(FilterInfo.FILTER_ID_NONE);
        this.mFileNameTemplate = this.mActivity.getString(R.string.ambi_file_name_format);
        this.mYuvAttribute = new DrawYuvAttribute();
        this.mYuvAttributeForVideo = new DrawYuvAttribute();
    }

    public void onDrawFrame(Rect rect, int i, int i2, boolean z) {
        GLCanvasImpl gLCanvas = getActivity().getGLView().getGLCanvas();
        if (gLCanvas != null) {
            gLCanvas.prepareYuvRenders();
            synchronized (this.mRenderLock) {
                this.mYuvAttribute.updatePosition(0, this.mTranslateY);
                gLCanvas.draw(this.mYuvAttribute.init(this.mImage, new Size(rect.width(), rect.height())));
                long pTSUs = getPTSUs();
                if (this.mMediaRecorderRecording && this.mLiveMediaRecorder != null && pTSUs - this.prevOutputPTSUs > 5900000) {
                    this.mLiveMediaRecorder.onSurfaceTextureUpdated((DrawAttribute) this.mYuvAttributeForVideo.init(this.mImage, this.mVideoSize), this.mMediaRecorderRecording);
                    this.prevOutputPTSUs = pTSUs;
                }
            }
        }
    }

    public void onFocusStateChanged(FocusTask focusTask) {
        if (isFrameAvailable() && !isDeparted()) {
            int focusTrigger = focusTask.getFocusTrigger();
            if (focusTrigger == 1) {
                Log.v(TAG, String.format(Locale.ENGLISH, "FocusTime=%1$dms focused=%2$b", new Object[]{Long.valueOf(focusTask.getElapsedTime()), Boolean.valueOf(focusTask.isSuccess())}));
                if (!this.mFocusManager.isFocusingSnapOnFinish() && getCameraState() != 3) {
                    setCameraState(1);
                }
                this.mFocusManager.onFocusResult(focusTask);
                this.mActivity.getSensorStateManager().reset();
                if (focusTask.isSuccess() && this.m3ALocked) {
                    if (!C0122O00000o.instance().OOoOo0() && isZoomRatioBetweenUltraAndWide()) {
                        CameraCapabilities cameraCapabilities = this.mUltraCameraCapabilities;
                        if (cameraCapabilities != null) {
                            boolean isAFRegionSupported = cameraCapabilities.isAFRegionSupported();
                            String str = TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onFocusStateChanged: isUltraFocusAreaSupported = ");
                            sb.append(isAFRegionSupported);
                            Log.d(str, sb.toString());
                            if (!isAFRegionSupported) {
                                this.mCamera2Device.setFocusMode(0);
                                this.mCamera2Device.setFocusDistance(0.0f);
                            }
                        }
                    }
                    this.mCamera2Device.lockExposure(true);
                }
            } else if (focusTrigger == 2 ? !focusTask.isIsDepthFocus() : focusTrigger == 3) {
                handleFocusStateChange(focusTask);
            }
        }
    }

    public void onHostStopAndNotifyActionStop() {
        if (this.mIsShooting) {
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onFinish();
            }
            stopAmbilightShooting();
            doLaterReleaseIfNeed();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        int i2;
        if (this.mPaused) {
            return true;
        }
        boolean z = false;
        if (!isFrameAvailable()) {
            return false;
        }
        if (i == 27 || i == 66) {
            if (keyEvent.getRepeatCount() == 0) {
                if (Util.isFingerPrintKeyEvent(keyEvent)) {
                    if (CameraSettings.isFingerprintCaptureEnable()) {
                        i2 = 30;
                    }
                    return true;
                }
                i2 = 40;
                performKeyClicked(i2, getString(R.string.pref_camera_volumekey_function_entryvalue_shutter), keyEvent.getRepeatCount(), true);
                return true;
            }
        } else if (i != 700) {
            if (!(i == 87 || i == 88)) {
                switch (i) {
                    case 23:
                        if (keyEvent.getRepeatCount() == 0) {
                            performKeyClicked(50, getString(R.string.pref_camera_volumekey_function_entryvalue_shutter), keyEvent.getRepeatCount(), true);
                            return true;
                        }
                        break;
                    case 24:
                    case 25:
                        break;
                }
            }
            if (i == 24 || i == 88) {
                z = true;
            }
            if (handleVolumeKeyEvent(z, true, keyEvent.getRepeatCount(), keyEvent.getDevice().isExternal())) {
                return true;
            }
        } else if (this.mIsShooting) {
            stopAmbilightShooting();
        }
        return super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 4) {
            if (i == 27 || i == 66) {
                return true;
            }
        } else if (((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).handleBackStackFromKeyBack()) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    public void onPause() {
        stopBackgroundThread();
        super.onPause();
        unregisterSensor();
        resetScreenOn();
        this.mHandler.sendMessageAtFrontOfQueue(this.mHandler.obtainMessage(105));
        LiveMediaRecorder liveMediaRecorder = this.mLiveMediaRecorder;
        if (liveMediaRecorder != null) {
            liveMediaRecorder.release();
        }
    }

    public void onPreviewLayoutChanged(Rect rect) {
        super.onPreviewLayoutChanged(rect);
        this.mActivity.onLayoutChange(rect);
        this.mTranslateY = getActivity().getCameraScreenNail().getTranslateY();
        this.mYuvAttribute.updatePosition(0, this.mTranslateY);
    }

    public void onPreviewMetaDataUpdate(CaptureResult captureResult) {
        super.onPreviewMetaDataUpdate(captureResult);
        if (captureResult != null) {
            super.onPreviewMetaDataUpdate(captureResult);
            Float f = (Float) captureResult.get(CaptureResult.LENS_FOCUS_DISTANCE);
            if (f != null) {
                this.mFocusDistance = f.floatValue();
            }
            if (mSupportAutoAe && C0124O00000oO.isMTKPlatform() && this.mSceneMode == 5) {
                Long l = (Long) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.AMBILIGHT_AE_EXPOSURE);
                if (!(l == null || l.longValue() == 0)) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("[onPreviewMetaDataUpdate] exposureTime: ");
                    sb.append(l);
                    Log.d(str, sb.toString());
                    this.mExposureTime = l.longValue() * 1000;
                    this.mHandler.obtainMessage(107, Long.valueOf(this.mExposureTime)).sendToTarget();
                }
            }
            if (this.mMetaDataFlowableEmitter != null && !this.mIsShooting) {
                this.mMetaDataFlowableEmitter.onNext(captureResult);
            }
        }
    }

    public void onPreviewSessionClosed(CameraCaptureSession cameraCaptureSession) {
        Log.k(3, TAG, "onPreviewSessionClosed");
    }

    public void onPreviewSessionFailed(CameraCaptureSession cameraCaptureSession) {
        Log.k(6, TAG, "onPreviewSessionFailed");
        if (!isTextureExpired() || !retryOnceIfCameraError(this.mHandler)) {
            this.mHandler.sendEmptyMessage(51);
        } else {
            Log.d(TAG, "sessionFailed due to surfaceTexture expired, retry");
        }
    }

    public void onPreviewSessionSuccess(CameraCaptureSession cameraCaptureSession) {
        Log.k(3, TAG, "onPreviewSessionSuccess");
        if (cameraCaptureSession != null && isAlive()) {
            this.mHandler.sendEmptyMessage(9);
            setCameraState(1);
            updatePreferenceInWorkThread(UpdateConstant.AMBILIGHT_TYPES_ON_PREVIEW_SUCCESS);
        }
    }

    public void onResume() {
        super.onResume();
        MiAlgoAsdSceneProfile.clearInitASDScenes();
        this.mTripodMode = 0;
        this.mImagedata = null;
        this.mIsPrepared = false;
        this.mDropFrameCount = 0;
        startBackgroundThread();
        registerSensorListener();
        this.mInDebugMode = SystemProperties.getBoolean("enable_ambi_debug", false);
    }

    public void onReviewCancelClicked() {
    }

    public void onReviewDoneClicked() {
    }

    public void onSceneModeSelect(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onSceneModeSelect:");
        sb.append(i);
        Log.k(3, str, sb.toString());
        if (i != this.mSceneMode) {
            this.mSceneMode = i;
            this.mCamera2Device.pausePreview();
            if (updateAutoAeParameters(true) && C0124O00000oO.Oo000OO()) {
                this.mCamera2Device.setFpsRange(new Range(Integer.valueOf(0), Integer.valueOf(30)));
            }
            resumePreview();
            updateAutoHibernation();
        }
    }

    public void onShineChanged(int i) {
        if (i == 196) {
            updatePreferenceTrampoline(2);
            this.mMainProtocol.updateEffectViewVisible();
        }
    }

    public void onShutterAnimationEnd() {
        if (this.mSceneMode == 4) {
            stopAmbilightShooting();
        }
    }

    public void onShutterButtonClick(int i) {
        if (!this.mPaused && getCameraState() != 0 && !isIgnoreTouchEvent() && !this.mActivity.isScreenSlideOff()) {
            if (this.mIsPrepareSaveTask || isProcessingSaveTask()) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onShutterButtonClick return, mIsPrepareSaveTask ");
                sb.append(this.mIsPrepareSaveTask);
                Log.w(str, sb.toString());
                return;
            }
            if (i == 100) {
                this.mActivity.onUserInteraction();
            } else if (i == 110) {
                this.mActivity.onUserInteraction();
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    topAlert.hideRecommendDescTip(FragmentTopAlert.TIP_SPEECH_SHUTTER_DESC);
                }
            }
            setTriggerMode(i);
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            String str3 = "onShutterButtonClick ";
            sb2.append(str3);
            sb2.append(getCameraState());
            Log.u(str2, sb2.toString());
            String str4 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str3);
            sb3.append(getCameraState());
            Log.k(3, str4, sb3.toString());
            if (!this.mIsShooting) {
                int countDownTimes = CameraSettings.getCountDownTimes();
                if (countDownTimes > 0) {
                    startCount(countDownTimes, i);
                } else {
                    Log.u(TAG, "onShutterButtonClick takeShot");
                    takeShot();
                }
            } else if (isShootingTooShort()) {
                Log.w(TAG, "shooting is too short, ignore this click");
            } else {
                int i2 = this.mSceneMode;
                if (!(i2 == 5 || i2 == 4)) {
                    Log.u(TAG, "onShutterButtonClick stopAmbilightShooting");
                    stopAmbilightShooting();
                }
            }
        }
    }

    public void onShutterButtonFocus(boolean z, int i) {
    }

    public boolean onShutterButtonLongClick() {
        return false;
    }

    public void onShutterButtonLongClickCancel(boolean z) {
        onShutterButtonFocus(false, 2);
    }

    public void onSingleTapUp(int i, int i2, boolean z) {
        super.onSingleTapUp(i, i2, z);
        if (!this.mPaused) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null && camera2Proxy.isSessionReady() && isInTapableRect(i, i2) && getCameraState() != 3 && getCameraState() != 0 && !this.mIsShooting && !isInCountDown()) {
                if (!isFrameAvailable()) {
                    Log.w(TAG, "onSingleTapUp: frame not available");
                } else if ((!isFrontCamera() || !this.mActivity.isScreenSlideOff()) && !((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).handleBackStackFromTapDown(i, i2)) {
                    this.mMainProtocol.setFocusViewType(true);
                    Point point = new Point(i, i2);
                    mapTapCoordinate(point);
                    unlockAEAF();
                    this.mFocusManager.onSingleTapUp(point.x, point.y, z);
                }
            }
        }
    }

    public void onThumbnailClicked(View view) {
        if (!this.mPaused && !isProcessingSaveTask() && this.mActivity.getThumbnailUpdater().getThumbnail() != null) {
            if (isCannotGotoGallery()) {
                Log.d(TAG, "onThumbnailClicked: CannotGotoGallery...");
                return;
            }
            this.mActivity.gotoGallery();
        }
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        if (!this.mPaused && !this.mIsShooting) {
            keepScreenOnAwhile();
        }
        if (this.mIsShooting && needAutoHibernationScene() && this.mIsAutoHibernationSupported) {
            keepAutoHibernation();
        }
    }

    public boolean onWaitingFocusFinished() {
        return false;
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onWindowFocusChanged: ");
        sb.append(z);
        Log.d(str, sb.toString());
        if (z && this.mIsAutoHibernationSupported && this.mIsShooting) {
            keepAutoHibernation();
        }
    }

    public void pausePreview() {
        Log.k(2, TAG, "pausePreview");
        this.mCamera2Device.pausePreview();
        setCameraState(0);
    }

    /* access modifiers changed from: protected */
    public void performKeyClicked(int i, String str, int i2, boolean z) {
        if (i2 == 0 && z) {
            TopAlert topAlert = this.mTopAlert;
            if (topAlert != null) {
                topAlert.hideAlert();
            }
            if (str.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_timer))) {
                startCount(2, 20);
            } else {
                onShutterButtonClick(i);
            }
        }
    }

    public void playFocusSound(int i) {
        playCameraSound(i);
    }

    public void registerProtocol() {
        super.registerProtocol();
        ModeCoordinatorImpl.getInstance().attachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(234, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(420, this);
        getActivity().getImplFactory().initAdditional(getActivity(), 164, 234, 212);
        this.mTopAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
    }

    public void releaseRender() {
    }

    public void resumePreview() {
        Log.k(2, TAG, "resumePreview");
        this.mCamera2Device.resumePreview();
        setCameraState(1);
    }

    public void setAsdScenes(ASDScene[] aSDSceneArr) {
        this.mAsdScenes = aSDSceneArr;
    }

    public boolean shouldCaptureDirectly() {
        return false;
    }

    public boolean shouldDisableStopButton() {
        return this.mSceneMode == 3;
    }

    public boolean shouldReleaseLater() {
        return isRecording();
    }

    public void startFocus() {
        if (isDeviceAlive() && isFrameAvailable()) {
            Log.v(TAG, "startFocus");
            if (this.mFocusOrAELockSupported) {
                this.mCamera2Device.startFocus(FocusTask.create(1), this.mModuleIndex);
            } else {
                this.mCamera2Device.resumePreview();
            }
        }
    }

    public void startPreview() {
        boolean isDeviceAlive = isDeviceAlive();
    }

    /* access modifiers changed from: protected */
    public void startVideoRecording() {
        if (this.mCamera2Device == null) {
            Log.e(TAG, "initializeRecorder: null camera");
            return;
        }
        Log.v(TAG, "startVideoRecording");
        if (this.mLiveMediaRecorder == null) {
            this.mLiveMediaRecorder = new LiveMediaRecorder();
        }
        if (this.mLiveMediaRecorder.init(Util.genContentValues(2, Util.genVideoPath(2, getString(R.string.video_file_name_format)), this.mVideoSize.getWidth(), this.mVideoSize.getHeight()), this.mOrientationCompensation, getActivity().getGLView().getEGLContext14(), this.mMediaEncoderListener, false, 0.0027777778f) && this.mLiveMediaRecorder.startRecorder(this.mRequestStartTime)) {
            this.mMediaRecorderRecording = true;
            Log.v(TAG, "startVideoRecording process done");
        }
    }

    public void tryRemoveCountDownMessage() {
        if (isInCountDown()) {
            this.mCountdownDisposable.dispose();
            this.mCountdownDisposable = null;
            this.mHandler.post(new Runnable() {
                public void run() {
                    Log.d(AmbilightModule.TAG, "run: hide delay number in main thread");
                    AmbilightModule.this.mTopAlert.hideDelayNumber();
                }
            });
        }
    }

    public void unRegisterModulePersistProtocol() {
        super.unRegisterModulePersistProtocol();
        Log.d(TAG, "unRegisterModulePersistProtocol");
        getActivity().getImplFactory().detachModulePersistent();
    }

    public void unRegisterProtocol() {
        tryRemoveCountDownMessage();
        this.mHandler.removeMessages(104);
        super.unRegisterProtocol();
        ModeCoordinatorImpl.getInstance().detachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(234, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(420, this);
        Camera camera = this.mActivity;
        if (camera != null) {
            camera.getImplFactory().detachAdditional();
        }
    }

    /* access modifiers changed from: protected */
    public void unlockAEAF() {
        Log.d(TAG, "unlockAEAF");
        this.m3ALocked = false;
        if (this.mAeLockSupported) {
            this.mCamera2Device.setAELock(false);
        }
        this.mFocusManager.setAeAwbLock(false);
    }

    /* access modifiers changed from: protected */
    public void updateAutoHibernation() {
        SettingUiState autoHibernationSettingNeedRemove = CameraSettings.getAutoHibernationSettingNeedRemove(this.mModuleIndex, isFrontCamera());
        boolean z = !autoHibernationSettingNeedRemove.isRomove && !autoHibernationSettingNeedRemove.isMutexEnable && CameraSettings.isAutoHibernationOn() && needAutoHibernationScene();
        this.mIsAutoHibernationSupported = z;
    }

    /* access modifiers changed from: protected */
    public void updateAutoHibernationFirstRecordingTime() {
        AutoHibernation autoHibernation = (AutoHibernation) ModeCoordinatorImpl.getInstance().getAttachProtocol(936);
        if (autoHibernation != null) {
            autoHibernation.updateAutoHibernationFirstRecordingTime(this.mRecordingTime, "");
        }
    }

    public void updateBuffers(int i) {
        if (this.mYBuffer == null) {
            this.mYBuffer = ByteBuffer.allocateDirect(i);
            this.mUBuffer = ByteBuffer.allocateDirect(i);
            this.mVBuffer = ByteBuffer.allocateDirect(i);
        }
        if (this.mAmbilightRoi == null) {
            this.mAmbilightRoi = new AmbilightRoi();
        }
        if (this.mImage == null) {
            this.mImage = new MiYuvImage(null, this.mPreviewWidth, this.mPreviewHeight, 35);
        }
        this.mImage.updateData(this.mYBuffer, this.mUBuffer, this.mVBuffer);
    }

    /* access modifiers changed from: protected */
    public void updateFlashPreference() {
        setFlashMode(DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex));
    }

    /* access modifiers changed from: protected */
    public void updateFocusArea() {
        if (this.mCamera2Device == null) {
            Log.e(TAG, "updateFocusArea: null camera device");
            return;
        }
        Rect cropRegion = getCropRegion();
        Rect activeArraySize = getActiveArraySize();
        this.mActivity.getSensorStateManager().setFocusSensorEnabled(this.mFocusManager.getMeteringAreas(cropRegion, activeArraySize) != null);
        this.mCamera2Device.setAERegions(this.mFocusManager.getMeteringAreas(cropRegion, activeArraySize));
        if (this.mFocusAreaSupported) {
            this.mCamera2Device.setAFRegions(this.mFocusManager.getFocusAreas(cropRegion, activeArraySize));
        } else {
            this.mCamera2Device.resumePreview();
        }
    }

    public void updateOnTripMode() {
        if (this.mCamera2Device != null) {
            ASDScene[] aSDSceneArr = this.mAsdScenes;
            if (aSDSceneArr != null) {
                for (ASDScene aSDScene : aSDSceneArr) {
                    if (aSDScene.type == 4) {
                        this.mTripodMode = aSDScene.value;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateRecordingTime() {
        if (this.mIsShooting && !shouldHideTimeLabel()) {
            if (this.mSceneMode == 5) {
                CountDownTimer countDownTimer = this.mCountDownTimer;
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                AnonymousClass5 r1 = new CountDownTimer((long) (getDuration() + 1000), 1) {
                    public void onFinish() {
                    }

                    public void onTick(long j) {
                        String millisecondToTimeString = Util.millisecondToTimeString(j, false);
                        if (AmbilightModule.this.mTopAlert != null) {
                            AmbilightModule.this.mTopAlert.updateRecordingTime(millisecondToTimeString);
                        }
                    }
                };
                this.mCountDownTimer = r1;
                this.mCountDownTimer.start();
                return;
            }
            String millisecondToTimeString = Util.millisecondToTimeString(SystemClock.uptimeMillis() - this.mRecordingStartTime, false);
            this.mRecordingTime = millisecondToTimeString;
            TopAlert topAlert = this.mTopAlert;
            if (topAlert != null) {
                topAlert.updateRecordingTime(millisecondToTimeString);
            }
            if (this.mIsAutoHibernationSupported) {
                AutoHibernation autoHibernation = (AutoHibernation) ModeCoordinatorImpl.getInstance().getAttachProtocol(936);
                if (autoHibernation != null) {
                    autoHibernation.updateAutoHibernationRecordingTimeOrCaptureCount(millisecondToTimeString, "");
                }
            }
            this.mHandler.sendEmptyMessageDelayed(42, 500);
        }
    }
}
