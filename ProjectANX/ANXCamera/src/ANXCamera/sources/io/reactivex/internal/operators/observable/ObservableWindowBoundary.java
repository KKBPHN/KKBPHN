package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.observers.QueueDrainObserver;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subjects.UnicastSubject;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableWindowBoundary extends AbstractObservableWithUpstream {
    final int bufferSize;
    final ObservableSource other;

    final class WindowBoundaryInnerObserver extends DisposableObserver {
        boolean done;
        final WindowBoundaryMainObserver parent;

        WindowBoundaryInnerObserver(WindowBoundaryMainObserver windowBoundaryMainObserver) {
            this.parent = windowBoundaryMainObserver;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.parent.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                this.parent.next();
            }
        }
    }

    final class WindowBoundaryMainObserver extends QueueDrainObserver implements Disposable {
        static final Object NEXT = new Object();
        final AtomicReference boundary = new AtomicReference();
        final int bufferSize;
        final ObservableSource other;
        Disposable s;
        UnicastSubject window;
        final AtomicLong windows = new AtomicLong();

        WindowBoundaryMainObserver(Observer observer, ObservableSource observableSource, int i) {
            super(observer, new MpscLinkedQueue());
            this.other = observableSource;
            this.bufferSize = i;
            this.windows.lazySet(1);
        }

        public void dispose() {
            this.cancelled = true;
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            MpscLinkedQueue mpscLinkedQueue = (MpscLinkedQueue) this.queue;
            Observer observer = this.actual;
            UnicastSubject unicastSubject = this.window;
            int i = 1;
            while (true) {
                boolean z = this.done;
                Object poll = mpscLinkedQueue.poll();
                boolean z2 = poll == null;
                if (z && z2) {
                    DisposableHelper.dispose(this.boundary);
                    Throwable th = this.error;
                    if (th != null) {
                        unicastSubject.onError(th);
                    } else {
                        unicastSubject.onComplete();
                    }
                    return;
                } else if (z2) {
                    i = leave(-i);
                    if (i == 0) {
                        return;
                    }
                } else if (poll == NEXT) {
                    unicastSubject.onComplete();
                    if (this.windows.decrementAndGet() == 0) {
                        DisposableHelper.dispose(this.boundary);
                        return;
                    } else if (!this.cancelled) {
                        unicastSubject = UnicastSubject.create(this.bufferSize);
                        this.windows.getAndIncrement();
                        this.window = unicastSubject;
                        observer.onNext(unicastSubject);
                    }
                } else {
                    NotificationLite.getValue(poll);
                    unicastSubject.onNext(poll);
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            this.queue.offer(NEXT);
            if (enter()) {
                drainLoop();
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                if (enter()) {
                    drainLoop();
                }
                if (this.windows.decrementAndGet() == 0) {
                    DisposableHelper.dispose(this.boundary);
                }
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.error = th;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            if (this.windows.decrementAndGet() == 0) {
                DisposableHelper.dispose(this.boundary);
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (fastEnter()) {
                this.window.onNext(obj);
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                SimplePlainQueue simplePlainQueue = this.queue;
                NotificationLite.next(obj);
                simplePlainQueue.offer(obj);
                if (!enter()) {
                    return;
                }
            }
            drainLoop();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                Observer observer = this.actual;
                observer.onSubscribe(this);
                if (!this.cancelled) {
                    UnicastSubject create = UnicastSubject.create(this.bufferSize);
                    this.window = create;
                    observer.onNext(create);
                    WindowBoundaryInnerObserver windowBoundaryInnerObserver = new WindowBoundaryInnerObserver(this);
                    if (this.boundary.compareAndSet(null, windowBoundaryInnerObserver)) {
                        this.windows.getAndIncrement();
                        this.other.subscribe(windowBoundaryInnerObserver);
                    }
                }
            }
        }
    }

    public ObservableWindowBoundary(ObservableSource observableSource, ObservableSource observableSource2, int i) {
        super(observableSource);
        this.other = observableSource2;
        this.bufferSize = i;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new WindowBoundaryMainObserver(new SerializedObserver(observer), this.other, this.bufferSize));
    }
}
