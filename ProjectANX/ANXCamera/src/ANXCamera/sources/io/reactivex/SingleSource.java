package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface SingleSource {
    void subscribe(@NonNull SingleObserver singleObserver);
}
