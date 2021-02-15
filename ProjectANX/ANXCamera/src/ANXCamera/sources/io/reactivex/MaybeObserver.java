package io.reactivex;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public interface MaybeObserver {
    void onComplete();

    void onError(@NonNull Throwable th);

    void onSubscribe(@NonNull Disposable disposable);

    void onSuccess(@NonNull Object obj);
}
