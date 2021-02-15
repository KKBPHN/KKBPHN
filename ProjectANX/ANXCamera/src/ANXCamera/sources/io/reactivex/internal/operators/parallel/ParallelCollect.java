package io.reactivex.internal.operators.parallel;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscribers.DeferredScalarSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelCollect extends ParallelFlowable {
    final BiConsumer collector;
    final Callable initialCollection;
    final ParallelFlowable source;

    final class ParallelCollectSubscriber extends DeferredScalarSubscriber {
        private static final long serialVersionUID = -4767392946044436228L;
        Object collection;
        final BiConsumer collector;
        boolean done;

        ParallelCollectSubscriber(Subscriber subscriber, Object obj, BiConsumer biConsumer) {
            super(subscriber);
            this.collection = obj;
            this.collector = biConsumer;
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Object obj = this.collection;
                this.collection = null;
                complete(obj);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.collection = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                try {
                    this.collector.accept(this.collection, obj);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
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

    public ParallelCollect(ParallelFlowable parallelFlowable, Callable callable, BiConsumer biConsumer) {
        this.source = parallelFlowable;
        this.initialCollection = callable;
        this.collector = biConsumer;
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    /* access modifiers changed from: 0000 */
    public void reportError(Subscriber[] subscriberArr, Throwable th) {
        for (Subscriber error : subscriberArr) {
            EmptySubscription.error(th, error);
        }
    }

    public void subscribe(Subscriber[] subscriberArr) {
        if (validate(subscriberArr)) {
            int length = subscriberArr.length;
            Subscriber[] subscriberArr2 = new Subscriber[length];
            int i = 0;
            while (i < length) {
                try {
                    Object call = this.initialCollection.call();
                    ObjectHelper.requireNonNull(call, "The initialSupplier returned a null value");
                    subscriberArr2[i] = new ParallelCollectSubscriber(subscriberArr[i], call, this.collector);
                    i++;
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    reportError(subscriberArr, th);
                    return;
                }
            }
            this.source.subscribe(subscriberArr2);
        }
    }
}
