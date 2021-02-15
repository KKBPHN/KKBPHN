package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ObservableTakeLastTimed extends AbstractObservableWithUpstream {
    final int bufferSize;
    final long count;
    final boolean delayError;
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    final class TakeLastTimedObserver extends AtomicBoolean implements Observer, Disposable {
        private static final long serialVersionUID = -5677354903406201275L;
        final Observer actual;
        volatile boolean cancelled;
        final long count;
        Disposable d;
        final boolean delayError;
        Throwable error;
        final SpscLinkedArrayQueue queue;
        final Scheduler scheduler;
        final long time;
        final TimeUnit unit;

        TakeLastTimedObserver(Observer observer, long j, long j2, TimeUnit timeUnit, Scheduler scheduler2, int i, boolean z) {
            this.actual = observer;
            this.count = j;
            this.time = j2;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
            this.queue = new SpscLinkedArrayQueue(i);
            this.delayError = z;
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.d.dispose();
                if (compareAndSet(false, true)) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (compareAndSet(false, true)) {
                Observer observer = this.actual;
                SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
                boolean z = this.delayError;
                while (!this.cancelled) {
                    if (!z) {
                        Throwable th = this.error;
                        if (th != null) {
                            spscLinkedArrayQueue.clear();
                            observer.onError(th);
                            return;
                        }
                    }
                    Object poll = spscLinkedArrayQueue.poll();
                    if (poll == null) {
                        Throwable th2 = this.error;
                        if (th2 != null) {
                            observer.onError(th2);
                        } else {
                            observer.onComplete();
                        }
                        return;
                    }
                    Object poll2 = spscLinkedArrayQueue.poll();
                    if (((Long) poll).longValue() >= this.scheduler.now(this.unit) - this.time) {
                        observer.onNext(poll2);
                    }
                }
                spscLinkedArrayQueue.clear();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onComplete() {
            drain();
        }

        public void onError(Throwable th) {
            this.error = th;
            drain();
        }

        public void onNext(Object obj) {
            SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
            long now = this.scheduler.now(this.unit);
            long j = this.time;
            long j2 = this.count;
            boolean z = j2 == Long.MAX_VALUE;
            spscLinkedArrayQueue.offer(Long.valueOf(now), obj);
            while (!spscLinkedArrayQueue.isEmpty()) {
                if (((Long) spscLinkedArrayQueue.peek()).longValue() <= now - j || (!z && ((long) (spscLinkedArrayQueue.size() >> 1)) > j2)) {
                    spscLinkedArrayQueue.poll();
                    spscLinkedArrayQueue.poll();
                } else {
                    return;
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableTakeLastTimed(ObservableSource observableSource, long j, long j2, TimeUnit timeUnit, Scheduler scheduler2, int i, boolean z) {
        super(observableSource);
        this.count = j;
        this.time = j2;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.bufferSize = i;
        this.delayError = z;
    }

    public void subscribeActual(Observer observer) {
        ObservableSource observableSource = this.source;
        TakeLastTimedObserver takeLastTimedObserver = new TakeLastTimedObserver(observer, this.count, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError);
        observableSource.subscribe(takeLastTimedObserver);
    }
}
