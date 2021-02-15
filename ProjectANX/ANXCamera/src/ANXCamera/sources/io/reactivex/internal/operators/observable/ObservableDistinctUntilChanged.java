package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Function;
import io.reactivex.internal.observers.BasicFuseableObserver;

public final class ObservableDistinctUntilChanged extends AbstractObservableWithUpstream {
    final BiPredicate comparer;
    final Function keySelector;

    final class DistinctUntilChangedObserver extends BasicFuseableObserver {
        final BiPredicate comparer;
        boolean hasValue;
        final Function keySelector;
        Object last;

        DistinctUntilChangedObserver(Observer observer, Function function, BiPredicate biPredicate) {
            super(observer);
            this.keySelector = function;
            this.comparer = biPredicate;
        }

        public void onNext(Object obj) {
            if (!this.done) {
                if (this.sourceMode == 0) {
                    try {
                        Object apply = this.keySelector.apply(obj);
                        if (this.hasValue) {
                            boolean test = this.comparer.test(this.last, apply);
                            this.last = apply;
                            if (test) {
                                return;
                            }
                        } else {
                            this.hasValue = true;
                            this.last = apply;
                        }
                    } catch (Throwable th) {
                        fail(th);
                        return;
                    }
                }
                this.actual.onNext(obj);
            }
        }

        @Nullable
        public Object poll() {
            Object poll;
            boolean test;
            do {
                poll = this.qs.poll();
                if (poll == null) {
                    return null;
                }
                Object apply = this.keySelector.apply(poll);
                if (!this.hasValue) {
                    this.hasValue = true;
                    this.last = apply;
                    return poll;
                }
                test = this.comparer.test(this.last, apply);
                this.last = apply;
            } while (test);
            return poll;
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }
    }

    public ObservableDistinctUntilChanged(ObservableSource observableSource, Function function, BiPredicate biPredicate) {
        super(observableSource);
        this.keySelector = function;
        this.comparer = biPredicate;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new DistinctUntilChangedObserver(observer, this.keySelector, this.comparer));
    }
}
