package io.reactivex.internal.operators.parallel;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscribers.DeferredScalarSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelReduce extends ParallelFlowable {
    final Callable initialSupplier;
    final BiFunction reducer;
    final ParallelFlowable source;

    final class ParallelReduceSubscriber extends DeferredScalarSubscriber {
        private static final long serialVersionUID = 8200530050639449080L;
        Object accumulator;
        boolean done;
        final BiFunction reducer;

        ParallelReduceSubscriber(Subscriber subscriber, Object obj, BiFunction biFunction) {
            super(subscriber);
            this.accumulator = obj;
            this.reducer = biFunction;
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Object obj = this.accumulator;
                this.accumulator = null;
                complete(obj);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.accumulator = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                try {
                    Object apply = this.reducer.apply(this.accumulator, obj);
                    ObjectHelper.requireNonNull(apply, "The reducer returned a null value");
                    this.accumulator = apply;
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

    public ParallelReduce(ParallelFlowable parallelFlowable, Callable callable, BiFunction biFunction) {
        this.source = parallelFlowable;
        this.initialSupplier = callable;
        this.reducer = biFunction;
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
                    Object call = this.initialSupplier.call();
                    ObjectHelper.requireNonNull(call, "The initialSupplier returned a null value");
                    subscriberArr2[i] = new ParallelReduceSubscriber(subscriberArr[i], call, this.reducer);
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
