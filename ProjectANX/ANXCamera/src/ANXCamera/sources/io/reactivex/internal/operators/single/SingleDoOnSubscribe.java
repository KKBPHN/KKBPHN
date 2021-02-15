package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class SingleDoOnSubscribe extends Single {
    final Consumer onSubscribe;
    final SingleSource source;

    final class DoOnSubscribeSingleObserver implements SingleObserver {
        final SingleObserver actual;
        boolean done;
        final Consumer onSubscribe;

        DoOnSubscribeSingleObserver(SingleObserver singleObserver, Consumer consumer) {
            this.actual = singleObserver;
            this.onSubscribe = consumer;
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }

        public void onSubscribe(Disposable disposable) {
            try {
                this.onSubscribe.accept(disposable);
                this.actual.onSubscribe(disposable);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.done = true;
                disposable.dispose();
                EmptyDisposable.error(th, this.actual);
            }
        }

        public void onSuccess(Object obj) {
            if (!this.done) {
                this.actual.onSuccess(obj);
            }
        }
    }

    public SingleDoOnSubscribe(SingleSource singleSource, Consumer consumer) {
        this.source = singleSource;
        this.onSubscribe = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new DoOnSubscribeSingleObserver(singleObserver, this.onSubscribe));
    }
}
