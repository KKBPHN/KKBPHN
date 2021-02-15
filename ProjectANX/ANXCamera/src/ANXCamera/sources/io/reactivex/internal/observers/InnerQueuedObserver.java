package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.util.QueueDrainHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class InnerQueuedObserver extends AtomicReference implements Observer, Disposable {
    private static final long serialVersionUID = -5417183359794346637L;
    volatile boolean done;
    int fusionMode;
    final InnerQueuedObserverSupport parent;
    final int prefetch;
    SimpleQueue queue;

    public InnerQueuedObserver(InnerQueuedObserverSupport innerQueuedObserverSupport, int i) {
        this.parent = innerQueuedObserverSupport;
        this.prefetch = i;
    }

    public void dispose() {
        DisposableHelper.dispose(this);
    }

    public int fusionMode() {
        return this.fusionMode;
    }

    public boolean isDisposed() {
        return DisposableHelper.isDisposed((Disposable) get());
    }

    public boolean isDone() {
        return this.done;
    }

    public void onComplete() {
        this.parent.innerComplete(this);
    }

    public void onError(Throwable th) {
        this.parent.innerError(this, th);
    }

    public void onNext(Object obj) {
        if (this.fusionMode == 0) {
            this.parent.innerNext(this, obj);
        } else {
            this.parent.drain();
        }
    }

    public void onSubscribe(Disposable disposable) {
        if (DisposableHelper.setOnce(this, disposable)) {
            if (disposable instanceof QueueDisposable) {
                QueueDisposable queueDisposable = (QueueDisposable) disposable;
                int requestFusion = queueDisposable.requestFusion(3);
                if (requestFusion == 1) {
                    this.fusionMode = requestFusion;
                    this.queue = queueDisposable;
                    this.done = true;
                    this.parent.innerComplete(this);
                    return;
                } else if (requestFusion == 2) {
                    this.fusionMode = requestFusion;
                    this.queue = queueDisposable;
                    return;
                }
            }
            this.queue = QueueDrainHelper.createQueue(-this.prefetch);
        }
    }

    public SimpleQueue queue() {
        return this.queue;
    }

    public void setDone() {
        this.done = true;
    }
}
