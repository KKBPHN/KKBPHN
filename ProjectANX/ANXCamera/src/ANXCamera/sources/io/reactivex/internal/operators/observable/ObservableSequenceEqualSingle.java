package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.disposables.ArrayCompositeDisposable;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableSequenceEqualSingle extends Single implements FuseToObservable {
    final int bufferSize;
    final BiPredicate comparer;
    final ObservableSource first;
    final ObservableSource second;

    final class EqualCoordinator extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = -6178010334400373240L;
        final SingleObserver actual;
        volatile boolean cancelled;
        final BiPredicate comparer;
        final ObservableSource first;
        final EqualObserver[] observers;
        final ArrayCompositeDisposable resources = new ArrayCompositeDisposable(2);
        final ObservableSource second;
        Object v1;
        Object v2;

        EqualCoordinator(SingleObserver singleObserver, int i, ObservableSource observableSource, ObservableSource observableSource2, BiPredicate biPredicate) {
            this.actual = singleObserver;
            this.first = observableSource;
            this.second = observableSource2;
            this.comparer = biPredicate;
            EqualObserver[] equalObserverArr = new EqualObserver[2];
            this.observers = equalObserverArr;
            equalObserverArr[0] = new EqualObserver(this, 0, i);
            equalObserverArr[1] = new EqualObserver(this, 1, i);
        }

        /* access modifiers changed from: 0000 */
        public void cancel(SpscLinkedArrayQueue spscLinkedArrayQueue, SpscLinkedArrayQueue spscLinkedArrayQueue2) {
            this.cancelled = true;
            spscLinkedArrayQueue.clear();
            spscLinkedArrayQueue2.clear();
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.resources.dispose();
                if (getAndIncrement() == 0) {
                    EqualObserver[] equalObserverArr = this.observers;
                    equalObserverArr[0].queue.clear();
                    equalObserverArr[1].queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                EqualObserver[] equalObserverArr = this.observers;
                EqualObserver equalObserver = equalObserverArr[0];
                SpscLinkedArrayQueue spscLinkedArrayQueue = equalObserver.queue;
                EqualObserver equalObserver2 = equalObserverArr[1];
                SpscLinkedArrayQueue spscLinkedArrayQueue2 = equalObserver2.queue;
                int i = 1;
                while (!this.cancelled) {
                    boolean z = equalObserver.done;
                    if (z) {
                        Throwable th = equalObserver.error;
                        if (th != null) {
                            cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                            this.actual.onError(th);
                            return;
                        }
                    }
                    boolean z2 = equalObserver2.done;
                    if (z2) {
                        Throwable th2 = equalObserver2.error;
                        if (th2 != null) {
                            cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                            this.actual.onError(th2);
                            return;
                        }
                    }
                    if (this.v1 == null) {
                        this.v1 = spscLinkedArrayQueue.poll();
                    }
                    boolean z3 = this.v1 == null;
                    if (this.v2 == null) {
                        this.v2 = spscLinkedArrayQueue2.poll();
                    }
                    boolean z4 = this.v2 == null;
                    if (z && z2 && z3 && z4) {
                        this.actual.onSuccess(Boolean.valueOf(true));
                        return;
                    } else if (!z || !z2 || z3 == z4) {
                        if (!z3 && !z4) {
                            try {
                                if (!this.comparer.test(this.v1, this.v2)) {
                                    cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                                    this.actual.onSuccess(Boolean.valueOf(false));
                                    return;
                                }
                                this.v1 = null;
                                this.v2 = null;
                            } catch (Throwable th3) {
                                Exceptions.throwIfFatal(th3);
                                cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                                this.actual.onError(th3);
                                return;
                            }
                        }
                        if (z3 || z4) {
                            i = addAndGet(-i);
                            if (i == 0) {
                                return;
                            }
                        }
                    } else {
                        cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                        this.actual.onSuccess(Boolean.valueOf(false));
                        return;
                    }
                }
                spscLinkedArrayQueue.clear();
                spscLinkedArrayQueue2.clear();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public boolean setDisposable(Disposable disposable, int i) {
            return this.resources.setResource(i, disposable);
        }

        /* access modifiers changed from: 0000 */
        public void subscribe() {
            EqualObserver[] equalObserverArr = this.observers;
            this.first.subscribe(equalObserverArr[0]);
            this.second.subscribe(equalObserverArr[1]);
        }
    }

    final class EqualObserver implements Observer {
        volatile boolean done;
        Throwable error;
        final int index;
        final EqualCoordinator parent;
        final SpscLinkedArrayQueue queue;

        EqualObserver(EqualCoordinator equalCoordinator, int i, int i2) {
            this.parent = equalCoordinator;
            this.index = i;
            this.queue = new SpscLinkedArrayQueue(i2);
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
            this.parent.setDisposable(disposable, this.index);
        }
    }

    public ObservableSequenceEqualSingle(ObservableSource observableSource, ObservableSource observableSource2, BiPredicate biPredicate, int i) {
        this.first = observableSource;
        this.second = observableSource2;
        this.comparer = biPredicate;
        this.bufferSize = i;
    }

    public Observable fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableSequenceEqual(this.first, this.second, this.comparer, this.bufferSize));
    }

    public void subscribeActual(SingleObserver singleObserver) {
        EqualCoordinator equalCoordinator = new EqualCoordinator(singleObserver, this.bufferSize, this.first, this.second, this.comparer);
        singleObserver.onSubscribe(equalCoordinator);
        equalCoordinator.subscribe();
    }
}
