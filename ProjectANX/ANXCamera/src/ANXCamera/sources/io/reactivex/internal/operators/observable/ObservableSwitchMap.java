package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableSwitchMap extends AbstractObservableWithUpstream {
    final int bufferSize;
    final boolean delayErrors;
    final Function mapper;

    final class SwitchMapInnerObserver extends AtomicReference implements Observer {
        private static final long serialVersionUID = 3837284832786408377L;
        volatile boolean done;
        final long index;
        final SwitchMapObserver parent;
        final SpscLinkedArrayQueue queue;

        SwitchMapInnerObserver(SwitchMapObserver switchMapObserver, long j, int i) {
            this.parent = switchMapObserver;
            this.index = j;
            this.queue = new SpscLinkedArrayQueue(i);
        }

        public void cancel() {
            DisposableHelper.dispose(this);
        }

        public void onComplete() {
            if (this.index == this.parent.unique) {
                this.done = true;
                this.parent.drain();
            }
        }

        public void onError(Throwable th) {
            this.parent.innerError(this, th);
        }

        public void onNext(Object obj) {
            if (this.index == this.parent.unique) {
                this.queue.offer(obj);
                this.parent.drain();
            }
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }
    }

    final class SwitchMapObserver extends AtomicInteger implements Observer, Disposable {
        static final SwitchMapInnerObserver CANCELLED = new SwitchMapInnerObserver(null, -1, 1);
        private static final long serialVersionUID = -3491074160481096299L;
        final AtomicReference active = new AtomicReference();
        final Observer actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable errors;
        final Function mapper;
        Disposable s;
        volatile long unique;

        static {
            CANCELLED.cancel();
        }

        SwitchMapObserver(Observer observer, Function function, int i, boolean z) {
            this.actual = observer;
            this.mapper = function;
            this.bufferSize = i;
            this.delayErrors = z;
            this.errors = new AtomicThrowable();
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.s.dispose();
                disposeInner();
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeInner() {
            SwitchMapInnerObserver switchMapInnerObserver = (SwitchMapInnerObserver) this.active.get();
            SwitchMapInnerObserver switchMapInnerObserver2 = CANCELLED;
            if (switchMapInnerObserver != switchMapInnerObserver2) {
                SwitchMapInnerObserver switchMapInnerObserver3 = (SwitchMapInnerObserver) this.active.getAndSet(switchMapInnerObserver2);
                if (switchMapInnerObserver3 != CANCELLED && switchMapInnerObserver3 != null) {
                    switchMapInnerObserver3.cancel();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x006b, code lost:
            if (r6 != false) goto L_0x006d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x007e, code lost:
            if (r6 != false) goto L_0x006d;
         */
        /* JADX WARNING: Removed duplicated region for block: B:58:0x00b8  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            if (getAndIncrement() == 0) {
                Observer observer = this.actual;
                int i = 1;
                while (!this.cancelled) {
                    boolean z = false;
                    if (this.done) {
                        boolean z2 = this.active.get() == null;
                        if (this.delayErrors) {
                            if (z2) {
                                Throwable th = (Throwable) this.errors.get();
                                if (th != null) {
                                    observer.onError(th);
                                } else {
                                    observer.onComplete();
                                }
                                return;
                            }
                        } else if (((Throwable) this.errors.get()) != null) {
                            observer.onError(this.errors.terminate());
                            return;
                        } else if (z2) {
                            observer.onComplete();
                            return;
                        }
                    }
                    SwitchMapInnerObserver switchMapInnerObserver = (SwitchMapInnerObserver) this.active.get();
                    if (switchMapInnerObserver != null) {
                        SpscLinkedArrayQueue spscLinkedArrayQueue = switchMapInnerObserver.queue;
                        if (switchMapInnerObserver.done) {
                            boolean isEmpty = spscLinkedArrayQueue.isEmpty();
                            if (!this.delayErrors) {
                                if (((Throwable) this.errors.get()) != null) {
                                    observer.onError(this.errors.terminate());
                                    return;
                                }
                            }
                            this.active.compareAndSet(switchMapInnerObserver, null);
                        }
                        while (!this.cancelled) {
                            if (switchMapInnerObserver == this.active.get()) {
                                if (this.delayErrors || ((Throwable) this.errors.get()) == null) {
                                    boolean z3 = switchMapInnerObserver.done;
                                    Object poll = spscLinkedArrayQueue.poll();
                                    boolean z4 = poll == null;
                                    if (z3 && z4) {
                                        this.active.compareAndSet(switchMapInnerObserver, null);
                                    } else if (!z4) {
                                        observer.onNext(poll);
                                    } else if (z) {
                                    }
                                } else {
                                    observer.onError(this.errors.terminate());
                                    return;
                                }
                            }
                            z = true;
                            if (z) {
                            }
                        }
                        return;
                    }
                    i = addAndGet(-i);
                    if (i == 0) {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(SwitchMapInnerObserver switchMapInnerObserver, Throwable th) {
            if (switchMapInnerObserver.index != this.unique || !this.errors.addThrowable(th)) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (!this.delayErrors) {
                this.s.dispose();
            }
            switchMapInnerObserver.done = true;
            drain();
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void onError(Throwable th) {
            if (this.done || !this.errors.addThrowable(th)) {
                if (!this.delayErrors) {
                    disposeInner();
                }
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            drain();
        }

        public void onNext(Object obj) {
            long j = this.unique + 1;
            this.unique = j;
            SwitchMapInnerObserver switchMapInnerObserver = (SwitchMapInnerObserver) this.active.get();
            if (switchMapInnerObserver != null) {
                switchMapInnerObserver.cancel();
            }
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The ObservableSource returned is null");
                ObservableSource observableSource = (ObservableSource) apply;
                SwitchMapInnerObserver switchMapInnerObserver2 = new SwitchMapInnerObserver(this, j, this.bufferSize);
                while (true) {
                    SwitchMapInnerObserver switchMapInnerObserver3 = (SwitchMapInnerObserver) this.active.get();
                    if (switchMapInnerObserver3 != CANCELLED) {
                        if (this.active.compareAndSet(switchMapInnerObserver3, switchMapInnerObserver2)) {
                            observableSource.subscribe(switchMapInnerObserver2);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.s.dispose();
                onError(th);
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableSwitchMap(ObservableSource observableSource, Function function, int i, boolean z) {
        super(observableSource);
        this.mapper = function;
        this.bufferSize = i;
        this.delayErrors = z;
    }

    public void subscribeActual(Observer observer) {
        if (!ObservableScalarXMap.tryScalarXMapSubscribe(this.source, observer, this.mapper)) {
            this.source.subscribe(new SwitchMapObserver(observer, this.mapper, this.bufferSize, this.delayErrors));
        }
    }
}
