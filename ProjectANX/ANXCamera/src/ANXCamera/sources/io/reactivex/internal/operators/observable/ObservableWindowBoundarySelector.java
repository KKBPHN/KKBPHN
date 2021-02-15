package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.observers.QueueDrainObserver;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subjects.UnicastSubject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableWindowBoundarySelector extends AbstractObservableWithUpstream {
    final int bufferSize;
    final Function close;
    final ObservableSource open;

    final class OperatorWindowBoundaryCloseObserver extends DisposableObserver {
        boolean done;
        final WindowBoundaryMainObserver parent;
        final UnicastSubject w;

        OperatorWindowBoundaryCloseObserver(WindowBoundaryMainObserver windowBoundaryMainObserver, UnicastSubject unicastSubject) {
            this.parent = windowBoundaryMainObserver;
            this.w = unicastSubject;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.close(this);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.parent.error(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                this.done = true;
                dispose();
                this.parent.close(this);
            }
        }
    }

    final class OperatorWindowBoundaryOpenObserver extends DisposableObserver {
        final WindowBoundaryMainObserver parent;

        OperatorWindowBoundaryOpenObserver(WindowBoundaryMainObserver windowBoundaryMainObserver) {
            this.parent = windowBoundaryMainObserver;
        }

        public void onComplete() {
            this.parent.onComplete();
        }

        public void onError(Throwable th) {
            this.parent.error(th);
        }

        public void onNext(Object obj) {
            this.parent.open(obj);
        }
    }

    final class WindowBoundaryMainObserver extends QueueDrainObserver implements Disposable {
        final AtomicReference boundary = new AtomicReference();
        final int bufferSize;
        final Function close;
        final ObservableSource open;
        final CompositeDisposable resources;
        Disposable s;
        final AtomicLong windows = new AtomicLong();
        final List ws;

        WindowBoundaryMainObserver(Observer observer, ObservableSource observableSource, Function function, int i) {
            super(observer, new MpscLinkedQueue());
            this.open = observableSource;
            this.close = function;
            this.bufferSize = i;
            this.resources = new CompositeDisposable();
            this.ws = new ArrayList();
            this.windows.lazySet(1);
        }

        public void accept(Observer observer, Object obj) {
        }

        /* access modifiers changed from: 0000 */
        public void close(OperatorWindowBoundaryCloseObserver operatorWindowBoundaryCloseObserver) {
            this.resources.delete(operatorWindowBoundaryCloseObserver);
            this.queue.offer(new WindowOperation(operatorWindowBoundaryCloseObserver.w, null));
            if (enter()) {
                drainLoop();
            }
        }

        public void dispose() {
            this.cancelled = true;
        }

        /* access modifiers changed from: 0000 */
        public void disposeBoundary() {
            this.resources.dispose();
            DisposableHelper.dispose(this.boundary);
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            MpscLinkedQueue mpscLinkedQueue = (MpscLinkedQueue) this.queue;
            Observer observer = this.actual;
            List<UnicastSubject> list = this.ws;
            int i = 1;
            while (true) {
                boolean z = this.done;
                Object poll = mpscLinkedQueue.poll();
                boolean z2 = poll == null;
                if (z && z2) {
                    disposeBoundary();
                    Throwable th = this.error;
                    if (th != null) {
                        for (UnicastSubject onError : list) {
                            onError.onError(th);
                        }
                    } else {
                        for (UnicastSubject onComplete : list) {
                            onComplete.onComplete();
                        }
                    }
                    list.clear();
                    return;
                } else if (z2) {
                    i = leave(-i);
                    if (i == 0) {
                        return;
                    }
                } else if (poll instanceof WindowOperation) {
                    WindowOperation windowOperation = (WindowOperation) poll;
                    UnicastSubject unicastSubject = windowOperation.w;
                    if (unicastSubject != null) {
                        if (list.remove(unicastSubject)) {
                            windowOperation.w.onComplete();
                            if (this.windows.decrementAndGet() == 0) {
                                disposeBoundary();
                                return;
                            }
                        } else {
                            continue;
                        }
                    } else if (!this.cancelled) {
                        UnicastSubject create = UnicastSubject.create(this.bufferSize);
                        list.add(create);
                        observer.onNext(create);
                        try {
                            Object apply = this.close.apply(windowOperation.open);
                            ObjectHelper.requireNonNull(apply, "The ObservableSource supplied is null");
                            ObservableSource observableSource = (ObservableSource) apply;
                            OperatorWindowBoundaryCloseObserver operatorWindowBoundaryCloseObserver = new OperatorWindowBoundaryCloseObserver(this, create);
                            if (this.resources.add(operatorWindowBoundaryCloseObserver)) {
                                this.windows.getAndIncrement();
                                observableSource.subscribe(operatorWindowBoundaryCloseObserver);
                            }
                        } catch (Throwable th2) {
                            Exceptions.throwIfFatal(th2);
                            this.cancelled = true;
                            observer.onError(th2);
                        }
                    }
                } else {
                    for (UnicastSubject unicastSubject2 : list) {
                        NotificationLite.getValue(poll);
                        unicastSubject2.onNext(poll);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void error(Throwable th) {
            this.s.dispose();
            this.resources.dispose();
            onError(th);
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                if (enter()) {
                    drainLoop();
                }
                if (this.windows.decrementAndGet() == 0) {
                    this.resources.dispose();
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
                this.resources.dispose();
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (fastEnter()) {
                for (UnicastSubject onNext : this.ws) {
                    onNext.onNext(obj);
                }
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
                this.actual.onSubscribe(this);
                if (!this.cancelled) {
                    OperatorWindowBoundaryOpenObserver operatorWindowBoundaryOpenObserver = new OperatorWindowBoundaryOpenObserver(this);
                    if (this.boundary.compareAndSet(null, operatorWindowBoundaryOpenObserver)) {
                        this.windows.getAndIncrement();
                        this.open.subscribe(operatorWindowBoundaryOpenObserver);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void open(Object obj) {
            this.queue.offer(new WindowOperation(null, obj));
            if (enter()) {
                drainLoop();
            }
        }
    }

    final class WindowOperation {
        final Object open;
        final UnicastSubject w;

        WindowOperation(UnicastSubject unicastSubject, Object obj) {
            this.w = unicastSubject;
            this.open = obj;
        }
    }

    public ObservableWindowBoundarySelector(ObservableSource observableSource, ObservableSource observableSource2, Function function, int i) {
        super(observableSource);
        this.open = observableSource2;
        this.close = function;
        this.bufferSize = i;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new WindowBoundaryMainObserver(new SerializedObserver(observer), this.open, this.close, this.bufferSize));
    }
}
