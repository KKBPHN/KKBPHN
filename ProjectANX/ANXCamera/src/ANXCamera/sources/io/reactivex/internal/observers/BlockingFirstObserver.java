package io.reactivex.internal.observers;

public final class BlockingFirstObserver extends BlockingBaseObserver {
    public void onError(Throwable th) {
        if (this.value == null) {
            this.error = th;
        }
        countDown();
    }

    public void onNext(Object obj) {
        if (this.value == null) {
            this.value = obj;
            this.d.dispose();
            countDown();
        }
    }
}
