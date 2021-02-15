package io.reactivex.internal.operators.single;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.ResumeSingleObserver;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class SingleDelayWithPublisher extends Single {
    final Publisher other;
    final SingleSource source;

    final class OtherSubscriber extends AtomicReference implements FlowableSubscriber, Disposable {
        private static final long serialVersionUID = -8565274649390031272L;
        final SingleObserver actual;
        boolean done;
        Subscription s;
        final SingleSource source;

        OtherSubscriber(SingleObserver singleObserver, SingleSource singleSource) {
            this.actual = singleObserver;
            this.source = singleSource;
        }

        public void dispose() {
            this.s.cancel();
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.source.subscribe(new ResumeSingleObserver(this, this.actual));
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.s.cancel();
            onComplete();
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public SingleDelayWithPublisher(SingleSource singleSource, Publisher publisher) {
        this.source = singleSource;
        this.other = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.other.subscribe(new OtherSubscriber(singleObserver, this.source));
    }
}
