package io.reactivex.internal.subscribers;

public final class BlockingLastSubscriber extends BlockingBaseSubscriber {
    public void onError(Throwable th) {
        this.value = null;
        this.error = th;
        countDown();
    }

    public void onNext(Object obj) {
        this.value = obj;
    }
}
