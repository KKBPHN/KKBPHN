package io.reactivex.internal.operators.flowable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscription;

public final class FlowableIgnoreElementsCompletable extends Completable implements FuseToFlowable {
    final Flowable source;

    final class IgnoreElementsSubscriber implements FlowableSubscriber, Disposable {
        final CompletableObserver actual;
        Subscription s;

        IgnoreElementsSubscriber(CompletableObserver completableObserver) {
            this.actual = completableObserver;
        }

        public void dispose() {
            this.s.cancel();
            this.s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.s == SubscriptionHelper.CANCELLED;
        }

        public void onComplete() {
            this.s = SubscriptionHelper.CANCELLED;
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.s = SubscriptionHelper.CANCELLED;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public FlowableIgnoreElementsCompletable(Flowable flowable) {
        this.source = flowable;
    }

    public Flowable fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableIgnoreElements(this.source));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe((FlowableSubscriber) new IgnoreElementsSubscriber(completableObserver));
    }
}
