package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableDefer extends Flowable {
    final Callable supplier;

    public FlowableDefer(Callable callable) {
        this.supplier = callable;
    }

    public void subscribeActual(Subscriber subscriber) {
        try {
            Object call = this.supplier.call();
            ObjectHelper.requireNonNull(call, "The publisher supplied is null");
            ((Publisher) call).subscribe(subscriber);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
