package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.ScalarSubscription;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableScalarXMap {

    final class ScalarXMapFlowable extends Flowable {
        final Function mapper;
        final Object value;

        ScalarXMapFlowable(Object obj, Function function) {
            this.value = obj;
            this.mapper = function;
        }

        public void subscribeActual(Subscriber subscriber) {
            try {
                Object apply = this.mapper.apply(this.value);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null Publisher");
                Publisher publisher = (Publisher) apply;
                if (publisher instanceof Callable) {
                    try {
                        Object call = ((Callable) publisher).call();
                        if (call == null) {
                            EmptySubscription.complete(subscriber);
                            return;
                        }
                        subscriber.onSubscribe(new ScalarSubscription(subscriber, call));
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        EmptySubscription.error(th, subscriber);
                    }
                } else {
                    publisher.subscribe(subscriber);
                }
            } catch (Throwable th2) {
                EmptySubscription.error(th2, subscriber);
            }
        }
    }

    private FlowableScalarXMap() {
        throw new IllegalStateException("No instances!");
    }

    public static Flowable scalarXMap(Object obj, Function function) {
        return RxJavaPlugins.onAssembly((Flowable) new ScalarXMapFlowable(obj, function));
    }

    public static boolean tryScalarXMapSubscribe(Publisher publisher, Subscriber subscriber, Function function) {
        if (!(publisher instanceof Callable)) {
            return false;
        }
        try {
            Object call = ((Callable) publisher).call();
            if (call == null) {
                EmptySubscription.complete(subscriber);
                return true;
            }
            Object apply = function.apply(call);
            ObjectHelper.requireNonNull(apply, "The mapper returned a null Publisher");
            Publisher publisher2 = (Publisher) apply;
            if (publisher2 instanceof Callable) {
                Object call2 = ((Callable) publisher2).call();
                if (call2 == null) {
                    EmptySubscription.complete(subscriber);
                    return true;
                }
                subscriber.onSubscribe(new ScalarSubscription(subscriber, call2));
            } else {
                publisher2.subscribe(subscriber);
            }
            return true;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
            return true;
        }
    }
}
