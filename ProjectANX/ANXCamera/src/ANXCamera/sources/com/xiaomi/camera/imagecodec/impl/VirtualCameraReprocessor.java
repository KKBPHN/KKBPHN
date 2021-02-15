package com.xiaomi.camera.imagecodec.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCaptureSession.StateCallback;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.ImageWriter;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.util.Range;
import android.view.Surface;
import com.xiaomi.camera.imagecodec.BaseReprocessor;
import com.xiaomi.camera.imagecodec.ImagePool;
import com.xiaomi.camera.imagecodec.ImagePool.ImageFormat;
import com.xiaomi.camera.imagecodec.ImageReaderHelper;
import com.xiaomi.camera.imagecodec.ImageReaderHelper.ImageReaderType;
import com.xiaomi.camera.imagecodec.OutputConfiguration;
import com.xiaomi.camera.imagecodec.ReprocessData;
import com.xiaomi.camera.imagecodec.ReprocessData.OnDataAvailableListener;
import com.xiaomi.camera.imagecodec.Reprocessor.Singleton;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class VirtualCameraReprocessor extends BaseReprocessor {
    private static String BACK_VT_CAMERA_ID_DEFAULT = "100";
    private static String FRONT_VT_CAMERA_ID_DEFAULT = "101";
    private static final int MAX_IMAGE_BUFFER_SIZE = 2;
    /* access modifiers changed from: private */
    public static final String TAG = "VirtualCameraReprocessor";
    public static final Singleton sInstance = new Singleton() {
        /* access modifiers changed from: protected */
        public VirtualCameraReprocessor create() {
            return new VirtualCameraReprocessor();
        }
    };
    private String mBackVtCameraId;
    /* access modifiers changed from: private */
    public final Object mCameraLock;
    private CameraManager mCameraManager;
    private Handler mCameraOperationHandler;
    private HandlerThread mCameraOperationThread;
    /* access modifiers changed from: private */
    public volatile boolean mCreatingReprocessSession;
    /* access modifiers changed from: private */
    public ReprocessData mCurrentProcessingData;
    /* access modifiers changed from: private */
    public int mCurrentSessionId;
    /* access modifiers changed from: private */
    public final Object mDataLock;
    private String mFrontVtCameraId;
    private boolean mInitialized;
    private InputConfiguration mInputConfiguration;
    /* access modifiers changed from: private */
    public boolean mNeedReopenCamera;
    private ImageReader mPicImageReader;
    private OutputConfiguration mPicOutputConfiguration;
    private Handler mReprocessHandler;
    /* access modifiers changed from: private */
    public ImageWriter mReprocessImageWriter;
    /* access modifiers changed from: private */
    public long mReprocessStartTime;
    private HandlerThread mReprocessThread;
    private Range mSessionFpsRange;
    /* access modifiers changed from: private */
    public LinkedList mTaskDataList;
    /* access modifiers changed from: private */
    public CameraDevice mVTCameraDevice;
    /* access modifiers changed from: private */
    public CameraCaptureSession mVTCaptureSession;
    private WakeLock mWakeLock;
    private ImageReader mYuvImageReader;
    private OutputConfiguration mYuvOutputConfiguration;

    class ReprocessHandler extends Handler {
        private static final int MSG_CLOSE_VT_CAMERA = 2;
        private static final int MSG_REPROCESS_IMG = 1;

        ReprocessHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                Log.d(VirtualCameraReprocessor.TAG, "recv MSG_REPROCESS_IMG");
                if (VirtualCameraReprocessor.this.checkConditionIsReady()) {
                    VirtualCameraReprocessor.this.reprocessImage();
                }
            } else if (i != 2) {
                super.handleMessage(message);
            } else {
                Log.d(VirtualCameraReprocessor.TAG, "recv MSG_CLOSE_VT_CAMERA");
                synchronized (VirtualCameraReprocessor.this.mCameraLock) {
                    if (VirtualCameraReprocessor.this.mVTCameraDevice != null) {
                        String access$100 = VirtualCameraReprocessor.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("close current VtCamera: ");
                        sb.append(VirtualCameraReprocessor.this.mVTCameraDevice);
                        Log.d(access$100, sb.toString());
                        VirtualCameraReprocessor.this.mVTCameraDevice.close();
                        VirtualCameraReprocessor.this.mVTCameraDevice = null;
                    }
                }
                VirtualCameraReprocessor.this.releaseWakeLock();
            }
        }
    }

    private VirtualCameraReprocessor() {
        this.mBackVtCameraId = BACK_VT_CAMERA_ID_DEFAULT;
        this.mFrontVtCameraId = FRONT_VT_CAMERA_ID_DEFAULT;
        this.mCurrentSessionId = -1;
        this.mCameraLock = new Object();
        this.mTaskDataList = new LinkedList();
        this.mDataLock = new Object();
        this.mYuvOutputConfiguration = new OutputConfiguration(0, 0, 35);
    }

    private void acquireWakeLock() {
        if (!this.mWakeLock.isHeld()) {
            Log.d(TAG, "acquireWakeLock");
            this.mWakeLock.acquire();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001a, code lost:
        if (r1 != null) goto L_0x0024;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
        android.util.Log.w(TAG, "checkConditionIsReady: ignore null request!");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0023, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002a, code lost:
        return !createCaptureSessionIfNeed(r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @WorkerThread
    public boolean checkConditionIsReady() {
        synchronized (this.mDataLock) {
            if (this.mCurrentProcessingData != null) {
                Log.d(TAG, "checkConditionIsReady: processor is busy!");
                return false;
            }
            ReprocessData reprocessData = (ReprocessData) this.mTaskDataList.peek();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00a4, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x018c, code lost:
        return r3;
     */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0159  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @WorkerThread
    private boolean createCaptureSessionIfNeed(@NonNull ReprocessData reprocessData) {
        boolean z;
        synchronized (this.mCameraLock) {
            if (this.mCreatingReprocessSession) {
                Log.d(TAG, "creating reprocess session...");
                return true;
            }
            boolean z2 = 0;
            Image image = (Image) reprocessData.getMainImage().get(0);
            InputConfiguration inputConfiguration = new InputConfiguration(image.getWidth(), image.getHeight(), image.getFormat());
            OutputConfiguration outputConfiguration = new OutputConfiguration(reprocessData.getOutputWidth(), reprocessData.getOutputHeight(), reprocessData.getOutputFormat());
            String str = reprocessData.isFrontCamera() ? this.mFrontVtCameraId : this.mBackVtCameraId;
            if (this.mVTCameraDevice != null) {
                if (str.equals(this.mVTCameraDevice.getId())) {
                    z = false;
                    if (!z) {
                        this.mCreatingReprocessSession = true;
                        if (this.mVTCameraDevice == null) {
                            openVTCamera(str);
                        } else if (!str.equals(this.mVTCameraDevice.getId())) {
                            String str2 = TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("createCaptureSessionIfNeed: expected device changed. oldId=");
                            sb.append(this.mVTCameraDevice.getId());
                            sb.append(" newId=");
                            sb.append(str);
                            Log.d(str2, sb.toString());
                            this.mVTCameraDevice.close();
                            this.mVTCameraDevice = null;
                            this.mNeedReopenCamera = true;
                        }
                    } else {
                        int format = outputConfiguration.getFormat();
                        OutputConfiguration outputConfiguration2 = format != 35 ? this.mPicOutputConfiguration : this.mYuvOutputConfiguration;
                        if (this.mVTCaptureSession != null) {
                            if (!inputConfiguration.equals(this.mInputConfiguration) || !outputConfiguration.equals(outputConfiguration2)) {
                                String str3 = TAG;
                                String str4 = "recreate session. in: %dx%d->%dx%d %s out: %dx%d->%dx%d";
                                Object[] objArr = new Object[9];
                                objArr[0] = Integer.valueOf(this.mInputConfiguration == null ? 0 : this.mInputConfiguration.getWidth());
                                objArr[1] = Integer.valueOf(this.mInputConfiguration == null ? 0 : this.mInputConfiguration.getHeight());
                                objArr[2] = Integer.valueOf(inputConfiguration.getWidth());
                                objArr[3] = Integer.valueOf(inputConfiguration.getHeight());
                                String str5 = format == 35 ? "yuv" : format == 256 ? "jpeg" : "heic";
                                objArr[4] = str5;
                                objArr[5] = Integer.valueOf(outputConfiguration2 == null ? 0 : outputConfiguration2.getWidth());
                                if (outputConfiguration2 != null) {
                                    z2 = outputConfiguration2.getHeight();
                                }
                                objArr[6] = Integer.valueOf(z2);
                                objArr[7] = Integer.valueOf(outputConfiguration.getWidth());
                                objArr[8] = Integer.valueOf(outputConfiguration.getHeight());
                                Log.d(str3, String.format(str4, objArr));
                            }
                            if (z2 != 0) {
                                this.mCreatingReprocessSession = true;
                                this.mInputConfiguration = inputConfiguration;
                                if (format != 35) {
                                    this.mPicOutputConfiguration = outputConfiguration;
                                    this.mYuvOutputConfiguration = new OutputConfiguration(outputConfiguration.getWidth(), outputConfiguration.getHeight(), 35);
                                } else {
                                    this.mYuvOutputConfiguration = outputConfiguration;
                                    this.mPicOutputConfiguration = new OutputConfiguration(outputConfiguration.getWidth(), outputConfiguration.getHeight(), 256);
                                }
                                createReprocessSession(this.mInputConfiguration, this.mYuvOutputConfiguration, this.mPicOutputConfiguration);
                            }
                        }
                        z2 = 1;
                        if (z2 != 0) {
                        }
                    }
                }
            }
            z = true;
            if (!z) {
            }
        }
    }

    @RequiresApi(api = 28)
    public static void createCaptureSessionWithSessionConfiguration(CameraDevice cameraDevice, InputConfiguration inputConfiguration, List list, CaptureRequest captureRequest, StateCallback stateCallback, final Handler handler) {
        SessionConfiguration sessionConfiguration = new SessionConfiguration(0, list, handler == null ? null : new Executor() {
            public void execute(Runnable runnable) {
                handler.post(runnable);
            }
        }, stateCallback);
        if (inputConfiguration != null) {
            sessionConfiguration.setInputConfiguration(inputConfiguration);
        }
        sessionConfiguration.setSessionParameters(captureRequest);
        cameraDevice.createCaptureSession(sessionConfiguration);
    }

    @WorkerThread
    private void createReprocessSession(@NonNull InputConfiguration inputConfiguration, @NonNull OutputConfiguration outputConfiguration, @NonNull OutputConfiguration outputConfiguration2) {
        Log.d(TAG, String.format(Locale.ENGLISH, "createReprocessSession>>input[%dx%d] output[%dx%d@%d]", new Object[]{Integer.valueOf(inputConfiguration.getWidth()), Integer.valueOf(inputConfiguration.getHeight()), Integer.valueOf(outputConfiguration2.getWidth()), Integer.valueOf(outputConfiguration2.getHeight()), Integer.valueOf(outputConfiguration2.getFormat())}));
        initYuvImageReader(outputConfiguration.getWidth(), outputConfiguration.getHeight());
        initPicImageReader(outputConfiguration2.getWidth(), outputConfiguration2.getHeight(), outputConfiguration2.getFormat());
        try {
            Surface[] surfaceArr = (outputConfiguration.getWidth() <= 0 || this.mYuvImageReader == null) ? new Surface[]{this.mPicImageReader.getSurface()} : new Surface[]{this.mPicImageReader.getSurface(), this.mYuvImageReader.getSurface()};
            List<Surface> asList = Arrays.asList(surfaceArr);
            InputConfiguration inputConfiguration2 = new InputConfiguration(inputConfiguration.getWidth(), inputConfiguration.getHeight(), inputConfiguration.getFormat());
            AnonymousClass5 r4 = new StateCallback() {
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Log.d(VirtualCameraReprocessor.TAG, "onConfigureFailed");
                    synchronized (VirtualCameraReprocessor.this.mCameraLock) {
                        VirtualCameraReprocessor.this.mCreatingReprocessSession = false;
                        VirtualCameraReprocessor.this.mVTCaptureSession = null;
                        VirtualCameraReprocessor.this.mReprocessImageWriter = null;
                    }
                }

                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Log.d(VirtualCameraReprocessor.TAG, "onConfigured>>");
                    synchronized (VirtualCameraReprocessor.this.mCameraLock) {
                        if (VirtualCameraReprocessor.this.mVTCameraDevice == null) {
                            Log.e(VirtualCameraReprocessor.TAG, "onConfigured: null camera device");
                            VirtualCameraReprocessor.this.mCreatingReprocessSession = false;
                            return;
                        }
                        VirtualCameraReprocessor.this.mVTCaptureSession = cameraCaptureSession;
                        VirtualCameraReprocessor.this.mCurrentSessionId = VirtualCameraReprocessor.this.mCurrentSessionId + 1;
                        int access$1200 = VirtualCameraReprocessor.getSessionId(cameraCaptureSession);
                        Log.d(VirtualCameraReprocessor.TAG, String.format("sessionId: %d %d", new Object[]{Integer.valueOf(VirtualCameraReprocessor.this.mCurrentSessionId), Integer.valueOf(access$1200)}));
                        if (!(access$1200 == -1 || VirtualCameraReprocessor.this.mCurrentSessionId == access$1200)) {
                            Log.w(VirtualCameraReprocessor.TAG, String.format("sessionId: %d->%d", new Object[]{Integer.valueOf(VirtualCameraReprocessor.this.mCurrentSessionId), Integer.valueOf(access$1200)}));
                            VirtualCameraReprocessor.this.mCurrentSessionId = access$1200;
                        }
                        if (VirtualCameraReprocessor.this.mReprocessImageWriter != null) {
                            VirtualCameraReprocessor.this.mReprocessImageWriter.close();
                        }
                        VirtualCameraReprocessor.this.mReprocessImageWriter = ImagePool.makeImageWriter(cameraCaptureSession.getInputSurface(), 2, true);
                        VirtualCameraReprocessor.this.mCreatingReprocessSession = false;
                        VirtualCameraReprocessor.this.sendReprocessRequest();
                        Log.d(VirtualCameraReprocessor.TAG, "onConfigured<<");
                    }
                }
            };
            if (VERSION.SDK_INT >= 28) {
                ArrayList arrayList = new ArrayList(asList.size());
                for (Surface outputConfiguration3 : asList) {
                    arrayList.add(new android.hardware.camera2.params.OutputConfiguration(outputConfiguration3));
                }
                CaptureRequest build = this.mVTCameraDevice.createCaptureRequest(2).build();
                Range range = (Range) build.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
                if (range != null) {
                    this.mSessionFpsRange = Range.create(range.getLower(), range.getUpper());
                }
                createCaptureSessionWithSessionConfiguration(this.mVTCameraDevice, inputConfiguration2, arrayList, build, r4, this.mCameraOperationHandler);
            } else {
                this.mVTCameraDevice.createReprocessableCaptureSession(inputConfiguration2, asList, r4, this.mCameraOperationHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createReprocessSession<<");
    }

    private static byte[] getJpegData(Image image) {
        Plane[] planes = image.getPlanes();
        if (planes.length <= 0) {
            return null;
        }
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] bArr = new byte[buffer.remaining()];
        buffer.get(bArr);
        return bArr;
    }

    /* access modifiers changed from: private */
    public static int getSessionId(CameraCaptureSession cameraCaptureSession) {
        if (cameraCaptureSession != null) {
            try {
                Field declaredField = Class.forName("android.hardware.camera2.impl.CameraCaptureSessionImpl").getDeclaredField("mId");
                declaredField.setAccessible(true);
                return declaredField.getInt(cameraCaptureSession);
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
                Log.w(TAG, "getSessionId: failed!", e);
            }
        }
        return -1;
    }

    @WorkerThread
    private void initPicImageReader(int i, int i2, int i3) {
        Log.d(TAG, "initPicImageReader>>");
        ImageReader imageReader = this.mPicImageReader;
        if (!(imageReader == null || (imageReader.getWidth() == i && this.mPicImageReader.getHeight() == i2 && this.mPicImageReader.getImageFormat() == i3))) {
            Log.d(TAG, "closing obsolete reprocess reader");
            this.mPicImageReader.close();
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initPicImageReader: ");
        sb.append(i);
        sb.append("x");
        sb.append(i2);
        sb.append("@");
        sb.append(i3);
        Log.d(str, sb.toString());
        this.mPicImageReader = ImageReader.newInstance(i, i2, i3, 2);
        ImageReaderHelper.setImageReaderNameDepends(this.mPicImageReader, ImageReaderType.JPEG, false);
        this.mPicImageReader.setOnImageAvailableListener(new OnImageAvailableListener() {
            public void onImageAvailable(ImageReader imageReader) {
                Image acquireNextImage = imageReader.acquireNextImage();
                String access$100 = VirtualCameraReprocessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onImageAvailable: received reprocessed image ");
                sb.append(acquireNextImage);
                Log.d(access$100, sb.toString());
                synchronized (VirtualCameraReprocessor.this.mDataLock) {
                    if (VirtualCameraReprocessor.this.mCurrentProcessingData != null) {
                        VirtualCameraReprocessor.this.mCurrentProcessingData.getResultListener().onJpegImageAvailable(acquireNextImage, VirtualCameraReprocessor.this.mCurrentProcessingData.getImageTag(), false);
                        String access$1002 = VirtualCameraReprocessor.TAG;
                        String str = "%s return for %s. cost=%d";
                        Object[] objArr = new Object[3];
                        objArr[0] = imageReader.getImageFormat() == 256 ? "jpeg" : "heic";
                        objArr[1] = VirtualCameraReprocessor.this.mCurrentProcessingData.getImageTag();
                        objArr[2] = Long.valueOf(System.currentTimeMillis() - VirtualCameraReprocessor.this.mReprocessStartTime);
                        Log.d(access$1002, String.format(str, objArr));
                        VirtualCameraReprocessor.this.mCurrentProcessingData = null;
                    } else {
                        Log.w(VirtualCameraReprocessor.TAG, "onImageAvailable: null task!");
                    }
                }
                acquireNextImage.close();
                VirtualCameraReprocessor.this.sendReprocessRequest();
            }
        }, this.mCameraOperationHandler);
        Log.d(TAG, "initPicImageReader<<");
    }

    @WorkerThread
    private void initYuvImageReader(int i, int i2) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initYuvImageReader>>");
        sb.append(i);
        sb.append("x");
        sb.append(i2);
        Log.d(str, sb.toString());
        ImageReader imageReader = this.mYuvImageReader;
        if (!(imageReader == null || (imageReader.getWidth() == i && this.mYuvImageReader.getHeight() == i2))) {
            Log.d(TAG, "closing obsolete yuv reader");
            this.mYuvImageReader.close();
            this.mYuvImageReader = null;
        }
        if (i > 0 && i2 > 0) {
            this.mYuvImageReader = ImageReader.newInstance(i, i2, 35, 2);
            ImageReaderHelper.setImageReaderNameDepends(this.mYuvImageReader, ImageReaderType.YUV, false);
            this.mYuvImageReader.setOnImageAvailableListener(new OnImageAvailableListener() {
                public void onImageAvailable(ImageReader imageReader) {
                    Image acquireNextImage = imageReader.acquireNextImage();
                    long timeStamp = VirtualCameraReprocessor.this.mCurrentProcessingData.getTotalCaptureResult().getTimeStamp();
                    acquireNextImage.setTimestamp(timeStamp);
                    ImagePool.getInstance().queueImage(acquireNextImage);
                    Image image = ImagePool.getInstance().getImage(timeStamp);
                    String access$100 = VirtualCameraReprocessor.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("receive yuv image: ");
                    sb.append(image);
                    sb.append(" | ");
                    sb.append(timeStamp);
                    Log.d(access$100, sb.toString());
                    ImagePool.getInstance().holdImage(image);
                    synchronized (VirtualCameraReprocessor.this.mDataLock) {
                        VirtualCameraReprocessor.this.mCurrentProcessingData.getResultListener().onYuvAvailable(image, VirtualCameraReprocessor.this.mCurrentProcessingData.getImageTag(), true);
                        Log.d(VirtualCameraReprocessor.TAG, String.format("yuv return for %s. cost=%d", new Object[]{VirtualCameraReprocessor.this.mCurrentProcessingData.getImageTag(), Long.valueOf(System.currentTimeMillis() - VirtualCameraReprocessor.this.mReprocessStartTime)}));
                        VirtualCameraReprocessor.this.mCurrentProcessingData = null;
                    }
                    VirtualCameraReprocessor.this.sendReprocessRequest();
                }
            }, this.mCameraOperationHandler);
            Log.d(TAG, "initYuvImageReader<<");
        }
    }

    @WorkerThread
    @SuppressLint({"MissingPermission"})
    private void openVTCamera(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("openVTCamera: ");
        sb.append(str);
        Log.d(str2, sb.toString());
        try {
            this.mCameraManager.openCamera(str, new CameraDevice.StateCallback() {
                public void onClosed(@NonNull CameraDevice cameraDevice) {
                    synchronized (VirtualCameraReprocessor.this.mCameraLock) {
                        String access$100 = VirtualCameraReprocessor.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onClosed>>id=");
                        sb.append(cameraDevice.getId());
                        Log.d(access$100, sb.toString());
                        VirtualCameraReprocessor.this.mCreatingReprocessSession = false;
                        if (cameraDevice == VirtualCameraReprocessor.this.mVTCameraDevice) {
                            String access$1002 = VirtualCameraReprocessor.TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("onClosed: ");
                            sb2.append(VirtualCameraReprocessor.this.mVTCameraDevice);
                            Log.d(access$1002, sb2.toString());
                            VirtualCameraReprocessor.this.mVTCameraDevice = null;
                        }
                        if (VirtualCameraReprocessor.this.mNeedReopenCamera) {
                            VirtualCameraReprocessor.this.sendReprocessRequest();
                            VirtualCameraReprocessor.this.mNeedReopenCamera = false;
                        }
                        Log.d(VirtualCameraReprocessor.TAG, "onClosed<<");
                    }
                }

                public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                    String access$100 = VirtualCameraReprocessor.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onDisconnected>>id=");
                    sb.append(cameraDevice.getId());
                    Log.d(access$100, sb.toString());
                    synchronized (VirtualCameraReprocessor.this.mCameraLock) {
                        boolean z = cameraDevice == VirtualCameraReprocessor.this.mVTCameraDevice;
                        cameraDevice.close();
                        VirtualCameraReprocessor.this.mCreatingReprocessSession = false;
                        if (z) {
                            String access$1002 = VirtualCameraReprocessor.TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("onDisconnected: ");
                            sb2.append(VirtualCameraReprocessor.this.mVTCameraDevice);
                            Log.d(access$1002, sb2.toString());
                            VirtualCameraReprocessor.this.mVTCameraDevice = null;
                        }
                    }
                    synchronized (VirtualCameraReprocessor.this.mDataLock) {
                        if (VirtualCameraReprocessor.this.mCurrentProcessingData != null && z) {
                            ICustomCaptureResult totalCaptureResult = VirtualCameraReprocessor.this.mCurrentProcessingData.getTotalCaptureResult();
                            String access$1003 = VirtualCameraReprocessor.TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("onDisconnected: clear task with timestamp = ");
                            sb3.append(totalCaptureResult != null ? totalCaptureResult.getTimeStamp() : 0);
                            Log.w(access$1003, sb3.toString());
                            VirtualCameraReprocessor.this.mCurrentProcessingData = null;
                        }
                    }
                    Log.d(VirtualCameraReprocessor.TAG, "onDisconnected<<");
                }

                public void onError(@NonNull CameraDevice cameraDevice, int i) {
                    String access$100 = VirtualCameraReprocessor.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onError>>id=");
                    sb.append(cameraDevice.getId());
                    sb.append(" error=");
                    sb.append(i);
                    Log.e(access$100, sb.toString());
                    synchronized (VirtualCameraReprocessor.this.mCameraLock) {
                        boolean z = cameraDevice == VirtualCameraReprocessor.this.mVTCameraDevice;
                        cameraDevice.close();
                        VirtualCameraReprocessor.this.mCreatingReprocessSession = false;
                        if (z) {
                            String access$1002 = VirtualCameraReprocessor.TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("onError: ");
                            sb2.append(VirtualCameraReprocessor.this.mVTCameraDevice);
                            Log.d(access$1002, sb2.toString());
                            VirtualCameraReprocessor.this.mVTCameraDevice = null;
                        }
                    }
                    synchronized (VirtualCameraReprocessor.this.mDataLock) {
                        if (VirtualCameraReprocessor.this.mCurrentProcessingData != null && z) {
                            ICustomCaptureResult totalCaptureResult = VirtualCameraReprocessor.this.mCurrentProcessingData.getTotalCaptureResult();
                            String access$1003 = VirtualCameraReprocessor.TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("onError: clear task with timestamp = ");
                            sb3.append(totalCaptureResult != null ? totalCaptureResult.getTimeStamp() : 0);
                            Log.w(access$1003, sb3.toString());
                            VirtualCameraReprocessor.this.mCurrentProcessingData = null;
                            VirtualCameraReprocessor.this.sendReprocessRequest();
                        }
                    }
                    Log.e(VirtualCameraReprocessor.TAG, "onError<<");
                }

                public void onOpened(@NonNull CameraDevice cameraDevice) {
                    synchronized (VirtualCameraReprocessor.this.mCameraLock) {
                        String access$100 = VirtualCameraReprocessor.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onOpened>>id=");
                        sb.append(cameraDevice.getId());
                        Log.d(access$100, sb.toString());
                        VirtualCameraReprocessor.this.mVTCameraDevice = cameraDevice;
                        String access$1002 = VirtualCameraReprocessor.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("onOpened: ");
                        sb2.append(VirtualCameraReprocessor.this.mVTCameraDevice);
                        Log.d(access$1002, sb2.toString());
                        VirtualCameraReprocessor.this.mCurrentSessionId = -1;
                        VirtualCameraReprocessor.this.mVTCaptureSession = null;
                        VirtualCameraReprocessor.this.mCreatingReprocessSession = false;
                        VirtualCameraReprocessor.this.sendReprocessRequest();
                        Log.d(VirtualCameraReprocessor.TAG, "onOpened<<");
                    }
                }
            }, this.mCameraOperationHandler);
        } catch (CameraAccessException e) {
            Log.e(TAG, "openVTCamera: open camera failed! No permission Exception.", e);
        }
    }

    /* access modifiers changed from: private */
    public void releaseWakeLock() {
        if (this.mWakeLock.isHeld()) {
            Log.d(TAG, "releaseWakeLock");
            this.mWakeLock.release();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0069, code lost:
        r10.mReprocessStartTime = java.lang.System.currentTimeMillis();
        r7 = r10.mCameraLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0071, code lost:
        monitor-enter(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r0 = r10.mVTCameraDevice.createReprocessCaptureRequest(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x007a, code lost:
        if (r10.mSessionFpsRange == null) goto L_0x0083;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x007c, code lost:
        r0.set(android.hardware.camera2.CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, r10.mSessionFpsRange);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0085, code lost:
        if (35 == r3) goto L_0x00f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0087, code lost:
        r0.addTarget(r10.mPicImageReader.getSurface());
        r0.set(android.hardware.camera2.CaptureRequest.JPEG_QUALITY, java.lang.Byte.valueOf(r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0099, code lost:
        if (r4 == false) goto L_0x0100;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x009b, code lost:
        r1 = (java.lang.Integer) r1.get(android.hardware.camera2.CaptureResult.JPEG_ORIENTATION);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00a6, code lost:
        if (1212500294 != r3) goto L_0x0100;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00a8, code lost:
        if (r1 == null) goto L_0x0100;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00ae, code lost:
        if (r1.intValue() == 0) goto L_0x0100;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r0.set((android.hardware.camera2.CaptureRequest.Key) com.xiaomi.camera.imagecodec.CaptureRequestVendorTags.XIAOMI_JPEG_ORIENTATION.getKey(), r1);
        r2 = TAG;
        r3 = new java.lang.StringBuilder();
        r3.append("reprocessImage: jpegOrientation=");
        r3.append(r1);
        android.util.Log.d(r2, r3.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00d2, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r2 = TAG;
        r3 = new java.lang.StringBuilder();
        r3.append("reprocessImage: failed to set XIAOMI_JPEG_ORIENTATION. ");
        r3.append(r1.getMessage());
        android.util.Log.e(r2, r3.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00f7, code lost:
        r0.addTarget(r10.mYuvImageReader.getSurface());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0141, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        r10.printStackTrace();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @WorkerThread
    public void reprocessImage() {
        Image image;
        Object obj;
        Builder createReprocessCaptureRequest;
        Log.d(TAG, "reprocessImage>>");
        synchronized (this.mDataLock) {
            this.mCurrentProcessingData = (ReprocessData) this.mTaskDataList.poll();
            TotalCaptureResult totalCaptureResult = ICustomCaptureResult.toTotalCaptureResult(this.mCurrentProcessingData.getTotalCaptureResult(), this.mCurrentSessionId, true);
            if (totalCaptureResult == null) {
                Log.wtf(TAG, "reprocessImage<<null metadata!");
                return;
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("reprocessImage: tag=");
            sb.append(this.mCurrentProcessingData.getImageTag());
            Log.d(str, sb.toString());
            byte jpegQuality = (byte) this.mCurrentProcessingData.getJpegQuality();
            int outputFormat = this.mCurrentProcessingData.getOutputFormat();
            boolean isRotateOrientationToZero = this.mCurrentProcessingData.isRotateOrientationToZero();
            image = (Image) this.mCurrentProcessingData.getMainImage().get(0);
        }
        Log.d(TAG, "reprocessImage<<");
        createReprocessCaptureRequest.set(CaptureRequest.JPEG_ORIENTATION, Integer.valueOf(0));
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("reprocessImage: ");
        sb2.append(image);
        sb2.append(" | ");
        sb2.append(image.getTimestamp());
        Log.d(str2, sb2.toString());
        this.mReprocessImageWriter.queueInputImage(image);
        ImagePool.getInstance().releaseImage(image);
        this.mVTCaptureSession.capture(createReprocessCaptureRequest.build(), new CaptureCallback() {
            public void onCaptureBufferLost(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, Surface surface, long j) {
                String access$100 = VirtualCameraReprocessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureBufferLost: frameNo=");
                sb.append(j);
                sb.append(" target=");
                sb.append(surface);
                Log.e(access$100, sb.toString());
            }

            public void onCaptureCompleted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull TotalCaptureResult totalCaptureResult) {
                String access$100 = VirtualCameraReprocessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureCompleted: frameNo=");
                sb.append(totalCaptureResult.getFrameNumber());
                sb.append(" taskSize=");
                sb.append(VirtualCameraReprocessor.this.mTaskDataList.size());
                Log.d(access$100, sb.toString());
            }

            public void onCaptureFailed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureFailure captureFailure) {
                String access$100 = VirtualCameraReprocessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureFailed: frameNo=");
                sb.append(captureFailure.getFrameNumber());
                sb.append(" reason=");
                sb.append(captureFailure.getReason());
                sb.append(" imageCaptured=");
                sb.append(captureFailure.wasImageCaptured());
                Log.e(access$100, sb.toString());
                synchronized (VirtualCameraReprocessor.this.mDataLock) {
                    if (VirtualCameraReprocessor.this.mCurrentProcessingData != null) {
                        OnDataAvailableListener resultListener = VirtualCameraReprocessor.this.mCurrentProcessingData.getResultListener();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("");
                        sb2.append(captureFailure.getReason());
                        resultListener.onError(sb2.toString(), VirtualCameraReprocessor.this.mCurrentProcessingData.getImageTag());
                        VirtualCameraReprocessor.this.mCurrentProcessingData = null;
                    }
                }
                VirtualCameraReprocessor.this.sendReprocessRequest();
            }
        }, this.mCameraOperationHandler);
        Log.d(TAG, "reprocessImage<<");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0024, code lost:
        r1 = r5.mDataLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0026, code lost:
        monitor-enter(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002e, code lost:
        if (r5.mTaskDataList.isEmpty() == false) goto L_0x0040;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0030, code lost:
        android.util.Log.d(TAG, "sendReprocessRequest: idle. Try to close device 30s later.");
        r5.mReprocessHandler.sendEmptyMessageDelayed(2, 30000);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003e, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003f, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0046, code lost:
        if (r5.mReprocessHandler.hasMessages(2) == false) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0048, code lost:
        r5.mReprocessHandler.removeMessages(2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004d, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0055, code lost:
        if (r5.mReprocessHandler.hasMessages(1) == false) goto L_0x005f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0057, code lost:
        android.util.Log.d(TAG, "sendReprocessRequest: busy");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x005f, code lost:
        android.util.Log.d(TAG, "sendReprocessRequest: send MSG_REPROCESS_IMG");
        r5.mReprocessHandler.sendEmptyMessageDelayed(1, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x006d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @AnyThread
    public void sendReprocessRequest() {
        Log.i(TAG, "=============================================================");
        if (!this.mInitialized) {
            Log.w(TAG, "sendReprocessRequest: NOT initialized!");
            return;
        }
        synchronized (this.mCameraLock) {
            if (this.mCreatingReprocessSession) {
                Log.d(TAG, "sendReprocessRequest: creating session...");
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001b, code lost:
        monitor-enter(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001e, code lost:
        if (r3.mVTCaptureSession == null) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0020, code lost:
        r3.mVTCaptureSession.close();
        r3.mVTCaptureSession = null;
        r3.mPicImageReader = null;
        r3.mYuvImageReader = null;
        r3.mReprocessImageWriter = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
        if (r3.mVTCameraDevice == null) goto L_0x0038;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0031, code lost:
        r3.mVTCameraDevice.close();
        r3.mVTCameraDevice = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0038, code lost:
        monitor-exit(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0039, code lost:
        r0 = r3.mCameraOperationThread;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003b, code lost:
        if (r0 == null) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003d, code lost:
        r0.quitSafely();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r3.mCameraOperationThread.join();
        r3.mCameraOperationThread = null;
        r3.mCameraOperationHandler = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004a, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004b, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0019, code lost:
        r2 = r3.mCameraLock;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @AnyThread
    public void deInit() {
        Log.d(TAG, "deInit>>");
        synchronized (this.mDataLock) {
            if (this.mInitialized) {
                this.mInitialized = false;
                this.mCameraManager = null;
                this.mCurrentProcessingData = null;
            } else {
                return;
            }
        }
        Log.d(TAG, "deInit<<");
        HandlerThread handlerThread = this.mReprocessThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            try {
                this.mReprocessThread.join();
                this.mReprocessThread = null;
                this.mReprocessHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "deInit<<");
    }

    @AnyThread
    public void init(Context context) {
        Log.d(TAG, "init>>");
        synchronized (this.mDataLock) {
            if (!this.mInitialized) {
                this.mWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, TAG);
                this.mWakeLock.setReferenceCounted(false);
                this.mCameraManager = (CameraManager) context.getSystemService("camera");
                this.mCameraOperationThread = new HandlerThread("CameraOperationThread");
                this.mCameraOperationThread.start();
                this.mCameraOperationHandler = new Handler(this.mCameraOperationThread.getLooper());
                this.mReprocessThread = new HandlerThread("JpegEncoderThread");
                this.mReprocessThread.start();
                this.mReprocessHandler = new ReprocessHandler(this.mReprocessThread.getLooper());
                this.mInitialized = true;
            }
        }
        Log.d(TAG, "init<<");
    }

    public void setOutputPictureSpec(int i, int i2, int i3) {
        if (this.mPicOutputConfiguration == null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setOutputPictureSpec: ");
            sb.append(i);
            sb.append("x");
            sb.append(i2);
            sb.append("@");
            sb.append(Integer.toHexString(i3));
            Log.d(str, sb.toString());
            this.mPicOutputConfiguration = new OutputConfiguration(i, i2, i3);
        }
    }

    @AnyThread
    public void setVirtualCameraIds(@NonNull String str, @NonNull String str2) {
        Log.d(TAG, String.format("setVTCameraIds: backId=%s frontId=%s", new Object[]{str, str2}));
        this.mBackVtCameraId = str;
        this.mFrontVtCameraId = str2;
    }

    @AnyThread
    public void submit(ReprocessData reprocessData) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("submit: ");
        sb.append(reprocessData.getImageTag());
        sb.append(", yuv = ");
        sb.append(reprocessData.getMainImage());
        sb.append(", fmt = ");
        sb.append(reprocessData.getOutputFormat());
        Log.d(str, sb.toString());
        if (reprocessData.getResultListener() == null) {
            Log.d(TAG, "submit: drop this request due to no callback was provided!");
        } else if (this.mInitialized) {
            acquireWakeLock();
            if (!reprocessData.isImageFromPool()) {
                Image image = (Image) reprocessData.getMainImage().get(0);
                ImageFormat imageQueueKey = ImagePool.getInstance().toImageQueueKey(image);
                if (ImagePool.getInstance().isImageQueueFull(imageQueueKey, 2)) {
                    Log.w(TAG, "submit: wait image pool>>");
                    ImagePool.getInstance().waitIfImageQueueFull(imageQueueKey, 2, 0);
                    Log.w(TAG, "submit: wait image pool<<");
                }
                long timestamp = image.getTimestamp();
                ImagePool.getInstance().queueImage(image);
                Image image2 = ImagePool.getInstance().getImage(timestamp);
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("submit: image: ");
                sb2.append(image2);
                sb2.append(" | ");
                sb2.append(timestamp);
                Log.d(str2, sb2.toString());
                reprocessData.setMainImage(image2);
                ImagePool.getInstance().holdImage(image2);
            }
            synchronized (this.mDataLock) {
                this.mTaskDataList.add(reprocessData);
            }
            sendReprocessRequest();
        } else {
            throw new RuntimeException("NOT initialized. Call init() first!");
        }
    }
}
