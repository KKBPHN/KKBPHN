package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface FlowableOnSubscribe {
    void subscribe(@NonNull FlowableEmitter flowableEmitter);
}
