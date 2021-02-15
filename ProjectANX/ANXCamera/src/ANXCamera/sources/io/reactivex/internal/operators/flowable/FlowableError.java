package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;

public final class FlowableError extends Flowable {
    final Callable errorSupplier;

    public FlowableError(Callable callable) {
        this.errorSupplier = callable;
    }

    public void subscribeActual(Subscriber subscriber) {
        try {
            Object call = this.errorSupplier.call();
            ObjectHelper.requireNonNull(call, "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
            th = (Throwable) call;
        } catch (Throwable th) {
            th = th;
            Exceptions.throwIfFatal(th);
        }
        EmptySubscription.error(th, subscriber);
    }
}
