package io.reactivex.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class SafeSubscriber implements FlowableSubscriber, Subscription {
    final Subscriber actual;
    boolean done;
    Subscription s;

    public SafeSubscriber(Subscriber subscriber) {
        this.actual = subscriber;
    }

    public void cancel() {
        try {
            this.s.cancel();
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.onError(th);
        }
    }

    public void onComplete() {
        if (!this.done) {
            this.done = true;
            if (this.s == null) {
                onCompleteNoSubscription();
                return;
            }
            try {
                this.actual.onComplete();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void onCompleteNoSubscription() {
        NullPointerException nullPointerException = new NullPointerException("Subscription not set!");
        try {
            this.actual.onSubscribe(EmptySubscription.INSTANCE);
            try {
                this.actual.onError(nullPointerException);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(new CompositeException(nullPointerException, th));
            }
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            RxJavaPlugins.onError(new CompositeException(nullPointerException, th2));
        }
    }

    public void onError(Throwable th) {
        if (this.done) {
            RxJavaPlugins.onError(th);
            return;
        }
        this.done = true;
        if (this.s == null) {
            NullPointerException nullPointerException = new NullPointerException("Subscription not set!");
            try {
                this.actual.onSubscribe(EmptySubscription.INSTANCE);
                try {
                    this.actual.onError(new CompositeException(th, nullPointerException));
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    RxJavaPlugins.onError(new CompositeException(th, nullPointerException, th2));
                }
            } catch (Throwable th3) {
                Exceptions.throwIfFatal(th3);
                RxJavaPlugins.onError(new CompositeException(th, nullPointerException, th3));
            }
        } else {
            if (th == null) {
                th = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
            }
            try {
                this.actual.onError(th);
            } catch (Throwable th4) {
                Exceptions.throwIfFatal(th4);
                RxJavaPlugins.onError(new CompositeException(th, th4));
            }
        }
    }

    public void onNext(Object obj) {
        CompositeException compositeException;
        if (!this.done) {
            if (this.s == null) {
                onNextNoSubscription();
            } else if (obj == null) {
                NullPointerException nullPointerException = new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
                try {
                    this.s.cancel();
                    onError(nullPointerException);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    compositeException = new CompositeException(nullPointerException, th);
                    onError(compositeException);
                }
            } else {
                try {
                    this.actual.onNext(obj);
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    compositeException = new CompositeException(th, th2);
                    onError(compositeException);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void onNextNoSubscription() {
        this.done = true;
        NullPointerException nullPointerException = new NullPointerException("Subscription not set!");
        try {
            this.actual.onSubscribe(EmptySubscription.INSTANCE);
            try {
                this.actual.onError(nullPointerException);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(new CompositeException(nullPointerException, th));
            }
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            RxJavaPlugins.onError(new CompositeException(nullPointerException, th2));
        }
    }

    public void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.validate(this.s, subscription)) {
            this.s = subscription;
            try {
                this.actual.onSubscribe(this);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(new CompositeException(th, th));
            }
        }
    }

    public void request(long j) {
        try {
            this.s.request(j);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.onError(new CompositeException(th, th));
        }
    }
}
