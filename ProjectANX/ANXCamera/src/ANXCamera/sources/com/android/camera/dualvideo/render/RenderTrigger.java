package com.android.camera.dualvideo.render;

import android.opengl.GLSurfaceView;
import com.android.camera.log.Log;
import com.xiaomi.camera.rx.CameraSchedulers;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.Timer;
import java.util.TimerTask;

public class RenderTrigger {
    private static final boolean DEBUG = false;
    private static final String TAG = "RenderTrigger";
    private static final int TRIGGER_DIRECT = 4;
    private static final int TRIGGER_MAIN = 1;
    private static final int TRIGGER_SUB = 2;
    private static final int TRIGGER_TIMER = 3;
    private Disposable mDisposable;
    /* access modifiers changed from: private */
    public ObservableEmitter mEmitter;
    private long mFrameInterval;
    private GLSurfaceView mGLView;
    /* access modifiers changed from: private */
    public final Object mLocker = new Object();
    private boolean mMainFrameReady;
    private boolean mSubFrameReady;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private long mWaitAnother;

    public RenderTrigger(GLSurfaceView gLSurfaceView, int i) {
        this.mFrameInterval = (long) (1000 / i);
        this.mWaitAnother = this.mFrameInterval / 2;
        this.mGLView = gLSurfaceView;
        this.mTimer = new Timer();
        this.mDisposable = Observable.create(new C0223O000oOOo(this)).observeOn(CameraSchedulers.sCameraSetupScheduler).subscribe((Consumer) new C0222O000oOOO(this));
    }

    private void delayTrigger(long j) {
        TimerTask timerTask = this.mTimerTask;
        if (timerTask != null) {
            timerTask.cancel();
            this.mTimerTask = null;
        }
        this.mTimerTask = new TimerTask() {
            public void run() {
                if (RenderTrigger.this.mEmitter != null && !RenderTrigger.this.mEmitter.isDisposed()) {
                    synchronized (RenderTrigger.this.mLocker) {
                        RenderTrigger.this.mEmitter.onNext(Integer.valueOf(3));
                    }
                }
            }
        };
        this.mTimer.schedule(this.mTimerTask, j);
    }

    /* access modifiers changed from: private */
    public void process(Integer num) {
        int intValue = num.intValue();
        if (intValue == 1) {
            processMain();
        } else if (intValue == 2) {
            processSub();
        } else if (intValue == 3 || intValue == 4) {
            requestRender();
        }
    }

    private void processMain() {
        if (this.mMainFrameReady || this.mSubFrameReady) {
            this.mMainFrameReady = true;
            requestRender();
            return;
        }
        this.mMainFrameReady = true;
        delayTrigger(this.mWaitAnother);
    }

    private void processSub() {
        boolean z = this.mMainFrameReady;
        this.mSubFrameReady = true;
        if (z) {
            requestRender();
        }
    }

    private void requestRender() {
        if (this.mMainFrameReady) {
            this.mMainFrameReady = false;
            this.mSubFrameReady = false;
            this.mGLView.requestRender();
        }
        delayTrigger(this.mFrameInterval);
    }

    public /* synthetic */ void O00000o(ObservableEmitter observableEmitter) {
        this.mEmitter = observableEmitter;
    }

    public void forceRender() {
        this.mEmitter.onNext(Integer.valueOf(4));
    }

    public void mainFrameAvailable() {
        ObservableEmitter observableEmitter = this.mEmitter;
        if (observableEmitter != null && !observableEmitter.isDisposed()) {
            synchronized (this.mLocker) {
                this.mEmitter.onNext(Integer.valueOf(1));
            }
        }
    }

    public void release() {
        Log.d(TAG, "release: ");
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer = null;
        }
        Disposable disposable = this.mDisposable;
        if (disposable != null) {
            disposable.dispose();
        }
        this.mGLView = null;
    }

    public void subFrameAvailable() {
        ObservableEmitter observableEmitter = this.mEmitter;
        if (observableEmitter != null && !observableEmitter.isDisposed()) {
            synchronized (this.mLocker) {
                this.mEmitter.onNext(Integer.valueOf(2));
            }
        }
    }
}
