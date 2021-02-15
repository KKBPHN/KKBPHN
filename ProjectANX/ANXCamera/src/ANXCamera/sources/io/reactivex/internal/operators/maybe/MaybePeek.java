package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class MaybePeek extends AbstractMaybeWithUpstream {
    final Action onAfterTerminate;
    final Action onCompleteCall;
    final Action onDisposeCall;
    final Consumer onErrorCall;
    final Consumer onSubscribeCall;
    final Consumer onSuccessCall;

    final class MaybePeekObserver implements MaybeObserver, Disposable {
        final MaybeObserver actual;
        Disposable d;
        final MaybePeek parent;

        MaybePeekObserver(MaybeObserver maybeObserver, MaybePeek maybePeek) {
            this.actual = maybeObserver;
            this.parent = maybePeek;
        }

        public void dispose() {
            try {
                this.parent.onDisposeCall.run();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        /* access modifiers changed from: 0000 */
        public void onAfterTerminate() {
            try {
                this.parent.onAfterTerminate.run();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        }

        public void onComplete() {
            if (this.d != DisposableHelper.DISPOSED) {
                try {
                    this.parent.onCompleteCall.run();
                    this.d = DisposableHelper.DISPOSED;
                    this.actual.onComplete();
                    onAfterTerminate();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    onErrorInner(th);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.d == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(th);
            } else {
                onErrorInner(th);
            }
        }

        /* access modifiers changed from: 0000 */
        public void onErrorInner(Throwable th) {
            try {
                this.parent.onErrorCall.accept(th);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                th = new CompositeException(th, th2);
            }
            this.d = DisposableHelper.DISPOSED;
            this.actual.onError(th);
            onAfterTerminate();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                try {
                    this.parent.onSubscribeCall.accept(disposable);
                    this.d = disposable;
                    this.actual.onSubscribe(this);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    disposable.dispose();
                    this.d = DisposableHelper.DISPOSED;
                    EmptyDisposable.error(th, this.actual);
                }
            }
        }

        public void onSuccess(Object obj) {
            if (this.d != DisposableHelper.DISPOSED) {
                try {
                    this.parent.onSuccessCall.accept(obj);
                    this.d = DisposableHelper.DISPOSED;
                    this.actual.onSuccess(obj);
                    onAfterTerminate();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    onErrorInner(th);
                }
            }
        }
    }

    public MaybePeek(MaybeSource maybeSource, Consumer consumer, Consumer consumer2, Consumer consumer3, Action action, Action action2, Action action3) {
        super(maybeSource);
        this.onSubscribeCall = consumer;
        this.onSuccessCall = consumer2;
        this.onErrorCall = consumer3;
        this.onCompleteCall = action;
        this.onAfterTerminate = action2;
        this.onDisposeCall = action3;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new MaybePeekObserver(maybeObserver, this));
    }
}
