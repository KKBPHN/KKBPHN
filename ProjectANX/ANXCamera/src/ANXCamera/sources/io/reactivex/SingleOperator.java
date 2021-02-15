package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface SingleOperator {
    @NonNull
    SingleObserver apply(@NonNull SingleObserver singleObserver);
}
