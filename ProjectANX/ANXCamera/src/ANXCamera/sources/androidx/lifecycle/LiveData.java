package androidx.lifecycle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.Lifecycle.State;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class LiveData {
    static final Object NOT_SET = new Object();
    static final int START_VERSION = -1;
    int mActiveCount;
    private volatile Object mData;
    final Object mDataLock;
    private boolean mDispatchInvalidated;
    private boolean mDispatchingValue;
    private SafeIterableMap mObservers;
    volatile Object mPendingData;
    private final Runnable mPostValueRunnable;
    private int mVersion;

    class AlwaysActiveObserver extends ObserverWrapper {
        AlwaysActiveObserver(Observer observer) {
            super(observer);
        }

        /* access modifiers changed from: 0000 */
        public boolean shouldBeActive() {
            return true;
        }
    }

    class LifecycleBoundObserver extends ObserverWrapper implements LifecycleEventObserver {
        @NonNull
        final LifecycleOwner mOwner;

        LifecycleBoundObserver(@NonNull LifecycleOwner lifecycleOwner, Observer observer) {
            super(observer);
            this.mOwner = lifecycleOwner;
        }

        /* access modifiers changed from: 0000 */
        public void detachObserver() {
            this.mOwner.getLifecycle().removeObserver(this);
        }

        /* access modifiers changed from: 0000 */
        public boolean isAttachedTo(LifecycleOwner lifecycleOwner) {
            return this.mOwner == lifecycleOwner;
        }

        public void onStateChanged(LifecycleOwner lifecycleOwner, Event event) {
            if (this.mOwner.getLifecycle().getCurrentState() == State.DESTROYED) {
                LiveData.this.removeObserver(this.mObserver);
            } else {
                activeStateChanged(shouldBeActive());
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean shouldBeActive() {
            return this.mOwner.getLifecycle().getCurrentState().isAtLeast(State.STARTED);
        }
    }

    abstract class ObserverWrapper {
        boolean mActive;
        int mLastVersion = -1;
        final Observer mObserver;

        ObserverWrapper(Observer observer) {
            this.mObserver = observer;
        }

        /* access modifiers changed from: 0000 */
        public void activeStateChanged(boolean z) {
            if (z != this.mActive) {
                this.mActive = z;
                int i = 1;
                boolean z2 = LiveData.this.mActiveCount == 0;
                LiveData liveData = LiveData.this;
                int i2 = liveData.mActiveCount;
                if (!this.mActive) {
                    i = -1;
                }
                liveData.mActiveCount = i2 + i;
                if (z2 && this.mActive) {
                    LiveData.this.onActive();
                }
                LiveData liveData2 = LiveData.this;
                if (liveData2.mActiveCount == 0 && !this.mActive) {
                    liveData2.onInactive();
                }
                if (this.mActive) {
                    LiveData.this.dispatchingValue(this);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void detachObserver() {
        }

        /* access modifiers changed from: 0000 */
        public boolean isAttachedTo(LifecycleOwner lifecycleOwner) {
            return false;
        }

        public abstract boolean shouldBeActive();
    }

    public LiveData() {
        this.mDataLock = new Object();
        this.mObservers = new SafeIterableMap();
        this.mActiveCount = 0;
        this.mPendingData = NOT_SET;
        this.mPostValueRunnable = new Runnable() {
            public void run() {
                Object obj;
                synchronized (LiveData.this.mDataLock) {
                    obj = LiveData.this.mPendingData;
                    LiveData.this.mPendingData = LiveData.NOT_SET;
                }
                LiveData.this.setValue(obj);
            }
        };
        this.mData = NOT_SET;
        this.mVersion = -1;
    }

    public LiveData(Object obj) {
        this.mDataLock = new Object();
        this.mObservers = new SafeIterableMap();
        this.mActiveCount = 0;
        this.mPendingData = NOT_SET;
        this.mPostValueRunnable = new Runnable() {
            public void run() {
                Object obj;
                synchronized (LiveData.this.mDataLock) {
                    obj = LiveData.this.mPendingData;
                    LiveData.this.mPendingData = LiveData.NOT_SET;
                }
                LiveData.this.setValue(obj);
            }
        };
        this.mData = obj;
        this.mVersion = 0;
    }

    static void assertMainThread(String str) {
        if (!ArchTaskExecutor.getInstance().isMainThread()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot invoke ");
            sb.append(str);
            sb.append(" on a background thread");
            throw new IllegalStateException(sb.toString());
        }
    }

    private void considerNotify(ObserverWrapper observerWrapper) {
        if (observerWrapper.mActive) {
            if (!observerWrapper.shouldBeActive()) {
                observerWrapper.activeStateChanged(false);
                return;
            }
            int i = observerWrapper.mLastVersion;
            int i2 = this.mVersion;
            if (i < i2) {
                observerWrapper.mLastVersion = i2;
                observerWrapper.mObserver.onChanged(this.mData);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchingValue(@Nullable ObserverWrapper observerWrapper) {
        if (this.mDispatchingValue) {
            this.mDispatchInvalidated = true;
            return;
        }
        this.mDispatchingValue = true;
        do {
            this.mDispatchInvalidated = false;
            if (observerWrapper == null) {
                IteratorWithAdditions iteratorWithAdditions = this.mObservers.iteratorWithAdditions();
                while (iteratorWithAdditions.hasNext()) {
                    considerNotify((ObserverWrapper) ((Entry) iteratorWithAdditions.next()).getValue());
                    if (this.mDispatchInvalidated) {
                        break;
                    }
                }
            } else {
                considerNotify(observerWrapper);
                observerWrapper = null;
            }
        } while (this.mDispatchInvalidated);
        this.mDispatchingValue = false;
    }

    @Nullable
    public Object getValue() {
        Object obj = this.mData;
        if (obj != NOT_SET) {
            return obj;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public int getVersion() {
        return this.mVersion;
    }

    public boolean hasActiveObservers() {
        return this.mActiveCount > 0;
    }

    public boolean hasObservers() {
        return this.mObservers.size() > 0;
    }

    @MainThread
    public void observe(@NonNull LifecycleOwner lifecycleOwner, @NonNull Observer observer) {
        assertMainThread("observe");
        if (lifecycleOwner.getLifecycle().getCurrentState() != State.DESTROYED) {
            LifecycleBoundObserver lifecycleBoundObserver = new LifecycleBoundObserver(lifecycleOwner, observer);
            ObserverWrapper observerWrapper = (ObserverWrapper) this.mObservers.putIfAbsent(observer, lifecycleBoundObserver);
            if (observerWrapper != null && !observerWrapper.isAttachedTo(lifecycleOwner)) {
                throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
            } else if (observerWrapper == null) {
                lifecycleOwner.getLifecycle().addObserver(lifecycleBoundObserver);
            }
        }
    }

    @MainThread
    public void observeForever(@NonNull Observer observer) {
        assertMainThread("observeForever");
        AlwaysActiveObserver alwaysActiveObserver = new AlwaysActiveObserver(observer);
        ObserverWrapper observerWrapper = (ObserverWrapper) this.mObservers.putIfAbsent(observer, alwaysActiveObserver);
        if (observerWrapper instanceof LifecycleBoundObserver) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        } else if (observerWrapper == null) {
            alwaysActiveObserver.activeStateChanged(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onActive() {
    }

    /* access modifiers changed from: protected */
    public void onInactive() {
    }

    /* access modifiers changed from: protected */
    public void postValue(Object obj) {
        boolean z;
        synchronized (this.mDataLock) {
            z = this.mPendingData == NOT_SET;
            this.mPendingData = obj;
        }
        if (z) {
            ArchTaskExecutor.getInstance().postToMainThread(this.mPostValueRunnable);
        }
    }

    @MainThread
    public void removeObserver(@NonNull Observer observer) {
        assertMainThread("removeObserver");
        ObserverWrapper observerWrapper = (ObserverWrapper) this.mObservers.remove(observer);
        if (observerWrapper != null) {
            observerWrapper.detachObserver();
            observerWrapper.activeStateChanged(false);
        }
    }

    @MainThread
    public void removeObservers(@NonNull LifecycleOwner lifecycleOwner) {
        assertMainThread("removeObservers");
        Iterator it = this.mObservers.iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (((ObserverWrapper) entry.getValue()).isAttachedTo(lifecycleOwner)) {
                removeObserver((Observer) entry.getKey());
            }
        }
    }

    /* access modifiers changed from: protected */
    @MainThread
    public void setValue(Object obj) {
        assertMainThread("setValue");
        this.mVersion++;
        this.mData = obj;
        dispatchingValue(null);
    }
}
