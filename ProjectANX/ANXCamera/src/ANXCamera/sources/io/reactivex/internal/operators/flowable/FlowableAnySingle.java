package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscription;

public final class FlowableAnySingle extends Single implements FuseToFlowable {
    final Predicate predicate;
    final Flowable source;

    final class AnySubscriber implements FlowableSubscriber, Disposable {
        final SingleObserver actual;
        boolean done;
        final Predicate predicate;
        Subscription s;

        AnySubscriber(SingleObserver singleObserver, Predicate predicate2) {
            this.actual = singleObserver;
            this.predicate = predicate2;
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
                this.actual.onSuccess(Boolean.valueOf(false));
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
                try {
                    if (this.predicate.test(obj)) {
                        this.done = true;
                        this.s.cancel();
                        this.s = SubscriptionHelper.CANCELLED;
                        this.actual.onSuccess(Boolean.valueOf(true));
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.s.cancel();
                    this.s = SubscriptionHelper.CANCELLED;
                    onError(th);
                }
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

    public FlowableAnySingle(Flowable flowable, Predicate predicate2) {
        this.source = flowable;
        this.predicate = predicate2;
    }

    public Flowable fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableAny(this.source, this.predicate));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe((FlowableSubscriber) new AnySubscriber(singleObserver, this.predicate));
    }
}
