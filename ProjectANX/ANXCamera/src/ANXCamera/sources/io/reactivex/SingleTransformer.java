package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface SingleTransformer {
    @NonNull
    SingleSource apply(@NonNull Single single);
}
