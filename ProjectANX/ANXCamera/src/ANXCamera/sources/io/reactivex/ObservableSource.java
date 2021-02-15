package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface ObservableSource {
    void subscribe(@NonNull Observer observer);
}
