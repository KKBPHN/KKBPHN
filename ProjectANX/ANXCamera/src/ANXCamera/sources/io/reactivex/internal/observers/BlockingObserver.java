package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.NotificationLite;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public final class BlockingObserver extends AtomicReference implements Observer, Disposable {
    public static final Object TERMINATED = new Object();
    private static final long serialVersionUID = -4875965440900746268L;
    final Queue queue;

    public BlockingObserver(Queue queue2) {
        this.queue = queue2;
    }

    public void dispose() {
        if (DisposableHelper.dispose(this)) {
            this.queue.offer(TERMINATED);
        }
    }

    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }

    public void onComplete() {
        this.queue.offer(NotificationLite.complete());
    }

    public void onError(Throwable th) {
        this.queue.offer(NotificationLite.error(th));
    }

    public void onNext(Object obj) {
        Queue queue2 = this.queue;
        NotificationLite.next(obj);
        queue2.offer(obj);
    }

    public void onSubscribe(Disposable disposable) {
        DisposableHelper.setOnce(this, disposable);
    }
}
