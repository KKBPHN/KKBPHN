package com.android.camera.timerburst;

import com.android.camera.log.Log;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;

public class CameraTimer {
    private static final String TAG = "CameraTimer";
    private long mConsumedTakeTimes;
    /* access modifiers changed from: private */
    public Disposable mCountdownDisposable;
    private int mInitialCount;
    private int mInitialRepeatTimes;
    /* access modifiers changed from: private */
    public Observer mOutdoorObserver;
    private long mTotalTakeTimes;

    static /* synthetic */ long access$314(CameraTimer cameraTimer, long j) {
        long j2 = cameraTimer.mConsumedTakeTimes + j;
        cameraTimer.mConsumedTakeTimes = j2;
        return j2;
    }

    /* access modifiers changed from: private */
    public void notifyNumber() {
        long j = this.mConsumedTakeTimes;
        int i = this.mInitialCount;
        long j2 = j % ((long) i);
        long j3 = ((long) i) - (1 + j2);
        StringBuilder sb = new StringBuilder();
        sb.append("notifyNumber: ");
        sb.append(this.mConsumedTakeTimes);
        String str = " | ";
        sb.append(str);
        sb.append(j2);
        sb.append(str);
        sb.append(j3);
        Log.e(TAG, sb.toString());
        this.mOutdoorObserver.onNext(Long.valueOf(j3));
    }

    public void dispose() {
        Disposable disposable = this.mCountdownDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mCountdownDisposable.dispose();
            this.mCountdownDisposable = null;
        }
    }

    public boolean isRunning() {
        Disposable disposable = this.mCountdownDisposable;
        return disposable != null && !disposable.isDisposed();
    }

    public void pause() {
        Log.d(TAG, BaseEvent.PAUSE);
        dispose();
    }

    public void resume() {
        Log.d(TAG, BaseEvent.RESUME);
        long j = (this.mTotalTakeTimes - this.mConsumedTakeTimes) - 1;
        if (j > 0) {
            Observable.interval(1, TimeUnit.SECONDS).take(j).observeOn(AndroidSchedulers.mainThread()).subscribe((Observer) new Observer() {
                public void onComplete() {
                    CameraTimer.this.mOutdoorObserver.onComplete();
                }

                public void onError(Throwable th) {
                    CameraTimer.this.mOutdoorObserver.onError(th);
                }

                public void onNext(Long l) {
                    CameraTimer.access$314(CameraTimer.this, 1);
                    CameraTimer.this.notifyNumber();
                }

                public void onSubscribe(Disposable disposable) {
                    CameraTimer.this.mCountdownDisposable = disposable;
                    CameraTimer.this.mOutdoorObserver.onSubscribe(disposable);
                    Log.d(CameraTimer.TAG, "onSubscribe");
                    CameraTimer.this.notifyNumber();
                }
            });
        }
    }

    public CameraTimer setCount(int i) {
        this.mInitialCount = i;
        return this;
    }

    public CameraTimer setRepeatTimes(int i) {
        this.mInitialRepeatTimes = i;
        return this;
    }

    public void start(Observer observer) {
        this.mOutdoorObserver = observer;
        this.mInitialCount++;
        this.mTotalTakeTimes = (long) (this.mInitialCount * this.mInitialRepeatTimes);
        this.mConsumedTakeTimes = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("start: ");
        sb.append(this.mInitialCount);
        sb.append(" | ");
        sb.append(this.mInitialRepeatTimes);
        Log.d(TAG, sb.toString());
        resume();
    }
}
