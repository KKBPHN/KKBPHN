package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableUsing extends Flowable {
    final Consumer disposer;
    final boolean eager;
    final Callable resourceSupplier;
    final Function sourceSupplier;

    final class UsingSubscriber extends AtomicBoolean implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = 5904473792286235046L;
        final Subscriber actual;
        final Consumer disposer;
        final boolean eager;
        final Object resource;
        Subscription s;

        UsingSubscriber(Subscriber subscriber, Object obj, Consumer consumer, boolean z) {
            this.actual = subscriber;
            this.resource = obj;
            this.disposer = consumer;
            this.eager = z;
        }

        public void cancel() {
            disposeAfter();
            this.s.cancel();
        }

        /* access modifiers changed from: 0000 */
        public void disposeAfter() {
            if (compareAndSet(false, true)) {
                try {
                    this.disposer.accept(this.resource);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }

        public void onComplete() {
            if (this.eager) {
                if (compareAndSet(false, true)) {
                    try {
                        this.disposer.accept(this.resource);
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.actual.onError(th);
                        return;
                    }
                }
                this.s.cancel();
                this.actual.onComplete();
            } else {
                this.actual.onComplete();
                this.s.cancel();
                disposeAfter();
            }
        }

        public void onError(Throwable th) {
            if (this.eager) {
                Throwable th2 = null;
                if (compareAndSet(false, true)) {
                    try {
                        this.disposer.accept(this.resource);
                    } catch (Throwable th3) {
                        th2 = th3;
                        Exceptions.throwIfFatal(th2);
                    }
                }
                this.s.cancel();
                Subscriber subscriber = this.actual;
                if (th2 != null) {
                    subscriber.onError(new CompositeException(th, th2));
                    return;
                }
                subscriber.onError(th);
                return;
            }
            this.actual.onError(th);
            this.s.cancel();
            disposeAfter();
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            this.s.request(j);
        }
    }

    public FlowableUsing(Callable callable, Function function, Consumer consumer, boolean z) {
        this.resourceSupplier = callable;
        this.sourceSupplier = function;
        this.disposer = consumer;
        this.eager = z;
    }

    public void subscribeActual(Subscriber subscriber) {
        try {
            Object call = this.resourceSupplier.call();
            try {
                Object apply = this.sourceSupplier.apply(call);
                ObjectHelper.requireNonNull(apply, "The sourceSupplier returned a null Publisher");
                ((Publisher) apply).subscribe(new UsingSubscriber(subscriber, call, this.disposer, this.eager));
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptySubscription.error(new CompositeException(th, th), subscriber);
            }
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            EmptySubscription.error(th2, subscriber);
        }
    }
}
