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

public final class FlowableElementAtSingle extends Single implements FuseToFlowable {
    final Object defaultValue;
    final long index;
    final Flowable source;

    final class ElementAtSubscriber implements FlowableSubscriber, Disposable {
        final SingleObserver actual;
        long count;
        final Object defaultValue;
        boolean done;
        final long index;
        Subscription s;

        ElementAtSubscriber(SingleObserver singleObserver, long j, Object obj) {
            this.actual = singleObserver;
            this.index = j;
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
            this.s = SubscriptionHelper.CANCELLED;
            if (!this.done) {
                this.done = true;
                Object obj = this.defaultValue;
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
                long j = this.count;
                if (j == this.index) {
                    this.done = true;
                    this.s.cancel();
                    this.s = SubscriptionHelper.CANCELLED;
                    this.actual.onSuccess(obj);
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

    public FlowableElementAtSingle(Flowable flowable, long j, Object obj) {
        this.source = flowable;
        this.index = j;
        this.defaultValue = obj;
    }

    public Flowable fuseToFlowable() {
        FlowableElementAt flowableElementAt = new FlowableElementAt(this.source, this.index, this.defaultValue, true);
        return RxJavaPlugins.onAssembly((Flowable) flowableElementAt);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe((FlowableSubscriber) new ElementAtSubscriber(singleObserver, this.index, this.defaultValue));
    }
}
