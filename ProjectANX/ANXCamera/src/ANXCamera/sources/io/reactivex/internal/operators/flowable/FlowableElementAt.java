package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableElementAt extends AbstractFlowableWithUpstream {
    final Object defaultValue;
    final boolean errorOnFewer;
    final long index;

    final class ElementAtSubscriber extends DeferredScalarSubscription implements FlowableSubscriber {
        private static final long serialVersionUID = 4066607327284737757L;
        long count;
        final Object defaultValue;
        boolean done;
        final boolean errorOnFewer;
        final long index;
        Subscription s;

        ElementAtSubscriber(Subscriber subscriber, long j, Object obj, boolean z) {
            super(subscriber);
            this.index = j;
            this.defaultValue = obj;
            this.errorOnFewer = z;
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Object obj = this.defaultValue;
                if (obj == null) {
                    boolean z = this.errorOnFewer;
                    Subscriber subscriber = this.actual;
                    if (z) {
                        subscriber.onError(new NoSuchElementException());
                    } else {
                        subscriber.onComplete();
                    }
                } else {
                    complete(obj);
                }
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
                long j = this.count;
                if (j == this.index) {
                    this.done = true;
                    this.s.cancel();
                    complete(obj);
                    return;
                }
                this.count = j + 1;
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public FlowableElementAt(Flowable flowable, long j, Object obj, boolean z) {
        super(flowable);
        this.index = j;
        this.defaultValue = obj;
        this.errorOnFewer = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        Flowable flowable = this.source;
        ElementAtSubscriber elementAtSubscriber = new ElementAtSubscriber(subscriber, this.index, this.defaultValue, this.errorOnFewer);
        flowable.subscribe((FlowableSubscriber) elementAtSubscriber);
    }
}
