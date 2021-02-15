package io.reactivex.internal.operators.flowable;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFlatMapCompletable extends AbstractFlowableWithUpstream {
    final boolean delayErrors;
    final Function mapper;
    final int maxConcurrency;

    final class FlatMapCompletableMainSubscriber extends BasicIntQueueSubscription implements FlowableSubscriber {
        private static final long serialVersionUID = 8443155186132538303L;
        final Subscriber actual;
        volatile boolean cancelled;
        final boolean delayErrors;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function mapper;
        final int maxConcurrency;
        Subscription s;
        final CompositeDisposable set = new CompositeDisposable();

        final class InnerConsumer extends AtomicReference implements CompletableObserver, Disposable {
            private static final long serialVersionUID = 8606673141535671828L;

            InnerConsumer() {
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

        FlatMapCompletableMainSubscriber(Subscriber subscriber, Function function, boolean z, int i) {
            this.actual = subscriber;
            this.mapper = function;
            this.delayErrors = z;
            this.maxConcurrency = i;
            lazySet(1);
        }

        public void cancel() {
            this.cancelled = true;
            this.s.cancel();
            this.set.dispose();
        }

        public void clear() {
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(InnerConsumer innerConsumer) {
            this.set.delete(innerConsumer);
            onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(InnerConsumer innerConsumer, Throwable th) {
            this.set.delete(innerConsumer);
            onError(th);
        }

        public boolean isEmpty() {
            return true;
        }

        public void onComplete() {
            if (decrementAndGet() == 0) {
                Throwable terminate = this.errors.terminate();
                Subscriber subscriber = this.actual;
                if (terminate != null) {
                    subscriber.onError(terminate);
                } else {
                    subscriber.onComplete();
                }
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.s.request(1);
            }
        }

        public void onError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                if (!this.delayErrors) {
                    cancel();
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
                InnerConsumer innerConsumer = new InnerConsumer();
                if (!this.cancelled && this.set.add(innerConsumer)) {
                    completableSource.subscribe(innerConsumer);
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

        @Nullable
        public Object poll() {
            return null;
        }

        public void request(long j) {
        }

        public int requestFusion(int i) {
            return i & 2;
        }
    }

    public FlowableFlatMapCompletable(Flowable flowable, Function function, boolean z, int i) {
        super(flowable);
        this.mapper = function;
        this.delayErrors = z;
        this.maxConcurrency = i;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new FlatMapCompletableMainSubscriber(subscriber, this.mapper, this.delayErrors, this.maxConcurrency));
    }
}
