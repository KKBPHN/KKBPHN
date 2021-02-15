package io.reactivex;

import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;

@Experimental
public interface ObservableConverter {
    @NonNull
    Object apply(@NonNull Observable observable);
}
