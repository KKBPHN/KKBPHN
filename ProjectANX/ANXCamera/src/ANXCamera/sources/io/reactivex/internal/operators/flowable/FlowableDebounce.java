package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDebounce extends AbstractFlowableWithUpstream {
    final Function debounceSelector;

    final class DebounceSubscriber extends AtomicLong implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = 6725975399620862591L;
        final Subscriber actual;
        final Function debounceSelector;
        final AtomicReference debouncer = new AtomicReference();
        boolean done;
        volatile long index;
        Subscription s;

        final class DebounceInnerSubscriber extends DisposableSubscriber {
            boolean done;
            final long index;
            final AtomicBoolean once = new AtomicBoolean();
            final DebounceSubscriber parent;
            final Object value;

            DebounceInnerSubscriber(DebounceSubscriber debounceSubscriber, long j, Object obj) {
                this.parent = debounceSubscriber;
                this.index = j;
                this.value = obj;
            }

            /* access modifiers changed from: 0000 */
            public void emit() {
                if (this.once.compareAndSet(false, true)) {
                    this.parent.emit(this.index, this.value);
                }
            }

            public void onComplete() {
                if (!this.done) {
                    this.done = true;
                    emit();
                }
            }

            public void onError(Throwable th) {
                if (this.done) {
                    RxJavaPlugins.onError(th);
                    return;
                }
                this.done = true;
                this.parent.onError(th);
            }

            public void onNext(Object obj) {
                if (!this.done) {
                    this.done = true;
                    cancel();
                    emit();
                }
            }
        }

        DebounceSubscriber(Subscriber subscriber, Function function) {
            this.actual = subscriber;
            this.debounceSelector = function;
        }

        public void cancel() {
            this.s.cancel();
            DisposableHelper.dispose(this.debouncer);
        }

        /* access modifiers changed from: 0000 */
        public void emit(long j, Object obj) {
            if (j != this.index) {
                return;
            }
            if (get() != 0) {
                this.actual.onNext(obj);
                BackpressureHelper.produced(this, 1);
                return;
            }
            cancel();
            this.actual.onError(new MissingBackpressureException("Could not deliver value due to lack of requests"));
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Disposable disposable = (Disposable) this.debouncer.get();
                if (!DisposableHelper.isDisposed(disposable)) {
                    ((DebounceInnerSubscriber) disposable).emit();
                    DisposableHelper.dispose(this.debouncer);
                    this.actual.onComplete();
                }
            }
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this.debouncer);
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                long j = this.index + 1;
                this.index = j;
                Disposable disposable = (Disposable) this.debouncer.get();
                if (disposable != null) {
                    disposable.dispose();
                }
                try {
                    Object apply = this.debounceSelector.apply(obj);
                    ObjectHelper.requireNonNull(apply, "The publisher supplied is null");
                    Publisher publisher = (Publisher) apply;
                    DebounceInnerSubscriber debounceInnerSubscriber = new DebounceInnerSubscriber(this, j, obj);
                    if (this.debouncer.compareAndSet(disposable, debounceInnerSubscriber)) {
                        publisher.subscribe(debounceInnerSubscriber);
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    cancel();
                    this.actual.onError(th);
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this, j);
            }
        }
    }

    public FlowableDebounce(Flowable flowable, Function function) {
        super(flowable);
        this.debounceSelector = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new DebounceSubscriber(new SerializedSubscriber(subscriber), this.debounceSelector));
    }
}
