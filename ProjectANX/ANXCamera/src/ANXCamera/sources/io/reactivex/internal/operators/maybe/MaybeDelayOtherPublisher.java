package io.reactivex.internal.operators.maybe;

import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class MaybeDelayOtherPublisher extends AbstractMaybeWithUpstream {
    final Publisher other;

    final class DelayMaybeObserver implements MaybeObserver, Disposable {
        Disposable d;
        final OtherSubscriber other;
        final Publisher otherSource;

        DelayMaybeObserver(MaybeObserver maybeObserver, Publisher publisher) {
            this.other = new OtherSubscriber(maybeObserver);
            this.otherSource = publisher;
        }

        public void dispose() {
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
            SubscriptionHelper.cancel(this.other);
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) this.other.get());
        }

        public void onComplete() {
            this.d = DisposableHelper.DISPOSED;
            subscribeNext();
        }

        public void onError(Throwable th) {
            this.d = DisposableHelper.DISPOSED;
            this.other.error = th;
            subscribeNext();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.other.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.d = DisposableHelper.DISPOSED;
            this.other.value = obj;
            subscribeNext();
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            this.otherSource.subscribe(this.other);
        }
    }

    final class OtherSubscriber extends AtomicReference implements FlowableSubscriber {
        private static final long serialVersionUID = -1215060610805418006L;
        final MaybeObserver actual;
        Throwable error;
        Object value;

        OtherSubscriber(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void onComplete() {
            Throwable th = this.error;
            if (th != null) {
                this.actual.onError(th);
                return;
            }
            Object obj = this.value;
            MaybeObserver maybeObserver = this.actual;
            if (obj != null) {
                maybeObserver.onSuccess(obj);
            } else {
                maybeObserver.onComplete();
            }
        }

        public void onError(Throwable th) {
            Throwable th2 = this.error;
            MaybeObserver maybeObserver = this.actual;
            if (th2 == null) {
                maybeObserver.onError(th);
                return;
            }
            maybeObserver.onError(new CompositeException(th2, th));
        }

        public void onNext(Object obj) {
            Subscription subscription = (Subscription) get();
            SubscriptionHelper subscriptionHelper = SubscriptionHelper.CANCELLED;
            if (subscription != subscriptionHelper) {
                lazySet(subscriptionHelper);
                subscription.cancel();
                onComplete();
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public MaybeDelayOtherPublisher(MaybeSource maybeSource, Publisher publisher) {
        super(maybeSource);
        this.other = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new DelayMaybeObserver(maybeObserver, this.other));
    }
}
