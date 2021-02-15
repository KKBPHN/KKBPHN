package io.reactivex.internal.subscribers;

import io.reactivex.plugins.RxJavaPlugins;

public final class BlockingFirstSubscriber extends BlockingBaseSubscriber {
    public void onError(Throwable th) {
        if (this.value == null) {
            this.error = th;
        } else {
            RxJavaPlugins.onError(th);
        }
        countDown();
    }

    public void onNext(Object obj) {
        if (this.value == null) {
            this.value = obj;
            this.s.cancel();
            countDown();
        }
    }
}
