package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableOperator;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableLift extends AbstractObservableWithUpstream {
    final ObservableOperator operator;

    public ObservableLift(ObservableSource observableSource, ObservableOperator observableOperator) {
        super(observableSource);
        this.operator = observableOperator;
    }

    public void subscribeActual(Observer observer) {
        try {
            Observer apply = this.operator.apply(observer);
            StringBuilder sb = new StringBuilder();
            sb.append("Operator ");
            sb.append(this.operator);
            sb.append(" returned a null Observer");
            ObjectHelper.requireNonNull(apply, sb.toString());
            this.source.subscribe(apply);
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.onError(th);
            NullPointerException nullPointerException = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
            nullPointerException.initCause(th);
            throw nullPointerException;
        }
    }
}
