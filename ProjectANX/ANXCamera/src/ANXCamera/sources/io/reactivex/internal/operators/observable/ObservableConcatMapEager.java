package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.observers.InnerQueuedObserver;
import io.reactivex.internal.observers.InnerQueuedObserverSupport;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableConcatMapEager extends AbstractObservableWithUpstream {
    final ErrorMode errorMode;
    final Function mapper;
    final int maxConcurrency;
    final int prefetch;

    final class ConcatMapEagerMainObserver extends AtomicInteger implements Observer, Disposable, InnerQueuedObserverSupport {
        private static final long serialVersionUID = 8080567949447303262L;
        int activeCount;
        final Observer actual;
        volatile boolean cancelled;
        InnerQueuedObserver current;
        Disposable d;
        volatile boolean done;
        final AtomicThrowable error = new AtomicThrowable();
        final ErrorMode errorMode;
        final Function mapper;
        final int maxConcurrency;
        final ArrayDeque observers = new ArrayDeque();
        final int prefetch;
        SimpleQueue queue;
        int sourceMode;

        ConcatMapEagerMainObserver(Observer observer, Function function, int i, int i2, ErrorMode errorMode2) {
            this.actual = observer;
            this.mapper = function;
            this.maxConcurrency = i;
            this.prefetch = i2;
            this.errorMode = errorMode2;
        }

        public void dispose() {
            this.cancelled = true;
            if (getAndIncrement() == 0) {
                this.queue.clear();
                disposeAll();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
            if (r0 != null) goto L_0x0004;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
            r0.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:3:0x0007, code lost:
            r0 = (io.reactivex.internal.observers.InnerQueuedObserver) r1.observers.poll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:4:0x000f, code lost:
            if (r0 != null) goto L_0x0004;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:5:0x0011, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void disposeAll() {
            InnerQueuedObserver innerQueuedObserver = this.current;
        }

        public void drain() {
            if (getAndIncrement() == 0) {
                SimpleQueue simpleQueue = this.queue;
                ArrayDeque arrayDeque = this.observers;
                Observer observer = this.actual;
                ErrorMode errorMode2 = this.errorMode;
                int i = 1;
                while (true) {
                    int i2 = this.activeCount;
                    while (true) {
                        if (i2 == this.maxConcurrency) {
                            break;
                        } else if (this.cancelled) {
                            simpleQueue.clear();
                            disposeAll();
                            return;
                        } else if (errorMode2 != ErrorMode.IMMEDIATE || ((Throwable) this.error.get()) == null) {
                            try {
                                Object poll = simpleQueue.poll();
                                if (poll == null) {
                                    break;
                                }
                                Object apply = this.mapper.apply(poll);
                                ObjectHelper.requireNonNull(apply, "The mapper returned a null ObservableSource");
                                ObservableSource observableSource = (ObservableSource) apply;
                                InnerQueuedObserver innerQueuedObserver = new InnerQueuedObserver(this, this.prefetch);
                                arrayDeque.offer(innerQueuedObserver);
                                observableSource.subscribe(innerQueuedObserver);
                                i2++;
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                this.d.dispose();
                                simpleQueue.clear();
                                disposeAll();
                                this.error.addThrowable(th);
                                observer.onError(this.error.terminate());
                                return;
                            }
                        } else {
                            simpleQueue.clear();
                            disposeAll();
                            observer.onError(this.error.terminate());
                            return;
                        }
                    }
                    this.activeCount = i2;
                    if (this.cancelled) {
                        simpleQueue.clear();
                        disposeAll();
                        return;
                    } else if (errorMode2 != ErrorMode.IMMEDIATE || ((Throwable) this.error.get()) == null) {
                        InnerQueuedObserver innerQueuedObserver2 = this.current;
                        if (innerQueuedObserver2 == null) {
                            if (errorMode2 != ErrorMode.BOUNDARY || ((Throwable) this.error.get()) == null) {
                                boolean z = this.done;
                                InnerQueuedObserver innerQueuedObserver3 = (InnerQueuedObserver) arrayDeque.poll();
                                boolean z2 = innerQueuedObserver3 == null;
                                if (!z || !z2) {
                                    if (!z2) {
                                        this.current = innerQueuedObserver3;
                                    }
                                    innerQueuedObserver2 = innerQueuedObserver3;
                                } else {
                                    if (((Throwable) this.error.get()) != null) {
                                        simpleQueue.clear();
                                        disposeAll();
                                        observer.onError(this.error.terminate());
                                    } else {
                                        observer.onComplete();
                                    }
                                    return;
                                }
                            } else {
                                simpleQueue.clear();
                                disposeAll();
                                observer.onError(this.error.terminate());
                                return;
                            }
                        }
                        if (innerQueuedObserver2 != null) {
                            SimpleQueue queue2 = innerQueuedObserver2.queue();
                            while (!this.cancelled) {
                                boolean isDone = innerQueuedObserver2.isDone();
                                if (errorMode2 != ErrorMode.IMMEDIATE || ((Throwable) this.error.get()) == null) {
                                    try {
                                        Object poll2 = queue2.poll();
                                        boolean z3 = poll2 == null;
                                        if (isDone && z3) {
                                            this.current = null;
                                            this.activeCount--;
                                        } else if (!z3) {
                                            observer.onNext(poll2);
                                        }
                                    } catch (Throwable th2) {
                                        Exceptions.throwIfFatal(th2);
                                        this.error.addThrowable(th2);
                                    }
                                } else {
                                    simpleQueue.clear();
                                    disposeAll();
                                    observer.onError(this.error.terminate());
                                    return;
                                }
                            }
                            simpleQueue.clear();
                            disposeAll();
                            return;
                        }
                        i = addAndGet(-i);
                        if (i == 0) {
                            return;
                        }
                    } else {
                        simpleQueue.clear();
                        disposeAll();
                        observer.onError(this.error.terminate());
                        return;
                    }
                }
            }
        }

        public void innerComplete(InnerQueuedObserver innerQueuedObserver) {
            innerQueuedObserver.setDone();
            drain();
        }

        public void innerError(InnerQueuedObserver innerQueuedObserver, Throwable th) {
            if (this.error.addThrowable(th)) {
                if (this.errorMode == ErrorMode.IMMEDIATE) {
                    this.d.dispose();
                }
                innerQueuedObserver.setDone();
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void innerNext(InnerQueuedObserver innerQueuedObserver, Object obj) {
            innerQueuedObserver.queue().offer(obj);
            drain();
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void onError(Throwable th) {
            if (this.error.addThrowable(th)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            if (this.sourceMode == 0) {
                this.queue.offer(obj);
            }
            drain();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                if (disposable instanceof QueueDisposable) {
                    QueueDisposable queueDisposable = (QueueDisposable) disposable;
                    int requestFusion = queueDisposable.requestFusion(3);
                    if (requestFusion == 1) {
                        this.sourceMode = requestFusion;
                        this.queue = queueDisposable;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceMode = requestFusion;
                        this.queue = queueDisposable;
                        this.actual.onSubscribe(this);
                        return;
                    }
                }
                this.queue = new SpscLinkedArrayQueue(this.prefetch);
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableConcatMapEager(ObservableSource observableSource, Function function, ErrorMode errorMode2, int i, int i2) {
        super(observableSource);
        this.mapper = function;
        this.errorMode = errorMode2;
        this.maxConcurrency = i;
        this.prefetch = i2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        ObservableSource observableSource = this.source;
        ConcatMapEagerMainObserver concatMapEagerMainObserver = new ConcatMapEagerMainObserver(observer, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode);
        observableSource.subscribe(concatMapEagerMainObserver);
    }
}
