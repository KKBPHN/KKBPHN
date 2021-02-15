package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import org.reactivestreams.Subscriber;

public final class FlowableOnErrorReturn extends AbstractFlowableWithUpstream {
    final Function valueSupplier;

    final class OnErrorReturnSubscriber extends SinglePostCompleteSubscriber {
        private static final long serialVersionUID = -3740826063558713822L;
        final Function valueSupplier;

        OnErrorReturnSubscriber(Subscriber subscriber, Function function) {
            super(subscriber);
            this.valueSupplier = function;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            try {
                Object apply = this.valueSupplier.apply(th);
                ObjectHelper.requireNonNull(apply, "The valueSupplier returned a null value");
                complete(apply);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
            }
        }

        public void onNext(Object obj) {
            this.produced++;
            this.actual.onNext(obj);
        }
    }

    public FlowableOnErrorReturn(Flowable flowable, Function function) {
        super(flowable);
        this.valueSupplier = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new OnErrorReturnSubscriber(subscriber, this.valueSupplier));
    }
}
