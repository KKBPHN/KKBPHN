package org.reactivestreams;

public interface Subscriber {
    void onComplete();

    void onError(Throwable th);

    void onNext(Object obj);

    void onSubscribe(Subscription subscription);
}
