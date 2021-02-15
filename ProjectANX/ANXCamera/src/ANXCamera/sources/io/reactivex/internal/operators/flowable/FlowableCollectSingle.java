package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscription;

public final class FlowableCollectSingle extends Single implements FuseToFlowable {
    final BiConsumer collector;
    final Callable initialSupplier;
    final Flowable source;

    final class CollectSubscriber implements FlowableSubscriber, Disposable {
        final SingleObserver actual;
        final BiConsumer collector;
        boolean done;
        Subscription s;
        final Object u;

        CollectSubscriber(SingleObserver singleObserver, Object obj, BiConsumer biConsumer) {
            this.actual = singleObserver;
            this.collector = biConsumer;
            this.u = obj;
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
                this.actual.onSuccess(this.u);
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
                    this.collector.accept(this.u, obj);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.s.cancel();
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

    public FlowableCollectSingle(Flowable flowable, Callable callable, BiConsumer biConsumer) {
        this.source = flowable;
        this.initialSupplier = callable;
        this.collector = biConsumer;
    }

    public Flowable fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableCollect(this.source, this.initialSupplier, this.collector));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        try {
            Object call = this.initialSupplier.call();
            ObjectHelper.requireNonNull(call, "The initialSupplier returned a null value");
            this.source.subscribe((FlowableSubscriber) new CollectSubscriber(singleObserver, call, this.collector));
        } catch (Throwable th) {
            EmptyDisposable.error(th, singleObserver);
        }
    }
}
