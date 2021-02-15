package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class CompletableMerge extends Completable {
    final boolean delayErrors;
    final int maxConcurrency;
    final Publisher source;

    final class CompletableMergeSubscriber extends AtomicInteger implements FlowableSubscriber, Disposable {
        private static final long serialVersionUID = -2108443387387077490L;
        final CompletableObserver actual;
        final boolean delayErrors;
        final AtomicThrowable error = new AtomicThrowable();
        final int maxConcurrency;
        Subscription s;
        final CompositeDisposable set = new CompositeDisposable();

        final class MergeInnerObserver extends AtomicReference implements CompletableObserver, Disposable {
            private static final long serialVersionUID = 251330541679988317L;

            MergeInnerObserver() {
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }

            public boolean isDisposed() {
                return DisposableHelper.isDisposed((Disposable) get());
            }

            public void onComplete() {
                CompletableMergeSubscriber.this.innerComplete(this);
            }

            public void onError(Throwable th) {
                CompletableMergeSubscriber.this.innerError(this, th);
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this, disposable);
            }
        }

        CompletableMergeSubscriber(CompletableObserver completableObserver, int i, boolean z) {
            this.actual = completableObserver;
            this.maxConcurrency = i;
            this.delayErrors = z;
            lazySet(1);
        }

        public void dispose() {
            this.s.cancel();
            this.set.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(MergeInnerObserver mergeInnerObserver) {
            this.set.delete(mergeInnerObserver);
            if (decrementAndGet() == 0) {
                Throwable th = (Throwable) this.error.get();
                CompletableObserver completableObserver = this.actual;
                if (th != null) {
                    completableObserver.onError(th);
                } else {
                    completableObserver.onComplete();
                }
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.s.request(1);
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(MergeInnerObserver mergeInnerObserver, Throwable th) {
            this.set.delete(mergeInnerObserver);
            if (!this.delayErrors) {
                this.s.cancel();
                this.set.dispose();
                if (this.error.addThrowable(th)) {
                    if (getAndSet(0) <= 0) {
                        return;
                    }
                }
                RxJavaPlugins.onError(th);
                return;
            }
            if (this.error.addThrowable(th)) {
                if (decrementAndGet() != 0) {
                    if (this.maxConcurrency != Integer.MAX_VALUE) {
                        this.s.request(1);
                        return;
                    }
                    return;
                }
            }
            RxJavaPlugins.onError(th);
            return;
            this.actual.onError(this.error.terminate());
        }

        public boolean isDisposed() {
            return this.set.isDisposed();
        }

        public void onComplete() {
            if (decrementAndGet() != 0) {
                return;
            }
            if (((Throwable) this.error.get()) != null) {
                this.actual.onError(this.error.terminate());
            } else {
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (!this.delayErrors) {
                this.set.dispose();
                if (this.error.addThrowable(th)) {
                    if (getAndSet(0) <= 0) {
                        return;
                    }
                }
                RxJavaPlugins.onError(th);
                return;
            }
            if (this.error.addThrowable(th)) {
                if (decrementAndGet() != 0) {
                    return;
                }
            }
            RxJavaPlugins.onError(th);
            return;
            this.actual.onError(this.error.terminate());
        }

        public void onNext(CompletableSource completableSource) {
            getAndIncrement();
            MergeInnerObserver mergeInnerObserver = new MergeInnerObserver();
            this.set.add(mergeInnerObserver);
            completableSource.subscribe(mergeInnerObserver);
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

    public CompletableMerge(Publisher publisher, int i, boolean z) {
        this.source = publisher;
        this.maxConcurrency = i;
        this.delayErrors = z;
    }

    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe(new CompletableMergeSubscriber(completableObserver, this.maxConcurrency, this.delayErrors));
    }
}
