package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.util.ObservableQueueDrain;
import io.reactivex.internal.util.QueueDrainHelper;

public abstract class QueueDrainObserver extends QueueDrainSubscriberPad2 implements Observer, ObservableQueueDrain {
    protected final Observer actual;
    protected volatile boolean cancelled;
    protected volatile boolean done;
    protected Throwable error;
    protected final SimplePlainQueue queue;

    public QueueDrainObserver(Observer observer, SimplePlainQueue simplePlainQueue) {
        this.actual = observer;
        this.queue = simplePlainQueue;
    }

    public void accept(Observer observer, Object obj) {
    }

    public final boolean cancelled() {
        return this.cancelled;
    }

    public final boolean done() {
        return this.done;
    }

    public final boolean enter() {
        return this.wip.getAndIncrement() == 0;
    }

    public final Throwable error() {
        return this.error;
    }

    public final boolean fastEnter() {
        return this.wip.get() == 0 && this.wip.compareAndSet(0, 1);
    }

    /* access modifiers changed from: protected */
    public final void fastPathEmit(Object obj, boolean z, Disposable disposable) {
        Observer observer = this.actual;
        SimplePlainQueue simplePlainQueue = this.queue;
        if (this.wip.get() != 0 || !this.wip.compareAndSet(0, 1)) {
            simplePlainQueue.offer(obj);
            if (!enter()) {
                return;
            }
        } else {
            accept(observer, obj);
            if (leave(-1) == 0) {
                return;
            }
        }
        QueueDrainHelper.drainLoop(simplePlainQueue, observer, z, disposable, this);
    }

    /* access modifiers changed from: protected */
    public final void fastPathOrderedEmit(Object obj, boolean z, Disposable disposable) {
        Observer observer = this.actual;
        SimplePlainQueue simplePlainQueue = this.queue;
        if (this.wip.get() != 0 || !this.wip.compareAndSet(0, 1)) {
            simplePlainQueue.offer(obj);
            if (!enter()) {
                return;
            }
        } else if (simplePlainQueue.isEmpty()) {
            accept(observer, obj);
            if (leave(-1) == 0) {
                return;
            }
        } else {
            simplePlainQueue.offer(obj);
        }
        QueueDrainHelper.drainLoop(simplePlainQueue, observer, z, disposable, this);
    }

    public final int leave(int i) {
        return this.wip.addAndGet(i);
    }
}
