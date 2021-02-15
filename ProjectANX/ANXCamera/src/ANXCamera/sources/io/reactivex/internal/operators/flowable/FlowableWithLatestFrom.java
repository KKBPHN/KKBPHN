package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWithLatestFrom extends AbstractFlowableWithUpstream {
    final BiFunction combiner;
    final Publisher other;

    final class FlowableWithLatestSubscriber implements FlowableSubscriber {
        private final WithLatestFromSubscriber wlf;

        FlowableWithLatestSubscriber(WithLatestFromSubscriber withLatestFromSubscriber) {
            this.wlf = withLatestFromSubscriber;
        }

        public void onComplete() {
        }

        public void onError(Throwable th) {
            this.wlf.otherError(th);
        }

        public void onNext(Object obj) {
            this.wlf.lazySet(obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (this.wlf.setOther(subscription)) {
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    final class WithLatestFromSubscriber extends AtomicReference implements ConditionalSubscriber, Subscription {
        private static final long serialVersionUID = -312246233408980075L;
        final Subscriber actual;
        final BiFunction combiner;
        final AtomicReference other = new AtomicReference();
        final AtomicLong requested = new AtomicLong();
        final AtomicReference s = new AtomicReference();

        WithLatestFromSubscriber(Subscriber subscriber, BiFunction biFunction) {
            this.actual = subscriber;
            this.combiner = biFunction;
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.s);
            SubscriptionHelper.cancel(this.other);
        }

        public void onComplete() {
            SubscriptionHelper.cancel(this.other);
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            SubscriptionHelper.cancel(this.other);
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!tryOnNext(obj)) {
                ((Subscription) this.s.get()).request(1);
            }
        }

        public void onSubscribe(Subscription subscription) {
            SubscriptionHelper.deferredSetOnce(this.s, this.requested, subscription);
        }

        public void otherError(Throwable th) {
            SubscriptionHelper.cancel(this.s);
            this.actual.onError(th);
        }

        public void request(long j) {
            SubscriptionHelper.deferredRequest(this.s, this.requested, j);
        }

        public boolean setOther(Subscription subscription) {
            return SubscriptionHelper.setOnce(this.other, subscription);
        }

        public boolean tryOnNext(Object obj) {
            Object obj2 = get();
            if (obj2 != null) {
                try {
                    Object apply = this.combiner.apply(obj, obj2);
                    ObjectHelper.requireNonNull(apply, "The combiner returned a null value");
                    this.actual.onNext(apply);
                    return true;
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    this.actual.onError(th);
                }
            }
            return false;
        }
    }

    public FlowableWithLatestFrom(Flowable flowable, BiFunction biFunction, Publisher publisher) {
        super(flowable);
        this.combiner = biFunction;
        this.other = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        WithLatestFromSubscriber withLatestFromSubscriber = new WithLatestFromSubscriber(serializedSubscriber, this.combiner);
        serializedSubscriber.onSubscribe(withLatestFromSubscriber);
        this.other.subscribe(new FlowableWithLatestSubscriber(withLatestFromSubscriber));
        this.source.subscribe((FlowableSubscriber) withLatestFromSubscriber);
    }
}
