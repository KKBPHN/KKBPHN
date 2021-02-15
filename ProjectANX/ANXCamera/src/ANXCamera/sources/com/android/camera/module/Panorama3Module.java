package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.TotalCaptureResult;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.format.DateFormat;
import android.util.Size;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import com.android.camera.AutoLockManager;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.FileCompat;
import com.android.camera.LocationManager;
import com.android.camera.OnClickAttr;
import com.android.camera.PictureSizeManager;
import com.android.camera.R;
import com.android.camera.SensorStateManager.SensorStateAdapter;
import com.android.camera.SensorStateManager.SensorStateListener;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.constant.UpdateConstant.UpdateType;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FilterInfo;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.log.Log;
import com.android.camera.module.loader.StartControl;
import com.android.camera.panorama.AttachRunnable;
import com.android.camera.panorama.Camera1Image;
import com.android.camera.panorama.Camera2Image;
import com.android.camera.panorama.CaptureImage;
import com.android.camera.panorama.DirectionFunction;
import com.android.camera.panorama.DownDirectionFunction;
import com.android.camera.panorama.GyroscopeRoundDetector;
import com.android.camera.panorama.InputSave;
import com.android.camera.panorama.LeftDirectionFunction;
import com.android.camera.panorama.MorphoPanoramaGP3;
import com.android.camera.panorama.MorphoPanoramaGP3.GalleryInfoData;
import com.android.camera.panorama.MorphoPanoramaGP3.InitParam;
import com.android.camera.panorama.MorphoSensorFusion.SensorData;
import com.android.camera.panorama.PanoramaGP3ImageFormat;
import com.android.camera.panorama.PanoramaSetting;
import com.android.camera.panorama.PanoramaState;
import com.android.camera.panorama.PositionDetector;
import com.android.camera.panorama.RightDirectionFunction;
import com.android.camera.panorama.RoundDetector;
import com.android.camera.panorama.SensorFusion;
import com.android.camera.panorama.SensorInfoManager;
import com.android.camera.panorama.UpDirectionFunction;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.PanoramaProtocol;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsConstants.Panorama;
import com.android.camera.statistic.MistatsWrapper.PictureTakenParameter;
import com.android.camera.storage.MediaProviderUtil;
import com.android.camera.storage.Storage;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.Camera2Proxy.PictureCallbackWrapper;
import com.android.gallery3d.exif.ExifHelper;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@TargetApi(21)
public class Panorama3Module extends BaseModule implements CameraAction, CameraPreviewCallback {
    /* access modifiers changed from: private */
    public static final boolean DUMP_YUV = SystemProperties.getBoolean("camera.debug.panorama", false);
    private static final int MIN_SHOOTING_TIME = 600;
    private static final int SENSOR_LIST = 186;
    /* access modifiers changed from: private */
    public static final String TAG = "Panorama3Module";
    public static final Object mEngineLock = new Object();
    /* access modifiers changed from: private */
    public static final Object mPreviewImageLock = new Object();
    /* access modifiers changed from: private */
    public static final CaptureImage sAttachExit = new Camera1Image(null, 0, 0);
    /* access modifiers changed from: private */
    public final LinkedBlockingQueue mAttachImageQueue = new LinkedBlockingQueue();
    private int mAttachPosOffsetX;
    /* access modifiers changed from: private */
    public int mAttachPosOffsetY;
    /* access modifiers changed from: private */
    public int mCameraOrientation = 0;
    /* access modifiers changed from: private */
    public volatile boolean mCanSavePanorama;
    /* access modifiers changed from: private */
    public boolean mCaptureDirectionDecided = false;
    /* access modifiers changed from: private */
    public SensorInfoManager mCurrentSensorInfoManager;
    /* access modifiers changed from: private */
    public int mDirection;
    /* access modifiers changed from: private */
    public DirectionFunction mDirectionFunction;
    /* access modifiers changed from: private */
    public Bitmap mDispPreviewImage;
    /* access modifiers changed from: private */
    public Canvas mDispPreviewImageCanvas;
    /* access modifiers changed from: private */
    public Paint mDispPreviewImagePaint;
    /* access modifiers changed from: private */
    public final ExecutorService mExecutor;
    private int mGoalAngle = af.bY;
    private int mGoalAngleVertical = m.cq;
    /* access modifiers changed from: private */
    public float[] mGravities;
    /* access modifiers changed from: private */
    public String mImageFormat = PanoramaGP3ImageFormat.YVU420_SEMIPLANAR;
    /* access modifiers changed from: private */
    public InitParam mInitParam = new InitParam();
    private boolean mIsRegisterSensorListener = false;
    /* access modifiers changed from: private */
    public boolean mIsSensorAverage;
    /* access modifiers changed from: private */
    public volatile boolean mIsShooting = false;
    /* access modifiers changed from: private */
    public boolean mIsVertical;
    private Location mLocation;
    /* access modifiers changed from: private */
    public int mMaxHeight;
    /* access modifiers changed from: private */
    public int mMaxWidth;
    /* access modifiers changed from: private */
    public MorphoPanoramaGP3 mMorphoPanoramaGP3;
    private PanoramaSetting mPanoramaSetting;
    private long mPanoramaShootingStartTime;
    /* access modifiers changed from: private */
    public PanoramaState mPanoramaState;
    /* access modifiers changed from: private */
    public int mPictureHeight;
    /* access modifiers changed from: private */
    public int mPictureWidth;
    /* access modifiers changed from: private */
    public Bitmap mPreviewImage;
    /* access modifiers changed from: private */
    public int mPreviewRefY;
    /* access modifiers changed from: private */
    public volatile boolean mRequestStop;
    /* access modifiers changed from: private */
    public RoundDetector mRoundDetector;
    private SaveOutputImageTask mSaveOutputImageTask;
    /* access modifiers changed from: private */
    public int mSensorCnt;
    private SensorEventListener mSensorEventListener = new MySensorEventListener(new SensorListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (Panorama3Module.this.mIsSensorAverage) {
                float[] access$100 = Panorama3Module.this.mGravities;
                access$100[0] = access$100[0] + sensorEvent.values[0];
                float[] access$1002 = Panorama3Module.this.mGravities;
                access$1002[1] = access$1002[1] + sensorEvent.values[1];
                float[] access$1003 = Panorama3Module.this.mGravities;
                access$1003[2] = access$1003[2] + sensorEvent.values[2];
                Panorama3Module.this.mSensorCnt = Panorama3Module.this.mSensorCnt + 1;
                return;
            }
            Panorama3Module.this.mGravities[0] = sensorEvent.values[0];
            Panorama3Module.this.mGravities[1] = sensorEvent.values[1];
            Panorama3Module.this.mGravities[2] = sensorEvent.values[2];
            Panorama3Module.this.mSensorCnt = 1;
        }
    });
    /* access modifiers changed from: private */
    public SensorFusion mSensorFusion = null;
    private int mSensorFusionMode;
    private ArrayList mSensorInfoManagerList;
    private SensorStateListener mSensorStateListener = new SensorStateAdapter() {
        public boolean isWorking() {
            return Panorama3Module.this.isAlive() && Panorama3Module.this.getCameraState() != 0;
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            super.onSensorChanged(sensorEvent);
            if (C0122O00000o.instance().OOO0oO() && sensorEvent.sensor.getType() != 9) {
                Panorama3Module.this.mSensorFusion.onSensorChanged(sensorEvent);
            }
        }
    };
    private String mShutterEndTime;
    private String mShutterStartTime;
    /* access modifiers changed from: private */
    public int mSmallPreviewHeight;
    /* access modifiers changed from: private */
    public int mSmallPreviewHeightVertical;
    private int mSnapshotFocusMode = 1;
    private int mTargetFocusMode = 4;
    /* access modifiers changed from: private */
    public Size mThumbnailViewSize;
    /* access modifiers changed from: private */
    public long mTimeTaken;
    /* access modifiers changed from: private */
    public float mViewAngleH = 71.231476f;
    /* access modifiers changed from: private */
    public float mViewAngleV = 56.49462f;

    class DecideDirection extends PanoramaState {
        private boolean mHasSubmit;

        class DecideDirectionAttach extends AttachRunnable {

            class DecideRunnable implements Runnable {
                private Size mThumbnailViewSize;

                public DecideRunnable(Size size) {
                    this.mThumbnailViewSize = size;
                }

                public void run() {
                    Panorama3Module.this.reInitGravitySensorData();
                    if (Panorama3Module.this.mRequestStop) {
                        Log.w(Panorama3Module.TAG, "DecideRunnable exit request stop");
                        return;
                    }
                    Log.d(Panorama3Module.TAG, "go to PanoramaPreview in DecideRunnable");
                    synchronized (Panorama3Module.mEngineLock) {
                        if (Panorama3Module.this.mMorphoPanoramaGP3 == null) {
                            Log.w(Panorama3Module.TAG, "DecideRunnable exit due to mMorphoPanoramaGP3 is null");
                            return;
                        }
                        Panorama3Module.this.mPanoramaState = new PanoramaPreview(Panorama3Module.this, this.mThumbnailViewSize);
                        Panorama3Module.this.mPanoramaState.setPanoramaStateEventListener(DecideDirection.this.listener);
                        DecideDirection.this.clearListener();
                    }
                }
            }

            private DecideDirectionAttach() {
            }

            /* JADX WARNING: type inference failed for: r13v1, types: [com.android.camera.panorama.DirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v10, types: [com.android.camera.panorama.LeftDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v17, types: [com.android.camera.panorama.RightDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v24, types: [com.android.camera.panorama.RightDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v31, types: [com.android.camera.panorama.LeftDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v40, types: [com.android.camera.panorama.DownDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v47, types: [com.android.camera.panorama.UpDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v56, types: [com.android.camera.panorama.UpDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v63, types: [com.android.camera.panorama.DownDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v74, types: [com.android.camera.panorama.DownDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v81, types: [com.android.camera.panorama.UpDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v88, types: [com.android.camera.panorama.UpDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v95, types: [com.android.camera.panorama.DownDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v104, types: [com.android.camera.panorama.RightDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v111, types: [com.android.camera.panorama.LeftDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v120, types: [com.android.camera.panorama.LeftDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v127, types: [com.android.camera.panorama.RightDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v128, types: [com.android.camera.panorama.LeftDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v129, types: [com.android.camera.panorama.RightDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v130, types: [com.android.camera.panorama.RightDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v131, types: [com.android.camera.panorama.LeftDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v132, types: [com.android.camera.panorama.DownDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v133, types: [com.android.camera.panorama.UpDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v134, types: [com.android.camera.panorama.UpDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v135, types: [com.android.camera.panorama.DownDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v136, types: [com.android.camera.panorama.DownDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v137, types: [com.android.camera.panorama.UpDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v138, types: [com.android.camera.panorama.UpDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v139, types: [com.android.camera.panorama.DownDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v140, types: [com.android.camera.panorama.RightDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v141, types: [com.android.camera.panorama.LeftDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v142, types: [com.android.camera.panorama.LeftDirectionFunction] */
            /* JADX WARNING: type inference failed for: r0v143, types: [com.android.camera.panorama.RightDirectionFunction] */
            /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v128, types: [com.android.camera.panorama.LeftDirectionFunction]
  assigns: [com.android.camera.panorama.LeftDirectionFunction, com.android.camera.panorama.RightDirectionFunction, com.android.camera.panorama.DownDirectionFunction, com.android.camera.panorama.UpDirectionFunction]
  uses: [com.android.camera.panorama.LeftDirectionFunction, com.android.camera.panorama.DirectionFunction, com.android.camera.panorama.RightDirectionFunction, com.android.camera.panorama.DownDirectionFunction, com.android.camera.panorama.UpDirectionFunction]
  mth insns count: 430
            	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
            	at java.util.ArrayList.forEach(ArrayList.java:1259)
            	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
            	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
            	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.util.ArrayList.forEach(ArrayList.java:1259)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.util.ArrayList.forEach(ArrayList.java:1259)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.util.ArrayList.forEach(ArrayList.java:1259)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:30)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
            	at java.util.ArrayList.forEach(ArrayList.java:1259)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
            	at jadx.core.ProcessClass.process(ProcessClass.java:35)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
             */
            /* JADX WARNING: Unknown variable types count: 17 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            private void createDirection(int i, Size size) {
                ? r13;
                Panorama3Module panorama3Module;
                String str = "direction : DIRECTION_HORIZONTAL_RIGHT ";
                String str2 = "direction : HORIZONTAL_LEFT ";
                String str3 = "direction : VERTICAL_DOWN";
                String str4 = "direction : VERTICAL_UP";
                if (Panorama3Module.this.mInitParam.output_rotation == 90 || Panorama3Module.this.mInitParam.output_rotation == 270) {
                    if (i == 3) {
                        Log.i(Panorama3Module.TAG, str4);
                        float scaleV = (float) getScaleV();
                        if (Panorama3Module.this.mCameraOrientation == 90) {
                            panorama3Module = Panorama3Module.this;
                            ? rightDirectionFunction = new RightDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleV, Panorama3Module.this.mInitParam.output_rotation);
                            r13 = rightDirectionFunction;
                        } else {
                            panorama3Module = Panorama3Module.this;
                            ? leftDirectionFunction = new LeftDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleV, Panorama3Module.this.mInitParam.output_rotation);
                            r13 = leftDirectionFunction;
                        }
                    } else if (i == 4) {
                        Log.i(Panorama3Module.TAG, str3);
                        float scaleV2 = (float) getScaleV();
                        if (Panorama3Module.this.mCameraOrientation == 90) {
                            panorama3Module = Panorama3Module.this;
                            ? leftDirectionFunction2 = new LeftDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleV2, Panorama3Module.this.mInitParam.output_rotation);
                            r13 = leftDirectionFunction2;
                        } else {
                            panorama3Module = Panorama3Module.this;
                            ? rightDirectionFunction2 = new RightDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleV2, Panorama3Module.this.mInitParam.output_rotation);
                            r13 = rightDirectionFunction2;
                        }
                    } else if (i == 5) {
                        String access$400 = Panorama3Module.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append(Panorama3Module.this.mCameraOrientation);
                        Log.i(access$400, sb.toString());
                        float scaleV3 = (float) getScaleV();
                        if (Panorama3Module.this.mCameraOrientation == 90) {
                            float access$2300 = (float) (Panorama3Module.this.mPictureHeight / size.getWidth());
                            panorama3Module = Panorama3Module.this;
                            ? upDirectionFunction = new UpDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, access$2300, Panorama3Module.this.mInitParam.output_rotation);
                            r13 = upDirectionFunction;
                        } else {
                            panorama3Module = Panorama3Module.this;
                            ? downDirectionFunction = new DownDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleV3, Panorama3Module.this.mInitParam.output_rotation);
                            r13 = downDirectionFunction;
                        }
                    } else if (i == 6) {
                        String access$4002 = Panorama3Module.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(Panorama3Module.this.mCameraOrientation);
                        Log.i(access$4002, sb2.toString());
                        float scaleV4 = (float) getScaleV();
                        if (Panorama3Module.this.mCameraOrientation == 90) {
                            float access$23002 = (float) (Panorama3Module.this.mPictureHeight / size.getWidth());
                            panorama3Module = Panorama3Module.this;
                            ? downDirectionFunction2 = new DownDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, access$23002, Panorama3Module.this.mInitParam.output_rotation);
                            r13 = downDirectionFunction2;
                        } else {
                            panorama3Module = Panorama3Module.this;
                            ? upDirectionFunction2 = new UpDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleV4, Panorama3Module.this.mInitParam.output_rotation);
                            r13 = upDirectionFunction2;
                        }
                    } else {
                        return;
                    }
                } else if (i == 3) {
                    Log.i(Panorama3Module.TAG, str4);
                    float scaleH = (float) getScaleH();
                    if (Panorama3Module.this.mCameraOrientation == 90) {
                        panorama3Module = Panorama3Module.this;
                        ? upDirectionFunction3 = new UpDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleH, Panorama3Module.this.mInitParam.output_rotation);
                        r13 = upDirectionFunction3;
                    } else {
                        panorama3Module = Panorama3Module.this;
                        ? downDirectionFunction3 = new DownDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleH, Panorama3Module.this.mInitParam.output_rotation);
                        r13 = downDirectionFunction3;
                    }
                } else if (i == 4) {
                    Log.i(Panorama3Module.TAG, str3);
                    float scaleH2 = (float) getScaleH();
                    if (Panorama3Module.this.mCameraOrientation == 90) {
                        panorama3Module = Panorama3Module.this;
                        ? downDirectionFunction4 = new DownDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleH2, Panorama3Module.this.mInitParam.output_rotation);
                        r13 = downDirectionFunction4;
                    } else {
                        panorama3Module = Panorama3Module.this;
                        ? upDirectionFunction4 = new UpDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleH2, Panorama3Module.this.mInitParam.output_rotation);
                        r13 = upDirectionFunction4;
                    }
                } else if (i == 5) {
                    String access$4003 = Panorama3Module.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str2);
                    sb3.append(Panorama3Module.this.mCameraOrientation);
                    Log.i(access$4003, sb3.toString());
                    float scaleH3 = (float) getScaleH();
                    if (Panorama3Module.this.mCameraOrientation == 90) {
                        float access$23003 = (float) (Panorama3Module.this.mPictureHeight / size.getWidth());
                        panorama3Module = Panorama3Module.this;
                        ? leftDirectionFunction3 = new LeftDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, access$23003, Panorama3Module.this.mInitParam.output_rotation);
                        r13 = leftDirectionFunction3;
                    } else {
                        panorama3Module = Panorama3Module.this;
                        ? rightDirectionFunction3 = new RightDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleH3, Panorama3Module.this.mInitParam.output_rotation);
                        r13 = rightDirectionFunction3;
                    }
                } else if (i == 6) {
                    String access$4004 = Panorama3Module.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str);
                    sb4.append(Panorama3Module.this.mCameraOrientation);
                    Log.i(access$4004, sb4.toString());
                    float scaleH4 = (float) getScaleH();
                    if (Panorama3Module.this.mCameraOrientation == 90) {
                        float access$23004 = (float) (Panorama3Module.this.mPictureHeight / size.getWidth());
                        panorama3Module = Panorama3Module.this;
                        ? rightDirectionFunction4 = new RightDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, access$23004, Panorama3Module.this.mInitParam.output_rotation);
                        r13 = rightDirectionFunction4;
                    } else {
                        panorama3Module = Panorama3Module.this;
                        ? leftDirectionFunction4 = new LeftDirectionFunction(panorama3Module.mPictureWidth, Panorama3Module.this.mPictureHeight, Panorama3Module.this.mMaxWidth, Panorama3Module.this.mMaxHeight, scaleH4, Panorama3Module.this.mInitParam.output_rotation);
                        r13 = leftDirectionFunction4;
                    }
                } else {
                    return;
                }
                panorama3Module.mDirectionFunction = r13;
            }

            private int getScaleH() {
                return Math.max(1, (Panorama3Module.this.mMaxHeight / Display.getWindowHeight()) * 2);
            }

            private int getScaleV() {
                return Math.max(1, (Panorama3Module.this.mMaxHeight / (CameraSettings.isPanoramaVertical(CameraAppImpl.getAndroidContext()) ? Panorama3Module.this.mSmallPreviewHeightVertical : Panorama3Module.this.mSmallPreviewHeight)) - 1);
            }

            private boolean isUnDecideDirection(int i) {
                return i == -1 || i == 0 || i == 2 || i == 1;
            }

            /* JADX WARNING: Code restructure failed: missing block: B:21:0x00b4, code lost:
                if (r4 != false) goto L_0x00ce;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:22:0x00b6, code lost:
                r4 = com.android.camera.module.Panorama3Module.access$400();
                r5 = new java.lang.StringBuilder();
                r5.append("DecideDirectionAttach error ret:");
                r5.append(r3);
                com.android.camera.log.Log.e(r4, r5.toString());
             */
            /* JADX WARNING: Code restructure failed: missing block: B:23:0x00ce, code lost:
                com.android.camera.log.Log.e(com.android.camera.module.Panorama3Module.access$400(), java.lang.String.format(java.util.Locale.US, "attach error ret:0x%08X", new java.lang.Object[]{java.lang.Integer.valueOf(r3)}));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
                closeSrc();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:27:0x00e9, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
                closeSrc();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
                closeSrc();
                com.android.camera.module.Panorama3Module.access$3402(r1.this$1.this$0, r3);
                createDirection(r3, com.android.camera.module.Panorama3Module.access$1500(r1.this$1.this$0));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:51:0x01d4, code lost:
                if (com.android.camera.module.Panorama3Module.access$3500(r1.this$1.this$0).enabled() == false) goto L_0x0002;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                int i;
                while (true) {
                    try {
                        CaptureImage captureImage = (CaptureImage) Panorama3Module.this.mAttachImageQueue.take();
                        if (captureImage == Panorama3Module.sAttachExit) {
                            break;
                        }
                        try {
                            setImage(captureImage);
                            synchronized (Panorama3Module.mEngineLock) {
                                Panorama3Module.this.setSensorFusionValue(captureImage);
                                if (Panorama3Module.this.mRequestStop) {
                                    Log.e(Panorama3Module.TAG, "DecideDirectionAttach request stop");
                                    closeSrc();
                                    return;
                                }
                                Log.d(Panorama3Module.TAG, "DecideDirectionAttach attach start");
                                int attach = Panorama3Module.this.mMorphoPanoramaGP3.attach(this.byteBuffer[0], this.byteBuffer[1], this.byteBuffer[2], this.rowStride[0], this.rowStride[1], this.rowStride[2], this.pixelStride[0], this.pixelStride[1], this.pixelStride[2], Panorama3Module.this.mCurrentSensorInfoManager, null, Panorama3Module.this.getActivity().getApplicationContext());
                                String access$400 = Panorama3Module.TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("DecideDirectionAttach attach end, resultCode = ");
                                sb.append(attach);
                                Log.d(access$400, sb.toString());
                                boolean z = attach == -1073741823;
                                if (attach != 0) {
                                    break;
                                }
                                if (isUnDecideDirection(Panorama3Module.this.mInitParam.direction)) {
                                    Log.e(Panorama3Module.TAG, "DecideDirectionAttach isUnDecideDirection");
                                    i = Panorama3Module.this.mMorphoPanoramaGP3.getDirection();
                                    if (i == Panorama3Module.this.mInitParam.direction) {
                                    }
                                } else {
                                    i = Panorama3Module.this.mInitParam.direction;
                                }
                                String access$4002 = Panorama3Module.TAG;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("getDirection = ");
                                sb2.append(i);
                                Log.d(access$4002, sb2.toString());
                                int[] iArr = new int[2];
                                int outputImageSize = Panorama3Module.this.mMorphoPanoramaGP3.getOutputImageSize(iArr);
                                if (outputImageSize != 0) {
                                    Log.e(Panorama3Module.TAG, String.format(Locale.US, "getOutputImageSize error ret:0x%08X", new Object[]{Integer.valueOf(outputImageSize)}));
                                    closeSrc();
                                    return;
                                }
                                Panorama3Module.this.mMaxWidth = iArr[0];
                                Panorama3Module.this.mMaxHeight = iArr[1];
                                String access$4003 = Panorama3Module.TAG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("mMaxWidth = ");
                                sb3.append(Panorama3Module.this.mMaxWidth);
                                sb3.append(", mMaxHeight = ");
                                sb3.append(Panorama3Module.this.mMaxHeight);
                                Log.d(access$4003, sb3.toString());
                            }
                        } catch (Throwable th) {
                            closeSrc();
                            throw th;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Panorama3Module.this.getActivity().runOnUiThread(new DecideRunnable(Panorama3Module.this.mThumbnailViewSize));
                Log.d(Panorama3Module.TAG, "DecideDirectionAttach end");
            }
        }

        private DecideDirection() {
            this.mHasSubmit = false;
        }

        public boolean onSaveImage(CaptureImage captureImage) {
            Panorama3Module.this.addAttachQueue(captureImage);
            if (!this.mHasSubmit) {
                Log.d(Panorama3Module.TAG, "submit DecideDirectionAttach");
                Panorama3Module.this.mExecutor.submit(new DecideDirectionAttach());
                this.mHasSubmit = true;
            }
            return true;
        }
    }

    class MainHandler extends Handler {
        private WeakReference mModule;

        public MainHandler(Panorama3Module panorama3Module, Looper looper) {
            super(looper);
            this.mModule = new WeakReference(panorama3Module);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:36:0x009f, code lost:
            if (r5.isActivityPaused() == false) goto L_0x007d;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(Message message) {
            Panorama3Module panorama3Module = (Panorama3Module) this.mModule.get();
            if (panorama3Module != null) {
                if (message.what == 45) {
                    Log.d(Panorama3Module.TAG, "onMessage MSG_ABANDON_HANDLER setActivity null");
                    panorama3Module.setActivity(null);
                }
                if (!panorama3Module.isCreated()) {
                    removeCallbacksAndMessages(null);
                } else if (panorama3Module.getActivity() != null) {
                    int i = message.what;
                    if (i != 2) {
                        if (i != 17) {
                            if (i == 51) {
                                Camera camera = panorama3Module.mActivity;
                                if (camera != null) {
                                }
                            } else if (i == 9) {
                                CameraSize cameraSize = panorama3Module.mPreviewSize;
                                int uIStyleByPreview = CameraSettings.getUIStyleByPreview(cameraSize.width, cameraSize.height);
                                if (uIStyleByPreview != panorama3Module.mUIStyle) {
                                    panorama3Module.mUIStyle = uIStyleByPreview;
                                }
                                panorama3Module.initPreviewLayout();
                            } else if (i != 10) {
                                String str = "no consumer for this message: ";
                                if (!BaseModule.DEBUG) {
                                    String access$400 = Panorama3Module.TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(str);
                                    sb.append(message.what);
                                    Log.e(access$400, sb.toString());
                                } else {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(str);
                                    sb2.append(message.what);
                                    throw new RuntimeException(sb2.toString());
                                }
                            }
                            panorama3Module.mOpenCameraFail = true;
                            panorama3Module.onCameraException();
                        } else {
                            removeMessages(17);
                            removeMessages(2);
                            panorama3Module.getWindow().addFlags(128);
                            sendEmptyMessageDelayed(2, (long) panorama3Module.getScreenDelay());
                        }
                    }
                    panorama3Module.getWindow().clearFlags(128);
                }
            }
        }
    }

    class MySensorEventListener implements SensorEventListener {
        private WeakReference mRefSensorListener;

        public MySensorEventListener(SensorListener sensorListener) {
            this.mRefSensorListener = new WeakReference(sensorListener);
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            SensorListener sensorListener = (SensorListener) this.mRefSensorListener.get();
            if (sensorListener != null) {
                sensorListener.onSensorChanged(sensorEvent);
            }
        }
    }

    class PanoramaFirst extends PanoramaState {
        private PanoramaFirst() {
        }

        public boolean onSaveImage(CaptureImage captureImage) {
            String access$400;
            String sb;
            captureImage.close();
            Panorama3Module.this.setNullDirectionFunction();
            if (Panorama3Module.this.mRequestStop) {
                access$400 = Panorama3Module.TAG;
                sb = "PanoramaFirst.onSaveImage request stop";
            } else {
                Panorama3Module.this.configMorphoPanoramaGP3();
                int start = Panorama3Module.this.mMorphoPanoramaGP3.start(Panorama3Module.this.mPictureWidth, Panorama3Module.this.mPictureHeight);
                if (start != 0) {
                    access$400 = Panorama3Module.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("start error resultCode:");
                    sb2.append(start);
                    sb = sb2.toString();
                } else {
                    Panorama3Module panorama3Module = Panorama3Module.this;
                    panorama3Module.mPanoramaState = new DecideDirection();
                    Panorama3Module.this.mPanoramaState.setPanoramaStateEventListener(this.listener);
                    clearListener();
                    return true;
                }
            }
            Log.e(access$400, sb);
            return false;
        }
    }

    class PanoramaInit extends PanoramaState {
        private double aspectRatio;

        public PanoramaInit() {
            float f;
            float f2;
            int min = Math.min(Panorama3Module.this.mThumbnailViewSize.getWidth(), Panorama3Module.this.mThumbnailViewSize.getHeight());
            int max = Math.max(Panorama3Module.this.mThumbnailViewSize.getWidth(), Panorama3Module.this.mThumbnailViewSize.getHeight());
            if (Panorama3Module.this.mIsVertical) {
                f = ((float) max) / ((float) min);
                f2 = 0.9f;
            } else {
                f = ((float) max) / ((float) min);
                f2 = 0.8f;
            }
            this.aspectRatio = (double) (f * f2);
        }

        public boolean onSaveImage(CaptureImage captureImage) {
            Panorama3Module.this.mImageFormat = captureImage.getImageFormat();
            String access$400 = Panorama3Module.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("PanoramaInit onSaveImage start, ImageFormat :");
            sb.append(Panorama3Module.this.mImageFormat);
            Log.d(access$400, sb.toString());
            if (Panorama3Module.this.mRequestStop) {
                Log.w(Panorama3Module.TAG, "mRequestStop when PanoramaInit");
                captureImage.close();
                return false;
            }
            synchronized (Panorama3Module.mEngineLock) {
                if (Panorama3Module.this.createEngine(this.aspectRatio)) {
                    int inputImageFormat = Panorama3Module.this.mMorphoPanoramaGP3.setInputImageFormat(Panorama3Module.this.mImageFormat);
                    if (inputImageFormat != 0) {
                        String access$4002 = Panorama3Module.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("setInputImageFormat error resultCode:");
                        sb2.append(inputImageFormat);
                        Log.e(access$4002, sb2.toString());
                    }
                    Panorama3Module.this.mPanoramaState = new PanoramaFirst();
                    Panorama3Module.this.mPanoramaState.setPanoramaStateEventListener(this.listener);
                    clearListener();
                    Panorama3Module.this.mPanoramaState.onSaveImage(captureImage);
                    Log.d(Panorama3Module.TAG, "PanoramaInit onSaveImage end");
                    return true;
                }
                captureImage.close();
                return true;
            }
        }
    }

    class PanoramaPreview extends PanoramaState {
        /* access modifiers changed from: private */
        public PositionDetector mDetector;
        private boolean mHasSubmit;
        private int mPreviewImgHeight = 0;
        private int mPreviewImgWidth = 0;
        private Size mThumbnailViewSize;
        /* access modifiers changed from: private */
        public final UiUpdateRunnable mUiUpdateRunnable = new UiUpdateRunnable();
        final /* synthetic */ Panorama3Module this$0;

        class PreviewAttach extends AttachRunnable {
            private InputSave mInputSave;
            private boolean mIsAttachEnd;
            private final PostAttachRunnable mPostAttachRunnable;
            private int mResultCode;

            class PostAttachRunnable implements Runnable {
                private PostAttachRunnable() {
                }

                public void run() {
                    if (!PanoramaPreview.this.this$0.mPaused && !PanoramaPreview.this.this$0.mRequestStop) {
                        PanoramaPreview.this.this$0.onPreviewMoving();
                        if (!PanoramaPreview.this.this$0.mCaptureDirectionDecided) {
                            PanoramaPreview.this.this$0.onCaptureDirectionDecided();
                        }
                        PanoramaProtocol panoramaProtocol = (PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176);
                        synchronized (Panorama3Module.mPreviewImageLock) {
                            if (panoramaProtocol != null) {
                                if (PanoramaPreview.this.this$0.mDispPreviewImage != null) {
                                    panoramaProtocol.setDisplayPreviewBitmap(PanoramaPreview.this.this$0.mDispPreviewImage);
                                }
                            }
                        }
                    }
                }
            }

            private PreviewAttach() {
                this.mIsAttachEnd = false;
                this.mPostAttachRunnable = new PostAttachRunnable();
                this.mInputSave = new InputSave();
            }

            private void checkAttachEnd(double[] dArr) {
                int detect = PanoramaPreview.this.mDetector.detect(dArr[0], dArr[1]);
                String access$400 = Panorama3Module.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("checkAttachEnd detect_result = ");
                sb.append(detect);
                Log.v(access$400, sb.toString());
                if (detect == -3 || detect == -2 || detect == -1 || detect == 1) {
                    this.mResultCode = 0;
                    this.mIsAttachEnd = true;
                }
                PanoramaPreview.this.mUiUpdateRunnable.setDetectResult(detect);
                PanoramaPreview.this.this$0.getActivity().runOnUiThread(PanoramaPreview.this.mUiUpdateRunnable);
                boolean z = this.mIsAttachEnd;
            }

            private void updatePreviewImage() {
                Rect rect;
                synchronized (Panorama3Module.mPreviewImageLock) {
                    int updatePreviewImage = PanoramaPreview.this.this$0.mMorphoPanoramaGP3.updatePreviewImage(PanoramaPreview.this.this$0.mPreviewImage);
                    if (updatePreviewImage != 0) {
                        String access$400 = Panorama3Module.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("updatePreviewImage error ret:");
                        sb.append(updatePreviewImage);
                        Log.e(access$400, sb.toString());
                    } else if (PanoramaPreview.this.this$0.mPreviewImage == null) {
                        Log.w(Panorama3Module.TAG, "mPreviewImage is null when updatePreviewImage");
                    } else {
                        int width = PanoramaPreview.this.this$0.mDispPreviewImage.getWidth();
                        int height = PanoramaPreview.this.this$0.mDispPreviewImage.getHeight();
                        int i = 180;
                        if ((PanoramaPreview.this.this$0.mPreviewImage.getWidth() <= PanoramaPreview.this.this$0.mPreviewImage.getHeight() || width <= height) && (PanoramaPreview.this.this$0.mPreviewImage.getWidth() >= PanoramaPreview.this.this$0.mPreviewImage.getHeight() || width >= height)) {
                            i = ((PanoramaPreview.this.this$0.mInitParam.direction == 5 || PanoramaPreview.this.this$0.mInitParam.direction == 6 || PanoramaPreview.this.this$0.mInitParam.direction == 3 || PanoramaPreview.this.this$0.mInitParam.direction == 4) && PanoramaPreview.this.this$0.mInitParam.output_rotation == 180) ? 270 : 90;
                        } else if ((PanoramaPreview.this.this$0.mInitParam.direction != 5 && PanoramaPreview.this.this$0.mInitParam.direction != 6 && PanoramaPreview.this.this$0.mInitParam.direction != 3 && PanoramaPreview.this.this$0.mInitParam.direction != 4) || PanoramaPreview.this.this$0.mInitParam.output_rotation != 270) {
                            i = 0;
                        }
                        Bitmap access$4500 = PanoramaPreview.this.this$0.mPreviewImage;
                        if (i != 0) {
                            Matrix matrix = new Matrix();
                            matrix.postRotate((float) i);
                            access$4500 = Bitmap.createBitmap(PanoramaPreview.this.this$0.mPreviewImage, 0, 0, PanoramaPreview.this.this$0.mPreviewImage.getWidth(), PanoramaPreview.this.this$0.mPreviewImage.getHeight(), matrix, true);
                        }
                        Rect rect2 = new Rect(0, 0, width, height);
                        if (width > height) {
                            int width2 = access$4500.getWidth();
                            int i2 = (int) (((float) width2) / (((float) width) / ((float) height)));
                            int height2 = (i2 - access$4500.getHeight()) / 2;
                            rect = new Rect(0, height2, width2, i2 + height2);
                        } else {
                            int height3 = access$4500.getHeight();
                            rect = new Rect(0, 0, ((int) (((float) height3) * (((float) width) / ((float) height)))) + 0, height3);
                        }
                        PanoramaPreview.this.this$0.mDispPreviewImageCanvas.drawBitmap(access$4500, rect, rect2, PanoramaPreview.this.this$0.mDispPreviewImagePaint);
                        String access$4002 = Panorama3Module.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("updatePreviewImage src ");
                        sb2.append(rect);
                        sb2.append(", dst = ");
                        sb2.append(rect2);
                        Log.d(access$4002, sb2.toString());
                    }
                }
            }

            /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
                r1.this$1.this$0.getActivity().runOnUiThread(r1.mPostAttachRunnable);
                r0 = r18;
                checkAttachEnd(r0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:34:0x010c, code lost:
                if (r1.mIsAttachEnd == false) goto L_0x0118;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:35:0x010e, code lost:
                com.android.camera.log.Log.d(com.android.camera.module.Panorama3Module.access$400(), "preview attach end");
             */
            /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
                closeSrc();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:45:0x0123, code lost:
                r0 = th;
             */
            /* JADX WARNING: Removed duplicated region for block: B:58:0x0146  */
            /* JADX WARNING: Removed duplicated region for block: B:61:0x0150  */
            /* JADX WARNING: Unknown top exception splitter block from list: {B:49:0x0129=Splitter:B:49:0x0129, B:27:0x00ba=Splitter:B:27:0x00ba} */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                String access$400;
                String str;
                int i;
                double[] dArr;
                Log.d(Panorama3Module.TAG, "PreviewAttach.run start");
                char c = 2;
                double[] dArr2 = new double[2];
                while (true) {
                    try {
                        CaptureImage captureImage = (CaptureImage) PanoramaPreview.this.this$0.mAttachImageQueue.take();
                        if (captureImage == Panorama3Module.sAttachExit) {
                            break;
                        }
                        try {
                            setImage(captureImage);
                            if (Panorama3Module.DUMP_YUV) {
                                this.mInputSave.onSaveImage(captureImage, PanoramaPreview.this.this$0.mImageFormat);
                            }
                            synchronized (Panorama3Module.mEngineLock) {
                                try {
                                    if (PanoramaPreview.this.this$0.mRequestStop) {
                                        Log.w(Panorama3Module.TAG, "PreviewAttach request stop");
                                        closeSrc();
                                        return;
                                    }
                                    Log.v(Panorama3Module.TAG, "PreviewAttach attach start");
                                    PanoramaPreview.this.this$0.setSensorFusionValue(captureImage);
                                    double[] dArr3 = dArr2;
                                    double[] dArr4 = dArr2;
                                    i = -1;
                                    try {
                                        if (PanoramaPreview.this.this$0.mMorphoPanoramaGP3.attach(this.byteBuffer[0], this.byteBuffer[1], this.byteBuffer[c], this.rowStride[0], this.rowStride[1], this.rowStride[c], this.pixelStride[0], this.pixelStride[1], this.pixelStride[c], PanoramaPreview.this.this$0.mCurrentSensorInfoManager, dArr3, PanoramaPreview.this.this$0.getActivity()) != 0) {
                                            Log.e(Panorama3Module.TAG, "PreviewAttach attach error.");
                                            this.mResultCode = -1;
                                        } else {
                                            Log.v(Panorama3Module.TAG, "PreviewAttach attach end");
                                            PanoramaPreview.this.this$0.mCanSavePanorama = true;
                                            updatePreviewImage();
                                            String access$4002 = Panorama3Module.TAG;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("mCenter = ");
                                            sb.append(dArr4[0]);
                                            sb.append(", ");
                                            sb.append(dArr4[1]);
                                            Log.v(access$4002, sb.toString());
                                        }
                                    } catch (Throwable th) {
                                        th = th;
                                        throw th;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    i = -1;
                                    throw th;
                                }
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            i = -1;
                            closeSrc();
                            throw th;
                        }
                        dArr2 = dArr;
                        c = 2;
                    } catch (InterruptedException e) {
                        e = e;
                        i = -1;
                        Log.w(Panorama3Module.TAG, "PreviewAttach interrupted", (Throwable) e);
                        this.mResultCode = i;
                        if (!PanoramaPreview.this.this$0.mRequestStop) {
                        }
                        Log.d(access$400, str);
                    }
                }
                try {
                    closeSrc();
                } catch (InterruptedException e2) {
                    e = e2;
                }
                if (!PanoramaPreview.this.this$0.mRequestStop) {
                    access$400 = Panorama3Module.TAG;
                    str = "PreviewAttach exit. (request exit)";
                } else {
                    final int i2 = this.mResultCode;
                    PanoramaPreview.this.this$0.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            PanoramaPreview.this.attachEnd(i2);
                        }
                    });
                    access$400 = Panorama3Module.TAG;
                    str = "PreviewAttach exit.";
                }
                Log.d(access$400, str);
            }
        }

        class UiUpdateRunnable implements Runnable {
            private int mDetectResult;

            private UiUpdateRunnable() {
            }

            /* JADX WARNING: Code restructure failed: missing block: B:30:0x00ba, code lost:
                com.android.camera.module.Panorama3Module.access$6202(r6.this$1.this$0, java.lang.Math.min(r2, r4) / 2);
                r0 = (com.android.camera.protocol.ModeProtocol.PanoramaProtocol) com.android.camera.protocol.ModeCoordinatorImpl.getInstance().getAttachProtocol(176);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:31:0x00d3, code lost:
                if (r0 == null) goto L_0x00ee;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:32:0x00d5, code lost:
                r0.setDirectionPosition(r1, com.android.camera.module.Panorama3Module.access$6200(r6.this$1.this$0));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:33:0x00e2, code lost:
                if (r6.mDetectResult == 2) goto L_0x00e9;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:34:0x00e4, code lost:
                r0.setDirectionTooFast(false, 0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:35:0x00e9, code lost:
                r0.setDirectionTooFast(true, com.android.camera.constant.DurationConstant.DURATION_LANDSCAPE_HINT);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:36:0x00ee, code lost:
                return;
             */
            /* JADX WARNING: Removed duplicated region for block: B:22:0x008c A[SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                float f;
                float f2;
                int i = this.mDetectResult;
                if (i == -2 || i == -3 || i == -1 || i == 1) {
                    if (this.mDetectResult != 1) {
                        String access$400 = Panorama3Module.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("stopPanoramaShooting due to detect result ");
                        sb.append(this.mDetectResult);
                        Log.w(access$400, sb.toString());
                    }
                    int i2 = this.mDetectResult;
                    String str = i2 != -3 ? i2 != -2 ? i2 != 1 ? "unknown" : Panorama.STOP_CAPTURE_MODE_COMPLETE : Panorama.STOP_CAPTURE_MODE_REVERSE : Panorama.STOP_CAPTURE_MODE_OUT_OF_RANGE;
                    PanoramaPreview.this.this$0.stopPanoramaShooting(true, str);
                    return;
                }
                RectF frameRect = PanoramaPreview.this.mDetector.getFrameRect();
                String access$4002 = Panorama3Module.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("frame_rect = ");
                sb2.append(frameRect);
                Log.v(access$4002, sb2.toString());
                Point point = new Point();
                if (PanoramaPreview.this.this$0.mDirection == 3) {
                    f2 = frameRect.right;
                } else if (PanoramaPreview.this.this$0.mDirection == 4) {
                    f2 = frameRect.left;
                } else {
                    if (PanoramaPreview.this.this$0.mDirection == 5) {
                        point.x = (int) frameRect.centerX();
                        f = frameRect.top;
                    } else {
                        if (PanoramaPreview.this.this$0.mDirection == 6) {
                            point.x = (int) frameRect.centerX();
                            f = frameRect.bottom;
                        }
                        synchronized (Panorama3Module.mPreviewImageLock) {
                            if (PanoramaPreview.this.this$0.mDispPreviewImage == null) {
                                Log.w(Panorama3Module.TAG, "mPreviewImage is null in UiUpdateRunnable");
                                return;
                            } else {
                                int width = PanoramaPreview.this.this$0.mDispPreviewImage.getWidth();
                                int height = PanoramaPreview.this.this$0.mDispPreviewImage.getHeight();
                            }
                        }
                    }
                    point.y = (int) f;
                    synchronized (Panorama3Module.mPreviewImageLock) {
                    }
                }
                point.x = (int) f2;
                f = frameRect.centerY();
                point.y = (int) f;
                synchronized (Panorama3Module.mPreviewImageLock) {
                }
            }

            public void setDetectResult(int i) {
                this.mDetectResult = i;
            }
        }

        @TargetApi(21)
        public PanoramaPreview(Panorama3Module panorama3Module, Size size) {
            int i;
            Panorama3Module panorama3Module2 = panorama3Module;
            this.this$0 = panorama3Module2;
            this.mThumbnailViewSize = size;
            if (panorama3Module.mInitParam.output_rotation % 180 == 90) {
                this.mPreviewImgWidth = size.getWidth();
                i = size.getHeight();
            } else {
                this.mPreviewImgWidth = size.getHeight();
                i = size.getWidth();
            }
            this.mPreviewImgHeight = i;
            String access$400 = Panorama3Module.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mPreviewImgWidth = ");
            sb.append(this.mPreviewImgWidth);
            sb.append(", mPreviewImgHeight = ");
            sb.append(this.mPreviewImgHeight);
            Log.d(access$400, sb.toString());
            int previewImage = panorama3Module.mMorphoPanoramaGP3.setPreviewImage(this.mPreviewImgWidth, this.mPreviewImgHeight);
            if (previewImage != 0) {
                Log.e(Panorama3Module.TAG, String.format(Locale.US, "setPreviewImage error ret:0x%08X", new Object[]{Integer.valueOf(previewImage)}));
            }
            PanoramaProtocol panoramaProtocol = (PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176);
            if (panoramaProtocol != null) {
                PositionDetector positionDetector = new PositionDetector(panorama3Module.mInitParam, panoramaProtocol.getPreivewContainer(), false, panorama3Module2.mCameraDisplayOrientation, panorama3Module.mPictureWidth, panorama3Module.mPictureHeight, panorama3Module.mDirectionFunction.getDirection(), panorama3Module.mMaxWidth, panorama3Module.mMaxHeight);
                this.mDetector = positionDetector;
                panorama3Module.mRoundDetector.setStartPosition(panorama3Module.mInitParam.output_rotation, 1, panorama3Module.mViewAngleH, panorama3Module.mViewAngleV, false);
                allocateDisplayBuffers();
            }
        }

        private void allocateDisplayBuffers() {
            synchronized (Panorama3Module.mPreviewImageLock) {
                if (!(this.this$0.mPreviewImage == null || (this.this$0.mPreviewImage.getWidth() == this.mPreviewImgWidth && this.this$0.mPreviewImage.getHeight() == this.mPreviewImgHeight))) {
                    this.this$0.mPreviewImage.recycle();
                    this.this$0.mPreviewImage = null;
                }
                if (!(this.this$0.mDispPreviewImage == null || (this.this$0.mDispPreviewImage.getWidth() == this.mThumbnailViewSize.getWidth() && this.this$0.mDispPreviewImage.getHeight() == this.mThumbnailViewSize.getHeight()))) {
                    this.this$0.mDispPreviewImage.recycle();
                    this.this$0.mDispPreviewImage = null;
                }
                if (this.this$0.mPreviewImage == null) {
                    this.this$0.mPreviewImage = Bitmap.createBitmap(this.mPreviewImgWidth, this.mPreviewImgHeight, Config.ARGB_8888);
                }
                if (this.this$0.mDispPreviewImage == null) {
                    this.this$0.mDispPreviewImage = Bitmap.createBitmap(this.mThumbnailViewSize.getWidth(), this.mThumbnailViewSize.getHeight(), Config.ARGB_8888);
                    this.this$0.mAttachPosOffsetY = ((this.this$0.mDispPreviewImage.getWidth() * this.this$0.mPictureWidth) / this.this$0.mPictureHeight) / 2;
                    Log.d(Panorama3Module.TAG, "mDispPreviewImage %s x %s mPicture %s x %s mAttachPosOffsetY %s", Integer.valueOf(this.this$0.mDispPreviewImage.getWidth()), Integer.valueOf(this.this$0.mDispPreviewImage.getHeight()), Integer.valueOf(this.this$0.mPictureWidth), Integer.valueOf(this.this$0.mPictureHeight), Integer.valueOf(this.this$0.mAttachPosOffsetY));
                    this.this$0.mDispPreviewImageCanvas = new Canvas(this.this$0.mDispPreviewImage);
                    this.this$0.mDispPreviewImagePaint = new Paint();
                    this.this$0.mDispPreviewImagePaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
                }
            }
        }

        /* access modifiers changed from: private */
        public void attachEnd(int i) {
            this.this$0.initAttachQueue();
            this.listener.requestEnd(this, i);
            if (i == 0) {
                this.this$0.stopPanoramaShooting(true, Panorama.STOP_CAPTURE_MODE_UN_EXPECT);
            }
        }

        public boolean onSaveImage(CaptureImage captureImage) {
            this.this$0.addAttachQueue(captureImage);
            if (!this.mHasSubmit) {
                Log.d(Panorama3Module.TAG, "submit PreviewAttach");
                this.this$0.mExecutor.submit(new PreviewAttach());
                this.mHasSubmit = true;
            }
            return true;
        }
    }

    class SaveOutputImageTask extends AsyncTask {
        private boolean mSaveImage;
        private String mStopMode;
        private long start_time;

        SaveOutputImageTask(boolean z, String str) {
            this.mSaveImage = z;
            this.mStopMode = str;
        }

        private void savePanoramaPicture() {
            long currentTimeMillis = System.currentTimeMillis();
            Log.d(Panorama3Module.TAG, "savePanoramaPicture start");
            synchronized (Panorama3Module.mEngineLock) {
                try {
                    Log.d(Panorama3Module.TAG, "savePanoramaPicture enter mEngineLock");
                    if (Panorama3Module.this.mMorphoPanoramaGP3 == null) {
                        Log.w(Panorama3Module.TAG, "savePanoramaPicture while mMorphoPanoramaGP3 is null");
                        Panorama3Module.this.finishEngine();
                    } else if (!this.mSaveImage) {
                        Log.w(Panorama3Module.TAG, String.format("savePanoramaPicture, don't save image", new Object[0]));
                        int end = Panorama3Module.this.mMorphoPanoramaGP3.end(1, (double) Panorama3Module.this.mRoundDetector.currentDegree0Base());
                        if (end != 0) {
                            Log.e(Panorama3Module.TAG, String.format("savePanoramaPicture, end() -> 0x%x", new Object[]{Integer.valueOf(end)}));
                        }
                        Panorama3Module.this.finishEngine();
                    } else {
                        int noiseReductionParam = Panorama3Module.this.mMorphoPanoramaGP3.setNoiseReductionParam(0);
                        if (noiseReductionParam != 0) {
                            String access$400 = Panorama3Module.TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("setNoiseReductionParam error ret:");
                            sb.append(noiseReductionParam);
                            Log.e(access$400, sb.toString());
                        }
                        int unsharpStrength = Panorama3Module.this.mMorphoPanoramaGP3.setUnsharpStrength(1536);
                        if (unsharpStrength != 0) {
                            Log.e(Panorama3Module.TAG, String.format(Locale.US, "setUnsharpStrength error ret:0x%08X", new Object[]{Integer.valueOf(unsharpStrength)}));
                        }
                        int end2 = Panorama3Module.this.mMorphoPanoramaGP3.end(1, (double) Panorama3Module.this.mRoundDetector.currentDegree0Base());
                        if (end2 != 0) {
                            Log.e(Panorama3Module.TAG, String.format("savePanoramaPicture, end() -> 0x%x", new Object[]{Integer.valueOf(end2)}));
                            Panorama3Module.this.finishEngine();
                            return;
                        }
                        Rect rect = new Rect();
                        int clippingRect = Panorama3Module.this.mMorphoPanoramaGP3.getClippingRect(rect);
                        if (clippingRect != 0) {
                            Log.e(Panorama3Module.TAG, String.format("getClippingRect() -> 0x%x", new Object[]{Integer.valueOf(clippingRect)}));
                            Panorama3Module.this.finishEngine();
                            return;
                        }
                        int width = rect.width();
                        int height = rect.height();
                        if (width != 0) {
                            if (height != 0) {
                                int createOutputImage = Panorama3Module.this.mMorphoPanoramaGP3.createOutputImage(rect);
                                if (createOutputImage != 0) {
                                    String access$4002 = Panorama3Module.TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("createOutputImage error ret:");
                                    sb2.append(createOutputImage);
                                    Log.e(access$4002, sb2.toString());
                                    Panorama3Module.this.finishEngine();
                                    return;
                                }
                                String access$1000 = Panorama3Module.this.createNameString(Panorama3Module.this.mTimeTaken);
                                String generateFilepath4Image = Storage.generateFilepath4Image(access$1000, false);
                                if (!Panorama3Module.this.savePanoramaFile(generateFilepath4Image, width, height)) {
                                    Panorama3Module.this.finishEngine();
                                    return;
                                }
                                Log.d(Panorama3Module.TAG, "savePanoramaPicture process duration %s ms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                                Panorama3Module.this.addImageAsApplication(generateFilepath4Image, access$1000, width, height, 0, this.mStopMode);
                                Panorama3Module.this.finishEngine();
                                Log.d(Panorama3Module.TAG, "savePanoramaPicture total duration %s ms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                                return;
                            }
                        }
                        String access$4003 = Panorama3Module.TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("getClippingRect() ");
                        sb3.append(rect);
                        Log.e(access$4003, sb3.toString());
                        Panorama3Module.this.finishEngine();
                    }
                } catch (Throwable th) {
                    Panorama3Module.this.finishEngine();
                    throw th;
                }
            }
        }

        /* access modifiers changed from: protected */
        public Integer doInBackground(Void... voidArr) {
            Log.v(Panorama3Module.TAG, "doInBackground>>");
            savePanoramaPicture();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Integer num) {
            Log.d(Panorama3Module.TAG, "PanoramaFinish done");
            Camera camera = Panorama3Module.this.mActivity;
            if (camera != null) {
                AutoLockManager.getInstance(camera).hibernateDelayed();
            }
            if (Panorama3Module.this.mPaused) {
                Panorama3Module.this.mIsShooting = false;
                Log.w(Panorama3Module.TAG, "panorama mode has been paused");
                return;
            }
            if (camera != null) {
                camera.getThumbnailUpdater().updateThumbnailView(true);
            }
            Panorama3Module.this.enableCameraControls(true);
            Panorama3Module.this.mHandler.post(new Runnable() {
                public void run() {
                    Panorama3Module.this.handlePendingScreenSlide();
                }
            });
            Panorama3Module.this.mIsShooting = false;
            Log.d(Panorama3Module.TAG, String.format(Locale.ENGLISH, "[MORTIME] PanoramaFinish time = %d", new Object[]{Long.valueOf(System.currentTimeMillis() - this.start_time)}));
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.start_time = System.currentTimeMillis();
        }
    }

    public interface SensorListener {
        void onSensorChanged(SensorEvent sensorEvent);
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    public Panorama3Module() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "PanoramaThread");
            }
        }, new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                String access$400 = Panorama3Module.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("rejectedExecution ");
                sb.append(runnable);
                Log.w(access$400, sb.toString());
            }
        });
        this.mExecutor = threadPoolExecutor;
        this.mCanSavePanorama = false;
        this.mRequestStop = false;
        this.mIsVertical = false;
    }

    /* access modifiers changed from: private */
    public void addAttachQueue(CaptureImage captureImage) {
        if (captureImage == null) {
            Log.w(TAG, "addAttachQueue failed due to image is null");
            return;
        }
        boolean z = false;
        try {
            if (this.mRequestStop) {
                Log.w(TAG, "addAttachQueue failed due to request stop");
                captureImage.close();
                return;
            }
            z = this.mAttachImageQueue.offer(captureImage);
            while (this.mAttachImageQueue.size() > 1) {
                CaptureImage captureImage2 = (CaptureImage) this.mAttachImageQueue.poll();
                if (captureImage2 != null) {
                    captureImage2.close();
                }
            }
            Log.v(TAG, "addAttachQueue");
        } finally {
            if (!z) {
                captureImage.close();
            }
        }
    }

    /* access modifiers changed from: private */
    public void addImageAsApplication(String str, String str2, int i, int i2, int i3, String str3) {
        Throwable th;
        String str4 = str;
        int i4 = i3;
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        if (!Storage.isUseDocumentMode()) {
            ExifHelper.writeExifByFilePath(str4, i4, currentLocation, this.mTimeTaken);
        } else {
            try {
                ParcelFileDescriptor parcelFileDescriptor = FileCompat.getParcelFileDescriptor(str4, true);
                try {
                    ExifHelper.writeExifByFd(parcelFileDescriptor.getFileDescriptor(), i4, currentLocation, this.mTimeTaken);
                    if (parcelFileDescriptor != null) {
                        $closeResource(null, parcelFileDescriptor);
                    }
                } catch (Throwable th2) {
                    Throwable th3 = th2;
                    if (parcelFileDescriptor != null) {
                        $closeResource(th, parcelFileDescriptor);
                    }
                    throw th3;
                }
            } catch (Exception e) {
                String str5 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("open file failed, filePath ");
                sb.append(str4);
                Log.e(str5, sb.toString(), (Throwable) e);
            }
        }
        boolean z = currentLocation != null;
        Uri addImageForGroupOrPanorama = Storage.addImageForGroupOrPanorama(CameraAppImpl.getAndroidContext(), str, i3, this.mTimeTaken, this.mLocation, i, i2);
        if (addImageForGroupOrPanorama == null) {
            String str6 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("insert MediaProvider failed, attempt to find uri by path, ");
            sb2.append(str4);
            Log.w(str6, sb2.toString());
            addImageForGroupOrPanorama = MediaProviderUtil.getContentUriFromPath(CameraAppImpl.getAndroidContext(), str4);
        }
        Uri uri = addImageForGroupOrPanorama;
        String str7 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("addImageAsApplication uri = ");
        sb3.append(uri);
        sb3.append(", path = ");
        sb3.append(str4);
        Log.d(str7, sb3.toString());
        HashMap hashMap = new HashMap();
        hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(false));
        hashMap.put(Panorama.PARAM_STOP_CAPTURE_MODE, str3);
        boolean z2 = z;
        trackGeneralInfo(hashMap, 1, false, null, z2, 0);
        PictureTakenParameter pictureTakenParameter = new PictureTakenParameter();
        pictureTakenParameter.takenNum = 1;
        pictureTakenParameter.burst = false;
        pictureTakenParameter.location = z2;
        pictureTakenParameter.aiSceneName = null;
        pictureTakenParameter.isEnteringMoon = false;
        pictureTakenParameter.isSelectMoonMode = false;
        pictureTakenParameter.beautyValues = null;
        trackPictureTaken(pictureTakenParameter);
        Camera camera = this.mActivity;
        if (isCreated() && camera != null) {
            camera.getScreenHint().updateHint();
            if (uri != null) {
                camera.onNewUriArrived(uri, str2);
                Thumbnail createThumbnailFromUri = Thumbnail.createThumbnailFromUri(camera, uri, false);
                Util.broadcastNewPicture(camera, uri);
                camera.getThumbnailUpdater().setThumbnail(createThumbnailFromUri, false, false);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean configMorphoPanoramaGP3() {
        Log.d(TAG, "configMorphoPanoramaGP3 start");
        this.mMorphoPanoramaGP3.setAttachEnabled(true);
        this.mMorphoPanoramaGP3.disableSaveInputImages();
        int shrinkRatio = this.mMorphoPanoramaGP3.setShrinkRatio((double) this.mPanoramaSetting.getShrink_ratio());
        if (shrinkRatio != 0) {
            Log.e(TAG, String.format(Locale.US, "setShrinkRatio error ret:0x%08X", new Object[]{Integer.valueOf(shrinkRatio)}));
        }
        int calcseamPixnum = this.mMorphoPanoramaGP3.setCalcseamPixnum(this.mPanoramaSetting.getCalcseam_pixnum());
        if (calcseamPixnum != 0) {
            Log.e(TAG, String.format(Locale.US, "setCalcseamPixnum error ret:0x%08X", new Object[]{Integer.valueOf(calcseamPixnum)}));
        }
        int useDeform = this.mMorphoPanoramaGP3.setUseDeform(this.mPanoramaSetting.isUse_deform());
        if (useDeform != 0) {
            Log.e(TAG, String.format(Locale.US, "setUseDeform error ret:0x%08X", new Object[]{Integer.valueOf(useDeform)}));
        }
        int useLuminanceCorrection = this.mMorphoPanoramaGP3.setUseLuminanceCorrection(this.mPanoramaSetting.isUse_luminance_correction());
        if (useLuminanceCorrection != 0) {
            Log.e(TAG, String.format(Locale.US, "setUseLuminanceCorrection error ret:0x%08X", new Object[]{Integer.valueOf(useLuminanceCorrection)}));
        }
        int seamsearchRatio = this.mMorphoPanoramaGP3.setSeamsearchRatio(this.mPanoramaSetting.getSeamsearch_ratio());
        if (seamsearchRatio != 0) {
            Log.e(TAG, String.format(Locale.US, "setSeamsearchRatio error ret:0x%08X", new Object[]{Integer.valueOf(seamsearchRatio)}));
        }
        int zrotationCoeff = this.mMorphoPanoramaGP3.setZrotationCoeff(this.mPanoramaSetting.getZrotation_coeff());
        if (zrotationCoeff != 0) {
            Log.e(TAG, String.format(Locale.US, "setZrotationCoeff error ret:0x%08X", new Object[]{Integer.valueOf(zrotationCoeff)}));
        }
        int drawThreshold = this.mMorphoPanoramaGP3.setDrawThreshold(this.mPanoramaSetting.getDraw_threshold());
        if (drawThreshold != 0) {
            Log.e(TAG, String.format(Locale.US, "setDrawThreshold error ret:0x%08X", new Object[]{Integer.valueOf(drawThreshold)}));
        }
        int aovGain = this.mMorphoPanoramaGP3.setAovGain(this.mPanoramaSetting.getAov_gain());
        if (aovGain != 0) {
            Log.e(TAG, String.format(Locale.US, "setAovGain error ret:0x%08X", new Object[]{Integer.valueOf(aovGain)}));
        }
        int distortionCorrectionParam = this.mMorphoPanoramaGP3.setDistortionCorrectionParam(this.mPanoramaSetting.getDistortion_k1(), this.mPanoramaSetting.getDistortion_k2(), this.mPanoramaSetting.getDistortion_k3(), this.mPanoramaSetting.getDistortion_k4());
        if (distortionCorrectionParam != 0) {
            Log.e(TAG, String.format(Locale.US, "setDistortionCorrectionParam error ret:0x%08X", new Object[]{Integer.valueOf(distortionCorrectionParam)}));
        }
        int rotationRatio = this.mMorphoPanoramaGP3.setRotationRatio(this.mPanoramaSetting.getRotation_ratio());
        if (rotationRatio != 0) {
            Log.e(TAG, String.format(Locale.US, "setRotationRatio error ret:0x%08X", new Object[]{Integer.valueOf(rotationRatio)}));
        }
        int sensorUseMode = this.mMorphoPanoramaGP3.setSensorUseMode(0);
        if (sensorUseMode != 0) {
            Log.e(TAG, String.format(Locale.US, "setSensorUseMode error ret:0x%08X", new Object[]{Integer.valueOf(sensorUseMode)}));
        }
        int projectionMode = this.mMorphoPanoramaGP3.setProjectionMode(0);
        if (projectionMode != 0) {
            Log.e(TAG, String.format(Locale.US, "setProjectionMode error ret:0x%08X", new Object[]{Integer.valueOf(projectionMode)}));
        }
        int motionDetectionMode = this.mMorphoPanoramaGP3.setMotionDetectionMode(0);
        if (motionDetectionMode != 0) {
            Log.e(TAG, String.format(Locale.US, "setMotionDetectionMode error ret:0x%08X", new Object[]{Integer.valueOf(motionDetectionMode)}));
        }
        Log.d(TAG, "configMorphoPanoramaGP3 end");
        return true;
    }

    public static String createDateStringForAppSeg(long j) {
        Date date = new Date(j);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(date);
    }

    /* access modifiers changed from: private */
    public boolean createEngine(double d) {
        String str;
        InitParam initParam;
        double d2;
        InitParam initParam2;
        if (this.mMorphoPanoramaGP3 != null) {
            Log.w(TAG, "finish prior Engine");
            finishEngine();
        }
        this.mMorphoPanoramaGP3 = new MorphoPanoramaGP3();
        if (PanoramaGP3ImageFormat.YUV420_PLANAR.equals(this.mImageFormat)) {
            initParam = this.mInitParam;
            initParam.input_format = this.mImageFormat;
            str = PanoramaGP3ImageFormat.YUV420_SEMIPLANAR;
        } else {
            initParam = this.mInitParam;
            str = this.mImageFormat;
            initParam.input_format = str;
        }
        initParam.output_format = str;
        InitParam initParam3 = this.mInitParam;
        initParam3.input_width = this.mPictureWidth;
        initParam3.input_height = this.mPictureHeight;
        initParam3.aovx = (double) this.mViewAngleH;
        initParam3.aovy = (double) this.mViewAngleV;
        this.mInitParam.direction = ((PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176)).getCurrentDirection();
        int displayRotation = Util.getDisplayRotation(this.mActivity);
        int i = this.mOrientation;
        if (i == -1) {
            this.mInitParam.output_rotation = ((this.mCameraOrientation + displayRotation) + m.cQ) % m.cQ;
        } else {
            this.mInitParam.output_rotation = (((this.mCameraOrientation + displayRotation) + i) + m.cQ) % m.cQ;
        }
        String cameraLensType = CameraSettings.getCameraLensType(166);
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("lensType ");
        sb.append(cameraLensType);
        Log.d(str2, sb.toString());
        if (ComponentManuallyDualLens.LENS_WIDE.equals(cameraLensType)) {
            initParam2 = this.mInitParam;
            d2 = (double) (CameraSettings.isPanoramaVertical(CameraAppImpl.getAndroidContext()) ? this.mGoalAngleVertical : this.mGoalAngle);
        } else if (Build.DEVICE.equals("cepheus")) {
            initParam2 = this.mInitParam;
            d2 = 152.18d;
        } else {
            initParam2 = this.mInitParam;
            d2 = ((double) this.mGoalAngle) * 0.6265d;
        }
        initParam2.goal_angle = d2;
        int i2 = this.mCameraOrientation;
        int i3 = i2 != 90 ? i2 != 180 ? i2 != 270 ? 0 : 3 : 2 : 1;
        int rotation = this.mSensorFusion.setRotation(i3);
        if (rotation != 0) {
            Log.e(TAG, String.format(Locale.US, "SensorFusion.setRotation error ret:0x%08X", new Object[]{Integer.valueOf(rotation)}));
        }
        initializeEngine(this.mMorphoPanoramaGP3, this.mInitParam, d);
        return true;
    }

    /* access modifiers changed from: private */
    public String createNameString(long j) {
        return DateFormat.format(getString(R.string.pano_file_name_format), j).toString();
    }

    private void doLaterReleaseIfNeed() {
        Camera camera = this.mActivity;
        if (camera != null && camera.isActivityPaused()) {
            this.mActivity.pauseIfNotRecording();
            this.mActivity.releaseAll(true, true);
        }
    }

    /* access modifiers changed from: private */
    public void finishEngine() {
        if (this.mMorphoPanoramaGP3 != null) {
            Log.d(TAG, "finishEngine start");
            this.mMorphoPanoramaGP3.deleteNativeOutputInfo();
            int finish = this.mMorphoPanoramaGP3.finish(true);
            if (finish != 0) {
                Log.e(TAG, String.format(Locale.US, "finish error ret:0x%08X", new Object[]{Integer.valueOf(finish)}));
            }
            Log.d(TAG, "finishEngine end");
            this.mMorphoPanoramaGP3 = null;
        }
    }

    private Size getThumbnailViewSize() {
        boolean z;
        int i;
        int i2;
        PanoramaProtocol panoramaProtocol = (PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176);
        if (panoramaProtocol == null) {
            return null;
        }
        int currentDirection = panoramaProtocol.getCurrentDirection();
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        if (currentDirection == 5 || currentDirection == 6) {
            i2 = resources.getDimensionPixelSize(R.dimen.pano_texture_view_width_vertical);
            i = resources.getDimensionPixelSize(R.dimen.pano_thumbnail_height_vertical);
            z = true;
        } else {
            i2 = (Display.getWindowWidth() - Display.getStartMargin()) - Display.getEndMargin();
            i = resources.getDimensionPixelSize(R.dimen.pano_preview_hint_frame_height);
            z = false;
        }
        this.mIsVertical = z;
        return new Size(i2, i);
    }

    /* access modifiers changed from: private */
    public void initAttachQueue() {
        while (this.mAttachImageQueue.size() > 0) {
            CaptureImage captureImage = (CaptureImage) this.mAttachImageQueue.poll();
            if (captureImage != null) {
                captureImage.close();
            }
        }
        Log.d(TAG, "initAttachQueue");
    }

    /* access modifiers changed from: private */
    public void initPreviewLayout() {
        if (isAlive()) {
            CameraScreenNail cameraScreenNail = this.mActivity.getCameraScreenNail();
            CameraSize cameraSize = this.mPreviewSize;
            cameraScreenNail.setPreviewSize(cameraSize.width, cameraSize.height);
            ((PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176)).initPreviewLayout(this.mPreviewSize);
        }
    }

    private boolean initializeEngine(MorphoPanoramaGP3 morphoPanoramaGP3, InitParam initParam, double d) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initializeEngine start ");
        sb.append(initParam.toString());
        Log.k(2, str, sb.toString());
        int createNativeOutputInfo = morphoPanoramaGP3.createNativeOutputInfo();
        if (createNativeOutputInfo != 0) {
            Log.e(TAG, String.format(Locale.US, "createNativeOutputInfo error ret:0x%08X", new Object[]{Integer.valueOf(createNativeOutputInfo)}));
        }
        int initialize = morphoPanoramaGP3.initialize(initParam, d);
        if (initialize != 0) {
            Log.e(TAG, String.format(Locale.US, "initialize error ret:0x%08X", new Object[]{Integer.valueOf(initialize)}));
            return false;
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("initializeEngine end ");
        sb2.append(MorphoPanoramaGP3.getVersion());
        Log.k(2, str2, sb2.toString());
        return true;
    }

    private boolean isProcessingFinishTask() {
        SaveOutputImageTask saveOutputImageTask = this.mSaveOutputImageTask;
        return (saveOutputImageTask == null || saveOutputImageTask.getStatus() == Status.FINISHED) ? false : true;
    }

    private boolean isShootingTooShort() {
        return SystemClock.elapsedRealtime() - this.mPanoramaShootingStartTime < 600;
    }

    /* access modifiers changed from: private */
    public void onCaptureDirectionDecided() {
        Log.d(TAG, "onCaptureDirectionDecided %s %s", Integer.valueOf(this.mAttachPosOffsetX), Integer.valueOf(this.mAttachPosOffsetY));
        ((PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176)).onCaptureDirectionDecided(this.mDirection, this.mAttachPosOffsetX, this.mAttachPosOffsetY);
        this.mCaptureDirectionDecided = true;
    }

    /* access modifiers changed from: private */
    public void onPreviewMoving() {
        ((PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176)).onPreviewMoving();
    }

    private void onSaveFinish() {
        if (isAlive()) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                if (this.mAeLockSupported) {
                    camera2Proxy.setAELock(false);
                }
                if (this.mAwbLockSupported) {
                    this.mCamera2Device.setAWBLock(false);
                }
                this.mCamera2Device.setFocusMode(this.mTargetFocusMode);
                startPreview();
                RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                if (recordState != null) {
                    recordState.onPostSavingFinish();
                }
            }
        }
    }

    private void onStopShooting(boolean z) {
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState == null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onStopShooting recordState is null, succeed = ");
            sb.append(z);
            Log.w(str, sb.toString());
            return;
        }
        if (z) {
            recordState.onPostSavingStart();
        } else {
            recordState.onFailed();
        }
        Bitmap bitmap = this.mDispPreviewImage;
        if (bitmap != null) {
            bitmap.eraseColor(0);
        }
        onSaveFinish();
    }

    /* access modifiers changed from: private */
    public void reInitGravitySensorData() {
        float[] fArr = this.mGravities;
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        fArr[2] = 0.0f;
        this.mSensorCnt = 0;
    }

    private void registerSensorListener() {
        if (!this.mIsRegisterSensorListener) {
            this.mIsRegisterSensorListener = true;
            this.mIsSensorAverage = false;
            this.mSensorCnt = 0;
            this.mGravities = new float[3];
            this.mRoundDetector = new GyroscopeRoundDetector();
            this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
            this.mActivity.getSensorStateManager().register(186, C0122O00000o.instance().Oo0Ooo0());
        }
    }

    private void requestStopShoot() {
        addAttachQueue(sAttachExit);
        this.mRequestStop = true;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0082  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x008d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean savePanoramaFile(String str, int i, int i2) {
        boolean z;
        int i3;
        ParcelFileDescriptor parcelFileDescriptor;
        Throwable th;
        String str2 = str;
        GalleryInfoData galleryInfoData = new GalleryInfoData();
        int integer = (CameraSettings.getEncodingQuality(false).toInteger(false) * 256) / 100;
        if (Storage.isUseDocumentMode()) {
            try {
                ParcelFileDescriptor parcelFileDescriptor2 = FileCompat.getParcelFileDescriptor(str2, true);
                try {
                    parcelFileDescriptor = parcelFileDescriptor2;
                    try {
                        i3 = this.mMorphoPanoramaGP3.savePanorama360(i, i2, parcelFileDescriptor2.getFileDescriptor(), integer, this.mShutterStartTime, this.mShutterEndTime, false, galleryInfoData, false);
                        if (parcelFileDescriptor != null) {
                            try {
                                $closeResource(null, parcelFileDescriptor);
                            } catch (Exception e) {
                                e = e;
                            }
                        }
                        z = true;
                    } catch (Throwable th2) {
                        th = th2;
                        th = th;
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    parcelFileDescriptor = parcelFileDescriptor2;
                    th = th;
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                i3 = -1;
                String str3 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("open file failed, filePath ");
                sb.append(str2);
                Log.e(str3, sb.toString(), (Throwable) e);
                z = true;
                if (i3 != 0) {
                }
            } catch (Throwable th4) {
                Throwable th5 = th4;
                if (parcelFileDescriptor != null) {
                    $closeResource(th, parcelFileDescriptor);
                }
                throw th5;
            }
        } else {
            z = true;
            i3 = this.mMorphoPanoramaGP3.savePanorama360(i, i2, str, integer, this.mShutterStartTime, this.mShutterEndTime, false, galleryInfoData, false);
        }
        if (i3 != 0) {
            Log.k(3, TAG, galleryInfoData.toString());
            return z;
        }
        String str4 = TAG;
        Object[] objArr = new Object[(z ? 1 : 0)];
        objArr[0] = Integer.valueOf(i3);
        Log.k(6, str4, String.format("savePanorama360() -> 0x%x", objArr));
        return false;
    }

    private void setInitialRotationByGravity() {
        if (this.mMorphoPanoramaGP3 != null) {
            int i = this.mSensorCnt;
            if (i > 0) {
                float[] fArr = this.mGravities;
                float f = fArr[0] / ((float) i);
                float f2 = fArr[1] / ((float) i);
                float f3 = fArr[2] / ((float) i);
                Log.d(TAG, String.format(Locale.US, "Gravity Sensor Value X=%f Y=%f Z=%f cnt=%d", new Object[]{Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3), Integer.valueOf(this.mSensorCnt)}));
                int initialRotationByGravity = this.mMorphoPanoramaGP3.setInitialRotationByGravity((double) f, (double) f2, (double) f3);
                if (initialRotationByGravity != 0) {
                    Log.e(TAG, String.format(Locale.US, "setInitialRotationByGravity error ret:0x%08X", new Object[]{Integer.valueOf(initialRotationByGravity)}));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void setSensorFusionValue(CaptureImage captureImage) {
        if (this.mMorphoPanoramaGP3 != null) {
            SensorFusion sensorFusion = this.mSensorFusion;
            if (sensorFusion != null) {
                int[] iArr = new int[4];
                int sensorMatrix = sensorFusion.getSensorMatrix(null, null, null, iArr);
                if (sensorMatrix != 0) {
                    Log.e(TAG, String.format(Locale.US, "SensorFusion.getSensorMatrix error ret:0x%08X", new Object[]{Integer.valueOf(sensorMatrix)}));
                }
                ArrayList stockData = this.mSensorFusion.getStockData();
                SensorInfoManager sensorInfoManager = new SensorInfoManager(4);
                sensorInfoManager.g_ix = iArr[0];
                sensorInfoManager.r_ix = iArr[3];
                sensorInfoManager.a_ix = iArr[1];
                sensorInfoManager.img_ix = this.mMorphoPanoramaGP3.getAttachCount();
                sensorInfoManager.timeMillis = System.currentTimeMillis();
                sensorInfoManager.imageTimeStamp = captureImage.getTimestamp();
                sensorInfoManager.sensitivity = captureImage.getSensitivity();
                sensorInfoManager.exposureTime = captureImage.getExposureTime();
                sensorInfoManager.rollingShutterSkew = captureImage.getRollingShutterSkew();
                sensorInfoManager.sensorTimeStamp = captureImage.getSensorTimeStamp();
                sensorInfoManager.sensorData[0] = (ArrayList) ((ArrayList) stockData.get(0)).clone();
                sensorInfoManager.sensorData[3] = (ArrayList) ((ArrayList) stockData.get(3)).clone();
                sensorInfoManager.sensorData[1] = (ArrayList) ((ArrayList) stockData.get(1)).clone();
                if (sensorInfoManager.sensorData[0].isEmpty()) {
                    int size = this.mSensorInfoManagerList.size();
                    if (size > 0) {
                        SensorInfoManager sensorInfoManager2 = (SensorInfoManager) this.mSensorInfoManagerList.get(size - 1);
                        sensorInfoManager.g_ix = sensorInfoManager2.g_ix;
                        sensorInfoManager.sensorData[0] = sensorInfoManager2.sensorData[0];
                    }
                }
                if (sensorInfoManager.sensorData[3].isEmpty()) {
                    int size2 = this.mSensorInfoManagerList.size();
                    if (size2 > 0) {
                        SensorInfoManager sensorInfoManager3 = (SensorInfoManager) this.mSensorInfoManagerList.get(size2 - 1);
                        sensorInfoManager.r_ix = sensorInfoManager3.r_ix;
                        sensorInfoManager.sensorData[3] = sensorInfoManager3.sensorData[3];
                    }
                }
                if (sensorInfoManager.sensorData[1].isEmpty()) {
                    int size3 = this.mSensorInfoManagerList.size();
                    if (size3 > 0) {
                        SensorInfoManager sensorInfoManager4 = (SensorInfoManager) this.mSensorInfoManagerList.get(size3 - 1);
                        sensorInfoManager.a_ix = sensorInfoManager4.a_ix;
                        sensorInfoManager.sensorData[1] = sensorInfoManager4.sensorData[1];
                    }
                }
                this.mCurrentSensorInfoManager = sensorInfoManager;
                this.mSensorInfoManagerList.add(sensorInfoManager);
                long attachCount = this.mMorphoPanoramaGP3.getAttachCount();
                int size4 = ((ArrayList) stockData.get(0)).size();
                if (size4 > 0 && attachCount > 0) {
                    int gyroscopeData = this.mMorphoPanoramaGP3.setGyroscopeData((SensorData[]) ((ArrayList) stockData.get(0)).toArray(new SensorData[size4]));
                    if (gyroscopeData != 0) {
                        Log.e(TAG, String.format(Locale.US, "setGyroscopeData error ret:0x%08X", new Object[]{Integer.valueOf(gyroscopeData)}));
                    }
                }
                this.mSensorFusion.clearStockData();
            }
        }
    }

    private void setupCaptureParams() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy == null) {
            Log.e(TAG, "camera device is not ready");
            return;
        }
        camera2Proxy.setFocusMode(this.mTargetFocusMode);
        this.mCamera2Device.setZoomRatio(1.0f);
        this.mCamera2Device.setFlashMode(0);
        String antiBanding = CameraSettings.getAntiBanding();
        this.mCamera2Device.setAntiBanding(Integer.valueOf(antiBanding).intValue());
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("antiBanding=");
        sb.append(antiBanding);
        Log.d(str, sb.toString());
        this.mCamera2Device.setEnableZsl(isZslPreferred());
        this.mCamera2Device.setHHT(false);
        this.mCamera2Device.setEnableOIS(false);
        this.mCamera2Device.setTimeWaterMarkEnable(false);
        this.mCamera2Device.setFaceWaterMarkEnable(false);
    }

    /* access modifiers changed from: private */
    public void stopPanoramaShooting(boolean z, String str) {
        stopPanoramaShooting(z, false, str);
    }

    private void stopPanoramaShooting(boolean z, boolean z2, String str) {
        if (!this.mIsShooting) {
            Log.w(TAG, "stopPanoramaShooting while not shooting");
            return;
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("stopPanoramaShooting: saveImage=");
        sb.append(z);
        sb.append(", isRelease=");
        sb.append(z2);
        Log.k(2, str2, sb.toString());
        requestStopShoot();
        keepScreenOnAwhile();
        this.mRoundDetector.stop();
        synchronized (this.mDeviceLock) {
            if (this.mCamera2Device != null) {
                if (z2) {
                    PanoramaProtocol panoramaProtocol = (PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176);
                    if (panoramaProtocol != null) {
                        Log.d(TAG, "onPause setDisplayPreviewBitmap null");
                        panoramaProtocol.setDisplayPreviewBitmap(null);
                    }
                } else {
                    this.mCamera2Device.captureAbortBurst();
                }
                this.mCamera2Device.stopPreviewCallback(z2);
            }
        }
        boolean z3 = z && this.mCanSavePanorama;
        this.mShutterEndTime = createDateStringForAppSeg(System.currentTimeMillis());
        this.mSaveOutputImageTask = new SaveOutputImageTask(z3, str);
        this.mSaveOutputImageTask.execute(new Void[0]);
        onStopShooting(z3);
    }

    private void unRegisterSensorListener() {
        if (this.mIsRegisterSensorListener) {
            this.mIsRegisterSensorListener = false;
            this.mActivity.getSensorStateManager().setSensorStateListener(null);
            this.mActivity.getSensorStateManager().unregister(186);
        }
    }

    private void updatePictureAndPreviewSize() {
        CameraSize bestPanoPictureSize = getBestPanoPictureSize(this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(256), C0122O00000o.instance().OoOOO());
        this.mPreviewSize = Util.getOptimalPreviewSize(false, this.mBogusCameraId, this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class), (double) CameraSettings.getPreviewAspectRatio(bestPanoPictureSize.width, bestPanoPictureSize.height));
        this.mPictureSize = bestPanoPictureSize;
        CameraSize cameraSize = this.mPictureSize;
        this.mPictureWidth = cameraSize.width;
        this.mPictureHeight = cameraSize.height;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("pictureSize= ");
        sb.append(bestPanoPictureSize.width);
        String str2 = "X";
        sb.append(str2);
        sb.append(bestPanoPictureSize.height);
        sb.append(" previewSize=");
        sb.append(this.mPreviewSize.width);
        sb.append(str2);
        sb.append(this.mPreviewSize.height);
        Log.k(3, str, sb.toString());
        CameraSize cameraSize2 = this.mPreviewSize;
        updateCameraScreenNailSize(cameraSize2.width, cameraSize2.height);
    }

    public void closeCamera() {
        Log.d(TAG, "closeCamera: start");
        synchronized (this.mDeviceLock) {
            setCameraState(0);
            if (this.mCamera2Device != null) {
                this.mCamera2Device.setErrorCallback(null);
                this.mCamera2Device.stopPreviewCallback(true);
                this.mCamera2Device = null;
            }
        }
        Log.d(TAG, "closeCamera: end");
    }

    public void consumePreference(@UpdateType int... iArr) {
        int length = iArr.length;
        for (int i = 0; i < length; i++) {
            int i2 = iArr[i];
            if (i2 == 1) {
                updatePictureAndPreviewSize();
            } else if (!(i2 == 2 || i2 == 5 || i2 == 6 || i2 == 11 || i2 == 20)) {
                if (i2 == 24) {
                    applyZoomRatio();
                } else if (i2 == 30) {
                    continue;
                } else if (i2 == 32) {
                    setupCaptureParams();
                } else if (!(i2 == 34 || i2 == 42)) {
                    if (i2 == 55) {
                        updateModuleRelated();
                    } else if (i2 != 66) {
                        switch (i2) {
                            case 46:
                            case 47:
                            case 48:
                                continue;
                            default:
                                String str = "no consumer for this updateType: ";
                                if (!BaseModule.DEBUG) {
                                    String str2 = TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(str);
                                    sb.append(i2);
                                    Log.w(str2, sb.toString());
                                    break;
                                } else {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(str);
                                    sb2.append(i2);
                                    throw new RuntimeException(sb2.toString());
                                }
                        }
                    } else {
                        updateThermalLevel();
                    }
                }
            }
        }
    }

    public void fillFeatureControl(StartControl startControl) {
        super.fillFeatureControl(startControl);
        if (startControl.mTargetMode == 166 && C0122O00000o.instance().OOOo00()) {
            startControl.getFeatureDetail().addFragmentInfo(R.id.wideselfie_content, 4094);
        }
    }

    /* access modifiers changed from: protected */
    public CameraSize getBestPanoPictureSize(List list, int i) {
        PictureSizeManager.initialize(list, i, this.mModuleIndex, this.mBogusCameraId);
        return PictureSizeManager.getBestPanoPictureSize();
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        return 32776;
    }

    public String getTag() {
        return TAG;
    }

    public boolean isDoingAction() {
        return isProcessingFinishTask();
    }

    public boolean isRecording() {
        return this.mIsShooting;
    }

    public boolean isUnInterruptable() {
        this.mUnInterruptableReason = null;
        if (this.mIsShooting) {
            this.mUnInterruptableReason = "shooting";
        }
        return this.mUnInterruptableReason != null;
    }

    public boolean isZoomEnabled() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isZslPreferred() {
        return !C0124O00000oO.isMTKPlatform();
    }

    /* access modifiers changed from: protected */
    public void keepScreenOn() {
        this.mHandler.removeMessages(17);
        this.mHandler.removeMessages(2);
        getWindow().addFlags(128);
    }

    /* access modifiers changed from: protected */
    public void keepScreenOnAwhile() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessageDelayed(17, 1000);
        }
    }

    public boolean onBackPressed() {
        if (!this.mIsShooting) {
            return super.onBackPressed();
        }
        if (!isProcessingFinishTask()) {
            playCameraSound(3);
            stopPanoramaShooting(true, Panorama.STOP_CAPTURE_MODE_PRESS_BACK);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        super.onCameraOpened();
        checkDisplayOrientation();
        updatePreferenceTrampoline(UpdateConstant.PANORAMA_TYPES_INIT);
        startSession();
        this.mHandler.sendEmptyMessage(9);
        Log.v(TAG, "SetupCameraThread done");
        this.mViewAngleH = this.mCameraCapabilities.getViewAngle(false);
        this.mViewAngleV = this.mCameraCapabilities.getViewAngle(true);
        if (C0124O00000oO.O0Ooo0o.equals("lavender") && this.mViewAngleH > 50.0f) {
            this.mGoalAngle = 291;
        }
        this.mCameraOrientation = this.mCameraCapabilities.getSensorOrientation();
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        this.mHandler = new MainHandler(this, Looper.getMainLooper());
        this.mGoalAngle = C0122O00000o.instance().OO0000();
        this.mGoalAngleVertical = C0122O00000o.instance().OO0000o();
        this.mSmallPreviewHeight = (int) this.mActivity.getResources().getDimension(R.dimen.pano_preview_hint_frame_height);
        this.mSmallPreviewHeightVertical = 1080;
        EffectController.getInstance().setAiSceneEffect(FilterInfo.FILTER_ID_NONE, true);
        onCameraOpened();
        this.mRoundDetector = new RoundDetector();
        this.mPanoramaSetting = new PanoramaSetting(CameraAppImpl.getAndroidContext());
        this.mSensorFusion = new SensorFusion(true);
        this.mSensorFusionMode = 1;
        int mode = this.mSensorFusion.setMode(this.mSensorFusionMode);
        if (mode != 0) {
            Log.e(TAG, String.format(Locale.US, "SensorFusion.setMode error ret:0x%08X", new Object[]{Integer.valueOf(mode)}));
        }
        int offsetMode = this.mSensorFusion.setOffsetMode(0);
        if (offsetMode != 0) {
            Log.e(TAG, String.format(Locale.US, "SensorFusion.setOffsetMode error ret:0x%08X", new Object[]{Integer.valueOf(offsetMode)}));
        }
        int appState = this.mSensorFusion.setAppState(1);
        if (appState != 0) {
            Log.e(TAG, String.format(Locale.US, "SensorFusion.setAppState error ret:0x%08X", new Object[]{Integer.valueOf(appState)}));
        }
        this.mSensorInfoManagerList = new ArrayList();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.sendEmptyMessage(45);
        this.mExecutor.shutdown();
    }

    public void onHostStopAndNotifyActionStop() {
        playCameraSound(3);
        stopPanoramaShooting(true, true, Panorama.STOP_CAPTURE_MODE_PRESS_BACK);
        synchronized (mPreviewImageLock) {
            if (this.mPreviewImage != null) {
                this.mPreviewImage.recycle();
                this.mPreviewImage = null;
            }
            if (this.mDispPreviewImage != null) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onPause recycle bitmap ");
                sb.append(this.mDispPreviewImage);
                Log.d(str, sb.toString());
                this.mDispPreviewImage.recycle();
                this.mDispPreviewImage = null;
            }
        }
        doLaterReleaseIfNeed();
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
            stopPanoramaShooting(true, Panorama.STOP_CAPTURE_MODE_PRESS_BACK);
        }
        return super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (this.mPaused) {
            return true;
        }
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
        super.onPause();
        unRegisterSensorListener();
        this.mHandler.removeCallbacksAndMessages(null);
        closeCamera();
        resetScreenOn();
    }

    public void onPreviewLayoutChanged(Rect rect) {
        this.mActivity.onLayoutChange(rect);
    }

    public void onPreviewSessionClosed(CameraCaptureSession cameraCaptureSession) {
    }

    public void onPreviewSessionFailed(CameraCaptureSession cameraCaptureSession) {
        if (!isTextureExpired() || !retryOnceIfCameraError(this.mHandler)) {
            this.mHandler.sendEmptyMessage(51);
        } else {
            Log.d(TAG, "sessionFailed due to surfaceTexture expired, retry");
        }
    }

    public void onPreviewSessionSuccess(CameraCaptureSession cameraCaptureSession) {
        if (cameraCaptureSession != null && isAlive()) {
            setCameraState(1);
            updatePreferenceInWorkThread(UpdateConstant.PANORAMA_ON_PREVIEW_SUCCESS);
        }
    }

    public void onPreviewSizeChanged(int i, int i2) {
    }

    public void onResume() {
        super.onResume();
        keepScreenOnAwhile();
        registerSensorListener();
    }

    public void onReviewCancelClicked() {
    }

    public void onReviewDoneClicked() {
    }

    public void onShutterButtonClick(int i) {
        if (!this.mPaused && getCameraState() != 0 && !isIgnoreTouchEvent() && (!isFrontCamera() || !this.mActivity.isScreenSlideOff())) {
            if (isDoingAction()) {
                Log.w(TAG, "onShutterButtonClick return, isDoingAction");
                return;
            }
            Log.u(TAG, "onShutterButtonClick");
            Log.k(3, TAG, String.format("onShutterButtonClick mode = %d", new Object[]{Integer.valueOf(i)}));
            setTriggerMode(i);
            if (!this.mIsShooting) {
                this.mActivity.getScreenHint().updateHint();
                if (Storage.isLowStorageAtLastPoint()) {
                    ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).onFailed();
                    return;
                }
                this.mThumbnailViewSize = getThumbnailViewSize();
                if (this.mThumbnailViewSize == null) {
                    Log.w(TAG, "onShutterButtonClick return, thumbnailViewSize is null");
                    return;
                }
                this.mPanoramaState = new PanoramaInit();
                initAttachQueue();
                playCameraSound(2);
                Log.u(TAG, "onShutterButtonClick startPanoramaShooting");
                startPanoramaShooting();
            } else if (isShootingTooShort()) {
                Log.w(TAG, "panorama shooting is too short, ignore this click");
            } else {
                playCameraSound(3);
                Log.u(TAG, "onShutterButtonClick stopPanoramaShooting");
                stopPanoramaShooting(true, false, Panorama.STOP_CAPTURE_MODE_PRESS_STOP);
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
        if (!this.mPaused && this.mCamera2Device != null && !hasCameraException() && this.mCamera2Device.isSessionReady() && isInTapableRect(i, i2)) {
            if (!isFrameAvailable()) {
                Log.w(TAG, "onSingleTapUp: frame not available");
            } else {
                BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
                if (backStack == null || backStack.handleBackStackFromTapDown(i, i2)) {
                }
            }
        }
    }

    public void onStop() {
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onThermalConstrained() {
        super.onThermalConstrained();
        if (this.mIsShooting) {
            if (isShootingTooShort()) {
                Log.w(TAG, "panorama shooting is too short, ignore this click");
            } else {
                playCameraSound(3);
                stopPanoramaShooting(true, false, Panorama.STOP_CAPTURE_MODE_THERMAL);
            }
        }
    }

    @OnClickAttr
    public void onThumbnailClicked(View view) {
        if (!this.mPaused && !isProcessingFinishTask() && this.mActivity.getThumbnailUpdater().getThumbnail() != null) {
            this.mActivity.gotoGallery();
        }
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        if (!this.mPaused && !this.mIsShooting) {
            keepScreenOnAwhile();
        }
    }

    public void pausePreview() {
        Log.v(TAG, "pausePreview");
        this.mCamera2Device.pausePreview();
        setCameraState(0);
    }

    /* access modifiers changed from: protected */
    public void performKeyClicked(int i, String str, int i2, boolean z) {
        if (i2 == 0 && z) {
            onShutterButtonClick(i);
        }
    }

    public void registerProtocol() {
        super.registerProtocol();
        ModeCoordinatorImpl.getInstance().attachProtocol(161, this);
        getActivity().getImplFactory().initAdditional(getActivity(), 174, 164, 212);
    }

    public void requestRender() {
        PanoramaProtocol panoramaProtocol = (PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176);
        if (panoramaProtocol != null) {
            panoramaProtocol.requestRender();
        }
    }

    /* access modifiers changed from: protected */
    public void resetScreenOn() {
        this.mHandler.removeMessages(17);
        this.mHandler.removeMessages(2);
    }

    public void resumePreview() {
        Log.v(TAG, "resumePreview");
        this.mCamera2Device.resumePreview();
        setCameraState(1);
    }

    /* access modifiers changed from: protected */
    public void sendOpenFailMessage() {
        this.mHandler.sendEmptyMessage(10);
    }

    public void setFrameAvailable(boolean z) {
        super.setFrameAvailable(z);
        if (z && CameraSettings.isCameraSoundOpen()) {
            Camera camera = this.mActivity;
            if (camera != null) {
                camera.loadCameraSound(2);
                camera.loadCameraSound(3);
            }
        }
    }

    public void setNullDirectionFunction() {
        DirectionFunction directionFunction = new DirectionFunction(this.mPictureWidth, this.mPictureHeight, 1, 1, 1.0f, 0);
        this.mDirectionFunction = directionFunction;
    }

    public boolean shouldReleaseLater() {
        return isRecording();
    }

    public void startPanoramaShooting() {
        if (isProcessingFinishTask()) {
            Log.e(TAG, "previous save task is on going");
            return;
        }
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        recordState.onPrepare();
        Log.k(2, TAG, "startPanoramaShooting");
        this.mCaptureDirectionDecided = false;
        this.mDirection = this.mInitParam.direction;
        this.mTimeTaken = System.currentTimeMillis();
        this.mIsShooting = true;
        this.mCanSavePanorama = false;
        this.mRequestStop = false;
        this.mShutterStartTime = createDateStringForAppSeg(System.currentTimeMillis());
        this.mShutterEndTime = "";
        synchronized (this.mDeviceLock) {
            if (this.mAeLockSupported) {
                this.mCamera2Device.setAELock(true);
            }
            if (this.mAwbLockSupported && C0122O00000o.instance().OO0O0oO()) {
                this.mCamera2Device.setAWBLock(true);
            }
            this.mLocation = LocationManager.instance().getCurrentLocation();
            this.mCamera2Device.setGpsLocation(this.mLocation);
            this.mCamera2Device.setFocusMode(this.mSnapshotFocusMode);
            this.mCamera2Device.setJpegQuality(CameraSettings.getEncodingQuality(false).toInteger(false));
            this.mCamera2Device.setJpegThumbnailSize(getJpegThumbnailSize());
            this.mCamera2Device.setEnableZsl(isZslPreferred());
            this.mCamera2Device.setNeedPausePreview(false);
            this.mCamera2Device.setShotType(3);
            this.mCamera2Device.captureBurstPictures(-1, new PictureCallbackWrapper() {
                public void onPictureTakenFinished(boolean z) {
                    String access$400 = Panorama3Module.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onPictureBurstFinished success = ");
                    sb.append(z);
                    Log.k(3, access$400, sb.toString());
                }

                public boolean onPictureTakenImageConsumed(Image image, TotalCaptureResult totalCaptureResult) {
                    String access$400 = Panorama3Module.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onPictureTaken>>image=");
                    sb.append(image);
                    Log.k(2, access$400, sb.toString());
                    if (Panorama3Module.this.mCamera2Device != null) {
                        if (!Panorama3Module.this.mPanoramaState.onSaveImage(new Camera2Image(image))) {
                            Log.w(Panorama3Module.TAG, "set mPanoramaState PanoramaState");
                            Panorama3Module.this.mPanoramaState = new PanoramaState();
                        }
                    } else {
                        image.close();
                    }
                    return true;
                }
            }, null);
        }
        keepScreenOnAwhile();
        recordState.onStart();
        this.mPanoramaShootingStartTime = SystemClock.elapsedRealtime();
        keepScreenOn();
        AutoLockManager.getInstance(this.mActivity).removeMessage();
    }

    public void startPreview() {
        synchronized (this.mDeviceLock) {
            if (this.mCamera2Device == null) {
                Log.e(TAG, "startPreview: camera has been closed");
                return;
            }
            checkDisplayOrientation();
            this.mCamera2Device.setDisplayOrientation(this.mCameraDisplayOrientation);
            if (this.mAeLockSupported) {
                this.mCamera2Device.setAELock(false);
            }
            if (this.mAwbLockSupported) {
                this.mCamera2Device.setAWBLock(false);
            }
            this.mCamera2Device.setFocusMode(this.mTargetFocusMode);
            this.mCamera2Device.resumePreview();
            setCameraState(1);
        }
    }

    public void startSession() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy == null) {
            Log.e(TAG, "startPreview: camera has been closed");
            return;
        }
        camera2Proxy.setDualCamWaterMarkEnable(false);
        this.mCamera2Device.setErrorCallback(this.mErrorCallback);
        this.mCamera2Device.setPreviewSize(this.mPreviewSize);
        this.mCamera2Device.setAlgorithmPreviewSize(this.mPreviewSize);
        this.mCamera2Device.setPictureSize(this.mPictureSize);
        this.mCamera2Device.setPictureMaxImages(3);
        this.mCamera2Device.setPictureFormat(35);
        this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
        boolean z = !C0124O00000oO.isMTKPlatform();
        this.mCamera2Device.startPreviewSession(new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture()), z ? 1 : 0, 0, null, getOperatingMode(), false, this);
    }

    /* access modifiers changed from: protected */
    public void trackModeCustomInfo(Map map, boolean z, BeautyValues beautyValues, int i, boolean z2) {
        CameraStatUtils.trackPictureTakenInPanorama(map, this.mActivity, beautyValues, i);
    }

    public void unRegisterProtocol() {
        super.unRegisterProtocol();
        ModeCoordinatorImpl.getInstance().detachProtocol(161, this);
        Camera camera = this.mActivity;
        if (camera != null) {
            camera.getImplFactory().detachAdditional();
        }
    }
}
