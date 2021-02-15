package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface MaybeOnSubscribe {
    void subscribe(@NonNull MaybeEmitter maybeEmitter);
}
