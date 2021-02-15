package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import org.reactivestreams.Subscriber;

public final class FlowableMap extends AbstractFlowableWithUpstream {
    final Function mapper;

    final class MapConditionalSubscriber extends BasicFuseableConditionalSubscriber {
        final Function mapper;

        MapConditionalSubscriber(ConditionalSubscriber conditionalSubscriber, Function function) {
            super(conditionalSubscriber);
            this.mapper = function;
        }

        public void onNext(Object obj) {
            Object apply;
            ConditionalSubscriber conditionalSubscriber;
            if (!this.done) {
                if (this.sourceMode != 0) {
                    conditionalSubscriber = this.actual;
                    apply = null;
                } else {
                    try {
                        apply = this.mapper.apply(obj);
                        ObjectHelper.requireNonNull(apply, "The mapper function returned a null value.");
                        conditionalSubscriber = this.actual;
                    } catch (Throwable th) {
                        fail(th);
                        return;
                    }
                }
                conditionalSubscriber.onNext(apply);
            }
        }

        @Nullable
        public Object poll() {
            Object poll = this.qs.poll();
            if (poll == null) {
                return null;
            }
            Object apply = this.mapper.apply(poll);
            ObjectHelper.requireNonNull(apply, "The mapper function returned a null value.");
            return apply;
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public boolean tryOnNext(Object obj) {
            if (this.done) {
                return false;
            }
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper function returned a null value.");
                return this.actual.tryOnNext(apply);
            } catch (Throwable th) {
                fail(th);
                return true;
            }
        }
    }

    final class MapSubscriber extends BasicFuseableSubscriber {
        final Function mapper;

        MapSubscriber(Subscriber subscriber, Function function) {
            super(subscriber);
            this.mapper = function;
        }

        public void onNext(Object obj) {
            Object apply;
            Subscriber subscriber;
            if (!this.done) {
                if (this.sourceMode != 0) {
                    subscriber = this.actual;
                    apply = null;
                } else {
                    try {
                        apply = this.mapper.apply(obj);
                        ObjectHelper.requireNonNull(apply, "The mapper function returned a null value.");
                        subscriber = this.actual;
                    } catch (Throwable th) {
                        fail(th);
                        return;
                    }
                }
                subscriber.onNext(apply);
            }
        }

        @Nullable
        public Object poll() {
            Object poll = this.qs.poll();
            if (poll == null) {
                return null;
            }
            Object apply = this.mapper.apply(poll);
            ObjectHelper.requireNonNull(apply, "The mapper function returned a null value.");
            return apply;
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }
    }

    public FlowableMap(Flowable flowable, Function function) {
        super(flowable);
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        FlowableSubscriber flowableSubscriber;
        Flowable flowable;
        if (subscriber instanceof ConditionalSubscriber) {
            flowable = this.source;
            flowableSubscriber = new MapConditionalSubscriber((ConditionalSubscriber) subscriber, this.mapper);
        } else {
            flowable = this.source;
            flowableSubscriber = new MapSubscriber(subscriber, this.mapper);
        }
        flowable.subscribe(flowableSubscriber);
    }
}
