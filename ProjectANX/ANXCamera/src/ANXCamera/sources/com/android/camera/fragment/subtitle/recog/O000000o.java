package com.android.camera.fragment.subtitle.recog;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements CompletableOnSubscribe {
    private final /* synthetic */ VoiceOnlineRecog O0OOoO0;

    public /* synthetic */ O000000o(VoiceOnlineRecog voiceOnlineRecog) {
        this.O0OOoO0 = voiceOnlineRecog;
    }

    public final void subscribe(CompletableEmitter completableEmitter) {
        this.O0OOoO0.O000000o(completableEmitter);
    }
}
