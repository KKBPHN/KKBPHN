package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface ObservableTransformer {
    @NonNull
    ObservableSource apply(@NonNull Observable observable);
}
