package com.android.camera.module.loader.camera2;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.params.OutputConfiguration;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Surface;
import androidx.annotation.NonNull;
import com.android.camera.CameraAppImpl;
import com.android.camera.constant.ExceptionConstant;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CaptureRequestBuilder;
import com.android.camera2.CaptureSessionConfigurations;
import com.android.camera2.compat.MiCameraCompat;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"MissingPermission"})
@TargetApi(21)
public class ParallelSnapshotManager {
    private static final int MANAGER_MSG_CLOSE_CAMERA = 2;
    private static final int MANAGER_MSG_CLOSE_FINISH = 3;
    private static final int MANAGER_MSG_CLOSE_SESSION = 6;
    private static final int MANAGER_MSG_CREATE_SESSION = 5;
    private static final int MANAGER_MSG_OPEN_FINISH = 4;
    private static final int MANAGER_MSG_REQUEST_CAMERA = 1;
    private static final int MANAGER_MSG_SET_CLOSE_STATE = 7;
    private static final int MANAGER_STATE_IDLE = 1;
    private static final int MANAGER_STATE_PENDING_CREATE_SESSION = 5;
    private static final int MANAGER_STATE_WAITING_CLOSE = 2;
    private static final int MANAGER_STATE_WAITING_CREATE_SESSION = 4;
    private static final int MANAGER_STATE_WAITING_OPEN = 3;
    public static final int REASON_DISCONNECTED = 1;
    /* access modifiers changed from: private */
    public static final String TAG = "ParallelSnapshotManager";
    public static boolean isParallelTagOpen;
    private static ParallelSnapshotManager sInstance;
    private int CAMERA_ID_VIRTUAL_PARALLEL;
    /* access modifiers changed from: private */
    public CameraCaptureSession mCameraCaptureSession;
    /* access modifiers changed from: private */
    public CameraDevice mCameraDevice;
    /* access modifiers changed from: private */
    public Handler mCameraHandler;
    private CameraManager mCameraManager;
    private final StateCallback mCameraOpenCallback = new StateCallback() {
        public void onClosed(@NonNull CameraDevice cameraDevice) {
            String access$100 = ParallelSnapshotManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("CameraOpenCallback: closed ");
            sb.append(cameraDevice.getId());
            Log.d(access$100, sb.toString());
            ParallelSnapshotManager.this.mCameraHandler.sendEmptyMessage(3);
        }

        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            String access$100 = ParallelSnapshotManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("CameraOpenCallback: onDisconnected ");
            sb.append(cameraDevice.getId());
            Log.d(access$100, sb.toString());
            if (ParallelSnapshotManager.this.mCameraDevice != null) {
                Message obtainMessage = ParallelSnapshotManager.this.mCameraHandler.obtainMessage();
                obtainMessage.what = 2;
                obtainMessage.arg1 = 1;
                ParallelSnapshotManager.this.mCameraHandler.sendMessageAtFrontOfQueue(obtainMessage);
            }
        }

        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            StringBuilder sb = new StringBuilder();
            sb.append("onError: cameraId=");
            sb.append(cameraDevice.getId());
            sb.append(" error=");
            sb.append(i);
            String sb2 = sb.toString();
            String access$100 = ParallelSnapshotManager.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("CameraOpenCallback: ");
            sb3.append(sb2);
            Log.e(access$100, sb3.toString());
            ParallelSnapshotManager.this.removeAllAppMessages();
            synchronized (ParallelSnapshotManager.this.mCaptureLock) {
                ParallelSnapshotManager.this.mIsCameraOpened = false;
                ParallelSnapshotManager.this.mCurrentSurfaceList = null;
                ParallelSnapshotManager.this.mPendingSurfaceList = null;
                ParallelSnapshotManager.this.mCameraDevice = null;
                ParallelSnapshotManager.this.mCameraCaptureSession = null;
                ParallelSnapshotManager.this.setManagerState(1);
            }
            ParallelSnapshotManager.this.onCameraOpenFailed(ExceptionConstant.transFromCamera2Error(i), sb2);
        }

        public void onOpened(@NonNull CameraDevice cameraDevice) {
            int parseInt = Integer.parseInt(cameraDevice.getId());
            CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(parseInt);
            ParallelSnapshotManager.this.mSessionConfigs = new CaptureSessionConfigurations(capabilities);
            ParallelSnapshotManager parallelSnapshotManager = ParallelSnapshotManager.this;
            boolean z = VERSION.SDK_INT >= 30 && capabilities.isCinematicVideoSupported();
            parallelSnapshotManager.mIsSupportCinematicVideoKey = z;
            StringBuilder sb = new StringBuilder();
            sb.append("CameraOpenCallback: camera ");
            sb.append(parseInt);
            sb.append(" was opened successfully");
            String sb2 = sb.toString();
            if (capabilities != null) {
                Message obtain = Message.obtain();
                obtain.what = 4;
                obtain.obj = cameraDevice;
                ParallelSnapshotManager.this.mCameraHandler.sendMessage(obtain);
                return;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append(", but corresponding CameraCapabilities is null");
            String sb4 = sb3.toString();
            Log.e(ParallelSnapshotManager.TAG, sb4);
            ParallelSnapshotManager.this.onCameraOpenFailed(231, sb4);
        }
    };
    /* access modifiers changed from: private */
    public final Object mCaptureLock = new Object();
    private int mCurrentActivityCode = 0;
    private int mCurrentState = 1;
    /* access modifiers changed from: private */
    public List mCurrentSurfaceList;
    /* access modifiers changed from: private */
    public boolean mIsCameraOpened;
    /* access modifiers changed from: private */
    public boolean mIsCreateSession = false;
    private boolean mIsNeedCloseCamera;
    private boolean mIsNeedReOpenCamera;
    /* access modifiers changed from: private */
    public boolean mIsSupportCinematicVideoKey = false;
    private int mMaxQueueSize;
    /* access modifiers changed from: private */
    public List mPendingSurfaceList;
    /* access modifiers changed from: private */
    public CaptureSessionConfigurations mSessionConfigs;
    private int mTeleParallelSurfaceIndex;
    private int mUltraTeleParallelSurfaceIndex;
    private int mUltraWideParallelSurfaceIndex;
    private int mWideParallelSurfaceIndex;

    class CaptureSessionStateCallback extends CameraCaptureSession.StateCallback {
        private CaptureSessionStateCallback() {
        }

        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
            Log.d(ParallelSnapshotManager.TAG, "onConfigureFailed");
            synchronized (ParallelSnapshotManager.this.mCaptureLock) {
                ParallelSnapshotManager.this.mCameraCaptureSession = null;
                ParallelSnapshotManager.this.mIsCreateSession = false;
                ParallelSnapshotManager.this.mCaptureLock.notifyAll();
            }
            ParallelSnapshotManager.this.setManagerState(1);
        }

        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
            Log.d(ParallelSnapshotManager.TAG, "onConfigured>>");
            synchronized (ParallelSnapshotManager.this.mCaptureLock) {
                ParallelSnapshotManager.this.mCameraCaptureSession = cameraCaptureSession;
                ParallelSnapshotManager.this.mIsCreateSession = false;
                ParallelSnapshotManager.this.mCaptureLock.notifyAll();
            }
            ParallelSnapshotManager.this.setManagerState(1);
            Log.d(ParallelSnapshotManager.TAG, "onConfigured<<");
        }
    }

    @interface ManagerState {
    }

    private ParallelSnapshotManager() {
        HandlerThread handlerThread = new HandlerThread("VT Camera Handler Thread");
        handlerThread.start();
        this.mCameraHandler = new Handler(handlerThread.getLooper()) {
            public void handleMessage(Message message) {
                ParallelSnapshotManager.this.onMessage(message);
            }
        };
        this.mCameraManager = (CameraManager) CameraAppImpl.getAndroidContext().getSystemService("camera");
        this.CAMERA_ID_VIRTUAL_PARALLEL = Camera2DataContainer.getInstance().getParallelVirtualCameraId();
    }

    public static synchronized ParallelSnapshotManager getInstance() {
        ParallelSnapshotManager parallelSnapshotManager;
        synchronized (ParallelSnapshotManager.class) {
            if (sInstance == null) {
                sInstance = new ParallelSnapshotManager();
            }
            parallelSnapshotManager = sInstance;
        }
        return parallelSnapshotManager;
    }

    @ManagerState
    private int getManagerState() {
        return this.mCurrentState;
    }

    /* access modifiers changed from: private */
    public void onCameraOpenFailed(int i, String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCameraOpenFailed: ");
        sb.append(i);
        sb.append(" msg:");
        sb.append(str);
        Log.e(str2, sb.toString());
        this.mIsCameraOpened = false;
        setManagerState(1);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:117:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMessage(Message message) {
        switch (message.what) {
            case 1:
                if (getManagerState() == 1) {
                    try {
                        Log.d(TAG, "open start");
                        setManagerState(3);
                        this.mCameraManager.openCamera(String.valueOf(this.CAMERA_ID_VIRTUAL_PARALLEL), this.mCameraOpenCallback, this.mCameraHandler);
                        return;
                    } catch (CameraAccessException | IllegalArgumentException | SecurityException e) {
                        e.printStackTrace();
                        onCameraOpenFailed(230, e.getClass().getSimpleName());
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("openCamera: failed to open camera ");
                        sb.append(this.CAMERA_ID_VIRTUAL_PARALLEL);
                        Log.e(str, sb.toString(), (Throwable) e);
                        return;
                    }
                } else if (getManagerState() == 2) {
                    this.mIsNeedReOpenCamera = true;
                    return;
                } else {
                    return;
                }
            case 2:
                if (getManagerState() != 1) {
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("not idle, break on msg.what ");
                    sb2.append(message.what);
                    sb2.append(", mCurrentState ");
                    sb2.append(this.mCurrentState);
                    Log.w(str2, sb2.toString());
                    return;
                }
                synchronized (this.mCaptureLock) {
                    if (this.mIsCameraOpened && this.mCameraDevice != null) {
                        setManagerState(2);
                        int i = message.arg1;
                        String str3 = TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("force close start reason ");
                        sb3.append(i);
                        Log.d(str3, sb3.toString());
                        this.mCameraDevice.close();
                        this.mCameraDevice = null;
                        this.mCameraCaptureSession = null;
                        this.mCurrentSurfaceList = null;
                        this.mIsCameraOpened = false;
                    }
                }
                return;
            case 3:
                synchronized (this.mCaptureLock) {
                    this.mIsCameraOpened = false;
                    this.mCameraDevice = null;
                    this.mCameraCaptureSession = null;
                    this.mCurrentSurfaceList = null;
                    setManagerState(1);
                    Log.d(TAG, "close finish");
                    if (this.mIsNeedReOpenCamera) {
                        if (!this.mIsNeedCloseCamera) {
                            this.mIsCameraOpened = true;
                            Log.d(TAG, "receive open request, need to reopen camera device");
                            this.mCameraHandler.sendEmptyMessage(1);
                        }
                        this.mIsNeedReOpenCamera = false;
                    }
                }
                return;
            case 4:
                Log.d(TAG, "open finish");
                this.mCameraDevice = (CameraDevice) message.obj;
                if (getManagerState() == 5) {
                    Log.w(TAG, "try to recreate session");
                    createCaptureSession(this.mPendingSurfaceList);
                    this.mPendingSurfaceList = null;
                }
                setManagerState(1);
                return;
            case 5:
                synchronized (this.mCaptureLock) {
                    Log.d(TAG, "create CaptureSession >>>>>>");
                    if (getManagerState() == 1) {
                        if (this.mCurrentSurfaceList == null || !this.mCurrentSurfaceList.equals((List) message.obj)) {
                            if (this.mCameraCaptureSession == null) {
                                if (!this.mIsNeedCloseCamera) {
                                    if (this.mCameraDevice != null) {
                                        setManagerState(4);
                                        try {
                                            Builder createCaptureRequest = this.mCameraDevice.createCaptureRequest(2);
                                            ArrayList arrayList = new ArrayList();
                                            List list = (List) message.obj;
                                            String str4 = TAG;
                                            StringBuilder sb4 = new StringBuilder();
                                            sb4.append("surfaces.size:");
                                            sb4.append(list.size());
                                            Log.d(str4, sb4.toString());
                                            int size = list.size();
                                            for (int i2 = 0; i2 < size; i2++) {
                                                arrayList.add(new OutputConfiguration((Surface) list.get(i2)));
                                            }
                                            if (this.mIsSupportCinematicVideoKey) {
                                                this.mSessionConfigs.set(CaptureRequestVendorTags.CINEMATIC_VIDEO_ENABLED, (Object) Byte.valueOf(0));
                                                MiCameraCompat.applyCinematicVideo(createCaptureRequest, 0);
                                                CaptureRequestBuilder.applySessionParameters(createCaptureRequest, this.mSessionConfigs);
                                            }
                                            CompatibilityUtils.createCaptureSessionWithSessionConfiguration(this.mCameraDevice, CameraCapabilities.SESSION_OPERATION_MODE_ALGO_UP_SAT, null, arrayList, createCaptureRequest.build(), new CaptureSessionStateCallback(), this.mCameraHandler);
                                        } catch (CameraAccessException | IllegalArgumentException e2) {
                                            e2.printStackTrace();
                                        }
                                        this.mCurrentSurfaceList = (List) message.obj;
                                        Log.d(TAG, "createSession<<");
                                        return;
                                    }
                                }
                                Log.w(TAG, "camera app is not active and surface list maybe null, so dont create session");
                                this.mCurrentSurfaceList = null;
                                this.mPendingSurfaceList = null;
                                this.mCaptureLock.notifyAll();
                                break;
                            } else {
                                Log.d(TAG, "cameraCaptureSession is not null");
                                this.mPendingSurfaceList = (List) message.obj;
                                this.mCaptureLock.notifyAll();
                                break;
                            }
                        } else {
                            Log.d(TAG, "the same surface, skip");
                            this.mCaptureLock.notifyAll();
                            break;
                        }
                    } else {
                        String str5 = TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("MANAGER_MSG_CREATE_SESSION manager state:");
                        sb5.append(getManagerState());
                        Log.w(str5, sb5.toString());
                        if (getManagerState() == 3) {
                            this.mPendingSurfaceList = (List) message.obj;
                            setManagerState(5);
                            Log.w(TAG, "waiting camera open finish to recreate session");
                        }
                        this.mCaptureLock.notifyAll();
                        break;
                    }
                }
                break;
            case 6:
                if (getManagerState() != 1) {
                    String str6 = TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("MANAGER_MSG_CLOSE_SESSION manager state:");
                    sb6.append(getManagerState());
                    Log.w(str6, sb6.toString());
                    return;
                }
                synchronized (this.mCaptureLock) {
                    if (this.mCameraCaptureSession != null) {
                        this.mCameraCaptureSession.close();
                        this.mCameraCaptureSession = null;
                        this.mCurrentSurfaceList = null;
                        Log.d(TAG, "CaptureSession close");
                    }
                    if (this.mIsNeedCloseCamera) {
                        Log.d(TAG, "begin to close camera");
                        closeCamera();
                    } else if (this.mPendingSurfaceList != null && !this.mIsNeedCloseCamera) {
                        Log.d(TAG, " have surface pending list, need to recreate session");
                        createCaptureSession(this.mPendingSurfaceList);
                        this.mPendingSurfaceList = null;
                    }
                }
                return;
            case 7:
                this.mIsNeedCloseCamera = Boolean.parseBoolean(message.obj.toString());
                String str7 = TAG;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("set close camera device state mIsNeedCloseCamera:");
                sb7.append(this.mIsNeedCloseCamera);
                Log.d(str7, sb7.toString());
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void removeAllAppMessages() {
        this.mCameraHandler.removeMessages(1);
        this.mCameraHandler.removeMessages(3);
        this.mCameraHandler.removeMessages(4);
        this.mCameraHandler.removeMessages(2);
    }

    /* access modifiers changed from: private */
    public void setManagerState(@ManagerState int i) {
        this.mCurrentState = i;
    }

    public void closeCamera() {
        synchronized (this.mCaptureLock) {
            if (this.mIsCameraOpened) {
                this.mCameraHandler.sendEmptyMessage(2);
            }
        }
    }

    public void closeCaptureSession(List list) {
        synchronized (this.mCaptureLock) {
            if (this.mIsCreateSession) {
                if (this.mCameraHandler.hasMessages(5)) {
                    this.mCameraHandler.removeMessages(5);
                } else {
                    try {
                        Log.d(TAG, "wait 500ms to create session finish");
                        this.mCaptureLock.wait(500, 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.mIsCreateSession = false;
            }
            if (!(list == null || this.mCurrentSurfaceList == null || !list.containsAll(this.mCurrentSurfaceList))) {
                this.mCameraHandler.sendEmptyMessage(6);
            }
        }
    }

    public void createCaptureSession(List list) {
        Message obtain = Message.obtain();
        obtain.obj = list;
        obtain.what = 5;
        synchronized (this.mCaptureLock) {
            this.mIsCreateSession = true;
            this.mCameraHandler.sendMessage(obtain);
        }
    }

    public CameraDevice getCameraDevice() {
        CameraDevice cameraDevice;
        synchronized (this.mCaptureLock) {
            cameraDevice = this.mCameraDevice;
        }
        return cameraDevice;
    }

    public CameraCaptureSession getCaptureSession() {
        CameraCaptureSession cameraCaptureSession;
        synchronized (this.mCaptureLock) {
            cameraCaptureSession = this.mCameraCaptureSession;
        }
        return cameraCaptureSession;
    }

    public Surface getCaptureSurface(int i) {
        List list;
        int i2;
        if (i == 1) {
            list = this.mCurrentSurfaceList;
            i2 = this.mUltraWideParallelSurfaceIndex;
        } else if (i == 2) {
            list = this.mCurrentSurfaceList;
            i2 = this.mWideParallelSurfaceIndex;
        } else if (i == 3) {
            list = this.mCurrentSurfaceList;
            i2 = this.mTeleParallelSurfaceIndex;
        } else if (i != 4) {
            return null;
        } else {
            list = this.mCurrentSurfaceList;
            i2 = this.mUltraTeleParallelSurfaceIndex;
        }
        return (Surface) list.get(i2);
    }

    public int getMaxQueueSize() {
        return this.mMaxQueueSize;
    }

    public boolean isParallelSessionReady() {
        return this.mPendingSurfaceList == null && this.mCameraCaptureSession != null;
    }

    public void openCamera() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append(" CAMERA_ID_VIRTUAL_PARALLEL = ");
        sb.append(this.CAMERA_ID_VIRTUAL_PARALLEL);
        sb.append(" mIsCameraOpened:");
        sb.append(this.mIsCameraOpened);
        Log.d(str, sb.toString());
        synchronized (this.mCaptureLock) {
            if (!this.mIsCameraOpened) {
                this.mCameraHandler.sendEmptyMessage(1);
                this.mIsCameraOpened = true;
            }
        }
    }

    public void release() {
        this.mCameraHandler.removeMessages(1);
        Handler handler = this.mCameraHandler;
        handler.sendMessage(handler.obtainMessage(2));
    }

    public void setCameraCloseState(boolean z, int i) {
        Message obtain;
        Handler handler;
        synchronized (this.mCaptureLock) {
            if (!z) {
                this.mCurrentActivityCode = i;
                obtain = Message.obtain();
                obtain.obj = Boolean.valueOf(z);
                obtain.what = 7;
                handler = this.mCameraHandler;
            } else if (this.mCurrentActivityCode == i) {
                obtain = Message.obtain();
                obtain.obj = Boolean.valueOf(z);
                obtain.what = 7;
                handler = this.mCameraHandler;
            }
            handler.sendMessage(obtain);
        }
    }

    public void setMaxQueueSize(int i) {
        this.mMaxQueueSize = i;
    }

    public void setSurfaceIndex(int i, int i2) {
        if (i == 1) {
            this.mUltraWideParallelSurfaceIndex = i2;
        } else if (i == 2) {
            this.mWideParallelSurfaceIndex = i2;
        } else if (i == 3) {
            this.mTeleParallelSurfaceIndex = i2;
        } else if (i == 4) {
            this.mUltraTeleParallelSurfaceIndex = i2;
        }
    }
}
