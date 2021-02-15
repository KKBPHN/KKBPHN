package io.reactivex.internal.operators.flowable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class FlowableFlatMapCompletableCompletable extends Completable implements FuseToFlowable {
    final boolean delayErrors;
    final Function mapper;
    final int maxConcurrency;
    final Flowable source;

    final class FlatMapCompletableMainSubscriber extends AtomicInteger implements FlowableSubscriber, Disposable {
        private static final long serialVersionUID = 8443155186132538303L;
        final CompletableObserver actual;
        final boolean delayErrors;
        volatile boolean disposed;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function mapper;
        final int maxConcurrency;
        Subscription s;
        final CompositeDisposable set = new CompositeDisposable();

        final class InnerObserver extends AtomicReference implements CompletableObserver, Disposable {
            private static final long serialVersionUID = 8606673141535671828L;

            InnerObserver() {
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }

            public boolean isDisposed() {
                return DisposableHelper.isDisposed((Disposable) get());
            }

            public void onComplete() {
                FlatMapCompletableMainSubscriber.this.innerComplete(this);
            }

            public void onError(Throwable th) {
                FlatMapCompletableMainSubscriber.this.innerError(this, th);
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this, disposable);
            }
        }

        FlatMapCompletableMainSubscriber(CompletableObserver completableObserver, Function function, boolean z, int i) {
            this.actual = completableObserver;
            this.mapper = function;
            this.delayErrors = z;
            this.maxConcurrency = i;
            lazySet(1);
        }

        public void dispose() {
            this.disposed = true;
            this.s.cancel();
            this.set.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(InnerObserver innerObserver) {
            this.set.delete(innerObserver);
            onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(InnerObserver innerObserver, Throwable th) {
            this.set.delete(innerObserver);
            onError(th);
        }

        public boolean isDisposed() {
            return this.set.isDisposed();
        }

        public void onComplete() {
            if (decrementAndGet() == 0) {
                Throwable terminate = this.errors.terminate();
                CompletableObserver completableObserver = this.actual;
                if (terminate != null) {
                    completableObserver.onError(terminate);
                } else {
                    completableObserver.onComplete();
                }
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.s.request(1);
            }
        }

        public void onError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                if (!this.delayErrors) {
                    dispose();
                    if (getAndSet(0) <= 0) {
                        return;
                    }
                } else if (decrementAndGet() != 0) {
                    if (this.maxConcurrency != Integer.MAX_VALUE) {
                        this.s.request(1);
                        return;
                    }
                    return;
                }
                this.actual.onError(this.errors.terminate());
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper returned a null CompletableSource");
                CompletableSource completableSource = (CompletableSource) apply;
                getAndIncrement();
                InnerObserver innerObserver = new InnerObserver();
                if (!this.disposed && this.set.add(innerObserver)) {
                    completableSource.subscribe(innerObserver);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.s.cancel();
                onError(th);
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                int i = this.maxConcurrency;
                subscription.request(i == Integer.MAX_VALUE ? Long.MAX_VALUE : (long) i);
            }
        }
    }

    public FlowableFlatMapCompletableCompletable(Flowable flowable, Function function, boolean z, int i) {
        this.source = flowable;
        this.mapper = function;
        this.delayErrors = z;
        this.maxConcurrency = i;
    }

    public Flowable fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableFlatMapCompletable(this.source, this.mapper, this.delayErrors, this.maxConcurrency));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe((FlowableSubscriber) new FlatMapCompletableMainSubscriber(completableObserver, this.mapper, this.delayErrors, this.maxConcurrency));
    }
}
