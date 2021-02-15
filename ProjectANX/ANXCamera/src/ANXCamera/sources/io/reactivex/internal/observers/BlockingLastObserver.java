package io.reactivex.internal.observers;

public final class BlockingLastObserver extends BlockingBaseObserver {
    public void onError(Throwable th) {
        this.value = null;
        this.error = th;
        countDown();
    }

    public void onNext(Object obj) {
        this.value = obj;
    }
}
