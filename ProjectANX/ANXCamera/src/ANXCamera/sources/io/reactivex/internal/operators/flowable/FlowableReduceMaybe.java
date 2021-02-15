package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class FlowableReduceMaybe extends Maybe implements HasUpstreamPublisher, FuseToFlowable {
    final BiFunction reducer;
    final Flowable source;

    final class ReduceSubscriber implements FlowableSubscriber, Disposable {
        final MaybeObserver actual;
        boolean done;
        final BiFunction reducer;
        Subscription s;
        Object value;

        ReduceSubscriber(MaybeObserver maybeObserver, BiFunction biFunction) {
            this.actual = maybeObserver;
            this.reducer = biFunction;
        }

        public void dispose() {
            this.s.cancel();
            this.done = true;
        }

        public boolean isDisposed() {
            return this.done;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Object obj = this.value;
                MaybeObserver maybeObserver = this.actual;
                if (obj != null) {
                    maybeObserver.onSuccess(obj);
                } else {
                    maybeObserver.onComplete();
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                Object obj2 = this.value;
                if (obj2 == null) {
                    this.value = obj;
                } else {
                    try {
                        Object apply = this.reducer.apply(obj2, obj);
                        ObjectHelper.requireNonNull(apply, "The reducer returned a null value");
                        this.value = apply;
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.s.cancel();
                        onError(th);
                    }
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

    public FlowableReduceMaybe(Flowable flowable, BiFunction biFunction) {
        this.source = flowable;
        this.reducer = biFunction;
    }

    public Flowable fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableReduce(this.source, this.reducer));
    }

    public Publisher source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe((FlowableSubscriber) new ReduceSubscriber(maybeObserver, this.reducer));
    }
}
