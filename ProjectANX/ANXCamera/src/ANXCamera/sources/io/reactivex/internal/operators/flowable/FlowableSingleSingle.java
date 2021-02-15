package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import org.reactivestreams.Subscription;

public final class FlowableSingleSingle extends Single implements FuseToFlowable {
    final Object defaultValue;
    final Flowable source;

    final class SingleElementSubscriber implements FlowableSubscriber, Disposable {
        final SingleObserver actual;
        final Object defaultValue;
        boolean done;
        Subscription s;
        Object value;

        SingleElementSubscriber(SingleObserver singleObserver, Object obj) {
            this.actual = singleObserver;
            this.defaultValue = obj;
        }

        public void dispose() {
            this.s.cancel();
            this.s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.s == SubscriptionHelper.CANCELLED;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.s = SubscriptionHelper.CANCELLED;
                Object obj = this.value;
                this.value = null;
                if (obj == null) {
                    obj = this.defaultValue;
                }
                SingleObserver singleObserver = this.actual;
                if (obj != null) {
                    singleObserver.onSuccess(obj);
                } else {
                    singleObserver.onError(new NoSuchElementException());
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.s = SubscriptionHelper.CANCELLED;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                if (this.value != null) {
                    this.done = true;
                    this.s.cancel();
                    this.s = SubscriptionHelper.CANCELLED;
                    this.actual.onError(new IllegalArgumentException("Sequence contains more than one element!"));
                    return;
                }
                this.value = obj;
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

    public FlowableSingleSingle(Flowable flowable, Object obj) {
        this.source = flowable;
        this.defaultValue = obj;
    }

    public Flowable fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableSingle(this.source, this.defaultValue));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe((FlowableSubscriber) new SingleElementSubscriber(singleObserver, this.defaultValue));
    }
}
