package io.reactivex.internal.operators.single;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;

public final class SingleFlatMapIterableFlowable extends Flowable {
    final Function mapper;
    final SingleSource source;

    final class FlatMapIterableObserver extends BasicIntQueueSubscription implements SingleObserver {
        private static final long serialVersionUID = -8938804753851907758L;
        final Subscriber actual;
        volatile boolean cancelled;
        Disposable d;
        volatile Iterator it;
        final Function mapper;
        boolean outputFused;
        final AtomicLong requested = new AtomicLong();

        FlatMapIterableObserver(Subscriber subscriber, Function function) {
            this.actual = subscriber;
            this.mapper = function;
        }

        public void cancel() {
            this.cancelled = true;
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public void clear() {
            this.it = null;
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                Subscriber subscriber = this.actual;
                Iterator it2 = this.it;
                if (!this.outputFused || it2 == null) {
                    int i = 1;
                    while (true) {
                        if (it2 != null) {
                            long j = this.requested.get();
                            if (j == Long.MAX_VALUE) {
                                slowPath(subscriber, it2);
                                return;
                            }
                            long j2 = 0;
                            while (j2 != j) {
                                if (!this.cancelled) {
                                    try {
                                        Object next = it2.next();
                                        ObjectHelper.requireNonNull(next, "The iterator returned a null value");
                                        subscriber.onNext(next);
                                        if (!this.cancelled) {
                                            j2++;
                                            try {
                                                if (!it2.hasNext()) {
                                                    subscriber.onComplete();
                                                    return;
                                                }
                                            } catch (Throwable th) {
                                                Exceptions.throwIfFatal(th);
                                                subscriber.onError(th);
                                                return;
                                            }
                                        } else {
                                            return;
                                        }
                                    } catch (Throwable th2) {
                                        Exceptions.throwIfFatal(th2);
                                        subscriber.onError(th2);
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            }
                            if (j2 != 0) {
                                BackpressureHelper.produced(this.requested, j2);
                            }
                        }
                        i = addAndGet(-i);
                        if (i != 0) {
                            if (it2 == null) {
                                it2 = this.it;
                            }
                        } else {
                            return;
                        }
                    }
                } else {
                    subscriber.onNext(null);
                    subscriber.onComplete();
                }
            }
        }

        public boolean isEmpty() {
            return this.it == null;
        }

        public void onError(Throwable th) {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            try {
                Iterator it2 = ((Iterable) this.mapper.apply(obj)).iterator();
                if (!it2.hasNext()) {
                    this.actual.onComplete();
                    return;
                }
                this.it = it2;
                drain();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }

        @Nullable
        public Object poll() {
            Iterator it2 = this.it;
            if (it2 == null) {
                return null;
            }
            Object next = it2.next();
            ObjectHelper.requireNonNull(next, "The iterator returned a null value");
            if (!it2.hasNext()) {
                this.it = null;
            }
            return next;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        public int requestFusion(int i) {
            if ((i & 2) == 0) {
                return 0;
            }
            this.outputFused = true;
            return 2;
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(Subscriber subscriber, Iterator it2) {
            while (!this.cancelled) {
                try {
                    subscriber.onNext(it2.next());
                    if (!this.cancelled) {
                        if (!it2.hasNext()) {
                            subscriber.onComplete();
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    subscriber.onError(th);
                    return;
                }
            }
        }
    }

    public SingleFlatMapIterableFlowable(SingleSource singleSource, Function function) {
        this.source = singleSource;
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe(new FlatMapIterableObserver(subscriber, this.mapper));
    }
}
