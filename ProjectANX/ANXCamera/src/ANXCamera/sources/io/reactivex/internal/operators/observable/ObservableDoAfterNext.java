package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.BasicFuseableObserver;

@Experimental
public final class ObservableDoAfterNext extends AbstractObservableWithUpstream {
    final Consumer onAfterNext;

    final class DoAfterObserver extends BasicFuseableObserver {
        final Consumer onAfterNext;

        DoAfterObserver(Observer observer, Consumer consumer) {
            super(observer);
            this.onAfterNext = consumer;
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
            if (this.sourceMode == 0) {
                try {
                    this.onAfterNext.accept(obj);
                } catch (Throwable th) {
                    fail(th);
                }
            }
        }

        @Nullable
        public Object poll() {
            Object poll = this.qs.poll();
            if (poll != null) {
                this.onAfterNext.accept(poll);
            }
            return poll;
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }
    }

    public ObservableDoAfterNext(ObservableSource observableSource, Consumer consumer) {
        super(observableSource);
        this.onAfterNext = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new DoAfterObserver(observer, this.onAfterNext));
    }
}
