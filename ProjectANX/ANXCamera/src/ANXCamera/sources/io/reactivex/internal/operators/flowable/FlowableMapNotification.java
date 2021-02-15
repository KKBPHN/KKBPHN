package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;

public final class FlowableMapNotification extends AbstractFlowableWithUpstream {
    final Callable onCompleteSupplier;
    final Function onErrorMapper;
    final Function onNextMapper;

    final class MapNotificationSubscriber extends SinglePostCompleteSubscriber {
        private static final long serialVersionUID = 2757120512858778108L;
        final Callable onCompleteSupplier;
        final Function onErrorMapper;
        final Function onNextMapper;

        MapNotificationSubscriber(Subscriber subscriber, Function function, Function function2, Callable callable) {
            super(subscriber);
            this.onNextMapper = function;
            this.onErrorMapper = function2;
            this.onCompleteSupplier = callable;
        }

        public void onComplete() {
            try {
                Object call = this.onCompleteSupplier.call();
                ObjectHelper.requireNonNull(call, "The onComplete publisher returned is null");
                complete(call);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }

        public void onError(Throwable th) {
            try {
                Object apply = this.onErrorMapper.apply(th);
                ObjectHelper.requireNonNull(apply, "The onError publisher returned is null");
                complete(apply);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
            }
        }

        public void onNext(Object obj) {
            try {
                Object apply = this.onNextMapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The onNext publisher returned is null");
                this.produced++;
                this.actual.onNext(apply);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }
    }

    public FlowableMapNotification(Flowable flowable, Function function, Function function2, Callable callable) {
        super(flowable);
        this.onNextMapper = function;
        this.onErrorMapper = function2;
        this.onCompleteSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new MapNotificationSubscriber(subscriber, this.onNextMapper, this.onErrorMapper, this.onCompleteSupplier));
    }
}
