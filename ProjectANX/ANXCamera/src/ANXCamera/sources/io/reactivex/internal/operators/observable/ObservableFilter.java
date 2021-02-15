package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.observers.BasicFuseableObserver;

public final class ObservableFilter extends AbstractObservableWithUpstream {
    final Predicate predicate;

    final class FilterObserver extends BasicFuseableObserver {
        final Predicate filter;

        FilterObserver(Observer observer, Predicate predicate) {
            super(observer);
            this.filter = predicate;
        }

        public void onNext(Object obj) {
            Observer observer;
            if (this.sourceMode == 0) {
                try {
                    if (this.filter.test(obj)) {
                        observer = this.actual;
                    }
                } catch (Throwable th) {
                    fail(th);
                    return;
                }
            } else {
                observer = this.actual;
                obj = null;
            }
            observer.onNext(obj);
        }

        @Nullable
        public Object poll() {
            Object poll;
            do {
                poll = this.qs.poll();
                if (poll == null) {
                    break;
                }
            } while (!this.filter.test(poll));
            return poll;
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }
    }

    public ObservableFilter(ObservableSource observableSource, Predicate predicate2) {
        super(observableSource);
        this.predicate = predicate2;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new FilterObserver(observer, this.predicate));
    }
}
