package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableJoin extends AbstractObservableWithUpstream {
    final Function leftEnd;
    final ObservableSource other;
    final BiFunction resultSelector;
    final Function rightEnd;

    final class JoinDisposable extends AtomicInteger implements Disposable, JoinSupport {
        static final Integer LEFT_CLOSE = Integer.valueOf(3);
        static final Integer LEFT_VALUE = Integer.valueOf(1);
        static final Integer RIGHT_CLOSE = Integer.valueOf(4);
        static final Integer RIGHT_VALUE = Integer.valueOf(2);
        private static final long serialVersionUID = -6071216598687999801L;
        final AtomicInteger active;
        final Observer actual;
        volatile boolean cancelled;
        final CompositeDisposable disposables = new CompositeDisposable();
        final AtomicReference error = new AtomicReference();
        final Function leftEnd;
        int leftIndex;
        final Map lefts = new LinkedHashMap();
        final SpscLinkedArrayQueue queue = new SpscLinkedArrayQueue(Observable.bufferSize());
        final BiFunction resultSelector;
        final Function rightEnd;
        int rightIndex;
        final Map rights = new LinkedHashMap();

        JoinDisposable(Observer observer, Function function, Function function2, BiFunction biFunction) {
            this.actual = observer;
            this.leftEnd = function;
            this.rightEnd = function2;
            this.resultSelector = biFunction;
            this.active = new AtomicInteger(2);
        }

        /* access modifiers changed from: 0000 */
        public void cancelAll() {
            this.disposables.dispose();
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelAll();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
                Observer observer = this.actual;
                int i = 1;
                while (!this.cancelled) {
                    if (((Throwable) this.error.get()) != null) {
                        spscLinkedArrayQueue.clear();
                        cancelAll();
                        errorAll(observer);
                        return;
                    }
                    boolean z = this.active.get() == 0;
                    Integer num = (Integer) spscLinkedArrayQueue.poll();
                    boolean z2 = num == null;
                    if (z && z2) {
                        this.lefts.clear();
                        this.rights.clear();
                        this.disposables.dispose();
                        observer.onComplete();
                        return;
                    } else if (z2) {
                        i = addAndGet(-i);
                        if (i == 0) {
                            return;
                        }
                    } else {
                        Object poll = spscLinkedArrayQueue.poll();
                        String str = "The resultSelector returned a null value";
                        if (num == LEFT_VALUE) {
                            int i2 = this.leftIndex;
                            this.leftIndex = i2 + 1;
                            this.lefts.put(Integer.valueOf(i2), poll);
                            try {
                                Object apply = this.leftEnd.apply(poll);
                                ObjectHelper.requireNonNull(apply, "The leftEnd returned a null ObservableSource");
                                ObservableSource observableSource = (ObservableSource) apply;
                                LeftRightEndObserver leftRightEndObserver = new LeftRightEndObserver(this, true, i2);
                                this.disposables.add(leftRightEndObserver);
                                observableSource.subscribe(leftRightEndObserver);
                                if (((Throwable) this.error.get()) != null) {
                                    spscLinkedArrayQueue.clear();
                                    cancelAll();
                                    errorAll(observer);
                                    return;
                                }
                                for (Object apply2 : this.rights.values()) {
                                    try {
                                        Object apply3 = this.resultSelector.apply(poll, apply2);
                                        ObjectHelper.requireNonNull(apply3, str);
                                        observer.onNext(apply3);
                                    } catch (Throwable th) {
                                        fail(th, observer, spscLinkedArrayQueue);
                                        return;
                                    }
                                }
                            } catch (Throwable th2) {
                                fail(th2, observer, spscLinkedArrayQueue);
                                return;
                            }
                        } else if (num == RIGHT_VALUE) {
                            int i3 = this.rightIndex;
                            this.rightIndex = i3 + 1;
                            this.rights.put(Integer.valueOf(i3), poll);
                            try {
                                Object apply4 = this.rightEnd.apply(poll);
                                ObjectHelper.requireNonNull(apply4, "The rightEnd returned a null ObservableSource");
                                ObservableSource observableSource2 = (ObservableSource) apply4;
                                LeftRightEndObserver leftRightEndObserver2 = new LeftRightEndObserver(this, false, i3);
                                this.disposables.add(leftRightEndObserver2);
                                observableSource2.subscribe(leftRightEndObserver2);
                                if (((Throwable) this.error.get()) != null) {
                                    spscLinkedArrayQueue.clear();
                                    cancelAll();
                                    errorAll(observer);
                                    return;
                                }
                                for (Object apply5 : this.lefts.values()) {
                                    try {
                                        Object apply6 = this.resultSelector.apply(apply5, poll);
                                        ObjectHelper.requireNonNull(apply6, str);
                                        observer.onNext(apply6);
                                    } catch (Throwable th3) {
                                        fail(th3, observer, spscLinkedArrayQueue);
                                        return;
                                    }
                                }
                            } catch (Throwable th4) {
                                fail(th4, observer, spscLinkedArrayQueue);
                                return;
                            }
                        } else {
                            LeftRightEndObserver leftRightEndObserver3 = (LeftRightEndObserver) poll;
                            (num == LEFT_CLOSE ? this.lefts : this.rights).remove(Integer.valueOf(leftRightEndObserver3.index));
                            this.disposables.remove(leftRightEndObserver3);
                        }
                    }
                }
                spscLinkedArrayQueue.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void errorAll(Observer observer) {
            Throwable terminate = ExceptionHelper.terminate(this.error);
            this.lefts.clear();
            this.rights.clear();
            observer.onError(terminate);
        }

        /* access modifiers changed from: 0000 */
        public void fail(Throwable th, Observer observer, SpscLinkedArrayQueue spscLinkedArrayQueue) {
            Exceptions.throwIfFatal(th);
            ExceptionHelper.addThrowable(this.error, th);
            spscLinkedArrayQueue.clear();
            cancelAll();
            errorAll(observer);
        }

        public void innerClose(boolean z, LeftRightEndObserver leftRightEndObserver) {
            synchronized (this) {
                this.queue.offer(z ? LEFT_CLOSE : RIGHT_CLOSE, leftRightEndObserver);
            }
            drain();
        }

        public void innerCloseError(Throwable th) {
            if (ExceptionHelper.addThrowable(this.error, th)) {
                drain();
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        public void innerComplete(LeftRightObserver leftRightObserver) {
            this.disposables.delete(leftRightObserver);
            this.active.decrementAndGet();
            drain();
        }

        public void innerError(Throwable th) {
            if (ExceptionHelper.addThrowable(this.error, th)) {
                this.active.decrementAndGet();
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void innerValue(boolean z, Object obj) {
            synchronized (this) {
                this.queue.offer(z ? LEFT_VALUE : RIGHT_VALUE, obj);
            }
            drain();
        }

        public boolean isDisposed() {
            return this.cancelled;
        }
    }

    public ObservableJoin(ObservableSource observableSource, ObservableSource observableSource2, Function function, Function function2, BiFunction biFunction) {
        super(observableSource);
        this.other = observableSource2;
        this.leftEnd = function;
        this.rightEnd = function2;
        this.resultSelector = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        JoinDisposable joinDisposable = new JoinDisposable(observer, this.leftEnd, this.rightEnd, this.resultSelector);
        observer.onSubscribe(joinDisposable);
        LeftRightObserver leftRightObserver = new LeftRightObserver(joinDisposable, true);
        joinDisposable.disposables.add(leftRightObserver);
        LeftRightObserver leftRightObserver2 = new LeftRightObserver(joinDisposable, false);
        joinDisposable.disposables.add(leftRightObserver2);
        this.source.subscribe(leftRightObserver);
        this.other.subscribe(leftRightObserver2);
    }
}
