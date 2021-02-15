package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.storage.ImageSaver;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.PostProcessor;
import com.xiaomi.camera.core.PostProcessor.PostProcessStatusCallback;
import com.xiaomi.camera.imagecodec.FeatureSetting;
import com.xiaomi.camera.imagecodec.QueryFeatureSettingParameter;
import com.xiaomi.camera.imagecodec.Reprocessor;
import com.xiaomi.camera.imagecodec.ReprocessorFactory;
import com.xiaomi.camera.imagecodec.ReprocessorFactory.ReprocessorType;
import com.xiaomi.camera.isp.IspInterfaceIO;
import com.xiaomi.engine.BufferFormat;
import com.xiaomi.engine.MiCameraAlgo;
import com.xiaomi.protocol.ICustomCaptureResult;
import com.xiaomi.protocol.IImageReaderParameterSets;
import com.xiaomi.protocol.ISessionStatusCallBackListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocalParallelService extends Service {
    /* access modifiers changed from: private */
    public static final String TAG = "LocalParallelService";
    private static int sReprocessorTypeIndex = -1;
    /* access modifiers changed from: private */
    public LocalBinder mLocalBinder;
    /* access modifiers changed from: private */
    public int mMaxParallelRequestNumber;
    /* access modifiers changed from: private */
    public PostProcessStatusCallback mPostProcessStatusCallback = new PostProcessStatusCallback() {
        public void onImagePostProcessEnd(ParallelTaskData parallelTaskData) {
            ServiceStatusListener serviceStatusListener;
            if (parallelTaskData.getServiceStatusListener() != null) {
                serviceStatusListener = parallelTaskData.getServiceStatusListener();
            } else if (LocalParallelService.this.mServiceStatusListenerRef != null && LocalParallelService.this.mServiceStatusListenerRef.get() != null) {
                serviceStatusListener = (ServiceStatusListener) LocalParallelService.this.mServiceStatusListenerRef.get();
            } else {
                return;
            }
            serviceStatusListener.onImagePostProcessEnd(parallelTaskData);
        }

        public void onImagePostProcessStart(ParallelTaskData parallelTaskData) {
            ServiceStatusListener serviceStatusListener;
            if (parallelTaskData.getServiceStatusListener() != null) {
                serviceStatusListener = parallelTaskData.getServiceStatusListener();
            } else if (LocalParallelService.this.mServiceStatusListenerRef != null && LocalParallelService.this.mServiceStatusListenerRef.get() != null) {
                serviceStatusListener = (ServiceStatusListener) LocalParallelService.this.mServiceStatusListenerRef.get();
            } else {
                return;
            }
            serviceStatusListener.onImagePostProcessStart(parallelTaskData);
        }

        public void onPostProcessorClosed(PostProcessor postProcessor) {
            if (LocalParallelService.this.mLocalBinder != null) {
                LocalParallelService.this.mLocalBinder.onPostProcessorClosed(postProcessor);
            }
        }
    };
    /* access modifiers changed from: private */
    public final Object mPostProcessorLock = new Object();
    /* access modifiers changed from: private */
    public boolean mSRRequireReprocess;
    /* access modifiers changed from: private */
    public WeakReference mServiceStatusListenerRef;

    public class LocalBinder extends Binder {
        private List mAlivePostProcessor = new CopyOnWriteArrayList();
        private BufferFormat mCurrentBufferFormat;
        private List mCurrentParams;
        private PostProcessor mCurrentPostProcessor;

        LocalBinder() {
            ReprocessorFactory.init(LocalParallelService.this);
            updateVirtualCameraIds();
            LocalParallelService.getReprocessor().init(LocalParallelService.this);
            MiCameraAlgo.init(LocalParallelService.this);
        }

        private void initCurrentPostProcessor(@NonNull List list) {
            this.mCurrentParams = list;
            LocalParallelService localParallelService = LocalParallelService.this;
            this.mCurrentPostProcessor = new PostProcessor(localParallelService, localParallelService.mPostProcessStatusCallback);
            this.mCurrentPostProcessor.setMaxParallelRequestNumber(LocalParallelService.this.mMaxParallelRequestNumber);
            this.mCurrentPostProcessor.setSRRequireReprocess(LocalParallelService.this.mSRRequireReprocess);
            String access$400 = LocalParallelService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("initCurrentPostProcessor: create a new PostProcessor: ");
            sb.append(this.mCurrentPostProcessor);
            sb.append(" | maxParallelRequestNumber: ");
            sb.append(LocalParallelService.this.mMaxParallelRequestNumber);
            Log.d(access$400, sb.toString());
            this.mCurrentBufferFormat = null;
            this.mCurrentPostProcessor.setParams(list);
            synchronized (LocalParallelService.this.mPostProcessorLock) {
                this.mAlivePostProcessor.add(this.mCurrentPostProcessor);
            }
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.xiaomi.protocol.IImageReaderParameterSets>, for r6v0, types: [java.util.List, java.util.List<com.xiaomi.protocol.IImageReaderParameterSets>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean isSetsEquals(List<IImageReaderParameterSets> list, List list2) {
            boolean z = false;
            if (!(list == null || list2 == null)) {
                if (list.size() != list2.size()) {
                    return false;
                }
                int i = 0;
                for (IImageReaderParameterSets iImageReaderParameterSets : list) {
                    Iterator it = list2.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (iImageReaderParameterSets.equals((IImageReaderParameterSets) it.next())) {
                                i++;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                if (list.size() == i) {
                    z = true;
                }
            }
            return z;
        }

        /* access modifiers changed from: private */
        public void onPostProcessorClosed(PostProcessor postProcessor) {
            synchronized (LocalParallelService.this.mPostProcessorLock) {
                String access$400 = LocalParallelService.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onPostProcessorClosed: processor:");
                sb.append(postProcessor);
                Log.d(access$400, sb.toString());
                this.mAlivePostProcessor.remove(postProcessor);
            }
        }

        public List configCaptureOutputBuffer(@NonNull List list, int i) {
            if (!list.isEmpty()) {
                if (isSetsEquals(list, this.mCurrentParams)) {
                    PostProcessor postProcessor = this.mCurrentPostProcessor;
                    if (postProcessor != null && !postProcessor.isStopping() && !this.mCurrentPostProcessor.getSurfaceList().isEmpty()) {
                        Log.d(LocalParallelService.TAG, "configCaptureOutputBuffer: sets is not changed, return the old.");
                        this.mCurrentPostProcessor.setToken(i);
                        return new ArrayList(this.mCurrentPostProcessor.getSurfaceList());
                    }
                }
                String access$400 = LocalParallelService.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configCaptureOutputBuffer: sets is changed, mAlivePostProcessor.size is ");
                sb.append(this.mAlivePostProcessor.size());
                Log.d(access$400, sb.toString());
                PostProcessor postProcessor2 = null;
                synchronized (LocalParallelService.this.mPostProcessorLock) {
                    for (int i2 = 0; i2 < this.mAlivePostProcessor.size(); i2++) {
                        PostProcessor postProcessor3 = (PostProcessor) this.mAlivePostProcessor.get(i2);
                        if (isSetsEquals(list, postProcessor3.getParams()) && postProcessor3 != null && !postProcessor3.getSurfaceList().isEmpty()) {
                            String access$4002 = LocalParallelService.TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("configCaptureOutputBuffer: ready to reuse activeProcessor:");
                            sb2.append(postProcessor3);
                            Log.d(access$4002, sb2.toString());
                            postProcessor2 = postProcessor3;
                        }
                    }
                }
                if (postProcessor2 != null) {
                    this.mCurrentParams = list;
                    this.mCurrentPostProcessor = postProcessor2;
                    if (this.mCurrentPostProcessor.tryToReuse()) {
                        String access$4003 = LocalParallelService.TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("configCaptureOutputBuffer: success to reuse activeProcessor:");
                        sb3.append(this.mCurrentPostProcessor);
                        Log.d(access$4003, sb3.toString());
                        return new ArrayList(this.mCurrentPostProcessor.getSurfaceList());
                    }
                    Log.w(LocalParallelService.TAG, "configCaptureOutputBuffer: failed to reuse activeProcessor, need to create new one");
                }
                PostProcessor postProcessor4 = this.mCurrentPostProcessor;
                if (postProcessor4 != null && !postProcessor4.isStopping()) {
                    this.mCurrentPostProcessor.destroyWhenTasksFinished(i);
                }
                initCurrentPostProcessor(list);
                this.mCurrentPostProcessor.setToken(i);
                return this.mCurrentPostProcessor.configHALOutputSurface(list);
            }
            throw new RemoteException("List is empty");
        }

        public void configCaptureSession(@NonNull BufferFormat bufferFormat, CameraSize cameraSize) {
            String str;
            String str2;
            if (this.mCurrentPostProcessor == null || bufferFormat.equals(this.mCurrentBufferFormat)) {
                str2 = LocalParallelService.TAG;
                str = "configCaptureSession: bufferFormat keeps unchanged";
            } else {
                this.mCurrentBufferFormat = bufferFormat;
                long currentTimeMillis = System.currentTimeMillis();
                this.mCurrentPostProcessor.configCaptureSession(this.mCurrentBufferFormat, cameraSize);
                str2 = LocalParallelService.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configCaptureSession: cost: ");
                sb.append(System.currentTimeMillis() - currentTimeMillis);
                str = sb.toString();
            }
            Log.d(str2, str);
        }

        public void configMaxParallelRequestNumber(int i) {
            LocalParallelService.this.mMaxParallelRequestNumber = i;
            PostProcessor postProcessor = this.mCurrentPostProcessor;
            if (postProcessor != null) {
                postProcessor.setMaxParallelRequestNumber(LocalParallelService.this.mMaxParallelRequestNumber);
            }
        }

        public void customizeReprocessor(HashMap hashMap) {
            LocalParallelService.getReprocessor().customize(hashMap);
        }

        public int getFrontProcessingCount() {
            PostProcessor postProcessor = this.mCurrentPostProcessor;
            if (postProcessor != null) {
                return postProcessor.getFrontProcessingCount();
            }
            return 0;
        }

        public boolean isAnyRequestBlocked() {
            PostProcessor postProcessor = this.mCurrentPostProcessor;
            if (postProcessor != null) {
                return postProcessor.isAnyRequestBlocked();
            }
            return false;
        }

        public boolean isAnyRequestIsHWMFNRProcessing() {
            PostProcessor postProcessor = this.mCurrentPostProcessor;
            if (postProcessor != null) {
                return postProcessor.isAnyRequestIsHWMFNRProcessing();
            }
            return false;
        }

        public boolean isIdle() {
            synchronized (LocalParallelService.this.mPostProcessorLock) {
                for (PostProcessor isIdle : this.mAlivePostProcessor) {
                    if (!isIdle.isIdle()) {
                        return false;
                    }
                }
                return true;
            }
        }

        public boolean needWaitProcess() {
            PostProcessor postProcessor = this.mCurrentPostProcessor;
            if (postProcessor != null) {
                return postProcessor.needWaitImageClose() || this.mCurrentPostProcessor.needWaitAlgorithmEngine();
            }
            return false;
        }

        public void onCameraClosed() {
            PostProcessor postProcessor = this.mCurrentPostProcessor;
            if (postProcessor != null) {
                postProcessor.clearFrontProcessingTask();
            }
        }

        public void onCaptureCompleted(ICustomCaptureResult iCustomCaptureResult, boolean z) {
            this.mCurrentPostProcessor.getCaptureStatusListener().onCaptureCompleted(iCustomCaptureResult, z);
        }

        public void onCaptureFailed(long j, int i) {
            this.mCurrentPostProcessor.getCaptureStatusListener().onCaptureFailed(j, i);
        }

        public void onCaptureStarted(ParallelTaskData parallelTaskData) {
            this.mCurrentPostProcessor.getCaptureStatusListener().onCaptureStarted(parallelTaskData);
        }

        public void onServiceDestroy() {
            LocalParallelService.getReprocessor().deInit();
            MiCameraAlgo.deInit();
        }

        public void prepareParallelCapture() {
            synchronized (this) {
                if (this.mCurrentPostProcessor != null) {
                    this.mCurrentPostProcessor.configParallelCaptureSession(true);
                }
            }
        }

        public FeatureSetting queryFeatureSetting(@NonNull IspInterfaceIO ispInterfaceIO, @NonNull Parcelable parcelable, @NonNull QueryFeatureSettingParameter queryFeatureSettingParameter, boolean z) {
            return LocalParallelService.getReprocessor().queryFeatureSetting(ispInterfaceIO, parcelable, queryFeatureSettingParameter, z);
        }

        public void setImageSaver(ImageSaver imageSaver) {
            this.mCurrentPostProcessor.setImageSaver(imageSaver);
        }

        public void setOnPictureTakenListener(ServiceStatusListener serviceStatusListener) {
            LocalParallelService.this.mServiceStatusListenerRef = new WeakReference(serviceStatusListener);
        }

        public void setOnSessionStatusCallBackListener(ISessionStatusCallBackListener iSessionStatusCallBackListener) {
            PostProcessor postProcessor = this.mCurrentPostProcessor;
            if (postProcessor != null) {
                postProcessor.setOnSessionStatusCallBackListener(iSessionStatusCallBackListener);
            }
        }

        public void setOutputPictureSpec(int i, int i2, int i3) {
            LocalParallelService.getReprocessor().setOutputPictureSpec(i, i2, i3);
        }

        public void setSRRequireReprocess(boolean z) {
            LocalParallelService.this.mSRRequireReprocess = z;
            PostProcessor postProcessor = this.mCurrentPostProcessor;
            if (postProcessor != null) {
                postProcessor.setSRRequireReprocess(z);
            }
        }

        public void stopPostProcessor(int i) {
            synchronized (this) {
                if (this.mCurrentPostProcessor != null && !this.mCurrentPostProcessor.isStopping()) {
                    String access$400 = LocalParallelService.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("stopPostProcessor: ");
                    sb.append(this.mCurrentPostProcessor);
                    Log.d(access$400, sb.toString());
                    this.mCurrentPostProcessor.destroyWhenTasksFinished(i);
                }
            }
        }

        public void updateVirtualCameraIds() {
            if (C0124O00000oO.O0o0000) {
                LocalParallelService.getReprocessor().setVirtualCameraIds("5", "6");
            } else if (C0124O00000oO.Oo00()) {
                LocalParallelService.getReprocessor().setVirtualCameraIds(String.valueOf(Camera2DataContainer.getInstance().getVirtualBackCameraId()), String.valueOf(Camera2DataContainer.getInstance().getVirtualFrontCameraId()));
            }
        }
    }

    public interface ServiceStatusListener {
        void onImagePostProcessEnd(ParallelTaskData parallelTaskData);

        void onImagePostProcessStart(ParallelTaskData parallelTaskData);
    }

    public static Reprocessor getReprocessor() {
        if (sReprocessorTypeIndex == -1) {
            sReprocessorTypeIndex = C0122O00000o.instance().OOOoo0o();
        }
        return ReprocessorFactory.getReprocessor(ReprocessorType.values()[sReprocessorTypeIndex]);
    }

    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: start");
        return this.mLocalBinder;
    }

    public void onCreate() {
        Log.d(TAG, "onCreate: start");
        if (!C0122O00000o.instance().OOo00O()) {
            Log.d(TAG, "This device does not support Algo up, do nothing.");
            stopSelf();
            return;
        }
        this.mLocalBinder = new LocalBinder();
        super.onCreate();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy: start");
        LocalBinder localBinder = this.mLocalBinder;
        if (localBinder != null) {
            localBinder.onServiceDestroy();
            this.mLocalBinder = null;
        }
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d(TAG, "onStartCommand: start");
        return super.onStartCommand(intent, i, i2);
    }

    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: start");
        return super.onUnbind(intent);
    }
}
