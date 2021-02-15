package io.reactivex.functions;

import io.reactivex.annotations.NonNull;

public interface Function {
    Object apply(@NonNull Object obj);
}
