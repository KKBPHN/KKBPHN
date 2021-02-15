package miuix.animation.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import miuix.animation.Folme;
import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.controller.AnimState;
import miuix.animation.physics.AnimationHandler;
import miuix.animation.physics.AnimationHandler.AnimationFrameCallback;
import miuix.animation.property.FloatProperty;
import miuix.animation.utils.LogUtils;

public class AnimRunner implements AnimationFrameCallback {
    public static final long MAX_DELTA = 16;
    private static final int MAX_RECORD = 5;
    private static final int MSG_START = 0;
    private static final Handler sMainHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            if (message.what == 0) {
                AnimRunner.startAnimRunner();
            } else {
                super.handleMessage(message);
            }
        }
    };
    private volatile long mAverageDelta = 16;
    private long[] mDeltaRecord = {0, 0, 0, 0, 0};
    private boolean mIsRunning;
    private long mLastFrameTime;
    private float mRatio;
    private int mRecordCount = 0;
    /* access modifiers changed from: private */
    public AtomicLong mRunningTime = new AtomicLong();
    private List mTargetList = new ArrayList();

    class Holder {
        static final AnimRunner inst = new AnimRunner();

        private Holder() {
        }
    }

    private long average(long[] jArr) {
        int i = 0;
        long j = 0;
        for (long j2 : jArr) {
            j += j2;
            if (j2 > 0) {
                i++;
            }
        }
        if (i > 0) {
            return j / ((long) i);
        }
        return 0;
    }

    private long calculateAverageDelta(long j) {
        long average = average(this.mDeltaRecord);
        if (average > 0) {
            j = average;
        }
        if (j == 0 || j > 16) {
            j = 16;
        }
        return (long) Math.ceil((double) (((float) j) / this.mRatio));
    }

    private void cleanOneshotTarget(IAnimTarget iAnimTarget) {
        AnimTask animTask = iAnimTarget.getAnimTask();
        if (!iAnimTarget.hasFlags(1)) {
            return;
        }
        if (!animTask.isValid() || animTask.isFinished()) {
            Folme.clean(iAnimTarget);
        }
    }

    private boolean endAnimation() {
        if (isRunning()) {
            return false;
        }
        if (LogUtils.isLogEnabled()) {
            LogUtils.debug("AnimRunner.endAnimation", new Object[0]);
        }
        this.mIsRunning = false;
        this.mRunningTime.set(0);
        this.mLastFrameTime = 0;
        AnimationHandler.getInstance().removeCallback(this);
        return true;
    }

    public static AnimTask getAnimTask(IAnimTarget iAnimTarget, long j, AnimState animState, AnimState animState2, AnimConfigLink animConfigLink) {
        AnimTask animTask = iAnimTarget.getAnimTask();
        animTask.addTransition(j, new TransitionInfo(iAnimTarget, animState, animState2, animConfigLink));
        return animTask;
    }

    public static AnimRunner getInst() {
        return Holder.inst;
    }

    private boolean isAnimRunning(IAnimTarget iAnimTarget) {
        return !iAnimTarget.getAnimTask().isFinished();
    }

    private boolean isRunning() {
        boolean z;
        Iterator it = this.mTargetList.iterator();
        while (true) {
            if (it.hasNext()) {
                if (isAnimRunning((IAnimTarget) it.next())) {
                    z = true;
                    break;
                }
            } else {
                z = false;
                break;
            }
        }
        this.mTargetList.clear();
        return z;
    }

    private void runAnim(AnimTask animTask, long j, long j2, long... jArr) {
        animTask.run(j, j2, jArr);
    }

    /* access modifiers changed from: private */
    public void start() {
        if (!this.mIsRunning) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                startAnimRunner();
            } else {
                sMainHandler.sendEmptyMessage(0);
            }
        }
    }

    /* access modifiers changed from: private */
    public static void startAnimRunner() {
        AnimRunner inst = getInst();
        if (!inst.mIsRunning) {
            if (LogUtils.isLogEnabled()) {
                LogUtils.debug("AnimRunner.start", new Object[0]);
            }
            inst.mRatio = Folme.getTimeRatio();
            inst.mIsRunning = true;
            AnimationHandler.getInstance().addAnimationFrameCallback(inst, 0);
        }
    }

    private long updateRunningTime(long j) {
        long j2 = this.mLastFrameTime;
        long j3 = 0;
        if (j2 != 0) {
            j3 = j - j2;
        }
        this.mLastFrameTime = j;
        int i = this.mRecordCount;
        this.mDeltaRecord[i % 5] = j3;
        this.mRecordCount = i + 1;
        this.mAverageDelta = calculateAverageDelta(j3);
        this.mRunningTime.addAndGet(this.mAverageDelta);
        return this.mAverageDelta;
    }

    public void cancel(final IAnimTarget iAnimTarget, final FloatProperty... floatPropertyArr) {
        iAnimTarget.post(new Runnable() {
            public void run() {
                iAnimTarget.getAnimTask().cancel(floatPropertyArr);
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void doAnimOnTarget(IAnimTarget iAnimTarget, long j, long j2) {
        AnimTask animTask = iAnimTarget.getAnimTask();
        runAnim(animTask, j, j2, new long[0]);
        iAnimTarget.onFrameEnd(animTask.isFinished());
        cleanOneshotTarget(iAnimTarget);
    }

    public boolean doAnimationFrame(long j) {
        long updateRunningTime = updateRunningTime(j);
        long j2 = this.mRunningTime.get();
        Folme.getTargets(this.mTargetList);
        for (IAnimTarget iAnimTarget : this.mTargetList) {
            if (iAnimTarget.allowAnimRun() && iAnimTarget.getAnimTask().isValid()) {
                iAnimTarget.executeAnim(j2, updateRunningTime);
            }
            cleanOneshotTarget(iAnimTarget);
        }
        return endAnimation();
    }

    public void end(final IAnimTarget iAnimTarget, final FloatProperty... floatPropertyArr) {
        iAnimTarget.post(new Runnable() {
            public void run() {
                iAnimTarget.getAnimTask().end(floatPropertyArr);
            }
        });
    }

    public long getAverageDelta() {
        return this.mAverageDelta;
    }

    public long getRunningTime() {
        return this.mRunningTime.get();
    }

    public void run(IAnimTarget iAnimTarget, AnimState animState, AnimState animState2, AnimConfigLink animConfigLink) {
        final IAnimTarget iAnimTarget2 = iAnimTarget;
        final AnimState animState3 = animState;
        final AnimState animState4 = animState2;
        final AnimConfigLink animConfigLink2 = animConfigLink;
        AnonymousClass4 r0 = new Runnable() {
            public void run() {
                AnimRunner.getAnimTask(iAnimTarget2, AnimRunner.this.mRunningTime.get(), animState3, animState4, animConfigLink2);
                AnimRunner.this.start();
            }
        };
        iAnimTarget.executeOnInitialized(r0);
    }
}
