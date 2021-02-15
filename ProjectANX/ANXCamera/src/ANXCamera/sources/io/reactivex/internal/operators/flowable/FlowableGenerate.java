package io.reactivex.internal.operators.flowable;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableGenerate extends Flowable {
    final Consumer disposeState;
    final BiFunction generator;
    final Callable stateSupplier;

    final class GeneratorSubscription extends AtomicLong implements Emitter, Subscription {
        private static final long serialVersionUID = 7565982551505011832L;
        final Subscriber actual;
        volatile boolean cancelled;
        final Consumer disposeState;
        final BiFunction generator;
        boolean hasNext;
        Object state;
        boolean terminate;

        GeneratorSubscription(Subscriber subscriber, BiFunction biFunction, Consumer consumer, Object obj) {
            this.actual = subscriber;
            this.generator = biFunction;
            this.disposeState = consumer;
            this.state = obj;
        }

        private void dispose(Object obj) {
            try {
                this.disposeState.accept(obj);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                if (BackpressureHelper.add(this, 1) == 0) {
                    Object obj = this.state;
                    this.state = null;
                    dispose(obj);
                }
            }
        }

        public void onComplete() {
            if (!this.terminate) {
                this.terminate = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.terminate) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (th == null) {
                th = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
            }
            this.terminate = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            Throwable nullPointerException;
            if (!this.terminate) {
                if (this.hasNext) {
                    nullPointerException = new IllegalStateException("onNext already called in this generate turn");
                } else if (obj == null) {
                    nullPointerException = new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
                } else {
                    this.hasNext = true;
                    this.actual.onNext(obj);
                    return;
                }
                onError(nullPointerException);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j) && BackpressureHelper.add(this, j) == 0) {
                Object obj = this.state;
                BiFunction biFunction = this.generator;
                long j2 = j;
                loop0:
                do {
                    long j3 = 0;
                    while (true) {
                        if (j3 == j2) {
                            j2 = get();
                            if (j3 == j2) {
                                this.state = obj;
                                j2 = addAndGet(-j3);
                            }
                        } else if (this.cancelled) {
                            break loop0;
                        } else {
                            this.hasNext = false;
                            try {
                                obj = biFunction.apply(obj, this);
                                if (this.terminate) {
                                    this.cancelled = true;
                                    break loop0;
                                }
                                j3++;
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                this.cancelled = true;
                                this.state = null;
                                onError(th);
                            }
                        }
                    }
                    this.state = null;
                    dispose(obj);
                    return;
                } while (j2 != 0);
            }
        }
    }

    public FlowableGenerate(Callable callable, BiFunction biFunction, Consumer consumer) {
        this.stateSupplier = callable;
        this.generator = biFunction;
        this.disposeState = consumer;
    }

    public void subscribeActual(Subscriber subscriber) {
        try {
            subscriber.onSubscribe(new GeneratorSubscription(subscriber, this.generator, this.disposeState, this.stateSupplier.call()));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
