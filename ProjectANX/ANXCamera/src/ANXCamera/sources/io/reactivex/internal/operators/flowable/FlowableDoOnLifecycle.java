package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.LongConsumer;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDoOnLifecycle extends AbstractFlowableWithUpstream {
    private final Action onCancel;
    private final LongConsumer onRequest;
    private final Consumer onSubscribe;

    final class SubscriptionLambdaSubscriber implements FlowableSubscriber, Subscription {
        final Subscriber actual;
        final Action onCancel;
        final LongConsumer onRequest;
        final Consumer onSubscribe;
        Subscription s;

        SubscriptionLambdaSubscriber(Subscriber subscriber, Consumer consumer, LongConsumer longConsumer, Action action) {
            this.actual = subscriber;
            this.onSubscribe = consumer;
            this.onCancel = action;
            this.onRequest = longConsumer;
        }

        public void cancel() {
            try {
                this.onCancel.run();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
            this.s.cancel();
        }

        public void onComplete() {
            if (this.s != SubscriptionHelper.CANCELLED) {
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.s != SubscriptionHelper.CANCELLED) {
                this.actual.onError(th);
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            try {
                this.onSubscribe.accept(subscription);
                if (SubscriptionHelper.validate(this.s, subscription)) {
                    this.s = subscription;
                    this.actual.onSubscribe(this);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                subscription.cancel();
                this.s = SubscriptionHelper.CANCELLED;
                EmptySubscription.error(th, this.actual);
            }
        }

        public void request(long j) {
            try {
                this.onRequest.accept(j);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
            this.s.request(j);
        }
    }

    public FlowableDoOnLifecycle(Flowable flowable, Consumer consumer, LongConsumer longConsumer, Action action) {
        super(flowable);
        this.onSubscribe = consumer;
        this.onRequest = longConsumer;
        this.onCancel = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new SubscriptionLambdaSubscriber(subscriber, this.onSubscribe, this.onRequest, this.onCancel));
    }
}
