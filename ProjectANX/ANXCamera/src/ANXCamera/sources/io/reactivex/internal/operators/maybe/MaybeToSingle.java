package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import java.util.NoSuchElementException;

public final class MaybeToSingle extends Single implements HasUpstreamMaybeSource {
    final Object defaultValue;
    final MaybeSource source;

    final class ToSingleMaybeSubscriber implements MaybeObserver, Disposable {
        final SingleObserver actual;
        Disposable d;
        final Object defaultValue;

        ToSingleMaybeSubscriber(SingleObserver singleObserver, Object obj) {
            this.actual = singleObserver;
            this.defaultValue = obj;
        }

        public void dispose() {
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            this.d = DisposableHelper.DISPOSED;
            Object obj = this.defaultValue;
            SingleObserver singleObserver = this.actual;
            if (obj != null) {
                singleObserver.onSuccess(obj);
            } else {
                singleObserver.onError(new NoSuchElementException("The MaybeSource is empty"));
            }
        }

        public void onError(Throwable th) {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(obj);
        }
    }

    public MaybeToSingle(MaybeSource maybeSource, Object obj) {
        this.source = maybeSource;
        this.defaultValue = obj;
    }

    public MaybeSource source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new ToSingleMaybeSubscriber(singleObserver, this.defaultValue));
    }
}
