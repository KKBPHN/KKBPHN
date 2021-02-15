package io.reactivex;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public interface SingleObserver {
    void onError(@NonNull Throwable th);

    void onSubscribe(@NonNull Disposable disposable);

    void onSuccess(@NonNull Object obj);
}
