package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscription;

public final class FlowableSingleMaybe extends Maybe implements FuseToFlowable {
    final Flowable source;

    final class SingleElementSubscriber implements FlowableSubscriber, Disposable {
        final MaybeObserver actual;
        boolean done;
        Subscription s;
        Object value;

        SingleElementSubscriber(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
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
                MaybeObserver maybeObserver = this.actual;
                if (obj == null) {
                    maybeObserver.onComplete();
                } else {
                    maybeObserver.onSuccess(obj);
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

    public FlowableSingleMaybe(Flowable flowable) {
        this.source = flowable;
    }

    public Flowable fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableSingle(this.source, null));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe((FlowableSubscriber) new SingleElementSubscriber(maybeObserver));
    }
}
