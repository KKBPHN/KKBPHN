package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface Emitter {
    void onComplete();

    void onError(@NonNull Throwable th);

    void onNext(@NonNull Object obj);
}
