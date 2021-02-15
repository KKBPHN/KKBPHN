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
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableCombineLatest extends Observable {
    final int bufferSize;
    final Function combiner;
    final boolean delayError;
    final ObservableSource[] sources;
    final Iterable sourcesIterable;

    final class CombinerObserver extends AtomicReference implements Observer {
        private static final long serialVersionUID = -4823716997131257941L;
        final int index;
        final LatestCoordinator parent;

        CombinerObserver(LatestCoordinator latestCoordinator, int i) {
            this.parent = latestCoordinator;
            this.index = i;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public void onComplete() {
            this.parent.innerComplete(this.index);
        }

        public void onError(Throwable th) {
            this.parent.innerError(this.index, th);
        }

        public void onNext(Object obj) {
            this.parent.innerNext(this.index, obj);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }
    }

    final class LatestCoordinator extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 8567835998786448817L;
        int active;
        final Observer actual;
        volatile boolean cancelled;
        final Function combiner;
        int complete;
        final boolean delayError;
        volatile boolean done;
        final AtomicThrowable errors = new AtomicThrowable();
        Object[] latest;
        final CombinerObserver[] observers;
        final SpscLinkedArrayQueue queue;

        LatestCoordinator(Observer observer, Function function, int i, int i2, boolean z) {
            this.actual = observer;
            this.combiner = function;
            this.delayError = z;
            this.latest = new Object[i];
            CombinerObserver[] combinerObserverArr = new CombinerObserver[i];
            for (int i3 = 0; i3 < i; i3++) {
                combinerObserverArr[i3] = new CombinerObserver(this, i3);
            }
            this.observers = combinerObserverArr;
            this.queue = new SpscLinkedArrayQueue(i2);
        }

        /* access modifiers changed from: 0000 */
        public void cancelSources() {
            for (CombinerObserver dispose : this.observers) {
                dispose.dispose();
            }
        }

        /* access modifiers changed from: 0000 */
        public void clear(SpscLinkedArrayQueue spscLinkedArrayQueue) {
            synchronized (this) {
                this.latest = null;
            }
            spscLinkedArrayQueue.clear();
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelSources();
                if (getAndIncrement() == 0) {
                    clear(this.queue);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
                Observer observer = this.actual;
                boolean z = this.delayError;
                int i = 1;
                while (!this.cancelled) {
                    if (z || this.errors.get() == null) {
                        boolean z2 = this.done;
                        Object[] objArr = (Object[]) spscLinkedArrayQueue.poll();
                        boolean z3 = objArr == null;
                        if (z2 && z3) {
                            clear(spscLinkedArrayQueue);
                            Throwable terminate = this.errors.terminate();
                            if (terminate == null) {
                                observer.onComplete();
                            } else {
                                observer.onError(terminate);
                            }
                            return;
                        } else if (z3) {
                            i = addAndGet(-i);
                            if (i == 0) {
                                return;
                            }
                        } else {
                            try {
                                Object apply = this.combiner.apply(objArr);
                                ObjectHelper.requireNonNull(apply, "The combiner returned a null value");
                                observer.onNext(apply);
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                this.errors.addThrowable(th);
                            }
                        }
                    } else {
                        cancelSources();
                        clear(spscLinkedArrayQueue);
                        observer.onError(this.errors.terminate());
                        return;
                    }
                }
                clear(spscLinkedArrayQueue);
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
            if (r2 == r0.length) goto L_0x0019;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x001c, code lost:
            if (r4 == false) goto L_0x0021;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x001e, code lost:
            cancelSources();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0021, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0024, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void innerComplete(int i) {
            synchronized (this) {
                Object[] objArr = this.latest;
                if (objArr != null) {
                    boolean z = objArr[i] == null;
                    if (!z) {
                        int i2 = this.complete + 1;
                        this.complete = i2;
                    }
                    this.done = true;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0023, code lost:
            if (r1 == r4.length) goto L_0x0025;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void innerError(int i, Throwable th) {
            boolean z;
            if (this.errors.addThrowable(th)) {
                if (this.delayError) {
                    synchronized (this) {
                        Object[] objArr = this.latest;
                        if (objArr != null) {
                            z = objArr[i] == null;
                            if (!z) {
                                int i2 = this.complete + 1;
                                this.complete = i2;
                            }
                            this.done = true;
                        } else {
                            return;
                        }
                    }
                } else {
                    z = true;
                }
                if (z) {
                    cancelSources();
                }
                drain();
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0023, code lost:
            if (r4 == false) goto L_0x0028;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0025, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0028, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void innerNext(int i, Object obj) {
            boolean z;
            synchronized (this) {
                Object[] objArr = this.latest;
                if (objArr != null) {
                    Object obj2 = objArr[i];
                    int i2 = this.active;
                    if (obj2 == null) {
                        i2++;
                        this.active = i2;
                    }
                    objArr[i] = obj;
                    if (i2 == objArr.length) {
                        this.queue.offer(objArr.clone());
                        z = true;
                    } else {
                        z = false;
                    }
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void subscribe(ObservableSource[] observableSourceArr) {
            CombinerObserver[] combinerObserverArr = this.observers;
            int length = combinerObserverArr.length;
            this.actual.onSubscribe(this);
            for (int i = 0; i < length && !this.done && !this.cancelled; i++) {
                observableSourceArr[i].subscribe(combinerObserverArr[i]);
            }
        }
    }

    public ObservableCombineLatest(ObservableSource[] observableSourceArr, Iterable iterable, Function function, int i, boolean z) {
        this.sources = observableSourceArr;
        this.sourcesIterable = iterable;
        this.combiner = function;
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
        int i3 = i;
        if (i3 == 0) {
            EmptyDisposable.complete(observer);
            return;
        }
        LatestCoordinator latestCoordinator = new LatestCoordinator(observer, this.combiner, i3, this.bufferSize, this.delayError);
        latestCoordinator.subscribe(observableSourceArr);
    }
}
