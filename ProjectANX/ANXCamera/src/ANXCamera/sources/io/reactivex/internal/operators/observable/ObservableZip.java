package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableZip extends Observable {
    final int bufferSize;
    final boolean delayError;
    final ObservableSource[] sources;
    final Iterable sourcesIterable;
    final Function zipper;

    final class ZipCoordinator extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 2983708048395377667L;
        final Observer actual;
        volatile boolean cancelled;
        final boolean delayError;
        final ZipObserver[] observers;
        final Object[] row;
        final Function zipper;

        ZipCoordinator(Observer observer, Function function, int i, boolean z) {
            this.actual = observer;
            this.zipper = function;
            this.observers = new ZipObserver[i];
            this.row = new Object[i];
            this.delayError = z;
        }

        /* access modifiers changed from: 0000 */
        public void cancel() {
            clear();
            cancelSources();
        }

        /* access modifiers changed from: 0000 */
        public void cancelSources() {
            for (ZipObserver dispose : this.observers) {
                dispose.dispose();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Observer observer, boolean z3, ZipObserver zipObserver) {
            if (this.cancelled) {
                cancel();
                return true;
            }
            if (z) {
                if (!z3) {
                    Throwable th = zipObserver.error;
                    if (th != null) {
                        cancel();
                        observer.onError(th);
                        return true;
                    } else if (z2) {
                        cancel();
                        observer.onComplete();
                        return true;
                    }
                } else if (z2) {
                    Throwable th2 = zipObserver.error;
                    cancel();
                    if (th2 != null) {
                        observer.onError(th2);
                    } else {
                        observer.onComplete();
                    }
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            for (ZipObserver zipObserver : this.observers) {
                zipObserver.queue.clear();
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelSources();
                if (getAndIncrement() == 0) {
                    clear();
                }
            }
        }

        public void drain() {
            if (getAndIncrement() == 0) {
                ZipObserver[] zipObserverArr = this.observers;
                Observer observer = this.actual;
                Object[] objArr = this.row;
                boolean z = this.delayError;
                int i = 1;
                while (true) {
                    int i2 = 0;
                    int i3 = 0;
                    for (ZipObserver zipObserver : zipObserverArr) {
                        if (objArr[i3] == null) {
                            boolean z2 = zipObserver.done;
                            Object poll = zipObserver.queue.poll();
                            boolean z3 = poll == null;
                            if (!checkTerminated(z2, z3, observer, z, zipObserver)) {
                                if (!z3) {
                                    objArr[i3] = poll;
                                } else {
                                    i2++;
                                }
                            } else {
                                return;
                            }
                        } else if (zipObserver.done && !z) {
                            Throwable th = zipObserver.error;
                            if (th != null) {
                                cancel();
                                observer.onError(th);
                                return;
                            }
                        }
                        i3++;
                    }
                    if (i2 != 0) {
                        i = addAndGet(-i);
                        if (i == 0) {
                            return;
                        }
                    } else {
                        try {
                            Object apply = this.zipper.apply(objArr.clone());
                            ObjectHelper.requireNonNull(apply, "The zipper returned a null value");
                            observer.onNext(apply);
                            Arrays.fill(objArr, null);
                        } catch (Throwable th2) {
                            Exceptions.throwIfFatal(th2);
                            cancel();
                            observer.onError(th2);
                            return;
                        }
                    }
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void subscribe(ObservableSource[] observableSourceArr, int i) {
            ZipObserver[] zipObserverArr = this.observers;
            int length = zipObserverArr.length;
            for (int i2 = 0; i2 < length; i2++) {
                zipObserverArr[i2] = new ZipObserver(this, i);
            }
            lazySet(0);
            this.actual.onSubscribe(this);
            for (int i3 = 0; i3 < length && !this.cancelled; i3++) {
                observableSourceArr[i3].subscribe(zipObserverArr[i3]);
            }
        }
    }

    final class ZipObserver implements Observer {
        volatile boolean done;
        Throwable error;
        final ZipCoordinator parent;
        final SpscLinkedArrayQueue queue;
        final AtomicReference s = new AtomicReference();

        ZipObserver(ZipCoordinator zipCoordinator, int i) {
            this.parent = zipCoordinator;
            this.queue = new SpscLinkedArrayQueue(i);
        }

        public void dispose() {
            DisposableHelper.dispose(this.s);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            this.parent.drain();
        }

        public void onNext(Object obj) {
            this.queue.offer(obj);
            this.parent.drain();
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.s, disposable);
        }
    }

    public ObservableZip(ObservableSource[] observableSourceArr, Iterable iterable, Function function, int i, boolean z) {
        this.sources = observableSourceArr;
        this.sourcesIterable = iterable;
        this.zipper = function;
        this.bufferSize = i;
        this.delayError = z;
    }

    public void subscribeActual(Observer observer) {
        int i;
        ObservableSource[] observableSourceArr = this.sources;
        if (observableSourceArr == null) {
            observableSourceArr = new Observable[8];
            i = 0;
            for (ObservableSource observableSource : this.sourcesIterable) {
                if (i == observableSourceArr.length) {
                    ObservableSource[] observableSourceArr2 = new ObservableSource[((i >> 2) + i)];
                    System.arraycopy(observableSourceArr, 0, observableSourceArr2, 0, i);
                    observableSourceArr = observableSourceArr2;
                }
                int i2 = i + 1;
                observableSourceArr[i] = observableSource;
                i = i2;
            }
        } else {
            i = observableSourceArr.length;
        }
        if (i == 0) {
            EmptyDisposable.complete(observer);
        } else {
            new ZipCoordinator(observer, this.zipper, i, this.delayError).subscribe(observableSourceArr, this.bufferSize);
        }
    }
}
