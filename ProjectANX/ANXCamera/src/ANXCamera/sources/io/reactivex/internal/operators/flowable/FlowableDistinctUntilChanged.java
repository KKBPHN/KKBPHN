package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import org.reactivestreams.Subscriber;

public final class FlowableDistinctUntilChanged extends AbstractFlowableWithUpstream {
    final BiPredicate comparer;
    final Function keySelector;

    final class DistinctUntilChangedConditionalSubscriber extends BasicFuseableConditionalSubscriber {
        final BiPredicate comparer;
        boolean hasValue;
        final Function keySelector;
        Object last;

        DistinctUntilChangedConditionalSubscriber(ConditionalSubscriber conditionalSubscriber, Function function, BiPredicate biPredicate) {
            super(conditionalSubscriber);
            this.keySelector = function;
            this.comparer = biPredicate;
        }

        public void onNext(Object obj) {
            if (!tryOnNext(obj)) {
                this.s.request(1);
            }
        }

        @Nullable
        public Object poll() {
            while (true) {
                Object poll = this.qs.poll();
                if (poll == null) {
                    return null;
                }
                Object apply = this.keySelector.apply(poll);
                if (!this.hasValue) {
                    this.hasValue = true;
                    this.last = apply;
                    return poll;
                }
                boolean test = this.comparer.test(this.last, apply);
                this.last = apply;
                if (!test) {
                    return poll;
                }
                if (this.sourceMode != 1) {
                    this.s.request(1);
                }
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public boolean tryOnNext(Object obj) {
            if (this.done) {
                return false;
            }
            if (this.sourceMode != 0) {
                return this.actual.tryOnNext(obj);
            }
            try {
                Object apply = this.keySelector.apply(obj);
                if (this.hasValue) {
                    boolean test = this.comparer.test(this.last, apply);
                    this.last = apply;
                    if (test) {
                        return false;
                    }
                } else {
                    this.hasValue = true;
                    this.last = apply;
                }
                this.actual.onNext(obj);
                return true;
            } catch (Throwable th) {
                fail(th);
                return true;
            }
        }
    }

    final class DistinctUntilChangedSubscriber extends BasicFuseableSubscriber implements ConditionalSubscriber {
        final BiPredicate comparer;
        boolean hasValue;
        final Function keySelector;
        Object last;

        DistinctUntilChangedSubscriber(Subscriber subscriber, Function function, BiPredicate biPredicate) {
            super(subscriber);
            this.keySelector = function;
            this.comparer = biPredicate;
        }

        public void onNext(Object obj) {
            if (!tryOnNext(obj)) {
                this.s.request(1);
            }
        }

        @Nullable
        public Object poll() {
            while (true) {
                Object poll = this.qs.poll();
                if (poll == null) {
                    return null;
                }
                Object apply = this.keySelector.apply(poll);
                if (!this.hasValue) {
                    this.hasValue = true;
                    this.last = apply;
                    return poll;
                }
                boolean test = this.comparer.test(this.last, apply);
                this.last = apply;
                if (!test) {
                    return poll;
                }
                if (this.sourceMode != 1) {
                    this.s.request(1);
                }
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public boolean tryOnNext(Object obj) {
            if (this.done) {
                return false;
            }
            if (this.sourceMode == 0) {
                try {
                    Object apply = this.keySelector.apply(obj);
                    if (this.hasValue) {
                        boolean test = this.comparer.test(this.last, apply);
                        this.last = apply;
                        if (test) {
                            return false;
                        }
                    } else {
                        this.hasValue = true;
                        this.last = apply;
                    }
                } catch (Throwable th) {
                    fail(th);
                    return true;
                }
            }
            this.actual.onNext(obj);
            return true;
        }
    }

    public FlowableDistinctUntilChanged(Flowable flowable, Function function, BiPredicate biPredicate) {
        super(flowable);
        this.keySelector = function;
        this.comparer = biPredicate;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        FlowableSubscriber flowableSubscriber;
        Flowable flowable;
        if (subscriber instanceof ConditionalSubscriber) {
            ConditionalSubscriber conditionalSubscriber = (ConditionalSubscriber) subscriber;
            flowable = this.source;
            flowableSubscriber = new DistinctUntilChangedConditionalSubscriber(conditionalSubscriber, this.keySelector, this.comparer);
        } else {
            flowable = this.source;
            flowableSubscriber = new DistinctUntilChangedSubscriber(subscriber, this.keySelector, this.comparer);
        }
        flowable.subscribe(flowableSubscriber);
    }
}
