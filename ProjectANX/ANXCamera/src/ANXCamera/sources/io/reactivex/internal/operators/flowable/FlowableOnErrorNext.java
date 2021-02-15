package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableOnErrorNext extends AbstractFlowableWithUpstream {
    final boolean allowFatal;
    final Function nextSupplier;

    final class OnErrorNextSubscriber implements FlowableSubscriber {
        final Subscriber actual;
        final boolean allowFatal;
        final SubscriptionArbiter arbiter = new SubscriptionArbiter();
        boolean done;
        final Function nextSupplier;
        boolean once;

        OnErrorNextSubscriber(Subscriber subscriber, Function function, boolean z) {
            this.actual = subscriber;
            this.nextSupplier = function;
            this.allowFatal = z;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.once = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (!this.once) {
                this.once = true;
                if (!this.allowFatal || (th instanceof Exception)) {
                    try {
                        Publisher publisher = (Publisher) this.nextSupplier.apply(th);
                        if (publisher == null) {
                            NullPointerException nullPointerException = new NullPointerException("Publisher is null");
                            nullPointerException.initCause(th);
                            this.actual.onError(nullPointerException);
                            return;
                        }
                        publisher.subscribe(this);
                    } catch (Throwable th2) {
                        Exceptions.throwIfFatal(th2);
                        this.actual.onError(new CompositeException(th, th2));
                    }
                } else {
                    this.actual.onError(th);
                }
            } else if (this.done) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }

        public void onNext(Object obj) {
            if (!this.done) {
                this.actual.onNext(obj);
                if (!this.once) {
                    this.arbiter.produced(1);
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            this.arbiter.setSubscription(subscription);
        }
    }

    public FlowableOnErrorNext(Flowable flowable, Function function, boolean z) {
        super(flowable);
        this.nextSupplier = function;
        this.allowFatal = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        OnErrorNextSubscriber onErrorNextSubscriber = new OnErrorNextSubscriber(subscriber, this.nextSupplier, this.allowFatal);
        subscriber.onSubscribe(onErrorNextSubscriber.arbiter);
        this.source.subscribe((FlowableSubscriber) onErrorNextSubscriber);
    }
}
