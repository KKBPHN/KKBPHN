package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;

public final class FlowableDistinct extends AbstractFlowableWithUpstream {
    final Callable collectionSupplier;
    final Function keySelector;

    final class DistinctSubscriber extends BasicFuseableSubscriber {
        final Collection collection;
        final Function keySelector;

        DistinctSubscriber(Subscriber subscriber, Function function, Collection collection2) {
            super(subscriber);
            this.keySelector = function;
            this.collection = collection2;
        }

        public void clear() {
            this.collection.clear();
            super.clear();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.collection.clear();
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.collection.clear();
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            Subscriber subscriber;
            if (!this.done) {
                if (this.sourceMode == 0) {
                    try {
                        Object apply = this.keySelector.apply(obj);
                        ObjectHelper.requireNonNull(apply, "The keySelector returned a null key");
                        if (this.collection.add(apply)) {
                            subscriber = this.actual;
                        } else {
                            this.s.request(1);
                        }
                    } catch (Throwable th) {
                        fail(th);
                        return;
                    }
                } else {
                    subscriber = this.actual;
                    obj = null;
                }
                subscriber.onNext(obj);
            }
        }

        @Nullable
        public Object poll() {
            Object poll;
            while (true) {
                poll = this.qs.poll();
                if (poll == null) {
                    break;
                }
                Collection collection2 = this.collection;
                Object apply = this.keySelector.apply(poll);
                ObjectHelper.requireNonNull(apply, "The keySelector returned a null key");
                if (collection2.add(apply)) {
                    break;
                } else if (this.sourceMode == 2) {
                    this.s.request(1);
                }
            }
            return poll;
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }
    }

    public FlowableDistinct(Flowable flowable, Function function, Callable callable) {
        super(flowable);
        this.keySelector = function;
        this.collectionSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        try {
            Object call = this.collectionSupplier.call();
            ObjectHelper.requireNonNull(call, "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
            this.source.subscribe((FlowableSubscriber) new DistinctSubscriber(subscriber, this.keySelector, (Collection) call));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
