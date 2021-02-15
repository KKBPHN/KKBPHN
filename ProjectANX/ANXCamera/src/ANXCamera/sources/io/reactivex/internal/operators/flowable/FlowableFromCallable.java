package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;

public final class FlowableFromCallable extends Flowable implements Callable {
    final Callable callable;

    public FlowableFromCallable(Callable callable2) {
        this.callable = callable2;
    }

    public Object call() {
        Object call = this.callable.call();
        ObjectHelper.requireNonNull(call, "The callable returned a null value");
        return call;
    }

    public void subscribeActual(Subscriber subscriber) {
        DeferredScalarSubscription deferredScalarSubscription = new DeferredScalarSubscription(subscriber);
        subscriber.onSubscribe(deferredScalarSubscription);
        try {
            Object call = this.callable.call();
            ObjectHelper.requireNonNull(call, "The callable returned a null value");
            deferredScalarSubscription.complete(call);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            subscriber.onError(th);
        }
    }
}
