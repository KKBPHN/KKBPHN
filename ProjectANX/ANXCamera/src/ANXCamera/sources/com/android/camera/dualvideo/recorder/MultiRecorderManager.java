package com.android.camera.dualvideo.recorder;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.SparseArray;
import com.android.camera.CameraSize;
import com.android.camera.dualvideo.recorder.MiRecorder.MiRecorderListener;
import com.android.camera.log.Log;
import com.android.camera.storage.ImageSaver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.SingleEmitter;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiRecorderManager {
    private static final int SUCCESS = 1;
    private static final String TAG = "MultiRecorderManager";
    private final ImageSaver mImageSaver;
    private boolean mIsRecording;
    private final Object mLock = new Object();
    private final ArrayList mRecorderList = new ArrayList();
    public int mStatPausedTimes = 0;
    public int mStatResumeTimes = 0;

    public MultiRecorderManager(ImageSaver imageSaver) {
        this.mImageSaver = imageSaver;
    }

    static /* synthetic */ boolean O00000oo(Object obj) {
        return ((Integer) obj).intValue() == 1;
    }

    public /* synthetic */ Observable O000000o(MiRecorder miRecorder) {
        return Observable.create(new O0000O0o(this, miRecorder)).subscribeOn(Schedulers.computation());
    }

    public /* synthetic */ void O000000o(MiRecorder miRecorder, ObservableEmitter observableEmitter) {
        StringBuilder sb = new StringBuilder();
        sb.append("stopRecorder: ");
        sb.append(miRecorder.toString());
        Log.d(TAG, sb.toString());
        miRecorder.stop();
        miRecorder.release();
        miRecorder.save(this.mImageSaver);
        observableEmitter.onNext(Integer.valueOf(1));
    }

    public /* synthetic */ void O000000o(SingleEmitter singleEmitter, long j, Boolean bool) {
        if (bool.booleanValue() && singleEmitter != null && !singleEmitter.isDisposed()) {
            singleEmitter.onSuccess(Boolean.valueOf(true));
        }
        this.mRecorderList.clear();
        StringBuilder sb = new StringBuilder();
        sb.append("stopRecorder: time spent(ms): ");
        sb.append(System.currentTimeMillis() - j);
        Log.d(TAG, sb.toString());
    }

    public long getDuration() {
        return ((MiRecorder) this.mRecorderList.get(0)).getDuration();
    }

    public SparseArray getRecorderSurface() {
        SparseArray sparseArray = new SparseArray();
        this.mRecorderList.forEach(new C0173O00000oo(sparseArray));
        return sparseArray;
    }

    public boolean isRecording() {
        return this.mIsRecording;
    }

    public boolean isRecordingPaused() {
        return this.mIsRecording && this.mRecorderList.stream().anyMatch(C0175O0000Ooo.INSTANCE);
    }

    public synchronized void pauseRecorder() {
        try {
            this.mRecorderList.forEach(C0174O0000OoO.INSTANCE);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        }
        this.mStatPausedTimes++;
    }

    public synchronized void release() {
        Log.v(TAG, "releaseRecorder");
        this.mIsRecording = false;
        this.mRecorderList.forEach(O0000Oo.INSTANCE);
    }

    public synchronized void resume() {
        this.mRecorderList.forEach(O00000Oo.INSTANCE);
        this.mStatResumeTimes++;
    }

    public synchronized void startRecorder(int[] iArr, Location location, CameraSize cameraSize, MiRecorderListener miRecorderListener, long j, int i) {
        int[] iArr2 = iArr;
        synchronized (this) {
            Log.d(TAG, "startRecorder: ");
            long currentTimeMillis = System.currentTimeMillis();
            synchronized (this.mLock) {
                if (this.mRecorderList.isEmpty()) {
                    int length = iArr2.length;
                    int i2 = 0;
                    while (i2 < length) {
                        int i3 = iArr2[i2];
                        MiRecorder miRecorder = r8;
                        ArrayList arrayList = this.mRecorderList;
                        MiRecorder miRecorder2 = new MiRecorder(i3, location, j, i, miRecorderListener, cameraSize);
                        arrayList.add(miRecorder);
                        i2++;
                        iArr2 = iArr;
                    }
                }
            }
            this.mIsRecording = true;
            this.mRecorderList.forEach(O000000o.INSTANCE);
            this.mStatPausedTimes = 0;
            this.mStatResumeTimes = 0;
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startRecorder: time spent(ms): ");
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            Log.d(str, sb.toString());
        }
    }

    @SuppressLint({"CheckResult"})
    public synchronized void stopRecorder(SingleEmitter singleEmitter) {
        if (this.mIsRecording) {
            Log.d(TAG, "stopRecorder: ");
            long currentTimeMillis = System.currentTimeMillis();
            this.mIsRecording = false;
            Observable.zip((Iterable) (List) this.mRecorderList.stream().map(new O00000o(this)).collect(Collectors.toList()), (Function) O00000o0.INSTANCE).subscribe((Consumer) new O0000OOo(this, singleEmitter, currentTimeMillis));
        }
    }
}
