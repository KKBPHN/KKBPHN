package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.ArrayListSupplier;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscription;

public final class FlowableToListSingle extends Single implements FuseToFlowable {
    final Callable collectionSupplier;
    final Flowable source;

    final class ToListSubscriber implements FlowableSubscriber, Disposable {
        final SingleObserver actual;
        Subscription s;
        Collection value;

        ToListSubscriber(SingleObserver singleObserver, Collection collection) {
            this.actual = singleObserver;
            this.value = collection;
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
            this.actual.onSuccess(this.value);
        }

        public void onError(Throwable th) {
            this.value = null;
            this.s = SubscriptionHelper.CANCELLED;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.value.add(obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public FlowableToListSingle(Flowable flowable) {
        this(flowable, ArrayListSupplier.asCallable());
    }

    public FlowableToListSingle(Flowable flowable, Callable callable) {
        this.source = flowable;
        this.collectionSupplier = callable;
    }

    public Flowable fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableToList(this.source, this.collectionSupplier));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        try {
            Object call = this.collectionSupplier.call();
            ObjectHelper.requireNonNull(call, "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
            this.source.subscribe((FlowableSubscriber) new ToListSubscriber(singleObserver, (Collection) call));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, singleObserver);
        }
    }
}
