package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableScan extends AbstractFlowableWithUpstream {
    final BiFunction accumulator;

    final class ScanSubscriber implements FlowableSubscriber, Subscription {
        final BiFunction accumulator;
        final Subscriber actual;
        boolean done;
        Subscription s;
        Object value;

        ScanSubscriber(Subscriber subscriber, BiFunction biFunction) {
            this.actual = subscriber;
            this.accumulator = biFunction;
        }

        public void cancel() {
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                Subscriber subscriber = this.actual;
                Object obj2 = this.value;
                if (obj2 != null) {
                    try {
                        obj = this.accumulator.apply(obj2, obj);
                        ObjectHelper.requireNonNull(obj, "The value returned by the accumulator is null");
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.s.cancel();
                        onError(th);
                        return;
                    }
                }
                this.value = obj;
                subscriber.onNext(obj);
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            this.s.request(j);
        }
    }

    public FlowableScan(Flowable flowable, BiFunction biFunction) {
        super(flowable);
        this.accumulator = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new ScanSubscriber(subscriber, this.accumulator));
    }
}
