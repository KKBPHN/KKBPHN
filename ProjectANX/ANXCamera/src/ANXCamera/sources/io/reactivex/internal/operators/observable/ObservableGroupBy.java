package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.observables.GroupedObservable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableGroupBy extends AbstractObservableWithUpstream {
    final int bufferSize;
    final boolean delayError;
    final Function keySelector;
    final Function valueSelector;

    public final class GroupByObserver extends AtomicInteger implements Observer, Disposable {
        static final Object NULL_KEY = new Object();
        private static final long serialVersionUID = -3688291656102519502L;
        final Observer actual;
        final int bufferSize;
        final AtomicBoolean cancelled = new AtomicBoolean();
        final boolean delayError;
        final Map groups;
        final Function keySelector;
        Disposable s;
        final Function valueSelector;

        public GroupByObserver(Observer observer, Function function, Function function2, int i, boolean z) {
            this.actual = observer;
            this.keySelector = function;
            this.valueSelector = function2;
            this.bufferSize = i;
            this.delayError = z;
            this.groups = new ConcurrentHashMap();
            lazySet(1);
        }

        public void cancel(Object obj) {
            if (obj == null) {
                obj = NULL_KEY;
            }
            this.groups.remove(obj);
            if (decrementAndGet() == 0) {
                this.s.dispose();
            }
        }

        public void dispose() {
            if (this.cancelled.compareAndSet(false, true) && decrementAndGet() == 0) {
                this.s.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled.get();
        }

        public void onComplete() {
            ArrayList<GroupedUnicast> arrayList = new ArrayList<>(this.groups.values());
            this.groups.clear();
            for (GroupedUnicast onComplete : arrayList) {
                onComplete.onComplete();
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            ArrayList<GroupedUnicast> arrayList = new ArrayList<>(this.groups.values());
            this.groups.clear();
            for (GroupedUnicast onError : arrayList) {
                onError.onError(th);
            }
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            try {
                Object apply = this.keySelector.apply(obj);
                Object obj2 = apply != null ? apply : NULL_KEY;
                GroupedUnicast groupedUnicast = (GroupedUnicast) this.groups.get(obj2);
                if (groupedUnicast == null) {
                    if (!this.cancelled.get()) {
                        groupedUnicast = GroupedUnicast.createWith(apply, this.bufferSize, this, this.delayError);
                        this.groups.put(obj2, groupedUnicast);
                        getAndIncrement();
                        this.actual.onNext(groupedUnicast);
                    } else {
                        return;
                    }
                }
                Object apply2 = this.valueSelector.apply(obj);
                ObjectHelper.requireNonNull(apply2, "The value supplied is null");
                groupedUnicast.onNext(apply2);
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

    final class GroupedUnicast extends GroupedObservable {
        final State state;

        protected GroupedUnicast(Object obj, State state2) {
            super(obj);
            this.state = state2;
        }

        public static GroupedUnicast createWith(Object obj, int i, GroupByObserver groupByObserver, boolean z) {
            return new GroupedUnicast(obj, new State(i, groupByObserver, obj, z));
        }

        public void onComplete() {
            this.state.onComplete();
        }

        public void onError(Throwable th) {
            this.state.onError(th);
        }

        public void onNext(Object obj) {
            this.state.onNext(obj);
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Observer observer) {
            this.state.subscribe(observer);
        }
    }

    final class State extends AtomicInteger implements Disposable, ObservableSource {
        private static final long serialVersionUID = -3852313036005250360L;
        final AtomicReference actual = new AtomicReference();
        final AtomicBoolean cancelled = new AtomicBoolean();
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final Object key;
        final AtomicBoolean once = new AtomicBoolean();
        final GroupByObserver parent;
        final SpscLinkedArrayQueue queue;

        State(int i, GroupByObserver groupByObserver, Object obj, boolean z) {
            this.queue = new SpscLinkedArrayQueue(i);
            this.parent = groupByObserver;
            this.key = obj;
            this.delayError = z;
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Observer observer, boolean z3) {
            if (this.cancelled.get()) {
                this.queue.clear();
                this.parent.cancel(this.key);
                this.actual.lazySet(null);
                return true;
            }
            if (z) {
                if (!z3) {
                    Throwable th = this.error;
                    if (th != null) {
                        this.queue.clear();
                        this.actual.lazySet(null);
                        observer.onError(th);
                        return true;
                    } else if (z2) {
                        this.actual.lazySet(null);
                        observer.onComplete();
                        return true;
                    }
                } else if (z2) {
                    Throwable th2 = this.error;
                    this.actual.lazySet(null);
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

        public void dispose() {
            if (this.cancelled.compareAndSet(false, true) && getAndIncrement() == 0) {
                this.actual.lazySet(null);
                this.parent.cancel(this.key);
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
                boolean z = this.delayError;
                Observer observer = (Observer) this.actual.get();
                int i = 1;
                while (true) {
                    if (observer != null) {
                        while (true) {
                            boolean z2 = this.done;
                            Object poll = spscLinkedArrayQueue.poll();
                            boolean z3 = poll == null;
                            if (!checkTerminated(z2, z3, observer, z)) {
                                if (z3) {
                                    break;
                                }
                                observer.onNext(poll);
                            } else {
                                return;
                            }
                        }
                    }
                    i = addAndGet(-i);
                    if (i != 0) {
                        if (observer == null) {
                            observer = (Observer) this.actual.get();
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled.get();
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            drain();
        }

        public void onNext(Object obj) {
            this.queue.offer(obj);
            drain();
        }

        public void subscribe(Observer observer) {
            if (this.once.compareAndSet(false, true)) {
                observer.onSubscribe(this);
                this.actual.lazySet(observer);
                if (this.cancelled.get()) {
                    this.actual.lazySet(null);
                } else {
                    drain();
                }
            } else {
                EmptyDisposable.error((Throwable) new IllegalStateException("Only one Observer allowed!"), observer);
            }
        }
    }

    public ObservableGroupBy(ObservableSource observableSource, Function function, Function function2, int i, boolean z) {
        super(observableSource);
        this.keySelector = function;
        this.valueSelector = function2;
        this.bufferSize = i;
        this.delayError = z;
    }

    public void subscribeActual(Observer observer) {
        ObservableSource observableSource = this.source;
        GroupByObserver groupByObserver = new GroupByObserver(observer, this.keySelector, this.valueSelector, this.bufferSize, this.delayError);
        observableSource.subscribe(groupByObserver);
    }
}
