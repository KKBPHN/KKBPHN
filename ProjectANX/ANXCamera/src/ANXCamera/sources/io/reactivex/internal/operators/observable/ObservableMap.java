package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BasicFuseableObserver;

public final class ObservableMap extends AbstractObservableWithUpstream {
    final Function function;

    final class MapObserver extends BasicFuseableObserver {
        final Function mapper;

        MapObserver(Observer observer, Function function) {
            super(observer);
            this.mapper = function;
        }

        public void onNext(Object obj) {
            Object apply;
            Observer observer;
            if (!this.done) {
                if (this.sourceMode != 0) {
                    observer = this.actual;
                    apply = null;
                } else {
                    try {
                        apply = this.mapper.apply(obj);
                        ObjectHelper.requireNonNull(apply, "The mapper function returned a null value.");
                        observer = this.actual;
                    } catch (Throwable th) {
                        fail(th);
                        return;
                    }
                }
                observer.onNext(apply);
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

    public ObservableMap(ObservableSource observableSource, Function function2) {
        super(observableSource);
        this.function = function2;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new MapObserver(observer, this.function));
    }
}
