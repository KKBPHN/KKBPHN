package io.reactivex.internal.util;

import io.reactivex.CompletableObserver;
import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public enum EmptyComponent implements FlowableSubscriber, Observer, MaybeObserver, SingleObserver, CompletableObserver, Subscription, Disposable {
    INSTANCE;

    public static Observer asObserver() {
        return INSTANCE;
    }

    public static Subscriber asSubscriber() {
        return INSTANCE;
    }

    public void cancel() {
    }

    public void dispose() {
    }

    public boolean isDisposed() {
        return true;
    }

    public void onComplete() {
    }

    public void onError(Throwable th) {
        RxJavaPlugins.onError(th);
    }

    public void onNext(Object obj) {
    }

    public void onSubscribe(Disposable disposable) {
        disposable.dispose();
    }

    public void onSubscribe(Subscription subscription) {
        subscription.cancel();
    }

    public void onSuccess(Object obj) {
    }

    public void request(long j) {
    }
}
