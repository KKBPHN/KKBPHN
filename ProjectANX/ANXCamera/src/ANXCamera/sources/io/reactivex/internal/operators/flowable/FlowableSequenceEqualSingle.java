package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class FlowableSequenceEqualSingle extends Single implements FuseToFlowable {
    final BiPredicate comparer;
    final Publisher first;
    final int prefetch;
    final Publisher second;

    final class EqualCoordinator extends AtomicInteger implements Disposable, EqualCoordinatorHelper {
        private static final long serialVersionUID = -6178010334400373240L;
        final SingleObserver actual;
        final BiPredicate comparer;
        final AtomicThrowable error = new AtomicThrowable();
        final EqualSubscriber first;
        final EqualSubscriber second;
        Object v1;
        Object v2;

        EqualCoordinator(SingleObserver singleObserver, int i, BiPredicate biPredicate) {
            this.actual = singleObserver;
            this.comparer = biPredicate;
            this.first = new EqualSubscriber(this, i);
            this.second = new EqualSubscriber(this, i);
        }

        /* access modifiers changed from: 0000 */
        public void cancelAndClear() {
            this.first.cancel();
            this.first.clear();
            this.second.cancel();
            this.second.clear();
        }

        public void dispose() {
            this.first.cancel();
            this.second.cancel();
            if (getAndIncrement() == 0) {
                this.first.clear();
                this.second.clear();
            }
        }

        public void drain() {
            if (getAndIncrement() == 0) {
                int i = 1;
                do {
                    SimpleQueue simpleQueue = this.first.queue;
                    SimpleQueue simpleQueue2 = this.second.queue;
                    if (simpleQueue != null && simpleQueue2 != null) {
                        while (!isDisposed()) {
                            if (((Throwable) this.error.get()) != null) {
                                cancelAndClear();
                                this.actual.onError(this.error.terminate());
                                return;
                            }
                            boolean z = this.first.done;
                            Object obj = this.v1;
                            if (obj == null) {
                                try {
                                    obj = simpleQueue.poll();
                                    this.v1 = obj;
                                } catch (Throwable th) {
                                    Exceptions.throwIfFatal(th);
                                    cancelAndClear();
                                    this.error.addThrowable(th);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                            boolean z2 = obj == null;
                            boolean z3 = this.second.done;
                            Object obj2 = this.v2;
                            if (obj2 == null) {
                                try {
                                    obj2 = simpleQueue2.poll();
                                    this.v2 = obj2;
                                } catch (Throwable th2) {
                                    Exceptions.throwIfFatal(th2);
                                    cancelAndClear();
                                    this.error.addThrowable(th2);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                            boolean z4 = obj2 == null;
                            if (z && z3 && z2 && z4) {
                                this.actual.onSuccess(Boolean.valueOf(true));
                                return;
                            } else if (z && z3 && z2 != z4) {
                                cancelAndClear();
                                this.actual.onSuccess(Boolean.valueOf(false));
                                return;
                            } else if (!z2 && !z4) {
                                try {
                                    if (!this.comparer.test(obj, obj2)) {
                                        cancelAndClear();
                                        this.actual.onSuccess(Boolean.valueOf(false));
                                        return;
                                    }
                                    this.v1 = null;
                                    this.v2 = null;
                                    this.first.request();
                                    this.second.request();
                                } catch (Throwable th3) {
                                    Exceptions.throwIfFatal(th3);
                                    cancelAndClear();
                                    this.error.addThrowable(th3);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                        }
                        this.first.clear();
                        this.second.clear();
                        return;
                    } else if (isDisposed()) {
                        this.first.clear();
                        this.second.clear();
                        return;
                    } else if (((Throwable) this.error.get()) != null) {
                        cancelAndClear();
                        this.actual.onError(this.error.terminate());
                        return;
                    }
                    i = addAndGet(-i);
                } while (i != 0);
            }
        }

        public void innerError(Throwable th) {
            if (this.error.addThrowable(th)) {
                drain();
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) this.first.get());
        }

        /* access modifiers changed from: 0000 */
        public void subscribe(Publisher publisher, Publisher publisher2) {
            publisher.subscribe(this.first);
            publisher2.subscribe(this.second);
        }
    }

    public FlowableSequenceEqualSingle(Publisher publisher, Publisher publisher2, BiPredicate biPredicate, int i) {
        this.first = publisher;
        this.second = publisher2;
        this.comparer = biPredicate;
        this.prefetch = i;
    }

    public Flowable fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableSequenceEqual(this.first, this.second, this.comparer, this.prefetch));
    }

    public void subscribeActual(SingleObserver singleObserver) {
        EqualCoordinator equalCoordinator = new EqualCoordinator(singleObserver, this.prefetch, this.comparer);
        singleObserver.onSubscribe(equalCoordinator);
        equalCoordinator.subscribe(this.first, this.second);
    }
}
