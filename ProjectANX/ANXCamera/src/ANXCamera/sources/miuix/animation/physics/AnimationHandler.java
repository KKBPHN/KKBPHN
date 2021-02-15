package miuix.animation.physics;

import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import java.util.ArrayList;

public class AnimationHandler {
    private static final long FRAME_DELAY_MS = 10;
    public static final ThreadLocal sAnimatorHandler = new ThreadLocal();
    /* access modifiers changed from: private */
    public final ArrayList mAnimationCallbacks = new ArrayList();
    private final AnimationCallbackDispatcher mCallbackDispatcher = new AnimationCallbackDispatcher();
    /* access modifiers changed from: private */
    public long mCurrentFrameTime = 0;
    private final ArrayMap mDelayedCallbackStartTime = new ArrayMap();
    private boolean mListDirty = false;
    private AnimationFrameCallbackProvider mProvider;

    class AnimationCallbackDispatcher {
        AnimationCallbackDispatcher() {
        }

        /* access modifiers changed from: 0000 */
        public void dispatchAnimationFrame() {
            AnimationHandler.this.mCurrentFrameTime = SystemClock.uptimeMillis();
            AnimationHandler animationHandler = AnimationHandler.this;
            animationHandler.doAnimationFrame(animationHandler.mCurrentFrameTime);
            if (AnimationHandler.this.mAnimationCallbacks.size() > 0) {
                AnimationHandler.this.getProvider().postFrameCallback();
            }
        }
    }

    public interface AnimationFrameCallback {
        boolean doAnimationFrame(long j);
    }

    abstract class AnimationFrameCallbackProvider {
        final AnimationCallbackDispatcher mDispatcher;

        AnimationFrameCallbackProvider(AnimationCallbackDispatcher animationCallbackDispatcher) {
            this.mDispatcher = animationCallbackDispatcher;
        }

        public abstract void postFrameCallback();
    }

    class FrameCallbackProvider14 extends AnimationFrameCallbackProvider {
        private final Handler mHandler = new Handler(Looper.myLooper());
        /* access modifiers changed from: private */
        public long mLastFrameTime = -1;
        private final Runnable mRunnable = new Runnable() {
            public void run() {
                FrameCallbackProvider14.this.mLastFrameTime = SystemClock.uptimeMillis();
                FrameCallbackProvider14.this.mDispatcher.dispatchAnimationFrame();
            }
        };

        FrameCallbackProvider14(AnimationCallbackDispatcher animationCallbackDispatcher) {
            super(animationCallbackDispatcher);
        }

        /* access modifiers changed from: 0000 */
        public void postFrameCallback() {
            this.mHandler.postDelayed(this.mRunnable, Math.max(AnimationHandler.FRAME_DELAY_MS - (SystemClock.uptimeMillis() - this.mLastFrameTime), 0));
        }
    }

    class FrameCallbackProvider16 extends AnimationFrameCallbackProvider {
        private final Choreographer mChoreographer = Choreographer.getInstance();
        private final FrameCallback mChoreographerCallback = new FrameCallback() {
            public void doFrame(long j) {
                FrameCallbackProvider16.this.mDispatcher.dispatchAnimationFrame();
            }
        };

        FrameCallbackProvider16(AnimationCallbackDispatcher animationCallbackDispatcher) {
            super(animationCallbackDispatcher);
        }

        /* access modifiers changed from: 0000 */
        public void postFrameCallback() {
            this.mChoreographer.postFrameCallback(this.mChoreographerCallback);
        }
    }

    private void cleanUpList() {
        if (this.mListDirty) {
            for (int size = this.mAnimationCallbacks.size() - 1; size >= 0; size--) {
                if (this.mAnimationCallbacks.get(size) == null) {
                    this.mAnimationCallbacks.remove(size);
                }
            }
            this.mListDirty = false;
        }
    }

    /* access modifiers changed from: private */
    public void doAnimationFrame(long j) {
        long uptimeMillis = SystemClock.uptimeMillis();
        for (int i = 0; i < this.mAnimationCallbacks.size(); i++) {
            AnimationFrameCallback animationFrameCallback = (AnimationFrameCallback) this.mAnimationCallbacks.get(i);
            if (animationFrameCallback != null && isCallbackDue(animationFrameCallback, uptimeMillis)) {
                animationFrameCallback.doAnimationFrame(j);
            }
        }
        cleanUpList();
    }

    public static long getFrameTime() {
        if (sAnimatorHandler.get() == null) {
            return 0;
        }
        return ((AnimationHandler) sAnimatorHandler.get()).mCurrentFrameTime;
    }

    public static AnimationHandler getInstance() {
        if (sAnimatorHandler.get() == null) {
            sAnimatorHandler.set(new AnimationHandler());
        }
        return (AnimationHandler) sAnimatorHandler.get();
    }

    /* access modifiers changed from: private */
    public AnimationFrameCallbackProvider getProvider() {
        if (this.mProvider == null) {
            this.mProvider = VERSION.SDK_INT >= 16 ? new FrameCallbackProvider16(this.mCallbackDispatcher) : new FrameCallbackProvider14(this.mCallbackDispatcher);
        }
        return this.mProvider;
    }

    private boolean isCallbackDue(AnimationFrameCallback animationFrameCallback, long j) {
        Long l = (Long) this.mDelayedCallbackStartTime.get(animationFrameCallback);
        if (l == null) {
            return true;
        }
        if (l.longValue() >= j) {
            return false;
        }
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        return true;
    }

    public void addAnimationFrameCallback(AnimationFrameCallback animationFrameCallback, long j) {
        if (this.mAnimationCallbacks.size() == 0) {
            getProvider().postFrameCallback();
        }
        if (!this.mAnimationCallbacks.contains(animationFrameCallback)) {
            this.mAnimationCallbacks.add(animationFrameCallback);
        }
        if (j > 0) {
            this.mDelayedCallbackStartTime.put(animationFrameCallback, Long.valueOf(SystemClock.uptimeMillis() + j));
        }
    }

    public void removeCallback(AnimationFrameCallback animationFrameCallback) {
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        int indexOf = this.mAnimationCallbacks.indexOf(animationFrameCallback);
        if (indexOf >= 0) {
            this.mAnimationCallbacks.set(indexOf, null);
            this.mListDirty = true;
        }
    }

    public void setProvider(AnimationFrameCallbackProvider animationFrameCallbackProvider) {
        this.mProvider = animationFrameCallbackProvider;
    }
}
