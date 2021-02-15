package com.android.camera;

import android.hardware.camera2.CaptureResult;
import com.android.camera.log.Log;
import com.xiaomi.camera.rx.CameraSchedulers;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreviewMetadata implements IPreviewMetadata {
    private static final String TAG = "PreviewMetadata";
    private List mDisposables = new ArrayList();
    /* access modifiers changed from: private */
    public List mFlowableEmitters = new ArrayList();
    private List mPartialDisposables = new ArrayList();
    /* access modifiers changed from: private */
    public List mPartialFlowableEmitters = new ArrayList();
    private List mPartialPreviewMeatadataList = new ArrayList();
    private List mPreviewMeatadataList = new ArrayList();

    private void initMetadataFlowable() {
        for (IFuncPreviewMetadataListener iFuncPreviewMetadataListener : this.mPreviewMeatadataList) {
            if (iFuncPreviewMetadataListener != null) {
                Flowable observeOn = Flowable.create(new FlowableOnSubscribe() {
                    public void subscribe(FlowableEmitter flowableEmitter) {
                        PreviewMetadata.this.mFlowableEmitters.add(flowableEmitter);
                    }
                }, BackpressureStrategy.DROP).observeOn(CameraSchedulers.sCameraSetupScheduler);
                if (iFuncPreviewMetadataListener.getSamplePeriod() > 0) {
                    observeOn = observeOn.sample(iFuncPreviewMetadataListener.getSamplePeriod(), TimeUnit.MILLISECONDS);
                }
                this.mPartialDisposables.add(observeOn.map(iFuncPreviewMetadataListener).observeOn(AndroidSchedulers.mainThread()).onTerminateDetach().subscribe((Consumer) iFuncPreviewMetadataListener));
            }
        }
        Log.d(TAG, "<preview metadata> init metadata flowable");
    }

    private void initPartialMetadataFlowable() {
        for (IFuncPreviewMetadataListener iFuncPreviewMetadataListener : this.mPartialPreviewMeatadataList) {
            if (iFuncPreviewMetadataListener != null) {
                Flowable observeOn = Flowable.create(new FlowableOnSubscribe() {
                    public void subscribe(FlowableEmitter flowableEmitter) {
                        PreviewMetadata.this.mPartialFlowableEmitters.add(flowableEmitter);
                    }
                }, BackpressureStrategy.DROP).observeOn(CameraSchedulers.sCameraSetupScheduler);
                if (iFuncPreviewMetadataListener.getSamplePeriod() > 0) {
                    observeOn = observeOn.sample(iFuncPreviewMetadataListener.getSamplePeriod(), TimeUnit.MILLISECONDS);
                }
                this.mDisposables.add(observeOn.map(iFuncPreviewMetadataListener).observeOn(AndroidSchedulers.mainThread()).onTerminateDetach().subscribe((Consumer) iFuncPreviewMetadataListener));
            }
        }
        Log.d(TAG, "<preview metadata> init partial metadata flowable");
    }

    public void onPartialPreviewMetadata(CaptureResult captureResult) {
        Object[] array;
        List list = this.mPartialFlowableEmitters;
        if (list != null && !list.isEmpty()) {
            for (Object obj : this.mPartialFlowableEmitters.toArray()) {
                if (obj != null) {
                    FlowableEmitter flowableEmitter = null;
                    if (obj instanceof FlowableEmitter) {
                        flowableEmitter = (FlowableEmitter) obj;
                    }
                    if (flowableEmitter != null) {
                        flowableEmitter.onNext(captureResult);
                    }
                }
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("<preview metadata> update partial preview metadata:");
            sb.append(captureResult.getFrameNumber());
            Log.c(str, sb.toString());
        }
    }

    public void onPreviewMetadata(CaptureResult captureResult) {
        Object[] array;
        List list = this.mFlowableEmitters;
        if (list != null && !list.isEmpty()) {
            for (Object obj : this.mFlowableEmitters.toArray()) {
                if (obj != null) {
                    FlowableEmitter flowableEmitter = null;
                    if (obj instanceof FlowableEmitter) {
                        flowableEmitter = (FlowableEmitter) obj;
                    }
                    if (flowableEmitter != null) {
                        flowableEmitter.onNext(captureResult);
                    }
                }
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("<preview metadata> update preview metadata:");
            sb.append(captureResult.getFrameNumber());
            Log.c(str, sb.toString());
        }
    }

    public void registerPreviewMeatedata(List list) {
        Log.d(TAG, "<preview metadata> register metadata listener");
        List list2 = this.mPreviewMeatadataList;
        if (list2 != null) {
            list2.addAll(list);
        }
        initMetadataFlowable();
    }

    public void registerPreviewPartialMetadata(List list) {
        Log.d(TAG, "<preview metadata> register partial metadata listener");
        List list2 = this.mPartialPreviewMeatadataList;
        if (list2 != null) {
            list2.addAll(list);
        }
        initPartialMetadataFlowable();
    }

    public void unregisterPreviewMetadata() {
        List list = this.mPreviewMeatadataList;
        if (list != null && !list.isEmpty()) {
            this.mPreviewMeatadataList.clear();
        }
        List list2 = this.mPartialPreviewMeatadataList;
        if (list2 != null && !list2.isEmpty()) {
            this.mPartialPreviewMeatadataList.clear();
        }
        List list3 = this.mPartialFlowableEmitters;
        if (list3 != null && !list3.isEmpty()) {
            this.mPartialFlowableEmitters.clear();
        }
        List list4 = this.mPartialDisposables;
        if (list4 != null && !list4.isEmpty()) {
            this.mPartialDisposables.clear();
        }
        List list5 = this.mFlowableEmitters;
        if (list5 != null && !list5.isEmpty()) {
            this.mFlowableEmitters.clear();
        }
        List list6 = this.mDisposables;
        if (list6 != null && !list6.isEmpty()) {
            this.mDisposables.clear();
        }
        Log.d(TAG, "<preview metadata> unregister all");
    }
}
